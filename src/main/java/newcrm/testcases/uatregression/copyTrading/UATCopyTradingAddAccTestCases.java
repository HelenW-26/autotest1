package newcrm.testcases.uatregression.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.AccountManagementTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATCopyTradingAddAccTestCases extends AccountManagementTestCases{

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        GlobalMethods.setPlatform("MTS");
        data = UATTestDataProvider.getUATCopyTradingAddAccUsersData(brand, server);
        assertNotNull(data);
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPACC_ADDITIONAL_ACC_MTS, groups = {"CP_Add_Acc_MTS"})
    public void testOpenAdditionalMTSAccount() {
        funcAddAcc(GlobalProperties.PLATFORM.MTS, GlobalProperties.ACCOUNTTYPE.MTS_HEDGE_STP, GlobalProperties.CURRENCY.USD);
    }

}
