package vantagecrm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newcrm.global.GlobalMethods;
import newcrm.utils.download.DownloadFile;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import clientBase.cpHeader;
import clientBase.cpLogIn;
import clientBase.ipNavigationBar;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;
import vantagecrm.TaskManagement.WithdrawM;


public class Utils {
	public static String ChromePath="C:\\Vantage\\automation\\chromedriver_win32\\chromedriver.exe";
	public static String AndroidChromePath="C:\\Program Files\\Appium\\resources\\app\\node_modules\\appium\\node_modules\\appium-chromedriver\\chromedriver\\win";
	public static String IEPath="";
	public static String FFPath="";
	public static String workingDir=System.getProperty("user.dir");
	//public static String ChromePath=workingDir+"\\chromedriver.exe";
	
	static final String emailSuffix="@auto-test.com";
	static final String webUserPrefix = "testcrmw";
	static final String ibUserPrefix = "testcrmib";
	static final String addUserPrefix = "testcrma";
	public static final String testcrmPrefix = "testcrm";

    protected static String registerUserName;  //User name / email prefix (they are the same for AU)
    protected static String registerUserNameVT;  //VT user name in Chinese
	protected static String addIBName;
	protected static String addClientName;
	protected static String addJointName;
  
	static final String SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static final String scSOURCE="许多读者都指出这是金庸小说的一个问题比如网上有人纠正说蚕豆花生两种作物都是中国本土所无而后才逐渐由国外传进来的蚕豆又名胡豆寒豆罗汉豆大概在元代才由波斯传入中国";
	static final String numberList="0123456789";	
	
	static	enum Currency {
		USD, AUD, GBP, EUR,	SGD, JPY, NZD, CAD;
		
		public static Currency getRandom() {
			Random r=new Random();
			
			return values()[r.nextInt(values().length)];
		}
}

	static enum AccountT {
		RAW, STP;
	
		public static AccountT getRandom() {
			Random r=new Random();
			
			return values()[r.nextInt(values().length)];
		}
}
	
	public static String[] getDBName(String Brand)
	{
		 
		String[] dbArray = new String[] {};

		switch (DBUtils.dbLocation.toLowerCase()) {

		/*
		 * case "taipei":
		 * dbArray = getDBNameTP(Brand);
		 * break;
		 * case "australia":
		 * dbArray = getDBNameAU(Brand);
		 * break;
		 */

		case "cloud":
			dbArray = getDBNameALPHA(Brand);
			break;
		/*
		 * case "mregulator":
		 * dbArray = getDBNameMRegulator(Brand);
		 * break;
		 */

		default:
			System.out.println("DB Location is neither Taipei nor Australia");
			Assert.assertTrue(false, "DB Location is neither Taipei nor Australia");

		}

		return dbArray;
	}


	// Added by Yanni on 16/07/2020: for newly added Alpha Test EVN which is based on AliCloud Australia
	public static String[] getDBNameALPHA(String Brand) {

		String[] dbArray = new String[] {};

		switch (Brand) {
		case "vt":
			dbArray = new String[] { "dev_vt_m_regulator_global", "dev_vt_m_regulator_business"};
			break;
		case "vtsvg":
			dbArray = new String[] { "dev_vt_m_regulator_global", "dev_vt_m_svg_business" };
			break;
		/*
		 * case "pug":
		 * dbArray = new String[] { "dev_pug_m_regulator_global", "dev_pug_m_regulator_business" };
		 * break;
		 */
		case "fsa":
			dbArray = new String[] { "dev_pug_m_reg_fsa_global", "dev_pug_m_reg_fsa_business" };
			break;
			
		case "svg":
			dbArray = new String[] { "dev_pug_m_reg_fsa_global", "dev_pug_m_reg_svg_business" };
			break;
			
		case "au":
			dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_asic" };
			break;

		case "ky":
			dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_cima" };
			break;

		case "vfsc":
			dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_vfsc" };
			break;

		case "vfsc2":
			dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_vfsc2" };
			break;
			
		case "fca":
			dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_fca" };
			break;

		case "ppau":
			dbArray = new String[] { "prospero_global", "prospero_asic_business" };
			break;

		case "movfsc":
			dbArray = new String[] { "mo_global", "mo_vfsc_business" };
			break;
			
		default:
			System.out.println(Brand + " is not supported。");

		}

		return dbArray;
	}

	/*
	 * public static String[] getDBNameMRegulator(String Brand) {
	 * 
	 * String[] dbArray = new String[] {};
	 * 
	 * switch (Brand) {
	 * case "vt":
	 * 
	 * dbArray = new String[] { "vt_m_regulator_global", "vt_m_regulator_business" };
	 * break;
	 * 
	 * case "pug":
	 * dbArray = new String[] { "pug_m_regulator_global", "pug_m_regulator_business" };
	 * break;
	 * 
	 * case "au":
	 * dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_asic" };
	 * break;
	 * 
	 * case "ky":
	 * dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_cima" };
	 * break;
	 * 
	 * case "vfsc":
	 * dbArray = new String[] { "dev_m_regulator_global", "dev_m_regulator_vfsc" };
	 * break;
	 * 
	 * case "regulator2":
	 * dbArray = new String[] { "db_au_global_vgp", "xxxx" };
	 * break;
	 * 
	 * default:
	 * System.out.println(Brand + " is not supported。");
	 * 
	 * }
	 * 
	 * return dbArray;
	 * }
	 */

	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		Random testr=new Random();
		for (int i = 0; i < length; i++) 
			{
				sb.append(SOURCE.charAt(testr.nextInt(SOURCE.length()))); 
			}
		
		return sb.toString(); 
	}
	
	public static String randomSCString(int length) 
	{ 
		StringBuilder sb = new StringBuilder(length);
		Random testr=new Random();
		for (int i = 0; i < length; i++) 
			{
				sb.append(scSOURCE.charAt(testr.nextInt(scSOURCE.length()))); 
			}
		
		return sb.toString(); 
	}
	
	public static String randomNumber(int length)
	{
		StringBuilder sb=new StringBuilder(length);
		Random testr=new Random();
		for(int i=0;i<length;i++)
		{
			sb.append(numberList.charAt(testr.nextInt(numberList.length())));
		}
		
		return sb.toString();
	}
	
	static void funcLogInAdmin(WebDriver driver,String userName, String passWord, String Brand) throws Exception
	{

		driver.findElement(By.id("exampleInputEmail1")).clear();;
		driver.findElement(By.id("exampleInputEmail1")).sendKeys(userName);
		driver.findElement(By.id("password_login")).clear();
		driver.findElement(By.id("password_login")).sendKeys(passWord);
		driver.findElement(By.id("btnLogin")).click();
		
		Thread.sleep(2000);
		
		if(Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("svg")|| Brand.equalsIgnoreCase("fsa"))
		{
			WebDriverWait wait03=new WebDriverWait(driver, Duration.ofSeconds(3));
			Actions aMenu=new Actions(driver);
		
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.header-profile"))).click();

			aMenu.moveToElement(driver.findElement(By.cssSelector("ul.dropdown-menu.dropdown-menu-right li.dropdown-submenu a"))).build().perform();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//a[contains(text(),'English')]")).click();
			Thread.sleep(1000);
		}
		switch(Brand)
		{
			case "au":
				Brand = "ASIC";
				break;
			case "vfsc":
				Brand = "VFSC";
				break;
			case "vfsc2":
				Brand = "VFSC2";
				break;
			case "fca":
				Brand = "FCA";
				break;
			case "fsa":
				Brand = "FSA";
				break;
			case "svg":
				Brand = "SVG";
				break;
			case "ky":
			default:
				Brand = "CIMA";
		}
		//For new regulator branch
		Thread.sleep(2500);
		if (driver.findElements(By.id("regulation_label")).size()>0) {
			driver.findElement(By.id("regulation_label")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//span[contains(text(),'"+Brand+"')]")).click();
			Thread.sleep(2000);
		}else {
			System.out.println("Single Regulator. No need to switch.");
		}
	}
	
	//Functions
	

	
	static Boolean funcLogOutAdmin(WebDriver driver)
	{
		//Logout Admin
		
		try
		{
			driver.findElement(By.cssSelector("a.header-profer")).click();
			driver.findElement(By.linkText("logout")).click();
			return true;
			
		}catch (NoSuchElementException e)
		{
			return false;
		}
		
	}
	
	static void funcLogInTrader(WebDriver driver, String userName, String passWord, String Brand) throws Exception
	{
		if(Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("svg")|| Brand.equalsIgnoreCase("fsa"))
		{
			Thread.sleep(1000);
			driver.findElement(By.id("language_title")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("language_box")).click();
			Thread.sleep(1000);
		}
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys(userName);
		driver.findElement(By.id("password_login")).clear();
		driver.findElement(By.id("password_login")).sendKeys(passWord);
		
		driver.findElement(By.id("btnSubmit")).click();
		
		//Need to remove after transition phase
	
		
	}
	
	static void funcLogOutTrader(WebDriver driver)
	{
		
		WebDriverWait wait01=new WebDriverWait(driver,Duration.ofSeconds(2));
		wait01.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
		
	}

	public static void funcLogInCP(WebDriver driver, String userName, String passWord, String Brand) throws Exception
	{
		
		String IBTitle = "Secure IB Portal";
		String cpTitle = "Secure Client Portal";
		
		if(Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) {
			
			IBTitle = "IB Portal";
			cpTitle = "Client Portal";
		}
		WebDriverWait wait50 = new WebDriverWait(driver, Duration.ofSeconds(50));
		WebDriverWait wait02=new WebDriverWait(driver,Duration.ofSeconds(2));
		cpLogIn loginCls = new cpLogIn(driver,Brand);
		String url = driver.getCurrentUrl();
		if(!url.contains("login")) {//return,if do not need login
			return;
		}
		funcLogin(driver,  userName,  passWord,  Brand);
		
		Utils.waitUntilLoaded(driver);
		
		//If login account is IB account, default login page is IB portal. Switch back to Client Portal
		if (driver.getTitle().equalsIgnoreCase(IBTitle))
		{
			
			Utils.waitUntilLoaded(driver);
			switch(Brand) {
				case "vt":
				case "fsa":
				case "svg":
					wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.login_inner.el-dropdown-selfdefine"))).click();
					break;
				default:
					wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.user.el-dropdown-selfdefine"))).click();
			}
			Thread.sleep(500);
			//Switch to Client Portal
			driver.findElement(By.cssSelector(".login_back > span")).click();
			wait50.until(ExpectedConditions.titleContains(cpTitle));
		}
		/*
		if(Brand.equalsIgnoreCase("svg")|| Brand.equalsIgnoreCase("fsa"))
		{
			loginCls.getLanguageIcon().click();	
			wait02.until(ExpectedConditions.visibilityOf(loginCls.getLanguageOption("English"))).click();
		}*/

	}
	static void funcLogin(WebDriver driver, String userName, String passWord, String Brand) throws Exception {
		WebDriverWait wait02=new WebDriverWait(driver,Duration.ofSeconds(2));
		Thread.sleep(1000);
		//change language to English
		cpLogIn loginCls = new cpLogIn(driver, Brand);
		loginCls.getLanguageIcon().click();
		wait02.until(ExpectedConditions.visibilityOf(loginCls.getLanguageOption("English"))).click();
		/*
		switch(Brand)
		{
			case "vt":
			case "svg":
			case "fsa":
				loginCls.getLanguageIcon().click();
				wait02.until(ExpectedConditions.visibilityOf(loginCls.getLanguageOption("English"))).click();
		}*/		
	
		//Input Email
		loginCls.getInputEmail().clear();
		loginCls.getInputEmail().sendKeys(userName);
		
		//Input Password
		loginCls.getInputPwd().clear();
		loginCls.getInputPwd().sendKeys(passWord);
		
		//Click Login
		loginCls.getLogINBtn().click();
	
		
		Thread.sleep(1000);
	}
	
	static void funcLogOutCP(WebDriver driver, String Brand) throws Exception
	{
		WebDriverWait wait02=new WebDriverWait(driver,Duration.ofSeconds(2));
		//cpLogIn loginCls = new cpLogIn(driver,Brand);
		cpHeader headCls = new cpHeader(driver,Brand);
		Thread.sleep(1000);
		//change language to English
		
		 
		headCls.getLangSwitchMenu().click();
		wait02.until(ExpectedConditions.visibilityOf(headCls.getLanguage("English"))).click();
		Thread.sleep(1000);
		
		headCls.getCustNameEle().click();
		Thread.sleep(1000);
		headCls.getLogoutEle().click();
	
	}
	
	

	/*
	 * Yanni on 18/09/2019: to log in IB Portal via Client Portal
	 */
	static void funcLogInIP(WebDriver driver, String userName, String passWord, String Brand) throws Exception
	{
		
		WebDriverWait wait50 = new WebDriverWait(driver, Duration.ofSeconds(50));
		//Login Client Portal first and will be redicreted to IB portal if user is IB user	
		funcLogin( driver,  userName,  passWord,  Brand);
		Utils.waitUntilLoaded(driver);
		
		////Switch to English
		ipNavigationBar ipnb = new ipNavigationBar(driver, Brand);
		ipnb.getLanguageList().click();
		//driver.findElement(By.xpath("//li[contains(@class,'fr')]/div/img")).click();
		WebDriverWait wait02=new WebDriverWait(driver,Duration.ofSeconds(2));
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'English')]"))).click();
		Thread.sleep(1000);
		
		Assert.assertEquals(driver.getTitle(), ipnb.getPageTitle());

	}
	
	/*
	 * Yanni on 18/09/2019: to log out IB Portal and goes back to Client Portal
	 */
	static void funcLogOutIP(WebDriver driver) throws Exception
	{
		//Click dropdown to show LOG OUT link
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.login_inner.el-dropdown-selfdefine")).click();

		Thread.sleep(500);

		driver.findElement(By.cssSelector(".logout > span")).click();
	}
	
	static BigDecimal splitAccount(String option) throws Exception
	{
		BigDecimal moneyAmount=new BigDecimal("0.00");
		String subStr;
		String[] strSplit;

		NumberFormat nf=NumberFormat.getInstance();
		Number n;
		
		
		if(option.contains("Commission Account"))
		{

			strSplit=option.split(" ");
			
			//the last second is the money text like $2000
			subStr=strSplit[strSplit.length-2].trim().substring(1);	
				
			
		} else
		{
	
			strSplit=option.split(" ");
			subStr=strSplit[1].substring(2);		

			
		}	
		
		n=nf.parse(subStr); //remove thousand separator
		moneyAmount=new BigDecimal(n.toString());
		
		return moneyAmount;
	}

	public static Boolean isTestIB(String ibName)
	{
		String ibExp="testcrmib[a-zA-Z]{3}";
		Pattern pat=Pattern.compile(ibExp);
		Matcher mat=pat.matcher(ibName);
		if(mat.find())
		{
		
			return true;
		}else
		{
			return false;
		}
		
	}
	
	public static Boolean isJoint(String jointName)
	{
		
		String jointExpr="^testcrma[0-9]{0,3}[a-zA-Z]{3}\\s{0,1}[a-zA-Z]{0,8}\\s{0,1}[a-zA-Z]{0,3}\\s{0,1}&\\s{0,1}testcrma[0-9]{0,3}[a-zA-Z]{3}\\s{0,1}[a-zA-Z]{0,8}\\s{0,1}[a-zA-Z]{0,3}$";
		Pattern pat=Pattern.compile(jointExpr);
		Matcher mat=pat.matcher(jointName);
		if(mat.matches())
		{
			return true;
		}else
		{
			return false;
		}
		
	}
	
	public static Boolean isWebUser(String webName)
	{
		String webExp="^testcrmw[a-z]{3}\\s[a-zA-Z]{0,8}\\s{0,1}[a-zA-Z]{3}";
		String webExpVT = "^[a-zA-Z]{3}\\s[a-zA-Z]{0,8}\\s{0,1}testcrmw[a-z]{3}";
		Pattern pat=Pattern.compile(webExp);
		Matcher mat=pat.matcher(webName);
	
		if(mat.matches() || Pattern.compile(webExpVT).matcher(webName).matches())
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	public static Boolean isAddUser(String addUser)
	{
		String userExp="^testcrma[0-9]{0,3}[a-zA-Z]{3}\\s{0,1}[a-zA-Z]{0,8}\\s{0,1}[a-zA-Z]{0,3}$";
		String userExpVT="^[a-zA-Z]{0,3}\\s[a-zA-Z]{0,8}\\s{0,1}testcrma[0-9]{0,3}[a-zA-Z]{3}$";
		
		Pattern pat=Pattern.compile(userExp);
		
		Matcher mat=pat.matcher(addUser);
		if(mat.find()|| Pattern.compile(userExpVT).matcher(addUser).matches())
		{
			if(!addUser.contains(Utils.addUserPrefix))
			{
				return true;
			}else
			{
				return false;				
			}
		}else
		{
			return false;
		}
		
	}
	
	/*	Choose the account with non-zero balance from a dropdown list.
		e.g. Account selection after clicking "WITHDRAW REBATE"
		Return the whole string with account number and balance
		e.g. 925017 - 1063.72 AUD                                   */
	public static String chooseRebateAccount(WebDriver driver, List<WebElement> menuGroup) throws Exception
	{
		BigDecimal moneyAmount=new BigDecimal("0.00");
		List<WebElement> t;
		WebElement menuSelected;
		Boolean flag=true;
		int j; //Select index
		String option="";
		String[] result;
		
		//After user clicks the rebate account list, a new DIV will be added directly under the Body
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		
		
		//Choose a rebate account with balance greater than 0
		for(int i=0;i<menuGroup.size();i++)
		{
			//If the div is set to display
			if(!menuGroup.get(i).getAttribute("style").contains("display: none"))
			{
				menuSelected = menuGroup.get(i);
				
				t=menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));
				
				
				j=t.size()-1;
					
				while(flag==true && j>=0)
				{
	
					option=t.get(j).findElement(By.tagName("span")).getText();
					result=option.split(" ");
					moneyAmount=new BigDecimal(result[2]);
						
					if (moneyAmount.compareTo(BigDecimal.ZERO)==1)
					{
						flag=false;
						t.get(j).findElement(By.tagName("span")).click();;  //This rebate account has rebate and can be used.
						break;
					}else
					{
						j--;
					}
				}
			}
		}
		return option;
	}
	
	//Login to Admin url and get cookie
	static String getAdminCookie(WebDriver driver,String userName, String passWord, String Brand, String TestEnv) throws Exception
	{

		Utils.funcLogInAdminNoLangSwitch(driver, userName, passWord, Brand);
		
		String result = "";
		//Check if the Verfication Code displays or not
		try
		{			
			
			//Click SendVerification code button and wait for 3 seconds
			//driver.findElement(By.className("btn btn-default send_code")).click();
			driver.findElement(By.xpath(".//div[@id='myModal']//input[@value = 'Get verification code']")).click();
			System.out.println("Getting Verification Code...");
			Thread.sleep(3000);
			
			//Read verification code from DB
			result = getVeriCode(userName, Brand, TestEnv);
			System.out.println("Code is:" + result);
			//Input verification code
			driver.findElement(By.id("code_inp")).sendKeys(result);
			//Click Confirm
			driver.findElement(By.xpath(".//button[contains(text(),'Confirm')]")).click();
			Thread.sleep(1000);
			
		}catch (NoSuchElementException e)
		{
			//Output No Verification Code dialog pops up
			
		}
		//Utils.funcLogInAdmin(driver,userName, passWord, Brand);
		//String text = driver.findElement(By.cssSelector("a.home")).getText();
		//assertEquals("Home",text);
		Set <Cookie> coo = driver.manage().getCookies();

		//System.out.println(coo);
		return coo.toString();

	}
	
	//Login to trader and get cookie
	static String getTraderCookie(WebDriver driver,String userName, String passWord, String Brand) throws Exception
	{

		driver.findElement(By.xpath("//input[@placeholder='Email']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(passWord);
		
		driver.findElement(By.cssSelector("button.el-button")).click();
		Thread.sleep(1000);
		
		//String text = driver.findElement(By.cssSelector("a.home")).getText();
		//assertEquals("Home",text);
		Set <Cookie> coo = driver.manage().getCookies();
		//System.out.println(coo);
		return coo.toString();
		
	}
	

	static String removeASIC(String nameOld)
	{
		String nameNew=nameOld;
		int i=nameOld.indexOf("(");
		if(i >0)
		{
			nameNew=nameOld.substring(0, i);
		}
		return nameNew;
	}
	
	
	//handlePopup should be removed after June 23.
	static void handlePopup(WebDriver driver, boolean flag) throws Exception
	{
		//flag==true: I Agree
		//flag==false: Remind me later->Close
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		try
		{
			if(flag==false)
			{
			
				//Click Remind me later button in the popup dialog
				wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Remind me later']"))).click();
				Thread.sleep(500);
				
				//Click Close button in the popup dialog
				wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Close']"))).click();
				
			} else
			{
				//Click agree button in the popup dialog
				wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='I Agree']"))).click();;
			}
			
		}catch(TimeoutException | NoSuchElementException e)
		{
			System.out.println("The logged in user has no popup dialog.");
			//e.printStackTrace();
		}
				
		
	}
	

	 public static void funcTakeScreenShot(WebDriver webdriver, String fileName) throws Exception{
		 
		 String folderName="ScreenShot";
		 
		 File dir = new File(workingDir+"\\" + folderName);
		 
		 if(!dir.exists())
		 {
			 dir.mkdir();
		 }
		 
		 //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file

         File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination

        File DestFile=new File(dir.getAbsolutePath()+"\\" + fileName + System.currentTimeMillis()+".png");

        //Copy file at destination

        Files.copy(SrcFile, DestFile);
        
            

	    }
	 
	 

	 
	 
	public static Boolean isInternalUser(String webName)
	{
		String internalUserExp="^IU[a-z]{4}$";
		Pattern pat=Pattern.compile(internalUserExp);
		Matcher mat=pat.matcher(webName);
		if(mat.matches())
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	//Parse the given url (AdminURL/TraderURL), remove path and return the remaining part plus /
		public static String ParseInputURL(String url) {
	        
	       String newURL = url;
			
			// Create a URL
	        try {
	          
	        	URL urlObj = new URL(url); 
	        	String path=urlObj.getPath();
	            
	            
	            if(path.equals("/"))
	            {
	            	newURL = url;
	            }else if(path.length()==0)
	            {
	            	newURL = url+"/";
	            }else
	            {
	            	newURL = url.substring(0, url.indexOf(path)+1);
	            }
	             
	        }
	        catch (MalformedURLException e) {
	            System.out.println("Malformed URL: " + e.getMessage());
	        }
	        
	        return newURL;
	         
	    }


		public static String ParseAdminURLtoHost(String url) {

		       String host = "";

				// Create a URL
		        try {

		        	URL urlObj = new URL(url);
		        	host=urlObj.getHost();

		        }
		        catch (MalformedURLException e) {
		            System.out.println("Malformed URL: " + e.getMessage());
		        }

		        return host;

		    }

	public static String ParseURLtoBaseURL(String url) {

		String baseUrl = "";

		// Create a URL
		try {

			URL urlObj = new URL(url);
			baseUrl = urlObj.getProtocol() + "://" + urlObj.getHost();

		}
		catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		}

		return baseUrl;

	}
		
		/*For checking if String a contains string b*/
		public static void funcIsStringContains(String a, String b, String Brand)
		{
			if(a.contains(b))
			{
				System.out.println("Succeed: *** String "+a+" contains "+b+" in Brand "+Brand);
			}else
			{
				System.out.println("Failed: !!! String "+a+" does NOT contains "+b+" in Brand "+Brand);
				Assert.assertTrue(false);
			}
		}
		
		/*For checking if String a equals string b*/
		public static void funcIsStringEquals(String a, String b, String Brand)
		{
			if(a.equals(b))
			{
				System.out.println("Succeed: *** String "+a+" equals to "+b+" in Brand "+Brand);
			}else
			{
				System.out.println("Failed: !!! String "+a+" is NOT equals to "+b+" in Brand "+Brand);
				Assert.assertTrue(false);
			}
		}



		/*
		 * Yanni on 23/08/2019: call Admin API to get MT4 comment with mt4Acc and its dataSourceId.
		 * To call Admin API, you need to provide AdminURL and cookie.
		 */
		
		public static String getMT4Comment(String domainURL, String mt4Acc, String dataSourceId, String cookie) throws org.apache.http.ParseException, IOException, Exception
		{
			
			
			JSONParser parser = new JSONParser();
			JSONObject objAPIResult, objRow;

			
			HashMap<String, String> headerMap= new HashMap<String, String>();
			String apiPath="account/queryTradesClosedListHistory", apiResult;
			String mt4Comment = "";
			
			apiPath=Utils.ParseInputURL(domainURL) + apiPath;
			
			//Get current date in format yyyy-MM-dd. Current date is the end date in search
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String endDate=sdf.format(new Date()); 
			
			
			
			//Move the current date 15 days ahead and set it as the start date in search
			Calendar calInst=Calendar.getInstance();
			
			calInst.setTime(sdf.parse(endDate));
			calInst.add(Calendar.DATE, -15);
			String startDate=sdf.format(calInst.getTime());
			
			//Prepare header
			headerMap.put("Content-Type", "application/json");
			headerMap.put("Accept", "application/json");
			headerMap.put("Cookie", cookie);
			
			//Prepare body
			String body="{\"order\":\"asc\",\"search\":{\"userQuery\":\"\",\"startDate\":\"0000-00-00\",\"endDate\":\"9999-99-99\",\"mt4Account\":\"222222\",\"dataSourceId\":\"66\"}}";
				
			//Populate startDate, endDate, mt4Account and dataSourceID
			body=body.replace("9999-99-99", endDate);
			body=body.replace("0000-00-00", startDate);
			body=body.replace("222222", mt4Acc);
			body=body.replace("66", dataSourceId);
	
	    	apiResult = RestAPI.commonPostAPI(apiPath,headerMap,body);	
			if(apiResult!=null && apiResult.length()>0)
			{
				objAPIResult =(JSONObject)parser.parse(apiResult);
				
				
				JSONArray data = (JSONArray)objAPIResult.get("rows");
				
				if(data.size()>=1)
				{
					objRow=(JSONObject) data.get(0);				
					mt4Comment = objRow.get("comment").toString();
					System.out.println("MT4 Comment found. It is " + mt4Comment + " on Data Source ID " + dataSourceId);
					
				}else
				{
					System.out.println("No Closed History and MT4 Comment found with the account " + mt4Acc + " on Data Source ID " + dataSourceId);
				}
			
			}
			return mt4Comment;
		
		}
		
		/* Yanni on 23/08/2019: verify MT4 comment is the same as requirement
		 * mt4Comment: W20190617A0001:COMMENT, 
		 * isRebateAccount: 0 (Trading Account), 1 (Rebate Account)
		 * isAdminOP: 0 (customer request withdraw via Trader), 1 (Operation cancel request via Admin)
		 */
	
		public static Boolean verifyMT4Comment(String mt4Comment, Boolean isRebateAccount, Boolean isAdminOP)
		{
			Boolean passedCheck = false;
			HashMap<String, String> commentMap = new HashMap<String, String> ();
			String key="";
			
			if(mt4Comment.contains(":"))
			{
			
				String[] splitComment = mt4Comment.split(":");
				mt4Comment = splitComment[1];
			}

			
			commentMap.put("TraderRebate", "R WITHDRAW");
			commentMap.put("TraderTrading", "WITHDRAW");
			commentMap.put("AdminTrading", "Withdrawal Reversal");
			commentMap.put("AdminRebate", "RabateBack Withdrawal Reversal");
			
			if(isAdminOP==true)
			{
				if(isRebateAccount == true)
				{
					key="AdminRebate";
				}else
				{
					key="AdminTrading";
				}
			}else
			{
				if(isRebateAccount == true)
				{
					key="TraderRebate";
				}else
				{
					key="TraderTrading";
				}
			}
			
		
			if(commentMap.get(key).equalsIgnoreCase(mt4Comment))
			{
				passedCheck = true;
				System.out.println( key + " Account MT4 Comment is correct: " + mt4Comment);
			}else
			{
				passedCheck = false;
				System.out.println(key + " Account MT4 Comment is wrong. It is "+ mt4Comment + " but should be " + commentMap.get(key));
			}
			
			return passedCheck;
			
		}
		
		/*
		 * Yanni on 23/08/2019: return DataSourceId by brand. If Brand is "", return all. 
		 */
		public static String[] getDataSourceId(String Brand)
		{
			String[] dataSourceId;
			
			switch(Brand)
			{
		
				case "vt":
				case "vtsvg":
					dataSourceId= new String[] {"10","18","19","58","68","78"};
					break;
				
				case "au":
					dataSourceId= new String[] {"5","8"};
					break;
					
				case "ky":
					dataSourceId= new String[] {"220","200","201","14","15","11","12","5","2","3","8"};
					break;
					
				case "vfsc":
				case "vfsc2":
					dataSourceId= new String[] {"270","260","240","230","220","200","201","14","15","11","12","5","2","3","8"};
					break;
					
				case "fca":
					dataSourceId= new String[] {"17","8"};
					break;
					
				case "svg":
				case "fsa":
					dataSourceId= new String[] {"7","21","20","59"};
					break;
					
				case "ppau":
					dataSourceId= new String[] {"903"};
					break;
					
				case "movfsc":
					dataSourceId= new String[] {"11","900","907"};
					break;
					
				case "um":
					dataSourceId= new String[] {"600"};
					break;
					
				default:
					dataSourceId= new String[] {"11","12","5","2","8","3", "10"};
		
					
			}	
			
			return dataSourceId;
		}
		
		/*For transforming string to MD5 encrypted string*/
		public static String funcMD5Encrypt(String yourTxt) throws UnsupportedEncodingException
		{
			String result="";
			try {
				byte[] bytesOfMessage = yourTxt.getBytes("UTF-8");
	
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(bytesOfMessage);
				
				StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < thedigest.length; ++i) {
		          sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1,3));
		       }
			   result = sb.toString();
			} catch (java.security.NoSuchAlgorithmException e) {
		    }
			
			return result;
		}
		
		
		/*For checking if account is rebate account or trading account*/
		public static String funcIsAccountRebateOrTrading(String url, String cookie, String mt4Acc) throws Exception
		{
			String result = "",group="";
			url = Utils.ParseInputURL(url);

			//Search in Client Database
			result=RestAPI.testPostClientDataSearch(url, mt4Acc, cookie);
			
			if (result.contains("mT4Group")) {
			 	//System.out.println(result);
				JSONParser parser = new JSONParser();
	            JSONObject json = (JSONObject) parser.parse(result);
	            JSONArray data = (JSONArray) json.get("rows");

	            //Limit the print data column to 6 and add is_del column
		        for (Object item : data) {  
		            for (Object key: ((JSONObject) item).keySet()) {
		            	Object value = ((JSONObject) item).get(key.toString());
		            	//Get user_id
		            	if (key.toString().equals("mT4Group")) {
		            		group=value.toString();
		            	}
		            }
		        }    
			}else {
			   	System.out.println("Query account failed!");
			   	//System.out.println(result);
			}
			
			//If searching trading account returns null (not a trading account), search Rebate account
			if(group.contains("REBA"))
			{
				System.out.println("Rebate Acc!");
				return "Rebate";
					
			}else {
				
				System.out.println("Trading Acc!");
				return "Trading";
			}
		}
		
		
		/*Yanni on 26/09/2019: In CP & IP, when dropdown list is clicked, a new div is added to the page. 
		* This function is used to find which dropdown is displayed and return all options (li) in the list.
		* Need to pass driver and  div CSSSelector (the div selector is usually div.el-select-dropdown.el-popper)
		*/
		public static List<WebElement> funcGetListItem(WebDriver driver, String dropDownCSSSelector)
		{
			List<WebElement> fromElement = null;
			
			fromElement=driver.findElements(By.cssSelector(dropDownCSSSelector));
			
			for(int i=0;i<fromElement.size();i++)
			{
				if(!fromElement.get(i).getAttribute("style").contains("display: none"))
				{
					fromElement=fromElement.get(i).findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));
					break;
				}
			}
			
			return fromElement;
		}
		
		
		/*
		 * Developed by Alex.L for checking if account group is test group on 19/07/2019
		 * 
		 */
		public static boolean funcCheckAccoutGroup(String Brand, String TestEnv, String mt4Acc) throws Exception
		{				
			String sampleStr="mock",gStr="group",gDetail="";
			boolean isTestGroup=false;
			String[] auServer= {"db_mt4_vantage_au","db_mt4_vantage_uk","db_mt4_vantage_au2","db_mt4_vantage_uk2","db_mt4_au3","db_mt4_uk3","db_mt4_au4","db_mt4_uk4","mt5_vfx_live"}; 
			String[] vtServer= {"db_mt4_vt_business","db_mt4_vt2","mt5_vfx_live"}; 
			//String[] pugServer = {"db_mt4_pug_business_new","db_mt4_pui","mt5_pug_live","mt5_vfx_live"};
			String[] pugServer = {"db_mt4_pui","mt5_pug_live"};
			String[] mtServerList=auServer;
			
			//System.out.println(url);
			String selectSql="select `GROUP` from mt4_users where login=\'"+mt4Acc+"\';";
			String selectSql2="select `GROUP` from mt5_users where login=\'"+mt4Acc+"\';";		
			
			String actDataSource = "";
			
			//for beta or prod env
			switch(Brand)
			{
	
				case "vt":
					sampleStr="test";
					mtServerList = vtServer;
					break;
					
				case "svg":
				case "fsa":
					sampleStr="test";
					mtServerList = pugServer;
					
					break;
					
				case "au":
				case "ky":
				case "fca":
				case "vfsc":
				case "vfsc2":
				case "regulator2":
					
					sampleStr="test";
					mtServerList = auServer;

					break;
					
				default:
					System.out.println(Brand + " is NOT supported. ");
						
			}
			
			
			for (String dataSource : mtServerList) 
			{
				if (dataSource.contains("mt5")) {
					gDetail = RestAPI.APIReadDB(dataSource,selectSql2,TestEnv);
					
				}else {
					gDetail = RestAPI.APIReadDB(dataSource,selectSql,TestEnv);
					
				}
				if (gDetail.toLowerCase().contains(gStr.toLowerCase())) 
				{
					actDataSource = dataSource;
					break;
				}
			}
			

			//String accDetail = RestAPI.testPostTradeAccountList(traderURL, cookie);
		
			if (gDetail.toLowerCase().contains(sampleStr.toLowerCase())) {
				System.out.println("Pass the group check: AccGroup of mt4 account "+mt4Acc+" is test group: "+gDetail.toString());
				isTestGroup=true;
			} else if(TestEnv.equalsIgnoreCase("test") && actDataSource.contains("mt5"))
			{
				//In Test environment, MT5 is a test server and all groups (even groups without 'test' in names) are testing groups
				System.out.println("Pass the group check in TEST ENV: AccGroup of mt5 account "+mt4Acc+" is test group: "+gDetail.toString());
				isTestGroup=true;
			}else
			{
				System.out.println("Failed the group check: AccGroup of mt4 account "+mt4Acc+" is NOT test group: "+gDetail.toString());
			}
			
			return isTestGroup;

		}
	
		
		/*
		 * Yanni on 18/11/2019: get status of Loading Mask. Return true if loading mask exists, false if loading mask doesn't exists anymore.
		 */
		
		public static boolean getLoadingMaskStatus(WebDriver driver) throws Exception
		{
			boolean existLoading;
			
			Thread.sleep(500);
			
			//If loading mask can be found (no exception), set existLoading to true; 
			try {
				
				driver.findElement(By.cssSelector("div.el-loading-mask.is-fullscreen"));
				existLoading = true;
				
			}catch (NoSuchElementException e)
			{
				existLoading = false;
			}
			
			return existLoading;
		}
		
		
		public static void waitUntilLoaded(WebDriver driver) throws Exception
		{
			boolean existLoading = getLoadingMaskStatus(driver);
			
			while(existLoading == true)
			{
				existLoading = getLoadingMaskStatus(driver);
			}
		}
		
		
	    public static void readEmail(String TraderName, String Brand, String testEnv, int numberOfEmail) throws Exception
	    {
	    	String encodedEmail;
	    	String selectSql = "select subject, template_invoke_name, create_time from tb_mail_send_log where to_mail = 'MAILMAIL' order by create_time desc limit XNO";
	    	String processedSql;
	    	String dbName="";
	    		    		    	
	    	encodedEmail = DecryptUtils.encode(TraderName);
	    	processedSql = selectSql.replace("MAILMAIL", encodedEmail);
	    	
	    	processedSql=processedSql.replace("XNO", Integer.toString(numberOfEmail));
	    	
	    	System.out.println(processedSql);
	    	
	    	dbName = Utils.getDBName(Brand)[1]; 
    	
	    	DBUtils.funcreadDB(dbName, processedSql, testEnv);
	    	
	    	
	    }
	    

	    
	    /*Added by Alex.L for resetting user CP/trader login password to 123Qwe*/
	    public static void funcResetUserLoginPW(WebDriver driver, String AdminURL, String TraderName, String AdminName,String AdminPass,String Brand, String TestEnv) throws Exception
	    {
	    	//Get admin cookie thru API in test env or browser in beta/prod env
			String url = Utils.ParseInputURL(AdminURL);
		
			driver.get(AdminURL);		
			String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);
			Thread.sleep(1000);
			//Encrypt the trader email (for modify email and passwd API)
			String encrypted_email1 = (String) ((JavascriptExecutor)driver).executeScript("return commonEncryptedString('"+TraderName+"test');");
			String encrypted_email2 = (String) ((JavascriptExecutor)driver).executeScript("return commonEncryptedString('"+TraderName+"');");
			//System.out.println(encrypted_email2);
			driver.close();
			Thread.sleep(500);		
					
			//Query user id by user name
			System.out.println(TraderName.substring(0,TraderName.indexOf("@")));
			String id = RestAPI.testQueryUserIdByName(url, cookie, TraderName.substring(0,TraderName.indexOf("@")));
			//System.out.println("id : "+id);
			
			//reset login pw to 123Qwe
			RestAPI.testPostResetLoginPassword(url, cookie, encrypted_email1, id);
			RestAPI.testPostResetLoginPassword(url, cookie, encrypted_email2, id);
	    }
	    
	    public static Boolean isTestUser(String userName)
	    {
	    	Boolean flag= false;
	    	
	    	flag = isWebUser(userName)||isAddUser(userName) || isJoint(userName) || isTestIB(userName);
	    	
	    	return flag;
	    }
	    
	    public static Boolean isTestcrmUser(String userName)
	    {
	    	Boolean flag= false;
	    	
	    	if(userName.toLowerCase().contains("testcrm"))
	    		flag = true;
	    	
	    	return flag;
	    }
	    
	    public static Boolean isTestEmail(String userEmail)
	    {
	    	
	    	if(userEmail.contains("@test.com"))
			{
				return true;
			}else
			{
				return false;				
			}
	    	
	    }
	    
	    public static BigDecimal getWDfee(String Brand, String withdrawType)
	    {
	    	BigDecimal fee = new BigDecimal("0.00");
	    	
	    	switch(Brand.toLowerCase())
	    	{
	    	case "vt":	    	
	    		fee = new BigDecimal("20.00");
	    		break;
	    		
	    	case "svg":
	    	case "fsa":
	    		if(!withdrawType.equalsIgnoreCase(WithdrawM.Malay.getName()))
	    		{
	    			fee = new BigDecimal("20.00");
	    		}else
	    		{
	    			fee = new BigDecimal("0.00");
	    		}
	    		break;
	    		
	    	case "au":
	    	case "ky":
	    	case "fca":
	    	case "vfsc":
	    	case "vfsc2":
	    	case "regulator2":
	    		fee = new BigDecimal("0.00");
	    		break;
	    		
	    		default:
	    			System.out.println(Brand + " is not supported in Getting Withdraw Fee function getWDfee(String Brand)");
	    		
	    	}
	    	
	    	return fee;
	    }

		static void funcMobileLogInCP(WebDriver driver, String phoneCode, String phoneNumber, String passWord, String Brand) throws Exception
		{
			
			String IBTitle = "Secure IB Portal";
			String cpTitle = "Secure Client Portal";
			WebDriverWait wait50 = new WebDriverWait(driver, Duration.ofSeconds(50));
			
			Thread.sleep(1000);
			//change language to English
			if (Brand.equalsIgnoreCase("svg")||Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("vt"))
			{
				driver.findElement(By.xpath("//li[contains(@class,'flag')]/div/img")).click();
				WebDriverWait wait02=new WebDriverWait(driver,Duration.ofSeconds(2));
				wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'English')]"))).click();
			}
			
			driver.findElement(By.xpath("//input[@placeholder='Country']")).clear();
			driver.findElement(By.xpath("//input[@placeholder='Country']")).sendKeys(phoneCode);
			driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).click();
			driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).clear();
			driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys(phoneNumber);
			
			driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
			driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(passWord);			
			
			//click login button
			driver.findElement(By.cssSelector("button.el-button.btn_red.el-button--default")).click();
			Thread.sleep(1000);
						
			//If login account is IB account, default login page is IB portal. Switch back to Client Portal
			if (driver.getTitle().equalsIgnoreCase(IBTitle))
			{
				
				Utils.waitUntilLoaded(driver);
				wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.login_inner.el-dropdown-selfdefine"))).click();
				
				//Switch to Client Portal
				driver.findElement(By.xpath(".//span[text()='BACK TO CLIENT PORTAL']")).click();
				wait50.until(ExpectedConditions.titleContains(cpTitle));
			}
		
		}

		static void funcLogInAdminNoLangSwitch(WebDriver driver,String userName, String passWord, String Brand) throws Exception
		{
		
			driver.findElement(By.id("exampleInputEmail1")).clear();;
			driver.findElement(By.id("exampleInputEmail1")).sendKeys(userName);
			driver.findElement(By.id("password_login")).clear();
			driver.findElement(By.id("password_login")).sendKeys(passWord);
			driver.findElement(By.id("btnLogin")).click();
			
			Thread.sleep(2000);		
			
			switch(Brand)
			{
				case "au":
					Brand = "ASIC";
					break;
				case "vfsc":
					Brand = "VFSC";
					break;
				case "vfsc2":
					Brand = "VFSC2";
					break;
				case "fca":
					Brand = "FCA";
					break;
				case "fsa":
					Brand = "FSA";
					break;
				case "svg":
					Brand = "SVG";
					break;
				case "ky":
				default:
					Brand = "CIMA";
			}
			//For new regulator branch
			Thread.sleep(2500);
			if (driver.findElements(By.id("regulation_label")).size()>0) {
				driver.findElement(By.id("regulation_label")).click();
				Thread.sleep(500);
				driver.findElement(By.xpath("//span[contains(text(),'"+Brand+"')]")).click();
				Thread.sleep(2000);
			}else {
				System.out.println("Single Regulator. No need to switch.");
			}
			
		}
	    
	    
		/*Developed by Yanni on 9/9/2020: to read email from DB and return verification code. 
		 * Designed for AdminAPITest.java _>AdminLogIn function
		 */
		public static String getVeriCode(String AdminName, String Brand, String TestEnv) throws Exception
	    {
	    	String loginCode="";
	    	String result;
	    	String dbName;
	    	String selectSql;
	    	
	    	dbName =Utils.getDBName(Brand)[1];
	    	
	       	selectSql = "SELECT vars FROM tb_mail_send_log where vars like '%code%' and vars like '%" + AdminName + "%' order by create_time desc limit 1;";
	    	
	    	result =  DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);
	    	
	    	/*
	    	 * Beta env return the following result:
	    	 *  {vars={"CODE":"957626","domain_traderSite":"https://secure.vantagefx.com","domain_adminSite":"https://admin.vantagefx.com","to":"H4xGgDzTnoCf32EC7GRpyb+JPzlAQBBE","NAME":"Test CRM","domain_webSite":"https://www.vantagefx.com"}}
	    	 */
	    	
	    	 /*
	    	  * Test env return the following result:
	    	  * [vars:{"CODE":"957626","domain_traderSite":"https://secure.vantagefx.com","domain_adminSite":"https://admin.vantagefx.com","to":"H4xGgDzTnoCf32EC7GRpyb+JPzlAQBBE","NAME":"Test CRM","domain_webSite":"https://www.vantagefx.com"}]
	    	  */
	    	
	    	//Remove extra words/symbol to make it a JSONOBJECT
	    	switch(TestEnv.toLowerCase())
	    	{
	    	case "test":
		    	result = result.replace("[vars:" , "").trim();
		    	result = result.replace("]" , "").trim();
	    		break;
	    		
	    	case "beta":
	    	case "prod":
	    		result = result.substring(1, result.length()-1);
		    	result = result.replace("vars=" , "").trim();
		    	
	    		break;
	    		
	    		default:
	    			System.out.println("TestEnv is not supported: " + TestEnv);
	    	}

	    	//System.out.println(result);
	    	
	    	JSONObject jsonResult = new JSONObject();
	    	JSONParser parser = new JSONParser();
	    	
	    	jsonResult = (JSONObject) parser.parse(result);
	    	
	    	loginCode = jsonResult.get((Object)"CODE").toString();
	    	
	   	 	
	    	//System.out.println("Verification code is:" + loginCode);
	    	return loginCode;
	    }
		
		//By Yanni on 15/10/2020: to convert regulator name to regulator code.
		static String getRegulatorCode(String regulatorName, String Brand)
		{
			String regulatorCode = "";
			
			
	
			switch(regulatorName.toUpperCase())
			{
				case "CIMA":
					switch(Brand.toLowerCase())
					{
						case "ky":
							regulatorCode = "ky";
							break;
						/*
						 * case "pug":
						 * regulatorCode = "pug";
						 * break;
						 */
						case "vt":
							regulatorCode = "vt";
							break;
					}

					break;
					
				case "ASIC":
					regulatorCode = "au";
					break;
					
					default:
						regulatorCode = regulatorName.toLowerCase();
						
			}
			return regulatorCode;
		}
		
		
		public static void funcVerifyHomePageLiveAccounts(String Brand,WebDriverWait wait01)
		{
			//Yanni: add VT support.
			if(Brand.equalsIgnoreCase("vt"))
			{
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Live accounts')]")));
			}else if(Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg"))
			{
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'LIVE ACCOUNTS')]")));
			}else
			{
			    //Wait for au loading spinner disappear
				wait01.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("i.client-portal-loading-au")));
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'LIVE ACCOUNTS')]")));
			}
		}
		
		
		public static WebDriver funcSetupDriver(WebDriver driver, String browserName)
		{
			switch (browserName.toLowerCase())
			{
			case "chrome":
			
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-sandbox");
				//deprecated
				//options.setExperimentalOption("useAutomationExtension", false);
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
			driver.manage().window().maximize();		 
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
			
			
			return driver;
		}
		
		//add headless option for chrome
		//overload
		public static WebDriver funcSetupDriver(WebDriver driver, String browserName,String headless)
		{
			switch (browserName.toLowerCase())
			{
			case "chrome":

				//WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();

				// Basic performance flags
				options.addArguments("--window-size=1920,1080");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-extensions");
				options.addArguments("--disable-gpu");
				options.addArguments("--disable-dev-shm-usage");

				String chromeBin = System.getProperty("chrome.binary", "");
				if (!chromeBin.isEmpty()) {
					System.out.println("Setting chrome-bin to " + chromeBin);
					options.setBinary(chromeBin);
				}

				//options.addArguments("incognito");
				//deprecated
				//options.setExperimentalOption("useAutomationExtension", false);
				
				//rebranding, ddos protected
				options.addArguments("--disable-blink-features=AutomationControlled");//rebranding
				
				//if selenium is blocked by cloudflare, need change the value of the user-agent
				options.addArguments("user-agent=Mozilla/5.0 CRM-Automation");

				//hashmap for do not pop up the remember password dialog
				HashMap<String,Object> pre = new HashMap<>();
				pre.put("credentials_enable_service", false);
				pre.put("profile.password_manager_enabled", false);

				// for download in headless mode
				if (DownloadFile.tempDirPath != null) {
					pre.put("download.default_directory", DownloadFile.tempDirPath.toAbsolutePath().toString());
					pre.put("download.prompt_for_download", false);
					pre.put("download.directory_upgrade", true);
					pre.put("safebrowsing.enabled", true); // Required in headless sometimes
				}

				options.setExperimentalOption("prefs", pre);

				// Remove automation banner
//				options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
				
				//log
				/*
				String logfile = Utils.workingDir +"\\ExtentReports\\log.json";
				options.addArguments("--log-level=0");
				options.addArguments("--log-net-log="+logfile);
				*/
				
				HashMap<String,Object> perflog = new HashMap<>();
				//perflog.put("traceCategories", "browser,devtools.timeline,devtools");
				perflog.put("traceCategories","blink.console,disabled-by-default-devtools.timeline");
				perflog.put("enableNetwork", false);
				perflog.put("enablePage", true);
				options.setExperimentalOption("perfLoggingPrefs", perflog);

				LoggingPreferences logPrefs = new LoggingPreferences();
	            logPrefs.enable(LogType.BROWSER, Level.ALL);
	            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
	            options.setCapability("goog:loggingPrefs", logPrefs);

				// Enable headless if requested
				if (Boolean.parseBoolean(headless.trim())) {
					options.addArguments("--headless=new");
				}
				
//				if(Boolean.valueOf(headless.trim())) {
//					//options.addArguments("window-size=1920,1080");
//					options.addArguments("headless");
//					//options.addArguments("--remote-debugging-port=9222");
//					options.addArguments("--disable-gpu");
//				}
				
			
				/*
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				//cap.setCapability(ChromeOptions.CAPABILITY, options);
				LoggingPreferences logPrefs = new LoggingPreferences();
	            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
	            //options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
	            cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
	            options.merge(cap);*/
	            
				//Passing a Capabilities object to the ChromeDriver() constructor is deprecated
	            
	            
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
			driver.manage().window().maximize();		 
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			
			
			return driver;
		}
		

		//Added by AlexL 10/10/2022 for reading account group from json file according to brand and currency
		public static String readGroupNamefromJson(String dataSourceId, String brand, String currency) throws FileNotFoundException, IOException, ParseException 
		{
			JSONParser parser = new JSONParser();
			String groupName = "";
	        String fileName = Utils.workingDir + "/src/main/resources/vantagecrm/Data/JsonFiles/DSGroupsCollection.json";
	        
	        JSONArray a = (JSONArray) parser.parse(new FileReader(fileName));

	        for (Object o : a)
	        {
	          JSONObject ds = (JSONObject) o;

	          //Find the json object that matches the dataSourceId
	          if (ds.get("dataSourceId").equals(dataSourceId)) {
	        	  
	        	  JSONArray data = (JSONArray) ds.get("data");

	              for (Object object : data) {
	            	  
						  JSONObject ds_1 = (JSONObject) object;
						  
						  JSONArray b_data = (JSONArray) ds_1.get("regulator");
						 
			              for (Object b : b_data) {

						       //Find out the group name matched the currency if brands in data contains brand
						       if ( b.toString().equals(brand)) {
						    	   
						    	   if ((String) ds_1.get(currency)!= null) {
						    		   groupName = (String) ds_1.get(currency);
						    	   }else {
						    		   //For those not existed currency group, use USD as default
						    		   groupName = (String) ds_1.get("USD");
						    	   }
						    		   
						    	   System.out.println("groupName is: "+groupName);
						    	   
						       }
						  }
	            	  	  
	            	  
	              }
	          }

	         
	        }
	        
	        
	        
			return groupName;
			
			
		
		}
		
		//Return the 'Test CRM' user ID of different brands
		public static String getTestCRMUserId(String Brand)
		{
			String userId = "";
			
			switch(Brand)
			{
		
				case "vt":
				case "vtsvg":
					userId= "80562";
					break;
				
				case "au":
				case "ky":
				case "vfsc":
				case "vfsc2":
				case "fca":
					userId= "211943";
					break;
					
				case "svg":
				case "fsa":
					userId= "110140";
					break;
					
				case "ppau":
					userId= "830135";
					break;
					
				case "movfsc":
					userId= "56717";
					break;
					
				case "um":
					userId= "830429";
					break;
					
				default:
					userId= "0";
		
					
			}	
			
			return userId;
		}

}



