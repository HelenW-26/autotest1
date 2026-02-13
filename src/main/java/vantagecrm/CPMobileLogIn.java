 package vantagecrm;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;


/*
 * Yanni on 02/06/2020: Test Mobile Login page: Links check in Login page,  CPMobileLogIn, Forgot password
 */

public class CPMobileLogIn {
	
	WebDriver driver;
	
	WebDriverWait wait01;
	WebDriverWait wait02;
	int waitIndex=1;
	Select t;
	CPLogInPage loginInst = new CPLogInPage(); 
	String phoneCode, phoneNumber;
	
	
	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);
    	  context.setAttribute("driver", driver);
   	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver,Duration.ofSeconds(20));
		  
		  loginInst.driver=driver;
		  

	}
	
	//TraderURL is mobile login url, eg: http://cp.vantagefx.com/loginByMobile
	//TraderName is a combination with phonecode and phone number. The seperator is a space.
	@Test(priority=0)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void MobileLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
  
		
		    //Navigate to Mobile Login Page
			driver.get(TraderURL);
			
			 //Seperate phonecode and phone number from TraderName
			 phoneCode = TraderName.split(" ")[0];
			 phoneNumber = TraderName.split(" ")[1];
	
			Utils.funcMobileLogInCP(driver,  phoneCode, phoneNumber, TraderPass, Brand);
			
			//When login failed, check error message
			List <WebElement> weErrorMessage = driver.findElements(By.cssSelector(".el-message__content"));
			String expectedErrorMessage = "Incorrect username or password";
			if (weErrorMessage.size()>0)
			{
				String errorMessage = weErrorMessage.get(0).getText();
				Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "Login error message expected: " + expectedErrorMessage + ", but got: " + errorMessage);
				System.out.println("There is error message: " + errorMessage);
			}
			
			Thread.sleep(waitIndex*2000);
			
			if(Brand.equalsIgnoreCase("vt"))
			{
				Assert.assertTrue(driver.findElement(By.cssSelector("h2")).getText().equals("Live accounts"));
			}else
			{
				Assert.assertTrue(driver.findElement(By.cssSelector("h3")).getText().equals("LIVE ACCOUNTS"));
			}
			
			
				
	}
	
	/*Yanni: Test All Links of LOGIN PAGE
	1. Links can work and won't get 404 error
	2. Links navigate to correct website. Like AU all go to VantageFX, PUG goes to puprime, etc
	 */	
	@Test(alwaysRun = true)
	@Parameters(value = {"Brand", "TraderURL"})
	public void testAllLinks(String Brand, String TraderURL) throws Exception
	{

		
		loginInst.testAllLinks(Brand, TraderURL);
	
	}
	
	
	//Test if reset password link works
	@Test(alwaysRun = true )
	@Parameters(value = {"Brand", "TraderURL","TraderName", "TestEnv","AdminURL","AdminName","AdminPass"})
	public void testResetPWLink(String Brand, String TraderURL, String TraderName, String TestEnv,String AdminURL, String AdminName,String AdminPass) throws Exception
	{
		
		String defaultPwd = "1234Qwer";
		Boolean isErrorCheck= false; 
		
		driver.navigate().to(TraderURL);
		
		//If session is already logged in, logged out first.
		try {
			Utils.funcLogOutCP(driver, Brand);
		}catch (TimeoutException e) {
			System.out.println("*****No need to logout. Going to click forgot password*****");
		}
		
		Thread.sleep(1000);
		driver.navigate().to(TraderURL);
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Country']")));
		//System.out.println(TraderName);
		
		//Click on reset password. This will navigate to Forgot Mobile Password page (the page you can get SMS code).
		driver.findElement(By.xpath("//a[contains(text(),'Forgot Password')]")).click();
		Thread.sleep(1000);

		//verify it goes to GetSMSCode page.
		wait01.until(ExpectedConditions.urlContains("/forgetPasswordMobile"));
		
		 //Seperate phonecode and phone number from TraderName
		 phoneCode = TraderName.split(" ")[0];
		 phoneNumber = TraderName.split(" ")[1];
		
		//Input in SMScode page
		funcSMSPage(phoneCode,  phoneNumber, Brand, TestEnv,isErrorCheck);
				
		//verify it goes to reset Password page.
		wait01.until(ExpectedConditions.urlContains("/resetPasswordMobile"));
		Thread.sleep(1000);
		//Fill in Reset Password Page
		funcResetPwd(defaultPwd, Brand,isErrorCheck);
		
		//Already in logged in status. Logout to verify the new password works
		Thread.sleep(1000);
		Utils.funcLogOutCP(driver,Brand);
		Thread.sleep(1000);
		//refresh to Mobile Login Page again:
		driver.navigate().refresh();
		driver.navigate().to(TraderURL);
		Thread.sleep(1000);
		Utils.funcMobileLogInCP(driver,  phoneCode, phoneNumber, defaultPwd, Brand);
		Utils.waitUntilLoaded(driver);
		

		if(Brand.equalsIgnoreCase("vt"))
		{
			Utils.funcIsStringEquals(driver.findElement(By.cssSelector("h2")).getText(),"LIVE ACCOUNTS",Brand);
		}else
		{
			Utils.funcIsStringEquals(driver.findElement(By.cssSelector("h3")).getText(),"LIVE ACCOUNTS",Brand);
		}
	
		System.out.println("Login successfully using NEW password" + defaultPwd);
		
		/*Yanni: comment out this paragraph temporarily. As in CPMobileLogin, we use mobile phone as the trader name not email. With moible phone number,
		 * Utils.funcResetUserLoginPW(driver3, AdminURL, TraderName,  AdminName,AdminPass,Brand) doesn't work. It requires an email.
		 * To do this, we need a new api to get user's email via mobile number.
		 * 
		 * 
		 * System.out.println("=========Going to reverse the password of " + TraderName
		 * + " to 123Qwe============"); //Get admin cookie thru API in test env or
		 * browser in beta/prod env ChromeOptions options3=new ChromeOptions();
		 * options3.addArguments("--incognito");
		 * System.setProperty("webdriver.chrome.driver", Utils.ChromePath); WebDriver
		 * driver3 = new ChromeDriver(options3);
		 * 
		 * Utils.funcResetUserLoginPW(driver3, AdminURL, TraderName,
		 * AdminName,AdminPass,Brand);
		 
		driver3.quit();
		*/
	}
	
	
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser() throws Exception
	{
		//driver.navigate().refresh();
		driver.quit();
	}

	public void funcSMSPage(String phoneCode, String phoneNumber, String Brand, String testEnv, Boolean isErrorCheck) throws Exception
	{
		
		String dbName = Utils.getDBName(Brand)[0];
		String encodedPhone = DecryptUtils.encode(phoneNumber);
		String sqlSel = "Select body from tb_sms_history where mobile_number = '" + encodedPhone + "' order by gateway_sent_time desc limit 1;";
		String sqlResult, smsCode;
		List <WebElement> weErrorMessage=null;
		String expectedErrorMessage;
		String errorMessage = "";
		
		//Input phoneCode & phoneNumber
		driver.findElement(By.xpath("//input[@placeholder='Country']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).clear();
		
		//If doing error message check
		if(isErrorCheck==true)
		{
			//Send wrong phone code and phone number
			System.out.println("\nSMS Page: Input wrong phone code and phone number...");
			driver.findElement(By.xpath("//input[@placeholder='Country']")).sendKeys("1111");
			driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).click();
			driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys("555555555");
			
			//Click Send SMS Code button
			driver.findElement(By.xpath("//button[@class='el-button btn_blue btn_sms el-button--primary']")).click();
			Thread.sleep(100);
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.el-message.el-message--warning>p")));
			//Verify error message
			weErrorMessage= driver.findElements(By.cssSelector("div.el-message.el-message--warning>p"));
			expectedErrorMessage= "Unable to send SMS code to your phone";
			
			Assert.assertTrue(weErrorMessage.size()>0, "No Error Message pops up when sending to invalid mobile phone.");
			
			//When mobile phone not registered, check error message
			if (weErrorMessage.size()>0)
			{
				errorMessage = weErrorMessage.get(0).getText();
				Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "Login error message expected: " + expectedErrorMessage + ", but got: " + errorMessage);
				System.out.println("There is error message: " + errorMessage);
			}
			
			//Wait 60S until next SMS can be sent
			System.out.println("Wait for 60 seconds until SMS can be sent again...");
			Thread.sleep(60000);
		
		}
		
		//Input correct phoneCode & phoneNumber
		driver.findElement(By.xpath("//input[@placeholder='Country']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Country']")).sendKeys(phoneCode);
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys(phoneNumber);
		
		//Click Send SMS Code button
		driver.findElement(By.xpath("//button[@class='el-button btn_blue btn_sms el-button--primary']")).click();
		//button[@class='el-button btn_blue btn_sms el-button--primary']
		Thread.sleep(10000);  //Give program a bit time to write db.
		
	
		//Get SMS code
		//Search from DB via encoded phone number and return the latest one record
		
		//db_au_global_vgp.tb_sms_history: country_phone_code, mobile_number, body, status, gateway_sent_time,		
		
		//System.out.println(sqlSel);
		sqlResult = DBUtils.funcReadDBReturnAll(dbName, sqlSel, testEnv);
		
		if(sqlResult.length()==0)
		{
			System.out.println("Can't find SMS history via mobile_number =" + encodedPhone + ". Input Phone number is: " + phoneNumber);
			Assert.assertTrue(false, "Can't find SMS history via mobile_number =" + encodedPhone + ". Input Phone number is: " + phoneNumber);
		}
				
		//System.out.println(sqlResult);
		
		//Extract SMS code
		smsCode = sqlResult.replaceAll("[^0-9]","");
		if (smsCode.trim().length()==0)
		{
			System.out.println("No SMS Code in DB, sending SMS Code: 1234");
			smsCode = "1234";
		}
		
		
		  if(isErrorCheck == true) { //Input SMS code
		  System.out.println("\nSMS Page: Input a wrong SMS code"); 
		  driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		  driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("1234567");
		  
		  //Click Submit button 
		  driver.findElement(By.xpath("//button[@class='el-button btn_blue el-button--primary']")).click();
		  Thread.sleep(100);
		  wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.el-message.el-message--error>p")));
		  //When SMS code not accepted by system, check error message weErrorMessage =
		  weErrorMessage=driver.findElements(By.cssSelector("div.el-message.el-message--error>p"));
		  expectedErrorMessage = "Incorrect SMS code";
		  
		  Assert.assertTrue(weErrorMessage.size()>0, "No error message pops up when sending WRONG SMS code");
		  
		  if (weErrorMessage.size()>0) 
		  { 
			  errorMessage =  weErrorMessage.get(0).getText();
			  Assert.assertTrue(errorMessage.contains(expectedErrorMessage),  "Login error message expected: " + expectedErrorMessage + ", but got: " +
		  errorMessage); 			  
			  
			  System.out.println("There is error message: " + errorMessage);
	
		  } }
		 
		
		//Input SMS code
		System.out.println("\nInput a right SMS code to go to Reset Password page");
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(smsCode);
						
		//Click Submit button
		driver.findElement(By.xpath("//button[@class='el-button btn_blue el-button--primary']")).click();
		
		//Wait until the loading icon disappears. System will navigate to Reset password automatically.
		Thread.sleep(2000);
		

		
	}
	
	public void funcResetPwd(String defaultPwd, String Brand, Boolean isErrorCheck) throws Exception
	{
				
		String errorMessage, expectedMessage = "Two inputs do not match";
		
		//div.change_form//input: no1 is password, no2 is confirm password.
		
		
		driver.findElements(By.xpath("//input[@class='el-input__inner']")).get(0).sendKeys(defaultPwd);
		
		//If do error check, input a different pwd and then click submit. check the error message. Else, do the normal function check.
		if(isErrorCheck == true)
		{
			
			System.out.println("\nReset Password page: input a different Confirm Password");
			//Input a different password in Confirmation field
			driver.findElements(By.xpath("//input[@class='el-input__inner']")).get(1).sendKeys(defaultPwd.concat("123"));
		
			//Click Submit button
			driver.findElement(By.xpath("//span[contains(text(), 'SUBMIT')]")).click();
			Thread.sleep(500);
			
			errorMessage = driver.findElement(By.cssSelector("div.el-form-item__error")).getText();
			Thread.sleep(500);
			
			System.out.println("Error message:" + errorMessage);
			
			Utils.funcIsStringEquals(errorMessage, expectedMessage, Brand);
			//********************************
			
		}else
		{
			System.out.println("System is going to reset with the default password: 1234Qwer");
			driver.findElements(By.xpath("//input[@class='el-input__inner']")).get(1).clear();
			driver.findElements(By.xpath("//input[@class='el-input__inner']")).get(1).sendKeys(defaultPwd);
			
			//Click Submit button
			driver.findElement(By.xpath("//button[@class='el-button btn_blue el-button--primary']")).click();
			Thread.sleep(500);
			
			//Verify message is successful message		
			Utils.funcIsStringContains(driver.findElement(By.cssSelector("p.success_info")).getText(), "Your password has been updated. You may now use your new password to access Client Portal.", Brand);
			Thread.sleep(500);
			
			//Click Back to Home Page
			driver.findElement(By.xpath("//a[contains(text(), 'Back To Home Page')]")).click();
			wait01.until(ExpectedConditions.urlContains("/home"));
		}
		
	}

	//Test if reset password link works
	@Test(alwaysRun = true )
	@Parameters(value = {"Brand", "TraderURL","TraderName", "TraderPass","TestEnv","AdminURL","AdminName","AdminPass"})
	public void ResetPwdErrorCheck(String Brand, String TraderURL, String TraderName, String TraderPass, String TestEnv,String AdminURL, String AdminName,String AdminPass) throws Exception
	{
		
		String defaultPwd = "1234Qwer";
		Boolean isErrorCheck= true; 
		
		 //Seperate phonecode and phone number from TraderName
		 phoneCode = TraderName.split(" ")[0];
		 phoneNumber = TraderName.split(" ")[1];
		
		
		driver.navigate().to(TraderURL);
		
		//If session is already logged in, logged out first.
		try {
			Utils.funcLogOutCP(driver,Brand);
		}catch (TimeoutException e) {
			System.out.println("*****No need to logout. Going to click forgot password*****");
		}
		
		Thread.sleep(1000);
		driver.navigate().to(TraderURL);
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Country']")));
		//System.out.println(TraderName);
		
		  //Login with wrong password and check error message;
		  System.out.println("\nLogIn Page: input wrong password");
		  funcLoginCheckError(driver, phoneCode, phoneNumber, TraderPass.concat("111"),  Brand);
		  
		//Login with wrong mobile phone and check error message: 
		  driver.navigate().to(TraderURL); 
		  Thread.sleep(1000); 
		  
		  System.out.println("\nLogIn Page: input wrong Mobile Number");
		  funcLoginCheckError(driver, phoneCode,  phoneNumber.concat("111"), TraderPass, Brand);
		 
		
		
		//Click on reset password. This will navigate to Forgot Mobile Password page (the page you can get SMS code).
		driver.findElement(By.xpath("//a[contains(text(),'Forgot Password')]")).click();
		Thread.sleep(1000);
	
		//verify it goes to GetSMSCode page.
		wait01.until(ExpectedConditions.urlContains("/forgetPasswordMobile"));
		

		//Input in SMScode page
		funcSMSPage(phoneCode,  phoneNumber, Brand, TestEnv,isErrorCheck);
						
		//verify it goes to reset Password page.
		wait01.until(ExpectedConditions.urlContains("/resetPasswordMobile"));
		Thread.sleep(1500);
		
		//Fill in Reset Password Page
		funcResetPwd(defaultPwd, Brand,isErrorCheck);	
	
		
	}
	
	void funcLoginCheckError(WebDriver driver, String phoneCode, String phoneNumber, String passWord, String Brand) throws Exception
	{
		
		String errMessage;
		String expectedMessage = "Incorrect username or password";
		WebDriverWait wait02=new WebDriverWait(driver,Duration.ofSeconds(2));
		//change language to English
		if (Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg")||Brand.equalsIgnoreCase("vt"))
		{
			driver.findElement(By.xpath("//li[contains(@class,'flag')]/div/img")).click();			
			wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'English')]"))).click();
		}
		
		driver.findElement(By.xpath("//input[@placeholder='Country']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Country']")).sendKeys(phoneCode);
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys(phoneNumber);
		
		driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(passWord);			
		
		//click login button
		Thread.sleep(300);
		//driver.findElement(By.cssSelector("button.el-button.btn_red.el-button--default")).click();
		driver.findElement(By.xpath("//span[contains(text(), 'LOGIN')]")).click();
		Thread.sleep(100);
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.el-message.el-message--error>p")));
		errMessage = driver.findElement(By.cssSelector("div.el-message.el-message--error>p")).getText();
		
		System.out.println("Error message is:" + errMessage);
		Utils.funcIsStringEquals(errMessage, expectedMessage, Brand);
		
		//Wait until the error message disappears
		Thread.sleep(2000);
				
		
	}
}
