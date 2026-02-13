package newcrm.testcases.alpharegression.account;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.RegisterTestcases;
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

public class AlphaAutoRegTestCases extends RegisterTestcases {

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
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],(String)data[0][4],"","","True",context);
    }

    @Test(description = testCaseDescUtils.CPACC_AUTO_REGISTER_MT5, groups = {"CP_Auto_Register"})
    public void testAutoRegistMT5LiveAccount(@Optional("")String country) throws Exception {
        // AU VFSC regulator support golden flow registration
        // ASIC regulator use testRegisterASIC
        if (Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Regulator.equalsIgnoreCase(REGULATOR.ASIC.toString())) {
            throw new SkipException("Skipping this test intentionally.");
        }
        registerAuto(ibCode, "", PLATFORM.MT5, country, ACCOUNTTYPE.HEDGE_STP, Brand.equalsIgnoreCase(BRAND.STAR.toString()) ? CURRENCY.NZD : CURRENCY.USD, true);
    }

}
