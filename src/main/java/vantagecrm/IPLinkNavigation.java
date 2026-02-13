package vantagecrm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;


/*
 * This class is to test the displayed Rebate and Total Value are consistent between Home Page & Rebate Report
 */

public class IPLinkNavigation {

	WebDriver driver;
	String IpURL="";
	WebDriverWait wait50;
	ArrayList<String> winHandles;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	int dateIndex=0;
	
	SoftAssert saAssert=new SoftAssert();
	

	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  context.setAttribute("driver", driver);
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
	  	wait50=new WebDriverWait(driver, Duration.ofSeconds(1));
	
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderName", "TraderPass", "Brand", "TraderURL"})
	void IPLogIn(String TraderName, String TraderPass, String Brand, String TraderURL) throws Exception
	
	{
		driver.get(TraderURL);
 		
  	  //Login AU IB Portal and keep Home Page
		Utils.funcLogInIP(driver, TraderName, TraderPass, Brand);
		
		IpURL=driver.getCurrentUrl();
					
	}
	
	/*
	 * Yanni: navigate via sidebar menu, verify the link works	
	 */
	@Test(dependsOnMethods="IPLogIn")
	@Parameters(value="Brand")
	void SidebarLinks(String Brand) throws Exception
	{
		
		String cssSel, cssOrg="";
		String menuText;
		String[] keywords;
	
		//Verify the sidebar should exist 
		ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='el-menu-vertical el-menu']"));
		Thread.sleep(1000);
		
		//Click to hide the sidebar
		Utils.waitUntilLoaded(driver);
		//wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img#menu_open_button.menu_toogle"))).click();
		//No menu hide/display button for VT anymore
		if (!Brand.equalsIgnoreCase("vt"))
		{
			wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img#menu_open_button"))).click();
			Thread.sleep(1000);
			//Verify the sidebar should not exist (fold away)
			ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ul[@class='el-menu-vertical el-menu']"));
			
			//To show sidebar menu
			driver.findElement(By.cssSelector("img#menu_open_button")).click();	
			
			//Allow sometime for menu to popup
			Thread.sleep(500);
			keywords=new String[] {"DASHBOARD","IB REPORT","REBATE REPORT","MULTI-LEVEL IB","IB ACCOUNTS","PENDING ACCOUNTS","LEADS","UNFUNDED ACCOUNTS","PAYMENT HISTORY","IB PROFILE","REFERRAL LINKS","CONTACT US"};
		} else {
			keywords=new String[] {"Dashboard","IB report","Rebate report","Multi-level IB","IB accounts","Pending accounts","Leads","Unfunded accounts","Payment history","IB profile","Referral links","Contact us"};
			}
			
		//Menu position, sublink, page h2 title, h2 title css position
		String arrayLink[][]= {
				{"2","ibReport",keywords[1],"div.contact_title"},
				{"3","rebateReport",keywords[2],"div.contact_title"},
				{"4","iblevel",keywords[3],"div.contact_title"},
				{"5","ibAccounts",keywords[4],"div.contact_title"},
				{"6", "pendingAccounts",keywords[5],"div.contact_title"},
				{"7","leads",keywords[6],"div.contact_title"},
				{"8","unfundedAccounts",keywords[7],"div.contact_title"},
				{"9","RebatePaymentHistory",keywords[8],"div.contact_title"},
				{"10","settings",keywords[9],"div.contact_title"},
				{"11","ReferralLinks",keywords[10],"div.contact_title"},
				{"12","contactUs",keywords[11],"div.contact_title"}
				};
		
		for(int i=0; i<arrayLink.length;i++)
		{
			
			//Get cssSelector path
			switch(Brand)
			{
			case "vt":
					int t = Integer.parseInt(arrayLink[i][0])-1;
					cssSel = "ul.el-menu-vertical.el-menu>div:nth-of-type(2)>div:nth-of-type("+ t +")>li";
				break;
				
				default:
					cssSel="ul.el-menu-vertical.el-menu>li:nth-of-type("+arrayLink[i][0]+")";
			}

			//Click the link
			Thread.sleep(1500);
			wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSel))).click();
			
			//Allow time for navigation
			Thread.sleep(1500);
			wait50.until(ExpectedConditions.urlContains(arrayLink[i][1]));
			
			//Get cssSelector path for page title
			cssSel=arrayLink[i][3]+">h2";
			wait50.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSel)));
			menuText=driver.findElement(By.cssSelector(cssSel)).getText();
			System.out.println("Test Navigation of " + menuText + " and compare with keywords " + arrayLink[i][2]);	
	
			saAssert.assertEquals(menuText, arrayLink[i][2], menuText+" failed");

		}
			
		/*//Click to show the sidebar
		//driver.findElement(By.cssSelector("img#menu_open_button.menu_toogle")).click();
		driver.findElement(By.cssSelector("img#menu_open_button")).click();
		Thread.sleep(1000);
		//Verify the sidebar should exist
		ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='el-menu-vertical el-menu']"));*/
		
		//clicking dashboard to return back to home page
		Thread.sleep(500);
				
		driver.findElement(By.xpath(".//span[text()='"+keywords[0]+"']")).click();
		Thread.sleep(500);
		wait50.until(ExpectedConditions.urlContains("home"));
		
		String keyword = driver.findElement(By.cssSelector("div#home h2")).getText();
		System.out.println("Compare keyword " + keyword + " with "+keywords[0]);
		saAssert.assertEquals(keyword, keywords[0], keyword+" failed");
		saAssert.assertAll();
	}
	
	
	/*
	 * Yanni on 26/09/2019: verify IB links are correct
	 */
	@Test(dependsOnMethods="IPLogIn", alwaysRun=true)
	@Parameters(value="Brand")
	void testIBLinks(String Brand) throws Exception
	{
		
		List<WebElement> dropdownListAcc, dropdownListLang,  linksBox = null;
		WebElement inputAccount, inputLang;
		String accountNo, lang;
				
		//navigate to IB Portal home page
		driver.navigate().to(IpURL);
		Thread.sleep(2000);	
		//wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));
		
		Thread.sleep(2000);
	
		//navigate to IB Links page
		if (Brand.equalsIgnoreCase("vt")) {
			driver.findElement(By.xpath("//span[text()='Referral links']")).click();
		} else {
			driver.findElement(By.xpath("//span[text()='REFERRAL LINKS']")).click();
		}
		Thread.sleep(2000);
		//wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar>div.calendar_box>div.el-loading-mask"), "display", "none"));
				
		//Get inputAccount and inputLang boxes
		inputAccount = driver.findElements(By.cssSelector("input.el-input__inner")).get(0);
		inputLang=driver.findElements(By.cssSelector("input.el-input__inner")).get(1);

		
		//Get default values of two input boxes
		accountNo= ((JavascriptExecutor) driver).executeScript("return arguments[0].value", inputAccount).toString();
		lang= ((JavascriptExecutor) driver).executeScript("return arguments[0].value", inputLang).toString();
		
		if (Brand.equalsIgnoreCase("vt")) {
			accountNo=accountNo.split("-")[0].trim();
		}

		System.out.println(accountNo + " + " + lang);
			
		//IB Account selection: click Account List to show the dropdown list
		Thread.sleep(1000);
		driver.findElements(By.xpath("//div[@class='el-input el-input--suffix']")).get(0).click();
		
		//Get dropdown items
		Thread.sleep(1000);
		dropdownListAcc = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		
		//System.out.println("IB Account list: " + dropdownListAcc.size());
		//If IB has multiple accounts, the 1st one in the list should be displayed by default.		
		if(accountNo.length()==0 )
		{
			Assert.assertTrue(dropdownListAcc.size()==0, "When IB has rebate accounts, the first account should be displayed by default."
					+ " When IB has no account, default should be Select");
		
		}
		
		//Click Language dropdown list to get language options
		driver.findElement(By.xpath("//div[@class='el-input el-input--suffix']")).click();
		Thread.sleep(1000);
		dropdownListLang = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		
			
		//To make language dropdown list diappear by clicking h2 title REFERAL LINKS
		driver.findElement(By.cssSelector("div.contact_title h2")).click();
		
		//Verify each account-language combinations with loops
		for(int i=0; i < dropdownListAcc.size(); i++)
		{
			//Select IB Account and get selected account No.
			driver.findElements(By.xpath("//div[@class='el-input el-input--suffix']")).get(0).click();
			Thread.sleep(1000);
			dropdownListAcc = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
			dropdownListAcc.get(i).click();
			Thread.sleep(1000);
			
			inputAccount = driver.findElements(By.cssSelector("input.el-input__inner")).get(0);
			
			accountNo= ((JavascriptExecutor) driver).executeScript("return arguments[0].value", inputAccount).toString();
			if (Brand.equalsIgnoreCase("vt")) {
				accountNo=accountNo.split("-")[0].trim();
			}
			
			for(int j=0; j < dropdownListLang.size(); j++)
			{
				//Select Language and get selected language
				driver.findElements(By.xpath("//div[@class='el-input el-input--suffix']")).get(1).click();
				Thread.sleep(1000);
				dropdownListLang = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
				
				//languages are Indonesian and Arabic which needs to be displayed after pulling scroll bar
				if(j>6)
				{
					((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", dropdownListLang.get(j));
					Thread.sleep(500);
				}
				
				
				dropdownListLang.get(j).click();
				Thread.sleep(1000);
				
				inputLang=driver.findElements(By.cssSelector("input.el-input__inner")).get(1);
				
				
				lang= ((JavascriptExecutor) driver).executeScript("return arguments[0].value", inputLang).toString();
				
				//Get all links
				if(Brand.equalsIgnoreCase("vt")) {
					linksBox = driver.findElements(By.cssSelector("div.main>p"));
				} else {
					linksBox = driver.findElements(By.cssSelector("div.fl>p"));
				}
				//Verify Live Account link
				verifyIBLinks(linksBox.get(0).getText(), accountNo, lang, "live", Brand);
				
				//Verify Demo Account link
				verifyIBLinks(linksBox.get(1).getText(), accountNo, lang, "demo", Brand);
				
				//Verify Website Home Page
				verifyIBLinks(linksBox.get(2).getText(), accountNo, lang, "website", Brand);
				
				System.out.println("Passed check for IB Links of account " + accountNo + " for language " + lang );
			}
			
			
		}
	}
	
	/*
	 * 
	 */
	
	@Test(dependsOnMethods="IPLogIn", alwaysRun=true)
	@Parameters(value="Brand")
	void navBnIPCP(String Brand) throws Exception
	{
		String cpTitle = "Secure Client Portal";
		String ibTitle = "Secure IB Portal";
		
		//Go back to Home Page
		driver.navigate().to(IpURL);
		
		System.out.println("Going to navigate to Client Portal...");
		//Click User Name in the top right corner
		Thread.sleep(2000);
		wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.login_inner.el-dropdown-selfdefine"))).click();
		System.out.println("Success.");
		Thread.sleep(500);
		//Switch to Client Portal

		if(Brand.equalsIgnoreCase("vt")) {
			driver.findElement(By.linkText("Back to Client Portal")).click();
		} else {
			driver.findElement(By.xpath(".//span[text()='BACK TO CLIENT PORTAL']")).click();
		}
		wait50.until(ExpectedConditions.titleContains(cpTitle));
		
		//In Client Portal, click button "SWITCH TO IB PORTAL"
		System.out.println("Going to navigate to IB Portal...");
		Utils.waitUntilLoaded(driver);
		//wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.el-scrollbar__view a.swith_to"))).click();
		driver.findElement(By.xpath("//a[@class='swith_to']")).click();
		Thread.sleep(2000);
		wait50.until(ExpectedConditions.titleContains(ibTitle));
		System.out.println("Success.");
		
	}
	void verifyIBLinks(String links, String accountNo, String lang, String type, String Brand)
	{
		String langIden=getLangIdentifier(lang);
		String auDomainName = "https://www.vantagefx.com.au/";
		String kyDomainName="https://www.vantagefx.com/";
		String vtDomainName="https://www.vtmarkets.com/";
		String vtDomainChineseName="https://zh.vtmarkets.com/";
		String pugDomainName="https://www.puprime.com/";
		
		String domainName = Utils.ParseInputURL(links);
	
		//IB Links should have correct protocol and domain name
		switch(Brand)
		{
			case "au":
				Assert.assertTrue(domainName.startsWith(auDomainName), "Domain Name " + domainName + " is wrong for brand " + Brand);
				break;
				
			case "ky":
			case "vfsc":
			case "vfsc2":
			case "regulator2":
				Assert.assertTrue(domainName.startsWith(kyDomainName), "Domain Name " + domainName + " is wrong for brand " + Brand);				
				break;
				
				
			case "vt":
				if (lang.equalsIgnoreCase("chinese"))
					Assert.assertTrue(domainName.startsWith(vtDomainChineseName), "Domain Name " + domainName + " is wrong for brand " + Brand);
				else
					Assert.assertTrue(domainName.startsWith(vtDomainName), "Domain Name " + domainName + " is wrong for brand " + Brand);				
				break;
				
			case "fsa":
			case "svg":
				Assert.assertTrue(domainName.startsWith(pugDomainName), "Domain Name " + domainName + " is wrong for brand " + Brand);				
				break;
				
			default:
				System.out.println("Brand " + Brand + "is not supported.");
		}
		
		
		//IB links should contain "?affid="
		if(!links.contains("?affid="+accountNo))
		{
			System.out.println("IB Links should contain ?affid=" + accountNo);
			Assert.assertTrue(false,"IB Links should contain ?affid=" + accountNo );
		}
		
		//IB links should contain language identifier when lang <> English
		if(langIden.length()==0)
		{
			if(!links.contains(langIden))
			{
				System.out.println("IB Links should contain language identifier " + langIden);
				Assert.assertTrue(false,"IB Links should contain language identifier " + langIden );
			}
		}
		
		switch(type)
		{
			case "live":
				if(!links.contains("trading"))
				{
					System.out.println(type + " IB Links should contain trading.");
					Assert.assertTrue(false, type + " IB Links should contain trading." );
				}
				break;
				
			case "demo":
				if(!links.contains("demo"))
				{
					System.out.println(type + " IB Links should contain demo.");
					Assert.assertTrue(false, type + " IB Links should contain demo." );
				}
				break;
				
			case "website":
				default:
					
		}
		
	
	}
	
	String getLangIdentifier(String lang)
	{
		String langIden="";
		
		switch(lang)
		{
			case "English":
				langIden="";
				break;
			case "Chinese":
				langIden="cn";
				break;
			case "French":
				langIden="fr";
				break;
			case "Thai":
				langIden="th";
				break;
			case "German":
				langIden="de";
				break;
			case "Spain":
				langIden="es";
				break;
			case "Malay":
				langIden="my";
				break;
			case "Vietnamese":
				langIden="vn";
				break;
			case "Indonesia":
				langIden="id";
				break;
			case "Arabic":
				langIden="ar";
				break;
				
				default:
					langIden="";
		}
		
		return langIden;
	}
	
	
	@AfterClass(alwaysRun=true)
	void ExitBrowser() throws Exception
	{
		
		Utils.funcLogOutIP(driver);
		//Close all browsers
		driver.quit();
	}
}
