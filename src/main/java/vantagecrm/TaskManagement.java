package vantagecrm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.http.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

public class TaskManagement {

	WebDriver driver;
	// different wait time among different environments. Test environment will use 1 while beta & production will use 2.
	// Initialized in LaunchBrowser function.
	int waitIndex = 1;
	String userName; // Client Name
	String IBName;  // IB Name
	String internalUserName; // Internal user name;
	Random tRandom = new Random();
	WebDriverWait wait03;

	String serverName = "VFX AU3";
	
	//for test retry
	int count  = 0;
	/************************/
	
	public enum OLDepositM {
		CC("CreditCard", 1), UnionPay("Union Pay", 3), Thai("Thailand bank transfer", 4),
		Malay("Malaysia bank transfer", 5), Fasa("FasaPay", 6), Viet("Vietnam Bank Transfer", 7),
		Mobile("Mobile Pay", 8), Neteller("Neteller", 9), Nigeria("Nigeria bank transfer", 10),
		UPP2P("UnionPay P2P", 11),PAYPAL("Paypal",12), CRYPTO("Cryptocurrency",13), CANADATRANS("Canadian Local Bank Transfer",14),
		INDONESIA("Indonesia bank transfer",15), BITWALLET("Bitwallet",16), SKRILL("Skrill",17),STICPAY("SticPay",18),
		TRUSTLY("Trustly",19);

		private String menuName;
		private int menuIndex;

		private OLDepositM(String menuName, int menuIndex) {
			this.menuName = menuName;
			this.menuIndex = menuIndex;
		}

		public int getIndex() {
			return menuIndex;
		}

		public String getName() {

			return menuName;
		}

	}

	// Yanni on 29/06/2020: the menuIndex is going to be obsoleted as different brands have different index. I have modified current methods to
	// avoid using menu index.
	public enum WithdrawM {
		CC("Credit Card", 1), BankTransfer("Bank Transfer(International)", 2), SkrillNeteller("Skrill/Neteller", 3),
		UnionPay("UnionPay", 4), Thai("Thailand Bank Transfer", 5), Malay("Malaysia Bank Transfer", 6),
		Fasa("FasaPay", 7), Viet("Vietnam Bank Transfer", 8), Nigeria("Nigeria Bank Transfer", 9), Skrill("Skrill", 10),
		Neteller("Neteller", 11), Paypal("Paypal", 12), CryptoBTC("Cryptocurrency-BTC", 13),
		CryptoUSDT("Cryptocurrency-USDT", 14),Bitwallet("Bitwallet",15),SticPay("Sticpay",16),PerfectMoney("Perfect Money",61),JapanBT("Japan Bank Transfer",62),
		CryptoETH("Cryptocurrency-ETH",80), PUGBankTransfer("Bank Transfer/Bpay/Poli/Equals Payment", 2),PoliBankTransfer("Bank Transfer/Bpay/Poli Payment", 2);

		private String menuName;
		private int menuIndex;

		private WithdrawM(String menuName, int menuIndex) {
			this.menuName = menuName;
			this.menuIndex = menuIndex;
		}

		public int getIndex() {
			return menuIndex;
		}

		public String getName() {

			return menuName;
		}

	}

	// Launch driver
	@BeforeClass(alwaysRun = true)
	// @Test(enabled=false)
	@Parameters(value = {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless,ITestContext context)  // Added one parameter ITestContext by Yanni on 5/15/2019
	{

		// System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
		
		//headless option
		System.out.println("Driver type: " + headless);
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		
		//driver = new ChromeDriver();

		utils.Listeners.TestListener.driver = driver;
		context.setAttribute("driver", driver);           // Added one parameter ITestContext by Yanni on 5/15/2019

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
		}

		wait03 = new WebDriverWait(driver, Duration.ofSeconds(1));
	}

	@AfterClass(alwaysRun = true)
	void ExitBrowser() {

		Utils.funcLogOutAdmin(driver);
		// Close all browsers
		driver.quit();
	}

	// Login Admin with credentials

	@Parameters({ "AdminURL", "AdminName", "AdminPass", "Brand" })
	@Test(priority = 0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand, Method method)
			throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Login to Admin Portal");
		// Login AU admin
		driver.get(AdminURL);
		Utils.funcLogInAdmin(driver, AdminName, AdminPass, Brand);
	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditIB(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;

		String[] keyWords = new String[] {Utils.ibUserPrefix, Utils.addUserPrefix, Utils.webUserPrefix, Utils.addUserPrefix };

		int i = 0, j;

		//serverName = "VFX AU3";

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		if (Utils.addIBName != null) {
			if (!Utils.addIBName.equals("")) {
				keyWords[0] = Utils.addIBName;
			}
		}

		System.out.println("Start to audit accounts for IB...");
		System.out.println("-------------------------------------------------------------");

		// i=0, audit testcrmib** user ;
		// i=1, audit joint user
		// i=2: audit web individual user;
		// i=3, audit test*** individual user

		Thread.sleep(3000);
		// Select Submitted status requests
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByValue("0");

		// Audit Web Registered User

		// Select Search Options
		driver.findElement(By.id("searchType")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
		driver.findElement(By.id("query")).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No results for keywords " + keyWords[i]);
		} else {

			// Look for 1st Submitted records with name starting with pattern pat.

			for (j = 0; j < trs.size(); j++) {

				// Get user name
				name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isTestIB(name)) {
					Thread.sleep(2000);
					trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

					Thread.sleep(3000);

					// audit IB user
					wait03.until(ExpectedConditions
							.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));

					// Choose IB role as level-1 IB
					t = new Select(driver.findElement(By.id("ib_level")));
					t.selectByVisibleText("level-1");

					// Keep account owner as default

					/*
					 * //Check if MT5 account type
					 * t = new
					 * Select(driver.findElement(By.id("mt4_account_type")));
					 * if (t.getFirstSelectedOption().getText().contains("MT5"))
					 * {
					 * serverName = "VFX MT5";
					 * }
					 */
					
					// Choose Server
					t = new Select(driver.findElement(By.id("ib_server")));

					for(int index = 1; index<= t.getOptions().size();index++)
					{
						t.selectByIndex(index);
						driver.findElement(By.id("ib_MAM_Number")).click();
						
						WebElement err = null;
						err = getErr();
						
						if(err == null || err.getText().trim().equals(""))
						{
							Thread.sleep(3000);
							break;
						}
					}

					/*
					 * // Choose Rebate Group: VT and AU have the same item
					 * t = new Select(driver.findElement(By.id("ib_mt4_set")));
					 * while (t.getOptions().size() == 1) {
					 * Thread.sleep(1000);
					 * }
					 * t.selectByVisibleText("TEST_VFX_RE_USD");
					 */
					
					// Choose Account Group
					funcChooseTradeGroup(testEnv, Brand);
					
					/*
					 * }else { t.selectByIndex(1); }
					 */
					Thread.sleep(2000);

					// Choose IB Groups. AU & VT have different options

					driver.findElement(By.id("ib_IB_Grounp")).click();
					Thread.sleep(4000);

					driver.findElement(By.cssSelector("input[type='checkbox'][value^='TEST_']")).click();

					// Choose Commission code

					if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
						t = new Select(driver.findElement(By.id("com_code")));
						t.selectByIndex(1); // Choose the first available commission code
					}

					// Choose Trading Account Group
					t = new Select(driver.findElement(By.id("ib_add_trading")));
					Assert.assertTrue(t.getOptions().size() > 1);
					t.selectByIndex(1);

					// Choose Trading account Commission code
					if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
						t = new Select(driver.findElement(By.id("com_code2")));
						Assert.assertTrue(t.getOptions().size() > 1);
						t.selectByIndex(1); // Choose the first available commission code
					}

					// Choose Trading leverage
					t = new Select(driver.findElement(By.id("ib_Leverage")));
					Assert.assertTrue(t.getOptions().size() > 1);
					t.selectByIndex(4); // 100:1

					Thread.sleep(2000);

					// Check Identity Proof. If not uploaded, upload. Otherwise ignore
					if (driver
							.findElements(By
									.cssSelector("div.form-group.passport div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input identity Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						Thread.sleep(1000);
						driver.findElement(By.cssSelector(
								"div.form-group.passport div.col-md-8.pull-right.input-box.clearfix span a")).click();
						Thread.sleep(1000);

					}

					// Check Address Proof. If not uploaded, upload. Otherwise ignore

					if (driver
							.findElements(
									By.cssSelector("div.form-group.upadd div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {

						// Input Address Proof
						driver.findElement(By.xpath("//input[@type='file' and @data-name='statement']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						Thread.sleep(1000);
						driver.findElement(By
								.cssSelector("div.form-group.upadd div.col-md-8.pull-right.input-box.clearfix span a"))
								.click();
						Thread.sleep(1000);

					}

					// Input World check: no need anymore
					/*
					 * driver.findElement(By.xpath("//input[@type='file' and @data-name='worldCheck']")).sendKeys(Utils.workingDir+"\\proof.png");
					 * Thread.sleep(1000);
					 * driver.findElement(By.cssSelector("div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix span a")).click();
					 * Thread.sleep(1000);
					 */

					// Input IB assignment doc
					driver.findElement(By.xpath("//input[@type='file' and @data-name='assignment']"))
							.sendKeys(Utils.workingDir + "\\proof.png");
					Thread.sleep(1000);
					driver.findElement(By
							.cssSelector("div.form-group.assignment div.col-md-8.pull-right.input-box.clearfix span a"))
							.click();
					Thread.sleep(1000);

					// Click Confirm button

					Thread.sleep(2000);
					driver.findElement(By.xpath("//button[text()='Confirm']")).click();
					// Thread.sleep(waitIndex*3000);

					// Log result
					System.out.println("Audit IB User " + name);

					resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Result is:" + resultMsg);
					System.out.println();
					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
					// break the current loop and go back to parent loop
					break;

				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
				System.out.println();
			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditPendingIB(String testEnv, String Brand, Method method) throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Pending audit account for IB");
		Select t;
		int pendingChoiceNo = 9;
		String name; // Used as temporary names
		String resultMsg;

		String keyWords = Utils.ibUserPrefix;

		int j;

		// VT doesn't have function to select Pending Reasons. So just pending once.
		if (Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			pendingChoiceNo = 1;
		}

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to pending accounts for IB...");
		System.out.println("-------------------------------------------------------------");

		// i=0, audit testcrmib** user ;
		// i=1, audit joint user
		// i=2: audit web individual user;
		// i=3, audit test*** individual user

		Thread.sleep(3000);
		// Select Submitted status requests
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByValue("0");

		// Select Search Options
		driver.findElement(By.id("searchType")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyWords);
		driver.findElement(By.id("query")).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No results for keywords " + keyWords);
		} else {

			// Look for 1st Submitted records with name starting with pattern pat.

			for (j = 0; j < trs.size(); j++) {

				// Get user name
				name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
				Thread.sleep(2000);
				trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

				Thread.sleep(3000);

				if (Utils.isTestIB(name)) {

					// Select Pending reason
					for (int k = 1; k <= pendingChoiceNo; k++) {

						// audit IB user
						wait03.until(ExpectedConditions
								.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));

						// Select Pending Reason: only available for au & ky
						// VT & PUG don't have Pending Reason field, set index k to be pendingChoiceNo so it will not loop.
						switch (Brand) {
						case "au":
						case "ky":
						case "fca":
						case "vfsc":
						case "vfsc2":
						case "regulator2":
							t = new Select(driver.findElement(By.id("pending_reason")));
							t.selectByIndex(k);
							System.out.println("Chosen pending reason: " + t.getOptions().get(k).getText());
							Thread.sleep(1000);
							break;

						default:
							System.out.println(Brand + " has no Pending Reason field.");
						}

						// Input processed Notes
						driver.findElement(By.id("refuseReason")).sendKeys("Automatic Pending.");

						// Click Pending button
						driver.findElement(By.xpath(".//button[text()='pending']")).click();
						Thread.sleep(2000);

						resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Result is:" + resultMsg);
						System.out.println();
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

						// Clear the Submitted status query selection
						t = new Select(driver.findElement(By.id("statusQuery")));
						t.selectByValue("3");

						// Select Search Options
						driver.findElement(By.id("searchType")).click();
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
						driver.findElement(By.id("userQuery")).clear();
						driver.findElement(By.id("userQuery")).sendKeys(name);
						driver.findElement(By.id("query")).click();
						Thread.sleep(2000);

						// Click on audit icon
						driver.findElement(By.xpath("//i[@class='glyphicon glyphicon-cog']")).click();
						Thread.sleep(1000);
					}
					// Close the audit overlay
					driver.findElement(By.xpath("//button[@class='close']")).click();
					Thread.sleep(2000);
					// break the current loop and go back to parent loop
					break;

				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Account Audit Records for keyword " + keyWords);
				System.out.println();
			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditPendingIndi(String testEnv, String Brand, Method method) throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Pending audit account for individual");
		Select t;
		int pendingChoiceNo = 6;
		String name; // Used as temporary names
		String resultMsg;

		String[] keyWords = new String[] { Utils.webUserPrefix, Utils.addUserPrefix };

		int i, j, flag = 0;

		// VT doesn't have function to select Pending Reasons. So just pending once.
		if (Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			pendingChoiceNo = 1;
		}

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to audit additional accounts for Individuals...");
		System.out.println("-------------------------------------------------------------");

		for (i = 0; i < keyWords.length; i++) {

			if (flag == 1) {
				break;
			}
			Thread.sleep(3000);
			// Select Submitted status requests
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByValue("0");

			// Select Search Options
			driver.findElement(By.id("searchType")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for keywords " + keyWords[i]);
			} else {

				// Look for 1st Submitted records with name starting with pattern pat.

				for (j = 0; j < trs.size(); j++) {
					// Get user name
					name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
					Thread.sleep(2000);
					trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

					Thread.sleep(3000);

					if ((Utils.isWebUser(name) || Utils.isAddUser(name) || Utils.isJoint(name))) {
						// Select Pending reason
						for (int k = 1; k <= pendingChoiceNo; k++) {

							// audit individual user
							wait03.until(ExpectedConditions
									.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));

							// VT & PUG don't have pending reason field.

							if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg")) {

								t = new Select(driver.findElement(By.id("pending_reason")));
								t.selectByIndex(k);
								System.out.println("Chosen pending reason: " + t.getOptions().get(k).getText());
								Thread.sleep(1000);

							}

							// Input process notes
							driver.findElement(By.id("refuseReason")).sendKeys(";Automatic Pending");

							// Click Pending button
							driver.findElement(By.xpath(".//button[text()='pending']")).click();
							Thread.sleep(2000);

							resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By
											.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Result is:" + resultMsg);
							System.out.println();
							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

							// Clear the Submitted status query selection
							t = new Select(driver.findElement(By.id("statusQuery")));
							t.selectByValue("3");
							// Thread.sleep(waitIndex*3000);
							// Select Search Options
							driver.findElement(By.id("searchType")).click();
							wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name")))
									.click();
							driver.findElement(By.id("userQuery")).clear();
							driver.findElement(By.id("userQuery")).sendKeys(name);
							driver.findElement(By.id("query")).click();
							Thread.sleep(2000);

							// Click on audit icon
							driver.findElement(By.xpath("//i[@class='glyphicon glyphicon-cog']")).click();
							Thread.sleep(1000);
						}
						// Close the audit overlay
						driver.findElement(By.xpath("//button[@class='close']")).click();
						// break the current loop and go back to parent loop
						flag = 1;
						break;

					} else {
						System.out.println("Not an automation test user!");
					}
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
					System.out.println();
				}

			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditReject(String testEnv, String Brand, Method method) throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Reject audit account for individual");
		Select t;
		String name; // Used as temporary names

		String[] keyWords = new String[] { Utils.webUserPrefix, Utils.addUserPrefix, Utils.ibUserPrefix };

		int i, j, flag = 0;

		driver.navigate().refresh();
		
		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to reject audit accounts...");
		System.out.println("-------------------------------------------------------------");

		for (i = 0; i < keyWords.length; i++) {

			if (flag == 1) {
				break;
			}
			Thread.sleep(3000);
			// Select Submitted status requests
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByValue("0");

			// Select Search Options
			driver.findElement(By.id("searchType")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for keywords " + keyWords[i]);
			} else {

				// Look for 1st Submitted records with name starting with pattern pat.

				for (j = 0; j < trs.size(); j++) {
					// Get user name
					name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

					if (Utils.isWebUser(name) || Utils.isAddUser(name) || Utils.isJoint(name) || Utils.isTestIB(name)) {
						trs.get(j).findElement(By.cssSelector("td:last-child a")).click();
						Thread.sleep(2000);

						// Start reject audit
						wait03.until(ExpectedConditions
								.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));

						driver.findElement(By.id("refuseReason")).sendKeys("Automatic Reject");
						Thread.sleep(500);

						// Click Reject button
						driver.findElement(By.xpath(".//button[text()='reject']")).click();
						Thread.sleep(2000);

						String resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Result is:" + resultMsg);
						System.out.println();
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

						// break the current loop and go back to parent loop
						flag = 1;
						break;
					} else {
						System.out.println("Not an automation test user!");
					}
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
					System.out.println();
				}

			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void OfflineDepositAudit(String AdminURL, @Optional("") String TraderName) throws Exception {

		Select t;
		int channelNum = 0, j;
		String userName, order_no, account;
		String[] searchKey = new String[] {Utils.webUserPrefix, Utils.addUserPrefix};
		driver.navigate().to(AdminURL);
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		driver.findElement(By.linkText("Deposit Audit")).click();
		// driver.findElement(By.linkText("Deposit Audit")).click();

		// wait until the deposit list is fully loaded
		/*
		 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		 * wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		 */

		System.out.println("Start to Audit Offline Deposit Records...");
		System.out.println("-------------------------------------------------------------");

		if (TraderName.length() > 0) {
			searchKey = new String[] { TraderName.substring(0, TraderName.indexOf("@")) };
		}

		// Select Audit status
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Audit");

		// Select Offline Payment
		t = new Select(driver.findElement(By.id("typeQuery")));
		t.selectByVisibleText("Offline Payment");

		// Offline has a couple of deposit channels. Using a loop to audit one by one.

		t = new Select(driver.findElement(By.id("channelQuery")));
		Thread.sleep(500); // to give 0.5 second for options loading
		channelNum = t.getOptions().size() - 1; // Remove the top entry "Please Select"

		for (int i = 1; i <= channelNum; i++) {

			t = new Select(driver.findElement(By.id("channelQuery")));

			// i = channel number;
			t.selectByIndex(i);

			// Set Search Options
			wait03.until(ExpectedConditions.elementToBeClickable(By.id("searchType"))).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Name"))).click();

			for (int k = 0; k < searchKey.length; k++) {

				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
				driver.findElement(By.id("userQuery")).clear();
				driver.findElement(By.id("userQuery")).sendKeys(searchKey[k]);
				driver.findElement(By.id("query")).click();
				
				Thread.sleep(500);
				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

				List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

				// if the search result shows no records:
				if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
					System.out.println("No results for keyword " + searchKey[k] + " with Channel "
							+ t.getFirstSelectedOption().getText());
				} else {

					for (j = 0; j < trs.size(); j++) {

						// Read each row's userName
						userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2")).getText();

						// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
						if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
								|| Utils.isTestIB(userName)) {
							trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
							Thread.sleep(1000);

							// Check the order number
							WebElement orderNo = wait03.until(ExpectedConditions
									.visibilityOfElementLocated(By.xpath("//input[@id='Merchant_Order']")));
							order_no = ((JavascriptExecutor) driver)
									.executeScript("return arguments[0].value", orderNo).toString();
							WebElement accNo = driver.findElement(By.xpath("//input[@id='MT4_Account']"));
							account = ((JavascriptExecutor) driver)
									.executeScript("return arguments[0].value", accNo).toString();
							Utils.funcIsStringContains(order_no, account, "");

							// click deposit
							driver.findElement(By.xpath(".//button[text()='Deposit']")).click();

							System.out.println("Deposit Audit User " + userName + " with Channel "
									+ t.getFirstSelectedOption().getText());
							String resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
											"li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Operation Result:" + resultMsg);
							System.out.println();
							Thread.sleep(3000);
							break;
						}
						
					}

					if (j >= trs.size()) {
						System.out.println("No Qualified Deposit Audit Records for keyword " + searchKey[k]
								+ " with Channel " + t.getFirstSelectedOption().getText());
					}

				}

			}

		}
	}

	/*
	 * Refactoring by Yanni on 04/11/2019. This function will handle all Deposit audit except Thailand, Malaysia, Vietnam and Nigeria
	 */

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void OnlineDepositAudit(String AdminURL, @Optional("") String TraderName) throws Exception {

		for (OLDepositM channel : OLDepositM.values()) {
			if (channel != OLDepositM.Thai && channel != OLDepositM.Malay && channel != OLDepositM.Viet
					&& channel != OLDepositM.Nigeria) {
				funcOLDepositAuditbyChannel(AdminURL, TraderName, channel);
			}
		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WithdrawlAuditSearch(String AdminURL, @Optional("") String TraderName, String Brand) throws Exception {

		// Navigate to Withdrawl Audit 
		driver.navigate().to(AdminURL);
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		driver.findElement(By.linkText("Withdrawal Audit")).click();		

		Thread.sleep(2000);
		
		System.out.println();
		System.out.println("Start to click search on withdraw audit ...");
		System.out.println("-------------------------------------------------------------");

		for(int i=1; i<60; i++) {
			
			// Click button "All Types" to show all withdraw channels
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
		}

	}	

	
	
	
	/*
	 * Refactoring by Yanni on 04/11/2019. This function will handle all Withdraw audit except Thailand, Malaysia, Vietnam and Nigeria
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WithdrawlAudit(String AdminURL, @Optional("") String TraderName, String Brand) throws Exception {

		switch (Brand.toLowerCase()) {

		/*
		 * VT & PUG has only BankTransfer withdraw and UnionPay HuaAo withdraw. UnionPay HuaAo withdraw will be tested
		 * in a different profile.
		 */

		// Yanni on 21/05/2020: add Credit card withdraw for VT
		case "vt":
			funcWDAuditbyChannel(AdminURL, TraderName, WithdrawM.BankTransfer);
			funcWDAuditbyChannel(AdminURL, TraderName, WithdrawM.CC);
			break;
		case "fsa":
		case "svg":
			funcWDAuditbyChannel(AdminURL, TraderName, WithdrawM.BankTransfer);

			break;

		default:
			
			  for(WithdrawM channel: WithdrawM.values())
			  {
			  
				  if(channel != WithdrawM.Thai && channel != WithdrawM.Malay && channel != WithdrawM.Viet && channel != WithdrawM.Nigeria)
				  {
					  	funcWDAuditbyChannel(AdminURL, TraderName, channel);
				  }
			 
			  }			 

			//funcWDAuditbyChannel(AdminURL, TraderName, WithdrawM.BankTransfer);
		}

	}

	void funcWDCompletebyChannel(String AdminURL, String TraderName, WithdrawM channelInfo, String Brand)
			throws Exception {

		Select t;
		int j = 0;
		String userName;
		String searchKey = Utils.webUserPrefix;

		// Navigate to Withdrawl Audit module
		driver.navigate().to(AdminURL);
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();

		/*
		 * if(AdminURL.toLowerCase().startsWith("https://admin."))
		 * driver.findElement(By.linkText("Withdrawal Audit")).click();
		 * else
		 * driver.findElement(By.linkText("Withdrawl Audit")).click();
		 */

		// Click Withdrawal Audit/Withdrawl Audit
		clickWithdrawAuditMenu();

		// wait until the withdraw audit list is fully loaded
		/*
		 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		 * wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		 */

		System.out.println();
		System.out.println("Start to complete audit records with channel " + channelInfo.getName() + " ...");
		System.out.println("-------------------------------------------------------------");

		if (TraderName.length() > 0) {
			searchKey = TraderName.substring(0, TraderName.indexOf("@"));
		}

		// Get the number of Withdraw channel
		List<WebElement> channelType = driver
				.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li a"));

		// Click button "All Types" to show all withdraw channels
		driver.findElement(By.id("typeQuery")).click();

		// Select Withdraw Audit channel
		channelType = driver.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li"));

		// Yanni on 29/06/2020: channelInfo.getIndex() won't work any more as PUG&AU have different menu inex
		// channelType.get(channelInfo.getIndex()).findElement(By.tagName("a")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(channelInfo.getName()))).click();

		// Click Search Options to show options
		driver.findElement(By.id("searchType")).click();

		// Select search by Account Name
		driver.findElement(By.linkText("Account Name")).click();
		Thread.sleep(1000);

		// Input Account Name
		driver.findElement(By.id("userQuery")).sendKeys(searchKey);

		// Select Accepted status。 System will do Search when status changes
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Accepted");

		// Wait until the list is loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// trs holds the collection of Submitted withdraw records
		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println(
					"No results for Channel " + driver.findElement(By.cssSelector("button#typeQuery div")).getText());
		} else  // The search result shows records. Go through records one by one to pickup which one is the testing user.
		{

			// Read each row's userName
			userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();

			// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
			if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
					|| Utils.isTestIB(userName)) {

				// Click Complete button in the list
				trs.get(j).findElement(By.cssSelector("td:last-child a")).click();
				Thread.sleep(1000);

				funcWDCheckFee(Brand, channelInfo.getName());

				// Click Confirm button
				wait03.until(ExpectedConditions.elementToBeClickable(
						By.cssSelector("div.bootstrap-dialog-footer-buttons button.btn.btn-default:nth-of-type(1)")))
						.click();

				// Print any error message if there have
				String resultMsg = wait03.until(ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
				System.out.println("Audit Result:" + resultMsg);
				System.out.println();

				// Wait until the popup message disappears.
				wait03.until(ExpectedConditions.invisibilityOfElementLocated(
						By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

			} else {
				System.out.println("No available audit records for channel " + channelInfo.getName());
			}

		}

		if (j >= trs.size()) {
			System.out.println("No Qualified Withdraw Audit test records for " + "channel "
					+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
			System.out.println();
		}

	}

	// To activate emails with different pending reason, search Pending status requests and pending with different reasons.
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	void WithdrawlPendingAll() throws Exception {

		Select t, pendingReason;
		int channelNum = 0, i = 0, j = 0;
		int pendingChoiceNo = 6;
		String userName, notes;
		boolean flag = false;

		// Navigate to Withdrawl Audit module
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		/* driver.findElement(By.linkText("Withdrawl Audit")).click(); */

		// Click Withdrawal Audit/Withdrawl Audit
		clickWithdrawAuditMenu();

		// wait until the withdraw audit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println();
		System.out.println("Start to Pending withdraw records...");
		System.out.println("-------------------------------------------------------------");

		// Get the number of Withdraw channel
		List<WebElement> channelType = driver
				.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li a"));
		channelNum = channelType.size();

		// Loop every Deposit channel
		for (i = 1; i < channelNum; i++) {

			// Click button "All Types" to show all withdraw channels
			driver.findElement(By.id("typeQuery")).click();

			// Select Withdraw Audit channel
			channelType = driver.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li"));
			wait03.until(ExpectedConditions.visibilityOf(channelType.get(i).findElement(By.tagName("a")))).click();

			// Select Pending status
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByVisibleText("Pending");

			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			Thread.sleep(1000);
			// trs holds the collection of pending withdraw records
			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for Channel "
						+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
				System.out.println();
			} else  // The search result shows records. Go through records one by one to pickup which one is the testing user.
			{

				for (j = 0; j < trs.size(); j++) {

					// Read each row's userName
					userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();


						// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
						if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
							|| Utils.isTestIB(userName)) {

						// click Audit link

						trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
						Thread.sleep(500);

						for (int r = 0; r < pendingChoiceNo; r++) {

							// Select one Pending reason
							wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("pending_reason")));
							pendingReason = new Select(driver.findElement(By.id("pending_reason")));
							pendingReason.selectByIndex(r);
							notes = pendingReason.getFirstSelectedOption().getText();

							// Input Processed Notes
							driver.findElement(By.id("Processed_Notes")).clear();
							driver.findElement(By.id("Processed_Notes")).sendKeys(notes);

							Thread.sleep(3000);
							// Click Pending button
							driver.findElement(By.xpath(".//button[text()='Pending']")).click();

							System.out.println("Withdrawl Pending for User " + userName + " with reason " + notes);

							Thread.sleep(1000);

							String resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
											"li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Withdraw Pending Result:" + resultMsg);
							System.out.println();

							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
							// Thread.sleep(8000);

							if (r < pendingChoiceNo - 1) {
								// redo search with Account Name = userName
								// Click the button Search Options to show search options
								driver.findElement(By.id("searchType")).click();

								// Click Account Name
								wait03.until(
										ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Name")))
										.click();

								// Input userName as the search keyword
								driver.findElement(By.id("userQuery")).clear();
								driver.findElement(By.id("userQuery")).sendKeys(userName);
								driver.findElement(By.id("query")).click();
								wait03.until(ExpectedConditions
										.visibilityOfElementLocated(By.className("fixed-table-loading")));
								wait03.until(ExpectedConditions
										.invisibilityOfElementLocated(By.className("fixed-table-loading")));
								Thread.sleep(2000);
								trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
								trs.get(0).findElement(By.cssSelector("td:last-of-type a")).click();

								Thread.sleep(2000);
							}
						}

						flag = true;
						break;
					}
					
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Withdraw Audit test records for " + "channel "
							+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
					System.out.println();
				}

			}

			if (flag == true) {
				break;
			}
			// driver.navigate().refresh();

		}

	}

	// Search submitted requests, change one request to Audit.
	// Then search this Audit request and change it to pending with pending reason None. Only pending one request.
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	void WithdrawlPending() throws Exception {

		Select t, pendingReason;
		int channelNum = 0, i = 0, j = 0;
		String userName;
		boolean flag = false;

		// Navigate to Withdrawl Audit module
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		/* driver.findElement(By.linkText("Withdrawl Audit")).click(); */

		// Click Withdrawal Audit/Withdrawl Audit
		clickWithdrawAuditMenu();

		// wait until the withdraw audit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println();
		System.out.println("Start to Pending withdraw records...");
		System.out.println("-------------------------------------------------------------");

		// Get the number of Withdraw channel
		List<WebElement> channelType = driver
				.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li a"));
		channelNum = channelType.size();

		// Loop every Deposit channel
		for (i = 1; i < channelNum; i++) {

			// Click button "All Types" to show all withdraw channels
			driver.findElement(By.id("typeQuery")).click();

			// Select Withdraw Audit channel
			channelType = driver.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li"));
			wait03.until(ExpectedConditions.visibilityOf(channelType.get(i).findElement(By.tagName("a")))).click();

			// Select Submitted status
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByVisibleText("Submitted");

			// driver.findElement(By.id("query")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			// trs holds the collection of Submitted withdraw records
			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for Channel "
						+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
			} else  // The search result shows records. Go through records one by one to pickup which one is the testing user.
			{

				for (j = 0; j < trs.size(); j++) {

					// Read each row's userName
					userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();

					// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
					if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
							|| Utils.isTestIB(userName)) {
						trs.get(j).findElement(By.cssSelector("td:first-of-type input")).click();
						Thread.sleep(500);

						// Click startAudit button

						driver.findElement(By.cssSelector("div#startAudit")).click();

						System.out.println("Changed Withdraw Audit from Submitted to Audit for User " + userName
								+ " with Channel "
								+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());

						Thread.sleep(1000);
						// Click OK button to dismiss the popup dialog
						wait03.until(
								ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-dialog")));
						driver.findElement(By.xpath(".//button[text()='OK']")).click();

						String resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Status Change Result:" + resultMsg);
						System.out.println();

						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						// Thread.sleep(8000);

						// Choose Audit status

						// driver.navigate().refresh();
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("statusQuery")));
						t = new Select(driver.findElement(By.id("statusQuery")));
						t.selectByVisibleText("Audit");

						// Choose the same withdraw channel again
						driver.findElement(By.id("typeQuery")).click();
						channelType = driver.findElements(By.cssSelector("ul.dropdown-menu.bj li"));
						channelType.get(i).findElement(By.tagName("a")).click();

						driver.findElement(By.id("searchType")).click();
						driver.findElement(By.linkText("Account Name")).click();
						driver.findElement(By.id("userQuery")).clear();
						driver.findElement(By.id("userQuery")).sendKeys(userName);
						driver.findElement(By.id("query")).click();
						Thread.sleep(1000);

						// trs2 holds the collection of Audit records
						List<WebElement> trs2 = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

						// if the search result shows no records:
						if (trs2.size() > 1 || (trs2.size() == 1
								&& (!trs2.get(0).getAttribute("class").equals("no-records-found")))) {

							// click Audit link
							trs2.get(0).findElement(By.cssSelector("td:last-of-type a")).click();

							Thread.sleep(1000);
							System.out.println("Pending Withdraw Record for User " + userName + " with Channel "
									+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());

							// Select one Pending reason
							wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("pending_reason")));
							pendingReason = new Select(driver.findElement(By.id("pending_reason")));
							pendingReason.selectByIndex(0);
							String notes = pendingReason.getFirstSelectedOption().getText();

							// Input Processed Notes
							driver.findElement(By.id("Processed_Notes")).clear();
							driver.findElement(By.id("Processed_Notes")).sendKeys(notes);

							// Click Pending button
							Thread.sleep(500);

							driver.findElement(By.xpath("//button[text()='Pending']")).click();

							System.out.println("Pending for User " + userName + " with reason " + notes);

							Thread.sleep(1000);
							// Print any error message if there have
							resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
											"li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Pending Result:" + resultMsg);
							System.out.println();

							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
							Thread.sleep(8000);

							flag = true;

						} else {
							System.out.println("No available audit records for channel "
									+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
						}

						break;
					}
					
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Withdraw Audit test records for " + "channel "
							+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
					System.out.println();
				}

			}

			if (flag == true) {
				break;
			}

			driver.navigate().refresh();

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	void WithdrawlAuditReject() throws Exception {

		Select t;
		int j = 0;
		String userName;

		// Navigate to Withdrawl Audit module
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		/* driver.findElement(By.linkText("Withdrawl Audit")).click(); */

		// Click Withdrawal Audit/Withdrawl Audit
		clickWithdrawAuditMenu();

		// wait until the withdraw audit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println();
		System.out.println("Start to reject withdraw records...");
		System.out.println("-------------------------------------------------------------");

		// Select Submitted status
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Submitted");

		// driver.findElement(By.id("query")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// trs holds the collection of Submitted withdraw records
		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println(
					"No results for Channel " + driver.findElement(By.cssSelector("button#typeQuery div")).getText());
		} else  // The search result shows records. Go through records one by one to pickup which one is the testing user.
		{

			for (j = 0; j < trs.size(); j++) {

				// Read each row's userName
				userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();

				// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
					if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
							|| Utils.isTestIB(userName)) {
						trs.get(j).findElement(By.cssSelector("td:first-of-type input")).click();
						Thread.sleep(500);

						// Click startAudit button

						driver.findElement(By.cssSelector("div#startAudit")).click();

						System.out.println(
								"Changed Withdraw Audit from Submitted to Audit for User " + userName + " with Channel "
										+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());

						Thread.sleep(1000);
						// Click OK button to dismiss the popup dialog
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-dialog")));
						driver.findElement(By.xpath(".//button[text()='OK']")).click();

						String resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Status Change Result:" + resultMsg);
						System.out.println();

						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						// Thread.sleep(8000);

						// Choose Audit status

						// driver.navigate().refresh();
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("statusQuery")));
						t = new Select(driver.findElement(By.id("statusQuery")));
						t.selectByVisibleText("Audit");

						driver.findElement(By.id("searchType")).click();
						driver.findElement(By.linkText("Account Name")).click();
						driver.findElement(By.id("userQuery")).clear();
						driver.findElement(By.id("userQuery")).sendKeys(userName);
						driver.findElement(By.id("query")).click();
						Thread.sleep(1000);

						// trs2 holds the collection of Audit records
						List<WebElement> trs2 = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

						// if the search result shows no records:
						if (trs2.size() > 1 || (trs2.size() == 1
								&& (!trs2.get(0).getAttribute("class").equals("no-records-found")))) {

							// click Audit link
							trs2.get(0).findElement(By.cssSelector("td:last-of-type a")).click();

							Thread.sleep(1000);
							System.out.println("Reject Withdraw Record for User " + userName + " with Channel "
									+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
							driver.findElement(By.id("Processed_Notes")).sendKeys("reject");
							wait03.until(ExpectedConditions.elementToBeClickable(By.cssSelector(
									"div.bootstrap-dialog-footer-buttons button.btn.btn-default:nth-of-type(3)")))
									.click();

							// Print any error message if there have
							resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By
											.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Audit Result:" + resultMsg);
							System.out.println();

							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
							// Thread.sleep(8000);

						} else {
							System.out.println("No available audit records for channel "
									+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
						}

						break;
					} else {
						System.out.println("Not automation test user!");
					}
				
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Withdraw Audit test records for " + "channel "
						+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
				System.out.println();
			}

		}

		driver.navigate().refresh();

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AddAccAuditIndi(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;

		String[] keyWords = new String[] { "Web", "test" };

		int i, j;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Additional Account Audit"))).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to audit additional accounts for Individuals...");
		System.out.println("-------------------------------------------------------------");

		for (i = 0; i < keyWords.length; i++) {
			// Select Submitted status requests
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByValue("0");

			// Audit Web Registered User

			// Select Search Options
			driver.findElement(By.id("searchType")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No Additional Audit results for keywords " + keyWords[i]);
			} else {

				// Look for 1st Submitted records with name starting with pattern pat.

				for (j = 0; j < trs.size(); j++) {

					// Get user name
					name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

					if (Utils.isWebUser(name) || Utils.isAddUser(name) || Utils.isJoint(name)) {

						trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

						Thread.sleep(3000);

						// audit additional account request from individual user
						// Choose Commission Code

						if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg")) {
							t = new Select(driver.findElement(By.id("com_code")));
							Assert.assertTrue(t.getOptions().size() >= 1);
							t.selectByIndex(1); // Choose the first available commission code
						}

						// Choose Server
						t = new Select(driver.findElement(By.id("ib_server")));
						switch (Brand) {
						case "au":
						case "ky":
						case "vfsc":
						case "vfsc2":
						case "fca":
						case "regulator2":
						default:

							if (testEnv.equals("test")||testEnv.equals("alpha")) {
								t.selectByIndex(1);
							} else {
								Thread.sleep(500);
								t.selectByVisibleText(serverName);
							}

							break;

						case "vt":
						case "fsa":
						case "svg":
							t.selectByIndex(1);

							break;

						}
						Thread.sleep(4000);

						// Choose Account Group
						funcChooseTradeGroup(testEnv, Brand);
						// Choose Trading leverage
						t = new Select(driver.findElement(By.id("ib_Leverage")));
						Assert.assertTrue(t.getOptions().size() > 1);
						t.selectByIndex(4); // 100:1

						// Click Confirm button
						Thread.sleep(2000);
						driver.findElement(By.xpath(".//button[text()='Confirm']")).click();

						// Print message
						System.out.println("Audit Additional Account Request from Individual User " + name);
						resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Result is:" + resultMsg);
						System.out.println();
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						// break the current loop and go back to parent loop
						break;

					}
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Additional Account Audit Records for keyword " + keyWords[i]);
					System.out.println();
				} else {
					break;
				}

			}

		}
	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AddAccAuditIBTrade(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;
		String accountTrade[] = new String[] { "IB-Trading (individual)", "IB-Trading (company)" };
		// String accountRebate[]=new String[] {"IB-Rebate (individual)","IB-Rebate (company)"};

		String keyWords = Utils.ibUserPrefix;
		boolean flag = false;

		int i, j;

		serverName = "VFX UK2";

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Additional Account Audit"))).click();

		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("-------------------------------------------------------------");
		System.out.println("Start to audit additional trading accounts for IBs...");
		System.out.println("-------------------------------------------------------------");

		// search each type of IB trading account. If there is record in 1st category, then process 1st record and return.
		for (i = 0; i < accountTrade.length; i++) {
			// Select Submitted status requests
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByValue("0");

			// Select IB-Trading account type
			t = new Select(driver.findElement(By.id("typeQuery")));
			t.selectByVisibleText(accountTrade[i]);

			// Select Search Options
			driver.findElement(By.id("searchType")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(keyWords);
			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No Additional Trading Account Audit requests for keywords " + keyWords);
			} else {

				// Look for 1st Submitted records with name starting with pattern pat.

				for (j = 0; j < trs.size(); j++) {

					// Get user name
					name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

					if (Utils.isTestIB(name)) {

						trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

						// Thread.sleep(2000);
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("ib_Account_type")));

						// Can't audit MT5 account in testing env
						if (driver.findElement(By.id("mt4_account_type")).getText().contains("MT5")) {
							driver.findElement(By.cssSelector("div.bootstrap-dialog-close-button>button.close"))
									.click();
							Thread.sleep(1000);
						} else {

							// audit additional account request from individual user
							// Choose Commission Code (AU only)
							if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg")) {
								t = new Select(driver.findElement(By.id("com_code")));
								Assert.assertTrue(t.getOptions().size() >= 1);
								t.selectByIndex(1); // Choose the first available commission code
							}

							Thread.sleep(1000);

							// Choose Server
							t = new Select(driver.findElement(By.id("ib_server")));

							switch (Brand) {
							case "au":
							case "ky":
							case "vfsc":
							case "vfsc2":
							case "fca":
							case "regulator2":
								if (testEnv.equals("test")||testEnv.equals("test")) {
									t.selectByIndex(1);
								} else {
									t.selectByVisibleText(serverName);
								}

								break;

							case "vt":
							case "fsa":
							case "svg":
							default:
								t.selectByIndex(1);
								break;
							}

							Thread.sleep(1000);

							// Choose Account Group:
							// Choose Account Group
							funcChooseTradeGroup(testEnv, Brand);
							// Choose Trading leverage
							t = new Select(driver.findElement(By.id("ib_Leverage")));
							Assert.assertTrue(t.getOptions().size() > 1);
							t.selectByIndex(4); // 100:1

							// Click Confirm button
							Thread.sleep(2000);
							driver.findElement(By.xpath(".//button[text()='Confirm']")).click();

							// Print message
							System.out.println("Audit Additional Account Request from IB " + name);
							resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By
											.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Result is:" + resultMsg);
							System.out.println();
							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
							// break the current loop and go back to parent loop

							flag = true;
							break;
						}

					}
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Additional Account Audit Records for keyword " + keyWords);
					System.out.println();
				}

			}

			if (flag == true) {
				break;
			}
		}

	}

	/*
	 * @Test(dependsOnMethods="AdminLogIn",alwaysRun=true,invocationCount=6)
	 * 
	 * @Parameters(value= {"TestEnv","Brand","keyWords"}) public void
	 * AddAccAuditCP(String testEnv, String Brand, String keyWords, Method method)
	 * throws Exception { ExtentTestManager.startTest(method.getName()
	 * ,"Description: Audit additional account"); Select t; String name; //Used as
	 * temporary names String resultMsg; String type;
	 * 
	 * //String keyWords="webovh";
	 * 
	 * int j;
	 * 
	 * //Navigate to Account Audit page
	 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("Task Management"))).click();
	 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("Additional Account Audit"))).click();
	 * 
	 * Thread.sleep(1000);
	 * wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className(
	 * "fixed-table-loading")));
	 * 
	 * System.out.println("Start to audit additional accounts for Client...");
	 * System.out.println(
	 * "-------------------------------------------------------------");
	 * 
	 * 
	 * 
	 * //Select Submitted status requests t=new
	 * Select(driver.findElement(By.id("statusQuery"))); t.selectByValue("0");
	 * 
	 * 
	 * //Select Search Options driver.findElement(By.id("searchType")).click();
	 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("Client Name"))).click();
	 * driver.findElement(By.id("userQuery")).clear();
	 * driver.findElement(By.id("userQuery")).sendKeys(keyWords);
	 * driver.findElement(By.id("query")).click();
	 * 
	 * 
	 * wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className(
	 * "fixed-table-loading")));
	 * 
	 * List<WebElement>
	 * trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
	 * 
	 * //if the search result shows no records: if(trs.size()==1 &&
	 * trs.get(0).getAttribute("class").equals("no-records-found")) {
	 * System.out.println("No Additional Audit results for keywords " + keyWords);
	 * }else { System.out.println("trs.size() = " + trs.size()); //Look for 1st
	 * Submitted records with name starting with pattern pat.
	 * 
	 * for(j=0;j<trs.size();j++) {
	 * 
	 * //Get user name
	 * name=trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
	 * System.out.println("Entering j = " + j);
	 * 
	 * trs.get(j).findElement(By.cssSelector("td:last-child a")).click();
	 * 
	 * //Thread.sleep(2000);
	 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id(
	 * "ib_Account_type")));
	 * type=driver.findElement(By.id("ib_Account_type")).getAttribute("value").trim(
	 * );
	 * 
	 * //audit additional account request from individual user //Choose Commission
	 * Code (AU && KY only)
	 * 
	 * switch(Brand) { case "ky": case "au": case "vfsc": case "regulator2":
	 * 
	 * t=new Select(driver.findElement(By.id("com_code")));
	 * Assert.assertTrue(t.getOptions().size()>=1); t.selectByIndex(1); //Choose the
	 * first available commission code
	 * 
	 * break; }
	 * 
	 * Thread.sleep(1000);
	 * 
	 * 
	 * //Choose Account Type when auditing IB rebate account
	 * if(type.startsWith("IB-Rebate")) { t=new
	 * Select(driver.findElement(By.id("mt4_account_type"))); t.selectByIndex(1); }
	 * 
	 * //Choose Server t=new Select(driver.findElement(By.id("ib_server")));
	 * switch(Brand) { case "au": case "ky": if(testEnv.equals("test")) {
	 * t.selectByIndex(1); }else { t.selectByVisibleText(serverName); }
	 * 
	 * break;
	 * 
	 * case "vt": case "pug": default: t.selectByIndex(1); break; }
	 * 
	 * Thread.sleep(1000);
	 * 
	 * 
	 * //Account Group Setting //Choose Account Group funcChooseTradeGroup(testEnv,
	 * Brand);
	 * 
	 * //Choose Trading leverage t=new
	 * Select(driver.findElement(By.id("ib_Leverage")));
	 * Assert.assertTrue(t.getOptions().size()>1); t.selectByIndex(4); //100:1
	 * 
	 * 
	 * Thread.sleep(1000); //if mt5 type then reject
	 * System.out.println("mt4_account_type is: "
	 * +driver.findElement(By.id("mt4_account_type")).getText());
	 * //if(driver.findElement(By.id("mt4_account_type")).getText().
	 * equals("Standard STP - MT5")||driver.findElement(By.id("mt4_account_type")).
	 * getText().equals("Islamic STP - MT5")||driver.findElement(By.id(
	 * "mt4_account_type")).getText().equals("Raw ECN - MT5")) {
	 * if(driver.findElement(By.id("mt4_account_type")).getText().contains("MT5")) {
	 * driver.findElement(By.id("refuseReason")).sendKeys("reject");
	 * Thread.sleep(1000); //click reject button
	 * driver.findElement(By.xpath(".//button[text()='reject']")).click(); }else {
	 * //Click Confirm button Thread.sleep(2000);
	 * driver.findElement(By.xpath(".//button[text()='Confirm']")).click(); }
	 * 
	 * //Print message
	 * System.out.println("Audit Additional Account Request from Client " +name);
	 * resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).
	 * getText(); System.out.println("Result is:" + resultMsg);
	 * System.out.println();
	 * wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.
	 * cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
	 * //break the current loop and go back to parent loop break;
	 * 
	 * 
	 * }
	 * 
	 * if(j>=trs.size()) { System.out.
	 * println("No Qualified Additional Account Audit Records for keyword " +
	 * keyWords); System.out.println(); }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = "Brand")
	public void CommAudit(String Brand) throws Exception {

		int j = 0;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Commission Audit"))).click();

		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to audit Commission Requests...");
		System.out.println("-------------------------------------------------------------");

		Thread.sleep(1000);
		driver.findElement(By.id("status")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Submitted"))).click();

		// wait until the deposit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// Only for AU && KY: Change display settings from displaying 10 records to displaying 25 records
		if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {

			try {
				System.out.println("Changing page to display 25 records each page.");
				driver.findElement(By.cssSelector("button.btn.btn-default.dropdown-toggle")).click();
				wait03.until(ExpectedConditions.elementToBeClickable(By.linkText("25"))).click();

				// wait until the deposit list is fully loaded
				wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			} catch (ElementNotInteractableException e) {
				System.out.println("No enough records. No need to change display options.");
			}

		}

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Submitted Commission Results!");
		} else {

			for (j = 0; j < trs.size(); j++) {

				// Read each row's userName
				userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)")).getText();

				if (Utils.isTestIB(userName))  // to check if it is a qualified test IB user
				{

					trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
					Thread.sleep(1000);

					driver.findElement(By.id("comment")).sendKeys("Automation Test Rebate Audit");

					wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(".//button[text()='Confirm']")))
							.click();

					System.out.println("Commission Audit User " + userName);
					String resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Operation Result:" + resultMsg);
					System.out.println();
					// Thread.sleep(8000);
					break;

				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Commission Audit Records for Default Time Period.");
			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditAddClient(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;
		String[] keyWords = new String[] {Utils.ibUserPrefix, Utils.addUserPrefix, Utils.webUserPrefix, Utils.addUserPrefix };

		int i = 3, j;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		if (Utils.registerUserName != null) {
			if (!Utils.registerUserName.equals("")) {
				keyWords[2] = Utils.registerUserName;
			}
		}

		System.out.println("Start to audit accounts added by Admin...");
		System.out.println("-------------------------------------------------------------");

		// i=0, audit testcrmib** user ;
		// i=1, audit joint user
		// i=2: audit web individual user;
		// i=3, audit test*** individual user

		Thread.sleep(3000);
		// Select Submitted status requests
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByValue("0");

		// Audit Web Registered User

		// Select Search Options
		driver.findElement(By.id("searchType")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
		driver.findElement(By.id("query")).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No results for keywords " + keyWords[i]);
		} else {

			// Look for 1st Submitted records with name starting with pattern pat.

			for (j = 0; j < trs.size(); j++) {

				// Get user name
				name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isAddUser(name)) {
					Thread.sleep(2000);
					trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

					Thread.sleep(3000);

					// audit individual user

					// Keep default account owner

					// Choose Commission Code. AU && KY has commission code but VT doesn't have.
					if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
						t = new Select(driver.findElement(By.id("com_code")));
						Assert.assertTrue(t.getOptions().size() >= 1);
						t.selectByIndex(1); // Choose the first available commission code
					}

					// Choose Server
					t = new Select(driver.findElement(By.id("ib_server")));
					switch (Brand) {
					case "au":
					case "ky":
					case "vfsc":
					case "vfsc2":
					case "fca":
					case "regulator2":

						// If Server list has serverName, select serverName as server; otherwise, select the first one in the list as Server.
						if (funcHasServerName(t) == true) {
							t.selectByVisibleText(serverName);
						} else {
							t.selectByIndex(1);
						}
						break;

					case "vt":
					case "fsa":
					case "svg":
					default:
						t.selectByIndex(1);
						break;
					}
					Thread.sleep(1000);

					// Choose Account Group
					funcChooseTradeGroup(testEnv, Brand);

					// Choose Trading leverage
					t = new Select(driver.findElement(By.id("ib_Leverage")));
					Assert.assertTrue(t.getOptions().size() > 1);
					t.selectByIndex(4); // 100:1

					// Check if there are identity proof and address proof. If no, upload
					if (driver
							.findElements(By
									.cssSelector("div.form-group.passport div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input identity Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By.cssSelector(
								"div.form-group.passport div.col-md-8.pull-right.input-box.clearfix span a")).click();

					}

					// Check if there are World check. If no, upload
					if (driver
							.findElements(By.cssSelector(
									"div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						driver.findElement(By.xpath("//input[@type='file' and @data-name='worldCheck']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By.cssSelector(
								"div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix span a")).click();
					}

					// Click Confirm button
					Thread.sleep(2000);
					driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
					// Thread.sleep(waitIndex*3000);

					// Print message
					System.out.println("Audit Individual User " + name);
					resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Result is:" + resultMsg);
					System.out.println();
					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

					// break the current loop, only audit one account each time
					break;
				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
				System.out.println();
			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditJoint(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;

		String[] keyWords = new String[] {Utils.ibUserPrefix, Utils.addUserPrefix, Utils.webUserPrefix, Utils.addUserPrefix };

		int i = 1, j;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		if (Utils.addJointName != null) {
			if (!Utils.addJointName.equals("")) {
				keyWords[1] = Utils.addJointName;
			}
		}

		System.out.println("Start to audit accounts for joint users...");
		System.out.println("-------------------------------------------------------------");

		// i=0, audit testcrmib** user ;
		// i=1, audit joint user
		// i=2: audit web individual user;
		// i=3, audit test*** individual user

		Thread.sleep(3000);
		// Select Submitted status requests
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByValue("0");

		// Audit Web Registered User

		// Select Search Options
		driver.findElement(By.id("searchType")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
		driver.findElement(By.id("query")).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No results for keywords " + keyWords[i]);
		} else {

			// Look for 1st Submitted records with name starting with pattern pat.

			for (j = 0; j < trs.size(); j++) {

				// Get user name
				name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isJoint(name)) {
					Thread.sleep(2000);
					trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

					Thread.sleep(3000);

					// audit joint user
					// Set Account Owner as default

					// Choose Commission Code
					if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
						t = new Select(driver.findElement(By.id("com_code")));
						Assert.assertTrue(t.getOptions().size() >= 1);
						t.selectByIndex(1); // Choose the first available commission code
					}

					// Choose Server
					t = new Select(driver.findElement(By.id("ib_server")));
					switch (Brand) {

						case "vt":
						case "fsa":
						case "svg":
							t.selectByIndex(1);
							break;
							
						case "au":
						case "ky":
						case "vfsc":
						case "vfsc2":
						case "fca":
						case "regulator2":
						default:
	
							// If the server list has serverName, choose serverName; if it doesn't have, select the first one
							if (funcHasServerName(t) == true) {
								t.selectByVisibleText(serverName);
							} else {
								t.selectByIndex(1);
							}

					}

					// Choose Account Group
					funcChooseTradeGroup(testEnv, Brand);

					// Choose Trading leverage
					t = new Select(driver.findElement(By.id("ib_Leverage")));
					Assert.assertTrue(t.getOptions().size() > 1);
					t.selectByIndex(4); // 100:1

					// Check the 1st applicant if there are identity proof and address proof. If no, upload
					if (driver
							.findElements(By
									.cssSelector("div.form-group.passport1 div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input identity Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='passport1']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						Thread.sleep(1000);
						driver.findElement(By.cssSelector(
								"div.form-group.passport1 div.col-md-8.pull-right.input-box.clearfix span a")).click();
						Thread.sleep(1000);

					}

					// Check the 1st applicant for Address Proof. If not uploaded, upload. Otherwise ignore

					if (driver
							.findElements(
									By.cssSelector("div.form-group.upadd1 div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {

						// Input Address Proof
						driver.findElement(By.xpath("//input[@type='file' and @data-name='statement1']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						Thread.sleep(1000);
						driver.findElement(By
								.cssSelector("div.form-group.upadd1 div.col-md-8.pull-right.input-box.clearfix span a"))
								.click();
						Thread.sleep(1000);
					}

					// Input World check for applicant1
					driver.findElement(By.xpath("//input[@type='file' and @data-name='worldCheck1']"))
							.sendKeys(Utils.workingDir + "\\proof.png");
					Thread.sleep(1000);
					driver.findElement(By.cssSelector(
							"div.form-group.worldCheck1 div.col-md-8.pull-right.input-box.clearfix span a")).click();
					Thread.sleep(1000);

					// Check the 2nd applicant if there are identity proof and address proof. If no, upload
					if (driver
							.findElements(By
									.cssSelector("div.form-group.passport2 div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input identity Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='passport2']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						Thread.sleep(1000);
						driver.findElement(By.cssSelector(
								"div.form-group.passport2 div.col-md-8.pull-right.input-box.clearfix span a")).click();
						Thread.sleep(1000);

					}

					// Check the 2nd applicant for Address Proof. If not uploaded, upload. Otherwise ignore

					if (driver
							.findElements(
									By.cssSelector("div.form-group.upadd2 div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {

						// Input Address Proof
						driver.findElement(By.xpath("//input[@type='file' and @data-name='statement2']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						Thread.sleep(1000);
						driver.findElement(By
								.cssSelector("div.form-group.upadd2 div.col-md-8.pull-right.input-box.clearfix span a"))
								.click();
						Thread.sleep(1000);
					}

					// Input World check for applicant2
					driver.findElement(By.xpath("//input[@type='file' and @data-name='worldCheck2']"))
							.sendKeys(Utils.workingDir + "\\proof.png");
					Thread.sleep(1000);
					driver.findElement(By.cssSelector(
							"div.form-group.worldCheck2 div.col-md-8.pull-right.input-box.clearfix span a")).click();
					Thread.sleep(1000);

					// Click Confirm button
					Thread.sleep(2000);
					driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
					// Thread.sleep(waitIndex*3000);

					// Print message
					System.out.println("Audit joint User " + name);
					resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Result is:" + resultMsg);
					System.out.println();
					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
					// break the current loop and go back to parent loop
					break;

				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
				System.out.println();
			}

		}

	}
	
	private WebElement getErr()
	{
		WebElement err = null;
		try {
			err = driver.findElement(By.id("ib_server-error"));
		}
		catch(Exception e) {
			System.out.println("Do not find error message");
		}
		
			return err;
		
	}
	@Test(groups="sanity", dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditWeb(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;
		String[] keyWords = new String[] {Utils.ibUserPrefix, Utils.addUserPrefix, Utils.webUserPrefix, Utils.addUserPrefix };

		int i = 2, j;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		if (Utils.registerUserName != null) {
			if (!Utils.registerUserName.equals("")) {
				keyWords[2] = Utils.registerUserName;
			}
		}

		System.out.println("Start to audit accounts registered from Live Account...");
		System.out.println("-------------------------------------------------------------");

		// i=0, audit testcrmib** user ;
		// i=1, audit joint user
		// i=2: audit web individual user;
		// i=3, audit test*** individual user

		Thread.sleep(3000);
		// Select Submitted status requests
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByValue("0");

		// Audit Web Registered User

		// Select Search Options
		driver.findElement(By.id("searchType")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
		driver.findElement(By.id("query")).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No results for keywords " + keyWords[i]);
		} else {

			// Look for 1st Submitted records with name starting with pattern pat.

			for (j = 0; j < trs.size(); j++) {

				// Get user name
				name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isWebUser(name)) {
					Thread.sleep(2000);
					trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

					Thread.sleep(3000);

					// audit individual user

					// Keep default account owner

					// Choose Commission Code. AU/KY has commission code but VT doesn't have.
					if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
						t = new Select(driver.findElement(By.id("com_code")));
						Assert.assertTrue(t.getOptions().size() >= 1);
						t.selectByIndex(1); // Choose the first available commission code
					}

					// Choose Server
					t = new Select(driver.findElement(By.id("ib_server")));
					Thread.sleep(1000);
					
					for(int index = 1; index<= t.getOptions().size();index++)
					{
						t.selectByIndex(index);
						driver.findElement(By.id("ib_MAM_Number")).click();
						
						WebElement err = null;
						err = getErr();
						
						if(err == null || err.getText().trim().equals(""))
						{
							Thread.sleep(3000);
							break;
						}
					}

					// Choose Account Group
					funcChooseTradeGroup(testEnv, Brand);
					// Choose Trading leverage
					t = new Select(driver.findElement(By.id("ib_Leverage")));
					Assert.assertTrue(t.getOptions().size() > 1);
					t.selectByIndex(4); // 100:1

					// Check if there is identity proof. If no, upload
					if (driver
							.findElements(By
									.cssSelector("div.form-group.passport div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input identity Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By.cssSelector(
								"div.form-group.passport div.col-md-8.pull-right.input-box.clearfix span a")).click();

					}

					// Check if there is address proof. If no, upload
					if (driver
							.findElements(
									By.cssSelector("div.form-group.upadd div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input addr Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='statement']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By
								.cssSelector("div.form-group.upadd div.col-md-8.pull-right.input-box.clearfix span a"))
								.click();

					}

					// Check if there are World check. If no, upload
					if (driver
							.findElements(By.cssSelector(
									"div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input World check
						driver.findElement(By.xpath("//input[@type='file' and @data-name='worldCheck']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By.cssSelector(
								"div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix span a")).click();
					}

					// Click Confirm button
					Thread.sleep(2000);
					driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
					// Thread.sleep(waitIndex*3000);
					
					
					

					// Print message
					System.out.println("Audit Individual User " + name);
					resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Result is:" + resultMsg);
					System.out.println();
					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

					// break the current loop, only audit one account each time
					break;
				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
				System.out.println();
			}

		}

	}

	boolean funcHasServerName(Select t) {
		boolean flag = false;
		int i = 0;

		for (i = 0; i < t.getOptions().size(); i++) {
			if (t.getOptions().get(i).getText().equals(serverName)) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AddAccAuditIBRebate(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;
		String type;

		String accountRebate[] = new String[] { "IB-Rebate (individual)", "IB-Rebate (company)" };

		String keyWords = Utils.ibUserPrefix;
		boolean flag = false;

		int i, j;

		serverName = "VFX AU3";

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Additional Account Audit"))).click();

		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("-------------------------------------------------------------");
		System.out.println("Start to audit additional rebate accounts for IBs...");
		System.out.println("-------------------------------------------------------------");

		// search each type of IB trading account. If there is record in 1st category, then process 1st record and return.
		for (i = 0; i < accountRebate.length; i++) {
			// Select Submitted status requests
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByValue("0");

			// Select IB-Trading account type
			t = new Select(driver.findElement(By.id("typeQuery")));
			t.selectByVisibleText(accountRebate[i]);

			// Select Search Options
			driver.findElement(By.id("searchType")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(keyWords);
			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No Additional Trading Account Audit requests for keywords " + keyWords);
			} else {

				// Look for 1st Submitted records with name starting with pattern pat.

				for (j = 0; j < trs.size(); j++) {

					// Get user name
					name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

					if (Utils.isTestIB(name)) {

						trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

						// Thread.sleep(2000);
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("ib_Account_type")));
						type = driver.findElement(By.id("ib_Account_type")).getAttribute("value").trim();

						// audit additional account request from individual user
						// Choose Commission Code (AU only)
						if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
							t = new Select(driver.findElement(By.id("com_code")));
							Assert.assertTrue(t.getOptions().size() >= 1);
							t.selectByIndex(1); // Choose the first available commission code
						}

						Thread.sleep(1000);

						// Choose Account Type when auditing IB rebate account
						if (type.startsWith("IB-Rebate")) {
							t = new Select(driver.findElement(By.id("mt4_account_type")));
							t.selectByIndex(1);
						}

						// Choose Server
						t = new Select(driver.findElement(By.id("ib_server")));
						
						switch (Brand) {
							case "vt":
							case "fsa":
							case "svg":
							case "fca":
							case "au":
								t.selectByIndex(1);
								break;

							default:
								t.selectByVisibleText(serverName);
						}

						Thread.sleep(2000);

						// Choose Rebate Group
						t = new Select(driver.findElement(By.id("ib_mt4_set")));

						for (int count = 1; count < t.getOptions().size(); count++) {
							if (t.getOptions().get(count).getText().startsWith("TEST")) {
								t.selectByIndex(count);
								break;
							}
						}

						Thread.sleep(2000);
						// Select IB Groups
						driver.findElement(By.id("ib_IB_Grounp")).click();

						wait03.until(ExpectedConditions.visibilityOfElementLocated(
								By.cssSelector("input[type='checkbox'][value^='TEST_']"))).click();

						/*
						 * Input World Check File. It's a bug to show World Check for IB additional rebate account audit. but to test the function,
						 * add file upload here. When bug is fixed ,need to remove this part.
						 * 
						 * System.out.println("World Check filed should NOT be here. There is a bug VT-121 address this. ");
						 * Thread.sleep(1000);
						 * driver.findElement(By.xpath("//input[@data-name='worldCheck']")).sendKeys(Utils.workingDir+"\\proof.png");
						 * Thread.sleep(1000);
						 * driver.findElement(By.cssSelector("div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix span a")).click();
						 * Thread.sleep(1000);
						 */

						// Click Confirm button
						Thread.sleep(2000);
						driver.findElement(By.xpath(".//button[text()='Confirm']")).click();

						// Print message
						System.out.println("Audit Additional Account Request from IB " + name);
						resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Result is:" + resultMsg);
						System.out.println();
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						// break the current loop and go back to parent loop

						flag = true;
						break;

							
						

					}else {
						
						System.out.println("Not Test IB user.");
					}
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Additional Account Audit Records for keyword " + keyWords);
					System.out.println();
				}

			}

			if (flag == true) {
				break;
			}
		}

	}

	// This function searches all Pending requests and try to pending with different reasons. This is to try all pending reasons and activate corresponding emails.
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL" })
	void OfflineDepositPendingAll(String AdminURL) throws Exception {

		Select t;
		int channelNum = 0, j;
		int pendingChoiceNo = 6; // The number of pending reasons
		String userName, order_no, account;
		String[] searchKey = new String[] {Utils.webUserPrefix, Utils.addUserPrefix, Utils.ibUserPrefix };
		boolean flag = false;

		Thread.sleep(2000);
		driver.navigate().to(AdminURL);
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		driver.findElement(By.linkText("Deposit Audit")).click();

		// wait until the deposit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to Pending Offline Deposit Records...");
		System.out.println("-------------------------------------------------------------");

		// Select Audit status
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Pending");

		// Select Offline Payment
		t = new Select(driver.findElement(By.id("typeQuery")));
		t.selectByVisibleText("Offline Payment");

		// Offline has a couple of deposit channels. Using a loop to audit one by one.

		t = new Select(driver.findElement(By.id("channelQuery")));
		Thread.sleep(500); // to give 0.5 second for options loading
		channelNum = t.getOptions().size() - 1; // Remove the top entry "Please Select"

		for (int i = 1; i <= channelNum; i++) {

			t = new Select(driver.findElement(By.id("channelQuery")));

			// i = channel number;
			t.selectByIndex(i);

			// Set Search Options
			wait03.until(ExpectedConditions.elementToBeClickable(By.id("searchType"))).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Name"))).click();

			for (int k = 0; k < searchKey.length; k++) {

				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
				driver.findElement(By.id("userQuery")).clear();
				driver.findElement(By.id("userQuery")).sendKeys(searchKey[k]);
				driver.findElement(By.id("query")).click();

				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

				List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

				// if the search result shows no records:
				if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
					System.out.println("No results for keyword " + searchKey[k] + " with Channel "
							+ t.getFirstSelectedOption().getText());
				} else {

					for (j = 0; j < trs.size(); j++) {

						// Read each row's userName
						userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2")).getText();

							// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
							if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
									|| Utils.isTestIB(userName)) {

								String notes = "";
								Select pendingSelect;

								// Click Audit button
								trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();

								// Check the order number
								WebElement orderNo = wait03.until(ExpectedConditions
										.visibilityOfElementLocated(By.xpath("//input[@id='Merchant_Order']")));
								order_no = ((JavascriptExecutor) driver)
										.executeScript("return arguments[0].value", orderNo).toString();
								WebElement accNo = driver.findElement(By.xpath("//input[@id='MT4_Account']"));
								account = ((JavascriptExecutor) driver)
										.executeScript("return arguments[0].value", accNo).toString();
								Utils.funcIsStringContains(order_no, account, "");

								for (int r = 0; r < pendingChoiceNo; r++) {

									Thread.sleep(1000);

									// wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("pending_reason")));

									pendingSelect = new Select(driver.findElement(By.id("pending_reason")));

									// Choose a pending reason
									pendingSelect.selectByIndex(r);

									// Get the pending reason and pass it to Process Notes
									notes = pendingSelect.getFirstSelectedOption().getText();
									driver.findElement(By.id("Processed_Notes")).clear();
									driver.findElement(By.id("Processed_Notes")).sendKeys(notes);

									// Click Pending button
									driver.findElement(By.xpath(".//button[text()='Pending']")).click();

									System.out.println("Deposit Pending User " + userName + " with Pending Reason "
											+ pendingSelect.getFirstSelectedOption().getText());
									String resultMsg = wait03
											.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
													"li.messenger-message-slot div div.messenger-message-inner")))
											.getText();
									System.out.println("Operation Result:" + resultMsg);
									System.out.println();
									wait03.until(ExpectedConditions.invisibilityOfElementLocated(By
											.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

									// redo search with Account Name = userName
									// Click the button Search Options to show search options

									if (r < pendingChoiceNo - 1) {
										driver.findElement(By.id("searchType")).click();

										// Click Account Name
										wait03.until(ExpectedConditions
												.visibilityOfElementLocated(By.linkText("Account Name"))).click();

										// Input userName as the search keyword
										driver.findElement(By.id("userQuery")).clear();
										driver.findElement(By.id("userQuery")).sendKeys(userName);
										driver.findElement(By.id("query")).click();
										Thread.sleep(500);
										wait03.until(ExpectedConditions
												.invisibilityOfElementLocated(By.className("fixed-table-loading")));
										trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
										trs.get(0).findElement(By.cssSelector("td:last-of-type a")).click();

									}
								}

								flag = true;
								break;
							}
					
					}

					if (j >= trs.size()) {
						System.out.println("No Qualified Deposit Audit Records for keyword " + searchKey[k]
								+ " with Channel " + t.getFirstSelectedOption().getText());
						System.out.println();
					}

				}

				// If all pending reasons have been tried, break the loop
				if (flag == true) {
					break;
				}

			}

			// If all pending reasons have been tried, break the loop
			if (flag == true) {
				break;
			}
		}
	}

	// It searches all submitted request and change its status from submitted to pending. Only one request is processed.
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	void OfflineDepositPending() throws Exception {

		Select t, pendingSelect;
		int channelNum = 0, j;
		String userName, notes, order_no, account;
		String[] searchKey = new String[] { Utils.webUserPrefix, Utils.addUserPrefix };
		boolean flag = false;

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		driver.findElement(By.linkText("Deposit Audit")).click();

		// wait until the deposit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to Audit Offline Deposit Records...");
		System.out.println("-------------------------------------------------------------");

		// Select Audit status
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Audit");

		// Select Offline Payment
		t = new Select(driver.findElement(By.id("typeQuery")));
		t.selectByVisibleText("Offline Payment");

		// Offline has a couple of deposit channels. Using a loop to audit one by one.

		t = new Select(driver.findElement(By.id("channelQuery")));
		Thread.sleep(500); // to give 0.5 second for options loading
		channelNum = t.getOptions().size() - 1; // Remove the top entry "Please Select"

		// Added by Yanni: allow more time for channel loading.
		while (channelNum == 0) {
			Thread.sleep(1000);
		}
		System.out.println(channelNum);
		for (int i = 1; i <= channelNum; i++) {

			System.out.println("Entering loop " + i);

			t = new Select(driver.findElement(By.id("channelQuery")));

			// i = channel number;
			t.selectByIndex(i);

			// Set Search Options
			wait03.until(ExpectedConditions.elementToBeClickable(By.id("searchType"))).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Name"))).click();

			for (int k = 0; k < searchKey.length; k++) {

				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
				driver.findElement(By.id("userQuery")).clear();
				driver.findElement(By.id("userQuery")).sendKeys(searchKey[k]);
				driver.findElement(By.id("query")).click();

				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

				List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

				// if the search result shows no records:
				if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
					System.out.println("No results for keyword " + searchKey[k] + " with Channel "
							+ t.getFirstSelectedOption().getText());
				} else {

					for (j = 0; j < trs.size(); j++) {

						// Read each row's userName
						userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2")).getText();

							// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
							if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
									|| Utils.isTestIB(userName)) {
								trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
								Thread.sleep(1000);

								// Check the order number
								WebElement orderNo = wait03.until(ExpectedConditions
										.visibilityOfElementLocated(By.xpath("//input[@id='Merchant_Order']")));
								order_no = ((JavascriptExecutor) driver)
										.executeScript("return arguments[0].value", orderNo).toString();
								WebElement accNo = driver.findElement(By.xpath("//input[@id='MT4_Account']"));
								account = ((JavascriptExecutor) driver)
										.executeScript("return arguments[0].value", accNo).toString();
								Utils.funcIsStringContains(order_no, account, "");

								// wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("pending_reason")));

								pendingSelect = new Select(driver.findElement(By.id("pending_reason")));

								// Choose a pending reason
								pendingSelect.selectByIndex(0);

								// Get the pending reason and pass it to Process Notes
								notes = pendingSelect.getFirstSelectedOption().getText();
								driver.findElement(By.id("Processed_Notes")).clear();
								driver.findElement(By.id("Processed_Notes")).sendKeys(notes);

								// Click Pending button
								driver.findElement(By.xpath(".//button[text()='Pending']")).click();

								System.out.println("Pending User " + userName + " with reason " + notes);
								String resultMsg = wait03
										.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
												"li.messenger-message-slot div div.messenger-message-inner")))
										.getText();
								System.out.println("Operation Result:" + resultMsg);
								System.out.println();
								Thread.sleep(3000);

								flag = true;
								break;
							}
						
					}

					if (j >= trs.size()) {
						System.out.println("No Qualified Deposit Audit Records for keyword " + searchKey[k]
								+ " with Channel " + t.getFirstSelectedOption().getText());
					}

				}

				if (flag == true) {
					break;
				}
			}

			if (flag == true) {
				break;
			}
		}
	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = "Brand")
	public void leverageAudit(String Brand) throws Exception {

		Assert.assertTrue(funcleverageChange("Confirm"), "Failed to change leverage.");

	}

	@Parameters({ "AdminURL", "AdminName", "AdminPass", "Brand", "TestEnv" })
	@Test(priority = 0)
	public void APIResendEmail(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv,
			Method method) throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Resend Email");
		driver.get(AdminURL);
		String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);

		// remove the admin/main from AdminURL
		String url = Utils.ParseInputURL(AdminURL);

		// Get the email list. Return json body in String format.
		String mailLog = RestAPI.testPostMailLogList(url, cookie);

		JSONParser parser = new JSONParser();
		Object resultObject = parser.parse(mailLog);
		JSONObject obj = (JSONObject) resultObject;

		JSONArray array = (JSONArray) obj.get("rows");
		for (Object object : array) {
			JSONObject item = (JSONObject) object;
			System.out.println("=====================\n item: " + item.toJSONString());

			// Get email id
			Object id = item.get("id");
			String uid = id.toString();
			// System.out.println("user_id is: " + uid);
			// Get email's creating time
			String createTime = (String) item.get("createTime");
			// System.out.println("createTime is: " + createTime);
			// Get email's To address
			Object mail = item.get("toMail");
			String toMail = mail.toString();

			// Get current date
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateFormatted = dateFormat.format(new Date());
			// System.out.println("dateFormatted is: " + dateFormatted);

			// Only resend emails of current date, and recipient is not shan.liu@ and Jaden.chang@
			if (createTime.contains(dateFormatted) && !toMail.equals("3nCE/eUCihWf32EC7GRpyb+JPzlAQBBE")
					&& !toMail.equals("pq+kMdkTEL7TvmCqAMpVKXXo547YgZXfBfelVVf/gFY=")) {
				System.out.println("---->>>Sending email matches current date");
				RestAPI.testPostResendMailSubmit(url, cookie, uid,"", "");
			} else {
				System.out.println("No email matches current date");
			}
		}
	}

	public boolean funcleverageChange(String operation) throws Exception {
		// operation should be exactly the button text in Leverage Audit dialog: Confirm, Pending, Reject

		int j = 0;
		Select t;
		String strPath;

		boolean flag = false;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Leverage Audit"))).click();

		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to audit Leverage Change Requests...");
		System.out.println("-------------------------------------------------------------");

		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Submitted");
		driver.findElement(By.id("query")).click();
		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Submitted Leverage Change Requests");
		} else {

			for (j = 0; j < trs.size(); j++) {

				// Read each row's userName
				userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isTestIB(userName) || Utils.isWebUser(userName) || Utils.isJoint(userName)
						|| Utils.isAddUser(userName))  // to check if it is a qualified test IB user
				{

					System.out.print("Account: ");
					System.out.print(trs.get(j).findElement(By.cssSelector("td:nth-of-type(3")).getText());

					trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
					Thread.sleep(1000);

					System.out.print(" New Leverage: "
							+ new Select(driver.findElement(By.id("leverage"))).getFirstSelectedOption().getText()
							+ "\n");
					driver.findElement(By.id("comment"))
							.sendKeys("Automation Test Leverage Audit for user " + userName);

					strPath = ".//button[text()='" + operation + "']";
					wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(strPath))).click();

					System.out.println(operation + " Leverage Change Request for user " + userName);
					String resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Operation Result:" + resultMsg);
					System.out.println();

					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

					// Thread.sleep(8000);

					flag = true;
					break;

				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Leverage Change Audit Records.");
			}

		}

		return flag;

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = "Brand")
	public void leveragePending(String Brand) throws Exception {

		Assert.assertTrue(funcleverageChange("Pending"), "Failed to change leverage.");

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = "Brand")
	public void leverageReject(String Brand) throws Exception {

		Assert.assertTrue(funcleverageChange("Reject"), "Failed to change leverage.");

	}

	/*
	 * Author: Yanni Qi
	 * Date: 01/07/2019
	 * This function is used for UnionPay Bank card information audit.
	 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
	 * Operation={"Complete/Pending/Reject"}
	 */
	public boolean funcWDDetailOperation(String TestEnv, String Brand, String Operation) throws Exception {
		// Navigate to withdrawal Details Audit page

		int j = 0;
		Select t;
		String strPath;
		List<String> auditStatus = new ArrayList<String>();

		boolean flag = false;

		auditStatus.add("All Status");
		auditStatus.add("Submitted");
		auditStatus.add("Pending");
		auditStatus.add("Completed");
		auditStatus.add("Rejected");

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Withdrawal Details"))).click();
		// wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Financial Information Audit"))).click();

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to " + Operation + " audit Withdrawal Details Requests...");
		System.out.println("-------------------------------------------------------------");

		t = new Select(driver.findElement(By.id("statusQuery")));

		// Assert all status listed in the dropdown list: Submitted, Pending, Completed, Rejected

		for (j = 0; j < t.getOptions().size(); j++) {
			Assert.assertTrue(auditStatus.contains(t.getOptions().get(j).getText()),
					"Status is not listed as expected: " + t.getOptions().get(j).getText());
		}

		Assert.assertEquals(t.getOptions().size(), auditStatus.size(), "Status list has more status than expected.");

		System.out.println("Withdraw Details Status List check passed.");

		t.selectByVisibleText("Submitted");
		driver.findElement(By.id("query")).click();
		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Submitted Withdraw Details Records.");
		} else {

			for (j = 0; j < trs.size(); j++) {

				// Read each row's userName
				userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isTestIB(userName) || Utils.isWebUser(userName) || Utils.isJoint(userName)
						|| Utils.isAddUser(userName))  // to check if it is a qualified test IB user
				{

					trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
					Thread.sleep(1000);

					// Edit Withdraw Details Information by OP
					funcUPWdUpdate();

					// Cannot find Processed Notes textarea directly and use TAB of the previous input field
					/*
					 * driver.findElement(By.id("ProcessedNotes")).sendKeys("Automation " + Operation + " Test Withdraw Details for user "+userName
					 * + " "+Utils.randomSCString(10));
					 */

					driver.findElement(By.id("nationalID")).sendKeys(Keys.TAB, "Automation " + Operation
							+ " Test Withdraw Details for user " + userName + " " + Utils.randomSCString(10));

					strPath = ".//button[text()='" + Operation + "']";
					wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(strPath))).click();

					System.out.println(Operation + " Withdraw Details for user " + userName);
					String resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Operation Result:" + resultMsg);
					System.out.println();

					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

					// Check the user's withdraw details request status: should be the same as the operation. If different, it will fail
					String newStatus = funcWDDetailsCheckStatus(userName, Operation);

					Assert.assertTrue(newStatus.contains(Operation),
							"Withdraw Details Status is not changed to " + Operation + " as expected.");

					flag = true;
					break;

				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Withdrawl Details Audit Records.");
			}

		}

		return flag;

	}

	/*
	 * Author: Yanni Qi
	 * Date: 02/07/2019
	 * This function is used to return the status of a Withdraw Details specified for one user
	 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
	 * Operation={"Complete/Pending/Reject"}
	 */
	public String funcWDDetailsCheckStatus(String userName, String Operation) throws Exception {
		String newStatus = "";

		Select t;

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		t = new Select(driver.findElement(By.id("statusQuery")));

		switch (Operation) {
		case "Complete":
			t.selectByVisibleText("Completed");
			break;

		case "Pending":
			t.selectByVisibleText("Pending");
			break;

		case "Reject":

			t.selectByVisibleText("Rejected");
			break;

		default:
			System.out.println("Operation is not in Complete/Pending/Reject status list.");
		}

		// Select Search Options: Account Name
		driver.findElement(By.id("searchType")).click();
		driver.findElement(By.linkText("Account Name")).click();

		// Input userName
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(userName);

		// Click button
		driver.findElement(By.id("query")).click();
		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("Can't find any records for user" + userName + " with status " + Operation);
		} else {
			// Get the Operation of latest record
			newStatus = trs.get(0).findElement(By.cssSelector("td:nth-of-type(7)")).getText();
			System.out.println("Check New Status. New Status is " + newStatus);
		}

		return newStatus;
	}

	/*
	 * Author: Yanni Qi
	 * Date: 02/07/2019
	 * This function is used to Audit (Complete) a Submitted Withdraw Details request
	 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
	 * Operation={"Complete/Pending/Reject"}
	 */

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv" })

	public void WithdrawDetailsAudit(String Brand, String TestEnv) throws Exception {

		String Operation = "Complete";
		Assert.assertTrue(funcWDDetailOperation(Brand, TestEnv, Operation), "Failed to audit Withdraw Details.");
	}

	/*
	 * Author: Yanni Qi
	 * Date: 02/07/2019
	 * This function is used to Pending a Submitted Withdraw Details request
	 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
	 * Operation={"Complete/Pending/Reject"}
	 */

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv" })

	public void WithdrawDetailsPending(String Brand, String TestEnv) throws Exception {

		String Operation = "Pending";
		Assert.assertTrue(funcWDDetailOperation(Brand, TestEnv, Operation), "Failed to pending Withdraw Details.");
	}

	/*
	 * Author: Yanni Qi
	 * Date: 02/07/2019
	 * This function is used to Reject a Submitted Withdraw Details request
	 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
	 * Operation={"Complete/Pending/Reject"}
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv" })

	public void WithdrawDetailsReject(String Brand, String TestEnv) throws Exception {

		String Operation = "Reject";
		Assert.assertTrue(funcWDDetailOperation(Brand, TestEnv, Operation), "Failed to reject Withdraw Details.");
	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void AccountAuditWebMT5(String testEnv, String Brand) throws Exception {

		Select t;
		String name; // Used as temporary names
		String resultMsg;
		String[] keyWords = new String[] {Utils.ibUserPrefix, Utils.addUserPrefix, Utils.webUserPrefix, Utils.addUserPrefix };
		Random r=new Random();
		
		int i = 2, j;
		serverName = "VFX MT5";

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Audit"))).click();

		Thread.sleep(waitIndex * 4000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		if (Utils.registerUserName != null) {
			if (!Utils.registerUserName.equals("")) {
				keyWords[2] = Utils.registerUserName;
			}
		}

		System.out.println("Start to audit MT5 accounts registered from Live Account...");
		System.out.println("-------------------------------------------------------------");

		// i=0, audit testcrmib** user ;
		// i=1, audit joint user
		// i=2: audit web individual user;
		// i=3, audit test*** individual user

		Thread.sleep(3000);

		// Select Submitted status requests
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByValue("0");

		// Select Search Options
		driver.findElement(By.id("searchType")).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Client Name"))).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
		driver.findElement(By.id("query")).click();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No MT5 results for keywords " + keyWords[i]);
		} else {

			// Look for 1st Submitted records with name starting with pattern pat.

			for (j = 0; j < trs.size(); j++) {

				// Get user name
				name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

				if (Utils.isWebUser(name)) {
					Thread.sleep(2000);
					trs.get(j).findElement(By.cssSelector("td:last-child a")).click();

					Thread.sleep(3000);

					// Start to audit individual user

					// Keep default account owner

					// Choose Commission Code. AU/KY has commission code but VT doesn't have.
					if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("fsa")&& !Brand.equalsIgnoreCase("svg")) {
						t = new Select(driver.findElement(By.id("com_code")));
						Assert.assertTrue(t.getOptions().size() >= 1);
						t.selectByIndex(1); // Choose the first available commission code
					}

					// Choose Server
					t = new Select(driver.findElement(By.id("ib_server")));
					switch (Brand) {
					case "au":
					case "ky":
					case "vfsc":
					case "vfsc2":
					case "fca":
					case "regulator2":
					default:
						// If Server list has serverName, select serverName as server; otherwise, select the first one in the list as Server.
						if (funcHasServerName(t) == true) {
							t.selectByVisibleText(serverName);
						} else {
							Assert.assertTrue(false,
									"Trying to select MT5 Server " + serverName + " but CAN'T find it in the list.");
						}
						break;

					case "vt":
						if (funcHasServerName(t) == true) {
							t.selectByVisibleText("VTMarkets-MT5");
						} else {
							Assert.assertTrue(false,
									"Trying to select MT5 Server " + serverName + " but CAN'T find it in the list.");
						}
						break;
					case "fsa":
					case "svg":
						if (funcHasServerName(t) == true) {
							t.selectByVisibleText("PUG-Live MT5");
						} else {
							Assert.assertTrue(false,
									"Trying to select MT5 Server " + serverName + " but CAN'T find it in the list.");
						}
						break;
					}
					Thread.sleep(1000);

					// Choose Account Group
					funcChooseTradeGroup(testEnv, Brand);
					
					// Choose Trading leverage
					t = new Select(driver.findElement(By.id("ib_Leverage")));
					Assert.assertTrue(t.getOptions().size() > 1);
					t.selectByIndex(4); // 100:1

					// Check if there is identity proof. If no, upload
					if (driver
							.findElements(By
									.cssSelector("div.form-group.passport div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input identity Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By.cssSelector(
								"div.form-group.passport div.col-md-8.pull-right.input-box.clearfix span a")).click();

					}

					// Check if there is address proof. If no, upload
					if (driver
							.findElements(
									By.cssSelector("div.form-group.upadd div.col-md-8.pull-right.input-box.clearfix"))
							.size() == 1) {
						// Input addr Proof

						driver.findElement(By.xpath("//input[@type='file' and @data-name='statement']"))
								.sendKeys(Utils.workingDir + "\\proof.png");
						driver.findElement(By
								.cssSelector("div.form-group.upadd div.col-md-8.pull-right.input-box.clearfix span a"))
								.click();

					}

					// Input World check
					driver.findElement(By.xpath("//input[@type='file' and @data-name='worldCheck']"))
							.sendKeys(Utils.workingDir + "\\proof.png");
					driver.findElement(By
							.cssSelector("div.form-group.worldCheck div.col-md-8.pull-right.input-box.clearfix span a"))
							.click();

					// Click Confirm button
					Thread.sleep(2000);
					driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
					// Thread.sleep(waitIndex*3000);

					// Print message
					System.out.println("Audit Individual User " + name);
					resultMsg = wait03
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
							.getText();
					System.out.println("Result is:" + resultMsg);
					System.out.println();
					wait03.until(ExpectedConditions.invisibilityOfElementLocated(
							By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));

					// break the current loop, only audit one account each time
					break;
				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified Account Audit Records for keyword " + keyWords[i]);
				System.out.println();
			}

		}

	}

	/*
	 * Author: Yanni Qi
	 * Date: 12/07/2019
	 * This function is used for the dropdown fields check in UnionPay Withdraw record audit list.
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand" })
	public void UPWithdrawAuditList(String TestEnv, String Brand) throws Exception {
		// Navigate to UnionPay withdrawal Audit page

		List<WebElement> t;
		List<String> auditStatus = new ArrayList<String>();

		// Navigate to UnionPay Withdrawl Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("UnionPay Withdrawal"))).click();

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to check UnionPay Withdraw Audit List...");
		System.out.println("-------------------------------------------------------------");

		auditStatus.add("All Status");
		auditStatus.add("Pending");
		auditStatus.add("Accepted");
		auditStatus.add("Completed");
		auditStatus.add("Withdrawal Failed");
		auditStatus.add("Cancelled");
		auditStatus.add("Cancel Failed");

		// Assert all status listed in the Status dropdown list
		t = new Select(driver.findElement(By.id("statusQuery"))).getOptions();
		Thread.sleep(500);
		Assert.assertTrue(t.size() > 1, "UnionPay Withdrawal Audit Status List has only " + t.size() + " status.");
		funcCheckList(t, auditStatus, "Status");

		// Check Withdraw channel types
		auditStatus.clear();
		auditStatus.add("All Types");
		auditStatus.add("Union Pay");
		auditStatus.add("Bank Transfer(China)");

		driver.findElement(By.id("typeQuery")).click();
		t = driver.findElements(By.cssSelector("div.button_select.btn-group.open>ul.dropdown-menu.bj a"));
		Assert.assertTrue(t.size() > 1, "UnionPay Withdrawal Channel List has only " + t.size() + " channels.");
		funcCheckList(t, auditStatus, "Channel Type");
		driver.findElement(By.id("typeQuery")).click();

		// Check Search options
		auditStatus.clear();
		auditStatus.add("Search Options");
		auditStatus.add("Email");
		auditStatus.add("Account");
		auditStatus.add("Order Number");
		auditStatus.add("CPS Order Number");
		auditStatus.add("Risk Group");
		auditStatus.add("MAM Number");

		driver.findElement(By.id("searchType")).click();
		t = driver.findElements(By.cssSelector("div.button_select.btn-group.open>ul.dropdown-menu.bj a"));
		Assert.assertTrue(t.size() > 1, "UnionPay Withdrawal Search Options List has only " + t.size() + " status.");
		funcCheckList(t, auditStatus, "Search Options");
		driver.findElement(By.id("searchType")).click();

		// Check search by date
		auditStatus.clear();
		auditStatus.add("Search by Date");
		auditStatus.add("Create Date");
		auditStatus.add("Update Date");
		t = driver.findElements(By.cssSelector("select#dateType option"));
		Assert.assertTrue(t.size() > 1, "UnionPay Withdrawal Search Date List has only " + t.size() + " status.");
		funcCheckList(t, auditStatus, "Search Date");
	}

	public void funcCheckList(List<WebElement> t, List<String> auditStatus, String operation) {
		int j;

		for (j = 0; j < t.size(); j++) {
			Assert.assertTrue(auditStatus.contains(t.get(j).getText()),
					"Status is not listed as expected: " + t.get(j).getText());
		}

		Assert.assertEquals(t.size(), auditStatus.size(), "Status list has more status than expected.");

		System.out.println("      " + operation + " List check passed.");

	}

	/*
	 * Developed by Yanni for common operations of UnionPay withdraw Audit on 15/07/2019
	 */
	public String funcUPWdOperation(String Brand, String AdminURL, String originStatus, String operation,
			String TraderName) throws Exception {
		String accountNo = "", strPath, userName, cpsOrderNo, accountType;
		Select t;
		int j = 0;

		BigDecimal oldBalance = new BigDecimal("0.00"), newBalance = new BigDecimal("0.00"),
				applyAmount = new BigDecimal("0.00");

		String cookie = driver.manage().getCookieNamed("jsId").toString();
		HashMap<String, String> result = new HashMap<String, String>();

		strPath = "//button[text()='" + operation + "']";

		if (originStatus == null || originStatus.length() == 0) {
			System.out.println("Original Status is not a qualified status. It's either NULL or EMPTY.");
			return accountNo;
		}

		if (operation == null || operation.length() == 0) {
			System.out.println("Operation is not a qualified operation. It's either NULL or EMPTY.");
			return accountNo;
		}

		driver.navigate().refresh();

		// Navigate to UnionPay Withdrawl Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("UnionPay Withdrawal"))).click();

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to " + operation + " UnionPay Withdraw Audit ...");
		System.out.println("-------------------------------------------------------------");

		// Filtering records with originStatus
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText(originStatus);

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// If TraderName is provided, using TraderName as email to filter record
		if (TraderName != null && TraderName.length() > 0) {

			driver.findElement(By.id("searchType")).click();
			driver.findElement(By.linkText("Email")).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(TraderName);
			driver.findElement(By.id("query")).click();

		}

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// Select the top one record and click
		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Records in status " + originStatus);
		} else {

			for (j = 0; j < trs.size(); j++) {
				// Read each row's userName
				userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();

				// If it is a qualified test user, click Actions in the last cell and operate. Break the loop.
				if (Utils.isTestIB(userName) || Utils.isWebUser(userName) || Utils.isJoint(userName)
						|| Utils.isAddUser(userName)) {
					trs.get(0).findElement(By.cssSelector("td:nth-last-of-type(1) >a")).click();
					Thread.sleep(500);

					// Wait until the dialog pops up
					wait03.until(ExpectedConditions
							.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));

					// Get AccountNo.
					accountNo = driver.findElement(By.id("mt4Account_no")).getAttribute("value");

					// Get Account Type
					accountType = driver.findElement(By.id("mt4Account_type_display")).getAttribute("value");

					// Get oldBalance

					if (accountType.equals("交易")) {
						result = RestAPI.funcGetTradingAccInfo(Brand, AdminURL, accountNo, cookie);
					} else if (accountType.equals("返佣")) {
						result = RestAPI.funcGetRebateAccInfo(Brand, AdminURL, accountNo, cookie);
					}

					if (result.get("balance") != null) {
						oldBalance = new BigDecimal(result.get("balance").toString()).setScale(2,
								BigDecimal.ROUND_HALF_UP);
					} else {
						System.out.println("Can't find the account on all servers for account type " + accountType);

					}

					// Get Applied Amount
					applyAmount = new BigDecimal(driver.findElement(By.id("withdraw_amount")).getAttribute("value"))
							.setScale(2, BigDecimal.ROUND_HALF_UP);

					// Get CPS Number
					cpsOrderNo = driver.findElement(By.id("cps_order_no")).getAttribute("value");

					if (cpsOrderNo.equals("")) {
						System.out.println("CPS Order No is null. Can't operation on this record.");
						Assert.assertFalse(cpsOrderNo.equals(""),
								"CPS Order No is null. Can't operation on this record.");
					}

					// Checking Fee
					System.out.println("Checking Fee and settlement amount....");
					funcWDCPSCheckFee(Brand);
					System.out.println("Fee and settlement amount are correct.");

					// Verify each fields' value comparing with RestAPI returned value
					if (operation.contains("Close")) {
						funcVerifyUPWdDetail(AdminURL, cpsOrderNo, driver.manage().getCookies().toString());
						System.out.println();
					}
					// Click the operation
					driver.findElement(By.xpath(strPath)).click();

					// Print the status
					if (!operation.contains("Close")) {
						String resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Operation Result:" + resultMsg);
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
					}

					// Get newBalance

					if (accountType.equals("交易")) {
						result = RestAPI.funcGetTradingAccInfo(Brand, AdminURL, accountNo, cookie);
					} else if (accountType.equals("返佣")) {
						result = RestAPI.funcGetRebateAccInfo(Brand, AdminURL, accountNo, cookie);
					}

					if (result.get("balance") != null) {

						newBalance = new BigDecimal(result.get("balance").toString()).setScale(2,
								BigDecimal.ROUND_HALF_UP);

					} else {
						System.out.println("Can't find the account on all servers for account type " + accountType);
					}

					System.out.println("Operation is: " + operation + ", Account NO is: " + accountNo);

					switch (operation) {
					case "Reject":
						System.out.println("Checking Balance Data. Old Balance: " + oldBalance + " , Applied Amount: "
								+ applyAmount + " ,New Balance: " + newBalance);
						Assert.assertEquals(newBalance, oldBalance.add(applyAmount),
								"Reject operation should refund the applied amount of money.");
						System.out.println("Refund is correct.");

						break;
					default:
						Assert.assertEquals(newBalance, oldBalance,
								"Operations other than Reject  should NOT modify Balance.");
					}

					break;
				}
			}

			if (j >= trs.size()) {
				System.out.println("No Qualified UnionPay Withdrawl Audit Records.");
			}
		}

		return accountNo;

	}

	// Developed by Fiona: to check Admin-> CPS withdraw audit window -> Fee is listed correctly, and settlement amount is calculated as expected.
	// Updated by Yanni: udate variable type from float to BigDecimal to avoid *.99999 error
	void funcWDCPSCheckFee(String Brand) {

		String withdrawT = "UnionPay Withdrawal";
		BigDecimal expectedFee = Utils.getWDfee(Brand, withdrawT);

		BigDecimal applyAmount = new BigDecimal(driver.findElement(By.id("withdraw_amount")).getAttribute("value"));
		BigDecimal fee = new BigDecimal(driver.findElement(By.id("fee")).getAttribute("value"));
		BigDecimal settle_amount = new BigDecimal(driver.findElement(By.id("settlement_amount")).getAttribute("value"));
		BigDecimal settlement_rate = new BigDecimal(driver.findElement(By.id("settlement_rate")).getAttribute("value"));

		// If withdraw amount >=100, fee should be 0.00. Else, compare fee in UI with expectedFee.

		System.out.println("Apply Amount: " + applyAmount + "  fee: " + fee + "  settlement_rate: " + settlement_rate
				+ "  settle_amount: " + settle_amount);

		if (applyAmount.compareTo(new BigDecimal("100.00")) >= 0) {
			Assert.assertTrue(fee.compareTo(BigDecimal.ZERO) == 0,
					"ApplyAmount is " + applyAmount.toString() + ". No Fee should be charged.");
		} else {
			Assert.assertTrue(fee.compareTo(expectedFee) == 0,
					"Actual fee is: " + fee.toString() + ". Expected fee is: " + expectedFee.toString());
		}

		// check settle amount is right considering with fee

		BigDecimal calcSettlement = applyAmount.subtract(fee).multiply(settlement_rate).setScale(2,
				BigDecimal.ROUND_DOWN);
		Assert.assertEquals(calcSettlement, settle_amount, "Calculated settlement not equal to provided");

	}

	/*
	 * Developed by Yanni for common operations of UnionPay withdraw Audit on 15/07/2019
	 */
	public void funcUPWdStatusCheck(String accountNo, String newStatus, String newSubStatus, String Actions)
			throws Exception {

		Select t;
		String auditStatus, withdrawStatus, action;

		if (accountNo == null || accountNo.length() == 0) {
			System.out.println("Account NO is either NULL or EMPTY. Can't use it for search");
			Assert.assertTrue(false, "Account NO is either NULL or EMPTY. Can't use it for search");
		}

		Thread.sleep(1000);
		// Navigate to UnionPay Withdrawl Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("UnionPay Withdrawal"))).click();

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("Start to check status of UnionPay Withdraw record...");

		// Filtering records with originStatus
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("All Status");

		driver.findElement(By.id("searchType")).click();
		driver.findElement(By.linkText("Account")).click();
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(accountNo);
		driver.findElement(By.id("query")).click();

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// Select the top one record and click
		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Records for account " + accountNo);
		} else {
			// Get Audit Status, Withdraw Status and Operation
			auditStatus = trs.get(0).findElement(By.cssSelector("td:nth-of-type(10)")).getText();
			withdrawStatus = trs.get(0).findElement(By.cssSelector("td:nth-of-type(11)")).getText();
			action = trs.get(0).findElement(By.cssSelector("td:nth-last-of-type(1) a")).getText();

			Assert.assertEquals(auditStatus, newStatus, "Audit Status is not the expected one.");
			Assert.assertEquals(withdrawStatus, newSubStatus, "Withdraw Status is not the expected one.");
			Assert.assertEquals(action, Actions, "Action is not the expected one.");

			System.out.println("Status check passed.");
		}

	}

	/*
	 * Yanni Qi on 16/07/2019
	 * Search Pending UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list
	 * Click Audit in the last cell
	 * Click Confirm in the popup dialog
	 */

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawAudit(String Brand, String AdminURL, @Optional("") String TraderName) throws Exception {

		String newStatus = "Accepted", newSubStatus = "Unpaid", Actions = "Complete";
		String accountNo = "";

		// Audit the record which is in Pending status
		accountNo = funcUPWdOperation(Brand, AdminURL, "Pending", "Audit", TraderName);

		if (accountNo.equals("")) {
			System.out.println("Audit Failed. The Account No. returned is empty.");
			Assert.assertFalse(accountNo.equals(""), "Audit Failed. The Account No. returned is empty.");
		}

		// Check status change after Audit
		funcUPWdStatusCheck(accountNo, newStatus, newSubStatus, Actions);
	}

	/*
	 * Yanni Qi on 16/07/2019
	 * Search Accepted UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list
	 * Click Complete in the last cell
	 * Click Confirm in the popup dialog
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawComplete(String Brand, String AdminURL, @Optional("") String TraderName) throws Exception {

		String newStatus = "Completed", newSubStatus = "Unpaid", Actions = "Withdraw";
		String accountNo = funcUPWdOperation(Brand, AdminURL, "Accepted", "Confirm", TraderName);

		if (accountNo.equals("")) {
			System.out.println("Complete UnionPay Withdraw Record Failed. The Account No. returned is empty.");
			Assert.assertFalse(accountNo.equals(""),
					"Complete UnionPay Withdraw Record Failed. The Account No. returned is empty.");
		}

		funcUPWdStatusCheck(accountNo, newStatus, newSubStatus, Actions);

	}

	/*
	 * Yanni Qi on 16/07/2019
	 * Search Completed UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list
	 * Click Withdraw in the last cell
	 * Click Confirm in the popup dialog
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawPaid(String Brand, String AdminURL, @Optional("") String TraderName) throws Exception {

		String newStatus = "Completed", newSubStatus = "Paid", Actions = "View";
		String accountNo = funcUPWdOperation(Brand, AdminURL, "Completed", "Withdraw", TraderName);

		if (accountNo.equals("")) {
			System.out.println("Withdraw Money with UnionPay Failed. The Account No. returned is empty.");
			Assert.assertFalse(accountNo.equals(""),
					"Withdraw Money with UnionPay Failed. The Account No. returned is empty.");
		}

		funcUPWdStatusCheck(accountNo, newStatus, newSubStatus, Actions);
	}

	/*
	 * Yanni Qi on 16/07/2019
	 * Search Pending UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list
	 * Click Audit in the last cell
	 * Click Reject in the popup dialog
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawPendingCancel(String Brand, String AdminURL, @Optional("") String TraderName)
			throws Exception {

		String newStatus = "Cancelled", newSubStatus = "Unpaid", Actions = "View";
		String accountNo;

		accountNo = funcUPWdOperation(Brand, AdminURL, "Pending", "Reject", TraderName);

		if (accountNo.equals("")) {
			System.out.println(
					"Reject UnionPay withdraw record (in Pending status) Failed. The Account No. returned is empty.");
			Assert.assertFalse(accountNo.equals(""),
					"Reject UnionPay withdraw record (in Pending status) Failed. The Account No. returned is empty.");
		}

		funcUPWdStatusCheck(accountNo, newStatus, newSubStatus, Actions);

	}

	/*
	 * Yanni Qi on 16/07/2019
	 * Search Accepted UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list
	 * Click Audit in the last cell
	 * Click Reject in the popup dialog
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })

	public void UPWithdrawAcceptCancel(String Brand, String AdminURL, @Optional("") String TraderName)
			throws Exception {

		String newStatus = "Cancelled", newSubStatus = "Unpaid", Actions = "View";
		String accountNo = funcUPWdOperation(Brand, AdminURL, "Accepted", "Reject", TraderName);

		if (accountNo.equals("")) {
			System.out.println(
					"Reject UnionPay withdraw record (in Accepted status) Failed. The Account No. returned is empty.");
			Assert.assertFalse(accountNo.equals(""),
					"Reject UnionPay withdraw record (in Accepted status) Failed. The Account No. returned is empty.");
		}

		funcUPWdStatusCheck(accountNo, newStatus, newSubStatus, Actions);
	}

	/*
	 * Developed by Alex.L for verification of items in operations of UnionPay withdraw Audit on 15/07/2019
	 * Example:
	 * String coo = driver.manage().getCookies().toString();
	 * funcVerifyUPWdDetail(AdminURL,"CPS456", coo);
	 */
	public void funcVerifyUPWdDetail(String AdminURL, String orderNo, String cookie) throws Exception {
		String displayValue;
		String url;
		// remove the admin/main from AdminURL

		url = Utils.ParseInputURL(AdminURL);

		// System.out.println(url);
		String cpsDetail = RestAPI.testPostWithdrawCPSList(url, orderNo, cookie);

		JSONParser parser = new JSONParser();
		Object resultObject = parser.parse(cpsDetail);
		JSONObject obj = (JSONObject) resultObject;
		System.out.println("==================Result====================");

		JSONArray data = (JSONArray) obj.get("rows");
		for (Object item : data) {
			for (Object key : ((JSONObject) item).keySet()) {
				Object value = ((JSONObject) item).get(key.toString());

				String ele_id = key.toString();

				// Only check the item that exist on the web page
				try {
					displayValue = driver.findElement(By.id(ele_id)).getAttribute("value");
					System.out.println("key: " + ele_id + "\t display value is: " + displayValue + "\t jason value is: "
							+ value.toString());

					// Assert.assertEquals(displayValue, value.toString());
				} catch (NoSuchElementException e) {
					System.out.println("**********");
				}

			}

		}

	}

	/*
	 * Yanni Qi on 19/07/2019
	 * Search Pending UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list
	 * Click Audit in the last cell
	 * Click Close in the popup dialog
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawPendingClose(String Brand, String AdminURL, @Optional("") String TraderName)
			throws Exception {

		String newStatus = "Pending", newSubStatus = "Unpaid", Actions = "Audit";
		String accountNo;

		accountNo = funcUPWdOperation(Brand, AdminURL, "Pending", "Close", TraderName);

		if (accountNo.equals("")) {
			System.out.println(
					"Close UnionPay withdraw record (in Pending status) Failed. The Account No. returned is empty.");
			Assert.assertFalse(accountNo.equals(""),
					"Close UnionPay withdraw record (in Pending status) Failed. The Account No. returned is empty.");
		}

		funcUPWdStatusCheck(accountNo, newStatus, newSubStatus, Actions);

	}

	/*
	 * Yanni Qi on 23/08/2019
	 * Search Pending/Cancelled UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list, call API to get MT4 Comment and then verify MT4 comment is right
	 * 
	 */

	public void funcUPWithdrawMT4Comment(String Brand, String AdminURL, @Optional("") String TraderName, String status)
			throws Exception {

		String[] dataSourceIdGroup;
		String dataSourceId = "", mt4Comment = "", cookie = "", accountNo = "";
		Select t;
		int j;

		// Check the record MT4 Comment written when user submitted withdraw request.
		// (Note: Can't verify RebateAccount MT4 Comment with current CRM functions):
		cookie = driver.manage().getCookies().toString();

		// Get dataSourceId
		dataSourceIdGroup = Utils.getDataSourceId(Brand);

		driver.navigate().refresh();

		// Navigate to UnionPay Withdrawl Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("UnionPay Withdrawal"))).click();

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out
				.println("Start to Verify MT4 Comment (Trading Account only) for " + status + " UnionPay Withdraw ...");
		System.out.println("-------------------------------------------------------------");

		// Filtering records with specified status, remove Date search limitation
		t = new Select(driver.findElement(By.id("dateType")));
		t.selectByVisibleText("Search by Date");

		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText(status);

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// If TraderName is provided, using TraderName as email to filter record
		if (TraderName != null && TraderName.length() > 0) {

			driver.findElement(By.id("searchType")).click();
			driver.findElement(By.linkText("Email")).click();
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(TraderName);
			driver.findElement(By.id("query")).click();

		}

		Thread.sleep(500);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// Select the top one record and click
		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Records in status " + status);
		} else {

			for (j = 0; j < trs.size(); j++) {
				// Read each row's userName
				userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();

				// If it is a qualified test user, click Actions in the last cell and operate. Break the loop.
				if (Utils.isTestIB(userName) || Utils.isWebUser(userName) || Utils.isJoint(userName)
						|| Utils.isAddUser(userName)) {
					accountNo = trs.get(j).findElement(By.cssSelector("td:nth-of-type(5)")).getText();
					break;
				}
			}

			if (j >= trs.size()) {
				System.out.println("Can't find Test Record.");
			}

		}

		// Check MT4 Comment written by Trader when customer submitted UnionPay withdraw request

		for (j = 0; j < dataSourceIdGroup.length; j++) {
			dataSourceId = dataSourceIdGroup[j];
			mt4Comment = Utils.getMT4Comment(AdminURL, accountNo, dataSourceId, cookie);
			if (mt4Comment.length() > 0) {
				Boolean isAdminOP = false;
				Boolean isRebateAccount = false;

				if (Utils.funcIsAccountRebateOrTrading(AdminURL, cookie, accountNo).contains("Rebate")) {

					isRebateAccount = true;
				}

				switch (status) {
				case "Pending":
					isAdminOP = false;
					break;

				case "Cancelled":
					isAdminOP = true;
					break;

				}
				// Utils.verifyMT4Comment(mt4Comment, isRebateAccount, isAdminOP);
				Assert.assertTrue(Utils.verifyMT4Comment(mt4Comment, isRebateAccount, isAdminOP),
						"The MT4 comments is wrong!");
				break;
			}

		}
	}

	/*
	 * Yanni Qi on 23/08/2019
	 * Search Pending UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list, call API to get MT4 Comment and then verify MT4 comment is right
	 * 
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawPendingMT4Comment(String Brand, String AdminURL, @Optional("") String TraderName)
			throws Exception {
		String statusPending = "Pending";

		funcUPWithdrawMT4Comment(Brand, AdminURL, TraderName, statusPending);

	}

	/*
	 * Yanni Qi on 23/08/2019
	 * Search Cancelled UnionPay withdraw records with TraderName (if not empty)
	 * Pickup the first one in the list, call API to get MT4 Comment and then verify MT4 comment is right
	 * 
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "AdminURL", "TraderName" })
	public void UPWithdrawCancelMT4Comment(String Brand, String AdminURL, @Optional("") String TraderName)
			throws Exception {
		String statusCancelled = "Cancelled";

		funcUPWithdrawMT4Comment(Brand, AdminURL, TraderName, statusCancelled);

	}

	/*
	 * Author: Yanni Qi
	 * Date: 02/09/2019
	 * This function is to verify Search function via Account Name
	 */

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv" })

	public void WithdrawDetailsSearchByName(String Brand, String TestEnv) throws Exception {
		// Navigate to withdrawal Details Audit page

		int j = 0;
		boolean flag = false;
		String searchUserName;

		// Navigate to Account Audit page
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Withdrawal Details"))).click();

		Thread.sleep(2500);

		List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Withdraw Details Records.");
			Assert.assertTrue(false, "No Withdraw Details Records. Can't verify Search function.");
		}

		System.out.println("Start to verify Search function via Account Name....");

		// Read first row's userName
		userName = trs.get(0).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

		// Change to Search by Account Name
		driver.findElement(By.id("searchType")).click();
		driver.findElement(By.linkText("Account Name")).click();

		// Use the user name to do search

		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(userName);

		// Click Search button
		driver.findElement(By.id("query")).click();

		Thread.sleep(1000);

		// Read search result
		trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

		// if the search result shows no records:
		if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("Search function failed with username " + userName);
			Assert.assertTrue(false, "Search function failed with username " + userName);
		}

		for (j = 0; j < trs.size(); j++) {

			searchUserName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
			flag = searchUserName.equalsIgnoreCase(userName);
			if (flag == false) {
				System.out.println("Searched with username " + userName + " but got " + searchUserName);
			}
			Assert.assertTrue(flag, "Searched with username " + userName + " but got " + searchUserName);
			;
		}
	}

	/*
	 * Developed by Yanni for UnionPay withdraw Record Update in Admin on 26/08/2019
	 * When Records are in Submitted/Pending status, OP can update the records and save. This function is to update different fields and click the button
	 * 
	 */
	public void funcUPWdUpdate() throws Exception {

		String oldValue = "", oldBank = "", oldProvince = "", oldCity = "", newValue = "";
		Random r = new Random();
		Select tBank, tProvince, tCity;
		int index = 0;

		// Get old bank
		tBank = new Select(driver.findElement(By.id("bankName")));
		oldBank = tBank.getFirstSelectedOption().getText();

		// Get old Branch Province
		tProvince = new Select(driver.findElement(By.id("branchProvince")));
		oldProvince = tProvince.getFirstSelectedOption().getText();

		// Get old Branch City
		tCity = new Select(driver.findElement(By.id("branchCity")));
		oldCity = tCity.getFirstSelectedOption().getText();

		System.out.println(oldBank + "-" + oldProvince + "-" + oldCity);

		// Select new Bank
		newValue = oldBank;
		while (newValue.equalsIgnoreCase(oldBank)) {
			index = r.nextInt(tBank.getOptions().size());
			tBank.selectByIndex(index);
			newValue = tBank.getFirstSelectedOption().getText();
		}

		System.out.print("Bank Name: Old =  " + oldBank);
		System.out.print("    New = " + newValue + "\n");

		// Wait for options load
		Thread.sleep(1000);

		// Select new Branch Province
		newValue = oldProvince;
		while (newValue.equalsIgnoreCase(oldProvince)) {
			index = r.nextInt(tProvince.getOptions().size());
			tProvince.selectByIndex(index);
			newValue = tProvince.getFirstSelectedOption().getText();
		}

		System.out.print("Branch Province: Old = " + oldProvince);
		System.out.print("    New = " + newValue + "\n");

		// Wait for options load
		Thread.sleep(1000);

		// Select new Branch City
		newValue = oldCity;
		while (newValue.equalsIgnoreCase(oldCity)) {
			index = r.nextInt(tCity.getOptions().size());

			tCity.selectByIndex(index);
			newValue = tCity.getFirstSelectedOption().getText();
		}

		System.out.print("Branch City:  " + oldCity);
		System.out.print("    New = " + newValue + "\n");

		// Update Branch Name by adding 'OP'
		oldValue = driver.findElement(By.id("branchName")).getAttribute("value");
		driver.findElement(By.id("branchName")).sendKeys("OP");
		newValue = driver.findElement(By.id("branchName")).getAttribute("value");

		System.out.print("Branch Name: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");

		// Update Bank Card Number by replacing the last three digits to '999'
		oldValue = driver.findElement(By.id("bankcardNumber")).getAttribute("value");
		newValue = oldValue.substring(0, oldValue.length() - 3) + "999";
		driver.findElement(By.id("bankcardNumber")).clear();
		driver.findElement(By.id("bankcardNumber")).sendKeys(newValue);
		newValue = driver.findElement(By.id("bankcardNumber")).getAttribute("value");

		System.out.print("Bank Card Number: Old = " + oldValue);
		System.out.print("    New= " + newValue + "\n");

		// Update Card Holder's name by adding 'OP'
		oldValue = driver.findElement(By.id("cardHolderName")).getAttribute("value");
		driver.findElement(By.id("cardHolderName")).sendKeys("OP");
		newValue = driver.findElement(By.id("cardHolderName")).getAttribute("value");

		System.out.print("Card Holder's Name: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");

		// Update Phone Number by adding '99'
		oldValue = driver.findElement(By.id("phoneNumber")).getAttribute("value");
		driver.findElement(By.id("phoneNumber")).sendKeys("99");
		newValue = driver.findElement(By.id("phoneNumber")).getAttribute("value");

		System.out.print("Phone Numbe:r Old = " + oldValue);
		System.out.print("    New= " + newValue + "\n");

		// Replace the first 2 digits for National ID No. 2 digits are the same and generated randomly.
		index = r.nextInt(10);
		newValue = Integer.toString(index) + Integer.toString(index);

		oldValue = driver.findElement(By.id("nationalID")).getAttribute("value");
		newValue = newValue + oldValue.substring(2);
		driver.findElement(By.id("nationalID")).clear();
		driver.findElement(By.id("nationalID")).sendKeys(newValue);

		System.out.print("NationalID Number: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");

		// Input Processed Notes;

		// driver.findElement(By.id("ProcessedNotes")).sendKeys("Edit Union Card Audit Details");
		// Cannot find Processed Notes textarea directly and used TAB of the previous input field
		driver.findElement(By.id("nationalID")).sendKeys(Keys.TAB, "Edit Union Card Audit Details");

		// Upload a replacement photo
		driver.findElement(By.xpath("//input[@type='file' and @data-name='replacementUnionCardPhoto']"))
				.sendKeys(Utils.workingDir + "\\proof.png");
		driver.findElements(By.xpath("//a[@title='Upload selected files']")).get(1).click();

		Thread.sleep(1000);

		// Print old image path and new image path
		List<WebElement> imgList = driver.findElements(By.cssSelector("img.pull-left.input-img.input_img"));
		System.out.println("Old Image Source is: " + imgList.get(0).getAttribute("src"));
		System.out.println("New Image Source is: " + imgList.get(1).getAttribute("src"));

	}

	/*
	 * Yanni on 04/11/2019: refactoring OnlineDepositAudit to support Audit by specific method like Thailand deposit audit
	 * This function is extracted
	 */
	void funcOLDepositAuditbyChannel(String AdminURL, String TraderName, OLDepositM channelInfo) throws Exception {

		String userName, order_no, account;
		Select channelType;
		Select t;
		int k, j;

		String[] searchKey = new String[] { Utils.webUserPrefix, Utils.addUserPrefix };
		driver.navigate().to(AdminURL);
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		// driver.findElement(By.linkText("Deposit Audit")).click();
		driver.findElement(By.linkText("Deposit Audit")).click();

		// wait until the deposit list is fully loaded
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println();
		System.out.println("Start to Audit Online Deposit Records with channel " + channelInfo.getName() + "...");
		System.out.println("-------------------------------------------------------------");

		if (TraderName.length() > 0) {
			searchKey = new String[] { TraderName.substring(0, TraderName.indexOf("@")) };
		}

		// Select Audit status
		t = new Select(driver.findElement(By.id("statusQuery")));
		t.selectByVisibleText("Audit");

		// Select Payment method
		channelType = new Select(driver.findElement(By.id("typeQuery")));
		if ((AdminURL.contains("pug") || AdminURL.contains("puprime")) && channelInfo.getIndex() == 11)// PUG UnionPay
			channelType.selectByIndex(9);
		else if ((AdminURL.contains("pug") || AdminURL.contains("puprime")) && channelInfo.getIndex() == 8)// PUG Mobile Pay
			channelType.selectByIndex(7);
		else if ((AdminURL.contains("pug") || AdminURL.contains("puprime")) && channelInfo.getIndex() == 9)// PUG No Neteller Payment
		{
			System.out.println("No Neteller payment in PUG");
			return;
		}

		else
			channelType.selectByIndex(channelInfo.getIndex());

		// Choose Select Options as Account Name
		Thread.sleep(1000);
		driver.findElement(By.id("searchType")).click();
		driver.findElement(By.linkText("Account Name")).click();

		for (k = 0; k < searchKey.length; k++) {
			// Input search keyword
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(searchKey[k]);
			driver.findElement(By.id("query")).click();

			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for keywords " + searchKey[k] + " with Channel "
						+ channelType.getFirstSelectedOption().getText());
			} else {

				for (j = 0; j < trs.size(); j++) {

					// Read each row's userName
					userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();

					// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
						if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
								|| Utils.isTestIB(userName)) {
							trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
							Thread.sleep(1000);

							// Check the order number
							WebElement orderNo = wait03.until(ExpectedConditions
									.visibilityOfElementLocated(By.xpath("//input[@id='Merchant_Order']")));
							order_no = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", orderNo)
									.toString();
							WebElement accNo = driver.findElement(By.xpath("//input[@id='MT4_Account']"));
							account = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", accNo)
									.toString();
							Utils.funcIsStringContains(order_no, account, "");

							// click deposit
							driver.findElement(By.xpath(".//button[text()='Deposit']")).click();

							System.out.println("Depost Audit User " + userName + " with Channel "
									+ channelType.getFirstSelectedOption().getText());

							String resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By
											.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Operation Result:" + resultMsg);
							System.out.println();

							// Thread.sleep(8000);
							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
							break;
						}
					
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Deposit Audit test records for keyword " + searchKey[k]
							+ " and channel " + channelType.getFirstSelectedOption().getText());
				}

			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void OLDepositAuditThailand(String AdminURL, @Optional("") String TraderName) throws Exception {

		OLDepositM channelInfo = OLDepositM.Thai;
		funcOLDepositAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void OLDepositAuditMalay(String AdminURL, @Optional("") String TraderName) throws Exception {

		OLDepositM channelInfo = OLDepositM.Malay;
		funcOLDepositAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void OLDepositAuditViet(String AdminURL, @Optional("") String TraderName) throws Exception {

		OLDepositM channelInfo = OLDepositM.Viet;
		funcOLDepositAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void OLDepositAuditNigeria(String AdminURL, @Optional("") String TraderName) throws Exception {

		OLDepositM channelInfo = OLDepositM.Nigeria;
		funcOLDepositAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	void funcWDAuditbyChannel(String AdminURL, String TraderName, WithdrawM channelInfo) throws Exception {

		Select t;
		int j = 0, k = 0;
		String userName;
		String[] searchKey = new String[] { Utils.webUserPrefix, Utils.addUserPrefix };
		boolean bPUG = false;

		// Navigate to Withdrawl Audit module
		driver.navigate().to(AdminURL);
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		if ((AdminURL.contains("pug") || AdminURL.contains("puprime"))) // PUG
		{
			bPUG = true;
		}
		/*
		 * if(AdminURL.toLowerCase().startsWith("https://admin."))
		 * driver.findElement(By.linkText("Withdrawal Audit")).click();
		 * else
		 * driver.findElement(By.linkText("Withdrawl Audit")).click();
		 */

		try {
			driver.findElement(By.linkText("Withdrawal Audit")).click();
		} catch (NoSuchElementException e) {
			System.out.println("Can't find menu Withdrawal Audit, trying to locate Withdrawl Audit...");
			driver.findElement(By.linkText("Withdrawl Audit")).click();
		}
		
		Thread.sleep(2000);

		// Yanni: comment out this 2 lines as payload is closed.
		// wait until the withdraw audit list is fully loaded
		/*
		 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		 * wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		 */

		System.out.println();
		System.out.println("Start to audit withdraw records with channel " + channelInfo.getName() + "...");
		System.out.println("-------------------------------------------------------------");

		if (TraderName.length() > 0) {
			searchKey = new String[] { TraderName.substring(0, TraderName.indexOf("@")) };
		}

		// Get the number of Withdraw channel
		List<WebElement> channelType = driver
				.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li a"));

		// Click button "All Types" to show all withdraw channels
		driver.findElement(By.id("typeQuery")).click();

		// Select Withdraw Audit channel

		// Yanni on 29/06/2020: channelInfo.getIndex() won't work any PUG & AU have different menu index
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(channelInfo.getName()))).click();
		/*
		 * channelType = driver.findElements(By.cssSelector("div#toolbar div:nth-of-type(2) ul.dropdown-menu.bj li"));
		 * wait03.until(ExpectedConditions.visibilityOf(channelType.get(channelInfo.getIndex()).findElement(By.tagName("a")))).click();
		 */

		// Click Search Options to show options
		driver.findElement(By.id("searchType")).click();

		// Select search by Account Name
		Thread.sleep(500);
		driver.findElement(By.linkText("Account Name")).click();

		for (k = 0; k < searchKey.length; k++) {
			// Input the keywords
			Thread.sleep(1000);
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(searchKey[k]);

			// Select Submitted status
			t = new Select(driver.findElement(By.id("statusQuery")));
			t.selectByVisibleText("Submitted");

			// System will load after selecting Submitted. Wait until the data is loaded
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			// Click Search again (sometimes it doesn't search) and wait until the result is loaded
			driver.findElement(By.id("query")).click();

			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			Thread.sleep(1000);

			// trs holds the collection of Submitted withdraw records
			List<WebElement> trs = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for Channel "
						+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
			} else  // The search result shows records. Go through records one by one to pickup the testing user.
			{

				for (j = 0; j < trs.size(); j++) {

					// Read each row's userName
					userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();

					// Match whether this user is a test user name, if Yes, audit this user's deposit and break;
					if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)
							|| Utils.isTestIB(userName)) {

						trs.get(j).findElement(By.cssSelector("td:first-of-type input")).click();
						Thread.sleep(500);

						// Click startAudit button

						driver.findElement(By.cssSelector("div#startAudit")).click();

						System.out.println(
								"Changed Withdraw Audit from Submitted to Audit for User " + userName + " with Channel "
										+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());

						Thread.sleep(1000);
						// Click OK button to dismiss the popup dialog
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-dialog")));
						driver.findElement(By.xpath(".//button[text()='OK']")).click();

						String resultMsg = wait03
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
								.getText();
						System.out.println("Status Change Result:" + resultMsg);
						System.out.println();

						wait03.until(ExpectedConditions.invisibilityOfElementLocated(
								By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						// Thread.sleep(8000);

						// Choose Audit status

						// driver.navigate().refresh();
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("statusQuery")));
						t = new Select(driver.findElement(By.id("statusQuery")));
						t.selectByVisibleText("Audit");

						// Choose the same withdraw channel again
						driver.findElement(By.id("typeQuery")).click();
						channelType = driver.findElements(By.cssSelector("ul.dropdown-menu.bj li"));

						// Yanni on 29/06/2020: channelInfo.getIndex() won't work any more as PUG&AU have different menu inex
						// channelType.get(channelInfo.getIndex()).findElement(By.tagName("a")).click();
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(channelInfo.getName())))
								.click();

						driver.findElement(By.id("searchType")).click();
						driver.findElement(By.linkText("Account Name")).click();
						driver.findElement(By.id("userQuery")).clear();
						driver.findElement(By.id("userQuery")).sendKeys(userName);
						Thread.sleep(1000);
						driver.findElement(By.id("query")).click();
						Thread.sleep(1000);

						// trs2 holds the collection of Audit records
						List<WebElement> trs2 = driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));

						// if the search result shows no records:
						if (trs2.size() > 1 || (trs2.size() == 1
								&& (!trs2.get(0).getAttribute("class").equals("no-records-found")))) {

							// click Audit link
							trs2.get(0).findElement(By.cssSelector("td:last-of-type a")).click();

							Thread.sleep(1000);

							// added by Alex.L to check if Bank Statement is exist for Bank Wire Withdraw

							if (driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText()
									.equals("Bank Transfer")) {

								// IB Portal I18N withdraw form doesn't have Bank Statement field. So no bankstatement uploaded.
								try {
									System.out.println(
											"Please check the Bank Statement file path: -------->>>>\n" + driver
													.findElement(By.xpath(
															"//div[@class='col-md-6 add_upload']//div[1]//input"))
													.getAttribute("data-paramer") + "\n");
								} catch (Exception e) {
									System.out.println(
											"Can't find Bank Statement File Path. IB Portal I18N withdraw has no bank statement but CP I18N withdraw SHOULD HAVE. ");
								}

							}

							// added by Yanni to check fee:

							// ===============================

							System.out.println("Audit Withdraw Record for User " + userName + " with Channel "
									+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
							wait03.until(ExpectedConditions.elementToBeClickable(By.cssSelector(
									"div.bootstrap-dialog-footer-buttons button.btn.btn-default:nth-of-type(1)")))
									.click();

							// Print any error message if there have
							resultMsg = wait03
									.until(ExpectedConditions.visibilityOfElementLocated(By
											.cssSelector("li.messenger-message-slot div div.messenger-message-inner")))
									.getText();
							System.out.println("Audit Result:" + resultMsg);
							System.out.println();

							wait03.until(ExpectedConditions.invisibilityOfElementLocated(
									By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
							// Thread.sleep(8000);

						} else {
							System.out.println("No available audit records for channel "
									+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
						}

						break;
					}

				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Withdraw Audit test records for " + "channel "
							+ driver.findElement(By.cssSelector("button#typeQuery div")).getText());
					System.out.println();
				}

			}

			driver.navigate().refresh();
		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void WDAuditThailand(String AdminURL, @Optional("") String TraderName) throws Exception {

		WithdrawM channelInfo = WithdrawM.Thai;
		funcWDAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void WDAuditMalay(String AdminURL, @Optional("") String TraderName) throws Exception {

		WithdrawM channelInfo = WithdrawM.Malay;
		funcWDAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void WDAuditViet(String AdminURL, @Optional("") String TraderName) throws Exception {

		WithdrawM channelInfo = WithdrawM.Viet;
		funcWDAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName" })
	void WDAuditNigeria(String AdminURL, @Optional("") String TraderName) throws Exception {

		WithdrawM channelInfo = WithdrawM.Nigeria;
		funcWDAuditbyChannel(AdminURL, TraderName, channelInfo);

	}

	@Test(groups="sanity", dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WithdrawlAuditComplete(String AdminURL, String TraderName, String Brand) throws Exception {

		switch (Brand.toLowerCase()) {

		// VT & PUG has only I18N transfer withdraw and UNIONPAY huaao withdraw. HuaAo withdraw is run in a separate xml file.
		case "vt":
			funcWDCompletebyChannel(AdminURL, TraderName, WithdrawM.BankTransfer, Brand);
			funcWDCompletebyChannel(AdminURL, TraderName, WithdrawM.CC, Brand);
			break;
		case "fsa":
		case "svg":
			funcWDCompletebyChannel(AdminURL, TraderName, WithdrawM.BankTransfer, Brand);
			break;

		default:
			for (WithdrawM channel : WithdrawM.values()) {
				if (channel != WithdrawM.Thai && channel != WithdrawM.Malay && channel != WithdrawM.Viet
						&& channel != WithdrawM.Nigeria) {
					funcWDCompletebyChannel(AdminURL, TraderName, channel, Brand);
				}
			}

		}

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WDAuditCompleteMalay(String AdminURL, @Optional("") String TraderName, String Brand) throws Exception {

		WithdrawM channelInfo = WithdrawM.Malay;
		funcWDCompletebyChannel(AdminURL, TraderName, channelInfo, Brand);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WDAuditCompleteNigeria(String AdminURL, @Optional("") String TraderName, String Brand) throws Exception {

		WithdrawM channelInfo = WithdrawM.Nigeria;
		funcWDCompletebyChannel(AdminURL, TraderName, channelInfo, Brand);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WDAuditCompleteThailand(String AdminURL, @Optional("") String TraderName, String Brand) throws Exception {

		WithdrawM channelInfo = WithdrawM.Thai;
		funcWDCompletebyChannel(AdminURL, TraderName, channelInfo, Brand);

	}

	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "TraderName", "Brand" })
	void WDAuditCompleteViet(String AdminURL, @Optional("") String TraderName, String Brand) throws Exception {

		WithdrawM channelInfo = WithdrawM.Viet;
		funcWDCompletebyChannel(AdminURL, TraderName, channelInfo, Brand);

	}

	// Developed by Yanni: to check Admin-> withdraw audit COMPLETE window -> Fee is listed correctly, and payable amount is calculated as expected.
	void funcWDCheckFee(String Brand, String withdrawT) {

		BigDecimal expectedFee = Utils.getWDfee(Brand, withdrawT);

		BigDecimal actualAmount = new BigDecimal(driver.findElement(By.id("Actual_Amount")).getAttribute("value"));
		BigDecimal fee = new BigDecimal(driver.findElement(By.id("Fee")).getAttribute("value"));
		BigDecimal payableAmount = new BigDecimal(driver.findElement(By.id("Payment_Amount")).getAttribute("value"));

		// If actualAamount >=100, fee should be 0.00. Else, compare fee in UI with expectedFee.

		System.out.println("Actual Amount: " + actualAmount + "  fee: " + fee + "  payableAmount: " + payableAmount);

		if (actualAmount.compareTo(new BigDecimal("100.00")) >= 0) {
			Assert.assertTrue(fee.compareTo(BigDecimal.ZERO) == 0,
					"actualAmount is " + actualAmount.toString() + ". No Fee should be charged.");
		} else {
			Assert.assertTrue(fee.compareTo(expectedFee) == 0,
					"Actual fee is: " + fee.toString() + ". Expected fee is: " + expectedFee.toString());
		}

		// check settle amount is right considering with fee

		BigDecimal calcPayableAmount = actualAmount.subtract(fee);
		Assert.assertEquals(calcPayableAmount, payableAmount, "Calculated payableAmount not equal to provided");

	}

	void funcChooseTradeGroup(String testEnv, String Brand) throws Exception {
		String serverName="",gName="";

		//Get current server name
		Select server = new Select(driver.findElement(By.id("ib_server")));
		serverName = server.getFirstSelectedOption().getText();
		
		Select t = new Select(driver.findElement(By.id("ib_mt4_set")));
		
		// If the list has only one option (that is "Select"), means the account group list has not been loaded. Wait until it is loaded
		while (t.getOptions().size() == 1) {
			Thread.sleep(1000);
		}

		if (serverName.contains("MT5")) {
			//For MT5, choose Hedge account groups
			for (int count = 1; count < t.getOptions().size(); count++) {
				gName = t.getOptions().get(count).getText();
				if (gName.contains("Hedge")) {
					Thread.sleep(1000);
					driver.findElement(By.xpath("//div[@id='ib_mt4_set_chosen']")).click();
					driver.findElement(By.xpath("//div[@id='ib_mt4_set_chosen']//input")).sendKeys(gName);
					driver.findElement(By.xpath("//div[@id='ib_mt4_set_chosen']//input")).sendKeys(Keys.RETURN);
					break;
				}
			}
		}else {
			// Choose MT4 test account groups
			for (int count = 1; count < t.getOptions().size(); count++) {
				gName = t.getOptions().get(count).getText();
				if (gName.startsWith("TEST_")) {
					Thread.sleep(1000);
					driver.findElement(By.xpath("//div[@id='ib_mt4_set_chosen']")).click();
					driver.findElement(By.xpath("//div[@id='ib_mt4_set_chosen']//input")).sendKeys(gName);
					driver.findElement(By.xpath("//div[@id='ib_mt4_set_chosen']//input")).sendKeys(Keys.RETURN);
					break;
				}
			}
		}
	}

	// Developed by Yanni on 23/06/2020: different brands/testenv have different spellings.
	// This function is to click Withdrawal Audit first, if failed, then click Withdrawl Audit

	void clickWithdrawAuditMenu() {
		try {
			driver.findElement(By.linkText("Withdrawal Audit")).click();
		} catch (NoSuchElementException e) {
			System.out.println("Can't find menu Withdrawal Audit, trying to locate Withdrawl Audit...");
			driver.findElement(By.linkText("Withdrawl Audit")).click();
		}
	}
	
	@Test(groups= "justfortest")
	public void testmethod() {
		count++;
		Assert.assertFalse(count<3);
	}
}