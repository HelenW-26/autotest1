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

public class IPAccountCompare {

	WebDriver driver;
	String IpURL="http://ib.vantagefx.com/?login_id=";
	String userID="69209";
	
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
		
		//Thread.sleep(2000);
		Utils.waitUntilLoaded(driver);
		//wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));
		
		//Click IB ACCOUNTS on the left menu
		driver.findElement(By.cssSelector("div:nth-child(5) > .el-menu-item")).click();
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.el-table__body")));
		
	
	}
	
		
	@Test(dependsOnMethods="IPLogIn")
	@Parameters (value= {"Brand"})
	void CompareAccounts(String Brand) throws Exception
	{
		String[][] homeData;
		String [][] accountData;
		String cssSelector;
		int accountNumber = 6;
		
		driver.switchTo().window(winHandles.get(0));
		
		//Scroll to Recently Opened accounts table
		if (!Brand.equalsIgnoreCase("vt"))
		{
			cssSelector="div.tables.clearfix>div.table_list:nth-of-type(2)";
		} else {
			cssSelector="div.tables.clearfix>div.tables_top>div.table_list:nth-of-type(2)";
		}
		
		WebElement e= driver.findElement(By.cssSelector(cssSelector));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", e);
		Thread.sleep(500);
		 
		List<WebElement> rows=driver.findElements(By.cssSelector(cssSelector+" table.el-table__body>tbody>tr"));
		
		if(rows.size()>0 )
		{
			
			//If the rebate account has less than 6 trading accounts
			if(rows.size()<accountNumber)
			{
				accountNumber=rows.size();
			}
			
			homeData = new String[accountNumber][3];
			
			for(int i = 0; i<accountNumber;i++)
			{
				//Account
				homeData[i][0]=rows.get(i).findElement(By.cssSelector("td:nth-of-type(1)>div")).getText();
				
				//name
				homeData[i][1]=rows.get(i).findElement(By.cssSelector("td:nth-of-type(2)>div")).getText();
				
				//Balance
				homeData[i][2]=rows.get(i).findElement(By.cssSelector("td:nth-of-type(3)>div")).getText();
						
				System.out.println("Home Page Row" + i +": Account Number "+homeData[i][0] + " Name " + homeData[i][1] + " Balance " + homeData[i][2]) ;	
			}
			
		}else
		{
			homeData = null;
			
			System.out.println("Home Page has no recently opened accounts.");
		}
		
		accountData=getIBAccount(winHandles.get(1));

		Assert.assertTrue(compareStringArray(homeData,accountData));
		
	}
	
	String[][] getIBAccount(String winHandle) throws Exception
	{
		String[][] data;
		int accountNumber=6;
		
		
		//Switch to IB Accounts tab
		driver.switchTo().window(winHandle);
	
		//click Date to sort accounts descendant. By default it is ascendant. Click once still ascendant. Click twice to get descendant.
		//Thread.sleep(2000);
		Utils.waitUntilLoaded(driver);
		wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("table.el-table__header>thead.has-gutter>tr:nth-of-type(1)>th:nth-of-type(1) i.sort-caret.descending"))).click();
	
		//Get all accounts
		List<WebElement> rows=driver.findElements(By.cssSelector("table.el-table__body>tbody>tr"));
		
		//If the user has less than 6 accounts, get all accounts information. Otherwise, only get the top 6 accounts
		if (rows.size()<6)
		{
			accountNumber=rows.size();
		}
		
		//Initialize data only if the IB rebate account has trading accounts. Otherwise, set data to null.
		if(accountNumber>0)
		{
		
			data=new String[accountNumber][3];
			
			for(int i=0;i<accountNumber;i++)
			{
				//Account Number
				data[i][0]=rows.get(i).findElement(By.cssSelector("td:nth-of-type(2)")).getText().trim();
				data[i][1]=rows.get(i).findElement(By.cssSelector("td:nth-of-type(3)")).getText().trim();
				data[i][2]=rows.get(i).findElement(By.cssSelector("td:nth-last-of-type(1)")).getText().trim();	
				
				System.out.println("IB Accounts Row" + i +": Account Number "+data[i][0] + " Name " + data[i][1] + " Balance " + data[i][2]) ;	
			}
		}else
		{
			data=null;
			System.out.println("IB Accounts page has no accounts");
		}
		
		return data;
	}
	
	boolean compareStringArray(String[][] a, String[][] b )
	{
		
		if(a==b)
		{
			return true;
		}
		
		if(a==null && b!=null)
		{
			System.out.println(a+" is null but "+b+" is not null.");
			return false;
		}
		
		if(a!=null && b==null)
		{
			System.out.println(a+" is not null but "+b+" is null.");
			return false;
		}
		
		if(a.length!=b.length)
		{
			System.out.println(a+ " doesn't have the same length with "+b);
			return false;
		}
		
		for(int i=0;i<a.length;i++)
		{
			for(int j=0;j<a[0].length;j++)
			{
				if(!a[i][j].equals(b[i][j]))
				{
					System.out.println(a[i][j]+" does not equal with "+b[i][j]);
					return false;
				}
			}
		}
		
		return true;
		
	}
	@AfterClass(alwaysRun=true)
	void ExitBrowser() throws Exception
	{
		
		Utils.funcLogOutIP(driver);
		//Close all browsers
		driver.quit();
	}
}
