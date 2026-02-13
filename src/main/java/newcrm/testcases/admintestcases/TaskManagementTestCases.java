package newcrm.testcases.admintestcases;


import java.time.Duration;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPI;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import newcrm.testcases.BaseTestCase;
import newcrm.utils.MyWebDriverListener;
import vantagecrm.Utils;
import adminBase.Login;
import adminBase.TaskManagement;


public class TaskManagementTestCases extends BaseTestCase {
	private AdminAPI admin;
	protected EmailDB emailDB;
	String optCode;
	
	//launch browser to get driver
	public void launchEmptyBrowser(String headless, ITestContext context) {
		//WebDriverManager.chromedriver().setup();
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        if(Objects.equals(headless, "true")) {
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        }

		//使用带有监听器的driver
		MyWebDriverListener ls = new MyWebDriverListener();
		/*EventFiringWebDriver e_driver = new  EventFiringWebDriver(driver);
		e_driver.register(ls);
		driver = e_driver;*/
		driver = new EventFiringDecorator(ls).decorate(driver);

		utils.Listeners.TestListener.driver=this.driver;
		context.setAttribute("driver", driver);

	}
	
	@Override
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
							@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
							@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
							ITestContext context) {
		launchEmptyBrowser( headless, context);
		
	}
	 

	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"headless","AdminURL", "AdminName", "AdminPass", "Regulator","Brand","TestEnv"})
	public void initiEnv(String headless, String AdminURL,String AdminName,String AdminPass, String Regulator, String Brand,String TestEnv,ITestContext context) throws Exception {
		String adminURL = AdminURL;
		String adminName = AdminName;
		String adminPass = AdminPass;

		launchEmptyBrowser(headless, context);

		//login admin portal
		Login login = new Login(driver);
		admin = new AdminAPI(AdminURL, GlobalProperties.REGULATOR.valueOf(Regulator),AdminName,AdminPass, GlobalProperties.BRAND.valueOf(Brand), GlobalProperties.ENV.valueOf(TestEnv.toUpperCase()));
		optCode = admin.getCode();
		login.AdminLogIn(AdminURL, AdminName, AdminPass, Regulator,optCode,GlobalProperties.ENV.getENV(TestEnv.toUpperCase()),GlobalProperties.BRAND.valueOf(Brand));

		//setCode(adminURL,adminName,adminPass);
	}
	
	@Test(priority = 0)
	@Parameters(value= {"Brand","AdminURL","TraderName","Regulator"})
	public void testadminwithdraw(String brand,String AdminURL,String TraderName,String Regulator) throws Exception {

		TaskManagement tm = new TaskManagement(driver,brand);
		//withdraw audit
		tm.funcWDCompletebyChannelSmokeTest(AdminURL, TraderName, brand, Regulator);
	}

	public void setCode(String AdminURL,String AdminName,String AdminPass)
	{
		// handle OPT popup
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try
		{

			driver.navigate().to(AdminURL);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exampleInputEmail1")));
			driver.findElement(By.id("exampleInputEmail1")).clear();
			driver.findElement(By.id("exampleInputEmail1")).sendKeys(AdminName);
			driver.findElement(By.id("password_login")).clear();
			driver.findElement(By.id("password_login")).sendKeys(AdminPass);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnLogin")));
			driver.findElement(By.id("btnLogin")).click();

			WebElement sendCodeBtn = driver.findElement(By.xpath("//input[@id='btnSendCode']"));
			//JavascriptExecutor js = (JavascriptExecutor) driver;
			//js.executeScript("arguments[0].click()", sendCodeBtn);
			sendCodeBtn.click();

			JSONObject obj = emailDB.getCodeRecord(dbenv, dbBrand, dbRegulator,TraderName);
			System.out.println(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
			String code = obj.getJSONObject("vars").getString("CODE");

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='code_mfa']")));
			WebElement codeInput = driver.findElement(By.xpath("//input[@id='code_mfa']"));
			codeInput.sendKeys(code);
			WebElement loginBtn = driver.findElement(By.xpath("//input[@value='Login']"));
			loginBtn.click();
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("no OPT needed");
		}
	}
}






