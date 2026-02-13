package newcrm.testcases;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import newcrm.utils.download.DownloadFile;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import newcrm.business.businessbase.CPLogin;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.MyWebDriverListener;
import utils.Listeners.DevtoolsListener;
import utils.LogUtils;
import vantagecrm.Utils;

public class BaseTestCaseNew {

    protected static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<UserConfig> configThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<CPLogin> loginThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Factor> factorThreadLocal = new ThreadLocal<>();


    protected static String serverName = "";
    protected static String CPUrl;
    protected static String TraderURL;
    protected static String OWSURL;
    protected static String TestEnv;
    protected static String Brand;
    protected static String Regulator;
    protected static String AdminURL;
    protected static boolean debug;

    protected static ENV dbenv = ENV.ALPHA;
    protected static BRAND dbBrand;
    protected static REGULATOR dbRegulator;

    protected static String prevExecuteCPAccEmail = "";
    protected static String prevExecuteAPAccEmail = "";
    protected static String prevExecuteIBAccEmail = "";
    protected static String prevExecutePlatform = "";

    public static class UserConfig {
        public String TraderName;
        public String TraderPass;
        public String AdminName;
        public String AdminPass;
    }

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
        if (!"".equals(server)) {
            serverName = server;
        }

        launchBrowser(TestEnv, headless, Brand, Regulator, TraderURL, TraderName, TraderPass,
                AdminURL, AdminName, AdminPass, Debug, context);
    }

    @Parameters(value = {"TestEnv", "headless", "Brand", "Regulator", "TraderURL", "TraderName", "TraderPass",
            "AdminURL", "AdminName", "AdminPass", "Debug", "Server"})
    public void launchBrowser(@Optional("alpha") String testEnv,
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
        GlobalMethods.printDebugInfo("find servername:" + serverName);
        BaseTestCaseNew.CPUrl = traderURL;

        for(ENV e: ENV.values()) {
            if(e.toString().equalsIgnoreCase(TestEnv)) {
                dbenv = e;
                break;
            }
        }

        for(BRAND b: BRAND.values()) {
            if(b.toString().equalsIgnoreCase(Brand)) {
                dbBrand = b;
                break;
            }
        }

        for(REGULATOR r: REGULATOR.values()) {
            if(r.toString().equalsIgnoreCase(Regulator)) {
                dbRegulator = r;
                break;
            }
        }

        printInfo();

        // Create temporary download folder
        if(GlobalProperties.isWeb) {
            DownloadFile.createTempFolder();
        }

        WebDriver driver = createWebDriver(headless, "chrome");

        driverThreadLocal.set(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        waitThreadLocal.set(wait);

        utils.Listeners.TestListener.driver = driver;
        context.setAttribute("driver", driver);

        Factor myfactor = new Factor(testEnv, brand, regulator, driver);
        factorThreadLocal.set(myfactor);
        login();
    }

    protected void login() {
        Factor factor = getFactorNew();

        CPLogin login = factor.newInstance(CPLogin.class, TraderURL);

        if (login == null) {
            Assert.fail("Login Initialization Failed");
        }

        UserConfig config = getConfigNew();
        if (config != null) {
            login.login(config.TraderName, config.TraderPass);
          /*   String loginErrMsg = login.checkExistsLoginAlertMsg();

           if (loginErrMsg != null) {
                Assert.fail("An error occurred during login. Error Msg: " + loginErrMsg);
            }*/

            // Check login success
            Map.Entry<Boolean, String> checkLoginSuccessResp = login.checkLoginSuccess();
            /*if (!checkLoginSuccessResp.getKey()) {
                Assert.fail("Login failed. Error Msg: " + checkLoginSuccessResp.getValue());
            }*/

        } else {
            Assert.fail("User config not found");
        }
        LogUtils.info("Login Successful,Set Login Session");
        loginThreadLocal.set(login);
    }

    @AfterClass(alwaysRun = true)
    public void cleanupThreadSafeBrowser() {
        String className = this.getClass().getName();
        try {
            if (DevtoolsListener.devTools != null) {
                DevtoolsListener.devTools.disconnectSession();
                DevtoolsListener.devTools = null;
            }
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                LogUtils.info("Attempting to quit driver: " + driver.hashCode() + " for thread: " + Thread.currentThread().getId());
                driver.quit();
                LogUtils.info("Driver quit for test class: " + className);
            }
        } catch (Exception e) {
            LogUtils.info("Error during driver cleanup: " +className + e.getMessage());
        } finally {
            driverThreadLocal.remove();
            waitThreadLocal.remove();
            configThreadLocal.remove();
            loginThreadLocal.remove();
            factorThreadLocal.remove();
            LogUtils.info("Driver cleanup completed for test class" + className);
        }
    }
    @AfterTest(alwaysRun = true)
    public void QuitDriver() {
        // Close CDP connection
        if (DevtoolsListener.devTools != null) {
            DevtoolsListener.devTools.disconnectSession();
            DevtoolsListener.devTools = null;
            GlobalMethods.printDebugInfo("DevTools session disconnected...");
        }
        // Close WebDriver
    }

    /**
     * 获取当前线程的WebDriver实例
     */
    protected WebDriver getDriverNew() {
        WebDriver driver =  driverThreadLocal.get();
        if (driver == null){
            LogUtils.info("WebDriver instance is null for current thread, attempting to recreate...");
            try {
                driver = createWebDriver("true","chrome");
                driverThreadLocal.set(driver);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                waitThreadLocal.set(wait);

                LogUtils.info("Successfully recreated WebDriver instance for current thread");
            } catch (Exception e) {
                LogUtils.error("Failed to recreate WebDriver instance: ", e);
                return null;
            }
        }
        return driver;
    }

    /**
     * 获取当前线程的WebDriverWait实例
     */
    protected WebDriverWait getWaitNew() {
        return waitThreadLocal.get();
    }

    /**
     * 获取当前线程的配置信息
     */
    protected UserConfig getConfigNew() {
        return configThreadLocal.get();
    }

    /**
     * 获取当前线程的工厂实例
     */
    protected Factor getFactorNew() {
        Factor factor = factorThreadLocal.get();
        if (factor == null) {
            LogUtils.info("Factor instance is null for current thread, attempting to recreate...");
            WebDriver driver = getDriverNew();
            factor = new Factor(TestEnv, Brand, Regulator, driver);
            factorThreadLocal.set(factor);
            }
        return factor;
    }

    /**
     * 获取当前线程的CPLogin对象
     */
    protected CPLogin getLogin() {
        CPLogin login = loginThreadLocal.get();
        checkValidLoginSession(login);
        return loginThreadLocal.get();
    }

    /**
     * 线程安全的元素等待方法
     */
    protected WebElement waitUntilVisibility(By by) {
        WebDriverWait wait = getWaitNew();
        if (wait == null) {
            WebDriver driver = getDriverNew();
            if (driver != null) {
                wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                waitThreadLocal.set(wait);
            }
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement waitUntilClickable(By by) {
        WebDriverWait wait = getWaitNew();
        if (wait == null) {
            WebDriver driver = getDriverNew();
            if (driver != null) {
                wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                waitThreadLocal.set(wait);
            }
        }
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void goToCpHomePage() {
        CPLogin login = getLogin();
        if (login != null) {
            login.goToCpHome();
        } else {
            throw new IllegalStateException("CPLogin not initialized for current thread");
        }
    }

    public static boolean isBrowserOpen() {
        try {
            WebDriver driver = driverThreadLocal.get();
            if (driver == null) {
                return false;
            }
            driver.getTitle();
            return true;
        } catch (WebDriverException e) {
            LogUtils.error("Browser not open", e);
            return false;
        }
    }

    public static boolean isDriverUsable() {
        try {
            WebDriver driver = driverThreadLocal.get();
            if (driver == null) {
                return false;
            }
            driver.getWindowHandle();
            return true;
        } catch (WebDriverException e) {
            LogUtils.error("Driver not usable", e);
            return false;
        }
    }

    public static String getCPURL() {
        return BaseTestCaseNew.CPUrl;
    }

    public WebElement waitUtilVisibility(By by)
    {
        return getWaitNew().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void printInfo() {
        LogUtils.info("*****************TestCase configuration********************");
        formatPrint("TestEnv",BaseTestCaseNew.TestEnv);
        formatPrint("Brand",BaseTestCaseNew.Brand);
        formatPrint("Regulator",BaseTestCaseNew.Regulator);
        formatPrint("TraderName",getConfigNew().TraderName);
        formatPrint("TraderURL",BaseTestCaseNew.TraderURL);
        formatPrint("AdminURL",BaseTestCaseNew.AdminURL);
        formatPrint("AdminName",getConfigNew().AdminName);
        formatPrint("Server",serverName);

    }

    private void formatPrint(String name, String value) {
        LogUtils.info(String.format("%-12s: %s", name, value));
    }

    public void checkValidLoginSession(CPLogin cpLogin) {
        // Reinit login session when connection is reset unexpectedly
        if (cpLogin == null) {
            GlobalMethods.printDebugInfo("Connection interrupted or reset. Reinit Login...");
            login();
            return;
        }
        try {
            WebDriver driver = getDriverNew();
            driver.navigate().refresh();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//            List<WebElement> elements = wait.until(webDriver ->
//                    webDriver.findElements(By.xpath("//li[@data-testid='menu.home' or @data-testid='/home']"))
//            );
            WebElement element = wait.until(webDriver ->
                    webDriver.findElement(By.xpath("//li[@data-testid='menu.home' or @data-testid='/home'] | //*[@id=\"home\"]"))
            );
            LogUtils.info("Is login home page: " + element);
            boolean isLoginPage = element.isDisplayed();
            if (!isLoginPage) {
                LogUtils.info("Reinit Login...");
                login();
            } else {
                LogUtils.info("Not Need Reinit Login");
            }

        }catch (TimeoutException e){
            LogUtils.error("Timeout during login session check: " , e);
            Assert.fail("Timeout during login session check");
        }
        catch (Exception e) {
            LogUtils.error("Error during login session check: " , e);
            Assert.fail("Error during login session check");
        }
    }

    protected void clearLoginSession() {
        WebDriver driver = getDriverNew();
        if (driver == null) {
            LogUtils.info("No driver found. Skip to proceed to clear login session");
            return;
        }

        LogUtils.info("Start clearing login session");

        // Clear cookies
        try {
            driver.manage().deleteAllCookies();
            LogUtils.info("Cookies deleted ");
        } catch (Exception e) {
            LogUtils.error("Failed to delete cookies", e);
        }

        // Clear storage
        String script = """
        try {
           if (location.origin !== 'null') {
              window.localStorage.clear();
              window.sessionStorage.clear();
           }
        } catch (e) {
           console.warn('Unable to clear storage:', e);
        }
        """;

        ((JavascriptExecutor) driver).executeScript(script);

        // Short wait to avoid race condition
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        // Clear the login-specific ThreadLocal
        loginThreadLocal.remove();

        LogUtils.info("Login session cleared completely");
    }

    protected void logout() {

        try {
            WebElement profilePanel =  getDriverNew().findElement(By.xpath("//span[@class='ht-popover profile-panel-popover']"));
            profilePanel.click();
        } catch (WebDriverException e) {
            System.err.println("Profile Panel does not exist");
        }

        try {
            WebElement logoutBtn = getDriverNew().findElement(By.xpath("//*[@alt='icon_logout']"));
            logoutBtn.click();
        } catch (WebDriverException e) {
            System.err.println("Logout button does not exist");
        }

    }

    protected WebDriver createWebDriver(String headless,String browserName) {
        try {
            //该方法里面driver就是new的，所以传null也可以
            WebDriver driver = Utils.funcSetupDriver(null, browserName, headless);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            if (Objects.equals(headless, "true")) {
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
            }

            /*if (GlobalProperties.isWeb) {
                DevtoolsListener.setUpDevTools(driver);
            } else {
                DevtoolsListener.setUpDevToolsWeb(driver, false);
            }
*/
            MyWebDriverListener listener = new MyWebDriverListener();
            driver = new EventFiringDecorator(listener).decorate(driver);

            return driver;
        } catch (Exception e) {
            LogUtils.error("Failed to create WebDriver: ", e);
            throw new RuntimeException("Cannot create WebDriver instance", e);
        }
    }


    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        // Delete temporarily download folder
        if(GlobalProperties.isWeb) {
            DownloadFile.deleteTempFolder();
        }
    }
}
