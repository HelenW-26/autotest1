package vantagecrm;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
import utils.ExtentReports.ExtentTestManager;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import adminBase.AccountResult;
import adminBase.AccountSearch;
import adminBase.CreditAdjust;
import io.github.bonigarcia.wdm.WebDriverManager;

public class UpdateTradACC {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String serverName="AU"; //Server Name
	String userName;
	WebDriverWait wait15;
	String newPwd1="12345678Qi";
	String newPwd2="123456789Qi";
	
	static final String defaultEmail="test369Dyg@test.com";
	
	SoftAssert saAssert = new SoftAssert();


	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		  
		  ChromeOptions options=new ChromeOptions();
		  options.setAcceptInsecureCerts(true);
		
			/* System.setProperty("webdriver.chrome.driver", Utils.ChromePath); */
		  WebDriverManager.chromedriver().setup();
		  
		  if(Boolean.valueOf(headless)) {
				options.addArguments("window-size=1920,1080");
				options.addArguments("headless");
			}
    	  driver = new ChromeDriver(options);	
    	  
    	  context.setAttribute("driver", driver);          //Added by Yanni on 5/15/2019
    	  
    	  utils.Listeners.TestListener.driver=driver;
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait15=new WebDriverWait(driver, Duration.ofSeconds(15));
	}
	
	@AfterClass(alwaysRun=true)
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
		ExtentTestManager.startTest(method.getName(),"Description: Login to Admin Portal");
		//Login AU admin
		driver.get(AdminURL);	
		Utils.funcLogInAdmin(driver, AdminName,AdminPass, Brand);
	}

	//resetPWD can change all trading account pwds that belongs to "TraderName" and resides on "serverName"
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TraderName", "Brand"})
	public void resetPWD(@Optional(defaultEmail) String userEmail, String Brand, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: reset mt4 trading password");
		int j,flag=0;		
		String a;
		String account, serverName;
		int numofServers=1;
		
		System.out.println("TEST STARTS: Start to resetPWD for user " + userEmail + " ....");
		System.out.println("-------------------------------------------------------------");
		//Navigate to Client menu
		driver.navigate().refresh(); //This line is required. In XML file, its prior test is to changeAccountGroup which also happens in Trading Account view.
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Trading Account")).click();
		
		Thread.sleep(2500);
		//Wait until the list is loaded completely		
		//wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		AccountSearch searchBar = new AccountSearch(driver);
		searchBar.inputUserEmail(userEmail);
		searchBar.clickSearch();	
		Thread.sleep(500);			
		
		//Wait until the list is loaded completely
		try {
			wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		}catch(TimeoutException e)
		{
			System.out.println("Result is loaded");
		}
		
		//Create AccountResults instance
		AccountResult queryTrs = new AccountResult(driver);
		
		List<WebElement> trs=queryTrs.getAccoutQueryResult();
			
		if(trs.size()==0)
		{
			Assert.assertTrue(false, "Loading list  error.");
		}
			
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println( "No Client is found." );
			
							
		}else {
			for(j=0;j<trs.size();j++)
			{
				userName=queryTrs.getUserEle(trs.get(j)).getText();
				if(Utils.isTestUser(userName))
				{
					
					account=queryTrs.getAccEle(trs.get(j),true).getText();
					serverName = queryTrs.getServerEle(trs.get(j)).getText();
					System.out.println("System is changing password for account "+account + " on MT4/5 Server " + serverName);
					
					//Scroll to the right until the Reset PWD is visible
					WebElement tEle=queryTrs.getResetPwdEle(trs.get(j));
					((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
					wait15.until(ExpectedConditions.visibilityOf(tEle));
					
					//Click the user name to open the client profile
					tEle.click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
					
					//Update password
					driver.findElement(By.id("master")).sendKeys(newPwd1);
					driver.findElement(By.id("investor")).sendKeys(newPwd2);
					
					//Check to send email
					driver.findElement(By.id("sendMailFlag")).click();
					
					//Click Submit button
					driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();

					//Assert success
					a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					System.out.println("Result is "+a);
					wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
					saAssert.assertEquals(a, "Success");
					
					
				}
				
			}
		}
		
	
		saAssert.assertAll();
	
	}

	
	//Change account group to test group
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand","TraderName"})
	public void changeAccountGroup(String testEnv, String Brand, String TraderName, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: change account group to test group");
		int i=2,j;	
		Select t;
		String name,accGroup, accNo; //Used as temporary names
		//String[] keyWords=new String[]{Utils.ibUserPrefix, Utils.addUserPrefix, Utils.webUserPrefix, Utils.addUserPrefix };
		String optionText;
			
		driver.navigate().refresh();
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Trading Account")).click();
		
		System.out.println("TEST STARTS: Start to change account group ...");
		System.out.println("-------------------------------------------------------------");
	
	
		Thread.sleep(2000);

		//Create a new instance searchBar
		//AccountSearch searchBar = new AccountSearch(driver);
		
		//Search by ClientName = keyWords[i]
		//searchBar.inputUserName(keyWords[i]);
		
		//Click Search button
		//searchBar.clickSearch();		
		
		AccountSearch searchBar = new AccountSearch(driver);
		searchBar.inputUserEmail(TraderName);
		searchBar.clickSearch();
		
		
		//Wait until the list is loaded completely	
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		List<WebElement> trs=driver.findElements(By.xpath("//table[@id='table']//tbody//tr"));
		
		
		//if the search result shows no records:
		if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No results for user " + TraderName);
		}else
		{
								
			//Look for 1st Submitted records with name starting with pattern pat. 
			for(j=0;j<trs.size();j++)
			{
				
				//Get user name
				name=trs.get(j).findElement(By.cssSelector("td:nth-of-type(4) a")).getText();	
				System.out.println("Name Yanni: " + name);
				
				//Get Trading account no.
				accNo=trs.get(j).findElement(By.cssSelector("td:nth-of-type(9) a")).getText();	
				
                //Get acc group
				accGroup=trs.get(j).findElement(By.cssSelector("td:nth-of-type(11)")).getText();	
				
				if(Utils.isTestUser(name)) 
				{
					Thread.sleep(2000);
					trs.get(j).findElement(By.linkText(name)).click();
					
					Thread.sleep(3000);		
					System.out.println("name =  " + name + " accountNo = " + accNo);
					//Choose Account Group
					t=new Select(driver.findElement(By.id("mt4set")));
					//If the list has only one option (that is "Select"), means the account group list has not been loaded. Wait until it is loaded
					while(t.getOptions().size()==0)
					{
						Thread.sleep(1500);
					}
					
					switch(Brand)
					{
						case "au":
							System.out.println("Change to another test_ account group if brand is AU.");
						    //Select the first option if in test env
						    Thread.sleep(1000);
						
						    driver.findElement(By.id("mt4set_chosen")).click();
						    Thread.sleep(1000);
						    //Input test_ to filter all the account group starts with test_ and then select another one
						    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys("test_");
						    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys(Keys.ARROW_DOWN);
						    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys(Keys.RETURN);
						    if (driver.findElement(By.cssSelector("a.chosen-single span")).getText().equals(accGroup)) {
						    	driver.findElement(By.id("mt4set_chosen")).click();
							    Thread.sleep(1000);
							    //Input test_ to filter all the account group starts with test_ and then select another one
							    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys("test_");
							    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys(Keys.ARROW_UP);
							    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys(Keys.RETURN);
						    }
						    
							break;
						
						case "ky":
						case "vt":
						case "svg":
						case "fsa":
						case "fca":
						case "vfsc":
						case "vfsc2":
						case "regulator2":
							System.out.println("Change to another test_ account group .");
						    //Select the first option if in test env
						    Thread.sleep(1000);
						
						    driver.findElement(By.id("mt4set_chosen")).click();
						    Thread.sleep(1000);
						    
							for(int count=1; count < t.getOptions().size();count++)
							{
								optionText = t.getOptions().get(count).getText();
								if(optionText.startsWith("test_") && !optionText.equalsIgnoreCase(accGroup.trim()) )
								{
									Thread.sleep(1000); 
								    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys(optionText);
								    driver.findElement(By.cssSelector("div.chosen-search > input")).sendKeys(Keys.RETURN);
								    System.out.println("New Account Group: " + optionText );
									Thread.sleep(1000); 
									break;
								}
							}	

							break;
							
						
						default:
							System.out.println(Brand + " is NOT supported.");
							break;
							
					}
						
					//Click Confirm button
					Thread.sleep(1000);
					driver.findElement(By.xpath(".//button[text()='Submit']")).click();
					Thread.sleep(1000);
					
					//Print message
					//Assert success
					String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					System.out.println("Result is: "+a);
					wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
					saAssert.assertEquals(a, "Success");
					
					//Check the balance
					String group2=driver.findElement(By.xpath("//tr[1]//td[7]")).getText();
					System.out.println("Result is: "+a+". Account group changed from "+accGroup+" to "+group2);
					System.out.println("When group is not changed, MT4 connect manager might has no right to change groups.");
					
					//break the current loop, only audit one account each time
					break;
				}
			}
			
			if(j>=trs.size())
			{
				System.out.println("No Qualified Account for user: " + TraderName );
				System.out.println();
			}
		
	    }
	}
		
	//Add Bpay number for each trading account that belongs to "TraderName"
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TraderName","Brand"})
	public void addBpayNo(@Optional(defaultEmail) String userEmail, String Brand) throws Exception
	{
				
		int j,flag=0;		
		String a;
		String account, country, curr;
		WebElement tEle;
		String serverName;
		int numofServers=0;
			
		System.out.println("TEST STARTS: Start to add BPayNo....");
		System.out.println("-------------------------------------------------------------");
		driver.navigate().refresh();
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Trading Account")).click();
		
		//Wait until the list is loaded completely		
		Thread.sleep(3000);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		numofServers = DBUtils.getNumOfServers(Brand);
		
		for (int i=1; i<=numofServers; i++) {
			if(flag==1) {
				break;
			}
			
			Thread.sleep(1000);
			
			if(!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("svg") && !Brand.equalsIgnoreCase("fsa"))
			{
				//Select Server
				//driver.findElement(By.id("server_type")).click();
				//liufeng
				driver.findElement(By.xpath("//div[contains(text(),'Server')]/../div[2]/div/div/button")).click();
				if(i>1) {
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/ul/li["+i+"]"))).click();//unselect
				}
				
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/ul/li["+(i+1)+"]"))).click();
							
				//wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.button_select.btn-group.open ul.dropdown-menu.bj>li:nth-of-type("+i+")>a")));
	
				//driver.findElement(By.cssSelector("div.button_select.btn-group.open ul.dropdown-menu.bj>li:nth-of-type("+i+")>a")).click();
				Thread.sleep(1000);
			}
			//Get selected server name
			serverName=driver.findElement(By.xpath("//div[contains(text(),'Server')]/../div[2]/div/div/button/span")).getText();
			//serverName=driver.findElement(By.cssSelector("#server_type div")).getText();
			
			//Select "Search by email"
			//do not have options
			//Select t = new Select(driver.findElement(By.id("search_option")));
			//t.selectByValue("contactEmail");
			
			//Input keyword, modify by feng liu
			WebElement email = driver.findElement(By.xpath("//div[contains(text(),'Login Email')]/../div[2]/div/input"));
			//driver.findElement(By.id("userQuery")).clear();
			//driver.findElement(By.id("userQuery")).sendKeys(userEmail);
			email.clear();
			email.sendKeys(userEmail);
			
			//Click Search button
			driver.findElement(By.id("query")).click();
			
			//Wait until the list is loaded completely
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
					
			//List<WebElement> trs=driver.findElements(By.cssSelector("table#accountList>tbody>tr"));
			List<WebElement> trs=driver.findElements(By.xpath("//table[@id='table']/tbody/tr"));
			
			if(trs.size()==0)
			{
				System.out.println( "Loading list  error.");
			}
			
			if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
			
				System.out.println("No Trading Account is found for user " + userEmail + " with server " + serverName);
			}else {
				for(j=0;j<trs.size();j++)
				{
				
					//trs=driver.findElements(By.cssSelector("table#accountList>tbody>tr"));
					trs = driver.findElements(By.xpath("//table[@id='table']/tbody/tr"));
					//change td:nth-of-type(3)>a to td:nth-of-type(4)>a
					userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a")).getText();
					if(Utils.isWebUser(userName)|| Utils.isAddUser(userName)|| Utils.isJoint(userName)|| Utils.isTestIB(userName))
					{
						
						//change td:nth-of-type(3)>a to td:nth-of-type(9)>a
						account=trs.get(j).findElement(By.cssSelector("td:nth-of-type(9)>a")).getText();
					
						
						//Click the user name to open dialog
						tEle=trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a"));	
						Thread.sleep(1000);
						tEle.click();
				
						wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
						
						country=driver.findElement(By.cssSelector("form.form-horizontal div.form-group:nth-of-type(7) div.col-md-3:nth-of-type(1)>label")).getText();
						curr=driver.findElement(By.cssSelector("form.form-horizontal div.form-group:nth-of-type(5) div.col-md-3:nth-of-type(2)>label")).getText();
						
						
						a=driver.findElement(By.id("bpayNumber")).getAttribute("value");
					
						//Set the account's bpay number to the trading account no. if it doesn't have bpay number and currency= AUD and country = Australia
						if(country.equals("Australia") && curr.equals("AUD") && ((a==null)||a.equals("")))
						{
							System.out.println("System is adding Bpay number for account "+account);
							driver.findElement(By.id("bpayNumber")).sendKeys(account);
							
							//Click Submit button
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
								
							//Assert success
							a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
							System.out.println("Result is "+a);
							wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
							saAssert.assertEquals(a, "Success");
							flag=1;
						}else
						{
							//Click Cancel button	
							System.out.println(account+ " is not qualified: country="+country + " currency="+curr +" and Bpay No.="+a);
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							//wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#accountList>tbody>tr")));
							
						}
												
					}else {
						System.out.println("Not an automation user!");
					}
					
					
				}
			}

		}	
		saAssert.assertAll();	
			
		
	}

	
	//Credit in and Credit out to 0
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TraderName","Brand"})
	public void changeCredit(@Optional(defaultEmail) String userEmail, String Brand, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: Credit in and Credit out to 0");
		int j,flag=0;		
		String a;
		String account;
		int numofServers;
		Select t;
		
		System.out.println("TEST STARTS: Start to change credit...");
		System.out.println("-------------------------------------------------------------");
		//Navigate to Client menu
		driver.navigate().refresh();
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Trading Account")).click();
		Thread.sleep(2000);
	
		//Create a new instance searchBar
		AccountSearch searchBar = new AccountSearch(driver);
		CreditAdjust creditDlg = new CreditAdjust(driver);
		
		//Select All Servers
		searchBar.selectServer("[Select all]");
		
		//Input userEmail
		searchBar.inputUserEmail(userEmail);
		
		//Click Search button
		searchBar.clickSearch();

				
		//Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
				
		//List<WebElement> trs=driver.findElements(By.cssSelector("table#table>tbody>tr"));
		List<WebElement> trs = driver.findElements(By.xpath("//table[@id='table']/tbody/tr"));
	
		
		if(trs.size()==0)
		{
			Assert.assertTrue(false, "Loading list  error.");
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println( "No Client is found under server ");			
		
		}else {
			for(j=0;j<trs.size();j++)
			{
				userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(4)>a")).getText();
				if(Utils.isTestUser(userName))
				{
					
					account=trs.get(j).findElement(By.cssSelector("td:nth-of-type(9)>a")).getText();
					System.out.println("System is changing credit for account "+account);
					
					//Scroll to the right until the Credit column is visible
					WebElement tEle=trs.get(j).findElement(By.cssSelector("a.update"));
					((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
					wait15.until(ExpectedConditions.visibilityOf(tEle));
					
					//Click the change credit link to open the client profile
					tEle.click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
					
					//select credit type as credit in
    				creditDlg.getCreditType().selectByIndex(1);
					
					//input credit amount
    				int amount = 20000;
					creditDlg.getCreditAmount().sendKeys(String.valueOf(amount));
					
	
					creditDlg.inputValidityDate();
					creditDlg.getCreditInNotes().selectByIndex(1); //Select CreditIN
					
					//Click Submit button
					creditDlg.getConfirmBtn().click();
					
					//if the amount exceeds 1000, need extra steps
					if(amount>=1000) {
						driver.findElement(By.xpath("//button[contains(text(),'Proceed')]")).click();
					}
					
					//Assert success
					//a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					System.out.println("Credit In Result is "+amount);
					//wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
					
					//Credit out 
					//Scroll to the right until the Credit column is visible
					Thread.sleep(2000);
					//trs=driver.findElements(By.cssSelector("table#table>tbody>tr"));
					trs = driver.findElements(By.xpath("//table[@id='table']/tbody/tr"));
					tEle=trs.get(j).findElement(By.cssSelector("a.update"));
					
					//Click the user name to open the client profile
					tEle.click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
					
					//select credit type as credit out
	  				creditDlg.getCreditType().selectByIndex(2);
					
					//input credit amount
					creditDlg.getCreditAmount().sendKeys(String.valueOf(amount));
					
					//Input Notes
					Thread.sleep(1000);
					creditDlg.getCreditOutNotes().selectByIndex(1); //Select CreditOut
					
					//Click Submit button
					creditDlg.getConfirmBtn().click();
					
					if(amount>=1000) {
						driver.findElement(By.xpath("//button[contains(text(),'Proceed')]")).click();
					}
					//Assert success
					//a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					System.out.println("Credit Out Result is "+amount);
					//wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
								
					break;
				}else {
					System.out.println("Not automation test user!");
				}
				trs = driver.findElements(By.xpath("//table[@id='table']/tbody/tr"));
			}
		}
				
	

		//saAssert.assertAll();
	
	}
	
	
	//Making transfer between different accounts
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand","TraderName"})
	public void differentUserAccountTransfer(String testEnv, String Brand,String TraderName, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: transfer between different user accounts");
		int i=0,j,k=0;	

		BigDecimal moneyAmount=new BigDecimal("0.00");
		String name1, name2, currency, toAccount,fromAccount; //Used as temporary names
		String[] keyWords=new String[]{Utils.webUserPrefix,Utils.addUserPrefix,Utils.ibUserPrefix};
			
		driver.navigate().refresh();
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Trading Account")).click();
		Thread.sleep(2000);
		
		System.out.println("TEST STARTS: Start to making transfer between different user accounts...");
		System.out.println("-------------------------------------------------------------");
	
		//Select all server
		AccountSearch searchBar = new AccountSearch(driver);
		//searchBar.selectServer("AU3");
		searchBar.selectServer("[Select all]");
		
		//Select search with userName = String
		//Select accound under test user
		searchBar.inputUserEmail(TraderName);
		
		//Click query button
		searchBar.clickSearch();

		
		//Wait until the list is loaded completely	
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		

		//ordered the account amount by descend
		WebElement tEle=driver.findElement(By.xpath("//div[@class='fixed-table-body']//thead//th[13]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
		wait15.until(ExpectedConditions.visibilityOf(tEle));
		tEle.click();
		tEle.click();
		Thread.sleep(3000);
		
		//Create AccountResults instance
		AccountResult queryTrs = new AccountResult(driver);
		
		//Get search  results
		List<WebElement> trs=queryTrs.getAccoutQueryResult();
		
		
		//if the search result shows no records:
		if(trs.size()==1)
		{
			System.out.println("At least two accounts needed. Result not usable for keywords " + keyWords[i]);
		}else
		{				

			
			for(j=1;j<trs.size();j++)
			{
				System.out.println("Entering for loop");
				wait15.until(ExpectedConditions.visibilityOf(queryTrs.getCurrencyEle(trs.get(1))));
				currency=queryTrs.getCurrencyEle(trs.get(0)).getText();	
				
				if(queryTrs.getCurrencyEle(trs.get(j)).getText().equals(currency))
				{
					System.out.println("j = "+j);
					k=j;
					break;
				}
			}
			  
		
			name1 = queryTrs.getUserEle(trs.get(0)).getText();
			name2 = queryTrs.getUserEle(trs.get(j)).getText();
			
			fromAccount = queryTrs.getAccEle(trs.get(0),true).getText();
			toAccount = queryTrs.getAccEle(trs.get(j),true).getText();
			
			moneyAmount = new BigDecimal(queryTrs.getBalanceEle(trs.get(0),true).getText());
				
			if(Utils.isTestUser(name1)&&Utils.isTestUser(name2))
			{
				Thread.sleep(3000);
				//Scroll to the right until the transfer column is visible
				//tEle=trs.get(j).findElement(By.cssSelector("td:nth-of-type(18)>a.accountNo"));
				tEle=queryTrs.getDiffTransferEle(trs.get(0),true);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
				wait15.until(ExpectedConditions.visibilityOf(tEle));
				
				//Open the transfer panel
				tEle.click();
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
				
				Thread.sleep(1000);		
				moneyAmount = moneyAmount.multiply(new BigDecimal("0.10")).setScale(2,BigDecimal.ROUND_HALF_UP);
				
				//input the transfer amount
				driver.findElement(By.id("outMoney")).sendKeys(moneyAmount.toString());
				
				//input the to account
				driver.findElement(By.id("account")).sendKeys(toAccount);
				
				//input the transfer amount
				driver.findElement(By.id("money")).sendKeys(moneyAmount.toString());
				
				//Click Confirm button
				Thread.sleep(2000);
				//Click Submit button
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
				
				//Print message
				//Assert success
				String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
				System.out.println("Result is: "+a+". Making transfer of "+moneyAmount.toString()+" from account "+fromAccount+" to account "+toAccount);
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				saAssert.assertEquals(a, "Success");
				

			}
	    }
	}
	
	//deposit and withdraw from the trading account
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand","TraderName"})
	public void makingAdjustment(String testEnv, String Brand,String TraderName, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: deposit and withdraw from the trading account");
		int i=0,j,k;	
		Random r=new Random();
		String name, balance, balance2, account; //Used as temporary names
		String[] keyWords=new String[]{Utils.webUserPrefix,Utils.addUserPrefix,Utils.ibUserPrefix};
			
		driver.navigate().refresh();
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Trading Account")).click();
		
		System.out.println("TEST STARTS: Start to deposit and withdraw from the trading account...");
		System.out.println("-------------------------------------------------------------");
							
		Thread.sleep(3000);
	
		AccountSearch searchBar = new AccountSearch(driver);		
		
		//Select search with userName = String
		//Select search by email
		searchBar.inputUserEmail(TraderName);
	
		//Click query button
		searchBar.clickSearch();	
		
		//Wait until the list is loaded completely	
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		//Create AccountResults instance
		AccountResult queryTrs = new AccountResult(driver);		
		//Get search  results
		List<WebElement> trs=queryTrs.getAccoutQueryResult();
		
			
		//if the search result shows no records:
		if(trs.size()==0 && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No results for keywords " + keyWords[i]);
		}else
		{				
			for(j=0;j<trs.size();j++)
			{
				//System.out.println("Entering for loop");
				name=queryTrs.getUserEle(trs.get(j)).getText();					
				balance=queryTrs.getBalanceEle(trs.get(j),true).getText();
				account=queryTrs.getAccEle(trs.get(j),true).getText();
				System.out.println("The balance of account "+account+" is: " + balance);
				
				if(Utils.isTestUser(name))
				{
					Thread.sleep(1000);
					//Scroll to the right until the Operation column is visible
					WebElement tEle=queryTrs.getAdjustmentEle(trs.get(j));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
					wait15.until(ExpectedConditions.visibilityOf(tEle));
					
					//Open the transfer panel
					tEle.click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
					
					//Select deposit
					Select t = new Select(driver.findElement(By.id("type")));
					t.selectByIndex(1);
					System.out.println(t.getFirstSelectedOption().getText());
					Thread.sleep(1000);		

					//Input the transfer amount
					driver.findElement(By.id("money")).sendKeys("100");
					
					//Select note
					t = new Select(driver.findElement(By.id("remark")));
					t.selectByIndex(1);
					
					//Click confirm button
					driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();

					//Print message
					//Assert success
					String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
					Thread.sleep(1000);	
					
					//To keep the same record, use AccountNo Search and Load the result again after update				
					searchBar.getSearchActEle().sendKeys(account);
					searchBar.clickSearch();
					Thread.sleep(1000);	
					queryTrs = new AccountResult(driver);
					trs=queryTrs.getAccoutQueryResult();
					
					//Check the balance
					balance2=queryTrs.getBalanceEle(trs.get(0),true).getText();
					System.out.println("Result is: "+a+". Making adjustment of "+account+". Balance changed from "+balance+" to "+balance2);
					
					//withdrawal
					//Scroll to the right until the Operation column is visible
					tEle=queryTrs.getAdjustmentEle(trs.get(0));;
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
					wait15.until(ExpectedConditions.visibilityOf(tEle));
					
					//Open the transfer panel
					tEle.click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
					
					//Select withdrawal
					t = new Select(driver.findElement(By.id("type")));
					t.selectByIndex(2);
					System.out.println(t.getFirstSelectedOption().getText());
					Thread.sleep(1000);		

					//Input the transfer amount
					driver.findElement(By.id("money")).sendKeys("50");
					
					//Select note
					t = new Select(driver.findElement(By.id("remark")));
					t.selectByIndex(2);
					
					//Click confirm button
					driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();

					//Print message
					//Assert success
					a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
					Thread.sleep(2000);

					//Load the result again after update
					searchBar.clickSearch();
					Thread.sleep(1000);
					trs=queryTrs.getAccoutQueryResult();
					
					//Check the balance
					balance=queryTrs.getBalanceEle(trs.get(j),true).getText();
					System.out.println("Result is: "+a+". Making adjustment of "+account+". Balance changed from "+balance2+" to "+balance);
					
					saAssert.assertEquals(a, "Success");
	                break;
					
				}else {
					System.out.println("Not an automation user!");
				}
			}
			  	
	    }
	}
	
	
	    //Check the user group, currency, proxy detail, login status and transaction status 
		@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
		@Parameters(value= {"AdminURL","TestEnv","Brand"})
		public void statusCheck(String AdminURL, String testEnv, String Brand, Method method) throws Exception
		{
			ExtentTestManager.startTest(method.getName(),"Description: Check the user group, currency, proxy detail, login status and transaction status");
			int i=0,j,k,flag=0;	
			Random r=new Random();
			String name, currency,accountGroup, msg; //Used as temporary names
			String msg1="Confirm modify account login status is not available Login"; 
			String msg2="confirm the changes to be login account login status";
			String msg3="confirm the changes the transaction status as non-transaction";
			String msg4="confirm the changes the transaction status is tradeable account";
			String[] keyWords=new String[]{Utils.webUserPrefix,Utils.addUserPrefix,Utils.ibUserPrefix};
				
			driver.navigate().to(AdminURL);	
			//Navigate to Client menu
			driver.findElement(By.linkText("Account Management")).click();
			driver.findElement(By.linkText("Trading Account")).click();
			
			System.out.println("TEST STARTS: Start to Check the user group, currency, proxy detail, login status and transaction status...");
			System.out.println("-------------------------------------------------------------");
								
			Thread.sleep(3000);
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			
			//Select all server
			AccountSearch searchBar = new AccountSearch(driver);
						
			//Select search with userName = String
			searchBar.inputUserName(keyWords[i]);
			
			//Click query button
			searchBar.clickSearch();

		
			//Wait until the list is loaded completely	
			wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			//Create AccountResults instance
			AccountResult queryTrs = new AccountResult(driver);
			
			List<WebElement> trs=queryTrs.getAccoutQueryResult();
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No results for keywords " + keyWords[i]);
			}else
			{				
				for(j=0;j<trs.size();j++)
				{
					
					if(flag==1) {
						break;
					}
					
					System.out.println("Entering for loop" + j);
		
					name=queryTrs.getUserEle(trs.get(j)).getText();
					if(Utils.isTestUser(name))
					{
						flag=1;
						String accountNo = queryTrs.getAccEle(trs.get(j),true).getText();
						System.out.println("Changing Login Status and Transaction Status for:" + name + " "+ accountNo);
						
						//Check if account group and currency are consistent
					    currency=queryTrs.getCurrencyEle(trs.get(j)).getText();	
					    accountGroup=queryTrs.getAccGroup(trs.get(j));
					    Assert.assertTrue(accountGroup.contains(currency), "Account group "+accountGroup+" and currency "+currency+" not consistent!");
					    
					    /***Change login status***/
					    //Click on Login status
					    WebElement tEle=queryTrs.getLoginEle(trs.get(j));
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						wait15.until(ExpectedConditions.visibilityOf(tEle));
						
						//Open the prompt panel
						tEle.click();
						wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
						msg=driver.findElement(By.xpath("//div[@class='bootstrap-dialog-message']")).getText();
						
						if(msg.equals(msg1)) {
							//Click Submit button
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							Thread.sleep(1000);
							
							//Verify the succeed msg
							String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
							Assert.assertTrue(a.contains("Has Been Modified, Please refresh the page to view"),"Message not correct after modification");
							wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
							
							//Verify the login status
							trs=queryTrs.getAccoutQueryResult();
							tEle=queryTrs.getLoginEle(trs.get(j));
							String loginStatus = tEle.getAttribute("title");
							System.out.println("After click, longinStatus is:"+loginStatus);
							saAssert.assertEquals(loginStatus, "can not login");
							Thread.sleep(1000);
							
							//change the loginstatus back
							tEle.click();
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							Thread.sleep(500);
							wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
							Thread.sleep(2000);
							trs=queryTrs.getAccoutQueryResult();
							tEle=queryTrs.getLoginEle(trs.get(j));
							loginStatus = tEle.getAttribute("title");
							System.out.println("After 2nd click, longinStatus is:"+loginStatus);
							
							
						}else if(msg.equals(msg2)) {
							//Click Submit button
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							Thread.sleep(2000);
							
							//Verify the succeed msg
							String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
							Assert.assertTrue(a.contains("Has Been Modified, Please refresh the page to view"),"Message not correct after modification");
							wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
							
							//Verify the login status
							trs=queryTrs.getAccoutQueryResult();
							tEle=queryTrs.getLoginEle(trs.get(j));
							String loginStatus = tEle.getAttribute("title");
							System.out.println("After click, loginStatus is:"+loginStatus);
							saAssert.assertEquals(loginStatus, "can login");
							Thread.sleep(2000);
						}
						
						/***Change transaction status***/
						//Click on Login status
						Thread.sleep(3000);
						trs=queryTrs.getAccoutQueryResult();
					    tEle=queryTrs.getTransEle(trs.get(j));
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						wait15.until(ExpectedConditions.visibilityOf(tEle));
						Thread.sleep(1000);
						
						//Open the prompt panel
						tEle.click();
						wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
						msg=driver.findElement(By.xpath("//div[@class='bootstrap-dialog-message']")).getText();
						
						if(msg.equals(msg3)) {
							//Click Submit button
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							Thread.sleep(2000);
							
							//Verify the succeed msg
							String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
							Assert.assertTrue(a.contains("Has Been Modified, Please refresh the page to view"),"Message not correct after modification");
							
							
							//Verify the login status
							trs=queryTrs.getAccoutQueryResult();
							tEle=queryTrs.getTransEle(trs.get(j));
							String tradeStatus = tEle.getAttribute("title");
							System.out.println("After click, tradeStatus is:"+tradeStatus);
							saAssert.assertEquals(tradeStatus, "non-tradable");
							Thread.sleep(1000);
							
							//change the tradeStatus back
							trs=queryTrs.getAccoutQueryResult();
							tEle.click();
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							Thread.sleep(500);
							wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
							System.out.println("Change the trade status back to can trade");
							Thread.sleep(2000);							
							
						}else if(msg.equals(msg4)) {
							//Click Submit button
							driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
							Thread.sleep(2000);
							
							//Verify the succeed msg
							String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
							Assert.assertTrue(a.contains("Has Been Modified, Please refresh the page to view"),"Message not correct after modification");
							wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
							
							//Verify the Transaction status
							trs=queryTrs.getAccoutQueryResult();
							tEle=queryTrs.getTransEle(trs.get(j));;
							String tradeStatus = tEle.getAttribute("title");
							System.out.println("After click, tradeStatus is:"+tradeStatus);
							saAssert.assertEquals(tradeStatus, "tradeable");
							Thread.sleep(2000);
						}
					}else {
						System.out.println("Not an automation user!");
					}
				}
		    }
		}
		
		//Activate account. Account No is passed from Activation in Trader via ITestContext. If account no is not set, this method doesn't do anything.
		@Test(dependsOnMethods="AdminLogIn",alwaysRun=true)
		@Parameters(value= {"TestEnv","Brand"})
		public void activateAcc(String testEnv, String Brand, ITestContext context) throws Exception
		{
			String accountNo = (String) context.getAttribute("account");

			if(accountNo != null && accountNo.length()>0)
			{
				driver.navigate().refresh();
				
				//Navigate to Account Activate page
				Thread.sleep(2000);
				driver.findElement(By.linkText("Account Management")).click();
				driver.findElement(By.linkText("Account Activate")).click();
				Thread.sleep(1000);
				//Input account
				driver.findElement(By.id("mt4set")).clear();
				driver.findElement(By.id("mt4set")).sendKeys(accountNo);
				
				//Click Activate button
				driver.findElement(By.id("active")).click();
				
				//Assert success
				String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
				System.out.println("Account Activation Result: "+a);
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						
			}else
			{
				
				Assert.assertTrue(false, "No Account is provided for activation.");
				System.out.println("No Account is provided for activation.");
			}
		}
}
