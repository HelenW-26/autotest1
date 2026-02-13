package newcrm.testcases.uatregression.account;

import newcrm.adminapi.AdminAPI;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.account.DemoAccountTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATDemoAccountUpdLeverage extends DemoAccountTestCases {

    private AdminAPI admin;
    String optCode="987654";

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = UATTestDataProvider.getUATAccountForgotPwdUsersData(brand, server);
        assertNotNull(data);

        AdminAPIUserAccount adminUserAcctAPI = new AdminAPIUserAccount((String) data[0][4], REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], BRAND.valueOf(brand.toUpperCase()), ENV.valueOf("UAT"));
        adminUserAcctAPI.apiEnableAutoLeverageAudit();
        //check admin leverage auto audit status
		/*try
		{
			launchAdminBrowser("true", context);
			//login admin portal
			Login login = new Login(driver);
			admin = new AdminAPI((String)data[0][4], GlobalProperties.REGULATOR.valueOf(((String)data[0][0]).toUpperCase()),(String)data[0][7],(String)data[0][8], GlobalProperties.BRAND.valueOf(GlobalMethods.getBrand().toUpperCase()), GlobalProperties.ENV.valueOf("uat"));
			login.AdminLogIn((String)data[0][4], (String)data[0][7], (String)data[0][8], (String)data[0][0],optCode,GlobalProperties.ENV.uat,GlobalProperties.BRAND.valueOf(brand.toUpperCase()));

			TaskManagement tm = new TaskManagement(driver,GlobalMethods.getBrand());
			System.out.println("brand:" + GlobalMethods.getBrand());

			tm.openLeverageAutoAudit((String)data[0][4]);
			driver.quit();
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("Check admin transfer status failed");
		}*/
        //launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

//    @Test(description = testCaseDescUtils.CPACC_UPDATE_MT4_DEMO_ACC_INFO, groups = {"CP_Demo_Acc_Info_Update"})
//    public void testChangeMT4DemoAccountInfo() throws Exception {
//        changeDemoAccountInfo(PLATFORM.MT4);
//    }

    @Test(description = testCaseDescUtils.CPACC_UPDATE_MT5_DEMO_ACC_INFO, groups = {"CP_Demo_Acc_Info_Update"})
    public void testChangeMT5DemoAccountInfo() throws Exception {
        changeDemoAccountInfo(PLATFORM.MT5);
    }

//    @Test(priority=0, description = testCaseDescUtils.CPACC_UPDATE_LEVERAGE_DEMO_ACC_MT4)
//    public void testChangeMT4DemoAccountLeverage() throws Exception {
//        changeDemoAccountLeverage(PLATFORM.MT4);
//    }
//
//    @Test(priority=0, description = testCaseDescUtils.CPACC_UPDATE_LEVERAGE_DEMO_ACC_MT5)
//    public void testChangeMT5DemoAccountLeverage() throws Exception {
//        changeDemoAccountLeverage(PLATFORM.MT5);
//    }

}
