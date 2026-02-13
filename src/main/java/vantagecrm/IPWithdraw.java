package vantagecrm;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import clientBase.ipNavigationBar;
import clientBase.ipWithdraw;
import clientBase.withdrawI18N;
import io.github.bonigarcia.wdm.WebDriverManager;


/*
 * This class is to test all withdraw methods in Trader
 */

public class IPWithdraw {

	WebDriver driver;	
	String userID;
	WebDriverWait wait10;
	
	ipWithdraw ipw;
	ipNavigationBar ipnb;
	JavascriptExecutor executor;
	Actions action;
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	int waitIndex=1; 
	
	Select t;  //Account Dropdown
	Random r=new Random(); 
	int j; //Select index
	
	//Withdraw index: which percentage of account balance is going to be withdrawn
	BigDecimal withdrawPartIndex = new BigDecimal ("0.10");
	BigDecimal withdrawFullIndex = new BigDecimal ("1.00");
	BigDecimal minimumAmount = new BigDecimal ("40.00");
			
	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  //System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  WebDriverManager.chromedriver().setup();
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	
		  context.setAttribute("driver", driver);
		  
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
	  	wait10=new WebDriverWait(driver, Duration.ofSeconds(10));
	  	executor = (JavascriptExecutor)driver;
	  	action = new Actions(driver);
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderName", "TraderPass", "Brand", "TraderURL"})
	void IPLogIn(String TraderName, String TraderPass, String Brand, String TraderURL) throws Exception
	
	{
		ipw = new ipWithdraw(driver, Brand);
		ipnb = new ipNavigationBar(driver, Brand);
		
  	  //Login AU IB Portal
		driver.get(TraderURL);
		Utils.funcLogInIP(driver, TraderName, TraderPass, Brand);
		
		userID = TraderName;
		Thread.sleep(5000);
		
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through AU Bank Transfer
	void WithdrawAUBank(String Brand) throws Exception
	{
		WebElement formWithdraw=null, menuSelected = null;
		List<WebElement> menuGroup, t;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		String withdrawMethod = "Bank Transfer", option;
		
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
			
		//After selection of withdraw method, 2nd form will change accordingly. Find form element again.
		formWithdraw = driver.findElements(By.cssSelector("div.form_container form")).get(1);
		
		//Country/Region: choose Australia
		Thread.sleep(1000);
		formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(1) li:nth-of-type(1) input.el-input__inner")).click();
		Thread.sleep(500);
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		
		//Choose which menugroup is popup
		for(int i=0;i<menuGroup.size();i++)
		{
			//If the div is set to display
			if(!menuGroup.get(i).getAttribute("style").contains("display: none"))
			{
				
				menuSelected = menuGroup.get(i);
				
				t=menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));
				

				j=t.size()-1;
					
				while(j>=0)
				{
					//System.out.println("Country/Region Selection loop :" + j);
					option=t.get(j).findElement(By.tagName("span")).getText();
					if(option.equals("Australia"))
					{
						t.get(j).findElement(By.tagName("span")).click();
						break;
					}else
					{
						j--;
					}

	
				}
			}
		}
	
		Thread.sleep(1000);
		
		//After selection of Australia, 2nd form will change accordingly. Find form element again.
		formWithdraw = driver.findElements(By.cssSelector("div.form_container form")).get(2);
	
		//Input Bank name		
		WebElement s=formWithdraw.findElement(By.cssSelector(".form_list_inner:nth-child(1) li:nth-child(1) .el-form-item__content > .el-input > .el-input__inner"));
		
		wait10.until(ExpectedConditions.visibilityOf(s)).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");

		//Input Bank Account Number
		formWithdraw.findElement(By.cssSelector(".clearfix:nth-child(1) > .clearfix > li:nth-child(2) .el-input__inner")).sendKeys(Utils.randomNumber(8));
		
		//Input BSB
		formWithdraw.findElement(By.cssSelector(".form_list_inner:nth-child(2) li:nth-child(1) .el-input__inner")).sendKeys(Utils.randomNumber(4));

		//Input Swift
		formWithdraw.findElement(By.cssSelector(".form_list_inner:nth-child(2) li:nth-child(2) .el-input__inner")).sendKeys(Utils.randomNumber(4));
		
		//Input Bank Beneficiary Name
		formWithdraw.findElement(By.cssSelector(".form_list_inner:nth-child(3) .is-required .el-input__inner")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		

		//Input Note
		formWithdraw.findElement(By.cssSelector(".form_list_inner:nth-child(3) li:nth-child(2) .el-input__inner")).sendKeys("IB Portal Australia Bank Transfer Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.cssSelector("button.el-button.el-button--primary")).click();
		
		//To print result
		funcPrintResult();
		Thread.sleep(4000); //To allow sometime for next function
		
		driver.findElement(By.xpath(".//span[text()='DASHBOARD']")).click();
		wait10.until(ExpectedConditions.urlContains("home"));
			
	}

	//@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	//withdraw through AU Bank Transfer
	void WithdrawChinaBank() throws Exception
	{

		String option;
		BigDecimal moneyAmount=new BigDecimal("0.00");

		WebElement form=null,menuSelected=null;
		List<WebElement> t;
		
		wait10.until(ExpectedConditions.elementToBeClickable(By.linkText("WITHDRAW REBATE"))).click();
		Thread.sleep(1000);
		
		//There are 9 forms in the source. Depending on the withdraw method user selects, the corresponding form will be displayed
		//This paragraph is to find which form is used by default
		List<WebElement> formGroup = driver.findElements(By.cssSelector("div.form_container form"));
		
		
		for(int i=0;i<formGroup.size();i++)
		{
			if(!formGroup.get(i).getAttribute("style").contains("display: none"))
			{
				form=formGroup.get(i);
			}
		}
		
		
		//Click the Rebate account list
		form.findElement(By.cssSelector("div.form_list_inner:nth-of-type(1) li:nth-of-type(1) input")).click();
		Thread.sleep(1000);
		
		//After user clicks the rebate account list, a new DIV will be added directly under the Body
		List<WebElement> menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		
		
		//Select one Rebate account with balance greater than 0 and return the balance
		moneyAmount=chooseRebateAccount(menuGroup,false);		
		Assert.assertFalse(moneyAmount.compareTo(BigDecimal.ZERO)==0, "Balance of all rebate accounts is 0.00. Can't perform WITHDRAW option.");	
		Thread.sleep(1000);
		
		//Input Amount: with around 1/6 balance so other withdraw methods can be tested
		moneyAmount = moneyAmount.multiply(new BigDecimal("0.10")).setScale(2,BigDecimal.ROUND_HALF_UP);
		form.findElement(By.cssSelector("div.form_list_inner:nth-of-type(1) li:nth-of-type(2) div.el-input input.el-input__inner")).sendKeys(moneyAmount.toString());

		//Choose Bank Transfer
		t=form.findElements(By.cssSelector("form>div:nth-of-type(2) div.el-radio-group li"));
		Assert.assertTrue(chooseWithdrawMethod(t, "Bank Transfer"));
		
		//Country/Region: choose China		

		form.findElement(By.cssSelector("form>div:nth-of-type(3) li:nth-of-type(1) input.el-input__inner")).click();
		Thread.sleep(500);
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		
		//Choose which menugroup is popup
		for(int i=0;i<menuGroup.size();i++)
		{
			//If the div is set to display
			if(!menuGroup.get(i).getAttribute("style").contains("display: none"))
			{
				
				menuSelected = menuGroup.get(i);
				
				t=menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));
				

				j=t.size()-1;
					
				while(j>=0)
				{
					//System.out.println("Country/Region Selection loop :" + j);
					option=t.get(j).findElement(By.tagName("span")).getText();
					if(option.equals("China"))
					{
						t.get(j).findElement(By.tagName("span")).click();
						break;
					}else
					{
						j--;
					}

	
				}
			}
		}
		
		
		//After selection of China as Country/Region,  the Form changed. Need to re-search which form is used now.
        formGroup = driver.findElements(By.cssSelector("div.form_container form"));
		

		for(int i=0;i<formGroup.size();i++)
		{
			if(!formGroup.get(i).getAttribute("style").contains("display: none"))
			{
				form=formGroup.get(i);
				//System.out.println("The form number is:"+i);
			}
		}
		
		//Input Bank name
		
		WebElement s=form.findElement(By.cssSelector("form>div:nth-of-type(4) li:nth-of-type(1) input.el-input__inner"));		
		wait10.until(ExpectedConditions.visibilityOf(s)).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");

		//Input Bank Account Number
		form.findElement(By.cssSelector("form>div:nth-of-type(4) li:nth-of-type(2) input")).sendKeys(Utils.randomNumber(8));
		
		//Input Bank Branch
		form.findElement(By.cssSelector("form>div:nth-of-type(5) li:nth-of-type(1) input")).sendKeys(Utils.randomString(5).toUpperCase()+" Branch");

		//Input Bank Address
		form.findElement(By.cssSelector("form>div:nth-of-type(5) li:nth-of-type(2) input")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
		
		//Input Account Holder's Address
		form.findElement(By.cssSelector("form>div:nth-of-type(6) li:nth-of-type(1) input")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Avenue");
		
		//Input Bank Account Beneficiary Name
		form.findElement(By.cssSelector("form>div:nth-of-type(6) li:nth-of-type(2) input")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		

		//Input Note
		//form.findElement(By.cssSelector("form>div:nth-of-type(6) li:nth-of-type(2) input")).sendKeys("Test China Bank Transfer Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.cssSelector("button.el-button.el-button--primary")).click();
		//To print result
		funcPrintResult();
		
		Thread.sleep(2000); //To allow sometime for next function
		
		driver.findElement(By.xpath(".//span[text()='DASHBOARD']")).click();
		wait10.until(ExpectedConditions.urlContains("home"));
		
	}
	
	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Skrill
	void WithdrawSkrill(String Brand) throws Exception
	{
		String wMethod = "";
		switch (Brand.toLowerCase()) {
			case "fsa":
			case "svg":
				wMethod = "E-WALLET:Skrill";
				break;
			default:
				wMethod = "Skrill";
		}
		funcWithdrawEmail(Brand, wMethod);
	
	}
	
	void funcWithdrawEmail(String Brand, String Method) throws Exception
	{
		WebElement formWithdraw=null;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		String withdrawMethod = Method;
		
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
		ipw.wdMethod = withdrawMethod;

		//input email:
	    ipw.getAllEmailEle().sendKeys(Utils.randomString(5)+"@test.com");

	    //input notes
	    ipw.getNotesEle().sendKeys("Test IP " + Method + " Withdraw " + moneyAmount.toString());

	    funcSubmitWithdraw();
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Fasapay
	void WithdrawFasaPay(String Brand) throws Exception
	{

		//WebElement formWithdraw=null;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		String withdrawMethod = "FasaPay";
		
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
		
		//Input Fasapay Account Name
		ipw.getFasaAcctNameEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		  
		//Input Fasapay Account Number
		ipw.getFasaAcctNoEle().sendKeys(Utils.randomNumber(8));
		  
		//Input Note
		ipw.getNotesEle().sendKeys("Test IP Fasapay Withdraw " + moneyAmount.toString());
			
		funcSubmitWithdraw();
		
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Unionpay Bank Transfer
	void WithdrawUnionPay(String Brand) throws Exception
	{

		WebElement formWithdraw=null, menuSelected=null;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		String withdrawMethod = "Union Pay";
		List<WebElement> menuGroup, t;
				
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
		
		
		//After selection of withdraw method, 2nd form will change accordingly. Find form element again.
		//formWithdraw = driver.findElement(By.cssSelector("div.form_container form"));
		
		//Input Bank name
		//ipw.getBankNameEle().sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
		WebElement s=driver.findElement(By.cssSelector(".form_list_inner:nth-child(1) li:nth-child(1) .el-form-item__content > .el-input > .el-input__inner"));		
		wait10.until(ExpectedConditions.visibilityOf(s)).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");

		//Input Bank Account Number
		//ipw.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));
		driver.findElement(By.cssSelector(".clearfix:nth-child(1) > .clearfix > li:nth-child(2) .el-input__inner")).sendKeys(Utils.randomNumber(8));
		
		//Input Province
		driver.findElement(By.cssSelector(".clearfix:nth-child(1) > li:nth-child(1) .el-select .el-input__inner")).click();
		
		Thread.sleep(500);
		ipw.getBankProvinceCityList().get(r.nextInt(ipw.getBankProvinceCityList().size()-1));
		
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		
		//Choose which menugroup is popup
		for(int i=0;i<menuGroup.size();i++)
		{
			//If the div is set to display
			if(!menuGroup.get(i).getAttribute("style").contains("display: none"))
			{
				
				menuSelected = menuGroup.get(i);
				//System.out.println("The div selected for Province is "+i);
				t=menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));				
				j=r.nextInt(t.size());		
				action.moveToElement(t.get(j).findElement(By.tagName("span"))).build().perform();
				Thread.sleep(500);
				//System.out.println("The number of provinces is "+t.size() + ". We are going to choose province with number "+j);
				t.get(j).click();
			}
			
		}
		
		//Input City
		driver.findElement(By.cssSelector("li:nth-child(2) .el-select .el-input__inner")).click();
		
		Thread.sleep(500);
		menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		
		//Choose a city
		for(int i=0;i<menuGroup.size();i++)
		{
			//If the div is set to display
			if(!menuGroup.get(i).getAttribute("style").contains("display: none"))
			{
				menuSelected = menuGroup.get(i);				
				t=menuSelected.findElements(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li"));
				j=r.nextInt(t.size());	
				action.moveToElement(t.get(j).findElement(By.tagName("span"))).build().perform();
				Thread.sleep(500);
				t.get(j).click();
			}
			
		}
		
		//Input Bank Branch
		driver.findElement(By.cssSelector(".form_list_inner:nth-child(3) li:nth-child(1) .el-input__inner")).sendKeys(Utils.randomString(5).toUpperCase()+" Branch");

		//Input Bank Account Name
		driver.findElement(By.cssSelector(".form_list_inner:nth-child(3) li:nth-child(2) .el-input__inner")).sendKeys(Utils.randomString(5).toUpperCase());
		
		//Input Note
		//driver.findElement(By.cssSelector(".form_list_inner:nth-child(4) .el-input__inner")).sendKeys("IB Portal Test UnionPay Bank Transfer Withdraw " + moneyAmount.toString());
		
		driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input")).sendKeys("IB Portal Test UnionPay Bank Transfer Withdraw " + moneyAmount.toString());
		Thread.sleep(6000);
		funcSubmitWithdraw();
	}
	
	void funcSubmitWithdraw() throws Exception
	{
		//Submit
		ipw.getSubmitBtn().click();
		Thread.sleep(1000);
		try {
			driver.findElement(By.xpath("//span[contains(.,'Confirm')]")).click();
		} catch (Exception e) {
			System.out.println("No popup");
		} finally {
			System.out.println("Checking popup and click 'Submit' button if there is popup");
		}
	
		//To print result
		funcPrintResult();
		Thread.sleep(2000); //To allow sometime for next function
		
		ipw.getDashboardEle().click();
		wait10.until(ExpectedConditions.urlContains("home"));
	}
	
	@Test(priority=3, dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Malay Bank Transfer
	void WithdrawMalayBank(String Brand) throws Exception
	{
		
		//reuse for country withdrawal methods by Fiona on 20/7/2020
		funcWithdrawCountry(Brand, "Malay");

	}
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser() throws Exception
	{
		
		Utils.funcLogOutIP(driver);
		//Close all browsers
		driver.quit();
	}

	@Test(priority=3, dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Thai Bank Transfer
	void WithdrawThaiBank(String Brand) throws Exception
	{
		//reuse for country withdrawal methods by Fiona on 20/7/2020
		funcWithdrawCountry(Brand, "Thailand");

	}
	
	@Test(priority=3, dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Vietnam Bank Transfer
	void WithdrawVietBank(String Brand) throws Exception
	{
		//reuse for country withdrawal methods by Fiona on 20/7/2020
		funcWithdrawCountry(Brand, "Vietnamese");
	}
	
	@Test(priority=3, dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Nigeria Bank Transfer
	void WithdrawNigeBank(String Brand) throws Exception
	{
		//reuse for country withdrawal methods by Fiona on 20/7/2020
		funcWithdrawCountry(Brand, "Nigerian");
	}
	
	void funcWithdrawCountry(String Brand, String Method) throws Exception
	{
		WebElement formWithdraw=null;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		String withdrawMethod = Method;
		
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
			
		//After selection of withdraw method, 2nd form will change accordingly. Find form element again.
		formWithdraw = driver.findElements(By.cssSelector("div.form_container form")).get(2);
		
		//Input Bank name		
		WebElement s=formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(1) li:nth-of-type(1) input"));		
		wait10.until(ExpectedConditions.visibilityOf(s)).sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");

		//Input Bank Address
		formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(1) li:nth-of-type(2) input")).sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
		
		//Input Bank Account Number
		formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(2) li:nth-of-type(1) input")).sendKeys(Utils.randomNumber(8));
	
		//Input Bank Beneficiary Name
		formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(2) li:nth-of-type(2) input")).sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());
		
		//Input Swift
		formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(3) li:nth-of-type(1) input")).sendKeys(Utils.randomNumber(4));
		
		//Input Note
		formWithdraw.findElement(By.cssSelector("div.form_list_inner.clearfix:nth-of-type(3) li:nth-of-type(2) input")).sendKeys("IB Portal Test "+Method+" Bank Transfer Withdraw " + moneyAmount.toString());
		
		//Submit
		driver.findElement(By.cssSelector("button.el-button.el-button--primary")).click();
		//To print result
		funcPrintResult();
		Thread.sleep(2000); //To allow sometime for next function
		
		driver.findElement(By.xpath(".//span[text()='DASHBOARD']")).click();
		wait10.until(ExpectedConditions.urlContains("home"));
	}
	
		
	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Neteller
	void WithdrawNeteller(String Brand) throws Exception
	{
		String methodName = "";
		switch (Brand.toLowerCase()) {
			case "fsa":
			case "svg":
				methodName = "E-WALLET:Neteller";
				break;
			default:
				methodName = "Neteller";
		}
		funcWithdrawEmail(Brand, methodName);
		
	}
	
	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Neteller
	void WithdrawCryptoBitcoin(String Brand) throws Exception
	{
		String methodName;
		switch(Brand.toLowerCase())
		{
			case "fsa":
			case "svg":
				methodName = "CRYPTOCURRENCY:Cryptocurrency-Bitcoin";
				break;
			default:
				methodName = "Cryptocurrency-Bitcoin";
		}
		funcWithdrawEmail(Brand, methodName);
	}
	
	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through Neteller
	void WithdrawCryptoUSDT(String Brand) throws Exception
	{
		
		String methodName;
		switch(Brand.toLowerCase())
		{
			case "vt":
				methodName="Cryptocurrency-USDT";
				break;
			case "fsa":
			case "svg":
				methodName = "CRYPTOCURRENCY:Cryptocurrency-USDT";
				break;
			default:
				methodName = "Cryptocurrency-USDT";
			
		}
		
		funcWithdrawEmail(Brand, methodName);
	}

	BigDecimal chooseRebateAccount(List<WebElement> menuGroup, boolean isUSDAccount) throws Exception
	{
		BigDecimal moneyAmount=new BigDecimal("0.00");
		List<WebElement> t;
		WebElement menuSelected;
		Boolean flag=true;
		String currency;
		String option;
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
					//IB rebate account list has xxx-null null. To avoid this
					if(!option.contains("null"))
					{
						result=option.split(" ");						
						moneyAmount=new BigDecimal(result[2]);
						currency = result[3];
						if (moneyAmount.compareTo(minimumAmount)==1)
						{
							if(isUSDAccount){
								if (currency.contains("USD"))
								{
									flag=false;
									t.get(j).findElement(By.tagName("span")).click();;  //This rebate account has rebate and can be used.
									break;
								} else {
									j--;
								}
								
							} else {
								flag=false;
								t.get(j).findElement(By.tagName("span")).click();;  //This rebate account has rebate and can be used.
								break;
							}
						}else
						{
							j--;
						}
					}else
					{
						j--;
					}
				}
			}
		}
		
		return moneyAmount;
	}

	Boolean chooseWithdrawMethod(List<WebElement> t, String method) throws Exception
	{
		Boolean flag=false;
		
		for(int i=0;i<t.size();i++)
		{
			
			//System.out.println("index = " + i + t.get(i).findElement(By.cssSelector("span.el-radio__label")).getText());
			if(t.get(i).findElement(By.cssSelector("span.el-radio__label")).getText().startsWith(method))
			{
		
				t.get(i).findElement(By.cssSelector("span.el-radio__label")).click();				
				Thread.sleep(2000);
				flag=true;
				break;
			}
		}
		
		return flag;
	}
	
	Boolean chooseWithdrawMethodPUG(List<WebElement> t,String method) throws Exception
	{
		Boolean flag=false;
		String[] methods=method.split(":");
		
		for(int i=0;i<t.size();i++)
		{
			
			//System.out.println("index = " + i + t.get(i).findElement(By.cssSelector("span.el-radio__label")).getText());
			if(t.get(i).getText().startsWith(methods[0]))
			{
		
				t.get(i).click();	
				t.get(i).findElement(By.xpath("//span[contains(.,'"+methods[1]+"')]")).click();
				Thread.sleep(2000);
				flag=true;
				break;
			}
		
		}
		return flag;
	}
	
	//For VT, t is withtraw type input control。  Need to click this input button to show withdraw list
	Boolean chooseWithdrawMethodVT(WebElement t, String method) throws Exception
	{
		Boolean flag=false;
		String cssSel = "";
		
		cssSel = ".//span[contains(text(),'" + method + "')]";
	
		//Click to show withdraw dropdown list.
		try
		{
			t.click(); 
			Thread.sleep(500);
			//driver.findElement(By.xpath(cssSel)).click();
			executor.executeScript("arguments[0].click();", driver.findElement(By.xpath(cssSel)));

			flag= true;
		}catch(Exception e)
		{
			flag = false;
		}
		return flag;
	}
	
	//Yanni on 10/15/2019: to print withdraw application message after submission.
	void funcPrintResult()
	{
		
		wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@role='alert']")));
		String result = driver.findElement(By.xpath(".//div[@role='alert']/p")).getText();
		System.out.println("Result: " + result);
		Assert.assertTrue(result.contains("submitted"));
		wait10.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@role='alert']")));
	}
	
	/*Yanni on 23/10/2019: withdraw form general part input: 
	1) select Rebate Account 
    2) input Amount
    3) select withdraw method: Bank Transfer, Skrill, Neteller, UnionPay, Fasapay, Thailand, Malaysia*/
	
	BigDecimal funcInputWithdrawGeneral(String Brand, String withdrawMethod, BigDecimal withdrawIndex) throws Exception
	{
		BigDecimal moneyAmount=new BigDecimal("0.00");
		List<WebElement> t;
		WebElement vtMethod;
		
		wait10.until(ExpectedConditions.elementToBeClickable(ipw.getWithdrawLink())).click();
		Thread.sleep(1000);
		
		if(Brand.equalsIgnoreCase("vt")) {
			wait10.until(ExpectedConditions.invisibilityOf(ipw.getVTLoadingEle()));
		}
		
		//Click the Rebate account list
		wait10.until(ExpectedConditions.elementToBeClickable(ipw.getRebateAccountList())).click();
		Thread.sleep(1000);
		
		//After user clicks the rebate account list, a new DIV will be added directly under the Body

		List<WebElement> menuGroup = ipw.getMenuGroup();
		
		//Select one Rebate account with balance greater than 0 and return the balance
		
		if(withdrawMethod.equalsIgnoreCase("UnionPay")) {
			moneyAmount=chooseRebateAccount(menuGroup,true);
		} else {
			moneyAmount=chooseRebateAccount(menuGroup,false);
		}

		Assert.assertFalse(moneyAmount.compareTo(BigDecimal.ZERO)==0, "Balance of all rebate accounts is 0.00. Can't perform WITHDRAW option.");	
		
		//Input Amount: without around 1/6 balance so other withdraw methods can be tested
		Assert.assertTrue(withdrawIndex.compareTo(BigDecimal.ZERO) ==1, "Withdraw Percentage is not qualified with " + withdrawIndex);
		moneyAmount = moneyAmount.multiply(withdrawIndex).setScale(2,BigDecimal.ROUND_HALF_UP);
		if (moneyAmount.compareTo(minimumAmount)<0) {
			moneyAmount = minimumAmount;
		}
		
		ipw.getWithdrawAmount().sendKeys(moneyAmount.toString());
		//Choose Withdraw type
		Thread.sleep(3000);
		
		switch (Brand.toLowerCase())
		{
		case "vt":
			vtMethod = driver.findElements(By.xpath("//input[@placeholder='Select']")).get(1);
			Assert.assertTrue(chooseWithdrawMethodVT(vtMethod, withdrawMethod));
			break;
		case "fsa":
		case "svg":
			t = driver.findElements(By.xpath("//div[@role='tab']"));
			//System.out.println(t.size());
			Assert.assertTrue(chooseWithdrawMethodPUG(t, withdrawMethod));
			break;
		default:
			t=driver.findElements(By.cssSelector("div.el-radio-group li"));
			//System.out.println(t.size());
			Assert.assertTrue(chooseWithdrawMethod(t, withdrawMethod));
		}
	
		return moneyAmount;
	}
	
	@AfterMethod(alwaysRun=true)
	void sleepAfterMethod() throws InterruptedException
	{
		Thread.sleep(1000);
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through AU Bank Transfer
	void WithdrawI18NBank(String Brand) throws Exception
	{
	
		//WebElement formWithdraw=null;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		String withdrawMethod = "Bank Transfer";
		//String countryRegion = "International";
		switch(Brand.toLowerCase())
		{
			case "vt":
				withdrawMethod="Bank transfer";
				break;
			case "fsa":
			case "svg":
				withdrawMethod = "BANK TRANSFER:Bank Transfer";
				break;
			default:
				withdrawMethod = "Bank Transfer";
			
		}
		
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
		
		withdrawI18N i18nCls = new withdrawI18N(driver, Brand);
		
		//Fill out I18N form
		withdrawI18NForm(i18nCls,moneyAmount);
		
		funcSubmitWithdraw();
		
	}
	
	void withdrawI18NForm(withdrawI18N i18nCls, BigDecimal withdrawAmount) throws Exception
	{
				
		List<WebElement> bankAcctList = null;
		
		//Click the Bank Account Element to show all Bank Accounts
		ipw.getBankAccountSelelctEle().click();
		
		bankAcctList = i18nCls.getBankAccountList();
		
		//If there is at least one saved bank account, use saved account infor. Otherwise, select 'Add bank account'. For both scenarios, click the first item in the list.
		bankAcctList.get(0).click();
		
		if(bankAcctList.size()==1)
		{
			//when there is only 1 option (Add bank account) available, need to do the following
			
			//Select region as International
		    ipw.getBankCountryEle("International").click();	     
	
	    	//input bank name
		    ipw.getBankNameEle().sendKeys(Utils.randomString(5)+" "+Utils.randomString(4)+" "+"Bank");
	    	
	    	//Input bank address
		    ipw.getBankAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Street");
				
			//Input Bank Beneficiary Name
		    ipw.getBankBenefiEle().sendKeys(Utils.randomString(4).toUpperCase() + " " + Utils.randomString(3).toUpperCase());	
			
		     //Input Bank Account Number
		    ipw.getBankAcctNoEle().sendKeys(Utils.randomNumber(8));					

			 //Input Account Holder's Address
		    ipw.getAcctHolderAddrEle().sendKeys(Utils.randomNumber(2)+" "+ Utils.randomString(4).toUpperCase()+" Road");

				
			//Input Swift
		    ipw.getSwiftCodeEle().sendKeys(Utils.randomNumber(4));
				
			//Input ABA/Sort Code
		    ipw.getABACodeEle().sendKeys(Utils.randomNumber(5));
			
			//Input Note
		    ipw.getNotesEle().sendKeys("Test IP International Bank Withdraw " + withdrawAmount.toString());
				
			//upload file
		    
			Thread.sleep(1500);		
	   
			 
		}else if(bankAcctList.size()>1)
		{
			/*when there is at least 1 saved bank account,  account infor is populated autoamtically when selecting this account.
			 * Only Notes  are required to input
			*/
				
			//Input Note
			ipw.getNotesEle().sendKeys("Test IP International Bank Withdraw " + withdrawAmount.toString());
				
			
		}
		

	
	}
	
	void funcSelectCounReg(String countryRegion) throws Exception
	{
		
		WebElement formCounReg=null;
		List<WebElement> menuGroup;
		
		//Choose Country/Region Method
		Thread.sleep(1000);
		formCounReg= driver.findElements(By.cssSelector("div.form_container form")).get(1);
		formCounReg.findElement(By.tagName("input")).click();
		Thread.sleep(500);
		menuGroup = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		
		//Choose Country/Region
		for(int i=0;i<menuGroup.size();i++)
		{
			
			//System.out.println("Country/Region Selection loop :" + j);
			String option=menuGroup.get(i).findElement(By.tagName("span")).getText();
			System.out.println(option);
			if(option.equalsIgnoreCase(countryRegion))
			{
				menuGroup.get(i).findElement(By.tagName("span")).click();
				break;
			}
		}
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//withdraw through AU Bank Transfer
	void WithdrawUnionPayCPS(String Brand) throws Exception
	{
		WebElement formWithdraw=null;
		BigDecimal moneyAmount= new BigDecimal("0.00"), withdrawIndex = new BigDecimal("0.10");
		BigDecimal withdraw100 = new BigDecimal("100");
		String withdrawMethod = "UnionPay";
		List<WebElement> menuOption = null;
			
		//Input Rebate Account, Amount and Choose Bank Transfer
		moneyAmount=funcInputWithdrawGeneral(Brand, withdrawMethod, withdrawIndex);
	
		Thread.sleep(1000);
		
		//After selection of country/region method, 3rd form will change accordingly. Find form element again.
		//formWithdraw = driver.findElements(By.cssSelector("div.form_container form")).get(1);	
		if (Brand.equalsIgnoreCase("vt")) {
			formWithdraw = driver.findElement(By.cssSelector("div.form_container form.el-form.form_bottom_row"));
		} else {
			formWithdraw = driver.findElement(By.cssSelector("div.form_container form.el-form.form_bottom_row.el-form--label-top"));
		}
		Thread.sleep(1000);
		
		//Select withdraw card
		//Click input control to show card list:
		formWithdraw.findElement(By.xpath(".//div[@class='el-form-item is-required']//input[@placeholder='Select']")).click();
		Thread.sleep(500);
		
		menuOption = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		
		if(menuOption.size()==0)
		{
			System.out.println("No Approved UnionCard is displayed.");
			Assert.assertTrue(false, "No Approved UnionCard is displayed.");
		}else
		{
			//Select 1st card listed.
			menuOption.get(0).findElement(By.tagName("span")).click();
		}
		
		//Input Note
		formWithdraw.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input[@class='el-input__inner']")).sendKeys("IB Portal UnionPay Withdraw " + moneyAmount.toString());
		
		//Submit
		//driver.findElement(By.cssSelector("button.el-button.el-button--primary")).click();
		ipw.getSubmitBtn().click();
		Thread.sleep(1000);
		if(moneyAmount.compareTo(withdraw100)==-1) {
			executor.executeScript("arguments[0].click();", ipw.getConfirmBtn());
		}
		
		//To print result
		funcPrintResult();
		Thread.sleep(4000); //To allow sometime for next function
		
		//driver.findElement(By.xpath(".//span[text()='DASHBOARD']")).click();
		ipnb.getDashboard().click();
		wait10.until(ExpectedConditions.urlContains("home"));
			
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//Yanni, withdraw through Bitwallet 25/09/2020
	void WithdrawBitwallet(String Brand) throws Exception
	{
		String methodName = "";
		switch (Brand.toLowerCase()) {
			case "fsa":
			case "svg":
				methodName = "E-WALLET:Bitwallet";
				break;
			default:
				methodName = "Bitwallet";
		}
				
		funcWithdrawEmail(Brand, methodName);		
	
	}

	@Test(dependsOnMethods="IPLogIn",alwaysRun=true)
	@Parameters(value="Brand")
	//Yanni 25/09/2020, withdraw through SticPay: thanks for Fiona's work. That makes developing script for Sticpay very easier.
	void WithdrawSticPay(String Brand) throws Exception
	{
		
		String methodName = "";
		switch (Brand.toLowerCase()) {
			case "fsa":
			case "svg":
				methodName = "E-WALLET:SticPay";
				break;
			default:
				methodName = "SticPay";
		}
		
		funcWithdrawEmail(Brand, methodName);		
	
	}
}
