 package vantagecrm;


import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
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
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import clientBase.cpLogIn;
import io.github.bonigarcia.wdm.WebDriverManager;


/*
 * This class is used to request additional accounts and transfer money between additional accounts
 */

public class CPLogInPage {
	
	WebDriver driver;
	CPAccountManagement actManageCls;
	
	WebDriverWait wait01;
	WebDriverWait wait02;
	int waitIndex=1;
	Select t;
	
	
	
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
		 
		  context.setAttribute("driver", driver);
	
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver,Duration.ofSeconds(20));
		  
		  actManageCls = new CPAccountManagement();
		  actManageCls.driver = driver;

	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
	  	  //Login AU CP
			driver.get(TraderURL);

			Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);	
			Thread.sleep(waitIndex*2000);

			Utils.funcVerifyHomePageLiveAccounts(Brand,wait01);
				
	}
	
	/*Yanni: Test All Links of LOGIN PAGE
	1. Links can work and won't get 404 error
	2. Links navigate to correct website. Like AU all go to VantageFX, PUG goes to puprime, etc
	 */	
	@Test(alwaysRun = true)
	@Parameters(value = {"Brand", "TraderURL"})
	public void testAllLinks(String Brand, String TraderURL) throws Exception
	{

		driver.navigate().to(TraderURL);
		
		WebElement webMain = driver.findElement(By.cssSelector("main.el-main"));
		List<WebElement> linkColl = webMain.findElements(By.tagName("a"));
		
		SoftAssert sa = new SoftAssert();
		int i=0;
		String href;
		String bodyClass;
		String error404 = "error404";
		
		
		
		if(linkColl.size()==0)
		{
			System.out.println(Brand.toUpperCase() + " Client Portal Login Page has no links to test.");
			Assert.assertTrue(linkColl.size()>0, "Client Portal Login Page has no links to test.");
		}
		
		for(i = 0; i<linkColl.size(); i++)
		{
			if(i != 0)
			{
				webMain = driver.findElement(By.cssSelector("main.el-main"));
				linkColl = webMain.findElements(By.tagName("a"));
			}
			
			//System.out.println(linkColl.size());
			
			href = linkColl.get(i).getAttribute("href");
			
			System.out.println("No" + i + ":" + href);
			
			//If link starts with 'http', test whether link directs to correct website. Don't cover mailto:
			
			if(href!=null && href.startsWith("http"))
			{
				//If link navigates to vantagefx website, need to test whether it is going to the correct one: ASIC or CIMA. Otherwise, just test the link doesn't return 404 error
				if(Utils.ParseInputURL(href).contains("www.vantagefx.com")) 
				{
					//Check whether 
					switch(Brand.toLowerCase())
					{
						case "au":
							sa.assertTrue(href.contains("." + Brand.toLowerCase()), "Link " + href + " should contain .au"  + " wtih brand " + Brand);
							break;
							
						case "ky":
						case "vfsc":
						case "vfsc2":							
						case "regulator2":
							sa.assertFalse(href.contains("." + Brand.toLowerCase()), "Link " + href + " should NOT contain .au"  + " wtih brand " + Brand);
							break;
							
						case "pug":
							sa.assertTrue(href.contains(Brand.toLowerCase()), "Link " + href + " should contain pug"  + " wtih brand " + Brand);
							break;
							
						case "vt":
							sa.assertTrue(href.contains(Brand.toLowerCase()), "Link " + href + " should contain vtmarkets"  + " wtih brand " + Brand);
							break;
							
							default:
								System.out.println(Brand + " is NOT supported.");
							
					}
				}
				
					
				//Click the link and link will open in the current page
				
				try
				{				
					linkColl.get(i).click();
					Thread.sleep(3000);
					System.out.println("Page Title: " + driver.getTitle());
					System.out.println();
					
					bodyClass = driver.findElement(By.tagName("body")).getAttribute("class");
					
					if(bodyClass.equalsIgnoreCase(error404))
					{
						sa.assertFalse(bodyClass.equalsIgnoreCase(error404), "Body class is " + bodyClass + " with href " + href);
					}
					
					
					driver.navigate().to(TraderURL);
					Thread.sleep(1000);;
					
				}catch(ElementNotInteractableException e)
				{
					System.out.println("Element not interactable. Element infor: " + linkColl.get(i));
				}
				

			}
			
	
		}
		
		sa.assertAll();
	}
	
	
	//Test if reset password link works
	@Test(alwaysRun = true)
	@Parameters(value = {"Brand", "TraderURL","TraderName", "TestEnv","AdminURL","AdminName","AdminPass"})
	public void testResetPWLink(String Brand, String TraderURL, String TraderName, String TestEnv,String AdminURL, String AdminName,String AdminPass) throws Exception
	{
		
		String handle,result,url;
		WebDriver driver3;
		String successSel;
		
		cpLogIn loginCls = new cpLogIn(driver,Brand);

		driver.navigate().to(TraderURL);
		
		//Yanni: cmment out this paragraph as no LogIn is required for this class. 
		/*
		 * try {
		 * Utils.funcLogOutCP(driver, Brand);
		 * }catch (TimeoutException e) {
		 * System.out.println("*****No need to logout. Going to click forgot password*****");
		 * }
		 * 
		 * Thread.sleep(1000);
		 */

		//After InputEmail element is visible, click ResetPassword Link
		wait02.until(ExpectedConditions.visibilityOf(loginCls.getInputEmail()));
		loginCls.getRestPwdLink().click();
		
		Thread.sleep(3000);
		//Reset password page: Input Email
		driver.findElement(By.xpath("//input[@class='el-input__inner']")).sendKeys(TraderName);		
		Thread.sleep(500);		
		driver.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]")).click();
		
		Utils.waitUntilLoaded(driver);
		
		//Verify return message
		switch(Brand.toLowerCase())
		{
			case "vt":
				successSel="div.success_content>div.main>p";
				break;
				
			case "fsa":
			case "svg":
				successSel="div#forgetPassword p";
				break;
				
				default:
					successSel="p.success_info:nth-child(2)";			
			
		}
	
		Utils.funcIsStringContains(driver.findElement(By.cssSelector(successSel)).getText(), "We have just sent you an email with instructions to reset your password.", Brand);
		Thread.sleep(500);
		
		//Back to Home Page
		switch(Brand.toLowerCase())
		{
			case "vt":
				driver.findElement(By.xpath("//a[@class='el-button blue_button']")).click();
				break;
				
			case "fsa":
			case "svg":
				driver.findElement(By.xpath("//a[@class='el-button']")).click();
				break;
				
				default:
					driver.findElement(By.xpath("//a[@class='el-button btn_blue']")).click();
		}
			
		//Get current window handler
		handle=driver.getWindowHandle();
		
		//Search tb_mail_send_log to get the password reset link. Call the function in AccoutManagement class
		result = actManageCls.funcQueryMailDB(TraderName, Brand, TestEnv);		
		
		//remove the curly braces
		result = result.substring(7, result.length()-2); 
		String[] sepa=result.split(",");
		url = sepa[5].substring(15, sepa[5].length()-1);
		//replace "\u003d" with "="
		url= url.replace("\\u003d", "=");
		System.out.println("Forgot password url is: "+url);
		
		//Open the reset pw link in new browser and reset passwd
		funcInputResetPassword(url, "1234Qwer",Brand);
		
		
		//Verify login in previous tab
		driver.switchTo().window(handle);
		Utils.funcLogInCP(driver, TraderName, "1234Qwer", Brand);	
		Thread.sleep(waitIndex*2000);
		
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);
		
	
		System.out.println("Login successfully using NEW password: 1234Qwer");
		
		System.out.println("=========Going to reverse the password of " + TraderName + " to 123Qwe============");
		//Get admin cookie thru API in test env or browser in beta/prod env
		ChromeOptions options3=new ChromeOptions();
		options3.addArguments("--incognito");			
		driver3 = new ChromeDriver(options3);
		
		Utils.funcResetUserLoginPW(driver3, AdminURL, TraderName, AdminName,AdminPass,Brand, TestEnv);
		
	}
	
	//Yanni:  This function is almost the same with the one in CPAccountManagement with only 1 difference in Success_Info element
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
        
        driver2.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]")).click();
        Thread.sleep(2000);
        
        switch(Brand.toLowerCase())
        {
        case "vt": 
            Utils.funcIsStringContains(driver2.findElement(By.xpath("//div[@class='success_content']/div[@class='main']/p")).getText(), "Your password has been updated.", "");
            
            Thread.sleep(1000);
            driver2.findElement(By.xpath("//a[@class='el-button blue_button']")).click();
        	break;
        	
        case "fsa":
        case "svg":
            Utils.funcIsStringContains(driver2.findElement(By.xpath("//div[@class='reset_success']//p")).getText(), "Your password has been updated.", "");
            break;
        	
        	default:

        	    Utils.funcIsStringContains(driver2.findElement(By.xpath("//p[@class='success_info']")).getText(), "Your password has been updated.", "");
        	    
                Thread.sleep(1000);
                driver2.findElement(By.xpath("//a[@class='el-button btn_blue']")).click();
        }
        
 

        driver2.quit();
	}
	@AfterClass(alwaysRun=true)
	void ExitBrowser() throws Exception
	{
		//driver.navigate().refresh();
		driver.quit();
	}

}
