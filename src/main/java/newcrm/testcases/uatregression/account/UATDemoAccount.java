package newcrm.testcases.uatregression.account;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.account.DemoAccountTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATDemoAccount extends DemoAccountTestCases {

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
        data = UATTestDataProvider.getUATAccountForgotPwdUsersData(brand, server);
        assertNotNull(data);
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(description = testCaseDescUtils.CPACC_MT4_DEMO_ACC_INFO_CHECK, groups = {"CP_Demo_Acc_Info_Check"})
    public void testMT4DemoAccountInfoCheck() throws Exception {
        demoAccountInfoCheck(PLATFORM.MT4);
    }

//    @Test(description = testCaseDescUtils.CPACC_MT5_DEMO_ACC_INFO_CHECK, groups = {"CP_Demo_Acc_Info_Check"})
//    public void testMT5DemoAccountInfoCheck() throws Exception {
//        demoAccountInfoCheck(PLATFORM.MT5);
//    }

}
