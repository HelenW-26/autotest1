package newcrm.testcases.prodregression;

import adminBase.Login;
import newcrm.adminapi.AdminAPI;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.adminpages.EmailManagementPage;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class ProdEmailCheckTestCase extends SystemSettingTestCases {
    private AdminAPI admin;
    String optCode;
    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        launchAdminBrowser( headless, AdminURL, AdminName, AdminPass, Regulator, Brand, TestEnv, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"headless","AdminURL", "AdminName", "AdminPass", "Regulator","Brand","TestEnv"})
    public void initiEnv(String headless, String AdminURL,String AdminName,String AdminPass, String Regulator, String Brand,String TestEnv,ITestContext context) throws Exception {

        launchAdminBrowser(headless, AdminURL, AdminName, AdminPass, Regulator, Brand, TestEnv, context);

        //login admin portal
//        Login login = new Login(driver);
//        admin = new AdminAPI(AdminURL, GlobalProperties.REGULATOR.valueOf(Regulator),AdminName,AdminPass, GlobalProperties.BRAND.valueOf(Brand), GlobalProperties.ENV.valueOf(TestEnv));
//        optCode = admin.getCode();
//        login.AdminLogIn(AdminURL, AdminName, AdminPass, Regulator,optCode,GlobalProperties.ENV.getENV(TestEnv.toUpperCase()),GlobalProperties.BRAND.valueOf(Brand));
    }

    @Test(priority = 0)
    @Parameters(value= {"AdminURL","TraderName"})
    public void testEmail(String adminURL,String TraderName) throws Exception {
        EmailManagementPage em = new EmailManagementPage(driver);
        em.myfactor = myfactor;

        //check email
        em.checkEmail(adminURL, TraderName);
    }

}
