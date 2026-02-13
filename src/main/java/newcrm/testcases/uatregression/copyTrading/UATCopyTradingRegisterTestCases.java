package newcrm.testcases.uatregression.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.ENV;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.RegisterTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class UATCopyTradingRegisterTestCases extends RegisterTestcases {

    RegisterTestcases register = new RegisterTestcases();

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
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
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],(String)data[0][4],"","","True",context);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPCOPYTRADING_OPENACCOUNT,groups= {"CP_Register_MTS"})
    @Parameters(value= {"Country"})
    public void testOpenCopyTradingAccount(@Optional("")String country) throws Exception {
        List<String> withoutCheck = Arrays.asList(GlobalProperties.BRAND.PUG.toString(),GlobalProperties.BRAND.STAR.toString(),GlobalProperties.BRAND.UM.toString(),GlobalProperties.BRAND.MO.toString(),GlobalProperties.BRAND.VJP.toString());

        if(!Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()))
        {
            registerNew(ibCode,"","", GlobalProperties.PLATFORM.MTS,country,false, GlobalProperties.ACCOUNTTYPE.STANDARD_STP,GlobalProperties.CURRENCY.USD, true);
        }
        else
        {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(description = testCaseDescUtils.CPACC_REGISTER_MT5_WITHOUT_CHECK)
    public void testRegistMTSLiveAccountWithoutCheck(@Optional("")String country) throws Exception {
        //open account audit check
        //registerNew(ibCode,"", GlobalProperties.PLATFORM.MTS,country,false, GlobalProperties.ACCOUNTTYPE.STANDARD_STP,GlobalProperties.CURRENCY.USD);
        register.registerNew(ibCode,"", "",GlobalProperties.PLATFORM.MTS,country,false, GlobalProperties.ACCOUNTTYPE.STANDARD_STP,GlobalProperties.CURRENCY.USD, true);
    }

    @Test(priority = 0, description = testCaseDescUtils.CPACC_REGISTER_MT5)
    public void testRegistMTSLiveAccount(@Optional("")String country) throws Exception {
        register.registerNew(ibCode, "", "",GlobalProperties.PLATFORM.MTS, country, true, GlobalProperties.ACCOUNTTYPE.STANDARD_STP, GlobalProperties.CURRENCY.USD, true);
    }

}
