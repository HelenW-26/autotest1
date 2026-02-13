package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.crmapi.CPAPIWithdrawTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.Listeners.MethodTracker;
import utils.LogUtils;

import static org.junit.Assert.assertNotNull;

public class UATAPIWithdrawalTestCases extends CPAPIWithdrawTestcases {
	private AllureTestListener allureTestListener = new AllureTestListener();
	@BeforeTest(alwaysRun = true)
	@Parameters({"Brand", "Server"})
	public void initEnv(String brand, String server, ITestContext context) {
		this.initbrand = GlobalMethods.setEnvValues(brand);
		this.initserver = server;

		data = UATTestDataProvider.getUATAPIWithdrawUsersData(this.initbrand, this.initserver);
		ibData = UATTestDataProvider.getUATAPIWithdrawTransferUsersDataIB(this.initbrand, this.initserver);
		adminData = UATTestDataProvider.getUATRegUsersData(brand, server);
		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(initbrand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
		adminPaymentAPI.apiDisableLoginWithdrawal2FA();
		pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
	}
	@AfterMethod
	public void tearDown(ITestResult result) {
		// 在测试结束后上报错误信息
		allureTestListener.logTestFailure(result);
	}
	@AfterClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void teardown(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
//		Brand = brand;
		data = UATTestDataProvider.getUATRegUsersData(brand, server);
		assertNotNull(data);
		Object[][] adminData = UATTestDataProvider.getUATRegUsersData(brand, server);

		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));;
		//批量更新出金risk audit状态为Accepted
		adminPaymentAPI.setUserIdAndCountryCode(pcswdapi);
		adminPaymentAPI.batchUpdateSRCRiskRecord(GlobalProperties.ENV.valueOf("UAT"), GlobalProperties.BRAND.valueOf(brand.toUpperCase()),GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]));
	}
	@Test(priority = 2,groups={"API_Payment_Withdraw"})
	@Parameters(value= {"Brand"})
	public void testWEBApiWithdrawal(String brand) throws Exception {
		switch(brand.toLowerCase())
		{
			case "um":
				//no ewallet
				//this.apiIBTWithdrawal();

				MethodTracker.trackMethodExecution(this, "apiUnionpayWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiThaiBTWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoTRCWithdrawalNew", true, null);
				break;
			case "mo":
				//this.apiIBTWithdrawal();

				MethodTracker.trackMethodExecution(this, "apiUnionpayWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiVoletWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiKoreaBTWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoERCWithdrawalNew", true, null);
				break;
			case "vfx":
			case "au" :
				//no unionpay
				//this.apiIBTWithdrawal();

				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiTygapayWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiVoletWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiNetellerWithdrawalNew", true, null);
				//非CPS IBT渠道已下线 MethodTracker.trackMethodExecution(this, "apiIBTWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiPhilipBTWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoETHWithdrawalNew", true, null);
				break;
			//non-PCS
			case "star":
				// old ibt
				MethodTracker.trackMethodExecution(this, "apiUnionpayWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiBrazilBTWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiSticpayWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiBitwalletWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBEPWithdrawalNew", true, null);
				break;
			case "pug":
				//no unionpay
				//this.apiIBTWithdrawal();

				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiSkrillWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiNetellerWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiVietBTWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNew", true, null);
				break;
			case "vjp":
				//no unionpay, ibt
				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiBitwalletWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiJapanBTWithdrawalNew", true, null);
				break;
			default: //vt
				//this.apiIBTWithdrawalNew();
				MethodTracker.trackMethodExecution(this, "apiUnionpayWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiCCWithdrawal", true, null);
				MethodTracker.trackMethodExecution(this, "apiSkrillWithdrawalNew", true, null);
				//MethodTracker.trackMethodExecution(this, "apiAirTMWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoUSDCWithdrawalNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiMsiaBTWithdrawalNew", true, null);
		}

		MethodTracker.checkResultFail();
	}

	@Test(priority = 0,description = testCaseDescUtils.CPAPIWITHDRAW_OTHERSAPI)
	public void testAPICPWithdrawalOthers() throws Exception {
		apiWDOthersInfo();
		apiWDPCSOthersInfo();
	}

	@Test(priority = 2)
	@Parameters(value= {"Brand"})
	public void testAPIIBWithdrawal(String brand) throws Exception {

		switch(brand.toLowerCase())
		{
			case "um":
				//no wallet
				MethodTracker.trackMethodExecution(this, "apiThaiBTWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);
				break;
			case "mo":
				MethodTracker.trackMethodExecution(this, "apiThaiBTWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);
				break;
			case "vfx":
			case "au" :
				MethodTracker.trackMethodExecution(this, "apiThaiBTWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiNetellerWithdrawalNewIB", true, null);
				break;
			//non-PCS
			case "star":
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);				break;
			case "pug":
				MethodTracker.trackMethodExecution(this, "apiThaiBTWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiNetellerWithdrawalNewIB", true, null);
				break;
			case "vjp":
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiBitwalletWithdrawalNewIB", true, null);

				break;
			default: //vt
				MethodTracker.trackMethodExecution(this, "apiIBTWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiThaiBTWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoBTCWithdrawalNewIB", true, null);
				MethodTracker.trackMethodExecution(this, "apiNetellerWithdrawalNewIB", true, null);
		}

		MethodTracker.checkResultFail();
	}

	/* 	IB Withdrawal Others API */
	@Test(priority = 0,description = testCaseDescUtils.IBAPIWITHDRAW_OTHERSAPI)
	public void testAPIIBWithdrawalOthers() throws Exception {
		apiWDOthersInfoIB();
		apiPaymentInfoIB();
	}

	@Test(priority = 3,description = testCaseDescUtils.CPAPIWITHDRAW_BIGAMOUNT_RISKAUDIT,groups={"API_Payment"})
	@Parameters(value= {"Brand", "Server"})
	public void testAPIBigAmountWithdrawal(String brand, String server) throws Exception {
		brand = GlobalMethods.setEnvValues(brand);
		data = UATTestDataProvider.getUATAPIBigAmountWDUserData(brand, server);
		if(server.toLowerCase().matches(".*uat.*")){
			LogUtils.info("UAT环境");

			dbenv = GlobalProperties.ENV.UAT;
		}else {
			LogUtils.info("Alpha环境");

			dbenv = GlobalProperties.ENV.ALPHA;
		}
		apiBigAmountWithdrawal( server, dbenv.toString(),brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2]);
	}

	@Test(priority = 4)
	public void testAPIWithdrawalCCLBTOther() {
		data = UATTestDataProvider.getUATAPIMixedWithdrawUserData(this.initbrand, this.initserver);
		pcswdapi = new PCSAPIWithdraw((String) data[0][0],(String) data[0][2],(String) data[0][1]);
		this.apiWithdrawalCCLBTOther();
	}

	@Test(priority = 5)
	public void testAPIWithdrawalCCOther() {
		data = UATTestDataProvider.getUATAPIMixedWithdrawUserData(this.initbrand, this.initserver);
		pcswdapi = new PCSAPIWithdraw((String) data[0][0],(String) data[0][3],(String) data[0][1]);
		this.apiWithdrawalCCOther();
	}

	@Test(priority = 5)
	public void testAPIWithdrawalLBTOther() {
		data = UATTestDataProvider.getUATAPIMixedWithdrawUserData(this.initbrand, this.initserver);
		pcswdapi = new PCSAPIWithdraw((String) data[0][0],(String) data[0][4],(String) data[0][1]);
		this.apiWithdrawalLBTOther();
	}

	@Test(priority = 5)
	public void testAPIWithdrawalCCLBT() {
		data = UATTestDataProvider.getUATAPIMixedWithdrawUserData(this.initbrand, this.initserver);
		pcswdapi = new PCSAPIWithdraw((String) data[0][0],(String) data[0][5],(String) data[0][1]);
		this.apiWithdrawalCCLBT();
	}
}
