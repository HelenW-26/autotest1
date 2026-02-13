package newcrm.testcases;

import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.*;


import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.MyWebDriverListener;
import utils.Listeners.DevtoolsListener;
import vantagecrm.Utils;


public class LoginBaseTestCaseNew extends BaseTestCaseNew {

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
                            ITestContext context) {
        if (!"".equals(server)) {
            serverName = server;
        }

        launchBrowserLoginNew(TestEnv, headless, Brand, Regulator, TraderURL, TraderName, TraderPass,
                AdminURL, AdminName, AdminPass, Debug, context);
    }

    @Parameters(value = {"TestEnv", "headless", "Brand", "Regulator", "TraderURL", "TraderName", "TraderPass",
            "AdminURL", "AdminName", "AdminPass", "Debug"})
    public void launchBrowserLoginNew(@Optional("alpha") String testEnv,
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
                                      ITestContext context) {

        BaseTestCaseNew.UserConfig config = new BaseTestCaseNew.UserConfig();
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

        WebDriver driver = Utils.funcSetupDriver(getDriverNew(), "chrome", headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        if (Objects.equals(headless, "true")) {
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        }

        if (GlobalProperties.isWeb) {
            DevtoolsListener.setUpDevTools(driver);
        } else {
            DevtoolsListener.setUpDevToolsWeb(driver, false);
        }
        MyWebDriverListener listener = new MyWebDriverListener();
        driver = new EventFiringDecorator(listener).decorate(driver);

        driverThreadLocal.set(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        waitThreadLocal.set(wait);

        utils.Listeners.TestListener.driver = driver;
        context.setAttribute("driver", driver);

        Factor myfactor = new Factor(testEnv, brand, regulator, driver);
        factorThreadLocal.set(myfactor);

        GlobalMethods.printDebugInfo("Initial browser completed");
        GlobalMethods.printDebugInfo("Login session not created...");
    }


    @Override
    protected void login() {

        GlobalMethods.printDebugInfo("Skipping automatic login in LoginBaseTestCaseNew");
    }
}
