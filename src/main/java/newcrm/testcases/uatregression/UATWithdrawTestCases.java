package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;
import utils.Listeners.MethodTracker;

import static org.testng.Assert.assertNotNull;

public class UATWithdrawTestCases extends WithdrawTestCases {
	Object data[][];

	@Override
	public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
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
		data = UATTestDataProvider.getUATRegUsersData(brand, server);
		assertNotNull(data);
		setData( data);

		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));;
		adminPaymentAPI.apiDisableLoginWithdrawal2FA();
		//取消所有出金
		new CPAPIWithdraw((String)data[0][3],(String)data[0][1],(String)data[0][2]);
		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);

	}

	@AfterClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void teardown(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
		Brand = brand;
		data = UATTestDataProvider.getUATRegUsersData(brand, server);
		assertNotNull(data);
		Object[][] adminData = UATTestDataProvider.getUATRegUsersData(brand, server);

		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));;
		//批量更新出金risk audit状态为Accepted
		PCSAPIWithdraw pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		adminPaymentAPI.setUserIdAndCountryCode(pcswdapi);
		adminPaymentAPI.batchUpdateSRCRiskRecord(GlobalProperties.ENV.valueOf("UAT"), GlobalProperties.BRAND.valueOf(brand.toUpperCase()),GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]));
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

}
