package vantagecrm;


import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import clientBase.liveAcctHome;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * This class is to test Client Portal Home Page functions listed as below:
 * 1) Change Leverage if  there is an available account
 * 2) Active an archived account when there is an available Inactive account
 */

public class CPHomepage {

	WebDriver driver;

	
	WebDriverWait wait50;
	
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	int dateIndex=0;
	
	SoftAssert saAssert=new SoftAssert();
	
	liveAcctHome acctHomeCls;
	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless)
	{
		
    	WebDriverManager.chromedriver().setup();
    	 driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
		driver.manage().window().maximize();		 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		{
			waitIndex=2;
	    }
		  
	  	wait50=new WebDriverWait(driver, Duration.ofSeconds(50));
	
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
//		ExtentTestManager.startTest(method.getName(),"Description: Login to Client Portal");
		
		driver.get(TraderURL);
 		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")));
		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("DEPOSIT FUNDS")));
		
		acctHomeCls = new liveAcctHome(driver,Brand);
	}
	
	
	@Test
	@Parameters(value = {"Brand"})
	void changeMT4Leverage(String Brand) throws Exception
	{
		changePlatformLeverage("MT4", Brand);
	}
	
	@Test
	@Parameters(value = {"Brand"})
	void changeMT5Leverage(String Brand) throws Exception
	{
		changePlatformLeverage("MT5", Brand);
	}
	

	void changePlatformLeverage(String platform, String Brand) throws Exception
	{
		
		int i=0;
		WebElement imgLink=null;
		List<WebElement> trs;
		String imgSrc;
		//Get Platform account list
		
		trs = acctHomeCls.getMTActList(platform, "Active");

		//When accounts are available
		if(trs.size()>0)
		{
			System.out.println(platform + " Accounts Number: "+trs.size());
			switch(Brand.toLowerCase())
			{
				case "fsa":
				case "svg":
					//Look for an account which doesn't have submitted leverage change request. Only PUG has this logic as leverage audit is not automatically approved.
					for(i=0; i<trs.size(); i++)
					{
						imgSrc = trs.get(i).findElement(By.tagName("img")).getAttribute("src");
						if(imgSrc.contains("CYII="))
						{
							break;
						}
					}
					
					//failed to find an account, quit immediately. 
					if(i>=trs.size())
					{
						System.out.println("No Available Accounts for Leverage Change.");
						Assert.assertTrue(false, "No Available Accounts for Leverage Change.");
					}else
					{
						imgLink=acctHomeCls.getLeverageIcon(trs.get(i));
					}
					
					break;
				
					default:
						imgLink=acctHomeCls.getLeverageIcon(trs.get(0));
			}
			
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView;", imgLink);
								
			//Submit a leverage change request
			funcChaLeverage(imgLink, Brand);
			
			//Check whether icon is changed and message can be displayed.
			/* funcPendLeverage(trs.get(i).findElement(By.cssSelector("td:nth-of-type(5) a img"))); */
				
		}else
		{
			Assert.assertFalse(true, "There are some accounts but none of them is available for leverage change.");
		}
		
		
		
	}
	
	
	
	//WebElement e is an img element
	void funcChaLeverage(WebElement e, String Brand) throws Exception
	{
		int i=0;
		Random r=new Random();
		List<WebElement> levChoice;
		String messageExp="Your request has been submitted and will be processed by our support team shortly.";
		String messageAct;
		
		if(e!=null)
		{
			Thread.sleep(500);
			e.click();
			wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form.el-form.el-form--label-top label.el-form-item__label:nth-of-type(1)")));
			
			//Output the current leverage			
			
			//Click leverage control
			driver.findElement(By.xpath("//input[@placeholder='Select']")).click();
			Thread.sleep(1000);
			levChoice = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
			
			Assert.assertTrue(levChoice.size()>1, "No available leverage choice displayed.");
			
			i=r.nextInt((levChoice.size()-1)+1);
			
			//If the option is not displayed, scroll the scroll bar
			if(i>=8)
			{
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", levChoice.get(i));
			}
			
			//Click the choice to select it
			levChoice.get(i).click();
			
			//Accept license
			driver.findElement(By.cssSelector("span.el-checkbox__inner")).click();
			
			//Click CHANGE LEVERAGE button
			Thread.sleep(500);
			driver.findElement(By.xpath("//span[contains(translate(text(),'Change leverage', 'CHANGE LEVERAGE'), 'CHANGE LEVERAGE')]")).click();
			Thread.sleep(1500);
			
			switch(Brand.toLowerCase())
			{
				case "vt":
					wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.el-dialog.el-dialog--center")));
					messageAct=driver.findElement(By.cssSelector("div.el-dialog.el-dialog--center div.dialog_body>p")).getText();
					
					
					break;
					
				case "fsa":
				case "svg":
					wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.el-dialog__body div.dialog_body>p")));
					messageAct=driver.findElement(By.cssSelector("div.el-dialog__body div.dialog_body>p")).getText();					
	
					break;
					
					default:
						wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.changeLeverageConfirm_dialog")));
						messageAct=driver.findElement(By.cssSelector("div.changeLeverageConfirm_dialog div.dialog_body p")).getText();
		
			}
			
			//close the dialog
			driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
			Assert.assertEquals(messageAct, messageExp);
	
		}else
		{
			System.out.println("WebElement e is null.");
		}
	}
	
	//Yanni: comment out this function as AU/VT already have leverage request approved automatically. PUG request still needs audit.
	/*
	 * //WebElement e is an img element
	 * void funcPendLeverage(WebElement e) throws Exception
	 * {
	 * 
	 * String messageExp="You have already requested";
	 * String messageAct;
	 * 
	 * if(e!=null)
	 * {
	 * Thread.sleep(500);
	 * Assert.assertTrue(e.getAttribute("src").contains("CYII="), "ICON is wrong.");
	 * e.click();
	 * wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.changeLeverageAlready_dialog div.el-dialog__body p")));
	 * 
	 * //Get the dialog message
	 * messageAct=driver.findElement(By.cssSelector("div.changeLeverageAlready_dialog div.el-dialog__body p")).getText();
	 * 
	 * //Close the dialog
	 * driver.findElement(By.cssSelector("div.changeLeverageAlready_dialog div.el-dialog__header img")).click();
	 * 
	 * Assert.assertTrue(messageAct.startsWith(messageExp));
	 * 
	 * 
	 * }else
	 * {
	 * System.out.println("WebElement e is null.");
	 * }
	 
	}*/
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		
		//Utils.funcLogOutTrader(driver);
		//Close all browsers
		driver.quit();
	}
}
