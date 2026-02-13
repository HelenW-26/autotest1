package vantagecrm;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import adminBase.AccountResult;
import adminBase.AccountSearch;

public class UpdateRebateACC {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String serverName="AU"; //Server Name
	String userName;
	WebDriverWait wait15,wait30,wait160;
	String newPwd1="12345678Qi";
	String newPwd2="123456789Qi";
	
	static final String defaultEmail="ibtestwkk@test.com";
	
	SoftAssert saAssert = new SoftAssert();


	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		  
		  ChromeOptions options=new ChromeOptions();
		  options.setAcceptInsecureCerts(true);
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
		  
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);	
    	  
    	  context.setAttribute("driver", driver);
    	  utils.Listeners.TestListener.driver=driver;
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait15=new WebDriverWait(driver, Duration.ofSeconds(15));
		  wait30=new WebDriverWait(driver,Duration.ofSeconds(30));
		  wait160=new WebDriverWait(driver,Duration.ofSeconds(160));
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
	
	//Configure commission rules for rebate account 
		@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
		@Parameters({"TraderName","Brand","TestEnv"})
		public void configCommissionRules(String userEmail, String Brand, String TestEnv, Method method) throws Exception
		{
			ExtentTestManager.startTest(method.getName(),"Description: configuring commission rules for specific user");
			int j,k,flag=0;	
			int tradeGroups=10;
			int cryptoPosition=7;
			if (TestEnv.contains("beta")||TestEnv.contains("prod"))
			{
				cryptoPosition=6;
			}
			
			switch (Brand) {

			case "vt":
			case "fsa":
			case "svg":
			case "regulator2":
				tradeGroups=9;
				cryptoPosition=0;
				break;
			default:
				tradeGroups=10;
			}

			//column number starts from 3
			tradeGroups=tradeGroups+3;
			
			String a,check_id="";
			
				
			System.out.println("TEST STARTS: Start to configuring commission rules for specific user...");
			System.out.println("-------------------------------------------------------------");
			driver.navigate().refresh();
			//Navigate to Client menu
			driver.findElement(By.linkText("Account Management")).click();
			driver.findElement(By.linkText("Rebate Account")).click();
			
			//Wait until the list is loaded completely		
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			//Select all server
			AccountSearch searchBar = new AccountSearch(driver);
			Thread.sleep(1000);
			if(!searchBar.selectServerAll())
			{
				Assert.assertTrue(false, "servers not selected");
			}
			//searchBar.selectServer("AU3");
			
			//Select search with userEmail = String
			Thread.sleep(2000);
			searchBar.inputUserEmail(userEmail);
			
			//
			//searchBar.inputAccount("805330324");
			//Click query button
			searchBar.clickSearch();
				
			//Wait until the list is loaded completely
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

			//Create AccountResults instance
			AccountResult queryTrs = new AccountResult(driver);
			
			//Get search  results
			List<WebElement> trs=queryTrs.getAccoutQueryResult();
			
			if(trs.size()==0)
			{
				Assert.assertTrue(false, "Loading list  error.");
			}
			
			if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println( "No Client is found.");
			}else {
				for(j=0;j<trs.size();j++)
				{	
					//Click the icon to open the client profile
					queryTrs.getCommissionRuleEle(trs.get(0)).click();
															
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-body")));
					
					//Click the IB group
					
						driver.findElement(By.xpath("//div[@class='panel-body']//tbody//tr[1]//td[1]")).click();
						
						wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SettingCommissionPlusCommission")));
						Thread.sleep(5000);
						
						/***Configure Plus Commission***/
						//driver.findElement(By.id("tradingGroup_2")).click();
						List<WebElement> sub_trs=driver.findElements(By.cssSelector("div#tab_a_0 table#table>thead>tr"));
						
						if(sub_trs.size()<=2)
						{
							Assert.assertTrue(false, "Loading list error.");
						}
						
						for(j=3;j<=sub_trs.size();j++)
						{
							for (k=3;k<tradeGroups;k++)
							{
							//input the commission
							driver.findElement(By.xpath("//div[@id='tab_a_0']//tr["+j+"]//td["+k+"]//input[1]")).clear();
							driver.findElement(By.xpath("//div[@id='tab_a_0']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
							}
						}
							
						//Check select all
						WebElement tEle=driver.findElement(By.xpath("//input[@id='checkall4']"));
						((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						tEle.click();
						
						//Click Submit button
						//Moving the stroller to show all the table
						tEle=driver.findElement(By.id("CheckSubmit4"));
						((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						tEle.click();
						
						//Click confirm button
						driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
						
						//Assert success
						a = wait160.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
						System.out.println("Result is "+a);
						wait160.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						saAssert.assertEquals(a, "Success");
						
						By loadingImage = By.id("AjaxLoading");
	
						WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
	
						wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
						
						wait160.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SettingCommissionPlusPoint")));
						wait160.until(ExpectedConditions.elementToBeClickable(By.linkText("SettingCommissionPlusPoint")));
						/***Configure Plus Points***/
						driver.findElement(By.linkText("SettingCommissionPlusPoint")).click();
						Thread.sleep(3000);
	
						if(sub_trs.size()<=2)
						{
							Assert.assertTrue(false, "Loading list error.");
						}
						
						for(j=3;j<=sub_trs.size();j++)
						{
							for (k=3;k<tradeGroups;k++)
							{
								if (k!=cryptoPosition)//Crypto
								{
								//input the commission
								driver.findElement(By.xpath("//div[@id='tab_a_1']//tr["+j+"]//td["+k+"]//input[1]")).clear();
								driver.findElement(By.xpath("//div[@id='tab_a_1']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
								}
							}
							
						}
							
						//Click Submit button
						//Moving the stroller to show all the table
						tEle=driver.findElement(By.id("CheckSubmit0"));
						((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						wait30.until(ExpectedConditions.visibilityOf(tEle));
						driver.findElement(By.id("checkall0")).click();
						tEle.click();
						
						//Click confirm button
						driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
						
						//Assert success
						a = wait30.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
						System.out.println("Result is "+a);
						wait30.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						saAssert.assertEquals(a, "Success");
						
						/***Configure Outer Plus Commission***/
						wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
						wait160.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SettingOuterCommissionPlusCommission")));
						wait160.until(ExpectedConditions.elementToBeClickable(By.linkText("SettingCommissionPlusPoint")));
						driver.findElement(By.linkText("SettingOuterCommissionPlusCommission")).click();
						Thread.sleep(3000);
	
						if(sub_trs.size()<=2)
						{
							Assert.assertTrue(false, "Loading list error.");
						}
						
						for(j=3;j<=sub_trs.size();j++)
						{
							for (k=3;k<tradeGroups;k++)
							{
								//input the commission
								driver.findElement(By.xpath("//div[@id='tab_a_2']//tr["+j+"]//td["+k+"]//input[1]")).clear();
								driver.findElement(By.xpath("//div[@id='tab_a_2']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
							}
						}
							
						//Click Submit button
						tEle=driver.findElement(By.id("CheckSubmit1"));
						((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						wait15.until(ExpectedConditions.visibilityOf(tEle));
						driver.findElement(By.id("checkall1")).click();
						tEle.click();
						
						//Click confirm button
						driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
						
						//Assert success
						a = wait160.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
						System.out.println("Result is "+a);
						wait160.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						saAssert.assertEquals(a, "Success");
						
						/***Configure Outer Plus Points***/
						wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
						wait160.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SettingOuterCommissionPlusPoint")));
						wait160.until(ExpectedConditions.elementToBeClickable(By.linkText("SettingCommissionPlusPoint")));
						driver.findElement(By.linkText("SettingOuterCommissionPlusPoint")).click();
						Thread.sleep(3000);
						
						sub_trs=driver.findElements(By.cssSelector("div#tab_a_3 table#table>thead>tr"));
						
						if(sub_trs.size()<=2)
						{
							Assert.assertTrue(false, "Loading list error.");
						}
						
						for(j=3;j<=sub_trs.size();j++)
						{
							for (k=3;k<tradeGroups;k++)
							{
								
								if (k!=cryptoPosition)//Crypto
								{
									//input the commission
									driver.findElement(By.xpath("//div[@id='tab_a_3']//tr["+j+"]//td["+k+"]//input[1]")).clear();
									driver.findElement(By.xpath("//div[@id='tab_a_3']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
								}
								
							}
						}
							
						//Click Submit button
						tEle=driver.findElement(By.id("CheckSubmit3"));
						((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
						wait15.until(ExpectedConditions.visibilityOf(tEle));
						driver.findElement(By.id("checkall3")).click();
						tEle.click();
						
						//Click confirm button
						driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
						
						//Assert success
						a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
						System.out.println("Result is "+a);
						wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
						saAssert.assertEquals(a, "Success");
	
						//Click confirm button
						//driver.findElement(By.xpath("//button[@class='btn btn-danger pull-right popModalQuit']")).click();
						
						flag=1;
						wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
						break;
						}
					
				/*	driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();;
				}*/
							
			}
			saAssert.assertAll();
		}

	//Configure commission rules for rebate account 
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters({"TraderName","Brand"})
	public void configCommissionRulesFX(String userEmail, String Brand, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: configuring commission rules for specific user");
		int j,k,flag=0;		
		String a,check_id="";
			
		System.out.println("TEST STARTS: Start to configuring commission rules for specific user...");
		System.out.println("-------------------------------------------------------------");
		driver.navigate().refresh();
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Rebate Account")).click();
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		//Select all server
		AccountSearch searchBar = new AccountSearch(driver);
		searchBar.selectServerAll();
		//searchBar.selectServer("AU3");
		
		//Select search with userEmail = String
		searchBar.inputUserEmail(userEmail);
				
		//Click query button
		searchBar.clickSearch();
			
		//Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));

		//Create AccountResults instance
		AccountResult queryTrs = new AccountResult(driver);
		
		//Get search  results
		List<WebElement> trs=queryTrs.getAccoutQueryResult();
		
		if(trs.size()==0)
		{
			Assert.assertTrue(false, "Loading list  error.");
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println( "No Client is found.");
		}else {
			for(j=0;j<trs.size();j++)
			{	
				//Click the icon to open the client profile
				queryTrs.getCommissionRuleEle(trs.get(0)).click();
														
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-body")));
				
				//Click the IB group
				driver.findElement(By.xpath("//div[@class='panel-body']//tbody//tr[1]//td[1]")).click();
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SettingCommissionPlusCommission")));
				Thread.sleep(2000);
				
				/***Configure FX Plus Commission***/
				//driver.findElement(By.id("tradingGroup_2")).click();
				List<WebElement> sub_trs=driver.findElements(By.cssSelector("div#tab_a_0 table#table>thead>tr"));
				
				//Find out "FX" column and check on
				for(k=3;k<10;k++)
				{
					System.out.println("Setting rules for "+sub_trs.get(0).findElement(By.xpath("//td["+k+"]//span[@class='CommissionText']")).getText());
					if (sub_trs.get(0).findElement(By.xpath("//td["+k+"]//span[@class='CommissionText']")).getText().equals("FX"))
					{
						sub_trs.get(0).findElement(By.xpath("//td["+k+"]//input")).click();
						check_id = sub_trs.get(0).findElement(By.xpath("//td["+k+"]//input")).getAttribute("id");
						break;
					}
				}
				
				if(sub_trs.size()<=2)
				{
					Assert.assertTrue(false, "Loading list error.");
				}
				
				for(j=3;j<=sub_trs.size();j++)
				{
					//input the commission
					driver.findElement(By.xpath("//div[@id='tab_a_0']//tr["+j+"]//td["+k+"]//input[1]")).clear();
					driver.findElement(By.xpath("//div[@id='tab_a_0']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
				}
					
				//Click Submit button
				//Moving the stroller to show all the table
				WebElement tEle=driver.findElement(By.id("CheckSubmit4"));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
				wait15.until(ExpectedConditions.visibilityOf(tEle));
				tEle.click();
				//driver.findElement(By.id("CheckSubmit4")).click();
				
				//Click confirm button
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
				
				//Assert success
				a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
				System.out.println("Result is "+a);
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				saAssert.assertEquals(a, "Success");
				
				/***Configure FX Plus Points***/
				driver.findElement(By.linkText("SettingCommissionPlusPoint")).click();
				Thread.sleep(3000);
				
				//Find out "FX" column and check on
				driver.findElement(By.xpath("//div[@id='tab_a_1']//input[@id='"+check_id+"']")).click();
				
				if(sub_trs.size()<=2)
				{
					Assert.assertTrue(false, "Loading list error.");
				}
				
				for(j=3;j<=sub_trs.size();j++)
				{
					//input the commission
					driver.findElement(By.xpath("//div[@id='tab_a_1']//tr["+j+"]//td["+k+"]//input[1]")).clear();
					driver.findElement(By.xpath("//div[@id='tab_a_1']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
				}
					
				//Click Submit button
				//Moving the stroller to show all the table
				tEle=driver.findElement(By.id("CheckSubmit0"));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
				wait15.until(ExpectedConditions.visibilityOf(tEle));
				tEle.click();
				
				//Click confirm button
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
				
				//Assert success
				a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
				System.out.println("Result is "+a);
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				saAssert.assertEquals(a, "Success");
				
				/***Configure FX Outer Plus Commission***/
				driver.findElement(By.linkText("SettingOuterCommissionPlusCommission")).click();
				Thread.sleep(3000);
				
				//Find out "FX" column and check on
				driver.findElement(By.xpath("//div[@id='tab_a_2']//input[@id='"+check_id+"']")).click();
				
				if(sub_trs.size()<=2)
				{
					Assert.assertTrue(false, "Loading list error.");
				}
				
				for(j=3;j<=sub_trs.size();j++)
				{
					//input the commission
					driver.findElement(By.xpath("//div[@id='tab_a_2']//tr["+j+"]//td["+k+"]//input[1]")).clear();
					driver.findElement(By.xpath("//div[@id='tab_a_2']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
				}
					
				//Click Submit button
				tEle=driver.findElement(By.id("CheckSubmit1"));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
				wait15.until(ExpectedConditions.visibilityOf(tEle));
				tEle.click();
				
				//Click confirm button
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
				
				//Assert success
				a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
				System.out.println("Result is "+a);
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				saAssert.assertEquals(a, "Success");
				
				/***Configure FX Outer Plus Points***/
				driver.findElement(By.linkText("SettingOuterCommissionPlusPoint")).click();
				Thread.sleep(3000);
				
				//Find out "FX" column and check on
				driver.findElement(By.xpath("//div[@id='tab_a_3']//input[@id='"+check_id+"']")).click();
				
				if(sub_trs.size()<=2)
				{
					Assert.assertTrue(false, "Loading list error.");
				}
				
				for(j=3;j<=sub_trs.size();j++)
				{
					//input the commission
					driver.findElement(By.xpath("//div[@id='tab_a_3']//tr["+j+"]//td["+k+"]//input[1]")).clear();
					driver.findElement(By.xpath("//div[@id='tab_a_3']//tr["+j+"]//td["+k+"]//input[1]")).sendKeys("10");
				}
					
				//Click Submit button
				tEle=driver.findElement(By.id("CheckSubmit3"));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
				wait15.until(ExpectedConditions.visibilityOf(tEle));
				tEle.click();
				
				//Click confirm button
				driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
				
				//Assert success
				a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
				System.out.println("Result is "+a);
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				saAssert.assertEquals(a, "Success");

				//Click confirm button
				driver.findElement(By.xpath("//button[@class='btn btn-danger pull-right popModalQuit']")).click();
				
				flag=1;
				break;
				}
			
						
		}
		saAssert.assertAll();
	}
				
	//Not able to proceed due to bug
	//Making transfer between different accounts
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=false)
	@Parameters(value= {"TestEnv","Brand","TraderName"})
	public void differentUserAccountTransfer(String testEnv, String Brand, String TraderName, Method method) throws Exception
	{
		ExtentTestManager.startTest(method.getName(),"Description: transfer between different user accounts");
		int i=2,j,k;	
		Random r=new Random();
		BigDecimal moneyAmount=new BigDecimal("0.00");
		String name1 = "", name2="", balance="", toAccount="",fromAccount=""; //Used as temporary names
		String[] keyWords=new String[]{Utils.webUserPrefix,Utils.addUserPrefix,Utils.ibUserPrefix};
			
		driver.navigate().refresh();
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Rebate Account")).click();
		
		System.out.println("TEST STARTS: Start to making transfer between different user accounts...");
		System.out.println("-------------------------------------------------------------");
							
		Thread.sleep(3000);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		//Select all server
		AccountSearch searchBar = new AccountSearch(driver);
		searchBar.selectServerAll();
		//searchBar.selectServer("AU3");
		
		//Select search with userName = String
		searchBar.inputUserName(keyWords[i]);
		
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
		if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No results for keywords " + keyWords[i]);
		}else
		{				
			//Find the from account 
			for(j=1;j<trs.size();j++)
			{
				System.out.println("Entering for loop");
				wait15.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//tr["+j+"]//td[13]"))));
				balance=driver.findElement(By.xpath("//tr["+j+"]//td[13]")).getText();	
				name1 = queryTrs.getUserEle(trs.get(j-1)).getText();
				if( !balance.equals("0") && Utils.isTestIB(name1))
				{
					System.out.println("j = "+j);
					break;
				}else {
					if(j==trs.size())
						Assert.assertTrue(false, "All the accounts are 0 balance!");
				}
			}
			
			fromAccount = queryTrs.getAccEle(trs.get(j-1),false).getText();
			
			//Find the to account
			for(k=1;k<trs.size();k++)
			{
				name2 = queryTrs.getUserEle(trs.get(k-1)).getText();
				toAccount = queryTrs.getAccEle(trs.get(k-1),false).getText();
				if( Utils.isTestIB(name2) && !toAccount.equals(fromAccount))
				{
					System.out.println("k = "+k);
					break;
				}else {
					Assert.assertTrue(false, "All other accounts are not testcrm account!");
				}
			}
			
			
			
			
			System.out.println("fromAccount is "+fromAccount+" toAccount is "+toAccount);
			
			moneyAmount = new BigDecimal(queryTrs.getBalanceEle(trs.get(j-1),false).getText());
				
			if((Utils.isWebUser(name1)|| Utils.isAddUser(name1)|| Utils.isJoint(name1)|| Utils.isTestIB(name1))&&(Utils.isWebUser(name2)|| Utils.isAddUser(name2)|| Utils.isJoint(name2)|| Utils.isTestIB(name2)))
			{
				Thread.sleep(3000);
			
				WebElement tEle=queryTrs.getDiffTransferEle(trs.get(j-1),false);
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

			}else {
				
				System.out.println("No qualified account! ");
			}
	    }
	}

	//deposit and withdraw from the rebate account
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand", "TraderName", "AdminURL"})
	public void adjustDeposit(String testEnv, String Brand, String TraderName, String AdminURL) throws Exception
	{
	
		int j;
		Select t;
		String name, balance, balance2, account; //Used as temporary names
		String keyWords=Utils.ibUserPrefix;		
	
		//Navigate to admin in case the test cases running before this one fail.
		driver.get(AdminURL);
		
		//Navigate to Client menu
		driver.findElement(By.linkText("Account Management")).click();
		driver.findElement(By.linkText("Rebate Account")).click();
											
		Thread.sleep(3000);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
						
		//Select all server
		AccountSearch searchBar = new AccountSearch(driver);
		searchBar.selectServerAll();
		//searchBar.selectServer("AU3");
		
		//Select search with userName = String
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
		if(trs.size()==0 || trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			/* System.out.println("No results for keywords " + keyWords + " on Server " + serverList.getFirstSelectedOption().getText()); */
			System.out.println("No results for keywords " + keyWords + " on all Servers");
		}else
		{				
			
			//If search returns result, get each row and check whether it is a test user
			for(j=0;j<trs.size();j++)
			{
				
				name=queryTrs.getUserEle(trs.get(j)).getText();
									
				//if the user is a test user, do cash adjustment and break the loop
				if(Utils.isWebUser(name)|| Utils.isAddUser(name)|| Utils.isJoint(name)|| Utils.isTestIB(name))
				{
											
					account = queryTrs.getAccEle(trs.get(j),false).getText();
					balance = queryTrs.getBalanceEle(trs.get(j),false).getText();
					
					System.out.println("The balance of account "+account+" for user " + name + ": " + balance);
					
					Thread.sleep(1000);
					//Scroll to the right until the Operation column is visible
									
					WebElement tEle=trs.get(j).findElement(By.cssSelector("a[title='Account Compensation']"));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tEle);
					wait15.until(ExpectedConditions.visibilityOf(tEle));
					
					//Open the transfer panel
					tEle.click();
					wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
					
					//Select deposit
					t = new Select(driver.findElement(By.id("type")));
					t.selectByIndex(1);
	
					Thread.sleep(1000);		
	
					//Input the transfer amount
					driver.findElement(By.id("money")).sendKeys("800.99");
					
					//Select Notes Content
					t = new Select(driver.findElement(By.id("remark")));
					t.selectByIndex(1);
					
					//Click confirm button
					driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
	
					//Print message
					//Assert success
					String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
					wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
					Thread.sleep(1000);	
					
					//Check the balance
					trs=queryTrs.getAccoutQueryResult();
					balance2=queryTrs.getBalanceEle(trs.get(j),false).getText();
					System.out.println("Result is: "+a+" Making adjustment of "+account+". Balance changed from "+balance+" to "+balance2);
					
				}
			}
	
			
		}
			  	
	}

	//resetPWD can change all rebate account pwds that belongs to "TraderName" and resides on "serverName"
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
		driver.findElement(By.linkText("Rebate Account")).click();
		
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
				if(Utils.isTestUser(userName)|| Utils.isTestIB(userName))
				{
					
					account=queryTrs.getAccEle(trs.get(j),false).getText();
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


}	

					

	

		



