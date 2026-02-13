package vantagecrm;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

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

import io.github.bonigarcia.wdm.WebDriverManager;

public class UpdateInternalUser {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String userName; //Client Name
	WebDriverWait wait15;
	String newPwd="123Qwe";

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
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
    	  context.setAttribute("driver", driver);          //Added by Yanni on 5/15/2019
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait15=new WebDriverWait(driver, Duration.ofSeconds(15));
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
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand) throws Exception
	{
	
		//Login AU admin
		driver.get(AdminURL);	
		Utils.funcLogInAdmin(driver, AdminName,AdminPass, Brand);
	}

	/*
	 * In Client Management -> Client, search with the specified keyword and return the search result.
	 *  If there is userName recorded in Utils, use it first. Otherwise, use TraderName. 
	*/
	List<WebElement> funcChooseInternalUser(String Brand, String TestEnv) throws Exception
	{
		List<WebElement> trs=null;
		
		int j;	
		String keyword="IU";
			
		//Navigate to Internal User menu
		wait15.until(ExpectedConditions.elementToBeClickable(By.linkText("Users")));
		driver.findElement(By.linkText("Users")).click();
		driver.findElement(By.linkText("Internal User")).click();
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		Thread.sleep(1000);
		//Select "Search by real Name"
		driver.findElement(By.id("searchType")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("User Name")).click();
		
		
		//Input keyword
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(keyword);
		
		//Click Search button
		driver.findElement(By.id("query")).click();
		Thread.sleep(500);
		
		//Wait until the list is loaded completely
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		trs=driver.findElements(By.cssSelector("table#table>tbody>tr"));
				
			
		return trs;
	}
	
	
	

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods="AdminLogIn")
	@Parameters(value={"AdminURL","Brand","TestEnv"})
	public void updateIUser(String AdminURL,String Brand, String TestEnv) throws Exception
	{
		driver.navigate().to(AdminURL);	
		List<WebElement> trs = funcChooseInternalUser(Brand, TestEnv);
		int j;
		String oldValue;
		Select t;
		Random r = new Random();
		
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Internal User is found.");
	
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if(Utils.isInternalUser(userName))
			{
				System.out.println("System is going to change general information for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified Internal User is found.");
			//must stop if j>=trs.size()
			Assert.assertTrue(j<trs.size(),"No qualified Internal User is found.");
		}
		
		
		//Click the user name to open the client profile
		trs.get(j).findElement(By.className("update")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);	
		
		//Change role		
		t=new Select(driver.findElement(By.id("roleId")));
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Role is: "+oldValue);
		
		t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		
		while(t.getFirstSelectedOption().getText().equalsIgnoreCase(oldValue))
		{
			t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
		}
		System.out.println("New Role is: "+ t.getFirstSelectedOption().getText());
		
			
		//Change Superior to test***		
		
		t=new Select(driver.findElement(By.id("superior")));
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Superior is: "+oldValue);
	
			
		for(j=0; j<t.getOptions().size();j++)
		{
			
			if(t.getOptions().get(j).getText().startsWith("test") && (!t.getOptions().get(j).getText().equalsIgnoreCase(oldValue)))
			{
				t.selectByIndex(j);
				break;
			}
				
		}
		
		if(j>=t.getOptions().size())
		{
			System.out.println("No qualified test Superor. Keep the old owner unchanged.");
		}else
		{
			System.out.println("New Superior is: "+ t.getFirstSelectedOption().getText());
		}
		
	
		//Reset PWD by inputting new value in New Password and Confirm Password field
		driver.findElement(By.id("password1")).clear();
		driver.findElement(By.id("password1")).sendKeys("123Qwe");
		
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123Qwe");
		
		System.out.println("New Passowrd is 123Qwe.");
			
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		String selectSql="select user_id, user_name, org_id, role_id,  parent_id, parent_name, is_del from tb_user_inner where user_name ='"+ userName +"';";			   
		
		String dbName = Utils.getDBName(Brand)[1]; 		
	
		DBUtils.funcreadDB(dbName, selectSql, TestEnv); 
	
	}

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods="AdminLogIn")
	@Parameters(value={"AdminURL","Brand","TestEnv"})
	public void addCCode(String AdminURL, String Brand, String TestEnv) throws Exception
	{
		driver.navigate().to(AdminURL);	
		List<WebElement> trs = funcChooseInternalUser(Brand, TestEnv);
		int j;
		String oldValue;
	
		if(Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
		{
			System.out.println(Brand +" user does NOT have commission code.");
			Assert.assertTrue(false, Brand + " user does NOT have commission code.");
		}
		
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Internal User is found.");
	
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if(Utils.isInternalUser(userName))
			{
				System.out.println("System is going to add commission code for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified Internal User is found.");
	
		}
		
		
		//Click the user name to open the client profile
		trs.get(j).findElement(By.className("update")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);	
		oldValue = driver.findElement(By.cssSelector("div.form-group.commision_con input.form-control.code_inp:nth-of-type(1)")).getAttribute("value");
		
		//If user has no Commission value, give it one. If user already has, click button + to add one.
		if(oldValue.isEmpty())
		{
			System.out.println(userName + " has NO Commission Code. System is going to add a new Commission Code.");
			driver.findElement(By.cssSelector("div.form-group.commision_con input.form-control.code_inp:nth-of-type(1)")).sendKeys(userName + "CC" + Utils.randomNumber(2).toString());
			
		}else
		{
			System.out.println(userName + " already has Commission Code. System is going to add a new Commission Code.");
			
			//Click + to add one
			driver.findElement(By.cssSelector("a.add_code.icon_design")).click();
			
			List<WebElement> inputLst=driver.findElements(By.cssSelector("div.form-group.commision_con input.form-control.code_inp"));
			inputLst.get(inputLst.size()-1).sendKeys(userName + "CC"+Utils.randomNumber(2).toString());
			
		}
	
			
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		
		
		
		  String selectSql="select user.user_id, user.user_name, code.commission_code, code.update_time, code.update_user, "
		  + "user.org_id, code.is_del from tb_commission_code code inner join tb_user_inner user "
		  + "on code.user_id = user.user_id where user.user_name='"+ userName +"';";
		 	
		
		//DBUtils.funcreadDB("db_asic_vgp", selectSql, TestEnv);
		//DBUtils.funcreadDB("db_cima_vgp", selectSql, TestEnv);
		String dbName = Utils.getDBName(Brand)[0]; 	
		DBUtils.funcreadDB(dbName, selectSql, TestEnv);
		
				
	}

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods="AdminLogIn")
	@Parameters(value={"AdminURL","Brand","TestEnv"})
	public void delCCode(String AdminURL, String Brand, String TestEnv) throws Exception
	{
		driver.navigate().to(AdminURL);	
		List<WebElement> trs = funcChooseInternalUser(Brand, TestEnv);
		int j;
		String oldValue;
		
		if(Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
		{
			System.out.println(Brand + " user does NOT have commission code.");
			Assert.assertTrue(false, Brand + " user does NOT have commission code.");
		}
		
		
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Internal User is found.");
	
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if(Utils.isInternalUser(userName))
			{
				System.out.println("System is going to remove commission code for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified Internal User is found.");
	
		}
		
		
		//Click the user name to open the client profile
		trs.get(j).findElement(By.className("update")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);	
		oldValue = driver.findElement(By.cssSelector("div.form-group.commision_con input.form-control.code_inp:nth-of-type(1)")).getAttribute("value");
		
		//If user has no Commission value, give it one. If user already has, click button + to add one.
		if(oldValue.isEmpty())
		{
			System.out.println(userName + " has NO Commission Code, can't remove Commission Code.");
			
			
		}else
		{
			System.out.println(userName + " has Commission Code. System is going to remove a Commission Code.");
			
			//Click X to delete one
			System.out.println("Remove Commission Code: "+driver.findElement(By.cssSelector("div.form-group.commision_con input.form-control.code_inp:nth-of-type(1)")).getAttribute("value"));
			driver.findElement(By.cssSelector("a.del_code.icon_design.icon_design_del")).click();
					
		}
	
			
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		String selectSql="select user.user_id, user.user_name, code.commission_code, code.update_time, code.update_user, "
				+ "user.org_id, code.is_del from tb_commission_code code inner join tb_user_inner user "
				+ "on code.user_id = user.user_id where user.user_name='"+ userName +"';";			   
		//DBUtils.funcreadDB("db_asic_vgp", selectSql, TestEnv);
		//DBUtils.funcreadDB("db_cima_vgp", selectSql, TestEnv);
		String dbName = Utils.getDBName(Brand)[0]; 	
		DBUtils.funcreadDB(dbName, selectSql, TestEnv);
				
	}

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods="AdminLogIn")
	@Parameters(value={"AdminURL","Brand","TestEnv"})
	public void updateCCode(String AdminURL,String Brand, String TestEnv) throws Exception
	{
		driver.navigate().to(AdminURL);	
		List<WebElement> trs = funcChooseInternalUser(Brand, TestEnv);
		int j;
		String oldValue;

		if(Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg"))
		{
			System.out.println(Brand + " user does NOT have commission code.");
			Assert.assertTrue(false, Brand + " user does NOT have commission code.");
		}
		
		
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Internal User is found.");
	
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if(Utils.isInternalUser(userName))
			{
				System.out.println("System is going to update commission code for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified Internal User is found.");
	
		}
		
		
		//Click the user name to open the client profile
		trs.get(j).findElement(By.className("update")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);	
		oldValue = driver.findElement(By.cssSelector("div.form-group.commision_con input.form-control.code_inp:nth-of-type(1)")).getAttribute("value");
		
		//If user has no Commission value, give it one. If user already has, click button + to add one.
		if(oldValue.isEmpty())
		{
			System.out.println(userName + " has NO Commission Code. Can't Update.");
				
		}else
		{
			System.out.println(userName + " has Commission Code. System is going to update Commission Code " + oldValue);
			
			//Add "up" in the first commission code
			driver.findElement(By.cssSelector("div.form-group.commision_con input.form-control.code_inp:nth-of-type(1)")).sendKeys("up");
			
		}
	
			
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		String selectSql="select user.user_id, user.user_name, code.commission_code, code.update_time, code.update_user, "
				+ "user.org_id, code.is_del from tb_commission_code code inner join tb_user_inner user "
				+ "on code.user_id = user.user_id where user.user_name='"+ userName +"';";			   
		//DBUtils.funcreadDB("db_asic_vgp", selectSql, TestEnv);
		//DBUtils.funcreadDB("db_cima_vgp", selectSql, TestEnv);
		String dbName = Utils.getDBName(Brand)[0]; 	
		DBUtils.funcreadDB(dbName, selectSql, TestEnv);
				
	}

	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods="AdminLogIn")
	@Parameters(value={"AdminURL", "Brand","TestEnv"})
	public void deleteIUser(String AdminURL, String Brand, String TestEnv) throws Exception
	{
		driver.navigate().to(AdminURL);	
		List<WebElement> trs = funcChooseInternalUser(Brand, TestEnv);
		int j;

		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Internal User is found.");
	
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if(Utils.isInternalUser(userName))
			{
				System.out.println("System is going to delete user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified Internal User is found.");

			//testcase must stop if j >=trs.size()
			assertTrue(j<trs.size(),"No qualified Internal User is found.");
		}
		
		
		//Click the user name to open the client profile
		trs.get(j).findElement(By.className("remove")).click();
		
		//Click OK in the confirmation dialog
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//button[text()='OK']"))).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		String selectSql="select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name like '" + userName +"%';";
		
		DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSql, TestEnv);
	    //DBUtils.readDB(selectSql, TestEnv,Brand);
				
	}
}
