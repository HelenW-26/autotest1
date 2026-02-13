package adminBase;

import com.bastiaanjansen.otp.TOTPGenerator;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.elements.AdvertiseElements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.time.Duration;
import java.util.HashMap;


public class Login {
	
	private WebDriver driver;

	private WebElement userName;
	private WebElement password;
	private WebElement btnLogin;
	private WebElement forgetPassword;

	private String AdminURL;

	private WebDriverWait wait03;

	protected WebDriverWait fastwait;



	public Login(WebDriver tDriver) {
		driver = tDriver;
	}

	
	// Input userName
	public WebElement getUserName() {

		try {

			userName = driver.findElement(By.name("userName_login"));

		} catch (Exception e) {
			System.out.println("Get User Name error.");
		}

		return userName;
	}
	
	// Input userName
	public WebElement getPassword() {

		try {

			password = driver.findElement(By.name("password_login"));

		} catch (Exception e) {
			System.out.println("Get Password error.");
		}

		return password;
	}
	
	// Button login
	public WebElement getBtnLogin() {

		try {

			btnLogin = driver.findElement(By.id("btnLogin"));

		} catch (Exception e) {
			System.out.println("Get Button Login error.");
		}

		return btnLogin;
	}
	
	// Link forget password
	public WebElement getForgetPassword() {

		try {

			forgetPassword = driver.findElement(By.className("button_box login_password"));

		} catch (Exception e) {
			System.out.println("Get Link Forget Password  error.");
		}
		
		return forgetPassword;
	}

	@Parameters({ "AdminURL", "AdminName", "AdminPass", "Regulator" })
	@Test(priority = 0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Regulator,String optCode,GlobalProperties.ENV env,GlobalProperties.BRAND brand)
			throws Exception {
		//ExtentTestManager.startTest(method.getName(), "Description: Login to Admin Portal");
		this.AdminURL = AdminURL;
		driver.get(AdminURL);
		funcLogInAdmin(driver, AdminName, AdminPass, Regulator,optCode,env,brand);
	}

	@Parameters({ "AdminURL", "AdminName", "AdminPass", "Regulator" })
	@Test(priority = 0)
	public void AdminLogInNew(String AdminURL, String AdminName, String AdminPass, String Regulator,String optCode,GlobalProperties.ENV env,GlobalProperties.BRAND brand)
			throws Exception {

		this.AdminURL = AdminURL;
		driver.get(AdminURL);

		// Clear login session
		driver.manage().deleteAllCookies(); // Deletes all cookies
		((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
		((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
		driver.navigate().refresh();

		// Login Process
		funcLogInAdmin(driver, AdminName, AdminPass, Regulator,optCode,env,brand);
	}

	public void waitLoading() {

		//GlobalMethods.printDebugInfo(new Date().toString() + " Loading: " + driver.getCurrentUrl());
		//fastwait.until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[contains(@class,'client-portal-loading')]"))));
		fastwait = new WebDriverWait(driver, Duration.ofSeconds(GlobalProperties.WAITTIME,50));
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.xpath("//i[contains(@class,'client-portal-loading') or contains(@class,'client-portal-loading-au')]"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});

		//GlobalMethods.printDebugInfo(new Date().toString() + " Loading page completed, url is: " + driver.getCurrentUrl());
	}

	public void funcLogInAdmin(WebDriver driver,String userName, String passWord, String Regulator,String optCode, GlobalProperties.ENV env,GlobalProperties.BRAND brand) throws Exception
	{
		// handle OPT popup
		waitLoading();
		wait03 = new WebDriverWait(driver,Duration.ofSeconds(10));
		driver.findElement(By.id("exampleInputEmail1")).clear();
		driver.findElement(By.id("exampleInputEmail1")).sendKeys(userName);
		LogUtils.info("Input admin userName: " + userName);
		driver.findElement(By.id("password_login")).clear();
		driver.findElement(By.id("password_login")).sendKeys(passWord);
		LogUtils.info("Input admin password: " + passWord);
		waitLoading();

		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnLogin")));
		driver.findElement(By.id("btnLogin")).click();
		LogUtils.info("Click login button");
		waitLoading();

		//input 2FA code
//		HashMap<String,String> prod2FAKey = new HashMap<>();
//		HashMap<String,String> alpha2FAKey= new HashMap<>();
//
//		prod2FAKey.put("vt","");
//		prod2FAKey.put("pug","");
//		prod2FAKey.put("vfx","");
//		prod2FAKey.put("mo","");
//		prod2FAKey.put("um","");
//
//		alpha2FAKey.put("vt","");
//		alpha2FAKey.put("vfx","");
//		alpha2FAKey.put("um","");
//		alpha2FAKey.put("pug","");
//		alpha2FAKey.put("mo","");

		//Comment 2FA for security reason
		/*if(env.toString().equalsIgnoreCase("prod"))
		{
			twoFACode(prod2FAKey.get(brand.toString().toLowerCase()));
		}
		else
		{
			twoFACode(alpha2FAKey.get(brand.toString().toLowerCase()));
		}*/
		//2FA has replaced to opt
		/*try
		{
			/*WebElement sendCodeBtn = driver.findElement(By.xpath("//input[@id='btnSendCode']"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", sendCodeBtn);*/
			//sendCodeBtn.click();
		/*	waitLoading();
			WebElement codeInput = driver.findElement(By.xpath("//input[@id='code_mfa']"));
			codeInput.sendKeys(optCode);
			WebElement loginBtn = driver.findElement(By.xpath("//input[@value='Login']"));
			loginBtn.click();
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("no OPT needed");
		}*/
		//wait for the next page to show
		waitLoading();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Users' or text()='用户管理']")));

		LogUtils.info("Click login button");

		//switch language
		if(Regulator.equalsIgnoreCase("vt") || Regulator.equalsIgnoreCase("svg")|| Regulator.equalsIgnoreCase("fsa"))
		{

			Actions aMenu=new Actions(driver);

			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.header-profile"))).click();

			aMenu.moveToElement(driver.findElement(By.cssSelector("ul.dropdown-menu.dropdown-menu-right li.dropdown-submenu a"))).build().perform();
			waitLoading();
			driver.findElement(By.xpath("//a[contains(text(),'English')]")).click();
			LogUtils.info("Switch language to English");
			waitLoading();
		}

		//For new regulator branch
		if (driver.findElements(By.id("regulation_label")).size()>0) {
			//driver.findElement(By.id("regulation_label")).click();
			WebElement regul= driver.findElement(By.id("regulation_label"));
			JavascriptExecutor javascript = (JavascriptExecutor) driver;
			javascript.executeScript("arguments[0].click()", regul);
			waitLoading();
			//driver.findElement(By.xpath("//span[contains(text(),'"+Regulator+"')]")).click();
			//Switch regulator
			//	driver.findElement(By.xpath("//label[@id='regulation_label']")).click();
			driver.findElement(By.xpath(String.format("//ul[@class='dropdown-menu favorite-content']/li/div/span[text()='%s']", Regulator.toUpperCase()))).click();
			LogUtils.info("Switch Regulator to " + Regulator.toUpperCase());
		} else {
			System.out.println("Single Regulator. No need to switch.");
		}
	}

	public void twoFACode(String key)
	{
		byte[] secret = key.getBytes();
		TOTPGenerator totp = new TOTPGenerator.Builder(secret).build();
		GlobalMethods.printDebugInfo("2FA code is: "+ totp.now());

		driver.findElement(By.xpath("//input[@id='googleAuthTotp']")).sendKeys(totp.now());
		driver.findElement(By.xpath("//input[@id='loginBtn-googleAuth']")).click();
	}
}
