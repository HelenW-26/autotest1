package newcrm.testcases.admintestcases;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalProperties;
import org.testng.SkipException;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class AdminPaymentTestCases {
	protected AdminAPIPayment adminPaymentAPI;
	protected Object[][] adminData;
	protected Object[][] ccData;
	protected Object[][] lbtData;
	protected Object[][] wdUserData;
	protected Object[][] transferData;
	protected List<String> unionPayBrand = Arrays.asList("VT", "STAR", "MO", "UM");


	@Test
	public void apiAdminDepositAuditPage() {
		JSONObject firstDepResult=adminPaymentAPI.apiDPAuditApproveFirstSuccess("testcrm api","-1");
		adminPaymentAPI.apiDPAuditUpdStatus(firstDepResult.getString("orderNumber"));
		adminPaymentAPI.apiDPAuditPending(firstDepResult.getString("id"));
		adminPaymentAPI.apiDPAuditReject(firstDepResult.getString("id"));
	}

	@Test
	public void apiAdminWithdrawAuditPage(GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator) {
		adminPaymentAPI.apiWDdailyCapAmt();
//		adminPaymentAPI.updateSRCRiskRecord("testcrm auto",  dbenv, dbBrand,dbRegulator);
		//API 出金账号的数据
		JSONObject wdAuditResult = adminPaymentAPI.apiWDAuditSearch("testcrm auto","5");
		String wdAuditRecordID = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("id");
		String recordWDActualAmt = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("actualAmount");
		String recordWDPaymentAmt = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("paymentAmount");
		String recordWDChannel =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("withdrawChannel");
		String recordWDType =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("withdrawType");
		String recordWDCategory =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("category");
		String recordWDRate =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("rate");
		String recordOperateName = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("operateName");
		String finalWDRate = null;
		if(recordWDRate!=null){
			finalWDRate = new BigDecimal(recordWDRate).stripTrailingZeros().toPlainString();

		}

		if (recordWDChannel == null){
			recordWDChannel = adminPaymentAPI.apiGetWDChannelID(wdAuditRecordID);
		}
		if(recordOperateName== null){
			adminPaymentAPI.apiWDAuditCheckClaimStatus(wdAuditRecordID);

		}
		adminPaymentAPI.apiWDAuditClaimRecord(wdAuditRecordID);
		adminPaymentAPI.apiWDAuditUpdateNote(wdAuditRecordID);
		adminPaymentAPI.apiWDAuditPendingRecord(wdAuditRecordID, recordWDActualAmt, recordWDChannel);
		adminPaymentAPI.apiWDAuditApproveRecord(wdAuditRecordID, recordWDActualAmt, recordWDChannel, recordWDType, recordWDPaymentAmt, recordWDCategory, finalWDRate);
		adminPaymentAPI.apiWDAuditFailRecord(wdAuditRecordID, recordWDActualAmt);
	}

	@Test
	public void apiAdminDepWithReportPage() {
		adminPaymentAPI.apiDepWithdReport();
		adminPaymentAPI.apiDepWithdReportStatistic();
	}

	@Test
	public void apiAdminFinancialInfoAuditPage(String brand) {
		//Unionpay
		if(unionPayBrand.contains(brand.toUpperCase()))
		{
			//type (cc 1 or unionpay 4 or both -1)
			JSONObject unionPayResult = adminPaymentAPI.apiFinancialInfoAuditSearch("testcrm","4");
			JSONObject firstResult = unionPayResult.getJSONArray("rows").getJSONObject(0);
			//status approve = 2, reject = 3
			adminPaymentAPI.apiFinancialInfoAuditUnionpay("3",firstResult.getString("id"),firstResult.getString("user_id"));
			adminPaymentAPI.apiFinancialInfoAuditUnionpay("2",firstResult.getString("id"),firstResult.getString("user_id"));
		}

		//Credit Card
		JSONObject ccResult = adminPaymentAPI.apiFinancialInfoAuditSearch("testcrm","1");
		JSONObject firstCCResult = ccResult.getJSONArray("rows").getJSONObject(0);
		adminPaymentAPI.apiFinancialInfoDisableCC(firstCCResult.getString("id"));
		adminPaymentAPI.apiFinancialInfoEnableCC(firstCCResult.getString("id"));
	}

	@Test
	public void apiAdminAccountTransferAuditPage() {
		String fromAcct = (String)transferData[0][3];
		String toAcct = (String)transferData[0][4];

		JSONObject fromAcctInfo = adminPaymentAPI.apiGetTransferFromAcctInfo(fromAcct);
		String fromAcctName = fromAcctInfo.getJSONObject("data").getString("name");
		String fromAcctCurrency = fromAcctInfo.getJSONObject("data").getString("currency");
		String fromAcctBalance = fromAcctInfo.getJSONObject("data").getString("balance");

		if (Double.parseDouble(fromAcctBalance) <= 10){
			adminPaymentAPI.apiCashAdjustmentAddRecord(fromAcct,fromAcctCurrency,"500","1");
			JSONObject cashAdjustResult1 = adminPaymentAPI.apiCashAdjustmentAuditSearch(fromAcct,"1");
			JSONObject toApproveRecord = cashAdjustResult1.getJSONArray("rows").getJSONObject(0);
			adminPaymentAPI.apiCashAdjustmentCheckApproveRecord(toApproveRecord);
			adminPaymentAPI.apiCashAdjustmentAuditApprove(toApproveRecord);
		}

		adminPaymentAPI.apiAddAcctTransferRecord(fromAcct,fromAcctName, fromAcctBalance, fromAcctCurrency, toAcct, "1");
		JSONObject searchResult = adminPaymentAPI.apiAcctTransferAuditSearch("testcrm auto", "1");
		adminPaymentAPI.apiAcctTransferAuditReject(searchResult.getJSONArray("rows").getJSONObject(0).getString("id"),"1");
		adminPaymentAPI.apiAcctTransferAuditApprove(searchResult.getJSONArray("rows").getJSONObject(1).getString("id"),"1");

		adminPaymentAPI.apiAcctTransferBulkReject(searchResult.getJSONArray("rows").getJSONObject(2).getString("id"),searchResult.getJSONArray("rows").getJSONObject(3).getString("id"));
	}

	@Test
	public void apiAdminCCTransactionAuditPage() {
		adminPaymentAPI.apiCCTransactionAuditSearch();
		JSONObject ccInfo = adminPaymentAPI.apiCCTransctionQueryCC((String) ccData[0][2]);
		adminPaymentAPI.apiCCTranscInsertAdjustment(ccInfo, "1");
		adminPaymentAPI.apiCCTranscBulkAdjustment(ccInfo);
	}

	@Test
	public void apiAdminCCArchiveAuditPage() {
		JSONObject ccArchiveList = adminPaymentAPI.apiCCArchiveAuditSearch("testcrm");
		JSONObject firstResult = ccArchiveList.getJSONArray("rows").getJSONObject(0);
		//status approve = 2, reject = 3, pending = 1
		adminPaymentAPI.apiCCArchiveAudit(firstResult.getString("id"),firstResult.getString("userId"),"1");
		adminPaymentAPI.apiCCArchiveAudit(firstResult.getString("id"),firstResult.getString("userId"),"3");
		adminPaymentAPI.apiCCArchiveAudit(firstResult.getString("id"),firstResult.getString("userId"),"2");
	}

	@Test
	public void apiAdminCashAdjustmentAuditPage() {
		String accountNum = (String) ccData[0][3];

		//Insert single adjustment record
		JSONObject accountDetails = adminPaymentAPI.apiCashAdjustmentCheckAcct(accountNum);
		adminPaymentAPI.apiCashAdjustmentCheckAmount(accountNum,"1");
		adminPaymentAPI.apiCashAdjustmentCheckRecord(accountDetails.getString("code"), "1");
		adminPaymentAPI.apiCashAdjustmentAddRecord(accountNum,accountDetails.getString("message"),"1","1");

		//Approve adjustment record
		JSONObject cashAdjustResult1 = adminPaymentAPI.apiCashAdjustmentAuditSearch(accountNum,"1");
		JSONObject toApproveRecord = cashAdjustResult1.getJSONArray("rows").getJSONObject(0);
		adminPaymentAPI.apiCashAdjustmentCheckApproveRecord(toApproveRecord);
		adminPaymentAPI.apiCashAdjustmentAuditApprove(toApproveRecord);

		//Insert bulk adjustment record
		String adjustmentKey = adminPaymentAPI.apiCashAdjustmentUploadFile();
		adminPaymentAPI.apiCashAdjustmentCheckBulkRecord(adjustmentKey);
		adminPaymentAPI.apiCashAdjustmentInsertBulkRecord(adjustmentKey);

		//Reject adjustment record
		JSONObject cashAdjustResult2 = adminPaymentAPI.apiCashAdjustmentAuditSearch(accountNum,"1");
		JSONObject toRejectRecord = cashAdjustResult2.getJSONArray("rows").getJSONObject(0);
		adminPaymentAPI.apiCashAdjustmentAuditReject(toRejectRecord);
	}

	@Test
	public void apiAdminLBTTransactionAuditPage() {
		String lbtUserID = (String) lbtData[0][0];
		adminPaymentAPI.apiLBTTransactionAuditInsertAdjustment(lbtUserID,"USD","1");
		adminPaymentAPI.apiLBTTransactionAuditCheckBalance(lbtUserID);
		adminPaymentAPI.apiLBTTransactionAuditSearch(lbtUserID);
	}

	@Test
	public void apiAdminUnionPayWithdrawalPage() {
		String unionPayUserEmail = (String) wdUserData[0][5];
		JSONObject searchResult = adminPaymentAPI.apiUnionpayWithdrawalSearch(unionPayUserEmail);
		String recordID = searchResult.getJSONArray("rows").getJSONObject(0).getString("id");

		adminPaymentAPI.apiUnionpayWithdrawalApprove(recordID,"1","2","-1");
		adminPaymentAPI.apiUnionpayWithdrawalApprove(recordID,"2","2","1");
		adminPaymentAPI.apiUnionpayWithdrawalReject(recordID,"2","4");
	}

	@Test
	public void apiAdminPaymentCacheMgmtPage() {
		adminPaymentAPI.apiCacheMgmtWDTypeRefresh();
		adminPaymentAPI.apiCacheMgmtWDType();
		adminPaymentAPI.apiCacheMgmtPortalWDTypeRefresh();
		adminPaymentAPI.apiCacheMgmtPortalWDType();
		adminPaymentAPI.apiCacheMgmtDepTypeRefresh();
		adminPaymentAPI.apiCacheMgmtDepType();
		adminPaymentAPI.apiCacheMgmtWDChnlRefresh();
		adminPaymentAPI.apiCacheMgmtWDChannel();
		adminPaymentAPI.apiCacheMgmtDepChnlRefresh();
	}

	@Test
	public void apiAdminAutoAuditSwitchPage(String brand) {
		adminPaymentAPI.apiAutoTransferOff();
		adminPaymentAPI.apiAutoTransferOn();
		adminPaymentAPI.apiAllDepositSwitchOff();
		adminPaymentAPI.apiAllDepositSwitchOn();
		//um dont have both , vjp/mo dont have quickpayment
		if(!brand.equalsIgnoreCase("UM"))
		{
			if(brand.equalsIgnoreCase("VT") || brand.equalsIgnoreCase("PUG"))
			{
				adminPaymentAPI.apiQuickPaymentAutoDepositOff();
				adminPaymentAPI.apiQuickPaymentAutoDepositOn();
			}
			adminPaymentAPI.apiUnionpayAutoDepositOff();
			adminPaymentAPI.apiUnionpayAutoDepositOn();
		}
	}

	@Test
	public void apiAdminExchangeRatePage() {
		adminPaymentAPI.apiGetExchangeCurrencyPair();
		JSONObject rateLimit = adminPaymentAPI.apiGetExchangeRateLimit("5");

		double minRateLimit = Double.parseDouble(rateLimit.getJSONObject("data").getString("min_alertrange"));
		double maxRateLimit = Double.parseDouble(rateLimit.getJSONObject("data").getString("max_alertrange"));
		String newRateValue = String.format("%.4f",(minRateLimit + Math.random() * (maxRateLimit - minRateLimit)));

		adminPaymentAPI.apiUpdateDepositExchangeRate(newRateValue);
		adminPaymentAPI.apiUpdateWithdrawalExchangeRate(newRateValue);
		adminPaymentAPI.apiGetExchangeRate();
	}

	@Test
	public void apiAdminDavinciPage() {
		String davincToken = adminPaymentAPI.apiDavinciToken();
		adminPaymentAPI.apiDavinciPaymentWDStatus(davincToken);
		adminPaymentAPI.apiDavinciPaymentWDMethod(davincToken);
		adminPaymentAPI.apiDavinciPaymentDPStatus(davincToken);
		adminPaymentAPI.apiDavinciPaymentDPMethod(davincToken);
		adminPaymentAPI.apiDavinciPaymentAcctType(davincToken);
		adminPaymentAPI.apiDavinciPaymentCurrency(davincToken);
		adminPaymentAPI.apiDavinciPaymentCountries(davincToken);
		adminPaymentAPI.apiDavinciDepWithdReport(davincToken);
		adminPaymentAPI.apiDavinciDepWithdReportSummary(davincToken);
		adminPaymentAPI.apiDavinciDepWithDailyReport(davincToken);
		adminPaymentAPI.apiDavinciFTDReport(davincToken);
	}

}
