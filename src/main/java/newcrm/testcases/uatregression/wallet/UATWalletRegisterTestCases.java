package newcrm.testcases.uatregression.wallet;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ENV;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.wallet.WalletRegisterTestCases;
import newcrm.testcases.uatregression.UATRegisterGoldTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATWalletRegisterTestCases extends WalletRegisterTestCases {

    UATRegisterGoldTestCases uatRegGoldTc = new UATRegisterGoldTestCases();

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server","BranchVersion"})
    public void initiEnv(String brand,String server, String branchVersion, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        regGoldTc.ibCode = GlobalMethods.getRegAffID(brand, ENV.UAT);
        regGoldTc.data= UATTestDataProvider.getUATRegisterUsersData(brand, server);
        Object[][] data = regGoldTc.data;
        assertNotNull(data);
        regGoldTc.branchVer = branchVersion;
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],(String)data[0][4],"","","True",context);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_OPENACCOUNT, groups= {"CP_Wallet_Register"})
    @Parameters(value= {"Country"})
    public void testWalletOpenAccount(@Optional("")String country) throws Exception {
        testWalletOpenAccount(true, country);
    }

}
