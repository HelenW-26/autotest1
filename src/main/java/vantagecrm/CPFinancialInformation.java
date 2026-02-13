package vantagecrm;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

/*
 * This class is to test all Deposit types in CP
 */

public class CPFinancialInformation {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	Select t;  //Account Dropdown
	Random r=new Random(); 
	int j; //Select index
	WebDriverWait wait01;
	WebDriverWait wait02;


	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);
//    	  driver = new ChromeDriver();	  
    	  context.setAttribute("driver", driver);
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver,Duration.ofSeconds(20));
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
 		
  	  //Login AU CP
		driver.get(TraderURL);

		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);	
		Thread.sleep(waitIndex*2000);
		
		Assert.assertTrue(driver.findElement(By.cssSelector("h3")).getText().equals("LIVE ACCOUNTS"));
		
		
	}
	
	
	
	
	
	/*
	 * Developed by Alex.L for validating the card adding history on 03/10/2019
	 */
	public static void funcValidateCardHistory(WebDriver driver, String type, String Brand) throws Exception
	{
		
		String rDate,rType,rStatus;
		
		//Get current date
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	String dateFormatted= dateFormat.format(new Date());
    	
		//check the date
		rDate = driver.findElement(By.xpath("//tr[1]//td[1]//div[1]")).getText();
		Utils.funcIsStringEquals(rDate, dateFormatted, Brand);
		
		//check the TYPE
		rType = driver.findElement(By.xpath("//tr[1]//td[2]//div[1]")).getText();
		Utils.funcIsStringEquals(rType, type, Brand);
		
		//check the amount
		rStatus = driver.findElement(By.xpath("//tr[1]//td[3]//div[1]")).getText();
		Utils.funcIsStringEquals(rStatus, "Submitted", Brand);
			
	 
    }
	


	@Test(priority=1,alwaysRun=true)
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	void AddFinancialInfoCC(String TraderURL,String TestEnv,String Brand) throws Exception
	{
	
		String urlbase,displayName="",cookie="";
		
		driver.navigate().to(TraderURL);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//li[4]//span[contains(text(),'FINANCIAL INFORMATION')]")).click();
		Thread.sleep(1000);
		
		//Select the payment method
		driver.findElement(By.xpath("//div[@class='el-input el-input--suffix']")).click();
		Thread.sleep(500);
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li//span[contains(text(),'Credit Card')]"))).click();
		
		//Get username from profile API
		urlbase = Utils.ParseInputURL(TraderURL);
		cookie = driver.manage().getCookies().toString();
		displayName = funcCPAPIQueryUsername(urlbase, cookie, TestEnv);
		//System.out.println(displayName);
		
		//Validate the username displayed 
		WebElement cName = driver.findElement(By.cssSelector("div.nameCard div.el-input.is-disabled > input"));
		Object value = ((JavascriptExecutor) driver).executeScript("return arguments[0].value", cName);
		Utils.funcIsStringEquals(value.toString(), displayName, Brand);
		
		//Input card number
		driver.findElement(By.xpath("//input[@placeholder='first six']")).sendKeys(Utils.randomNumber(6));
		driver.findElement(By.xpath("//input[@placeholder='last four']")).sendKeys(Utils.randomNumber(4));
		
		//Input Expiry Date
		driver.findElement(By.xpath("//input[@placeholder='Month']")).click();
		Thread.sleep(500);
		
		WebElement webElement = driver.findElement(By.xpath("//li//span[contains(text(),'12')]"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();
		
		driver.findElement(By.xpath("//input[@placeholder='Year']")).click();
		Thread.sleep(500);
		webElement = driver.findElement(By.xpath("//li//span[contains(text(),'2020')]"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();
		
		//Input card photo
		driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir+"\\proof.png");
		Thread.sleep(500);
		
	
		driver.findElement(By.xpath("//span[contains(text(),'SUBMIT')]")).click();
		Thread.sleep(1000);
		
		//Confirm
		wait01.until(ExpectedConditions.visibilityOfElementLocated((By.linkText("BACK TO HOME PAGE")))).click();
		System.out.println("Add CC Card info for " + displayName + "!");
		
		//Verify the card adding history
		Thread.sleep(1000);
		driver.findElement(By.xpath("//li[4]//span[contains(text(),'FINANCIAL INFORMATION')]")).click();
		Thread.sleep(1000);
		funcValidateCardHistory(driver, "credit card", Brand);
		
		//Check in db
		String selectSql = "select user_id, card_holder_name,card_begin_six_digits,card_last_four_digits, expiry_month,expiry_year,is_del from tb_credit_card where card_holder_name = \'"+displayName+"\' order by create_time desc limit 1;";
		//System.out.println(selectSql);

		String dbName = Utils.getDBName(Brand)[1];
		DBUtils.funcreadDB(dbName,selectSql, TestEnv);		
		
	}
	
	//Get user's realname from Profile API
	public static String funcCPAPIQueryUsername(String urlbase, String cookie, String TestEnv) throws Exception
	{
		System.out.println("\n===========>>>Entering funcCPAPIQueryUsername");
		String body="", username="";
		String url = urlbase.concat("cp/api/profile/info");
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
        body = "userId=-1"; 
			
		String result = RestAPI.commonPostAPI(url,header,body);

		username = funcParseResult(result, TestEnv, "name");
		return username;
	}
	
	public static String funcParseResult(String result, String TestEnv, String item) throws Exception
	{
		String value="";
		String[] entry=null;
		//parse the result and get the column we need
		if(result.contains(item))
        result = result.substring(result.indexOf(item),result.length()); 
        String[] b=result.split(",");
        
        if(!TestEnv.equalsIgnoreCase("test")) {
        	value = b[0].substring(b[0].indexOf("=")+1,b[0].length()).trim();    
		}else {
			value = b[0].substring(b[0].indexOf(":")+1,b[0].length()).trim();       
		}
        
        if(value.contains("\"")) {
        	value = value.replace("\"", "");
        }
        //System.out.println("\n"+ "Result converted is: " + value);

		
		return value;
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
