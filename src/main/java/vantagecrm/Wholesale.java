package vantagecrm;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;
import adminBase.WholesaleAudit;

public class Wholesale {
	
	
	static WebDriver driver;
	int waitIndex = 1;
	WebDriverWait wait03;
 //   public WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
	// Launch driver
	@BeforeClass(alwaysRun = true)
	// @Test(enabled=false)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{  // Added one parameter ITestContext by Yanni on 5/15/2019

		// System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
		driver = Utils.funcSetupDriver(driver, "chrome", headless);

		utils.Listeners.TestListener.driver = driver;
		context.setAttribute("driver", driver);           // Added one parameter ITestContext by Yanni on 5/15/2019

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
		}

		wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	@AfterClass(alwaysRun = true)
	void ExitBrowser() {

		Utils.funcLogOutAdmin(driver);
		// Close all browsers
		driver.quit();
	}

	// Login Admin with credentials

	@Parameters({ "AdminURL", "AdminName", "AdminPass", "Brand" })
	@Test(priority = 0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand, Method method)
			throws Exception {
		ExtentTestManager.startTest(method.getName(), "Description: Login to Admin Portal");
		// Login AU admin
		driver.get(AdminURL);
		Utils.funcLogInAdmin(driver, AdminName, AdminPass, Brand);
	}
	
	

	/*
	 * public void SearchAudit(String TraderName) throws Exception { WholesaleAudit
	 * wholesaleAuditObject = new adminBase.WholesaleAudit(driver); //Navigate to
	 * Wholesale audit Page
	 * driver.findElement(By.linkText("Task Management")).click();
	 * Thread.sleep(500);
	 * driver.findElement(By.linkText("Wholesale Audit")).click();
	 * Thread.sleep(2000);
	 * 
	 * wholesaleAuditObject.operationAudit(TraderName); Thread.sleep(5000); }
	 */
	public void WholesaleAudit(String TraderName) throws Exception
	{
		
		//Navigate to Wholesale audit Page
		driver.findElement(By.linkText("Task Management")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("Wholesale Audit")).click();
		Thread.sleep(2000);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.operationAudit(TraderName);
		Thread.sleep(5000);
		//Switch to pop up audit window
		  String parentWindow = driver.getWindowHandle();
		  
		  Set <String> windows = driver.getWindowHandles();
		  
		  for (String window:windows) 
		  { 
			  if (!window.equals(parentWindow)) {
		  driver.switchTo().window(window); 
			  } 
		  }
		  
		  Thread.sleep(2000);
		
		 
	}
	
	// Wealthy Test pending
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderName"})
	public void WealthyAuditPending(String TraderName) throws Exception
	{
		WholesaleAudit(TraderName);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.wealthyAuditPending();
		Thread.sleep(2000);
		wholesaleAuditObject.validateStatus("PENDING");
	}
	
	// Wealthy Test reject
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderName"})
	public void WealthyAuditReject(String TraderName) throws Exception
	{
		WholesaleAudit(TraderName);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.wealthyAuditReject();
		Thread.sleep(2000);
		wholesaleAuditObject.validateStatus("REJECTED");
	}
	
	// Wealthy Test complete
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderName"})
	public void WealthyAuditComplete(String TraderName) throws Exception
	{
		WholesaleAudit(TraderName);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.wealthyAuditComplete();
		Thread.sleep(2000);
		wholesaleAuditObject.validateStatus("COMPLETED");
	}
	
	
	// Sophisticated Test pending
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderName"})
	public void SophisticatedAuditPending(String TraderName) throws Exception
	{
		WholesaleAudit(TraderName);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.sophisticatedAuditPending();
		Thread.sleep(2000);
		wholesaleAuditObject.validateStatus("PENDING");
	}
	
	// Sophisticated Test Reject
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderName"})
	public void SophisticatedAuditReject(String TraderName) throws Exception
	{
		WholesaleAudit(TraderName);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.sophisticatedAuditReject();
		Thread.sleep(2000);
		wholesaleAuditObject.validateStatus("REJECTED");
	}
	
	// Sophisticated Test Complete
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderName"})
	public void SophisticatedAuditComplete(String TraderName) throws Exception
	{
		WholesaleAudit(TraderName);
		WholesaleAudit wholesaleAuditObject = new adminBase.WholesaleAudit(driver);
		wholesaleAuditObject.sophisticatedAuditComplete();
		Thread.sleep(2000);
		wholesaleAuditObject.validateStatus("COMPLETED");
	}
}
