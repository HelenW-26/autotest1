package newcrm.testcases;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.OWSLogin;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.MyWebDriverListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.Listeners.DevtoolsListener;
import vantagecrm.Utils;

import java.time.Duration;
import java.util.Objects;

public class OWSBaseTestCaseNew extends BaseTestCaseNew {


    protected static final ThreadLocal<String> prevExecuteIBAccEmail = new ThreadLocal<>();
    protected static final ThreadLocal<String> prevExecutePlatform = new ThreadLocal<>();

    @BeforeClass(alwaysRun = true)
    @Parameters(value = {"TestEnv", "headless", "Brand", "Regulator", "TraderURL", "TraderName", "TraderPass",
            "AdminURL", "AdminName", "AdminPass", "Debug", "Server"})
    public void beforMethod(@Optional("alpha") String TestEnv,
                            @Optional("False") String headless,
                            String Brand,
                            String Regulator,
                            @Optional("") String TraderURL,
                            @Optional("") String TraderName,
                            @Optional("") String TraderPass,
                            @Optional("") String AdminURL,
                            @Optional("") String AdminName,
                            @Optional("") String AdminPass,
                            @Optional("True") String Debug,
                            @Optional("") String server,
                            ITestContext context) throws Exception {
        if (!"".equals(server))  {
            serverName = server;
        }

        launchBrowserOWS(TestEnv, headless, Brand, Regulator, TraderURL, TraderName, TraderPass,
                AdminURL, AdminName, AdminPass, Debug, context);
    }

    @Parameters(value = {"TestEnv", "headless", "Brand", "Regulator", "TraderURL", "TraderName", "TraderPass",
            "AdminURL", "AdminName", "AdminPass", "Debug"})
    public void launchBrowserOWS(@Optional("alpha") String testEnv,
                                @Optional("False") String headless,
                                String brand,
                                String regulator,
                                @Optional("") String traderURL,
                                @Optional("") String traderName,
                                @Optional("") String traderPass,
                                @Optional("") String adminURL,
                                @Optional("") String adminName,
                                @Optional("") String adminPass,
                                @Optional("True") String debug,
                                ITestContext context) throws Exception{

        UserConfig config = new UserConfig();
        BaseTestCaseNew.TestEnv = testEnv;
        BaseTestCaseNew.Brand = brand;
        BaseTestCaseNew.Regulator = regulator;
        BaseTestCaseNew.TraderURL = traderURL;
        config.TraderName = traderName;
        config.TraderPass = traderPass;
        BaseTestCaseNew.AdminURL = adminURL;
        config.AdminName = adminName;
        config.AdminPass = adminPass;
        BaseTestCaseNew.debug = Boolean.parseBoolean(debug.trim());
        configThreadLocal.set(config);

        GlobalProperties.debug = BaseTestCaseNew.debug;
        GlobalProperties.env = testEnv;
        GlobalProperties.brand = brand;
        GlobalProperties.regulator = regulator;

        WebDriver driver = createWebDriver(headless, "chrome");

        driverThreadLocal.set(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        waitThreadLocal.set(wait);

        utils.Listeners.TestListener.driver = driver;
        context.setAttribute("driver", driver);

        Factor factor = new Factor(testEnv, brand, regulator, driver);
        factorThreadLocal.set(factor);

        performOWSLogin(driver, factor, traderURL, traderName, traderPass);


        prevExecutePlatform.set("OWS");
        prevExecuteIBAccEmail.set(traderName);
    }


    protected void performOWSLogin(WebDriver driver, Factor factor, String traderUrl, String traderName, String traderPass) {
        OWSLogin login = factor.newInstance(OWSLogin.class, traderUrl);

        if (login == null) {
            Assert.fail("Login Initialization Failed");
        }

        login.login(traderName, traderPass);

//        String loginErrMsg = login.checkExistsLoginAlertMsg();

//        if (loginErrMsg != null) {
//            Assert.fail("An error occurred during login. Error Msg: " + loginErrMsg);
//        }

//        BaseTestCaseNew.loginThreadLocal.set(login);
    }

    /**
     * 获取前一次执行的IB账户邮箱
     */
    protected String getPrevExecuteIBAccEmail() {
        return prevExecuteIBAccEmail.get();
    }

    /**
     * 获取前一次执行的平台类型
     */
    protected String getPrevExecutePlatform() {
        return prevExecutePlatform.get();
    }

    @AfterClass(alwaysRun = true)
    public void cleanupThreadSafeBrowser() {
        try {
            if (DevtoolsListener.devTools != null) {
                DevtoolsListener.devTools.disconnectSession();
                DevtoolsListener.devTools = null;
            }
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Error during driver cleanup: " + e.getMessage());
        } finally {
            driverThreadLocal.remove();
            waitThreadLocal.remove();
            configThreadLocal.remove();
            prevExecuteIBAccEmail.remove();
            prevExecutePlatform.remove();
            loginThreadLocal.remove();
        }
    }
}
