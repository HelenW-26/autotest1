package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

/*
 * This class is to test all Deposit types in Trader
 */

public class TraderSearchKeyword {
	
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
		
		//Search LogIn Page
		SearchKeyWord(driver.getCurrentUrl());
		
		Utils.funcLogInTrader(driver, TraderName, TraderPass, Brand);	
		Thread.sleep(waitIndex*2000);
		
		Assert.assertTrue(driver.getTitle().equals("My Account")||driver.getTitle().equals("Live Accounts"));
	}
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Credit card payment
	void DepositPage(String TraderURL,String TestEnv,String Brand) throws Exception
	{

		//Navigate to Deposit Page
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
			
		//Search Credit Card Deposit Page
		SearchKeyWord(driver.getCurrentUrl());
	
	}	
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Credit card payment
	void CCDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{

		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Credit or Debit Card')]")).click();
		
		Thread.sleep(1000);
		Assert.assertEquals(driver.getTitle(),"Credit Card");
		
		//Search Credit Card Deposit Page
		SearchKeyWord(driver.getCurrentUrl());
	
	}	

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)	
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//International bank wire transfer supports all 8 currencies: AUD, USD, EUR, GBP, SGD, JPY, NZD, CAD
	void I18NDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();		
		driver.findElement(By.xpath(".//span[contains(text(),'International SWIFT Deposit')]")).click();
		
		//Search International Bank Transfer Page
		SearchKeyWord(driver.getCurrentUrl());
	}
	
	
	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Bank Wire Transfer: Australia Deposit. The account currency must be AUD
	void AuBankDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		
		driver.navigate().refresh();
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Australia Deposit')]")).click();
		
		//Search Australia Bank Transfer Page
		SearchKeyWord(driver.getCurrentUrl());
					
	}
	
	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//BPay deposit. The account owner's resident country must be Australia and account currency must be AUD
	void BPayDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		
		driver.navigate().refresh();
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'BPAY')]")).click();
		
		//Search BPay Page
		SearchKeyWord(driver.getCurrentUrl());
		
		
	}
	
	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Polipay deposit. The account owner's resident country must be Australia and account currency must be AUD
	void PoliDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		driver.navigate().refresh();
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to POLi Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'POLi Payments')]")).click();
		
		//Search Polipay Page
		SearchKeyWord(driver.getCurrentUrl());

		
	}
	
	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Skrill deposit. 
	void SkrillDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		driver.navigate().refresh();
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to Skrill page
		driver.findElement(By.xpath(".//span[contains(text(),'Skrill/Moneybookers')]")).click();
		
		//Search Skrill Page
		SearchKeyWord(driver.getCurrentUrl());
		
		
	}
	
	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","TestEnv", "Brand"})
	//Neteller deposit. 
	void NetellerDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{
	
		driver.navigate().refresh();
		
		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to Neteller Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'Neteller Deposit')]")).click();
		
		//Search Neteller Page
		SearchKeyWord(driver.getCurrentUrl());		
		
	}
	
	@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		Utils.funcLogOutTrader(driver);
		driver.quit();
	}

	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//Polipay deposit. The account owner's resident country must be Australia and account currency must be AUD
	void BrokerDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		//Navigate to Deposit menu
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		//Navigate to POLi Payments page
		driver.findElement(By.xpath(".//span[contains(text(),'Broker to Broker')]")).click();
		
		//Search Broker to Broker Page
		SearchKeyWord(driver.getCurrentUrl());		
		
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Unionpay only supports USD currency
	void UnionPayDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{
	
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Union Pay Deposit')]")).click();
		
		//Search UnionPay Page
		SearchKeyWord(driver.getCurrentUrl());
		
		
	}

	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Fasapay only supports USD
	void FasapayDeposit(String TraderURL,String TestEnv,String Brand) throws Exception
	{

		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'FasaPay')]")).click();
		
		//Search Fasapay Page
		SearchKeyWord(driver.getCurrentUrl());
		
	}

	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	//Mobilepay
	void MobilepayDeposit(String TraderURL,String TestEnv, String Brand) throws Exception
	{

		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Mobile Pay')]")).click();
		
		//Search Mobile Pay Page
		SearchKeyWord(driver.getCurrentUrl());
	
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","Brand", "TestEnv"})
	//Unionpay only supports USD currency
	void UnionPayP2PDeposit(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'CHINA UNIONPAY TRANSFER')]")).click();
		
		//Search UnionPay P2P Page
		SearchKeyWord(driver.getCurrentUrl());
		
		
		
	}

	//@Test(priority=3,alwaysRun=true)
	@Parameters(value= {"TraderURL","ThaiAccount","ThaiPwd","Brand", "TestEnv"})
	
	//Thailand bank transfer needs a special user account (with country = Thailand)
	void ThailandDeposit(String TraderURL, String ThaiAccount, String ThaiPwd,String Brand, String TestEnv) throws Exception
	{
		Thread.sleep(waitIndex*2000);
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Thailand')]")).click();
		
		//Search Thailand Bank Transfer Page
		SearchKeyWord(driver.getCurrentUrl());
	}

	//@Test(priority=3,alwaysRun=true)
	@Parameters(value= {"TraderURL","MalayAccount","MalayPwd","Brand", "TestEnv"})
	//Malaysia bank transfer needs a special user account (with country = Malaysia)
	void MalaysiaDeposit(String TraderURL, String MalayAccount, String MalayPwd, String Brand, String TestEnv) throws Exception
	{
		
		Thread.sleep(waitIndex*2000);
		
		driver.findElement(By.cssSelector("div#nav ul.menuBox li:nth-of-type(2) a")).click();
		driver.findElement(By.xpath(".//span[contains(text(),'Malaysia')]")).click();
		
		//Search Malyasia Bank Transfer Page
		SearchKeyWord(driver.getCurrentUrl());
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	void changeLeverage() throws Exception
	{
	
		List<WebElement> leverageButtons;
		
		driver.navigate().refresh();
				
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#nav ul.menuBox li:nth-of-type(1) a"))).click();
		
		//Click reset password icon(lock icon)
		
		leverageButtons=driver.findElements(By.cssSelector("a.LeverageModify>i.glyphicon.glyphicon-cog"));	
		if(leverageButtons.size()>=1)
		{
			
			leverageButtons.get(0).click();
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
			
			//Search Leverage Change Page
			SearchKeyWord(driver.getCurrentUrl());
			
			
		}else
		{
			System.out.println("No Available accounts for leverage change!");
		}
		
	}

	@Test(dependsOnMethods="TraderLogIn")
	void ChangeMT4Pwd()
	{
		

		driver.navigate().refresh();
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#nav ul.menuBox li:nth-of-type(1) a"))).click();
		
		//Click reset password icon(lock icon)
		driver.findElements(By.cssSelector("a.resetPass")).get(0).click();
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
		
		//Search Leverage Change Page
		try {
			SearchKeyWord(driver.getCurrentUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	
	@Test(dependsOnMethods="TraderLogIn", alwaysRun=true)

	void ChangePWD() throws Exception
	{
		
		driver.navigate().refresh();
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("MY PROFILE"))).click();	
		driver.findElement(By.linkText("CHANGE CLIENT PORTAL PASSWORD")).click();
			
		//Search the page of Change Portal Password
		SearchKeyWord(driver.getCurrentUrl());
		
	}
	
	@Test(dependsOnMethods="TraderLogIn", invocationCount=1)
	@Parameters(value= {"Brand"})
	void RequestAddiAccount(String Brand) throws Exception
	{

		driver.navigate().refresh();
		
		//Click My Accounts link
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("MY ACCOUNTS"))).click();	
		Thread.sleep(1000);
		Assert.assertEquals(wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title_u"))).getText(), "LIVE ACCOUNTS");
		
		//Search the page of Terms
		SearchKeyWord(driver.getCurrentUrl());
		
		//Agree with terms and click SUBMIT
		driver.findElement(By.id("applyMt4Account")).click();
		
		
		wait01.until(ExpectedConditions.elementToBeClickable(By.id("chk_agree"))).click();
		driver.findElement(By.id("button")).click();
		
		//Search Additional Account Form
		SearchKeyWord(driver.getCurrentUrl());
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	void TransferFund() throws Exception
	{

		driver.navigate().refresh();
		
		//Click My Accounts link
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("MY ACCOUNTS"))).click();		
		Assert.assertEquals(wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title_u"))).getText(), "LIVE ACCOUNTS");
		
		//Click Submit
		driver.findElement(By.id("transferFunds")).click();
		
		Thread.sleep(1000);
		
		//Search Transfer Fund page
		SearchKeyWord(driver.getCurrentUrl());
	
	}
	
	
	void SearchKeyWord(String strURL) throws Exception
	
	{
 		
	
		String[] vtKeywords = new String[] {"万致", "VFX", "Vantage", "VantageFX", "Vantage FX", "1300945517", "31 Market街 悉尼,新南威尔士 2000, 澳大利亚",
				"+61 2 8999 2044", "VT Markets", "VT", "400-880-7900", "31 Market Street Sydney NSW 2000 Australia", "Vantage Global Prime", "AFSL 428901",
				"info@vtmarkets.com.cn", "4th Floor The Harbour Centre, 42 N Church St", "1157 2701", "1383491"};
		
		String contentSrc =driver.getPageSource().toLowerCase() ; 
		
		Thread.sleep(2000);

    	System.out.println("URL: " + driver.getCurrentUrl());
		
       for(String strFind: vtKeywords)
       {
	       
           int count = 0, fromIndex = 0;
           
           strFind=strFind.toLowerCase();
    	   while ((fromIndex = contentSrc.indexOf(strFind, fromIndex)) != -1 ){
	     
	            count++;
	            fromIndex++;
	            
	        }
	        
	        if(count >0)
	        {

	        	System.out.println("Total occurrences of " + strFind +": " + count);
	        }
	    
       }
	}	
	
	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawAUBank() throws Exception
	{
		boolean flag=true;
		String option;
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
		//Choose Bank Transfer/BPay/Poli Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("2");
		
		//Country/Region: choose Australia
		
		t=new Select(driver.findElement(By.id("aus_country")));
		t.selectByVisibleText("Australia");
		
		//Search AUBank witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
	}
	

	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through International Bank Transfer
	void WithdrawI18NBank(String Brand) throws Exception
	{
		boolean flag=true;
		String option;
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
		
		driver.findElement(By.linkText("WITHDRAW")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
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
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("2");
		
		//Country/Region: choose International
		
		t=new Select(driver.findElement(By.id("aus_country")));
		t.selectByVisibleText("International Payment");
		
		//Search AUBank witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
	}

	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
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
		
		//Choose Bank Transfer/BPay/Poli Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("2");
		
		//Country/Region: choose China
		
		t=new Select(driver.findElement(By.id("aus_country")));
		t.selectByVisibleText("China");
		
		//Search AUBank witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
	}

	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw Credit Card
	void WithdrawCC() throws Exception
	{
		boolean flag=true;
		String option;
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
		
		//Choose Credit card option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("1");
		
		//Search AUBank witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
	}

	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawSkrNet() throws Exception
	{
		boolean flag=true;
		String option;
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
		
		//Choose Skrill/Neteller option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("3");
		
		//Search Skrill witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
	}

	//@Test(dependsOnMethods="TraderLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawFasaPay() throws Exception
	{
		boolean flag=true;
		String option;
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
		driver.navigate().refresh();
	
		driver.findElement(By.linkText("WITHDRAW")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_account")));
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
		
		//Choose FasaPay option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("7");
		
	
		//Search Fasapay witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
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
		
		//Choose Union Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("4");
		
		//Search Unionpay witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
	}

	//@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"MalayAccount","MalayPwd","Brand"})
	//withdraw through Malay Bank Transfer
	void WithdrawMalayBank(String MalayAccount, String MalayPwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option;
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

		//Search Malaysia witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
	}


	//@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"ThaiAccount","ThaiPwd","Brand"})
	//withdraw through Thai Bank Transfer
	void WithdrawThaiBank(String ThaiAccount, String ThaiPwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option;
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
	
		//Search Thaibank witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
	}

	//@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"NigeAccount","NigePwd","Brand"})
	//withdraw through Malay Bank Transfer
	void WithdrawNigeBank(String NigeAccount, String NigePwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option;
		BigDecimal moneyAmount=new BigDecimal("0.00");
		
						
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
	
		//Search Nigiria witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
		
	}

	//@Test(priority=3, dependsOnMethods="TraderLogIn",alwaysRun=true)
	@Parameters(value={"VietAccount","VietPwd","Brand"})
	//withdraw through Malay Bank Transfer
	void WithdrawVietBank(String VietAccount, String VietPwd,String Brand) throws Exception
	{
		boolean flag=true;
		String option;
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
	
				
		//Search Vietnam witdhraw page
		SearchKeyWord(driver.getCurrentUrl());
	}

	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)
	//Input UnionPay detail
	@Parameters(value= {"TestEnv","Brand","TraderName"})
	void AddUnionPayWithdrawDetail(String testEnv, String Brand, String TraderName) throws Exception
	{	
		driver.navigate().refresh();

		Actions builder = new Actions(driver);
		builder.moveToElement(driver.findElement(By.linkText("WITHDRAW"))).perform();
		driver.findElement(By.linkText("Withdrawal Details")).click();
		wait02.until(ExpectedConditions.elementToBeClickable(By.id("withdraw_way")));

		//Choose Union Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("4");
		
		//Search Withdraw Details page
		SearchKeyWord(driver.getCurrentUrl());
	}
	
	
	@Test(dependsOnMethods="TraderLogIn",alwaysRun=true,invocationCount=1)
	@Parameters(value= {"TestEnv","TraderURL","Brand"})
	//withdraw through UnionPay
	void WithdrawUnionPayWithBankDetail(String testEnv, String TraderURL, String Brand) throws Exception
	{
		boolean flag=true;
		String option="",account = "",currency="";
		BigDecimal moneyAmount=new BigDecimal("0.00");
		BigDecimal min=new BigDecimal("200.00");
		BigDecimal result=new BigDecimal("0.00");
		BigDecimal standard=new BigDecimal("1.5");
		
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
			//System.out.println(j);
			t.selectByIndex(j);
			option=t.getFirstSelectedOption().getText();
			moneyAmount=Utils.splitAccount(option);
			System.out.println(moneyAmount);
			account = option.substring(0, option.indexOf("(")).trim();
  			//System.out.println(account);
			currency = option.substring(option.indexOf(")")-3, option.indexOf(")"));
			System.out.println(currency);
			
			//account balance is greater than 200(because minimum withdrawal is 20), and currency are only AUD and USD 
			if ((moneyAmount.compareTo(min)==1)&&((currency.equals("AUD")||currency.equals("USD"))))
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
    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
    		System.out.println("Confirm that this account is not on hold position");
    		//add margin check
        	
    	}catch (NoSuchElementException e) {
    		System.out.println("This account is not on hold position");
    	}
		
		//Choose Union Payment option
		t=new Select(driver.findElement(By.id("withdraw_way")));
		t.selectByValue("4");
		
		try 
    	{
    		driver.findElement(By.linkText("link card")).click();
        	
    	}catch (NoSuchElementException e) {
    		System.out.println("Already linked card. Proceeding...");
    	}
		

		//Select card detail for UnionPay
		t=new Select(driver.findElement(By.id("unionpay_cards")));
		t.selectByIndex(1);
		
		//Search UnionPay Withdrawl page
		SearchKeyWord(driver.getCurrentUrl());
		
	}
	
	

}
