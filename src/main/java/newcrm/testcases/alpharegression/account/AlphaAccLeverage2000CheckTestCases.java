package newcrm.testcases.alpharegression.account;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.AccountManagementTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LogUtils;

import static org.testng.Assert.assertNotNull;

public class AlphaAccLeverage2000CheckTestCases extends AccountManagementTestCases {

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server","Country"})
    public void initiEnv(String brand,String server, String country, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        data = TestDataProvider.getAlphaOpenAddAccConfigCheckUsersData(brand, server, country);
        assertNotNull(data);

        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(description = testCaseDescUtils.CPACC_MT5_LIVE_ACC_LEVERAGE_2000_CHECK, groups = {"CP_Live_Acc_Leverage_Check"})
    @Parameters("Country")
    public void testMT5LiveAccountLeverage2000Check(@Optional("")String country) throws Exception {
        LogUtils.info(String.format("Country: %s, Leverage: %s", country, "2000"));
        liveAccountLeverageCheck(GlobalProperties.PLATFORM.MT5, "2000");
    }

}
