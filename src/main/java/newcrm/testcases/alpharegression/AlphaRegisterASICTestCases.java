package newcrm.testcases.alpharegression;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.ENV;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.RegisterTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class AlphaRegisterASICTestCases extends RegisterTestcases {

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
                            ITestContext context) {

        Regulator = "ASIC";

        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server","BranchVersion"})
    public void initiEnv(String brand, String server, String branchVersion, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        ibCode = GlobalMethods.getRegAffID(brand, ENV.ALPHA);
        data = TestDataProvider.getAlphaRegisterUsersData(brand, server);
        assertNotNull(data);
        data[0][0] = "ASIC";
        branchVer = branchVersion;
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],(String)data[0][4],(String)data[0][7],(String)data[0][8],"True",context);
    }

    @Test(description = testCaseDescUtils.CPACC_REGISTER_ASIC_MT5_ALPHA, groups= {"CP_Register_ASIC"})
    @Parameters(value= {"Country"})
    public void testRegisterASIC(@Optional("")String Country)  {

        if (!Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) && !Brand.equalsIgnoreCase(GlobalProperties.BRAND.STAR.toString()) && !Brand.equalsIgnoreCase(GlobalProperties.BRAND.PUG.toString())) {
            throw new SkipException("Skipping this test intentionally.");
        }

        List<String> withoutCheck = Arrays.asList(GlobalProperties.BRAND.PUG.toString(), GlobalProperties.BRAND.STAR.toString());

        if(withoutCheck.contains(GlobalMethods.getBrand().toUpperCase()))
        {
            MethodTracker.trackMethodExecution(this, "testRegistMT5LiveAccountWithoutCheck", true, null, Country);
        }
        else
        {
            MethodTracker.trackMethodExecution(this, "testRegistMT5LiveAccount", true, null, Country);
        }

        MethodTracker.checkResultFail();
    }


    @Test(description = testCaseDescUtils.CPACC_REGISTER_ASIC_MT5_WITHOUT_CHECK)
    public void testRegistMT5LiveAccountWithoutCheck(@Optional("")String country) throws Exception {
        //open account audit check
        registerNew(ibCode,"","", GlobalProperties.PLATFORM.MT5,country,false, GlobalProperties.ACCOUNTTYPE.STANDARD_STP,GlobalProperties.CURRENCY.USD, false);
    }

    @Override
    @Test(priority = 0, description = testCaseDescUtils.CPACC_REGISTER_ASIC_MT5_ALPHA)
    public void testRegistMT5LiveAccount(@Optional("")String country) throws Exception {
        registerNew(ibCode, "","", GlobalProperties.PLATFORM.MT5, country, true, GlobalProperties.ACCOUNTTYPE.STANDARD_STP, GlobalProperties.CURRENCY.USD, false);
    }

}
