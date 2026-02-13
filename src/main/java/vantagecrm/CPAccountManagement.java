package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.StaleElementReferenceException;
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
import org.testng.annotations.*;

import clientBase.addiAccount;
import clientBase.cpHeader;
import clientBase.liveAcctHome;
import clientBase.navigationBar;
import clientBase.transferPage;
import utils.ExtentReports.ExtentTestManager;
import java.lang.reflect.Method;

/*
 * This class is used to request additional accounts and transfer money between additional accounts
 */

public class CPAccountManagement {

	WebDriver driver;

	WebDriverWait wait01;
	WebDriverWait wait02;
	int waitIndex = 1;
	Select t;
	
	navigationBar navCls;
	liveAcctHome actHomeCls;

	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{

		//System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		context.setAttribute("driver", driver);

		//utils.Listeners.TestListener.driver = driver;

		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.NONE);

		options.addArguments("start-maximized");
		options.addArguments("enable-automation");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
		}

		wait01 = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait02 = new WebDriverWait(driver, Duration.ofSeconds(20));

	}

	@Test(priority = 0)
	@Parameters(value = { "TraderURL", "TraderName", "TraderPass", "Brand" })
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand, Method method) throws Exception

	{
		navCls = new navigationBar(driver,Brand);
		actHomeCls = new liveAcctHome(driver,Brand);
		
		// Login CP
		driver.get(TraderURL);

		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		Thread.sleep(waitIndex * 2000);

		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

	}

	@Test(dependsOnMethods = "CPLogIn", invocationCount = 1, alwaysRun = true)
	@Parameters(value = { "TraderURL", "Brand" })
	void RequestAddiAccount(String TraderURL, String Brand, Method method) throws Exception {
		/*
		 * ExtentTestManager.startTest(method.getName(),
		 * "Description: Request 6 Additional Accounts with different account type/platform/currency");
		 */
		String accType[] = { "Standard STP", "Raw ECN", "Islamic STP", "Islamic ECN" };
		String accTypePug[] = { "Standard", "Prime","Islamic Standard","Islamic Prime" };
		String accCurrency[] = { "USD", "GBP", "CAD", "AUD", "EUR", "SGD","NZD","HKD","JPY"};
		
		String platform[] = { "mt4", "mt5" };
			
		switch (Brand) {
		case "fsa":
		case "svg":
		case "vt":

			//Click left navigation bar -> Account
			wait02.until(ExpectedConditions.elementToBeClickable(navCls.getAccountEle())).click();
			Thread.sleep(1000);
			for (int i = 0; i < accTypePug.length; i++) {
			
				funcOpenAdditionalAccount("mt4", accTypePug[i], "USD", Brand);
				System.out.println("===Created account with===: mt4 " + accTypePug[i] + " USD");
				funcVerifyAdditionalAccount(Brand, "mt4", accTypePug[i], "USD", "Processing");
				 } 
			break;
		case "ky":
		case "au":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":

		default:
			 for (int i=0; i<accCurrency.length; i++) {
			//for (int i = 0; i < 1; i++) {
				int j = i, k = i;
				if (j >= accType.length)
					j = 0;
				if (k >= platform.length)
					k = 0;
				funcOpenAdditionalAccount(platform[k], accType[j], accCurrency[i], Brand);
				System.out.println("===Created account with==: " + platform[k] + " " + accType[j] + " " + accCurrency[i]);
				
				//After additional account request is submitted, System goes to Home Page. The function below verifies newly created account
				Thread.sleep(1000);
				funcVerifyAdditionalAccount(Brand, platform[k], accType[j], accCurrency[i], "Processing");
			}
			break;
		}
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "TraderURL", "Brand", "TestEnv" })
	void TransferFund(String TraderURL, String Brand, String TestEnv) throws Exception {

		BigDecimal balance = new BigDecimal("0.00");
		String toAccount, currency = "";
		String fromAccount = "", amount = "";
		navigationBar navCls = new navigationBar(driver, Brand);
		transferPage transCls = new transferPage(driver,Brand);

		driver.navigate().to(TraderURL);
		Thread.sleep(1000);

		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

		
		Thread.sleep(1000);
		navCls.getFundsEle().click();
		Thread.sleep(1000);
		navCls.getTransferMenuEle().click();
		Thread.sleep(1000);
		if (!TestEnv.equals("test")) {
			Thread.sleep(1000);
		}

		// Click the from account
		transCls.getFromAcctEle().click();			
		Thread.sleep(500);

		//Get all From Accounts
		List<WebElement> fromElement = null;
		fromElement = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

		if (fromElement.size() == 0) {
			Assert.assertTrue(false, "This user doesn't have any from account!");
		}

		//Loop each account. Break the loop when finding an account with balance >0
		for (WebElement webElement : fromElement) {
			String accountInfo = webElement.getText();
			fromAccount = accountInfo.substring(0, accountInfo.indexOf("-") - 1).trim();
			currency = accountInfo.substring(accountInfo.length() - 3, accountInfo.length()).trim();
			balance = funcCPGetBalance(accountInfo);
			// Choose the account that in test group and has non-zero balance
			if (Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && balance.compareTo(BigDecimal.ZERO) > 0) {
				// click on the from account
				webElement.findElement(By.tagName("span")).click();

				// Check whether the account is in HOLD position or not
				try {
					transCls.getOpenPosConfirmBtn().click();
					System.out.println(fromAccount + " is in HOLD position.");

				} catch (NoSuchElementException | StaleElementReferenceException e) {
					System.out.println(fromAccount + " is not on hold position");
				}

				// Select the to account
				transCls.getToAcctEle().click();
				Thread.sleep(500);

				List<WebElement> toElement = null;
				toElement =Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");

				if (toElement.size() == 0) {
					Assert.assertTrue(false, "This user doesn't have any to account!");
				}

				// Click the first toAccount
				toAccount = toElement.get(0).getText().substring(0, accountInfo.indexOf("-")).trim();
				toElement.get(0).click();
				Thread.sleep(500);

				if (Utils.funcCheckAccoutGroup(Brand, TestEnv, toAccount)) {
					// Input transfer amount
					amount = balance.multiply(new BigDecimal("0.2")).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					System.out.println("Transfer amount: " + amount);
					
					transCls.getAmountEle().sendKeys(amount);					

					// submit
					transCls.getSubmitBtn().click();
					Thread.sleep(1000);

					// Back to Home Page button
					wait01.until(ExpectedConditions.elementToBeClickable(transCls.getBack2HomeBtn())).click();

					// Verify the transaction history
					Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);
					Thread.sleep(2000);
					
					wait01.until(ExpectedConditions.visibilityOf(navCls.getTrasactionHistoryMenuEle())).click();
					Thread.sleep(1500);

					//Transfer History tab & Transaction History tab is merged into one. 
					// So after we enter Transaction History, needs to clieck "Transfer" tab				
					driver.findElement(By.xpath(".//li[contains(translate(text(),'Transfers','TRANSFERS'),'TRANSFERS')]")).click();
					Thread.sleep(500);
									
					funcValidateTransferHistory(driver, fromAccount, toAccount, amount, currency, Brand);
					System.out.println("Made account transfer of " + amount + " from account " + fromAccount
							+ " to account " + toAccount);
					break;
				} else {
					System.out.println("ToAccount is not in test group!");
				}

			} else {
				System.out.println(
						"Not able to check the accout group or not test group or zero balance! Not able to make fund transfer!");
			}

		}

	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "Brand", "TestEnv", "TraderURL", "AdminURL", "AdminName", "AdminPass" })
	void ChangeCPMT4PWD(String Brand, String TestEnv, String TraderURL, String AdminURL, String AdminName,
			String AdminPass) throws Exception {
		
		String mtPlatform = "mt4";
		String acctStatus = "active";
		
		funcChangeMTXPWDE2E(Brand, TestEnv, TraderURL, AdminURL, AdminName,	AdminPass, mtPlatform,acctStatus);
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "Brand", "TestEnv", "TraderURL", "AdminURL", "AdminName", "AdminPass" })
	void ChangeCPMT5PWD(String Brand, String TestEnv, String TraderURL, String AdminURL, String AdminName,
			String AdminPass) throws Exception {
		
		String mtPlatform = "mt5";
		String acctStatus = "active";
		
		funcChangeMTXPWDE2E(Brand, TestEnv, TraderURL, AdminURL, AdminName,	AdminPass, mtPlatform,acctStatus);

	}
	
	//The whole flow of Change Password: choose an active account, click the Change Password icon, then input new password.
	//After changing is done, revert password to the old one.
	void funcChangeMTXPWDE2E(String Brand, String TestEnv, String TraderURL, String AdminURL, String AdminName,
			String AdminPass, String mtPlatform, String acctStatus) throws Exception {
		String OldPwd = "12345678Qi", NewPwd = "123456789Qi";
		String mt4Account, url, cookie="";

		//Selected Account List
		List<WebElement> menuSelected = null;
		
		//Change Password icon
		WebElement changePwdIcon;
		
		WebDriver driver2;
		// List<WebElement> rows,tds;

		driver.navigate().to(TraderURL);

		// Wait until LIVE ACCOUNTS lable is displayed.
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

		Thread.sleep(2000);
		// Find for 1st active account for password change. If no, print a message.
		try {
			
			//Get All active MT4 accounts
			menuSelected = actHomeCls.getMTActList(mtPlatform, acctStatus);
						
			//Get the Change Password ICON of 1st row
			 changePwdIcon = actHomeCls.getPwdIcon(menuSelected.get(0));
	
			System.out.println(menuSelected.size());
			// Click the first active account
			// ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", menuSelected.get(0));
			Thread.sleep(3000);
			wait02.until(ExpectedConditions.elementToBeClickable(changePwdIcon)).click();

		} catch (NoSuchElementException e) {
			System.out.println("No Active MT4 Account(s) for password change.");
		}

		Thread.sleep(1000);

		// Wait for the pop up window for password operation
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='el-dialog__body']")));

		// Get the account number
		mt4Account = driver.findElement(By.xpath("//span[@class='number']")).getText();

		// Open new window and get admin cookie
		url = Utils.ParseInputURL(AdminURL);

		
		  switch (TestEnv) {
		  case "test":
			  //Temporary comment the line below due to new entry parameter introduced.
			  //cookie = RestAPI.testPostForAdminCookie(url, AdminName, AdminPass, Brand, TestEnv);
			  break;
		  default:
			  ChromeOptions options = new ChromeOptions();
			  options.addArguments("--incognito");
			 // WebDriverManager.chromedriver().setup();
			  driver2 = new ChromeDriver(options);
			  driver2.get(AdminURL);
			  cookie = Utils.getAdminCookie(driver2, AdminName, AdminPass, Brand, TestEnv);
			  driver2.close();
			  Thread.sleep(500);
			  break;
		  }
		 

		
		  // Reset mtx password before making change
		  if (Utils.funcCheckAccoutGroup(Brand, TestEnv, mt4Account)) {
		  RestAPI.testPostAdminResetPasswordSubmit(url, cookie, mt4Account, Brand);
		  }
		 

		// Change mt4 password
		  funcChangeMTXPWDWindow(OldPwd, NewPwd, Brand);

		// Going to reverse the password change
		driver.navigate().to(TraderURL);

		// Wait until LIVE ACCOUNTS lable is displayed.
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

		// Click the first active account
		actHomeCls.getPwdIcon(actHomeCls.getMTActList(mtPlatform, acctStatus).get(0)).click();

		// Change back mt4 password
		funcChangeMTXPWDWindow(NewPwd, OldPwd, Brand);

	}

	//Specific for Password change window: input old pwd/new pwd and click Submit
	void funcChangeMTXPWDWindow(String OldPwd, String NewPwd, String Brand) throws Exception {

		// Click Change password button
			
		switch(Brand)
		{
		case "fsa":
		case "svg":
			driver.findElement(By.xpath("//div[@class='btn_box']//span[contains(text(),'CHANGE PASSWORD')]")).click();
			break;
			
			default:
				driver.findElement(By.xpath("//span[contains(translate(text(),'Change password','CHANGE PASSWORD'),'CHANGE PASSWORD')]")).click();
		}
		
		Thread.sleep(1000);

		// Input oldpwd, newpwd
		String message = "";
		funcChangePassword(OldPwd, NewPwd, Brand);
		
		//Get success message
		switch(Brand)
		{
		case "fsa":
		case "svg":
			Thread.sleep(500);
			message = wait01
			.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@class='el-dialog__body']/div[@class='dialog_body']/p"))).getText();
			break;
		case "vt":
			message = wait01
			.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".el-dialog__body > p")))
			.getText();
			break;
		//liufeng 24.06.2021 for vfsc2
		case "vfsc2":
			message = wait01
			.until(ExpectedConditions
					.visibilityOfElementLocated(By.cssSelector("div.el-dialog__body div.dialog_body>p")))
			.getText();
			break;
			default:
				message = wait01
				.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("div.changePwd_confirm_dialog div.dialog_body")))
				.getText();
		}
	
		//Assert.assertTrue(message.contains("Your password has been updated"));
		//liufeng 24.06.2021
		Assert.assertTrue(message.contains("Your password has been updated"));

		// Close the dialog
		switch(Brand)
		{
			case "fsa":
			case "svg":
				driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
				
				break;
			case "vt":
				driver.findElement(By.cssSelector(".home_common_dialog:nth-child(7) .closeImg")).click();
				break;
			//for vfsc2 liufeng 24.06.2021
			case "vfsc2":
				driver.findElement(By.xpath("//*[@id='home']/div[7]/div/div/div/div[1]/div/img")).click();
				break;
				default:
					driver.findElement(By.cssSelector("div.changePwd_confirm_dialog div.el-dialog__header img:nth-child(1)")).click();
		}
	

			
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "Brand","TestEnv" })
	void ResetCPMTXPwd(String Brand, String TestEnv,Method method) throws Exception {
		Thread.sleep(2000);
		ExtentTestManager.startTest(method.getName(), "Description: Reset password");

		String handle, result,url,account = "",server;
		WebElement rowSelected;
		JSONObject obj = null;
		
		
		int platformNums = 2;
		String platformValue = "";
		String message="";

		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

		//Create a LiveAccount Home Page instance
		liveAcctHome acctHomeCls = new liveAcctHome(driver, Brand);
		
		//To reset password for both MT4 & MT5 active accounts
		for(int i=0; i<platformNums; i++)
		{
			
			//Decide which platform is going to be used
			if(i==0)
			{
				platformValue = "mt4";
			}else
			{
				platformValue = "mt5";
			}
			
			//To get 1st of active accounts in the specified platform
			rowSelected = acctHomeCls.getMTActList(platformValue, "Active").get(0);
					
			System.out.println("Going to reset active account: "+ platformValue + " " + acctHomeCls.getAcctNoTxt(rowSelected));
			
			//Click Password icon
			acctHomeCls.getPwdIcon(rowSelected).click();
			Thread.sleep(1000);
	
			// In the popup dialog, click Forgot password button
			driver.findElement(By.xpath("//span[contains(translate(text(),'Forgot password','FORGOT PASSWORD'),'FORGOT PASSWORD')]")).click();
			Thread.sleep(1000);
	
			//Get the success message
			switch(Brand.toLowerCase())
			{
				case "fsa":
				case "svg":
					message = wait01.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//p[@class='first_info']"))).getText();
					break;
				//for vfsc2 liufeng 24.06.2021
				case "vfsc2":
					
					message= wait01.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[@class='dialog_body']/div/p[@class='first_info']"))).getText();
					break;
					default:
						message = wait01.until(ExpectedConditions.visibilityOfElementLocated(
								By.xpath("//div[@class='forgotPwd_dialog']//div[@class='dialog_body']"))).getText();
			}

			//Assert message content
			System.out.println("Result: " + message);
			System.out.println();
			Assert.assertTrue(message
					.contains("We have just sent you an email with instructions to reset your password."));
		
			Thread.sleep(1000);
			
			// Close the dialog
			switch(Brand.toLowerCase())
			{
				case "fsa":
				case "svg":
					driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
					break;
				//for vfsc2 liufeng 24.06.2021
				case "vfsc2":
					driver.findElement(By.xpath("//*[@id=\"home\"]/div[8]/div/div/div/div[1]/div/img"))
					.click();
					break;
					default:
						driver.findElement(By.cssSelector("div.forgotPwd_dialog div.el-dialog__header img:nth-child(1)"))
						.click();
			}
			
			//Get current window handler
			handle=driver.getWindowHandle();
			
			//Search tb_mail_send_log to get the password reset link
			result = funcQueryMailDB(account, Brand, TestEnv);	
			//use json
			System.out.println("result: " + result);
			JSONParser parser = new JSONParser();
			obj = (JSONObject) parser.parse(result.substring(result.indexOf(':')+1, result.length()-1));
			//obj = new JSONObject(result);
			//url = (String) obj.get("CHANGE_LINK");
			//System.out.println("link: " + url);
			
			//result已经转成了JSON，不需要再对result处理 liufeng vfsc2
			//remove the curly braces
			//result = result.trim().substring(7, result.length()-2); 
			//String[] sepa=result.split(",");
			
			//get mt4 server name
			//server = sepa[1].substring(10, sepa[1].length()-1);
			//use json, liufeng 
			server = (String) obj.get("SERVER");
			System.out.println("server:"+server);
			
			//Verify server qualified for regulation standard
			switch(Brand.toLowerCase())
			{
				case "ky":
				case "vfsc":
				case "vfsc2":
				case "regulator2":
					Utils.funcIsStringContains(server, "VantageFXInternational-", Brand);
					break;
				case "au":
					Utils.funcIsStringContains(server, "VantageFX-", Brand);
					break;
				case "fca":
					Utils.funcIsStringContains(server, "VantageGlobalPrime-", Brand);
					break;
				case "vt":
					Utils.funcIsStringContains(server, "VT", Brand);
					break;
				case "fsa":
					Utils.funcIsStringContains(server, "PacificUnionInt", Brand);
					break;
				case "svg":
					Utils.funcIsStringContains(server, "PacificUnionLLC", Brand);
					break;
					
				default:
					System.out.println(Brand + " is not supported.");
				
			}
			
			//Get reset link
			//url = sepa[6].substring(15, sepa[6].length()-1);
			//replace "\u003d" with "="
			//url= url.replace("\\u003d", "=");
						
			//liufeng
			url = (String) obj.get("CHANGE_LINK");
			System.out.println("Reset URL in Email:"+url);
			
			//Open the reset pw link in new browser and reset passwd
			funcInputResetPassword(url, "12345678Ab",Brand);
			
		
		}		
	
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "OldPwd", "NewPwd", "AdminURL", "AdminName", "AdminPass", "Brand", "TraderName", "TestEnv" })
	void ChangeAccountPWD(@Optional("123Qwe") String OldPwd, @Optional("123Qweabc") String NewPwd, String AdminURL,
			String AdminName, String AdminPass, String Brand, String TraderName, String TestEnv, Method method)
			throws Exception {
		WebDriver driver2;
		String url;
		ExtentTestManager.startTest(method.getName(),
				"Description: Change account password to new and then rollback to old one");
		
		transferPage transferCls = new transferPage(driver,Brand);  //will use the BacktoHomePage function only
		
		driver.navigate().refresh();

		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

		Thread.sleep(1000);

		switch(Brand)
		{
			case "fsa":
			case "svg":
				//PUG CHANGE PASSWORD
				cpHeader cheadCls = new cpHeader(driver, Brand);
				cheadCls.getCustNameEle().click();
				Thread.sleep(1000);
				cheadCls.getChgPwdEle().click();
				break;
				
				default:
					navCls.getProfileEle().click();
					Thread.sleep(1000);
					navCls.getChangePWDEle().click();
		}

		Thread.sleep(1000);
		funcChangePassword(OldPwd, NewPwd, Brand);

		String message;
		if(Brand.equalsIgnoreCase("vt"))
		{
			message= wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='success_content']//p"))).getText();
		}else
		{
			message= wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='success_info']"))).getText();
		}

		Assert.assertTrue(message.contains("Your password has been updated"));

		Thread.sleep(500);
		
		transferCls.getBack2HomeBtn().click();

		// Code below is for changing the password back using API
		url = Utils.ParseInputURL(AdminURL);

		// Get admin cookie thru API in test env or browser in beta/prod env
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		//WebDriverManager.chromedriver().setup();
		driver2 = new ChromeDriver(options);

		Utils.funcResetUserLoginPW(driver2, AdminURL, TraderName, AdminName, AdminPass, Brand, TestEnv);
		
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "TraderURL" })
	void passQuestionaire(String TraderURL) throws Exception {
		driver.navigate().to(TraderURL);
		try {
			wait02.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[contains(text(),'ASIC Questionnaire Incomplete')]")));

			// Pass the questionarie
			Assert.assertTrue(funcQuestion(true), "Answer Questionaire has something wrong.");

		} catch (TimeoutException e) {
			System.out.println("No Questionaire link is available.");
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "TraderURL" })
	void failQuestionaire(String TraderURL) throws Exception {
		driver.navigate().to(TraderURL);
		try {
			wait02.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[contains(text(),'ASIC Questionnaire Incomplete')]")));

			// Fail the questionarie
			Assert.assertTrue(funcQuestion(false), "Answer Questionaire has something wrong.");

		} catch (TimeoutException e) {
			System.out.println("No Questionaire link is available.");
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "CPLogIn")
	@Parameters(value = { "TraderURL", "Brand" })
	void CPSideBarNavigation(String TraderURL, String Brand) throws Exception {

		navigationBar navCls = new navigationBar(driver, Brand);
		cpHeader headCls = new cpHeader(driver, Brand);

		driver.navigate().to(TraderURL);

		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);

		// Verify the sidebar should exist
		//ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='el-menu-vertical-demo el-menu']"));
		ExpectedConditions.visibilityOf(headCls.getHideSideBarBtn());
		Thread.sleep(1000);

		// Click to fold the sidebar away and expand again
		if (!Brand.equalsIgnoreCase("vt"))
		{
			headCls.getHideSideBarBtn().click();
			headCls.getShowSideBarBtn().click();
		}

		Thread.sleep(1000);
		// Verify the sidebar should not exist (fold away)
		ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ul[@class='el-menu-vertical-demo el-menu']"));
		driver.findElement(By.cssSelector("div:nth-child(1) ul div:nth-child(1) li > div"));
		System.out.println("Passed");

		// Click and verify the Home
		
	  funcSideBarVerification(navCls.getHomeEle(), null, "home");
	  
	  // Click and verify the LIVE ACCOUNTS
	  funcSideBarVerification(navCls.getAccountEle(), navCls.getLiveAccountEle(), "liveAccount");
	  
	  // Click and verify the OPEN ADDITIONAL ACCOUNTS
	  funcSideBarVerification(navCls.getAccountEle(), navCls.getAddiAccountEle(), "openLiveAccount");
	  
	  // Click and verify the DEPOSIT FUNDS
	  funcSideBarVerification(navCls.getFundsEle(), navCls.getDepositMenuEle(), "depositFunds");
	  
	  // Click and verify the WITHDRAW FUNDS
	  funcSideBarVerification(navCls.getFundsEle(), navCls.getWithdrawMenuEle(), "withdrawFunds");
	  
	  // Click and verify the TRANSFER BETWEEN ACCOUNTS
	  funcSideBarVerification(navCls.getFundsEle(), navCls.getTransferMenuEle(), "transferFunds");
	  
	  // Click and verify the PAYMENT DETAILS
	  //vfsc2 CP does not have "paymentDetails" due to 25.06.2021
	  if(!"vfsc2".equals(Brand)) {
		  funcSideBarVerification(navCls.getFundsEle(), navCls.getPayDetailMenuEle(), "paymentDetails");
	  }
	  
	  // Click and verify the TRANSACTION HISTORY
	  funcSideBarVerification(navCls.getFundsEle(), navCls.getTrasactionHistoryMenuEle(), "transactionHistory");
		 
		Thread.sleep(500);

		// Click and verify the DOWNLOADS
		funcSideBarVerification(navCls.getDownloadsEle(), null, "downloads");

		switch(Brand)
		{
			case "fsa":
			case "svg":
				// Click and verify the MY PROFILE
				funcSideBarVerification(headCls.getCustNameEle(), headCls.getMyProfileEle(), "myProfile");
				
				//Yanni on 10/03/2021: must have this line otherwise the next one will fail.
				driver.navigate().refresh();
				
				// Click and verify the CHANGE CLINET PORTAL PASSWORD
				funcSideBarVerification(headCls.getCustNameEle(), headCls.getChgPwdEle(), "changePassword");
				break;
			
			default:
				// Click and verify the MY PROFILE
				funcSideBarVerification(navCls.getProfileEle(), navCls.getMyProfileEle(), "myProfile");

				// Click and verify the CHANGE CLINET PORTAL PASSWORD
				funcSideBarVerification(navCls.getProfileEle(), navCls.getChangePWDEle(), "changePassword");
				
		}


		// Click and verify the CONTACT US
		funcSideBarVerification(navCls.getContactUS(), null, "contactUs");		
	
	}

	@AfterClass(alwaysRun = true)
	@Parameters(value = { "Brand" })
	void ExitBrowser(String Brand) throws Exception {
		// driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}

	void funcVerifyAdditionalAccount(String Brand, String platform, String accType, String accCurrency, String status)
			throws Exception {

		liveAcctHome acctHomeCls = new liveAcctHome(driver, Brand);
		
		WebElement row = acctHomeCls.getMTActList(platform,status).get(0);
		
		/* List<WebElement> tds = row.findElements(By.tagName("td")); */
		System.out.println("The actual account type is: " + acctHomeCls.getAcctTypeTxt(row) + " and the expected is: " + accType);
		Assert.assertEquals(acctHomeCls.getAcctTypeTxt(row), accType);
		
		System.out.println("The actual currency is: " + acctHomeCls.getCurrencyTxt(row) + " and the expected is: " + accCurrency);
		Assert.assertEquals(acctHomeCls.getCurrencyTxt(row), accCurrency);
		
		System.out.println("The actual status is: " + acctHomeCls.getStatusTxt(row) + " and the expected is: " + status);
		Assert.assertEquals(acctHomeCls.getStatusTxt(row), status);
	}

	void funcOpenAdditionalAccount(String platform, String accType, String accCurrency, String Brand) throws Exception {
		
		addiAccount addiCls = new addiAccount(driver, Brand);
		WebElement temp = null;
		System.out.println("===Entering OpenAdditionalAccount method.===");

		Thread.sleep(2000);
		//liufeng 
		wait02.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[contains(text(),'OPEN ADDITIONAL ACCOUNTS')]")))).click();
		//wait02.until(ExpectedConditions.elementToBeClickable(navCls.getAddiAccountEle())).click();

		Thread.sleep(1000);
		
		//Choose platform
		switch (platform) {
			case "mt5": 
				addiCls.getMT5Plat().click(); // select CP platform MT5.
				break;
			
			case "mt4":
			default: 
				addiCls.getMT4Plat().click();
		}

		//Choose Account Type
		switch (accType) 
		{
			case "Raw ECN":  //VT AU account Type
			case "Prime":   //PUG Account Type
				temp = addiCls.getAccountType(1);
				break;
		

			case "Islamic STP": 
			case "Islamic Standard":  //PUG
				temp =addiCls.getAccountType(2);
				break;		

			case "Islamic ECN": 
			case "Islamic Prime":
				temp =addiCls.getAccountType(3);
				break;
		
			case "Standard STP":
			case "Standard":   //PUG
			default: 
				temp =addiCls.getAccountType(0);
		}

		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", temp);
		temp.click();
		
		//Choose currency
		switch (accCurrency) 
		{
			case "USD": 
				temp=addiCls.getCurrency(0);
				break;		

			case "GBP": 
				if(Brand.equalsIgnoreCase("vt"))
				{
					temp =addiCls.getCurrency(2);
				}else
				{
					temp =addiCls.getCurrency(1);
				}
				break;
		

			case "CAD": 
				if(Brand.equalsIgnoreCase("vt"))
				{
					temp =addiCls.getCurrency(3);
				}else
				{
					temp =addiCls.getCurrency(2);
				}
				break;
	
			case "AUD": 
				if(Brand.equalsIgnoreCase("vt"))
				{
					temp =addiCls.getCurrency(1);
				}else
				{
					temp =addiCls.getCurrency(3);
				}
				break;
			
			case "EUR": 
				temp =addiCls.getCurrency(4);
				break;			
	
			case "SGD": 
				temp =addiCls.getCurrency(5);
				break;
				
			case "NZD": 
				temp =addiCls.getCurrency(6);
				break;
				
			case "HKD": 
				temp =addiCls.getCurrency(7);
				break;
				
			case "JPY": 
				temp =addiCls.getCurrency(8);
				break;
				
				default:
					
			
		}
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", temp);
		temp.click();
		Thread.sleep(200);
		// Agree the terms and conditions
		addiCls.getAgreeCheckbox().click();

		// Submit
		addiCls.getBtnSubmit().click();
		Thread.sleep(2000);
		
		//Back to Home Page
		addiCls.getBack2HomePageBtn().click();

		Thread.sleep(2000);

	}

	void funcChangePassword(String OldPwd, String NewPwd, String Brand) throws Exception {
		
		String accountNumber;
		
		try {
	
			accountNumber = driver.findElement(By.cssSelector("div.dialog_body div.top span.number")).getText();
			System.out.println(
					"Changing Password for Account: " + accountNumber + " OldPwd: " + OldPwd + " NewPwd: " + NewPwd);
		} catch (NoSuchElementException e) {
			System.out.println("Going to change password from OldPwd: " + OldPwd + " to NewPwd: " + NewPwd);
		}

		driver.findElements(By.xpath("//div[2]//div[1]//div[1]//input[1]")).get(0).sendKeys(OldPwd);
		Thread.sleep(1000);
		driver.findElements(By.xpath("//div[2]//div[1]//div[1]//input[1]")).get(1).sendKeys(NewPwd);
		Thread.sleep(1000);
		driver.findElements(By.xpath("//div[2]//div[1]//div[1]//input[1]")).get(2).sendKeys(NewPwd);
		Thread.sleep(1000);

		//Click Submit button
		switch(Brand.toLowerCase())
		{
			case "vt":
				driver.findElement(By.cssSelector("div.change_form button.el-button.blue_button.el-button--default")).click();
				break;
				
			case "fsa":
			case "svg":
				driver.findElement(By.xpath(".//button[@class='el-button btn_submit el-button--default']")).click();
				break;
				
			default:
				driver.findElement(By.cssSelector("button.el-button.btn_blue.el-button--primary")).click(); 
				
		}
	
		Thread.sleep(500);
	}

	/*
	 * Choose the account with non-zero balance and click on it
	 */
	public static boolean funcChooseFromAccount(WebDriver driver, List<WebElement> menuGroup) throws Exception {
		BigDecimal moneyAmount = new BigDecimal("0.00");
		Boolean flag = false;
		int j; // Select index
		String option = "", amount = "";
		String[] result;

		System.out.println("Entering funcChooseFromAccount...");

		// Choose an account with balance greater than 0
		for (int i = 0; i < menuGroup.size(); i++) {

			j = menuGroup.size() - 1;

			while (flag == false && j >= 0) {

				option = menuGroup.get(j).findElement(By.tagName("span")).getText();
				System.out.println("option: " + option);

				result = option.split(" ");

				switch (result[3]) {
				case "AUD":
				case "CAD":
				case "SGD":
					amount = result[2].substring(2, result[2].length());
					break;

				case "USD":
				case "EUR":
				case "GBP":
				default:
					amount = result[2].substring(1, result[2].length());
					break;
				}
				if (amount.contains(",")) {
					moneyAmount = new BigDecimal(amount.replace(",", ""));
				} else {
					moneyAmount = new BigDecimal(amount);
				}
				// System.out.println("moneyAmount： "+moneyAmount);
				if (moneyAmount.compareTo(BigDecimal.ZERO) == 1) {
					flag = true;
					menuGroup.get(j).findElement(By.tagName("span")).click();  // This account has non-zero balance and can be used.
					break;
				} else {
					j--;
				}
			}

		}
		return flag;
	}

	/*
	 * Separate balance from the text in each dropdown list item
	 */
	public static BigDecimal funcCPGetBalance(String text) throws Exception {
		BigDecimal moneyAmount = new BigDecimal("0.00");
		// Boolean flag=false;
		String amount = "";
		String[] result;

		System.out.println("Entering funcCPGetBalance...");

		result = text.split(" ");
		try {
			switch (result[3]) {
			case "AUD":
			case "CAD":
			case "SGD":
				amount = result[2].substring(2, result[2].length());
				break;

			case "USD":
			case "EUR":
			case "GBP":
			default:
				amount = result[2].substring(1, result[2].length());
				break;
			}
		} catch (Exception e) {
			System.out.println("This account has 0 balance so that no currency found!");
			amount = "0";
		}
		if (amount.contains(",")) {
			moneyAmount = new BigDecimal(amount.replace(",", ""));
		} else {
			moneyAmount = new BigDecimal(amount);
		}
		System.out.println(moneyAmount);
		return moneyAmount;
	}

	/*
	 * Developed by Alex.L for validating the info in transfer history on 10/09/2019
	 */
	public static void funcValidateTransferHistory(WebDriver driver, String fAccount, String tAccount, String amount,
			String currency, String Brand) throws Exception {

		String rfAccount, rtAccount, rAmount, transferStatus;

		// check the from account
		rfAccount = driver.findElement(By.xpath("//ul[@class='table_demo']/li[3]//tr[1]/td[2]/div")).getText();
		Utils.funcIsStringContains(rfAccount, fAccount + " (" + currency + ")", Brand);

		// check the to account
		rtAccount = driver.findElement(By.xpath("//ul[@class='table_demo']/li[3]//tr[1]//td[3]//div[1]")).getText();
		Utils.funcIsStringContains(rtAccount, tAccount + " (" + currency + ")", Brand);

		// check the amount
		rAmount = driver.findElement(By.xpath("//ul[@class='table_demo']/li[3]//tr[1]//td[4]//div[1]")).getText();
		if (rAmount.contains(",")) {
			rAmount = rAmount.replace(",", "");
		}
		Utils.funcIsStringContains(amount, rAmount, Brand);

		// Check status
		transferStatus = driver.findElement(By.xpath("//ul[@class='table_demo']//li[3]//tr[1]//td[5]//div[1]")).getText();
		System.out.println("Transfer status is " + transferStatus);
		if (!transferStatus.equalsIgnoreCase("Paid")) {

			System.out.println(
					"Transfer is usually automatically done and the status should be Paid unless the account has credit. "
							+ "Please manually check to ensure it is not a problem.");
		} else {
			System.out.println("Transfer Status is correct.");
		}
	}

	boolean funcQuestion(boolean result) throws Exception {
		boolean flag = false;
		int[] failResult = new int[] { 1, 1, 3, 3, 1, 1, 1, 3, 2, 3 };
		int[] successResult = new int[] { 2, 2, 1, 1, 2, 2, 2, 2, 1, 4 };
		String selector, messageAct, messageExpSuc = "Congratulations. You have passed the ASIC questionnaire.";
		String messageExpFail = "Unfortunately, you have not passed the ASIC questionnaire. Please try again.";
		int j;

		// Click the questionnaire button to show questions dialog
		Thread.sleep(1000);
		wait02.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//button[@class='el-button home_pendingAction_button el-button--default']")));
		driver.findElement(By.xpath("//button[@class='el-button home_pendingAction_button el-button--default']"))
				.click();
		Thread.sleep(1000);

		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='form_list clearfix']")));

		if (result == true) {

			for (int i = 0; i < successResult.length; i++) {

				Thread.sleep(500);
				j = i + 1;
				selector = "//ul[@class='form_list clearfix']//li[" + j + "]//label[" + successResult[i] + "]";
				driver.findElement(By.xpath(selector)).click();
			}

			// Click submit button
			driver.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();

			try {
				wait02.until(ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("div.success_content > p.success_info:nth-child(2)")));
				messageAct = driver.findElement(By.cssSelector("div.success_content > p.success_info:nth-child(2)"))
						.getText();
				Assert.assertEquals(messageAct, messageExpSuc);
				flag = true;
			} catch (NoSuchElementException e) {
				flag = false;
				e.printStackTrace();
			}

		} else {
			for (int i = 0; i < failResult.length; i++) {

				Thread.sleep(500);
				j = i + 1;
				selector = "//ul[@class='form_list clearfix']//li[" + j + "]//label[" + failResult[i] + "]";

				driver.findElement(By.xpath(selector)).click();

			}

			// Click submit button
			driver.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();

			try {
				wait02.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("div.error div.error_content > p:nth-child(2)")));
				messageAct = driver.findElement(By.cssSelector("div.error div.error_content > p:nth-child(2)"))
						.getText();
				Assert.assertEquals(messageAct, messageExpFail);
				flag = true;

			} catch (NoSuchElementException e) {
				flag = false;
				e.printStackTrace();
			}
		}

		Thread.sleep(1000);
		wait01.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(".//a[text()='Back To Home Page']"))))
				.click();

		return flag;
	}

	void funcSideBarVerification(WebElement mainMenu, WebElement subMenu, String keyword) throws Exception {
		// Click and verify the DEPOSIT FUNDS
		Thread.sleep(1500);
		mainMenu.click();

		if (subMenu!=null) {
			Thread.sleep(1500);
			subMenu.click();;
			
		}
		Thread.sleep(1500);
		Utils.funcIsStringContains(driver.getCurrentUrl(), keyword, "");
		Thread.sleep(500);
		mainMenu.click();
	}
	
	//Yanni: Moved from CPLogin.java. ResetCPMTXPwd is duplicate in CPLogin.
	//Used for query reset passwd link from tb_mail_send_log
	String funcQueryMailDB(String queryString, String Brand, String TestEnv) throws Exception {
		
		//Search tb_mail_send_log to get the password reset link
		String selectSql="select vars from tb_mail_send_log where vars like '%" + queryString +"%' order by create_time desc limit 1;";
		String dbName = Utils.getDBName(Brand)[1];  
	
		return DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);
	}
	
	//Yanni:  Copied from CPLogin.java. ResetCPMTXPwd is duplicate in CPLogin.
	//Open the reset pw link in new browser and reset password
	void funcInputResetPassword(String url, String Pwd, String Brand) throws Exception {
		WebDriver driver2;
        ChromeOptions options2=new ChromeOptions();
        options2.addArguments("--incognito");
        options2.addArguments("--no-sandbox");
        options2.setExperimentalOption("useAutomationExtension", false);
       // System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
        driver2 = new ChromeDriver(options2);
        			  
        driver2.manage().window().maximize();
        driver2.get(url);
        Thread.sleep(3000);
        
        driver2.findElements(By.xpath("//input")).get(0).sendKeys(Pwd);
        Thread.sleep(1000);
        driver2.findElements(By.xpath("//input")).get(1).sendKeys(Pwd);
        Thread.sleep(1000);
        
        switch(Brand.toLowerCase())
        {
        case "vt":
        	        	
        	driver2.findElement(By.xpath("//span[contains(text(),'Submit')]")).click();
            Thread.sleep(2000);
            Utils.funcIsStringContains(driver2.findElement(By.xpath("//div[@class='success_content']/div[@class='main']/p")).getText(), "Your password has been updated.", "");
            
            Thread.sleep(1000);
            driver2.findElement(By.xpath("//a[@class='el-button blue_button']")).click();
        	break;
        	
        	default:
        		driver2.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();
        	    Thread.sleep(2000);
        	    Utils.funcIsStringContains(driver2.findElement(By.xpath("//p[@class='success_info']")).getText(), "Your password has been updated.", "");
        	    
                Thread.sleep(1000);
                driver2.findElement(By.xpath("//a[@class='el-button btn_blue']")).click();
        }
        
 

        driver2.quit();
	}
	
}
