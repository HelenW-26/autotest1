package vantagecrm;

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

import io.github.bonigarcia.wdm.WebDriverManager;

public class UpdateIB {
	
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
		  
		  ChromeOptions options=new ChromeOptions();
		  options.setAcceptInsecureCerts(true);
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
		  
		  if(Boolean.valueOf(headless)) {
				options.addArguments("window-size=1920,1080");
				options.addArguments("headless");
			}
    	  driver = new ChromeDriver(options);	
    	  
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

	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand","TestEnv","TraderName"})
	public void resetPWD(String Brand, String TestEnv, String TraderName) throws Exception
	{
				
		int j;
		Select t;
		
		driver.navigate().refresh();
		
		List<WebElement> trs=funcChooseClient(Brand, TestEnv, TraderName);
		//Thread.sleep(1000);
		
		if(trs.size()==0)
		{
			Assert.assertTrue(false, "Loading list  error.");
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			Assert.assertTrue(false, "No IB (with name starting with " + Utils.ibUserPrefix + ") is found.");
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).getText();
						
			if(Utils.isTestIB(userName))
			{
				System.out.println("System is going to change Client Portal password for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			Assert.assertTrue(false, "No qualified IB (with name starting with " + Utils.ibUserPrefix+ ") is found.");
		}
		
		
		//Click the IB user name to open the client profile
		trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		//Update password
		driver.findElement(By.id("ib_password")).sendKeys(newPwd);
		
		//Click Submit button
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
			
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("Change Status is: " + a);
			
	}
	
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv","TraderName"})
	public void setCountry2Aus(String Brand, String TestEnv, String TraderName) throws Exception
	{
		boolean flag;
		flag=funcChangeCountry(Brand, TestEnv,TraderName, "Australia");
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		Assert.assertTrue(flag, "Failed to set country to Australia for user "+TraderName);
	}
	
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv","ThaiAccount"})
	public void setCountry2Thai(String Brand, String TestEnv,String ThaiAccount) throws Exception
	{
		boolean flag;
		flag = funcChangeCountry(Brand, TestEnv, ThaiAccount, "Thailand");
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		Assert.assertTrue(flag, "Failed to set country to Thailand for user "+ThaiAccount);
	}
	
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv","MalayAccount"})
	public void setCountry2Malay(String Brand, String TestEnv, String MalayAccount) throws Exception
	{
		boolean flag;
		flag=funcChangeCountry(Brand, TestEnv, MalayAccount, "Malaysia");
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));		
		
		Assert.assertTrue(flag, "Failed to set country to Malaysia for user "+MalayAccount);
	}
	
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv","VietAccount"})
	public void setCountry2Viet(String Brand, String TestEnv,String VietAccount) throws Exception
	{
		boolean flag;
		flag=funcChangeCountry(Brand, TestEnv, VietAccount, "Vietnam");
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		Assert.assertTrue(flag, "Failed to set country to VietNam for user "+VietAccount);
	}
	
	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand", "TestEnv","NigeAccount"})
	public void setCountry2Nige(String Brand, String TestEnv,String NigeAccount) throws Exception
	{
		boolean flag;
		flag=funcChangeCountry(Brand, TestEnv, NigeAccount, "Nigeria");
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		
		Assert.assertTrue(flag, "Failed to set country to Nigeria for user "+NigeAccount);
	}
	

	
	/*
	 * Update general client information: Address, Date of birth, Account Owner
	 */
	
	@Test(dependsOnMethods="AdminLogIn")
	@Parameters(value={"Brand","TestEnv","TraderName"})
	public void updateClientGeneral(String Brand, String TestEnv, String TraderName) throws Exception
	{
		int j;
		String oldDOB, oldValue, newValue;
		Select t;
		
		driver.navigate().refresh();
		
		List<WebElement> trs = funcChooseClient(Brand, TestEnv, TraderName);

		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
	
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No IB is found.");
	
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).getText();
			if(Utils.isTestIB(userName))
			{
				System.out.println("System is going to change general information for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified IB is found.");
	
		}
		
		
		//Click the user name to open the client profile
		trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		Thread.sleep(2000);			
	
		//Change Address
		
		String idValue="ib_street_adress";
		
			
		oldValue=driver.findElement(By.id(idValue)).getAttribute("value");
		System.out.println("Old Address is: "+oldValue);
		
		driver.findElement(By.id(idValue)).clear();
		driver.findElement(By.id(idValue)).sendKeys(Utils.randomNumber(2) + " " + Utils.randomString(4).toUpperCase() 
				+ " " + Utils.randomString(4).toUpperCase() + " Street");
		System.out.println("New Address is: "+driver.findElement(By.id(idValue)).getAttribute("value"));
		
		//Change  the DOB
		oldDOB=driver.findElement(By.id("birthday")).getAttribute("value");
		System.out.println("Old Birthday is: "+oldDOB);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dt=sdf.parse(oldDOB);
		Calendar calInst=Calendar.getInstance();
		calInst.setTime(dt);
		calInst.add(Calendar.DATE, -7);
		dt=calInst.getTime();
		
		String queryStr="jQuery('#birthday').val('" + sdf.format(dt).toString()+"');";
		System.out.println(queryStr);
		((JavascriptExecutor)driver).executeScript(queryStr);
		driver.findElement(By.id("birthday")).sendKeys(sdf.format(dt));
		System.out.println("New Birthday is: "+driver.findElement(By.id("birthday")).getAttribute("value"));
		
		
		//Change Account Owner when its role is level-1, change account owner to test***		
		
		t=new Select(driver.findElement(By.id("ib_Role")));
		if(t.getFirstSelectedOption().getText().equalsIgnoreCase("level-1"))
		{
		
			//t=new Select(driver.findElement(By.id("ib_Account_Owner")));
			t=new Select(driver.findElement(By.id("accountOwner")));
			
			oldValue=t.getFirstSelectedOption().getText();
			System.out.println("Old Account Owner is: "+oldValue);
			
			for(j=0; j<t.getOptions().size();j++)
			{
				
				newValue=t.getOptions().get(j).getText();
				if(newValue.startsWith(Utils.ibUserPrefix) && (!newValue.equalsIgnoreCase(oldValue)))
				{
					t.selectByIndex(j);
					break;
				}
					
			}
			
			if(j>=t.getOptions().size())
			{
				System.out.println("No qualified test account owner. Keep the old owner unchanged.");
			}else
			{
				System.out.println("New Account Owner is: "+ t.getFirstSelectedOption().getText());
			}
			
		}
				
		//Change Promotion
		t=new Select(driver.findElement(By.id("ib_Promotion")));
		oldValue=t.getFirstSelectedOption().getText();
		System.out.println("Old Promotion is: "+oldValue);
		
		for(j=0; j<t.getOptions().size();j++)
		{
			
			if(!t.getOptions().get(j).getText().equalsIgnoreCase(oldValue))
			{
				t.selectByIndex(j);
				break;
			}
				
		}
		
		if(j>=t.getOptions().size())
		{
			System.out.println("No qualified promotion. Keep the old promotion unchanged.");
		}else
		{
			System.out.println("New Promotion is: "+t.getFirstSelectedOption().getText());
		}
		
		
		//Upload WorldCheck file when there are less than 3 files uploaded.
		funcUploadWorkCk(driver, TestEnv, Brand);
		
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		
		//Print Status
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		System.out.println("The status is: " + a);
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				
	}



	@Test(dependsOnMethods="AdminLogIn", alwaysRun=true)
	void RequestAddiRebateAccount() throws Exception
	{
	
		Select t;
		List<WebElement> trs;
		WebElement rowRe;
		int i=0;
		
		
		Thread.sleep(waitIndex*1000);		
	    driver.navigate().refresh();
	    
	    //Navigate to External User Module
		driver.findElement(By.linkText("Users")).click();
		driver.findElement(By.linkText("External user")).click();
		
		//wait until the list is loaded
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		//Use "Client Name=testcrmib" to search test data
		t=new Select(driver.findElement(By.id("search_type")));
		t.selectByVisibleText("Client Name");
		
		driver.findElement(By.id("userQuery")).clear();
		driver.findElement(By.id("userQuery")).sendKeys(Utils.ibUserPrefix);
		
		driver.findElement(By.id("query")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));		
		
		//Get the searched result
		trs=driver.findElements(By.cssSelector("table#table tbody tr"));
		for(i=0;i<trs.size();i++)
		{
			rowRe=trs.get(i);
			//If IB is approved, request additional rebate account
			if(!rowRe.findElement(By.cssSelector("td:nth-of-type(5)")).getText().equals("-"))
			{
				
				System.out.println("Requesting Additional Rebate Account for: " + rowRe.findElement(By.cssSelector("td:nth-of-type(3)")).getText()); 
				
				rowRe.findElement(By.cssSelector("td:nth-last-of-type(2) a")).click();
				Thread.sleep(3000);					
							
				wait15.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.bootstrap-dialog-footer-buttons button.btn.btn-default:nth-of-type(1)"))).click();
				
				String resultMsg=wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
				Assert.assertEquals(resultMsg, "success");
				
				//wait until the list is loaded
				wait15.until(ExpectedConditions.visibilityOfElementLocated(By.className("fixed-table-loading")));
				wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
				
				break;
				
			}
		}
	}

	@Test(invocationCount=1,dependsOnMethods="AdminLogIn",alwaysRun=true)
	@Parameters(value= {"Brand","TestEnv","TraderName"})
	public void DeleteIB(String Brand, String TestEnv, String TraderName) throws Exception
	{
				
		int j;
		Select t;
		
		driver.navigate().refresh();
		
		List<WebElement> trs=funcChooseClient(Brand, TestEnv, TraderName);
		//Thread.sleep(1000);
		
		if(trs.size()==0)
		{
			Assert.assertTrue(false, "Loading list  error.");
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			Assert.assertTrue(false, "No IB (with name starting with " + Utils.ibUserPrefix + ") is found.");
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).getText();
						
			if(Utils.isTestIB(userName))
			{
				System.out.println("System is going to change Client Portal password for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			Assert.assertTrue(false, "No qualified IB (with name starting with " + Utils.ibUserPrefix+ ") is found.");
		}
		
		//Click the checkbox for delete
		trs.get(j).findElement(By.cssSelector("td:nth-of-type(1)>input")).click();
				
					
		//Click delete button
		driver.findElement(By.id("del_ibList")).click();
				
		//Confirm
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
				
		//Assert success
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
				
				
		System.out.println("Delete status is: " + a);
				
		String selectSql="select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name = '" + userName +"';";	
		String dbArray[]=Utils.getDBName(Brand);	
		DBUtils.funcreadDB(dbArray[1], selectSql, TestEnv); 
	}
	
	
	/*
	 * In Users -> External User, search with the specified keyword and return the search result.
	 *  If there is userName recorded in Utils, use it first. Otherwise, use TraderName. 
	*/
	List<WebElement> funcChooseClient(String Brand, String TestEnv, String TraderName) throws Exception
	{
		List<WebElement> trs=null;
		
		int j;	
		String keyword=TraderName;
		boolean flag=false;
		
		if((Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("fsa") || Brand.equalsIgnoreCase("svg")) && TestEnv.equalsIgnoreCase("beta"))
		{
			if(Utils.registerUserNameVT !=null)
			{
				if(Utils.registerUserNameVT.length()!=0)
				{
					keyword=Utils.registerUserNameVT;
					flag=true;
				}
			}
		}
		else
		{
			if(Utils.registerUserName !=null)
			{
				if(Utils.registerUserName.length()!=0)
				{
					keyword=Utils.registerUserName;
					flag=true;
				}
			}
		}
		
		//Navigate to Client menu
		wait15.until(ExpectedConditions.elementToBeClickable(By.linkText("Users")));
		driver.findElement(By.linkText("Users")).click();
		driver.findElement(By.linkText("External user")).click();
		
		//Wait until the list is loaded completely		
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		
		//Select "Search by real Name"
		Select t = new Select(driver.findElement(By.id("search_type")));
		
		if(flag==true)
		{
			t.selectByValue("real_name");
		}else
		{
			t.selectByValue("email");
		}
		
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
	
	
	//To change IB country of residence given login email and country name
	boolean funcChangeCountry(String Brand, String TestEnv, String loginEmail, String countryName) throws Exception
	{
		int j;
		Select t;
	
		driver.navigate().refresh();
		
		List<WebElement> trs=funcChooseClient(Brand, TestEnv, loginEmail);
		//Thread.sleep(1000);
		
		if(trs.size()==0)
		{
			System.out.println("Loading list  error.");
			return false;
		}
		
		if((trs.size()==1) && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No IB (with name starting with " + Utils.ibUserPrefix +") is found.");
			return false;
		}
		
		for(j=0;j<trs.size();j++)
		{
			userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).getText();
			if(Utils.isTestIB(userName))
			{
				System.out.println("System is going to change Country of Residence for user "+userName);
				break;
			}
		}
		
		if(j>=trs.size())
		{
			System.out.println("No qualified Client is found.");
			return false;
		}
		
		
		//Click the user name to open the IB profile
		trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)>a")).click();
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-heading")));
		
		//Choose Country. Need to wait until the whole list is loaded
		t=new Select(driver.findElement(By.id("ib_Country")));
		Thread.sleep(2000);
		
		System.out.println("The old country is: " + t.getFirstSelectedOption().getText());
		if(t.getFirstSelectedOption().getText().equals(countryName))
		{
			System.out.println("Country is already set. No need to modify.");
			
			//Click Cancel button
			driver.findElement(By.xpath(".//button[text()='Cancel']")).click();
				
			return true;
		}else
		{
			t.selectByVisibleText(countryName);  //Australia, Thailand, Viet Nam, Malaysia, Nigeria
		}	
		
		System.out.println("The new country is: " + t.getFirstSelectedOption().getText());
		
		//If country=Australia, the field state is a dropdownlist. Otherwise it is an input textbox
		if(countryName.equals("Australia"))
		{
			t=new Select(driver.findElement(By.id("ib_State")));
			wait15.until(ExpectedConditions.visibilityOf(t.getFirstSelectedOption()));
			t.selectByIndex(1);
			
		}else
		{
			wait15.until(ExpectedConditions.visibilityOfElementLocated((By.id("inputState"))));
			driver.findElement(By.id("inputState")).sendKeys(countryName + "Province");

		}
		
		//Need to reset the Annual Income field
		/*t=new Select(driver.findElement(By.id("ib_Income")));
		t.selectByIndex(3);
		*/
		//Click Submit button
		driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
			
		//Assert success
		String a = wait15.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.messenger-message-inner"))).getText();
		wait15.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.messenger-message-inner")));
		

		if(a.equals("success"))
		{
		
			System.out.println("Country is changed to " + countryName+" successfully.");
			//Wait until the list is loaded completely		
				
			return true;
		}else
		{
			
			System.out.println("Status is " + a);
			return false;
		}
		
	}

	
	
	//Yanni on 01/06/2020
	//Upload a World Check file if there are less than 3 files
	/*check the number of elements (div.col-zdy-5.worldCheck > div.col-md-11.input-box). 
	If NO = 2, then one img is already there. Number of images = Number of elements found - 1
	
	This function is also used by IBClient update.
	*/
	 
	public void funcUploadWorkCk(WebDriver driver, String TestEnv, String Brand) throws Exception
	{
		
		List<WebElement> imageList = driver.findElements(By.cssSelector("div.col-zdy-5.worldCheck > div.col-md-11.input-box"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", imageList.get(0));
		int j = imageList.size()-1;
		
		
		// Limit the number of world check files to 3 files. 
		if(j<3)
		{
			System.out.println("Client orginally has " + j + " World Check files. Uploading a new one... ");
			imageList.get(j).findElement(By.xpath(".//input[@type='file' and @data-name='worldCheck']")).sendKeys(Utils.workingDir+"\\proof.png");
			imageList.get(j).findElement(By.xpath(".//a[@href='/file/upload']")).click();
			Thread.sleep(1000);
			
			System.out.println("New file path: " + imageList.get(j).findElement(By.tagName("img")).getAttribute("src"));
		}else
		{
			System.out.println("Client already has " + j + " World Check files. NOT UPLOAD a new one... ");
		}
		
	}
}
