package vantagecrm;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CPWholesaleWealthy {
	WebDriver driver;

	// different wait time among different environments. Test environment will
	// use 1 while beta & production will use 2.
	// Initialized in LaunchBrowser function.
	int waitIndex = 1;

	static WebDriverWait wait01;
	static WebDriverWait wait02;
	static WebDriverWait wait03;
	WebDriverWait wait60;
	WebDriverWait wait100;


	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{

		// System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		context.setAttribute("driver", driver);          // Added by Yanni on
												          // 5/15/2019
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		wait01 = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait02 = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait03 = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait60 = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait100 = new WebDriverWait(driver, Duration.ofSeconds(1000));
	}

	@Test(priority = 0)
	@Parameters(value = { "TraderURL", "TraderName", "TraderPass", "Brand" })
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception

	{

		// Login AU CP
		driver.get(TraderURL);

		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		Thread.sleep(waitIndex * 2000);
	}
	
	@Test(dependsOnMethods = "CPLogIn", alwaysRun = true)
	void WealthTest() throws Exception
	{
		clientBase.wholesale wholesaleObj = new clientBase.wholesale(driver);
		wholesaleObj.getUpgrade2ProLnk().click();
		Thread.sleep(waitIndex * 2000);
		wholesaleObj.getUpgradeCheckbox().click();
		wholesaleObj.getUpgradeButton().click();
		
		wholesaleObj.get1stQuestionOption250k().click();
		Thread.sleep(waitIndex * 2000);
		wholesaleObj.get2ndQuestionOptionYes().click();
		wholesaleObj.get3rdQuestionOptionYes().click();
		
		wholesaleObj.getNextButton().click();
		
		wholesaleObj.chooseUploadedFile();
		Thread.sleep(waitIndex * 2000);
		
		wholesaleObj.getDeclarationCheckbox().click();
		
		wholesaleObj.getUploadButton().click();
		
		Thread.sleep(waitIndex * 2000);
		wholesaleObj.getBackButton().click();
		
		
		
	}
}
