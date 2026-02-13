package newcrm.testcases.uatregression.account;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.account.AdditionalAccTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LogUtils;

import static org.testng.Assert.assertNotNull;

public class UATOpenAddAccConfigCheckTestCases extends AdditionalAccTestCases {

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server","Country"})
    public void initiEnv(String brand,String server, String country, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        data = UATTestDataProvider.getUATOpenAddAccConfigCheckUsersData(brand, server, country);
        assertNotNull(data);
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(description = testCaseDescUtils.CPACC_ADDITIONAL_ACC_MT4_CONFIG_CHECK, groups = {"CP_Open_Additional_Acc_Config_Check"})
    @Parameters("Country")
    public void testOpenMT4AddAccConfigCheck(@Optional("")String country) throws Exception {
        LogUtils.info("Country: " + country);
        openAddAccConfigCheck(PLATFORM.MT4, country);
    }

    @Test(description = testCaseDescUtils.CPACC_ADDITIONAL_ACC_MT5_CONFIG_CHECK, groups = {"CP_Open_Additional_Acc_Config_Check"})
    @Parameters("Country")
    public void testOpenMT5AddAccConfigCheck(@Optional("")String country) throws Exception {
        LogUtils.info("Country: " + country);
        openAddAccConfigCheck(PLATFORM.MT5, country);
    }

    @Test(description = testCaseDescUtils.CPACC_ADDITIONAL_ACC_MTS_CONFIG_CHECK, groups = {"CP_Open_Additional_Acc_Config_Check"})
    @Parameters("Country")
    public void testOpenMTSAddAccConfigCheck(@Optional("")String country) throws Exception {
        LogUtils.info("Country: " + country);
        openAddAccConfigCheck(PLATFORM.MTS, country);
    }

}
