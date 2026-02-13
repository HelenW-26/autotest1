package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import adminBase.ClientSearch;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;

public class UpdateClient {

	WebDriver driver;
	// different wait time among different environments. Test environment will
	// use 1 while beta & production will use 2.
	// Initialized in LaunchBrowser function.
	int waitIndex = 1;
	String userName; // Client Name
	WebDriverWait wait15;
	String newPwd = "123Qwe";
	SoftAssert saAssert = new SoftAssert();

	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{

		ChromeOptions options = new ChromeOptions();
		options.setAcceptInsecureCerts(true);

		//System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
		
		if(Boolean.valueOf(headless)) {
			options.addArguments("window-size=1920,1080");
			options.addArguments("headless");
		}
		driver = new ChromeDriver(options);

		context.setAttribute("driver", driver);          // Added by Yanni on
												          // 5/15/2019

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
		}

		wait15 = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	// @AfterClass(alwaysRun=true)
	void ExitBrowser() {

		Utils.funcLogOutAdmin(driver);
		// Close all browsers
		driver.quit();
	}

	// Login Admin with credentials

	@Parameters({ "AdminURL", "AdminName", "AdminPass", "Brand" })
	@Test(priority = 0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand) throws Exception {

		// Login AU admin
		driver.get(AdminURL);
		Utils.funcLogInAdmin(driver, AdminName, AdminPass, Brand);
	}

	@Test(invocationCount = 1, dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv", "TraderName" })
	public void resetPWD(String Brand, String TestEnv, String TraderName) throws Exception {

		int j;
		String keyword = TraderName;
		boolean flag = false;

		if ((Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) && TestEnv.equalsIgnoreCase("beta")) {
			if (Utils.registerUserName != null && Utils.registerUserName.length() > 0) {

				keyword = Utils.registerUserNameVT;
				flag = true;

			}
		} else {
			if (Utils.registerUserName != null && Utils.registerUserName.length() > 0) {
				keyword = Utils.registerUserName;
				flag = true;

			}
		}

		// Navigate to Client menu
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Client")).click();
		Thread.sleep(2000);  // it is required otherwise the method will fail
							  // when run together with other methods in the
							  // class

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// Select "Search by real Name"
		// Create a new instance searchBar
		ClientSearch searchBar = new ClientSearch(driver);

		if (flag == true) {
			searchBar.inputUserName(keyword);
		} else {
			searchBar.inputUserEmail(keyword);
		}

		// Click Search button
		searchBar.clickSearch();
		
		Thread.sleep(500);
		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		List<WebElement> trs = driver.findElements(By.cssSelector("table#table>tbody>tr"));
		// Thread.sleep(1000);

		if (trs.size() == 0) {
			Assert.assertTrue(false, "Loading list  error.");
		}

		if ((trs.size() == 1) && trs.get(0).getAttribute("class").equals("no-records-found")) {
			Assert.assertTrue(false, "No Client is found.");
		}

		for (j = 0; j < trs.size(); j++) {
			
			//for vfsc2 feng liu
			userName = trs.get(j).findElement(By.xpath("//td[5]/a")).getText();

			if (Utils.isWebUser(userName) || Utils.isJoint(userName) || Utils.isAddUser(userName)) {
				System.out.println("System is going to change Client Portal password for user " + userName);
				break;
			}
		}

		if (j >= trs.size()) {
			Assert.assertTrue(false,
					"No qualified Client (with name starting with (" + Utils.webUserPrefix + ") is found.");
		}

		// Click the user name to open the client profile, changed as above: feng liu
		trs.get(j).findElement(By.xpath("//td[5]/a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));

		// Update password
		driver.findElement(By.id("password")).sendKeys(newPwd);

		// Click Submit button
		driver.findElement(By.cssSelector("div.panel-footer.input-footer button:nth-of-type(1)")).click();

		// Assert success
		String a = wait15
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")))
				.getText();
		System.out.println("Change Status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));

	}

	@Test(invocationCount = 1, dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv", "TraderName" })
	public void setCountry2Aus(String Brand, String TestEnv, String TraderName) throws Exception {
		boolean flag;
		flag = funcChangeCountry(Brand, TestEnv, TraderName, "Australia");

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		Assert.assertTrue(flag, "Failed to set country to Australia for user " + TraderName);
	}

	@Test(invocationCount = 1, dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv", "ThaiAccount" })
	public void setCountry2Thai(String Brand, String TestEnv, String ThaiAccount) throws Exception {
		boolean flag;
		flag = funcChangeCountry(Brand, TestEnv, ThaiAccount, "Thailand");

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		Assert.assertTrue(flag, "Failed to set country to Thailand for user " + ThaiAccount);
	}

	@Test(invocationCount = 1, dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv", "MalayAccount" })
	public void setCountry2Malay(String Brand, String TestEnv, String MalayAccount) throws Exception {
		boolean flag;
		flag = funcChangeCountry(Brand, TestEnv, MalayAccount, "Malaysia");

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		Assert.assertTrue(flag, "Failed to set country to Malaysia for user " + MalayAccount);
	}

	@Test(invocationCount = 1, dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv", "VietAccount" })
	public void setCountry2Viet(String Brand, String TestEnv, String VietAccount) throws Exception {
		boolean flag;
		//Change Viet Nam to Vietname, feng liu
		flag = funcChangeCountry(Brand, TestEnv, VietAccount, "Vietnam");

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		Assert.assertTrue(flag, "Failed to set country to VietNam for user " + VietAccount);
	}

	@Test(invocationCount = 1, dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "Brand", "TestEnv", "NigeAccount" })
	public void setCountry2Nige(String Brand, String TestEnv, String NigeAccount) throws Exception {
		boolean flag;
		flag = funcChangeCountry(Brand, TestEnv, NigeAccount, "Nigeria");

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		Assert.assertTrue(flag, "Failed to set country to Nigeria for user " + NigeAccount);
	}

	/*
	 * In Client Management -> Client, search with the specified keyword and
	 * return the search result.
	 * If there is userName recorded in Utils, use it first. Otherwise, use
	 * TraderName.
	 */
	List<WebElement> funcChooseClient(String Brand, String TestEnv, String TraderName) throws Exception {
		List<WebElement> trs = null;

		int j;
		String keyword = TraderName;
		boolean flag = false;

		if ((Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) && TestEnv.equalsIgnoreCase("beta")) {
			if (Utils.registerUserNameVT != null) {
				if (Utils.registerUserNameVT.length() != 0) {
					keyword = Utils.registerUserNameVT;
					flag = true;
				}
			}
		} else {
			if (Utils.registerUserName != null) {
				if (Utils.registerUserName.length() != 0) {
					keyword = Utils.registerUserName;
					flag = true;
				}
			}
		}

		// Navigate to Client menu
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Client")).click();
		Thread.sleep(2000);

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		// Create a new instance searchBar
		ClientSearch searchBar = new ClientSearch(driver);

		if (flag == true) {
			searchBar.inputUserName(keyword);
		} else {
			searchBar.inputUserEmail(keyword);
		}

		// Click Search button
		searchBar.clickSearch();
		

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		trs = driver.findElements(By.cssSelector("table#table>tbody>tr"));

		return trs;
	}

	// To change Client country of residence given login email and country name
	boolean funcChangeCountry(String Brand, String TestEnv, String loginEmail, String countryName) throws Exception {
		int j;
		Select t;

		List<WebElement> trs = funcChooseClient(Brand, TestEnv, loginEmail);
		// Thread.sleep(1000);

		if (trs.size() == 0) {
			System.out.println("Loading list  error.");
			return false;
		}

		if ((trs.size() == 1) && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Client is found.");
			return false;
		}

		for (j = 0; j < trs.size(); j++) {
			//feng liu for vfsc2
			userName = trs.get(j).findElement(By.xpath("//td[5]/a")).getText();
			if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)) {
				System.out.println("System is going to change Country of Residence for user " + userName);
				break;
			}
		}

		if (j >= trs.size()) {
			System.out.println("No qualified Client is found.");
			return false;
		}

		// Click the user name to open the client profile.
		//feng liu, changed as above
		trs.get(j).findElement(By.xpath("//td[5]/a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));

		// Choose Country
		t = new Select(driver.findElement(By.id("country")));
		if (t.getFirstSelectedOption().getText().equals(countryName)) {
			System.out.println("Country is already set. No need to modify.");

			// Click Submit button
			driver.findElement(By.cssSelector("div.panel-footer.input-footer button:nth-of-type(2)")).click();

			return true;
		} else {

			t.selectByVisibleText(countryName);  // Australia, Thailand, Viet
												  // Nam, Malaysia, Nigeria
		}

		// If country=Australia, the field state is a dropdownlist. Otherwise it
		// is an input textbox
		if (countryName.equals("Australia")) {
			t = new Select(driver.findElement(By.id("state")));
			wait15.until(ExpectedConditions.visibilityOf(t.getFirstSelectedOption()));
			t.selectByIndex(1);

		} else {
			wait15.until(ExpectedConditions.visibilityOfElementLocated((By.id("inputState"))));
			driver.findElement(By.id("inputState")).sendKeys(countryName + "Province");

		}

		// Need to reset the Annual Income field
		t = new Select(driver.findElement(By.id("income")));
		t.selectByIndex(3);

		// Click Submit button
		driver.findElement(By.cssSelector("div.panel-footer.input-footer button:nth-of-type(1)")).click();

		// Assert success
		String a = wait15
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")))
				.getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));

		if (a.equals("Modify User successful") || a.equals("update success")) {

			System.out.println("Country is changed to " + countryName + " successfully.");
			// Wait until the list is loaded completely

			return true;
		} else {
			return false;
		}

	}

	// @Test(invocationCount=50,threadPoolSize=5,
	// dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value = { "Brand", "TestEnv", "TraderName" })
	public void simulateUser(String Brand, String TestEnv, String TraderName) throws Exception {

		int j;
		String keyword = TraderName;
		boolean flag = false;

		System.out.printf("%n[START] Thread Id : %s is started!", Thread.currentThread().getId());

		if ((Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) && TestEnv.equalsIgnoreCase("beta")) {
			if (Utils.registerUserNameVT != null) {
				if (Utils.registerUserNameVT.length() != 0) {
					keyword = Utils.registerUserNameVT;
					flag = true;
				}
			}
		} else {
			if (Utils.registerUserName != null) {
				if (Utils.registerUserName.length() != 0) {
					keyword = Utils.registerUserName;
					flag = true;
				}
			}
		}

		// Navigate to Client menu
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Client")).click();

		//driver.findElement(By.id("query")).click();
		
		// Create a new instance searchBar
		ClientSearch searchBar = new ClientSearch(driver);
		// Click Search button
        searchBar.clickSearch();
        
		Thread.sleep(1000);
		System.out.printf("%n[START] Thread Id : %s is ended!", Thread.currentThread().getId());
	}

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */

	@Test
	@Parameters(value = { "Brand", "TestEnv", "TraderName" })
	public void updateClientGeneral(String Brand, String TestEnv, String TraderName) throws Exception {
		List<WebElement> trs = funcChooseClient(Brand, TestEnv, TraderName);
		boolean flag = false;
		int i, j;
		String oldDOB, oldValue, newValue;
		Select t;

		if (trs.size() == 0) {
			System.out.println("Loading list  error.");

		}

		if ((trs.size() == 1) && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Client is found.");

		}

		for (j = 0; j < trs.size(); j++) {
			//liufeng
			userName = trs.get(j).findElement(By.xpath("//td[5]/a")).getText();
			//userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a")).getText();
			if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)) {
				System.out.println("System is going to change general information for user " + userName);
				break;
			} else {
				System.out.println("userName not qualified.");
			}
		}
		//liufeng j must be smaller than trs.size
		assertTrue(j < trs.size(), "No qualified Client is found.");
		/*
		 * if (j >= trs.size()) { System.out.println("No qualified Client is found.");
		 * 
		 * }
		 */

		// Click the user name to open the client profile
		trs.get(j).findElement(By.xpath("//td[5]/a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));

		Thread.sleep(2000);
		// Change Address

		String idValue = "street_name";

		if (driver.findElement(By.id("account_type")).getAttribute("value").equalsIgnoreCase("Joint")) {
			idValue = "streent_name";
		}

		oldValue = driver.findElement(By.id(idValue)).getAttribute("value");
		System.out.println("Old Address is: " + oldValue);

		driver.findElement(By.id(idValue)).clear();
		driver.findElement(By.id(idValue)).sendKeys(Utils.randomNumber(2) + " " + Utils.randomString(4).toUpperCase()
				+ " " + Utils.randomString(4).toUpperCase() + " Street");
		System.out.println("New Address is: " + driver.findElement(By.id(idValue)).getAttribute("value"));

		// Change the DOB
		oldDOB = driver.findElement(By.id("firstBirthday")).getAttribute("value");
		System.out.println("Old Birthday is: " + oldDOB);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = sdf.parse(oldDOB);
		Calendar calInst = Calendar.getInstance();
		calInst.setTime(dt);
		calInst.add(Calendar.YEAR, -2);
		calInst.add(Calendar.MONTH, 1);
		calInst.add(Calendar.DATE, -1);
		dt = calInst.getTime();

		String queryStr = "jQuery('#firstBirthday').val('" + sdf.format(dt).toString() + "');";
		// System.out.println(queryStr);
		((JavascriptExecutor) driver).executeScript(queryStr);
		driver.findElement(By.id("firstBirthday")).sendKeys(sdf.format(dt));
		System.out.println("New Birthday is: " + driver.findElement(By.id("firstBirthday")).getAttribute("value"));

		// Change Account Owner
		//liufeng ID has been changed
		t = new Select(driver.findElement(By.id("accountOwner")));
		//t = new Select(driver.findElement(By.id("p_Id")));
		oldValue = t.getFirstSelectedOption().getText();
		System.out.println("Old Account Owner is: " + oldValue);
		
		  for(i=450; i<t.getOptions().size();i++)
		  {
			newValue=t.getOptions().get(i).getText();
		  	if(newValue.contains(Utils.ibUserPrefix) &&(!newValue.equalsIgnoreCase(oldValue)))
		  	{
			  t.selectByIndex(i);
			  break;
		  	}
		  }
		  /*
		  switch(Brand) {
		  case "au":
		  case "ky":
		  case "vfsc":
		  case "fca":
		  case "regulator2":
		  
		  if(newValue.startsWith(Utils.ibUserPrefix) &&
		  (!newValue.equalsIgnoreCase(oldValue)))
		  {
		  t.selectByIndex(i);
		  flag = true;
		  }
		  break;
		  case "vt":
		  case "pug":
		  if(newValue.contains(Utils.ibUserPrefix) &&
		  (!newValue.equalsIgnoreCase(oldValue)))
		  {
		  t.selectByIndex(i);
		  flag = true;
		  }
		  break;
		  }
		  
		  if (flag == true) {
		  break;
		  }
		  }*/
		 
		System.out.println("New Account Owner is: " + t.getFirstSelectedOption().getText());
		/*if (i >= t.getOptions().size()) {
			System.out.println("Can't find an account owner with name starting with " + Utils.ibUserPrefix + ".");
		} else {
			System.out.println("New Account Owner is: " + t.getFirstSelectedOption().getText());
		}*/

		// Assign Promotion
		t = new Select(driver.findElement(By.id("promotion")));
		if (t.getOptions().size() == 0) {
			System.out.println("No promotion available!");
		} else {

			for (int count = 1; count < t.getOptions().size(); count++) {
				if (t.getOptions().get(count).getText().startsWith("testPromo")) {
					Thread.sleep(1000);
					t.selectByIndex(count);
					Thread.sleep(1000);
					break;
				}
			}
		}

		System.out.println("New Promotion is: " + t.getFirstSelectedOption().getText());

		// Upload a World Check file if there are less than 2 files
		/*
		 * check the number of elelemts (div.col-zdy-5.worldCheck >
		 * div.col-md-11.input-box).
		 * If NO = 2, then one img is already there.
		 * If NO = 1, then no img
		 */
		funcUploadWorkCk(driver, TestEnv, Brand);

		// If type is Joint, update 2nd application: Mobile and Suburb
		if (driver.findElement(By.id("account_type")).getAttribute("value").equalsIgnoreCase("Joint")) {
			// Update Mobile
			oldValue = driver.findElement(By.id("second_mobile")).getAttribute("value");
			System.out.println("2nd Application old Mobile number is: " + oldValue);

			oldValue = oldValue.concat(Utils.randomNumber(2).toString());
			driver.findElement(By.id("second_mobile")).sendKeys(oldValue);
			System.out.println("2nd Application old Mobile number is: "
					+ driver.findElement(By.id("second_mobile")).getAttribute("value"));

			// Update Address
			oldValue = driver.findElement(By.id("second_street")).getAttribute("value");
			System.out.println("2nd Application old Address is: " + oldValue);
			oldValue = oldValue.concat(" Modified");
			driver.findElement(By.id("second_street")).clear();
			driver.findElement(By.id("second_street")).sendKeys(oldValue);
			System.out.println("2nd Application new Address is: "
					+ driver.findElement(By.id("second_street")).getAttribute("value"));

		}

		driver.findElement(By.xpath(".//button[text()='Submit']")).click();

		// Assert success
		String a = wait15
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")))
				.getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		System.out.println("The status is: " + a);

	}

	/***
	 * Delete the specific client by TraderName
	 * @param testEnv
	 * @param Brand
	 * @param method
	 * @param TraderName
	 * @throws Exception
	 */
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand","TraderName" })
	public void deleteClient(String TestEnv, String Brand, String TraderName, Method method) throws Exception {
		List<WebElement> trs = funcChooseClient(Brand, TestEnv, TraderName);
		boolean flag = false;
		
		if (trs.size() == 0) {
			System.out.println("Loading list  error.");
			assertTrue(trs.size()!=0,"Loading list  error.");

		}
		

		if ((trs.size() == 1) && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Client is found.");
			assertTrue(!((trs.size() == 1) && trs.get(0).getAttribute("class").equals("no-records-found")),"No Client is found.");

		}
		
		
		assertTrue(trs.size()==1,"please make sure the client which  to be delete is unique");
		Thread.sleep(1000);
		trs.get(0).findElement(By.name("btSelectItem")).click();//select the client
		driver.findElement(By.id("delCustomer")).click();//click the delete button
		//click confirm button
		wait15.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.modal-dialog div.bootstrap-dialog-footer-buttons>button:nth-child(2)"))).click();
		
		
		System.out.println("Delete client: " + TraderName + " Success.");
	}
	
	
	// Bulk Delete Client
	@Test(dependsOnMethods = "AdminLogIn", alwaysRun = true)
	@Parameters(value = { "TestEnv", "Brand","KeyWords" })
	public void bulkDeleteClient(String TestEnv, String Brand, String KeyWords, Method method) throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Delete Client ");
		int i = 0, j;
		Boolean flag = false;
		String name = "", account; // Used as temporary names
		//String[] keyWords = new String[] { Utils.webUserPrefix, Utils.addUserPrefix };
		
		//need read keywords from xml,feng liu
		ArrayList<String> keyWords = null;
		String [] keys = KeyWords.split(";");
		assertTrue(keys.length > 0, "you shold give some keywords. For example: abc;def;ghi");
		keyWords =(ArrayList<String>) Arrays.asList(keys);
		
		
		
		driver.navigate().refresh();
		// Navigate to Client menu
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Client")).click();
		Thread.sleep(2000);

		// Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		System.out.println("TEST STARTS: Start to delete client ...");
		System.out.println("-------------------------------------------------------------");

		for (i = 0; i < keyWords.size(); i++) {

			if (flag == true) {
				break;
			}

			// Select Search Options
			ClientSearch searchBar = new ClientSearch(driver);

			searchBar.inputUserName(keyWords.get(i));

			// Click Search button
			searchBar.clickSearch();
			
			Thread.sleep(500);
			// Wait until the list is loaded completely
			wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			List<WebElement> trs = driver.findElements(By.xpath("//div[@class='fixed-table-body']//tbody//tr"));

			// if the search result shows no records:
			if (trs.size() == 1 && trs.get(0).getAttribute("class").equals("no-records-found")) {
				System.out.println("No results for keywords " + keyWords.get(i));
			} else {

				// Look for 1st record with name starting with pattern pat.

				for (j = 0; j < trs.size(); j++) {

					// Get user name
					name = trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)")).getText();

					// Get user account
					account = trs.get(j).findElement(By.cssSelector("td:nth-of-type(11)")).getText();

					// Only delete not-yet-audited user.
					if ((Utils.isWebUser(name) || Utils.isAddUser(name) || Utils.isJoint(name) || Utils.isTestIB(name))
							&& (account.equals(""))) {
						System.out.println("Going to delete user: " + name);
						Thread.sleep(2000);
						trs.get(j).findElement(By.xpath("//td[1]//input[1]")).click();
						// Click delete button
						Thread.sleep(500);
						driver.findElement(By.id("delCustomer")).click();

						// Click confirm button
						driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)"))
								.click();

						// Print message
						// Assert success
						String a = wait15
								.until(ExpectedConditions
										.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")))
								.getText();
						System.out.println("Result is: " + a);
						wait15.until(ExpectedConditions
								.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						saAssert.assertEquals(a, "Delete Successful!");

						String selectSql = "select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name like '%"
								+ name + "%';";
						DBUtils.readDB(selectSql, TestEnv, Brand);

						// break the current loop, only audit one account each
						// time
						break;
					} /*
						 * else {
						 * System.out.println("Not automation user!");
						 * }
						 */
				}

				if (j >= trs.size()) {
					System.out.println("No Qualified Account for keyword " + keyWords.get(i));
					System.out.println();
				}

			}
		}
	}

	/*
	 * Update PROOF OF ADDRESS REQUIRE flag
	 */
	// @Test deprecated by Yanni on 01/06/2020
	@Parameters(value = { "Brand", "TestEnv", "TraderName" })
	public void updatePOAFlag(String Brand, String TestEnv, String TraderName) throws Exception {
		String strFlag = "1";
		funcUpdatePOAFlag(Brand, TestEnv, TraderName, strFlag);
	}

	public void funcUpdatePOAFlag(String Brand, String TestEnv, String TraderName, String strFlag) throws Exception {
		List<WebElement> trs = funcChooseClient(Brand, TestEnv, TraderName);
		int j;
		String currentValue;

		if (trs.size() == 0) {
			System.out.println("Loading list  error.");

		}

		if ((trs.size() == 1) && trs.get(0).getAttribute("class").equals("no-records-found")) {
			System.out.println("No Client is found.");

		}

		for (j = 0; j < trs.size(); j++) {
			userName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a")).getText();
			if (Utils.isWebUser(userName) || Utils.isAddUser(userName) || Utils.isJoint(userName)) {
				System.out.println("System is going to change PROOF OF ADDRESS REQUIRE for user " + userName);
				break;
			}
		}

		if (j >= trs.size()) {
			System.out.println("No qualified Client is found.");

		}

		// Click the user name to open the client profile
		trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));

		Thread.sleep(2000);

		currentValue = driver.findElement(By.id("suspected4")).getAttribute("value");

		System.out.println("Current status of PROOF OF ADDRESS REQUIRE: " + currentValue);
		if (currentValue.equalsIgnoreCase(strFlag)) {
			System.out.println("No need to change to " + strFlag);
			driver.findElement(By.xpath(".//button[text() = 'Close']")).click();
		} else {

			driver.findElement(By.id("suspected4")).click();
			Thread.sleep(1000);

			System.out.println("Change status of PROOF OF ADDRESS REQUIRE to " + strFlag);
			driver.findElement(By.xpath(".//button[text() = 'Submit']")).click();

			System.out.println(driver.findElement(By.cssSelector("div.messenger-message-inner")).getText());

		}

	}

	// Yanni on 01/06/2020
	// Upload a World Check file if there are less than 3 files
	/*
	 * check the number of elements (div.col-zdy-5.worldCheck >
	 * div.col-md-11.input-box).
	 * If NO = 2, then one img is already there. Number of images = Number of
	 * elements found - 1
	 * 
	 * This function is also used by IBClient update.
	 */

	public void funcUploadWorkCk(WebDriver driver, String TestEnv, String Brand) throws Exception {

		List<WebElement> imageList = driver
				.findElements(By.cssSelector("div.col-zdy-5.worldCheck > div.col-md-11.input-box"));
		int j = imageList.size() - 1;

		// Limit the number of world check files to 3 files.
		if (j < 3) {
			System.out.println("Client orginally has " + j + " World Check files. Uploading a new one... ");
			imageList.get(j).findElement(By.xpath(".//input[@type='file' and @data-name='worldCheck']"))
					.sendKeys(Utils.workingDir + "\\proof.png");
			imageList.get(j).findElement(By.xpath(".//a[@href='/file/upload']")).click();
			Thread.sleep(1000);

			System.out.println("New file path: " + imageList.get(j).findElement(By.tagName("img")).getAttribute("src"));
		} else {
			System.out.println("Client already has " + j + " World Check files. NOT UPLOAD a new one... ");
		}

	}
}
