package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
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

import clientBase.navigationBar;
import clientBase.withdrawGeneral;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CPHuaAoWithdraw{
	
	
	WebDriver driver;

	WebDriverWait wait10;
	WebDriverWait wait02;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	
	Select t;  //Account Dropdown
	Random r=new Random();  
	int j; //Select index
	
	/*
	 * //New CPWithdraw instance to use the common functions
	 * CPWithdraw generalWD = new CPWithdraw();
	 */
	
	withdrawGeneral generalWD;
	CPWithdraw withdrawCls = new CPWithdraw();
	
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
    	  //driver = new ChromeDriver();
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);
    	  context.setAttribute("driver", driver);
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait10=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver, Duration.ofSeconds(2));
		  
		  //give current webdriver to withrawCls.driver
			
			  withdrawCls.driver= driver;
			  withdrawCls.wait10=wait10;
			 		  
		 
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
	  	  
		   generalWD = new withdrawGeneral(driver, Brand);
		   
			//Login AU CP
			driver.get(TraderURL);

			Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);	
			Thread.sleep(waitIndex*2000);
						
			Utils.funcVerifyHomePageLiveAccounts(Brand, wait10);
	}
	
	
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true,invocationCount=1)
	@Parameters(value= {"TestEnv","TraderURL","Brand", "AdminURL", "AdminName", "AdminPass"})
	//withdraw through UnionPay
	void WithdrawUnionPayWithBankDetail(String TestEnv, String TraderURL, String Brand, String AdminURL, String AdminName, String AdminPass) throws Exception
	{
		boolean successSubmit = false;
		String account = "";
		String[] ActAmt = new String[2];
		String methodName = "";
		
		BigDecimal moneyAmount=new BigDecimal("0.00");  //amount of withdraw
		BigDecimal min=new BigDecimal("20.00");
		BigDecimal result=new BigDecimal("0.00");
		BigDecimal upperStandard=new BigDecimal("1.50");
		BigDecimal lowerStandard = new BigDecimal("0.50");
		
		switch(Brand)
		{
			case "vt":
				lowerStandard = new BigDecimal("0.50");
				min=new BigDecimal("40.00");
				methodName = "UnionPay";
				break;
			case "fsa":
			case "svg":
				lowerStandard = new BigDecimal("0.50");
				min=new BigDecimal("40.00");
				methodName = "LOCAL TRANSFER:UnionPay";
				break;
				
			case "au":
			case "ky":
			case "vfsc":
			case "vfsc2":
			//case "fca":
			case "regulator2":

			default:					
				lowerStandard = new BigDecimal("0.50");
				break;
				
		}
		
	
		//Go to Withdraw Home Page
		funcGo2WDPage(TraderURL,Brand);
		
		//If there is popup open position dialog, close it so program can go to next step
		Thread.sleep(500);
		
		//Choose one testing account and return the selected account NO. & balance。 If balance < min, deposit 200.99 via Admin API
		ActAmt = funcChooseAcct(Brand, TestEnv, AdminURL, AdminName, AdminPass, min);
		moneyAmount = new BigDecimal(ActAmt[1]);
		account = ActAmt[0];
		
		System.out.println("Selected Account:" + account + " Selected Amount: " + moneyAmount);
		
		Thread.sleep(1000);
		
		//Input amount:
		 
	    //Calculate withdraw amount
		moneyAmount = moneyAmount.multiply(new BigDecimal("0.9")).setScale(2, BigDecimal.ROUND_HALF_UP); 		
		 
		//Input withdraw amount
		generalWD.getAmountEle().sendKeys(moneyAmount.toString());
		Thread.sleep(500);
		
		generalWD.selectWDMethodEle(methodName).click();
		Thread.sleep(500);
		//Select "LOCAL TRANSFER"
		/*
		 * if (Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
		 * driver.findElement(By.xpath("//div[contains(text(),'LOCAL TRANSFER')]")).click();
		 * }
		 */
	    	     

		
		//Check if card is linked or not。 When not linked ,failed the test case.
		try 
    	{
			System.out.println(driver.findElement(By.cssSelector("div.dialog_body p")).getText());
			driver.findElement(By.xpath(".//div[@class='btn_box']/button/span[contains(text(), 'Confirm')]")).click();
						
       		Assert.assertTrue(false, "Please run link card method first.");
        	
    	}catch (NoSuchElementException e) {
    		System.out.println("Already linked card. Proceeding...");
    	}
	

		//Comment out the following paragraph. First card is always selected by default.
		//Select card detail for UnionPay. Always select the first one in the list.
		/*
		 * driver.findElement(By.xpath(".//div[@id='unionPayForm']//input[@placeholder='Select']")).click();
		 * 
		 * List<WebElement> cardList = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		 * cardList.get(0).click();
		 */
		
		
		//Input Note
		if(Brand.equalsIgnoreCase("vt"))
		{			
			driver.findElement(By.id("notes_unionpay")).sendKeys("Test UnionPay Withdraw " + moneyAmount.toString());
		}else
		{
			driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input[@class='el-input__inner']")).sendKeys("Test UnionPay Withdraw " + moneyAmount.toString());
		}
		
		
		/*
		* Return values:
			 *    -- normal result if trading account/rebate account can be found
			 *    -- 8888.88 if account can NOT be found
			 *    -- 8888.66 if margin is 0 (that means this account doesn't have open positions)
			 */
		
		//Calculate (Equity - Withdrawal)/ Margin *100% > 150% 
		result=funcOpenPositionCheck(driver, AdminURL, AdminName, AdminPass, TraderURL, Brand, account,moneyAmount,TestEnv);
				
		//Submit
		/*
		 * if(Brand.equalsIgnoreCase("vt"))
		 * {
		 * driver.findElement(By.xpath(".//span[contains(text(), 'Submit')]")).click();
		 * }else
		 * {
		 * driver.findElement(By.xpath(".//span[contains(text(), 'SUBMIT')]")).click();
		 * }
		 */
		
		generalWD.getSubmitBtn().click();
		Thread.sleep(1000);
		
		//Handle the warning dialog when fee is charged.
		generalWD.clickConfirmFeeBtn();
				
		//If account exists and there are open positions
		if(result.compareTo(new BigDecimal("8888.88"))!=0 && result.compareTo(new BigDecimal("8888.66"))!=0)
		{
			//If (Equity - Withdrawal)/ Margin > lowerStandard and <= UppderStandard, there should be an Alert pop up window 
			if (result.compareTo(upperStandard) <= 0 && result.compareTo(lowerStandard) >0 ) {
				
				
				//Print the dialog message and Confirm the dialog
				System.out.println(driver.findElement(By.cssSelector("div.dialog_body>p")).getText());
				
				//Click CONFIRM button on the warning dialog 
				if(Brand.equalsIgnoreCase("vt"))
				{
					driver.findElement(By.xpath(".//span[contains(text(),'Confirm')]")).click();
				}else
				{
					driver.findElement(By.xpath(".//span[contains(text(),'CONFIRM')]")).click();
				}
				
				Thread.sleep(1000);
					
				successSubmit= true;
		
			} else if(result.compareTo(lowerStandard) <=0) 
				//If smaller than lowerStandard, user is not allowed to submit the withdraw request.
			
			{
				//Withdraw request failed
				//Print the dialog message and Confirm the dialog
				System.out.println(driver.findElement(By.cssSelector("div.dialog_body>p")).getText());
				Thread.sleep(500);
				//Cancel
				if(Brand.equalsIgnoreCase("vt"))
				{
					driver.findElement(By.xpath(".//span[contains(text(), 'Cancel')]")).click();
				}else
				{
					driver.findElement(By.xpath(".//span[contains(text(), 'CANCEL')]")).click();
				}
				
				successSubmit= false;
				
			} 	else{
				//If greater than upper standard, user can submit withdraw request.	
				successSubmit= true;
			}
		
		} else
			
			//If account doesn't have open position, user is allow to submit withdraw request.
		{

			successSubmit= true;
			
		}
    		
		if(successSubmit)
		{
			Thread.sleep(1000);
			
			//Withdraw request submitted
			if(Brand.equalsIgnoreCase("vt"))
				Assert.assertTrue(driver.findElement(By.cssSelector("div.main")).getText().contains("withdrawal request was successful"));
			else
				Assert.assertTrue(driver.findElement(By.cssSelector("p.success_info")).getText().contains("withdrawal request was successful"));
									
			Thread.sleep(500);
			
			//Click BacktoHomePage
			//funBack2HomePage();
			driver.findElement(By.xpath(".//a[contains(text(),'Back To Home Page')]")).click();
			
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
				
				//System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
				WebDriverManager.chromedriver().setup();
		  	  	driver = new ChromeDriver(options);	
		  	  	driver.get(AdminURL);
				
				
				cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand,TestEnv);
				
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
			
			
        	System.out.println("Margin is " + margin + "; Equity is : " + equity + "; Withdraw Amount is : " + amount);
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
        
		String dbName = Utils.getDBName(Brand)[1];
		result = DBUtils.funcReadDBReturnAll(dbName,selectSql, TestEnv);
        
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
			case "vfsc":
			case "vfsc2":
			//case "fca":
			case "regulator2":

				ibExp="A[0-9]{10}$";
				break;	
				
			case "vt":
				ibExp="A[0-9]{8}$";
				break;	
				
			case "fsa":
			case "svg":
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


	@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}
	
	public void funcGo2WDPage(String TraderURL,String Brand) throws Exception
	{
		
			
		navigationBar navCls = new navigationBar(driver, Brand);
		driver.navigate().to(TraderURL);		
		Thread.sleep(2000);
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait10);
		navCls.getFundsEle().click();
		navCls.getWithdrawMenuEle().click();
		
		Thread.sleep(2000);
		
		//Handle OPEN POSITION popup when the default selected account has open positions
		withdrawCls.handleWithdrawHold(Brand, "");
		 
		Thread.sleep(2000);
	}
	
	public String[] funcChooseAcct(String Brand, String TestEnv, String AdminURL, String AdminName, String AdminPass, BigDecimal minAmount) throws Exception
	{
		
		
		String fromAccount; //Account No.
		BigDecimal amount = new BigDecimal("0.00");
		String ActAmt[] = new String[]{"",""};   //First one is Account, 2nd is Amount
		
		//Click the Account Element to show Account list
		generalWD.getFromAcctEle().click();;
		Thread.sleep(500);
		
		List<WebElement> fromElement = null ;
		
		fromElement= generalWD.getAllWithdrawAccounts();
		       
		if (fromElement.size()==0) {
		   Assert.assertTrue(false, "This user doesn't have any account!");
		}

		//Selecting Testing accounts in the account list
		for (int j=0; j< fromElement.size(); j++)
		{
		   
			WebElement webElement = fromElement.get(j);
			String accountInfo = webElement.getText();  //example of accountINfo: 8000607 - $1,000.00 USD
			
		   fromAccount = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
		   amount = CPAccountManagement.funcCPGetBalance(accountInfo);
		   
		   ActAmt[0] = fromAccount;
		   ActAmt[1] = amount.toString();
		   
		   //Choose the account that in test group and has non-zero balance
		   if(Utils.funcCheckAccoutGroup(Brand, TestEnv, fromAccount) &&  accountInfo.contains("USD"))
		   {
		     
				//If the balance is less than minAmount, calling ADMIN API to topup 
			   if(amount.compareTo(minAmount)<=0)	
				{
					String url = Utils.ParseInputURL(AdminURL);					
					String cookie;
					
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
					  	  	WebDriver driverInco = new ChromeDriver(options);	
					  	  	driverInco.get(AdminURL);							
										
							cookie = Utils.getAdminCookie(driverInco, AdminName, AdminPass, Brand,TestEnv);
							
							driverInco.quit();
						
					}
					
					//deposit 200.99 to this account
					//Temporary comment the line below due to new entry parameter introduced.
					//RestAPI.testPostAccountMakeSubmit(url, fromAccount, "200.99", cookie);
					Thread.sleep(500);
										
					//refresh to get the balance after deposit
					driver.navigate().refresh();
					//wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input")));
					
					//Click the from account and verify if there is available from account
					/* driver.findElement(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input")).click(); */
					generalWD.getFromAcctEle().click();;
					Thread.sleep(500); 
					fromElement=generalWD.getAllWithdrawAccounts();
		
					//Get the account balance and accountNo
					accountInfo = fromElement.get(j).getText().trim();
					System.out.println("accountInfo:" + accountInfo);
							
					ActAmt[0] = accountInfo.substring(0,accountInfo.indexOf("-")-1).trim();
			   	    amount = CPAccountManagement.funcCPGetBalance(accountInfo);
					ActAmt[1] = amount.toString();
					
				}
				
				fromElement.get(j).click();
				
			     //Check whether the account is in HOLD position or not
				 withdrawCls.handleWithdrawHold(Brand, fromAccount);
				 
				 break;

			}else
				//Account Group of account is not testing purpose

			{
				System.out.println(fromAccount + ": Account Group is not a testing group or Account is not a USD account. ");
			}
		     
		}
	     
		
		return ActAmt;
	}

}
