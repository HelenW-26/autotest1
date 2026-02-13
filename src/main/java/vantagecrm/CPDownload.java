package vantagecrm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * This class is to test Client Portal Download page
 * 1) Verify file can be downloaded and the file name is the expected one
 * 2) Verify Android/iPhone links are correct
 * 3) Verify .com is used for CIMA and .com.au is used for ASIC
 */

public class CPDownload {

	WebDriver driver;
	WebDriverWait wait50;

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		ChromeOptions options=new ChromeOptions();
		
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.prompt_for_download", true);
		options.setExperimentalOption("prefs", prefs);
		if(Boolean.valueOf(headless)) {
			options.addArguments("window-size=1920,1080");
			options.addArguments("headless");
		}
		
		
		driver = new ChromeDriver(options);
		
		
		context.setAttribute("driver", driver);
		  
		driver.manage().window().maximize();		 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		  
		wait50=new WebDriverWait(driver, Duration.ofSeconds(50));
	
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{

		driver.get(TraderURL);
 		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		wait50.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("DEPOSIT FUNDS")));
			
	}
	

	@Test(dependsOnMethods="CPLogIn")
	@Parameters(value= {"TraderURL", "Brand"})
	void CheckDownloads(String TraderURL, String Brand) throws Exception
	{
		
		HashMap<String, String> fileList = new HashMap<String, String>();
		String fileName="";

		//Put AU MT4 values
		fileList.put("aumt4win", "vantagefx4setup");
		fileList.put("aumt4mac", "MT4-Mac10.13");
		
		//Put AU MT5 values
		fileList.put("aumt5win", "vantagefx5setup");
		fileList.put("aumt5mac", "MetaTrader5-Mac-New");
		
		//Put KY MT4 values
		fileList.put("kymt4win", "vantagefxinternational4setup");
		fileList.put("kymt4mac", "MetaTrader4_VIG-setup");
		
		//Put KY MT5 values
		fileList.put("kymt5win", "vantagefxinternational5setup");
		fileList.put("kymt5mac", "MetaTrader4_VIG-setup");
		
		//Yanni: need to put new regulators' files names here if they don't sharewith KY
		/*
		 * ======================
		 * ======================
		 */
		
		//Navigate to HomePage
		driver.navigate().to(TraderURL);	
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait50);
		
				
		//Click DOWNLOADS link
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span[contains(text(),'DOWNLOADS')]")).click();
		Thread.sleep(1000);
	
		
		switch(Brand)
		{
		case "au":
				
			//Verify MT4 Window file name
			try {
				driver.findElement(By.xpath("//a[contains(@href, 'vantagefx4setup.exe')]")).click();
				fileName=getSaveAsFileName();
				Thread.sleep(2000);
				
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			
			
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt4win")), Brand + " MT4 Windows Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() + " MT4 Windows Download Passed the check.");
			
			//Verify MT4 Mac File
			try {
				driver.findElement(By.xpath("//a[contains(@href, 'download-mac-MT4')]")).click();
				fileName=getSaveAsFileName();
				Thread.sleep(2000);
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			
			
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt4mac")) , Brand + " MT4 Mac Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() +  " MT4 Mac Download Passed the check.");
			
			//Verify MT5 Windows file
			try {
			driver.findElement(By.xpath("//a[contains(@href, 'vantagefx5setup.exe')]")).click();
			fileName=getSaveAsFileName();
			Thread.sleep(2000);
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			
			System.out.println(fileName);
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt5win")), Brand + " MT5 Windows Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() +  " MT5 Windows Download Passed the check.");
			
			//Verify MT5 Mac File
			try {
			driver.findElement(By.xpath("//a[contains(@href, 'download-mac-MT5')]")).click();
			fileName=getSaveAsFileName();
			Thread.sleep(3000);
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt5mac")), Brand + " MT5 Mac Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() + " MT5 Mac Download Passed the check.");
			
			
			break;
			
		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
		case "regulator2":

			
			try {
				driver.findElement(By.xpath("//a[contains(@href, 'MT4_Windows')]")).click();
				fileName=getSaveAsFileName();
				
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			
			
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt4win")), Brand + " MT4 Windows Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() +  " MT4 Windows Download Passed the check.");
			
			//Verify MT4 Mac File
			try {
				driver.findElement(By.xpath("//a[contains(@href, 'MT4_Mac')]")).click();
				fileName=getSaveAsFileName();
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			
			
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt4mac")) , Brand + " MT4 Mac Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() +  " MT4 Mac Download Passed the check.");
			
			//Verify MT5 Windows file
			try {
			driver.findElement(By.xpath("//a[contains(@href, 'MT5_Windows')]")).click();
			fileName=getSaveAsFileName();
			
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt5win")), Brand + " MT5 Windows Download File Name is not the expected one.");
			System.out.println("Brand " + Brand.toUpperCase() +  " MT5 Windows Download Passed the check.");
			
			//Verify MT5 Mac File
	/*		try {
			driver.findElement(By.xpath("//a[contains(@href, 'MT5_Mac')]")).click();
			fileName=getSaveAsFileName();
			}catch (NoSuchElementException e )
			{
				System.out.println("MT4 Windows Link can NOT be found");
			}
			Assert.assertTrue(fileName.contains(fileList.get(Brand.toLowerCase()+"mt5mac")), Brand + " MT5 Mac Download File Name is not the expected one.");
			System.out.println(Brand + " MT5 Mac Download Passed the check.");*/
			
			
			break;
			
		case "vt":
		case "fsa":
		case "svg":
			default:
				System.out.println("Brand " + Brand.toUpperCase() +  " is not supported.");
		}
		

		
		//Verify MT4 iPhone/iPad
		verifyAppLink(Brand, "MT4", "iPhone");
		
		//Verify MT4 Android
		verifyAppLink(Brand, "MT4", "Android");
		
		

		//Verify MT5 iPhone/iPad
		verifyAppLink(Brand, "MT5", "iPhon");
		
		//Verify MT5 Android
		verifyAppLink(Brand, "MT5", "Android");
		
		//Verify WebTrader
		List<WebElement> webTraderLinks = driver.findElements(By.xpath("//a[contains(@href, 'webtrader')]"));
		
		Assert.assertTrue(webTraderLinks.size()==2, "Both MT4 and MT5 WebTrader should exist.");
		
		for(int i=0; i<webTraderLinks.size();i++)
		{
			
			switch(Brand)
			{
				case "au":
					/*
					 * Assert.assertTrue(webTraderLinks.get(i).getAttribute("href").contains("vantagefx.com.au/"), "ASIC should use vantagefx.com.au");
					 * break;
					 */
					
				case "ky":
				case "vfsc":
				case "vfsc2":
				case "fca":
					Assert.assertTrue(webTraderLinks.get(i).getAttribute("href").contains("vantagefx.com/"), "VFX should use vantagefx.com");
					break;
					
				case "vt":
				case "fsa":
				case "svg":
					default:
						System.out.println("Brand " + Brand.toUpperCase() +  " is not supported.");
			}
			
		}
	
		
	}
	
	@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}
	
	//Call AutoIT to get the download file Name and close the download dialog, return the filename
	public String getSaveAsFileName() throws Exception
	{
		String filePath=Utils.workingDir + "\\src\\main\\resources\\vantagecrm\\doc\\getSaveAsFileName.exe";
		String fileName="";
		
		Thread.sleep(1000);
		
		try {
			Process result=Runtime.getRuntime().exec(filePath);
			Thread.sleep(2500);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(result.getInputStream()));

			String line;
			
			while ((line = input.readLine()) != null) {
			  System.out.println("File Name downloaded is: " + line);
			  fileName=fileName.concat(line);
			}
			
			result.waitFor();
			//System.out.println("File Name is " +fileName);
			
			
		} catch (IOException | InterruptedException e) {
			System.out.println("Failed to Getting File Name");
		}
		
		return fileName;
		
	}
	
	//Verify Apple/Android MT4/MT5 links
	public void verifyAppLink(String Brand, String platform, String appsBrand) throws Exception
	{
		
		String xpath = "//a[contains(@href, '" + platform + "_" + appsBrand + "')]";
		String appWebsite="";
		WebElement e;
		
		switch(appsBrand)
		{
			case "iPhone":
			case "iPhon":
				appWebsite = "apps.apple.com";
				break;
				
			case "Android":
				appWebsite = "play.google.com";
				break;
					
			default:
				System.out.println("Don't support " + appsBrand);
		}
		

		e= driver.findElement(By.xpath(xpath));
		
		//Verify link 
		switch(Brand)
		{
			case "au":
				/*
				 * Assert.assertTrue(e.getAttribute("href").contains("vantagefx.com.au/"), "Domain should be vantagefx.com.au.");
				 * break;
				 */
				
			case "ky":
			case "fca":
			case "vfsc":
			case "vfsc2":
				Assert.assertTrue(e.getAttribute("href").contains("vantagefx.com/"), "Domain should be vantagefx.com.");
				break;
				
			case "vt":
			case "fsa":
			case "svg":
				Assert.assertTrue(e.getAttribute("href").contains("puprime.com/"), "Domain should be puprime.com.");
				break;
			default:
				System.out.println("Don't support " + Brand);
		}
	
		//Click the link to open Android/Apple market
		Thread.sleep(1000);
		e.click();		
		Thread.sleep(500);

		String currentURL = driver.getCurrentUrl();
		driver.navigate().back();
		
		Assert.assertTrue(currentURL.contains(appWebsite), "Link Navigation is wrong.");
		
		
	}
}
