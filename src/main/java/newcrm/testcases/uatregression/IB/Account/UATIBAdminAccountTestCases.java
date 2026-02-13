package newcrm.testcases.uatregression.IB.Account;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.testcases.ibtestcases.IBAdminAccountTestCases;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertNotNull;

public class UATIBAdminAccountTestCases extends IBAdminAccountTestCases {

//    private SystemSettingTestCases systemSettingTestCases = new SystemSettingTestCases();

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        launchAdminBrowser(headless, AdminURL, AdminName, AdminPass, Regulator, Brand, TestEnv, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);
        data = UATTestDataProvider.getUATIBReportUsersData(brand, server);
        agreementData = UATTestDataProvider.getUATIBAgreementUsersData(brand, server);
        assertNotNull(data);

        launchAdminBrowser("true", (String)data[0][4], (String)data[0][8], (String)data[0][9], (String)data[0][0], brand, "uat", context);
        contextData = context;
//        TraderURL = (String)data[0][3];
//        TraderName = (String)data[0][1];
//        TraderPass = (String)data[0][2];
    }

}
