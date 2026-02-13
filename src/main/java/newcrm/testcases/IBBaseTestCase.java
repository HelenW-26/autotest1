package newcrm.testcases;

import newcrm.business.businessbase.CPLogin;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.MyWebDriverListener;

import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import utils.Listeners.DevtoolsListener;
import utils.LogUtils;
import vantagecrm.Utils;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class IBBaseTestCase extends BaseTestCase {


    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug","Server"})
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        if(!"".equals(server)) {
            serverName = server;
        }

        launchBrowserIB( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug"})
    public void launchBrowserIB(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                                @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                                @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
                                ITestContext context) throws Exception{
        BaseTestCase.TestEnv = TestEnv;
        BaseTestCase.Brand = Brand;
        BaseTestCase.Regulator = Regulator;
        BaseTestCase.TraderName = TraderName;
        BaseTestCase.TraderURL = TraderURL;
        BaseTestCase.TraderPass = TraderPass;
        BaseTestCase.AdminURL = AdminURL;
        BaseTestCase.AdminName = AdminName;
        BaseTestCase.AdminPass = AdminPass;
        Boolean debug = Boolean.valueOf(Debug.trim());
        GlobalProperties.debug = debug;
        GlobalProperties.env = BaseTestCase.TestEnv;
        GlobalProperties.brand = BaseTestCase.Brand;
        GlobalProperties.regulator = BaseTestCase.Regulator;
        BaseTestCase.prevExecuteCPAccEmail = "";
        BaseTestCase.prevExecuteAPAccEmail = "";

        for(GlobalProperties.ENV e: GlobalProperties.ENV.values()) {
            if(e.toString().equalsIgnoreCase(TestEnv)) {
                dbenv = e;
                break;
            }
        }

        for(GlobalProperties.BRAND b: GlobalProperties.BRAND.values()) {
            if(b.toString().equalsIgnoreCase(Brand)) {
                dbBrand = b;
                break;
            }
        }

        for(GlobalProperties.REGULATOR r: GlobalProperties.REGULATOR.values()) {
            if(r.toString().equalsIgnoreCase(Regulator)) {
                dbRegulator = r;
                break;
            }
        }

        printInfo();

        if (driver == null || !isBrowserOpen()) {

            driver = Utils.funcSetupDriver(driver, "chrome", headless);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            if(Objects.equals(headless, "true")) {
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
            }

            //Setup devtools listener
            if(GlobalProperties.isWeb)
                DevtoolsListener.setUpDevTools(driver);
            else
                DevtoolsListener.setUpDevToolsWeb(driver,false);

            MyWebDriverListener ls = new MyWebDriverListener();
            driver = new EventFiringDecorator(ls).decorate(driver);

            utils.Listeners.TestListener.driver=this.driver;
            context.setAttribute("driver", driver);
            myfactor = new Factor(TestEnv,Brand,Regulator,driver);
            loginIB();
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
            //boolean isDifferentLoginURL = !baseURL.equalsIgnoreCase(TraderURL);
            boolean isDifferentLoginURL = BaseTestCase.prevExecutePlatform != "IB";
            boolean isDifferentLoginUser = !BaseTestCase.prevExecuteIBAccEmail.equalsIgnoreCase(TraderName);

            // Create login session when:-
            // 1. Logon to portal without Login Page, occurs when register new client via simulator / prod client registration site
            // 2. Redirect from different URL
            // 3. Different user login credentials
            if (login == null) {
                LogUtils.info("Create login session...");
                clearLoginSession();
                myfactor = new Factor(TestEnv,Brand,Regulator,driver);
                loginIB();
            } else if (isDifferentLoginURL) {
                LogUtils.info("Different login URL, create login session...");
                loginIB();
            } else if (isDifferentLoginUser) {
                LogUtils.info("Different User, create login session...");
                clearLoginSession();
                loginIB();
            } else {
                // Continue with login session when next event occurs within the same site and same user login credentials
                LogUtils.info("Continue with current login session...");
            }
        }

        BaseTestCase.prevExecutePlatform = "IB";
        BaseTestCase.prevExecuteIBAccEmail = TraderName;
    }

    protected void loginIB() {
        login = myfactor.newInstance(CPLogin.class,TraderURL);

        if (login == null) {
            Assert.fail("Login Initialization Failed");
        }

        // Login
        login.loginIB(TraderName, TraderPass);

        // Check for login error message
        String loginErrMsg = login.checkExistsLoginAlertMsg();

        if (loginErrMsg != null) {
            Assert.fail("An error occurred during login. Error Msg: " + loginErrMsg);
        }

        // Check login success
        Map.Entry<Boolean, String> checkLoginSuccessResp = login.checkLoginSuccess();
        if (!checkLoginSuccessResp.getKey()) {
            Assert.fail("Login failed. Error Msg: " + checkLoginSuccessResp.getValue());
        }
    }

}
