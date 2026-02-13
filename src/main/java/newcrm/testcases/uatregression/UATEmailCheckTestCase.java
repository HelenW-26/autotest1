package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.pages.adminpages.EmailManagementPage;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATEmailCheckTestCase extends SystemSettingTestCases {

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        launchAdminBrowser( headless, AdminURL, AdminName, AdminPass, Regulator, Brand, TestEnv, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
        assertNotNull(data);

        launchAdminBrowser("true", (String)data[0][4], (String)data[0][7], (String)data[0][8], (String)data[0][0], brand, "uat", context);
    }

    @Test(priority = 2, description = testCaseDescUtils.APSETTING_EMAIL,groups = {"CP_EmailCheck"})
    @Parameters(value= {"Server"})
    public void testEmail(String server) throws Exception {
        Object data[][] = UATTestDataProvider.getUATRegUsersData(GlobalMethods.getBrand(), server);

        EmailManagementPage em = new EmailManagementPage(driver);
        em.myfactor = myfactor;

        //check email
        em.checkEmail((String)data[0][4], (String)data[0][1]);
    }
}
