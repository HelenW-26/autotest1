package vantagecrm;


import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * This class is to test the new Client Portal Register
 */

public class CPID3WithProdUser {

	WebDriver driver;
	WebDriverWait wait50;
	WebDriverWait wait15;
	WebDriverWait wait03;
	
	Random r=new Random();
	
	String userID;
	
	String TestEnv;
	String Brand;
	String IBpromotion;
	String WebSiteURL;
	String RegisterURL;
	
	CPRegister registerInst = new CPRegister(); 
	
	//Launch driver
	@BeforeClass(alwaysRun=true)	
	@Parameters(value= {"TestEnv","Brand", "IBpromotion", "WebSiteURL", "RegisterURL","headless"})
	public void LaunchBrowser(String xmlTestEnv, String xmlBrand, @Optional("") String xmlIBpromotion, String xmlWebSiteURL, String xmlRegisterURL,@Optional("False") String headless, ITestContext context)
	{
		
		System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		ChromeOptions options=new ChromeOptions();
		
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
		
		wait03=new WebDriverWait(driver, Duration.ofSeconds(3));
		wait15=new WebDriverWait(driver, Duration.ofSeconds(15));
		wait50=new WebDriverWait(driver, Duration.ofSeconds(50));
		
	
		registerInst.driver=driver; 
		registerInst.wait15=wait15;
		registerInst.wait03=wait03;
		registerInst.wait50=wait50;
		
		TestEnv=xmlTestEnv;
		Brand = xmlBrand;
		IBpromotion = xmlIBpromotion;  
		WebSiteURL = xmlWebSiteURL;
		RegisterURL = xmlRegisterURL;
		
	}
	
	
	@DataProvider(name="GetCustomerInfo")
	String[][] getCustomerInfo() throws Exception
	{
		String filePath = Utils.workingDir + "/src/main/resources/vantagecrm/Data/FRCustomer.xlsx";
		String sheetName = "Sheet1";
		int colNumber = 17;
			
		String[][] customerInfo = FileUtils.readExcel(filePath, sheetName, colNumber);
		
		return customerInfo;
		
		
		
	}
	
	
	@Test(dataProvider="GetCustomerInfo", invocationCount=1)		
	public void RegisterUserMT4(String fName, String lName, String countryName, String phCountryCode,String phNumber, 
			String nationNatlity, String birthDay, String birthMonth, String birthYear, String birthPlace, String idType, String idNo, 
			String streetNo, String streetName, String city, String proVince, String postCode) throws Exception
	{
						
		String strPlatform = "MT4";
		boolean uploadFile = true;		
			
		
		String email=fName +Utils.randomNumber(2).toString()+ Utils.emailSuffix;
		
		System.out.println(email);
		
        int numberOfEmail=6; //Yanni: Need to revise when requirement is finally decided. Most Email expected: Client Portal Access, Live Account, ID3 Pass, Worldcheck pass
		Boolean isBack = false;
	
		
		Map<String, String> countryInfo = new HashMap<String, String>();
		
		
		//Navigate to Entry Page		
		registerInst.funcGo2ClientEntry(Brand, TestEnv, WebSiteURL, RegisterURL, IBpromotion, strPlatform);
		
		
		//Input Entry Page and Submit
		
		/* input email
		 * input Country and phNumber
		 * return Country and Phone code*/
		countryInfo = funcClientEntryPg(fName, lName, countryName, phNumber,  email);
		
		if(countryInfo.isEmpty())
		{
			System.out.println("ClientEntryPage didn't return country name and country code.");
			Assert.assertTrue(false, "ClientEntryPage didn't return country name and country code.");
		}else
		{
			phCountryCode = countryInfo.get("code");
		}
		
		//VerifyClientPortal Page and switch language if language is not English
		
		
		
		//Input values in Page1 and click Next
		funcClientPg1(fName, lName, phCountryCode, phNumber, nationNatlity, birthDay, birthMonth, birthYear, 
				birthPlace,idType, idNo, email);
		
		//Next
		registerInst.clickPgNextBackButton(Brand, isBack);
		 //Check DB	         
	    System.out.println();
		System.out.println("After Page1 Submission....");
		userID=DBUtils.checkDBStatus(fName, TestEnv, Brand);
		
		//Input Page2 - Residential Address etc
		funcClientPg2(fName, TestEnv, Brand, streetNo, streetName, city, proVince, 	postCode);
		//Click Next
		registerInst.clickPgNextBackButton(Brand, isBack);
		//Check DB
        System.out.println();
        System.out.println("After Page2 Submission....");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
		
		//Page3
		registerInst.funcClientPg3(fName, TestEnv, Brand);
		
		registerInst.clickPgNextBackButton(Brand, isBack);
        //Check DB
		System.out.println();
        System.out.println("After Page3 Submission....");       		
		DBUtils.checkDBStatus(fName, TestEnv, Brand);

    
        //Page4
		
		
		//Account Configuration
		registerInst.funcClientPg4(fName, TestEnv,  strPlatform, Brand);
		//Next
		registerInst.clickPgNextBackButton(Brand, isBack);
        //Check DB
		System.out.println();
		System.out.println("After Page4 Submission....");		
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
		
		//Page5
		//Upload ID & POA
		if (uploadFile)
			registerInst.funcClientPg5(fName, lName, TestEnv, Brand);
		
        //If Page5 is active
		try
		{
			if(driver.findElement(By.cssSelector("div.tab_item:nth-child(4)")).getAttribute("class").contains("active")) {
		
				//Next
				registerInst.clickPgNextBackButton(Brand, isBack);

                //Check DB
                System.out.println();
                System.out.println("After Page5 Submission....");
                DBUtils.checkDBStatus(fName, TestEnv, Brand);
			}else
			{
				System.out.println("Page5 is hidden.");
			}
		
		} catch (NoSuchElementException e)
		{
			System.out.println("Page5 is hidden.");
		}
		//Successful Page
		if (!Brand.equalsIgnoreCase("au"))
			registerInst.funcClientPg6(fName, TestEnv, Brand);
        Thread.sleep(2000);
        //Check DB
         System.out.println();
         System.out.println("Page 6, check ID record in DB...");
         System.out.println("When ID3CheckStatus = 1, status = Completed; ID3CheckStatus = 0, status = Submitted.");
         registerInst.funcGetIDRecord(userID, TestEnv, Brand);  //Need to comment out when ID function is ready

         System.out.println();
         System.out.println("Page 6, check World Check record in DB...");
         System.out.println("DB query should return at least 1 record. WorldCheckStatus =1, status = Completed; WorldCheckStatus = 0, status = Submitted.");
         registerInst.funcGetWorldCheck(userID, TestEnv, Brand);         //Need to comment out when ID function is ready

         System.out.println();
         System.out.println("Page 6, check POA record in DB...");
         System.out.println("When ID3CheckStatus = 1 and WorldCheckStatus = 1, DB query returns nothing. Otherwise, return 1 record with status = Submitted.");
         registerInst.funcGetPOARecord(userID, TestEnv, Brand);

                
         System.out.println();
         System.out.println("Page6, check Email Records...");
         System.out.println("\n There should have 4 emails after user completes registration:");
         System.out.println("1) 2*Important Documentation Relating to Your Account\n2) Client Portal Access\n3) New Leads email Sales");
         
         System.out.println("\nWhen Account is automatically audited, 2 emails should be sent:");
         System.out.println("1) New Open Account Notification(to sales)"); 
         System.out.println("2) New MT4/5 Live Account");
                 
         //Check Email
         //Utils.readEmail(email.toLowerCase(), Brand, TestEnv, numberOfEmail);	
         DBUtils.readEmailvUserName(fName, Brand, TestEnv, numberOfEmail);         
         
         //Print password
         registerInst.funcCheckEmailtoGetPassword(Brand, TestEnv, 1);
	}
	
	

	@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}

	void funcSwitchLanguage() throws Exception
	{
		registerInst.funcSwitchLanguage();
	}
	
	void funcDropdownSelectValue(String nationality) throws Exception
	{
		List<WebElement> liItems;
		int j;
		//Get all li items which has Nationality listed
		liItems = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		for(j=0; j<liItems.size(); j++)
		{
			if (liItems.get(j).getText().trim().equalsIgnoreCase(nationality))
			{
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
				liItems.get(j).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(j>=liItems.size())
		{
			System.out.println("Can't find the specified Nationality: " + nationality);
			Assert.assertTrue(false, "Can't find the specified Nationality: " + nationality);
		}

	}
	
	void funcClientPg1(String fName, String lName, String phCountryCode, String phNumber, String nationNatlity, String birthDay, String birthMonth, String birthYear, 
			String birthPlace, String idType, String idNo, String email) throws Exception
	{
		
		String valueTxt;
		WebElement txtControl;
		
		if(Brand.equalsIgnoreCase("fsa") ||Brand.equalsIgnoreCase("svg") || Brand.equalsIgnoreCase("vt"))
		{
			funcSwitchLanguage(); //switch to English otherwise PUG
			Thread.sleep(1000);
		}
		
		Thread.sleep(1000);
		
		//Verify age title 
		if(Brand.equalsIgnoreCase("vt")) {
			String pageTitle = driver.findElement(By.cssSelector("strong.poppins-medium")).getText();
			Assert.assertTrue(pageTitle.equals("Personal Details"), "Page Title is " + pageTitle + " Expected is: Personal Details");
		
		}else {
			String pageTitle = driver.findElement(By.cssSelector("h3")).getText();
			Assert.assertTrue(pageTitle.equals("PERSONAL DETAILS"), "Page Title is " + pageTitle + " Expected is: PERSONAL DETAILS");
		
		}
		
		//Run steps only when tab is active
		if(driver.findElement(By.cssSelector("div.tab_item:nth-child(1)")).getAttribute("class").contains("active")) {
		
			//Input Personal details
			
			//Input title: ASIC doesn't have title field
			switch(Brand)
			{
				case "au":
					break;

				default:					
					driver.findElement(By.id("title")).click();
					Thread.sleep(500);
					funcDropdownSelectRandomOption();
										
			}

			//Verify First Name is brought in as expected
			txtControl = driver.findElement(By.id("firstName"));
			funcVerifyBringInValue(txtControl, fName);
			
			
			//Verify Last Name is brought in as expected
			txtControl = driver.findElement(By.id("lastName"));
			funcVerifyBringInValue(txtControl, lName);
			
					
			//Input the Nationality
			driver.findElement(By.id("nationalityId")).click();
			Thread.sleep(500);
			funcDropdownSelectValue(nationNatlity);
			
			//Check email value is brought in correctly
			txtControl = driver.findElement(By.id("email"));
			valueTxt= ((JavascriptExecutor)driver).executeScript("return arguments[0].value", txtControl).toString();
			
			Assert.assertTrue(valueTxt.equalsIgnoreCase(email), "Email brought in from Entry page is wrong. It is " + valueTxt + ". It should be " + email);
			
			
			//Verify phoneCode is brought in as expected
			if(!Brand.equalsIgnoreCase("fsa") && !Brand.equalsIgnoreCase("svg"))
			{
				txtControl = driver.findElement(By.id("phoneCode"));
				funcVerifyBringInValue(txtControl, phCountryCode);
			}
	
			//Verify phoneNumber is brought in as expected
			txtControl = driver.findElement(By.id("mobile"));
			funcVerifyBringInValue(txtControl, phNumber);
						
			//Select birth day
			driver.findElement(By.id("dob")).click();
			Thread.sleep(500);
			funcDropdownSelectValue(birthDay);
		
			//Select birth month
			driver.findElement(By.xpath(".//input[@placeholder='Month']")).click();
			Thread.sleep(500);
			funcDropdownSelectValue(birthMonth);
			
			//Select birth year
			driver.findElement(By.xpath(".//input[@placeholder='Year']")).click();
			Thread.sleep(500);
			funcDropdownSelectValue(birthYear);
			
			if(!Brand.equalsIgnoreCase("au")) {
				//Input the placeOfBirth
				driver.findElement(By.id("placeOfBirth")).click();
				Thread.sleep(500);
				funcDropdownSelectValue(birthPlace);
			
				//Select Identification Type
				driver.findElement(By.id("idType")).click();
				Thread.sleep(500);
				funcDropdownSelectValue(idType);
					
				//Input Identification Number
				driver.findElement(By.id("idNumber")).sendKeys(idNo);
			
			}
			
			//Check the reference
			driver.findElement(By.xpath("//span[@class='el-checkbox__inner']")).click();
			Thread.sleep(500);
			
			//Input referrer
			if(Brand.equalsIgnoreCase("vt")) {
				
				driver.findElement(By.xpath("//div[@class='el-form-item checkbox']//input[@class='el-input__inner']")).sendKeys("Automation");
			}else {
				
				driver.findElement(By.xpath("//li[@class='pd_checkbox']//input[@class='el-input__inner']")).sendKeys("Automation");
			}
		}
	}
	
	void funcClientPg2(String fName, String TestEnv, String Brand, String streetNo, String streetName, String city, String proVince, 
			String postCode) throws Exception
	{
		
		//Run steps only when tab is active
		if(driver.findElement(By.cssSelector("div.tab_item:nth-child(1)")).getAttribute("class").contains("active")) {
		
			String countryName; 
			

            countryName =  ((JavascriptExecutor)driver).executeScript("return arguments[0].value", driver.findElement(By.id("countryCode"))).toString();
            
            System.out.println("Country Name is: " + countryName);
            
			//Yanni: to reflect UK address format change.
            //Input the address。 When country Name = "United Kingdom", need to input Street Number and Street Name seperately
            if(countryName.equalsIgnoreCase("United Kingdom") || countryName.equalsIgnoreCase("France"))
            {
            	driver.findElement(By.id("streetNumber")).sendKeys(streetNo);
            	driver.findElement(By.id("address")).sendKeys(streetName);
            }else
            {
            	driver.findElement(By.id("address")).sendKeys("16 Gordon Rd");
            }
						
			//Input the state. When country = Australia, state will be a dropdown list.
			if(countryName.equalsIgnoreCase("Australia"))
			{

				driver.findElement(By.id("state")).click();
				funcDropdownSelectRandomOption();  
				
			}else
			{
				driver.findElement(By.id("state")).sendKeys(proVince);
			}
			//Input the suburb
			driver.findElement(By.id("suburb")).sendKeys(city);

			//Input the postcode
			driver.findElement(By.id("postcode")).sendKeys(postCode);

		}
	}
	
	
	Map<String, String> funcClientEntryPg(String fName, String lName, String countryName, String phNumber, String email) throws Exception
	{
		
			Map<String, String> countryInfo = new HashMap<String, String>();
			String cimaprodTempCountry = "";
			Select t=null;

			//Input First Name
			driver.findElement(By.id("firstName")).clear();
			driver.findElement(By.id("firstName")).sendKeys(fName);
			
			//Input Last Name
			driver.findElement(By.id("lastName")).clear();
			driver.findElement(By.id("lastName")).sendKeys(lName);

	        //Input Country (set to UK for ID3 and world check verification)
				//Added by Yanni: when Brand = ky and TestEnv = prod, country control is not a Select control. Need to handle seperately for choosing country.
			if(!(Brand.equalsIgnoreCase("ky") && TestEnv.equalsIgnoreCase("prod")))
			{
				t = new Select(driver.findElement(By.id("country")));
			}
						
			//If countryName as a parameter is empty, then select based on common rules,.
			//If countryName as a parameter is not empty, then select based on countryName. This is actually used for duplicate phone number verification.
		
		
			if(!(Brand.equalsIgnoreCase("ky")&&TestEnv.equalsIgnoreCase("prod")))
			{
				t.selectByValue(countryName);
				countryInfo.put("country", t.getFirstSelectedOption().getText().trim());
			}else
			{
				driver.findElement(By.id("country")).click();
				Thread.sleep(500);
				
				cimaprodTempCountry= registerInst.funcCIMAProdChooseCountry(driver, countryName);
				countryInfo.put("country", cimaprodTempCountry);
			}
			
			/*
			 * //Log selected country name
			 * Thread.sleep(500);
			 * if(!(Brand.equalsIgnoreCase("ky")&&TestEnv.equalsIgnoreCase("prod")))
			 * {
			 * countryInfo.put("country", t.getFirstSelectedOption().getText().trim());
			 * }else
			 * {
			 * countryInfo.put("country", cimaprodTempCountry);
			 * }
			 */
			
			//Input phone number
			driver.findElement(By.id("phone")).clear();
			driver.findElement(By.id("phone")).sendKeys(phNumber);		
			
			//Input email
			driver.findElement(By.id("email")).clear();
			driver.findElement(By.id("email")).sendKeys(email);
				
			//Log selected country name and code
			if (TestEnv.equalsIgnoreCase("prod") && Brand.equalsIgnoreCase("au"))
				countryInfo.put("code", driver.findElement(By.id("phoneCode")).getText().trim());
			else
			{
				t = new Select(driver.findElement(By.id("phoneCode")));
				countryInfo.put("code", t.getFirstSelectedOption().getText().trim());
			}
			
			driver.findElement(By.id("button")).click();
			Thread.sleep(2000);
		
		//Thread.sleep(1000);
		//Check DB status 
		System.out.println("DB status After Entry page submission:");
		DBUtils.checkDBStatus(fName, TestEnv, Brand);
		
		if(TestEnv.equalsIgnoreCase("Beta")) {
			Thread.sleep(5000);
		}
		
		System.out.println("countryInfo: " + countryInfo);
		
		return countryInfo;
	}
	
	void funcVerifyBringInValue(WebElement txtControl, String expectValue)
	{
		registerInst.funcVerifyBringInValue(txtControl, expectValue);
	}
	
	void clickPgNextBackButton(String Brand, Boolean isBack) throws Exception
	{
		WebElement nextButton = null;
		WebElement backButton = null;
		
		if (Brand.equalsIgnoreCase("vt")) {
			nextButton = driver.findElement(By.xpath("//button[@class='el-button btn_next btn el-button--default']"));
			
		}else {
			nextButton = driver.findElement(By.xpath("//button[@class='el-button btn_blue el-button--default']")); 
			
		}
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
		Thread.sleep(1000);
		
		if(isBack == false)
		{
			//Click Next
			nextButton.click();
		}else
		{
			if (Brand.equalsIgnoreCase("vt")) {
				backButton = driver.findElement(By.xpath("//button[@class='el-button btn_back btn_default btn el-button--default']")); 

			}else {
				backButton = driver.findElement(By.xpath("//button[@class='el-button btn_default el-button--default']")); 
				
			}
			//Click Back
			backButton.click();
		}
		Thread.sleep(1000);	

	}
	
	
	
	
	
	
/*	@Test
	public void testFunction() throws Exception
	{
		String userID="10000257";
		String TestEnv = "test";
		String Brand = "pug";
		
		funcReadUserInGlobalDB(userID, TestEnv, Brand);
		
		funcUpdateID3Flag(userID, true, TestEnv, Brand);
		
		funcUpdateWldCkFlag(userID, true, TestEnv, Brand);
		
		funcReadUserInGlobalDB(userID, TestEnv, Brand);
		
		funcUpdateID3Flag(userID, false, TestEnv, Brand);
		
		funcUpdateWldCkFlag(userID, false, TestEnv, Brand);		
		funcReadUserInGlobalDB(userID, TestEnv, Brand);
	}*/
	
	void funcDropdownSelectRandomOption() throws Exception
	{
		registerInst.funcDropdownSelectRandomOption();
	}
}
