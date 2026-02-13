package newcrm.testcases.alpharegression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;
import utils.Listeners.MethodTracker;

import static org.testng.Assert.assertNotNull;

public class AlphaWithdrawTestCases extends WithdrawTestCases {

	Object data[][];
	@Override
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
							@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
							@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
							ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}
	
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
		Brand = brand;
		data = TestDataProvider.getAlphaRegUsersData(brand, server);
		assertNotNull(data);
		setData( data);
		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));;
		adminPaymentAPI.apiDisableLoginWithdrawal2FA();

		//new CPAPIWithdraw((String)data[0][3],(String)data[0][1],(String)data[0][2]);
		launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);

	}

	@AfterClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void teardown(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
		Brand = brand;
		data = TestDataProvider.getAlphaRegUsersData(brand, server);;
		assertNotNull(data);
		Object[][] adminData =  TestDataProvider.getAlphaRegUsersData(brand, server);;

		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));;
		//批量更新出金risk audit状态为Accepted
		PCSAPIWithdraw pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		adminPaymentAPI.setUserIdAndCountryCode(pcswdapi);

		adminPaymentAPI.batchUpdateSRCRiskRecord(GlobalProperties.ENV.valueOf("ALPHA"), GlobalProperties.BRAND.valueOf(brand.toUpperCase()),GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]));
	}

	@Test(priority = 2, description = testCaseDescUtils.CPWITHDRAW_ST,groups = {"CP_Withdraw"})
	public void testcpwithdraw() {
		// MethodTracker for 多层API call
		switch(Brand.toLowerCase())
		{
			case "vjp":
				MethodTracker.trackMethodExecution(this, "testCryptoCRYPTOETHNew", true, null);
				break;
			default:
				MethodTracker.trackMethodExecution(this, "testInternationalBankWithdrawNew", true, null);
		}

		MethodTracker.checkResultFail();
	}

	@Test(priority = 3, description = testCaseDescUtils.CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT_DESC)
	public void testWithdrawDeductedMaxAmount() {
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			withdrawDeductedMaxAmount(Brand);
		}else {
			throw new SkipException("Skipping this test intentionally.");
		}

	}
	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_WITHDRAW_DETAIL_INFO_CHECK)
	public void testWithDrawPageChangeRateCheck() {
		if(!Brand.equalsIgnoreCase("star")){
			withDrawPageChangeRateCheck();
		}else {
			throw new SkipException("Skipping this test intentionally.");
		}

	}
}
