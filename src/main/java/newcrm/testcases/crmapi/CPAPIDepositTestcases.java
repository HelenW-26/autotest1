package newcrm.testcases.crmapi;

import static org.testng.Assert.assertTrue;

import com.alibaba.fastjson.JSONArray;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIDeposit;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import org.testng.SkipException;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import newcrm.cpapi.CPAPIBase;
import newcrm.cpapi.CPAPIDeposit;
import newcrm.utils.testCaseDescUtils;
import utils.CustomAssert;
import utils.LogUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import static newcrm.cpapi.CustomPaymentPayload.submitDPPayloadBuilder.*;


public class CPAPIDepositTestcases{
	private String[] cpsCurrency = {"USD"};
	protected String initbrand;
	protected String initserver;
	protected Object data[][];
	protected PCSAPIDeposit pcsapi;

	protected AdminAPIPayment adminPaymentAPI;

	protected AdminAPIPayment adminPaymentAPIAudit;



	//PCS Deposit
	@Test
	public void apiDepositNew(String cpsCode) throws Exception{
		//PCS brand,url,host as variable for easier read
		String pcsBrand = (String) data[0][0];

		Integer depAmt = pcsapi.getRandomDepositAmount(cpsCode);

		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		JSONObject initDepResult = pcsapi.apiDPPCSInitDeposit(accInfo,pcsBrand,depAmt);

		//Get pcs host, Authroization and token
		String pcsURLToken = initDepResult.getString("data");
		String str1=pcsURLToken.substring(0,pcsURLToken.indexOf("="));
		String str2=pcsURLToken.substring(str1.length()+1,pcsURLToken.length());
		String authorization=str2.substring(0,str2.indexOf("&"));

		URL url = new URL(pcsURLToken);
		String pcsUrl = url.getProtocol() + "://" + url.getHost();
		String pcsHost = url.getHost();

		Map<String, String> channelDetails = pcsapi.apiPCSChannelDetails(cpsCode, pcsUrl, pcsHost, pcsURLToken, authorization);
		if(channelDetails== null){
            throw new SkipException(String.format("No channel details found for cpsCode:%s)", cpsCode));
		}

		pcsapi.apiPCSCheckout(accInfo, channelDetails, pcsUrl, pcsHost, pcsURLToken, authorization);
		pcsapi.apiPCSSubmitDeposit(channelDetails, pcsUrl, pcsHost, pcsURLToken, authorization);
		pcsapi.printUserFundsInfo(initbrand, accInfo.getString("account"), accInfo.getString("currency"), "Deposit", channelDetails.get("channelName"), cpsCode);
	}

	/**
	 * 审核入金成功
	 * @param cpsCode
	 * @param typeQuery 入金渠道类型
	 * @throws Exception
	 */
	public void apiSuccessDeposit(String cpsCode,String typeQuery) throws Exception{
		String pcsBrand = (String) data[0][0];

		Integer depAmt = pcsapi.getRandomDepositAmount(cpsCode);

		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
//		JSONObject accInfo = pcsapi.queryMetaTraderMT5AccountDetails(cpsCurrency);

		LogUtils.info("accInfo: \n" + accInfo.toJSONString());
		JSONObject initDepResult = pcsapi.apiDPPCSInitDeposit(accInfo,pcsBrand,depAmt);

		//Get pcs host, Authroization and token
		String pcsURLToken = initDepResult.getString("data");
		String str1=pcsURLToken.substring(0,pcsURLToken.indexOf("="));
		String str2=pcsURLToken.substring(str1.length()+1,pcsURLToken.length());
		String authorization=str2.substring(0,str2.indexOf("&"));

		URL url = new URL(pcsURLToken);
		String pcsUrl = url.getProtocol() + "://" + url.getHost();
		String pcsHost = url.getHost();

		Map<String, String> channelDetails = pcsapi.apiPCSChannelDetails(cpsCode, pcsUrl, pcsHost, pcsURLToken, authorization);
		pcsapi.apiPCSCheckout(accInfo, channelDetails, pcsUrl, pcsHost, pcsURLToken, authorization);
		pcsapi.apiPCSSubmitDeposit(channelDetails, pcsUrl, pcsHost, pcsURLToken, authorization);

		String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		//获取初始状态的邮件发送记录
		String userId = accInfo.getString("userId");
		String eStartDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 00:00:00";
		String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 23:59:59";
		String callTemplate = "";
		String subject="Your deposit was successful";
		JSONObject emailSendRecordBeforeApprove =adminPaymentAPI.apiSearchEmailSendRecord(userId,eStartDate,endDate,callTemplate, subject);
		int emailSendRecordSize = emailSendRecordBeforeApprove.getInteger("total");
		//查询状态为Submit to Third Party入金记录
//		JSONObject dpAudit = adminPaymentAPI.apiDPAuditSearch(startDate,typeQuery,"testcrm api","2");
		JSONObject dpAudit = adminPaymentAPI.apiDPAuditSearchByUserId(startDate,typeQuery,userId,"2");
		int dpAuditSize =dpAudit.getJSONArray("rows").size();
		if (dpAudit.getJSONArray("rows").size() == 0) {
			LogUtils.info("No deposit record found");
			throw new SkipException("No deposit record found");
		}
		CustomAssert.assertTrue(dpAuditSize>0,"没有查询状态为Submit to Third Party入金记录");
		// 校验入金金额
		LogUtils.info("验证入金金额: \n");

		//更新状态为audit
		JSONObject firstSuccessDepResult = dpAudit.getJSONArray("rows").getJSONObject(0);
		String actualAmount=firstSuccessDepResult.getString("actualAmount");
		String adminCurrency = firstSuccessDepResult.getString("currency");
		//验证入金金额
		CustomAssert.assertEquals(new BigDecimal(actualAmount),new BigDecimal(depAmt),"入金金额不一致");

		//验证账号
		CustomAssert.assertEquals(firstSuccessDepResult.getString("mt4Account"),accInfo.getString("account"),"账号不一致");

		adminPaymentAPI.apiDPAuditUpdStatus(firstSuccessDepResult.getString("orderNumber"),"6");

		//审核入金成功
		adminPaymentAPI.apiDPAuditApprove(firstSuccessDepResult.getString("id"),actualAmount);
		Thread.sleep(10*1000);
		JSONObject accInfoDepositApprove= pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		JSONArray dpHistory =pcsapi.queryTransactionHistoryDeposit();
		LogUtils.info("dpHistory: \n" + dpHistory.toJSONString());
		// 校验入金记录状态
		LogUtils.info("验证入金历史记录状态: \n");
		int statusSuccess = Integer.parseInt(dpHistory.getJSONObject(0).getString("status"));
		//校验币种
		String currency = dpHistory.getJSONObject(0).getString("currency");
		CustomAssert.assertEquals(currency,adminCurrency,"CP and admin Currency not equal");
		// 校验订单状态，成功的状态为5
		CustomAssert.assertEquals(statusSuccess,5,"After Deposit Success status not equal");
		// mt4 入金会比较慢，可能需要十几分钟之久,mt5才做校验
		int accountType = Integer.parseInt(accInfo.getString("accountType"));
		if (accountType==13){
			LogUtils.info("当前账号类型为MT5账号，进行入金成功校验");
			CustomAssert.assertEquals(accInfoDepositApprove.getBigDecimal("balance"),accInfo.getBigDecimal("balance").add(new BigDecimal(actualAmount)),"After Deposit Approve Balance not equal");
		}
//		Thread.sleep(10*1000);
		JSONObject emailSendRecordAfterApprove =adminPaymentAPI.apiSearchEmailSendRecord(userId,eStartDate,endDate,callTemplate, subject);
		int emailSendRecordSizeApporve = emailSendRecordAfterApprove.getInteger("total");
		CustomAssert.assertEquals(emailSendRecordSizeApporve,emailSendRecordSize+1,"Email Send Record Size not equal");
		CustomAssert.assertAll();

	}

	public void apiFailedPendingDeposit(String cpsCode,String typeQuery) throws Exception{
		String pcsBrand = (String) data[0][0];

		Integer depAmt = pcsapi.getRandomDepositAmount(cpsCode);

		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfo: \n" + accInfo.toJSONString());
		JSONObject initDepResult = pcsapi.apiDPPCSInitDeposit(accInfo,pcsBrand,depAmt);

		//Get pcs host, Authroization and token
		String pcsURLToken = initDepResult.getString("data");
		String str1=pcsURLToken.substring(0,pcsURLToken.indexOf("="));
		String str2=pcsURLToken.substring(str1.length()+1,pcsURLToken.length());
		String authorization=str2.substring(0,str2.indexOf("&"));

		URL url = new URL(pcsURLToken);
		String pcsUrl = url.getProtocol() + "://" + url.getHost();
		String pcsHost = url.getHost();

		Map<String, String> channelDetails = pcsapi.apiPCSChannelDetails(cpsCode, pcsUrl, pcsHost, pcsURLToken, authorization);
		pcsapi.apiPCSCheckout(accInfo, channelDetails, pcsUrl, pcsHost, pcsURLToken, authorization);
		pcsapi.apiPCSSubmitDeposit(channelDetails, pcsUrl, pcsHost, pcsURLToken, authorization);

		String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		//获取初始状态的邮件发送记录
		String userId = accInfo.getString("userId");
		String eStartDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 00:00:00";
		String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 23:59:59";
		String callTemplate = "pendingEmail_Deposit";
		JSONObject emailSendRecordBeforeApprove =adminPaymentAPI.apiSearchEmailSendRecord(userId,eStartDate,endDate,callTemplate,null);
		int emailSendRecordSize = emailSendRecordBeforeApprove.getInteger("total");

		JSONObject dpAudit = adminPaymentAPI.apiDPAuditSearch(startDate,typeQuery,"testcrm api","2");
		if (dpAudit.getJSONArray("rows").size() == 0) {
			LogUtils.info("No deposit record found");
			throw new SkipException("No deposit record found");
		}

		JSONObject firstDepResult = dpAudit.getJSONArray("rows").getJSONObject(0);
		LogUtils.info("firstDepResult: \n" + firstDepResult.toJSONString());
		//更新状态为Failed Payment
		adminPaymentAPI.apiDPAuditUpdStatus(firstDepResult.getString("orderNumber"),"3");

		JSONObject accInfoDepositFailed = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		JSONArray dpHistory =pcsapi.queryTransactionHistoryDeposit();
		LogUtils.info("dpHistory: \n" + dpHistory.toJSONString());
		// 校验入金记录状态
		LogUtils.info("验证入金历史记录状态: \n");
		int status = Integer.parseInt(dpHistory.getJSONObject(0).getString("status"));
		CustomAssert.assertEquals(status,3,"After Deposit Failed Payment status not equal");

		LogUtils.info("accInfoDepositFailed: \n" + accInfo.toJSONString());
		// 校验账户余额
		CustomAssert.assertEquals(accInfoDepositFailed.getBigDecimal("balance"),accInfo.getBigDecimal("balance"),"After Deposit Failed Payment Balance not equal");

		//更新状态为audit
		JSONObject dpFailedAudit = adminPaymentAPI.apiDPAuditSearch(startDate,typeQuery,"testcrm api","3");
		JSONObject firstFailedDepResult = dpFailedAudit.getJSONArray("rows").getJSONObject(0);

		adminPaymentAPI.apiDPAuditUpdStatus(firstFailedDepResult.getString("orderNumber"),"6");
		// pending deposit record
		LogUtils.info("pending deposit record: \n" );
		adminPaymentAPI.apiDPAuditPending(firstFailedDepResult.getString("id"));
		JSONObject accInfoDepositPending = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfoDeposit: \n" + accInfo.toJSONString());
		LogUtils.info("验证pending deposit 记录后的账户金额: \n" );

		CustomAssert.assertEquals(accInfoDepositPending.getBigDecimal("balance"),accInfo.getBigDecimal("balance"),"After Deposit Pending Balance not equal");
		LogUtils.info("验证Pending后的历史记录");
		JSONArray dpPendingHistory =pcsapi.queryTransactionHistoryDeposit();
		LogUtils.info("dpHistory: \n" + dpHistory.toJSONString());
		// 校验入金记录状态
		LogUtils.info("验证入金历史记录状态: \n");
		int statusPending = Integer.parseInt(dpPendingHistory.getJSONObject(0).getString("status"));
		// pending的状态为9
		CustomAssert.assertEquals(statusPending,9,"After Deposit Pending status not equal");

		JSONObject emailSendRecordAfterApprove =adminPaymentAPI.apiSearchEmailSendRecord(userId,eStartDate,endDate,callTemplate,null);
		int emailSendRecordSizeApporve = emailSendRecordAfterApprove.getInteger("total");
		CustomAssert.assertEquals(emailSendRecordSizeApporve,emailSendRecordSize+1,"Email Send Record Size not equal");

		CustomAssert.assertAll();
	}

	//non-PCS LBT deposit
	@Test
	public void apiDPNonPCSLbt(String cpsCode) throws Exception{
		pcsapi.apiAntiReuseToken();
		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);

		JSONObject methodresult = pcsapi.sendNonJsonGETrequest("/web-api/cp/api/deposit/cps/availableChannel?country");
		Map<String, String> channelDetails = pcsapi.getChannelDetails(methodresult, cpsCode);

		String body = bankTransDPRequest(accInfo.getString("account"), cpsCode);
		pcsapi.apiSubmitDeposit(body);
		pcsapi.printUserFundsInfo(initbrand, accInfo.getString("account"), accInfo.getString("currency"), "Deposit", channelDetails.get("channelName"), cpsCode);
	}

	//non-PCS ewallet deposit
	@Test
	public void apiDPNonPCSEwallet(String cpsCode) throws Exception{
		pcsapi.apiAntiReuseToken();
		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);

		JSONObject methodresult = pcsapi.sendNonJsonGETrequest("/web-api/cp/api/deposit/cps/availableChannel?country");
		Map<String, String> channelDetails = pcsapi.getChannelDetails(methodresult, cpsCode);

		String body = eWalletDPRequest(accInfo.getString("account"), cpsCode);
		pcsapi.apiSubmitDeposit(body);
		pcsapi.printUserFundsInfo(initbrand, accInfo.getString("account"), accInfo.getString("currency"), "Deposit", channelDetails.get("channelName"), cpsCode);

	}

	//non-PCS unionpay deposit
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_UNIONPAY)
	public void apiDPNonPCSUnionPay() throws Exception{
		data = TestDataProvider.getAlphaAPIDepositUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][4],(String) data[0][2]);

		pcsapi.apiAntiReuseToken();
		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		JSONObject methodresult = pcsapi.sendNonJsonGETrequest("/web-api/cp/api/deposit/cps/availableChannel?country=CN");
		Map<String, String> channelDetails = pcsapi.getChannelDetails(methodresult, "T00600");

		String body = "{\"mt4Account\":" + accInfo.getString("account") + ",\"operateAmount\":1500,\"applicationNotes\":\"\",\"depositAmount\":\"11092.65\",\"cpsCode\":\"T00600\",\"rate\":7.3951,\"orderCurrency\":\"156\",\"actualCurrency\":\"156\",\"mandatory\":\"address,attach_variable,birthday,card_name,city,country,email,first_name,last_name,notify_url,order_amount,order_currency,order_id,payment_method,phone,remarks,return_url,state,timestamp,transaction_type,user_id,zip,tag,personal_id\",\"attachVariables\":\"{\\\"tag\\\":\\\"AA\\\"}\",\"email\":\"test@test.com\",\"bankCode\":\"\",\"redemptionCode\":null,\"raw\":\"{\\\"tag\\\":\\\"AA\\\"}\"}";

		pcsapi.apiSubmitDeposit(body);
		pcsapi.printUserFundsInfo(initbrand, accInfo.getString("account"), accInfo.getString("currency"), "Deposit", channelDetails.get("channelName"), "T00600");

	}



	//PCS LBT, E-wallet, unionpay, crypto
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_SKRILL)
	public void apiSkrillpayDepositNew() throws Exception { apiDepositNew("T00100_057"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedSkrillpayDepositNew() throws Exception { apiFailedPendingDeposit("T00100_057","21"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_STICPAY)
	public void apiSticpayDepositNew() throws Exception {apiDepositNew("T00100_048"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_BinancePay)
	public void apiBinancePayDepositNew() throws Exception {apiDepositNew("T00100_094"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedSticpayDepositNew() throws Exception {apiFailedPendingDeposit("T00100_048","17"); }
	@Test
	public void apiAirTMDepositNew() throws Exception { apiDepositNew( "T00100_070"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_TYGAPAY)
	public void apiTygaPayDepositNew() throws Exception {apiDepositNew( "T00100_085"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiTygaPayPendingOrFailingDepositNew() throws Exception {
		apiFailedPendingDeposit("T00100_085","100");
	}
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_BITWALLET)
	public void apiBitWalletDepositNew() throws Exception {apiDepositNew("T00100_047"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedBitWalletDepositNew() throws Exception {apiFailedPendingDeposit("T00100_047","20"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_NETELLER)
	public void apiNetellerDepositNew() throws Exception { apiDepositNew("T00100_058"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedNetellerDepositNew() throws Exception { apiFailedPendingDeposit("T00100_058","11"); }
	@Test
	public void apiNigeriaBTDepositNew() throws Exception {apiDepositNew("T00600_154"); }
	@Test
	public void apiVietMomoDepositNew() throws Exception { apiDepositNew("T00100_063"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_VIETNAM_BT)
	public void apiVietBTDepositNew() throws Exception { apiDepositNew("T00600_055"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedVietBTDepositNew() throws Exception { apiFailedPendingDeposit("T00600_055","8"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_THAI_BT)
	public void apiThaiBTDepositNew() throws Exception { apiDepositNew("T00600_062"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedThaiBTDepositNew() throws Exception { apiFailedPendingDeposit("T00312_056","4"); }
	@Test
	public void apiThaiQRDepositNew() throws Exception { apiDepositNew("T00312_055"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_PHP_BT)
	public void apiPhpBTDepositNew() throws Exception { apiDepositNew("T00600_053"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedPhpBTDepositNew() throws Exception { apiFailedPendingDeposit("T00600_053","25"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_VOLET)
	public void apiVoletDepositNew() throws Exception { apiDepositNew("T00100_045"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedVoletDepositNew() throws Exception { apiFailedPendingDeposit("T00100_045","61"); }

	@Test
	public void apiMsiaBTEeziepayDepositNew() throws Exception { apiDepositNew("T00600_055"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_MALAYSIA_BT)
	public void apiMsiaBTXpayDepositNew() throws Exception { apiDepositNew("T00600_062"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiMsiaBTXpayPendingOrFailedDepositNew() throws Exception {
//		apiDepositNew("T00600_062");
		apiFailedPendingDeposit("T00600_062","5");
	}

//	@Test()
//	public void apiMsiaBTXpayAuditSuccessDepositNew() throws Exception {
////		apiDepositNew("T00600_062");
//		apiSuccessDeposit("T00600_062","5");
//	}


	@Test
	public void apiJapanEwalletNew() throws Exception { apiDepositNew("T00100_067"); }
	@Test
	public void apiSlashDepositNew() throws Exception { apiDepositNew("T00600_168"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_JAPAN_BT)
	public void apiJapanBTDepositNew() throws Exception { apiDepositNew("T00600_119"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedJapanBTDepositNew() throws Exception { apiFailedPendingDeposit("T00600_119","50"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_JAPAN_EMONEY)
	public void apiJapanEmoneyDepositNew() throws Exception { apiDepositNew(  "T00600_167"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedJapanEmoneyDepositNew() throws Exception { apiFailedPendingDeposit(  "T00600_167","50"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_MALAYSIA)
	public void apiMsiaEwalletNew() throws Exception { apiDepositNew("T00100_046"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedMsiaEwalletNew() throws Exception { apiFailedPendingDeposit("T00100_046","57"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_MALAYSIA_FPX)
	public void apiMsiaFXpayDepositNew() throws Exception { apiDepositNew( "T00600_060"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedMsiaFXpayDepositNew() throws Exception { apiFailedPendingDeposit( "T00600_060","56"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_CC_BANXA)
	public void apiBanxaDepositNew() throws Exception { apiDepositNew( "T00200_009"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedBanxaDepositNew() throws Exception { apiFailedPendingDeposit( "T00200_009","88"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_UNIONPAY)
	public void apiUnionPayDepositNew() throws Exception {
		data = TestDataProvider.getAlphaAPIDepositUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][4],(String) data[0][2]);
		if (!initbrand.equalsIgnoreCase("VT")){
			//chippay
			apiDepositNew("T00600_033");
		}else {
			//chinapay
			apiDepositNew("T00600_123");
		}
	}

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)

	public void apiPendingOrFailedUnionPayDepositNew() throws Exception {
		data = TestDataProvider.getAlphaAPIDepositUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][4],(String) data[0][2]);
		if (!initbrand.equalsIgnoreCase("VT")){
			apiFailedPendingDeposit("T00600_033","13");
			//chippay
		}else {
			//chinapay
			apiFailedPendingDeposit("T00600_123","3");
		}
	}
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_CRYPTO_TRC)
	public void apiCryptoTRCDepositNew() throws Exception { apiDepositNew( "T00400_008"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK)
	public void apiPendingOrFailedCryptoTRCDepositNew() throws Exception { apiFailedPendingDeposit( "T00400_008","15"); }
	//non-PCS Bank Transfer
	@Test
	public void apiMsiaBTEeziepayDeposit() throws Exception { apiDPNonPCSLbt( "T00600_055"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_MALAYSIA_BT)
	public void apiMsiaBTXpayDeposit() throws Exception { apiDPNonPCSLbt( "T00600_062"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_MALAYSIA_FPX)
	public void apiMsiaFXpayDeposit() throws Exception { apiDPNonPCSLbt( "T00600_060"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_JAPAN_BT)
	public void apiJapanBTDeposit() throws Exception { apiDPNonPCSLbt("T00600_119"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_JAPAN_EMONEY)
	public void apiJapanEmoneyDeposit() throws Exception { apiDPNonPCSLbt(  "T00600_167"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_VIETNAM_BT)
	public void apiVietamBTDeposit() throws Exception { apiDPNonPCSLbt( "T00600"); } //Not usable, this cpscode alot duplicate
	@Test
	public void apiBrazilBTPagsmileDeposit() throws Exception { apiDPNonPCSLbt( "T00600_054"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_CC_BANXA)
	public void apiBanxaDeposit() throws Exception { apiDPNonPCSLbt( "T00200_009"); }

	//non-PCS E-wallet
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_SKRILL)
	public void apiSkrillpayDeposit() throws Exception { apiDPNonPCSEwallet("T00100_057"); }
	@Test
	public void apiSlashDeposit() throws Exception { apiDPNonPCSEwallet("T00600_168"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_STICPAY)
	public void apiSticpayDeposit() throws Exception { apiDPNonPCSEwallet("T00100_048"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_VOLET)
	public void apiVoletDeposit() throws Exception { apiDPNonPCSEwallet("T00100_045"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_NETELLER)
	public void apiNetellerDeposit() throws Exception { apiDPNonPCSEwallet("T00100_058"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_EWALLET_MALAYSIA)
	public void apiMsiaEwallet() throws Exception { apiDPNonPCSEwallet("T00100_046"); }
	@Test
	public void apiJapanEwallet() throws Exception { apiDPNonPCSEwallet("T00100_067"); }

	public void apiDPOthersInfo() throws Exception {
		pcsapi.apiDPTransHist();
	}

	@Test
	public void apiCrashAdjustmentDeposit() throws Exception {
		/**
		 * Crash Adjustment
		 */

		data = UATTestDataProvider.getUATAPIAdminDepositUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][1],(String) data[0][2]);

		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfo: " + accInfo.toJSONString());
		String account= accInfo.getString("account");
		String currency = accInfo.getString("currency");
		int accountType = accInfo.getIntValue("accountType");
		double balance = Double.parseDouble(accInfo.getString("balance"));

		adminPaymentAPI.apiCashAdjustmentAddRecord(account,currency,"50","1");
		JSONObject cashAdjustResult1 = adminPaymentAPI.apiCashAdjustmentAuditSearch(account,"1");
		JSONObject toApproveRecord = cashAdjustResult1.getJSONArray("rows").getJSONObject(0);
		adminPaymentAPI.apiCashAdjustmentCheckApproveRecord(toApproveRecord);
		adminPaymentAPI.apiCashAdjustmentAuditApprove(toApproveRecord);
		// 轮询检查余额是否增加，最多等待15秒
		boolean balanceUpdated = false;
		long startTime = System.currentTimeMillis();
		double balanceAfterAdjust = balance;

		while (System.currentTimeMillis() - startTime < 180000 && !balanceUpdated) {
			JSONObject accInfoAfterAdjust = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
			balanceAfterAdjust = Double.parseDouble(accInfoAfterAdjust.getString("balance"));

			if (balanceAfterAdjust == balance + 50) {
				balanceUpdated = true;
			} else {
				Thread.sleep(1000); // 每秒检查一次
			}
		}		JSONObject accInfoAfterAdjust = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfoAfterAdjust: " + accInfoAfterAdjust.toJSONString());
//		double balanceAfterAdjust = Double.parseDouble(accInfoAfterAdjust.getString("balance"));
		if(accountType!=1){
			CustomAssert.assertEquals(balanceAfterAdjust,balance+50, "Balance after adjustment is not correct");

		}else {
			CustomAssert.assertEquals(balanceAfterAdjust,balance+50, "MT4 Account Balance after adjustment is not correct,Please manual check after 10 min");

		}
		CustomAssert.assertAll();

	}

	@Test
	public  void apiCreditAdjustmentDeposit() throws Exception {
		/**
		 * Crash Adjustment
		 */

		data = TestDataProvider.getAlphaCreditInUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][1],(String) data[0][2]);

		JSONObject accInfo = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfo: " + accInfo.toJSONString());
		String account= accInfo.getString("account");
		String currency = accInfo.getString("currency");
		int credit = accInfo.getInteger("credit");

//		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], GlobalProperties.BRAND.valueOf(String.valueOf(dbBrand)), GlobalProperties.ENV.valueOf(String.valueOf(dbenv)));
		adminPaymentAPI.apiAdminCreditAdjustmentForCP(account, currency, "50", "1");
		// 轮询检查积分是否增加，最多等待3min
		boolean creditUpdated = false;
		long startTime = System.currentTimeMillis();
		int creditAfterAdjust = credit;

		while (System.currentTimeMillis() - startTime < 180000 && !creditUpdated) {
			JSONObject accInfoAfterAdjust = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
			creditAfterAdjust =accInfoAfterAdjust.getInteger("credit");

			if (creditAfterAdjust == credit + 50) {
				creditUpdated = true;
			} else {
				Thread.sleep(1000); // 每秒检查一次
			}
		}
		JSONObject accInfoAfterAdjust = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfoAfterAdjust: " + accInfoAfterAdjust.toJSONString());
//		double creditAfterAdjust = Double.parseDouble(accInfoAfterAdjust.getString("balance"));
		CustomAssert.assertEquals(creditAfterAdjust,credit+50, "credit after adjustment is not correct");
		CustomAssert.assertAll();
		// 取消积分

		adminPaymentAPI.apiAdminCreditAdjustmentForCP(account, currency, String.valueOf(creditAfterAdjust), "2");

		// 轮询检查积分是否减少，最多等待15秒
		boolean creditCanceled = false;
		startTime = System.currentTimeMillis();
		int creditAfterCancel = creditAfterAdjust;

		while (System.currentTimeMillis() - startTime < 15000 && !creditCanceled) {
			JSONObject accInfoAfterCancel = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
			creditAfterCancel = accInfoAfterCancel.getInteger("credit");

			if (creditAfterCancel == credit) {
				creditCanceled = true;
			} else {
				Thread.sleep(1000); // 每秒检查一次
			}
		}

		JSONObject accInfoAfterCancel = pcsapi.queryMetaTraderAccountDetails(cpsCurrency);
		LogUtils.info("accInfoAfterCancel: " + accInfoAfterCancel.toJSONString());
		CustomAssert.assertEquals(creditAfterCancel, 0, "credit after cancel is not correct");
		CustomAssert.assertAll();


	}
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessTygaPayDepositNew() throws Exception {
		apiSuccessDeposit("T00100_085","100");
	}
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessVoletDepositNew() throws Exception { apiSuccessDeposit("T00100_045","61"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessMsiaBTXpayDepositNew() throws Exception { apiSuccessDeposit("T00600_062","5"); }

	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessBrazilBTXpayDepositNew() throws Exception { apiSuccessDeposit("T00600_054","26"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessUnionPayDepositNew() throws Exception {
		data = TestDataProvider.getAlphaAPIDepositUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][4],(String) data[0][2]);
//		initbrand.equalsIgnoreCase("VT")||
		if (initbrand.equalsIgnoreCase("MO")||initbrand.equalsIgnoreCase("STAR")){
			apiSuccessDeposit("T00600_033","13");
		}else {
			apiSuccessDeposit("T00600_123","3");
		}
	}
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessThaiBTDepositNew() throws Exception { apiSuccessDeposit("T00600_055","4"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessCryptoTRCDepositNew() throws Exception { apiSuccessDeposit("T00400_008","15"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessBitWalletDepositNew() throws Exception { apiSuccessDeposit("T00100_045","61"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessJapanBTDepositNew() throws Exception { apiSuccessDeposit("T00600_119","50"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessJapanEmoneyDepositNew() throws Exception { apiSuccessDeposit("T00600_167","50"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessSticpayDepositNew() throws Exception { apiSuccessDeposit("T00100_048","17"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessBanxaDepositNew() throws Exception { apiSuccessDeposit("T00200_009","88"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessPhpBTDepositNew() throws Exception { apiSuccessDeposit("T00600_053","25"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessMsiaFXpayDepositNew() throws Exception { apiSuccessDeposit("T00600_060","56"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessSkrillpayDepositNew() throws Exception { apiSuccessDeposit("T00100_057","21"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessNetellerDepositNew() throws Exception { apiSuccessDeposit("T00100_058","11"); }
	@Test(description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
	public void apiAuditSuccessVietBTDepositNew() throws Exception { apiSuccessDeposit("T00600_055","8"); }

}
