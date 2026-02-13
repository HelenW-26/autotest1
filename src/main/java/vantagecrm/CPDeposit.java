package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
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

import clientBase.cpLogIn;
import clientBase.cpDeposit;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * This class is to test all Deposit types in CP
 */

public class CPDeposit {

	WebDriver driver;

	// different wait time among different environments. Test environment will
	// use 1 while beta & production will use 2.
	// Initialized in LaunchBrowser function.
	int waitIndex = 1;
	Select t;  // Account Dropdown
	Random r = new Random();
	int j; // Select index
	static WebDriverWait wait01;
	static WebDriverWait wait02;
	static WebDriverWait wait03;
	WebDriverWait wait60;
	WebDriverWait wait100;

	String depositAmount = "20000.66";

	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value = {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv,@Optional("False") String headless, ITestContext context) {
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		context.setAttribute("driver", driver);          // Added by Yanni on
												          // 5/15/2019
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
			depositAmount = "2000.66";
		}

		wait01 = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait02 = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait03 = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait60 = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait100 = new WebDriverWait(driver, Duration.ofSeconds(1000));
	}

	@Test(priority = 0)
	@Parameters(value = { "TraderURL", "TraderName", "TraderPass", "Brand" })
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception

	{

		// Login AU CP
		driver.get(TraderURL);

		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		Thread.sleep(waitIndex*2000);
		
		Utils.funcVerifyHomePageLiveAccounts(Brand,wait01);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Credit card payment : Cardpay added on 11 Feb, 2020 by Fiona
	void CCCardpayDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		String mt4Account = "";
		String cardNumber = "4000000000000002";
		String cardHolderName = "Testing CC Cardpay";

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Credit Card Payment Method
		driver.findElement(By.xpath("//div[contains(text(),'Credit/Debit Card')]")).click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}

		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(1) li.fr div.el-input > input"))
						.sendKeys(depositAmount);
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(2) li.fl div.el-input > input"))
						.sendKeys("Test CP CC Cardpay Deposit " + depositAmount);

				funcClickSubmitButton();

				Utils.funcIsStringContains(driver.getCurrentUrl(), "cardpay.com", Brand);
				driver.findElement(By.id("input-card-number")).sendKeys(cardNumber);
				driver.findElement(By.id("input-card-holder")).sendKeys(cardHolderName);
				Select dropdownMonth = new Select(driver.findElement(By.id("card-expires-month")));
				dropdownMonth.selectByIndex(2);

				Select dropdownYear = new Select(driver.findElement(By.id("card-expires-year")));
				dropdownYear.selectByIndex(2);

				driver.findElement(By.id("input-card-cvc")).sendKeys(cardNumber);
				// submit cardpay form
				driver.findElement(By.id("action-submit")).click();

				Thread.sleep(5000);

				// Check 3D
				if (driver.findElements(By.id("success")).size() > 0)
					driver.findElement(By.id("success")).click();

				// click 'Back to the shop' button
				if (driver.findElements(By.id("formSubmit")).size() > 0)
					driver.findElement(By.id("formSubmit")).click();

				// check 'Back To Home Page' button
				wait03.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Back To Home Page')]")));
				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[contains(text(),'Back To Home Page')]")).click();

				Thread.sleep(1000);

				// Go to Transaction History page
				funcNavigate2DepositHistory(Brand);

				// Verify the transaction history
				funcValidateTransacHistory(driver, mt4Account, "Credit/Debit Card", depositAmount, Brand);
				System.out.println(
						"Made Credit/Debit Card Cardpay deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Credit card payment
	void CCNABDepositExistingCard(String TraderURL, String TestEnv, String Brand) throws Exception {

		Boolean isNewCard = false;
		Boolean isNAB = true;
		// int status3DSecure = 2;

		funCCDeposit(isNewCard, isNAB, TraderURL, TestEnv, Brand);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Credit card payment
	void CCNABDepositNewCard(String TraderURL, String TestEnv, String Brand) throws Exception {

		Boolean isNewCard = true;
		Boolean isNAB = true;
		// int status3DSecure = 1;

		funCCDeposit(isNewCard, isNAB, TraderURL, TestEnv, Brand);

	}

	// By Alex Liu 05022021
	// This is for all credit card payment except NAB. 
	// For example, VirtualPay, BridgerPay and SDPay.
	// It only has account number, amount and notes.
	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	void CCGenericDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		Boolean isNewCard = true;
		Boolean isNAB = false;
		// int status3DSecure = 0;

		funCCDeposit(isNewCard, isNAB, TraderURL, TestEnv, Brand);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// International bank wire transfer supports all 8 currencies: AUD, USD,
	// EUR, GBP, SGD, JPY, NZD, CAD
	void I18NDeposit(String TraderURL, String Brand, String testEnv) throws Exception {
		String mt4Account = "", depositHistory = "";

		cpDeposit depositEle = new cpDeposit(driver, Brand);
		
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select international bank transfer
		depositEle.getI18N().click();
		
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, testEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount 
				depositEle.getAmount().sendKeys(depositAmount);

				// Input Notes
				driver.findElement(By.xpath("//div[label = 'Notes']//input"))
							.sendKeys("Test CP International Bank Transfer Deposit " + depositAmount);
				
				// Input receipt
				driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir + "\\proof.png");
				Thread.sleep(1000);
				/*
				 * wait01.until(ExpectedConditions.visibilityOfElementLocated((
				 * By.cssSelector(
				 * "a.btn.btn-default.kv-fileinput-upload.fileinput-upload-button"
				 * )))).click();
				 * Thread.sleep(1000);
				 */

				funcClickSubmitButton();

				// Confirm
				funcBack2HomePage(Brand);
				System.out.println(
						"Made International bank trasfer deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				if (Brand.equalsIgnoreCase("vt")) {
					depositHistory = "Bank wire transfer (International)";
				} else {
					depositHistory = "Bank Wire Transfer (International)";
				}
				funcValidateTransacHistory(driver, mt4Account, depositHistory, depositAmount, Brand);
				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}
	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true, invocationCount = 1)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// International bank wire transfer supports all 8 currencies: AUD, USD,
	// EUR, GBP, SGD, JPY, NZD, CAD
	void ValidateI18NDeposit(String TraderURL, String Brand, String testEnv) throws Exception {
		String mt4Account = "", currency;
		cpDeposit depositEle = new cpDeposit(driver, Brand);
		
		funcNavigate2DepositFund( TraderURL, Brand );
		
		Thread.sleep(1000);
		Utils.waitUntilLoaded(driver);

		// Select international bank transfer
		depositEle.getI18N().click();

		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {

			try {
				String accountInfo = webElement.getText();
				mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
				currency = accountInfo.substring(accountInfo.indexOf("-") + 1, accountInfo.length()).trim();
				Thread.sleep(500);
				webElement.click();
			} catch (StringIndexOutOfBoundsException e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
				Thread.sleep(500);
				String accountInfo = webElement.getText();
				mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
				currency = accountInfo.substring(accountInfo.indexOf("-") + 1, accountInfo.length()).trim();
				webElement.click();
			}
			switch (Brand) {
			case "vt":
			case "fsa":
			case "svg":
			case "ky":
			case "vfsc":
			case "vfsc2":
			case "fca":
			case "regulator2":
				funcValidateI8NBankInfo(driver, currency, Brand);
				break;

			case "au":
			default:
				System.out
						.println(Brand + " is not supported checking Bank Info in International Bank Transfer Deposit");
				break;
			}

			Thread.sleep(500);
			// Select the account for the next iteration
			if (Brand.equalsIgnoreCase("vt")) {
				driver.findElement(By.id("accountNumber")).click();
			} else {
				driver.findElement(
						By.cssSelector("div.el-form-item.el-form-item--feedback.is-success.is-required div> input"))
						.click();
				Thread.sleep(500);//wait for list be expand, liufeng
			}

		}
	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// Bank Wire Transfer: Australia Deposit. The account currency must be AUD
	void AuBankDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {
		String mt4Account = "";

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Australia bank transfer
		driver.findElement(By.xpath("//div[contains(text(),'Australia Bank Transfer')]")).click();
		Thread.sleep(1000);

		// Verify Australia Bank Info
		funcValidateAUBankInfo(driver, Brand);

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.cssSelector("div.form_list ul.clearfix:nth-child(1) li.fr div > input"))
						.sendKeys(depositAmount);
				driver.findElement(By.cssSelector("div.form_list ul.clearfix:nth-child(2) input.el-input__inner"))
						.sendKeys("Test CP Australia Bank Transfer Deposit " + depositAmount);

				// Input receipt
				driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir + "\\proof.png");
				Thread.sleep(1000);
				/*
				 * wait01.until(ExpectedConditions.visibilityOfElementLocated((
				 * By.cssSelector(
				 * "a.btn.btn-default.kv-fileinput-upload.fileinput-upload-button"
				 * )))).click();
				 * Thread.sleep(1000);
				 */

				funcClickSubmitButton();

				// Confirm
				wait01.until(
						ExpectedConditions.visibilityOfElementLocated((By.xpath("//a[@class='el-button btn_blue']"))))
						.click();
				System.out.println("Made AU bank trasfer deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "Bank Wire Transfer (Australia)", depositAmount, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// BPay deposit. The account owner's resident country must be Australia and
	// account currency must be AUD
	void BPayDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String mt4Account = "";
		String bPayNo = "228494";

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Bpay
		driver.findElement(By.xpath("//div[contains(text(),'BPAY Bank Transfer')]")).click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		/*
		 * List<WebElement> element = null ;
		 * element=driver.findElements(By.
		 * xpath("//*[@class='el-select-dropdown el-popper']/div/div/ul/li"));
		 */
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have AUD account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Check the biller code
				WebElement bCode = driver.findElement(
						By.cssSelector("div.form_main ul.clearfix:nth-child(1) li.fr div.el-input > input"));
				Object value = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", bCode);
				Assert.assertTrue(value.toString().equalsIgnoreCase(bPayNo),
						"Biller code should be " + bPayNo + " instead of " + value.toString());
				System.out.println("Biller code is correct: " + value.toString());

				// Need to find the account with bpay number
				WebElement bpayNo = driver.findElement(
						By.cssSelector("div.form_main ul.clearfix:nth-child(2) li.fl div.el-input > input"));
				value = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", bpayNo);
				System.out.println("bpay no: " + value);

				if (!value.toString().equals("")) {
					System.out.println("This is it!");
					// Input deposit amount
					driver.findElement(
							By.cssSelector("div.form_main ul.clearfix:nth-child(2) li.fr div.el-input > input"))
							.sendKeys(depositAmount);
					driver.findElement(
							By.cssSelector("div.form_main ul.clearfix:nth-child(3) li.fr div.el-input > input"))
							.sendKeys("Test CP Bpay Transfer Deposit " + depositAmount);

					// Input receipt
					driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir + "\\proof.png");
					Thread.sleep(500);

					funcClickSubmitButton();

					// Confirm
					wait01.until(ExpectedConditions
							.visibilityOfElementLocated((By.xpath("//a[@class='el-button btn_blue']")))).click();
					System.out.println("Made Bpay deposit of " + depositAmount + " to account " + mt4Account);

					// Verify transaction history
					Thread.sleep(500);
					wait01.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'LIVE ACCOUNTS')]")));
					funcNavigate2DepositHistory(Brand);
					Thread.sleep(1000);
					funcValidateTransacHistory(driver, mt4Account, "Bpay", depositAmount, Brand);

					break;
				}

				// Select the account for next iteration
				driver.findElement(By.cssSelector("div.el-select div> input")).click();

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// Polipay deposit. The account owner's resident country must be Australia
	// and account currency must be AUD
	void PoliDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String handle;
		Set<String> handleS;
		String mt4Account = "";

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select PoliPay
		driver.findElement(By.xpath("//div[contains(text(),'POLi Payments')]")).click();
		Thread.sleep(1000);

		// Test MAKE PAYMENT NOW button to ensure it can navigate to Poli
		handle = driver.getWindowHandle();
		Thread.sleep(500);

		// Navigate to POLi Payments page
		driver.findElement(By.xpath("//a[contains(text(),'MAKE A POLI PAYMENT')]")).click();
		Thread.sleep(2000);

		handleS = driver.getWindowHandles();
		for (String s : handleS) {
			if (!s.equals(handle)) {
				/* Assert.assertEquals(driver.getTitle(), "POLi Payments"); */
				Assert.assertTrue(!driver.getCurrentUrl().contains("txn.apac.paywithpoli.com"),
						"Open Polipay webpage failed!");
				driver.switchTo().window(s).close();
				driver.switchTo().window(handle);
			}
		}

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(1) li.fr div > input"))
						.sendKeys(depositAmount);

				// Input notes
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(2) li.fl div > input"))
						.sendKeys("Test CP Polipay Deposit " + depositAmount);

				// Input receipt
				driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir + "\\proof.png");
				Thread.sleep(500);

				funcClickSubmitButton();

				// Confirm
				wait01.until(
						ExpectedConditions.visibilityOfElementLocated((By.xpath("//a[@class='el-button btn_blue']"))))
						.click();
				System.out.println("Made Polipay deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				Thread.sleep(500);
				wait01.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'LIVE ACCOUNTS')]")));
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "Poli Payment", depositAmount, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(groups="sanity", dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv", "CallbackUrl" })
	// Skrill deposit.
	void SkrillDeposit(String TraderURL, String Brand, String TestEnv, String CallbackUrl) throws Exception {
		String mt4Account = "", orderNo="", status="";
		WebElement selAcc = null;
		cpDeposit depositEle = new cpDeposit(driver, Brand);
		//Adjust the amount less than 10000 for auto deposit
		depositAmount = "200.99";

		String sql = "select order_number from tb_payment_deposit where payment_type=21 and mt4_account=MT4ACCOUNT order by create_time desc limit 1;";
		String sql2 = "select status from tb_payment_deposit where payment_type=21 and mt4_account=MT4ACCOUNT order by create_time desc limit 1;";


		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);
		
	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'E-WALLET')]")).click();
		}

		// Select Skrill
		WebElement skrill = depositEle.getSkrill();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", skrill);
		skrill.click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have USD/GBP/EUR/CAD accounts!");
		}

		for (WebElement webElement : element) {

			String accountInfo = webElement.findElement(By.tagName("span")).getText();
			System.out.println("AccountInfo is " + accountInfo);
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount and notes
				driver.findElement(By.xpath("//div[label = 'Amount']//input")).sendKeys(depositAmount);

				driver.findElement(By.xpath("//div[label = 'Important notes']//input"))
						.sendKeys("Test CP Skrill Transfer Deposit " + depositAmount);

				driver.findElement(By.xpath("//div[label = 'Skrill/Moneybookers Email']//input"))
						.sendKeys("testSkrill@test.com");

				funcClickSubmitButton();

				try {
					// Wait until navigating to 3rd party
					wait01.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Amount')]")));
					
					wait03.until(ExpectedConditions.urlContains("pay.skrill.com"));
					
				} catch (Exception e) {
					System.out.println(
							"\n!!!Submit failure or error occours! Please make sure correct Skrill info configured in disconf (biz_payment.properties).\n");
	
				}
				
				// Navigate back
				driver.navigate().back();
				//wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("el-loading-parent")));
				wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("el-loading-parent")));

				System.out.println("Made Skrill deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "Skrill / Moneybookers", depositAmount, Brand);

				

				// Get order number in DB
				sql = sql.replace("MT4ACCOUNT", mt4Account);
				orderNo = DBUtils.funcQueryDB(TestEnv, sql, Brand);
				System.out.println("orderNo: " + orderNo);
				
				
				//Going to send callback for specified order number
				RestAPI.testPostSkrillCallback(CallbackUrl, orderNo);
				
				//Need to wait 15 seconds for Kafka and Cron consuming the notification and updating the order status
				Thread.sleep(15000);
				
				// Get order status in DB
				sql2 = sql2.replace("MT4ACCOUNT", mt4Account);
				status = DBUtils.funcQueryDB(TestEnv, sql2, Brand);
				
				System.out.println("The status of Skrill order " + orderNo + " is: " + status);
				Utils.funcIsStringContains(status, "5", Brand);
				
				break;

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Neteller deposit.
	void NetellerDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		String mt4Account = "";
		cpDeposit depositEle = new cpDeposit(driver, Brand);
		// Set<String> handleS;
		String sql = "select order_number from tb_payment_deposit where payment_type=7 and mt4_account=MT4ACCOUNT order by create_time desc limit 1;";

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Neteller
		WebElement neteller = depositEle.getNeteller();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", neteller);
		neteller.click();

		Thread.sleep(1000);

		// Get current window handler
		// handle = driver.getWindowHandle();

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any suitable account!");
		}

		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount and notes
				driver.findElement(By.xpath("//div[label = 'Amount']//input")).sendKeys(depositAmount);

				driver.findElement(By.xpath("//div[label = 'Important notes']//input"))
						.sendKeys("Test CP Neteller Deposit " + depositAmount);

				if (Brand.equalsIgnoreCase("vt")) {
					driver.findElement(By.xpath("//div[label = 'Neteller email']//input"))
							.sendKeys("testSkrill@test.com");
				} else {
					driver.findElement(By.xpath("//div[label = 'Neteller Email']//input"))
							.sendKeys("testSkrill@test.com");
				}

				funcClickSubmitButton();

				// Wait until navigating to 3rd party
				try {
					wait01.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Amount')]")));
					// Wait until visibility of 3rd party element
					wait01.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[contains(text(),'Pay with your Neteller account.')]")));
					
					System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
					String amount = driver.findElement(By.cssSelector("div.amount.ng-binding.ng-isolate-scope")).getText();
					if (amount.contains(",")) {
						amount = amount.replace(",","");
					}
					Utils.funcIsStringEquals(amount, depositAmount, Brand);
					

				} catch (TimeoutException e) {
					System.out.println(
							"\n!!!Submit failure! Please make sure correct Neteller info configured in disconf (biz_payment.properties).\n");
				}

				// Navigate back
				driver.navigate().back();
				System.out.println("Made Neteller deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);

				// Verify transaction history
				funcValidateTransacHistory(driver, mt4Account, "Neteller", depositAmount, Brand);
				System.out.println("Made Neteller deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);


				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Paypal only supports USD
	void PaypalDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		String handle, mt4Account = "";
		Set<String> handleS;
		
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Paypal
		driver.findElement(By.xpath("//div[contains(text(),'Paypal Deposit')]")).click();
		Thread.sleep(1000);

		// Get current window handler
		handle = driver.getWindowHandle();

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}

		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(1) li.fr div.el-input > input"))
						.sendKeys(depositAmount);
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(2) li.fl div.el-input > input"))
						.sendKeys("Test CP Paypal Deposit " + depositAmount);

				funcClickSubmitButton();

				handleS = driver.getWindowHandles();

				for (String s : handleS) {
					if (!s.equals(handle)) {
						driver.switchTo().window(s);
						Utils.funcIsStringContains(driver.getCurrentUrl(), "paypal.com", Brand);
						driver.close();
						driver.switchTo().window(handle);
					}
				}

				// Click YES for completing the deposit
				wait01.until(
						ExpectedConditions.visibilityOfElementLocated((By.xpath("//span[contains(text(),'Yes')]"))))
						.click();
				Thread.sleep(1000);

				// Verify the transaction history
				funcValidateTransacHistory(driver, mt4Account, "Paypal", depositAmount, Brand);
				System.out.println("Made Paypal deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@AfterClass(alwaysRun = true)
	@Parameters(value = { "Brand" })
	void ExitBrowser(String Brand) throws Exception {
		// driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// Polipay deposit. The account owner's resident country must be Australia
	// and account currency must be AUD
	void BrokerDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String mt4Account = "";
		WebDriver driver2;

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Broker deposit
		driver.findElement(By.xpath("//div[contains(text(),'Broker to Broker Transfer')]")).click();
		Thread.sleep(1000);

		// Validate broker to broker form
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
				driver.findElement(By.linkText("Broker to Broker Transfer Form")));
		String formUrl = driver.findElement(By.linkText("Broker to Broker Transfer Form")).getAttribute("href");
		
		//liufeng 
		/*
		 * ChromeOptions options = new ChromeOptions();
		 * options.addArguments("--incognito");
		 * System.setProperty("webdriver.chrome.driver", Utils.ChromePath);//system
		 * should be config the path of webdriver,feng liu driver2 = new
		 * ChromeDriver(options); driver2.get(formUrl);
		 */

		switch (Brand) {
		case "au":
			//liufeng 
			Utils.funcIsStringContains(formUrl,
					".com.au/wp-content/uploads/2016/02/VFX-Form_FINANCIAL-SERVICES-PROVIDER-TRANSFER-FORM.pdf", Brand);
			break;

		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":
			//liufeng for vfsc2
			String  testUrl = "files/broker-to-broker-form";
			
			
			  Utils.funcIsStringContains(formUrl,testUrl,Brand);
			 
			
			/*
			 * Utils.funcIsStringContains(driver2.getCurrentUrl(),
			 * "com/files/pdf/guides/Vantage-FX-Broker-to-Broker-Transfer_Form20191216.pdf",
			 * Brand);
			 */
			break;
		}
		
		//liufeng 
		//driver2.close();

		// Select the account
		funcClickAccountList(Brand);

		/*
		 * List<WebElement> element = null ;
		 * 
		 * element=driver.findElements(By.
		 * xpath("//*[@class='el-select-dropdown el-popper']/div/div/ul/li"));
		 */
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(1) li.fr div.el-input > input"))
						.sendKeys(depositAmount);
				driver.findElement(By.cssSelector("div.form_main ul.clearfix:nth-child(2) li.fr div.el-input > input"))
						.sendKeys("Test CP Broker to Broker Deposit " + depositAmount);
				Thread.sleep(500);

				// Upload form
				driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir + "\\proof.png");
				Thread.sleep(500);

				funcClickSubmitButton();

				// Confirm
				wait01.until(
						ExpectedConditions.visibilityOfElementLocated((By.xpath("//a[@class='el-button btn_blue']"))))
						.click();

				// Verify transaction history
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "Broker to Broker Transfer", depositAmount, Brand);
				System.out.println("Made broker to broker deposit of " + depositAmount + " to account " + mt4Account);
				break;

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Unionpay only supports USD currency
	void UnionPayDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		String mt4Account = "";
		WebElement selAcc = null;
		BigDecimal rmbAmount = null;
		depositAmount = "5000.00";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
	    
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Click Union Pay Deposit
		WebElement unionPay = driver.findElement(
				By.xpath("//span[contains(translate(., 'UnionPay deposit', 'UnionPay Deposit'),'UnionPay Deposit')]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", unionPay);
		unionPay.click();

		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}

		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.xpath("//div[label = 'Amount']//input")).sendKeys(depositAmount);

				// Input Notes
				depositEle.getImportantNotes()
							.sendKeys("Test CP Union Pay Deposit " + depositAmount);
				

				Thread.sleep(500);

				// Verify Exchange Rate and RMB amount
				rmbAmount = funcVerifyExchangeRateAndAmount(Brand);

				/*
				 * //verify if the result of (calculated amount - displayed
				 * amount) is between [0, 0.01]
				 * diff = exRate.multiply(new
				 * BigDecimal("5000.00")).subtract(rmbAmount);
				 * if ( diff.compareTo( new BigDecimal("0.00") ) < 0
				 * || diff.compareTo( new BigDecimal("0.01")) > 0 )
				 * {
				 * Assert.assertTrue(false,
				 * "Deposit Amount * Exchange Rate != calculated RMB: "+exRate.
				 * multiply(new BigDecimal("5000.00")));
				 * }
				 * 
				 * System.out.println("Calculated RMB is correct");
				 */

				// handle = driver.getWindowHandle();
				funcClickSubmitButton();

				wait01.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Amount')]")));
				driver.navigate().back();

				// Verify deposit history
				Thread.sleep(1000);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);

				// Verify transaction history
				funcValidateTransacHistory(driver, mt4Account, "UnionPay", depositAmount, Brand);
				System.out.println("Made UnionPay deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Fasapay only supports USD
	void FasapayDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		String fasaAmount, mt4Account = "";
		// Set<String> handleS;
		WebElement selAcc = null;
		cpDeposit depositEle = new cpDeposit(driver, Brand);
		
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'E-WALLET')]")).click();
		}
	    
		// Select fasapay
		WebElement fasapay = depositEle.getFasapay();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fasapay);
		fasapay.click();
		Thread.sleep(1000);

		// Get current window handler
		// handle = driver.getWindowHandle();

		// Select the account
		funcClickAccountList(Brand);
		// Need to allow time for CSS change. Otherwise, script can't
		// accountInfo

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}

		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.xpath("//div[label = 'Amount(USD)']//input")).sendKeys(depositAmount);

				// Input Notes
				depositEle.getImportantNotes()
						.sendKeys("Test CP FasaPay Deposit " + depositAmount);

				funcClickSubmitButton();

				// Wait until navigating to 3rd party
				try {
					wait01.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Amount')]")));
					// Wait until visibility of 3rd party element
					wait01.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//h4[contains(text(),'Your Order Summary')]")));
					fasaAmount = driver.findElement(By.xpath("//tr[3]//td[1]//strong")).getText();
					if (fasaAmount.contains(","))
						fasaAmount = fasaAmount.replace(",", "");
					System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
					Utils.funcIsStringContains(fasaAmount, depositAmount, Brand);

				} catch (TimeoutException e) {
					System.out.println(
							"\n!!!Submit failure! Please make sure correct Fasapay info configured in disconf (biz_payment.properties).\n");
				}
				driver.navigate().back();
				Thread.sleep(1000);

				// Verify the transaction history
				driver.get(Utils.ParseInputURL(TraderURL) + "transactionHistory");
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "FasaPay", depositAmount, Brand);
				System.out.println("Made Fasapay deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				
				
				
				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "TestEnv", "Brand" })
	// Mobilepay
	void MobilepayDeposit(String TraderURL, String TestEnv, String Brand) throws Exception {

		depositAmount = "1999.55";
		String handle, mt4Account = "";
		WebElement selAcc = null;
		BigDecimal rmbAmount = null;
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
	    
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'E-WALLET')]")).click();
		}
	    
		// Select Mobile deposit
		depositEle.getMobilepay().click();
		Thread.sleep(1000);

		// Get current window handler
		// handle = driver.getWindowHandle();

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any suitable account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				depositEle.getUSDAmount().sendKeys(depositAmount);
				

				// Input Notes
				depositEle.getImportantNotes()
							.sendKeys("Test CP MobilePay Deposit " + depositAmount);
				
				/*
				 * //Input deposit amount
				 * driver.findElement(By.
				 * cssSelector("div.form_box ul.clearfix:nth-child(1) li.fr div > input"
				 * )).sendKeys(depositAmount);
				 * driver.findElement(By.
				 * cssSelector("div.form_box ul.clearfix:nth-child(3) div > input"
				 * )).sendKeys("Test CP Mobile Deposit "+depositAmount);
				 */
				// Verify Exchange Rate and RMB amount
				rmbAmount = funcVerifyExchangeRateAndAmount(Brand);

				funcClickSubmitButton();

				try {
					/*
					 * handleS=driver.getWindowHandles();
					 * for(String s:handleS)
					 * {
					 * if(!s.equals(handle))
					 * {
					 * driver.switchTo().window(s);
					 */
					wait02.until(
							ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='customer_ac']")));
					// Assert.assertEquals(driver.getCurrentUrl(),"http://transferpay.paylomo.net/vtmarketsali/");
					driver.findElement(By.xpath("//input[@name='customer_ac']")).sendKeys(mt4Account);
					driver.findElement(By.xpath("//button[@class='btn']")).click();
					Thread.sleep(1000);

					// Acquire order info
					wait02.until(ExpectedConditions.visibilityOfElementLocated(By.id("order_amount")));
					Thread.sleep(500);

					// Click submit and check rmb amount
					driver.findElement(By.xpath("//button[@class='btn pull-right']")).click();
					Thread.sleep(1000);
					String amount = driver
							.findElement(By.cssSelector("div.col-12 div:nth-child(1) p > font:nth-child(2)")).getText();
					// Utils.funcIsStringEquals(amount, rmbAmount.toString(),
					// Brand);
					BigDecimal bigDecimalMoney = new BigDecimal(amount);
					System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
					Assert.assertTrue(rmbAmount.compareTo(bigDecimalMoney) < 0, "Calculated RMB Amount " + rmbAmount
							+ " should be less than the actual payment " + bigDecimalMoney);

					// driver.close();
					// driver.switchTo().window(handle);
					/*
					 * }
					 * }
					 */
				} catch (Exception e) {
					System.out.println(
							"\n!!!Submit failure or channel closed! Please make sure correct Mobile Pay info configured in disconf (biz_payment.properties).\n");
				}
				// confirm deposit completed
				// driver.findElement(By.cssSelector("button.el-button:nth-child(2)")).click();
				driver.get(Utils.ParseInputURL(TraderURL) + "transactionHistory");

				// Verify the transaction history
				funcValidateTransacHistory(driver, mt4Account, "Mobile Pay", depositAmount, Brand);
				System.out.println("Made Mobile deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// Unionpay only supports USD currency
	void UnionPayP2PDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String mt4Account = "", handle = "", urlbase = "", cookie = "", displayName = "";
		Set<String> handleS;
		BigDecimal depositLimit = new BigDecimal("200.00");
		BigDecimal errorInput = depositLimit.subtract(new BigDecimal("0.01"));
		BigDecimal diff = depositLimit.subtract(new BigDecimal("0.00"));
		BigDecimal rmbAmount = null;
		WebElement up2p = null;
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
	    
		depositAmount = "5000.00";
		
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);
		
	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'LOCAL TRANSFER')]")).click();
		}
	    
		// Click China UnionPay Transfer block
	    up2p = depositEle.getUP2P();
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", up2p);
		Thread.sleep(500);
		up2p.click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}

		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				depositEle.getAmount().sendKeys(depositAmount);

				// Input Notes
				depositEle.getImportantNotes()
							.sendKeys("Test CP UnionPay P2P Deposit " + depositAmount);

				// Get username from profile API
				urlbase = Utils.ParseInputURL(TraderURL);
				cookie = driver.manage().getCookies().toString();
				displayName = CPPaymentDetail.funcCPAPIQueryUsername(urlbase, cookie, TestEnv);

				// Input Card holder's name
				depositEle.getUP2PCardHolderName().sendKeys(displayName);

				Thread.sleep(500);

				// Verify Exchange Rate and RMB amount
				rmbAmount = funcVerifyExchangeRateAndAmount(Brand);

				/*
				 * //verify if the result of (calculated amount - displayed
				 * amount) is between [0, 0.01]
				 * diff = exRate.multiply(new
				 * BigDecimal("5000.00")).subtract(rmbAmount);
				 * if ( diff.compareTo( new BigDecimal("0.00") ) > 0
				 * || diff.compareTo( new BigDecimal("-0.01")) < 0 )
				 * {
				 * Assert.assertTrue(false,
				 * "Deposit Amount * Exchange Rate != calculated RMB: "+exRate.
				 * multiply(new BigDecimal("5000.00")));
				 * }
				 * 
				 * System.out.println("Calculated RMB is correct");
				 */

				handle = driver.getWindowHandle();
				funcClickSubmitButton();

				try {
					// Assert.assertEquals(driver.getCurrentUrl(),"http://www.settlebm.com/in/gateway");
					wait01.until(ExpectedConditions.visibilityOfElementLocated(
							By.cssSelector("a.layui-layer-ico.layui-layer-close.layui-layer-close1"))).click();
					// wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("money")));
					String money = driver.findElement(By.id("money")).getText();
					BigDecimal bigDecimalMoney = new BigDecimal(money);
					
					System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
					Assert.assertTrue(rmbAmount.compareTo(bigDecimalMoney) < 0, "Calculated RMB Amount " + rmbAmount
							+ " should be less than the actual payment " + bigDecimalMoney);
					System.out.println("Actual payment amount : " + bigDecimalMoney + " is correct");
				} catch (Exception e) {
					System.out.println(
							"\n!!!Submit failure or channel closed! Please make sure correct Neteller info configured in disconf (biz_payment.properties).\n");

				}

				/*
				 * driver.close();
				 * driver.switchTo().window(handle);
				 * }
				 * }
				 */

				// Click YES for completing the deposit
				// wait01.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//span[contains(text(),'Yes')]")))).click();
				driver.get(Utils.ParseInputURL(TraderURL) + "transactionHistory");
				Thread.sleep(1000);

				// Verify the transaction history
				if (Brand.equalsIgnoreCase("vt")) {
					funcValidateTransacHistory(driver, mt4Account, "UnionPay transfer", depositAmount, Brand);
				} else {
					funcValidateTransacHistory(driver, mt4Account, "UnionPay Transfer", depositAmount, Brand);

				}
				System.out.println(
						"Made China UnionPay Transfer deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "TraderURL", "ThaiAccount", "ThaiPwd", "Brand",
			"TestEnv" })
	// Thailand Zotapay
	void ThailandDeposit(String AdminURL, String AdminName, String AdminPass, String TraderURL, String ThaiAccount,
			String ThaiPwd, String Brand, String TestEnv) throws Exception {

		String subChannel = "ZOTAPAY";
		String channelNo = "1";

		funcThailandDeposit(AdminURL, AdminName, AdminPass, TraderURL, Brand, TestEnv, subChannel, channelNo);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "TraderURL", "ThaiAccount", "ThaiPwd", "Brand",
			"TestEnv" })
	// Thailand bank transfer needs a special user account (with country =
	// Thailand)
	void ThailandPaytoday(String AdminURL, String AdminName, String AdminPass, String TraderURL, String ThaiAccount,
			String ThaiPwd, String Brand, String TestEnv) throws Exception {

		String subChannel = "PAYTODAY";
		String channelNo = "2";

		funcThailandDeposit(AdminURL, AdminName, AdminPass, TraderURL, Brand, TestEnv, subChannel, channelNo);

	}
	
	

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "TraderURL", "ThaiAccount", "ThaiPwd", "Brand",
			"TestEnv" })
	void ThailandThaiPay(String AdminURL, String AdminName, String AdminPass, String TraderURL, String ThaiAccount,
			String ThaiPwd, String Brand, String TestEnv) throws Exception {

		String subChannel = "THAIPAY";
		String channelNo = "2";

		funcThailandDeposit(AdminURL, AdminName, AdminPass, TraderURL, Brand, TestEnv, subChannel, channelNo);

	}
	
	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "TraderURL", "ThaiAccount", "ThaiPwd", "Brand",
			"TestEnv" })
	void ThailandMijiPay(String AdminURL, String AdminName, String AdminPass, String TraderURL, String ThaiAccount,
			String ThaiPwd, String Brand, String TestEnv) throws Exception {

		String subChannel = "MIJIPAY";
		String channelNo = "3";

		funcThailandDeposit(AdminURL, AdminName, AdminPass, TraderURL, Brand, TestEnv, subChannel, channelNo);

	}
	

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "TraderURL", "MalayAccount", "MalayPwd", "Brand",
			"TestEnv" })
	// Malaysia bank transfer needs a special user account (with country =
	// Malaysia)
	void MalaysiaDeposit(String AdminURL, String AdminName, String AdminPass, String TraderURL, String MalayAccount,
			String MalayPwd, String Brand, String TestEnv) throws Exception {

		// channel: 1 ZotaPay 2 PayTrust
		String payChannel = "1";
		funcMalaysiaPay(AdminURL, AdminName, AdminPass, TraderURL, MalayAccount, MalayPwd, Brand, TestEnv, payChannel);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "AdminURL", "AdminName", "AdminPass", "TraderURL", "MalayAccount", "MalayPwd", "Brand",
			"TestEnv" })
	// Malaysia bank transfer needs a special user account (with country =
	// Malaysia)
	void MalaysiaPayTrust(String AdminURL, String AdminName, String AdminPass, String TraderURL, String MalayAccount,
			String MalayPwd, String Brand, String TestEnv) throws Exception {

		// channel: 1 ZotaPay 2 PayTrust
		String payChannel = "2";
		funcMalaysiaPay(AdminURL, AdminName, AdminPass, TraderURL, MalayAccount, MalayPwd, Brand, TestEnv, payChannel);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "NigeAccount", "NigePwd", "Brand", "TestEnv" })
	// Nigeria bank transfer needs a special user account (with country =
	// Nigeria)
	void NigeriaDeposit(String TraderURL, String NigeAccount, String NigePwd, String Brand, String TestEnv)
			throws Exception {
		BigDecimal diff = new BigDecimal("0.00");
		String handle, mt4Account = "";
		Set<String> handleS;
		depositAmount = "1300.00";

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		// Select Nigeria deposit
		driver.findElement(By.xpath(".//div[@class='bottom'][contains(text(),'Nigeria Instant Bank Wire Transfer')]"))
				.click();
		Thread.sleep(1000);

		// Get current window handler
		handle = driver.getWindowHandle();

		// Select the account
		funcClickAccountList(Brand);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any suitable account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				driver.findElement(By.cssSelector("div.form_box ul.clearfix:nth-child(1) li.fr div > input"))
						.sendKeys(depositAmount);
				driver.findElement(By.cssSelector("div.form_box ul.clearfix:nth-child(2) li.fl div > input"))
						.sendKeys("Test CP Nigeria Deposit " + depositAmount);

				// Verify Exchange Rate and NGN amount
				funcVerifyExchangeRateAndAmount(Brand);

				funcClickSubmitButton();

				handleS = driver.getWindowHandles();
				for (String s : handleS) {
					if (!s.equals(handle)) {
						driver.switchTo().window(s);
						// Check if the url contains specified string
						Utils.funcIsStringContains(driver.getCurrentUrl(), "vantage-fx1.account.fund/paynet", Brand);
						driver.close();
						driver.switchTo().window(handle);
					}
				}

				// Click YES for completing the deposit
				wait01.until(
						ExpectedConditions.visibilityOfElementLocated((By.xpath("//span[contains(text(),'Yes')]"))))
						.click();
				Thread.sleep(1000);

				// Verify the transaction history
				funcValidateTransacHistory(driver, mt4Account, "Internet Banking (Nigeria)", depositAmount, Brand);
				System.out.println("Made Nigeria deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "VietAccount", "VietPwd", "Brand", "TestEnv" })
	// Vietnam bank transfer needs a special user account (with country =
	// Vietnam)
	void VietnamDeposit(String TraderURL, String VietAccount, String VietPwd, String Brand, String TestEnv)
			throws Exception {

		String subChannel = "ZOTAPAY";

		funcVietnamDeposit(TraderURL, Brand, TestEnv, subChannel);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "VietAccount", "VietPwd", "Brand", "TestEnv" })
	void VietnamXpay(String TraderURL, String VietAccount, String VietPwd, String Brand, String TestEnv)
			throws Exception {

		String subChannel = "XPAY";

		funcVietnamDeposit(TraderURL, Brand, TestEnv, subChannel);

	}
	
	

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// BITCOIN deposit
	void BitCoinDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String subChannel = "Bitcoin";

		funcCryptoDeposit(TraderURL, Brand, TestEnv, subChannel);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// USDT deposit
	void UsdtDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String subChannel = "OMNI";

		funcCryptoDeposit(TraderURL, Brand, TestEnv, subChannel);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// ERC20 deposit
	void ERC20Deposit(String TraderURL, String Brand, String TestEnv) throws Exception {

		String subChannel = "ERC20";

		funcCryptoDeposit(TraderURL, Brand, TestEnv, subChannel);

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// Bitwallet deposit.
	void BitwalletDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {
		String mt4Account = "";
		String vendorSite = "bitwallet.com";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
	    
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'E-WALLET')]")).click();
		}
	    
		// Select Bitwallet Deposit
		driver.findElement(By.xpath("//div[contains(text(),'bitwallet Deposit')]")).click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have USD/EUR/JPY accounts!");
		}

		for (WebElement webElement : element) {

			String accountInfo = webElement.findElement(By.tagName("span")).getText();
			System.out.println("AccountInfo is " + accountInfo);

			// Get mt4 Account
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();

			// If mt4 account is a test account
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount and notes
				depositEle.getAmount().sendKeys(depositAmount);
				depositEle.getImportantNotes().sendKeys("Test CP Bitwallet Deposit " + depositAmount);

				funcClickSubmitButton();

				Assert.assertTrue(driver.getCurrentUrl().contains(vendorSite),
						"Current URL is: " + driver.getCurrentUrl() + ", doesn't contain " + vendorSite);

				// Confirm
				/*
				 * wait01.until(
				 * ExpectedConditions.visibilityOfElementLocated((By.
				 * xpath("//a[@class='el-button btn_blue']"))))
				 * .click();
				 */
				System.out.println("Made Bitwallet deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				driver.navigate().to(TraderURL);
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);

				// Validate Transaction History
				funcValidateTransacHistory(driver, mt4Account, "Bitwallet", depositAmount, Brand);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// SticPay deposit.
	void SticPayDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {
		String mt4Account = "";
		String vendorSite = "sticpay.com";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'E-WALLET')]")).click();
		}
	    
		// Select Sticpay
		driver.findElement(By.xpath("//div[contains(text(),'SticPay')]")).click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have available accounts!");
		}

		for (WebElement webElement : element) {

			String accountInfo = webElement.findElement(By.tagName("span")).getText();
			System.out.println("AccountInfo is " + accountInfo);
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount and notes
				depositEle.getAmount().sendKeys(depositAmount);

				// Input SticPay Email
				driver.findElement(By.xpath("//div[label = 'SticPay Email']//input"))
						.sendKeys("testSticpay@test.com");

				// Input Notes
				depositEle.getImportantNotes().sendKeys("Test CP SticPay Deposit " + depositAmount);

				// After Submit, browser navigates to pay.sticpay.com url
				funcClickSubmitButton();
				Assert.assertTrue(driver.getCurrentUrl().contains(vendorSite),
						"Current URL is: " + driver.getCurrentUrl() + ", doesn't contain " + vendorSite);

				// Confirm
				/*
				 * wait01.until(
				 * ExpectedConditions.visibilityOfElementLocated((By.
				 * xpath("//a[@class='el-button btn_blue el-button--default']"))
				 * ))
				 * .click();
				 */
				System.out.println("Made Sticpay deposit of " + depositAmount + " to account " + mt4Account);

				// Verify transaction history
				Thread.sleep(500);
				driver.navigate().to(TraderURL);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "SticPay", depositAmount, Brand);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	// Canada Interac E-Transfer deposit.
	void InteracDeposit(String TraderURL, String Brand, String TestEnv) throws Exception {
		String mt4Account = "";
		depositAmount = "1100.55";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
	    
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			driver.findElement(By.xpath("//div[contains(text(),'LOCAL TRANSFER')]")).click();
		}
		// Select Canada E-transfer Deposit
		depositEle.getInterac().click();
		Thread.sleep(1000);

		// Select the account
		funcClickAccountList(Brand);

		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have CAD accounts!");
		}

		for (WebElement webElement : element) {

			String accountInfo = webElement.findElement(By.tagName("span")).getText();
			System.out.println("AccountInfo is " + accountInfo);

			// Get mt4 Account
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();

			// If mt4 account is a test account
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				depositEle.getAmount().sendKeys(depositAmount);

				// Input Notes
				depositEle.getImportantNotes()
						.sendKeys("Test CP " + "Canada Interac E-Transfer Deposit " + depositAmount);

				funcClickSubmitButton();

				
				try {
					// Ensure navigate to thaipaygo.com
					wait03.until(ExpectedConditions.urlContains("interac.express-connect.com"));
					
					//Choose sub channel as TD Canada Trust
					driver.findElement(By.cssSelector("div.bank-item:nth-child(12) a")).click();
					
					//Click Interac E-Transfer
					driver.findElement(By.id("interace")).click();
					
					//Verify 3rd party amount 
					System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
					Utils.funcIsStringContains(driver.findElement(By.xpath("//span[contains(text(), 'Amount')]//span[1]")).getText(), depositAmount, Brand);

				} catch (Exception e) {

					System.out.println("The current url is: " +
							driver.getCurrentUrl());
					
					// If submit failure happens
					if (driver.findElements(By.xpath("//div[label = 'Important notes']//input")).size()>0) {
						
						System.out.println(
								"\n!!!Submit failure or error occours! Please make sure correct Crypto info configured in disconf (biz_payment.properties).\n");
			
					//If redirect to 3rd party and there is an exception happening, abort the test case.
					}else {
						
						Assert.assertTrue(false,
							"\n!!!Something wrong. Please execute the case manually! \n");
						
				    }
				}
				
				// Verify transaction history
				Thread.sleep(500);
				driver.navigate().to(TraderURL);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);

				// Validate Transaction History
				funcValidateTransacHistory(driver, mt4Account, "Canadian Local Bank Transfer", depositAmount, Brand);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}
	
	
	
	
	
	
	
	/*
	 * Developed by Alex.L for checking if account group is test group on
	 * 19/07/2019
	 * Example:
	 * boolean flag =
	 * TraderDeposit.funcCheckBPayNo("http://ben.vantagefx.com:8004", cookie,
	 * "975023");
	 */
	public static boolean funcCheckBPayNo(String traderURL, String cookie, String mt4Acc) throws Exception {
		String sampleStr = "test";
		boolean flag = false, isTestGroup = false;

		// System.out.println(url);
		String accDetail = RestAPI.testPostTradeAccountList(traderURL, cookie);

		JSONParser parser = new JSONParser();
		Object resultObject = parser.parse(accDetail);
		JSONObject obj = (JSONObject) resultObject;
		System.out.println("==================Result====================");

		JSONArray data = (JSONArray) obj.get("rows");
		for (Object item : data) {
			if (flag) {
				break;
			}
			for (Object key : ((JSONObject) item).keySet()) {
				Object value = ((JSONObject) item).get(key.toString());

				if (value.toString().equals(mt4Acc)) {
					flag = true;
					try {
						Object groupName = ((JSONObject) item).get("mT4Group");
						if (groupName.toString().toLowerCase().contains(sampleStr.toLowerCase())) {
							System.out.println("Pass the group check: AccGroup of mt4 account " + mt4Acc
									+ " is test group: " + groupName.toString());
							isTestGroup = true;
						} else {
							System.out.println("Failed the group check: AccGroup of mt4 account " + mt4Acc
									+ " is NOT test group: " + groupName.toString());
						}
					} catch (NullPointerException e) {
						System.out.println("Something wrong! mT4Group not exist in restful API!");
					}

					break;
				}

			}

		}
		return isTestGroup;

	}

	/*
	 * Developed by Alex.L for validating the bank info in International Bank
	 * Transfer on 13/08/2019
	 * It would validate the bank info with the specified currency
	 * 
	 */
	public static void funcValidateI8NBankInfo(WebDriver driver, String currency, String Brand) throws Exception {

		String[] entry = null;
		boolean flag = false;
		String bank_name = "", acc_no = "", swift = "", branch_name = "", branch_addr = "", acc_name = "",
				bene_addr = "", bene_acc_no = "", bsb = "";
		String fileName = "";
		Scanner inputFile;

		switch (Brand) {

		case "ky":
		case "fca":
		case "vfsc2":

			fileName = "I8Ndeposit_cima.txt";
			break;

		case "vt":
			fileName = "I8Ndeposit_vt.txt";
			break;

		case "fsa":
		case "svg":
			fileName = "I8Ndeposit_pug.txt";
			break;

		case "vfsc":
			fileName = "I8Ndeposit_vfsc.txt";
			break;
			
		case "au":
		default:
			System.out.println(Brand + " is not supported for Bank Info Check in International Bank Transfer Deposit.");
			Assert.assertTrue(false,
					Brand + " is not supported for Bank Info Check in International Bank Transfer Deposit.");
			break;

		}

		inputFile = new Scanner(new File(Utils.workingDir + "//src//main//resources//vantagecrm//doc//" + fileName));

		inputFile.useDelimiter("[\\r]");
		while (inputFile.hasNext()) {
			if (flag == true) {
				// System.out.println("going to break");
				break;
			}
			String line = inputFile.next();
			if (line.contains(currency)) {
				flag = true;
				System.out.println("\nGoing to verify " + currency + " bank info----->");

				String[] b = line.split(";");
				for (String a : b) {
					// System.out.println(a);
					entry = a.split("=");

					switch (entry[0]) {
					case "Bank Name":
						bank_name = entry[1];
						//liufeng vfsc2
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[contains(text(),'Bank Name')]/../span[2]")).getText(),
								bank_name, Brand);
						
						
						
						/*
						 * Utils.funcIsStringEquals(
						 * driver.findElement(By.cssSelector("li:nth-child(1) > span.value_info")).
						 * getText(), bank_name, Brand);
						 */
						break;
 
					case "SWIFT AUD":
						swift = entry[1];
						Utils.funcIsStringEquals(
								driver.findElement(By.cssSelector("div.bank_info ul:nth-child(10) span.value_info")).getText(),
								swift, Brand);
						break;

					case "SWIFT Code":
						swift = entry[1];
						//liufeng vfsc2
						
						Utils.funcIsStringEquals(
								driver.findElement(By.cssSelector("div.bank_info ul:nth-child(10) span.value_info")).getText(),
								swift, Brand);
						break;

					case "Branch Name":
						branch_name = entry[1];
						//liufeng vfsc2
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[contains(text(),'Bank Branch')]/../span[2]")).getText(),
								branch_name, Brand);
						break;

					case "Branch Address":
						branch_addr = entry[1];
						
						//liufeng vfsc2
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[contains(text(),'Branch Address')]/../span[2]")).getText(),
								branch_addr, Brand);
						break;

					case "Beneficiary Account Name":
						acc_name = entry[1];
						
						//liufeng vfsc2
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[contains(text(),'Bank Beneficiary Account Name')]/../span[2]")).getText(),
								acc_name, Brand);
						
						/*
						 * Utils.funcIsStringEquals(
						 * driver.findElement(By.cssSelector("li:nth-child(4) > span.value_info")).
						 * getText(), acc_name, Brand);
						 */
						break;

					case "Beneficiary Account Number":
						bene_acc_no = entry[1];
						
						//liufeng vfsc2
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[contains(text(),'Bank Beneficiary Account Number')]/../span[2]")).getText(),
								bene_acc_no, Brand);
						
						/*
						 * Utils.funcIsStringEquals(
						 * driver.findElement(By.cssSelector("li:nth-child(5) > span.value_info")).
						 * getText(), bene_acc_no, Brand);
						 */
						break;

					case "Beneficiary Address":
						bene_addr = entry[1];
						
						//liufeng vfsc2
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[contains(text(),'Beneficiary Address')]/../span[2]")).getText(),
								bene_addr, Brand);
						
						/*
						 * Utils.funcIsStringEquals(
						 * driver.findElement(By.cssSelector("li:nth-child(6) > span.value_info")).
						 * getText(), bene_addr, Brand);
						 */
						break;

					case "BSB Number":
						bsb = entry[1];
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[text()='BSB Number']/following-sibling::span")).getText(), bsb,
								Brand);
						break;

					case "Account Number":
						acc_no = entry[1];
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[text()='Account Number']/following-sibling::span")).getText(),
								acc_no, Brand);
						break;

					case "Account Name":
						acc_name = entry[1];
						Utils.funcIsStringEquals(
								driver.findElement(By.xpath("//span[text()='Account Name']/following-sibling::span")).getText(),
								acc_name, Brand);
						break;

					}

				}
			}
		}
	}

	/*
	 * Developed by Alex.L for validating the bank info in Australia Bank
	 * Transfer on 15/08/2019
	 * It would validate the bank info with the specified currency
	 * 
	 */
	public static void funcValidateAUBankInfo(WebDriver driver, String Brand) throws Exception {

		String[] entry = null;
		String bank_name = "", acc_no = "", swift = "", addr = "", acc_name = "", bsb = "";
		Scanner inputFile = new Scanner(
				new File(Utils.workingDir + "//src//main//resources//vantagecrm//doc//AUdeposit.txt"));

		System.out.println("Going to check AU bank info ========>>>>>");
		inputFile.useDelimiter("[\\r]");
		while (inputFile.hasNext()) {
			String line = inputFile.next();
			String[] b = line.split(";");

			for (String a : b) {
				entry = a.split("=");
				switch (entry[0]) {
				case "Bank Name":
					bank_name = entry[1];
					WebElement webElement = driver.findElement(By.cssSelector("li:nth-child(2) > span.value_info"));
					// ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);",
					// webElement);
					Utils.funcIsStringEquals(
							driver.findElement(By.cssSelector("li:nth-child(2) > span.value_info")).getText(),
							bank_name, Brand);
					break;

				case "SWIFT Code":
					swift = entry[1];
					Utils.funcIsStringEquals(
							driver.findElement(By.cssSelector("li:nth-child(7) > span.value_info")).getText(), swift,
							Brand);
					break;

				case "Address":
					addr = entry[1];
					Utils.funcIsStringEquals(
							driver.findElement(By.cssSelector("li:nth-child(3) > span.value_info")).getText(), addr,
							Brand);
					break;

				case "BSB Number":
					bsb = entry[1];
					Utils.funcIsStringEquals(
							driver.findElement(By.cssSelector("li:nth-child(5) > span.value_info")).getText(), bsb,
							Brand);
					break;

				case "Account Number":
					acc_no = entry[1];
					Utils.funcIsStringEquals(
							driver.findElement(By.cssSelector("li:nth-child(6) > span.value_info")).getText(), acc_no,
							Brand);
					break;

				case "Account Name":
					acc_name = entry[1];
					Utils.funcIsStringEquals(
							driver.findElement(By.cssSelector("li:nth-child(4) > span.value_info")).getText(), acc_name,
							Brand);
					break;

				}

			}

		}
	}

	/*
	 * Developed by Alex.L for validating the transaction info in transaction
	 * history on 21/08/2019
	 */
	public static void funcValidateTransacHistory(WebDriver driver, String mt4Account, String method, String amount,
			String Brand) throws Exception {

		String rAccount, rMethod, rAmount;
		Thread.sleep(1000);

		// check the account
		rAccount = driver.findElement(By.xpath("//li[contains(@class,'table_item active')]//tr[1]//td[2]//div[1]"))
				.getText();
		Utils.funcIsStringContains(rAccount, mt4Account, Brand);

		// check the method
		rMethod = driver.findElement(By.xpath("//li[contains(@class,'table_item active')]//tr[1]//td[3]//div[1]"))
				.getText();
		Utils.funcIsStringContains(rMethod.toLowerCase(), method.toLowerCase(), Brand);

		// check the amount
		rAmount = driver.findElement(By.xpath("//li[contains(@class,'table_item active')]//tr[1]//td[4]//div[1]"))
				.getText();
		if (rAmount.contains(",")) {
			rAmount = rAmount.replace(",", "");
		}
		Utils.funcIsStringContains(rAmount, amount, Brand);

	}

	// Yanni on 25/09/2019: Navigate to Deposit Fund page
	void funcNavigate2DepositFund(String TraderURL, String Brand ) throws Exception {
		
		TraderURL = Utils.ParseInputURL(TraderURL)+"home";
		driver.navigate().to(TraderURL);
		
		  if (Brand.equalsIgnoreCase("vt")) { 
			  //wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Live accounts')]"))); 
			  }
		  else { 
			  //wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'LIVE ACCOUNTS')]"))); 
		  	  
			  //Wait for spinner's disappearance
			  wait02.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("i.client-portal-loading-au")));
		 }
		 
		
		
		
		driver.findElement(By.xpath("//span[contains(translate(., 'Funds', 'FUNDS'), 'FUNDS')]")).click();
		Thread.sleep(2000);
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//li[@class='el-submenu is-opened']//ul//li[contains(translate(., 'Deposit funds', 'DEPOSIT FUNDS'),'DEPOSIT FUNDS')]")))
				.click();
		Thread.sleep(1000);
	}

	// Yanni on 25/09/2019: Navigate to TRASACTION HISTORY -> DEPOSIT
	void funcNavigate2DepositHistory(String Brand) throws Exception {

		driver.findElement(By.xpath("//span[contains(translate(., 'Home', 'HOME'), 'HOME')]")).click();

		if (Brand.equalsIgnoreCase("vt")) {
			wait01.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Live accounts')]")));
		} else {
			wait01.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'LIVE ACCOUNTS')]")));
		}
		// Yanni: Commented it out as new design is used. When user logs in, the
		// sidebar menu is expanded by default.
		// driver.findElement(By.xpath("//div[@class='header_btn']")).click();

		Thread.sleep(1000);

		try {
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"//li[@class='el-submenu is-opened']//ul//li[contains(translate(., 'Transaction history', 'TRANSACTION HISTORY'),'TRANSACTION HISTORY')]")))
					.click();
		} catch (Exception e) {

			wait01.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//li//div//span[contains(translate(., 'Funds', 'FUNDS'),'FUNDS')]"))).click();
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"//li[@class='el-submenu is-opened']//ul//li[contains(translate(., 'Transaction history', 'TRANSACTION HISTORY'),'TRANSACTION HISTORY')]")))
					.click();
		}
	}

	/*
	 * Alex Liu 13/05/2020 for clicking back to home page after deposit
	 */
	void funcBack2HomePage(String Brand) throws Exception {
		cpDeposit depositEle = new cpDeposit(driver, Brand);
		WebElement back2home = null;
		
		back2home = depositEle.getB2H();
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", back2home);
		Thread.sleep(1000);
		back2home.click();
	}

	/*
	 * Alex Liu 08/02/2021 for clicking Submit
	 */
	void funcClickSubmitButton() throws Exception {
		WebElement submit = driver.findElement(By.xpath("//span[contains(translate(., 'Submit', 'SUBMIT'),'SUBMIT')]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submit);
		submit.click();
		Thread.sleep(3000);
	}

	/*
	 * Alex Liu 09/02/2021 for clicking Yes
	 */
	void funcClickYesButton() throws Exception {
		WebElement submit = driver.findElement(By.xpath("//span[contains(translate(., 'YES', 'Yes'),'Yes')]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submit);
		submit.click();
		Thread.sleep(3000);
	}

	/*
	 * Alex Liu 08/02/2021 for select and click account list
	 */
	void funcClickAccountList(String Brand) throws Exception {

		//wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("el-loading-parent")));
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("el-loading-parent")));

		WebElement selAcc = null;

		// Select the account
		if (Brand.equalsIgnoreCase("vt")) {
			selAcc = driver.findElement(By.id("accountNumber"));
		} else {
			selAcc = driver.findElement(
					By.cssSelector("div.el-form-item.el-form-item--feedback.is-success.is-required div> input"));
		}
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selAcc);
		Thread.sleep(500);
		selAcc.click();
		Thread.sleep(500);
	}

	/*
	 * Yanni on 31/10/2019: re-factor duplicate codes in CCDepositExistingCard,
	 * CCDepositNewCard3D, CCDepositNewCardNon3D to this function to simplify
	 * 
	 * isNewCard
	 * true: Deposit with new card information
	 * false: use the existing card.
	 * 
	 * status3DSecure
	 * 0: non 3D Secure;
	 * 1： 3D Secure
	 * 2: use existing card. radio button is disabled and can't select
	 * 
	 * Alex Liu on 05/02/2021 for distinguish NAB from non-NAB channel
	 * 
	 * isNAB
	 * true: NAB CC deposit
	 * false: Non-NAB CC deposit, such as VirtualPay, BridgerPay and SDPay.
	 */
	void funCCDeposit(Boolean isNewCard, Boolean isNAB, String TraderURL, String TestEnv, String Brand)
			throws Exception {

		String mt4Account = "", urlbase = "", cookie = "", displayName = "", cardNo = "",
				notes = "Automation test CC deposit";

		List<WebElement> liItems, cardList;
		WebElement cardNoEle = null, cName = null;
		cpDeposit depositEle = new cpDeposit(driver, Brand);
		
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			driver.findElement(By.xpath("//div[contains(text(),'CREDIT CARD')]")).click();
		}
		
		depositEle.getCC().click();
		
		Thread.sleep(1500);

		// Get username from profile API
		urlbase = Utils.ParseInputURL(TraderURL);
		cookie = driver.manage().getCookies().toString();
		displayName = CPPaymentDetail.funcCPAPIQueryUsername(urlbase, cookie, TestEnv);
		// System.out.println(displayName);

		//Click on account number
		depositEle.getCCAccountNumber().click();
		
		Thread.sleep(500);

		// List<WebElement>
		// element=driver.findElements(By.cssSelector("div.el-select-dropdown.el-popper:nth-child(9)
		// div.el-scrollbar
		// div.el-select-dropdown__wrap.el-scrollbar__wrap:nth-child(1) ul >
		// li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		// System.out.println(element.size());
		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
			String accountInfo = webElement.getText();
			// System.out.println(accountInfo);
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				depositEle.getAmount().sendKeys(depositAmount);
				
				if (isNAB == true) {
					// Select "Use New Card" option
					if (isNewCard == true) {

						cardList = depositEle.getCardList();

						if (cardList.size() > 0) {

							cardList.get(0).click();
							Thread.sleep(1000);

							WebElement newCard = driver.findElement(
									By.xpath("//span[contains(translate(., 'New card', 'New Card'),'New Card')]"));
							((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", newCard);
							newCard.click();

							Thread.sleep(1000);
						}

						// Get Element for inputing card number
						cardNoEle = depositEle.getCCInputNumber();

						//Card number should start with "5" for APG
						if (driver.getCurrentUrl().contains("apg")) {
							cardNo = "51" + Utils.randomNumber(14);
						}else {
							cardNo = Utils.randomNumber(16);
						}
						
						// Input card number
						cardNoEle.sendKeys(cardNo);
						System.out.println("USE NEW CARD option, New Card No is: " + cardNo);

						// Input card name
						depositEle.getCCCardName().sendKeys(displayName);

						// Select expire year
						driver.findElement(By.xpath("//input[@placeholder='Year']")).click();
						Thread.sleep(500);

						// Get all li items which has YEAR listed
						liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
						j = r.nextInt(liItems.size());
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
								liItems.get(j));
						liItems.get(j).click();
						Thread.sleep(500);

						// Select expiry month
						driver.findElement(By.xpath("//input[@placeholder='Month']")).click();
						Thread.sleep(500);

						// Get all li items which has MONTHS listed
						liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
						j = r.nextInt(liItems.size());
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
								liItems.get(j));
						liItems.get(j).click();
						
						

					} else {
						// Click on the card list
						depositEle.getCardListElement().click();

						List<WebElement> sub_element = Utils.funcGetListItem(driver,
								"div.el-select-dropdown.el-popper");
						// List<WebElement>
						// sub_element=driver.findElements(By.cssSelector("div.el-select-dropdown.el-popper:nth-child(9)
						// ul > li"));
						// System.out.println(sub_element.size());
						if (sub_element.size() == 0) {
							Assert.assertTrue(false, "This user doesn't have any approved Credit Card Records!");
						}

						// choose the first bind card
						if (sub_element.size() > 0) {
							sub_element.get(0).click();
						}
						Thread.sleep(500);

						// input the middle 8 numbers
						depositEle.getCCMiddle6Num().sendKeys(Utils.randomNumber(6));
						
						// Validate the username displayed
						cName = depositEle.getCCCardName();

						Object value = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", cName);
						System.out.println(
								"User's account name is: " + value.toString() + " and display name is: " + displayName);

						/* No need to input expiry info for binded card */

					}

					// CVV
					depositEle.getCVV().sendKeys("123");
					
					//Card Issuing Bank
					if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
						driver.findElement(By.xpath("//input[@placeholder= 'Please enter your card issuing bank name']")).sendKeys("BOC");
					}
					Thread.sleep(1000);

				} else {
					// Input notes
					depositEle.getCCNotes().sendKeys(notes);
				}

				funcClickSubmitButton();

				/*
				 * if (TestEnv.equalsIgnoreCase("beta")) {
				 * Thread.sleep(20000);
				 * } else {
				 * wait01.until(ExpectedConditions.invisibilityOfElementLocated(
				 * By.xpath("//label[contains(text(),'Amount')]")));
				 * }
				 */
				// New Card Deposit, with 3D Secure or non 3D Secure, the return
				// page has different CSS.
				/*
				 * // VT default: 3ds
				 * if (Brand.equalsIgnoreCase("vt")) {
				 * returnMsgSelector =
				 * "//div[@id='result_wrapper']//div[2]//p[1]";
				 * } else {
				 * if (status3DSecure != 1) {
				 * returnMsgSelector =
				 * "//div[@class='form_success_content']//p[@class='last']";
				 * } else {
				 * 
				 * // returnMsgSelector ="//div[@class='result_content']//p";
				 * returnMsgSelector =
				 * "//div[@class='form_success_content']//p[@class='last']";
				 * }
				 * }
				 * //
				 * wait100.until(ExpectedConditions.visibilityOfElementLocated(
				 * By.xpath(returnMsgSelector)));
				 * if (Brand.equalsIgnoreCase("vt")) {
				 * Utils.funcIsStringContains(driver.findElement(By.xpath(
				 * returnMsgSelector)).getText(),
				 * "Your deposit was unsuccessful. Please try again.", Brand);
				 * }
				 * Utils.funcIsStringContains(driver.findElement(By.xpath(
				 * returnMsgSelector)).getText(),
				 * "Deposit failed, please try again later.", Brand);
				 * 
				 * // Confirm
				 * funcBack2HomePage(Brand);
				 * System.out.println("Made Credit Card deposit of " +
				 * depositAmount + " to account " + mt4Account);
				 */

				// Won't verify the 3rd party status due to different cc channels and disconf issues.
				// Only verify if submission was completed.  Abort if keep staying at submission page.
				wait01.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Amount')]")));
				

				driver.navigate().back();

				// Verify CC deposit history
				Thread.sleep(1000);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, "Credit/Debit", depositAmount, Brand);

				if (isNewCard == true) {
					// Verify the card info item. CC deposit with new card will
					// add new card info item in tb_credit_card
					// Check in db
					String selectSql = "select user_id, card_holder_name,card_begin_six_digits,card_last_four_digits, expiry_month,expiry_year,is_del from tb_credit_card where card_holder_name = \'"
							+ displayName + "\' order by create_time desc limit 1;";
					System.out.println();
					System.out.println(
							"Automation test uses fake Credit Card and the deposit is going to fail. When CC Deposit failed,  "
									+ cardNo + " should NOT be recorded in DB! ");
					System.out.println(
							"In the following DB query, check columns card_begin_six_digits and card_last_four_digits please. Ensure they can't match with "
									+ cardNo);

					String dbName = Utils.getDBName(Brand)[1];
					DBUtils.funcreadDB(dbName, selectSql, TestEnv);

				}

				break;

			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	// Alex 04/11/2019 for compare the calculated amount and displayed amount
	void funCCompareAmount(BigDecimal exRate, String depositAmount, BigDecimal disRmbAmount) throws Exception {
		BigDecimal calculateRt = exRate.multiply(new BigDecimal(depositAmount)).setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println("exrate is " + exRate + ", Amount displayed is " + disRmbAmount
				+ " and calculated result is " + calculateRt);

		// verify if the abs result of (calculated amount - displayed amount) is
		// between [0, 0.01]
		BigDecimal diff = exRate.multiply(new BigDecimal(depositAmount)).subtract(disRmbAmount);
		if (diff.compareTo(new BigDecimal("0.00")) > 0 || diff.compareTo(new BigDecimal("-0.01")) < 0) {
			Assert.assertTrue(false, "Deposit Amount * Exchange Rate != calculated RMB: "
					+ exRate.multiply(new BigDecimal(depositAmount)));
		}

		System.out.println("Calculated AMOUNT is correct");
	}

	// Alex Liu 08/02/2021 for verify exchange rate and amount
	BigDecimal funcVerifyExchangeRateAndAmount(String Brand) throws Exception {
		// exAmount is the translated currency amount, e.g. rmbAmount,
		// thbAmount, etc.
		BigDecimal exRate = null, exAmount = null;
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
		
	    exRate = new BigDecimal(depositEle.getExRate().getText());
	    exAmount = new BigDecimal(depositEle.getExAmount().getText());
	    
		System.out.println("Displayed exrate is " + exRate + " exAmount is " + exAmount);

		funCCompareAmount(exRate, depositAmount, exAmount);

		return exAmount;
	}

	// common function: Malaysia bank transfer needs a special user account
	// (with country = Malaysia)
	// Paychannel: 1 ZotaPay 2 PayTrust
	void funcMalaysiaPay(String AdminURL, String AdminName, String AdminPass, String TraderURL, String MalayAccount,
			String MalayPwd, String Brand, String TestEnv, String payChannel) throws Exception {
		BigDecimal diff = new BigDecimal("0.00");
		String handle, mt4Account = "", cookie = "";
		Set<String> handleS;
		depositAmount = "1300.00";

		String payChanneltext = "";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);

		// Change Malaysia payment channel

		if (TestEnv.equals("test")) {

			switch (payChannel) {
			case "1":
				payChanneltext = "Malaysia - Zotapay";
				break;

			case "2":
				payChanneltext = "Malaysia - PayTrust";
			default:

			}
			System.out.println("Set Malaysia payment channnel to: " + payChanneltext);

			AdminURL = Utils.ParseInputURL(AdminURL);
			
			//Temporary comment the line below due to new entry parameter introduced.
			//cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, Brand, TestEnv);

			// test Post modify_malaysia_channel_setting method
			// channel: 1 ZotaPay 2 PayTrust
			RestAPI.testPostMalaysiaChannelSetting(AdminURL, cookie, payChannel);
		}
	    
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'LOCAL TRANSFER')]")).click();
		}
	    
		// Select Malaysia deposit
		depositEle.getMalayDeposit().click();
		Thread.sleep(1000);

		// Get current window handler
		handle = driver.getWindowHandle();

		// Select the account
		// driver.findElement(By.id("accountNumber")).click();
		WebElement selAcc = driver.findElement(By.id("accountNumber"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selAcc);
		Thread.sleep(1000);

		selAcc.click();
		Thread.sleep(500);

		// List<WebElement>
		// element=driver.findElements(By.xpath("//*[@class='el-select-dropdown
		// el-popper']/div/div/ul/li"));
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any available USD account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			System.out.println("AccountInfo:" + accountInfo);
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amountCalculated RMB is correct
				depositEle.getAmount().sendKeys(depositAmount);
				
				WebElement notes = depositEle.getImportantNotes();
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", notes);
				notes.sendKeys("Test CP Malaysia Deposit " + depositAmount);

				// Verify Exchange Rate and MYR amount
				BigDecimal myrAmount = funcVerifyExchangeRateAndAmount(Brand);

				// Submit the deposit form. User is redicrected to paytrust url.
				funcClickSubmitButton();

				handleS = driver.getWindowHandles();
				for (String s : handleS) {
					if (!s.equals(handle)) {
						// Switch to paytrust window
						driver.switchTo().window(s);

						// Verify the page is from paytrust
						Utils.funcIsStringContains(driver.getCurrentUrl(), "paytrust88", Brand);

						// Verify amount of money: amountB == rmbAmount
						BigDecimal amountB = new BigDecimal(driver.findElement(By.cssSelector("h3.m-t-xs")).getText()
								.replaceAll("[a-zA-Z]", "").replace(",", "").trim());
						Utils.funcIsStringContains(driver.findElement(By.cssSelector("h3.m-t-xs")).getText(), "MYR",
								Brand);

						// verify if the abs result of (calculated amount -
						// displayed amount) is between [0, 0.01]
						diff = amountB.subtract(myrAmount);
						if (diff.compareTo(new BigDecimal("0.00")) > 0 || diff.compareTo(new BigDecimal("-0.01")) < 0) {
							Assert.assertTrue(false,
									"Displayed amount: " + myrAmount + " != calculated RMB: " + amountB);
						}

						Select bankSelect = new Select(driver.findElement(By.id("bank")));
						if (bankSelect.getOptions().size() > 0) {

							// Select 1st bank in the list and click button Make
							// Payment
							bankSelect.selectByIndex(0);
							driver.findElement(By.id("submit")).click();
							Thread.sleep(3000);
						} else {
							Assert.assertTrue(false, "No Bank Names in the Bank List.");
						}

						driver.close();
						driver.switchTo().window(handle);
					}
				}

				// Yanni on 1/07/2020: catch exception when CONFIRM dialog is
				// not popped up so the remaining lines can be executed.

				try {
					// Click YES for completing the deposit
					driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
					Thread.sleep(3000);
				} catch (NoSuchElementException e) {
					System.out.println("No CONFIRM dialog popped up.");
					// Manually navigate to HOME PAGE
					driver.navigate().to(TraderURL);
				}

				// Navigate to deposit history
				Thread.sleep(500);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);

				// Verify the transaction history
				funcValidateTransacHistory(driver, mt4Account, "Internet Banking (Malaysia)", depositAmount, Brand);
				System.out.println("Made Malaysia deposit of " + depositAmount + " to account " + mt4Account);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

	
	// Added by Alex Liu: Navigate to Thailand deposit page, switch to subChannel,
	// select Account, input amount(200.00) and notes, submit
	void funcThailandDeposit(String AdminURL, String AdminName, String AdminPass, String TraderURL, String Brand, String TestEnv, String subChannel, String channelNo) throws Exception {

		WebElement thai = null;
		String mt4Account = "", currency = "", cookie = "";
		BigDecimal diff = new BigDecimal("0.00");
		depositAmount = "1300.00";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);

		// Change Thailand payment channel
		if (TestEnv.equals("test") || TestEnv.equals("alpha")) {
			AdminURL = Utils.ParseInputURL(AdminURL);
			//Temporary comment the line below due to new entry parameter introduced.
			//cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, Brand, TestEnv);
				/*
				* API Post testPostThaiChannelSetting method
				* channel: 1 ZotaPay
				* 2 PaytoDay
				* 3 MijiPay
				*/
			RestAPI.testPostThaiChannelSetting(AdminURL, cookie, channelNo);
		}
		
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				driver.findElement(By.xpath("//div[contains(text(),'LOCAL TRANSFER')]")).click();
		}
	    
		// Select Thailand deposit
		thai = depositEle.getThaiDeposit();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", thai);
		Thread.sleep(500);
		thai.click();
		Thread.sleep(1000);
				
		System.out.println(subChannel);
		
		// Choose SECONDARY for ThaiPay in VT
		if (subChannel.contains("THAIPAY")) {
			driver.findElement(
					By.xpath("//span[contains(text(),'SECONDARY')]")).click();

			Thread.sleep(500);
		}

		// Select the account
		funcClickAccountList(Brand);

		// List all account and select one of which group is a TEST account
		// group;
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			currency = accountInfo.replaceAll("[^a-zA-Z]", "");

			// If group is a test group, deposit
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				// VT and Mijipay (both AU and VT) are using Amount(USD)..
				if (Brand.equalsIgnoreCase("vt") || subChannel.equalsIgnoreCase("MIJIPAY")) {
					driver.findElement(By.xpath("//div[label = 'Amount(USD)']//input")).sendKeys(depositAmount);
				}else {
					driver.findElement(By.xpath("//div[label = 'Amount']//input")).sendKeys(depositAmount);
				}
				
				depositEle.getImportantNotes().sendKeys("Test CP Thailand " + subChannel + " Deposit " + depositAmount);

				// Verify Exchange Rate and THB amount
				BigDecimal thbAmount = funcVerifyExchangeRateAndAmount(Brand);
				
				// Submit
				funcClickSubmitButton();

				System.out.println("Made " + subChannel + " deposit of " + depositAmount + " to account " + mt4Account);

				// Allow some time to write records to DB
				Thread.sleep(2000);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				try {
					// Check 
				  switch (subChannel) {
					case "THAIPAY":
						// Ensure navigate to thaipaygo.com
						wait03.until(ExpectedConditions.urlContains("thaipaygo.com"));

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						// GET 3rd party THB amount
						BigDecimal amount = new BigDecimal(driver.findElement(By.cssSelector("p.price-info:nth-child(1) > span.price-item2"))
								.getText().trim().substring(1));
						
						// verify if the abs result of (calculated amount -
						// displayed amount) is between [0, 0.01]
						diff = amount.subtract(thbAmount);
						if (diff.compareTo(new BigDecimal("0.00")) > 0 || diff.compareTo(new BigDecimal("-0.01")) < 0) {
							Assert.assertTrue(false,
									"Displayed amount: " + amount + " != calculated THB: " + thbAmount);
						}else {
							System.out.println("3rd party Displayed amount: " + amount + " almost equals to calculated THB: " + thbAmount);
						}

						break;

					case "PAYTODAY":
						wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(text(),'Scan to make a deposit')]")));
						
						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						// GET thb amount
						String getTextAmount = driver.findElement(By.cssSelector("li:nth-child(4) > span:nth-child(1)"))
								.getText();
						amount = new BigDecimal(getTextAmount);
		
						// verify if the abs result of (calculated amount -
						// displayed amount) is between [0, 0.01]
						diff = amount.subtract(thbAmount);
						if (diff.compareTo(new BigDecimal("0.00")) > 0 || diff.compareTo(new BigDecimal("-0.01")) < 0) {
							Assert.assertTrue(false,
									"Displayed amount: " + amount + " != calculated THB: " + thbAmount);
						}else {
							System.out.println("3rd party Displayed amount: " + amount + " almost equals to calculated THB: " + thbAmount);
						}
						break;

					case "ZOTAPAY":
						// Ensure navigate to thaipaygo.com
						wait03.until(ExpectedConditions.urlContains("zotapay.com"));

						wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.title-base")));
						Assert.assertEquals(driver.getTitle(), "Bank selection");

						break;
						
					case "MIJIPAY":
						// Ensure navigate to thaipaygo.com
						wait03.until(ExpectedConditions.urlContains("payment.pa-sys.com"));

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						// GET 3rd party THB amount
						amount = new BigDecimal(driver.findElement(By.xpath("//h2[contains(text(),'THB')]"))
								.getText().trim().substring(4).replace(",",""));
						
						// verify if the abs result of (calculated amount -
						// displayed amount) is between [0, 0.01]
						diff = amount.subtract(thbAmount);
						if (diff.compareTo(new BigDecimal("0.00")) > 0 || diff.compareTo(new BigDecimal("-0.01")) < 0) {
							Assert.assertTrue(false,
									"Displayed amount: " + amount + " != calculated THB: " + thbAmount);
						}else {
							System.out.println("3rd party Displayed amount: " + amount + " almost equals to calculated THB: " + thbAmount);
						}

						break;
					default:
						System.out.println(subChannel + " is not supported!");

					}
				} catch (Exception e) {

					System.out.println("The current url is: " +
							driver.getCurrentUrl());
					
					// If submit failure happens
					if (driver.findElements(By.xpath("//div[label = 'Important notes']//input")).size()>0) {
						
						System.out.println(
								"\n!!!Submit failure or error occours! Please make sure correct Crypto info configured in disconf (biz_payment.properties).\n");
			
					//If redirect to 3rd party and there is an exception happening, abort the test case.
					}else {
						
						Assert.assertTrue(false,
							"\n!!!Something wrong. Please execute the case manually! \n");
						
				    }
				}
				// Verify transaction history
				Thread.sleep(500);
				driver.navigate().to(TraderURL);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				
				if (Brand.equalsIgnoreCase("vt")) {
					
					funcValidateTransacHistory(driver, mt4Account, "Internet banking (Thailand)", depositAmount, Brand);					
				
				}else {
					
					funcValidateTransacHistory(driver, mt4Account, "Internet Banking (Thailand)", depositAmount, Brand);
				}

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}
	
	
	// Added by Alex Liu 10/02/2021: Navigate to Vietnam deposit page, switch to subChannel,
	// select Account, input amount and notes, submit
	void funcVietnamDeposit(String TraderURL, String Brand, String TestEnv, String subChannel) throws Exception {

		WebElement thai = null;
		String mt4Account = "", currency = "";
		BigDecimal diff = new BigDecimal("0.00");
		depositAmount = "1300.00";	
	    cpDeposit depositEle = new cpDeposit(driver, Brand);

		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);

	    if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
	   				driver.findElement(By.xpath("//div[contains(text(),'LOCAL TRANSFER')]")).click();
	   	}
	    
		// Select Thailand deposit
		thai = depositEle.getVietDeposit();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", thai);
		Thread.sleep(500);
		thai.click();
		Thread.sleep(1000);
				
		System.out.println(subChannel);
		
		// Choose SECONDARY for ZOTAPAY
		if (subChannel.contains("ZOTAPAY") ) {
			driver.findElement(
					By.xpath("//span[contains(translate(., 'Secondary', 'SECONDARY'), 'SECONDARY')]")).click(); 

			Thread.sleep(500);
		}

		// Select the account
		funcClickAccountList(Brand);

		// List all account and select one of which group is a TEST account
		// group;
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			currency = accountInfo.replaceAll("[^a-zA-Z]", "");

			// If group is a test group, deposit
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				depositEle.getAmount().sendKeys(depositAmount);
				
				depositEle.getImportantNotes().sendKeys("Test CP Vietnam " + subChannel + " Deposit " + depositAmount);

				// Verify Exchange Rate and VND amount
				BigDecimal vndAmount = funcVerifyExchangeRateAndAmount(Brand);
				
				// Submit
				funcClickSubmitButton();

				System.out.println("Made " + subChannel + " deposit of " + depositAmount + " to account " + mt4Account);

				// Allow some time to write records to DB
				Thread.sleep(2000);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				try {
					// Check 
				  switch (subChannel) {
					case "ZOTAPAY":
						// Ensure navigate to thaipaygo.com
						wait03.until(ExpectedConditions.urlContains("secure.clients.fund"));

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						Utils.funcIsStringContains(driver.findElement(By.xpath("//div[contains(text(),'VND')]")).getText(), vndAmount.toString(), Brand);
						break;

					case "XPAY":
						// Ensure navigate to thaipaygo.com
						wait03.until(ExpectedConditions.urlContains("transfer1515.com"));

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						Utils.funcIsStringContains(driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText(), vndAmount.toString(), Brand);
						break;

					default:
						System.out.println(subChannel + " is not supported!");

					}
				} catch (Exception e) {

					System.out.println("The current url is: " +
							driver.getCurrentUrl());
					
					// If submit failure happens
					if (driver.findElements(By.xpath("//div[label = 'Important notes']//input")).size()>0) {
						
						System.out.println(
								"\n!!!Submit failure or error occours! Please make sure correct Crypto info configured in disconf (biz_payment.properties).\n");
			
					//If redirect to 3rd party and there is an exception happening, abort the test case.
					}else {
						
						Assert.assertTrue(false,
							"\n!!!Something wrong. Please execute the case manually! \n");
						
				    }
				}
				// Verify transaction history
				Thread.sleep(500);
				driver.navigate().to(TraderURL);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				
				if (Brand.equalsIgnoreCase("vt")) {
					
					funcValidateTransacHistory(driver, mt4Account, "Internet banking (Vietnam)", depositAmount, Brand);					
				
				}else {
					
					funcValidateTransacHistory(driver, mt4Account, "Internet Banking (Vietnam)", depositAmount, Brand);
				}

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}
	
	// Added by Yanni: Navigate to CryptoCurrency page, switch to subChannel,
	// select Account, input amount(200.00) and notes, submit
	void funcCryptoDeposit(String TraderURL, String Brand, String TestEnv, String subChannel) throws Exception {

		String mt4Account = "";
		String currency = "";
	    cpDeposit depositEle = new cpDeposit(driver, Brand);
	    
		funcNavigate2DepositFund( TraderURL, Brand );
		Thread.sleep(1000);
		
		// Select Cryptocurrency Deposit
		if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			driver.findElement(By.xpath("//div[contains(text(),'CRYPTOCURRENCY')]")).click();
			
		}else if (Brand.equalsIgnoreCase("vt")) {
			driver.findElement(By.xpath("//span[contains(text(),'Cryptocurrency deposit')]")).click();
		
		} else {
			driver.findElement(By.xpath("//div[contains(text(),'Cryptocurrency')]")).click();
		}
		Thread.sleep(1000);
		System.out.println(subChannel);
		// Switch to subChannel: BITCOIN or TETHER. Default is BITCOIN. When
		// subChannel = TETHER, do switch
		if (subChannel.contains("OMNI")) {
			
			if(Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				
				driver.findElement(By.xpath("//div[@id='pane-3']//li[2]")).click();
				
			}else {
				driver.findElement(
					By.xpath("//span[contains(translate(., 'Tether(Omni)', 'TETHER(OMNI)'),'TETHER(OMNI)')]")).click();
			}
			
			if (Brand.equalsIgnoreCase("vt")) {
				wait01.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//p[contains(text(), 'How to fund your account with Tether(Omni)')]")));
			} else {
				wait01.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//p[contains(text(), '(OMNI) FORM')]")));
			}

		} else if (subChannel.contains("ERC20")) {

			if(Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				
				driver.findElement(By.xpath("//div[@id='pane-3']//li[3]")).click();
				
			}else {
				driver.findElement(
					By.xpath("//span[contains(translate(., 'Tether(ERC20)', 'TETHER(ERC20)'),'TETHER(ERC20)')]"))
					.click();
			}

			if (Brand.equalsIgnoreCase("vt")) {
				wait01.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//p[contains(text(), 'How to fund your account with Tether(ERC20)')]")));
			} else {
				wait01.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//p[contains(text(), '(ERC20) FORM')]")));
			}
		}else if (subChannel.contains("Bitcoin")) {

			if(Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
				
				driver.findElement(By.xpath("//div[@id='pane-3']//li[1]")).click();
				
			}
		}
		
		// Select the account
		funcClickAccountList(Brand);

		// List all account and select one of which group is a TEST account
		// group;
		List<WebElement> element = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		if (element.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any account!");
		}
		for (WebElement webElement : element) {
			String accountInfo = webElement.getText();
			mt4Account = accountInfo.substring(0, accountInfo.indexOf("-")).trim();
			currency = accountInfo.replaceAll("[^a-zA-Z]", "");

			// If group is a test group, deposit
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
				webElement.click();
				// After account is selected, currency will be loaded
				// automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);

				// Input deposit amount
				depositEle.getAmount().sendKeys(depositAmount);

				// Input Notes
				depositEle.getImportantNotes()
						.sendKeys("Test CP " + subChannel + " CryptoCurrency Deposit " + depositAmount);

				// Submit
				funcClickSubmitButton();

				if(!Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg")) {
					// Click Yes on pop up window
					funcClickYesButton();	
				}

				System.out.println("Made " + subChannel + " deposit of " + depositAmount + " to account " + mt4Account);

				// Allow some time to write records to DB
				Thread.sleep(2000);

				// Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);

				try {
					// Check BIB2INPAY page, get BTC/USDT amount, if it is usdt,
					// check RATE.
					switch (subChannel) {
					case "OMNI":
						// Ensure navigate to B2BINPAY URL:TETHER
						wait03.until(ExpectedConditions.urlContains("b2binpay.com"));

						// GET usdt amount
						String usdtAmount = driver.findElement(By.cssSelector("div.row > div:nth-of-type(1) >p"))
								.getText();

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						// Verify the total amount should be USDT amount
						Utils.funcIsStringContains(usdtAmount, "USDT", Brand);

						BigDecimal numOfUsdt = new BigDecimal(usdtAmount.replaceAll("[^.,0-9]", ""));

						System.out.println("Deposit amount: " + depositAmount + currency + ", usdt number:"
								+ numOfUsdt.toString());
						System.out.println("Please check rate is correct: rate = "
								+ numOfUsdt.divide(new BigDecimal(depositAmount)) + "\n");

						break;

					case "Bitcoin":
						// Ensure navigate to B2BINPAY URL:TETHER
						wait03.until(ExpectedConditions.urlContains("b2binpay.com"));

						// GET btc amount
						String btcAmount = driver.findElement(By.cssSelector("div.row >div:nth-of-type(1)>p"))
								.getText();

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						// Verify the total amount should be BTC amount
						Utils.funcIsStringContains(btcAmount, "BTC", Brand);
						System.out.println("btcAmount:" + btcAmount);
						System.out.println("numOfBTC:" + btcAmount.replaceAll("[^.,0-9]", ""));
						BigDecimal numOfBTC = new BigDecimal(btcAmount.replaceAll("[^.,0-9]", ""));

						System.out.println(
								"Deposit amount: " + depositAmount + currency + ", BTC AMOUNT:" + numOfBTC.toString() + "\n");
						break;

					case "ERC20":
						// Ensure navigate to B2BINPAY URL:TETHER
						wait03.until(ExpectedConditions.urlContains("eth.b2binpay.com"));

						// GET eth amount
						usdtAmount = driver.findElement(By.cssSelector("div.row > div:nth-of-type(1) >p")).getText();

						System.out.println("\n++++++++++ Going to verify 3rd party payment amount +++++++++++");
						// Verify the total amount should be ETH amount
						Utils.funcIsStringContains(usdtAmount, "ETH", Brand);

						numOfUsdt = new BigDecimal(usdtAmount.replaceAll("[^.,0-9]", ""));

						System.out.println("Deposit amount: " + depositAmount + currency + ", usdt number:"
								+ numOfUsdt.toString());
						System.out.println("Please check rate is correct: rate = "
								+ numOfUsdt.divide(new BigDecimal(depositAmount)) + "\n");

						break;
					default:
						System.out.println(subChannel + " is not supported!");

					}
				} catch (Exception e) {
					System.out.println(
							"\n!!!Submit failure or error occours! Please make sure correct Crypto info configured in disconf (biz_payment.properties).\n");

				}
				// Verify transaction history
				Thread.sleep(500);
				driver.navigate().to(TraderURL);
				funcNavigate2DepositHistory(Brand);
				Thread.sleep(1000);
				funcValidateTransacHistory(driver, mt4Account, subChannel, depositAmount, Brand);

				break;
			} else {
				System.out.println(
						"Not able to check the accout group or not test group! Not able to make deposit to these accounts");
			}

		}

	}

}
