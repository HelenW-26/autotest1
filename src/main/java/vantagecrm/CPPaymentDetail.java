package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * This class is to test all Deposit types in CP
 */

public class CPPaymentDetail {
	
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
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		 WebDriverManager.chromedriver().setup();
		 driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
    	  context.setAttribute("driver", driver);
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
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
		Utils.waitUntilLoaded(driver);
		
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);
		
		
	}
	
	
	
	//Add UnionPay(CPS) Card info. Now only VT & PUG have this function
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)	
	@Parameters(value= {"TraderURL","TestEnv","Brand"})
	void AddPaymentDetailCC(String TraderURL,String TestEnv,String Brand) throws Exception
	{
	
		String urlbase,displayName="",cookie="";
		
		driver.navigate().to(TraderURL);
		Thread.sleep(1000);
		
		//Check whether the current page is HOME PAGE by checking Live Accounts label exists or not: 
		Utils.funcVerifyHomePageLiveAccounts(Brand,wait02);
		
		go2PaymentDetails(Brand);
		
		//Select the payment method
		wait02.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='el-input el-input--suffix']"))).click();
		//driver.findElement(By.xpath("//div[@class='el-input el-input--suffix']")).click();
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
		driver.findElement(By.xpath("//input[@placeholder='First Six']")).sendKeys(Utils.randomNumber(6));
		driver.findElement(By.xpath("//input[@placeholder='Last Four']")).sendKeys(Utils.randomNumber(4));
		
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
		//wait01.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[@class='el-button btn_blue']")))).click();
		funcBackToHomePage(Brand);
		System.out.println("Add CC Card info for " + displayName + "!");
		
		//Verify the card adding history
		Thread.sleep(1000);
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait02);
		
		Thread.sleep(1500);
		//driver.findElement(By.xpath("//span[contains(text(),'FUNDS')]")).click();
		//Thread.sleep(500);
		driver.findElement(By.xpath("//li[contains(text(),'PAYMENT DETAILS')]")).click();
		Thread.sleep(1000);
		funcValidateCardHistory(driver, "Credit Card", Brand);
		
		//Check in db
		String selectSql = "select user_id, card_holder_name,card_begin_six_digits,card_last_four_digits, expiry_month,expiry_year,is_del from tb_credit_card where card_holder_name = \'"+displayName+"\' order by create_time desc limit 1;";
		//System.out.println(selectSql);

		String dbName = Utils.getDBName(Brand)[1];
		DBUtils.funcreadDB(dbName,selectSql, TestEnv);
			
	}


	//Submit UnionPay(CPS) withdraw request
	@Test(dependsOnMethods="CPLogIn",alwaysRun=true)
	@Parameters(value= {"TraderURL","TestEnv","Brand","TraderName"})
	void AddUnionPayWithdrawDetail(String TraderURL,String testEnv, String Brand, String TraderName) throws Exception
	{	
		
		Select  t;
		Random r = new Random();
		int index = 0;
		WebElement webElement;
		
		driver.navigate().to(TraderURL);
		Thread.sleep(1000);
		
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait02);
		go2PaymentDetails(Brand);
		Thread.sleep(1500);		
		
		//Click payment method element
		if(Brand.equalsIgnoreCase("vt"))
		{
			//wait02.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='el-input__inner']"))).click();
			driver.findElement(By.xpath("//input[@class='el-input__inner' and @placeholder='Select']")).click();
		}else
		{
			wait02.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='el-input el-input--suffix']"))).click();
		}		
	
		Thread.sleep(500);
		
		//Select UnionPay
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li//span[contains(text(),'UnionPay')]"))).click();
		payMethodForm(Brand);
		Thread.sleep(500);
		
		//Submit
		if(Brand.equalsIgnoreCase("vt"))
		{
			driver.findElement(By.xpath("//span[contains(text(), 'Submit')]")).click();
		}else
		{
			driver.findElement(By.cssSelector("button.el-button.btn_blue.el-button--primary")).click();
		}
		
		Thread.sleep(1000);
		
		//Assert success
		
		if(Brand.equalsIgnoreCase("vt"))
		{
			Assert.assertEquals(driver.findElement(By.cssSelector("div.success_content div.main")).getText(),"Union Pay Card details successfully submitted, your new bank details will be available once it gets approved");
		}else
		{
			Assert.assertEquals(driver.findElement(By.cssSelector("p.success_info")).getText(),"Union Pay Card details successfully submitted, your new bank details will be available once it gets approved");
		}
		
		funcBackToHomePage(Brand);
		
		//Verify it's in home page now
		Thread.sleep(1000);
		Utils.funcVerifyHomePageLiveAccounts(Brand, wait01);
		
		Thread.sleep(1500);
		if(Brand.equalsIgnoreCase("vt"))
		{
			driver.findElement(By.xpath("//li[contains(text(),'Payment details')]")).click();
		}else
		{
			driver.findElement(By.xpath("//li[contains(text(),'PAYMENT DETAILS')]")).click();
		}
		
		Thread.sleep(1000);
		funcValidateCardHistory(driver, "UnionPay", Brand);
				
		//Check in db
		String uname = TraderName.substring(0, TraderName.indexOf("@"));
		String selectSql = "SELECT user.real_name, card.bank_name,card.card_number,card.branch_name,card.card_holder_name, card.national_id_card,card.is_del FROM tb_cps_union_card card join tb_user user on card.user_id=user.id  where user.real_name like '%"+uname+"%' order by card.create_time desc limit 1;";
		//System.out.println(selectSql);

		String dbName = Utils.getDBName(Brand)[1];
		DBUtils.funcreadDB(dbName,selectSql, testEnv);

	}
	
	
	//Update Pending UnionPay Withdraw Details record in trader
	@Test(dependsOnMethods = "CPLogIn", alwaysRun= true)
	@Parameters(value="TraderURL")
	public void UPWdPendingEdit(String TraderURL) throws Exception
	{
		String status = "Pending";
		funcUpdateUPDetails(TraderURL, status);
	}

	//Update Submitted UnionPay Withdraw Details record in Trader, 
	@Test(dependsOnMethods = "CPLogIn", alwaysRun= true)
	@Parameters(value="TraderURL")
	public void UPWdSubmittedEdit(String TraderURL) throws Exception
	{
		String status = "Submitted";
		funcUpdateUPDetails(TraderURL, status);
	}

	/*
	 * Developed by Alex.L for validating the card adding history on 03/10/2019
	 */
	public static void funcValidateCardHistory(WebDriver driver, String type, String Brand) throws Exception
	{
		
		String rDate,rType,rStatus;
		SimpleDateFormat dateFormat;
		
		//Get current date
		if(Brand.equalsIgnoreCase("vt"))
		{
			dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		}else
		{
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		}
		String dateFormatted= dateFormat.format(new Date());
		
		//check the date
		rDate = driver.findElement(By.xpath("//tr[1]//td[1]//div[1]")).getText();
		Utils.funcIsStringEquals(rDate, dateFormatted, Brand);
		
		//check the TYPE
		rType = driver.findElement(By.xpath("//tr[1]//td[2]//div[1]")).getText();
		Utils.funcIsStringEquals(rType, type, Brand);
		
		//check the Status
		rStatus = driver.findElement(By.xpath("//tr[1]//td[4]//div[1]")).getText();
		Utils.funcIsStringEquals(rStatus, "Submitted", Brand);
			
	
	}

	/*
	 * For CPS withdraw verification 
	 */
	public void funcCPSWithdrawVerification(String Brand, String TestEnv, String account) throws Exception
	{				
		String[] entry=null;
		BigDecimal fee=new BigDecimal("0.00"),actual_amount = new BigDecimal("0.00"),settlement_rate=new BigDecimal("0.00"),withdraw_amount = new BigDecimal("0.00"),settlement_amount=new BigDecimal("0.00");
		String csp_order_no="",is_equal_over_10k = "",result="",ibExp="";
        String selectSql="SELECT cps_order_no,fee,actual_amount,settlement_rate,is_equal_over_10k,withdraw_amount,settlement_amount FROM tb_payment_withdraw_cps where mt4Account_no = \'"+ account +"\' order by create_time desc limit 1;";			   
        String dbName = Utils.getDBName(Brand)[1];
        result = DBUtils.funcReadDBReturnAll(dbName,selectSql, TestEnv);

		//parse the result and get the column we need
		result = result.substring(1, result.length()-1); 
		String[] b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			//Get the cps_order_no
		    if (entry[0].trim().equals("cps_order_no")) {
		    	csp_order_no = entry[1].trim();
		
		    }
		    
		    //Get the fee
		    if (entry[0].trim().equals("fee")) {
		    	fee = new BigDecimal(entry[1].trim());
		    }
		    
		    //Get the actual_amount
		    if (entry[0].trim().equals("actual_amount")) {
		    	actual_amount = new BigDecimal(entry[1].trim());
		    }
		    
		    //Get the settlement_rate
		    if (entry[0].trim().equals("settlement_rate")) {
		    	settlement_rate = new BigDecimal(entry[1].trim());
	
		    }
		    
		    //Get the is_equal_over_10k
		    if (entry[0].trim().equals("is_equal_over_10k")) {
		    	try {
		    	    is_equal_over_10k = entry[1].trim();
		    	}catch (ArrayIndexOutOfBoundsException e) {
		    		is_equal_over_10k = "0";
		    	}
	
		    }
		    
		    //Get the withdraw_amount withdraw_amount
		    if (entry[0].trim().equals("withdraw_amount")) {
		    	withdraw_amount = new BigDecimal(entry[1].trim());
		
		    }
		    
		    //Get the settlement_amount
		    if (entry[0].trim().equals("settlement_amount")) {
		    	settlement_amount = new BigDecimal(entry[1].trim());
	
		    }
		}
		
		//Verify is_equal_over_10k equals 1 when withdraw_amount is greater than 10000
		
		System.out.println("withdraw amount is: " + withdraw_amount);
		if (withdraw_amount.compareTo(new BigDecimal("10000.00"))>=0) {
			Assert.assertEquals(is_equal_over_10k, "1");
			System.out.println("Verify is_equal_over_10k pass!");
		}else {
			Assert.assertEquals(is_equal_over_10k, "0");
			System.out.println("Verify is_equal_over_10k pass!");
		}
		
		//Verify the actual_amount
		if (withdraw_amount.subtract(fee).compareTo(actual_amount)!=0) {
			Assert.assertTrue(false, "actual_amount "+actual_amount+" not correct! Actual amount is: "+withdraw_amount.subtract(fee));
		}else {
			System.out.println("Verify actual_amount pass!");
		}
		
		//Verify the settlement_amount
		if (actual_amount.multiply(settlement_rate).setScale(2, BigDecimal.ROUND_DOWN).compareTo(settlement_amount)!=0) {
			Assert.assertTrue(false, "settlement_amount "+settlement_amount+" not correct! Actual amount is: "+actual_amount.multiply(settlement_rate).setScale(2, BigDecimal.ROUND_DOWN));
		}else {
			System.out.println("Verify settlement_amount pass!");
		}
		
		//Verify the csp_order_no
		String order_no = csp_order_no.substring(csp_order_no.indexOf("A"),csp_order_no.length());
		//System.out.println(order_no);
		
		switch(Brand)
		{
			case "au":
				ibExp="A[0-9]{9}$";
				break;
				
			case "ky":
			case "vfsc":
			case "vfsc2":				
			//case "fca": no CPS
			case "regulator2":
				ibExp="A[0-9]{10}$";
				break;	
				
			case "vt":
				ibExp="A[0-9]{8}$";
				break;	
				
			case "fsa":
			case "svg":
			default:
				ibExp="A[0-9]{11}$";
				break;				
		}
		Pattern pat=Pattern.compile(ibExp);
		Matcher mat=pat.matcher(order_no);
		if(mat.find())
		{
		    System.out.println("Verify cps_order_no pass! "+csp_order_no+" is a correct "+Brand+" order number");
		}else
		{
			Assert.assertTrue(false, "cps_order_no "+csp_order_no+" not in correct pattern for "+Brand);
		}
	    
	}	
	
	/*
	 * Update Withdraw Details record when the record is in Submitted/Pending status
	 * status = Submitted / Pending
	 */

	public void funcUpdateUPDetails(String TraderURL, String status) throws Exception
	{
		
		List<WebElement> trs;
		int i=0;
		WebElement e;
		
		//Navigate to Withdraw Details History page
		driver.navigate().to(TraderURL);
		
		
		e= driver.findElement(By.linkText("WITHDRAW"));
		Actions a = new Actions(driver);
		a.moveToElement(e).perform();
		Thread.sleep(1000);
				
		driver.findElement(By.linkText("Withdrawal Details History")).click();
		Thread.sleep(1000);
		
		//Find 1st record status of which = status
		trs=driver.findElements(By.cssSelector("table.table>tbody>tr"));
		
		//If the user does NOT have withdraw details record, stop and exit
		if(trs.size()==0)
		{
			Assert.assertTrue(trs.size()>=1, "No Withdraw Details Records in History Page.");
			System.out.println("No Withdraw Details Records in History Page.");
		}
		
		//Loop all records. When finding the 1st one matched, break the current loop
		for(i=0; i<trs.size();i++)
		{
			if(trs.get(i).findElement(By.cssSelector("td:nth-of-type(5)")).getText().equalsIgnoreCase(status))
			{
				break;
			}

		}
		
		//If no matched record is found, stop and exit. Else, click the edit button
		if(i>=trs.size())
		{
			Assert.assertTrue(i<trs.size(), "No Records in status " + status);
			System.out.println("No Records in status " + status);
		}else
		{
			trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(2)")).click();
			
			//Edit details
			funcUPWdUpdate();
		
			//Submit
			driver.findElement(By.xpath(".//button[text() = 'Update']")).click();;
			
			
			//Print the operation result:			
			String cssPath = "li.messenger-message-slot div.messenger-message-inner";
			wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssPath)));
			String result= driver.findElement(By.cssSelector(cssPath)).getText();
			System.out.println("Result is: " + result);
			
			wait02.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(cssPath)));
			
			
			
		}
		
		
	}

	/*
	 * For UnionPay withdraw Record Update in CP
	 * When Records are in Submitted/Pending status, Customers can update the records and save. This function is to update different fields
	 * 
	 */
	public void funcUPWdUpdate() throws Exception
	{
		
		String oldValue="", oldBank = "", oldProvince="", oldCity = "", newValue="";
		Random r=new Random();
		Select tBank, tProvince, tCity;
		int index=0;
		
		//Get old bank
		tBank= new Select(driver.findElement(By.id("bankName")));
		oldBank=tBank.getFirstSelectedOption().getText();
		
		//Get old Branch Province
		tProvince= new Select(driver.findElement(By.id("branchProvince")));
		oldProvince=tProvince.getFirstSelectedOption().getText();			
		
		//Get old Branch City
		tCity= new Select(driver.findElement(By.id("branchCity")));
		oldCity=tCity.getFirstSelectedOption().getText();
		
	
		//Select new Bank
		newValue = oldBank;
		while(newValue.equalsIgnoreCase(oldBank))				
		{
			index = r.nextInt(tBank.getOptions().size());
			tBank.selectByIndex(index);
			newValue=tBank.getFirstSelectedOption().getText();
		}
		
		System.out.print("Bank Name: Old =  " + oldBank);
		System.out.print("    New = " + newValue + "\n");
		
		//Wait for options load
		Thread.sleep(1000);

		//Select new Branch Province
		newValue = oldProvince;
		while(newValue.equalsIgnoreCase(oldProvince))				
		{
			index = r.nextInt(tProvince.getOptions().size());
			tProvince.selectByIndex(index);
			newValue=tProvince.getFirstSelectedOption().getText();
		}
		
		System.out.print("Branch Province: Old = " + oldProvince);
		System.out.print("    New = " + newValue + "\n");
		
		//Wait for options load
		Thread.sleep(1000);

	
		//Select new Branch City
		newValue = oldCity;
		while(newValue.equalsIgnoreCase(oldCity))				
		{
			index = r.nextInt(tCity.getOptions().size());
	
			tCity.selectByIndex(index);
			newValue=tCity.getFirstSelectedOption().getText();
		}
		
		System.out.print("Branch City:  " + oldCity);
		System.out.print("    New = " + newValue + "\n");
		
		//Update Branch Name by adding 'TR'
		oldValue=driver.findElement(By.id("branchName")).getAttribute("value");
		driver.findElement(By.id("branchName")).sendKeys("TR");
		newValue=driver.findElement(By.id("branchName")).getAttribute("value");
		
		System.out.print("Branch Name: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");
		
		//Update Bank Card Number by replacing the last three digits to '888'
		oldValue=driver.findElement(By.id("bankcardNumber")).getAttribute("value");
		newValue = oldValue.substring(0, oldValue.length()-3) + "888";
		newValue =newValue.replace("X", "8");
		
		driver.findElement(By.id("bankcardNumber")).clear();
		driver.findElement(By.id("bankcardNumber")).sendKeys(newValue);
		newValue=driver.findElement(By.id("bankcardNumber")).getAttribute("value");
		
		System.out.print("Bank Card Number: Old = " + oldValue);
		System.out.print("    New= " + newValue + "\n");
		
		//Update Card Holder's name by adding 'TR'
		oldValue=driver.findElement(By.id("cardHolderName")).getAttribute("value");
		driver.findElement(By.id("cardHolderName")).sendKeys("TR");
		newValue=driver.findElement(By.id("cardHolderName")).getAttribute("value");
		
		System.out.print("Card Holder's Name: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");
		
		//Update Phone Number by adding '88'
		oldValue=driver.findElement(By.id("phoneNumber")).getAttribute("value");
		driver.findElement(By.id("phoneNumber")).sendKeys("88");
		newValue=driver.findElement(By.id("phoneNumber")).getAttribute("value");
		
		System.out.print("Phone Numbe:r Old = " + oldValue);
		System.out.print("    New= " + newValue + "\n");			
		
		//Replace the first 2 digits  for National ID No. 2 digits are the same and generated randomly.
		index = r.nextInt(10);
		newValue=Integer.toString(index) + Integer.toString(index);
		
		
		oldValue=driver.findElement(By.id("nationalID")).getAttribute("value");
		newValue=newValue + oldValue.substring(2);
		driver.findElement(By.id("nationalID")).clear();
		driver.findElement(By.id("nationalID")).sendKeys(newValue);
		
		System.out.print("NationalID Number: Old = " + oldValue);
		System.out.print("    New = " + newValue + "\n");		
		
			
		//Upload a replacement photo
		driver.findElement(By.xpath("//input[@type='file' and @data-name='replacementUnionCardPhoto']")).sendKeys(Utils.workingDir+"\\proof.png");
		Thread.sleep(1000);
		driver.findElements(By.xpath("//a[@title='Upload Selected Files']")).get(1).click();
		
		Thread.sleep(1000);
		
		//Print old image path and new image path
		List<WebElement> imgList= driver.findElements(By.cssSelector("img.pull-left.input-img.input_img"));
		System.out.println("Old Image Source is: " + imgList.get(0).getAttribute("src"));
		System.out.println("New Image Source is: " + imgList.get(1).getAttribute("src"));
		
		
	}
	
	//Get user's realname from Profile API using CP cookie and API
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
        
/*        if(!TestEnv.equalsIgnoreCase("test")) {
        	value = b[0].substring(b[0].indexOf("=")+1,b[0].length()).trim();    
		}else {
			value = b[0].substring(b[0].indexOf(":")+1,b[0].length()).trim();       
		}*/
        
        value = b[0].substring(b[0].indexOf(":")+1,b[0].length()).trim(); 
        
        if(value.contains("\"")) {
        	value = value.replace("\"", "");
        }
        //System.out.println("\n"+ "Result converted is: " + value);

		
		return value;
	}	
	
	
	 /* Click on Back To Home Page
	 */
	public void funcBackToHomePage(String Brand) throws Exception
	{
		
		if(Brand.equalsIgnoreCase("vt"))
		{
			wait01.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[contains(text(), 'Back To Home Page')]")))).click();
		}else
		{
			wait01.until(ExpectedConditions.elementToBeClickable((By.xpath("//a[@class='el-button btn_blue']")))).click();
		}
	}
	 
	 
	
	static BigDecimal funcSplitAccount(String option) throws Exception
	{
		BigDecimal moneyAmount=new BigDecimal("0.00");
		String subStr = "";
		String[] strSplit;

		NumberFormat nf=NumberFormat.getInstance();
		Number n;
		
		strSplit=option.split(" ");
		
		switch(strSplit[3])
		{
		case "AUD":
		case "SGD":
		case "CAD":
			//remove A$,S$,C$ currency symbol
			subStr=strSplit[2].substring(2);
			break;
		case "USD":
		case "GBP":		
		case "EUR":
			//remove $, £, € currency symbol
			subStr=strSplit[2].substring(1);
		default:
			break;
			
		}

		n=nf.parse(subStr); //remove thousand separator
		moneyAmount=new BigDecimal(n.toString());
		
		return moneyAmount;
	}
	
	//@AfterClass(alwaysRun=true)
	@Parameters(value= {"Brand"})
	void ExitBrowser(String Brand) throws Exception
	{
		//driver.navigate().refresh();
		Utils.funcLogOutCP(driver, Brand);
		driver.quit();
	}
	
	void go2PaymentDetails(String Brand) throws Exception
	{
		if(Brand.equalsIgnoreCase("vt"))
		{
			driver.findElement(By.xpath("//span[contains(text(),'Funds')]")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//li[contains(text(),'Payment details')]")).click();
			Thread.sleep(2000);
		}else
		{
			driver.findElement(By.xpath("//span[contains(text(),'FUNDS')]")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//li[contains(text(),'PAYMENT DETAILS')]")).click();
			Thread.sleep(2000);
		}

	}
	
	void payMethodForm(String Brand) throws Exception
	{
		WebElement webElement;
		
		//Select Bank name
		driver.findElement(By.id("bankName")).click();
		Thread.sleep(500);
		
		if(Brand.equalsIgnoreCase("vt"))
		{
			List<WebElement> bankList = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
			webElement = bankList.get(0);
		}else
		{
			webElement = driver.findElement(By.cssSelector("div:nth-child(9) div.el-scrollbar div:nth-child(1) ul > li:nth-child(4)"));
		}
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();
		
		if(Brand.equalsIgnoreCase("vt")){
			
			//Input Card Holder Name
			driver.findElement(By.id("cardHolderName")).sendKeys(Utils.randomSCString(3));			
			
		}else
		{				
			//Input Card Holder Name
			driver.findElement(By.id("name")).sendKeys(Utils.randomSCString(3));		
		
		}
		
		//Input ID number
		driver.findElement(By.id("nationalId")).sendKeys(Utils.randomNumber(17)+Utils.randomString(1).toUpperCase());
		
		//Input Card number
		driver.findElement(By.id("cardNumber")).sendKeys(Utils.randomNumber(20));
		
		//Input phone number
		driver.findElement(By.id("cardPhoneNumber")).sendKeys(Utils.randomNumber(20));
		
		//Select Province
		driver.findElement(By.xpath("//div[@data-toggle='distpicker']//select[1]")).click();
		Thread.sleep(500);
		
		webElement = driver.findElement(By.xpath("//div[@data-toggle='distpicker']//select[1]//option[4]"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();
		
		//Select City
		driver.findElement(By.xpath("//div[@data-toggle='distpicker']//select[2]")).click();
		Thread.sleep(500);
		
		webElement = driver.findElement(By.xpath("//div[@data-toggle='distpicker']//select[2]//option[4]"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
		webElement.click();
		
		//Input Bank branch
		driver.findElement(By.id("branchName")).sendKeys(Utils.randomSCString(5));		
		
		//Input card photo
		driver.findElement(By.xpath("//input[@name='file']")).sendKeys(Utils.workingDir+"\\proof.png");
	}
}
