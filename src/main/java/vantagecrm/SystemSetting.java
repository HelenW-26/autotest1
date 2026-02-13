package vantagecrm;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
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
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;
import adminBase.EmailManagement;

/*
 * This class is to test 
 * --Register User from Official website, 
 * --Add clientsfrom Admin
 * --Add IB from Admin
 * --Add Internal Users from Admin
 */

public class SystemSetting {
	
	WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String userName; //Client Name
	static WebDriverWait wait15;
	String IBName;  //IB Name
	String internalUserName; //Internal user name;
	Random tRandom=new Random();

	SoftAssert saAssert = new SoftAssert();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
	Calendar calInst=Calendar.getInstance();
	String strDt="";
	Date dt;
	String queryStr="";
	
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
			 * System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
			 * driver = new ChromeDriver();
			 */
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  context.setAttribute("driver", driver);
		  
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
		Utils.funcLogInAdmin(driver, AdminName,AdminPass,Brand);
	}


	//Add Internal User
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv", "Brand"})
	void AddRole(String TestEnv, String Brand) throws Exception
	{
	
		Select t;
		int i;
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		driver.navigate().refresh();
		
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Role Management")).click();
		
	   //Click Add new user button
		wait10.until(ExpectedConditions.elementToBeClickable(By.id("button1"))).click();
		wait10.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.panel.panel-default")));
		
		//Assert New User dialog is popped up
		Thread.sleep(1000);
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(), "ADD ROLE");
		
		//Fill out the form
		//Input Role name
		String roleName="TestR"+Utils.randomString(4).toLowerCase();
		driver.findElement(By.id("role_name")).sendKeys(roleName);
		
	
		//Select Data Scope type: Organization Data or Personal Data
		t=new Select(driver.findElement(By.id("dataScopeType")));
		t.selectByVisibleText("Organization Data");
		Thread.sleep(500);
		
		//Choose Data Scope
		//Expand the top level
		driver.findElement(By.id("permissionTree_1_switch")).click();
		
		//Navigate the level2 tree and choose one organization name of which starts with "test"
		
		List<WebElement> treeL2=driver.findElements(By.cssSelector("ul#permissionTree_1_ul li"));
		
		for(i=0; i < treeL2.size(); i++)
		{
			
			String orgName=treeL2.get(i).findElement(By.tagName("a")).getAttribute("title");
			if(orgName.startsWith("test") || orgName.endsWith("QA"))
			{
				treeL2.get(i).findElement(By.id("permissionTree_2_check")).click();
				break;
			}
		}
		
		if(i>=treeL2.size())
		{
			System.out.println("No Test Organization(name starting with 'test') is available.");
			Assert.assertTrue(false, "No Test Organization(name starting with 'test' is available.");
		}
		
				
		//Click Confirm button
		Thread.sleep(500);
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
	    String selectSql="select id, name, status, create_user, create_time, data_scope_type, is_del from tb_role"
	    		+ " where name = '" + roleName +"';";
	    
	    String dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
		
	}

	//Delete Role
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv", "Brand"})
	void DeleteRole(String TestEnv, String Brand) throws Exception
	{
	
		int i;
		String roleName="";
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		driver.navigate().refresh();

	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Role Management")).click();
		
		try {
			//Change display records to 100 per page
			wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.btn-default.dropdown-toggle")));
			driver.findElement(By.cssSelector("button.btn.btn-default.dropdown-toggle")).click();
	
			Thread.sleep(500);
			driver.findElement(By.linkText("100")).click();
			Thread.sleep(1000);
		}catch (TimeoutException e) {
			System.out.println("No need to setup paging");
		}
		
		
		List<WebElement> trs=driver.findElements(By.cssSelector("table#table tbody tr"));
		
			
		for(i=0; i<trs.size(); i++)
		{
			roleName=trs.get(i).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
			
			if(roleName.startsWith("TestR"))
			{
				
				System.out.println("System is going to delete role " + roleName);
				trs.get(i).findElement(By.className("remove")).click();
				break;
			}
		}
		
	
		if(i>=trs.size())
		{
			System.out.println("No Test Role(name starting with 'TestR') is available.");
			Assert.assertTrue(false, "No Test Role(name starting with 'TestR') is available.");
		}
		
				
		//Click OK button
		Thread.sleep(500);
		driver.findElement(By.xpath(".//button[text()='OK']")).click();
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
	    String selectSql="select id, name, status, update_user, update_time, data_scope_type, is_del from tb_role"
	    		+ " where name = '" + roleName +"';";
	    
	    String dbName=Utils.getDBName(Brand)[0];//global database
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
	    
		
	}

	//Add Internal User
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand"})
	void UpdateRole(String TestEnv, String Brand) throws Exception
	{
	
		int i;
		String roleName="", oldValue="", newValue="", newName="";
		WebElement org=null;
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		driver.navigate().refresh();
	
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Role Management")).click();
		
		
		try {
			//Change display records to 100 per page
			wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.btn-default.dropdown-toggle")));
			driver.findElement(By.cssSelector("button.btn.btn-default.dropdown-toggle")).click();
		
			Thread.sleep(1000);
			driver.findElement(By.linkText("100")).click();
			Thread.sleep(1000);
		}catch (TimeoutException e) {
			System.out.println("No need to setup paging");
		}
		
		
		List<WebElement> trs=driver.findElements(By.cssSelector("table#table tbody tr"));
		
			
		for(i=0; i<trs.size(); i++)
		{
			roleName=trs.get(i).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
			
			if(roleName.startsWith("TestR"))
			{
				
				System.out.println("System is going to update role " + roleName);
				trs.get(i).findElement(By.className("update")).click();
				break;
			}
		}
		
	
		if(i>=trs.size())
		{
			System.out.println("No Test Role(name starting with 'TestR') is available.");
			Assert.assertTrue(false, "No Test Role(name starting with 'TestR') is available.");
		}
		
		//Update role name
		oldValue=driver.findElement(By.id("role_name")).getAttribute("value");
		System.out.println("The old role name is: " + oldValue);
		
		newName=oldValue+"M";
		driver.findElement(By.id("role_name")).clear();
		driver.findElement(By.id("role_name")).sendKeys(newName);
		System.out.println("The new role name is: " + driver.findElement(By.id("role_name")).getAttribute("value"));
		
		//Update Data Scope
		//Expand the top level
		driver.findElement(By.id("permissionTree_1_switch")).click();
		
		//Navigate the level2 tree and choose one organization name of which starts with "test"
		
		List<WebElement> treeL2=driver.findElements(By.cssSelector("ul#permissionTree_1_ul li"));
		
		//Loop once to uncheck all checked items
		for(i=0; i < treeL2.size(); i++)
		{
			org=treeL2.get(i).findElement(By.xpath(".//span[contains(@id,'_check')]"));
			
			if(org.getAttribute("class").contains("true"))  
			{
				
				oldValue=treeL2.get(i).findElement(By.tagName("a")).getAttribute("title");
				System.out.println("Old Organizatio Name is: " + oldValue);
				treeL2.get(i).findElement(By.xpath(".//span[contains(@id,'_check')]")).click();  //If checked, then uncheck
				break;
			}
		}
		
		if(i>=treeL2.size())
		{
			System.out.println("No Test Organization is selected.");
			Assert.assertTrue(false, "No Test Organization is selected.");
		}
		
		//Loop again to check a new organization
		for(i=0; i < treeL2.size(); i++)
		{
			newValue=treeL2.get(i).findElement(By.tagName("a")).getAttribute("title");
			if(!newValue.equals(oldValue))  
			{
				
				treeL2.get(i).findElement(By.xpath(".//span[contains(@id,'_check')]")).click();  //Check the first orga name which is not equal to the old one.
				System.out.println("New Organization Name is: " + newValue);
				
				break;
			}
		}
		
		if(i>=treeL2.size())
		{
			System.out.println("No Test Organization is selected.");
			Assert.assertTrue(false, "No Test Organization is selected.");
		}
				
		//Click Confirm button
		Thread.sleep(500);
		driver.findElement(By.xpath("//button[text()='Confirm']")).click();
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
	    String selectSql= "select count(*) as total,id, name, role_update_time, scope_update_time, is_del  from ("  
	    		+ "select role.id, role.name, role.update_time as role_update_time, role.is_del as roleDel, scope.org_id, scope.update_time as scope_update_time, scope.is_del "
	    		+ "from tb_role role inner join tb_role_datascope scope on role.id = scope.role_id"
	    		+ " where role.name = '" + newName 
	    		+"') a group by scope_update_time, is_del";
	    
	    String dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
		
	}

	//Add Internal User
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand"})
	void ChangePermission(String TestEnv,String Brand) throws Exception
	{
	

		int i;
		String roleName="", oldValue="", newValue="";
		WebElement org=null;
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		driver.navigate().refresh();
	
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Role Management")).click();
		
		
		//Change display records to 100 per page
		try {
			wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.btn-default.dropdown-toggle")));
			driver.findElement(By.cssSelector("button.btn.btn-default.dropdown-toggle")).click();
		
			Thread.sleep(500);
			driver.findElement(By.linkText("100")).click();
			Thread.sleep(1000);
		}catch (TimeoutException e) {
			System.out.println("No need to setup paging");
		}
		
		
		List<WebElement> trs=driver.findElements(By.cssSelector("table#table tbody tr"));
		
			
		for(i=0; i<trs.size(); i++)
		{
			roleName=trs.get(i).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
			
			if(roleName.startsWith("TestR"))
			{
				
				System.out.println("System is going to change permission for role " + roleName);
				trs.get(i).findElement(By.className("addpermission")).click();
				break;
			}
		}
		
	
		if(i>=trs.size())
		{
			System.out.println("No Test Role(name starting with 'TestR') is available.");
			Assert.assertTrue(false, "No Test Role(name starting with 'TestR') is available.");
		}
		
			
		//Update Permission
	
		List<WebElement> treeL2=driver.findElements(By.cssSelector("ul#permissionTree li"));
		
		//Loop once to uncheck all checked items
		for(i=0; i < treeL2.size(); i++)
		{
			org=treeL2.get(i).findElement(By.xpath(".//span[contains(@id,'_check')]"));
			
			if(org.getAttribute("class").contains("true"))  
			{
				
				oldValue=treeL2.get(i).findElement(By.tagName("a")).getAttribute("title");
				System.out.println("Old Organizatio Name is: " + oldValue);
				treeL2.get(i).findElement(By.xpath(".//span[contains(@id,'_check')]")).click();  //If checked, then uncheck
				break;
			}
		}
		
		if(i>=treeL2.size())
		{
			System.out.println("No Permission is given to role " + roleName);
		}
		
		//Loop again to check a new permission
		for(i=0; i < treeL2.size(); i++)
		{
			newValue=treeL2.get(i).findElement(By.tagName("a")).getAttribute("title");
			if(!newValue.equals(oldValue))  
			{
				
				treeL2.get(i).findElement(By.xpath(".//span[contains(@id,'_check')]")).click();  //Check the first orga name which is not equal to the old one.
				System.out.println("New Permission Name is: " + newValue);
				
				break;
			}
		}
		
		if(i>=treeL2.size())
		{
			System.out.println("No Available Permission is selected.");
			Assert.assertTrue(false, "No Available Permission is selected.");
		}
				
		//Click Confirm button
		Thread.sleep(500);
		driver.findElement(By.xpath("//button[text()='Confirm']")).click();
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
		    
	    String selectSql= "select count(*) as total,id, name, role_update_time, permission_update_time, create_user, is_del  from "
	    + "(select role.id, role.name, role.update_time as role_update_time, role.is_del as roleDel, "
	    + "permission.create_user, permission.update_time as permission_update_time, permission.is_del"
	    + " from tb_role role inner join tb_sys_role_permission permission on role.id = permission.role_id where role.name = '" + roleName 
	    + "') a group by permission_update_time, is_del;";
	    
	    String dbName=Utils.getDBName(Brand)[0];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
		
	}

	//Add Internal User
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv","Brand"})
	void OrganizationManagement(String TestEnv,String Brand) throws Exception
	{
	
		Select t;
		String userIDky,userIDau,dpKy,dpAu,delKy,delAu,dp,del;
		String orgName="testOrg"+Utils.randomString(3);
		String selectSQL = "select id,parent_id,parent_ids,org_name,org_type,is_del FROM tb_sys_organization where org_name='"+orgName+"';";
		String selectSQL2 = "select org_type,id,org_name,parent_id,address,is_del FROM tb_sys_organization where org_name='"+orgName+"';";
		String selectSQL3 = "select is_del,org_type,id,org_name,parent_id,address FROM tb_sys_organization where org_name='"+orgName+"';";
		
		System.out.println("TEST STARTS: Start to making organization change...");
		System.out.println("-------------------------------------------------------------");
		driver.navigate().refresh();	
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Organization Management")).click();		
		
		//Click add button
		Thread.sleep(500);
		driver.findElement(By.id("addBtn_orgtree_1")).click();
		Thread.sleep(1000);
		
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		//Input name
		driver.findElement(By.id("orgName")).sendKeys(orgName);
		
		//Input manager
		driver.findElement(By.id("master")).sendKeys("Alex");
		
		//Select type as department
		t = new Select(driver.findElement(By.id("orgType")));
		t.selectByValue("Department");
				
		//Input phone
		driver.findElement(By.id("phone")).sendKeys("12345678");
		
		//Input fax
		driver.findElement(By.id("fax")).sendKeys("12345678");
	   
		//Input email
		driver.findElement(By.id("email")).sendKeys("email@test.com");
				
		//Input address
		driver.findElement(By.id("address")).sendKeys("123 Chadstone Road");

		//Input remarks
		driver.findElement(By.id("remarks")).sendKeys("remarks");
		
		//Click submit button
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
		
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		saAssert.assertEquals(a, "Success");
		Thread.sleep(1000);
		
		//Verify in database
		System.out.println("Verify the new organization records please.");
		switch(Brand)
		{
		case "fsa":
		case "svg":
		case "vt":
			userIDky=DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSQL, TestEnv); 
			break;
			
		case "au":
		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
			userIDky=DBUtils.funcreadDB(Utils.getDBName("vfsc")[1], selectSQL, TestEnv); 
			
			//Verify in ASIC database
			userIDau=DBUtils.funcreadDB(Utils.getDBName("au")[1], selectSQL, TestEnv); 
			
			//user IDs should be equal
			Assert.assertEquals(userIDky, userIDau);
			break;
			
		default:
				System.out.println("Brand is NOT supported: " + Brand);
		}
	

	
		//Hover on the item and click modify
		WebElement element = driver.findElement(By.linkText(orgName));
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		driver.findElement(By.xpath("//span[text()='"+orgName+"']//i[3]")).click();
		Thread.sleep(1000);
		
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		//Update the Organization from Department to Company
		t = new Select(driver.findElement(By.id("orgType")));
		t.selectByValue("Company");
		
		//Click submit button
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
		
		a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		saAssert.assertEquals(a, "Success");
		Thread.sleep(1000);
		
		//Verify in KY database
		System.out.println();
		System.out.println("Verify the update of organization: org_type should be Company.");
		
		switch(Brand)
		{
		case "fsa":
		case "svg":
		case "vt":
		case "regulator2":
			dp=DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSQL2, TestEnv); 
			if (TestEnv.equalsIgnoreCase("test") || TestEnv.equalsIgnoreCase("alpha")) {
	        	Assert.assertTrue(dp.equals("Company"), "Modify the org type failed!");
	        }
			break;
			
		case "au":
		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
			dpKy=DBUtils.funcreadDB(Utils.getDBName("vfsc")[1], selectSQL2, TestEnv); 
	        //if (TestEnv.equalsIgnoreCase("testEnv")) {
			if (TestEnv.equalsIgnoreCase("test")  || TestEnv.equalsIgnoreCase("alpha")) {
	        	Assert.assertTrue(dpKy.equals("Company"), "Modify the org type failed!");
	        }
			//Verify in ASIC database
			dpAu=DBUtils.funcreadDB(Utils.getDBName("au")[1], selectSQL2, TestEnv); 
			//if (TestEnv.equalsIgnoreCase("testEnv")) {
			if (TestEnv.equalsIgnoreCase("test")|| TestEnv.equalsIgnoreCase("alpha")) {
				Assert.assertTrue(dpAu.equals("Company"), "Modify the org type failed!");
			}
			break;
			
			default:
				System.out.println("Brand is NOT supported: " + Brand);
				
				
		}
	
		//Hover on the item and click delete
		element = driver.findElement(By.linkText(orgName));
		action = new Actions(driver);
		action.moveToElement(element).build().perform();
		driver.findElement(By.xpath("//span[text()='"+orgName+"']//i[1]")).click();
		Thread.sleep(1000);
		
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		//Click ok button
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
		
		//Assert success
		a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		saAssert.assertEquals(a, "Successfully deleted");
		Thread.sleep(1000);
		
		//Verify the delete
		switch(Brand)
		{
		case "fsa":
		case "svg":
		case "vt":
		case "regulator2":
			del=DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSQL3, TestEnv); 
			if (TestEnv.equalsIgnoreCase("test")) {
				Assert.assertTrue(del.equals("1"), "delete the organization failed!");
	        }
			break;
			
		case "au":
		case "ky":
		case "vfsc":
		case "vfsc2":
		case "fca":
			//Verify in VFSC database
			System.out.println();
			System.out.println("Verify the organization: is_del should be 1.");
			delKy=DBUtils.funcreadDB(Utils.getDBName("vfsc")[1], selectSQL3, TestEnv); 
			//if (TestEnv.equalsIgnoreCase("testEnv")) {
			if (TestEnv.equalsIgnoreCase("test") || TestEnv.equalsIgnoreCase("alpha")) {
				Assert.assertTrue(delKy.equals("1"), "delete the organization failed!");
			}	
			//Verify in ASIC database
			delAu=DBUtils.funcreadDB(Utils.getDBName("au")[1], selectSQL3, TestEnv);
			//if (TestEnv.equalsIgnoreCase("testEnv")) {
			if (TestEnv.equalsIgnoreCase("test") || TestEnv.equalsIgnoreCase("alpha")) {
				Assert.assertTrue(delAu.equals("1"), "delete the organization failed!");
			}
		
			break;
			
			default:
				System.out.println("Brand is NOT supported: " + Brand);				
				
		}
	}
	
	//Add Promotion
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"AdminURL","TestEnv","Brand"})
	void AddPromotionSetting(String AdminURL,String TestEnv,String Brand) throws Exception
	{
	
		String proName="testPromo"+Utils.randomString(3);
		String selectSQL = "select * from tb_event_market_activity where name='"+proName+"';";
		
	
		System.out.println("TEST STARTS: Start to adding promotion setting...");
		System.out.println("-------------------------------------------------------------");
		//driver.navigate().to(AdminURL);
		driver.get(AdminURL);
		driver.navigate().refresh();
		Thread.sleep(1000);
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Promotion Setting")).click();		
		Thread.sleep(2000);
		
		//Change Search Date range otherwise newly created promotion won't be displayed
		//Get end date and calculate the date one month after
		
		strDt=driver.findElement(By.id("interviewDate2")).getAttribute("value");
		dt=sdf.parse(strDt);
		calInst.setTime(dt);
		calInst.add(Calendar.MONTH, +1);
		
		//Change the end date
		queryStr="jQuery('#interviewDate2').val('" + sdf.format(calInst.getTime())+"');";
		((JavascriptExecutor)driver).executeScript(queryStr);
		
		//Click Search button
		driver.findElement(By.id("query")).click();
		Thread.sleep(500);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		//Click add button
		Thread.sleep(500);
		driver.findElement(By.id("addActive")).click();
		Thread.sleep(1000);
		
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		//Input name
		driver.findElement(By.id("active_name")).sendKeys(proName);
		
		//Change Starting Date to tomorrow 
		calInst = Calendar.getInstance();
		strDt=sdf.format(calInst.getTime());
		dt = sdf.parse(strDt);
		calInst.setTime(dt);
		calInst.add(Calendar.DATE, +1);
		queryStr="jQuery('#startTime').val('" + sdf.format(calInst.getTime())+"');";
		((JavascriptExecutor)driver).executeScript(queryStr);
		
		//Change Ending Date to tomorrow + 7
		calInst.add(Calendar.DATE,  +7);
		queryStr="jQuery('#endTime').val('" + sdf.format(calInst.getTime())+"');";
		((JavascriptExecutor)driver).executeScript(queryStr);
						
		//Input description
		driver.findElement(By.id("salesRecord2")).sendKeys("Test adding 1 promotion setting!");
		
		
		//Click submit button
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
		
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		Utils.funcIsStringContains(a, "success", Brand);
		Thread.sleep(1000);
		
		//Verify in KY database
		System.out.println("Verify the new promotion records please.");
		//liufeng
		
		if (Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg") ||Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("vfsc2")) {
			
			DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSQL, TestEnv); 
			
		}else{
			
			DBUtils.funcreadDB(Utils.getDBName("ky")[1], selectSQL, TestEnv); 
			
			//Verify in ASIC database
			DBUtils.funcreadDB(Utils.getDBName("au")[1], selectSQL, TestEnv); 
			
		}
	
	}	

	
	//Edit Promotion
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"AdminURL","TestEnv","Brand"})
	void EditPromotionSetting(String AdminURL,String TestEnv,String Brand) throws Exception
	{
	
		String proName="";
		int j=0;
		
		System.out.println("TEST STARTS: Start to editing promotion setting...");
		System.out.println("-------------------------------------------------------------");
		driver.navigate().to(AdminURL);
		driver.navigate().refresh();
		Thread.sleep(500);
		
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Promotion Setting")).click();		
		Thread.sleep(500);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));

		//Change Search Date range otherwise newly created promotion won't be displayed
		//Get end date and calculate the date one month after
		strDt=driver.findElement(By.id("interviewDate2")).getAttribute("value");
		dt=sdf.parse(strDt);
		calInst.setTime(dt);
		calInst.add(Calendar.MONTH, +1);
		
		//Change the end date
		queryStr="jQuery('#interviewDate2').val('" + sdf.format(calInst.getTime())+"');";
		((JavascriptExecutor)driver).executeScript(queryStr);
		
		//Input filter
		driver.findElement(By.id("login")).sendKeys("testPromo");
		Thread.sleep(500);
		driver.findElement(By.id("query")).click();
		Thread.sleep(1000);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		List<WebElement> trs=driver.findElements(By.cssSelector("#activityTable tbody tr"));
		j=trs.size();
		
	
		if(trs.size()==0)
		{
			System.out.println("No Promotion(name starting with 'testPromo') is available.");
			Assert.assertTrue(false, "No Promotion(name starting with 'testPromo') is available.");
		}else {
			//Get the promotion name for editing
			proName=trs.get(j-1).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			System.out.println("Selected promotion name is: "+proName);
			
			//Click edit icon
			trs.get(j-1).findElement(By.xpath("//td[6]/a[1]")).click();
			Thread.sleep(500);
			
			wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
			
			//Input name
			driver.findElement(By.id("active_name")).clear();
			driver.findElement(By.id("active_name")).sendKeys(proName+"Edit");
			
			//Input description
			driver.findElement(By.id("salesRecord2")).clear();
			driver.findElement(By.id("salesRecord2")).sendKeys("Test editing promotion setting!");
			
			
			//Click submit button
			driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)")).click();
			
			String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
			Utils.funcIsStringContains(a, "success", Brand);
			Thread.sleep(1000);
			
			//Verify in KY database
			System.out.println("Verify the new promotion records please.");
			String selectSQL = "select * from tb_event_market_activity where name='"+proName+"Edit';";
			//liufeng
			if (Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg") || Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("vfsc2")) {
				
				DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSQL, TestEnv); 
				
			}else {
				
				DBUtils.funcreadDB(Utils.getDBName("ky")[1], selectSQL, TestEnv); 
				
				//Verify in ASIC database
				DBUtils.funcreadDB(Utils.getDBName("au")[1], selectSQL, TestEnv); 

			}
			
		}

	
	}
	
	
	
	//Edit Promotion
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"AdminURL","TestEnv","Brand"})
	void DelPromotionSetting(String AdminURL,String TestEnv,String Brand) throws Exception
	{
	
		String proName="";
		int j=0;
		
		System.out.println("TEST STARTS: Start to deleting promotion setting...");
		System.out.println("-------------------------------------------------------------");
		driver.navigate().to(AdminURL);	
		Thread.sleep(500);
		
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		driver.findElement(By.linkText("Promotion Setting")).click();		
		Thread.sleep(500);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		
		//Change Search Date range otherwise newly created promotion won't be displayed
		//Get end date and calculate the date one month after
		strDt=driver.findElement(By.id("interviewDate2")).getAttribute("value");
		dt=sdf.parse(strDt);
		calInst.setTime(dt);
		calInst.add(Calendar.MONTH, +1);
		
		//Change the end date
		queryStr="jQuery('#interviewDate2').val('" + sdf.format(calInst.getTime())+"');";
		((JavascriptExecutor)driver).executeScript(queryStr);
		Thread.sleep(1000);
		//Input filter
		driver.findElement(By.id("login")).sendKeys("testPromo");
		Thread.sleep(500);
		driver.findElement(By.id("query")).click();
		Thread.sleep(2000);
		
		
		List<WebElement> trs=driver.findElements(By.cssSelector("#activityTable tbody tr"));
		j=trs.size();
		
	
		if(trs.size()==0)
		{
			System.out.println("No Promotion(name starting with 'testPromo') is available.");
			Assert.assertTrue(false, "No Promotion(name starting with 'testPromo') is available.");
		}else {
			//Get the promotion name for editing
			proName=trs.get(j-1).findElement(By.cssSelector("td:nth-of-type(1)")).getText();			
			System.out.println("System is going to delete Promotion " + proName);
			
			//Click delete icon
			trs.get(j-1).findElement(By.xpath("//td[6]/a[2]")).click();
			Thread.sleep(1000);
			
			wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
			
			
			//Click OK button
			driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)")).click();
			
			String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
			wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
			Utils.funcIsStringContains(a, "Delete Successful！", Brand);
			Thread.sleep(1000);
			
			//Verify in KY database
			System.out.println("Verify the is_del in records please.");
			String selectSQL = "select * from tb_event_market_activity where name='"+proName+"';";
			//liufeng
			if (Brand.equalsIgnoreCase("fsa")||Brand.equalsIgnoreCase("svg") || Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("vfsc2")) {
				
				DBUtils.funcreadDB(Utils.getDBName(Brand)[1], selectSQL, TestEnv); 
				
			}else {
				
				DBUtils.funcreadDB(Utils.getDBName("ky")[1], selectSQL, TestEnv); 
				
				//Verify in ASIC database
				DBUtils.funcreadDB(Utils.getDBName("au")[1], selectSQL, TestEnv); 

			}
			
		}
		
	}
	
	//Added by Alex L for verifying keys display in role management
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"AdminURL", "Brand"})
	void RoleKeysVerification(String AdminURL, String Brand) throws Exception
	{

		List<String> result = new ArrayList<>();
		
		driver.navigate().refresh();
		
		String cookie = driver.manage().getCookies().toString();
		
		//remove the admin/main from AdminURL
		String url= Utils.ParseInputURL(AdminURL);
		
		//Using API to query and check role permission
		result = RestAPI.testPostQueryPermissionTree(url, cookie);
		
		if(result.size()==0) {
			System.out.println("***  All keys are displaying correctly. ***");
		}else {
			System.out.println( "Some keys' name are not correctly displayed!\n" + result);
		}
		
		
	}
	
	
	// 13/07/2020 Added by Alex L for adding plain notification
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderURL","TraderName","TraderPass","TestEnv", "AdminURL", "Brand"})
	void AddPlainNotification(String TraderURL, String TraderName, String TraderPass, String TestEnv, String AdminURL, String Brand) throws Exception
	{

		Select t;
		String subject="", content="";
		
		driver.navigate().refresh();
		
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("Notification Settings")).click();
		
	   //Click Add new notification button
		wait15.until(ExpectedConditions.elementToBeClickable(By.id("creation"))).click();
		wait15.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.panel.panel-default")));
		
		//Assert Add New Notification dialog is popped up
		Thread.sleep(1000);
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(), "Add New Notification");
		
		//Fill out the form
		//Input Notification name
		subject="TestNotification"+Utils.randomString(4).toLowerCase();
		driver.findElement(By.id("subject")).sendKeys(subject);
		
	
		//Select Notification type: Notification(plain type)
		t=new Select(driver.findElement(By.id("type")));
		t.selectByVisibleText("Notification");
		Thread.sleep(500);
		
		//It is enabled by default.
		
		//Input the content
		content = "Test Notification " + Utils.randomSCString(5) + Utils.randomString(5);
		driver.findElement(By.cssSelector("div.ck.ck-content")).sendKeys(content);
		
		//Click Confirm button
		Thread.sleep(500);
		driver.findElement(By.xpath(".//button[text()='Submit']")).click();
		
		String resultMsg=wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
	    String selectSql="select id,notification_type,subject,document from tb_activity_notification order by id desc limit 1;";
	    
	    String dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
	    
	    //Verify Notification in CP
	    funcCPVerifyNotification(driver, TraderURL, TraderName, TraderPass, subject, content, Brand);
		
	}
	
	
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderURL","TraderName","TraderPass","TestEnv", "AdminURL", "Brand"})
	void AddMaintainenceNotification(String TraderURL, String TraderName, String TraderPass, String TestEnv, String AdminURL, String Brand) throws Exception
	{

		Select t;
		String subject="", content="";
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		if(AdminURL.equals(driver.getCurrentUrl())){
			driver.navigate().refresh();
		}else {
			driver.get(AdminURL);
		}
		
		
		
	    //Navigate to notification Management Page
		driver.findElement(By.linkText("System Setting")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("Notification Settings")).click();
		
	   //Click Add new notification button
		wait10.until(ExpectedConditions.elementToBeClickable(By.id("creation"))).click();
		wait10.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.panel.panel-default")));
		
		//Assert Add New Notification dialog is popped up
		Thread.sleep(1000);
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-title")).getText(), "Add New Notification");
		
		//Fill out the form
		//Input Notification name
		subject="TestNotification"+Utils.randomString(4).toLowerCase();
		driver.findElement(By.id("subject")).sendKeys(subject);
		
	
		//Select Notification type: Notification(plain type)
		t=new Select(driver.findElement(By.id("type")));
		
		t.selectByVisibleText("Maintenance Notification");
		Thread.sleep(500);
		
		//It is enabled by default.
		
		//Input the content
		content = "Due to the scheduled maintenance, your MT4/MT5 service will be unavailable between [2020-06-13 01:00:00 to 2020-06-13 13:00:00] (local date and time format). Sorry for any inconvenience. If you have any further inquiries, please contact our support team support@vantagefx.com";
		driver.findElement(By.cssSelector("div.ck.ck-content")).sendKeys(content);
		
		//Click Confirm button
		Thread.sleep(500);
		driver.findElement(By.xpath(".//button[text()='Submit']")).click();
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
	    String selectSql="select id,notification_type,subject,document from tb_activity_notification order by id desc limit 1;";
	    
	    String dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
	    
	    //Verify Notification in CP
	    funcCPVerifyNotification(driver, TraderURL, TraderName, TraderPass, subject, content, Brand);
	}
	
	
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TraderURL","TraderName","TraderPass","TestEnv", "AdminURL", "Brand"})
	void EditNotification(String TraderURL, String TraderName, String TraderPass,String TestEnv, String AdminURL, String Brand) throws Exception
	{

		Select t;
		String item_id="", selectSql="",oldSub="",newSub="", oldValue="",newValue="";
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		driver.navigate().refresh();
		
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("Notification Settings")).click();
		Thread.sleep(2000);
		
		item_id = wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[1]//td[1]"))).getText();
		
		if(item_id.contains("No matching records found")) {
			
			System.out.printf("There is no existing notification. Please add one first.");
			Assert.assertTrue(false, "There is no existing notification. Please add one first.");
		}
		
		System.out.printf("\n***Please check the notification in DB before modification: ");
		selectSql="select id,status,notification_type,subject,document from tb_activity_notification where id=" + item_id + ";";
	    
	    String dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
		
	    //Click Edit
	    driver.findElement(By.cssSelector("tr:nth-child(1) td:nth-child(8) a.edit > span.label.label-success")).click();
	    Thread.sleep(500);
	    
	    //Update Subject
		oldSub=driver.findElement(By.id("subject")).getAttribute("value");
		driver.findElement(By.id("subject")).clear();
		driver.findElement(By.id("subject")).sendKeys("Update Subject by Automation " + Utils.randomSCString(2) + Utils.randomString(2));
		newSub=driver.findElement(By.id("subject")).getAttribute("value");
		
		System.out.print("Subject: Old = " + oldSub + "\n");
		System.out.print("Subject: New = " + newSub + "\n");
		
	    //Update notification type
		if(!Brand.equalsIgnoreCase("vfsc2") && !Brand.equalsIgnoreCase("cima")) {
		t=new Select(driver.findElement(By.id("type")));
		oldValue = t.getFirstSelectedOption().getText();
		
		switch(oldValue)
		{
			case "Maintenance Notification":
				t.selectByVisibleText("Notification");
				newValue = "Notification";
				break;
				
			case "Notification":
			default:
				t.selectByVisibleText("Maintenance Notification");	
				newValue = "Maintenance Notification";
		}
		System.out.print("Statue: Old = " + oldValue + "\n");
		System.out.print("Statue: New = " + newValue + "\n");		
		}
	    //Update enabled/disabled status
		Boolean checkOnclient = false;
		t=new Select(driver.findElement(By.id("status")));
		oldValue = t.getFirstSelectedOption().getText();
		
		switch(oldValue)
		{
			case "Disabled":
				t.selectByVisibleText("Enabled");
				newValue = "Enabled";
				checkOnclient = true;
				break;
				
			case "Enabled":
			default:
				t.selectByVisibleText("Disabled");	
				newValue = "Disabled";
		}
		System.out.print("Statue: Old = " + oldValue + "\n");
		System.out.print("Statue: New = " + newValue + "\n");
		
	    //Update Content
		oldValue=driver.findElement(By.cssSelector("div.ck.ck-content > p:nth-child(1)")).getText();
		System.out.print("Content: Old = " + oldValue + "\n");
		
		driver.findElement(By.cssSelector("div.ck.ck-content > p:nth-child(1)")).clear();
		driver.findElement(By.cssSelector("div.ck.ck-content > p:nth-child(1)")).sendKeys("Updated by automation with "+Utils.randomSCString(5) + Utils.randomString(5));
		
		newValue=driver.findElement(By.cssSelector("div.ck.ck-content > p:nth-child(1)")).getText();
		System.out.print("Content: New = " + newValue + "\n");
			    
	    		
		//Click Confirm button
		Thread.sleep(500);
		driver.findElement(By.xpath(".//button[text()='Save']")).click();
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		
		System.out.printf("\n***Please check the notification in DB AFTER modification: ");
		selectSql="select id,status,notification_type,subject,document from tb_activity_notification where id=" + item_id + ";";
	    
	    dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
	    
	    //Verify Notification in CP
	    if(checkOnclient) {
	    	funcCPVerifyNotification(driver, TraderURL, TraderName, TraderPass, newSub, newValue, Brand);
	    }
	    
		
	}

	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv", "AdminURL", "Brand"})
	void DeleteNotification(String TestEnv, String AdminURL, String Brand) throws Exception
	{

		String item_id="",selectSql="";
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		if(AdminURL.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.navigate().refresh();
		}else {
			driver.get(AdminURL);
		}
		
		
		
	    //Navigate to Role Management Page
		driver.findElement(By.linkText("System Setting")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("Notification Settings")).click();
		Thread.sleep(2000);
		
		item_id = wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[1]//td[1]"))).getText();
		
		if(item_id.contains("No matching records found")) {
			
			Assert.assertTrue(false, "There is no existing notification. Please add one first.");
		}
		
		//Print the notification going to be deleted
		System.out.printf("\n***Please check the notification in DB before delete: ");
		selectSql="select is_del,id,status,notification_type,subject,document from tb_activity_notification where id=" + item_id + ";";
	    
	    String dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
	    
		//Click Delete
	    driver.findElement(By.cssSelector("tr:nth-child(1) td:nth-child(8) a.delete")).click();
	    Thread.sleep(500);
		
		String resultMsg=wait10.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + resultMsg);
		Utils.funcIsStringContains(resultMsg, "Successful", Brand);
		
		//Print the notification had been deleted
		System.out.printf("\n***Please check the notification in DB after delete: ");
		selectSql="select is_del,id,status,notification_type,subject,document from tb_activity_notification where id=" + item_id + ";";
	    
	    dbName=Utils.getDBName(Brand)[1];
	    DBUtils.funcreadDB(dbName, selectSql, TestEnv);
	}

	
	
	/*
	 * Developed by Alex.L for verifying the notification in CP on 15/07/2020
	 */
	public static void funcCPVerifyNotification(WebDriver driver, String TraderURL, String TraderName, String TraderPass, String subject, String content, String Brand) throws Exception
	{
		//String maintain_content="Sorry for any inconvenience. If you have any further inquiries, please contact our support team support@vantagefx.com";
		
		//Login AU CP
		((JavascriptExecutor)driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
	    driver.switchTo().window(tabs.get(1)); //switches to new tab 
		driver.get(TraderURL);
		Thread.sleep(500);

		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);	

		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'HOME')]")));
		
		/*****Verify Notification in CP****/
		System.out.printf("\n*****Verify Notification in CP****\n");
		funcVerifyNotification(driver, subject, content, Brand);
		//close the pop up window (Not included in funcVerifyNotification due to different xpath in cp and ip)
		driver.findElement(By.xpath("//img[@data-testid='close']")).click();
			
		
		/*****Verify Notification in IB Portal****/
		System.out.printf("\n*****Verify Notification in IB Portal****\n");
		//If login account is IB account, default login page is IB portal. Switch back to Client Portal
		try {
			driver.findElement(By.xpath("//a[@class='swith_to']")).click();
			Thread.sleep(2000);
			//wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'WITHDRAW REBATE')]")));			
			funcVerifyNotification(driver, subject, content, Brand);
			
			//close the pop up window
			driver.findElement(By.xpath("//body/div/div/div/button/i[1]")).click();
			
		}catch (Exception e){
			System.out.printf("Not an IB account. No need to verify in IB portal.\n");
		}
		
		/*****Verify Notification in Login Page****/
		/*
		 * System.out.printf("\n*****Verify Notification in Login Page****\n");
		 * wait15.until(ExpectedConditions.elementToBeClickable(By.cssSelector(
		 * "div.login_inner.el-dropdown-selfdefine"))).click();
		 * wait15.until(ExpectedConditions.elementToBeClickable(By.
		 * xpath(".//span[text()='LOG OUT']"))).click();
		 * wait15.until(ExpectedConditions.elementToBeClickable(By.xpath(
		 * "//span[contains(text(),'LOGIN')]")));
		 * 
		 * //Verify the notification title
		 * Utils.funcIsStringContains(driver.findElement(By.xpath(
		 * "//span[@class='notification--unlogined']")).getText(), subject,
		 * Brand);
		 * 
		 * //Click on the notification
		 * driver.findElement(By.xpath(
		 * "//span[@class='notification--unlogined']")).click();
		 * Thread.sleep(500);
		 * 
		 * //Verify the subject and content
		 * Utils.funcIsStringEquals(driver.findElement(By.xpath(
		 * "//p[@class='title']")).getText(), subject, Brand);
		 * if(content.contains("local date and time format")) {
		 * 
		 * Utils.funcIsStringContains(driver.findElement(By.xpath(
		 * "//div[@class='dialog_body']//div//p")).getText(), maintain_content,
		 * Brand);
		 * }else {
		 * Utils.funcIsStringEquals(driver.findElement(By.xpath(
		 * "//div[@class='dialog_body']//div//p")).getText(), content, Brand);
		 * }
		 * //close the pop up window
		 * driver.findElement(By.xpath(
		 * "//div[@class='el-dialog']//div[@class='el-dialog__header']//div//img[@class='closeImg']"
		 * )).click();
		 */
		driver.close();
		driver.switchTo().window(tabs.get(0)); // switch back to main screen
    }
	
	
	
	public static void funcVerifyNotification(WebDriver driver, String subject, String content, String Brand) throws Exception
	{
		String maintain_content="Sorry for any inconvenience. If you have any further inquiries, please contact our support team support@vantagefx.com";
		//Verify the notification title
		Utils.funcIsStringContains(driver.findElement(By.xpath("//div[@class='notice']//span")).getText(), subject, Brand);
		
		//Click on the notification
		driver.findElement(By.xpath("//div[@class='notice']//span")).click();		
		Thread.sleep(500);
		
		//Verify the subject and content
		Utils.funcIsStringEquals(driver.findElement(By.xpath("//p[@class='title']")).getText(), subject, Brand);
		if(content.contains("local date and time format")) {
			
			Utils.funcIsStringContains(driver.findElement(By.xpath("//div[@class='dialog_body']//div//p")).getText(), maintain_content, Brand);
		}else {
			Utils.funcIsStringEquals(driver.findElement(By.xpath("//div[@class='dialog_body']//div//p")).getText(), content, Brand);
		}
    }

	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters(value= {"TestEnv", "Brand", "TraderName", "Account", "SendStatus", "ResendEmail"})
	public void EmailSearch(String TestEnv, String Brand, String TraderName, @Optional("") String Account, String SendStatus, String ResendEmail) throws Exception
	{
	
		EmailManagement emailManagementObject = new adminBase.EmailManagement(driver);
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		driver.navigate().refresh();
		
	    //Navigate to Email Management Page
		driver.findElement(By.linkText("System Setting")).click();
		Thread.sleep(500);
		driver.findElement(By.linkText("Email Management")).click();
		Thread.sleep(2000);
		
		//Search criteria can be changed accordingly
		emailManagementObject.setRecipients(TraderName);
		//emailManagementObject.setAccount(Account);
		emailManagementObject.selectSendStatus(SendStatus);
		Thread.sleep(500);
		
		emailManagementObject.clickSearchButton();
		Thread.sleep(2000);
		//Resend the search results to the provided email
		emailManagementObject.resendEmail(ResendEmail);
		Thread.sleep(2000);
		driver.close();
		
		
	}
}
