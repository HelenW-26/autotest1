package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import clientBase.navigationBar;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * This class is to test the new Client Portal Register
 */

public class CPRegister {

	WebDriver driver;
	WebDriverWait wait50;
	WebDriverWait wait15;
	WebDriverWait wait03;
	navigationBar naviB;
	Wait<WebDriver> wait00;
	Random r = new Random();

	String userID;
	String fName = Utils.webUserPrefix + Utils.randomString(4).toLowerCase();

	Map<String, String> personalInfo;
	Map<String, String> accountInfo;
	String[] currencyList = {"USD","GBP","CAD","AUD","EUR","SGD","NZD","HKD","JPY"};
	String[] acctTypePUG = {"Standard","Prime","Islamic Standard","Islamic Prime", "Cent", "Islamic Cent"};
	String accountType = "";
	String platform = "";
	
	//Shufti ID/POA Audit test parameters
	String streetNumber="64-1号";//64-1号";大芬村
	String address = "东圃镇塔林镇山尾";// "Downing st"; "东圃镇塔林镇山尾";布吉镇
	String suburb = "莆田市秀屿区";// "Westminister";"莆田市秀屿区";深圳市龙岗区
	String state = "福建省";//"London"; "福建省";广东省
	
	String bYear="1982";//"1982";//"1986";
	String bMonth="10";
	String bDay="10";
	
	String fileFirstName="chakthip";//"chakthip";//"John";
	String fileLastName= "sothornsak";//"sothornsak";//"Livone";
	String fileCountry="United Kingdom";
	String fileType="Driver License";//"Driver License"; //"National ID Card";
	String fileNumber="61039234";//"090909090";//"61039234";//"A123456";
	
	//currency/Account Type = "", will select randomly
	String currency = "USD";
	String acctType = "Standard";
	
	//String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png"; //Passport.png
	//String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back.png";
	String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
	String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back.png";
	String filePOA = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
	
	boolean isShufti = true;
	 
	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{

		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();

		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.prompt_for_download", true);
		options.setExperimentalOption("prefs", prefs);
		if(Boolean.valueOf(headless)) {
			options.addArguments("window-size=1920,1080");
			options.addArguments("headless");
		}
		driver = new ChromeDriver(options);

		context.setAttribute("driver", driver);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		wait03 = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait15 = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait50 = new WebDriverWait(driver, Duration.ofSeconds(50));

		wait00 = new FluentWait<WebDriver>(driver)							
				.withTimeout(Duration.ofSeconds(15))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(Exception.class);
		
		personalInfo = new HashMap<String, String>();
		accountInfo = new HashMap<String, String>();
		
		
		
	}

	@AfterClass(alwaysRun=true)
	public void closedriver() {
		driver.quit();
	}
	
	@Test(priority = 0)
	@Parameters(value = { "TraderURL", "TraderName", "TraderPass", "Brand" })
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception

	{
		naviB = new navigationBar(driver, Brand);
		driver.get(TraderURL);
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'HOME')]")));

	}

	@Test(groups="sanity",invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterUserMT4(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String strPlatform = "MT4";
		boolean uploadFile = true;
		System.out.println(TestEnv);
		System.out.println(Brand);
		System.out.println(RegisterURL);
		funcCPRegisterClient(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform, uploadFile);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterUserMT4NoUpload(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String strPlatform = "MT4";
		boolean uploadFile = false;
		funcCPRegisterClient(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform, uploadFile);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterUserMT5NoUpload(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {

		String strPlatform = "MT5";
		boolean uploadFile = false;
		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "vt":
		case "regulator2":
			funcCPRegisterClient(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform, uploadFile);
			break;

		case "fsa":
		case "svg":
		default:
			System.out.println(Brand + " doesn't support MT5 User Registration");
			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "IBpromotion" })

	public void RegisterDemoMT4(String TestEnv, String Brand, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String fName = "TestcrmDemo" + Utils.randomString(4).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = Utils.randomNumber(2), phNumber = Utils.randomNumber(8);
		String accountType = Utils.AccountT.getRandom().toString();
		String currency = Utils.Currency.getRandom().toString();
		String strPlatform = "MT4";

		//Yanni on 28/01/2021: PUG & VT now support multiple currencies, so comment it out.
		/*
		 * if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg") || Brand.equalsIgnoreCase("vt")) {
		 * currency = "USD";
		 * }
		 */

		// Navigate to Entry Page
		funcGo2DemoEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion);

		// The registration page for Demo
		funcCPRegisterDemo(fName, lName, phCountryCode, phNumber, email, strPlatform, accountType, currency, TestEnv,
				Brand);
		// Check DB status
		System.out.println("\n\n****Please check user type should be DEMO ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "IBpromotion" })

	public void RegisterDemoMT5(String TestEnv, String Brand, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String fName = "TestcrmDemo" + Utils.randomString(4).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = Utils.randomNumber(2), phNumber = Utils.randomNumber(8);
		String accountType = Utils.AccountT.getRandom().toString();
		String currency = Utils.Currency.getRandom().toString();
		String strPlatform = "MT5";

		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "vt":
		case "fsa":
		case "svg":
		case "regulator2":
			// Navigate to Entry Page
			funcGo2DemoEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion);

			// The registration page for Demo
			funcCPRegisterDemo(fName, lName, phCountryCode, phNumber, email, strPlatform, accountType, currency,
					TestEnv, Brand);
			// Check DB status
			System.out.println("\n\n****Please check user type should be DEMO ****\n");
			DBUtils.checkDBStatus(fName, TestEnv, Brand);
			break;


		default:
			System.out.println(Brand + " doesn't support MT5 Demo User Registration");
			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterDemo2LeadP1(String TestEnv, String Brand, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String fName = "Demo" + Utils.randomString(4).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = Utils.randomNumber(2), phNumber = Utils.randomNumber(8);
		String accountType = Utils.AccountT.getRandom().toString();
		String currency = Utils.Currency.getRandom().toString();
		String strPlatform = "MT4";
		String password;
		int numberOfEmail = 3;
		Boolean isBack = false;

		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			currency = "USD";
		}

		// Navigate to Entry Page
		funcGo2DemoEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion);

		// The registration page for Demo
		funcCPRegisterDemo(fName, lName, phCountryCode, phNumber, email, strPlatform, accountType, currency, TestEnv,
				Brand);
		// Check DB status
		System.out.println("\n\n****Please check user type should be DEMO ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		Utils.funcLogOutCP(driver,Brand);

		// Get CP login password from email
		password = funcCheckEmailtoGetPassword(Brand, TestEnv, 1);

		Utils.funcLogInCP(driver, email, password, Brand);

		// In ClientPortal, click "Register" menu to go to RegisterPage
		funcNavigate2Register();

		// Type should be Demo
		System.out.println("\n\n****Please check user type should be DEMO ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		// Type should be Leads
		System.out.println("\n****Please check user type should be LEADS ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Check Email:
		Thread.sleep(2000);
		System.out.println();
		System.out.println("Check Email Sent Records: ");
//		Utils.readEmail(email, Brand, TestEnv, numberOfEmail);
		DBUtils.readEmailvUserName(fName, Brand, TestEnv, numberOfEmail);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterLeads(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {

		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		String countryName = "France";

		Map<String, String> countryInfo = new HashMap<String, String>();

		if (Brand.equalsIgnoreCase("vfsc"))
			countryName = "Thailand";		
		
		String strPlatform = "MT4";
		Boolean isBack = false;
		int numberOfEmail = 2;

		// Navigate to Entry Page
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		// Input Entry Page and Submit

		/*
		 * input email
		 * input phCountryCode and phNumber
		 */
		countryInfo = funcClientEntryPg(fName, lName, countryName, phNumber, email, TestEnv, Brand, IBpromotion);

		if (countryInfo.isEmpty()) {
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		} else {
			phCountryCode = countryInfo.get("code");
		}

		// Should be Leads
		System.out.println("\n\n****Please check user type should be LEADS at page 1****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// In ClientPortal, click "Register" menu to go to RegisterPage
		funcNavigate2Register();

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		// Should be Leads
		System.out.println("\n\n****Please check user type should be LEADS at page 2****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);

		// Should be Leads
		System.out.println("\n\n****Please check user type should be LEADS at page 3****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		funcClientPg3(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);

		// Should be Leads
		System.out.println("\n\n****Please check user type should be LEADS at page 4****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		System.out.println("There should have 2 emails sent for new registered leads:");
		System.out.println("\n 1. Client Portal Access \n 2. New Leads Emails ");
		DBUtils.readEmailvUserName(fName, Brand, TestEnv, numberOfEmail);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterMT4Leads2Client(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String strPlatform = "MT4";
		funcCPRegisterLeads2Client(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion, strPlatform);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterMT5Leads2Client(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {

		String strPlatform = "MT5";
		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "vt":
		case "regulator2":
			funcCPRegisterLeads2Client(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion, strPlatform);
			break;

		case "fsa":
		case "svg":
		default:
			System.out.println(Brand + " doesn't support MT5 User Registration");
			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterMT4Demo2MT4Client(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String strPlatform = "MT4";
		funcCPRegisterDemo2Client(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion, strPlatform, strPlatform);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterMT4Demo2MT5Client(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String demoPlatform = "MT4";
		String clientPlatform = "MT5";
		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "regulator2":
			funcCPRegisterDemo2Client(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion, demoPlatform,
					clientPlatform);
			break;

		case "vt":
		case "fsa":
		case "svg":
		default:
			System.out.println(Brand + " doesn't support MT5 User Registration");
			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterMT5Demo2MT4Client(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String demoPlatform = "MT5";
		String clientPlatform = "MT4";
		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "vt":
		case "regulator2":
			funcCPRegisterDemo2Client(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion, demoPlatform,
					clientPlatform);
			break;

		case "fsa":
		case "svg":
		default:
			System.out.println(Brand + " doesn't support MT5 User Registration");
			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterMT5Demo2MT5Client(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
		String strPlatform = "MT5";
		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "vt":
		case "regulator2":
			funcCPRegisterDemo2Client(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion, strPlatform, strPlatform);
			break;

		case "fsa":
		case "svg":
		default:
			System.out.println(Brand + " doesn't support MT5 User Registration");
			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "Brand", "TestEnv", "WebSiteURL", "RegisterURL","IBpromotion" })
	public void RegisterClientMT4ForAutoAudit(String AdminURL, String AdminName, String AdminPass, String Brand,String TestEnv, String WebSiteURL, String RegisterURL, @Optional("") String IBpromotion) throws Exception {
		String strPlatform = "MT4";
		boolean uploadFile = true;
		SoftAssert softAssert = new SoftAssert();
		funcCPRegisterForAutoAudit(AdminURL, AdminName, AdminPass, Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion,strPlatform, uploadFile);
		softAssert.assertAll();
		
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "Brand", "TestEnv", "WebSiteURL", "RegisterURL","IBpromotion" })
	public void RegisterClientMT5ForAutoAudit(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, String WebSiteURL, String RegisterURL, @Optional("") String IBpromotion) throws Exception {
		SoftAssert softAssert = new SoftAssert();
		String strPlatform = "MT5";
		boolean uploadFile = true;
	
		funcCPRegisterForAutoAudit(AdminURL, AdminName, AdminPass, Brand, TestEnv, WebSiteURL, RegisterURL,IBpromotion, strPlatform, uploadFile);
		softAssert.assertAll();

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "TraderURL", "IBpromotion" })
	void CheckDuplicatedEmail(String TestEnv, String Brand, String WebSiteURL, String RegisterURL, String TraderURL,
			String IBpromotion) throws Exception {
		String dupItem = "email";
		funcDupClientEmailMobile(TestEnv, Brand, WebSiteURL, RegisterURL, TraderURL, IBpromotion, dupItem);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "TraderURL", "IBpromotion" })
	void CheckDuplicatedMobile(String TestEnv, String Brand, String WebSiteURL, String RegisterURL, String TraderURL,
			String IBpromotion) throws Exception {
		String dupItem = "mobile";
		funcDupClientEmailMobile(TestEnv, Brand, WebSiteURL, RegisterURL, TraderURL, IBpromotion, dupItem);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "TraderURL", "IBpromotion" })
	void CheckDuplicatedDemoEmail(String TestEnv, String Brand, String WebSiteURL, String RegisterURL, String TraderURL,
			String IBpromotion) throws Exception {
		String dupItem = "email";
		funcDupDemoEmailMobile(TestEnv, Brand, WebSiteURL, RegisterURL, TraderURL, IBpromotion, dupItem);

	}

	void funcCPRegisterDemo2Client(String TestEnv, String Brand, String WebSiteURL, String RegisterURL,
			String IBpromotion, String demoPlatform, String clientPlatform) throws Exception {

		String fName = "Demo" + Utils.randomString(4).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		String accountType = Utils.AccountT.getRandom().toString();
		String currency = Utils.Currency.getRandom().toString();
		String password;
		int numberOfEmail = 3;
		Map<String, String> countryInfo = new HashMap<String, String>();

		Boolean isBack = false;

		// Navigate to Entry Page
		funcGo2DemoEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion);

		// The registration page for Demo
		countryInfo = funcCPRegisterDemo(fName, lName, phCountryCode, phNumber, email, demoPlatform, accountType,
				currency, TestEnv, Brand);
		phCountryCode = countryInfo.get("code");
		// Check DB status
		System.out.println("\n\n****Please check user type should be DEMO ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Verify in CP and logout CP
		Utils.funcLogOutCP(driver,Brand);

		// Get CP login password from email
		password = funcCheckEmailtoGetPassword(Brand, TestEnv, 1);

		Utils.funcLogInCP(driver, email, password, Brand);

		// Type should be Demo
		System.out.println("\n\n****Please check user type should be DEMO ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// In ClientPortal, click "Register" menu to go to RegisterPage
		funcNavigate2Register();

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);

		System.out.println("\n\n****Please check user type should be LEADS at page 1****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		System.out.println("\n\n****Please check user type should be LEADS at page 2****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);
		System.out.println("\n\n****Please check user type should be LEADS at page 3****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		funcClientPg3(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);
		System.out.println("\n\n****Please check user type should be LEADS at page 4****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Account Configuration
		funcClientPg4(fName, TestEnv, clientPlatform, Brand);

		clickPgNextBackButton(Brand, isBack);

		System.out.println("\n\n****Please check user type should be LEADS at page 5****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Upload ID & POA
		funcClientPg5(fName, lName, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		// Successful Page
		funcClientPg6(fName, TestEnv, Brand);

		System.out.println("\n\n****Please check user type should be Client at page 6****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Check Email:
		Thread.sleep(2000);
		System.out.println();
		System.out.println("Check Email Sent Records: ");
		Utils.readEmail(email, Brand, TestEnv, numberOfEmail);
	}

	void funcCPRegisterLeads2Client(String TestEnv, String Brand, String WebSiteURL, String RegisterURL,
			String IBpromotion, String strPlatform) throws Exception {

		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		Map<String, String> countryInfo = new HashMap<String, String>();
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		String password, countryName = "";
		int numberOfEmail = 3;

		Boolean isBack = false;

		// Navigate to Entry Page
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		// Input Entry Page and Submit

		/*
		 * input email
		 * input phCountryCode and phNumber
		 */
		countryInfo = funcClientEntryPg(fName, lName, countryName, phNumber, email, TestEnv, Brand, IBpromotion);

		if (countryInfo.isEmpty()) {
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		} else {
			phCountryCode = countryInfo.get("code");
		}

		// Logout CP
		Utils.funcLogOutCP(driver,Brand);

		// Get CP login password from email
		password = funcCheckEmailtoGetPassword(Brand, TestEnv, 1);

		Utils.funcLogInCP(driver, email, password, Brand);

		// In ClientPortal, click "Register" menu to go to RegisterPage
		funcNavigate2Register();

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);

		System.out.println("\n\n****Please check user type should be LEADS at page 1****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		System.out.println("\n\n****Please check user type should be LEADS at page 2****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);
		System.out.println("\n\n****Please check user type should be LEADS at page 3****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		funcClientPg3(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);
		System.out.println("\n\n****Please check user type should be LEADS at page 4****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Account Configuration
		funcClientPg4(fName, TestEnv, strPlatform, Brand);

		clickPgNextBackButton(Brand, isBack);

		System.out.println("\n\n****Please check user type should be LEADS at page 5****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Upload ID & POA
		funcClientPg5(fName, lName, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		// Successful Page
		funcClientPg6(fName, TestEnv, Brand);

		System.out.println("\n\n****Please check user type should be Client at page 6****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Check Email:
		Thread.sleep(2000);
		System.out.println();
		System.out.println("Check Email Sent Records: ");
		Utils.readEmail(email, Brand, TestEnv, numberOfEmail);
	}

	void funcCPRegisterClient(String Brand, String TestEnv, String WebSiteURL, String RegisterURL, String IBpromotion,
			String strPlatform, boolean uploadFile) throws Exception {

		// String fName="Webt"+Utils.randomString(3).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		//String email = fName + Utils.emailSuffix + "." + Brand;
		String email = fName + Utils.emailSuffix;
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		String countryName = "France";
		int numberOfEmail = 6; // Yanni: Need to revise when requirement is finally decided. Most Email expected: Client Portal Access, Live Account, ID3 Pass, Worldcheck pass
		Boolean isBack = false;

		Map<String, String> countryInfo = new HashMap<String, String>();

		if (Brand.equalsIgnoreCase("vfsc"))
			countryName = "Thailand";		
		
		// Navigate to Entry Page
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		// Input Entry Page and Submit

		/*
		 * input email
		 * input Country and phNumber
		 * return Country and Phone code
		 */
		countryInfo = funcClientEntryPg(fName, lName, countryName, phNumber, email, TestEnv, Brand, IBpromotion);

		if (countryInfo.isEmpty()) {
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		} else {
			phCountryCode = countryInfo.get("code");
		}

		// VerifyClientPortal Page and switch language if language is not English

		// In ClientPortal, click "Register" menu to go to RegisterPage
		// funcNavigate2Register();

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);
		// Check DB
		System.out.println();
		System.out.println("After Page1 Submission....");
		userID = DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand);
		// Click Next
		clickPgNextBackButton(Brand, isBack);
		// Check DB
		System.out.println();
		System.out.println("After Page2 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Page3
		funcClientPg3(fName, TestEnv, Brand);

		// Update DB set worldcheck flag = 1
		//funcUpdateWldCkFlag(userID, true, TestEnv, Brand);

		// Update DB set ID3 flag = 1
		//funcUpdateID3Flag(userID, true, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);
		// Check DB
		System.out.println();
		System.out.println("After Page3 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);


		// Page4

		// Set worldcheck to 1
		/*
		 * funcUpdateWldCkFlag(userID, true, TestEnv, Brand);
		 * 
		 * // Set ID3 to 1
		 * funcUpdateID3Flag(userID, true, TestEnv, Brand);
		 */

		// Account Configuration
		funcClientPg4(fName, TestEnv, strPlatform, Brand);
		// Next
		clickPgNextBackButton(Brand, isBack);
		// Check DB
		System.out.println();
		System.out.println("After Page4 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		Thread.sleep(2000);
		// Page5
		// Upload ID & POA
		if (uploadFile)
			funcClientPg5(fName, lName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);

		// Check DB
		System.out.println();
		System.out.println("After Page5 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// If Page5 is active
		/*
		 * try
		 * {
		 * if(driver.findElement(By.cssSelector("div.tab_item:nth-child(4)")).getAttribute("class").contains("active")) {
		 * 
		 * //Next
		 * clickPgNextBackButton(Brand, isBack);
		 * 
		 * //Check DB
		 * System.out.println();
		 * System.out.println("After Page5 Submission....");
		 * DBUtils.checkDBStatus(fName, TestEnv, Brand);
		 * }else
		 * {
		 * System.out.println("Page5 is hidden.");
		 * }
		 * 
		 * } catch (NoSuchElementException e)
		 * {
		 * System.out.println("Page5 is hidden.");
		 * }
		 */
		// Successful Page
		if (!Brand.equalsIgnoreCase("au"))
			funcClientPg6(fName, TestEnv, Brand);
		Thread.sleep(2000);
		// Check DB
		System.out.println();
		System.out.println("Page 6, check ID record in DB...");
		System.out.println("When ID3CheckStatus = 1, status = Completed; ID3CheckStatus = 0, status = Submitted.");
		funcGetIDRecord(userID, TestEnv, Brand);  // Need to comment out when ID function is ready

		System.out.println();
		System.out.println("Page 6, check World Check record in DB...");
		System.out.println(
				"DB query should return at least 1 record. WorldCheckStatus =1, status = Completed; WorldCheckStatus = 0, status = Submitted.");
		funcGetWorldCheck(userID, TestEnv, Brand);         // Need to comment out when ID function is ready

		System.out.println();
		System.out.println("Page 6, check POA record in DB...");
		System.out.println(
				"When ID3CheckStatus = 1 and WorldCheckStatus = 1, DB query returns nothing. Otherwise, return 1 record with status = Submitted.");
		funcGetPOARecord(userID, TestEnv, Brand);

		/*
		 * System.out.println();
		 * System.out.println("Page 6, check tb_user_outer for needAddressProof flag");
		 * NewTaskManagement.funcGetIDPOAFlag(Brand, TestEnv, email, NewTaskManagement.IDPOAType.AddrP);
		 */

		System.out.println();
		System.out.println("Page6, check Email Records...");
		System.out.println("\n There should have 4 emails after user completes registration:");
		System.out.println(
				"1) 2*Important Documentation Relating to Your Account\n2) Client Portal Access\n3) New Leads email Sales");

		System.out.println("\nWhen Account is automatically audited, 2 emails should be sent:");
		System.out.println("1) New Open Account Notification(to sales)");
		System.out.println("2) New MT4/5 Live Account");

		// Check Email
		// Utils.readEmail(email.toLowerCase(), Brand, TestEnv, numberOfEmail);
		DBUtils.readEmailvUserName(fName, Brand, TestEnv, numberOfEmail);

		// Print password
		funcCheckEmailtoGetPassword(Brand, TestEnv, 1);
	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "TraderURL", "IBpromotion" })
	public void ValidateRegisterUser(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			String TraderURL, String IBpromotion) throws Exception {
		/*
		 * Personal Info Keys
		 * mName, Nationality="",acc_title="",mName="",cfEmail="",phoneCode,idType="",idNumber="",usCitizen="",dob,birthMonth, birthYear="",IBurl="";
		 * street="",suburb="",state="",postcode="",emStatus="",
		 * acc_income="",acc_invest="",acc_invest_deposit="",source_fund="",acc_industry="",
		 * acc_investmentexp_security="",acc_investmentexp_derivative="",acc_investmentexp_levfx="",acc_investmentexp_vol="",
		 * acc_investmentexp_tradew="",acc_investmentexp_amount_tradew="";
		 */
		String strPlatform = "MT4";
		// String fName="Webt"+Utils.randomString(3).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		Map<String, String> countryInfo = new HashMap<String, String>();
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		boolean isBack = false;
		String selectSql;

		// Navigate to Entry Page (added by Yanni)
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		/*
		 * input email
		 * input phCountryCode and phNumber
		 */
		countryInfo = funcClientEntryPg(fName, lName, phCountryCode, phNumber, email, TestEnv, Brand, IBpromotion);

		if (countryInfo.isEmpty()) {
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		} else {
			phCountryCode = countryInfo.get("code");
		}
		// In ClientPortal, click "Register" menu to go to RegisterPage
		funcNavigate2Register();

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);
		if (!Brand.equalsIgnoreCase("au"))
			personalInfo.put("acc_title", driver.findElement(By.id("title")).getText());
		personalInfo.put("mName", driver.findElement(By.id("middleName")).getAttribute("value"));
		personalInfo.put("Nationality", driver.findElement(By.id("nationalityId")).getAttribute("value"));
		personalInfo.put("phoneCode", driver.findElement(By.id("phoneCode")).getAttribute("value"));
		personalInfo.put("dob", driver.findElement(By.id("dob")).getAttribute("value"));
		personalInfo.put("birthMonth",
				driver.findElement(By.xpath(".//input[@placeholder='Month']")).getAttribute("value"));
		personalInfo.put("birthYear",
				driver.findElement(By.xpath(".//input[@placeholder='Year']")).getAttribute("value"));
		if (!Brand.equalsIgnoreCase("au")) {
			personalInfo.put("idType", driver.findElement(By.id("idType")).getAttribute("value"));
			personalInfo.put("idNumber", driver.findElement(By.id("idNumber")).getAttribute("value"));
		}
		personalInfo.put("referer", "Automation");

		Utils.funcTakeScreenShot(driver, "P1" + Brand);
		// Next
		clickPgNextBackButton(Brand, isBack);
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand);

		personalInfo.put("countryName", driver.findElement(By.id("countryCode")).getAttribute("value"));
		// Street Number & name
		personalInfo.put("address", driver.findElement(By.id("address")).getAttribute("value"));
		// Suburb
		personalInfo.put("suburb", driver.findElement(By.id("suburb")).getAttribute("value"));
		// State
		personalInfo.put("state", driver.findElement(By.id("state")).getAttribute("value"));
		// Postcode
		personalInfo.put("postcode", driver.findElement(By.id("postcode")).getAttribute("value"));

		// usCitizen
		if (!Brand.equalsIgnoreCase("au"))
			personalInfo.put("usCitizen", driver.findElement(By.xpath("//label[@aria-checked='true']")).getText());

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");
		Utils.funcTakeScreenShot(driver, "P2-1 " + Brand);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000);");
		Utils.funcTakeScreenShot(driver, "P2-2 " + Brand);

		clickPgNextBackButton(Brand, isBack);
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Input Page3 - Employment and financial details
		funcClientPg3(fName, TestEnv, Brand);

		List<WebElement> radioButton = driver.findElements(By.xpath("//label[@aria-checked='true']"));
		// Employment Status
		personalInfo.put("emStatus", radioButton.get(0).getText());
		// Annual Income
		personalInfo.put("acc_income", radioButton.get(1).getText());
		// Saving and Investment
		personalInfo.put("acc_invest", radioButton.get(2).getText());
		personalInfo.put("acc_invest_deposit", radioButton.get(3).getText());
		// Source of Funds
		personalInfo.put("source_fund", radioButton.get(4).getText());
		if (!Brand.equalsIgnoreCase("au")) {
			// trade per week?
			personalInfo.put("acc_investmentexp_tradew", radioButton.get(5).getText());
			// amount trade per week?
			personalInfo.put("acc_investmentexp_amount_tradew", radioButton.get(6).getText());
		}

		clickPgNextBackButton(Brand, isBack);
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Account Configuration
		funcClientPg4(fName, TestEnv, strPlatform, Brand);
		Utils.funcTakeScreenShot(driver, "P4" + Brand);
		clickPgNextBackButton(Brand, isBack);
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		// Upload ID & POA
		funcClientPg5(fName, lName, TestEnv, Brand);

		isBack = true;

		// Click BACK twice to verify Page 3 info
		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);
		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);

		// Employment and financial details:
		radioButton = driver.findElements(By.xpath("//label[@aria-checked='true']"));

		if (radioButton.size() == 7 || radioButton.size() == 5) {
			Utils.funcIsStringEquals(radioButton.get(0).getText(), personalInfo.get("emStatus"), Brand);
			// Annual Income
			Utils.funcIsStringEquals(radioButton.get(1).getText(), personalInfo.get("acc_income"), Brand);
			// Saving and Investment
			Utils.funcIsStringEquals(radioButton.get(2).getText(), personalInfo.get("acc_invest"), Brand);
			Utils.funcIsStringEquals(radioButton.get(3).getText(), personalInfo.get("acc_invest_deposit"), Brand);
			// Source of Funds
			Utils.funcIsStringEquals(radioButton.get(4).getText(), personalInfo.get("source_fund"), Brand);
			if (!Brand.equalsIgnoreCase("au")) {
				// trade per week
				Utils.funcIsStringEquals(radioButton.get(5).getText(), personalInfo.get("acc_investmentexp_tradew"),
						Brand);
				// amount trade per week
				Utils.funcIsStringEquals(radioButton.get(6).getText(),
						personalInfo.get("acc_investmentexp_amount_tradew"), Brand);
			}
		} else {
			Assert.assertTrue(false, "Expected 7 or 5 elements selected but only " + radioButton.size() + " selected");
		}

		// click back button to page 2 address
		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);

		// Country of main residence
		Utils.funcIsStringEquals(driver.findElement(By.id("countryCode")).getAttribute("value"),
				personalInfo.get("countryName"), Brand);
		// Street Number & name
		Utils.funcIsStringEquals(driver.findElement(By.id("address")).getAttribute("value"),
				personalInfo.get("address"), Brand);
		// Suburb
		Utils.funcIsStringEquals(driver.findElement(By.id("suburb")).getAttribute("value"), personalInfo.get("suburb"),
				Brand);
		// State
		Utils.funcIsStringEquals(driver.findElement(By.id("state")).getAttribute("value"), personalInfo.get("state"),
				Brand);
		// Postcode
		Utils.funcIsStringEquals(driver.findElement(By.id("postcode")).getAttribute("value"),
				personalInfo.get("postcode"), Brand);
		// usCitizen
		if (!Brand.equalsIgnoreCase("au"))
			Utils.funcIsStringEquals(driver.findElement(By.xpath("//label[@aria-checked='true']")).getText(),
					personalInfo.get("usCitizen"), Brand);

		// Click BACK again and return to page 1
		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);

		// Verify Page 1: Personal Details
		if (!Brand.equalsIgnoreCase("au"))
			Utils.funcIsStringEquals(driver.findElement(By.id("title")).getText(), personalInfo.get("acc_title"),
					Brand);
		// First Name
		Utils.funcIsStringEquals(driver.findElement(By.id("firstName")).getAttribute("value"), fName, Brand);
		// Middle Name
		Utils.funcIsStringEquals(driver.findElement(By.id("middleName")).getAttribute("value"),
				personalInfo.get("mName"), Brand);
		// Last Name
		Utils.funcIsStringEquals(driver.findElement(By.id("lastName")).getAttribute("value"), lName, Brand);
		// Nationality
		Utils.funcIsStringEquals(driver.findElement(By.id("nationalityId")).getAttribute("value"),
				personalInfo.get("Nationality"), Brand);
		// Confirm Email
		Utils.funcIsStringEquals(driver.findElement(By.id("email")).getAttribute("value").toLowerCase(),
				email.toLowerCase(), Brand);
		// Date of Birth
		Utils.funcIsStringEquals(driver.findElement(By.id("dob")).getAttribute("value"), personalInfo.get("dob"),
				Brand);
		Utils.funcIsStringEquals(driver.findElement(By.xpath(".//input[@placeholder='Month']")).getAttribute("value"),
				personalInfo.get("birthMonth"), Brand);
		Utils.funcIsStringEquals(driver.findElement(By.xpath(".//input[@placeholder='Year']")).getAttribute("value"),
				personalInfo.get("birthYear"), Brand);
		if (!Brand.equalsIgnoreCase("au")) {
			// Identification types
			Utils.funcIsStringEquals(driver.findElement(By.id("idType")).getText(), personalInfo.get("idType"), Brand);
			// Identification No.
			Utils.funcIsStringEquals(driver.findElement(By.id("acc_id_num")).getAttribute("idNum"),
					personalInfo.get("idNum"), Brand);
		}

		// Click Next four times to complete register
		isBack = false;
		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);

		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);

		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(500);

		clickPgNextBackButton(Brand, isBack);
		Thread.sleep(1000);

		// Upload ID & POA
		funcClientPg5(fName, lName, TestEnv, Brand);

		Thread.sleep(1000);
		Utils.funcTakeScreenShot(driver, "P4-1 " + Brand);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000);");
		Utils.funcTakeScreenShot(driver, "P4-2 " + Brand);

		clickPgNextBackButton(Brand, false);
		Thread.sleep(1000);

		// Verify the URL and title
		Utils.funcIsStringContains(Utils.ParseInputURL(driver.getCurrentUrl()), Utils.ParseInputURL(TraderURL), Brand);

		driver.findElement(By.xpath("//a[@href='/home']")).click();
		Thread.sleep(1000);

		// If VT, switch the default language from Chinese to English
		if (Brand.equals("vt")) {
			// You need to switch twice when switching from Chinese to English. It should be an online bug.

			if (driver.findElement(By.cssSelector("div#language_title span:nth-of-type(1)")).getAttribute("class")
					.equals("zh_CN")) {
				driver.findElement(By.id("language_title")).click();
				wait03.until(
						ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#language_box_item_span")))
						.click();
				wait03.until(ExpectedConditions.attributeToBe(By.cssSelector("div#language_title span:nth-of-type(1)"),
						"class", "en_US"));
			}
		}
		Thread.sleep(1000);
		if (driver.getTitle().equals("Secure Client Portal")) {
			Utils.funcIsStringEquals(driver.findElement(By.cssSelector("h3")).getText(), "LIVE ACCOUNTS", Brand);
		} else {
			Utils.funcIsStringEquals(driver.findElement(By.cssSelector("h1.title_u")).getText(), "LIVE ACCOUNTS",
					Brand);
		}

		if (Brand.equalsIgnoreCase("au")) {
			if (driver.getTitle().equals("Secure Client Portal")) {
				Utils.funcIsStringEquals(driver.findElement(By.cssSelector("div.header > span")).getText().trim(),
						"ASIC Questionnaire Incomplete", Brand);
			} else {
				Utils.funcIsStringEquals(driver.findElement(By.xpath("//div[5]//a[1]//p[1]")).getText().trim(),
						"ASIC Questionnaire Incomplete", Brand);
			}
		}

		System.out.println("***************************");
		System.out.println("The user registered is " + email);
		System.out.println("***************************");

		// Pass fName to Utils string so Audit function can get the username
		Utils.registerUserName = fName + " " + lName;

		Thread.sleep(1000);
		Utils.funcTakeScreenShot(driver, "P5 " + Brand);

		selectSql = "select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name like '%"
				+ fName + "%';";

		String userID[] = DBUtils.readDB(selectSql, TestEnv, Brand);

		if (!userID[0].equals(userID[1]) && !userID[0].equals(userID[2])) {
			Assert.assertTrue(false, "Global User ID should be same with one in either ASIC or CIMA DB.");
		}
	}

	//@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}

	void funcNavigate2Register() throws Exception {

		// Switch to English
		funcSwitchLanguage();

		Thread.sleep(1000);

		driver.findElement(By.xpath("//span[contains(text(),'CONTINUE APPLICATION')]")).click();
		Thread.sleep(1000);

		// driver.findElement(By.xpath("//span[contains(text(),'註冊')]")).click();
	}

	void funcSwitchLanguage() throws Exception {
		WebDriverWait wait02 = new WebDriverWait(driver, Duration.ofSeconds(2));
		// Thread.sleep(2000);
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(@class,'flag')]/div/img")));
		driver.findElement(By.xpath("//li[contains(@class,'flag')]/div/img")).click();
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'English')]")))
				.click();
		Thread.sleep(1000);
	}
	void funcSwitchPUGLanguage() throws Exception {
		WebDriverWait wait02 = new WebDriverWait(driver, Duration.ofSeconds(2));
		
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".langSelect > img:nth-child(2)")));
		WebElement element = driver.findElement(By.cssSelector(".langSelect > img:nth-child(2)"));
	
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
		Thread.sleep(2000);

		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(.,'English')]")));
		driver.findElement(By.xpath("//span[contains(.,'English')]")).click();
		Thread.sleep(1000);
	}

	void funcDropdownSelectRandomOption() throws Exception {
		List<WebElement> liItems;
		int j;
		// Get all li items which has Nationality listed
		liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		j = r.nextInt(liItems.size() - 1) + 1;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
		liItems.get(j).click();
		Thread.sleep(500);
	}
	
	void funcDropdownSelect(String option) throws Exception {
		List<WebElement> liItems;
		
		// Get all li items 
		liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		for (int j=0; j<liItems.size(); j++)
		{
			if (liItems.get(j).getText().equalsIgnoreCase(option)) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
				liItems.get(j).click();
				break;
			}
		}
		
		Thread.sleep(500);
	}

	
	void funcCountryDropdownSelect(String option) throws Exception {
		List<WebElement> liItems;
		
		// Get all country items 
		liItems = driver.findElements(By.cssSelector("div.results_option"));
		
		if (!option.equals("")) {
			for (int j=0; j<liItems.size(); j++)
			{
				if (liItems.get(j).getText().equalsIgnoreCase(option)) {
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
					liItems.get(j).click();
					break;
				}
			}
		}else {
			liItems.get(r.nextInt(liItems.size()-1)+1).click();
		}
		
		Thread.sleep(500);
	}
	void funcClientPg1(String fName, String lName, String phCountryCode, String phNumber, String TestEnv, String Brand)
			throws Exception {

		String valueTxt, tempTxt;
		
		WebElement txtControl;
		
		if (TestEnv.equalsIgnoreCase("alpha"))
		{
			if(driver.findElements(By.id("proceed-button")).size()>0)
			{
				driver.findElements(By.id("proceed-button")).get(0).click();
			}
		
		}

		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
		{
			funcSwitchPUGLanguage();
		}
		else if (Brand.equalsIgnoreCase("vt")) {
			funcSwitchLanguage(); // switch to English otherwise PUG
			Thread.sleep(1000);
		}

		Thread.sleep(3000);

		if (Brand.equalsIgnoreCase("vt")) {
			String pageTitle = driver.findElement(By.cssSelector("strong")).getText();
			Assert.assertTrue(pageTitle.equals("Personal details"),
					"Page Title is " + pageTitle + " Expected is: Personal details");

		} else {
			//Yanni: currently PUG PROD
			String pageTitle = driver.findElement(By.cssSelector("h3")).getText();
			Assert.assertTrue(pageTitle.equals("PERSONAL DETAILS"),
					"Page Title is " + pageTitle + " Expected is: PERSONAL DETAILS");
		}

		// Run steps only when tab is active
		if (driver.findElement(By.cssSelector("div."+getActiveElement(Brand)+":nth-child(1)")).getAttribute("class").contains("active")) {

			// Input Personal details

			// Input title: ASIC doesn't have title field
			switch (Brand) {
			case "ky":
			case "vt":
			case "fsa":
			case "svg":
			case "vfsc":
			case "vfsc2":
			case "fca":
			case "regulator2":
				driver.findElement(By.id("title")).click();
				Thread.sleep(500);
				funcDropdownSelectRandomOption();
				break;
			case "au":
			default:
				break;

			}

			// Verify First Name is brought in as expected
			Thread.sleep(500);
			txtControl = driver.findElement(By.id("firstName"));
			funcVerifyBringInValue(txtControl, fName);

			// Input Middle Name
			driver.findElement(By.id("middleName")).clear();
			driver.findElement(By.id("middleName")).sendKeys("MName");

			// Verify Last Name is brought in as expected
			txtControl = driver.findElement(By.id("lastName"));
			funcVerifyBringInValue(txtControl, lName);

			// Input the Nationality
			driver.findElement(By.id("nationalityId")).click();
			Thread.sleep(500);
			funcDropdownSelectRandomOption();

			// Check email value is brought in correctly
			txtControl = driver.findElement(By.id("email"));
			valueTxt = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", txtControl).toString();
			tempTxt = fName + Utils.emailSuffix;
			tempTxt = tempTxt.toLowerCase();
			Assert.assertTrue(valueTxt.equalsIgnoreCase(tempTxt),
					"Email brought in from Entry page is wrong. It is " + valueTxt + ". It should be " + tempTxt);

			// Verify phoneCode is brought in as expected
			if (!Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg")) {
				txtControl = driver.findElement(By.id("phoneCode"));

				// Yanni: This part has a bug and comment out this check temporarily as it takes time to fix.
				funcVerifyBringInValue(txtControl, phCountryCode);
			}

			// Input the phoneCode
			driver.findElement(By.id("phoneCode")).click();
			Thread.sleep(500);
			funcDropdownSelectRandomOption();

			// Verify phoneNumber is brought in as expected
			txtControl = driver.findElement(By.id("mobile"));
			funcVerifyBringInValue(txtControl, phNumber);

			// Input PhoneNumber
			driver.findElement(By.id("mobile")).clear();
			driver.findElement(By.id("mobile")).sendKeys(Utils.randomNumber(8));

			// Select birth year
			driver.findElement(By.xpath(".//input[@placeholder='Year']")).click();
			Thread.sleep(500);
			funcDropdownSelectRandomOption();

			// Select birth month
			driver.findElement(By.xpath(".//input[@placeholder='Month']")).click();
			Thread.sleep(500);
			funcDropdownSelectRandomOption();
			
			// Select birth day
			switch (Brand.toLowerCase()) {
				case "vt":
					driver.findElement(By.xpath("//input[@placeholder='Day']")).click();
					Thread.sleep(500);
					funcDropdownSelectRandomOption();
					break;
					
				default:
					driver.findElement(By.id("dob")).click();
					Thread.sleep(500);
					funcDropdownSelectRandomOption();	
			}

			if (!Brand.equalsIgnoreCase("au")) {
				// Input the placeOfBirth
				driver.findElement(By.id("placeOfBirth")).click();
				Thread.sleep(500);
				funcDropdownSelectRandomOption();

				// Select Identification Type
				driver.findElement(By.id("idType")).click();
				Thread.sleep(500);
				funcDropdownSelectRandomOption();

				// Input Identification Number
				driver.findElement(By.id("idNumber")).sendKeys(Utils.randomNumber(6) + Utils.randomString(2));

			}

			// Check the reference
			WebElement terms = driver.findElement(By.xpath("//span[@class='el-checkbox__inner']"));

			if (Brand.equalsIgnoreCase("vt")) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", terms);
			}

			terms.click();
			Thread.sleep(500);

			// Input referrer
			if (Brand.equalsIgnoreCase("vt")) {

				driver.findElement(By.xpath("//div[@class='el-form-item checkbox']//input[@class='el-input__inner']"))
						.sendKeys("Automation");
			} else {

				driver.findElement(By.xpath("//li[@class='pd_checkbox']//input[@class='el-input__inner']"))
						.sendKeys("Automation");
			}
		}
	}
	
	String getActiveElement(String Brand) {
		String cssSlctActive;
		//PUG changed to step instead of tab
		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			cssSlctActive="el-step";
		}else {
			cssSlctActive="tab_item";
		}
		
		return cssSlctActive;
	}
	void funcClientPg2(String fName, String TestEnv, String Brand) throws Exception {

		String cssSlctActive;
		// Run steps only when tab is active
		
		if (driver.findElement(By.cssSelector("div."+getActiveElement(Brand)+":nth-child(1)")).getAttribute("class").contains("active")) {

			String countryName;

			// MAIN RESIDENTIAL ADDRESS
			// Input the countryCode
			// Yanni: comment out country selection as country can always be brought from Entry Page.

			/*
			 * if(!Brand.equalsIgnoreCase("pug")){
			 * 
			 * driver.findElement(By.id("countryCode")).click();
			 * Thread.sleep(500);
			 * 
			 * if(Brand.equalsIgnoreCase("au")){
			 * funcSetCountry2AU();
			 * }else if(Brand.equalsIgnoreCase("ky")){
			 * // funcDropdownSelectRandomOption(); temporary comment out
			 * //funcSetCountry2UK(); //temporary use: to set Country of main residence as UK to test ID3 and WorldCheck result
			 * }else
			 * {
			 * funcDropdownSelectRandomOption();
			 * }
			 * }
			 */

			countryName = ((JavascriptExecutor) driver)
					.executeScript("return arguments[0].value", driver.findElement(By.id("countryCode"))).toString();

			System.out.println("Country Name is: " + countryName);

			// Yanni: to reflect UK address format change.
			// Input the address。 When country Name = "United Kingdom", need to input Street Number and Street Name seperately
			
			if (countryName.equalsIgnoreCase("United Kingdom") || countryName.equalsIgnoreCase("France")) {
				driver.findElement(By.id("streetNumber")).sendKeys(streetNumber);
				driver.findElement(By.id("address")).sendKeys(address);
			} else {
				driver.findElement(By.id("address")).sendKeys(streetNumber + " " + address);
			}

			// Input the state. When country = Australia, state will be a dropdown list.
			if (countryName.equalsIgnoreCase("Australia")) {

				driver.findElement(By.id("state")).click();
				funcDropdownSelectRandomOption();

			} else {
				driver.findElement(By.id("state")).sendKeys(state);
			}
			// Input the suburb
			driver.findElement(By.id("suburb")).sendKeys(suburb);

			// Input the postcode
			driver.findElement(By.id("postcode")).sendKeys("2000");

			if (Brand.equalsIgnoreCase("fca")) {
				funcFCAAddress(countryName);
			}

		}
	}

	void funcFCAAddress(String countryName) throws Exception {

		String previousCountryName = "";

		if (countryName.equalsIgnoreCase("United Kingdom")) {
			driver.findElement(By.id("nationalInsuranceNumber")).sendKeys("QQ123456C");
		}

		driver.findElement(By.id("taxResidencyCountryId")).click();
		Thread.sleep(500);
		// funcDropdownSelectRandomOption();
		funcSetCountry2UK();

		int yearsAtAddress = r.nextInt(3);

		driver.findElement(By.id("yearsAtAddress")).clear();
		driver.findElement(By.id("yearsAtAddress")).sendKeys(String.valueOf(yearsAtAddress));

		if (yearsAtAddress <= 3) {
			driver.findElement(By.id("previousCountryId")).click();
			Thread.sleep(500);
			funcDropdownSelectRandomOption();

			previousCountryName = driver.findElement(By.id("previousCountryId")).getAttribute("value");

			if (previousCountryName.equalsIgnoreCase("United Kingdom")
					|| previousCountryName.equalsIgnoreCase("France")) {
				driver.findElement(By.id("previousStreetNumber")).sendKeys("666");
				driver.findElement(By.id("previousAddress")).sendKeys("Oxford Street");
			} else {
				driver.findElement(By.id("previousAddress")).sendKeys("10 Oxford Street");
			}

			driver.findElement(By.id("previousState")).sendKeys("London");
			driver.findElement(By.id("previousSuburb")).sendKeys("London");
			driver.findElement(By.id("previousPostcode")).sendKeys("100010");
		}
	}

	void funcClientPg3(String fName, String TestEnv, String Brand) throws Exception {
		Thread.sleep(5000);
		// Run steps only when tab is active
		if (driver.findElement(By.cssSelector("div."+getActiveElement(Brand)+":nth-child(2)")).getAttribute("class").contains("active")) {
			// EMPLOYMENT AND FINANCIAL DETAILS

			int j = 0;
			if (Brand.equalsIgnoreCase("au")) {
				// Employment Status
				j = r.nextInt(4) + 1;
				driver.findElement(By.xpath("//ul[1]//li[1]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Estimated Annual Income
				j = r.nextInt(3) + 1;
				driver.findElement(By.xpath("//ul[1]//li[2]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Estimated Savings and Investments
				j = r.nextInt(4) + 1;
				driver.findElement(By.xpath("//ul[1]//li[3]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Source of Funds
				j = r.nextInt(4) + 1;
				driver.findElement(By.xpath("//ul[1]//li[4]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Industry
				j = r.nextInt(20) + 1;
				driver.findElement(By.xpath("//ul[1]//li[5]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();
			} else if (Brand.equalsIgnoreCase("vt")) {
				//Yanni: new field Occupation is added and can only be displayed when the 1st/2nd option of first question is selected. 
				// Employment Status
				//j = r.nextInt(5) + 1;
				j=1;   //Let j=1 to display occupation
				driver.findElement(By.xpath("//ul//li[1]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

				//Occupation
				j = r.nextInt(11) + 1;
				driver.findElement(By.xpath("//ul//li[2]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

				// Estimated Annual Income
				j = r.nextInt(5) + 1;
				driver.findElement(By.xpath("//ul//li[3]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

				// Estimated Savings and Investments
				j = r.nextInt(8) + 1;
				driver.findElement(By.xpath("//ul/li[4]/div[1]/div[1]/div[1]/label[" + j + "]")).click();

				// Estimated Intended Deposit
				j = r.nextInt(7) + 1;
				driver.findElement(By.xpath("//ul//li[5]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

				// Source of Funds
				j = r.nextInt(11) + 1;
				driver.findElement(By.xpath("//ul//li[6]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

				// Utils.funcTakeScreenShot(driver, "P3-1"+Brand);

				// Number Of Trades Per Week
				j = r.nextInt(4) + 1;
				driver.findElement(By.xpath("//ul[2]/li[1]/div[1]/div[1]/div[1]/label[" + j + "]")).click();

				// Trading Amount Per Week
				j = r.nextInt(4) + 1;
				driver.findElement(By.xpath("//ul[2]/li[2]/div[1]/div[1]/div[1]/label[" + j + "]")).click();

				// Utils.funcTakeScreenShot(driver, "P3-2"+Brand);
			} else if (Brand.equalsIgnoreCase("fca")) {
				funcFCAEmpFin();
			} else {
				// Employment Status
				j = r.nextInt(5) + 1;
				driver.findElement(By.xpath("//ul[1]//li[1]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Estimated Annual Income
				j = r.nextInt(5) + 1;
				driver.findElement(By.xpath("//ul[1]//li[2]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Estimated Savings and Investments
				j = r.nextInt(8) + 1;
				driver.findElement(By.xpath("//ul[1]//li[3]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Estimated Intended Deposit
				j = r.nextInt(7) + 1;
				driver.findElement(By.xpath("//ul[1]//li[4]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Source of Funds
				j = r.nextInt(11) + 1;
				WebElement webElement = driver
						.findElement(By.xpath("//ul[1]//li[5]//div[@role='radiogroup']//label[" + j + "]"));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
				webElement.click();

				// Utils.funcTakeScreenShot(driver, "P3-1"+Brand);

				// Number Of Trades Per Week
				j = r.nextInt(4) + 1;
				driver.findElement(
						By.xpath("//ul[@class='clearfix']//li[1]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Trading Amount Per Week
				j = r.nextInt(4) + 1;
				driver.findElement(
						By.xpath("//ul[@class='clearfix']//li[2]//div[1]//div[1]//div[2]//div[1]//label[" + j + "]"))
						.click();

				// Utils.funcTakeScreenShot(driver, "P3-2"+Brand);
			}

		}
	}

	void funcFCAEmpFin() {
		int j;

		// Employment Status
		j = r.nextInt(5) + 1;
		driver.findElement(By.xpath("//ul//li[1]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

		// Occupation
		j = r.nextInt(27) + 1;
		WebElement webElement = driver.findElement(By.xpath("//ul//li[2]//div[1]//div[1]//div[1]//label[" + j + "]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();

		// Employment Sector
		j = r.nextInt(20) + 1;
		webElement = driver.findElement(By.xpath("//ul//li[3]//div[1]//div[1]//div[1]//label[" + j + "]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();

		// Annual Income
		j = r.nextInt(6) + 1;
		driver.findElement(By.xpath("//ul//li[4]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

		// Estimated Size of Investment Portfolio
		j = r.nextInt(6) + 1;
		driver.findElement(By.xpath("//ul//li[5]//div[1]//div[1]//div[1]//label[" + j + "]")).click();

		// Utils.funcTakeScreenShot(driver, "P3-1"+Brand);

		// How Do You Intend to Fund Your Account?
		j = r.nextInt(8) + 1;
		webElement = driver.findElement(By.xpath("//div/ul/li/div/div/div/div[2]/div/label[" + j + "]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();

		// What Type of Securities Do You Intend to Trade?
		j = r.nextInt(4) + 1;
		driver.findElement(By.xpath("//div/ul/li[2]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// Expected Initial Deposit into Investment Account
		j = r.nextInt(6) + 1;
		driver.findElement(By.xpath("//div/ul/li[3]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// Expected Average Daily Trade Value
		j = r.nextInt(6) + 1;
		driver.findElement(By.xpath("//div/ul/li[4]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// Shares
		j = r.nextInt(3) + 1;
		webElement = driver.findElement(By.xpath("//div/ul/li[5]/div/div/div/div[2]/div/label[" + j + "]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();

		// Spot FX
		j = r.nextInt(4) + 1;
		driver.findElement(By.xpath("//div/ul/li[6]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// Equity derivatives
		j = r.nextInt(4) + 1;
		driver.findElement(By.xpath("//div/ul/li[7]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// FX CFDs
		j = r.nextInt(4) + 1;
		driver.findElement(By.xpath("//div/ul/li[8]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// Have you worked in a financial services institution in the last 12 months
		j = r.nextInt(2) + 1;
		driver.findElement(By.xpath("//div/ul/li[9]/div/div/div/div[2]/div/label[" + j + "]")).click();

		// Have you received education or on-the-job training
		j = r.nextInt(4) + 1;
		driver.findElement(By.xpath("//div/ul/li[10]/div/div/div/div[2]/div/label[" + j + "]")).click();

	}

	void funcClientPg4(String fName, String TestEnv, String strPlatform, String Brand) throws Exception {
		
		// Run steps only when tab is active
		if (driver.findElement(By.cssSelector("div."+getActiveElement(Brand)+":nth-child(3)")).getAttribute("class").contains("active")) {

			// ACCOUNT CONFIGURATION
			// Choose Trading Platform: MT4 or MT5 (MT4)
			List<WebElement> liItems = null;
			int j = 0;

			List<WebElement> lstElement = null;
			if (Brand.equalsIgnoreCase("vt")) {
				//lstElement = driver.findElements(By.xpath("//main[@id='elMain']//li[1]//div[1]//div[1]//ul[1]//li[1]//img"));
				lstElement = driver.findElements(By.cssSelector("div.row.tradingPlatform div.backgroung > img"));
			} else if (Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg"))
			{
				lstElement = driver.findElements(By.xpath("//div[@id='register']/div/div/div/div/div[2]/div/div/form/div/div/div/div/ul/li"));
			}
			else {
				// lstElement=driver.findElements(By.xpath("//section[@class='el-container is-vertical']//li[1]//ul[1]//div//li")); ul.accountConfiguration
				lstElement = driver.findElements(By.cssSelector("ul.accountConfiguration:nth-of-type(1) img"));
			}

			Assert.assertTrue(lstElement.size() >= 1, "No Available Platform to choose.");

			switch (strPlatform) {
			case "MT4":
				lstElement.get(0).click();
				break;

			case "MT5":
				lstElement.get(1).click();
				break;

			default:
				lstElement.get(0).click();

			}
			platform = strPlatform;
			// Get all account types listed and choose random one
			if (!Brand.equalsIgnoreCase("vt")) {
				if(!Brand.equalsIgnoreCase("fsa")&&!Brand.equalsIgnoreCase("svg"))
				{
					liItems = driver
							.findElements(By.xpath("//section[@class='el-container is-vertical']//li[2]//ul[1]//div//li"));
					j = r.nextInt(liItems.size());
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
	
					// liItems.get(j).click();
					//liItems.get(j).click();
					liItems.get(j).click();
					System.out.println("Account Type choice is: " + j);
				} else
				{
					liItems = driver.findElements(By.xpath("//div[2]/div/div/div/ul/li"));
					//*****************0=Standard,1=Prime,2=Islamic Standard,3=Islamic Prime, 4=Cent, 5=Islamic Cent***********************//
					if((Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg")) && !acctType.isEmpty()) {
						for (int i=0; i<acctTypePUG.length; i++) {
							if  (acctTypePUG[i].equalsIgnoreCase(acctType)) {
								j=i;
								break;
							}
						}
						
					} else {
						j = r.nextInt(liItems.size());
					}
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
	
					liItems.get(j).click();
					liItems.get(j).click();
					//liItems.get(3).click();
					System.out.println("Account Type choice is: " + j);
				}

			} else {
				try {
					//liItems = driver.findElements(By.xpath("//li[2]//div[1]//div[1]//ul[1]//li//img"));
					liItems = driver.findElements(By.cssSelector("div.row.accountType div.backgroung > img"));
					
					j = r.nextInt(liItems.size());

					liItems.get(j).click();
					System.out.println("Account Type choice is: " + j);

				} catch (Exception e) {
					System.out.println("*****No need to select account type!*****");
				}

			}
			Thread.sleep(500);
			accountType = liItems.get(j).getText().trim();
			// Select USD as currency for PUG， Randome selection for AU & VT
			if (Brand.equalsIgnoreCase("vt")) {
				liItems = driver.findElements(
						By.cssSelector("div.shadow_inner > img"));

			} else if (Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg")) {
				liItems = driver.findElements(By.xpath("//div[@id='register']/div/div/div/div/div[2]/div/div/form/div[3]/div/div/div/ul/li"));
			}
			else {
				// Get all the currency listed and choose random one
				liItems = driver.findElements(By.xpath("//section[@class='el-container is-vertical']//li[3]//ul[1]//div//li"));
			}

			if (liItems.size() >= 1) {
				//********PUG: 0=USD,1=GBP,2=CAD,3=AUD，4=EUR，5=SGD,6=NZD,7=HKD,8=JPY*********//
				if((Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg")) && !currency.isEmpty()) {
					if (acctType.toLowerCase().contains("cent")) {
						j=0; // currency only USC 
					} else {
						for (int i=0; i<currencyList.length; i++) {
							if  (currencyList[i].equalsIgnoreCase(currency)) {
								j=i;
								break;
							}
						}
					}
				} else {
					j = r.nextInt(liItems.size());
				}
				
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
				/*
				 * if(Brand.equalsIgnoreCase("pug")) {
				 * liItems.get(0).click();
				 * }else {
				 */
				liItems.get(j).click();
				// liItems.get(4).click();
				/* } */
			} else {
				System.out.println("No Currency is listed.");
			}
			Thread.sleep(500);

			// Check on the agreement
			if (Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg"))
				driver.findElement(By.xpath("//span[@class='el-checkbox__inner']")).click();
			else
				driver.findElement(By.id("checkbox")).click();

		} else {
			System.out.println("Page 4 not active.");
		}
	}

	void funcClientPg5(String fName,  String lName, String TestEnv, String Brand) throws Exception {

		try {

			wait15.until(ExpectedConditions.attributeContains(
					driver.findElement(By.cssSelector("div."+getActiveElement(Brand)+":nth-child(4)")), "class", "active"));
			// Run steps only when tab is active
			if (driver.findElement(By.cssSelector("div."+getActiveElement(Brand)+":nth-child(4)")).getAttribute("class")
					.contains("active")) {

				if(!Brand.equals("au")) {
					// UPLOAD YOUR ID & POA
					//for (int k = 0; k < 2; k++) {
						// Upload files
						driver.findElements(By.xpath("//input[@type='file']")).get(0).sendKeys(Utils.workingDir + fileFront);
						Thread.sleep(500);
						
						driver.findElements(By.xpath("//input[@type='file']")).get(0).sendKeys(Utils.workingDir + fileBack);
						Thread.sleep(500);
						
						//driver.findElements(By.xpath("//input[@type='file']")).get(0).sendKeys(Utils.workingDir + fileFront);
						Thread.sleep(500);
						
						driver.findElements(By.xpath("//input[@type='file']")).get(1).sendKeys(Utils.workingDir + filePOA);
						Thread.sleep(500);
						
						driver.findElements(By.xpath("//input[@type='file']")).get(1).sendKeys(Utils.workingDir + filePOA);
						Thread.sleep(500);
				}
				


					//Upload 2nd ID file
					/*
					 * if (k == 0) {
					 * driver.findElements(By.xpath("//input[@type='file']")).get(k)
					 * .sendKeys(Utils.workingDir + fileBack);
					 * Thread.sleep(500);
					 * }
					 */

				//}
				
				//Input shufti info
				switch (Brand.toLowerCase()) {
				case "vt":
				case "fsa":
				case "svg":
					driver.findElement(By.xpath("//input[@data-testid='firstName']")).sendKeys(fileFirstName);
					//driver.findElement(By.id("middleName")).sendKeys(fileLastName);
					driver.findElement(By.xpath("//input[@data-testid='lastName']")).sendKeys(fileLastName);
					
					//file country not verified by Shufti for now
					/*
					driver.findElement(By.id("countryCode")).click();
					Thread.sleep(500);
					funcDropdownSelect(fileCountry);
					*/
					driver.findElement(By.id("idType")).sendKeys(Keys.ENTER);
					Thread.sleep(5000);
					funcDropdownSelect(fileType);
					driver.findElement(By.id("idNumber")).clear();
					driver.findElement(By.id("idNumber")).sendKeys(fileNumber);
					Thread.sleep(5000);
					break;
				case "vfsc":
				case "vfsc2":
					driver.findElement(By.xpath("//input[@data-testid='firstName']")).sendKeys(fileFirstName);
					//driver.findElement(By.id("middleName")).sendKeys(fileLastName);
					driver.findElement(By.xpath("//input[@data-testid='lastName']")).sendKeys(fileLastName);
					
					//file country not verified by Shufti for now
					/*
					driver.findElement(By.id("countryCode")).click();
					Thread.sleep(500);
					funcDropdownSelect(fileCountry);
					*/

					driver.findElement(By.xpath("//div[@data-testid='idType']/div/input")).sendKeys(Keys.ENTER);
					Thread.sleep(500);
					funcDropdownSelect(fileType);
					Thread.sleep(500);
					driver.findElement(By.xpath("//input[@data-testid='idNumber']")).clear();
					Thread.sleep(500);
					driver.findElement(By.xpath("//input[@data-testid='idNumber']")).sendKeys(fileNumber);
					Thread.sleep(500);
					break;
				
				case "au":
					driver.findElement(By.id("greenid-option-list-toggle")).click();
					Thread.sleep(500);
					driver.findElement(By.xpath("//a[contains(text(),'Save & complete later')]")).click();
					Thread.sleep(500);
					driver.findElement(By.xpath("//button[@id='greenid-save-and-complete-later']")).click();
					break;
				default:
						System.out.println("No Shufti in Brand "+Brand);
				
				}
				
			} else
				System.out.println("Page5 is not active.");
		} catch (NoSuchElementException e) {
			System.out.println("Page5 is not visible.");
		}
	}

	void funcClientPg6(String fName, String TestEnv, String Brand) throws Exception {

		String message = "";
		Thread.sleep(500);
		// Verify Successful Page

		// verify page contains 'thank you'
		switch (Brand) {
		case "au":
		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":
			try {
				message = driver.findElement(By.xpath(".//span[contains(text(), 'thank you')]")).getText();
				System.out.println(message);
			} catch (NoSuchElementException e) {
				System.out.println("Thank You message can't be found.");
			}
			break;

		case "fsa":
		case "svg":
		case "vt":
			try {
				message = driver.findElement(By.xpath(".//span[contains(text(), 'thank you')]")).getText();
				System.out.println(message);
			} catch (NoSuchElementException e) {
				System.out.println("Thank You message can't be found.");
			}

			break;

		default:
			System.out.println(Brand + " is not supported.");
		}

		// Verify FUND NOW button can be clicked and navigated to Deposit page
		try {
			driver.findElement(By.cssSelector("div.content div.btn_box a.el-button.btn_blue")).click();
		} catch (Exception e) {
			System.out.println("FUND NOW button is not visible.");
		}
	}

	void funcGo2ClientEntry(String Brand, String TestEnv, String WebSiteURL, String RegisterURL, String IBpromotion,
			String strPlatform) throws InterruptedException {

		String IBurl;

		if (Brand.equalsIgnoreCase("fca") && TestEnv.equalsIgnoreCase("prod")) {
			driver.get("https://www.vantagefx.co.uk/");
			wait50.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='cookieIdYes']"))).click();
			
		} else if (!TestEnv.equalsIgnoreCase("prod")) {
			driver.get("https://newvantagefx:147258@new.vantagefx.com");
		} else {
			driver.navigate().to(WebSiteURL);
		}

		// Navigate to Live Account Registration page
		switch (Brand) {
		case "au":
		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":
			try {
				// wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Live Account"))).click();
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href,'/forex-trading/forex-trading-account/')]"))).click();
			} catch (TimeoutException e) {
				System.out.println("No Live Account Menu.");
				assertTrue(false, "No Live Account Menu.");
			}
			break;

		case "vt":

			// Yanni on 1/07/2020: comment out prod ads check as we can use vtmarkets.com for registration. It has no ads.

			/*
			 * if(TestEnv.equalsIgnoreCase("prod")) //vt-beta url has different URL structure
			 * {
			 * 
			 * //Close the online popup window
			 * try
			 * {
			 * wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.close_button"))).click();
			 * }catch (TimeoutException | NoSuchElementException e)
			 * {
			 * System.out.println("No Popup Dialog to handle.");
			 * }
			 * 
			 * //Click LIVE ACCOUNT link:
			 * //driver.findElement(By.cssSelector("div.right a:nth-of-type(2")).click();
			 * 
			 * }else
			 * {
			 */
			if(TestEnv.equalsIgnoreCase("prod")) {
				//close pop up window
				if (!driver.findElement(By.cssSelector("div.deal_announcement_bg")).getAttribute("style").contains("display: none"))
					driver.findElement(By.cssSelector("span.deal_announcement_close")).click();
				System.out.println("No Live Account Menu.");
				
				//switch language to English
				driver.findElement(By.cssSelector("div.new_country_block")).click();
				driver.findElement(By.xpath("//div[contains(@class,'new_country_hide')]//span[contains(text(),'English')]")).click();
				
				try {
					// wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Live Account"))).click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Open a Live Account"))).click();
				} catch (TimeoutException e) {
					System.out.println("No Live Account Menu.");
				}
			}else {
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("LIVE ACCOUNT"))).click();
			}
			/* } */
			break;

		case "fsa":
		case "svg":

			if (TestEnv.equalsIgnoreCase("prod"))
			{

				Thread.sleep(1000);
				// Close the online popup window
				try {
					wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.close_button img"))).click();
					//wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("LIVE ACCOUNT"))).click();
				} catch (TimeoutException | NoSuchElementException e) {
					System.out.println("No Popup Dialog to handle.");
				}
				// Click LIVE ACCOUNT link:
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Live Account"))).click();
				// driver.findElement(By.cssSelector("div.right a:nth-of-type(2")).click();
			} else {
				try {
					// wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Live Account"))).click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("LIVE ACCOUNT"))).click();
				} catch (TimeoutException e) {
					System.out.println("No Live Account Menu.");
				}
			}
			break;
		default:
			System.out.println(Brand + " is not supported.");
			Assert.assertTrue(false, Brand + " is not supported.");
			break;
		}

		try {
			wait15.until(ExpectedConditions.urlContains("/forex-trading-account/"));
		} catch (TimeoutException e) {
			System.out.println("Using " + driver.getCurrentUrl() + " for Live Account Registration.");
		}

		// Refresh with IB promotion

		if (!IBpromotion.equals("")) {
			IBurl = driver.getCurrentUrl() + IBpromotion;
			driver.navigate().to(IBurl);
			wait15.until(ExpectedConditions.urlContains(IBpromotion));
		}

		// Input redirection URL and click the button for TEST/Beta Env
		if (!RegisterURL.equals("")) {
			// the input and change controls in vt-beta are different
			/*
			 * if((Brand.equalsIgnoreCase("pug")) && TestEnv.equalsIgnoreCase("beta"))
			 * {
			 * 
			 * driver.findElement(By.cssSelector("input.change_text")).clear();
			 * driver.findElement(By.cssSelector("input.change_text")).sendKeys(RegisterURL);
			 * driver.findElement(By.cssSelector("button.change_button")).click();
			 * }else
			 * {
			 */
			System.out.println("Register URL is:" + RegisterURL);
			driver.findElement(By.cssSelector("input.change_live")).clear();
			driver.findElement(By.cssSelector("input.change_live")).sendKeys(RegisterURL);
			// To accommodate Alpha's LONG url
			Thread.sleep(2500);
			driver.findElement(By.cssSelector("button#change_button")).click();
			driver.findElement(By.cssSelector("button#change_button")).click();
			Thread.sleep(1000);
			// }

		}

	}

	public Map<String, String> funcCPRegisterDemo(String fName, String lName, String phCountryCode, String phNumber,
			String demoEmail, String Platform, String AccountType, String Currency, String TestEnv, String Brand)
			throws Exception {

		int numberOfEmail = 3;
		Select t;
		int selectedIndex = 0;
		Map<String, String> countryInfo = new HashMap<String, String>();

		// PUG： only 1 page for demo registratio (PROD only); PUG TEST & BETA & Others: 2 pages
		switch (Brand) {

		case "au":
		case "ky":
		case "fca":
		case "vt":
		case "vfsc":
		case "vfsc2":
		case "regulator2":

			// Demo Entry page
			countryInfo = funcDemoEntryPg(fName, lName, phCountryCode, phNumber, demoEmail, TestEnv, Brand);

			// Page2: choose Platform, Account Type and Currency
			funcDemoPage2(TestEnv, Brand, Platform);

			// Click Next
			if (Brand.equalsIgnoreCase("vt") & TestEnv.equalsIgnoreCase("prod")) {

				((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 100);");
				Thread.sleep(2000);
				driver.findElement(By.cssSelector("form#open_demo_form_two button")).click();

			} else {
				driver.findElement(By.id("sub-open")).click();
			}

			break;

		case "fsa":
		case "svg":
			// Demo Entry page. PROD demo has 1 page. Other ENV has 2 pages.
			countryInfo = funcDemoEntryPg(fName, lName, phCountryCode, phNumber, demoEmail, TestEnv, Brand);
			if (!TestEnv.equalsIgnoreCase("prod")) {
				// Page2: choose Platform, Account Type and Currency
				funcDemoPage2(TestEnv, Brand, Platform);
				driver.findElement(By.id("sub-open")).click();
			}

			break;

		default:
			System.out.println(Brand + " is not supported");

		}

		System.out.println("There should have 3 emails sent for new registered Demos:");
		System.out.println("\n 1. Client Portal Access \n 2. New Leads Emails \n 3. New MT4/5 Demo Account");
		Thread.sleep(1000);
		DBUtils.readEmailvUserName(fName, Brand, TestEnv, numberOfEmail);

		return countryInfo;

	}

	void funcGo2DemoEntry(String Brand, String TestEnv, String WebSiteURL, String RegisterURL, String IBpromotion)
			throws Exception {
		String IBurl;


		//Yanni on 28/01/2021: These lines have logic issues. It will result it all other product demo goes to new.vantagefx.com
		/*
		 * if (Brand.equalsIgnoreCase("fca") && TestEnv.equalsIgnoreCase("prod")) {
		 * driver.get("https://vantagfxcouk:3a7c9740@www.vantagefx.co.uk/signin");
		 * } else if(!TestEnv.equalsIgnoreCase("prod"))
		 * {
		 * //driver.navigate().to(WebSiteURL);
		 * driver.get("https://newvantagefx:147258@new.vantagefx.com");
		 * 
		 * }
		 */
		
		
		if(TestEnv.equalsIgnoreCase("prod"))
		{
			if(Brand.equalsIgnoreCase("fca"))
			{
				driver.get("https://vantagfxcouk:3a7c9740@www.vantagefx.co.uk/signin");
			}else if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) 
			{
				// Process WebSiteURL to match PUG. PUG Test Env has different register URL structure. PUG OFFICIAL WEBSITE: DEMO ACCOUNT is a button instead of link
				WebSiteURL = Utils.ParseInputURL(WebSiteURL) + "open-demo-account";
			}else
			{
				driver.navigate().to(WebSiteURL);
			}
			
			
			
			if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				WebSiteURL = Utils.ParseInputURL(WebSiteURL) + "open-demo-account";
			}
			
		}else  //non-prod registration goes to new.vatnagefx.com
		{
			driver.get("https://newvantagefx:147258@new.vantagefx.com");
		}

		// Navigate to Live Account Registration page
		switch (Brand) {
		case "au":
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "regulator2":
			try {
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("DEMO ACCOUNT"))).click();
			} catch (TimeoutException e) {
				System.out.println("No Demo Account Menu.");
			}

			break;

		case "vt":

			try {
				//VT PROD:  Link Text is "Request a Demo". Test: "DEMO ACCOUNT"
				if(TestEnv.equalsIgnoreCase("prod"))
				{
					System.out.println("Request a demo...");
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Request a demo"))).click();
				}else
				{
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("DEMO ACCOUNT"))).click();
				}
				
			} catch (TimeoutException e) {
				System.out.println("No Demo Account Menu.");
			}
			/* } */

			break;

		case "fsa":
		case "svg":
			if(TestEnv.equalsIgnoreCase("prod"))
			{
				System.out.println("Using " + WebSiteURL + " for Demo Registration.");
				driver.navigate().to(WebSiteURL);
				
		  }else
		  {
			  try {
			  // wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Live Account"))).click();
			  wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("DEMO ACCOUNT"))).click();
			  } catch (TimeoutException e) {
			  System.out.println("No Demo Account Menu.");
			  }
		  }
				 
			
			break;

		default:
			// wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Demo Account"))).click();
			wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("DEMO ACCOUNT"))).click();
		}

		if (!Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg")) {
/*		if (!((Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) && TestEnv.equalsIgnoreCase("prod"))) {*/

			try {
				wait15.until(ExpectedConditions.urlContains("/open-demo-account/"));
			} catch (TimeoutException e) {
				System.out.println("Using " + driver.getCurrentUrl() + " for Demo Account Registration.");
			}
		}

		// Include IBpromotion if it is not ""
		if (!IBpromotion.equals("")) {
			// Input redirection URL and click the button
			IBurl = driver.getCurrentUrl() + IBpromotion;
			driver.navigate().to(IBurl);
			wait15.until(ExpectedConditions.urlContains(IBpromotion));
		}

		// Redirect to Testing Trader with the help of ChangeAction button
		if (!RegisterURL.equals("")) {
			// the input and change controls in vt-beta are different
			if ((Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) && TestEnv.equalsIgnoreCase("beta")) {

				driver.findElement(By.cssSelector("input.change_text")).clear();
				driver.findElement(By.cssSelector("input.change_text")).sendKeys(RegisterURL);
				driver.findElement(By.cssSelector("button.change_button")).click();
			} else {

				driver.findElement(By.cssSelector("input.change_live")).clear();
				driver.findElement(By.cssSelector("input.change_live")).sendKeys(RegisterURL);
				driver.findElement(By.cssSelector("button#change_button")).click();
			}

		}

	}

	Map<String, String> funcClientEntryPg(String fName, String lName, String countryName, String phNumber, String email,
			String TestEnv, String Brand, String IBpromotion) throws Exception {

		Map<String, String> countryInfo = new HashMap<String, String>();
		String cimaprodTempCountry = "";

		// Input First Name
		driver.findElement(By.id("firstName")).clear();
		driver.findElement(By.id("firstName")).sendKeys(fName);

		// Input Last Name
		driver.findElement(By.id("lastName")).clear();
		// driver.findElement(By.id("lastName")).sendKeys("Zhu");
		driver.findElement(By.id("lastName")).sendKeys(lName);

		// set value for hidden input
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("return document.getElementById('cxd').value = '7.01';");

		// Input Country (set to UK for ID3 and world check verification)
		Select t = null;

		// Added by Yanni: when Brand = ky and TestEnv = prod, country control is not a Select control. Need to handle separately for choosing country.
		// Added by Yanni on 22/06/2020: make assume new regulator register page follows ASIC format, that is , "country" control is a select

		driver.findElement(By.id("country")).click();
		
		

		// If countryName as a parameter is empty, then select based on common rules,.
		// If countryName as a parameter is not empty, then select based on countryName. This is actually used for duplicate phone number verification.
		
		if(Brand.equalsIgnoreCase("au")) {
			countryName = "Australia";
			if (TestEnv.equalsIgnoreCase("alpha")) {
				WebElement country = driver.findElement(By.xpath("//div[@data-conutry='"+countryName + "']"));
				((JavascriptExecutor)driver).executeScript("window.scrollTo(0,100)");
				country.click();
			}
		}else {
		
			if (countryName.length() == 0) {
	
				switch (Brand.toLowerCase()) {
				case "au":
					//t.selectByValue("Australia");
					funcCountryDropdownSelect("Australia");
					break;
				
				default:
					funcCountryDropdownSelect("");
				}
	
			} else {
				if (!((Brand.equalsIgnoreCase("ky") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
						&& TestEnv.equalsIgnoreCase("prod"))) {
					
					//t.selectByValue(countryName);
					WebElement country = driver.findElement(By.xpath("//div[@data-conutry='"+countryName + "']"));
					((JavascriptExecutor)driver).executeScript("window.scrollTo(0,100)");
					country.click();
	
				} else {
					//driver.findElement(By.id("country")).click();
					Thread.sleep(500);
	
					if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
						cimaprodTempCountry = funcPUGProdChooseCountry(driver, countryName);
					} else if (Brand.equalsIgnoreCase("vt") ) {
						cimaprodTempCountry = funcVTProdChooseCountry(driver, countryName);
					} else {
						cimaprodTempCountry = funcCIMAProdChooseCountry(driver, countryName);
					}
				}
			}
		}
		
		// Log selected country name
		Thread.sleep(500);
		if(TestEnv.equalsIgnoreCase("prod"))
		{
			if( Brand.equalsIgnoreCase("au"))
			{
				countryInfo.put("country", countryName);
				
			}else if (Brand.equalsIgnoreCase("vt")) {
				
				countryInfo.put("country", cimaprodTempCountry);
			}
				
		} else {
			countryInfo.put("country", cimaprodTempCountry);
		}

		// Input phone number
		driver.findElement(By.id("phone")).clear();
		driver.findElement(By.id("phone")).sendKeys(phNumber);

		// Input email
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(email);

		// Log selected country name and code
		if (TestEnv.equalsIgnoreCase("prod") && (Brand.equalsIgnoreCase("au") || Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") ||Brand.equalsIgnoreCase("svg")))
			countryInfo.put("code", driver.findElement(By.id("phoneCode")).getAttribute("value"));
		else {
			
			t = new Select(driver.findElement(By.id("phoneCode")));
			countryInfo.put("code", t.getFirstSelectedOption().getText().trim());
			/* } */
		}

		// Yanni on 31/07/2020: a temporary solution for new regulator test. Only for registration with new.vantagefx.com
		selectRegulator(Brand, TestEnv);

		Thread.sleep(5000);  // Yanni: need to have this wait otherwise ASIC register will fail
		driver.findElement(By.id("button")).click();
		Thread.sleep(2000);

		// Thread.sleep(1000);
		// Check DB status
		System.out.println("DB status After Entry page submission:");

		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		if (TestEnv.equalsIgnoreCase("prod")) {
			Thread.sleep(2000);
		}

		System.out.println("countryInfo: " + countryInfo);

		return countryInfo;
	}

	void funcVerifyBringInValue(WebElement txtControl, String expectValue) {
		// Check value is brought in correctly
		String valueTxt = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", txtControl)
				.toString();
		Assert.assertTrue(valueTxt.equalsIgnoreCase(expectValue),
				valueTxt + "Value brought in from Entry page is wrong. It should be " + expectValue);
	}

	void clickPgNextBackButton(String Brand, Boolean isBack) throws Exception {
		WebElement nextButton = null;
		WebElement backButton = null;

		if (Brand.equalsIgnoreCase("vt")) {
			// Alex L on 28/01/2020: VT Next button changed due to re-Branding 
			nextButton = driver.findElement(By.cssSelector("button.el-button.btn_default.blue_button.el-button--default"));
		} else {
			nextButton = driver.findElement(By.xpath("//button[@class='el-button btn_blue el-button--default']"));

		}

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
		Thread.sleep(1000);

		if (isBack == false) {
			// Click Next
			nextButton.click();
		} else {
			if (Brand.equalsIgnoreCase("vt")) {
				backButton = driver.findElement(By.cssSelector("button.el-button.white_button.el-button--default"));

			} else {
				backButton = driver.findElement(By.xpath("//button[@class='el-button btn_default el-button--default']"));

			}
			// Click Back
			backButton.click();
		}
		Thread.sleep(1000);

	}

	// Read the latest email with subject of "Client Portal Access" and get the password string from the Email content
	public String funcCheckEmailtoGetPassword(String Brand, String testEnv, int numberOfEmail) throws Exception {
		Thread.sleep(1000);

		String selectSql = "select vars from tb_mail_send_log where subject like 'Client Portal Access' order by create_time desc limit XNO;";
		String processedSql, result, password="";
		String dbName = "";
		String entry[] = null;

		processedSql = selectSql.replace("XNO", Integer.toString(numberOfEmail));

		System.out.println(processedSql);

		dbName = Utils.getDBName(Brand)[1];

		result = DBUtils.funcreadDB(dbName, processedSql, testEnv);

		// parse the result and get the column we need
		entry = result.substring(1, result.length() - 1).split(",");

		for(String subEntry:entry) {
			if(subEntry.contains("PASSWORD"))
			{
				String pw[] = subEntry.split(":");
				password = pw[1].replace("\"", "");
				break;
			}
			
		}

		System.out.println("\n" + "password is: " + password);

		return password;
	}

	/*
	 * Yanni on 02/03/2020. Update Global-> tb_user_login -> ID3 flag.
	 * flagValue == true, set the ID3 flag to 1;
	 * flagValue == false, set the ID3 flag to 0;
	 */
	void funcUpdateID3Flag(String user_id, Boolean flagValue, String testEnv, String Brand) throws Exception {
		String updateSql = "update tb_user_login set client_status = (client_status | 32) where user_id = " + user_id
				+ ";";
		String dbName = Utils.getDBName(Brand)[0];

		if (flagValue == false) {
			updateSql = updateSql.replace("| 32", "& 223");
		}

		DBUtils.funcupdateDB(dbName, updateSql, testEnv);
	}

	/*
	 * Yanni on 02/03/2020. Update Global: tb_user_login -> WorldCheck flag
	 * flagValue == true, set the WorldCheck flag to 1;
	 * flagValue == false, set the WorldCheck flag to 0;
	 */
	void funcUpdateWldCkFlag(String user_id, Boolean flagValue, String testEnv, String Brand) throws Exception {
		String updateSql = "update tb_user_login set client_status = (client_status | 16) where user_id = " + user_id
				+ ";";
		String dbName = Utils.getDBName(Brand)[0];

		if (flagValue == false) {
			updateSql = updateSql.replace("| 16", "& 239");
		}

		DBUtils.funcupdateDB(dbName, updateSql, testEnv);
	}

	/*
	 * @Test
	 * public void testFunction() throws Exception
	 * {
	 * String userID="10000257";
	 * String TestEnv = "test";
	 * String Brand = "pug";
	 * 
	 * funcReadUserInGlobalDB(userID, TestEnv, Brand);
	 * 
	 * funcUpdateID3Flag(userID, true, TestEnv, Brand);
	 * 
	 * funcUpdateWldCkFlag(userID, true, TestEnv, Brand);
	 * 
	 * funcReadUserInGlobalDB(userID, TestEnv, Brand);
	 * 
	 * funcUpdateID3Flag(userID, false, TestEnv, Brand);
	 * 
	 * funcUpdateWldCkFlag(userID, false, TestEnv, Brand);
	 * funcReadUserInGlobalDB(userID, TestEnv, Brand);
	 * }
	 */

	void funcSetCountry2UK() throws Exception {
		List<WebElement> liItems;
		int j;
		// Get all li items which has Nationality listed
		liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		// j=r.nextInt(liItems.size());

		for (int i = 0; i < liItems.size(); i++) {
			if (liItems.get(i).getText().equalsIgnoreCase("United Kingdom")) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(i));
				liItems.get(i).click();
				Thread.sleep(500);
			}
		}

	}

	void funcSetCountry2AU() throws Exception {
		List<WebElement> liItems;
		// Get all li items which has Nationality listed
		liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		// j=r.nextInt(liItems.size());

		for (int i = 0; i < liItems.size(); i++) {
			if (liItems.get(i).getText().equalsIgnoreCase("Australia")) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(i));
				liItems.get(i).click();
				Thread.sleep(500);
			}
		}

	}

	/*
	 * Yanni on 28/2/2020: for New Registration Process POA record check.
	 * Provided with userID, TestEnv, Brand, it will check DB and print the result if there is.
	 * Also print the record status if there is result.
	 */
	public String funcGetPOARecord(String userID, String TestEnv, String Brand) throws Exception {

		String[] listResult;
		HashMap<String, String> poaStatus = new HashMap<String, String>();
		String statusNo = "";

		poaStatus.put("0", "Submitted");
		poaStatus.put("1", "Pending");
		poaStatus.put("2", "Approved");
		poaStatus.put("3", "Rejected");

		String selectSql = "Select * from tb_address_proof where user_id = " + userID
				+ " order by update_time desc limit 1;";
		String dbName = Utils.getDBName(Brand)[1];

		String returnResult = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);

		// If record is found, interpret the status from No to status and print
		if (!returnResult.equals("")) {
			System.out.println(returnResult);
			listResult = returnResult.split(", ");

			for (int i = 0; i < listResult.length; i++) {
				if (listResult[i].trim().startsWith("status")) {
					System.out.print(listResult[i]);
					statusNo = listResult[i].split(":")[1].trim();

					System.out.println(" POA Record Status is " + poaStatus.get(statusNo));

				}

			}
		} else {
			System.out.println("No POA record is found in tb_address_prof");
		}

		return statusNo;
	}

	/*
	 * Yanni on 28/2/2020: for New Registration Process ID record check.
	 * Provided with userID, TestEnv, Brand, it will check DB and print the result if there is.
	 * Also print the record status if there is result.
	 * 
	 * Return the status
	 */
	public String funcGetIDRecord(String userID, String TestEnv, String Brand) throws Exception {

		String[] listResult;
		HashMap<String, String> IDStatus = new HashMap<String, String>();
		String statusNo = "";

		IDStatus.put("0", "Submitted");
		IDStatus.put("1", "Pending");
		IDStatus.put("2", "Approved");
		IDStatus.put("3", "Rejected");

		String selectSql = "Select * from tb_id_proof where user_id = " + userID + " order by update_time desc limit 1";
		String dbName = Utils.getDBName(Brand)[1];
		String returnResult = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);

		// If record is found, interpret the status from No to status and print
		if (!returnResult.equals("")) {
			System.out.println(returnResult);
			listResult = returnResult.split(", ");

			for (int i = 0; i < listResult.length; i++) {
				if (listResult[i].startsWith("status")) {
					System.out.print(listResult[i]);
					statusNo = listResult[i].split(":")[1].trim();

					System.out.println(" ID Record Status is " + IDStatus.get(statusNo));

				}
			}
		} else {
			System.out.println("No POA record is found in tb_address_prof");
		}

		return statusNo;

	}

	// WorldCheck result: placeholder
	public String funcGetWorldCheck(String userID, String TestEnv, String Brand) throws Exception {

		String[] listResult;
		HashMap<String, String> wkStatus = new HashMap<String, String>();
		String statusNo = "";

		wkStatus.put("0", "Processing");
		wkStatus.put("1", "Completed");
		wkStatus.put("2", "Rejected");
		wkStatus.put("3", "Pending");
		wkStatus.put("4", "Re_Audit");
		wkStatus.put("9", "RE_Register");

		String selectSql = "Select * from tb_account_mt4 where user_id = " + userID
				+ " order by update_time desc limit 1";
		String dbName = Utils.getDBName(Brand)[1];
		String returnResult = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);

		// If record is found, interpret the status from No to status and print
		if (!returnResult.equals("")) {
			System.out.println(returnResult);
			listResult = returnResult.split(", ");

			for (int i = 0; i < listResult.length; i++) {
				if (listResult[i].startsWith("status")) {
					System.out.print(listResult[i]);
					statusNo = listResult[i].split(":")[1].trim();

					System.out.println(" World Record Status is " + wkStatus.get(statusNo));

				}
			}
		} else {
			System.out.println("No World Check record is found in tb_account_mt4 with user id: " + userID);
		}

		return statusNo;
	}

	public Map<String, String> funcDemoEntryPg(String fName, String lName, String phCountryCode, String phNumber,
			String demoEmail, String TestEnv, String Brand) throws Exception {

		// System.out.println("Country code: " + phCountryCode);
		// first name
		Map<String, String> countryInfo = new HashMap<String, String>();
		String cimaprodTempCountry = "";

		// The following 3 variables are for PUG PROD DEMO
		List<WebElement> optionList;
		Random r = new Random();
		int indexR = 0;

		Thread.sleep(6000);

		// wait50.until(ExpectedConditions.elementToBeClickable(By.id("firstName")));

		// driver.findElement(By.id("firstName")).click();
		driver.findElement(By.id("firstName")).clear();
		driver.findElement(By.id("firstName")).sendKeys(fName);

		// lastName
		driver.findElement(By.id("lastName")).clear();
		driver.findElement(By.id("lastName")).sendKeys(lName);

		// email
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(demoEmail);

		// Country:
/*		if ((Brand.equalsIgnoreCase("vfsc") && TestEnv.equalsIgnoreCase("prod"))
				|| (Brand.equalsIgnoreCase("fca") && TestEnv.equalsIgnoreCase("prod"))) {
			driver.findElement(By.id("country")).click();
			Thread.sleep(500);
			cimaprodTempCountry = funcCIMAProdChooseCountry(driver, "");
			countryInfo.put("country", cimaprodTempCountry);
		} else if ((Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
				&& TestEnv.equalsIgnoreCase("prod")) {
			// PROD PUG: DEMO has different country designs
			driver.findElement(By.id("country")).click();
			Thread.sleep(500);

			optionList = driver
					.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
			indexR = r.nextInt(optionList.size() - 1) + 1;

			String selectedCountry = optionList.get(indexR).getText();
			optionList.get(indexR).click();
			countryInfo.put("country", selectedCountry);

		} */
		if(TestEnv.equalsIgnoreCase("prod"))
		{
			switch(Brand.toLowerCase())
			{
				case "vfsc":
				case "vfsc2":
				case "fca":
					driver.findElement(By.id("country")).click();
					Thread.sleep(500);
					cimaprodTempCountry = funcCIMAProdChooseCountry(driver, "");
					countryInfo.put("country", cimaprodTempCountry);
					
					break;
				case "asic":
					driver.findElement(By.id("country")).click();
					Thread.sleep(500);
					
					// country
					Select t = new Select(driver.findElement(By.id("country")));
					t.selectByValue("Australia");
					
					break;
					
					
				case "fsa":
				case "svg":
					// PROD PUG: DEMO has different country designs
					driver.findElement(By.id("country")).click();
					Thread.sleep(500);

					optionList = driver
							.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
					indexR = r.nextInt(optionList.size() - 1) + 1;

					String selectedCountry = optionList.get(indexR).getText();
					optionList.get(indexR).click();
					countryInfo.put("country", selectedCountry);
					break;
					
					
				case "vt":
					driver.findElement(By.id("country")).click();
					Thread.sleep(500);
					cimaprodTempCountry = funcVTProdChooseCountry(driver, "");
					countryInfo.put("country", cimaprodTempCountry);
					break;
					
					default:
						
					
			}
			
		}else {

			driver.findElement(By.id("country")).click();
			Thread.sleep(500);

			// country
			Select t = new Select(driver.findElement(By.id("country")));
			if (!Brand.equalsIgnoreCase("au")) {

				t.selectByIndex(r.nextInt(t.getOptions().size() - 1) + 1);
			} else {
				t.selectByValue("Australia");
			}

			countryInfo.put("country", t.getFirstSelectedOption().getText().trim());
		}

		// phone
		driver.findElement(By.id("phone")).clear();
		driver.findElement(By.id("phone")).sendKeys(phNumber);

		Thread.sleep(1000);

		// Yanni on 31/07/2020: a temporary solution for new regulator test. Only for registration with new.vantagefx.com
		selectRegulator(Brand, TestEnv);

		Thread.sleep(1000);

		// phoneCode

		/*
		 * driver.findElement(By.id("phoneCode")).clear();
		 * driver.findElement(By.id("phoneCode")).sendKeys(phCountryCode);
		 */

		// Log phonecode
		/*
		 * if (TestEnv.equalsIgnoreCase("prod") && Brand.equalsIgnoreCase("au")) {
		 * countryInfo.put("code", driver.findElement(By.id("phoneCode")).getAttribute("value"));
		 * } else if (TestEnv.equalsIgnoreCase("prod")
		 * && (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))) {
		 * driver.findElement(By.id("phoneCode")).click();
		 * Thread.sleep(500);
		 * optionList = driver
		 * .findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
		 * indexR = r.nextInt(optionList.size());
		 * 
		 * optionList.get(indexR).click();
		 * countryInfo.put("code", driver.findElement(By.id("phoneCode")).getAttribute("value"));
		 * 
		 * } else {
		 * Select t = new Select(driver.findElement(By.id("phoneCode")));
		 * countryInfo.put("code", t.getFirstSelectedOption().getText().trim());
		 * }
		 */
		
		if(TestEnv.equalsIgnoreCase("prod"))
		{
			switch( Brand.toLowerCase())
			{
				//VFSC ASIC
				case "au":
				case "vfsc":
				case "vfsc2":
					countryInfo.put("code", driver.findElement(By.id("phoneCode")).getAttribute("value"));
					break;
			
				//PUG
				case "fsa":
				case "svg":
					  driver.findElement(By.id("phoneCode")).click();
					  Thread.sleep(500);
					  optionList = driver.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
					  indexR = r.nextInt(optionList.size());
					  
					  optionList.get(indexR).click();
					  countryInfo.put("code", driver.findElement(By.id("phoneCode")).getAttribute("value"));
					  
					  
					break;
					
				case "vt":
					//String phoneCode = driver.findElement(By.cssSelector("div.country_code>div.phone_option_accounr_hover")).getText();
					String phoneCode = driver.findElement(By.xpath(".//div[@class='phone_option accounr_hover']")).getText();
					//phone_option accounr_hover
					System.out.println("VT PROD PHONE CODE: " + phoneCode);
					countryInfo.put("code", phoneCode);
					break;
					
				//Other regulators of PROD
				default:
					  Select t = new Select(driver.findElement(By.id("phoneCode")));
					  countryInfo.put("code", t.getFirstSelectedOption().getText().trim());
					
			
			}
		}else  //non-prod all relguators
		{
			 Select t = new Select(driver.findElement(By.id("phoneCode")));
			 countryInfo.put("code", t.getFirstSelectedOption().getText().trim());
		}

		
		//div.country_code>div.phone_option_accounr_hover
		
		// click Next button
		switch (Brand.toLowerCase()) {
		case "svg":
		case "fsa":

			if (TestEnv.equalsIgnoreCase("prod")) {

				// Select leverage, updated by Yanni on 24/09/2020
				/*
				 * Select t= new Select(driver.findElement(By.id("leverage")));
				 * t.selectByIndex(r.nextInt(t.getOptions().size()));
				 */
				driver.findElement(By.id("leverage")).click();
				Thread.sleep(500);
				optionList = driver
						.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
				indexR = r.nextInt(optionList.size());

				optionList.get(indexR).click();
				System.out.println("Leverage: " + driver.findElement(By.id("leverage")).getAttribute("value"));

				// Select Currency, added by Yanni on 24/09/2020
				driver.findElement(By.id("currency")).click();
				Thread.sleep(500);

				optionList = driver
						.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
				/*
				 * 0: usd, 1: cad, 2: gbp,3:eur, 4: sgd, 5:aud, 6:nzd, 7:hkd, 8:jpy
				 */
				indexR = r.nextInt(optionList.size());

				optionList.get(indexR).click();
				System.out.println("Currency: " + driver.findElement(By.id("currency")).getAttribute("value"));

				// Select Account Balance
				driver.findElement(By.id("balance")).click();
				Thread.sleep(500);
				optionList = driver
						.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
				indexR = r.nextInt(optionList.size());

				optionList.get(indexR).click();
				System.out.println("Balance: " + driver.findElement(By.id("balance")).getAttribute("value"));
				/*
				 * t= new Select(driver.findElement(By.id("balance")));
				 * t.selectByIndex(r.nextInt(t.getOptions().size()));
				 * System.out.println("Balance: " + t.getFirstSelectedOption().getText());
				 */

				driver.findElement(By.id("button")).click();
			} else {
				driver.findElement(By.id("sub-next")).click();
			}

			break;

		case "vt":
			if (TestEnv.equalsIgnoreCase("prod"))
			{
				driver.findElement(By.id("button_next")).click();
			}else
			{
				driver.findElement(By.id("sub-next")).click();
			}
			break;

		case "au":
		case "ky":
		default:
			driver.findElement(By.id("sub-next")).click();

		}

		Thread.sleep(1000);
		/*
		 * System.out.println("country: " + countryInfo.get("country"));
		 * System.out.println("code: " + countryInfo.get("code"));
		 */
		return countryInfo;
	}

	void funcDupDemoEmailMobile(String TestEnv, String Brand, String WebSiteURL, String RegisterURL, String TraderURL,
			String IBpromotion, String dupItem) throws Exception {

		String fName = "Demo" + Utils.randomString(4).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = Utils.randomNumber(2), phNumber = Utils.randomNumber(8);
		String accountType = Utils.AccountT.getRandom().toString();
		String currency = Utils.Currency.getRandom().toString();
		String strPlatform = "MT4";

		System.out.println("Register a MT4 demo...\n");

		// Navigate to Entry Page
		funcGo2DemoEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion);

		// The registration page for Demo
		funcCPRegisterDemo(fName, lName, phCountryCode, phNumber, email, strPlatform, accountType, currency, TestEnv,
				Brand);
		// Check DB status
		System.out.println("\n\n****Please check user type should be DEMO ****\n");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

		System.out.println(
				"\nDemo is registered successfully. Register this demo again with the same set of information...\n");

		// Navigate to Entry Page
		funcGo2DemoEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion);

		if (dupItem.equalsIgnoreCase("email")) {
			phNumber = Integer.toString(Integer.parseInt(phNumber) - 1);
		} else if (dupItem.equalsIgnoreCase("mobile")) {
			email = email + ".au";
		}
		// Check duplicated email/mobile
		try {
			funcDemoEntryPg(fName, lName, phCountryCode, phNumber, email, TestEnv, Brand);
		} catch (ElementNotInteractableException e) {
			System.out.println("Entry Page Input is not done.");
		}
		// Print the popup dialog message:
		System.out.println("Got Popup Dialog...");
		System.out.println(driver.findElement(By.cssSelector("div.form_email_box h3")).getText());

		// Click the link
		driver.findElement(By.cssSelector("a.client_btn")).click();
		Thread.sleep(1000);

		String urlAddr = driver.getCurrentUrl();
		String expectAddr = "";
		switch (Brand) {
		case "ky":
		case "au":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":
			expectAddr = "secure.vantagefx.com/";
			break;

		/*
		 * case "au":
		 * expectAddr = "secure.vantagefx.com.au/";
		 * break;
		 */
		case "fsa":
		case "svg":
			expectAddr = "https://myaccount.puprime.com/";
			break;

		default:
			System.out.println(Brand + " is not supported.");

		}
		Assert.assertTrue(urlAddr.contains(expectAddr), "Client Portal Login button doesn't navigate to Login window");

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand", "WebSiteURL", "RegisterURL", "TraderURL", "IBpromotion" })
	void CheckDuplicatedDemoMobile(String TestEnv, String Brand, String WebSiteURL, String RegisterURL,
			String TraderURL, String IBpromotion) throws Exception {
		String dupItem = "mobile";
		funcDupDemoEmailMobile(TestEnv, Brand, WebSiteURL, RegisterURL, TraderURL, IBpromotion, dupItem);

	}

	void funcDupClientEmailMobile(String TestEnv, String Brand, String WebSiteURL, String RegisterURL, String TraderURL,
			String IBpromotion, String dupItem) throws Exception {
		// String fName="Webt"+Utils.randomString(3).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		String strPlatform = "", countryName = "";

		Map<String, String> countryInfo = new HashMap<String, String>();

		// Navigate to Entry Page
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		// Input Entry Page and Submit

		/*
		 * input email
		 * input phCountryCode and phNumber
		 */
		System.out.println("Register a lead...\n");
		countryInfo = funcClientEntryPg(fName, lName, countryName, phNumber, email, TestEnv, Brand, IBpromotion);

		if (countryInfo.isEmpty()) {
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		} else {
			phCountryCode = countryInfo.get("code");
		}

		Thread.sleep(1000);

		System.out.println(
				"Lead is registered successfully. Register this lead again with the same set of information...\n");

		if (dupItem.equalsIgnoreCase("email")) {
			phNumber = Integer.toString(Integer.parseInt(phNumber) - 1);
		} else if (dupItem.equalsIgnoreCase("mobile")) {
			email = email + ".au";
		}

		// Navigate to Entry Page 2nd time
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		try {
			funcClientEntryPg(fName, lName, countryInfo.get("country"), phNumber, email, TestEnv, Brand, IBpromotion);
		} catch (NoSuchElementException e) {
			System.out.println("Entry Page Input is not done.");
		}

		// Print the popup dialog message:
		System.out.println("Got Popup Dialog...if not, the test case would fail");
		System.out.println(driver.findElement(By.cssSelector("div.form_email_box h3")).getText());

		// Click the link
		WebElement ele = driver.findElement(By.cssSelector("a.client_btn"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", ele);

		Thread.sleep(1000);
		System.out.println("Button navigates to: " + driver.getCurrentUrl());
		String urlAddr = driver.getCurrentUrl();
		String expectAddr = "";

		// Handle the popup dialog indicating client already has registered and prompting to login
		switch (Brand) {
		case "ky":
		case "fca":
		case "vfsc":
		case "vfsc2":
		case "au":
		case "regulator2":
			expectAddr = "https://secure.vantagefx.com/login";
			break;

		/*
		 * case "au":
		 * expectAddr = "https://secure.vantagefx.com.au/login";
		 * break;
		 */
		case "fsa":
		case "svg":
			expectAddr = "https://myaccount.puprime.com/login";
			break;

		default:
			System.out.println(Brand + " is not supported.");

		}
		Assert.assertTrue(urlAddr.contains(expectAddr), "Client Portal Login button doesn't navigate to Login window");

	}

	public String funcCIMAProdChooseCountry(WebDriver driver, String countryName) {
		String selectedCountry = countryName;
		List<WebElement> countryList;
		WebElement e;

		String strSelector = ".//span[contains(text(),'" + countryName + "')]";

		// System.out.println("strSelector: " + strSelector);

		// If no countryName is specified, choose randomly. Otherwise choose the specified one.
		if (countryName.length() == 0) {

			countryList = driver.findElements(By.cssSelector("div.results_option>span"));
			e = countryList.get(r.nextInt(countryList.size() - 1) + 1);  // Don't choose 1st Australia

		} else {
			e = driver.findElement(By.xpath(strSelector));

		}

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
		selectedCountry = e.getText();
		e.click();

		// System.out.println("selectedCountry:" + selectedCountry);
		return selectedCountry;
	}

	// FCA choose country
	public String funcFCAProdChooseCountry(WebDriver driver, String countryName) {
		String selectedCountry = countryName;
		List<WebElement> countryList;
		WebElement e;

		String strSelector = ".//span[contains(text(),'" + countryName + "')]";

		// System.out.println("strSelector: " + strSelector);

		// If no countryName is specified, choose randomly. Otherwise choose the specified one.
		if (countryName.length() == 0) {

			countryList = driver.findElements(By.cssSelector("div.results_option>span"));
			
			System.out.println(countryList.size());
			e = countryList.get(r.nextInt(countryList.size() - 1)); 

		} else {
			e = driver.findElement(By.xpath(strSelector));

		}
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", e); //can not use scrollIntoView function as there is a warning pop for FCA
		selectedCountry = e.getText();
		return selectedCountry;
	}
	
	
	public String funcPUGProdChooseCountry(WebDriver driver, String countryName) {
		String selectedCountry = countryName;
		List<WebElement> countryList;
		WebElement e;
		int indexR;

		String strSelector = ".//li[contains(text(),'" + countryName + "')]";

		// System.out.println("strSelector: " + strSelector);

		// If no countryName is specified, choose randomly. Otherwise choose the specified one.
		if (countryName.length() == 0) {

			countryList = driver
					.findElements(By.xpath("//ul[@class='account_opiton' and @style = 'display: block;']/li"));
			indexR = r.nextInt(countryList.size() - 1) + 1;
			selectedCountry = countryList.get(indexR).getText();
			countryList.get(indexR).click();
		} else {
			e = driver.findElement(By.xpath(strSelector));
			Actions actions = new Actions(driver);
			/* ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", e); */
			selectedCountry = e.getText();
			actions.moveToElement(e).click().build().perform();
		}

		/*
		 * Actions actions = new Actions(driver);
		 * ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", e);
		 * //selectedCountry = e.getText();
		 * actions.moveToElement(e).click().build().perform();
		 * //e.click();
		 */

		// System.out.println("selectedCountry:" + selectedCountry);
		return selectedCountry;
	}
	
	public String funcVTProdChooseCountry(WebDriver driver, String countryName) {
		String selectedCountry = countryName;
		List<WebElement> countryList;
		WebElement e;
		int indexR;

		String strSelector = ".//div[contains(text(),'" + countryName + "')]";

		// System.out.println("strSelector: " + strSelector);

		// If no countryName is specified, choose randomly. Otherwise choose the specified one.
		if (countryName.length() == 0) {

			countryList = driver.findElements(By.cssSelector("div.country_code > div.country_option"));
			indexR = r.nextInt(countryList.size() - 1) + 1;
			selectedCountry = countryList.get(indexR).getText();
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", countryList.get(indexR));
			countryList.get(indexR).click();
		} else {
			e = driver.findElement(By.xpath(strSelector));
			Actions actions = new Actions(driver);
			/* ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", e); */
			selectedCountry = e.getText();
			actions.moveToElement(e).click().build().perform();
		}

		return selectedCountry;
	}

	// Added by Alex Liu for testing auto audit result
	void funcCPRegisterForAutoAudit(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv,
			String WebSiteURL, String RegisterURL, String IBpromotion, String strPlatform, boolean uploadFile) throws Exception {

		// String fName="Webt"+Utils.randomString(3).toLowerCase();
		String lName = Utils.randomString(3).toUpperCase();
		String email = fName + Utils.emailSuffix + "." + Brand;
		String phCountryCode = "", phNumber = Utils.randomNumber(8);
		String countryName = "";
		Boolean isBack = false;

		Map<String, String> countryInfo = new HashMap<String, String>();

		// Navigate to Entry Page
		funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);

		// Input Entry Page and Submit

		/*
		 * input email
		 * input Country and phNumber
		 * return Country and Phone code
		 */
		countryInfo = funcClientEntryPg(fName, lName, countryName, phNumber, email, TestEnv, Brand, IBpromotion);

		if (countryInfo.isEmpty()) {
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		} else {
			phCountryCode = countryInfo.get("code");
		}

		// Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, TestEnv, Brand);

		// Next
		clickPgNextBackButton(Brand, isBack);

		// Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand);

		// Click Next
		clickPgNextBackButton(Brand, isBack);
		System.out.println("After Page2 Submission....");

		// Page3
		funcClientPg3(fName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);
		System.out.println("After Page3 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
		
		// Set worldcheck to 1
		//funcUpdateWldCkFlag(userID, true, TestEnv, Brand);
		
		// Account Configuration
		funcClientPg4(fName, TestEnv, strPlatform, Brand);
		// Next
		clickPgNextBackButton(Brand, isBack);
		// Check DB
		System.out.println();
		System.out.println("After Page4 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
		Thread.sleep(6000);
		
		// Page5
		// Upload ID & POA
		
		if (uploadFile)
			funcClientPg5(fName, lName, TestEnv, Brand);

		clickPgNextBackButton(Brand, isBack);

		// Check DB
		System.out.println();
		System.out.println("After Page5 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
		
		
		Thread.sleep(6000);
		//check home page account details
		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			driver.findElement(By.xpath("//span[contains(text(),'HOME')]")).click();
		}
		Thread.sleep(500);
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		//Thread.sleep(6000);

		// Page5-7 is not necessary for auto audit testing.
		// Going to check auto audit result
		funcVerifyAuditResult(AdminURL, AdminName, AdminPass, TestEnv, Brand);
	}

	/*
	 * Alex L on 27/05/2020: for checking auto audit result for registration. Including
	 * check if mt4/5 account has been assigned
	 * check currency and leverage
	 * check the data source ID
	 * check if the account was able to trade
	 */
	public String funcVerifyAuditResult(String AdminURL, String AdminName, String AdminPass, String TestEnv,
			String Brand) throws Exception {

		String[] listResult;
		HashMap<String, String> dataSourceID = new HashMap<String, String>();
		HashMap<String, String> accType = new HashMap<String, String>();
		String dbSourceNo = "", account = "", leverage = "", group = "", accTypeNo = "";
		String cookie = "", tradingStatus = "", currency = "",acctOwner="";

		AdminURL = Utils.ParseInputURL(AdminURL);

		System.out.println("\n\n===============================================");
		System.out.println("==== Going to verify account audit result. ====");
		System.out.println("===============================================\n");
		// auto audit on VIG/VGP only refer to AU3, UK3 and MT5
		dataSourceID.put("14", "AU3");
		dataSourceID.put("15", "UK3");
		dataSourceID.put("8", "MT5");
		dataSourceID.put("10", "VT");
		dataSourceID.put("7", "PUG");
		dataSourceID.put("19", "VT MT5");
		dataSourceID.put("20", "PUG MT5");

		// account types
		accType.put("1", "Standard STP");
		accType.put("2", "Raw ECN");
		accType.put("6", "Islamic STP");
		accType.put("7", "Islamic ECN");
		accType.put("8", "PRO ECN");
		accType.put("9", "VIP STP");
		accType.put("10", "PAMM");
		accType.put("11", "Net STP");
		accType.put("12", "Net ECN");
		accType.put("13", "MT5 Hedge STP");
		accType.put("14", "MT5 Hedge ECN");
		accType.put("15", "MT5 Islamic STP");
		accType.put("16", "MT5 Islamic ECN");
		accType.put("17", "MT5 PRO ECN");
		accType.put("19", "Cent");
		accType.put("20", "Islamic Cent");
		accType.put("101", "MT5 Standard STP");

		String selectSql = "SELECT mt.apply_currency,mt.leverage,mt.accountMT4Type,o.real_name,o.mt4Group,mt.mt4_account,mt.mt4_datasource_id,r.p_id "
						+ "FROM tb_account_mt4 mt,tb_user_outer o,tb_user_relation r where mt.user_id=o.user_id and mt.user_id=r.user_id order by o.user_id desc limit 1;";
		String dbName = Utils.getDBName(Brand)[1];
		String returnResult = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);

		// If record is found, interpret the status from No to status and print
		if (!returnResult.equals("")) {
			 System.out.println(returnResult);
			listResult = returnResult.split(", ");
			
			try {
				if (TestEnv.equalsIgnoreCase("test")) {
					dbSourceNo = listResult[6].split(": ")[1].trim();
					//dbSourceNo = dbSourceNo.substring(0, dbSourceNo.length() - 1).trim();
					account = listResult[5].split(": ")[1].trim();
					leverage = listResult[1].split(": ")[1];
					group = listResult[4].split(": ")[1];
					accTypeNo = listResult[2].split(": ")[1];
					currency = listResult[0].split(": ")[1];
					acctOwner = listResult[7].split(": ")[1];
					acctOwner = acctOwner.substring(0, acctOwner.length() - 1).trim();
				} else {
					account = listResult[5].split("=")[1].trim();
					leverage = listResult[0].split("=")[1];
					dbSourceNo = listResult[1].split("=")[1];
					group = listResult[6].substring(0, listResult[6].length() - 1).split("=")[1];
					accTypeNo = listResult[3].split("=")[1];
					currency = listResult[2].split("=")[1];
					acctOwner = listResult[7].split("=")[1];
					acctOwner = acctOwner.substring(0, acctOwner.length() - 1).trim();
				}
			} catch (Exception e) {
				System.out.println("Some fields don't have value. Please double check.");
			}

			if (!account.equals("") && !account.contains("null")) {
				
				funcGetCPAccountInfo();
				// Going to get Admin portal cookie
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--incognito");
				//System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
				driver = new ChromeDriver(options);
				driver.manage().window().maximize();
				driver.get(AdminURL);

				cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);
				//System.out.println("cookie: "+cookie);
				driver.close();

				System.out.println("\n****** Auto Audit Successfully! See the details as below:******\n");
				System.out.println("****** Account Server is: " + dataSourceID.get(dbSourceNo));
				System.out.println("****** Account Number is: " + account);
				System.out.println("****** Account Type is: " + accType.get(accTypeNo));
				System.out.println("****** Leverage is: " + leverage + ":1");
				System.out.println("****** Currency is: " + currency);
				System.out.println("****** Account Group is: " + group);
				System.out.println("****** Account Owner's User ID is: " + acctOwner);

				tradingStatus = RestAPI.funcAPICheckAccountTradingStatus(Brand, AdminURL, account.trim(), cookie);

				if (tradingStatus.equals("1")) {
					System.out.println("****** Account Trading Status is: READ ONLY\n");

				} else if (tradingStatus.equals("na")) {
					System.out.println("!!!!!! Not able to identify trading status.\n");

				} else {
					System.out.println("****** Account Trading Status is: TRADING ALLOWED\n");
				}
				
				funcCompareCPDB(account, currency, leverage.trim() + ":1", tradingStatus);

			} else {
				System.out.println("\n!!!  No Account Aduit Happened  !!!\n");
			}

		} else {
			Assert.assertTrue(false, "\n!!!  No return from reading DB  !!!\n");
		}
		//funcCompareCPDB(account, currency, leverage.trim() + ":1", tradingStatus);
		return account;

	}
	
	void funcGetCPAccountInfo() {
		//Account No
		accountInfo.put("accountNo", driver.findElement(By.xpath("//tr[@class='el-table__row']/td[1]/div")).getText().trim());
		//TYPE: Standard/Islamic etc
		accountInfo.put("accountType",driver.findElement(By.xpath("//tr[@class='el-table__row']/td[2]/div")).getText().trim());
		//PLATFORM: MT4/5
		accountInfo.put("platform", driver.findElement(By.xpath("//tr[@class='el-table__row']/td[3]/div")).getText().trim());
		//CURRENCY
		accountInfo.put("currency", driver.findElement(By.xpath("//tr[@class='el-table__row']/td[4]/div")).getText().trim());
		//LEVERAGE
		accountInfo.put("leverage", driver.findElement(By.xpath("//tr[@class='el-table__row']/td[6]/div/div/span")).getText().trim());
		//STATUS: pending or active
		accountInfo.put("status",  driver.findElement(By.xpath("//tr[@class='el-table__row']/td[7]/div")).getText().trim());
	}
	
	void funcCompareCPDB(String accountNo, String currency, String leverage, String status) {
		
		//ACCOUNT NUMBER
		Assert.assertTrue(accountNo.trim().equals(accountInfo.get("accountNo")),"Account No not match");
		//TYPE: Standard/Islamic etc;
		/*
		 * System.out.println("CP Account Type: " + accountInfo.get("accountType"));
		 * System.out.println("DB Account Type: " + accountType);
		 */
		Assert.assertTrue(accountInfo.get("accountType").equalsIgnoreCase(accountType), "Account Type not match");
		
		//PLATFORM: MT4/5
		Assert.assertTrue(platform.equalsIgnoreCase(accountInfo.get("platform")), "Platform not match");
		//CURRENCY
		Assert.assertTrue(currency.trim().equalsIgnoreCase(accountInfo.get("currency")),"Currency not match");
		//LEVERAGE
		Assert.assertTrue(leverage.trim().equalsIgnoreCase(accountInfo.get("leverage").replace(" ", "")), "Leverage not match");
		
		//STATUS: pending or active
		switch (status.toLowerCase().trim()) {
		case "0":
			Assert.assertTrue(accountInfo.get("status").toLowerCase().contains("active"), "Status not match");
			break;
			default:
				Assert.assertTrue(accountInfo.get("status").contains("pending"), "Status not match");
		}
		
		
	}
	/*
	 * Yanni on 30/07/2020: temporarily function for new regulator
	 * new.vantagefx.com has a new page for new regulator registration. A new field (regulator) is added. This function is to choose a regulator based on BRAND
	 */

	void selectRegulator(String Brand, String TestEnv) {

		// PROD env: no need to choose regulator
		if (!TestEnv.equalsIgnoreCase("prod")) {

			Select selRegulator = new Select(driver.findElement(By.id("regulator")));
			System.out.println(Brand);
			switch (Brand) {
			case "ky":
			case "vt":
				selRegulator.selectByVisibleText("CIMA");
				selRegulator.selectByValue("CIMA");
				// System.out.println("Selected brand: " + selRegulator.getFirstSelectedOption().getText());
				break;
			case "fsa":
				selRegulator.selectByVisibleText("FSA");
				break;
			case "svg":
				selRegulator.selectByVisibleText("SVG");
				break;

			case "au":
				selRegulator.selectByVisibleText("ASIC");
				break;

			case "vfsc":
				selRegulator.selectByVisibleText("VFSC");
				break;
			case "vfsc2":
				selRegulator.selectByVisibleText("VFSC2");
				break;
			case "fca":
				selRegulator.selectByVisibleText("FCA");
				break;

			default:
				System.out.println(Brand + " is NOT supported.");
			}
		}
	}

	/*
	 * Extracted from funcCPRegisterDemo to make the original function more clear
	 * Extracted by Yanni on 11/09/2020
	 */
	void funcDemoPage2(String TestEnv, String Brand, String Platform) {

		int selectedIndex = 0;

		// Page2: choose Platform, Account Type and Currency
		// List<WebElement> platformList = driver.findElements(By.cssSelector("div.mode.platform span img")); platform_box

		// Comment out IF as VT & PUG add MT5 server support
		/* if (!Brand.equalsIgnoreCase("vt")) { */
			List<WebElement> platformList = driver.findElements(By.cssSelector("div.platform_box span img"));
			switch (Platform) {
			case "MT4":
				platformList.get(0).click();
				System.out.println("Platform: MT4");
				break;

			case "MT5":
				platformList.get(1).click();
				System.out.println("Platform: MT5");
				break;

			default:
				platformList.get(0).click();
			}
			/* } */

		// Account Type:randomly pick
		// List<WebElement> actList = driver.findElements(By.cssSelector("div.mode.mode1.accountType span img"));
		List<WebElement> actList = driver.findElements(By.cssSelector("div.accountType_box span img"));
		Random r = new Random();
		selectedIndex = r.nextInt(actList.size());
		// int actTypeNumber =2; //2 types: RAW, STANARD
		actList.get(selectedIndex).click();
		System.out.println("Account Type: " + selectedIndex);

		// Currency: define currency box selector
		String currencySelector = "div.currency_box div span";
		switch (TestEnv) {

		case "prod":
			if(Brand.equalsIgnoreCase("vt"))
			{
				currencySelector ="div.currency_box span";
			}
			break;
			
		case "test":
		case "beta":
			/*
			 * t = new Select(driver.findElement(By.id("currency")));
			 * t.selectByIndex(r.nextInt(t.getOptions().size()));
			 * 
			 * break;
			 */
			break;		
		default:
	
		}

		//Select Currency
		actList = driver.findElements(By.cssSelector(currencySelector));
		selectedIndex = r.nextInt(actList.size());
		actList.get(selectedIndex).click();
		System.out.println("Currency: " + selectedIndex);

	
		// Leverage: randomly pick div.leverage_box
		switch (TestEnv) {

		case "test":
		case "beta":
		case "prod":
		default:

			actList = driver.findElements(By.cssSelector("div.leverage_box span"));
			selectedIndex = r.nextInt(actList.size());
			actList.get(selectedIndex).click();
			System.out.println("Leverage: " + actList.get(selectedIndex).getText());

			break;
		}

		// Account Balance: random pick div.balance_box>span
		switch (TestEnv) {

		case "test":
		case "beta":
		case "prod":
		default:

			actList = driver.findElements(By.cssSelector("div.balance_box span"));
			selectedIndex = r.nextInt(actList.size());
			actList.get(selectedIndex).click();
			System.out.println("Balance: " + actList.get(selectedIndex).getText());

			break;
		}

	}

	@Test(invocationCount = 1)
	@Parameters(value = { "Brand", "TestEnv", "WebSiteURL", "RegisterURL", "IBpromotion" })
	public void RegisterUserMT5(String Brand, String TestEnv, String WebSiteURL, String RegisterURL,
			@Optional("") String IBpromotion) throws Exception {
	
		String strPlatform = "MT5";
		boolean uploadFile = true;
		switch (Brand) {

			case "fca":
				System.out.println(Brand + " doesn't support MT5 User Registration");
				break;
			
			default:		
				funcCPRegisterClient(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform, uploadFile);

		}
	
	}

}
