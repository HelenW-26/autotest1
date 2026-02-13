package vantagecrm;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
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
import java.lang.reflect.Method;
import net.bytebuddy.utility.RandomString;
import adminBase.ClientSearch;
/*
 * This class is to test 
 * --Register User from Official website, 
 * --Add clientsfrom Admin
 * --Add IB from Admin
 * --Add Internal Users from Admin
 */

public class UserManagement {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String userName; //Client Name
	String IBfName, IBlName;  //IB Name
	String ibPwd="123Qwe";
	String internalUserName; //Internal user name;
	String emailSuffix=Utils.emailSuffix;
	Random tRandom=new Random();
	WebDriverWait wait03;
	WebDriverWait wait15;
	CPRegister registInst = new CPRegister();
	
	Map <String, String> applicantNames;
	Map <String, String> applicant2Names;
	
	enum Currency {
		USD, AUD, GBP, EUR,	SGD, JPY, NZD, CAD;
		
		public static Currency getRandom() {
			Random r=new Random();
			
			return values()[r.nextInt(values().length)];
		}
	}

	enum AccountT {
		RAW, STP;
		
		public static AccountT getRandom() {
			Random r=new Random();
			
			return values()[r.nextInt(values().length)];
		}
	}
		

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		  
		
		  String browserName= "chrome";
		  System.out.println("Browser is: " + browserName);
		  System.out.println("Thread Id is: " + Thread.currentThread().getId());
		  
		  switch (browserName)
		  {
		  case "chrome":
		  
			  WebDriverManager.chromedriver().setup();
			  ChromeOptions options = new ChromeOptions();
			  options.addArguments("--no-sandbox");
			  options.setAcceptInsecureCerts(true);
			  options.setExperimentalOption("useAutomationExtension", false);
			  
			  if(Boolean.valueOf(headless)) {
					options.addArguments("window-size=1920,1080");
					options.addArguments("headless");
				}
			  driver = new ChromeDriver(options);
			  
			  break;
		  
		  case "firefox":
			  WebDriverManager.firefoxdriver().setup();
			  driver = new FirefoxDriver();
			  break;
		  case "edge":
			  WebDriverManager.edgedriver().setup();
			  driver = new EdgeDriver();
			  break;
		  case "safari":
			  driver = new SafariDriver();
			  break;
		  }
		 
		
		  /*
		  ChromeOptions options=new ChromeOptions();
		  options.setAcceptInsecureCerts(true);
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
    	  driver = new ChromeDriver(options);	
    	  */
    	  utils.Listeners.TestListener.driver=driver;
    	  context.setAttribute("driver", driver);      //Added by Yanni: to pass driver to Listener
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait03=new WebDriverWait(driver,Duration.ofSeconds(3));
		  wait15=new WebDriverWait(driver, Duration.ofSeconds(15));
		  
		  applicantNames = new HashMap <String, String>();
		  applicant2Names = new HashMap <String, String>();
	}
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		
		Utils.funcLogOutAdmin(driver);
		//Close all browsers
		driver.quit();
	}

	//Login Admin with credentials

	@Parameters({"AdminURL","AdminName","AdminPass","Brand"})
	@Test(priority=0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand, Method method) throws Exception
	{
	
		//Login AU admin
		ExtentTestManager.startTest(method.getName(),"Description: Login to Admin Portal");
		
		driver.get(AdminURL);	
		Thread.sleep(1000); 
		Utils.funcLogInAdmin(driver, AdminName,AdminPass, Brand);
	}
	
	//Add a new individual user
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv"})
	public void AddClient(String Brand, String TestEnv, ITestContext context, Method method) throws Exception
	{
		funcGo2Client(method);
		funcApplicantInfo(Brand,false, context);
		
		funcSubmitApplicant(Brand);		
	   //Read Data to test migration
		funcCheckDB(Brand,TestEnv,false);
	}
	
	//Add a new joint user
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv"})
	public void AddJointClient(String Brand, String TestEnv, ITestContext context,Method method) throws Exception
	{
		funcGo2Client(method);
				
		funcApplicantInfo(Brand,true, context);
		funcApplicant2Info(Brand);
		
		funcSubmitApplicant(Brand);		
	   //Read Data to test migration
		funcCheckDB(Brand,TestEnv,true);
	}
	
	public void funcGo2Client(Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: Add Client");
		
		driver.findElement(By.linkText("Client Management")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Client")).click();
		driver.findElement(By.id("addCustomer")).click();
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.panel-heading")).getText(),"Add Accounts");
		
	
		Thread.sleep(waitIndex*1000);
	}
	public void funcCheckDB(String Brand, String TestEnv, boolean jointFlag) throws Exception
	{
	
		String userName2="";
		/*
		 * Pass userName to the variable in Utils. 
		 * If Brand is KY/AISC...whose full name  follows firstname + middle name + last name , pass full name, otherwise
		 * Pass only firstname. 
		 */ 
		
		switch(Brand)
		{
		
		case "ky":
		case "au":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":
			
			//Get 1st applicant user name
			userName=applicantNames.get("firstName")+" "+applicantNames.get("midName")+" "+applicantNames.get("lastName");
			
			//If it is a joint, get 2nd applicant user name
			if (jointFlag) {
				userName2=applicant2Names.get("firstName")+" "+applicant2Names.get("midName")+" "+applicant2Names.get("lastName");
			}
		
			break;
			
		//PUG and VT use Last Name + First Name, when doing DB search, it can't match when the order is not right. Only store FirstName
		case "svg":
		case "fsa":
		case "vt":
			//Get 1st applicant user name
			userName=applicantNames.get("firstName");
			
			//If it is a joint, get 2nd applicant user name
			if (jointFlag) {
				userName2=applicant2Names.get("firstName");
			}
			
			break;
			
			default:
				System.out.println("UserManagement - > funcCheckDB doesn't support Brand: " + Brand);
	
		}
		
		if(jointFlag)
			Utils.addJointName=userName;
		else
			Utils.addClientName=userName;
						
		//Read Data to test migration
		
	   String userID = DBUtils.checkDBStatus(userName, TestEnv, Brand);
	   	
       if (jointFlag) {
    	   System.out.println("======>>>>The second user info in joint account:");
   
    	   DBUtils.checkDBStatus(userName2, TestEnv, Brand);
	   }
	   
       System.out.println("Check ID record in DB...");
       registInst.funcGetIDRecord(userID, TestEnv, Brand);  //Need to comment out when ID function is ready

       System.out.println();
       System.out.println("Check World Check record in DB...");
       registInst.funcGetWorldCheck(userID, TestEnv, Brand);         //Need to comment out when ID function is ready

       System.out.println();
       System.out.println("Check POA record in DB...");
       registInst.funcGetPOARecord(userID, TestEnv, Brand);
	}
	
	public void funcSubmitApplicant(String Brand) {
		//Submit
		wait03.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.pull-left.popModalSub"))).click();;
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		

		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		Assert.assertEquals(resultMsg, "add success");
						
	   try
	   {
	    wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));	
	
	   } catch (TimeoutException e)
	   {
		   System.out.println("List is already loaded.");
	   }
	
	}
	public void funcApplicantInfo(String Brand, boolean jointFlag, ITestContext context) throws Exception
	{
		int j=0;
		String firstName = "",lastName = "",midName = "";
		Select t;
		//Input the required information
		Random r=new Random();
					
		//Input username; 
				
		// ASIC doesn't have column "title"
		 if(!Brand.equalsIgnoreCase("au"))
		 {
			t=new Select(driver.findElement(By.id("acc_title")));
			Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
		 }
		firstName=Utils.addUserPrefix+Utils.randomNumber(3)+Utils.randomString(3);
		lastName=Utils.randomString(3);
		midName="MName" + Utils.randomString(3);
		applicantNames.put("firstName", firstName);
		applicantNames.put("lastName", lastName);
		applicantNames.put("midName", midName);
		
		//input first name and last name
		driver.findElement(By.id("acc_first_name")).clear();
		driver.findElement(By.id("acc_first_name")).sendKeys(firstName);
		
		driver.findElement(By.id("acc_last_name")).clear();
		driver.findElement(By.id("acc_last_name")).sendKeys(lastName);
		
		driver.findElement(By.id("acc_middle_name")).clear();
		driver.findElement(By.id("acc_middle_name")).sendKeys(midName);
		
		//This is for input email 
		userName=firstName;
				
				
		
		//Input email and password
		driver.findElement(By.id("email")).sendKeys(userName.toLowerCase()+emailSuffix);
		driver.findElement(By.id("password")).sendKeys("123Qwe");
		
		//Yanni on 18/05/2020: set email in testcontext for later use
		context.setAttribute("TraderName", userName.toLowerCase()+emailSuffix);
		
		//Input Mobile
		Select mobileCode = new Select(driver.findElement(By.name("mobile_code")));
		mobileCode.selectByIndex(r.nextInt(20)+1);
		driver.findElement(By.id("mobile")).sendKeys(Integer.toString(r.nextInt(10000)));
				
		//Australia can be in the dropdown list if Brand is ky
		Select country=new Select(driver.findElement(By.id("country")));

		//Input Country of Residence
		if (Brand.equalsIgnoreCase("au")) {
			country.selectByValue("3512");
		}else {
			country.selectByIndex(r.nextInt(country.getOptions().size()-1)+1);
		}
				
		//Iraq is a banned country so can't be used as registration
		if(country.getFirstSelectedOption().getText().equals("Iraq") || country.getFirstSelectedOption().getText().equals("Afghanistan")|| country.getFirstSelectedOption().getText().equals("Syria"))
		{
			country.selectByIndex(51);
		}
		
		//Nationality
		Select nationality=new Select(driver.findElement(By.id("ib_Nationality")));
		nationality.selectByIndex(r.nextInt(nationality.getOptions().size()-1)+1);
		
		//Input place of birth
		Select placeofBirth=new Select(driver.findElement(By.id("place_of_birth")));
		placeofBirth.selectByIndex(r.nextInt(placeofBirth.getOptions().size()-1)+1);
		
		//Input ID type and Number
		if (!Brand.equalsIgnoreCase("au") ) {
			t=new Select(driver.findElement(By.id("acc_id_type")));
			Assert.assertTrue(t.getOptions().size()==4);  //Have 4 options
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
			
			driver.findElement(By.id("acc_id_num")).sendKeys(Utils.randomString(3)+Utils.randomNumber(15));	

			if (Brand.equalsIgnoreCase("fca"))
			{
				driver.findElement(By.id("national_insurance_number")).sendKeys(Utils.randomString(3)+Utils.randomNumber(15));	
				
			}
		}
		
		//Input address infor
		driver.findElement(By.id("street")).sendKeys(Utils.randomNumber(2)+ " " + Utils.randomString(5).toUpperCase() +" Ave");
		driver.findElement(By.id("suburb")).sendKeys("Gordon");
		
		if (country.getFirstSelectedOption().getText().equals("Australia")) {
			t=new Select(driver.findElement(By.id("state")));
			t.selectByIndex(1);
		}else {
			driver.findElement(By.id("inputState1")).sendKeys("NSW");
		}
		driver.findElement(By.id("indi_postcode")).sendKeys("2172");
					
		//Input Client Type	
		Select clientT=new Select(driver.findElement(By.id("account_type")));
		if (jointFlag)
			clientT.selectByVisibleText("Joint");
		else
			clientT.selectByIndex(r.nextInt(clientT.getOptions().size()-1));
				
		//Input Account Type
		Select accountT=new Select(driver.findElement(By.id("mt4_account_type")));
		//accountT.selectByIndex(r.nextInt(3)+1);
		accountT.selectByIndex(r.nextInt(2)+1);
		
		//Input Currency
		Select currency=new Select(driver.findElement(By.id("currency")));
		j=r.nextInt(currency.getOptions().size()-1)+1;
		currency.selectByIndex(j);
		
		//Input Industry
		switch(Brand)
		{
			case "au":
			/*case "vt":*/   //Yanni: VT won't have Industry after migrating to new look 
			    Select industry=new Select(driver.findElement(By.id("occupation")));
			    j=r.nextInt(industry.getOptions().size()-1)+1;
			    industry.selectByIndex(j);
			    
			    if(industry.getFirstSelectedOption().getText().equals("Other"))
				{
					driver.findElement(By.id("op_other")).sendKeys("Test Industry "+j);
				}
				
				break;
				
				
		}
		
		if (!Brand.equalsIgnoreCase("fca"))
		{
			
			//Employment status
			Select employment=new Select(driver.findElement(By.id("ib_Mployment")));
			j=r.nextInt(employment.getOptions().size()-1)+1;
			employment.selectByIndex(j);
			
			//Annual Income 
			Select aIncome=new Select(driver.findElement(By.id("income")));
			j=r.nextInt(aIncome.getOptions().size()-1)+1;
			aIncome.selectByIndex(j);
			
			//Approximate Net Worth
			Select saving=new Select(driver.findElement(By.id("ib_Sinvest")));
			j=r.nextInt(saving.getOptions().size()-1)+1;
			saving.selectByIndex(j);
					
			//Source of Funds
			Select funds=new Select(driver.findElement(By.id("ib_funds")));
	
			if (!Brand.equalsIgnoreCase("au") ) {
				Assert.assertTrue(funds.getOptions().size()==12,"Source of Funds should have 11 options in ky");  //Have 11 options
			}else {
				Assert.assertTrue(funds.getOptions().size()==5,"Source of Funds should have 5 options in non-ky env");  //Have 5 options
			}
			j=r.nextInt(funds.getOptions().size()-1)+1;
			funds.selectByIndex(j);
			
			if(funds.getFirstSelectedOption().getText().equals("Other"))
			{
				driver.findElement(By.id("funds_other")).sendKeys("Other Source " + j);
			}
		}
		
	    //US citizen

		if (!Brand.equalsIgnoreCase("au")) {
			t=new Select(driver.findElement(By.id("acc_tax_us")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
		}
		
		//Web Source
		Select wSource=new Select(driver.findElement(By.id("websource")));
		if (wSource.getOptions().size()==1) {
			j=0;
		}else {
			j=r.nextInt(wSource.getOptions().size()-1)+1;
		}
		wSource.selectByIndex(j);
		
		//non-ASIC special requirement

		if (!Brand.equalsIgnoreCase("au") && !Brand.equalsIgnoreCase("fca")) {
			//Expected Deposit
			t=new Select(driver.findElement(By.id("acc_invest_deposit")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
			
			//Intended Trade Value (Weekly)
			t=new Select(driver.findElement(By.id("acc_investmentexp_amount_tradew")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
			
			//Intended Trade Volume (Weekly)
			t=new Select(driver.findElement(By.id("acc_investmentexp_tradew")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
						
		}
			
		//Input identity Proof 1
		driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']")).sendKeys(Utils.workingDir+"\\proof.png");
		
		//Click Upload button and then upload 2nd file. This is not applicable to ASIC
		if(!Brand.equalsIgnoreCase("au"))
		{
			driver.findElement(By.cssSelector("div.col-md-6.individual_upload_pass span a")).click();
			
			//Input identity Proof 2
			Thread.sleep(1000);
			driver.findElements(By.xpath("//input[@type='file' and @data-name='passport']")).get(1).sendKeys(Utils.workingDir+"\\proof2.png");
			driver.findElements(By.cssSelector("div.col-md-6.individual_upload_pass span a")).get(1).click();		
		}	
		Thread.sleep(1000);
		
		//Input Address Proof
		driver.findElement(By.xpath("//input[@type='file' and @data-name='bankStatement']")).sendKeys(Utils.workingDir+"\\proof.png");
		//For non-asic regulators, click Upload button
		if(!Brand.equalsIgnoreCase("au"))
		{
			driver.findElement(By.cssSelector("div.col-md-6.individual_upload_bank span a")).click();
		}
		Thread.sleep(1000);
		
		if (Brand.equalsIgnoreCase("fca"))
		{
			funcFCAFinancialFirst();
		}
		
		
		
	}
	
	public void funcFCAFinancialFirst()
	{
		Select t=null;
		int j=0;
		Random r=new Random();
		//display Compliance Section
		driver.findElement(By.id("first-button")).click();
		
		//Employment Status
		t=new Select(driver.findElement(By.id("EmploymentAnswersfirst1")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Occupation
		t=new Select(driver.findElement(By.id("EmploymentAnswersfirst2")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Employment Sector
		t=new Select(driver.findElement(By.id("EmploymentAnswersfirst3")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Annual Income
		t=new Select(driver.findElement(By.id("EmploymentAnswersfirst4")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
				
		//Estimated Size of Investment Portfolio
		t=new Select(driver.findElement(By.id("EmploymentAnswersfirst5")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//How Do You Intend to Fund Your Account?
		t=new Select(driver.findElement(By.id("TradingAnswersfirst6")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//What Type of Securities Do You Intend to Trade?
		t=new Select(driver.findElement(By.id("TradingAnswersfirst7")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Expected Initial Deposit into Investment Account
		t=new Select(driver.findElement(By.id("TradingAnswersfirst8")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Expected Average Daily Trade Value
		t=new Select(driver.findElement(By.id("TradingAnswersfirst9")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
				
		//Shares
		t=new Select(driver.findElement(By.id("TradingAnswersfirst10")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Spot FX
		t=new Select(driver.findElement(By.id("TradingAnswersfirst11")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Equity derivatives
		t=new Select(driver.findElement(By.id("TradingAnswersfirst12")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//FX CFDs
		t=new Select(driver.findElement(By.id("TradingAnswersfirst13")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Have you worked in a financial services institution 
		t=new Select(driver.findElement(By.id("TradingAnswersfirst14")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
				
		//Have you received education or on-the-job training on the instruments
		t=new Select(driver.findElement(By.id("TradingAnswersfirst15")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
	}
	public void funcApplicant2Info(String Brand) throws Exception
	{
		int j=0;
		String firstName2 = "",lastName2 = "",midName2 = "";
		Select t;
		Random r=new Random();	
				
		
		  if (!Brand.equalsIgnoreCase("au") )
		  {
		 
			t=new Select(driver.findElement(By.id("second_acc_title")));
			Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
		  }
				
			firstName2=Utils.addUserPrefix+Utils.randomString(3);
			lastName2=Utils.randomString(3);
			midName2="MName" + Utils.randomString(3);
			
			applicant2Names.put("firstName", firstName2);
			applicant2Names.put("lastName", lastName2);
			applicant2Names.put("midName", midName2);
			
			//input first name and last name
			driver.findElement(By.id("second_acc_first_name")).clear();
			driver.findElement(By.id("second_acc_first_name")).sendKeys(firstName2);
				
			driver.findElement(By.id("second_acc_middle_name")).clear();
			driver.findElement(By.id("second_acc_middle_name")).sendKeys(midName2);
			
			driver.findElement(By.id("second_acc_last_name")).clear();
			driver.findElement(By.id("second_acc_last_name")).sendKeys(lastName2);
				
		
		//Input email and password
		driver.findElement(By.id("second_email")).sendKeys(applicant2Names.get("firstName").toLowerCase()+emailSuffix);
			
		//Input Mobile
		t = new Select(driver.findElement(By.name("second_mobile_code")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		driver.findElement(By.id("second_mobile")).sendKeys(Integer.toString(r.nextInt(10000)));
		
		//Input address infor
		driver.findElement(By.id("second_street")).sendKeys(Utils.randomNumber(2)+ " "+Utils.randomString(4).toUpperCase() +" Ave");
		driver.findElement(By.id("second_suburb")).sendKeys("Gordon");			
		
		//Input Country of Residence
		Select country=new Select(driver.findElement(By.cssSelector("select#second_country")));
		country.selectByIndex(r.nextInt(country.getOptions().size()-1)+1);
		
		//Iraq is a banned country so can't be used as registration
		if(country.getFirstSelectedOption().getText().equals("Iraq") || country.getFirstSelectedOption().getText().equals("Afghanistan") ||country.getFirstSelectedOption().getText().equals("United States") )
		{
			country.selectByVisibleText("Italy");
		}
		
		//dropdown state for Australia while input state for other countries
		if (country.getFirstSelectedOption().getText().equals("Australia")) {
			t=new Select(driver.findElement(By.id("second_state")));
			t.selectByIndex(1);
		}else {
			driver.findElement(By.id("second_state_input")).sendKeys("NSW");
		}
		
		driver.findElement(By.cssSelector("input#second_postcode")).sendKeys("2172");
			
		//Nationality
		Select nationality=new Select(driver.findElement(By.id("second_Nationality")));
		nationality.selectByIndex(r.nextInt(nationality.getOptions().size()-1)+1);
		
		//US citizen

		if (!Brand.equalsIgnoreCase("au") ) {
			t=new Select(driver.findElement(By.id("second_acc_tax_us")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
		}
		
		//Input ID type and Number

		if (!Brand.equalsIgnoreCase("au") ) {
			t=new Select(driver.findElement(By.id("second_acc_id_type")));
			Assert.assertTrue(t.getOptions().size()==4);  //Have 4 options
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
			
			driver.findElement(By.id("second_acc_id_num")).sendKeys(Utils.randomString(3)+Utils.randomNumber(15));	
		}
		
		//Input Industry
		switch(Brand)
		{
			case "au":
			/*case "vt":*/
					
			    Select industry=new Select(driver.findElement(By.id("second_occupation")));
			    j=r.nextInt(industry.getOptions().size()-1)+1;
			    industry.selectByIndex(j);
			    
			    if(industry.getFirstSelectedOption().getText().equals("Other"))
				{
					driver.findElement(By.id("second_op_other")).sendKeys("Test Industry "+j);
				}
			    
				break;
								
		}
		
		if ( !Brand.equalsIgnoreCase("fca"))
		{
			
			//Employment status
			Select employment=new Select(driver.findElement(By.id("second_Mployment")));
			j=r.nextInt(employment.getOptions().size()-1)+1;
			employment.selectByIndex(j);
						
			//Annual Income 
			Select aIncome=new Select(driver.findElement(By.id("second_income")));
			j=r.nextInt(aIncome.getOptions().size()-1)+1;
			aIncome.selectByIndex(j);
				
			//Saving and Invenstment
			Select saving=new Select(driver.findElement(By.id("second_Sinvest")));
			j=r.nextInt(saving.getOptions().size()-1)+1;
			saving.selectByIndex(j);
					
			//Source of Funds
		
			Select funds=new Select(driver.findElement(By.id("second_funds")));
	
			if (!Brand.equalsIgnoreCase("au") ) {
				Assert.assertTrue(funds.getOptions().size()==12,"Source of Funds should have 12 options in ky");  //Have 11 options
			}else {
				Assert.assertTrue(funds.getOptions().size()==5,"Source of Funds should have 5 options in non-ky env");  //Have 5 options
			}
			j=r.nextInt(funds.getOptions().size()-1)+1;
			funds.selectByIndex(j);
			
			if(funds.getFirstSelectedOption().getText().equals("Other"))
			{
				driver.findElement(By.id("second_funds_other")).sendKeys("Other Source " + j);
			}
		}
		
		//Non-ASIC special fields
		if (!Brand.equalsIgnoreCase("au") && !Brand.equalsIgnoreCase("fca")) {
			//Expected Deposit
			t=new Select(driver.findElement(By.id("second_acc_invest_deposit")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
			
			//Intended Trade Value (Weekly)
			t=new Select(driver.findElement(By.id("second_acc_investmentexp_amount_tradew")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
			
			//Intended Trade Volume (Weekly)
			t=new Select(driver.findElement(By.id("second_acc_investmentexp_tradew")));
			j=r.nextInt(t.getOptions().size()-1);
			t.selectByIndex(j+1);
		}
		
		//Input identity Proof 1
		
		driver.findElement(By.xpath("//input[@type='file' and @data-name='joint_passport']")).sendKeys(Utils.workingDir+"\\proof.png");
		
		if(!Brand.equalsIgnoreCase("au"))
		{
			driver.findElement(By.cssSelector("div.col-md-6.joint_upload_pass div span a")).click();
			
			//Input identity Proof 2 (Yanni on 01/06/2020 for 2 identity files)
			Thread.sleep(1000);
			driver.findElements(By.xpath("//input[@type='file' and @data-name='joint_passport']")).get(1).sendKeys(Utils.workingDir+"\\proof2.png");
			driver.findElements(By.cssSelector("div.col-md-6.joint_upload_pass div span a")).get(1).click();
		}
		
		Thread.sleep(1000);
		
		//Input Address Proof
		driver.findElement(By.xpath("//input[@type='file' and @data-name='joint_bankStatement']")).sendKeys(Utils.workingDir+"\\proof.png");
		
		if(!Brand.equalsIgnoreCase("au"))
		{
			driver.findElement(By.cssSelector("div.col-md-6.joint_upload_bank div span a")).click();
		}
		
		Thread.sleep(1000);

		if (Brand.equalsIgnoreCase("fca"))
		{
			funcFCAFinancial2nd();
		}
		
		
		
	}
	
	public void funcFCAFinancial2nd()
	{
		Select t=null;
		int j=0;
		Random r=new Random();
		//display Compliance Section
		driver.findElement(By.id("second-button")).click();
				
		//Employment Status
		t=new Select(driver.findElement(By.id("EmploymentAnswerssecond1")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Occupation
		t=new Select(driver.findElement(By.id("EmploymentAnswerssecond2")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Employment Sector
		t=new Select(driver.findElement(By.id("EmploymentAnswerssecond3")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Annual Income
		t=new Select(driver.findElement(By.id("EmploymentAnswerssecond4")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
				
		//Estimated Size of Investment Portfolio
		t=new Select(driver.findElement(By.id("EmploymentAnswerssecond5")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//How Do You Intend to Fund Your Account?
		t=new Select(driver.findElement(By.id("TradingAnswerssecond6")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//What Type of Securities Do You Intend to Trade?
		t=new Select(driver.findElement(By.id("TradingAnswerssecond7")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Expected Initial Deposit into Investment Account
		t=new Select(driver.findElement(By.id("TradingAnswerssecond8")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Expected Average Daily Trade Value
		t=new Select(driver.findElement(By.id("TradingAnswerssecond9")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
				
		//Shares
		t=new Select(driver.findElement(By.id("TradingAnswerssecond10")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Spot FX
		t=new Select(driver.findElement(By.id("TradingAnswerssecond11")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Equity derivatives
		t=new Select(driver.findElement(By.id("TradingAnswerssecond12")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//FX CFDs
		t=new Select(driver.findElement(By.id("TradingAnswerssecond13")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
					
		//Have you worked in a financial services institution 
		t=new Select(driver.findElement(By.id("TradingAnswerssecond14")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
				
		//Have you received education or on-the-job training on the instruments
		t=new Select(driver.findElement(By.id("TradingAnswerssecond15")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
	}
	
	//Add a new external user
	@Test (invocationCount=1,alwaysRun=true)
	@Parameters({"AdminURL", "Brand", "TestEnv"})
	public void AddIB(String AdminURL,String Brand, String TestEnv, ITestContext context, Method method) throws Exception
	{
	      
     String IBfName, IBlName;
	 IBfName=Utils.ibUserPrefix+Utils.randomString(3);
	 IBlName=Utils.randomString(3).toUpperCase();
  
     Thread.sleep(waitIndex*1000);
      
     ExtentTestManager.startTest(method.getName(),"Description: Add IB");
      
     driver.navigate().to(AdminURL);
	      
	      
	  driver.findElement(By.linkText("Users")).click();
	  driver.findElement(By.linkText("External user")).click();
	  driver.findElement(By.id("button1")).click();
	  
	  Thread.sleep(1000);
	  Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"Add new user");
	      
	  //Input the required information
	  Random r=new Random();
	  int index=r.nextInt(100);
	  
	  //Input username
	  IBfName=Utils.ibUserPrefix+Utils.randomString(3);

	//Input First Name
	driver.findElement(By.id("acc_first_name")).clear();
	driver.findElement(By.id("acc_first_name")).sendKeys(IBfName);

	if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("svg")&& !Brand.equalsIgnoreCase("fsa")) {
		//Input middle name
		String IBmName="MName" + Utils.randomString(3);
		driver.findElement(By.id("acc_middle_name")).clear();
		driver.findElement(By.id("acc_middle_name")).sendKeys(IBmName);
	}
	
	//Input Last Name
	driver.findElement(By.id("acc_last_name")).clear();
	driver.findElement(By.id("acc_last_name")).sendKeys(IBlName);
	  
	  
	  
	  //Input email address
	  driver.findElement(By.id("ib_email")).sendKeys(IBfName+emailSuffix);
	  context.setAttribute("TraderName", IBfName+emailSuffix);
	      
	  //Input Mobile
	  Select mobileCode = new Select(driver.findElement(By.name("mobile_code")));
	  mobileCode.selectByIndex(r.nextInt(mobileCode.getOptions().size()-1)+1);
	  driver.findElement(By.id("ib_Mobile")).sendKeys(Integer.toString(r.nextInt(10000)));
	  
	  //Input Country of Residence
	  Select country=new Select(driver.findElement(By.id("ib_Country")));
	  if (Brand.equalsIgnoreCase("au")) {
		country.selectByValue("3512");
	  }else {
		country.selectByIndex(r.nextInt(country.getOptions().size()-1)+1);
	  }
	  
	  //Iraq is a banned country so can't be used as registration
	  if(country.getFirstSelectedOption().getText().equals("Iraq") || country.getFirstSelectedOption().getText().equals("Afghanistan") || country.getFirstSelectedOption().getText().equals("United States"))
	  {
	    country.selectByVisibleText("Australia");;
	  }
	  
	  //Input Nationality
	  Select nationality=new Select(driver.findElement(By.id("ib_Nationality")));
	  nationality.selectByIndex(r.nextInt(nationality.getOptions().size()-1)+1);
	  
	  //Input address infor
	  driver.findElement(By.id("ib_street_adress")).sendKeys(index+" Pearl Ave");
	  driver.findElement(By.id("ib_Suburb")).sendKeys(Utils.randomString(4));
	  //driver.findElement(By.id("inputState")).sendKeys("NSW");
	  if (country.getFirstSelectedOption().getText().equals("Australia")) {
		 Select t=new Select(driver.findElement(By.id("ib_State")));
		 t.selectByIndex(1);
	  }else {
		 driver.findElement(By.id("inputState")).sendKeys("NSW");
	  }
	  driver.findElement(By.id("ib_Postcode")).sendKeys(Utils.randomNumber(4));
	  
	  
	  //Input Account Type 1 (Individual /Company)
	  Select accountT1=new Select(driver.findElement(By.id("ib_Account_type")));
	  accountT1.selectByIndex(r.nextInt(accountT1.getOptions().size()));
	  
	  //Input password
	  driver.findElement(By.id("ib_password")).sendKeys("123Qwe");
	  
	  
	  //Input Industry
	  Select industry=new Select(driver.findElement(By.id("occupation")));
	  int j=r.nextInt(industry.getOptions().size()-1)+1;
	  industry.selectByIndex(j);
	  
	  if(industry.getFirstSelectedOption().getText().equals("Other"))
	  {
	    driver.findElement(By.id("op_other")).sendKeys("Test Industry "+j);
	  }
	  
	  
	  //Employment status

	  Select employment=new Select(driver.findElement(By.id("ib_Mployment")));
	  j=r.nextInt(employment.getOptions().size()-1)+1;
	  employment.selectByIndex(j);
	  

	  
	  //Annual Income 

	  Select aIncome=new Select(driver.findElement(By.id("income")));
	  j=r.nextInt(aIncome.getOptions().size()-1)+1;
	  aIncome.selectByIndex(j);
	  

	  //Saving and Invenstment

	  Select saving=new Select(driver.findElement(By.id("ib_Sinvest")));
	  j=r.nextInt(saving.getOptions().size()-1)+1;
	  saving.selectByIndex(j);
	  
	  
	  //Source of Funds

	  Select funds=new Select(driver.findElement(By.id("ib_funds")));
	  j=r.nextInt(funds.getOptions().size()-1)+1;
	  funds.selectByIndex(j);
	  
	  if(funds.getFirstSelectedOption().getText().equals("Other"))
	  {
	    driver.findElement(By.id("funds_other")).sendKeys("Other Source " + j);
	  }
	  
	  
	  //Who referred you to us:
	  driver.findElement(By.id("ib_referto")).sendKeys(RandomString.make(5));
	  
	  
	  //Input Account Type 2 (MT4 Account type)
	  Select accountT2=new Select(driver.findElement(By.id("ib_accountType")));
	  accountT2.selectByIndex(1);
	  
	  //How many clients does the IB currently have?

	  Select numberofClients=new Select(driver.findElement(By.id("ib_clients")));
	  j=r.nextInt(numberofClients.getOptions().size()-1)+1;
	  numberofClients.selectByIndex(j);
	  
	  //IB working with other brokers?
	  j=r.nextInt(2);
	  driver.findElements(By.name("ib_relation")).get(j).click();
	  if(j==0)
	  {
	    driver.findElement(By.id("ib_rela_broker")).sendKeys(RandomString.make(5) + ""+RandomString.make(3));
	  }
	  
	  //How many clients does the IB expect to introduce each month
	  Select noNewClients=new Select(driver.findElement(By.id("ib_roped")));
	  j=r.nextInt(noNewClients.getOptions().size()-1)+1;
	  noNewClients.selectByIndex(j);
	  
	  //Average monthly trading volume
	  Select tradeVol=new Select(driver.findElement(By.id("ib_trading_volume")));
	  j=r.nextInt(tradeVol.getOptions().size()-1)+1;
	  tradeVol.selectByIndex(j);
	  
	  //IB working with other brokers?
	  j=r.nextInt(2);
	  driver.findElements(By.name("ib_web_relation")).get(j).click();
	  if(j==0)
	  {
	    driver.findElement(By.id("ib_website")).sendKeys("http://www."+RandomString.make(5) + "."+RandomString.make(3));
	  }
	  
	  //Input identity Proof 1
	      
	  driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']")).sendKeys(Utils.workingDir+"\\proof.png");
	  driver.findElement(By.cssSelector("div.col-md-6.individual_upload div.col-md-12.input-box span a")).click();
	  
	  //Input identity Proof 2: (Yanni on 01/06/2020 for uploading 2nd identity proof)
	  Thread.sleep(1000);
	  driver.findElements(By.xpath("//input[@type='file' and @data-name='passport']")).get(1).sendKeys(Utils.workingDir+"\\proof.png");
	  driver.findElements(By.cssSelector("div.col-md-6.individual_upload div.col-md-12.input-box span a")).get(1).click();
	  
	  //Input Address Proof
	  Thread.sleep(1000);
	  driver.findElement(By.xpath("//input[@type='file' and @data-name='bankStatement']")).sendKeys(Utils.workingDir+"\\proof.png");
	  driver.findElement(By.cssSelector("div.col-md-6.individual_upload_address div.col-md-12.input-box span a")).click();
	  Thread.sleep(1000);
	  //Submit		
	  driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
	  
	  
	  String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
	  Assert.assertEquals(resultMsg, "Add IbUser Successful");
	  

	  //Wait until the list is loaded completely		
	  wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
	  wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

	  String userID = DBUtils.checkDBStatus(IBfName, TestEnv, Brand);
	  
      System.out.println("Check ID record in DB...");
      registInst.funcGetIDRecord(userID, TestEnv, Brand);  //Need to comment out when ID function is ready

      System.out.println();
      System.out.println("Check World Check record in DB...");
      registInst.funcGetWorldCheck(userID, TestEnv, Brand);         //Need to comment out when ID function is ready

      System.out.println();
      System.out.println("Check POA record in DB...");
      registInst.funcGetPOARecord(userID, TestEnv, Brand);
	} 
	
	
	//Add Internal User
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void AddIUser(String AdminURL,String TestEnv, String Brand) throws Exception
	{
	
		Select t;
		int j;
		
		WebDriverWait wait01=new WebDriverWait(driver,Duration.ofSeconds(1));
		
		driver.navigate().to(AdminURL);
	    //Navigate to Internal User Page
		driver.findElement(By.linkText("Users")).click();
		driver.findElement(By.linkText("Internal User")).click();
		
	   //Click Add new user button
		wait01.until(ExpectedConditions.elementToBeClickable(By.id("button1"))).click();
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.panel.panel-default")));
		
		//Assert New User dialog is popped up
		Thread.sleep(1000);
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(), "Add new user");
		
		//Fill out the form
		//Input login name
		Thread.sleep(1000);
		internalUserName="IU"+Utils.randomString(4).toLowerCase();
		driver.findElement(By.id("username")).sendKeys(internalUserName);
		
		//Input User Name
		driver.findElement(By.id("cnName")).sendKeys(internalUserName.toUpperCase()+" " + Utils.randomString(4).toUpperCase());
		
		//Choose Organization
		driver.findElement(By.id("admin_add_1_switch")).click();
		driver.findElement(By.id("admin_add_2_check")).click();
		
		//Input role
		t=new Select(driver.findElement(By.id("roleId")));
		j=tRandom.nextInt(t.getOptions().size()-1)+1;
		t.selectByIndex(j);
		
		//Select Superior
		t=new Select(driver.findElement(By.id("superior")));
		j=tRandom.nextInt(t.getOptions().size()-1)+1;
		t.selectByIndex(j);
		
		//Input new password
		driver.findElement(By.id("password1")).sendKeys("123Qwe");		
		
		//Input confirm password
		driver.findElement(By.id("password2")).sendKeys("123Qwe");				
		
		//Input Email address
		driver.findElement(By.id("email")).sendKeys(internalUserName.toLowerCase()+emailSuffix);			
		
		//Input Mobile Phone
		driver.findElement(By.id("phoneNum")).sendKeys(Utils.randomNumber(8));				
		
		switch(Brand)
		{
			case "au":
			case "ky":
			case "fca":
			case "vfsc":
			case "vfsc2":
			case "regulator2":
				//Add Commission code
				driver.findElement(By.cssSelector("div.col-md-11.paddingClear input")).sendKeys("CC"+Utils.randomString(3));				
				break;
				
			case "vt":
			case "svg":
			case "fsa":
				default:
					System.out.println(Brand + " does NOT have COMMISSION CODE.");
					break;
				
				
		}
		//Select subordinate account type
		t=new Select(driver.findElement(By.id("sales_accounttype")));
		j=tRandom.nextInt(t.getOptions().size());
		t.selectByIndex(j);	
		
		//Added by Yanni: to add regulator column
		/*
		 * Click regulator , then select one regulator. Code will select based on brands
		 * <input type="checkbox" class="multDropCheckAll" value="">
		 */
		//Click input( name="regulators") filed to show the dropdown list:
		driver.findElement(By.xpath("//input[@name = 'regulators']")).click();
		Thread.sleep(300);
		switch(Brand)
		{
			case "au":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='ASIC']")).click();
				
				break;
	
			case "vfsc":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='VFSC']")).click();
				break;
				
			case "vfsc2":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='VFSC2']")).click();
				break;
	
			case "fca":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='FCA']")).click();
				break;
				
			case "regulator2":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='REGULATOR2']")).click();			
				break;
				
			
			case "svg":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='SVG']")).click();
				break;
			case "fsa":
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='FSA']")).click();
				break;
				
			case "ky":
			case "vt":
			default:
				driver.findElement(By.xpath("//input[@type='checkbox'][@value='CIMA']")).click();			
				
				
		}
		
		//Click Confirm button
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		String userID = DBUtils.checkDBStatus(internalUserName, TestEnv, Brand);
		
		//Search global.tb_user_inner to check default_regulator and mutli_regulator
		String userInner = "SELECT user_id, real_name, regulators, default_regulator, is_del, parent_name FROM tb_user_inner WHERE user_id = " + userID + ";";
		DBUtils.funcreadDB(Utils.getDBName(Brand)[0], userInner, TestEnv);  
		
/*	    String selectSql="select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name like '" + internalUserName +"%';";
	    String userID[] = Utils.readDB(selectSql, TestEnv,Brand);
		
		switch(Brand)
		{
			case "au":
			case "ky":
			case "vfsc":
			case "regulator2":
			default:
			    //Global user ID should be the same as the one in ASIc and the one in CIMA db
			    if(!(userID[0].equals(userID[1]) && userID[0].equals(userID[2])) )
			    {
				    System.out.println("Global User ID should be same with both ASIC and CIMA DB.");
			    	Assert.assertTrue(false, "Global User ID should be same with both ASIC and CIMA DB.");
			    }				
				break;
				
			case "vt":
			case "pug":
			    //Global user ID should be the same as the one in business DB
			    if(!(userID[0].equals(userID[1])))
			    {
				    System.out.println("Global User ID should be same with business DB.");
			    	Assert.assertTrue(false, "Global User ID should be same with business DB.");
			    }		
					break;
				
				
		}*/
	    
	
		
		//Search user to ensure the user is added
		Thread.sleep(2000);
		driver.findElement(By.id("searchType")).click();
		driver.findElement(By.linkText("User Name")).click();
		driver.findElement(By.id("userQuery")).sendKeys(internalUserName);
		driver.findElement(By.id("query")).click();
		Thread.sleep(waitIndex*4000);
		
		List<WebElement> links=driver.findElements(By.cssSelector("table#table tbody tr td"));
		Iterator<WebElement> iter=links.iterator();
		int count=0;
		
		while(iter.hasNext())
		{
			String text=iter.next().getText();
			if (text.contains(internalUserName))
			{
				count=count+1;
			}
		}
		
		Assert.assertTrue(count>=1,"Can't find the newly created Internal User.");
		
	
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void AddLeads(String AdminURL,String TestEnv, String Brand) throws Exception
	{
		String leadfName;
		String leadlName;
		Select t;
		//Input the required information
		Random r=new Random();
				
		driver.navigate().to(AdminURL);
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Leads")).click();
		
		driver.findElement(By.id("addCustomer")).click();
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.panel-heading")).getText(), "Add Leads");
		
		
		//Input Client Name
		leadfName="Lead"+Utils.randomString(3).toLowerCase();
		leadlName= Utils.randomString(4).toUpperCase();
		driver.findElement(By.id("accountName")).sendKeys( leadfName + " " + leadlName);
		
		driver.findElement(By.id("email")).sendKeys(leadfName+emailSuffix);
		driver.findElement(By.id("password")).sendKeys("123Qwe");
		
		//Input leadSource
		driver.findElement(By.id("leadSource")).sendKeys("Automation");
		
		//Input Mobile
		driver.findElement(By.id("mobile")).sendKeys(Utils.randomNumber(8));
		
		//Input Title
		t=new Select(driver.findElement(By.id("title")));
		t.selectByIndex(tRandom.nextInt(t.getOptions().size()-1)+1);
		
		//Input Country
		t=new Select(driver.findElement(By.id("country")));
		//Input Country of Residence
		if (Brand.equalsIgnoreCase("au")) {
			t.selectByValue("3512");
		}else {
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		}
		
		//Iraq is a banned country so can't be used as registration
		if(t.getFirstSelectedOption().getText().equals("Iraq") || t.getFirstSelectedOption().getText().equals("Afghanistan"))
		{
			t.selectByVisibleText("Australia");;
		}
		
		//Click Submit
		driver.findElement(By.cssSelector("button.btn.btn-primary.pull-left.popModalSub")).click();
		
		//Wait until the table is loaded completely
		Thread.sleep(2000);
		
		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("Status is: " + resultMsg);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
		
		String selectSql="select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name like '" + leadfName + " " + leadlName +"%';";
		String userID[] = DBUtils.readDB(selectSql, TestEnv,Brand);
		  
	    if(!userID[0].equals(userID[1]) && !userID[0].equals(userID[2]) )
	    {
		    Assert.assertTrue(false, "Global User ID should be same with one in either ASIC or CIMA DB.");
	    }
		
	}

	//Modify email and password for a Client
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn")
	@Parameters(value= {"OldPwd", "NewPwd", "TestEnv","Brand"})
	public void ModifyClientEmailAndPassword(String OldPwd, String NewPwd, String TestEnv, String Brand, Method method) throws Exception
	{
		String email,userName,a;
		String newEmail = "automation"+Utils.randomString(3)+"@test.com";
		String[] keyWords=new String[]{Utils.webUserPrefix,Utils.addUserPrefix};
		int i,j,flag=0;
			
		ExtentTestManager.startTest(method.getName(),"Description: Modify Client email and password in admin");
		
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Client")).click();
		
		Thread.sleep(1000);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		for(i=0;i<keyWords.length;i++)
		{
			if(flag==1) {
				break;
			}
			// Create a new instance searchBar
			ClientSearch searchBar = new ClientSearch(driver);

			searchBar.inputUserName(keyWords[i]);

			// Click Search button
			searchBar.clickSearch();
			/*
			 * //Select Search Options
			 * driver.findElement(By.id("search_type")).click();
			 * //wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
			 * linkText("Client Name"))).click();
			 * Select t=new Select(driver.findElement(By.id("search_type")));
			 * t.selectByValue("real_name");
			 * driver.findElement(By.id("userQuery")).clear();
			 * driver.findElement(By.id("userQuery")).sendKeys(keyWords[i]);
			 * driver.findElement(By.id("query")).click();
			 */
			
			Thread.sleep(1000);
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
					
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No results for keywords " + keyWords[i]);
			}else
			{
				//Look for 1st records with name starting with pattern pat. 
				Thread.sleep(2000);
				
				for(j=0;j<trs.size();j++)
				{
					userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a")).getText();
					if(Utils.isWebUser(userName)|| Utils.isAddUser(userName)|| Utils.isJoint(userName)|| Utils.isTestIB(userName))
					{
						System.out.println("This is the db before modification:\n");
						String selectSql="select id,first_name,real_name,email,password,is_del from tb_user where real_name like '" + userName +"%';";			   
					    DBUtils.readDB(selectSql, TestEnv,Brand);
						String regTag = driver.findElement(By.cssSelector("table.table.table-hover thead tr:nth-child(1) th:nth-child(6) > div.th-inner")).getText();
						if (regTag.equals("REG")) {
							trs.get(j).findElement(By.cssSelector("td:nth-of-type(10) div > a")).click();
						}else {
						    trs.get(j).findElement(By.cssSelector("td:nth-of-type(9) div > a")).click();
						}

						//Click on the edit email icon
						driver.findElements(By.cssSelector("i.glyphicon.glyphicon-edit")).get(j).click();
						Thread.sleep(500);
						
						//Get original email
						email=driver.findElement(By.cssSelector("div.form-group:nth-child(3) div.col-md-7.pull-left.input-append.input-group > label.control-label")).getText();
						System.out.println("Original email is: "+email);
						
						//Modify email and password
						driver.findElement(By.id("new_email")).sendKeys(newEmail);
						driver.findElement(By.id("password")).sendKeys(NewPwd);
						driver.findElement(By.id("repassword")).sendKeys(NewPwd);
						driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
					
						//expect success message
						a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
						System.out.println("Result is "+a);
						wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						
						System.out.println("This is the db result after modification:\n");
						selectSql="select id,first_name,real_name,email,password,is_del from tb_user where real_name like '" + userName +"%';";			   
					    DBUtils.readDB(selectSql, TestEnv,Brand);
						
						//Reverse the email modification
						//Look for 1st records with name starting with pattern pat. 
						Thread.sleep(2000);
						trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
						
						if (regTag.equals("REG")) {
							trs.get(j).findElement(By.cssSelector("td:nth-of-type(10) div > a")).click();
						}else {
						    trs.get(j).findElement(By.cssSelector("td:nth-of-type(9) div > a")).click();
						}
						
						//Click on the edit email icon
						driver.findElements(By.cssSelector("i.glyphicon.glyphicon-edit")).get(j).click();
						Thread.sleep(500);

						//Modify email and password
						driver.findElement(By.id("new_email")).sendKeys(email);
						driver.findElement(By.id("password")).sendKeys(OldPwd);
						driver.findElement(By.id("repassword")).sendKeys(OldPwd);
						driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
						
						//expect success message
						a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
						System.out.println("Result is "+a);
						wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						
						System.out.println("This is the db result after reverse:\n");
						selectSql="select id,first_name,real_name,email,password,is_del from tb_user where real_name like '" + userName +"%';";			   
					    DBUtils.readDB(selectSql, TestEnv,Brand);
						flag=1;
						break;
					}else {
						System.out.println("Not an automation test user!");
					}

				}		

			}
		}
		
	}

	//Add a new external user
	@Test (invocationCount=1,alwaysRun=true)
	@Parameters(value= {"AdminURL","AdminName","Brand", "TestEnv"})
	public void AddIBNew(String AdminURL, String AdminName,String Brand, String TestEnv, ITestContext context) throws Exception
	{
				
        Select t;
		
		Thread.sleep(waitIndex*1000);
        
        driver.navigate().to(AdminURL);
        
        
		driver.findElement(By.linkText("Users")).click();
		driver.findElement(By.linkText("External user")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("button1")).click();
		
		Thread.sleep(1000);
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"Add new user");
        
		//Input the required information
		Random r=new Random();
		IBfName=Utils.ibUserPrefix+Utils.randomString(3);
		IBlName=Utils.randomString(3).toUpperCase();
		
		//Input gender
		 if(!Brand.equalsIgnoreCase("au"))
		 {
			t=new Select(driver.findElement(By.id("gender")));
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		 }
		//Input First Name
		driver.findElement(By.id("acc_first_name")).clear();
		driver.findElement(By.id("acc_first_name")).sendKeys(IBfName);
	
		if (!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("svg")&& !Brand.equalsIgnoreCase("fsa")) {
			//Input middle name
			String IBmName="MName" + Utils.randomString(3);
			driver.findElement(By.id("acc_middle_name")).clear();
			driver.findElement(By.id("acc_middle_name")).sendKeys(IBmName);
		}
		
		//Input Last Name
		driver.findElement(By.id("acc_last_name")).clear();
		driver.findElement(By.id("acc_last_name")).sendKeys(IBlName);
		
		//IB Level
		t=new Select(driver.findElement(By.id("ib_Role")));
		//t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		t.selectByVisibleText("level-1");
		
		//Account owner
		t=new Select(driver.findElement(By.id("accountOwner")));
		t.selectByVisibleText(AdminName);

		//Input email address
		driver.findElement(By.id("ib_email")).sendKeys(IBfName+emailSuffix);
		context.setAttribute("TraderName", IBfName+emailSuffix);
		
		System.out.println(context.getAttribute("TraderName").toString());
		
		//Input Client Portal Password
		driver.findElement(By.id("ib_password")).sendKeys(ibPwd);
		
		
		//Input Mobile
		Select mobileCode = new Select(driver.findElement(By.name("mobile_code")));
		mobileCode.selectByIndex(r.nextInt(mobileCode.getOptions().size()-1)+1);
		driver.findElement(By.id("ib_Mobile")).sendKeys(Utils.randomNumber(8));
		
		//Input Birthday
		//Keep the default birthday and don't change
		
		//Input Country of Residence
		Select country=new Select(driver.findElement(By.id("ib_Country")));
		country.selectByIndex(r.nextInt(country.getOptions().size()-1)+1);
		
		//Iraq and Afghanistan are banned country so can't be used as registration
		if(country.getFirstSelectedOption().getText().equals("Iraq") || country.getFirstSelectedOption().getText().equals("Afghanistan"))
		{
			country.selectByVisibleText("China");;
		}
		
		//Input Nationality
		Select nationality=new Select(driver.findElement(By.id("ib_Nationality")));
		nationality.selectByIndex(r.nextInt(nationality.getOptions().size()-1)+1);
		
		//Input address infor
		driver.findElement(By.id("ib_street_adress")).sendKeys(Utils.randomNumber(2)+" " + Utils.randomString(5) + " Street");
		driver.findElement(By.id("ib_Suburb")).sendKeys(Utils.randomString(4));
		driver.findElement(By.id("inputState")).sendKeys("NSW");
		driver.findElement(By.id("ib_Postcode")).sendKeys(Utils.randomNumber(4));
		
			
		//Input Client Type 1 (Individual /Company)
		Select accountT1=new Select(driver.findElement(By.id("ib_Account_type")));
		
		/*if(!Brand.equalsIgnoreCase("vt"))
		{
			Assert.assertTrue(accountT1.getOptions().size()==2, Brand + " should have 2 IB types: Individual & Company");
		}else
		{
			Assert.assertTrue(accountT1.getOptions().size()==1, Brand + " should have 1 IB type: Individual");
		}*/
		
		accountT1.selectByIndex(r.nextInt(accountT1.getOptions().size()));
		
		//Input Account Type 1 (Standard STP, RAW ECN, etc)
		t=new Select(driver.findElement(By.id("ib_accountType")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);	
		
		
		//Input Identification Type
		t=new Select(driver.findElement(By.id("acc_identification_type")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Input Identification Number
		driver.findElement(By.id("acc_identification_number")).sendKeys(Utils.randomString(3)+Utils.randomNumber(15));	
		
		//U.S Citizen/Resident
		t=new Select(driver.findElement(By.id("acc_tax_us")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
					
		//Employment status	
		Select employment=new Select(driver.findElement(By.id("ib_Mployment")));
		employment.selectByIndex(r.nextInt(employment.getOptions().size()-1)+1);
		

		
		//Annual Income 	
		Select aIncome=new Select(driver.findElement(By.id("income")));
		aIncome.selectByIndex(r.nextInt(aIncome.getOptions().size()-1)+1);
		

		//Approximate Net Worth	
		Select saving=new Select(driver.findElement(By.id("ib_Sinvest")));
		saving.selectByIndex(r.nextInt(saving.getOptions().size()-1)+1);
		
		
		//Source of Funds

		Select funds=new Select(driver.findElement(By.id("ib_funds")));
		funds.selectByIndex(r.nextInt(funds.getOptions().size()-1)+1);
		
		if(funds.getFirstSelectedOption().getText().equals("Other"))
		{
			driver.findElement(By.id("funds_other")).sendKeys("Other Source " + Utils.randomString(4));
		}
		
		//Expected Deposit
		t=new Select(driver.findElement(By.id("acc_invest_deposit")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Intended Trade Volume(Weekly)
		t=new Select(driver.findElement(By.id("acc_investmentexp_tradew")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Intended Trade Value(Weekly)
		t=new Select(driver.findElement(By.id("acc_investmentexp_amount_tradew")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Input refereral
		driver.findElement(By.id("ib_referto")).sendKeys(Utils.randomString(4) + " " + Utils.randomString(3));
				
		//How many clients does the IB currently have?
		Select numberofClients=new Select(driver.findElement(By.id("ib_clients")));
		numberofClients.selectByIndex(r.nextInt(numberofClients.getOptions().size()-1)+1);
		
		//IB working with other brokers?
		int j=r.nextInt(2);
		driver.findElements(By.name("ib_relation")).get(j).click();
		if(j==0)
		{
			driver.findElement(By.id("ib_rela_broker")).sendKeys(RandomString.make(5) + ""+RandomString.make(3));
		}
		
		//How many clients does the IB expect to introduce each month
		Select noNewClients=new Select(driver.findElement(By.id("ib_roped")));
		j=r.nextInt(noNewClients.getOptions().size()-1)+1;
		noNewClients.selectByIndex(j);
		
		//Average monthly trading volume
		Select tradeVol=new Select(driver.findElement(By.id("ib_trading_volume")));
		j=r.nextInt(tradeVol.getOptions().size()-1)+1;
		tradeVol.selectByIndex(j);
		
		//IB working with other brokers?
		j=r.nextInt(2);
		driver.findElements(By.name("ib_web_relation")).get(j).click();
		if(j==0)
		{
			driver.findElement(By.id("ib_website")).sendKeys("http://www."+RandomString.make(5) + "."+RandomString.make(3));
		}
		
		//Input identity Proof 1
				
		driver.findElement(By.xpath("//input[@type='file' and @data-name='passport']")).sendKeys(Utils.workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("div.col-md-6.individual_upload div.col-md-12.input-box span a")).click();
		
		//Input identity Proof 2 (Yanni on 01/06/2020 for 2 identity files)
		Thread.sleep(1000);
		driver.findElements(By.xpath("//input[@type='file' and @data-name='passport']")).get(1).sendKeys(Utils.workingDir+"\\proof2.png");
		driver.findElements(By.cssSelector("div.col-md-6.individual_upload div.col-md-12.input-box span a")).get(1).click();
		
		
		//Input Address Proof
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@type='file' and @data-name='bankStatement']")).sendKeys(Utils.workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("div.col-md-6.individual_upload_address div.col-md-12.input-box span a")).click();
		Thread.sleep(1000);
		//Submit		
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		
		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		Assert.assertEquals(resultMsg, "Add IbUser Successful");
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		String userID = DBUtils.checkDBStatus(IBfName, TestEnv, Brand);
		
	   System.out.println("Check ID record in DB...");
       registInst.funcGetIDRecord(userID, TestEnv, Brand);  //Need to comment out when ID function is ready

       System.out.println();
       System.out.println("Check World Check record in DB...");
       registInst.funcGetWorldCheck(userID, TestEnv, Brand);         //Need to comment out when ID function is ready

       System.out.println();
       System.out.println("Check POA record in DB...");
       registInst.funcGetPOARecord(userID, TestEnv, Brand);
		
	}	

		
	/*Added by Alex.L for verifying the hyper link*/
	public void funcVerifyHyperlink(WebDriver driver, String handle,String xpath,String url) throws Exception {

		Set<String> handleS;
		
		//Click on hyper link
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(1000);

		handleS=driver.getWindowHandles();
		for(String s:handleS)
		{
			if(!s.equals(handle))
			{
				driver.switchTo().window(s);
				Thread.sleep(1000);
				Utils.funcIsStringContains(driver.getCurrentUrl(), url, "");
				driver.close();
				driver.switchTo().window(handle);
			}
		}
		Thread.sleep(1000);
	}
}
