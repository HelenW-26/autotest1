package newcrm.testcases.crmapi;

import com.alibaba.fastjson.JSONArray;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.business.dbbusiness.PaymentDB;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalProperties;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.CustomAttribute;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import newcrm.business.dbbusiness.EmailDB;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.testCaseDescUtils;
import tools.RiskAuditCallBack;
import utils.CustomAssert;
import utils.LogUtils;

import java.math.BigDecimal;
import java.util.*;

import static newcrm.cpapi.CustomPaymentPayload.submitWDPayloadBuilder.*;
import static tools.RiskAuditRequest.sendRiskAuditRequest;
import static tools.RiskAuditRequest.sendSRCRiskAuditRequest;


public class CPAPIWithdrawTestcases {
	protected EmailDB emailDB;
	protected static ENV dbenv = ENV.ALPHA;
	protected static BRAND dbBrand;
	protected static REGULATOR dbRegulator;

	protected String initbrand;
	protected String initserver;
	protected Object data[][];
	protected Object ibData[][];
	protected Object[][] adminData;

	protected PCSAPIWithdraw pcswdapi;
	protected CPAPIWithdraw cpapi;
	protected AdminAPIPayment adminPaymentAPI;

	BigDecimal amount = BigDecimal.valueOf(50);

	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand"})
	public void initiEnv(String brand, ITestContext context) throws Exception {
		GlobalMethods.setEnvValues(brand);
	}

	/**
	 * 等待账户余额达到指定金额或超时退出
	 * @param targetAmount 目标金额
	 * @param maxWaitSeconds 最大等待时间（秒）
	 * @return true表示余额足够，false表示超时
	 */
	private boolean waitForSufficientBalance(BigDecimal targetAmount, int maxWaitSeconds) throws Exception {
		int maxRetries = maxWaitSeconds * 2; // 每0.5秒查询一次
		int retryCount = 0;

		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		BigDecimal currentBalance = accInfo.getBigDecimal("balance");

		// 循环直到余额足够或超过最大重试次数
		while (currentBalance.compareTo(targetAmount) < 0 && retryCount < maxRetries) {
			Thread.sleep(500); // 等待0.5秒
			retryCount++;

			// 重新查询账户余额
			accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
			currentBalance = accInfo.getBigDecimal("balance");

			// 可选：打印调试信息
			if (retryCount % 10 == 0) { // 每5秒打印一次
				GlobalMethods.printDebugInfo("等待余额充足... 当前余额: " + currentBalance + ", 目标金额: " + targetAmount);
			}
		}

		// 返回是否余额足够
		return currentBalance.compareTo(targetAmount) >= 0;
	}

	//Perform cash adjusment if balance < 100
	public void checkBalanceAndCashAdjustment(JSONObject accInfo){
		double acctBalance = Double.parseDouble(accInfo.getString("balance"));
		String account = accInfo.getString("account");
		String currency = accInfo.getString("currency");

		GlobalMethods.printDebugInfo("Acct Balance: "+acctBalance);
		if (acctBalance >= 0 && acctBalance <= 100) {
			adminPaymentAPI.apiAdminCashAdjustmentForCP(account, currency, "500", "1");
		} else if (acctBalance < 0) {
			acctBalance = 1000 - acctBalance;
			adminPaymentAPI.apiAdminCashAdjustmentForCP(account, currency, String.valueOf(acctBalance), "1");
		}
	}

	//Perform credit card balance adjusment if cc balance < wdAmt
	public void checkCCBalanceAndCCAdjustment(JSONObject ccDetails, String adjAmt){
		BigDecimal ccBalance = ccDetails.getBigDecimal("balance");
		GlobalMethods.printDebugInfo("Credit Card Balance: "+ccBalance);

		if (ccBalance.compareTo(amount) < 0){
			GlobalMethods.printDebugInfo("Going to perform credit card adjustment with amount of: "+adjAmt);
            JSONObject ccOrderInfo = adminPaymentAPI.apiCCTransctionQueryCC(ccDetails.getString("userId"));
            adminPaymentAPI.apiCCTranscInsertAdjustment(ccOrderInfo, adjAmt);
			ccBalance = pcswdapi.apiWDCCBalanceInfo(ccDetails.getString("currency"), amount).getBigDecimal("balance");
			Assert.assertTrue(ccBalance.compareTo(amount) >= 0, "CC balance is not enough");
		}
	}

	/* ------------------------------------ Start of PCS CP Withdrawal ------------------------------------*/
	@Test
	private void apiPCSWithdrawalNew(String cpsCode, Integer wdCategory, Integer wdID) throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		checkBalanceAndCashAdjustment(accInfo);

		Map<String, String> channelDetails = pcswdapi.apiWDPCSChannelInfo(accInfo, cpsCode, wdCategory, wdID);
		if(channelDetails.isEmpty()){
		channelDetails = pcswdapi.apiWDPCSChannelInfoV2(accInfo, cpsCode, wdCategory, wdID);
		}
		if(channelDetails.isEmpty()){
			throw new SkipException("No matching cpsMethodCode found for "+ cpsCode +" (Withdraw ID = "+wdID+ "). Kindly check manually through CP.");
		}
		GlobalMethods.printDebugInfo("Channel Details: "+channelDetails);

		Assert.assertNotNull(channelDetails, "This withdraw method is not available, kindly check manually through CP.");
		String wdbody = "";
		switch(wdCategory){
			case 5: //LBT
			case 1: //IBT
				wdbody = bankTranWDRequest(accInfo, channelDetails, amount);
				break;
			case 4: //Crypto
				wdbody = cryptoWDRequest(accInfo, channelDetails, amount);
				break;
			case 3: //E-Wallet
				wdbody = eWalletWDRequest(accInfo, channelDetails, amount);
				break;
		}
		LogUtils.info("apiWDSubmitWithdraw  request \n:body\n"+ wdbody);
		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", channelDetails.get("wdchannelTitle") ,cpsCode);
	}

	//PCS E-Wallet Withdrawal
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_SKRILL)
	public void apiSkrillWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_104",3, 31); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_NETELLER)
	public void apiNetellerWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_106",3, 32); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_AIRTM)
	public void apiAirTMWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_179",3,109); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_VOLET)
	public void apiVoletWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_086",3,69); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_BITWALLET)
	public void apiBitwalletWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_112",3,37); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_TYGAPAY)
	public void apiTygapayWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_217",3,119); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_STICPAY)
	public void apiSticpayWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_111",3,38); }

	//PCS Bank Transfer Withdrawal
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_MALAYSIA_BT)
	public void apiMsiaBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5,6); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_VIETNAM_BT)
	public void apiVietBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5, 8); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_KOREA_BT)
	public void apiKoreaBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5, 35); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_THAI_BT)
	public void apiThaiBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5,5); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_JAPAN_BT)
	public void apiJapanBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5,62); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_PHP_BT)
	public void apiPhilipBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5,40); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_BRAZIL_BT)
	public void apiBrazilBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",5, 64); }

	//PCS Crypto Withdrawal
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_BITCOIN)
	public void apiCryptoBTCWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",4,34); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_ETH)
	public void apiCryptoETHWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",4,80); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_USDC_ERC20)
	public void apiCryptoUSDCWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",4,81); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_USDT_TRC20)
	public void apiCryptoTRCWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",4,86); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_USDT_BEP20)
	public void apiCryptoBEPWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",4,106); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_USDT_ERC20)
	public void apiCryptoERCWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000",4,87); }

	//PCS IBT Withdrawal
	@Test
	public void apiIBTWithdrawalNew() throws Exception { apiPCSWithdrawalNew("F00000_500",1,2); }
	/* ------------------------------------ End of PCS CP Withdrawal ------------------------------------*/



	/* ------------------------------------ Start of PCS IB Withdrawal ------------------------------------*/
	private void apiPCSWithdrawalNewIB(String cpsCode, Integer wdCategory, Integer wdID) throws Exception {
		pcswdapi = new PCSAPIWithdraw((String) ibData[0][3], (String) ibData[0][1], (String) ibData[0][2]);
		JSONObject accInfo = pcswdapi.apiWithdrawalDataIB();
		checkBalanceAndCashAdjustment(accInfo);
		Map<String, String> channelDetails = pcswdapi.apiWDPCSChannelInfo(accInfo, cpsCode, wdCategory, wdID);

		String wdbody = "";
		switch (wdCategory) {
			case 5: //LBT
			case 1: //IBT
				wdbody = bankTranWDRequest(accInfo, channelDetails, amount);
				break;
			case 4: //Crypto
				wdbody = cryptoWDRequest(accInfo, channelDetails, amount);
				break;
			case 3: //E-Wallet
				wdbody = eWalletWDRequest(accInfo, channelDetails, amount);
				break;
		}
		pcswdapi.apiWDSubmitWithdrawIB(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", channelDetails.get("wdchannelTitle"), cpsCode);
	}

	@Test(description = testCaseDescUtils.IBAPIWITHDRAW_CRYPTO_BITCOIN)
	public void apiCryptoBTCWithdrawalNewIB() throws Exception { apiPCSWithdrawalNewIB("F00000",4,34); }
	@Test(description = testCaseDescUtils.IBAPIWITHDRAW_THAI_BT)
	public void apiThaiBTWithdrawalNewIB() throws Exception { apiPCSWithdrawalNewIB("F00000",5,5); }
	@Test(description = testCaseDescUtils.IBAPIWITHDRAW_EWALLET_NETELLER)
	public void apiNetellerWithdrawalNewIB() throws Exception { apiPCSWithdrawalNewIB("F00000_106",3, 32); }
	@Test(description = testCaseDescUtils.IBAPIWITHDRAW_INTER_BANK_TRANS)
	public void apiIBTWithdrawalNewIB() throws Exception { apiPCSWithdrawalNewIB("F00000_500",1,2); }
	@Test(description = testCaseDescUtils.IBAPIWITHDRAW_EWALLET_BITWALLET)
	public void apiBitwalletWithdrawalNewIB() throws Exception { apiPCSWithdrawalNewIB("F00000_112", 3, 37);  }
	/* ------------------------------------ End of PCS IB Withdrawal ------------------------------------*/



	/* ------------------------------------ Start of Non-PCS CP Withdrawal ------------------------------------*/
	//Credit Card Withdrawal
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CC)
	public void apiCCWithdrawal() throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][4],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		LogUtils.info("accInfo: " + accInfo);
		checkBalanceAndCashAdjustment(accInfo);
		JSONObject ccDetails = pcswdapi.apiWDCCBalanceInfo(accInfo.getString("currency"),amount);
		Assert.assertFalse(ccDetails.isEmpty(), "No active / available credit card");
		//Assert.assertTrue(new BigDecimal(ccDetails.get("ccBalance")).compareTo(amount) >= 0, "No CC balance for withdrawal");

		checkCCBalanceAndCCAdjustment(ccDetails, "3000");

		String wdbody = creditCardWDRequest(accInfo, ccDetails, amount);
		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", "Credit Card" ,"");
	}

	//Non-PCS Crypto Withdrawal
	@Test
	public void apiNonPCSCryptoWithdrawal(String cpsCode, Integer wdTypeID) throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		checkBalanceAndCashAdjustment(accInfo);
		Map<String, String> channelDetails = pcswdapi.apiWDChannelInfo(cpsCode, wdTypeID);

		String wdbody = cryptoWDRequest(accInfo, channelDetails, amount);

		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", channelDetails.get("wdchannelTitle") ,cpsCode);
	}

	//Non-PCS LBT Withdrawal
	public void apiNonPCSLBTWithdrawal(String cpsCode, Integer wdTypeID) throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		checkBalanceAndCashAdjustment(accInfo);
		Map<String, String> channelDetails = pcswdapi.apiWDChannelInfo(cpsCode, wdTypeID);

		String wdbody = bankTranWDRequest(accInfo, channelDetails, amount);

		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", channelDetails.get("wdchannelTitle") ,"F00000");
	}

	//Non-PCS E-Wallet Withdrawal
	@Test
	public void apiNonPCSEwalletWithdrawal(String cpsCode, Integer wdTypeID) throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		checkBalanceAndCashAdjustment(accInfo);
		Map<String, String> channelDetails = pcswdapi.apiWDChannelInfo(cpsCode, wdTypeID);

		String wdbody = eWalletWDRequest(accInfo, channelDetails, amount);

		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", channelDetails.get("wdchannelTitle") ,"F00000");
	}

	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_STICPAY)
	public void apiSticpayWithdrawal() throws Exception { apiNonPCSEwalletWithdrawal("F00000_111",38); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_EWALLET_BITWALLET)
	public void apiBitwalletWithdrawal() throws Exception { apiNonPCSEwalletWithdrawal("F00000_112",37); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_BRAZIL_BT)
	public void apiBrazilBTWithdrawal() throws Exception { apiNonPCSLBTWithdrawal("F00000",64); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_USDT_BEP20)
	public void apiCryptoBEPWithdrawal() throws Exception { apiNonPCSCryptoWithdrawal("F00000",106); }
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_CRYPTO_USDT_BEP20)
	public void apiCryptoBTCWithdrawal() throws Exception { apiNonPCSCryptoWithdrawal("F00000",34); }

	//Non-CPS Withdrawal (Unionpay)
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_UNIONPAY)
	public void apiUnionpayWithdrawal() throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][5],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		checkBalanceAndCashAdjustment(accInfo);
		Map<String, String> unionDetails = pcswdapi.getUnionpayInfo();

		String wdbody = "[{\"qAccount\":"+accInfo.getString("account")+",\"amount\":55,\"withdrawalType\":"+unionDetails.get("unionType")+"," +
				"\"bankName\":\""+unionDetails.get("unionBankName")+"\",\"accountNumber\":\""+unionDetails.get("unionCardNum")+"\"," +
				"\"bankBranchName\":\""+unionDetails.get("unionBranch")+"\",\"bankAccountName\":\""+unionDetails.get("unionCardName")+"\"," +
				"\"importantNotes\":\"testnote\",\"followerResultIds\":[]}]";

		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", unionDetails.get("unionName") ,"");
	}

	//Non-CPS Withdrawal (IBT)
	@Test
	public void apiIBTWithdrawal() throws Exception{
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		checkBalanceAndCashAdjustment(accInfo);

		String wdbody = "[{\"qAccount\":"+accInfo.getString("account")+",\"amount\":55,\"withdrawalType\":2,\"country\":2,\"bankName\":\"bankname\",\"bankAddress\":\"bankaddr\",\"accountNumber\":\"testiban\"," +
				"\"beneficiaryName\":\"benename\",\"holderAddress\":\"testaha\",\"sortCode\":\"testaba\",\"swift\":\"testswift\",\"importantNotes\":\"testapiwithdrawal\"," +
				"\"fileList\":[\"https://crm-pu-alpha.s3.ap-southeast-1.amazonaws.com/other/8439d97d060c489facce3a0a866e519e.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250320T084507Z&X-Amz-SignedHeaders=host&X-Amz-Expires=600&X-Amz-Credential=AKIA6LZROUZKAQU5T4EI%2F20250320%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=0f6d06f811398e6269c83994cfd15dfaba24202258922fd292b08c31e54f0ba7\"]," +
				"\"isRememberInfo\":true,\"userPaymentInfoId\":\"\"}]";

		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", "International Bank Transfer" ,"");
	}

	/* ------------------------------------ End of Non-PCS CP Withdrawal ------------------------------------*/


	/* ----------------------------------- Start of Big Amount Withdrawal -----------------------------------*/
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_BIGAMOUNT_RISKAUDIT)
	@Parameters(value= {"TestEnv","Brand","Regulator","TraderURL","TraderName","TraderPass"})
	public void apiBigAmountWithdrawal(String server,String testEnv, String brand, String regulator, String url, String user, String password) throws Exception {
		BigDecimal balance = null, amount = BigDecimal.valueOf(50000.55), finalBalance = null;;
		Integer status = 0;
		brand = brand.toUpperCase();
		server = server.toLowerCase();
		if(testEnv.toLowerCase().matches(".*uat.*")){
			LogUtils.info("UAT环境");
			testEnv = "UAT";
			dbenv = ENV.UAT;
		}else {
			LogUtils.info("Alpha环境");
			testEnv = "ALPHA";
			dbenv = ENV.ALPHA;
		}
		emailDB = new EmailDB(ENV.valueOf(testEnv), BRAND.valueOf(brand), REGULATOR.valueOf(regulator));
		PaymentDB paymentDB = new PaymentDB(ENV.valueOf(testEnv), BRAND.valueOf(brand), REGULATOR.valueOf(regulator));
		//Cancel withdrawal if there's any
		pcswdapi = new PCSAPIWithdraw(url,user,password);

		JSONObject  accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		LogUtils.info("accInfo:\n " + accInfo);
		if(accInfo!= null){
			balance = accInfo.getBigDecimal("balance");

		}else {
			throw new Exception("No suitable MT4/5 account!! All accounts balance less than (USC)20000 / (Non-USC)200");
		}

		boolean isBalanceSufficient = waitForSufficientBalance(amount, 90); // 最多等待30秒

		if (isBalanceSufficient) {
			GlobalMethods.printDebugInfo("余额已充足，继续执行后续操作");
			LogUtils.info("余额充足，继续执行后续操作");
			// 继续执行业务逻辑
		} else {
			GlobalMethods.printDebugInfo("等待超时，余额仍不足");
			LogUtils.info("等待超时，余额不足");
			// 处理超时情况
		}

		//Update withdraw status to accepted if balance is less than withdrawal amount
		Integer withdrawStatus = paymentDB.getWithdrawStatus(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator), accInfo.getString("account"));
		if(balance.compareTo(amount) <= 0 && withdrawStatus != null && withdrawStatus == 21){
			paymentDB.updateWithdrawStatus(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator), accInfo.getString("account"), "5");
		}

		/*Test Start*/
		//Submit big amount withdrawal
//        try {
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
		apiSubmitWDBigAmountWithdrawal("F00000",86, amount, emailDB);

		balance = accInfo.getBigDecimal("balance");

		for (int i = 0; i < 9; i++) {
			accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
			finalBalance = balance.subtract(accInfo.getBigDecimal("balance"));

			if (finalBalance.equals(amount)) {
				break;
			}

			if(i == 4){ //buffer time for sync account balance
				GlobalMethods.printDebugInfo("Retrying on account balance syncing after 5th attempt...");
				Thread.sleep(3000);
			}
		}

		//Verify withdrawal in risk audit status
        try {
            status = paymentDB.getWithdrawStatus(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator),accInfo.getString("account"));
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
        CustomAssert.assertEquals(status, 21, "Incorrect withdrawal status: expected 21, but got " + status + ".");

		//Query  risk audit order_num
		String account = accInfo.getString("account");
		String userId =accInfo.getString("userId");
		JSONObject riskAuditResult =adminPaymentAPI.apiWDAuditAccountSearch(account, "21");
		JSONArray riskAuditOrders= riskAuditResult.getJSONArray("rows");
		String riskAuditOrderNum = riskAuditOrders.getJSONObject(0).getString("orderNumber");
		String transferId = riskAuditOrders.getJSONObject(0).getString("id");
		//查询是否满足SRC出金流程
//		boolean isSrcWithdrawalEnabled =paymentDB.getSRCWithdrawStatus(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator));
		String query = userId != null ? userId : user;
		LogUtils.info(String.format("根据用户userID或邮箱: %s 查询用户信息" , query));

		JSONArray clientInfo =adminPaymentAPI.apiClientSearch(query);
		String countryCode = clientInfo.getJSONObject(0).getString("countryCode");

		boolean isSRCSwitchOn =paymentDB.judgeSRCWithdrawStatus(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator),userId, countryCode);
		if(isSRCSwitchOn){
			//优先走新流程 3.0
			try {
				// 替换原来的代码块为：
				LogUtils.info("flow to src new withdraw process");
				RiskAuditCallBack callBackDataAU = new RiskAuditCallBack(
						testEnv,
						brand,
						regulator,
						userId,
						riskAuditOrderNum
				);
			sendSRCRiskAuditWithRetry(callBackDataAU, brand);

			}catch (Exception e) {
				e.printStackTrace();

			}

		}else{
			//优先走新流程2.0
			LogUtils.info("flow to new big amount 2.0 process");
			RiskAuditCallBack callBackData = new RiskAuditCallBack(
					testEnv,
					server,
					brand,
					regulator,
					transferId,
					riskAuditOrderNum
			);
			try {

				JSONObject riskAuditRes =sendRiskAuditRequest(callBackData,brand);
				if (Objects.equals(riskAuditRes.getString("res"), "true")&& testEnv.equalsIgnoreCase("alpha")) {
					LogUtils.info("Risk audit request sent successfully.");
				}else if ("true".equals(riskAuditRes.getString("data")) && testEnv.equalsIgnoreCase("uat")) {
					LogUtils.info("Risk audit request sent successfully.");
				}
//			else {
//				LogUtils.info("Risk audit request failed.go to old emial approve process");
//				//新流程走不通再走老邮件流程
//				String approveLink = emailDB.getRiskApprovalLink(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator),accInfo.getString("account"),"APPROVE_URL");
//				//Big amount risk audit
//				if(approveLink!= null){
//					LogUtils.info("Risk audit Email link not found!");
//					pcswdapi.apiRiskAuditApproval(approveLink,"APPROVE_URL");
//
//				}
//
//			}
			} catch (Exception e) {
				e.printStackTrace();

			}

		}


		//Verify withdrawal in accepted status
		status = paymentDB.getWithdrawStatus(dbenv, BRAND.valueOf(brand), REGULATOR.valueOf(regulator),accInfo.getString("account"));
		CustomAssert.assertEquals(status, 5, "Incorrect withdrawal status: expected 5, but got " + status + ".");
		CustomAssert.assertAll();
		//Cancel withdrawal - comment in to avoid balance > 5k and being cleared by daily cron
		//pcswdapi = new PCSAPIWithdraw(url,user,password);
	}
	/**
	 * 发送SRC风险审核请求并处理重试逻辑
	 * @param callBackDataAU 风险审核回调数据
	 * @param brand 品牌
	 * @return 风险审核响应结果
	 */
	private JSONObject sendSRCRiskAuditWithRetry(RiskAuditCallBack callBackDataAU, String brand) {
		try {
			JSONObject riskAuditRes = sendSRCRiskAuditRequest(callBackDataAU, brand);
			int retryCount = 0;
			int maxRetryTime = 180; // 3分钟 = 180秒
			int retryInterval = 15; // 15秒重试间隔

			while (Objects.equals(riskAuditRes.getInteger("code"), 500) && retryCount * retryInterval < maxRetryTime) {
				retryCount++;
				LogUtils.info("Risk audit request failed. Waiting " + retryInterval + "s before retry. Attempt: " + retryCount);
				Thread.sleep(retryInterval * 1000);
				riskAuditRes = sendSRCRiskAuditRequest(callBackDataAU, brand);

				if (Objects.equals(riskAuditRes.getInteger("code"), 200)) {
					LogUtils.info("Risk audit request sent successfully after " + retryCount + " retries.");
					break;
				}
			}

			if (!Objects.equals(riskAuditRes.getInteger("code"), 200) && !Objects.equals(riskAuditRes.getInteger("code"), 500)) {
				LogUtils.info("Risk audit request failed with code: " + riskAuditRes.getInteger("code") + "\n" + riskAuditRes);
			} else if (Objects.equals(riskAuditRes.getInteger("code"), 500) && retryCount * retryInterval >= maxRetryTime) {
				LogUtils.info("Risk audit request failed. Maximum retry time (3 minutes) exceeded.");
			} else if (Objects.equals(riskAuditRes.getInteger("code"), 200)) {
				LogUtils.info("Risk audit request sent successfully.");
			}

			return riskAuditRes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject apiSubmitWDBigAmountWithdrawal(String cpsCode, Integer wdID, BigDecimal amount, EmailDB emailDB) throws Exception {
		pcswdapi = new PCSAPIWithdraw((String) data[0][3], (String) data[0][1], (String) data[0][2]);
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		int wdCategory = 4;
		Map<String, String> channelDetails = pcswdapi.apiWDPCSChannelInfo(accInfo, cpsCode, wdCategory, wdID);
		LogUtils.info("PCS channel details:\n " + channelDetails);
		if(channelDetails.isEmpty()){
			//未匹配到渠道信息则跳过
			throw new SkipException(String.format("match channelDetails:%s No channel details found for cpsCode:%s,wdCategory:%s,wdId:%s)",channelDetails, cpsCode,wdCategory,wdID));
		}

		//if account balance sufficient for big amount withdrawal, skip reject email
		BigDecimal accBalance = accInfo.getBigDecimal("balance");
		if(!(accBalance.compareTo(amount) >= 0)) {
			//Reject previous risk withdrawal if there's any
			//Query DB for risk audit reject link
			String rejectLink = emailDB.getRiskApprovalLink(dbenv, BRAND.valueOf(GlobalMethods.getBrand().toUpperCase()), emailDB.getRegulator(), accInfo.getString("account"), "REJECT_URL");
			//Reject
			pcswdapi.apiRiskAuditApproval(rejectLink, "REJECT_URL");
			GlobalMethods.printDebugInfo("Perform REJECT_URL");
		}

		//Get latest balance after reject (Without this, the "BEFORE" balance for assertion will be balance before reject)
		accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		GlobalMethods.printDebugInfo("Account balance before submit big amount withdrawal = " + accInfo.getString("currency") + " " + accInfo.getString("balance"));
		String wdbody = bigAmountWDRequest(accInfo, channelDetails, amount);

		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", channelDetails.get("wdchannelTitle"), cpsCode);
		return accInfo;
	}
	/* ----------------------------------- End of Big Amount Withdrawal -----------------------------------*/



	/* ------------------------------------------- Start of 混合出金 -----------------------------*/
	public void apiCombinedWithdrawal(boolean includeCC, boolean includeLBT, boolean includeOther)  {
		System.out.println("************Start combination withdrawal************");
		List<Integer> categoriesList = new ArrayList<>();

		//Get Account Balance and Currency
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		String acctCurrency = accInfo.getString("currency");
		BigDecimal acctBalance = accInfo.getBigDecimal("balance");
		String acctNum = accInfo.getString("account");
		String userId = accInfo.getString("userId");

		//Get CC / LBT / Other balance and assertion its balance
		BigDecimal ccBalance = BigDecimal.ZERO;
		BigDecimal lbtBalance = BigDecimal.ZERO;
		BigDecimal otherBalance = BigDecimal.ZERO;
		JSONObject ccDetails = new JSONObject();

		if (includeCC) {
			ccDetails = pcswdapi.apiWDCCBalanceInfo(acctCurrency, amount);
			ccBalance = ccDetails.getBigDecimal("balance");
			checkCCBalanceAndCCAdjustment(ccDetails, String.valueOf(amount.subtract(ccBalance)));
		}
		if (includeLBT) {
			categoriesList.add(5);
			lbtBalance = pcswdapi.apiWDLBTData(acctCurrency);
			//if lbtBalance less than amount withdrawal amount (50), then add lbt balance
			if(lbtBalance.compareTo(amount) < 0) {
				GlobalMethods.printDebugInfo("UserID: " + userId + ", currency: " + acctCurrency + ", Initial LBT Balance: " + lbtBalance);
				adminPaymentAPI.apiLBTTransactionAuditInsertAdjustment(userId, acctCurrency, String.valueOf(amount.subtract(lbtBalance)));
				lbtBalance = pcswdapi.apiWDLBTData(acctCurrency); //retrieve new lbtBalance amount
				GlobalMethods.printDebugInfo("UserID: " + userId + ", currency: " + acctCurrency + ", After LBT Adjustment: " + lbtBalance);
			}
			Assert.assertTrue(lbtBalance.compareTo(amount) >= 0, "LBT balance is not enough");
		}
		if (includeOther) {
			categoriesList.addAll(Arrays.asList(3, 4));
			otherBalance = acctBalance.subtract(ccBalance).subtract(lbtBalance);
			Assert.assertTrue(otherBalance.compareTo(amount) >= 0, "Other balance is not enough");
		}
		System.out.println("Acct Balance: " + acctBalance + "\nCC Balance: " + ccBalance + "\nLBT Balance: " + lbtBalance + "\nOther Balance: " + otherBalance);

		//Build request body and submit withdraw
		Map<Integer, Map<String, String>> pcsChannelDetails = pcswdapi.apiWDPCSChannelInfoByCategoryList(acctNum, categoriesList);
		String wdbody = mixedWithdrawalWDRequest(ccBalance, lbtBalance, otherBalance, accInfo, ccDetails, categoriesList, pcsChannelDetails);
		pcswdapi.apiWDSubmitWithdraw(wdbody);
		pcswdapi.cancelSubmittedWithdrawals();

		System.out.println("************End combination withdrawal************");
	}

	public void apiWithdrawalCCLBTOther() { apiCombinedWithdrawal(true, true, true); }
	public void apiWithdrawalCCOther()  { apiCombinedWithdrawal(true, false, true); }
	public void apiWithdrawalLBTOther()  { apiCombinedWithdrawal(false, true, true); }
	public void apiWithdrawalCCLBT()  { apiCombinedWithdrawal(true, true, false); }
	/* ------------------------------------------- End of 混合出金 -----------------------------*/


	/*  Withdrawal Others API  */
	public void apiWDOthersInfo() throws Exception {
		CPAPIWithdraw cpApi = new CPAPIWithdraw((String) data[0][3], (String) data[0][1], (String) data[0][2]);
		JSONObject accInfo = cpApi.apiWithdrawalData();
		String currency = accInfo.getJSONObject("data").getJSONArray("logins").getJSONObject(0).getString("currency");

		cpApi.apiWDExchgRate();
		cpApi.apiWDLBTData(currency);
		cpApi.apiWDBlacklist();
		cpApi.apiWDLimitInfo(accInfo);
		cpApi.apiWDNonCCType(accInfo);
		cpApi.apiWDValidateMamFeeSett(accInfo);
		cpApi.apiWDTransHist();
	}

	/*  PCS-Withdrawal Others API  */
	public void apiWDPCSOthersInfo() throws Exception {
		PCSAPIWithdraw pcswdapi = new PCSAPIWithdraw((String) data[0][3], (String) data[0][1], (String) data[0][2]);
		JSONObject accInfo = pcswdapi.apiWithdrawalData();
		pcswdapi.apiWDPCSEnabled();
		pcswdapi.apiWDPCSCurrLimit(accInfo);
		pcswdapi.apiWDPCSSortInfo(accInfo);
	}

	/* IB payment related API */
	public void apiWDOthersInfoIB() throws Exception {
		CPAPIWithdraw ibApi = new CPAPIWithdraw((String) ibData[0][3], (String) ibData[0][1], (String) ibData[0][2]);
		ibApi.apiWDBlacklistIB();
	}

	public void apiPaymentInfoIB() throws Exception {
		CPAPIWithdraw  ibApi= new CPAPIWithdraw((String) ibData[0][3], (String) ibData[0][1], (String) ibData[0][2]);
		JSONObject accInfo = ibApi.apiWithdrawalDataIB();
		ibApi.apiPaymentInfoIB(accInfo);
		ibApi.apiPaymentListIB(accInfo);
	}

}

