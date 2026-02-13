package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.json.Json;
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

import clientBase.navigationBar;
import clientBase.withdrawCC;
import clientBase.withdrawEmailForm;
import clientBase.withdrawFasaForm;
import clientBase.withdrawGeneral;
import clientBase.withdrawI18N;
import clientBase.withdrawIndia;
import clientBase.withdrawIndonesia;
import clientBase.withdrawMalay;
import clientBase.withdrawNigeria;
import clientBase.withdrawPhilippine;
import clientBase.withdrawSAfrica;
import clientBase.withdrawThailand;
import clientBase.withdrawVietNam;
import io.github.bonigarcia.wdm.WebDriverManager;

import vantagecrm.Utils;

/*
 * This class is to test all withdraw methods in Trader
 */

public class CPWithdraw {

	WebDriver driver;

	WebDriverWait wait10;

	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	
	Select t;  //Account Dropdown
	Random r=new Random();  
	int j; //Select index
	
	BigDecimal amount = new BigDecimal("0.00");
	BigDecimal withdrawAmount = new BigDecimal("0.00");
	BigDecimal miniAmount= new BigDecimal("400.00");
	
	navigationBar navCls;

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  // System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
		  //liufeng
		  
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);
		  	
    	  
    	 
    	  context.setAttribute("driver", driver);
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
	  	wait10=new WebDriverWait(driver, Duration.ofSeconds(30));
	
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
	  	  //Login AU CP
			driver.get(TraderURL);

			Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);	
			Thread.sleep(waitIndex*2000);
			
			// Set VT/PUG withdraw account minimum balance: 400
			switch(Brand.toLowerCase())
			{
				case "vt":
				case "fsa":
				case "svg":
					miniAmount= new BigDecimal("400");
					break;
					
					
			}
			
			navCls = new navigationBar(driver,Brand);
			
			Utils.waitUntilLoaded(driver);

	}
	
	
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through AU Bank Transfer
	void WithdrawAUBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		
		String fromAccount = "",trans_type="";

		System.out.println("***Entering WithdrawAUBank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
			   
		//Select the from account and verify if there is available from account
		driver.findElement(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input")).click();
		       
		List<WebElement> fromElement = null ;
		       
		fromElement=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		       
		if (fromElement.size()==0) {
		   Assert.assertTrue(false, "This user doesn't have any account!");
		}
		   
		for (WebElement webElement : fromElement)
		{
		   String accountInfo = webElement.getText();
		   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
		   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
		     
		   //Choose the account that in test group and has non-zero balance
		   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(BigDecimal.ZERO)>0)
		   {
		     //click on the from account
		     webElement.click();
		     Thread.sleep(500);	 
		     //Check whether the account is in HOLD position or not
			 try 
		     {
		    	driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
		    	System.out.println(fromAccount + " is in HOLD position.");
		    		
		        	
		     }catch (NoSuchElementException | StaleElementReferenceException e) {
		    	System.out.println(fromAccount + " is not on hold position");
		     }
		     //System.out.println(amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		     //Input withdraw amount
		     driver.findElement(By.xpath("//li[@class='fr colAmount']//input[@class='el-input__inner']")).sendKeys(amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		     Thread.sleep(500);
		     
		     //Select the withdraw method as bank transfer
		     driver.findElement(By.xpath("//ul[2]//li[1]//div[1]//div[1]//div[1]//div[1]//input[1]")).click();
		               
		     List<WebElement> methods = null ;
		               
		     methods=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		               
		     if (methods.size()==0) {
		        Assert.assertTrue(false, "Can not load withdraw methods!");
		     }
		       
		     //Click  Bank Transfer
		     Thread.sleep(1000);
		     driver.findElement(By.xpath("//span[contains(text(),'Bank Transfer')]")).click();
		     Thread.sleep(500);
		     
		     //Select region as Australia
		     driver.findElement(By.cssSelector("div.card_left:nth-child(2) div.el-select div > input")).click();
		     Thread.sleep(500);
		     driver.findElement(By.xpath("//span[contains(text(),'Australia')]")).click();
		     Thread.sleep(500);
		     
		     ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,500);");
		     //Input Bank name
		     driver.findElement(By.xpath("//div[@class='card_box']//ul[1]//li[1]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
			
			 //Input BSB 
			driver.findElement(By.xpath("//li[@class='fr']//div[@class='el-form-item is-required']//input")).sendKeys(Utils.randomNumber(4));

			//Input Bank Beneficiary Name
			driver.findElement(By.xpath("//div[@class='card_box']//ul[2]//li[1]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
				
			//Input Swift
			driver.findElement(By.xpath("//ul[2]//li[2]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomNumber(4));
				
			//Input Bank Account Number
			driver.findElement(By.xpath("//ul[3]//li[1]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomNumber(8));					

			//Input Note
			driver.findElement(By.xpath("//ul[3]//li[2]//div[1]//div[1]//div[1]//input[1]")).sendKeys("Test CP Australia Bank Withdraw " + amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				
			//upload file
			driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir+"\\proof.png");
			Thread.sleep(500);
			
				
  	        //submit
		    driver.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();
		    Thread.sleep(3500);
		       
		    //Back to home page
		    //wait10.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.withdrawSuccess:nth-child(2) div.btn_box:nth-child(3) > a"))).click();
		    
		    funBack2HomePage();
		    
		    //Verify the transaction history
		  	Thread.sleep(500);
		  	funcNavigate2WithdrawHistory(Brand);
			Thread.sleep(1000);
			
			switch(Brand)
			{
			case "ky":
			case "vfsc":
			case "vfsc2":
			//case "fca": no au BT
			case "regulator2":
				trans_type = "Bank Transfer (Australia)";
				break;

			case "au":
			default:	
				trans_type = "Bank Transfer/BPay/PoliPay (Australia)";
				break;
			}
			CPDeposit.funcValidateTransacHistory(driver, fromAccount, trans_type, amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), Brand);		
		    System.out.println("Made Australia bank wire withdraw of "+amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+" from account "+fromAccount);
		    break;

		    		       
		   }else {
		       System.out.println("Not able to pass the accout group check or all zeor-balance account! Not able to make withdraw!");
		   }
		   
		}
		
			
	}
	
	void funcGo2WithdrawPg(String Brand) throws Exception
	{ 
		
		Thread.sleep(2000);
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait10);
		Thread.sleep(2000);
		navCls.getFundsEle().click();
		navCls.getWithdrawMenuEle().click();
		
		Thread.sleep(2000);
		
		//Handle OPEN POSITION popup when the default selected account has open positions
		handleWithdrawHold(Brand, "");
	}
	
	@Test(groups="sanity", dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through International Bank Transfer
	void WithdrawI18NBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{


		String fromAccount = "",trans_type="";
		String methodName="";
		List<WebElement> fromElement = null ;
		
		withdrawI18N i18nCls = new withdrawI18N(driver, Brand);

		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Bank transfer";
				break;
			case "fsa":
			case "svg":
				methodName = "BANK TRANSFER:Bank Transfer";
				break;
			default:
				methodName = "Bank Transfer";
			
		}

		System.out.println("***Entering WithdrawI18NBank");
		driver.navigate().to(Utils.ParseInputURL(TraderURL)+"home");	
		
		funcGo2WithdrawPg(Brand);
		   
		//Click the withdraw account element to show Account List
		//withdrawClickAccountlist(Brand);
		Thread.sleep(2000); 
		i18nCls.getFromAcctEle().click();
		Thread.sleep(1000);      
		fromElement=i18nCls.getAllWithdrawAccounts();
		
		       
		if (fromElement.size()==0) {
		   Assert.assertTrue(false, "This user doesn't have any account!");
		}
		   
		//Check accounts in Account List one by one:
		for (WebElement webElement : fromElement)
		{
		   String accountInfo = webElement.findElement(By.tagName("span")).getText();  //Yanni on 21/05/2020: Add "span" otherwise, accountInfo is empty
		   System.out.println(accountInfo); 
		   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
		   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
		   withdrawAmount = amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		   
		   //Choose the account that in test group and has non-zero balance
		   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(miniAmount)>=0)		   {
		     
				 //click on the from account
			     webElement.click();
			     Thread.sleep(500);	 

			     //Handle HOLD dialog if the dialog pops up
			     handleWithdrawHold(Brand, fromAccount);
		     
			     //Input withdraw amount
			     //inputWithdrawAmount(Brand, withdrawAmount.toString());
			     i18nCls.getAmountEle().sendKeys(withdrawAmount.toString());
			     Thread.sleep(500);	
		     
			     //Select withdraw Method
			     Thread.sleep(1000);
			     //withdrawSelectMethod(Brand, methodName);
			     i18nCls.selectWDMethodEle(methodName).click();
			     
			     //Fill out I18N form
			     withdrawI18NForm(i18nCls);
		     
			     //Click Submit in withdraw Form
		    	i18nCls.getSubmitBtn().click();
		    	
		    	//Click Confirm if needs to confirm withdraw fee 
		    	try {
		    		
		    		i18nCls.getConfirmAmountFee().click();
		    		
		    	}catch(Exception e) {
		    		//do nothing
		    	}
		 	    Thread.sleep(3000);
			  
			    //Back to home page
			    funBack2HomePage();		       

			    //Verify the transaction history
			    Thread.sleep(1000);
			    funcNavigate2WithdrawHistory(Brand);
				Thread.sleep(1000);
				switch(Brand)
				{
					case "ky":
					case "fsa":
					case "svg":
					case "vfsc":
					case "vfsc2":
					case "fca":
					case "regulator2":
						trans_type = "Bank Transfer (International)";
						break;
						
					case "vt":
						trans_type = "Bank transfer (International)";
						break;
						
					case "au":
					default:	
						trans_type = "Bank Transfer/BPay/PoliPay (International)";
						break;
				}
				
				CPDeposit.funcValidateTransacHistory(driver, fromAccount, trans_type, withdrawAmount.toString(), Brand);		
			    System.out.println("Made International bank wire withdraw of "+withdrawAmount.toString()+" from account "+fromAccount);
			    break;
		       
		   }else 
		   {
		       System.out.println("Not able to pass the accout group check or all zeor-balance account! Not able to make withdraw!");
		   }
		   
		}
		
	}

	

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Skrill
	void WithdrawSkrill(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		// reuse for email address withdrawal methods by Fiona on 12/7/20		
		
		funcWithdrawWithEmail(TraderURL,Brand, TestEnv, "Skrill");	
	}
	
	
	//Yanni on 22/03/2021: Paypal is no longer supported any more.	
		@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
		@Parameters(value= {"TraderURL","Brand","TestEnv"})
			//withdraw through Paypal
		void WithdrawPaypal(String TraderURL,String Brand, String TestEnv) throws Exception
		{

			String fromAccount = "";

			driver.navigate().to(TraderURL);
			
			funcGo2WithdrawPg(Brand);
			   
			//Select the from account and verify if there is available from account
			driver.findElement(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input")).click();
			Thread.sleep(1000);
			List<WebElement> fromElement = null ;
			       
					
			//Get withdraw Account List items
			fromElement=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
				
			if (fromElement == null || fromElement.size()==0) {
			   Assert.assertTrue(false, "This user doesn't have any account!");
			}
			   
			for (WebElement webElement : fromElement)
			{
			   
		       String accountInfo = webElement.getText();
			   
			   System.out.println("account info: " + accountInfo);
			   
			   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
			   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
			     
			   //Choose the account that in test group and has non-zero balance
			   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(BigDecimal.ZERO)>0)
			   {
			     //click on the from account
			     webElement.click();
			     Thread.sleep(500);
			     
			     //Check whether the account is in HOLD position or not
				 try 
			     {
			    	driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
			    	System.out.println(fromAccount + " is in HOLD position.");
			    		
			        	
			     }catch (NoSuchElementException | StaleElementReferenceException e) {
			    	System.out.println(fromAccount + " is not on hold position");
			     }
				 
			     //System.out.println(amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			     //Input withdraw amount
			     driver.findElement(By.xpath("//li[@class='fr colAmount']//input[@class='el-input__inner']")).sendKeys(amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			     Thread.sleep(500);
			     
			     //Select the withdraw method as bank transfer
			     driver.findElement(By.xpath("//ul[2]//li[1]//div[1]//div[1]//div[1]//div[1]//input[1]")).click();
			               
			     List<WebElement> methods = null ;
			               
			     //methods=driver.findElements(By.xpath("//div[4]//div[1]//div[1]//ul[1]//li"));
			     methods=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
			     
			     if (methods == null || methods.size()==0) {
			        Assert.assertTrue(false, "Can not load withdraw methods!");
			     }
			       
			     //Click Paypal
			     Thread.sleep(1000);
			     driver.findElement(By.xpath("//span[contains(text(),'Paypal')]")).click();
			     Thread.sleep(500);
			     
			     //Input Paypal email
			     driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input")).sendKeys(Utils.randomString(5)+"@test.com");

				 //wait10.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='card_left']//div[@class='el-form-item is-error is-required']//input")))).sendKeys(Utils.randomString(5)+"@test.com");

				//Input Note
				driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input")).sendKeys("Test CP Paypal Withdraw " + amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
	  	        //submit
			    driver.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();
			    Thread.sleep(3000);
			       
			    //Back to home page
			    //wait10.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.withdrawSuccess:nth-child(2) div.btn_box:nth-child(3) > a"))).click();
			    funBack2HomePage();
			    
			    //Verify the transaction history
			    Thread.sleep(500);
			    funcNavigate2WithdrawHistory(Brand);
				Thread.sleep(1000);
				CPDeposit.funcValidateTransacHistory(driver, fromAccount, "Paypal", amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), Brand);		
			    System.out.println("Made Paypal withdraw of "+amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+" from account "+fromAccount);
			    break;


			       
			   }else {
			       System.out.println("Not able to pass the accout group check or all zeor-balance account! Not able to make withdraw!");
			   }
			   
			}
		}
		
		@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
		@Parameters(value= {"TraderURL","Brand","TestEnv"})
			//withdraw through CryptoUSDT
		void WithdrawCryptoUSDT(String TraderURL,String Brand, String TestEnv)  throws Exception
		{
			
			String methodName;
			switch(Brand.toLowerCase())
			{
				case "vt":
					methodName="Cryptocurrency-USDT";
					break;
				case "fsa":
				case "svg":
					methodName = "CRYPTOCURRENCY:Cryptocurrency-USDT";
					break;
				default:
					methodName = "Cryptocurrency-USDT";
				
			}
			
			funcWithdrawWithEmail(TraderURL,Brand, TestEnv, methodName);
			
		}
		
		@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
		@Parameters(value= {"TraderURL","Brand","TestEnv"})
			//withdraw through CryptoBitcoin
		void WithdrawCryptoBitcoin(String TraderURL,String Brand, String TestEnv)  throws Exception
		{
			
			String methodName;
			switch(Brand.toLowerCase())
			{
				case "vt":
					methodName="Cryptocurrency-Bitcoin";
					break;
				case "fsa":
				case "svg":
					methodName = "CRYPTOCURRENCY:Cryptocurrency-Bitcoin";
					break;
				default:
					methodName = "Cryptocurrency-Bitcoin";
				
			}
			
			funcWithdrawWithEmail(TraderURL,Brand, TestEnv, methodName);
			
		}
		
		@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
		@Parameters(value= {"TraderURL","Brand","TestEnv"})
			//withdraw through CryptoBitcoin
		void WithdrawAstropay(String TraderURL,String Brand, String TestEnv)  throws Exception
		{
			
			String methodName;
			switch(Brand.toLowerCase())
			{
				case "vt":
					methodName="";  //VT doesn't support Astropay
					break;
				case "fsa":
				case "svg":
					methodName = "E-WALLET:Astropay";
					break;
				default:
					methodName = "";
				
			}
			
			funcWithdrawWithEmail(TraderURL,Brand, TestEnv, methodName);
			
		}
		
		
	void funcWithdrawWithEmail(String TraderURL,String Brand, String TestEnv, String Method) throws Exception
	{
		
		String fromAccount = "";
		String splitMethod = "";
		withdrawEmailForm withdrawEmailCls = new withdrawEmailForm(driver,Brand, Method);
		driver.navigate().to(TraderURL);		
			
		funcGo2WithdrawPg(Brand);

		//Click the withdraw account element to show Account List
		withdrawEmailCls.getFromAcctEle().click();
		
		Thread.sleep(1000);
		List<WebElement> fromElement = null ;		       
				
		//Get withdraw Account List items
		fromElement=withdrawEmailCls.getAllWithdrawAccounts();
			
		if (fromElement == null || fromElement.size()==0) {
		   Assert.assertTrue(false, "This user doesn't have any account!");
		}
		   
		for (WebElement webElement : fromElement)
		{
		   
	       String accountInfo = webElement.getText();
		   
		   System.out.println("account info: " + accountInfo);
		   
		   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
		   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
		   withdrawAmount = amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
		   
		   //Choose the account that in test group and has non-zero balance
		   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(miniAmount)>=0)
		   {
		     //click on the selected from account
		     webElement.click();
		     Thread.sleep(500);
		     
		     //Handle HOLD dialog if the dialog pops up
		     handleWithdrawHold(Brand, fromAccount);
			 
		     //Input withdraw amount
		     withdrawEmailCls.getAmountEle().sendKeys(withdrawAmount.toString());
		     Thread.sleep(500);	
		     
		     //Select withdraw Method
		     Thread.sleep(1000);
		      withdrawEmailCls.selectWDMethodEle(Method).click();
		     Thread.sleep(1000);
		     
		     //input email:
		     withdrawEmailCls.getAllEmailEle().sendKeys(Utils.randomString(5)+"@test.com");

		     //input notes
		     withdrawEmailCls.getAllNotesEle().sendKeys("Test CP " + Method + " Withdraw " + withdrawAmount.toString());
		     
		     //Click Submit in withdraw Form
	    	 withdrawEmailCls.getSubmitBtn().click();
		    Thread.sleep(3000);
		       
		    //Back to home page
		    funBack2HomePage();
		    
		    //Verify the transaction history
		    Thread.sleep(500);
		    funcNavigate2WithdrawHistory(Brand);
			Thread.sleep(1000);
			
			if(Method.contains(":"))
			{
				splitMethod =  Method.split(":")[1];
			}else
			{
				splitMethod = Method;
			}
		    CPDeposit.funcValidateTransacHistory(driver, fromAccount, splitMethod, withdrawAmount.toString(), Brand);
		     
			System.out.println("Made "+Method+" withdraw of "+withdrawAmount.toString()+" from account "+fromAccount);
		    break;
		       
		   }else {
		       System.out.println("Not able to pass the accout group check or all zeor-balance account! Not able to make withdraw!");
		   }
		   
		}
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Neteller
	void WithdrawNeteller(String TraderURL,String Brand, String TestEnv) throws Exception
	{
		// reuse for email address withdrawal methods by Fiona on 12/7/20
		funcWithdrawWithEmail(TraderURL,Brand, TestEnv, "Neteller");				
	
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Unionpay Bank Transfer
	void WithdrawUnionPay(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String fromAccount = "";

		
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
	
		//Select the from account and verify if there is available from account
		if(Brand.equalsIgnoreCase("vt")) {
			driver.findElement(By.xpath("//div[@class='el-form-item is-success is-required']//input")).click();
		}else {
			driver.findElement(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input")).click();
		}
		       
		List<WebElement> fromElement = null ;
		       
		fromElement=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		       
		if (fromElement.size()==0) {
		   Assert.assertTrue(false, "This user doesn't have any account!");
		}
		   
		for (WebElement webElement : fromElement)
		{
		   String accountInfo = webElement.getText();
		   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
		   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
		     
		   //Choose the account that in test group and has non-zero balance
		   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(BigDecimal.ZERO)>0)
		   {
		     //click on the from account
		     webElement.click();
		     Thread.sleep(1000);
		     //Check whether the account is in HOLD position or not
			 try 
		     {
		    	driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
		    	System.out.println(fromAccount + " is in HOLD position.");
		    		
		        	
		     }catch (NoSuchElementException | StaleElementReferenceException e) {
		    	System.out.println(fromAccount + " is not on hold position");
		     }
		     //Input withdraw amount
			 		     
			 if(Brand.equalsIgnoreCase("vt")) {
			    driver.findElement(By.xpath("//div[@class='el-input']//input[@class='el-input__inner']")).sendKeys(amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			 }else {
			 driver.findElement(By.xpath("//li[@class='fr colAmount']//input[@class='el-input__inner']")).sendKeys(amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			 }
			 Thread.sleep(500);
		     
		     //Select the withdraw method as Unionpay Bank Transfer
			 if(Brand.equalsIgnoreCase("vt")) {
			    driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input[@placeholder='Select']")).click();
			 }else {
				 driver.findElement(By.xpath("//ul[2]//li[1]//div[1]//div[1]//div[1]//div[1]//input[1]")).click();
			 }      
		     List<WebElement> methods = null ;
		               
		     methods=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		               
		     if (methods.size()==0) {
		        Assert.assertTrue(false, "Can not load withdraw methods!");
		     }
		       
		     //Click UnionPay
		     Thread.sleep(1000);
		     driver.findElement(By.xpath("//span[contains(text(),'UnionPay')]")).click();
		     Thread.sleep(500);
		     
		     //Input Bank name
			 wait10.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='card_box']//li[1]//div[1]//div[1]//div[1]//input[1]")))).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
			 
			 //Input Bank Account Number
			 driver.findElement(By.xpath("//div[@class='card_box']//li[2]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomNumber(8));					

			 //Input Bank Branch
			 driver.findElement(By.xpath("//li[3]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomString(4) + "Branch");

			 //Input Bank Account Name
			 driver.findElement(By.xpath("//li[4]//div[1]//div[1]//div[1]//input[1]")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
				
			 //Input Note
			 driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input")).sendKeys("Test CP Unionpay Withdraw " + amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
  	         //submit
		     driver.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();
		     Thread.sleep(3000);
		       
			 //Back to home page
			 wait10.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='content_box']//div[2]//div[1]//a[1]"))).click();
			   
		     //Verify the transaction history
		     Thread.sleep(500);
		     funcNavigate2WithdrawHistory(Brand);
			 Thread.sleep(1000);
			 CPDeposit.funcValidateTransacHistory(driver, fromAccount, "UnionPay", amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), Brand);		
		     System.out.println("Made Unionpay Bank Transfer withdraw of "+amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+" from account "+fromAccount);
		     break;


		       
		   }else {
		       System.out.println("Not able to pass the accout group check or all zeor-balance account! Not able to make withdraw!");
		   }
		   
		}
		
		
		
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Fasapay
	void WithdrawFasaPay(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String fromAccount = "";
		String methodName="FasaPay";
		withdrawFasaForm fasaCls = new withdrawFasaForm(driver,Brand);
		
		switch(Brand.toLowerCase())
		{
		
			case "fsa":
			case "svg":
				methodName = "E-WALLET:FasaPay";
				break;
			default:
				methodName = "FasaPay";
			
		}
		
		
		System.out.println("***Entering WithdrawFasaPay");
		driver.navigate().to(TraderURL);
				
		//Go to Withdraw Page
		funcGo2WithdrawPg(Brand);

		//Click the withdraw account element to show Account List
		fasaCls.getFromAcctEle().click();
		Thread.sleep(1000); //Need to have a bit wait time otherwise we might get out of bound error when parsing account info
		       
		List<WebElement> fromElement = null ;
		       
		fromElement=fasaCls.getAllWithdrawAccounts();
		       
		if (fromElement.size()==0) {
		   Assert.assertTrue(false, "This user doesn't have any account!");
		}
		   
		for (WebElement webElement : fromElement)
		{
		   String accountInfo = webElement.getText();
		   System.out.println(accountInfo);
		   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
		   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
		   withdrawAmount = amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);  
		   
		   //Choose the account that in test group and has non-zero balance
		   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(miniAmount)>0)
		   {
		     //click on the from account
		     webElement.click();
		     Thread.sleep(1000);
		     //Check whether the account is in HOLD position or not

		     //Handle HOLD dialog if the dialog pops up
		     handleWithdrawHold(Brand, fromAccount);
		     
		     //Input withdraw amount
		     fasaCls.getAmountEle().sendKeys(withdrawAmount.toString());
		     Thread.sleep(500);		     
		    
		     //Select withdraw Method
		     Thread.sleep(1000);
		     fasaCls.selectWDMethodEle(methodName).click();
		     Thread.sleep(500);
			
			  //Input Fasapay Account Name
			  fasaCls.getFasaAcctNameEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
			  
			  //Input Fasapay Account Number
			  fasaCls.getFasaAcctNoEle().sendKeys(Utils.randomNumber(8));
			  
			  //Input Note
			  fasaCls.getFasaNotesEle().sendKeys("Test CP Fasapay Withdraw " + withdrawAmount.toString());
				
		     //Click Submit in withdraw Form
	    	 fasaCls.getSubmitBtn().click();
	 	     Thread.sleep(3000);
	 	    
			 //Back to home page
		     funBack2HomePage();
		     
		     //Verify the transaction history
		     Thread.sleep(500);
		     funcNavigate2WithdrawHistory(Brand);
			 Thread.sleep(1000);
			 CPDeposit.funcValidateTransacHistory(driver, fromAccount, "FasaPay",withdrawAmount.toString(), Brand);		
		     System.out.println("Made Fasapay withdraw of "+withdrawAmount.toString()+" from account "+fromAccount);
		     break;


		       
		   }else {
		       System.out.println("Not able to pass the accout group check or all zeor-balance account! Not able to make withdraw!");
		   }
		   
		}
		
	}
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Credit Card
	void WithdrawCC(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String newAmount = "";
		String fromAccount = "",keyword="";
		
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawAmount = new BigDecimal ("40.00");
		withdrawCC ccCls = new withdrawCC(driver, Brand);
		
		
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Credit card";
				break;
			case "fsa":
			case "svg":
				methodName = "CREDIT CARD:Credit Card";
				break;
			default:
				methodName = "Credit Card";
			
		}

		System.out.println("***Entering WithdrawCC");
		driver.navigate().to(TraderURL);	
		
		funcGo2WithdrawPg(Brand);		
		 
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(ccCls, Brand, TestEnv, methodName, withdrawAmount.toString());
	
	     //Input Credit Card withdraw Form
	     Thread.sleep(1000);
	     newAmount = withdrawCCForm(ccCls, acctInfoMap.get("withdrawAmount"));
	     
			
		  ccCls.getSubmitBtn().click();
		  Thread.sleep(3000);
		  
		  //Back to home page
		  funBack2HomePage();
		  
		  //Verify the transaction history
		  System.out.println("Check Withdraw History...");
		  funcNavigate2WithdrawHistory(Brand);
		  Thread.sleep(1000);
		  
		  if(Brand.equalsIgnoreCase("vt"))
		  {
		  keyword = "Credit/Debit card";
		  }else
		  {
		  keyword = "Credit/Debit Card";
		  }
		  
		  CPDeposit.funcValidateTransacHistory(driver, fromAccount, keyword, newAmount, Brand);
		  System.out.println("Made Credit Card withdraw of "+newAmount+" from account "+fromAccount);
	
		
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Vietnam Bank
	void WithdrawVietnamBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawVietNam vnCls = new withdrawVietNam(driver, Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Vietnamese instant bank wire transfer";
				break;
			case "fsa":
			case "svg":
				methodName = "LOCAL TRANSFER:Vietnamese Instant Bank Wire Transfer";
				break;
			
			default:
				methodName = "Vietnamese Instant Bank Wire Transfer";
			
		}

		System.out.println("***Entering WithdrawVietnamBank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
		
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(vnCls, Brand, TestEnv, methodName, "");
		
		Thread.sleep(1000);    
	     //Input VietNam Wtidhraw form
	     withdrawVNamForm(vnCls, acctInfoMap.get("withdrawAmount"));
	   				
	     //Click Submit in withdraw Form
    	 vnCls.getSubmitBtn().click();
 	     Thread.sleep(3000);
	       
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	    Thread.sleep(500);
	    funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (Vietnam)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (Vietnam)";
		}
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory, acctInfoMap.get("withdrawAmount"), Brand);		
	    System.out.println("Made Vietnum bank wire withdraw of "+acctInfoMap.get("withdrawAmount")+" from account "+fromAccount);	  	

		
	}
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Malaysia Bank
	void WithdrawMalayBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawMalay malayCls = new withdrawMalay(driver, Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Malaysian instant bank transfer";
				break;
			case "fsa":
			case "svg":
				methodName = "LOCAL TRANSFER:Malaysian Instant Bank Transfer";
				break;
			
			default:
				methodName = "Malaysian Instant Bank Transfer";
			
		}
	
		System.out.println("***Entering Withdraw Malaysian Bank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
		
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(malayCls, Brand, TestEnv, methodName, "");		
		Thread.sleep(1000);  
		
	    //Input Malaysia Withdraw form
		withdrawMalayForm(malayCls, acctInfoMap.get("withdrawAmount"));
				
	    //submit
	    malayCls.getSubmitBtn().click();
	    Thread.sleep(3000);
	
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	    Thread.sleep(500);
	    funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (Malaysia)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (Malaysia)";
		}
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory, acctInfoMap.get("withdrawAmount"), Brand);		
	    System.out.println("Made Malaysian bank wire withdraw of "+acctInfoMap.get("withdrawAmount")+" from account "+fromAccount);	  	
			
	}
	
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Nigeria Bank
	void WithdrawNigeriaBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawNigeria saCls = new withdrawNigeria(driver,Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="";
				break;
			case "fsa":
			case "svg":
				methodName="LOCAL TRANSFER:Nigerian Instant Bank Wire Transfer";
				break;
				
			
			default:
				methodName = "Nigerian Instant Bank Wire Transfer";
			
		}
	
		System.out.println("***Entering Withdraw Nigerian Instant Bank Wire Transfer");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
	
		   
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(saCls, Brand, TestEnv, methodName, "");		
		Thread.sleep(1000);    
		     
	
		 //Input Thailand bank withdraw form
		 withdrawNigeriaForm(saCls, acctInfoMap.get("withdrawAmount"));
	       
	     //Click Submit in withdraw Form
		saCls.getSubmitBtn().click();
	    Thread.sleep(3000);
	
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	   	Thread.sleep(500);
		funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (Nigeria)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (Nigeria)";
		}
					
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory,acctInfoMap.get("withdrawAmount") , Brand);		
	    System.out.println("Made Nigeria bank wire withdraw of "+ acctInfoMap.get("withdrawAmount") +" from account "+fromAccount);
		
	}
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Thailand Bank
	void WithdrawThaiBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{

		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawThailand thaiCls = new withdrawThailand(driver,Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Thailand instant bank wire transfer";
				break;
			case "fsa":
			case "svg":
				methodName="LOCAL TRANSFER:Thailand Instant Bank Wire Transfer";
				break;
				
			
			default:
				methodName = "Thailand Instant Bank Wire Transfer";
			
		}

		System.out.println("***Entering WithdrawThaiBank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
	
		   
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(thaiCls, Brand, TestEnv, methodName, "");		
		Thread.sleep(1000);    
		     

    	 //Input Thailand bank withdraw form
    	 withdrawThaiForm(thaiCls, acctInfoMap.get("withdrawAmount"), Brand);
	       
	     //Click Submit in withdraw Form
    	thaiCls.getSubmitBtn().click();
 	    Thread.sleep(3000);
	  
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	   	Thread.sleep(500);
		funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (Thailand)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (Thailand)";
		}
					
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory,acctInfoMap.get("withdrawAmount") , Brand);		
	    System.out.println("Made Thailand bank wire withdraw of "+ acctInfoMap.get("withdrawAmount") +" from account "+fromAccount);
	}
	
	
	void funcNavigate2WithdrawHistory(String Brand) throws Exception
	{
		
		navigationBar navCls = new navigationBar(driver, Brand);
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait10);
		wait10.until(ExpectedConditions.visibilityOf(navCls.getTrasactionHistoryMenuEle())).click();
		Utils.waitUntilLoaded(driver);
		driver.findElement(By.xpath("//ul[@class='tab_demo']//li[contains(translate(., 'Withdrawals', 'WITHDRAWALS'),'WITHDRAWALS')]")).click();
		
		
	}
	
	void funBack2HomePage() throws Exception
	{
		wait10.until(ExpectedConditions.elementToBeClickable(By.xpath(".//a[contains(text(),'Back To Home Page')]"))).click();   
	}
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through SticPay
	void WithdrawSticPay(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String methodName;
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="";  //VT doesn't support Astropay
				break;
			case "fsa":
			case "svg":
				methodName = "E-WALLET:SticPay";
				break;
			default:
				methodName = "SticPay";
			
		}
		
		funcWithdrawWithEmail(TraderURL,Brand, TestEnv, methodName);	
	}
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Bitwallet
	void WithdrawBitwallet(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String methodName;
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Bitwallet";
				break;
			case "fsa":
			case "svg":
				methodName = "E-WALLET:Bitwallet";
				break;
			default:
				methodName = "Bitwallet";
			
		}
		
		funcWithdrawWithEmail(TraderURL,Brand, TestEnv, methodName);	
	}
	
	//@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}

	
	/*Yanni on 03/02/2021: when PUG/VT withdraw amount is less than $100, it will popup a confirm dialog after user submits the withdraw requests.
	 * This function is click Confirm button in the popup dialog
	*/
	/*
	 * void confirmFeeDialog(BigDecimal withdrawAmount, String Brand)
	 * {
	 * //Confirm in the withdraw fee popup dialog if it pops up
	 * if(withdrawAmount.compareTo(new BigDecimal("100.00"))<0)
	 * {
	 * switch(Brand.toLowerCase())
	 * {
	 * case "vt":
	 * driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
	 * break;
	 * 
	 * case "pug":
	 * driver.findElement(By.xpath("//span[contains(text(),'CONFIRM')]")).click();
	 * break;
	 * 
	 * default:
	 * }
	 * 
	 * }
	 * 
	 * }
	 */
	
	/*
	 * Yanni on 03/02/2021: extracted from functions. This piece of code is to click Account Number control in Withdraw Form
	 */
	/*
	 * void withdrawClickAccountlist(String Brand)
	 * {
	 * //Select the from account and verify if there is available from account
	 * if(Brand.equalsIgnoreCase("vt")) {
	 * driver.findElement(By.xpath("//div[@class='el-form-item is-success is-required']//input")).click();
	 * }else {
	 * driver.findElement(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input")).click();
	 * }
	 * 
	 * }
	 */

	/*
	 * Yanni on 03/02/2021: Thailand Witdhraw Form extracted from code
	 */
	void withdrawThaiForm(withdrawThailand thaiCls, String withdrawAmount, String Brand) throws Exception
	{
		
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		thaiCls.getBankAccountSelelctEle().click();
		
		bankAcctList = thaiCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
			//Input Bank name														
			thaiCls.getBankNameEle().sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
			
			 //Input bank address
			thaiCls.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
			
			//Input Bank Beneficiary Name
			thaiCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
			
			//Input Account Number
			thaiCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));					
	
			switch(Brand.toLowerCase())
			{
				case "fsa":
				case "svg":
					//Input Account Holder's address： pug only
					thaiCls.getAcctHolderEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");					
				
			}			
	
			//Input Swift
			thaiCls.getSwiftCodeEle().sendKeys(Utils.randomNumber(5));				
		}
		
		//Input Note
		thaiCls.getNotesEle().sendKeys("Test CP Thailand Bank Withdraw " + withdrawAmount);
		
	
	}
	
		
	void handleWithdrawHold(String Brand, String fromAccount)
	{
	     //Check whether the account is in HOLD position or not. When it is in HOLD, click Confirm button
		 try 
	     {
	    	driver.findElement(By.xpath("//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]")).click();
	    	System.out.println(fromAccount + " is in HOLD position.");
	    		
	        	
	     }catch (Exception e) {
	    	System.out.println(fromAccount + " is not on hold position");
	     }
		 
	}
	
	
	//Credit Card Withdraw Form: return actual withdraw amount. CC amount will be modified based on each Credit card's quota
	 String withdrawCCForm(withdrawCC ccCls, String withdrawAmount) throws Exception
	{
		
		List<WebElement> ccNumberList = null;
		String ccAmount = withdrawAmount;
		
		//Click 1st row in Credit Card List. If there is no saved Credit card, it would be "Manually add credit cards..."
		ccCls.getSelectCCEle().click();
		Thread.sleep(500);
		
		ccNumberList = ccCls.getAllCCList();
		ccNumberList.get(0).click();
		
		Thread.sleep(500);		
		
		//If ccNumberList.size()>1, it means there is at least 1 save cc number. The 1st CCNumber is selected. Else it would be "Manualled add credit cards"
		if(ccNumberList.size()>1)
		{			

			//to extract the withdraw quota of this card
			String extractQuota = ccCls.getCCWDQuota();
			
			//If extracted quota is not null, ccAmount will be extract quota
			if(extractQuota.length()>0)
			{
				ccAmount = extractQuota;
			}
			
			//reset withdraw amount to the quota
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", ccCls.getAmountEle());
			Thread.sleep(500);
			ccCls.getAmountEle().clear();
			ccCls.getAmountEle().sendKeys(extractQuota);
					
			//If ccNumberList.size()>1, it means there is at least 1 save cc number. The 1st CCNumber is selected. Only need to input notes.
			ccCls.getNotesEle().sendKeys("Test CreditCard Withdraw " + extractQuota);
			
		}else if(ccNumberList.size()==1)
		{
			//Only one option "Manualled add credit cards". Needs to input every field
			ccNumberList.get(0).click();
			
			//Input Name on Card
			ccCls.getNameOnCardEle().sendKeys(Utils.randomString(5).toUpperCase()+" "+Utils.randomString(4).toUpperCase());
			
			//Input first 6 digits on Card
			ccCls.getFirstSixEle().sendKeys(Utils.randomNumber(6));
			
			//Input last 4 digits on Card
			ccCls.getLastFourEle().sendKeys(Utils.randomNumber(4));
			
			//Select Expiry Year randomly
			ccCls.setExpiryYearRandom();
			Thread.sleep(500);
			
			//Set Expiry Date randomly
			ccCls.setExpiryMonthRandom();
			
			//Notes
			ccCls.getNotesEle().sendKeys("Test CreditCard Withdraw " + withdrawAmount);
			
		}else if(ccNumberList.size() <1) 
		{
			Assert.assertTrue(false, "No options in Credit Card List to choose.");
		}	
		
		return ccAmount;
	}
	
	//Yanni on 04/02/2021: Extracted from code. International Bank Transfer Form
	void withdrawI18NForm(withdrawI18N i18nCls) throws Exception
	{
				
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		i18nCls.getBankAccountSelelctEle().click();
		
		bankAcctList = i18nCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		
		if(bankAcctList.size()==1)
		{
			//when there is only 1 option (Add bank account) available, need to do the following
			
			//Select region as International
		     i18nCls.getBankCountryEle("International").click();		     
		     Thread.sleep(1000);
		     
	    	//input bank name
			i18nCls.getBankNameEle().sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
	    	
	    	//Input bank address
	    	i18nCls.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
				
			//Input Bank Beneficiary Name
			i18nCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());	
			
		     //Input Bank Account Number
			i18nCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));					

			 //Input Account Holder's Address
			i18nCls.getAcctHolderAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Road");

				
			//Input Swift
			i18nCls.getSwiftCodeEle().sendKeys(Utils.randomNumber(4));
				
			//Input ABA/Sort Code
			i18nCls.getABACodeEle().sendKeys(Utils.randomNumber(5));
			
			//Input Note
			i18nCls.getNotesEle().sendKeys("Test CP International Bank Withdraw " + withdrawAmount.toString());
				
			//upload file
			i18nCls.getUploadFileEle().sendKeys(Utils.workingDir+"\\proof.png");
			Thread.sleep(1500);		
	   
			 
		}else if(bankAcctList.size()>1)
		{
			/*when there is at least 1 saved bank account,  account infor is populated autoamtically when selecting this account.
			 * Only Notes  are required to input
			*/
				
			//Input Note
			i18nCls.getNotesEle().sendKeys("Test CP International Bank Withdraw " + withdrawAmount.toString().toString());
				
			
		}
		

	
	}
	
	//withdraw VietNamForm: Vietnam instant bank wire transfer	
	void withdrawVNamForm(withdrawVietNam vnCls, String withdrawAmount) throws Exception
	{
	 
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		vnCls.getBankAccountSelelctEle().click();
		
		bankAcctList = vnCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
	     //Input Bank name														
		 wait10.until(ExpectedConditions.visibilityOf(vnCls.getBankNameEle())).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		
		 //Input bank address
		vnCls.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
		
		//Input Bank Beneficiary Name
		vnCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
		
		//Input Account Number
		vnCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));	
		
		}

		//Input Note
		vnCls.getNotesEle().sendKeys("Test CP Vietnam Bank Withdraw " + withdrawAmount.toString());	

	}
	
	
	//Yanni on 26/03/2021: this function performs withdraw common operations: select account; input amount, select withdraw method. 
	//Return fromAccount and actual withdrawAmount. Because of CC quota, CC actual withdraw amount might be different with withdrawAmount、
	Map<String, String> funcWDGeneralPart(withdrawGeneral generalCls, String Brand, String TestEnv, String Method, String withdrawAmount) throws Exception
	 {
			
		 	String fromAccount = "";
		 	Map<String, String> acctInfoMap= new HashMap<String, String>();
		 	int i = 0;
		 	
		 	//Click the withdraw account element to show Account List
		 	generalCls.getFromAcctEle().click();
			
			Thread.sleep(1000);
			List<WebElement> fromElement = null ;		       
					
			//Get withdraw Account List items
			fromElement=generalCls.getAllWithdrawAccounts();
				
			if (fromElement == null || fromElement.size()==0) {
			   Assert.assertTrue(false, "This user doesn't have any account!");
			}
			   
			for (i=0;i<fromElement.size();i++)
			{
			   
				WebElement webElement = fromElement.get(i);
		       String accountInfo = webElement.getText();
			   
			   System.out.println("account info: " + accountInfo);
			   
			   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
			   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
			   
			   //Define withdraw amount. If parent pass withdrawAmount as "", then use 1%*amount. Otherwise use withdrawAmount			   
			   if(withdrawAmount.length()==0)
			   {
				   withdrawAmount = amount.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			   }
			  			   
			   //Choose the account that in test group and has non-zero balance
			   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) && amount.compareTo(miniAmount)>=0)
			   {
			    
				   
				   //click on the selected from account
			     webElement.click();
			     Thread.sleep(500);
			     
			     //Handle HOLD dialog if the dialog pops up
			     handleWithdrawHold(Brand, fromAccount);
				 
			     //Input withdraw amount
			     generalCls.getAmountEle().sendKeys(withdrawAmount.toString());
			     Thread.sleep(500);	
			     
			     //Select withdraw Method
			     Thread.sleep(1000);
			     generalCls.selectWDMethodEle(Method).click();
			     Thread.sleep(1000);
			     
			     break;
			   }
			}
			
			if(i>fromElement.size())
			{
				System.out.println("No Qualified Account for Withdraw (Account group is not a test group or account balance <=0 ");
				Assert.assertTrue(false, "No Qualified Account for Withdraw (Account group is not a test group or account balance <=0 ");
			}
			
			acctInfoMap.put("fromAccount", fromAccount);
			acctInfoMap.put("withdrawAmount", withdrawAmount.toString());
	
			return acctInfoMap;
	 }

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Thailand Bank
	void WithdrawSAfrica(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawSAfrica saCls = new withdrawSAfrica(driver,Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="";
				break;
			case "fsa":
			case "svg":
				methodName="LOCAL TRANSFER:South Africa Instant Bank Wire Transfer";
				break;
				
			
			default:
				methodName = "South Africa Instant Bank Wire Transfer";
			
		}
	
		System.out.println("***Entering Withdraw SouthAfrica Bank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
	
		   
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(saCls, Brand, TestEnv, methodName, "");		
		Thread.sleep(1000);    
		     
	
		 //Input Thailand bank withdraw form
		 withdrawSAForm(saCls, acctInfoMap.get("withdrawAmount"));
	       
	     //Click Submit in withdraw Form
		saCls.getSubmitBtn().click();
	    Thread.sleep(3000);
	
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	   	Thread.sleep(500);
		funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (South Africa)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (South Africa)";
		}
					
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory,acctInfoMap.get("withdrawAmount") , Brand);		
	    System.out.println("Made South Africa bank wire withdraw of "+ acctInfoMap.get("withdrawAmount") +" from account "+fromAccount);
	}

	/*
	 * Yanni on 03/02/2021: Thailand Witdhraw Form extracted from code
	 */
	void withdrawSAForm(withdrawSAfrica saCls, String withdrawAmount) throws Exception
	{
		
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		saCls.getBankAccountSelelctEle().click();
		
		bankAcctList = saCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
			//Input Bank name														
			saCls.getBankNameEle().sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
			
			 //Input bank address
			saCls.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
			
			//Input Bank Beneficiary Name
			saCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
			
			//Input Account Number
			saCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));	
				
		}
		
		//Input Note
		saCls.getNotesEle().sendKeys("Test CP Thailand Bank Withdraw " + withdrawAmount);
		
	
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Vietnam Bank
	void WithdrawIndonesiaBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawIndonesia inCls = new withdrawIndonesia(driver, Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Indonesia instant bank transfer";
				break;
			case "fsa":
			case "svg":
				methodName = "LOCAL TRANSFER:Indonesia Instant Bank Transfer";
				break;
			
			default:
				methodName = "Indonesia Instant Bank Transfer";
			
		}
	
		System.out.println("***Entering WithdrawIndonesiaBank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
		
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(inCls, Brand, TestEnv, methodName, "");
		
		Thread.sleep(1000);    
	     //Input VietNam Wtidhraw form
	     withdrawIndonesiaForm(inCls, acctInfoMap.get("withdrawAmount"));
	   				
	     //Click Submit in withdraw Form
		 inCls.getSubmitBtn().click();
	     Thread.sleep(3000);
	       
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	    Thread.sleep(500);
	    funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (Indonesia)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (Indonesia)";
		}
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory, acctInfoMap.get("withdrawAmount"), Brand);		
	    System.out.println("Made Indonesia bank wire withdraw of "+acctInfoMap.get("withdrawAmount")+" from account "+fromAccount);	  	
	
		
	}

	//withdraw VietNamForm: Vietnam instant bank wire transfer	
	void withdrawIndonesiaForm(withdrawIndonesia inCls, String withdrawAmount) throws Exception
	{
	
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		inCls.getBankAccountSelelctEle().click();
		
		bankAcctList = inCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
	     //Input Bank name														
		 wait10.until(ExpectedConditions.visibilityOf(inCls.getBankNameEle())).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		
		//Input Bank Beneficiary Name
		 inCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
		
		//Input Account Number
		 inCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));			 

		}
	
		//Input Note
		inCls.getNotesEle().sendKeys("Test CP India Bank Withdraw " + withdrawAmount.toString());	
	
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Vietnam Bank
	void WithdrawIndiaBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawIndia inCls = new withdrawIndia(driver, Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="India instant bank wire transfer";
				break;
			case "fsa":
			case "svg":
				methodName = "LOCAL TRANSFER:India Instant Bank Wire Transfer";
				break;
			
			default:
				methodName = "India Instant Bank Wire Transfer";
			
		}
	
		System.out.println("***Entering WithdrawIndiaBank");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
		
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(inCls, Brand, TestEnv, methodName, "");
		
		Thread.sleep(1000);    
	     //Input VietNam Wtidhraw form
	     withdrawIndiaForm(inCls, acctInfoMap.get("withdrawAmount"));
	   				
	     //Click Submit in withdraw Form
		 inCls.getSubmitBtn().click();
	     Thread.sleep(3000);
	       
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	    Thread.sleep(500);
	    funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (India)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (India)";
		}
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory, acctInfoMap.get("withdrawAmount"), Brand);		
	    System.out.println("Made India bank wire withdraw of "+acctInfoMap.get("withdrawAmount")+" from account "+fromAccount);	  	
	
		
	}

	//withdraw Phlippine instant bank wire transfer	
	void withdrawPhilippineForm(withdrawPhilippine inCls, String withdrawAmount) throws Exception
	{
	
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		inCls.getBankAccountSelelctEle().click();
		
		bankAcctList = inCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
	     //Input Bank name														
		 wait10.until(ExpectedConditions.visibilityOf(inCls.getBankNameEle())).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		
		 //Input bank address
		 inCls.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
		
		//Input Bank Beneficiary Name
		 inCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
		
		//Input Account Number
		 inCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));		 

		
		}
	
		//Input Note
		inCls.getNotesEle().sendKeys("Test CP Phlippine Bank Withdraw " + withdrawAmount.toString());	
	
	}

	//withdraw Malay Form: Malaysian instant bank wire transfer	
	void withdrawMalayForm(withdrawMalay malayCls, String withdrawAmount) throws Exception
	{
	
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		malayCls.getBankAccountSelectEle().click();
		
		bankAcctList = malayCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account information. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
	     //Input Bank name														
		 wait10.until(ExpectedConditions.visibilityOf(malayCls.getBankNameTxtbox())).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		
		 //Input bank address
		 malayCls.getBankAddTxtbox().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
		
		//Input Bank Beneficiary Name
		 malayCls.getBankBeneficiaryNameTxtbox().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
		
		//Input Account Number
		 malayCls.getBankAcctNoTxtbox().sendKeys(Utils.randomNumber(8));			 
	
		}
	
		//Input Note
		malayCls.getNotesTxtbox().sendKeys("Test CP Malaysian Bank Withdraw " + withdrawAmount.toString());	
	
	}

	/*
	 * Yanni on 07/04/2021: Nigeria withdraw Form extracted from code
	 */
	void withdrawNigeriaForm(withdrawNigeria nigCls, String withdrawAmount) throws Exception
	{
		
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		nigCls.getBankAccountSelectEle().click();
		
		bankAcctList = nigCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
			//Input Bank name														
			nigCls.getBankNameTxtbox().sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
			
			 //Input bank address
			nigCls.getBankAddrTxtbox().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
			
			//Input Bank Beneficiary Name
			nigCls.getBankBeneficiaryTxtbox().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
			
			//Input Account Number
			nigCls.getBankAcctNoTxtbox().sendKeys(Utils.randomNumber(8));	
				
		}
		
		//Input Note
		nigCls.getNotesTxtbox().sendKeys("Test CP Nigeria Bank Withdraw " + withdrawAmount);
		
	
	}

	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","Brand","TestEnv"})
	//withdraw through Phlippine Bank
	void WithdrawPhilippineBank(String TraderURL,String Brand, String TestEnv) throws Exception
	{
	
		String fromAccount = "";
		String methodName="";
		Map<String,String> acctInfoMap = new HashMap<String, String>();
		withdrawPhilippine inCls = new withdrawPhilippine(driver, Brand);
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Philippine Instant Bank Transfer";
				break;
			case "fsa":
			case "svg":
				methodName = "LOCAL TRANSFER:Philippine Instant Bank Transfer";
				break;
			
			default:
				methodName = "Philippine Instant Bank Transfer";
			
		}
	
		System.out.println("***Entering Withdraw Philippine Instant Bank Transfer");
		driver.navigate().to(TraderURL);
		
		funcGo2WithdrawPg(Brand);
		
		//Select qualified account, input amount and choose withdraw method. Return the info of selected account and withdraw amount
		acctInfoMap = funcWDGeneralPart(inCls, Brand, TestEnv, methodName, "");
		
		Thread.sleep(1000);    
	     //Input VietNam Wtidhraw form
	     withdrawPhilippineForm(inCls, acctInfoMap.get("withdrawAmount"));
	   				
	     //Click Submit in withdraw Form
		 inCls.getSubmitBtn().click();
	     Thread.sleep(3000);
	       
	    //Back to home page
	    funBack2HomePage();
	    
	    //Verify the transaction history
	    Thread.sleep(500);
	    funcNavigate2WithdrawHistory(Brand);
		Thread.sleep(1000);
		
		String withdrawMethodinHistory = "Internet Banking (Philippine)";
		if(Brand.equalsIgnoreCase("vt"))
		{
			withdrawMethodinHistory = "Internet banking (Philippine)";
		}
		
		CPDeposit.funcValidateTransacHistory(driver, fromAccount, withdrawMethodinHistory, acctInfoMap.get("withdrawAmount"), Brand);		
	    System.out.println("Made Phlippine bank wire withdraw of "+acctInfoMap.get("withdrawAmount")+" from account "+fromAccount);	  	
	
		
	}

	//withdraw VietNamForm: Vietnam instant bank wire transfer	
	void withdrawIndiaForm(withdrawIndia inCls, String withdrawAmount) throws Exception
	{
	
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		inCls.getBankAccountSelelctEle().click();
		
		bankAcctList = inCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		Thread.sleep(1000);
		
		if(bankAcctList.size()==1)
		{
		
	     //Input Bank name														
		 wait10.until(ExpectedConditions.visibilityOf(inCls.getBankNameEle())).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		
		 //Input bank address
		 inCls.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
		
		//Input Bank Beneficiary Name
		 inCls.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3));
		
		//Input Account Number
		 inCls.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));	
		 
	    //Input Bank Branch
		 inCls.getBankBranchEle().sendKeys(Utils.randomString(5) + " Branch");
		 
		 //Input IFSC CODE
		 inCls.getIFSCodeEle().sendKeys(Utils.randomNumber(6));
		
		}
	
		//Input Note
		inCls.getNotesEle().sendKeys("Test CP India Bank Withdraw " + withdrawAmount.toString());	
	
	}

}
