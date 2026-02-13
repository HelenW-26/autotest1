package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.ENV;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.RegisterGoldTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATRegisterGoldTestCases extends RegisterGoldTestcases {

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server","BranchVersion"})
    public void initiEnv(String brand, String server, String branchVersion, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        ibCode = GlobalMethods.getRegAffID(brand, ENV.UAT);
        data = UATTestDataProvider.getUATRegisterUsersData(brand, server);
        assertNotNull(data);
        branchVer = branchVersion;
        launchBrowser("uat", "true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], (String)data[0][4], "", "", "True", context);
    }

    // Golden flow only support MT5 when register first trading account
    @Override
    @Test(description = testCaseDescUtils.CPACC_REGISTER_GOLDEN_FLOW_MT5_WITH_SUMSUB, groups = {"CP_GoldenFlow_AU_Only"})
    @Parameters(value= {"Country"})
    public void testRegistMT5LiveAccount_Gold(@Optional("")String country) throws Exception {
        if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(REGULATOR.VFSC.toString())))
        {
            register(ibCode, "", "",PLATFORM.MT5, country, true, ACCOUNTTYPE.STANDARD_STP, CURRENCY.USD, true);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(description = testCaseDescUtils.CPACC_REGISTER_GOLDEN_FLOW_MT5, groups = {"CP_GoldenFlow_AU_Only"})
    @Parameters(value= {"Country"})
    public void testRegistMT5LiveAccount_Gold_withoutSumsub(@Optional("")String country) throws Exception {
        if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(REGULATOR.VFSC.toString())))
        {
            register(ibCode, "", "",PLATFORM.MT5, country, true, ACCOUNTTYPE.STANDARD_STP, CURRENCY.USD, false);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

}
