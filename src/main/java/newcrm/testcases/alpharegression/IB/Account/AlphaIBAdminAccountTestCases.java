package newcrm.testcases.alpharegression.IB.Account;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.testcases.ibtestcases.IBAdminAccountTestCases;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertNotNull;

public class AlphaIBAdminAccountTestCases extends IBAdminAccountTestCases {

    private SystemSettingTestCases systemSettingTestCases = new SystemSettingTestCases();

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        systemSettingTestCases.launchAdminBrowser(headless, AdminURL, AdminName, AdminPass, Regulator, Brand, TestEnv, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);
        data = TestDataProvider.getAlphaIBReportUsersData(brand, server);
        assertNotNull(data);

        systemSettingTestCases.launchAdminBrowser("true", (String)data[0][4], (String)data[0][7], (String)data[0][8], (String)data[0][0], brand, "alpha", context);
        contextData = context;
//        TraderURL = (String)data[0][3];
//        TraderName = (String)data[0][1];
//        TraderPass = (String)data[0][2];
    }

}
