package vantagecrm;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
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

import io.github.bonigarcia.wdm.WebDriverManager;

public class UpdateLead {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String userName; //Client Name
	WebDriverWait wait15;
	String newPwd="123Qwe";

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		 String browserName= "chrome";
		  System.out.println("Browser is: " + browserName);
		  System.out.println("Thread Id is: " + Thread.currentThread().getId());
		  
		  switch (browserName)
		  {
		  case "chrome":
		  
			  WebDriverManager.chromedriver().setup();
			  ChromeOptions options = new ChromeOptions();
			  options.addArguments("--no-sandbox");
			  options.setExperimentalOption("useAutomationExtension", false);
			  
			  if(Boolean.valueOf(headless)) {
					options.addArguments("window-size=1920,1080");
					options.addArguments("headless");
				}
			  driver = new ChromeDriver(options);
			  break;
		  
		  case "firefox":
			  WebDriverManager.firefoxdriver().setup();
			  driver = new FirefoxDriver();
			  break;
		  case "edge":
			  WebDriverManager.edgedriver().setup();
			  driver = new EdgeDriver();
			  break;
		  case "safari":
			  driver = new SafariDriver();
			  break;
		  }
		 
		
			/*
			 * ChromeOptions options=new ChromeOptions();
			 * options.setAcceptInsecureCerts(true);
			 * 
			 * System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
			 * driver = new ChromeDriver(options);
			 */	
    	  
    	  context.setAttribute("driver", driver);          //Added by Yanni on 5/15/2019
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait15=new WebDriverWait(driver, Duration.ofSeconds(15));
	}
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		
		Utils.funcLogOutAdmin(driver);
		//Close all browsers
		driver.quit();
	}

	//Login Admin with credentials

	@Parameters({"AdminURL","AdminName","AdminPass","Brand"})
	@Test(priority=0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand) throws Exception
	{
	
		//Login AU admin
		driver.get(AdminURL);	
		Utils.funcLogInAdmin(driver, AdminName,AdminPass, Brand);
	}

	/*
	 * Search username starting with Lead and return the results.
	 *  
	*/
	List<WebElement> funcChooseLead(String Brand, String TestEnv) throws Exception
	{
		List<WebElement> trs=null;
		String keyword=Utils.webUserPrefix;
			
		//Navigate to Client menu
		wait15.until(ExpectedConditions.elementToBeClickable(By.linkText("Client Management")));
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Leads")).click();
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		//Select "Search by real Name"
		
		//search type has been changed
		//Select t = new Select(driver.findElement(By.id("search_type")));
		
		//t.selectByValue("real_name");
				
		//Input keyword
		//driver.findElement(By.id("userQuery")).clear();
		//driver.findElement(By.id("userQuery")).sendKeys(keyword);
		
		driver.findElement(By.xpath("//table[@id='table']/thead/tr/th[5]//input")).sendKeys(keyword);
		
		//Click Search button
		driver.findElement(By.id("query")).click();
		Thread.sleep(500);
		
		//Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		trs=driver.findElements(By.cssSelector("table#table>tbody>tr"));
		return trs;
	}
	
	
	

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test
	@Parameters(value={"Brand","TestEnv"})
	public void updateLeadGeneral(String Brand, String TestEnv) throws Exception
	{
		List<WebElement> trs = funcChooseLead(Brand, TestEnv);
		List<WebElement> ownerList;
		int j;
		String oldDOB, oldValue, newValue;
		String pathValue="";
		Select t;
		Date dt;
			
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Leads (with name starting with Lead) is found.");
	
		}
		
				
		//Modify the 1st lead record
		trs.get(0).findElement(By.cssSelector("td:nth-of-type(5)>a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);	
		
		//Change Account Owner to testcrmib***		
		
		t=new Select(driver.findElement(By.id("accountOwner")));
		
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Account Owner is: "+oldValue);
		
		pathValue = ".//select[@id='accountOwner']/option[contains(text(),'" + Utils.ibUserPrefix + "')]";
		ownerList = driver.findElements(By.xpath(pathValue));
		
		if(ownerList.size()>1)
		{
			for(j=0; j<ownerList.size();j++)
			{
				newValue=ownerList.get(j).getText();
				if(!newValue.equals(oldValue))
				{
					ownerList.get(j).click();
					System.out.println("New owner is: " + newValue);
					break;
				}
			}
		
		}else
		{
			System.out.println("No qualified test account owner. Keep the old owner unchanged.");
		}
				
				
		//Change Country
		
		t=new Select(driver.findElement(By.id("country")));
		
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Country is: "+oldValue);
		
		//j=0: select; j=1: Afghanistan (irregular country)
		for(j=2; j<t.getOptions().size();j++)
		{
			
			String selected=t.getOptions().get(j).getText();
			
			if(!selected.equalsIgnoreCase(oldValue) )
			{
				t.selectByIndex(j);
				break;
			}
				
		}
		
		if(j>=t.getOptions().size())
		{
			System.out.println("No Other Country can be selected. Keep the old one unchanged.");
		}else
		{
			System.out.println("New country is: "+ t.getFirstSelectedOption().getText());
		}
		
		//Change  the DOB
		oldDOB=driver.findElement(By.id("birthday")).getAttribute("value");
		System.out.println("Old Birthday is: "+oldDOB);
		
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	

		Calendar calInst=Calendar.getInstance();
		
		//When old Date of Birth is null, use TODAY as i base. Otherwise, use oldDOB as base
		if(oldDOB.length()!=0)
		{
			dt=sdf.parse(oldDOB);
			calInst.setTime(dt);
			calInst.add(Calendar.DATE, -7);
		}else
		{
			calInst.add(Calendar.YEAR, -20);
		}

		dt=calInst.getTime();
		
		String queryStr="jQuery('#birthday').val('" + sdf.format(dt).toString()+"');";
		System.out.println(queryStr);
		((JavascriptExecutor)driver).executeScript(queryStr);
		driver.findElement(By.id("birthday")).sendKeys(sdf.format(dt));
		System.out.println("New Birthday is: "+driver.findElement(By.id("birthday")).getAttribute("value"));

		//Change Title
		t=new Select(driver.findElement(By.id("title")));
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Title is: " + oldValue);
		
		for(j=1; j<t.getOptions().size();j++)
		{
			
			if(!t.getOptions().get(j).getText().equalsIgnoreCase(oldValue))
			{
				t.selectByIndex(j);
				break;
			}
				
		}
		
		if(j>=t.getOptions().size())
		{
			System.out.println("No Title is available. Keep the old Title unchanged.");
		}else
		{
			System.out.println("New Title is: "+ t.getFirstSelectedOption().getText());
		}
		
		//Submit
		driver.findElement(By.xpath(".//button[text()='Submit']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				
	}
}
