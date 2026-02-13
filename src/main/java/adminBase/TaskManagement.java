package adminBase;

import newcrm.global.GlobalMethods;
import newcrm.pages.adminpages.AdminPage;
import newcrm.pages.clientpages.elements.AdvertiseElements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;

public class TaskManagement {
	
	private WebDriver driver;
	private String Brand;

	WebDriverWait wait03;
	WebDriverWait longwait;
	JavascriptExecutor js;
	public TaskManagement(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
		this.longwait = new WebDriverWait(driver, Duration.ofSeconds(50));
		js = (JavascriptExecutor)driver;
	}
	
	//The index of 'Other' in pending reason 
	public int getIDPOAPendingOtherReasonIndex()
	{
		int reasonIndex = 0;
		
		switch(Brand)
		{
			case "vt":
			case "fsa":
			case "svg":
				reasonIndex = 9;
				break;
		
			default:
				reasonIndex = 6;
				
		}
		
		return reasonIndex;		
		
	}

	public void openAutoTransfer(String AdminURL)
	{
		try {
			// Navigate to Account Transfer Setting
			driver.navigate().to(AdminURL);

			// Click System Setting
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("System Setting"))).click();

			//Click Account Transfer Setting
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Account Transfer Setting"))).click();


			WebElement transferStatus = driver.findElement(By.xpath("//div[@class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-id-mySwitch bootstrap-switch-off bootstrap-switch-animate']"));

			if (transferStatus!= null) {
				driver.findElement(By.xpath("//div[@class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-id-mySwitch bootstrap-switch-off bootstrap-switch-animate']")).click();
			}
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("Cannot check auto transfer status");
		}

	}

	public void openLeverageAutoAudit(String AdminURL)
	{
		try {
			// Navigate to Leverage Setting
			driver.navigate().to(AdminURL);

			// Click System Setting
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("System Setting"))).click();

			//Click Leverage Transfer Setting
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Leverage Setting"))).click();

			WebElement leverageStatus = driver.findElement(By.xpath("//div[contains(@class, 'bootstrap-switch-off')]"));

			GlobalMethods.printDebugInfo("leverageStatus is off ");
			leverageStatus.click();

		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("LeverageStatus is on or cannot check auto transfer status");
		}

	}

	public void funcWDCompletebyChannelSmokeTest(String AdminURL, String TraderEmail,String Brand,String Regulator) throws Exception {
		LogUtils.info("TraderEmail:\n"+TraderEmail);
		AdminPage ap = new AdminPage(driver);

		vantagecrm.TaskManagement.WithdrawM channelInfo;
			//String searchKey = Utils.webUserPrefix;
		switch(Brand.toLowerCase()) {
			/*case "um":
			case "mo":
				channelInfo = vantagecrm.TaskManagement.WithdrawM.BankTransfer;
				break;
			case "vfx":
				channelInfo = vantagecrm.TaskManagement.WithdrawM.Neteller;
				break;*/
			case "vt":
			case "star":
            case "mo":
            case "um":
				channelInfo = vantagecrm.TaskManagement.WithdrawM.PoliBankTransfer;
				break;
			case "pug":
				channelInfo = vantagecrm.TaskManagement.WithdrawM.PUGBankTransfer;
				break;
			case "vjp":
				channelInfo = vantagecrm.TaskManagement.WithdrawM.CryptoETH;
				break;
			default:
				channelInfo = vantagecrm.TaskManagement.WithdrawM.BankTransfer;
		}
		// Navigate to Withdrawal Audit module
		driver.navigate().to(AdminURL);
		waitLoading();

		// Click Task Management/Withdrawl Audit
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task"))).click();
		clickWithdrawAuditMenu();
		waitLoading();

		// Wait for page content to load
		waitLoadingWithdrawAuditContent();

		GlobalMethods.printDebugInfo("Start to complete audit records with channel " + channelInfo.getName() + " ...");

		// Select Withdraw Audit channel
		Select withdrawType = new Select(driver.findElement(By.xpath("//select[@class='form-control bootstrap-table-filter-control-withdrawType']")));
		withdrawType.selectByVisibleText(channelInfo.getName());

		//input account email
		WebElement inputEmail = driver.findElement(By.xpath("//input[@class='form-control bootstrap-table-filter-control-email search-input']"));
		inputEmail.click();
		inputEmail.sendKeys(TraderEmail);

		//click search button
		WebElement searchBtn = driver.findElement(By.xpath("//button[@id='query']"));
		searchBtn.click();

		waitLoading();

		//check the first withdraw is auto withdraw or not
		WebElement firstS = driver.findElement(By.xpath("//*[@id='table']/tbody/tr[1]/td[12]"));
		String fStatus = firstS.getText();
		GlobalMethods.printDebugInfo("fStatus:" + fStatus);

		WebElement firstO = driver.findElement(By.xpath("//*[@id='table']/tbody/tr[1]/td[20]"));
		String fOperator = firstO.getText();
		GlobalMethods.printDebugInfo("fOperator:" + fOperator);

		if(fStatus.equalsIgnoreCase("Completed")&& fOperator.equalsIgnoreCase("System Auto"))
		{
				//click Reverse button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='table']/tbody/tr[1]/td//a/span[text()='Reverse']")));
			driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td//a/span[text()='Reverse']")).click();
			ap.waitLoading();

			// After click reverse button, table need time to load.
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='bootstrap-dialog-message' and not(string(div)='')]")));

			//click confirm button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='btn cantOp btn-primary'][text()='Confirm']")));
			WebElement confirmReverseBtn = driver.findElement(By.xpath("//button[@class='btn cantOp btn-primary'][text()='Confirm']"));
			confirmReverseBtn.click();

		}
		else if(!fStatus.equalsIgnoreCase("Reversed") && !fStatus.equalsIgnoreCase("Submitted")){
			//select "Accepted" status
			Select status = new Select(driver.findElement(By.xpath("//select[@class='form-control bootstrap-table-filter-control-status']")));
			status.selectByVisibleText("Accepted");

			waitLoading();

			searchBtn = driver.findElement(By.xpath("//button[@id='query']"));
			searchBtn.click();

			// Wait until the list is loaded
			waitLoading();

			//claim the first withdraw application
			WebElement claimCheckbox = driver.findElement(By.xpath("//table[@id='table']/tbody/tr/td[1]/input[@name='checkname']"));
			claimCheckbox.click();

			WebElement claimBtn = driver.findElement(By.xpath("//div[@id='startClaim']"));
			//claimBtn.click();
			js.executeScript("arguments[0].click();", claimBtn);

			// Wait for page content to load
			waitLoadingWithdrawAuditContent();

			//wait for success message
			//wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='messenger-message-inner'][text()='Successful operation']")));
			try {
				WebElement successMes = driver.findElement(By.xpath("//div[@class='messenger-message-inner'][text()='Successful operation']"));
				Boolean eleDisplay = successMes.isDisplayed();
				GlobalMethods.printDebugInfo("successMes: " + eleDisplay);

			} catch (Exception e) {
				GlobalMethods.printDebugInfo("no successful message found");
			}

			//refresh
			driver.navigate().refresh();
			ap.waitLoading();

			// Wait for page content to load
			waitLoadingWithdrawAuditContent();

			//input account email
			inputEmail = driver.findElement(By.xpath("//input[@class='form-control bootstrap-table-filter-control-email search-input']"));
			inputEmail.click();
			inputEmail.sendKeys(TraderEmail);

			//select "Accepted" status
			status = new Select(driver.findElement(By.xpath("//select[@class='form-control bootstrap-table-filter-control-status']")));
			status.selectByVisibleText("Accepted");

			//click search button
			driver.findElement(By.xpath("//button[@id='query']")).click();
			ap.waitLoading();

			//click complete button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='table']/tbody/tr[1]/td//a/span[text()='Complete']")));
			driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td//a/span[text()='Complete']")).click();
			ap.waitLoading();

			// After click complete button, table need time to load.
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='bootstrap-dialog-message' and not(string(div)='')]")));

			//click confirm button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='btn cantOp'][text()='Confirm']")));
			WebElement confirmBtn = driver.findElement(By.xpath("//button[@class='btn cantOp'][text()='Confirm']"));
			confirmBtn.click();

			//refresh
			driver.navigate().refresh();
			ap.waitLoading();

			// Wait for page content to load
			waitLoadingWithdrawAuditContent();

			//input account email
			WebElement emailAfterRefresh = driver.findElement(By.xpath("//input[@class='form-control bootstrap-table-filter-control-email search-input']"));
			emailAfterRefresh.click();
			emailAfterRefresh.sendKeys(TraderEmail);

			//click search button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='query']")));
			driver.findElement(By.xpath("//button[@id='query']")).click();

			waitLoading();

			//check if the first row status is completed
			//wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='table']/tbody/tr/td[11]")));
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='table']/tbody/tr[1]/td/p[contains(text(),'Completed')]")));

			//WebElement comStatus = driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td[11]"));
			WebElement comStatus = driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td/p[contains(text(),'Completed')]"));
			String ifCompleted = comStatus.getText();

			if (ifCompleted.equalsIgnoreCase("Completed")) {
				GlobalMethods.printDebugInfo("Withdraw audit accepted to completed passed");

			}
			Assert.assertEquals(ifCompleted, "Completed", "Withdraw audit accepted to completed failed");

			//click Reverse button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='table']/tbody/tr[1]/td//a/span[text()='Reverse']")));
			driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td//a/span[text()='Reverse']")).click();
			ap.waitLoading();

			// After click reverse button, table need time to load.
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='bootstrap-dialog-message' and not(string(div)='')]")));

			//click confirm button
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='btn cantOp btn-primary'][text()='Confirm']")));
			WebElement confirmReverseBtn = driver.findElement(By.xpath("//button[@class='btn cantOp btn-primary'][text()='Confirm']"));
			confirmReverseBtn.click();

		}else if((fStatus.equalsIgnoreCase("Submitted")))
		{
			GlobalMethods.printDebugInfo("The withdraw status is submitted");
			return;
		}

		//check if status is reversed
		WebElement revStatus;
		ap.waitLoading();

		//try again if reversed status didn't get
		for (int attempt = 0; attempt < 2; attempt++) {
			try {
				boolean reversedStatus = driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td/p[contains(text(),'Reversed')]")).isDisplayed();
				GlobalMethods.printDebugInfo("reversed button status: " + reversedStatus);
				if (reversedStatus) {
					GlobalMethods.printDebugInfo("Withdraw audit completed to reversed passed");
				}
				Assert.assertEquals(reversedStatus, true, "Withdraw audit completed to reversed failed");
				break;
			} catch (Exception e) {
				if (attempt == 0) {
					searchBtn = driver.findElement(By.xpath("//button[@id='query']"));
					searchBtn.click();

					// Wait until the list is loaded
					waitLoading();

					GlobalMethods.printDebugInfo("Reversed button didn't show, retrying...");

					// Small wait before retry
					try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
				} else {
					GlobalMethods.printDebugInfo("Reversed button didn't show");
				}
			}
		}

//		revStatus = driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td/p[contains(text(),'Reversed')]"));
//		String ifReversed = revStatus.getText();
//		if (ifReversed.equalsIgnoreCase("Reversed")) {
//			GlobalMethods.printDebugInfo("Withdraw audit completed to reversed passed");
//
//		}

	}

	private void waitLoadingWithdrawAuditContent() {
		try {
			wait03.until(driver -> {
				WebElement mainContent = driver.findElement(By.xpath("//div[@id='mainContent']"));
				List<WebElement> children = mainContent.findElements(By.xpath("./*"));
				return !children.isEmpty();
			});
			longwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#table")));

			WebElement tbl = driver.findElement(By.cssSelector("table#table"));
			String cls = tbl.getAttribute("class");

			if (cls == null) {
				Assert.fail("Withdraw audit table abnormal");
			}
		}
		catch(Exception e)
		{
			Assert.fail("Withdraw audit page content not found");
		}
	}

	public void clickWithdrawAuditMenu() {
		try {
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Withdrawal Audit"))).click();
		} catch (NoSuchElementException e) {
			System.out.println("Can't find menu Withdrawal Audit, trying to locate Withdrawal Audit...");
			driver.findElement(By.linkText("Withdrawal Audit")).click();
		}
	}

	public void waitLoading() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));

		wait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.xpath("//div[contains(@class,'fixed-table-loading') and contains(@style,'display: flex')]"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});

		//GlobalMethods.printDebugInfo(new Date().toString() + " Loading page completed, url is: " + driver.getCurrentUrl());
	}

}






