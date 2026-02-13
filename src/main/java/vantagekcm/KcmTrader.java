package vantagekcm;

import static org.testng.Assert.assertTrue;


import java.time.Duration;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * This class is to test all Deposit types in Trader
 */

public class KcmTrader {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	Select t;  //Account Dropdown
	Random r=new Random(); 
	int j; //Select index
	WebDriverWait wait01;
	WebDriverWait wait02;
	
	String depositAmount = "20000.66";
	

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value="TestEnv")
	public void LaunchBrowser(String TestEnv, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
    	  driver = new ChromeDriver();	  
    	  context.setAttribute("driver", driver);        
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
			  depositAmount = "2000.66";
		  }
		  
		  wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver,Duration.ofSeconds(20));
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
	void TraderLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
 		
  	  //Login AU Trader
		driver.get(TraderURL);

		Utils.funcLogInKcmTrader(driver, TraderName, TraderPass, Brand);	
		Thread.sleep(waitIndex*2000);
		
		Assert.assertTrue(driver.getTitle().equals("My Account")||driver.getTitle().equals("Live Accounts"));
		
		if(Brand.equalsIgnoreCase("kcm"))
		{
			Utils.handlePopup(driver, true);
		}
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TestEnv", "LogServer"})
	//Credit card payment
	void CCDeposit(String testEnv, String logServer) throws Exception
	{
		String mt4Account;
		driver.navigate().refresh();
		
		driver.findElement(By.xpath("//div[@id='memberBtnMenu']//div[1]//a[1]")).click();
		driver.findElement(By.xpath("//h3[contains(text(),'Credit Card')]")).click();
		
		Thread.sleep(1000);
		Assert.assertEquals(driver.getTitle(),"Credit or Debit Card");
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("mt4account")));
		j=t.getOptions().size();
		assertTrue(j>0);
		
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);
		
		//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
		Thread.sleep(1000);
		
		//Input deposit amount and click Submit
		driver.findElement(By.id("Amount")).sendKeys("10000");
		Thread.sleep(1000);
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSubmit"))).click();
		Thread.sleep(1000);
		
		//click PAYMENT button
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("button"))).click();
		Thread.sleep(1000);
		
		//Input the card information
		driver.findElement(By.id("cardName")).sendKeys("card name");
		driver.findElement(By.id("cardNumber")).sendKeys("1000200030004000");
		driver.findElement(By.id("CVV")).sendKeys("123");
		Thread.sleep(1000);
		
		//Select card expiry randomly
		Random r=new Random();
		t=new Select(driver.findElement(By.id("expiry_month")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);

		t=new Select(driver.findElement(By.id("expiry_year")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+2);
		
		//check the acknowledgement.
		driver.findElement(By.id("accept")).click();
		Thread.sleep(500);
		
		//Click submit
		driver.findElement(By.id("btnSubmit")).click();
		
		//Click confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		System.out.print("Credit Card deposit succeffully!");
		
		//Check merchant number and print out
		Utils.checkLogBySSH(logServer);

	}	

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)	
	//International bank wire transfer supports 4 currencies: AUD, USD, EUR, GBP
	void I18NDeposit() throws Exception
	{
		driver.findElement(By.linkText("My Account")).click();
		driver.findElement(By.xpath("//div[@id='memberBtnMenu']//div[1]//a[1]")).click();
		driver.findElement(By.xpath("//a[@class='account_content_box account_content_box1']")).click();
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("mt4account")));
		j=t.getOptions().size();
		assertTrue(j>1, "No Available Accounts for deposit.");
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		
		//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
		Thread.sleep(1000);
		
		//Input deposit amount
		driver.findElement(By.id("amount")).sendKeys(depositAmount);
		driver.findElement(By.id("remark")).sendKeys("Test International Bank Transfer Deposit "+depositAmount);
		
		
		//Input receipt
		driver.findElement(By.name("upload_file")).sendKeys(Utils.workingDir+"//proof.png");
		Thread.sleep(1000);
		wait01.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector("a.btn.btn-default.kv-fileinput-upload.fileinput-upload-button")))).click();
		Thread.sleep(1000);
		
	
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("h1.title")).getText(),"Application Completed");

	}
	
	@Test(dependsOnMethods="TraderLogIn", invocationCount=1)
	@Parameters(value= {"Brand"})
	void RequestAddiAccount(String Brand) throws Exception
	{
		
		driver.navigate().refresh();
		
		//Click My Accounts link
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("MY ACCOUNTS"))).click();	
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button.btn.btn-primary")).click();
		

		//Agree with terms and click SUBMIT
		driver.findElement(By.id("applyMt4Account")).click();
		wait01.until(ExpectedConditions.elementToBeClickable(By.id("chk_agree"))).click();
		wait01.until(ExpectedConditions.elementToBeClickable(By.id("chk_agree2"))).click();
		driver.findElement(By.id("button")).click();

	}

	@Test(dependsOnMethods="TraderLogIn")
	void ResetMT4Pwd() throws Exception
	{
		String message;
		driver.navigate().refresh();
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Trading Account"))).click();	
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Apply Trading Account"))).click();
		
		//Click reset password icon(lock icon)
		driver.findElements(By.cssSelector("a.resetMt4Password")).get(0).click();
		Thread.sleep(1000);
		
		//Click Forgot password link
		driver.findElement(By.cssSelector("a.forget_the_password_link.col-sm-4.forget_addcontr")).click();
		
		Thread.sleep(1000);

		driver.findElements(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).get(2).click();
		message=wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		Assert.assertTrue(message.contains("The reset password email has been sent to your email, please check your email."));
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	@Parameters(value= {"OldPwd", "NewPwd"})
	void ChangeMT4PWD(String OldPwd, String NewPwd)
	{
		String message;
		
		for(int i=0;i<2;i++)
		{
			driver.navigate().refresh();
			wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Trading Account"))).click();	
			wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Apply Trading Account"))).click();
			//Click reset password icon(lock icon)
			driver.findElements(By.cssSelector("a.resetMt4Password")).get(0).click();
			
			if(i==0)
			{
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(OldPwd);
				driver.findElement(By.id("password1")).sendKeys(NewPwd);
				driver.findElement(By.id("password2")).sendKeys(NewPwd);		
			} else if(i==1)
			{
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(NewPwd);
				driver.findElement(By.id("password1")).sendKeys(OldPwd);
				driver.findElement(By.id("password2")).sendKeys(OldPwd);		
			}
				
			driver.findElements(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).get(0).click();
			
			message=wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
			Assert.assertTrue(message.contains("Modify Success"));
		}
		
		
	}
	
}
