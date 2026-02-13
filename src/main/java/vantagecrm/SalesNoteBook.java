package vantagecrm;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import adminBase.AccountSearch;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;

public class SalesNoteBook {

	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String userName; //Client Name
	String IBfName, IBlName;  //IB Name
	String ibPwd="123Qwe";
	String internalUserName; //Internal user name;
	String emailSuffix=Utils.emailSuffix;
	Random tRandom=new Random();
	WebDriverWait wait03;
	WebDriverWait wait15;

	enum eSearchOptions {
		
		accountOwner(3, "Account Owner"),
		clientName(4, "Client Name"),
		country(5, "Country"),
		mobile(7, "Mobile"),
		contactEmail(8, "Contact Email"),
		leadSource(10, "Lead Sources");
		
		private int index;  //the column sequence in Sales Notebook list
		private String searchKey;  //search options
		
		private eSearchOptions(int index, String searchKey)
		{
			this.index=index;
			this.searchKey = searchKey;
		}
		
		public String getSearchKey()
		{
			return this.searchKey;
		}
		
		public int getIndex()
		{
			return this.index;
		}
	}

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{  //Added one parameter ITestContext context to pass variables by Yanni on 5/15/2019
		  String browserName= "chrome";
		  System.out.println("Browser is: " + browserName);
		  System.out.println("Thread Id is: " + Thread.currentThread().getId());
		  
		  switch (browserName)
		  {
		  case "chrome":
		  
			  WebDriverManager.chromedriver().setup();
			  ChromeOptions options = new ChromeOptions();
			  options.addArguments("--no-sandbox");
			  options.setExperimentalOption("useAutomationExtension", false);
			  if(Boolean.valueOf(headless)) {
					options.addArguments("window-size=1920,1080");
					options.addArguments("headless");
				}
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
		 
		
			/*
			 * ChromeOptions options=new ChromeOptions();
			 * options.setAcceptInsecureCerts(true);
			 * 
			 * System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
			 * driver = new ChromeDriver(options);
			 */
      	  context.setAttribute("driver", driver);      //Added by Yanni: to pass driver to Listener
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait03=new WebDriverWait(driver, Duration.ofSeconds(3));
		  wait15=new WebDriverWait(driver,Duration.ofSeconds(15));
	}
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		
		Utils.funcLogOutAdmin(driver);
		//Close all browsers
		driver.quit();
	}

	//Login Admin with credentials

	@Parameters({"AdminURL","AdminName","AdminPass","Brand"})
	@Test(priority=0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand, Method method) throws Exception
	{
	
		//Login AU admin
		driver.get(AdminURL);	
		Utils.funcLogInAdmin(driver, AdminName,AdminPass, Brand);
	}

	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=2)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void AddLeads(String AdminURL,String TestEnv, String Brand) throws Exception
	{
		String leadfName;
		String leadlName;
		Select t;
		Boolean flag = false;
		//Input the required information
		Random r=new Random();
				
		funcNavigate2SalesNotebook(AdminURL);
		
		driver.findElement(By.id("addCustomer")).click();
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div.panel-heading")).getText(), "Add SalesNote");

		//Input Account Owner, keep account owner as your name or select testcrmib*** randomly. Default is login user.
		int k = r.nextInt(2);
		k=0; //to make account owner as sales himself
		if(k==1)
		{
			t = new Select(driver.findElement(By.id("account_owner_id")));
			
			for(int s =0; s<t.getOptions().size(); s++)
			{
				if (t.getOptions().get(s).getText().startsWith(Utils.ibUserPrefix))
				{
					t.getOptions().get(s).click();	
					flag = true;
				}
				
				if(flag == true)
				{
					break;
				}
			}
		}		
		
		//Input Client Name
		leadfName="Note"+Utils.randomString(3).toLowerCase();
		leadlName= Utils.randomString(4).toUpperCase();
		driver.findElement(By.id("accountName")).sendKeys( leadfName + " " + leadlName);
		
		//Input leadSource
		driver.findElement(By.id("leadSource")).sendKeys("Automation Add Leads");
		
		//Input email
		driver.findElement(By.id("email")).sendKeys(leadfName+Utils.emailSuffix);
		
		//Select Date of Birth
		
		String oldDOB=driver.findElement(By.id("birthday")).getAttribute("value");
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dt=sdf.parse(oldDOB);
		Calendar calInst=Calendar.getInstance();
		calInst.setTime(dt);
		calInst.add(Calendar.YEAR, -2);
		calInst.add(Calendar.MONTH, 1);
		calInst.add(Calendar.DATE, -1);
		dt=calInst.getTime();
		
		String queryStr="jQuery('#birthday').val('" + sdf.format(dt).toString()+"');";
		//System.out.println(queryStr);
		((JavascriptExecutor)driver).executeScript(queryStr);
	
		//Input Mobile Phone code
		t=new Select(driver.findElement(By.id("phone_code")));
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		//Input Mobile
		driver.findElement(By.id("mobile")).sendKeys(Utils.randomNumber(8));
		
		//Input Country
		t=new Select(driver.findElement(By.id("country")));
		//Input Country of Residence
		if (Brand.equalsIgnoreCase("au")) {
			t.selectByValue("3512");
		}else {
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		}
		
		//Iraq is a banned country so can't be used as registration
		if(t.getFirstSelectedOption().getText().equals("Iraq") || t.getFirstSelectedOption().getText().equals("Afghanistan"))
		{
			t.selectByVisibleText("Australia");;
		}
		
		//Click Submit
		driver.findElement(By.cssSelector("button.btn.btn-primary.pull-left.popModalSub")).click();
		
		//Wait until the table is loaded completely
		Thread.sleep(2000);
		
		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("Status is: " + resultMsg);
		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
		
		String selectSql="select * from tb_sales_note where real_name like '" + leadfName + " " + leadlName +"%';";
		String result = DBUtils.funcReadDBReturnAll(Utils.getDBName(Brand)[1], selectSql, TestEnv);
		
		System.out.println("Query tb_sales_note and return the following:");
		System.out.println(result);
		
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void DeleteLeads(String AdminURL,String TestEnv, String Brand) throws Exception
	{
	
		String leadName;
		String selector = "td:nth-of-type(" + eSearchOptions.clientName.getIndex() + ")";
		List<WebElement> trs=funcChooseSalesNote(AdminURL);
		//Select 1st row in the results
		
		leadName = trs.get(0).findElement(By.cssSelector(selector)).getText();
		trs.get(0).findElement(By.cssSelector("td.bs-checkbox input")).click();
		
		//Click Delete button
		driver.findElement(By.id("delCustomer")).click();
		
		Thread.sleep(1000);
		//Confirm Delete
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		
		//Print result message
		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("Status is: " + resultMsg);
//		wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));	
		
		String selectSql="select * from tb_sales_note where real_name like '"  + leadName +"%';";
		String result = DBUtils.funcReadDBReturnAll(Utils.getDBName(Brand)[1], selectSql, TestEnv);
		
		System.out.println(selectSql);
		System.out.println("Query tb_sales_note and return the following:");
		System.out.println(result);
		
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void modifyAttribution(String AdminURL,String TestEnv, String Brand) throws Exception
	{
		List<WebElement> trs=funcChooseSalesNote(AdminURL);
		Select t;
		//Select 1st row in the results
		String accountOwner = trs.get(0).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
		trs.get(0).findElement(By.cssSelector("td.bs-checkbox input")).click();
		
		//Click Modify Attribution button
		driver.findElement(By.id("updateBelong")).click();
		
		//Wait until Please Select Attribution dialog loads
		Thread.sleep(1000);
		
		//Click Please Select
		driver.findElement(By.cssSelector("a.chosen-single.chosen-default")).click();
		
		//Input "test"
		driver.findElement(By.cssSelector("div.chosen-search input")).clear();
		driver.findElement(By.cssSelector("div.chosen-search input")).sendKeys("test");
		
		//Choose the first one (can't use Select control to select one item. Using Robot class)
		 Robot robot = new Robot();
		 robot.keyPress(KeyEvent.VK_DOWN);
		 robot.keyRelease(KeyEvent.VK_DOWN);
	
		 robot.keyPress(KeyEvent.VK_ENTER);
		 robot.keyRelease(KeyEvent.VK_ENTER);
		
		//Confirm
		 driver.findElement(By.xpath(".//button[contains(text(), 'Confirm')]")).click();
	
		//Print result message
		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("Status is: " + resultMsg);
							
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void AddFollowUp(String AdminURL,String TestEnv, String Brand) throws Exception
	{
		Select t;
		List<WebElement> trs=funcChooseSalesNote(AdminURL);
		Random r = new Random();
		String note = "Automation Note";
				
		//Select 1st row in the results
		trs.get(0).findElement(By.cssSelector("td:nth-last-of-type(1) a")).click();
		
		//Wait until the Appointment dialog popup
		Thread.sleep(1000);  
		
		//Set appointment information
		
		//Set Date to a future date
		//setDatePicker("interviewDate_time", 0, 1, 0 );
		
		//Select Method
		t= new Select(driver.findElement(By.id("interview_mode")));
		t.selectByIndex(r.nextInt((t.getOptions().size()-1))+1);
		note = note + " " +  t.getFirstSelectedOption().getText();
		
		//Select Interest
		t= new Select(driver.findElement(By.id("interest_degree2")));
		t.selectByIndex(r.nextInt((t.getOptions().size()-1))+1);
		note = note + " " + t.getFirstSelectedOption().getText();
		
		//Input note:
		driver.findElement(By.id("salesRecord2")).sendKeys(note);
		
		driver.findElement(By.id("salesInfoRelease")).click();
		
		//Print result message
		String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("Status is: " + resultMsg);
		//wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));		
		
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value= {"AdminURL","TestEnv", "Brand"})
	void verifySearch(String AdminURL,String TestEnv, String Brand) throws Exception
	{
		List<WebElement> trs;
		List<WebElement> tds;
		String accountOwner, clientName, counTry, moBile, email, fromDate;
		String leadSource;
		
		//Search to get recent 2 years' records
	
		trs = funcChooseSalesNote(AdminURL);
				
		tds = trs.get(0).findElements(By.tagName("td"));
		
		fromDate = driver.findElement(By.id("interviewDate1")).getAttribute("value");
		
		
		accountOwner = tds.get(eSearchOptions.accountOwner.getIndex()-1).getText().trim();
		clientName = tds.get(eSearchOptions.clientName.getIndex()-1).getText().trim();
		counTry = tds.get(eSearchOptions.country.getIndex()-1).getText().trim();
		
		tds.get(eSearchOptions.mobile.getIndex()-1).click();
		Thread.sleep(300);
		moBile = tds.get(eSearchOptions.mobile.getIndex()-1).getText().trim();
		
		tds.get(eSearchOptions.contactEmail.getIndex()-1).click();
		Thread.sleep(300);
		email = tds.get(eSearchOptions.contactEmail.getIndex()-1).getText().trim();
		
		
		leadSource = tds.get(eSearchOptions.leadSource.getIndex()-1).getText().trim();
		
	
		//Test Search via account owner		
		//Search via accountOwner to get results
		System.out.println("Search via Account Owner...");
		funcSearch(fromDate, "", accountOwner, "", "");
		//Check results are qualified
		validateSearchResult(eSearchOptions.accountOwner, accountOwner);
		
		//Test Search via Client Name
		//Search via Client Name to get results
		System.out.println();
		System.out.println("Search via Client Name...");
		funcSearch(fromDate, "", "", eSearchOptions.clientName.getSearchKey(), clientName);
		//Validate results
		validateSearchResult(eSearchOptions.clientName, clientName);		

		//Test Search via Email
		//Search via Contact Email to get results
		System.out.println("Search via Contact Email...");
		funcSearch(fromDate, "", "", eSearchOptions.contactEmail.getSearchKey(), email);
		//Validate results
		validateSearchResult(eSearchOptions.contactEmail, email);			

		//Test Search via Country
		//Search via country to get results
		System.out.println("Search via Country Name...");
		funcSearch(fromDate, "", "", eSearchOptions.country.getSearchKey(), counTry);
		//Check results are qualified
		validateSearchResult(eSearchOptions.country, counTry);		
		
		//Test Search via Lead source		
		//Search via lead source to get results
		System.out.println("Search via lead source...");
		funcSearch(fromDate, "", "", eSearchOptions.leadSource.getSearchKey(), leadSource);
		//Check results are qualified
		validateSearchResult(eSearchOptions.leadSource, leadSource);		
		
		//Test Search via Mobile		
		//Search via Mobile to get results
		System.out.println("Search via Mobile...");
		funcSearch(fromDate, "", "", eSearchOptions.mobile.getSearchKey(), moBile);
		//Check results are qualified
		validateSearchResult(eSearchOptions.mobile, moBile);		
		
	}
	
	void funcNavigate2SalesNotebook(String AdminURL)
	{
		driver.navigate().to(AdminURL);
		driver.findElement(By.linkText("Client Management")).click();
		driver.findElement(By.linkText("Sales Note")).click();
		
	}

	void setDatePicker(String idSelector, int yearOffset, int monthOffset, int dayOffset ) throws Exception
	{
		//String oldDOB=driver.findElement(By.id(idSelector)).getAttribute("value");
		String oldDOB=driver.findElement(By.xpath(idSelector)).getAttribute("value");
		SimpleDateFormat sdf;
		
		if(oldDOB.contains("/"))
		{
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		}else
		{		
		    sdf=new SimpleDateFormat("yyyy-MM-dd");
		}
		
		Date dt=sdf.parse(oldDOB);
		Calendar calInst=Calendar.getInstance();
		calInst.setTime(dt);
		calInst.add(Calendar.YEAR, yearOffset);
		calInst.add(Calendar.MONTH, monthOffset);
		calInst.add(Calendar.DATE, dayOffset);
		dt=calInst.getTime();
		
		String queryStr="jQuery('#" + idSelector + "').val('" + sdf.format(dt).toString()+"');";
		((JavascriptExecutor)driver).executeScript(queryStr);
	}
	
	void funcSearch(String fromDate, String toDate, String accountOwner, String searchOptions, String keyWord) throws Exception 
	{
		
		int yearOffset = -2;
		int monthOffset = -2;
		int dayOffset = -2;
		
		//Set fromDate
		if(!fromDate.equals(""))
		{
			setDatePicker("//input[@name='create_time_from_filter']", yearOffset, monthOffset, dayOffset);
		}			
		
		//Set endDate
		if(!toDate.equals(""))
		{
			setDatePicker("//input[@name='create_time_to_filter']", yearOffset, monthOffset, dayOffset);
		}
		
		//Set Account Owner
		driver.findElement(By.id("agentuserQuery")).clear();
		if(!accountOwner.equals(""))
		{			
			driver.findElement(By.id("agentuserQuery")).sendKeys(accountOwner);
		}
		
		//Set SearchOptions
		Select t = new Select(driver.findElement(By.id("search_type")));
		if(!searchOptions.equals(""))
		{
			t.selectByVisibleText(searchOptions);
		}else
		{
			t.selectByVisibleText("Search Options");
		}
		
		//Input keyword
		driver.findElement(By.id("userQuery")).clear();
		
		if(!keyWord.equals(""))
		{
			driver.findElement(By.id("userQuery")).sendKeys(keyWord);
		}
		
		//Click Search button
		driver.findElement(By.id("query")).click();
	
		//Wait until loading is done
		Thread.sleep(200);
		WebDriverWait wait60 = new WebDriverWait(driver,Duration.ofSeconds(60));
		wait60.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
	}

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods = "AdminLogIn")
	@Parameters(value={"Brand","TestEnv", "AdminURL"})
	public void updateSalesNote(String Brand, String TestEnv, String AdminURL) throws Exception
	{

		List<WebElement> ownerList=null, trs;
		String oldValue, newValue ;
		Select t;
		int j;
		String che = "change";
		String selector = "td:nth-of-type(" + eSearchOptions.clientName.getIndex() + ") > a";
		String leadName;
		
		trs=funcChooseSalesNote(AdminURL);
		
		//Update the 1st lead record
		trs.get(0).findElement(By.cssSelector(selector)).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);	
		
		//Change Account Owner to testcrmib*** 
		
		t=new Select(driver.findElement(By.id("account_owner_id")));
		
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Account Owner is: "+oldValue);
		
		
		ownerList = driver.findElements(By.xpath(".//select[@id='account_owner_id']/option[contains(text(),'testcrmib')]"));
		
		if(ownerList.size()>1)
		{
			for(j=0; j<ownerList.size();j++)
			{
				newValue=ownerList.get(j).getText();
				if(!newValue.equals(oldValue))
				{
					ownerList.get(j).click();
					System.out.println("New owner is: " + newValue);
					break;
				}
			}
		
		}else
		{
			System.out.println("No qualified test account owner. Keep the old owner unchanged.");
		}
				
				
		//Change Client Name
		oldValue = driver.findElement(By.id("accountName")).getAttribute("value");
		newValue = oldValue +  che;
		driver.findElement(By.id("accountName")).clear();
		driver.findElement(By.id("accountName")).sendKeys(newValue);
		System.out.println("Old Client Name is: "+oldValue);
		System.out.println("New Client Name is: "+newValue);
		leadName = newValue;
		
		//Change email
		oldValue = driver.findElement(By.id("email")).getAttribute("value");
		newValue = oldValue +  che;
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(newValue);
		System.out.println("Old email is: "+oldValue);
		System.out.println("New email is: "+newValue);		
		
		//Change Date of Birth
		
		String oldDOB=driver.findElement(By.id("birthday")).getAttribute("value");
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dt=sdf.parse(oldDOB);
		Calendar calInst=Calendar.getInstance();
		calInst.setTime(dt);
		calInst.add(Calendar.YEAR, -2);
		calInst.add(Calendar.MONTH, 1);
		calInst.add(Calendar.DATE, -1);
		dt=calInst.getTime();
		
		String queryStr="jQuery('#birthday').val('" + sdf.format(dt).toString()+"');";
		//System.out.println(queryStr);
		((JavascriptExecutor)driver).executeScript(queryStr);
		
		newValue = driver.findElement(By.id("birthday")).getAttribute("value");
		
		System.out.println("Old Date of Birth is: "+oldDOB);
		System.out.println("New Date of Birth is: "+newValue);		
		
		//Change Mobile
		oldValue = driver.findElement(By.id("mobile")).getAttribute("value");
		newValue = oldValue + "000";
		driver.findElement(By.id("mobile")).clear();
		driver.findElement(By.id("mobile")).sendKeys(newValue);
		
		System.out.println("Old Mobile is: "+oldValue);
		System.out.println("New Mobile is: "+newValue);				
		
		//Change leadSource
		oldValue = driver.findElement(By.id("leadSource")).getAttribute("value");
		newValue = oldValue + che;
		driver.findElement(By.id("leadSource")).clear();
		driver.findElement(By.id("leadSource")).sendKeys(newValue);

		System.out.println("Old Lead Source is: "+oldValue);
		System.out.println("New Lead Source is: "+newValue);		
		
		
		//Change Country
		
		t=new Select(driver.findElement(By.id("country")));
		
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Country is: "+oldValue);
		
		//j=0: select; j=1: Afghanistan (irregular country)
		for(j=2; j<t.getOptions().size();j++)
		{
			
			String selected=t.getOptions().get(j).getText();
			
			if(!selected.equalsIgnoreCase(oldValue) )
			{
				t.selectByIndex(j);
				break;
			}
				
		}
		
		if(j>=t.getOptions().size())
		{
			System.out.println("No Other Country can be selected. Keep the old one unchanged.");
		}else
		{
			System.out.println("New country is: "+ t.getFirstSelectedOption().getText());
		}

	
		//Submit
		driver.findElement(By.xpath(".//button[text()='Submit']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		String selectSql="select * from tb_sales_note where real_name like '"  + leadName +"%';";
		String result = DBUtils.funcReadDBReturnAll(Utils.getDBName(Brand)[1], selectSql, TestEnv);
		
		System.out.println(selectSql);
		System.out.println("Query tb_sales_note and return the following:");
		System.out.println(result);
				
	}
	
	List<WebElement> funcChooseSalesNote(String AdminURL) throws Exception
	{
		String fromDate;
		String searchOptions = "Client Name";
		String keyWord = "Note";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");

		
		fromDate = sdf.format(Calendar.getInstance().getTime()).toString();
		System.out.println("Today is: " + fromDate);
		funcNavigate2SalesNotebook(AdminURL);
		Thread.sleep(2000);
		funcSearch(fromDate, "", "", searchOptions, keyWord);
		
		List<WebElement> trs = driver.findElements(By.cssSelector("table#table > tbody > tr"));
		
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
			Assert.assertTrue(false, "Loading list  error.");
		}
		
	
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Leads (with name starting with Lead) is found.");
			Assert.assertTrue(false, "No Leads (with name starting with Lead) is found.");
		}
		
		return trs;
	}
	
	Boolean validateSearchResult(eSearchOptions searchOptions, String searchValue) throws Exception
	{
		List<WebElement> trs = driver.findElements(By.cssSelector("table#table tbody tr"));
		Boolean resultFlag=false;
		String cssSelector;
		String actValue;
	
		
		cssSelector = "td:nth-of-type(" + searchOptions.getIndex() + ")";
		
		System.out.println("Search " + searchOptions + "=" + searchValue + ", got " + trs.size() + " records.");
		
		if(trs.size()==0)
		{
			resultFlag = false;
			System.out.println("No results are found.");
			return resultFlag;
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			resultFlag = false;
			System.out.println("No results are found.");
			System.out.println();
			return resultFlag;
		}
		
		
		for(int i=0; i<trs.size(); i++)
		{
			if(searchOptions.getSearchKey().equalsIgnoreCase("Mobile") ||searchOptions.getSearchKey().equalsIgnoreCase("Contact Email") )
			{
				trs.get(i).findElement(By.cssSelector(cssSelector + " a")).click();
				Thread.sleep(300);
			}
			
			actValue = trs.get(i).findElement(By.cssSelector(cssSelector)).getText().trim();
			
			if(!actValue.equalsIgnoreCase(searchValue))
			{
				
				System.out.println("Actual value " + actValue + " is not the same as the search value " + searchValue);
				resultFlag = false;
				return resultFlag;
			}else
			{
				resultFlag = true;
			}

		}
		
		if(resultFlag ==  true)
		{
			System.out.println("Verified the search results one by one, Passed.");
			System.out.println();
		}
		
		return resultFlag;
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value={"Brand","TestEnv", "AdminURL"})
	
	public void dismissAptment(String Brand, String TestEnv, String AdminURL) throws Exception
	{
		List<WebElement> trs= funcChooseApt(AdminURL);
		
		if(trs!=null)
		{
			//Check the checkbox
			trs.get(0).findElement(By.cssSelector("td:nth-of-type(1) input")).click();
			
			//Click Dismiss button
			driver.findElement(By.id("dis_btn")).click();
			
			//Print result
			String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
			System.out.println("Result is: " + resultMsg);
	
		}
			
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value={"Brand","TestEnv", "AdminURL"})
	public void addNotes2Aptment(String Brand, String TestEnv, String AdminURL) throws Exception
	{
		List<WebElement> trs= funcChooseApt(AdminURL);
		String linkText;
		
		if(trs!=null)
		{
			//Click Add button
			for(int i=0; i<trs.size(); i++)
			{
				linkText = trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(1) a:nth-of-type(1)")).getText();
				if(linkText.equalsIgnoreCase("Add"))
				{
					trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(1) a:nth-of-type(1)")).click();
					break;
				}
			}

						
			//Verify Client Name is not null
			Thread.sleep(1000);
			String clientName = driver.findElement(By.cssSelector("div.col-sm-9")).getText();
			if(clientName.equalsIgnoreCase(""))
			{
				Assert.assertTrue(false, "Error. Client Name is empty");
			}
			
			//Input notes
			driver.findElement(By.id("note-val")).sendKeys("Automation Add Notes.");
			
			//Click Save
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			
			//Output message
			String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
			System.out.println("Result is: " + resultMsg);
		
		}
	}
	
	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true, invocationCount=1)
	@Parameters(value={"Brand","TestEnv", "AdminURL"})
	public void rescheduleAptment(String Brand, String TestEnv, String AdminURL) throws Exception
	{
		List<WebElement> trs= funcChooseApt(AdminURL);
		String idSelector="interviewDate_time";
		int yearOffset=0;
		int monthOffset=0;
		int dayOffset=-1;
		String linkText;
		
		if(trs!=null)
		{
			
			Thread.sleep(1000);
			//Click Reschedule button
	/*		for(int i=0; i<trs.size(); i++)
			{
				
				//At most 3 notes are added. When number of notes is above 3, Add button will disappear
				linkText = trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(1) a:nth-of-type(1)")).getText();
				if(linkText.equalsIgnoreCase("Add"))
				{
					//trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(1) a:nth-of-type(2)")).click();
					trs.get(i).findElement(By.xpath(".//a[@title='Reschedule']")).click();
				}else if((linkText.equalsIgnoreCase("Re")))
				{
					trs.get(i).findElement(By.cssSelector("td:nth-last-of-type(1) a:nth-of-type(1)")).click();
				}
			}*/
	
			trs.get(0).findElement(By.xpath(".//a[@title='Reschedule']")).click();
			
			//Verify Client Name is not null
			Thread.sleep(1000);
			String clientName = driver.findElement(By.cssSelector("div.col-sm-9")).getText();
			if(clientName.equalsIgnoreCase(""))
			{
				Assert.assertTrue(false, "Error. Client Name is empty");
			}
			
			//Input new time
			setDatePicker(idSelector, yearOffset, monthOffset, dayOffset );
			
			//Click Save
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			
			//Output message
			String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
			System.out.println("Result is: " + resultMsg);
		
		}
	}
	
	void funcGo2PendApt(String AdminURL) throws Exception
	{
		funcNavigate2SalesNotebook(AdminURL);
		Thread.sleep(1000);
		
		
		driver.findElement(By.id("action_pending")).click();
		Thread.sleep(1000);
		
		
	}
	
	List<WebElement> funcChooseApt(String AdminURL) throws Exception
	{
		List<WebElement> trs = null;
		
		funcGo2PendApt(AdminURL);		
		trs = driver.findElements(By.cssSelector("table#sale_table tbody tr"));
		
		if(trs.size()==0)
		{
			System.out.println("Getting Appointment List error.");
			Assert.assertTrue(false, "Getting Appointment List error.");
			
		}else if(trs.size()==1 && trs.get(0).getAttribute("class").equalsIgnoreCase("no-records-found"))
		{
			System.out.println("No Appointments Available");
			Assert.assertTrue(false, "No Appointments Available");					
		}
				
		
		return trs;
		
		
		
	}
}
