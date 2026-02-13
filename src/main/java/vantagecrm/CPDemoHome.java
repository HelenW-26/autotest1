package vantagecrm;


import java.time.Duration;
import java.util.List;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import clientBase.cpHeader;
import clientBase.navigationBar;

/*
 * This class is to test Client Portal Download page
 * 1) Verify file can be downloaded and the file name is the expected one
 * 2) Verify Android/iPhone links are correct
 * 3) Verify .com is used for CIMA and .com.au is used for ASIC
 */

public class CPDemoHome {

	WebDriver driver;
	WebDriverWait wait50;
	CPRegister regIns = new CPRegister();
	Random r = new Random();
	
	cpHeader headerCls;
	navigationBar navCls;

	//Launch driver
	//@BeforeClass(alwaysRun=true)
	@Test
	@Parameters(value= {"TestEnv", "Brand", "WebSiteURL", "RegisterURL", "IBpromotion","headless"})
	public void InitDemo(String TestEnv, String Brand, String WebSiteURL, String RegisterURL, String IBpromotion,@Optional("False") String headless, ITestContext context) throws Exception
	{
		
		regIns.LaunchBrowser(TestEnv,headless, context);
		driver=(WebDriver) context.getAttribute("driver");	
		
	
		driver.manage().window().maximize();		 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		  
		wait50=new WebDriverWait(driver, Duration.ofSeconds(50));
		
		regIns.RegisterDemoMT4(TestEnv, Brand, WebSiteURL, RegisterURL, IBpromotion);
		
		headerCls = new cpHeader(driver, Brand);
		navCls = new navigationBar(driver, Brand);

	}
	
	@Test
	@Parameters(value= {"Brand"})
	void AddMT4Demo(String Brand) throws Exception
	{
		
		String platform="MT4";
		funcAddDemo(platform, Brand);
		
	}
	
	@Test
	@Parameters(value= {"Brand"})
	void AddMT5Demo(String Brand) throws Exception
	{
		
		String platform="MT5";
		funcAddDemo(platform, Brand);
		
	}
	
	public void funcGo2DemoHome(String Brand) throws Exception
	
	{

		if( Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg") ) {
		
			Utils.waitUntilLoaded(driver);
			headerCls.getLangSwitchMenu().click();
			wait50.until(ExpectedConditions.visibilityOf(headerCls.getLanguage("English"))).click();
	
			Thread.sleep(1000);
		}
			
		//If current page is not demo home, than Click Account->Demo Account to enter demo home page
	    if(!driver.getCurrentUrl().contains("homeDemo"))
	    {
	    	//Click Account Management
	    	navCls.getAccountEle().click();
		    Thread.sleep(1000);
		    navCls.getDemoAccountEle().click();
	    }
	}
	
	public void funcGo2LiveHome(String Brand) throws Exception
	
	{

		
		if( Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg") ) 
		{
			headerCls.getLangSwitchMenu().click();
			wait50.until(ExpectedConditions.visibilityOf(headerCls.getLanguage("English"))).click();
			/*
			 * driver.findElement(By.id("lang")).click();
			 * wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'English')]"))).click();
			 */
			Thread.sleep(1000);
		}
			
		//Click Account Management
		driver.navigate().to(Utils.ParseInputURL(driver.getCurrentUrl()));		
		
		//If current page is not DEMO HOME PAGE, don't need to click ACCOUNT。
		if(!driver.getCurrentUrl().contains("homeDemo"))
		{
			//Click Account menu in the navigation bar
			navCls.getAccountEle().click();
		}
		
		//Click Live Account menu in the navigation bar
		navCls.getLiveAccountEle().click();			

	}
	
	public void funcAddDemo(String platform, String Brand) throws Exception
	{
		
		String actTypeSel=""; 
		String curTypeSel="";
		String tempStr = "";
		//Go to Demo Home
		funcGo2DemoHome(Brand);
		Utils.waitUntilLoaded(driver);
		
		
		//Click OpenDemoAccount button
		switch(platform.toLowerCase())
		{
			case "mt4": 

				if(Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
				{
					//PUG has different design: only one OPEN DEMO ACCOUNT button for both MT4 & MT5. Therefore, user has to choose platform
					driver.findElement(By.xpath(".//a[contains(text(), 'open demo account')]")).click();
					
					Thread.sleep(1000);
					//Select Platform
					driver.findElements(By.xpath("//div[@class='box box_platform']//li")).get(0).click();
				}else
				{
					driver.findElement(By.xpath(".//a[contains(@href, 'openDemoAccount?p=4')]")).click();
				}
				break;
				
			case "mt5":
	
				
				if(Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
				{
					driver.findElement(By.xpath(".//a[contains(text(), 'open demo account')]")).click();
					
					Thread.sleep(1000);
					//Select Platform
					driver.findElements(By.xpath("//div[@class='box box_platform']//li")).get(1).click();
				}else
				{
					driver.findElement(By.xpath(".//a[contains(@href, 'openDemoAccount?p=5')]")).click();
				}
				break;
				
			default:
				System.out.println("MT Plaotform is not supported: " + platform);
		}
		
	
		switch(Brand)
		{
			case "vt":
				actTypeSel="//img[contains(@class, 'img')]";
				break;
				
			case "fsa":
			case "svg":
				actTypeSel="//div[@class='box box_type']//li";
				break;
			
			default:
				actTypeSel="//div[@class='box box2']//li//img";
		}
		//Get all account types listed and choose random one
		List<WebElement> liItems = driver.findElements(By.xpath(actTypeSel));
		
		//ystem.out.println("Account Type Size is: " + liItems.size());
		int j=r.nextInt(liItems.size());
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
		if(Brand.equalsIgnoreCase("vt")) {
			liItems.get(0).click();
		}else {
			liItems.get(j).click();
			System.out.println("Account Type choice is: "+j);
		}
		Thread.sleep(500);
		

		//Get all the currency listed and choose random one  img.poppins-medium
		
		switch(Brand)
		{
			case "vt":
				curTypeSel="//div[@class='currency_img']/div/img";  
				break;
				
			case "fsa":
			case "svg":
				curTypeSel="//div[@class='box box_currency']//li";
				break;
				
			default:
				curTypeSel="//div[@class='box box3']//li//img";
		}
		liItems = driver.findElements(By.xpath(curTypeSel));
		j=r.nextInt(liItems.size());
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
	
		liItems.get(j).click();

		
		
		//Choose Leverage div: el-form-item is-required
		switch(Brand)
		{
			case "vt":
				tempStr="div.el-form-item.is-required";
				liItems = driver.findElements(By.cssSelector(tempStr)).get(2).findElements(By.cssSelector("div.radio p"));
				break;
			
			case "fsa":
			case "svg":
				liItems = driver.findElements(By.xpath("//div[@class='box box_default']")).get(0).findElements(By.xpath(".//li"));
				break;
				
			default:
				tempStr="div.el-form-item.is-required";
				liItems = driver.findElements(By.cssSelector(tempStr)).get(2).findElements(By.cssSelector("div.radio p"));
		}	
		

		j=r.nextInt(liItems.size());
		//System.out.println("Leverage options: " + liItems.size() + " Selected index: "+ j);
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
	   liItems.get(j).click();
	
		//Choose Balance
		switch(Brand)
		{
			case "vt":
				tempStr="div.el-form-item.is-required";
				liItems = driver.findElements(By.cssSelector(tempStr)).get(3).findElements(By.cssSelector("div.radio p"));
				break;
			
			case "fsa":
			case "svg":
				liItems = driver.findElements(By.xpath("//div[@class='box box_default']")).get(1).findElements(By.xpath(".//li"));
				break;
				
			default:
				tempStr="div.el-form-item.is-required";
				liItems = driver.findElements(By.cssSelector(tempStr)).get(3).findElements(By.cssSelector("div.radio p"));
		}	
		

		j=r.nextInt(liItems.size());
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", liItems.get(j));
	   liItems.get(j).click();	   
	   
		//Click Submit
		 driver.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]")).click();
	
		Thread.sleep(500);
		
		//Print return messages:
		switch(Brand)
		{
			case "vt":
				System.out.println(driver.findElement(By.cssSelector("div.success_content>div.main>div")).getText());
				break;
				
			case "fsa":
			case "svg":
				System.out.println(driver.findElement(By.cssSelector("div.success_content p.success_info>p")).getText());
				break;
			
			default:
				System.out.println(driver.findElement(By.cssSelector("div.success_content p.success_info div")).getText());
		}
	
		
		//Click back to home page
		//driver.findElement(By.linkText("BACK TO HOME PAGE")).click();  
		driver.findElement(By.xpath("//a[contains(translate(text(),'Back to Home Page','BACK TO HOME PAGE'),'BACK TO HOME PAGE')]")).click();
		
	}

	@Test
	@Parameters(value= {"Brand"})
	public void AddLive(String Brand) throws Exception
	{
		
		//Go to Demo Home
		Thread.sleep(1000);
		funcGo2LiveHome(Brand);
		//Utils.waitUntilLoaded(driver); 
		//Click OPEN LIVE ACCOUNT

		driver.findElement(By.xpath(".//a[@href='/register']")).click();
	
		Thread.sleep(500);
		
		Assert.assertTrue(driver.getCurrentUrl().endsWith("register"), "OPEN LIVE ACCOUNT button doesn't navigate to REGISTER.");
			
	}
	
	@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}
	

}
