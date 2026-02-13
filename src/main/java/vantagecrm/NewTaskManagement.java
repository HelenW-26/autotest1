package vantagecrm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
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

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;

import java.lang.reflect.Method;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.openqa.selenium.NoSuchElementException;
import adminBase.TaskManagement;

public class NewTaskManagement {
	
	static WebDriver driver;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	String userName; //Client Name
	String IBName;  //IB Name
	String internalUserName; //Internal user name;
	Random tRandom=new Random();
	WebDriverWait wait03;	
	
	public static enum IDPOAType{
		
		AddrP("Proof of Address", 1, "needAddressProof", "tb_address_proof"),
		ID("Proof of ID", 2, "needIDProof", "tb_id_proof");
		
		private String menuName;
		private int menuIndex;	
		private String dbFlag;	
		private String dbName;
		
		private IDPOAType(String menuText, int menuIndex, String dbFlag, String dbName)
		{
			this.menuName = menuText;
			this.menuIndex = menuIndex;
			this.dbFlag = dbFlag;
			this.dbName = dbName;
			
		}
		
		public String getMenuName()
		{
			return this.menuName;
		}
		
		public int getMenuIndex()
		{
			return this.menuIndex;
					
		}
		
		public String getDBFlag()
		{
			return this.dbFlag;
					
		}
		public String getDBName()
		{
			return this.dbName;
		}
	}

	//Launch driver
	@BeforeClass(alwaysRun=true)
	//@Test(enabled=false)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		  
		  WebDriverManager.chromedriver().setup();
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);	
    	  
    	  utils.Listeners.TestListener.driver=driver;
    	  context.setAttribute("driver", driver);           //Added one parameter ITestContext by Yanni on 5/15/2019
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait03=new WebDriverWait(driver, Duration.ofSeconds(20));
	}
	
	@AfterClass(alwaysRun=true)
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
		ExtentTestManager.startTest(method.getName(),"Description: Login to Admin Portal");
		//Login AU admin
		driver.get(AdminURL);	
		Utils.funcLogInAdmin(driver, AdminName,AdminPass, Brand);
	}




		/*
		 * Author: Alex Liu
		 * Date: 04/10/2019
		 * This function is used for Credit Card information audit. 
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */
		public boolean funcCCDetailOperation(String Brand, String TestEnv, String Operation) throws Exception
		{
			//Navigate to Financial Information Audit page
			
			int j=0;
			Select t;
			String strPath;
			List<String> auditStatus = new ArrayList<String>();
			
			boolean flag=false;
			
			auditStatus.add("All Status");
			auditStatus.add("Submitted");
			auditStatus.add("Pending");
			auditStatus.add("Completed");
			auditStatus.add("Rejected");
			
			//Navigate to Account Audit page
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
					
			if (Brand.equalsIgnoreCase("fsa")|| Brand.equalsIgnoreCase("svg"))
			{
				wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Withdrawal Details"))).click();
			}else
				wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Financial Information Audit"))).click();
			
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			System.out.println("Start to " + Operation + " audit Credit Card Details Requests...");
			System.out.println("-------------------------------------------------------------");
		
			t=new Select(driver.findElement(By.id("statusQuery")));
			
			//Assert all status listed in the dropdown list: Submitted, Pending, Completed, Rejected
			
			for(j=0; j<t.getOptions().size();j++)
			{
				Assert.assertTrue(auditStatus.contains(t.getOptions().get(j).getText()), "Status is not listed as expected: " + t.getOptions().get(j).getText());
			}
			
			Assert.assertEquals(t.getOptions().size(), auditStatus.size(), "Status list has more status than expected.");
			
			System.out.println("Withdraw Details Status List check passed.");
			
			t.selectByVisibleText("Submitted");

			
			//choose type as Credit Card or UnionPay
			driver.findElement(By.id("typeQuery")).click();
			Thread.sleep(1000);
			
			if (Brand.equalsIgnoreCase("fsa")|| Brand.equalsIgnoreCase("svg"))
				driver.findElements(By.xpath("//a[contains(text(),'UnionPay')]")).get(1).click();
			else
				driver.findElement(By.xpath("//a[contains(text(),'Credit Card')]")).click();
			
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No Submitted Withdraw Details Records.");
			}else
			{
			
				for(j=0;j<trs.size();j++)
				{
					
					//Read each row's userName
					userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
					
										
					if(Utils.isTestIB(userName)||Utils.isWebUser(userName)||Utils.isJoint(userName)||Utils.isAddUser(userName))  //to check if it is a qualified test IB user
					{
											
						trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
						Thread.sleep(1000);
						
										
						//Edit Withdraw Details Information by OP
						funcCCUpdate();
						
						driver.findElement(By.id("processedNotes")).sendKeys("Automation " + Operation + " Test Credit Card Details for user "+userName 
								+ " "+Utils.randomSCString(10));
						
						
						strPath= ".//button[text()='" + Operation +"']";
						wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(strPath))).click();
						
						System.out.println(Operation+ " Credit Card Details for user "+userName);
						String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
						System.out.println("Operation Result:" + resultMsg);
						System.out.println();
						
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						
						
						//Check the user's withdraw details request status: should be the same as the operation. If different, it will fail
						String newStatus = funcCCDetailsCheckStatus(userName, Operation);
						
						Assert.assertTrue(newStatus.contains(Operation), "Credit Card Details Status is not changed to " + Operation + " as expected.");
						
						flag=true;
						break;
						
					}
				}
				
				if(j>=trs.size())
				{
					System.out.println("No Qualified Credit Card Details Audit Records.");
				}
				
			}	
		
			
			return flag;
			
			
		}
		
		/*
		 * Author: Alex Liu
		 * Date: 04/10/2019
		 * This function is used to return the status of a Credit Card Details specified for one user
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		public String funcCCDetailsCheckStatus(String userName, String Operation) throws Exception
		{
			String newStatus = "";
			
			Select t;
					
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			t=new Select(driver.findElement(By.id("statusQuery")));		
			
			switch(Operation)
			{
				case "Complete":
					t.selectByVisibleText("Completed");
					break;
					
				case "Pending":
					t.selectByVisibleText("Pending");
					break;
					
				case "Reject":
				
					t.selectByVisibleText("Rejected");
					break;
					
				default:
					System.out.println("Operation is not in Complete/Pending/Reject status list.");
			}
			
			//choose type as Credit Card
			driver.findElement(By.id("typeQuery")).click();
			// update for UnionPay on 8 Jan, 2020 by Fiona
			Boolean isPresent = driver.findElements(By.xpath("//a[contains(text(),'Credit Card')]")).size()>0;
			if (isPresent)
				driver.findElement(By.xpath("//a[contains(text(),'Credit Card')]")).click();
			
			//Select Search Options: Account Name
			driver.findElement(By.id("searchType")).click();
			driver.findElement(By.linkText("Account Name")).click();
			
			//Input userName
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(userName);
			
			//Click button
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("Can't find any records for user " + userName + " with status "+ Operation);
			}else
			{
				//Get the Operation of latest record
				newStatus = trs.get(0).findElement(By.cssSelector("td:nth-of-type(7)")).getText();
				System.out.println("Check New Status. New Status is " + newStatus);
			}
			
			return newStatus;
		}
		
		/*
		 * Author: Alex Liu
		 * Date: 04/10/2019
		 * This function is used to Audit (Complete) a Submitted Credit Card Details request
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv"})
		
		public void CreditCardDetailsAudit(String Brand, String TestEnv) throws Exception
		{
			
			String Operation="Complete";
			Assert.assertTrue(funcCCDetailOperation(Brand, TestEnv, Operation), "Failed to audit Credit Card Details.");
		}
		
		
		/*
		 * Author: Alex Liu
		 * Date: 04/10/2019
		 * This function is used to Pending a Submitted Withdraw Details request
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv"})
		
		public void CreditCardDetailsPending(String Brand, String TestEnv) throws Exception
		{
			
			String Operation="Pending";
			Assert.assertTrue(funcCCDetailOperation(Brand, TestEnv, Operation), "Failed to pending Credit Card Details.");
		}
		
		
		/*
		 * Author: Alex Liu
		 * Date: 04/10/2019
		 * This function is used to Reject a Submitted Withdraw Details request
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv"})
		
		public void CreditCardDetailsReject(String Brand, String TestEnv) throws Exception
		{
			
			String Operation="Reject";
			Assert.assertTrue(funcCCDetailOperation(Brand, TestEnv, Operation), "Failed to reject Credit Card Details.");
		}
	
		
		/*
		 * Author: Alex Liu
		 * Date: 04/10/2019
		 * This function is to verify Search function via Account Name
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv"})		
		public void CreditCardDetailsSearchByName(String Brand, String TestEnv) throws Exception
		{
			//Navigate to CreditCard Details Audit page
			
			int j=0;
			boolean flag = false;
			String searchUserName;
	
			//Navigate to Account Audit page
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
			if (Brand.equalsIgnoreCase("fsa")|| Brand.equalsIgnoreCase("svg"))
				wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Withdrawal Details"))).click();
			else
				wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Financial Information Audit"))).click();
			
			Thread.sleep(2500);
		
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No CreditCard Details Records.");
				Assert.assertTrue(false, "No CreditCard Details Records. Can't verify Search function.");
			}
			
			System.out.println("Start to verify Search function via Account Name....");
			
			//Read first row's userName
			userName=trs.get(0).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
			
			//choose type as Credit Card
			driver.findElement(By.id("typeQuery")).click();
			if (Brand.equalsIgnoreCase("pug"))
				driver.findElements(By.xpath("//a[contains(text(),'UnionPay')]")).get(1).click();
			else
				driver.findElement(By.xpath("//a[contains(text(),'Credit Card')]")).click();
			
			//Change to Search by Account Name
			driver.findElement(By.id("searchType")).click();
			driver.findElement(By.linkText("Account Name")).click();
			
			//Use the user name to do search
			
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(userName);
			
			//Click Search button
			driver.findElement(By.id("query")).click();
			
			Thread.sleep(1000);
				
			//Read search result
			trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("Search function failed with username " + userName);
				Assert.assertTrue(false, "Search function failed with username " + userName);
			}
				
			for(j=0; j<trs.size(); j++)
			{
				
				searchUserName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
				flag = searchUserName.equalsIgnoreCase(userName);
				if(flag == false)
				{
					System.out.println("Searched with username " + userName + " but got " + searchUserName);
				}
				Assert.assertTrue(flag, "Searched with username " + userName + " but got " + searchUserName);;
			}
		}
		
		public void funcCheckList(List<WebElement> t, List<String> auditStatus, String operation)
		{
			int j;
			
			for(j=0; j<t.size();j++)
			{
				Assert.assertTrue(auditStatus.contains(t.get(j).getText()), "Status is not listed as expected: " + t.get(j).getText());
			}
			
			Assert.assertEquals(t.size(), auditStatus.size(), "Status list has more status than expected.");
			
			System.out.println("      " + operation + " List check passed.");
			
		}
		

		
		/*
		 * Developed by Alex.L for verification of items in operations of UnionPay withdraw Audit on 15/07/2019
		 * Example:
		 * String coo = driver.manage().getCookies().toString();
		 * funcVerifyCCDetail(AdminURL,"CPS456", coo);
		 */
		public void funcVerifyCCDetail(String AdminURL, String orderNo, String cookie) throws Exception
		{				
			String displayValue;
			String url;
			//remove the admin/main from AdminURL
			
			url=Utils.ParseInputURL(AdminURL);
			
			//System.out.println(url);
			String cpsDetail = RestAPI.testPostWithdrawCPSList(url, orderNo, cookie);
			
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(cpsDetail);
			JSONObject obj =(JSONObject)resultObject;
			System.out.println("==================Result====================");

			JSONArray data = (JSONArray)obj.get("rows");
			for (Object item : data) {  
			  for (Object key: ((JSONObject) item).keySet()) {
            	Object value = ((JSONObject) item).get(key.toString());
            	
            	String ele_id = key.toString();
            	
            	//Only check the item that exist on the web page 
            	try 
            	{
            		displayValue = driver.findElement(By.id(ele_id)).getAttribute("value");
                	System.out.println("key: "+ele_id+"\t display value is: "+displayValue+"\t jason value is: "+value.toString());
                	
                	//Assert.assertEquals(displayValue, value.toString());
            	}catch (NoSuchElementException e) {
            		System.out.println("**********");
            	}
            	
			  }	
			  
    		}

		}
		

		/*
		 * Developed by Alex Liu for Credit Card Record Update in Admin on 04/10/2019
		 * When Records are in Submitted/Pending status, OP can update the records and save. This function is to update different fields and click the button
		 * 
		 */
		public void funcCCUpdate() throws Exception
		{
			
			String oldValue="", cardHolderName = "", oldProvince="", oldCity = "", newValue="";
			Random r=new Random();
			Select tBank, tProvince, tCity;
			int index=0;

			//Update cardHolderName by adding 'OP'
			oldValue=driver.findElement(By.id("cardHolderName")).getAttribute("value");
			driver.findElement(By.id("cardHolderName")).sendKeys("OP");
			newValue=driver.findElement(By.id("cardHolderName")).getAttribute("value");
			
			System.out.print("cardHolderName: Old = " + oldValue + "\n");
			System.out.print("cardHolderName: New = " + newValue + "\n");
			
			
			/*//Update Phone Number by adding '99'
			oldValue=driver.findElement(By.id("phoneNumber")).getAttribute("value");
			driver.findElement(By.id("phoneNumber")).sendKeys("99");
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
			
			//Input Processed Notes
			driver.findElement(By.id("processedNotes")).sendKeys("Automation - Edit Credit Card Audit Details!");*/
			
			//Upload a replacement photo
			driver.findElements(By.xpath("//input[@type='file']")).get(1).sendKeys(Utils.workingDir+"\\proof.png");
						
			if (driver.findElements(By.xpath("//a[@title='Upload selected files']")).size()>0)
				driver.findElements(By.xpath("//a[@title='Upload selected files']")).get(1).click();
			
			Thread.sleep(1000);
			
			//Print old image path and new image path
			List<WebElement> imgList= driver.findElements(By.cssSelector("img.pull-left.input-img.input_img"));
			System.out.println("Old Image Source is: " + imgList.get(0).getAttribute("src"));
			System.out.println("New Image Source is: " + imgList.get(1).getAttribute("src"));
			
			
		}

		/*
		 * Author: Yanni Qi
		 * Date: 12/11/2019
		 * This function is used to Audit (Complete) a Submitted Credit Card Details request
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void POAAudit(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			String Operation="Complete",fName;
			IDPOAType searchType = IDPOAType.AddrP;
			int numberOfEmail = 1;
			
			//Yanni on 18/05/2020: if test context has email, use that email as a priority. Otherwise use TraderName.
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in./");
			}
			
			Assert.assertTrue(funcIDPOAOperation(Brand, TestEnv, Operation, searchType, TraderName), "Failed to audit Address Proof Record.");
		
			//Get needAddressProof value from DB
/*			System.out.println();
			System.out.println("After Audit is done, the flag needAddressProof should be set to 0.");
			funcGetIDPOAFlag(Brand, TestEnv, TraderName, searchType);*/
			
/*			//Verify POAStatus in Global DB
			fName = TraderName.substring(0, TraderName.indexOf("@"));
			System.out.println("\n****Please verify POAStatus should be 1 ****\n");
			DBUtils.checkDBStatus(fName, TestEnv, Brand);
			
			//Check Email:
			Thread.sleep(2000);
			System.out.println();
			System.out.println("Check Email Sent Records: ");	
			Utils.readEmail(TraderName.toLowerCase(), Brand, TestEnv, numberOfEmail);
*/
		}

		/*
		 * Author: Yanni Qi
		 * Date: 12/11/2019
		 * This function is to Pending a Submitted Address Proof request
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		
		public void POAPending(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			String Operation="Pending";
			IDPOAType searchType = IDPOAType.AddrP;
			
			//Yanni on 18/05/2020: if test context has email, use that email as a priority. Otherwise use TraderName.
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in.");
			}
			
			Assert.assertTrue(funcIDPOAOperation(Brand, TestEnv, Operation, searchType,TraderName), "Failed to Pending Address Proof Record.");
		}

		/*
		 * Author: Yanni Qi
		 * Date: 12/11/2019
		 * This function is  to Reject a Submitted Address Proof request
		 * The 1st qualified record( record is in Submitted status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		
		public void POAReject(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			String Operation="Reject",fName;
			IDPOAType searchType = IDPOAType.AddrP;
	/*		int numberOfEmail = 1;*/
			
			//Yanni on 18/05/2020: if test context has email, use that email as a priority. Otherwise use TraderName.
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in.");
			}		
			
			Assert.assertTrue(funcIDPOAOperation(Brand, TestEnv, Operation, searchType,TraderName), "Failed to reject Address Proof Record.");
			
/*			//Get needAddressProof value from DB
			System.out.println();
			System.out.println("After Reject, the flag needAddressProof is still kept 1.");
			funcGetIDPOAFlag(Brand, TestEnv, TraderName, searchType);*/
			
/*			//Verify POAStatus in Global DB
			fName = TraderName.substring(0, TraderName.indexOf("@"));
			System.out.println("\n****Please verify POAStatus should be 0 ****\n");
			DBUtils.checkDBStatus(fName, TestEnv, Brand);
			
			//Check Email:
			Thread.sleep(2000);
			System.out.println();
			System.out.println("Check Email Sent Records: ");			
			Utils.readEmail(TraderName.toLowerCase(), Brand, TestEnv, numberOfEmail);*/
		}

		/*
		 * Author: Yanni Qi
		 * Date: 12/11/2019
		 * This function is to verify Address Proof Search function via Account Name
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv"})		
		public void POASearchByName(String Brand, String TestEnv) throws Exception
		{
			//Set searchType as Address Proof
			IDPOAType searchType = IDPOAType.AddrP;			
			funcIDPOASearchByName(Brand, TestEnv, searchType);
		}

		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void IDProofAudit(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			String Operation="Complete",fName;
			IDPOAType searchType = IDPOAType.ID;
			/*int numberOfEmail = 1;*/
			
			//Yanni on 18/05/2020: if test context has email, use that email as a priority. Otherwise use TraderName.
	
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in. Will user TraderName in XML cofiguration");
			}
			
			//Assert.assertTrue(funcIDPOAOperation(Brand, TestEnv, Operation, searchType,TraderName), "Failed to audit ID Proof Record.");
			funcIDPOAOperation(Brand, TestEnv, Operation, searchType,TraderName);
			/*
			//Get needIDProof value from DB
			System.out.println();
			System.out.println("After Audit is done, the flag needIDProof should be set to 0.");
			funcGetIDPOAFlag(Brand, TestEnv, TraderName, searchType);
			*/
			


		}
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void IDProofPending(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			String Operation="Pending";
			IDPOAType searchType = IDPOAType.ID;
			
	
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in.");
			}
			
			Assert.assertTrue(funcIDPOAOperation(Brand, TestEnv, Operation, searchType,TraderName), "Failed to Pending ID Proof Record.");

		}
		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void IDProofReject(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			String Operation="Reject",fName;
			IDPOAType searchType = IDPOAType.ID;
			int numberOfEmail = 1;
			
			//Yanni on 18/05/2020: if test context has email, use that email as a priority. Otherwise use TraderName.
		
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in.");
			}
			
			Assert.assertTrue(funcIDPOAOperation(Brand, TestEnv, Operation, searchType,TraderName), "Failed to reject ID Proof Record.");
			
			/*
			//Get needIDProof value from DB
			System.out.println();
			System.out.println("After Reject, the flag needIDProof is still kept 1.");
			funcGetIDPOAFlag(Brand, TestEnv, TraderName, searchType);
			*/
			
/*			//Verify ID3CheckStatus in Global DB
			fName = TraderName.substring(0, TraderName.indexOf("@"));
			System.out.println("\n****Please verify ID3CheckStatus should be 0 ****\n");
			DBUtils.checkDBStatus(fName, TestEnv, Brand);
			
			//Check Email:
			Thread.sleep(2000);
			System.out.println();
			System.out.println("Check Email Sent Records: ");			
			Utils.readEmail(TraderName.toLowerCase(), Brand, TestEnv, numberOfEmail);*/
		}
		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv"})		
		public void IDProofSearchByName(String Brand, String TestEnv) throws Exception
		{
			//Set searchType as Address Proof
			IDPOAType searchType = IDPOAType.ID;			
			funcIDPOASearchByName(Brand, TestEnv, searchType);
		}
		
		

		/*
		 * Author: Alex Liu
		 * Date: 28/04/2020
		 * This function is to add POA record for specific user by email.
		 */		
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void AddPOA(String Brand, String TestEnv, String TraderName) throws Exception
		{
			IDPOAType searchType = IDPOAType.AddrP;
			
			funcAddingIDPOAProof(TraderName, searchType);
		
			//Show latest record for ID/POA in DB
			System.out.println();
			System.out.println("Going to show the latest POA proof record...");
			funcShowDBIDPOAProof(Brand, TestEnv, searchType);
			

		}
		

		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void AddIDProof(String Brand, String TestEnv, String TraderName) throws Exception
		{
			IDPOAType searchType = IDPOAType.ID;
			
			funcAddingIDPOAProof(TraderName, searchType);
		
			//Get needIDProof value from DB
			System.out.println();
			System.out.println("Going to show the latest POA proof record...");
			funcShowDBIDPOAProof(Brand, TestEnv, searchType);
			

		}
		
		
		
		
		/*
		 * Yanni Qi on 12/11/2019
		 * Provided with searchType, this function will do search  test in Task Management -> ID & POA Audit module
		 */
		public void funcIDPOASearchByName(String Brand, String TestEnv, IDPOAType searchType) throws Exception
		{
			//Navigate to CreditCard Details Audit page
			
			int j=0;
			boolean flag = false;
			String searchUserName;
		
			//Navigate to Account Audit page
			/*
			 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
			 * linkText("Task Management"))).click();
			 * wait03.until(ExpectedConditions.visibilityOfElementLocated(By.
			 * linkText("ID & POA Audit"))).click();
			 */
			driver.findElement(By.linkText("Task Management")).click();
			driver.findElement(By.linkText("ID & POA Audit")).click();
			
			Thread.sleep(2500);
		
			//Click All Types button to show the dropdown list
			driver.findElement(By.id("typeQuery")).click();
			driver.findElement(By.linkText(searchType.getMenuName())).click();
			
			//Click Search button
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No Records of " + searchType.getMenuName());
				Assert.assertTrue(false, "No Records of " + searchType.getMenuName() + ". Can't verify Search function.");
			}
			
			System.out.println("Start to verify Search function via Account Name....");
			
			//Read first row's userName
			userName=trs.get(0).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
			
			//choose type as SearchType
			driver.findElement(By.id("typeQuery")).click();
			driver.findElement(By.linkText(searchType.getMenuName())).click();
			
			//Change to Search by Account Name
			driver.findElement(By.id("searchType")).click();
			driver.findElement(By.linkText("Account Name")).click();
			
			//Use the user name to do search
			
			driver.findElement(By.id("userQuery")).clear();
			driver.findElement(By.id("userQuery")).sendKeys(userName);
			
			//Click Search button
			driver.findElement(By.id("query")).click();
			
			Thread.sleep(1000);
				
			//Read search result
			trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("Search function failed with username " + userName);
				Assert.assertTrue(false, "Search function failed with username " + userName);
			}
				
			for(j=0; j<trs.size(); j++)
			{
				
				searchUserName = trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
				flag = searchUserName.equalsIgnoreCase(userName);
				if(flag == false)
				{
					System.out.println("Searched with username " + userName + " but got " + searchUserName);
				}
				Assert.assertTrue(flag, "Searched with username " + userName + " but got " + searchUserName);;
			}
		}

		
		/*
		 * Author: Yanni Qi
		 * Date: 12/11/2019
		 * This function is for Address Proof audit. 
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 */
		public boolean funcIDPOAOperation(String Brand, String TestEnv, String Operation, IDPOAType searchType, String TraderName) throws Exception
		{
			//Navigate to Financial Information Audit page
			
			int j=0;
			Select t;
			String strPath, userEmail="";
			List<String> auditStatus = new ArrayList<String>();
			int numberOfEmail=1,reasonIndex=0;
			Random r = new Random();
			
			boolean flag=false;
			
			auditStatus.add("All Status");
			auditStatus.add("Submitted");
			auditStatus.add("Pending");
			auditStatus.add("Completed");
			auditStatus.add("Rejected");
			auditStatus.add("Re-Audit");
			
			//Navigate to Account Audit page
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("ID & POA Audit"))).click();
			
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			System.out.println("Start to " + Operation + " Proof of Address/ID Requests...");
			System.out.println("-------------------------------------------------------------");
		
			t=new Select(driver.findElement(By.id("statusQuery")));
			
			//Assert all status listed in the dropdown list: Submitted, Pending, Completed, Rejected
			
			for(j=0; j<t.getOptions().size();j++)
			{
				Assert.assertTrue(auditStatus.contains(t.getOptions().get(j).getText()), "Status is not listed as expected: " + t.getOptions().get(j).getText());
			}
			
			Assert.assertEquals(t.getOptions().size(), auditStatus.size(), "Status list has more status than expected.");
			
			System.out.println("ID & POA Audit Status List check passed.");
			
			Thread.sleep(500);
			t.selectByVisibleText("Submitted");
			Thread.sleep(2000);
			
			//choose type as Address Proof/ID
			driver.findElement(By.id("typeQuery")).click();
			driver.findElement(By.linkText(searchType.getMenuName())).click();
			
			//Search with email when TraderName is not empty
			if(TraderName.length()>0)   //Added by Yanni on 19/05/2020 when no TraderName is provided.
			{
				driver.findElement(By.id("searchType")).click();
				driver.findElement(By.xpath("//ul[@class='dropdown-menu bj']//a[contains(text(),'Email')]")).click();		
				driver.findElement(By.id("userQuery")).sendKeys(TraderName);
			}else
			{
				System.out.println("TraderName is empty. System is going to process 1st test user in list.");
			}
			
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No Submitted ID & POA Audit Records.");
			}else
			{
			
				for(j=0;j<trs.size();j++)
				{
					
					//Read each row's userEmail
			
					userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
					userEmail=trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();
										
					if(Utils.isTestUser(userName))  //to check if it is a qualified test user
					{
											
						trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
						Thread.sleep(1000);
						
						//Check File Paths
						checkProofPaths(searchType);
										
						//Edit Address Proof/ID by OP
						//funcIDPOAUpdate(searchType.getMenuName());
						if(Operation.equals("Pending")) {
							//Select Pending reason and output pending reason
							Select pendingReason = new Select(driver.findElement(By.id("pending_reason")));
							pendingReason.selectByIndex(r.nextInt(6));
							Thread.sleep(300);
							System.out.println("Pending Reason:" + pendingReason.getFirstSelectedOption().getText() );
						}
						
						
						
						driver.findElement(By.id("processedNotes")).sendKeys("Automation " + Operation + " Test Address Proof/ID for user "+userName 
								+ " "+Utils.randomSCString(10));
						
						
						strPath= ".//button[text()='" + Operation +"']";
						wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(strPath))).click();
						
						System.out.println(Operation+ " " + searchType.menuName + "  for user "+userEmail);
						String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
						System.out.println("Operation Result:" + resultMsg);
						System.out.println();
						
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						
						
						//Check the user's withdraw details request status: should be the same as the operation. If different, it will fail
						String newStatus = funcIDPOACheckStatus(userEmail, Operation, searchType);
						
						Assert.assertTrue(newStatus.contains(Operation), "POA/ID Status is not changed to " + Operation + " as expected.");
						
						
						//Moved this block from its parent and put it here.
						//Don't update DB and send email when status is Pending. 
						if(!Operation.equalsIgnoreCase("Pending")) 
						{
							//Verify ID3CheckStatus in Global DB
							if(searchType == IDPOAType.ID)
							{
								if(Operation.equalsIgnoreCase("Complete"))
								{
									System.out.println("\n****Please verify ID3CheckStatus should be 1 ****\n");
								}else if(Operation.equalsIgnoreCase("Reject"))
								{
									System.out.println("\n****Please verify ID3CheckStatus should be 0 ****\n");
								}
							}else if(searchType==IDPOAType.AddrP)
							{
								if(Operation.equalsIgnoreCase("Complete"))
								{
									System.out.println("\n****Please verify POAStatus should be 1 ****\n");
								}else if(Operation.equalsIgnoreCase("Reject"))
								{
									System.out.println("\n****Please verify POAStatus should be 0 ****\n");
								}
							}
							
							DBUtils.checkDBStatus(userName, TestEnv, Brand);
							
							//Check Email:
							Thread.sleep(2000);
							System.out.println();
							System.out.println("Check Email Sent Records: ");	
							DBUtils.readEmailvUserName(userName, Brand, TestEnv, numberOfEmail);

						}
						
						flag=true;
						break;
						
					}
				}
				
				if(j>=trs.size())
				{
					System.out.println("No Qualified Credit Card Details Audit Records.");
				}
				
			}	
		
			
			return flag;
			
			
		}

		/*
		 * Author: Yanni Qi
		 * Date: 12/11/2019
		 * This function is used to return the status of Address Proof/ID specified for one user
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 * Operation={"Complete/Pending/Reject"}
		 * 
		 * Replaced userName with userEmail because username will change after ID audit. 
		 *  ---Updated by Alex Liu
		 */		
		public String funcIDPOACheckStatus(String userEmail, String Operation, IDPOAType searchType) throws Exception
		{
			String newStatus = "";
			
			Select t;
					
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			t=new Select(driver.findElement(By.id("statusQuery")));		
			
			switch(Operation)
			{
				case "Complete":
					t.selectByVisibleText("Completed");
					break;
					
				case "Pending":
					t.selectByVisibleText("Pending");
					break;
					
				case "Reject":
				
					t.selectByVisibleText("Rejected");
					break;
				case "Submitted":
					
					t.selectByVisibleText("Submitted");
					break;	
				default:
					System.out.println("Operation is not in Complete/Pending/Reject status list.");
			}
			
			//choose type as searchType
			driver.findElement(By.id("typeQuery")).click();
			driver.findElement(By.linkText(searchType.getMenuName())).click();
			
			//Select Search Options: Account Name
			driver.findElement(By.id("searchType")).click();
			//driver.findElement(By.linkText("Account Name")).click();
			driver.findElement(By.linkText("Email")).click();
			
			//Input userName
			driver.findElement(By.id("userQuery")).clear();
			//driver.findElement(By.id("userQuery")).sendKeys(userName);
			driver.findElement(By.id("userQuery")).sendKeys(userEmail);
			
			//Click button
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("Can't find any records for user" + userEmail + " with status "+ Operation);
			}else
			{
				//Get the Operation of latest record
				newStatus = trs.get(0).findElement(By.cssSelector("td:nth-of-type(9)")).getText();
				System.out.println("Check New Status. New Status is " + newStatus);
			}
			
			return newStatus;
		}

		/*
		 * NEED UPDATE FOR ID PROOF AUDIT PAGE
		 */
		public void funcIDPOAUpdate(String menuName) throws Exception
		{
			
			String oldValue="", newValue="";
			String country,proofTag = "";
			int index=0;
			Select t;
			Random r = new Random();
		    Boolean flag = false;
		
			if(menuName.equals("Proof of Address")) {
				
				proofTag = "address";
				
				//Get country
				country = driver.findElement(By.id("country")).getAttribute("value");
				
				//Update Province/State. If country <> Australia, do try. else, do catch.
							
				if(country.equalsIgnoreCase("Australia"))
				{
					t = new Select(driver.findElement(By.id("state")));
					index = t.getOptions().size();
					oldValue = t.getFirstSelectedOption().getText();
					
					t.selectByIndex(r.nextInt(index-1)+1);
					while(t.getFirstSelectedOption().getText().equalsIgnoreCase(oldValue))
					{
						t.selectByIndex(r.nextInt(index-1)+1);
					}
					
					newValue = t.getFirstSelectedOption().getText();
					
				}else
				{
					oldValue = driver.findElement(By.id("state")).getAttribute("value");
					driver.findElement(By.id("state")).sendKeys("OP");
					newValue=driver.findElement(By.id("state")).getAttribute("value");
				}
				
			
				System.out.print("Province/State: Old = " + oldValue + "\n");
				System.out.print("Province/State: New = " + newValue + "\n");
				
				
				//Update City/Suburb
				oldValue=driver.findElement(By.id("citySuburb")).getAttribute("value");
				driver.findElement(By.id("citySuburb")).sendKeys("OP");
				newValue=driver.findElement(By.id("citySuburb")).getAttribute("value");
				
				System.out.print("City/Suburb: Old = " + oldValue + "\n");
				System.out.print("City/Suburb: New = " + newValue + "\n");
				
				//Update Address
				oldValue=driver.findElement(By.id("address")).getAttribute("value");
				driver.findElement(By.id("address")).sendKeys("OP");
				newValue=driver.findElement(By.id("address")).getAttribute("value");
				
				System.out.print("Address: Old = " + oldValue + "\n");
				System.out.print("Address: New = " + newValue + "\n");
				
				//Update Postcode
				oldValue=driver.findElement(By.id("postcode")).getAttribute("value");
				driver.findElement(By.id("postcode")).sendKeys("11");
				newValue=driver.findElement(By.id("postcode")).getAttribute("value");
				
				System.out.print("Postcode: Old = " + oldValue + "\n");
				System.out.print("Postcode: New = " + newValue + "\n");
				
			}else if (menuName.equals("Proof of ID")) {
				
				proofTag = "id";
				
				//Update First Name
				oldValue=driver.findElement(By.id("firstName")).getAttribute("value");
				driver.findElement(By.id("firstName")).clear();
				driver.findElement(By.id("firstName")).sendKeys(oldValue.substring(0, oldValue.length() - 1) + Utils.randomString(1));
				newValue=driver.findElement(By.id("firstName")).getAttribute("value");
				
				System.out.print("First Name: Old = " + oldValue + "\n");
				System.out.print("First Name: New = " + newValue + "\n");
				
				//Update Middle Name
				oldValue=driver.findElement(By.id("middleName")).getAttribute("value");
				driver.findElement(By.id("middleName")).clear();
				driver.findElement(By.id("middleName")).sendKeys(oldValue.substring(0, oldValue.length() - 1) + Utils.randomString(1));
				newValue=driver.findElement(By.id("middleName")).getAttribute("value");
				
				System.out.print("Middle Name: Old = " + oldValue + "\n");
				System.out.print("Middle Name: New = " + newValue + "\n");
				
				//Update Last Name
				oldValue=driver.findElement(By.id("lastName")).getAttribute("value");
				driver.findElement(By.id("lastName")).clear();
				driver.findElement(By.id("lastName")).sendKeys(oldValue.substring(0, oldValue.length() - 1) + Utils.randomString(1));
				newValue=driver.findElement(By.id("lastName")).getAttribute("value");
				
				System.out.print("Last Name: Old = " + oldValue + "\n");
				System.out.print("Last Name: New = " + newValue + "\n");
				
				//Update Date Of Birth
				oldValue=driver.findElement(By.id("dateOfBirth")).getAttribute("value");
				driver.findElement(By.cssSelector("#datetimepicker1 span")).click();
				wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table-condensed")));
				driver.findElement(By.cssSelector("table.table-condensed>tbody>tr:nth-of-type(1)>td:nth-of-type(6)")).click();
				newValue=driver.findElement(By.id("dateOfBirth")).getAttribute("value");
				
				System.out.print("Date Of Birth: Old = " + oldValue + "\n");
				System.out.print("Date Of Birth: New = " + newValue + "\n");
				
			}
			

			//Input Processed Notes
			//driver.findElement(By.id("processedNotes")).sendKeys("Automation - Edit Proof of address Details!");
			
			//Upload a replacement photo
			driver.findElement(By.xpath("//input[@type='file' and @data-name='replacement"+ proofTag +"ProofDoc']")).sendKeys(Utils.workingDir+"\\proof.png");
			driver.findElements(By.xpath("//a[@title='Upload selected files']")).get(1).click();
			Thread.sleep(1000);
			
			//Upload the second replacement photo if exists
			if (driver.findElements(By.xpath("//input[@type='file' and @data-name='replacement"+ proofTag +"ProofDoc']")).size() > 1) 
			{
				driver.findElement(By.id("replacementIdProof1")).sendKeys(Utils.workingDir+"\\proof.png");
				driver.findElements(By.xpath("//a[@title='Upload selected files']")).get(3).click();
				Thread.sleep(1000);
				
				//set flag as true if there are two files
				flag = true;
			}
			
			//Print old image path and new image path
			List<WebElement> imgList= driver.findElements(By.cssSelector("img.pull-left.input-img.input_img"));
			System.out.println("Old Image Source is: " + imgList.get(0).getAttribute("src"));
			System.out.println("New Image Source is: " + imgList.get(1).getAttribute("src"));
			
			if (flag==true) {
				System.out.println("The Second Old Image Source is: " + imgList.get(2).getAttribute("src"));
				System.out.println("The Second New Image Source is: " + imgList.get(3).getAttribute("src"));
				
			}
			
		}

		public void funcCheckIDPOAList(List<WebElement> t, List<String> auditStatus, String operation)
		{
			int j;
			
			for(j=0; j<t.size();j++)
			{
				Assert.assertTrue(auditStatus.contains(t.get(j).getText()), "Status is not listed as expected: " + t.get(j).getText());
			}
			
			Assert.assertEquals(t.size(), auditStatus.size(), "Status list has more status than expected.");
			
			System.out.println("      " + operation + " List check passed.");
			
		}


		public static void funcGetIDPOAFlag(String Brand, String TestEnv, String TraderName, IDPOAType searchType) throws Exception
		{
			String selectSql = "select real_name,"+ searchType.getDBFlag() +" from tb_user_outer where real_name like '%XXXX%';";
			String realName = TraderName.substring(0, TraderName.indexOf("@"));
			
			selectSql = selectSql.replace("XXXX", realName);
			
			System.out.println("Check Database records in " + TestEnv + ":" + selectSql);
			
		    String dbName = Utils.getDBName(Brand)[1];
		    DBUtils.funcreadDB(dbName,selectSql, TestEnv);		    
		
		}
		
		public static void funcShowDBIDPOAProof(String Brand, String TestEnv, IDPOAType searchType) throws Exception
		{
			String selectSql = "select * from " + searchType.getDBName() + " order by id desc limit 1;";
			
			System.out.println("Check Database records in " + TestEnv + ":" + selectSql);
			
		    String dbName = Utils.getDBName(Brand)[1];
		    DBUtils.funcreadDB(dbName,selectSql, TestEnv);		    
		
		}
		
		/*
		 * Author: Alex Liu
		 * Date: 28/04/2020
		 * This function is for adding ID proof or POA proof. 
		 */
		public void funcAddingIDPOAProof(String TraderName, IDPOAType searchType) throws Exception
		{
			//Navigate to ID/POA Audit page

			Select t;
			
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("ID & POA Audit"))).click();
			
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			System.out.println("Start to add " + searchType.getMenuName() + " ...");
			System.out.println("-------------------------------------------------------------");
		
			driver.findElement(By.id("creation")).click();
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.className("modal-dialog")));
			
			driver.findElement(By.id("email")).sendKeys(TraderName);
			
			//choose type as Address Proof/ID
			t=new Select(driver.findElement(By.id("idProofType")));
			t.selectByIndex(searchType.getMenuIndex()-1);
			
			//Upload two files
			driver.findElement(By.xpath("//input[@name='upload_file']")).sendKeys(Utils.workingDir+"\\proof.png");
			driver.findElement(By.xpath("//a[@title='Upload selected files']")).click();
			Thread.sleep(500);
			
			driver.findElements(By.xpath("//input[@name='upload_file']")).get(1).sendKeys(Utils.workingDir+"\\proof.png");
			driver.findElements(By.xpath("//a[@title='Upload selected files']")).get(1).click();
			Thread.sleep(1000);
			
			driver.findElement(By.xpath("//button[text()='Submit']")).click();
			
			//Verify message:
			System.out.println("Result is: " + driver.findElement(By.className("messenger-message-inner")).getText());
			
			String newStatus = funcIDPOACheckStatus(TraderName, "Submitted", searchType);
	          						
	        Assert.assertTrue(newStatus.contains("Submitted"), "POA/ID record was not added successffully.");
	        
			
		}
		
		
		//Yanni on 25/05/2020, if there are more than 1 file attached, this function check whether the 2 files have different paths.
		public void checkProofPaths(IDPOAType searchType)
		{
			
			String selAddress = ".//div[contains(@class,'clearfix addressProofDoc')]";
			String selID = ".//div[contains(@class,'clearfix idProofDoc')]";
			String xpathSel="";
			String[] pathArray;
			
			List<WebElement> proofList = null;
			
			switch(searchType)
			{
				case AddrP:
					xpathSel = selAddress;
					break;
					
				case ID:
					xpathSel = selID;
					break;
					
					default:
						System.out.println(searchType.getMenuName() + " is NOT supported in function checkProofPaths!");
						Assert.assertTrue(false, "IDPOAType is not supported in function checkProofPaths!");
			}
			
			proofList = driver.findElements(By.xpath(xpathSel));
			driver.findElements(By.xpath(".//div[contains(@class,'clearfix addressProofDoc')]"));
			
			if (proofList.size()>=1)
			{
				
				System.out.println("\nStarting Proof Paths Check... ");
				pathArray = new String[proofList.size()];
				for(int i=0; i<proofList.size(); i++)
				{
					pathArray[i] = proofList.get(i).findElement(By.tagName("img")).getAttribute("src");
					System.out.println("  No." + i + " proof: " + pathArray[i]);
				}
				
				//Compare if there are 2 identical paths
					
				for(int i=0; i<pathArray.length; i ++)
				{
					for(int j=i+1; j<pathArray.length; j++ ) 
					{
						if(pathArray[i].equalsIgnoreCase(pathArray[j]))
						{
							Assert.assertTrue(false, "2 Proof Files have identical paths. It's wrong.");
						}
					}
				}
				
				System.out.println("Passed Proof Paths Check. Not Identifical. ");
			}else
			{
				System.out.println("No Proof file is attached.");
			}
			
			
			//ID files attachement: div[ contains(@class, 'clearfix idProofDoc')]
			//Address proof files attachment: "div[contains(@class,'clearfix addressProofDoc')]"
		}

	
		//Developed by Yanni on 3/07/2020: Navigate to Task Management -》 ID & POA Audit module
		void funcGo2IDPOA(String Brand, String TestEnv) throws Exception
		{
			//Navigate to Account Audit page
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Task Management"))).click();
		
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("ID & POA Audit"))).click();
			
			Thread.sleep(500);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
		}
		
		List<WebElement> funSearchIDPOA(String Brand, IDPOAType idpoaType, String idpoaStatus, String searchOption, String searchKeyword) throws Exception
		{
			
			String searchOptSel="";
			
			//Input Status: Submitted/Pending/Completed/Rejected
			Select t=new Select(driver.findElement(By.id("statusQuery")));
			t.selectByVisibleText(idpoaStatus);
			Thread.sleep(2000);
			
			//choose type as Address Proof/ID
			driver.findElement(By.id("typeQuery")).click();
			driver.findElement(By.linkText(idpoaType.getMenuName())).click();
			
			//Search searchOption = searchKeyword; when both searchOption && searchKeyword are null,  use default search
			if(searchKeyword.length()>0 && searchOption.length()>0)   //Added by Yanni on 19/05/2020 when no TraderName is provided.
			{
				searchOptSel="//ul[@class='dropdown-menu bj']//a[contains(text(),'" + searchOption + "')]";
				driver.findElement(By.id("searchType")).click();
				driver.findElement(By.xpath(searchOptSel)).click();		
				driver.findElement(By.id("userQuery")).sendKeys(searchKeyword);
			}else
			{
				System.out.println("No Search Keyword is specified. System is going to process 1st test user in list.");
			}
			
			//Click Search button
			driver.findElement(By.id("query")).click();
			Thread.sleep(1000);
			wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fixed-table-loading")));
			
			List<WebElement> trs=driver.findElements(By.xpath(".//table[@id='table']/tbody/tr"));
			
			return  trs;
		}

		/*
		 * Author: Yanni Qi
		 * Date: 3/07/2020
		 * This function is to Pending a Submitted ID/POA request. Then search in PENDING status and trigger difficult Pending reasons. Check email at last.
		 * The 1st qualified record( record is in Audit status, userName is a test user) will be picked up for process
		 *  */		
		
		public Boolean funcIDPOAPendingReason(String Brand, String TestEnv, IDPOAType idpoaType, String searchStatus, String searchOption, String searchKeyword, int reasonIndex) throws Exception
		{
			
			String Operation="Pending";
			TaskManagement taskMgtEle = new TaskManagement(driver, Brand);
			
			//Search results
			List<WebElement> trs = null;
			
			//Pending Reason control in ID/POA AUDIT PANEL
			Select pendingReason;
			
			//Pending button selector
			String pendButtonSel;
			
			///function return flag
			Boolean flag = false;
			
			int j=0;			
			String userName, userEmail;			

			
			//Number of emails checked
			int numberOfEmail = 1;
			
			//Go to IDPOA MODULE
			funcGo2IDPOA(Brand, TestEnv);
			
			//Search  records with specified criteria
			trs = funSearchIDPOA(Brand, idpoaType, searchStatus, searchOption, searchKeyword);
			
			//if the search result shows no records:
			if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
			{
				System.out.println("No Submitted ID & POA Audit Records.");
			}else
			{
			
				for(j=0;j<trs.size();j++)
				{
					
					//Read each row's userEmail
			
					userName=trs.get(j).findElement(By.cssSelector("td:nth-of-type(2)")).getText();
					userEmail=trs.get(j).findElement(By.cssSelector("td:nth-of-type(3)")).getText();
										
					if(Utils.isTestUser(userName))  //to check if it is a qualified test user
					{
											
						trs.get(j).findElement(By.cssSelector("td:last-of-type a")).click();
						Thread.sleep(1000);
						
						//Select Pending reason and output pending reason
						pendingReason = new Select(driver.findElement(By.id("pending_reason")));
						pendingReason.selectByIndex(reasonIndex);
						Thread.sleep(300);
						System.out.println("Pending Reason:" + pendingReason.getFirstSelectedOption().getText() );
						
						//If pending reason is the last option: other, input Custom Pending Reason
						if(reasonIndex == taskMgtEle.getIDPOAPendingOtherReasonIndex())
						{
							driver.findElement(By.id("custom_pending_reason")).sendKeys("Austomation Custom Pending Reason");
						}
						
						//Clear Processed Notes if it has
						driver.findElement(By.id("processedNotes")).clear();
						
						//Input Processed Notes		
						driver.findElement(By.id("processedNotes")).sendKeys("Pending Reason:" + pendingReason.getFirstSelectedOption().getText() + "  for user "+userName 
								+ " "+Utils.randomSCString(10));
		
						//Click PENDING button
						pendButtonSel= ".//button[text()='" + Operation +"']";
						wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(pendButtonSel))).click();
						
						System.out.println(Operation+ " " + idpoaType.menuName + "  for user "+userEmail);
						String resultMsg=wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner"))).getText();
						System.out.println("Operation Result:" + resultMsg);
						System.out.println();
						
						wait03.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div div.messenger-message-inner")));
						
						
						//Check the user's withdraw details request status: should be the same as the operation. If different, it will fail
						String newStatus = funcIDPOACheckStatus(userEmail, Operation, idpoaType);
						
						Assert.assertTrue(newStatus.contains(Operation), "POA/ID Status is not changed to " + Operation + " as expected.");
						
						//Check Email:
						Thread.sleep(2000);
						System.out.println();
						System.out.println("Check Email Sent Records: ");	
						DBUtils.readEmailvUserName(userName, Brand, TestEnv, numberOfEmail);
						System.out.println();
																		
						flag=true;
						break;
						
					}
				}
				
				if(j>=trs.size())
				{
					System.out.println("No Qualified Credit Card Details Audit Records.");
				}									
		
			}
			
			return flag;
		}


		public void loopIDProofPendingReason(String Brand, String TestEnv, IDPOAType idpoaType, String searchOption, String searchKeyword) throws Exception
		{
			
			String searchStatus = "Submitted";
			int numOfPendingReason = 7;
			
			//i==1: pending reason = none, can't use it for Pending
			
			for (int i=1; i<numOfPendingReason; i++)
			{
			
				//Process one submitted and make pending reason the default one. Next go to Pending view and trigger other pending options
				if(i>1)
				{
					searchStatus = "Pending";
				}
				Assert.assertTrue(funcIDPOAPendingReason(Brand, TestEnv, idpoaType, searchStatus, searchOption, searchKeyword, i), "Failed to Pending ID Proof Record.");
			}
		
		}
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void IDProofPendingReason(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			IDPOAType idpoaType = IDPOAType.ID;
			String searchOption="";
			String searchKeyword="";
			
		
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in.");
			}
			
			
			if(TraderName.length()>0)
			{
				searchOption = "Email";
				searchKeyword = TraderName;
			}
			
			loopIDProofPendingReason(Brand, TestEnv, idpoaType, searchOption, searchKeyword);
		
		}
		
		@Test(dependsOnMethods = "AdminLogIn", alwaysRun=true)
		@Parameters(value= {"Brand", "TestEnv", "TraderName"})
		public void POAPendingReason(String Brand, String TestEnv, String TraderName, ITestContext context) throws Exception
		{
			
			IDPOAType idpoaType = IDPOAType.AddrP;
			String searchOption="";
			String searchKeyword="";
			
		
			try
			{
				if(context.getAttribute("TraderName").toString().length()>0)
				{
					TraderName = context.getAttribute("TraderName").toString();
				}	
			}catch(NullPointerException e)	
			{
				System.out.println("No ITestContext is passed in.");
			}
			
			
			if(TraderName.length()>0)
			{
				searchOption = "Email";
				searchKeyword = TraderName;
			}
			
			loopIDProofPendingReason(Brand, TestEnv, idpoaType, searchOption, searchKeyword);
		
		}
}
