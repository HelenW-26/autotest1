package newcrm.testcases;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import newcrm.business.businessbase.CPLogin;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.MyWebDriverListener;
import newcrm.utils.download.DownloadFile;
import utils.Listeners.DevtoolsListener;
import utils.LogUtils;
import vantagecrm.Utils;

public  class BaseTestCase {
	protected static WebDriver driver;
	protected WebDriverWait wait;
	protected static Factor myfactor;
	protected static String TestEnv;
	protected static String Brand;
	protected static String Regulator;
	protected static String TraderURL;
	protected static String TraderName;
	protected static String TraderPass;
	protected static String AdminURL;
	protected static String AdminName;
	protected static String AdminPass;
	
	protected static String serverName = "";
	
	protected static CPLogin login;
	
	protected static ENV dbenv = ENV.ALPHA;
	protected static BRAND dbBrand;
	protected static REGULATOR dbRegulator;

	protected static String prevExecuteCPAccEmail = "";
	protected static String prevExecuteAPAccEmail = "";
	protected static String prevExecuteIBAccEmail = "";
	protected static String prevExecutePlatform = "";

	/***
	 * launchBrowser and cp login before run the class
	 * 默认的是在每个class启动之前重启浏览器，测试有特殊需求需要覆盖这个方法
	 * @param TestEnv alphar or prod
	 * @param headless true or false
	 * @param Brand VT,PUG, VFX
	 * @param Regulator 
	 * @param TraderURL
	 * @param TraderName
	 * @param TraderPass
	 * @param context
	 */
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug","Server"})
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
				              ITestContext context) throws Exception {
		if(!"".equals(server)) {
			serverName = server;
		}
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}

	@Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug"})
	public void launchBrowser(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
				              ITestContext context) {
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
		BaseTestCase.prevExecuteAPAccEmail = "";
		BaseTestCase.prevExecuteIBAccEmail = "";

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

		if (driver == null || !isBrowserOpen()) {

			// Create temporary download folder
			if(GlobalProperties.isWeb) {
				DownloadFile.createTempFolder();
			}

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

			//使用带有监听器的driver
			MyWebDriverListener ls = new MyWebDriverListener();
			driver = new EventFiringDecorator(ls).decorate(driver);

			utils.Listeners.TestListener.driver=this.driver;
			context.setAttribute("driver", driver);
			myfactor = new Factor(TestEnv,Brand,Regulator,driver);
			login();
			//goToCpHomePage();
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
			boolean isDifferentLoginURL = BaseTestCase.prevExecutePlatform != "CP";
			boolean isDifferentLoginUser = !BaseTestCase.prevExecuteCPAccEmail.equalsIgnoreCase(TraderName);

			// Create login session when:-
			// 1. Logon to portal without Login Page, occurs when register new client via simulator / prod client registration site
			// 2. Redirect from different URL
			// 3. Different user login credentials
			if (login == null) {
				LogUtils.info("Create login session...");
				clearLoginSession();
				myfactor = new Factor(TestEnv,Brand,Regulator,driver);
				login();
			} else if (isDifferentLoginURL) {
				LogUtils.info("Different login URL, create login session...");
				login();
			} else if (isDifferentLoginUser) {
				LogUtils.info("Different User, create login session...");
				clearLoginSession();
				login();
			} else {
				// Continue with login session when next event occurs within the same site and same user login credentials
				LogUtils.info("Continue with current login session...");
			}
		}

		BaseTestCase.prevExecutePlatform = "CP";
		BaseTestCase.prevExecuteCPAccEmail = TraderName;
	}

	protected void login() {
		login = myfactor.newInstance(CPLogin.class,TraderURL);

		if (login == null) {
			Assert.fail("Login Initialization Failed");
		}

		// Login
		login.login(TraderName, TraderPass);

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

    protected void logout() {

        try {
            WebElement profilePanel = driver.findElement(By.xpath("//*[@class='ht-popover profile-panel-popover' or contains(@class,'login_inner el-dropdown-selfdefine')]"));
            profilePanel.click();
        } catch (WebDriverException e) {
            System.err.println("Profile Panel does not exist");
        }

        try {
            WebElement logoutBtn = driver.findElement(By.xpath("//*[@alt='icon_logout' or @data-testid='logout' or contains(@class,'logout')]"));
            logoutBtn.click();
        } catch (WebDriverException e) {
            System.err.println("Logout button does not exist, unable to logout");
        }

    }

	protected void clearLoginSession() {
		if (driver == null) return;

		LogUtils.info("Clearing login session...");

		// Clear cookies
		try {
			driver.manage().deleteAllCookies();
		} catch (Exception e) {
			LogUtils.info("Failed to delete cookies: " + e.getMessage());
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
		} catch (InterruptedException ignored) {}

		LogUtils.info("Login session cleared.");
	}

	@AfterTest(alwaysRun = true)
	public void QuitDriver() {
		// Close CDP connection
		if (DevtoolsListener.devTools != null) {
			DevtoolsListener.devTools.disconnectSession();
			DevtoolsListener.devTools = null;
			LogUtils.info("DevTools session disconnected...");
		}

		// Close WebDriver
		if (driver != null) {
			driver.quit();
			driver = null;
			LogUtils.info("WebDriver quit...");
		}
	}

	@AfterSuite(alwaysRun = true)
	public void tearDownSuite() {
		// Delete temporarily download folder
		if(GlobalProperties.isWeb) {
			DownloadFile.deleteTempFolder();
		}
	}

	public static boolean isBrowserOpen() {
		try {
			if (driver == null) return false;

			driver.getTitle(); // simple check
			return true;
		} catch (WebDriverException e) {
			System.err.println("Browser not open");
			return false;
		}
	}

	public static boolean isDriverUsable() {
		try {
			driver.getWindowHandle(); // Throws if window is closed
			return true;
		} catch (WebDriverException e) {
			System.err.println("Driver not usable");
			return false;
		}
	}

	public void goToCpHomePage() {
		login.goToCpHome();
		LogUtils.info("Before method: go to cp home page");
	}

	public void checkValidLoginSession() {
		// Reinit login session when connection is reset unexpectedly
		if (login == null) {
			LogUtils.info("Connection interrupted or reset. Reinit Login...");
			login();
		}
	}
	
	public void printInfo() {
		System.out.println("*****************TestCase configuration********************");
		formatPrint("TestEnv",BaseTestCase.TestEnv);
		formatPrint("Brand",BaseTestCase.Brand);
		formatPrint("Regulator",BaseTestCase.Regulator);
		formatPrint("TraderName",BaseTestCase.TraderName);
		//formatPrint("TraderPass",BaseTestCase.TraderPass);
		formatPrint("TraderURL",BaseTestCase.TraderURL);
		formatPrint("AdminURL",BaseTestCase.AdminURL);
		formatPrint("AdminName",BaseTestCase.AdminName);
		formatPrint("Server",serverName);
		//formatPrint("AdminPass",BaseTestCase.AdminPass);
		
	}
	private void formatPrint(String name, String value) {
		System.out.printf("%-12s: %s\n", name,value);
	}

	public WebElement waitUtilVisibility(By by)
	{
		return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public WebElement waitUtilClickable(By by)
	{
		return wait.until(ExpectedConditions.elementToBeClickable(by));
	}

	public static String getCPURL()
	{
		return BaseTestCase.TraderURL ;
	}

}
