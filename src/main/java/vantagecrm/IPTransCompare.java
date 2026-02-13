package vantagecrm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
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


/*
 * This class is to test the displayed Rebate and Total Value are consistent between Home Page & Rebate Report
 */

public class IPTransCompare {

	WebDriver driver;
	String IpURL="http://ib.vantagefx.com/?login_id=";
	String userID="69127";
	
/*	String IpURL="http://ib-new.vantagefx.com/?login_id=";
	String userID="23203";*/

	WebDriverWait wait50;
	ArrayList<String> winHandles;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	int dateIndex=0;
	
	SoftAssert saAssert=new SoftAssert();
	

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  context.setAttribute("driver", driver);
		  
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
	  	wait50=new WebDriverWait(driver, Duration.ofSeconds(50));
	
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderName", "TraderPass", "Brand", "TraderURL"})
	void IPLogIn(String TraderName, String TraderPass, String Brand, String TraderURL) throws Exception	
	{
 
 	    String ibURL;
		
		//Login AU IB Portal and keep Home Page
		driver.get(TraderURL);
		Utils.funcLogInIP(driver, TraderName, TraderPass, Brand);
		
		ibURL=driver.getCurrentUrl();
				
		//Open another tab
		((JavascriptExecutor)driver).executeScript("window.open()");
		
		winHandles=new ArrayList<String>(driver.getWindowHandles());
		
		
		//Login AU IB Portal and go to IB Accounts
		driver.switchTo().window(winHandles.get(1));
		driver.get(ibURL);
		
		/*//Switch language to English
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.login input.el-input__inner")).click();
		Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper").get(0).click();*/
		
		Thread.sleep(2000);	
		//wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));
		
		Thread.sleep(1000);
		//driver.findElement(By.xpath(".//span[text()='IB REPORT']")).click();
		driver.findElement(By.xpath(".//span[contains(translate(.,'IB report','IB REPORT'),'IB REPORT')]")).click();
		Utils.waitUntilLoaded(driver);
		
		//Thread.sleep(2000);

	}
	
		
	@Test(dependsOnMethods="IPLogIn")
	@Parameters(value="Brand")
	void CompareTransaction(String Brand) throws Exception
	{
		int i=0;
		String IBNetFund, IBDeposit, IBWithdraw;
		String[] IBReport=new String[] {};
		String cssSel="";
		
		driver.switchTo().window(winHandles.get(0));

		//Get all date shortcut options
		Thread.sleep(2000);	
		
		driver.findElement(By.cssSelector("div.calendar div.calendar_shortcut div.el-input.el-input--suffix input.el-input__inner")).click();
		Thread.sleep(500);
		List<WebElement> shortcutDates= driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper ul.el-scrollbar__view.el-select-dropdown__list>li>span"));
		//System.out.println(shortcutDates.size());
		
		List<WebElement> dataElements;
		

		for(i=0;i<shortcutDates.size();i++)  
		{
		
			driver.switchTo().window(winHandles.get(0));
			driver.navigate().refresh();
			
			//Wait until all summary data is loaded
			Thread.sleep(2000);	
						
			//Wait until the date shortcut is clickable
			wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.calendar div.calendar_shortcut div.el-input.el-input--suffix input.el-input__inner")));
			
			//Click date shortcut
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("div.calendar div.calendar_shortcut div.el-input.el-input--suffix input.el-input__inner")).click();

			Thread.sleep(5000);
			shortcutDates= driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper ul.el-scrollbar__view.el-select-dropdown__list>li>span"));
						
			
			if(i>6)
			{
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", shortcutDates.get(i));
				Thread.sleep(5000); 

			}
			
			//Choose the first date range shortcut
			System.out.println("Date option is "+shortcutDates.get(i).getText());
			shortcutDates.get(i).click();
			
			//Click Update button
			//driver.findElement(By.cssSelector("div.calendar div.right button.el-button.el-button--default")).click();
			if(Brand.equalsIgnoreCase("vt")) {
				driver.findElement(By.xpath("//span[text()='Update']")).click();
			} else {
				driver.findElement(By.xpath("//button[@class='el-button el-button--default']//span[text()='UPDATE']")).click();
			}
			
			//Allow sometime for search
			Thread.sleep(2000);	
			//wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));
			//Thread.sleep(2000);
			
			//Get Netfunding, Deposits and Withdraw
			switch(Brand)
			{
				
				case "vt":
					cssSel="div.calendar_content_bottom ul.clearfix>li>div>div p";
					break;
					
				default:
					cssSel="div.calendar_content ul.clearfix>li>div.bottom p";
					
			}
			
			dataElements=driver.findElements(By.cssSelector(cssSel));
			
			//System.out.println("The number of function blocks are: "+dataElements.size());
			
			IBNetFund=dataElements.get(2).getText().trim();
			IBDeposit=dataElements.get(3).getText().trim(); //remove thousand separator
			IBWithdraw=dataElements.get(4).getText().trim();
			
			System.out.println("Home Page Net Funding is "+IBNetFund + " , Deposit is "+IBDeposit + "  and Withdraw is " + IBWithdraw);
			
			IBReport=getIBReportData(driver,winHandles.get(1),i,Brand);
			
		
			saAssert.assertTrue(IBNetFund.equals(IBReport[0]));
			saAssert.assertTrue(IBDeposit.equals(IBReport[1]));
			saAssert.assertTrue(IBWithdraw.equals(IBReport[2]));
		}
		
		saAssert.assertAll();
	}
	
	String[] getIBReportData(WebDriver driver,String winHandle, int shortcutIndex,String Brand) throws Exception
	{
		String[] data=new String[3];
		List<WebElement> dataElements=null;

	
		//Switch to rebate report page
		driver.switchTo().window(winHandle);
		driver.navigate().refresh();
		
		Utils.waitUntilLoaded(driver);
		
		
		//Click the date shortcut box to show the dropdown list
		if (Brand.equalsIgnoreCase("vt")) {
			driver.findElement(By.xpath("//div[@class='calendar_shortcut']/form/div/div/div/div/div/input")).click();
		} else {
			driver.findElement(By.xpath("//div[@class='calendar_shortcut']/div/div/input")).click();
		}
		Thread.sleep(500);
		
		
		//Get all date shortcut options
		List<WebElement> shortcutDates= driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper ul.el-scrollbar__view.el-select-dropdown__list>li>span"));
		//System.out.println("getRebateReportData:"+shortcutDates.size());

		//When the shortcut date is not in the view, need to scroll down to show it.
		if(shortcutIndex>6)
		{
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", shortcutDates.get(shortcutIndex));
			Thread.sleep(500); 

		}
		
		
		//Choose the first date range shortcut
		shortcutDates.get(shortcutIndex).click();
		
		//Click Update button
		if(Brand.equalsIgnoreCase("vt")) {
			driver.findElement(By.xpath("//div[@class='query_content_main_right']/button")).click();
		} else {
			driver.findElement(By.cssSelector("div.query div.right_box>button.el-button.el-button--primary")).click();
		}
		//Allow sometime for search
		Utils.waitUntilLoaded(driver);		
			
		//Get all data
		if(Brand.equalsIgnoreCase("vt")) {
			dataElements=driver.findElements(By.cssSelector("div.calendar_box>ul>li>div.left>div>p"));
		} else {
			dataElements=driver.findElements(By.cssSelector("div.query>div.calendar_box>ul>li>div.bottom>p"));
		}
		//System.out.println("Get rebate report: The number of function blocks are: "+dataElements.size());
		
		//Get Net Funding
		data[0]=dataElements.get(0).getText().trim();
		
		//Get Deposits
		data[1]=dataElements.get(1).getText().trim();
		
		//Get Withdraw
		data[2]=dataElements.get(2).getText().trim();
		
		System.out.println("IB Report NetFunding is: "+data[0] + ", Deposit is: "+data[1] + " and Withdraw is "+data[2]);
		System.out.println();
		
		return data;
	}
	@AfterClass(alwaysRun=true)
	void ExitBrowser() throws Exception
	{
		
		Utils.funcLogOutIP(driver);
		//Close all browsers
		driver.quit();
	}
}
