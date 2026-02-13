package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
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
import org.testng.asserts.SoftAssert;

//import bsh.util.Util;

/*
 * This class is to test all withdraw methods in Trader
 */

public class TraderWithdraw {

	WebDriver driver;
	String lTraderURL;
	String userName;
	SoftAssert sAssert=new SoftAssert();

	WebDriverWait wait01;
	WebDriverWait wait02;
	
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	
	Select t;  //Account Dropdown
	Random r=new Random(); 
	int j; //Select index

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
    	  context.setAttribute("driver", driver);          //Added by Yanni on 5/15/2019
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
	  	wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		wait02=new WebDriverWait(driver, Duration.ofSeconds(20));
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
	void TraderLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
 		
  	  //Login AU Trader
		driver.get(TraderURL);
		
		Utils.funcLogInTrader(driver, TraderName, TraderPass,Brand);	
		Thread.sleep(waitIndex*2000);
		//Assert.assertEquals(driver.getTitle(),"My Account");
		
		Thread.sleep(1000);
		
		lTraderURL=TraderURL;
		userName=TraderName;
		
	
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through AU Bank Transfer
	void WithdrawAUBank(String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
		
		j=t.getOptions().size()-1;
			
		
		//Choose an account with balance greater than 0
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Bank Transfer/BPay/Poli Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("2");
		
		//Country/Region: choose Australia
		
		t=new Select(driver.findElement(By.id("aus_country")));
		t.selectByVisibleText("Australia");
		
		//Input Bank name
		driver.findElement(By.id("aus_bk_name")).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		
		//Input BSB
		driver.findElement(By.id("aus_bsb")).sendKeys(Utils.randomNumber(4));
		
		//Input Bank Beneficiary Name
		driver.findElement(By.id("aus_ba_Name")).sendKeys(Utils.randomString(4) + " " + Utils.randomString(3));
		
		//Input Swift
		driver.findElement(By.id("aus_swift")).sendKeys(Utils.randomNumber(4));
		
		//Input Bank Account Number
		driver.findElement(By.id("aus_ba_number")).sendKeys(Utils.randomNumber(8));
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("aus_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("aus_notes")).sendKeys("Test AUBank Withdraw " + moneyAmount.toString());
		
		if (Brand.equals("ky")) {
			  //Input bank statement
			  driver.findElement(By.id("fileImage1")).sendKeys(Utils.workingDir+"\\proof.png");
			  Thread.sleep(500);
	    }
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		//Thread.sleep(2000);
		
		//Assert success
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}
	

	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through International Bank Transfer
	void WithdrawI18NBank(String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{

			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("2");
		
		//Country/Region: choose International
		
		t=new Select(driver.findElement(By.id("aus_country")));
		t.selectByVisibleText("International Payment");
		
		//Input Bank name
		driver.findElement(By.id("inter_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+" Bank");
		
		//Input Bank Address
		driver.findElement(By.id("inter_add")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");		
		
		//Input Bank Beneficiary Name
		driver.findElement(By.id("inter_ba_Name")).sendKeys(Utils.randomString(4) + " " + Utils.randomString(3));
		
		//Input Bank Account Number
		driver.findElement(By.id("inter_acNum")).sendKeys(Utils.randomNumber(8));
		
		//Input Bank Holders' Address
		driver.findElement(By.id("inter_holder")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Avenue");	
		
		//Input Swift
		driver.findElement(By.id("inter_swift")).sendKeys(Utils.randomNumber(3));	
	
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("inter_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("inter_notes")).sendKeys("Test International Bank Withdraw " + moneyAmount.toString());
		
		if (Brand.equals("ky")) {
			  //Input bank statement
			  driver.findElement(By.id("fileImage1")).sendKeys(Utils.workingDir+"\\proof.png");
			  Thread.sleep(500);
			}
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawChinaBank() throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
	
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Bank Transfer/BPay/Poli Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("2");
		
		//Country/Region: choose Australia
		
		t=new Select(driver.findElement(By.id("aus_country")));
		t.selectByVisibleText("China");
		
		//Input Bank name
		driver.findElement(By.id("chn_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+"Bank");
		
		//Input Bank Account Number
		driver.findElement(By.id("chn_bk_number")).sendKeys(Utils.randomNumber(8));
		
		//When Account is Commission account, Select controls are visible; when account is trading account, no Select controls
		if(option.contains("Commission"))
		{
			//Select Province
			t=new Select(driver.findElement(By.id("union_province1")));
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
			
			//Select City
			t=new Select(driver.findElement(By.id("union_city1")));
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
			
			//Input Bank branch
			driver.findElement(By.id("union_br_name1")).sendKeys(Utils.randomString(5).toUpperCase()+" Branch");
		}else
		{
			//Input Bank branch
			driver.findElement(By.id("chn_br_name2")).sendKeys(Utils.randomString(5).toUpperCase()+" Branch");
		}
		

		
		//Input Bank Address
		driver.findElement(By.id("chn_add")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");		
		
		//Input Bank Beneficiary Name
		driver.findElement(By.id("chn_ba_Name")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		
		//Input Account Holders' Address
		driver.findElement(By.id("chn_holder")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Avenue");	
		
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("chn_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("chn_notes")).sendKeys("Test China Bank Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through AU Bank Transfer
	void WithdrawCC(String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
		j=t.getOptions().size()-1;	

		while(flag==true&&j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			
			//Commission account doesn't have Credit Card withdraw option
			if(!option.contains("Commission"))
			{
						
				moneyAmount=Utils.splitAccount(option);
					
				if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
				{
					flag=false;
					break;
				}
			}
			
			j--;
		}
		
		if(j==0)
		{
			//No funded account for withdraw
			Assert.assertTrue(false);
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Credit card option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("1");
		

		Thread.sleep(1000);
		
		switch(Brand)
		{
			
			//CIMA doesn't support Credit Card any more
			case "ky":
				
				wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.modal-dialog div.bootstrap-dialog-title")).getText(), "IMPORTANT", "Popup Dialog Title should be IMPORTANT.");
				
				//Click Withdraw via Bank Transfer
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons>button")).click();
				Thread.sleep(1000);
				
				t=new Select(driver.findElement(By.id("withdraw_way")));
				Assert.assertEquals(t.getFirstSelectedOption().getText(), "Bank Transfer/Bpay/Poli Payment", 
						"Credit Card is not available any more. Withdraw method should be Bank Transfer/Bpay/Poli Payment.");
				
				t=new Select(driver.findElement(By.id("aus_country")));
				Assert.assertEquals(t.getFirstSelectedOption().getText(), "International Payment", "Credit Card is not available any more. Should use International Payment");
				
				//Input Bank name
				driver.findElement(By.id("inter_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+" Bank");
				
				//Input Bank Address
				driver.findElement(By.id("inter_add")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");		
				
				//Input Bank Beneficiary Name
				driver.findElement(By.id("inter_ba_Name")).sendKeys(Utils.randomString(4) + " " + Utils.randomString(3));
				
				//Input Bank Account Number
				driver.findElement(By.id("inter_acNum")).sendKeys(Utils.randomNumber(8));
				
				//Input Bank Holders' Address
				driver.findElement(By.id("inter_holder")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Avenue");	
				
				//Input Swift
				driver.findElement(By.id("inter_swift")).sendKeys(Utils.randomNumber(3));	
			
				
				//Input Amount
				moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
				driver.findElement(By.id("inter_amount")).sendKeys(moneyAmount.toString());
				
				//Input Note
				driver.findElement(By.id("inter_notes")).sendKeys("Test International Bank Withdraw " + moneyAmount.toString());
				
				//Input card photo
				driver.findElement(By.id("fileImage1")).sendKeys(Utils.workingDir+"\\proof.png");
				Thread.sleep(500);
				
				//Submit
				driver.findElement(By.id("btnSubmit")).click();
				Thread.sleep(1000);
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				
				break;
				
			case "au":
				
				//Input name on card
				driver.findElement(By.id("credit_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase());
				
				//Input first 4 digits on Card
				driver.findElement(By.id("credit_number1")).sendKeys(Utils.randomNumber(4));
				
				//Input last 3 digits on Card
				driver.findElement(By.id("credit_number2")).sendKeys(Utils.randomNumber(3));
				
				//Input card expiry date
				driver.findElement(By.id("credit_expiry")).sendKeys("10/20");
				
					
				//Input Amount
				moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
				driver.findElement(By.id("credit_amount")).sendKeys(moneyAmount.toString());
				
				//Input Note
				driver.findElement(By.id("credit_notes")).sendKeys("Test CreditCard Withdraw " + moneyAmount.toString());
				
				//Submit
				driver.findElement(By.id("btnSubmit")).click();
				Thread.sleep(1000);
				
				//Assert success
				
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
				
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				
				break;
				
			case "vt":
			case "pug":
		      default:
		    	  
		    	  System.out.println("Only ASIC Brand support Credit Card withdraw.");
		}
		

		
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawSkrNet() throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		//Thread.sleep(1000);
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Skrill/Neteller option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("3");
		
	
		//Input Skrill/Neteller email
		driver.findElement(By.id("skrill_email")).sendKeys(userName);
		
	
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("skrill_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("skrill_notes")).sendKeys("Test Skrill/Neteller Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawFasaPay() throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
	
		driver.findElement(By.linkText("WITHDRAW")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose FasaPay option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("7");
		
	
		//Input FasaPay Account Name
		driver.findElement(By.id("fasaPay_Name")).sendKeys(userName.substring(0, userName.indexOf("@")-1));
		
		//Input FasaPay Account Number
		driver.findElement(By.id("fasaPay_number")).sendKeys(Utils.randomNumber(8));
		
	
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("fasaPay_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("fasaPay_notes")).sendKeys("Test FasaPay Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawUnionPay() throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();

		driver.findElement(By.linkText("WITHDRAW")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Union Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("4");
		

		//Input Bank name
		driver.findElement(By.id("union_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+"Bank");
		
		//Input Bank Account Number
		driver.findElement(By.id("union_bk_number")).sendKeys(Utils.randomNumber(8));
		
		//When Account is trading account, Select controls are visible; when account is commission account, no Select controls
		if(!option.contains("Commission"))
		{
			//Select Province
			t=new Select(driver.findElement(By.id("union_province")));
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
			
			//Select City
			t=new Select(driver.findElement(By.id("union_city")));
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
			
			//Input Bank branch
			driver.findElement(By.id("union_br_name3")).sendKeys(Utils.randomString(5).toUpperCase()+" Branch");
		}else
		{
			//Input Bank branch
			driver.findElement(By.id("chn_br_name4")).sendKeys(Utils.randomString(5).toUpperCase()+" Branch");
		}
		

		
		//Input Bank account name
		driver.findElement(By.id("union_ba_Name")).sendKeys(Utils.randomString(5).toUpperCase());
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("union_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("union_notes")).sendKeys("Test Union Pay Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
	}

	@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"MalayAccount","MalayPwd","Brand"})
	//withdraw through Malay Bank Transfer
	void WithdrawMalayBank(String MalayAccount, String MalayPwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		//driver.navigate().refresh();
		
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		try
		{
			driver.findElement(By.xpath(".//button[text()='confirm']")).click();
		}catch(NoSuchElementException e)
		{
			System.out.println("Account is not in HOLD position.");
		}
		
		//Choose Malaysia Instant Bank Wire Transfer
		t=new Select(driver.findElement(By.id("withdraw_way")));
		for(j=1;j<=t.getOptions().size();j++)
		{
		
			if(t.getOptions().get(j).getText().contains("Malaysia"))
			{
				t.selectByIndex(j);
				break;
			}
		}

		//Input Bank name
		driver.findElement(By.id("mlpay_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+"Bank");
		
		//Input Bank Address
		driver.findElement(By.id("mlpay_add")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");	
		
		//Input Bank Branch
		driver.findElement(By.id("mlpay_branch")).sendKeys(Utils.randomString(4).toUpperCase()+" Branch");		
		
		//Input Bank City
		driver.findElement(By.id("mlpay_bank_city")).sendKeys(Utils.randomString(6).toUpperCase());	
		
		//Input province
		driver.findElement(By.id("mlpay_bank_pro")).sendKeys(Utils.randomString(5).toUpperCase());	
		
		//Input Bank Beneficiary Name
		driver.findElement(By.id("mlpay_ba_Name")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		
		//Input Bank Account Number
		driver.findElement(By.id("mlpay_acNum")).sendKeys(Utils.randomNumber(8));
		
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("mlpay_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("mlpay_notes")).sendKeys("Test Malaysia Bank Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		Thread.sleep(2000); //To verify the message
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}
	
	@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		
		Utils.funcLogOutTrader(driver);
		//Close all browsers
		driver.quit();
	}

	@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"ThaiAccount","ThaiPwd","Brand"})
	//withdraw through Thai Bank Transfer
	void WithdrawThaiBank(String ThaiAccount, String ThaiPwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		//driver.navigate().refresh();
		
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
			System.out.println(j);
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Malaysia Instant Bank Wire Transfer
		t=new Select(driver.findElement(By.id("withdraw_way")));
		for(j=1;j<=t.getOptions().size();j++)
		{
		
			if(t.getOptions().get(j).getText().contains("Thailand"))
			{
				t.selectByIndex(j);
				break;
			}
		}
	
		//Input Bank name
		driver.findElement(By.id("zotapay_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+"Bank");
		
		//Input Bank Address
		driver.findElement(By.id("zotapay_add")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");	
		

		//Input Bank Beneficiary Name
		driver.findElement(By.id("zotapay_ba_Name")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		
		//Input Bank Account Number
		driver.findElement(By.id("zotapay_acNum")).sendKeys(Utils.randomNumber(8));
		
		//Input Account Holder's Address
		driver.findElement(By.id("zotapay_holder")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");			
		
		//Input Swift
		driver.findElement(By.id("zotapay_swift")).sendKeys(Utils.randomNumber(4));		

		//Input ABA/SortCode
		driver.findElement(By.id("zotapay_sort")).sendKeys(Utils.randomNumber(5));		
		
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("zotapay_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("zotapay_notes")).sendKeys("Test Thai Bank Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		
		Thread.sleep(2000); //To verify the message
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}

	@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"NigeAccount","NigePwd","Brand"})
	//withdraw through Malay Bank Transfer
	void WithdrawNigeBank(String NigeAccount, String NigePwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
						
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Malaysia Instant Bank Wire Transfer
		t=new Select(driver.findElement(By.id("withdraw_way")));
		for(j=1;j<=t.getOptions().size();j++)
		{
		
			if(t.getOptions().get(j).getText().contains("Nigeria"))
			{
				t.selectByIndex(j);
				break;
			}
		}
	
		//Input Bank name
		driver.findElement(By.id("ngpay_bk_name")).sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase()+" "+"Bank");
		
		//Input Bank Address
		driver.findElement(By.id("ngpay_add")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");	
		
		//Input Bank Branch
		driver.findElement(By.id("ngpay_branch")).sendKeys(Utils.randomString(4).toUpperCase()+" Branch");		
		
		//Input Bank City
		driver.findElement(By.id("ngpay_bank_city")).sendKeys(Utils.randomString(6).toUpperCase());	
		
		//Input province
		driver.findElement(By.id("ngpay_bank_pro")).sendKeys(Utils.randomString(5).toUpperCase());	
		
		//Input Bank Beneficiary Name
		driver.findElement(By.id("ngpay_ba_Name")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		
		//Input Bank Account Number
		driver.findElement(By.id("ngpay_acNum")).sendKeys(Utils.randomNumber(8));
		
		
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("ngpay_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("ngpay_notes")).sendKeys("Test Nigeria Bank Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		Thread.sleep(2000); //To verify the message
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}

	@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"VietAccount","VietPwd","Brand"})
	//withdraw through Malay Bank Transfer
	void WithdrawVietBank(String VietAccount, String VietPwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		//driver.navigate().refresh();
	
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		Thread.sleep(1000);
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
	
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			
			if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
			{
				flag=false;
				break;
			}else
			{
				j--;
			}
		}
		
		Thread.sleep(1000);
		
		//Check whether the account is in HOLD position or not
		try 
    	{
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println(option + " is in HOLD position.");
    		
        	
    	}catch (NoSuchElementException | StaleElementReferenceException e) {
    		System.out.println(option + " is not on hold position");
    	}
		
		Thread.sleep(1000);
		
		//Choose Vietnam Instant Bank Wire Transfer
		t=new Select(driver.findElement(By.id("withdraw_way")));
		for(j=1;j<=t.getOptions().size();j++)
		{
		
			if(t.getOptions().get(j).getText().contains("Vietnam"))
			{
				t.selectByIndex(j);
				break;
			}
		}
	
				
		//Input Bank Account Name
		
		driver.findElement(By.id("vn_bank_account_name")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		
		//Input Bank Account Number
		driver.findElement(By.id("vn_bank_account_number")).sendKeys(Utils.randomNumber(8));
		
		//Input Bank Branch
		driver.findElement(By.id("vn_bank_branck")).sendKeys(Utils.randomString(4).toUpperCase()+" Branch");	
		
		//Input Bank City
		driver.findElement(By.id("vn_bank_city")).sendKeys(Utils.randomString(6).toUpperCase());	
		
	
		//Input Amount
		moneyAmount=moneyAmount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("vn_amount")).sendKeys(moneyAmount.toString());
		
		//Input Note
		driver.findElement(By.id("vn_notes")).sendKeys("Test Vietnam Bank Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
		Thread.sleep(2000); //To verify the message
		
		//Confirm
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)
	//Input UnionPay detail
	@Parameters(value= {"TestEnv","Brand","TraderName"})
	void AddUnionPayWithdrawDetail(String testEnv, String Brand, String TraderName) throws Exception
	{	
		
		Select  t;
		Random r = new Random();
		int index = 0;
		
		driver.navigate().refresh();

		Actions builder = new Actions(driver);
		builder.moveToElement(driver.findElement(By.linkText("WITHDRAW"))).perform();
		driver.findElement(By.linkText("Withdrawal Details")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_way")));

		//Choose Union Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("4");
		
		//Select Bank name
		t=new Select(driver.findElement(By.id("bank_name")));
		index = r.nextInt(t.getOptions().size()-1)+1;
		t.selectByIndex(index);
		
		//Input Bank Account Number
		driver.findElement(By.id("card_number")).sendKeys(Utils.randomNumber(19));
		
		//Select Province
		t=new Select(driver.findElement(By.id("union_province")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Select City
		t=new Select(driver.findElement(By.id("union_city")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Input Bank branch
		driver.findElement(By.id("union_br_name3")).sendKeys(Utils.randomSCString(5));		
		
		//Input Card holder name
		driver.findElement(By.id("card_holder_name")).sendKeys(Utils.randomSCString(3));
		
		//Input Mobile number
		driver.findElement(By.id("union_phone")).sendKeys(Utils.randomNumber(10));
		
		//Input ID number
		driver.findElement(By.id("national_id_card")).sendKeys(Utils.randomNumber(17)+Utils.randomString(1).toUpperCase());
		//driver.findElement(By.id("national_id_card")).sendKeys(Utils.randomNumber(18));
		
		//Input card photo
		driver.findElement(By.xpath("//input[@type='file' and @data-name='cardFront']")).sendKeys(Utils.workingDir+"\\proof.png");
		driver.findElement(By.cssSelector("div.card_upload div.input-box span a")).click();
		Thread.sleep(500);
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//Assert success
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"Card Registration Successful");
		
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
		
		//Check in db
		String uname = TraderName.substring(0, TraderName.indexOf("@"));
		String selectSql = "SELECT user.real_name, card.bank_name,card.card_number,card.branch_name,card.card_holder_name, card.national_id_card,card.is_del FROM tb_cps_union_card card join tb_user user on card.user_id=user.id  where user.real_name like '%"+uname+"%' order by card.create_time desc limit 1;";
		//System.out.println(selectSql);

		switch(Brand)
		{
			case "vt":
				DBUtils.funcreadDB("vt_business_db",selectSql, testEnv);
				break;
				
			case "pug":
				DBUtils.funcreadDB("pug_business",selectSql, testEnv);
				break;
		
			case "au":
			case "ky":
		    default:
		    	DBUtils.funcreadDB("db_cima_vgp",selectSql, testEnv);
		    	DBUtils.funcreadDB("db_asic_vgp",selectSql, testEnv);
				break;	
				
		}
	}
	
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)
	@Parameters(value= {"TestEnv","TraderURL","Brand", "AdminURL", "AdminName", "AdminPass"})
	//withdraw through UnionPay
	void WithdrawUnionPayWithBankDetail(String TestEnv, String TraderURL, String Brand, String AdminURL, String AdminName, String AdminPass) throws Exception
	{
		boolean flag=true, successSubmit = false;
		String option="",account = "",url="",cookie;
		BigDecimal moneyAmount=new BigDecimal("0.00");
		BigDecimal min=new BigDecimal("20.00");
		BigDecimal result=new BigDecimal("0.00");
		BigDecimal upperStandard=new BigDecimal("1.50");
		BigDecimal lowerStandard = new BigDecimal("0.50");
		
		switch(Brand)
		{
			case "vt":
			case "pug":
				lowerStandard = new BigDecimal("0.50");
				break;
				
			case "au":
			case "ky":
			default:					
				lowerStandard = new BigDecimal("0.50");
				break;
				
		}
		
		driver.navigate().refresh();

		Actions builder = new Actions(driver);
		builder.moveToElement(driver.findElement(By.linkText("WITHDRAW"))).perform();
		driver.findElement(By.linkText("WITHDRAW")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
		//Assert.assertEquals(driver.findElement(By.cssSelector("div.paddingBox h1.title_u")), "WITHDRAW");
		t=new Select(driver.findElement(By.id("withdraw_account")));
			
		j=t.getOptions().size()-1;
		
		while(flag==true && j>0)
		{
			//Select the account and get the account info
			t.selectByIndex(j);
			
			
			option=t.getFirstSelectedOption().getText();
			
			//Get the account balance and accountNo
			moneyAmount=Utils.splitAccount(option);			
			account = option.substring(0, option.indexOf("(")).trim();
			
			//Check whether the account is in HOLD position or not
			try 
	    	{
	    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
	    		System.out.println(account + " is in HOLD position.");
	    		
	        	
	    	}catch (NoSuchElementException | StaleElementReferenceException e) {
	    		System.out.println(account + " is not on hold position");
	    	}
 
			//if account balance is greater than 200(because minimum withdrawal is 20)
			//System.out.println("\n\nmoneyAmount.compareTo(min): "+moneyAmount.compareTo(min));
			if ((moneyAmount.compareTo(min)>=0))
			{				
				flag=false;
				break;
			}else
			{
				if (j==1) {
					
					url = Utils.ParseInputURL(AdminURL);
					
					switch(TestEnv)
					{
						case "test":
							//Temporary comment the line below due to new entry parameter introduced.
							//cookie  = RestAPI.testPostForAdminCookie(url,AdminName,AdminPass,Brand, TestEnv);
							break;
					    default:
					    	ChromeOptions options=new ChromeOptions();
							options.addArguments("--incognito");
							
							System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
					  	  	driver = new ChromeDriver(options);	
					  	  	driver.get(AdminURL);
							
							
							cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand,TestEnv);
							
							driver.close();
							break;
					}
					
					//deposit 200.99 to this account
					//Temporary comment the line below due to new entry parameter introduced.
					//RestAPI.testPostAccountMakeSubmit(url, account, "200.99", cookie);
					Thread.sleep(500);
					
					//refresh to get the balance after deposit
					driver.navigate().refresh();
					t=new Select(driver.findElement(By.id("withdraw_account")));
					t.selectByIndex(j);
					
					option=t.getFirstSelectedOption().getText();
					
					//Get the account balance and accountNo
					moneyAmount=Utils.splitAccount(option);			
					account = option.substring(0, option.indexOf("(")).trim();
				}
				j--;
			}
		}
		
		Thread.sleep(1000);

		
		//Choose Union Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("4");
		Thread.sleep(500);
		try 
    	{
    		driver.findElement(By.linkText("link card")).click();
        	
    	}catch (NoSuchElementException e) {
    		System.out.println("Already linked card. Proceeding...");
    	}
		if(driver.findElement(By.cssSelector("h1.title_u")).getText().equals("WITHDRAW DETAILS")) {
			Assert.assertTrue(false, "Please run link card method first.");
		}

		//Select card detail for UnionPay
		t=new Select(driver.findElement(By.id("unionpay_cards")));
		t.selectByIndex(1);
		
		//Input withdraw amount
		moneyAmount=moneyAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		driver.findElement(By.id("union_amount")).sendKeys(moneyAmount.toString());
				
		//Input Note
		driver.findElement(By.id("union_notes")).sendKeys("Test UnionPay Withdraw " + moneyAmount.toString());

		//Calculate (Equity - Withdrawal)/ Margin *100% > 150% 
		result=funcOpenPositionCheck(driver, AdminURL, AdminName, AdminPass, TraderURL, Brand, account,moneyAmount,TestEnv);
		
		
		//Submit
		driver.findElement(By.id("btnSubmit")).click();
		Thread.sleep(1000);
		
		//If (Equity - Withdrawal)/ Margin > 1.00 and <= 1.5, there should be a pop up window for inform the risk
		
		if(result.compareTo(new BigDecimal("8888.88"))!=0 && result.compareTo(new BigDecimal("8888.66"))!=0)
		{
			if (result.compareTo(upperStandard) <= 0 && result.compareTo(lowerStandard) >0 ) {
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"Information");
				
				//Print the dialog message and Confirm the dialog
				System.out.println(driver.findElement(By.cssSelector("div.bootstrap-dialog-message")).getText());
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
				Thread.sleep(1000);
				
		
				//Assert success
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
				Thread.sleep(500);
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				
				successSubmit= true;
		
			} else if(result.compareTo(lowerStandard) <=0) 
			
			{
				//Withdraw request failed
				//Print the dialog message and Confirm the dialog
				System.out.println(driver.findElement(By.cssSelector("div.bootstrap-dialog-message")).getText());
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"Information");
				Thread.sleep(500);
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				
				successSubmit= false;
				
			} 	else{
				
				//Withdraw request submitted
				Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(),"WITHDRAWAL SUCCESSFUL");
				Thread.sleep(500);
				//Confirm
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
				
				successSubmit= true;
			}
		
		} else
		{
			//Click Confirm button
			driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
			
			successSubmit= true;
			
		}
    		
		if(successSubmit)
		{
			Thread.sleep(1000);
			
			//Verify DB information is correct or not: CPS Order No, withdraw amount >=10000.00, actual amount, settlement amount
			funcCPSWithdrawVerification(Brand, TestEnv, account);
		
			
		} else
		{
			System.out.println("Failed to submit UnionPay Withdraw request.");
		}
		
		
		
	}
	
	/*
	 * Developed by Alex.L for checking (Equity - Withdrawal)/ Margin *100% > 150%  on 22/07/2019
	 * Edit by Yanni to make the function work on 08/08/2019
	 * 
	 * Return values:
	 *    -- normal result if trading account/rebate account can be found
	 *    -- 8888.88 if account can NOT be found
	 *    -- 8888.66 if margin is 0 (that means this account doesn't have open positions)
	 */
	public BigDecimal funcOpenPositionCheck(WebDriver driver, String AdminURL, String AdminName, String AdminPass, String traderURL, String Brand, String mt4Acc,BigDecimal amount,String TestEnv) throws Exception
	{				
		String adminUrl,cookie="";
		HashMap<String, String> accountInfo = new HashMap<String, String>();
	
		BigDecimal equity, margin, result;
		
			
		//Open new window and get admin cookie
		
		adminUrl = Utils.ParseInputURL(AdminURL);

		switch(TestEnv)
		{
			case "test":
				//Temporary comment the line below due to new entry parameter introduced.
				//cookie  = RestAPI.testPostForAdminCookie(adminUrl,AdminName,AdminPass,Brand, TestEnv);
				break;
		    default:
		    	ChromeOptions options=new ChromeOptions();
				options.addArguments("--incognito");
				
				System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  	  	driver = new ChromeDriver(options);	
		  	  	driver.get(AdminURL);
				
				
				cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);
				
				driver.close();
				break;
		}
		
		
		accountInfo=RestAPI.funcGetTradingAccInfo(Brand, adminUrl, mt4Acc, cookie);
		
		//If searching trading account returns null (not a trading account), search Rebate account
		if(accountInfo.isEmpty())
		{
			accountInfo=RestAPI.funcGetRebateAccInfo(Brand, adminUrl, mt4Acc, cookie);
			
			//If searching rebate account returns null, return with error code 8888.88
			if(accountInfo.isEmpty())
			{
				return new BigDecimal("8888.88");
			}
				
		}
		
		
		//Get equity and balance from result:
	
        equity = new BigDecimal(accountInfo.get("equity"));
        margin = new BigDecimal(accountInfo.get("margin")).setScale(2,BigDecimal.ROUND_HALF_UP);
          
       
		//If margin is not zero, calcuatlre the margin level with equation  (Equity - Withdrawal)/ Margin
        if (margin.compareTo(BigDecimal.ZERO)!=0) {
			
			
        	System.out.println("Margin is " + margin + " Equity is : " + equity + " Amount is : " + amount);
        	result = equity.subtract(amount).divide(margin, 3, BigDecimal.ROUND_HALF_DOWN);
			
        			
		} else
		{
			result = new BigDecimal("8888.66");
		}
		
        System.out.println("Calculated Margin Level result is " + result);
		
		return result;
	    
	}

	
	/*
	 * Developed by Alex.L for CPS withdraw verification on 09/08/2019
	 */
	public void funcCPSWithdrawVerification(String Brand, String TestEnv, String account) throws Exception
	{				
		String[] entry=null;
		BigDecimal fee=new BigDecimal("0.00"),actual_amount = new BigDecimal("0.00"),settlement_rate=new BigDecimal("0.00"),withdraw_amount = new BigDecimal("0.00"),settlement_amount=new BigDecimal("0.00");
		String csp_order_no="",is_equal_over_10k = "",result="",ibExp="";
        String selectSql="SELECT cps_order_no,fee,actual_amount,settlement_rate,is_equal_over_10k,withdraw_amount,settlement_amount FROM tb_payment_withdraw_cps where mt4Account_no = \'"+ account +"\' order by create_time desc limit 1;";			   
        
        result = DBUtils.funcReadDBReturnAll(Utils.getDBName(Brand)[1],selectSql, TestEnv);
        
   /*     switch(Brand)
		{
			case "vt":
				result = Utils.funcReadDBReturnAll("vt_business_db",selectSql, TestEnv);
				break;
			case "pug":
				result = Utils.funcReadDBReturnAll("pug_business",selectSql, TestEnv);
				break;
		
			case "au":
				result = Utils.funcReadDBReturnAll("db_asic_vgp",selectSql, TestEnv);
				break;
				
			case "ky":
				result = Utils.funcReadDBReturnAll("db_cima_vgp",selectSql, TestEnv);
				break;	
				
		}*/
        
		//parse the result and get the column we need
		result = result.substring(1, result.length()-1); 
		String[] b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			//Get the cps_order_no
		    if (entry[0].trim().equals("cps_order_no")) {
		    	csp_order_no = entry[1].trim();
		
		    }
		    
		    //Get the fee
		    if (entry[0].trim().equals("fee")) {
		    	fee = new BigDecimal(entry[1].trim());
		    }
		    
		    //Get the actual_amount
		    if (entry[0].trim().equals("actual_amount")) {
		    	actual_amount = new BigDecimal(entry[1].trim());
		    }
		    
		    //Get the settlement_rate
		    if (entry[0].trim().equals("settlement_rate")) {
		    	settlement_rate = new BigDecimal(entry[1].trim());
	
		    }
		    
		    //Get the is_equal_over_10k
		    if (entry[0].trim().equals("is_equal_over_10k")) {
		    	try {
		    	    is_equal_over_10k = entry[1].trim();
		    	}catch (ArrayIndexOutOfBoundsException e) {
		    		is_equal_over_10k = "0";
		    	}
	
		    }
		    
		    //Get the withdraw_amount withdraw_amount
		    if (entry[0].trim().equals("withdraw_amount")) {
		    	withdraw_amount = new BigDecimal(entry[1].trim());
		
		    }
		    
		    //Get the settlement_amount
		    if (entry[0].trim().equals("settlement_amount")) {
		    	settlement_amount = new BigDecimal(entry[1].trim());
	
		    }
		}
		
		//Verify is_equal_over_10k equals 1 when withdraw_amount is greater than 10000
		
		System.out.println("withdraw amount is: " + withdraw_amount);
		if (withdraw_amount.compareTo(new BigDecimal("10000.00"))>=0) {
			Assert.assertEquals(is_equal_over_10k, "1");
			System.out.println("Verify is_equal_over_10k pass!");
		}else {
			Assert.assertEquals(is_equal_over_10k, "0");
			System.out.println("Verify is_equal_over_10k pass!");
		}
		
		//Verify the actual_amount
		if (withdraw_amount.subtract(fee).compareTo(actual_amount)!=0) {
			Assert.assertTrue(false, "actual_amount "+actual_amount+" not correct! Actual amount is: "+withdraw_amount.subtract(fee));
		}else {
			System.out.println("Verify actual_amount pass!");
		}
		
		//Verify the settlement_amount
		if (actual_amount.multiply(settlement_rate).setScale(2, BigDecimal.ROUND_DOWN).compareTo(settlement_amount)!=0) {
			Assert.assertTrue(false, "settlement_amount "+settlement_amount+" not correct! Actual amount is: "+actual_amount.multiply(settlement_rate).setScale(2, BigDecimal.ROUND_DOWN));
		}else {
			System.out.println("Verify settlement_amount pass!");
		}
		
		//Verify the csp_order_no
		String order_no = csp_order_no.substring(csp_order_no.indexOf("A"),csp_order_no.length());
		//System.out.println(order_no);
		
		switch(Brand)
		{
			case "au":
				ibExp="A[0-9]{9}$";
				break;
				
			case "ky":
				ibExp="A[0-9]{10}$";
				break;	
				
			case "vt":
				ibExp="A[0-9]{8}$";
				break;	
				
			case "pug":
			default:
				ibExp="A[0-9]{11}$";
				break;				
		}
		Pattern pat=Pattern.compile(ibExp);
		Matcher mat=pat.matcher(order_no);
		if(mat.find())
		{
		    System.out.println("Verify cps_order_no pass! "+csp_order_no+" is a correct "+Brand+" order number");
		}else
		{
			Assert.assertTrue(false, "cps_order_no "+csp_order_no+" not in correct pattern for "+Brand);
		}
	    
	}	
	
	/*
	 * Developed by Yanni on 26/08/2019 for requirement: update Withdraw Details record when the record is in Submitted/Pending status
	 * status = Submitted / Pending
	 */

	public void funcUpdateUPDetails(String TraderURL, String status) throws Exception
	{
		
		List<WebElement> trs;
		int i=0;
		WebElement e;
		
		//Navigate to Withdraw Details History page
		driver.navigate().to(TraderURL);
		
		
		e= driver.findElement(By.linkText("WITHDRAW"));
		Actions a = new Actions(driver);
		a.moveToElement(e).perform();
		Thread.sleep(1000);
				
		driver.findElement(By.linkText("Withdrawal Details History")).click();
		Thread.sleep(1000);
		
		//Find 1st record status of which = status
		trs=driver.findElements(By.cssSelector("table.table>tbody>tr"));
		
		//If the user does NOT have withdraw details record, stop and exit
		if(trs.size()==0)
		{
			Assert.assertTrue(trs.size()>=1, "No Withdraw Details Records in History Page.");
			System.out.println("No Withdraw Details Records in History Page.");
		}
		
		//Loop all records. When finding the 1st one matched, break the current loop
		for(i=0; i<trs.size();i++)
		{
			if(trs.get(i).findElement(By.cssSelector("td:nth-of-type(5)")).getText().equalsIgnoreCase(status))
			{
				break;
			}

		}
		
		//If no matched record is found, stop and exit. Else, click the edit button
		if(i>=trs.size())
		{
			Assert.assertTrue(i<trs.size(), "No Records in status " + status);
			System.out.println("No Records in status " + status);
		}else
		{
			trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(2)")).click();
			
			//Edit details
			funcUPWdUpdate();
		
			//Submit
			driver.findElement(By.xpath(".//button[text() = 'Update']")).click();;
			
			
			//Print the operation result:			
			String cssPath = "li.messenger-message-slot div.messenger-message-inner";
			wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssPath)));
			String result= driver.findElement(By.cssSelector(cssPath)).getText();
			System.out.println("Result is: " + result);
			
			wait02.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(cssPath)));
			
			
			
		}
		
		
	}

	/*
	 * Developed by Yanni for UnionPay withdraw Record Update in Trader on 28/08/2019
	 * When Records are in Submitted/Pending status, Customers can update the records and save. This function is to update different fields
	 * 
	 */
	public void funcUPWdUpdate() throws Exception
	{
		
		String oldValue="", oldBank = "", oldProvince="", oldCity = "", newValue="";
		Random r=new Random();
		Select tBank, tProvince, tCity;
		int index=0;
		
		//Get old bank
		tBank= new Select(driver.findElement(By.id("bankName")));
		oldBank=tBank.getFirstSelectedOption().getText();
		
		//Get old Branch Province
		tProvince= new Select(driver.findElement(By.id("branchProvince")));
		oldProvince=tProvince.getFirstSelectedOption().getText();			
		
		//Get old Branch City
		tCity= new Select(driver.findElement(By.id("branchCity")));
		oldCity=tCity.getFirstSelectedOption().getText();
		
	
		//Select new Bank
		newValue = oldBank;
		while(newValue.equalsIgnoreCase(oldBank))				
		{
			index = r.nextInt(tBank.getOptions().size());
			tBank.selectByIndex(index);
			newValue=tBank.getFirstSelectedOption().getText();
		}
		
		System.out.print("Bank Name: Old =  " + oldBank);
		System.out.print("    New = " + newValue + "\n");
		
		//Wait for options load
		Thread.sleep(1000);

		//Select new Branch Province
		newValue = oldProvince;
		while(newValue.equalsIgnoreCase(oldProvince))				
		{
			index = r.nextInt(tProvince.getOptions().size());
			tProvince.selectByIndex(index);
			newValue=tProvince.getFirstSelectedOption().getText();
		}
		
		System.out.print("Branch Province: Old = " + oldProvince);
		System.out.print("    New = " + newValue + "\n");
		
		//Wait for options load
		Thread.sleep(1000);

	
		//Select new Branch City
		newValue = oldCity;
		while(newValue.equalsIgnoreCase(oldCity))				
		{
			index = r.nextInt(tCity.getOptions().size());
	
			tCity.selectByIndex(index);
			newValue=tCity.getFirstSelectedOption().getText();
		}
		
		System.out.print("Branch City:  " + oldCity);
		System.out.print("    New = " + newValue + "\n");
		
		//Update Branch Name by adding 'TR'
		oldValue=driver.findElement(By.id("branchName")).getAttribute("value");
		driver.findElement(By.id("branchName")).sendKeys("TR");
		newValue=driver.findElement(By.id("branchName")).getAttribute("value");
		
		System.out.print("Branch Name: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");
		
		//Update Bank Card Number by replacing the last three digits to '888'
		oldValue=driver.findElement(By.id("bankcardNumber")).getAttribute("value");
		newValue = oldValue.substring(0, oldValue.length()-3) + "888";
		newValue =newValue.replace("X", "8");
		
		driver.findElement(By.id("bankcardNumber")).clear();
		driver.findElement(By.id("bankcardNumber")).sendKeys(newValue);
		newValue=driver.findElement(By.id("bankcardNumber")).getAttribute("value");
		
		System.out.print("Bank Card Number: Old = " + oldValue);
		System.out.print("    New= " + newValue + "\n");
		
		//Update Card Holder's name by adding 'TR'
		oldValue=driver.findElement(By.id("cardHolderName")).getAttribute("value");
		driver.findElement(By.id("cardHolderName")).sendKeys("TR");
		newValue=driver.findElement(By.id("cardHolderName")).getAttribute("value");
		
		System.out.print("Card Holder's Name: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");
		
		//Update Phone Number by adding '88'
		oldValue=driver.findElement(By.id("phoneNumber")).getAttribute("value");
		driver.findElement(By.id("phoneNumber")).sendKeys("88");
		newValue=driver.findElement(By.id("phoneNumber")).getAttribute("value");
		
		System.out.print("Phone Numbe:r Old = " + oldValue);
		System.out.print("    New= " + newValue + "\n");			
		
		//Replace the first 2 digits  for National ID No. 2 digits are the same and generated randomly.
		index = r.nextInt(10);
		newValue=Integer.toString(index) + Integer.toString(index);
		
		
		oldValue=driver.findElement(By.id("nationalID")).getAttribute("value");
		newValue=newValue + oldValue.substring(2);
		driver.findElement(By.id("nationalID")).clear();
		driver.findElement(By.id("nationalID")).sendKeys(newValue);
		
		System.out.print("NationalID Number: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");		
		
			
		//Upload a replacement photo
		driver.findElement(By.xpath("//input[@type='file' and @data-name='replacementUnionCardPhoto']")).sendKeys(Utils.workingDir+"\\proof.png");
		Thread.sleep(1000);
		driver.findElements(By.xpath("//a[@title='Upload Selected Files']")).get(1).click();
		
		Thread.sleep(1000);
		
		//Print old image path and new image path
		List<WebElement> imgList= driver.findElements(By.cssSelector("img.pull-left.input-img.input_img"));
		System.out.println("Old Image Source is: " + imgList.get(0).getAttribute("src"));
		System.out.println("New Image Source is: " + imgList.get(1).getAttribute("src"));
		
		
	}
	
	//Yanni on 28/08/2019: to update Pending UnionPay Withdraw Details record in trader
	@Test(dependsOnMethods = "TraderLogIn", alwaysRun= true)
	@Parameters(value="TraderURL")
	public void UPWdPendingEdit(String TraderURL) throws Exception
	{
		String status = "Pending";
		funcUpdateUPDetails(TraderURL, status);
	}
	
	//Yanni on 28/08/2019: to update Submitted UnionPay Withdraw Details record in Trader, 
	@Test(dependsOnMethods = "TraderLogIn", alwaysRun= true)
	@Parameters(value="TraderURL")
	public void UPWdSubmittedEdit(String TraderURL) throws Exception
	{
		String status = "Submitted";
		funcUpdateUPDetails(TraderURL, status);
	}
	
}
