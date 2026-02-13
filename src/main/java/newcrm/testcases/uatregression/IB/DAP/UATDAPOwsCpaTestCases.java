package newcrm.testcases.uatregression.IB.DAP;

import newcrm.global.GlobalMethods;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibtestcases.daptestcases.DAPClientJourneyTestCases;
import newcrm.testcases.ibtestcases.daptestcases.DAPOwsCpaTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class UATDAPOwsCpaTestCases extends DAPOwsCpaTestCases {

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        launchBrowserOWS( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);

        data= UATTestDataProvider.getUATDAPAccountUsersData(brand, server);

        UserConfig config = new UserConfig();

        launchBrowserOWS("uat","true", brand, (String)data[0][0], (String)data[0][7], (String)data[0][10], (String)data[0][11], (String)data[0][4], (String)data[0][8], (String)data[0][9], "True", context);
    }

}