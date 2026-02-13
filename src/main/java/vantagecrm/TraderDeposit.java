package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
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

/*
 * This class is to test all Deposit types in Trader
 */

public class TraderDeposit {
	
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
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);  
    	  context.setAttribute("driver", driver);          //Added by Yanni on 5/15/2019
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

		Utils.funcLogInTrader(driver, TraderName, TraderPass, Brand);	
		Thread.sleep(waitIndex*2000);
		
		Assert.assertTrue(driver.getTitle().equals("My Account")||driver.getTitle().equals("Live Accounts"));
		
		
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Credit card payment
	void CCDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{

		String pageTitle,mt4Account = "",payTo="";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Credit or Debit Card')]")).click();
		
		Thread.sleep(1000);
		Assert.assertEquals(driver.getTitle(),"Credit Card");
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>0);
		
		/*j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			System.out.println("mt4Account is:"+mt4Account);
			//if(funcCheckAccoutGroup(TraderURL, coo, mt4Account))
		    if(funcCheckAccoutGroup(Brand, TestEnv,mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount and click Submit
				driver.findElement(By.id("deposit_amount")).sendKeys("2000");
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Credit Card or Debit Card Deposit 2000");
				Thread.sleep(1000);
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmBtn"))).click();
				Thread.sleep(1000);
				
				//click PAYMENT button
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.btn_red"))).click();
				Thread.sleep(1000);
				
				//Assert navigating to NAB payment page
				pageTitle=driver.getTitle();
				Assert.assertTrue(pageTitle.contains("Make a Secure Payment to"), "Credit Card Payment Page is not the expected one.");
				
				//Verify Pay To info
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[2]//td[1]//table[1]//tbody[1]//tr[1]//td[2]")));
				payTo = driver.findElement(By.xpath("//tr[2]//td[1]//table[1]//tbody[1]//tr[1]//td[2]")).getText();
				
				if (TestEnv.equals("test")) {
					Utils.funcIsStringContains(payTo, "NAB Transact Test Account", Brand);
				}else {
					switch(Brand)
					{
						case "au":
							Utils.funcIsStringContains(payTo, "VANTAGE GLOBAL PRIME PTY LTD", Brand);
							break;
						case "ky":	
						case "vt":
						case "pug":
						default:
							Utils.funcIsStringContains(payTo, "ADVANTAGE COLLEGE", Brand);
							break;
					}
				}
				/*Here should input the card information
				
				
				*/
				//Update deposit audit status in mysql
				driver.navigate().to(TraderURL);
				
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				break;
			}else {
				System.out.println("Not able to check the account group or account group not TEST group! Not able to make deposit to these accounts");
			}
		}
		
		

	}	

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)	
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//International bank wire transfer supports all 8 currencies: AUD, USD, EUR, GBP, SGD, JPY, NZD, CAD
	void I18NDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		String mt4Account = "";
		String coo = driver.manage().getCookies().toString();
		if((Brand.equalsIgnoreCase("vt"))||(Brand.equalsIgnoreCase("pug")))
		  {
			  depositAmount = "10000.66";
		  }
		
		driver.navigate().to(TraderURL);
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'International SWIFT Deposit')]")).click();
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1, "No Available Accounts for deposit.");
		/*j=r.nextInt(j-1);
		t.selectByIndex(j+1);*/
		
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test International Bank Transfer Deposit "+depositAmount);
				
				
				//Input receipt
				driver.findElement(By.name("upload_file")).sendKeys(Utils.workingDir+"//proof.png");
				Thread.sleep(1000);
				wait01.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector("a.btn.btn-default.kv-fileinput-upload.fileinput-upload-button")))).click();
				Thread.sleep(1000);
				
			
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
				Thread.sleep(1000);
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				break;
			}else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
		
		}
	}
	
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Bank Wire Transfer: Australia Deposit. The account currency must be AUD
	void AuBankDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		String mt4Account = "";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Australia Deposit')]")).click();
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		
		j=t.getOptions().size();
		assertTrue(j>0);
		
		/*j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				Thread.sleep(1000);
				
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				wait01.until(ExpectedConditions.presenceOfElementLocated(By.id("deposit_amount")));
				
				//Input deposit amount
				driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test AU Bank Transfer Deposit "+depositAmount);
				driver.findElement(By.id("confirmBtn")).click();
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				break;
			}else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
		}
		
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//BPay deposit. The account owner's resident country must be Australia and account currency must be AUD
	void BPayDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String s;
		String mt4Account = "";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'BPAY')]")).click();
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		
		j=t.getOptions().size();
		assertTrue(j>0);
		
		//Select an account which has a bpay number
		while(j>=2)
		{
			t.selectByIndex(j-1);
			mt4Account = t.getOptions().get(j-1).getText();
			//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
			Thread.sleep(1000);
			
			s=driver.findElement(By.id("deposit_Number")).getAttribute("value");
			
			if((!(s==null||s.equals("")))&&(funcCheckAccoutGroup(Brand, TestEnv, mt4Account)))
			{
				break;
			
			}else
			{
				j--;
			}
			
		}

		
		//Input deposit amount
		driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
		driver.findElement(By.id("deposit_Note")).sendKeys("Test BPay Deposit "+depositAmount);
		driver.findElement(By.id("confirmBtn")).click();
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Polipay deposit. The account owner's resident country must be Australia and account currency must be AUD
	void PoliDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String handle;
		Set<String> handleS;
		String mt4Account = "";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to POLi Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'POLi Payments')]")).click();
		
		//Test MAKE PAYMENT NOW button to ensure it can navigate to Poli
		handle=driver.getWindowHandle();
		driver.findElement(By.linkText("MAKE PAYMENT NOW")).click();
		Thread.sleep(2000);
		handleS=driver.getWindowHandles();
		for(String s:handleS)
		{
			if(!s.equals(handle))
			{
				Assert.assertEquals(driver.getTitle(), "POLi Payments");
				driver.switchTo().window(s).close();
				driver.switchTo().window(handle);
			}
		}

		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		
		j=t.getOptions().size();
		assertTrue(j>0);
		
		/*//Randomly select one account
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test POLi pay Transfer Deposit "+depositAmount);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Skrill deposit. 
	void SkrillDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		String mt4Account = "";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to POLi Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'Skrill/Moneybookers')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		
		j=t.getOptions().size();
		assertTrue(j>0);
		
		/*//Randomly select one account
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Email")).sendKeys("testsrill@test.com");
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Skrill/MoneyBooker Deposit "+depositAmount);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","TestEnv", "Brand"})
	//Neteller deposit. 
	void NetellerDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{

		String handle,mt4Account="";
		Set<String> handleS;
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to POLi Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'Neteller Deposit')]")).click();
		
		//Test MAKE PAYMENT NOW button to ensure it can navigate to Poli
		handle=driver.getWindowHandle();
	
	
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		
		j=t.getOptions().size();
		assertTrue(j>0);
		
		/*//Randomly select one account
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Neteller Deposit "+depositAmount);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Assert success
				Assert.assertTrue(driver.getTitle().equals("Neteller Deposit")||driver.getTitle().equals("Neteller"));
				//Click Payment. Will navigate to a new page and make payments
				driver.findElement(By.id("button")).click();
				Thread.sleep(2000);

				handleS=driver.getWindowHandles();
				for(String s:handleS)
				{
					if(!s.equals(handle))
						{
							driver.switchTo().window(s);
						}
				}
				
				/*
				 Neteller payment
				 Assert.assertEquals(driver.getTitle(),"NETELLERgo!");
				 driver.findElement(By.cssSelector("i.icon-card-neteller")).click();
				 driver.findElement(By.id("login_identity")).sendKeys("yanni.qi@vantagefx.com");
				 driver.findElement(By.id("password")).sendKeys("");
				 driver.findElement(By.id("checkout_continue_btn")).click();

				 */	 
				 
				
				//After Neteller payment, close the page and go back to the original deposit page	
				driver.close();
				driver.switchTo().window(handle);
				
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}
	
	@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		Utils.funcLogOutTrader(driver);
		driver.quit();
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Polipay deposit. The account owner's resident country must be Australia and account currency must be AUD
	void BrokerDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String handle;
		Set<String> handleS;
		String mt4Account = "";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to POLi Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'Broker to Broker')]")).click();
		
		//Test MAKE PAYMENT NOW button to ensure it can navigate to Poli
		handle=driver.getWindowHandle();
		driver.findElement(By.cssSelector("a.btn_red")).click();
		Thread.sleep(2000);
		handleS=driver.getWindowHandles();
		for(String s:handleS)
		{
			if(!s.equals(handle))
			{
				driver.switchTo().window(s);
				
				System.out.println(driver.getCurrentUrl());
			
				assertTrue(driver.getCurrentUrl().contains("VFX-Form_FINANCIAL-SERVICES-PROVIDER-TRANSFER-FORM.pdf") 
						|| driver.getCurrentUrl().contains("Vantage-FX-Broker-to-Broker-form.pdf"), "PDF document is wrong.");
			
				
				driver.switchTo().window(s).close();
				driver.switchTo().window(handle);
			}
		}
	
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		
		j=t.getOptions().size();
		assertTrue(j>0);
		
		/*//Randomly select one account
		j=r.nextInt(t.getOptions().size()-1);
		t.selectByIndex(j+1);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_amount")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test broker to broker Transfer Deposit "+depositAmount);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"DEPOSIT SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Unionpay only supports USD currency
	void UnionPayDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{
	
		String mt4Account="",rmbAmount="";
		
		depositAmount="5000.00";
		String coo = driver.manage().getCookies().toString();
		driver.navigate().to(TraderURL);
		
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Union Pay Deposit')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>0);
		
		//Randomly select one account
		j=r.nextInt(j-1);
		/*t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);
		*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Union Pay Deposit "+depositAmount);
				Thread.sleep(1000);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Assert navigation to Union Pay page
				Assert.assertEquals(driver.getTitle(),"Union Pay Deposit");
				
				rmbAmount = driver.findElement(By.cssSelector(" div.rowList:nth-child(9) div > p")).getText();
				//Click Payment. Will navigate to a new page and make payments
				driver.findElement(By.cssSelector("input.btn_red")).click();
				Thread.sleep(3000);
				
				switch(TestEnv) {
				
				case "beta":
				case "prod":
				
						Assert.assertEquals(driver.getCurrentUrl(), "https://deposit.paylomo.net/pay.php");
						break;
			
					
				case "test":
					//Choose mockbank
					/*driver.findElement(By.id("TESTBC")).click();
					driver.findElement(By.cssSelector("div.btn input")).click();
					Thread.sleep(1000);
					
					//Make payment
					driver.findElement(By.cssSelector("button.myButton")).click();
					Thread.sleep(2000);*/
					break;

					
				}
				
				//After payment, navigate back to original page
				driver.navigate().to(TraderURL);
			
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account,TestEnv,Brand);
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Fasapay only supports USD
	void FasapayDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{
	
		
		String mt4Account="";
		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);	
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'FasaPay')]")).click();
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1, "No Available Accounts for deposit.");
		/*j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		
		mt4Account=t.getFirstSelectedOption().getText().trim();*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText().trim();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Fasapay Deposit "+depositAmount);
				Thread.sleep(1000);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(2000);
				
				//Assert payment page
				
				Assert.assertEquals(driver.findElement(By.cssSelector("h1.title_u")).getText(),"FASAPAY");
				driver.findElement(By.cssSelector("input.btn_red")).click();
				Thread.sleep(2000);
				
				//Navigate to Fasapay payment page
				Assert.assertEquals(driver.getCurrentUrl(),"https://sci.fasapay.com/");
		/*		driver.findElement(By.id("LoginForm_username")).sendKeys("");
				driver.findElement(By.id("LoginForm_password")).sendKeys("");
				driver.findElement(By.id("ok_btn")).click();*/
				
				//Navigate to next page and input pin:
				
				
				//Update database record status
				System.out.println("mt4Account is:"+mt4Account);
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				
				//Navigate back to main deposit page		
				driver.navigate().to(TraderURL);
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Mobilepay
	void MobilepayDeposit(String TraderURL,String TestEnv, String Brand) throws Exception
	{
	
		
		String rmbAmount="";
		depositAmount="1999.55";

		String coo = driver.manage().getCookies().toString();
		
		driver.navigate().to(TraderURL);	
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Mobile Pay')]")).click();
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>0);
/*		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		String option = t.getFirstSelectedOption().getText();

		//remove the currency from account number
		String[] mt4Account = option.split("\\(");
		System.out.println("mt4Account is:"+mt4Account[0]);*/
		
		for(int count=1; count < t.getOptions().size();count++)
		{
			String option = t.getOptions().get(count).getText();
			String mt4Account = option.substring(0, option.indexOf("("));
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				Thread.sleep(1000);
				
				//Choose Wechat or Alipay to pay
				if(driver.findElement(By.id("weixin1")).isDisplayed())
				{
					driver.findElement(By.id("weixin1")).click();
				}else 
				{
					driver.findElement(By.id("alipay1")).click();
				}
				Thread.sleep(500);
				rmbAmount = driver.findElement(By.id("deposit_RMB")).getText();
				
				driver.findElement(By.id("deposit_Note")).sendKeys("Test MOBILEPAY Deposit "+depositAmount);
				Thread.sleep(500);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(3000);
	
				//Assert.assertEquals(driver.getCurrentUrl(), "http://transferpay.paylomo.net/vtmarketsali/");
				
				wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='customer_ac']")));
				
				driver.findElement(By.xpath("//input[@name='customer_ac']")).sendKeys(mt4Account);
				driver.findElement(By.xpath("//button[@class='btn']")).click();
				Thread.sleep(1000);
				
				//Acquire order info
				wait02.until(ExpectedConditions.visibilityOfElementLocated(By.id("order_amount")));
				Thread.sleep(500);
				
				//Click submit
				driver.findElement(By.xpath("//button[@class='btn pull-right']")).click();
				Thread.sleep(1000);
				String amount = driver.findElement(By.cssSelector("div.col-12 div:nth-child(1) p > font:nth-child(2)")).getText();
				BigDecimal bigDecimal_rmbAmount = new BigDecimal(rmbAmount);
				BigDecimal bigDecimalMoney = new BigDecimal(amount);
				Assert.assertTrue(bigDecimal_rmbAmount.compareTo(bigDecimalMoney) <0, "Calculated RMB Amount "+rmbAmount+" should be less than the actual payment "+bigDecimalMoney);		
				
				//Navigate back to main deposit page
				driver.navigate().to(TraderURL);
				
				switch(TestEnv) {
				
					case "test":				
						//Update deposit audit status in mysql
						DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
						break;
				}
		
				//Break the loop
				break;
				
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","Brand", "TestEnv"})
	//Unionpay only supports USD currency
	void UnionPayP2PDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String mt4Account="", rmbAmount="";
		String coo = driver.manage().getCookies().toString();
		
		depositAmount="5000.00";
			
		driver.navigate().to(TraderURL);	
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'CHINA UNIONPAY TRANSFER')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1, "No Available Accounts for Deposit.");
		
		/*//Randomly select one account
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Union Pay P2P Deposit "+depositAmount);
				
				((JavascriptExecutor)driver).executeScript("window.scrollBy(0,1000);");		
				Thread.sleep(500);
				
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				rmbAmount = driver.findElement(By.cssSelector(" div.rowList:nth-child(9) div > p")).getText();
				driver.findElement(By.xpath("//input[@value='PAYMENT']")).click();
				Thread.sleep(1000);
				
				
				switch(Brand)		
				{		
							
					case "vt":	
					case "pug":
					{		
						//Assert.assertTrue(driver.getCurrentUrl().contains("http://www.fn858.com/pay?id"));		
						wait02.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='layui-layer-btn0']")));
						driver.findElement(By.xpath("//a[@class='layui-layer-btn0']")).click();
						String amount = driver.findElement(By.id("money")).getText();
						BigDecimal bigDecimal_rmbAmount = new BigDecimal(rmbAmount);
						BigDecimal bigDecimalMoney = new BigDecimal(amount);
						Assert.assertTrue(bigDecimal_rmbAmount.compareTo(bigDecimalMoney) <0, "Calculated RMB Amount "+rmbAmount+" should be less than the actual payment "+bigDecimalMoney);		
						
						break;		
								
					}		
						
					case "au":		
					case "ky":		
						default:		
									
						{		
							//Assert navigation to Union Pay page		
							//Assert.assertEquals(driver.getTitle(),"China Unionpay Transfer");		
									
								
							//Unionpay payment		
							//Assert.assertEquals(driver.getCurrentUrl(), "http://www.settlebm.com/in/gateway");		
							Utils.funcIsStringContains(driver.getCurrentUrl(), "uuu888999.scdn8", Brand);
							
									
							//Choose mockbank		
							/*driver.findElement(By.id("TESTBC")).click();		
							driver.findElement(By.cssSelector("div.btn input")).click();		
							Thread.sleep(1000);		
									
							driver.findElement(By.cssSelector("button.myButton")).click();	*/
									
							break;		
						}
				}
			
				//After payment, navigate back to original page		
				driver.navigate().to(TraderURL);		
				
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	@Test(priority=3,alwaysRun=true)
	@Parameters(value= {"TraderURL","ThaiAccount","ThaiPwd","Brand", "TestEnv"})
	
	//Thailand bank transfer needs a special user account (with country = Thailand)
	void ThailandDeposit(String TraderURL, String ThaiAccount, String ThaiPwd,String Brand, String TestEnv) throws Exception
	{
	
		String mt4Account="";
		String coo = driver.manage().getCookies().toString();
		depositAmount="1300.00";
		
		/*//Logout AU Trader if already logged in
		driver.navigate().to(TraderURL);
		
		if(driver.findElements(By.linkText("LOGOUT")).size()>0)
		{		
			wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT")));
			driver.findElement(By.linkText("LOGOUT")).click();
		}
		
	    //Login AU Trader
		driver.get(TraderURL);
		Utils.funcLogInTrader(driver, ThaiAccount, ThaiPwd,Brand);	*/
		Thread.sleep(waitIndex*2000);
		//Assert.assertEquals(driver.getTitle(),"My Account");
			
		driver.navigate().to(TraderURL);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Thailand')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1, "No Available USD accounts for deposit.");
		
		/*//Randomly select one account
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(2000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Thailand Bank Wire Pay Deposit "+depositAmount);
				
				Thread.sleep(1000);
				wait02.until(ExpectedConditions.elementToBeClickable(By.id("confirmBtn")));
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(2000);
				
				//Navigate to Payment confirm page
				Assert.assertEquals(driver.findElement(By.cssSelector("h1.title_u")).getText(), "THAILAND INSTANT BANK WIRE TRANSFER");
				driver.findElement(By.id("payBtn")).click();
				Thread.sleep(2000);
				//Assert navigation to Bank Selection page
/*				String selectBank = new Select(driver.findElement(By.id("bank"))).getFirstSelectedOption().getText();
				Assert.assertEquals(selectBank,"Select your bank...");*/
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.title-base")));
				Assert.assertEquals(driver.getTitle(),"Bank selection");
				
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				
				//After payment, navigate back to original page and log out
				driver.navigate().to(TraderURL);
				
				//Need to remove this line after the popup dialog feature is removed.
				Utils.handlePopup(driver,false);
				
				wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	@Test(priority=3,alwaysRun=true)
	@Parameters(value= {"TraderURL","MalayAccount","MalayPwd","Brand", "TestEnv"})
	//Malaysia bank transfer needs a special user account (with country = Malaysia)
	void MalaysiaDeposit(String TraderURL, String MalayAccount, String MalayPwd, String Brand, String TestEnv) throws Exception
	{
	
		String mt4Account="";
		String coo = driver.manage().getCookies().toString();
		depositAmount="1300.00";
		
		/*//Logout AU Trader if already logged in
		driver.navigate().to(TraderURL);
		if(driver.findElements(By.linkText("LOGOUT")).size()>0)
		{		
			wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT")));
			driver.findElement(By.linkText("LOGOUT")).click();
		}
	    //Login AU Trader
		driver.get(TraderURL);
		Utils.funcLogInTrader(driver, MalayAccount, MalayPwd, Brand);	
		*/
		Thread.sleep(waitIndex*2000);
		//Assert.assertEquals(driver.getTitle(),"My Account");
			
		driver.navigate().to(TraderURL);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Malaysia')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1);
		
		/*//Randomly select one account
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Malaysia Bank Wire Pay Deposit "+depositAmount);
				Thread.sleep(500);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Navigate to Payment confirm page
				Assert.assertEquals(driver.findElement(By.cssSelector("h1.title_u")).getText(), "MALAYSIA INSTANT BANK WIRE TRANSFER");
				driver.findElement(By.id("payBtn")).click();
				Thread.sleep(2000);
				//Assert navigation to Bank Selection page
				/*String selectBank = new Select(driver.findElement(By.id("bank"))).getFirstSelectedOption().getText();
				Assert.assertEquals(selectBank,"Select your bank...");*/
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.title-base")));
				Assert.assertEquals(driver.getTitle(),"Bank selection");
				
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				
				//After payment, navigate back to original page and log out
				driver.navigate().to(TraderURL);
				
					
				wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	@Test(priority=3,alwaysRun=true)
	@Parameters(value= {"TraderURL","NigeAccount","NigePwd","Brand", "TestEnv"})
	//Nigeria bank transfer needs a special user account (with country = Nigeria)
	void NigeriaDeposit(String TraderURL, String NigeAccount, String NigePwd, String Brand, String TestEnv) throws Exception
	{
	
		String mt4Account="";
		String coo = driver.manage().getCookies().toString();
		depositAmount="1300.00";
		
		/*//Logout AU Trader if already logged in
		driver.navigate().to(TraderURL);
		if(driver.findElements(By.linkText("LOGOUT")).size()>0)
		{		
			wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT")));
			driver.findElement(By.linkText("LOGOUT")).click();
		}

	    //Login AU Trader
		driver.get(TraderURL);
		Utils.funcLogInTrader(driver, NigeAccount, NigePwd,Brand);	*/
		Thread.sleep(waitIndex*2000);
		//Assert.assertEquals(driver.getTitle(),"My Account");
			
		driver.navigate().to(TraderURL);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Nigeria')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1, "No available accounts for deposit.");
		
		/*//Randomly select one account
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Nigeria Bank Wire Pay Deposit "+depositAmount);
				Thread.sleep(500);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(2000);
				
				//Navigate to Payment confirm page
				Assert.assertEquals(driver.findElement(By.cssSelector("h1.title_u")).getText(), "NIGERIA INSTANT BANK WIRE TRANSFER");
				driver.findElement(By.id("payBtn")).click();
				Thread.sleep(3000);
				
				//Assert navigation to Bank Selection page
				/*String selectBank = new Select(driver.findElement(By.id("bank"))).getFirstSelectedOption().getText();
				Assert.assertEquals(selectBank,"Select your bank...");*/
				Utils.funcIsStringContains(driver.getCurrentUrl(), "vantage-fx1.account.fund/paynet/form/wait", Brand);
				
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				
				//After payment, navigate back to original page and log out
				driver.navigate().to(TraderURL);
				
				wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}

	@Test(priority=3,alwaysRun=true)
	@Parameters(value= {"TraderURL","VietAccount","VietPwd","Brand", "TestEnv"})
	//Vietnam bank transfer needs a special user account (with country = Vietnam)
	void VietnamDeposit(String TraderURL, String VietAccount, String VietPwd,String Brand, String TestEnv) throws Exception
	{
	
		String mt4Account="";
		String coo = driver.manage().getCookies().toString();
		depositAmount="1300.00";
		
		/*//Logout AU Trader if already logged in
		driver.navigate().to(TraderURL);
		if(driver.findElements(By.linkText("LOGOUT")).size()>0)
		{		
			wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT")));
			driver.findElement(By.linkText("LOGOUT")).click();
		}
	    //Login AU Trader
		driver.get(TraderURL);
		Utils.funcLogInTrader(driver, VietAccount, VietPwd,Brand);	*/
		Thread.sleep(waitIndex*2000);
		//Assert.assertEquals(driver.getTitle(),"My Account");
			
		driver.navigate().to(TraderURL);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Vietnam')]")).click();
		
		
		//Select an account randomly
		t=new Select(driver.findElement(By.id("deposit_MT4_account")));
		j=t.getOptions().size();
		assertTrue(j>1);
		
		/*//Randomly select one account
		j=r.nextInt(j-1);
		t.selectByIndex(j+1);
		mt4Account = t.getFirstSelectedOption().getText();
		System.out.println("mt4Account is:"+mt4Account);*/
		for(int count=1; count < t.getOptions().size();count++)
		{
			mt4Account = t.getOptions().get(count).getText();
			if(funcCheckAccoutGroup(Brand, TestEnv, mt4Account))
			{
				t.selectByIndex(count);
				//After account is selected, currency will be loaded automatically. Wait for 1 second until refresh finishes
				Thread.sleep(1000);
				
				//Input deposit amount
				driver.findElement(By.id("deposit_USD")).sendKeys(depositAmount);
				driver.findElement(By.id("deposit_Note")).sendKeys("Test Vietnam Bank Wire Pay Deposit "+depositAmount);
				Thread.sleep(500);
				driver.findElement(By.id("confirmBtn")).click();
				Thread.sleep(1000);
				
				//Navigate to Payment confirm page
				Assert.assertEquals(driver.findElement(By.cssSelector("h1.title_u")).getText(), "VIETNAM INSTANT BANK WIRE TRANSFER");
				driver.findElement(By.id("payBtn")).click();
				Thread.sleep(2000);
				//Assert navigation to Bank Selection page
				/*String selectBank = new Select(driver.findElement(By.id("bank"))).getFirstSelectedOption().getText();
				Assert.assertEquals(selectBank,"Select your bank...");*/
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.title-base")));
				Assert.assertEquals(driver.getTitle(),"Bank selection");
				
				//Update deposit audit status in mysql
				DBUtils.updateSQLDepositStatus(mt4Account, TestEnv, Brand);
				
				//After payment, navigate back to original page and log out
				driver.navigate().to(TraderURL);
				
				
				wait02.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
				break;
            }else {
				System.out.println("Not able to check the accout group! Not able to make deposit to these accounts");
			}
      	}
		
	}
	
	
	/*
	 * Developed by Alex.L for checking if account group is test group on 19/07/2019
	 * Example:
	 * boolean flag = TraderDeposit.funcCheckAccoutGroup("http://ben.vantagefx.com:8004", cookie, "975023");
	 */
	public static boolean oldFuncCheckAccoutGroup(String traderURL, String cookie,String mt4Acc) throws Exception
	{				
		String sampleStr="test";
		boolean flag=false,isTestGroup=false;
					
		//System.out.println(url);
		String accDetail = RestAPI.testPostTradeAccountList(traderURL, cookie);
		
		JSONParser parser = new JSONParser();
		Object resultObject = parser.parse(accDetail);
		JSONObject obj =(JSONObject)resultObject;
		System.out.println("==================Result====================");

		JSONArray data = (JSONArray)obj.get("rows");
		for (Object item : data) {  
		  if (flag) {
			  break;
		  }
		  for (Object key: ((JSONObject) item).keySet()) {
        	Object value = ((JSONObject) item).get(key.toString());

        	if (value.toString().equals(mt4Acc)) {
        		flag=true;
        		try  {
        			Object groupName = ((JSONObject) item).get("mT4Group");
        			if (groupName.toString().toLowerCase().contains(sampleStr.toLowerCase())) {
            			System.out.println("Pass the group check: AccGroup of mt4 account "+mt4Acc+" is test group: "+groupName.toString());
            			isTestGroup=true;
            		}else {
            			System.out.println("Failed the group check: AccGroup of mt4 account "+mt4Acc+" is NOT test group: "+groupName.toString());
            		}
        		}catch (NullPointerException e) {
        			System.out.println("Something wrong! mT4Group not exist in restful API!");
        		}

        		break;
        	}

		  }	
		  
		}
		return isTestGroup;

	}
	
	/*
	 * Developed by Alex.L for checking if account group is test group on 19/07/2019
	 * 
	 */
	public static boolean funcCheckAccoutGroup(String Brand, String TestEnv, String mt4Acc) throws Exception
	{				
		String sampleStr="test",gStr="group",gDetail="";
		boolean isTestGroup=false;
		String[] server= {"db_mt4_vantage_au","db_mt4_vantage_uk","db_mt4_vantage_au2","db_mt4_vantage_uk2","mt5_vfx_live"}; 
		
		//System.out.println(url);
		String selectSql="select `GROUP` from mt4_users where login=\'"+mt4Acc+"\';";
		String selectSql2="select `GROUP` from mt5_users where login=\'"+mt4Acc+"\';";			 
		
		//for beta or prod env
		switch(Brand)
		{
			case "au":
			case "ky":
				
				for (String dataSource : server) 
				{
					if (dataSource.equals("mt5_vfx_live")) {
						gDetail = RestAPI.APIReadDB(dataSource,selectSql2,TestEnv);
						
					}else {
						gDetail = RestAPI.APIReadDB(dataSource,selectSql,TestEnv);
						
					}
					if (gDetail.toLowerCase().contains(gStr.toLowerCase())) 
					{
						break;
					}
				}
				
				break;
				
			case "vt":

				gDetail = DBUtils.funcreadDB("db_mt4_vt_business",selectSql, TestEnv);
				break;
				
			case "pug":
				System.out.println("PUB production SYNC DB is required.");
				//gDetail = Utils.funcreadDB("db_mt4_pug_business",selectSql, TestEnv);
				return true;
				
				
			default:
				System.out.println(Brand + " is NOT supported. ");
					
		}

		//String accDetail = RestAPI.testPostTradeAccountList(traderURL, cookie);
		if (gDetail.toLowerCase().contains(sampleStr.toLowerCase())) {
			System.out.println("Pass the group check: AccGroup of mt4 account "+mt4Acc+" is test group: "+gDetail.toString());
			isTestGroup=true;
		}else {
			System.out.println("Failed the group check: AccGroup of mt4 account "+mt4Acc+" is NOT test group: "+gDetail.toString());
		}


		return isTestGroup;

	}
}
