package newcrm.testcases.uatregression;

import adminBase.TaskManagement;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATWithdrawAuditTestCases extends SystemSettingTestCases {

	@Override
	public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
							@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
							@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
							ITestContext context) throws Exception {
		launchAdminBrowser( headless, AdminURL, AdminName, AdminPass, Regulator, Brand, TestEnv, context);
	}

	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) throws Exception {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
		assertNotNull(data);
		Object[][] adminData = UATTestDataProvider.getUATRegUsersData(brand, server);

		AdminAPIPayment adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
		//更新SRC risk audit状态的订单为Accepted
		PCSAPIWithdraw pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		adminPaymentAPI.setUserIdAndCountryCode(pcswdapi);
		adminPaymentAPI.batchUpdateSRCRiskRecord(GlobalProperties.ENV.valueOf("UAT"), GlobalProperties.BRAND.valueOf(brand.toUpperCase()),GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]));
		launchAdminBrowser( "true", (String)data[0][4], (String)data[0][7], (String)data[0][8], (String)data[0][0], brand, "uat", context);
	}
	
	@Test(priority = 2, description = testCaseDescUtils.APAUDIT_WITHDAWAL,groups = {"CP_Withdraw"})
	@Parameters(value= {"Server"})
	public void testadminwithdraw(String server) throws Exception {
		Object data[][] = UATTestDataProvider.getUATRegUsersData(GlobalMethods.getBrand(), server);
		TaskManagement tm = new TaskManagement(driver,GlobalMethods.getBrand());
 			System.out.println("brand:" + GlobalMethods.getBrand());
		//withdraw audit
		tm.funcWDCompletebyChannelSmokeTest((String)data[0][4], (String)data[0][1], GlobalMethods.getBrand(),(String)data[0][0]);
	}

}
