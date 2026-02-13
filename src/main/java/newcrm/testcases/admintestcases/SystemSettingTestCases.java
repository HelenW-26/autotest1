package newcrm.testcases.admintestcases;

import adminBase.Login;
import newcrm.adminapi.AdminAPI;
import newcrm.business.businessbase.CPLogin;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.MyWebDriverListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.ITestContext;

import org.testng.annotations.BeforeMethod;
import utils.LogUtils;
import vantagecrm.Utils;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;

public class SystemSettingTestCases extends BaseTestCaseNew {

    protected static Login AdminLogin;
    protected Factor myfactor;
    protected WebDriver driver;
    protected BaseTestCaseNew.UserConfig user;
    @BeforeMethod(alwaysRun = true)
    public void initMethod(){
        if (myfactor == null){
            myfactor = getFactorNew();
        }
        if (driver ==null){
            driver = getDriverNew();
        }
    }
    public void launchAdminBrowser(String headless, String AdminURL, String AdminName, String AdminPass, String Regulator, String Brand, String TestEnv, ITestContext context) throws Exception {

        BaseTestCaseNew.TestEnv = TestEnv;
        BaseTestCaseNew.Brand = Brand;
        BaseTestCaseNew.Regulator = Regulator;
        BaseTestCaseNew.AdminURL = AdminURL;
        user = new UserConfig();
        user.AdminName = AdminName;
        user.AdminPass = AdminPass;
        BaseTestCaseNew.prevExecuteCPAccEmail = "";
        BaseTestCaseNew.prevExecuteIBAccEmail = "";

        if (driver == null || !isBrowserOpen()) {
            //WebDriverManager.chromedriver().setup();
            driver = Utils.funcSetupDriver(driver, "chrome", headless);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            if(Objects.equals(headless, "true")) {
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
            }

            //使用带有监听器的driver
            MyWebDriverListener ls = new MyWebDriverListener();

            driver = new EventFiringDecorator(ls).decorate(driver);

            utils.Listeners.TestListener.driver=this.driver;
            context.setAttribute("driver", driver);
            myfactor = new Factor(TestEnv,Brand,Regulator,driver);
            AdminLogin();
            LogUtils.info("***********************************************");
            LogUtils.info("Initial browser completed");
            LogUtils.info("Create login session...");
        } else {
            LogUtils.info("***********************************************");
            LogUtils.info("Browser already open. Reusing existing instance...");

            if (!context.getAttributeNames().contains("driver"))
            {
                context.setAttribute("driver", driver);
            }

            //String baseURL = Utils.ParseURLtoBaseURL(driver.getCurrentUrl());
            //boolean isDifferentLoginURL = !baseURL.equalsIgnoreCase(AdminURL);
            boolean isDifferentLoginURL = BaseTestCaseNew.prevExecutePlatform != "AP";
            boolean isDifferentLoginUser = !BaseTestCaseNew.prevExecuteAPAccEmail.equalsIgnoreCase(AdminName);

            // Create login session when:-
            // 1. Logon to portal without Login Page, occurs when register new client via simulator / prod client registration site
            // 2. Redirect from different URL
            // 3. Different user login credentials
            if (AdminLogin == null || isDifferentLoginURL || isDifferentLoginUser) {
                LogUtils.info("Create login session...");
                myfactor = new Factor(TestEnv,Brand,Regulator,driver);
                AdminLogin();
            } else {
                // Continue with login session when next event occurs within the same site and same user login credentials
                LogUtils.info("Continue with current login session...");
            }

        }

        BaseTestCaseNew.prevExecutePlatform = "AP";
        BaseTestCaseNew.prevExecuteAPAccEmail = AdminName;
    }

    public void AdminLogin() throws Exception {
        AdminLogin = new Login(driver);
//        UserConfig user = getConfigNew();
        AdminAPI admin = new AdminAPI(AdminURL, GlobalProperties.REGULATOR.valueOf(Regulator.toUpperCase()), user.AdminName, user.AdminPass, GlobalProperties.BRAND.valueOf(Brand.toUpperCase()), GlobalProperties.ENV.valueOf(TestEnv.toUpperCase()));
        String optCode = admin.getCode();
        AdminLogin.AdminLogInNew(AdminURL, user.AdminName, user.AdminPass, Regulator, optCode, GlobalProperties.ENV.valueOf(TestEnv.toUpperCase()), GlobalProperties.BRAND.valueOf(Brand.toUpperCase()));
    }

}
