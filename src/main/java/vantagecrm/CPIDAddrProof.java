package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
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

import clientBase.idPOAUpload;
import clientBase.navigationBar;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * This class is to test Client Portal Upload ID/Address Proof function. Currently only Address Proof is available
 * 1) Verify Upload dialog displays in both Home page and Withdraw page when user has no address proof 
 * 2) Verify Withdraw form is not displayed when no address proof is uploaded. 
 *    Verify Withdraw form is displayed when at least one address proof is uploaded and the record status <> completed and status <> rejected
 * 3) Verify Address proof can be uploaded successfully when only submit 1 file
 * 
 */

public class CPIDAddrProof {

	WebDriver driver;
	WebDriverWait wait50;
	WebDriverWait wait10;
	navigationBar navCls;
	idPOAUpload idPOACls;
	
	public static enum SupportEMAIL{
		
		ky("support@vantagefx.com"),
		vfsc("support@vantagefx.com"),
		vfsc2("support@vantagefx.com"),
		au("support@vantagefx.com.au"),
		fca("support@vantagefx.co.uk"),
		vt("info@vtmarkets.com"),
		fsa("info@puprime.com"),
		svg("info@puprime.net"),
		regulator2("support@vantagefx.com");
		
		private String email;
		
		private SupportEMAIL(String email)
		{
			this.email = email;
			
		}

	}
	
	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand","headless"})
	public void LaunchBrowser(String TestEnv, String Brand, @Optional("False") String headless, ITestContext context)
	{
		
		//System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
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
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		wait10=new WebDriverWait(driver, Duration.ofSeconds(10));
		wait50=new WebDriverWait(driver, Duration.ofSeconds(50));
	
		navCls = new navigationBar(driver,Brand);
		idPOACls = new idPOAUpload(driver,Brand);
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
	  	  //Login AU CP
			driver.get(TraderURL);

			Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);	
			Thread.sleep(2000);
			
			navCls = new navigationBar(driver,Brand);
			
			Utils.waitUntilLoaded(driver);

	}
	
	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void UploadAddrProof(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	{
		String idpoaType = "poa";
		funcUploadIDOrPOA(idpoaType, TraderURL,TraderName, TraderPass, Brand);
		
	}

	
	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand"})
	void UploadProofOfIdentity(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	{
		String idpoaType = "id";
		funcUploadIDOrPOA(idpoaType, TraderURL,TraderName, TraderPass, Brand);
		
	}
	
	
	//idpoaType: idpoaType = poa when upload poa files. idpoaType = id when uploading id files
	void funcUploadIDOrPOA(String idpoaType, String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	{
		driver.get(TraderURL);
		String xpathCOMAPPbtn = "//span[contains(translate(text(),'Complete application', 'COMPLETE APPLICATION'),'COMPLETE APPLICATION')]";
		
		//Check Home Page: COMPLETE APPLICATION button is displayed.
		System.out.println("Is COMPLETE APPLICATION button displayed in Home page...");
		try 
		{
			wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathCOMAPPbtn)));
			System.out.println("Displayed.");
		}catch(TimeoutException e)
		{
			System.out.println("Not Displayed.");
			Assert.assertTrue(false, "COMPLETE APPLICATION button is not displayed.");
		}
		
		
		//Navigate to Withdraw From
		Thread.sleep(2000);
		navCls.getFundsEle().click();
		Thread.sleep(500);
		//wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(),'WITHDRAW FUNDS')]"))).click();
		wait10.until(ExpectedConditions.visibilityOf(navCls.getWithdrawMenuEle())).click();
		Thread.sleep(2000);
		
		//Check Withdraw Page: ACCOUNT APPLICATION INCOMPLETE dialog is displayed.
		System.out.println("Is COMPLETE APPLICATION button displayed  in Withdraw page...");
		try
		{
			wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathCOMAPPbtn)));	
			System.out.println("Displayed in WITHDRAW PAGE.");
		}catch(TimeoutException e)
		{
			System.out.println("Not Displayed.");
			Assert.assertTrue(false, "COMPLETE APPLICATION button is not displayed.");
		}
		

		
		//Click COMPLETE APPLICATION button to upload Proof of Address
		Thread.sleep(1000);
		driver.findElement(By.xpath(xpathCOMAPPbtn)).click();
		

		//Address proof is the second one if there are two upload input windows
		idPOACls.getUploadFileEle(idpoaType).sendKeys(Utils.workingDir+"\\proof.png");
		Thread.sleep(500);
		idPOACls.getUploadFileEle(idpoaType).sendKeys(Utils.workingDir+"\\proof.png");
		
		Thread.sleep(1000);
		System.out.println("Submit Required Documents...");
		
		
		  //Click UPLOAD button
		  idPOACls.getUploadBtn().click();
		  
		  Thread.sleep(1000);
		  //Verify message:
		  System.out.println("Result is: " + idPOACls.getMsgEle().getText());
	
		  Thread.sleep(500);
		  
		  //Press ESC to close the popup dialog
		  driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
		
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
