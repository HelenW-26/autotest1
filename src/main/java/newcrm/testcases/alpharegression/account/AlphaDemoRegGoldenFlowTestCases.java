package newcrm.testcases.alpharegression.account;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.RegisterGoldTestcases;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.ENV;
import newcrm.utils.testCaseDescUtils;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class AlphaDemoRegGoldenFlowTestCases extends RegisterGoldTestcases {

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server","BranchVersion"})
    public void initiEnv(String brand, String server, String branchVersion, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        ibCode = GlobalMethods.getRegAffID(brand, ENV.ALPHA);
        data = TestDataProvider.getAlphaRegisterUsersData(brand, server);
        assertNotNull(data);
        branchVer = branchVersion;
        launchBrowser("alpha", "true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], (String)data[0][4], "", "", "True", context);
    }

    @Test(description = testCaseDescUtils.CPACC_REGISTER_DEMO_GOLDEN_FLOW_MT5_WITH_SUMSUB_ALPHA, groups = {"CP_GF_Demo_Register"})
    @Parameters(value= {"Country"})
    public void testRegistMT5DemoAccount_Gold(@Optional("")String Country) throws Exception {
        if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(REGULATOR.VFSC.toString())))
        {
            registerDemo(ibCode, "", PLATFORM.MT5, Country, true, ACCOUNTTYPE.HEDGE_STP, CURRENCY.USD, true);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

}
