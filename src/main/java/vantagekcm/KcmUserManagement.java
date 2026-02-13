package vantagekcm;

import static org.testng.Assert.assertEquals;
import vantagecrm.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import vantagecrm.Utils;



public class KcmUserManagement {
	WebDriver driver;
		
	String emailSuffix="@test.com";
	
	WebDriverWait wait10;

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value="TestEnv")
	public void LaunchBrowser(String TestEnv, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
    	  driver = new ChromeDriver();	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		  
		  context.setAttribute("driver", driver);
		  wait10=new WebDriverWait(driver, Duration.ofSeconds(10));
	
	}
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		

		//Close all browsers
		driver.quit();
	}

	@Test
	@Parameters(value= {"RegisterURL", "TestEnv"})
	void RegisterIndividual(String RegisterURL, String TestEnv) throws Exception
	{
		RegisterURL = Utils.ParseInputURL(RegisterURL) + "tradingaccounts/registerlive";
		funcRegisterUser(RegisterURL, "Individual", TestEnv);		
		
	}
	
	@Test
	@Parameters(value= {"RegisterURL", "TestEnv"})
	void RegisterJoint(String RegisterURL, String TestEnv) throws Exception
	{
		
		RegisterURL = Utils.ParseInputURL(RegisterURL) + "tradingaccounts/registerlive";
		funcRegisterUser(RegisterURL, "Joint", TestEnv);		
		
	}
	
	//AccountType: Individual/Joint
	void funcRegisterUser(String RegisterURL, String AccountType, String TestEnv) throws Exception
	{
		
		Select t;  //Create Select control
		int j;  //random selection index
		Random r=new Random(); //randome selector
		String fName,lName,mName;
		String emailAddress;
		String SecondfName="";
		String workingDir=System.getProperty("user.dir");
		
				
		String[] licenseStr=new String[] {"Vantage International Group Risk Warning Notice",
										  "Vantage International Group Client Agreement",
										  "Vantage International Group Best Execution Policy",
										  "Vantage International Group Conflicts of Interest Policy",
										  "Vantage International Group Privacy Policy"};
		String[] urlStr=new String[] {"https://klimexcm.com/about-us/legal-documents/risk-warning-notice",
									  "https://klimexcm.com/about-us/legal-documents/client-agreement",
									  "https://klimexcm.com/about-us/legal-documents/best-execution-policy",
									  "https://klimexcm.com/about-us/legal-documents/conflict-of-interest-policy",
									  "https://klimexcm.com/about-us/legal-documents/privacy-policy"
				
		};
		
		
				
		//open user registration page
		driver.navigate().to(RegisterURL);
		
		//Verify the legal document TEXT and links
		
		List<WebElement> linksLegal=driver.findElements(By.cssSelector("p.newtradingaccount-inner2-Explain-text a"));
		
		Assert.assertTrue(linksLegal.size()==5, "The Number of Legal Documents should be 5, but now it is "+linksLegal.size());
		
		for(int i=0; i<linksLegal.size(); i++)
		{
			Assert.assertTrue(licenseStr[i].equals(linksLegal.get(i).getText()), "Link "+licenseStr[i] + " does not equal to the actual value "+linksLegal.get(i).getText());
			Assert.assertEquals(linksLegal.get(i).getAttribute("href"), urlStr[i]);
		}
		
		//Accept all terms of Trading Account
		driver.findElement(By.id("checkes")).click();
		driver.findElement(By.id("checkes1")).click();
		driver.findElement(By.id("checkes2")).click();
		
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0);");
		Utils.funcTakeScreenShot(driver, "KCM P0-1");
		((JavascriptExecutor)driver).executeScript("window.scrollBy(0,1000);");
		Utils.funcTakeScreenShot(driver, "KCM P0-2");
		
		//Submit
		driver.findElement(By.id("button")).click();
		
		Thread.sleep(1000);
		
		//Verify the Individual Account registration page is navigated
		assertEquals(driver.getTitle(),"Individual Account");
		
		//Choose account type
		t=new Select(driver.findElement(By.id("accountType")));
		if(AccountType.equalsIgnoreCase("Individual"))
		{
			//Only create Individual account
			t.selectByIndex(0);
		}else if(AccountType.equalsIgnoreCase("Joint"))
		{
			//Only create Joint account
			t.selectByIndex(1);
		}
		
				
		//Account Information
		t=new Select(driver.findElement(By.id("title")));
		j=r.nextInt(t.getOptions().size());
		t.selectByIndex(j);
		
		fName="testcrm"+Utils.randomString(3);
		mName=Utils.randomString(3).toUpperCase();
		lName=Utils.randomString(3).toUpperCase();
		
		driver.findElement(By.id("firstName")).sendKeys(fName);
		driver.findElement(By.id("middleName")).sendKeys(mName);
		driver.findElement(By.id("surName")).sendKeys(lName);
		
		
		//Date of Birth
		t=new Select(driver.findElement(By.id("day")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		t=new Select(driver.findElement(By.id("month")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		t=new Select(driver.findElement(By.id("year")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		
		//Identification Type
		t=new Select(driver.findElement(By.id("acc_id_type")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//ID Number
		driver.findElement(By.id("idNum")).sendKeys("56478"+ Utils.randomString(4));
		
		//US citizen
		t=new Select(driver.findElement(By.id("acc_tax_us")));
		
		//Assert No is the default option
		Assert.assertEquals(t.getFirstSelectedOption().getText(), "No");
		
		//Select Yes or No
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Residential Address
		t=new Select(driver.findElement(By.id("residencyCountryCode")));
		
		//Verify Australia is not listed in the country list
		for (WebElement el : t.getOptions()) {
			
			Assert.assertFalse(el.getText().equals("AUSTRALIA"), "Australia should be removed from the country list.");
		}
			
		//Select a country
		j=r.nextInt(t.getOptions().size()+1);
		t.selectByIndex(j);
		
		//Province
		driver.findElement(By.id("input_province_code")).sendKeys(Utils.randomString(4).toUpperCase());
	
		//City
		driver.findElement(By.id("cityCode")).sendKeys(Utils.randomString(5).toUpperCase());
		
		//Input Suburb
		driver.findElement(By.id("suburb")).sendKeys(Utils.randomString(6).toUpperCase());
		
		//Input Postcode
		driver.findElement(By.id("postcode")).sendKeys("1234");
		
		//Input Street Name
		driver.findElement(By.id("streetName")).sendKeys(Utils.randomString(5)+ " " + Utils.randomString(4));
		
		//Input Street Type		
		driver.findElement(By.id("input_street_type")).sendKeys(Utils.randomString(5).toUpperCase());
		
		//Contact Details
		
		//Work Phone Area Code
		driver.findElement(By.id("workPhone_first")).sendKeys(Utils.randomNumber(3));
		
		//Work Phone
		driver.findElement(By.id("workPhone_last")).sendKeys(Utils.randomNumber(8));
		
		//Home Phone Area Code
		driver.findElement(By.id("homePhone_first")).sendKeys(Utils.randomNumber(3));
		
		//Home Phone
		driver.findElement(By.id("homePhone_last")).sendKeys(Utils.randomNumber(8));
		
		
		//Mobile Phone
		driver.findElement(By.id("phoneNum")).sendKeys("6190234565");
		
		//Fax Area Code
		driver.findElement(By.id("fax_first")).sendKeys(Utils.randomNumber(3));
		
		//Fax Phone
		driver.findElement(By.id("fax_last")).sendKeys(Utils.randomNumber(8));
		
		//Email Address
		emailAddress=fName+emailSuffix;
		driver.findElement(By.id("userName")).sendKeys(emailAddress);
		driver.findElement(By.id("reUserName")).sendKeys(emailAddress);
		
		//Employment and financial details
		//Employment Status
		t=new Select(driver.findElement(By.id("risk_work_status")));
		Assert.assertTrue(t.getOptions().size()==5);  //Have 5 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Approximate Net Worth
		t=new Select(driver.findElement(By.id("risk_invest")));
		Assert.assertTrue(t.getOptions().size()==5);  //Have 5 options
		Assert.assertEquals(t.getFirstSelectedOption().getText(), "*Approximate Net Worth");
		
		j=r.nextInt(t.getOptions().size()-1);
	
		t.selectByIndex(j+1);
		
		//Source/Destination of Funds
		t=new Select(driver.findElement(By.id("risk_earnings")));
		Assert.assertTrue(t.getOptions().size()==8);  //Have 8 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Nationality
		t=new Select(driver.findElement(By.id("risk_nationality")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		
		//You Traded Securities or Bonds
		t=new Select(driver.findElement(By.id("acc_investmentexp_security")));
		Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//You Trade derivatives
		t=new Select(driver.findElement(By.id("acc_investmentexp_derivative")));
		Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//You traded leveraged FX, CFDs'
		t=new Select(driver.findElement(By.id("acc_investmentexp_levfx")));
		Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Your approximate trading value was
		t=new Select(driver.findElement(By.id("acc_investmentexp_vol")));
		Assert.assertTrue(t.getOptions().size()==5);  //Have 5 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Intended number of weekly trades
		t=new Select(driver.findElement(By.id("acc_investmentexp_tradew")));
		Assert.assertTrue(t.getOptions().size()==4);  //Have 4 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		
		//Intended value of weekly trades
		t=new Select(driver.findElement(By.id("acc_investmentexp_amount_tradew")));
		Assert.assertTrue(t.getOptions().size()==4);  //Have 4 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0);");
		Utils.funcTakeScreenShot(driver, "KCM P1-1");
		((JavascriptExecutor)driver).executeScript("window.scrollBy(0,600);");
		Utils.funcTakeScreenShot(driver, "KCM P1-2");
		((JavascriptExecutor)driver).executeScript("window.scrollBy(0,1000);");
		Utils.funcTakeScreenShot(driver, "KCM P1-3");
		
		
		//If it is a joint customer
		if(AccountType.equalsIgnoreCase("Joint"))
		{
			//Click Next
			driver.findElement(By.id("se_SubBtn")).click();
			
			Thread.sleep(1000);
			
			SecondfName=funcRegister2ndAppli();
			
			//Capture 2nd applicant information
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0);");
			Utils.funcTakeScreenShot(driver, "KCM P2-1");
			((JavascriptExecutor)driver).executeScript("window.scrollBy(0,600);");
			Utils.funcTakeScreenShot(driver, "KCM P2-2");
			((JavascriptExecutor)driver).executeScript("window.scrollBy(0,1000);");
			Utils.funcTakeScreenShot(driver, "KCM P2-3");
		}
		
	
		//Submit
		driver.findElement(By.id("SubBtn")).click();
		
		Thread.sleep(1000);
		
		assertEquals(driver.getTitle(), "Update Personal Information");
	
		
		//Navigated to Trader to provide attachments: Input Passport
		driver.findElement(By.id("file-3")).sendKeys(workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("form#fileForm-3 span a")).click();
		
		//Navigated to Trader to provide attachments: Input Utility bill
		driver.findElement(By.id("file-4")).sendKeys(workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("form#fileForm-4 span a")).click();
		
		//Navigated to Trader to provide attachments: Save
		driver.findElement(By.id("btnSubmit")).click();
		
		System.out.println("The Registered User Account is: "+emailAddress);
		
		Thread.sleep(1000);
		
		//If it is test env, print the DB record status.
		/*
		 * if(TestEnv.equalsIgnoreCase("test"))
		 * {
		 * //Print the record in tb_user_nonau
		 * funcReadNonAU(fName);
		 * 
		 * //Print the newly added fields in tb_user_register
		 * funcReadUserExtends(fName);
		 * 
		 * if(!SecondfName.equals(""))
		 * {
		 * funcReadNonAU(SecondfName);
		 * funcReadUserExtends(SecondfName);
		 * }
		 * }
		 */
	}

	@Test
	@Parameters(value= {"RegisterURL", "TestEnv"})
	void RegisterIB(String RegisterURL, String TestEnv) throws Exception
	{
		
		Select t;  //Create Select control
		int j;  //random selection index
		Random r=new Random(); //randome selector
		String fName,lName,mName, ibPWD="123Qwe";
		String emailAddress;
		String workingDir=Utils.workingDir;
		
		//open user registration page
		RegisterURL = Utils.ParseInputURL(RegisterURL) + "tradingaccounts/registerLive_customer";
		
		driver.get(RegisterURL);
		
		//Click RegisterIB link
		wait10.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("REGISTER IB"))).click();
		Thread.sleep(1000);
		
		//Verify the IB Account registration page is navigated
		assertEquals(driver.getTitle(),"Register IB Account");

				
		//IB Name
		fName="IBtestcrm"+Utils.randomString(3);
		mName=Utils.randomString(3);
		lName=Utils.randomString(3);
		
		driver.findElement(By.id("firstName")).sendKeys(fName);
		driver.findElement(By.id("middleName")).sendKeys(mName);
		driver.findElement(By.id("surName")).sendKeys(lName);
		
		//Input password
		driver.findElement(By.id("password")).sendKeys(ibPWD);
		
		//Email Address
		emailAddress=fName+emailSuffix;
		driver.findElement(By.id("userName")).sendKeys(emailAddress);
		
		//Mobile Phone
		driver.findElement(By.id("phoneNum")).sendKeys(Utils.randomNumber(10));
		
		//ID/Passport number
		driver.findElement(By.name("idNum")).sendKeys(Utils.randomString(2)+Utils.randomNumber(10));
		
		//Date of Birth: Day
		t=new Select(driver.findElement(By.id("day")));
		j=r.nextInt(t.getOptions().size());
		t.selectByIndex(j);
		
		//Date of Birth: Month
		t=new Select(driver.findElement(By.id("month")));
		j=r.nextInt(t.getOptions().size());
		t.selectByIndex(j);
		
		t=new Select(driver.findElement(By.id("year")));
		j=r.nextInt(t.getOptions().size());
		t.selectByIndex(j);
		
		//Address Line
		driver.findElement(By.id("address")).sendKeys(Utils.randomNumber(2) + " " + Utils.randomString(5) + " Ave");
		
		//Address: Country
		t=new Select(driver.findElement(By.id("residencyCountryCode")));
		
		/*for(int i=0;i<t.getOptions().size();i++)
		{
			Assert.assertFalse(t.getOptions().get(i).getText().equalsIgnoreCase("AUSTRALIA"), "Australia should be removed from the country list.");
			
		}*/
		
		j=r.nextInt(t.getOptions().size());
		t.selectByIndex(j);

		
		//Provincre: when country is Australia, select States.Otherwise input random generated states
		if(t.getFirstSelectedOption().getText().equalsIgnoreCase("AUSTRALIA"))
		{
			t=new Select(driver.findElement(By.id("select_province_code")));
			j=r.nextInt(t.getOptions().size());
			t.selectByIndex(j);			
		} else
		{
			driver.findElement(By.id("input_province_code")).sendKeys(Utils.randomString(4) + " Prov");
		}
		
		//City:
		driver.findElement(By.id("cityCode")).sendKeys(Utils.randomString(4) + " City");
		
				
		//Input Postcode
		driver.findElement(By.id("postcode")).sendKeys(Utils.randomString(2)+Utils.randomNumber(5));
		
			            
		//Submit
		driver.findElement(By.id("SubBtn")).click();
		
		Thread.sleep(1000);
		
		assertEquals(driver.getTitle(), "Update Personal Information");
		
		//Input Passport
		driver.findElement(By.id("file-3")).sendKeys(workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("form#fileForm-3 span a")).click();
		
		//Input Utility bill
		driver.findElement(By.id("file-4")).sendKeys(workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("form#fileForm-4 span a")).click();
		
		//Save
		driver.findElement(By.id("btnSubmit")).click();
		
		System.out.println("The Registered IB Account is " + emailAddress);
		/*
		 * System.out.println("tb_user_nonau record is: ");
		 * funcReadNonAU(fName);
		 */
	
	}

	
	String funcRegister2ndAppli() throws Exception
	{
		Select t;  //Create Select control
		int j;  //random selection index
		Random r=new Random(); //randome selector
		String fName,lName,mName;
		String emailAddress;
		
						
		Thread.sleep(1000);
		
		//Verify the Individual Account registration page is navigated
		assertEquals(driver.getTitle(),"Individual Account");
		
		
		//Account Information
		t=new Select(driver.findElement(By.id("joint2_title")));
		j=r.nextInt(t.getOptions().size());
		t.selectByIndex(j);
		
		fName="testcrm2nd"+Utils.randomString(3);
		mName=Utils.randomString(3).toUpperCase();
		lName=Utils.randomString(3).toUpperCase();
		
		driver.findElement(By.id("joint2_firstName")).sendKeys(fName);
		driver.findElement(By.id("joint2_middleName")).sendKeys(mName);
		driver.findElement(By.id("joint2_surName")).sendKeys(lName);
		
		
		//Date of Birth
		t=new Select(driver.findElement(By.id("joint2_day")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		t=new Select(driver.findElement(By.id("joint2_month")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		t=new Select(driver.findElement(By.id("joint2_year")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		
		//Identification Type
		t=new Select(driver.findElement(By.id("joint_acc_id_type")));
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//ID Number
		driver.findElement(By.id("joint2_idNum")).sendKeys("56478"+ Utils.randomString(4));
		
		//US citizen
		t=new Select(driver.findElement(By.id("joint_acc_tax_us")));
		
		//Assert No is the default option
		Assert.assertEquals(t.getFirstSelectedOption().getText(), "No");
		
		//Select Yes or No
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Residential Address
		t=new Select(driver.findElement(By.id("joint2_residencyCountryCode")));
		
		//Verify Australia is not listed in the country list
		for (WebElement el : t.getOptions()) {
			
			Assert.assertFalse(el.getText().equals("AUSTRALIA"), "Australia should be removed from the country list.");
		}
			
		//Select a country
		j=r.nextInt(t.getOptions().size()+1);
		t.selectByIndex(j);
		
		//Province
		driver.findElement(By.id("joint2_input_province_code")).sendKeys(Utils.randomString(4));
	
		//City
		driver.findElement(By.id("joint2_cityCode")).sendKeys(Utils.randomString(5));
		
		//Input Suburb
		driver.findElement(By.id("joint2_suburb")).sendKeys(Utils.randomString(6));
		
		//Input Postcode
		driver.findElement(By.id("joint2_postcode")).sendKeys("1234");
		
		//Input Street Name
		driver.findElement(By.id("streetName")).sendKeys(Utils.randomString(5)+ Utils.randomString(4));
		
		//Input Street Type		
		driver.findElement(By.id("joint_input_street_type")).sendKeys(Utils.randomString(5).toUpperCase());
		
		
		//Contact Details
		
		//Work Phone Area Code
		driver.findElement(By.id("joint2_workPhone_first")).sendKeys(Utils.randomNumber(3));
		
		//Work Phone
		driver.findElement(By.id("joint2_workPhone_last")).sendKeys(Utils.randomNumber(8));
		
		//Home Phone Area Code
		driver.findElement(By.id("joint2_homePhone_first")).sendKeys(Utils.randomNumber(3));
		
		//Home Phone
		driver.findElement(By.id("joint2_homePhone_last")).sendKeys(Utils.randomNumber(8));
		

		//Mobile Phone
		driver.findElement(By.id("joint2_phoneNum")).sendKeys(Utils.randomNumber(8));
		
		//Fax Area Code
		driver.findElement(By.id("joint2_fax_first")).sendKeys(Utils.randomNumber(3));
		
		//Fax Phone
		driver.findElement(By.name("joint2_fax_last")).sendKeys(Utils.randomNumber(8));
		
		//Email Address
		emailAddress=fName+emailSuffix;
		driver.findElement(By.id("joint2_userName")).sendKeys(emailAddress);
		driver.findElement(By.id("joint2_reUserName")).sendKeys(emailAddress);
		
				
		//You Traded Securities or Bonds
		t=new Select(driver.findElement(By.id("joint_acc_investmentexp_security")));
		Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//You Trade derivatives
		t=new Select(driver.findElement(By.id("joint_acc_investmentexp_derivative")));
		Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//You traded leveraged FX, CFDs'
		t=new Select(driver.findElement(By.id("joint_acc_investmentexp_levfx")));
		Assert.assertTrue(t.getOptions().size()==7);  //Have 7 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Your approximate trading value was
		t=new Select(driver.findElement(By.id("joint_acc_investmentexp_vol")));
		Assert.assertTrue(t.getOptions().size()==5);  //Have 5 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		//Intended number of weekly trades
		t=new Select(driver.findElement(By.id("joint_acc_investmentexp_tradew")));
		Assert.assertTrue(t.getOptions().size()==4);  //Have 4 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		
		//Intended value of weekly trades
		t=new Select(driver.findElement(By.id("joint_acc_investmentexp_amount_tradew")));
		Assert.assertTrue(t.getOptions().size()==4);  //Have 4 options
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		
		return fName;
	}
	
	public void funcReadUserExtends(String fName) {
		
		
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
	         String connectionUrl =
	                 "jdbc:sqlserver://192.168.66.151:1433;databaseName=DB_KCM_AU_TEST";
	         String userName="sa";
	         String password="Admin123";
	         Connection con = DriverManager.getConnection(connectionUrl, userName, password);
	         Statement stmt = con.createStatement(); 
	         String SQL = "select * from dbo.tb_user_register r inner join dbo.tb_user u on u.user_id=r.id  "
	         		+ "where first_name='" + fName + "';";
	         ResultSet rs = stmt.executeQuery(SQL);
	         ResultSetMetaData rsmd=rs.getMetaData();
	         int columnsNumber = rsmd.getColumnCount();
             while (rs.next()) 
             {
              	                 
            	 for (int i = 1; i <= columnsNumber; i++) {
            	       
            	        String columnName = rsmd.getColumnName(i);
            	        
            	        if(columnName.equalsIgnoreCase("id") || columnName.startsWith("acc"))
            	        {
            	        	System.out.print(columnName + ": " + rs.getString(i));
            	        	System.out.println();
            	        }
            	    }
            	    System.out.println("");
             }
	             
             stmt.close();
             con.close();
             
	        } catch (SQLException | ClassNotFoundException e) {
	             e.printStackTrace();
	         }
	     }

	public void funcReadNonAU(String fName) {
	
	
	try 
	{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	
	     String connectionUrl =
	             "jdbc:sqlserver://192.168.66.151:1433;databaseName=DB_KCM_AU_TEST";
	     String userName="sa";
	     String password="Admin123";
	     Connection con = DriverManager.getConnection(connectionUrl, userName, password);
	     Statement stmt = con.createStatement(); 
	     String SQL = "SELECT * FROM dbo.Yanni_Test_KCM_View where cn_name like '%" + fName + "%';";
	     ResultSet rs = stmt.executeQuery(SQL);
	     ResultSetMetaData rsmd=rs.getMetaData();
	     int columnsNumber = rsmd.getColumnCount();
	     while (rs.next()) 
	     {
	      	                 
	    	 for (int i = 1; i <= columnsNumber; i++) {
	    	        if (i > 1) System.out.print(",  ");
	    	        String columnName = rsmd.getColumnName(i);
	    	        System.out.print(columnName + ": " + rs.getString(i));
	    	    }
	    	    System.out.println("");
	     }
	         
	     stmt.close();
	     con.close();
	     
	    } catch (SQLException | ClassNotFoundException e) {
	         e.printStackTrace();
	     }
	 }
}
