package newcrm.testcases.uatregression.IB.Report;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibtestcases.IBReportTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertNotNull;

public class UATIBReportTestCases extends IBReportTestCases {

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        data = UATTestDataProvider.getUATIBReportUsersData(brand, server);
        assertNotNull(data);

        launchBrowser("uat","true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], (String)data[0][4], (String)data[0][8], (String)data[0][9], "True", context);
//        launchBrowser("alpha","true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], (String)data[0][4], (String)data[0][7], (String)data[0][8], "True", context);
    }

}
