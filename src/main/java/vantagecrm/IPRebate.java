package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
//import utils.Listeners.TestngListener;
import utils.ExtentReports.ExtentTestManager;
import java.lang.reflect.Method;
/*
 * This class is to test all withdraw methods in Trader
 */

public class IPRebate {

	public WebDriver driver;

	public WebDriver getDriver() {
		return driver;
	}

	String IpURL = "http://ib.vantagefx.com/?login_id=";
	String paymentHistoryURL = "http://ib.vantagefx.com/RebatePaymentHistory";
	String userID = "69127";

	WebDriverWait wait10;

	// different wait time among different environments. Test environment will use 1 while beta & production will use 2.
	// Initialized in LaunchBrowser function.
	int waitIndex = 1;

	Select t;  // Account Dropdown
	Random r = new Random();
	int j; // Select index

	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{

		// System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
		driver = Utils.funcSetupDriver(driver, "chrome", headless);

		utils.Listeners.TestListener.driver = driver;
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		context.setAttribute("driver", driver);

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
		}

		wait10 = new WebDriverWait(driver, Duration.ofSeconds(30));

	}

	@Test(priority = 0)
	@Parameters(value = { "TraderName", "TraderPass", "Brand", "TraderURL" })
	void IPLogIn(String TraderName, String TraderPass, String Brand, String TraderURL) throws Exception

	{
		// ExtentTestManager.startTest(method.getName(),"Description: Login IB Portal");
		// Login AU IB Portal
		driver.get(TraderURL);
		Utils.funcLogInIP(driver, TraderName, TraderPass, Brand);

		// wait10.until(ExpectedConditions.elementToBeClickable(By.linkText("APPLY FOR A REBATE")));

		wait10.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//a[contains(translate(.,'ply for a rebt','PLY FOR A REBT'), 'APPLY FOR A REBATE')]")));

	}

	@AfterClass(alwaysRun = true)
	void ExitBrowser() throws Exception {

		Thread.sleep(500);
		Utils.funcLogOutIP(driver);
		// Close all browsers
		driver.quit();
	}

	@Test(dependsOnMethods = "IPLogIn", alwaysRun = true)
	@Parameters(value = "Brand")
	// Apply for a rebate from Dashboard
	void ApplyForRebateFromDashboard(String Brand, Method method) throws Exception {
		//Boolean flag = false;
		String  moneyAmount, commission;

		/*
		 * ExtentTestManager.startTest(method.getName(),"Description: Test Case for ApplyForRebateFromDashboard");
		 * System.out.println("=======Test Case for ApplyForRebateFromDashboard========");
		 */

		// Select one Rebate account with balance greater than 0 and return the balance
		moneyAmount = switchToNonZeroBalanceRebateAccount(Brand);

		// Get the current rebate account number
		String accNumber = driver.findElement(By.cssSelector("span#showMessage")).getText();
		
		// Verify in payment history
		if (funcApply4Rebate(Brand,moneyAmount,accNumber))
		// if (true)//by Alex Liu
		{
			// driver.findElement(By.linkText("PAYMENT HISTORY")).click();
			// driver.findElement(By.xpath(".//span[text()='PAYMENT HISTORY']")).click();
			driver.findElement(By.xpath("//span[contains(translate(.,'Transaction history','TRANSACTION HISTORY'),'TRANSACTION HISTORY')]")).click();

			Thread.sleep(1000);

			// Switch to the Rebate Account that just applied for rebate
			switchToSpecificRebateAccount(accNumber);

			// Verify the total commission displayed is 0.00
			commission = getTotalCommission(Brand);
			Assert.assertEquals("0.00", commission);

			// Verify the rebate history
			verifyRebateHistory(moneyAmount);

		}
		// Return to home page as each case end
		// driver.findElement(By.xpath("//span[contains(translate(.,'Dashboard','DASHBOARD'),'DASHBOARD']")).click();
		driver.findElement(By.xpath(".//span[contains(translate(.,'ashbord','ASHBORD'),'DASHBOARD')]")).click();
	}

	@Test(dependsOnMethods = "IPLogIn", alwaysRun = true)
	@Parameters(value = "Brand")
	// Apply for a rebate from Payment History
	void ApplyForRebateFromPaymentHistory(String Brand, Method method) throws Exception {
		//Boolean flag = false;
		String moneyAmount, commission;

		// ExtentTestManager.startTest(method.getName(),"Description: Test Case for ApplyForRebateFromPaymentHistory");
		// System.out.println("=======Test Case for ApplyForRebateFromPaymentHistory========");

		// driver.findElement(By.linkText("PAYMENT HISTORY")).click();
		Thread.sleep(1000);
		
		wait10.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(translate(.,'Transaction history','TRANSACTION HISTORY'),'TRANSACTION HISTORY')]"))).click();
		//wait10.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(translate(.,'Payment history','PAYMENT HISTORY'),'PAYMENT HISTORY')]"))).click();
		
		// driver.findElement(By.xpath(".//span[text()='PAYMENT HISTORY']")).click();
		Thread.sleep(1000);

		// Select one Rebate account with balance greater than 0 and return the balance
		moneyAmount = switchToNonZeroBalanceRebateAccount(Brand);

		// Get the current rebate account number
		String accNumber = driver.findElement(By.cssSelector("span#showMessage")).getText();

		// Verify commission balance in Dashboard
		if (funcApply4Rebate(Brand,moneyAmount,accNumber))
		// if (true)//by Alex Liu
		{
			// Verify the rebate history
			verifyRebateHistory(moneyAmount);

			// Navigate to home page / DASHBOARD
			if (Brand.equalsIgnoreCase("vt")) {
				driver.findElement(By.cssSelector("div:nth-child(1) > div > .el-menu-item")).click();
			} else {
				driver.findElement(By.cssSelector("div:nth-child(1) > .el-menu-item")).click();
			}
			Thread.sleep(1000);

			// Wait until DASHBOARD appears
			wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.contact_title")));

			// Switch to the Rebate Account that just applied for rebate
			switchToSpecificRebateAccount(accNumber);

			// Verify the total commission displayed is 0.00
			commission = getTotalCommission(Brand);
			Assert.assertEquals("0.00", commission);

		}
		// Return to home page as each case end
		driver.findElement(By.xpath(".//span[contains(translate(.,'ashbord','ASHBORD'),'DASHBOARD')]")).click();
	}

	boolean funcApply4Rebate(String Brand, String moneyAmount, String accNumber) throws Exception {
		String cssSel, msg, commission, errText;
		boolean flag = false;

		if (moneyAmount.compareTo("0.00") != 0) {
			Thread.sleep(1000);
			System.out.println("Apply rebate for account " + accNumber);
			// wait10.until(ExpectedConditions.elementToBeClickable(By.linkText("APPLY FOR A REBATE"))).click();
			wait10.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(translate(.,'ply for a rebt','PLY FOR A REBT'), 'APPLY FOR A REBATE')]"))).click();

			Thread.sleep(1000);

			// Verify the pop up success message
			// msg = driver.findElement(By.cssSelector("div.el-dialog__body > p:nth-of-type(2)")).getText();

			cssSel = "div.el-dialog__body > p:nth-child(4)";
			if (Brand.equalsIgnoreCase("vt")) {
				cssSel = "div.dialog_body > p:nth-child(3)";
			}
			msg = driver.findElement(By.cssSelector(cssSel)).getText();

			System.out.println("Commission Apply Result:" + msg);
			Assert.assertTrue(msg.contains(moneyAmount + " will be transferred to the rebate Account " + accNumber));

			// Close the pop up window
			// cssSel="div.el-dialog__body > img.close";
			driver.findElement(By.cssSelector("img.close")).click();
			Thread.sleep(1000);

			// Wait until window disappears
			wait10.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.el-dialog__body")));

			flag = true;

			// Verify if the total amount turn to 0.00
			commission = getTotalCommission(Brand);
			Assert.assertEquals("0.00", commission);
		}

		if (flag == false) {
			System.out.println("Balance of all rebate accounts is 0.00. Can't perform APPLY REBATE option.");
		}

		// Expect error if click "APPLY FOR A REBATE" when 0 commission
		driver.findElement(By.xpath("//a[contains(translate(.,'Apply for a rebate','APPLY FOR A REBATE'), 'APPLY FOR A REBATE')]")).sendKeys(Keys.RETURN);
		Thread.sleep(1000);

		/*
		 * errText = driver.findElement(By.cssSelector("body > div.el-message.el-message--error > p")).getText();
		 * Assert.assertEquals(errText, "You can't ask for a commission");
		 * 
		 * //Wait until error msg disappears
		 * wait10.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > div.el-message.el-message--error > p")));
		 */

		// errText = driver.findElement(By.cssSelector("body > div.el-message.el-message--info > p")).getText();
		errText = driver.findElement(By.xpath("//p[@class='el-message__content']")).getText();
		Assert.assertEquals(errText, "You don't have any applicable rebates");
		wait10.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > div.el-message.el-message--info > p")));

		return flag;
	}

	@Test(dependsOnMethods = "IPLogIn", alwaysRun = true)
	// Transfer rebate from Dashboard
	void TransferRebateFromDashboard(Method method) throws Exception {

		ExtentTestManager.startTest(method.getName(), "Description: Test Case for TransferRebateFromDashboard");
		System.out.println("=======Test Case for TransferRebateFromDashboard========");

		Thread.sleep(1000);
		wait10.until(ExpectedConditions.elementToBeClickable(By.linkText("TRANSFER REBATE INTO YOUR TRADING ACCOUNT")))
				.click();
		Thread.sleep(1000);

		funcTransfer();
	}

	@Test(dependsOnMethods = "IPLogIn", alwaysRun = true)
	// Transfer rebate from Payment History
	void TransferRebateFromPaymentHistory(Method method) throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Test Case for TransferRebateFromPaymentHistory");
		System.out.println("=======Test Case for TransferRebateFromPaymentHistory========");

		// Click "PAYMENT HISTORY" from dashboard
		Thread.sleep(2000);
		wait10.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[text()='PAYMENT HISTORY']"))).click();
		Thread.sleep(2000);
		wait10.until(ExpectedConditions.elementToBeClickable(By.linkText("TRANSFER REBATE INTO YOUR TRADING ACCOUNT")))
				.click();
		Thread.sleep(1000);

		funcTransfer();
	}

	void verifyWithdrawHistory(BigDecimal moneyAmount) throws Exception {
		List<WebElement> rows;
		WebElement menuSelected;
		String amount, destination = null, status = null;
		BigDecimal formattedAmount = new BigDecimal("0.00");

		String todayDate = getCurrentDate();

		// Get webElement of the table
		menuSelected = driver.findElement(By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']"));

		WebElement tableBody = menuSelected.findElement(By.tagName("tbody"));
		rows = tableBody.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> tds = row.findElements(By.tagName("td"));
			if (tds.get(0).getText().equals(todayDate)) {
				amount = tds.get(1).getText();
				destination = tds.get(2).getText();
				status = tds.get(3).getText();

				// Remove the currency symbol
				amount = removeCurrency(amount);
				formattedAmount = new BigDecimal(amount.replace(",", ""));
				// Only need to get the top one record of today
				break;
			}
		}

		System.out.println("The actual amount is: " + formattedAmount + " and the expected is: " + moneyAmount);
		Assert.assertEquals(destination, "Transfer to trading accounts");
		System.out.println(
				"The actual destination is: " + destination + " and the expected is: Transfer to trading accounts");
		Assert.assertTrue(status.contains("Process"), "Transfer status should be Processed or Processing.");
		// Assert.assertEquals(status, "Processing");
		System.out.println("The actual status is: " + status + " and the expected is: Processing/Processed");

		Assert.assertEquals(formattedAmount, moneyAmount);

	}

	void verifyRebateHistory(String moneyAmount) throws Exception {
		List<WebElement> rows;
		WebElement menuSelected;
		BigDecimal formattedAmount = new BigDecimal("0.00");
		BigDecimal totalAmount = new BigDecimal("0.00");
		BigDecimal expectedAmount = new BigDecimal("0.00");

		// Expected commission amount
		expectedAmount = new BigDecimal(moneyAmount.replace(",", ""));

		String todayDate = getCurrentDate();
		menuSelected = driver.findElement(By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']"));

		WebElement tableBody = menuSelected.findElement(By.tagName("tbody"));
		rows = tableBody.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> tds = row.findElements(By.tagName("td"));
			if (tds.get(0).getText().equals(todayDate)) {
				String amount = tds.get(2).getText();

				// Remove the currency symbol
				amount = removeCurrency(amount);
				formattedAmount = new BigDecimal(amount.replace(",", ""));

				totalAmount = totalAmount.add(formattedAmount);
			}
		}
		System.out.println(
				"The sum of totalAmount is: " + totalAmount + " and the expected amount is: " + expectedAmount);
		Assert.assertEquals(totalAmount, expectedAmount);
	}

	// Click on each option in the dropdown list to find the account which has non-zero commission
	// Then return the commission
	String switchToNonZeroBalanceRebateAccount(String Brand) throws Exception {
		BigDecimal moneyAmount = new BigDecimal("0.00");
		List<WebElement> t, menuGroup;
		WebElement menuSelected;
		Boolean flag = true;
		String money = null;

		// Click the Rebate account list
		Thread.sleep(4000);
		wait10.until(
				ExpectedConditions.elementToBeClickable(By.cssSelector("div.calendar_content li:nth-of-type(1) input")))
				.click();
		// driver.findElement(By.cssSelector("div.calendar_content li:nth-of-type(1) input")).click();
		Thread.sleep(1500);

		// After user clicks the rebate account list, a new DIV will be added directly under the Body
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));

		// Choose a rebate account with balance greater than 0
		for (int i = 0; i < menuGroup.size(); i++) {
			// If the div is set to display
			if (!menuGroup.get(i).getAttribute("style").contains("display: none")) {
				menuSelected = menuGroup.get(i);

				t = menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));

				j = t.size() - 1;

				while (flag == true && j >= 0) {
					// Click on the account in the list one by one
					t.get(j).findElement(By.tagName("span")).click();

					money = getTotalCommission(Brand);

					// Remove the "," if there is any (e.g. 1,234.56)
					moneyAmount = new BigDecimal(money.replace(",", ""));
					// System.out.println("moneyAmount :" + moneyAmount);

					if (moneyAmount.compareTo(BigDecimal.ZERO) == 1) {
						flag = false; // This rebate account has rebate and can be used.
						break;
					} else {
						j--;
						// Click on the Rebate account list

						Thread.sleep(2000);
						driver.findElement(By.cssSelector("div.calendar_content li:nth-of-type(1) input")).click();
						Thread.sleep(500);
					}
				}
			}
		}
		return money;
	}

	// Search all the option in the dropdown list and switch to the specific account accoring to account number
	void switchToSpecificRebateAccount(String accNumber) throws Exception {
		List<WebElement> t, menuGroup;
		WebElement menuSelected;

		// Click the Rebate account list
		Utils.waitUntilLoaded(driver);
		driver.findElement(By.cssSelector("div.calendar_content li:nth-of-type(1) input")).click();
		Thread.sleep(1000);

		// After user clicks the rebate account list, a new DIV will be added directly under the Body
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));

		// Switch to specific rebate account
		for (int i = 0; i < menuGroup.size(); i++) {
			// If the div is set to display
			if (!menuGroup.get(i).getAttribute("style").contains("display: none")) {
				menuSelected = menuGroup.get(i);

				t = menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));

				j = t.size() - 1;
				while (j >= 0) {
					Thread.sleep(500);
					String number = t.get(j).findElement(By.tagName("span")).getText();

					if (number.equals(accNumber)) {
						// Click on the account
						t.get(j).findElement(By.tagName("span")).click();
						Thread.sleep(1000);
						break;
					} else {
						j--;
					}

				}
			}
		}
	}

	String getTotalCommission(String Brand) throws Exception {
		String moneyDisplay, money = null;
		String cssSel = "div.left.clearfix div.container_left_bottom > p";
		if (Brand.equalsIgnoreCase("vt")) {
			cssSel = "div.left.clearfix li.container_bottom div.container_bottom_left > p";
		}
		moneyDisplay = driver.findElement(By.cssSelector(cssSel)).getText();

		System.out.println("moneyDisplay is " + moneyDisplay);
		money = removeCurrency(moneyDisplay);
		return money;
	}

	String getAvailableBalance() throws Exception {
		String moneyDisplay, money = null;

		moneyDisplay = driver.findElement(By.cssSelector("div.right.clearfix div.container_left_bottom > p"))
				.getAttribute("title");
		money = removeCurrency(moneyDisplay);
		return money;
	}

	String getAccountBalance() throws Exception {
		String moneyDisplay, money = null;

		moneyDisplay = driver.findElement(By.cssSelector("div.left.clearfix div.container_left_bottom > p"))
				.getAttribute("title");
		money = removeCurrency(moneyDisplay);
		return money;
	}

	String removeCurrency(String moneyDisplay) throws Exception {
		String currency, money;
		// currency=driver.findElement(By.cssSelector("div.left.clearfix div.container_left_bottom > p > span")).getText();
		// money = moneyDisplay.replace(currency, "");
		money = moneyDisplay.replaceAll("[^-.,0-9]+", "");
		System.out.println(moneyDisplay + " remove currency symbol: " + money);
		return money;
	}

	String getCurrentDate() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateFormatted = dateFormat.format(new Date());
		System.out.println("dateFormatted: " + dateFormatted);
		return dateFormatted;
	}

	void verifyInputBoxErrMsg(WebDriver driver, String expectedErrMsg) throws Exception {
		driver.findElement(By.cssSelector("button.el-button.el-button--default")).click();

		Thread.sleep(500);
		String errText = driver.findElements(By.cssSelector("div.el-form-item__error")).get(0).getText();
		Assert.assertEquals(errText, expectedErrMsg);
		Thread.sleep(500);
	}

	void funcTransfer() throws Exception {

		Boolean flag = false;
		String accItem, errText, accNumber;
		String[] result;
		BigDecimal balance = new BigDecimal("0.00");
		BigDecimal balanceNew = new BigDecimal("0.00");

		// Click the Rebate account list
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("div.form_list_inner:nth-of-type(1) li:nth-of-type(1) input")).click();
		Thread.sleep(1000);

		// After user clicks the rebate account list, a new DIV will be added directly under the Body
		List<WebElement> menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));

		// Select one Rebate account with balance greater than 0 and return the balance
		accItem = Utils.chooseRebateAccount(driver, menuGroup);

		// Get the account number and account balance
		result = accItem.split(" ");
		accNumber = result[0];
		balance = new BigDecimal(result[2]).setScale(2);

		// Transfer money
		if (balance.compareTo(BigDecimal.ZERO) != 0) {
			// Check if there is available trading account
			// Click the trading account list
			driver.findElement(By.cssSelector("div.form_list_inner:nth-of-type(2) li:nth-of-type(1) input")).click();
			// After user clicks the rebate account list, a new DIV will be added directly under the Body
			if (driver.findElement(By.cssSelector("body>div.el-select-dropdown.el-popper")) == null) {
				Assert.assertFalse(true, "No trading account available!");
			}

			// Alex Liu - Should add new method of verify errText
			// Check error message if clicking "Submit" when no input of "Amount"
			verifyInputBoxErrMsg(driver, "It can't be 0");
			Thread.sleep(1000);

			// Check error message if clicking "Submit" when input "Amount" greater than balance
			balanceNew = balance.add(new BigDecimal("0.1"));
			driver.findElement(
					By.cssSelector("div.form_list_inner li:nth-of-type(2) div.el-input input.el-input__inner"))
					.sendKeys(balanceNew.toString());
			verifyInputBoxErrMsg(driver, "Your transfer amount is greater than the available balance");
			Thread.sleep(1000);

			// Transfer money that equals to account balance

			// balance = new BigDecimal("0.10"); ///Alex Liu - Bug here! can not transfer the whole amount!!! Let balance be 0.1
			driver.findElement(
					By.cssSelector("div.form_list_inner li:nth-of-type(2) div.el-input input.el-input__inner"))
					.sendKeys(Keys.CONTROL + "a");
			driver.findElement(
					By.cssSelector("div.form_list_inner li:nth-of-type(2) div.el-input input.el-input__inner"))
					.sendKeys(balance.toString());
			driver.findElement(By.cssSelector("button.el-button.el-button--default")).click();
			Thread.sleep(500);

			// Yanni on 18/09/2019: need to check whether there are Trading Accounts listed.

			// Verify the success message
			errText = wait10
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='el-message__content']")))
					.getText();
			// errText = driver.findElement(By.xpath("//p[@class='el-message__content']")).getText();
			Assert.assertEquals(errText, "your rebate transfer has been submitted");

			Thread.sleep(2000);

			// Mark flag for checking transfer history
			flag = true;

			// Click "PAYMENT HISTORY" from dashboard
			wait10.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[text()='PAYMENT HISTORY']")))
					.click();
			Thread.sleep(1000);
		} else {
			Assert.assertFalse(true, "All the rebate accounts are 0 balance!");
		}

		// Verify in payment history
		if (flag == true)
		// if (true)//by Alex Liu test
		{
			// Switch to the Rebate Account that just applied for rebate
			Thread.sleep(1000);
			switchToSpecificRebateAccount(accNumber);

			// Navigate to WITHDRAW HISTORY
			driver.findElement(By.xpath("//li[contains(text(),'WITHDRAW HISTORY')]")).click();
			Thread.sleep(1000);

			// Verify the withdraw history
			verifyWithdrawHistory(balance);

		}
		// Return to home page as each case end
		driver.findElement(By.xpath(".//span[text()='DASHBOARD']")).click();
	}
}
