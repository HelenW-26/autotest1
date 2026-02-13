package vantagecrm;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import	org.testng.annotations.*;

/*
 * This class is used to request additional accounts and transfer money between additional accounts
 */

public class TraderMyAccount {
	
	WebDriver driver;
	WebDriverWait wait01;
	WebDriverWait wait02;
	int waitIndex=1;
	Select t;
	
	
	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
    	  
    	  context.setAttribute("driver", driver);          //Added by Yanni on 5/15/2019
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver,Duration.ofSeconds(20));
	}
	
	@Test(priority=0)
	@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
	void TraderLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
	
	{
 		
  	  //Login AU Trader
		driver.get(TraderURL);		

		Utils.funcLogInTrader(driver, TraderName, TraderPass,Brand);	
		Thread.sleep(waitIndex*2000);
	
	//	Assert.assertEquals(driver.getTitle(),"My Account");
		
	}
	
	@Test(dependsOnMethods="TraderLogIn", invocationCount=1)
	@Parameters(value= {"Brand"})
	void RequestAddiAccount(String Brand) throws Exception
	{
		
		Random r=new Random();
		
		driver.navigate().refresh();
		
		//Click My Accounts link
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("MY ACCOUNTS"))).click();	
		Thread.sleep(1000);
		Assert.assertEquals(wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title_u"))).getText(), "LIVE ACCOUNTS");
		
		//Agree with terms and click SUBMIT
		driver.findElement(By.id("applyMt4Account")).click();
		wait01.until(ExpectedConditions.elementToBeClickable(By.id("chk_agree"))).click();
		driver.findElement(By.id("button")).click();
		
		//Fill out request form		
		Assert.assertEquals(wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title_u"))).getText(), "REQUEST AN ADDITIONAL ACCOUNT");
		t=new Select(driver.findElement(By.id("mt4_account_mt")));
		

		switch(Brand)
		{

			case "au":
			case "ky":
			case "kcm":
				{
					t.selectByIndex(1); //select MT4. KCM has only one option MT4
					Thread.sleep(1000);
					
					//Select Account type
					t=new Select(driver.findElement(By.id("deposit_MT4_account")));
					t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1); //Select Standard STP/RAW ECN/Islamic (only for AU) account type
					
					//Select currency
					t=new Select(driver.findElement(By.id("currency")));
					t.selectByIndex(r.nextInt(t.getOptions().size()-1)+1);
					
					//Input Notes
					driver.findElement(By.id("notes")).sendKeys("Automation Additional Account Request.");
					
					break;
				}	
			case "vt":
			case "pug":
				{
					t.selectByIndex(0); //select Trader platform MT4. 
					break;
				}
			
			default:
				{
					t.selectByIndex(1); //select MT4. 
					break;
				}
				
		}
		


		
		driver.findElement(By.id("confirmBtn")).click();		
		
		
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	void TransferFund() throws Exception
	{
		
		Boolean flag=true;
		int j;
		BigDecimal fundAmount = new BigDecimal("0.00");
		Select toAccount;
		
		driver.navigate().refresh();
		
		//Click My Accounts link
		wait01.until(ExpectedConditions.presenceOfElementLocated(By.linkText("MY ACCOUNTS"))).click();		
		Assert.assertEquals(wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title_u"))).getText(), "LIVE ACCOUNTS");
		
		//Click Submit
		driver.findElement(By.id("transferFunds")).click();
		
		Thread.sleep(1000);
		//Fill out request form		
		Assert.assertEquals(wait01.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title_u"))).getText(), "TRANSFER FUNDS BETWEEN ACCOUNTS");
		
		t=new Select(driver.findElement(By.id("frommt4Account")));
		j=t.getOptions().size();
		
		Assert.assertTrue(j>1);
		
		toAccount=new Select(driver.findElement(By.id("tomt4Account")));
		
		while(flag==true && j>1)
		{
			t.selectByIndex(j-1); //Select the last option
			
			//Check whether the account is in HOLD position or not
			try 
	    	{
	    		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button")).click();
	    		System.out.println(t.getFirstSelectedOption().getText() + " is in HOLD position.");
	    		
	        	
	    	}catch (NoSuchElementException | StaleElementReferenceException e) {
	    		System.out.println(t.getFirstSelectedOption().getText() + " is not on hold position");
	    	}
			
			Thread.sleep(1000);
			
			try {
			
				fundAmount = Utils.splitAccount(t.getFirstSelectedOption().getText());
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if (fundAmount.compareTo(BigDecimal.ZERO)==1)
			{
				if((toAccount.getOptions().size()>1) || (toAccount.getOptions().size()==1 && (!toAccount.getFirstSelectedOption().getText().contains("Please select"))))
				{
					flag=false;
					break;
	
				}
			}
			
			j--;
			
		}
		
		Assert.assertTrue(j>=1,"Either the FROM account has no balance or no available TO account");

		
		driver.findElement(By.id("transferMoney")).sendKeys(fundAmount.multiply(new BigDecimal("0.2")).setScale(2, BigDecimal.ROUND_HALF_UP).toString()); //Transfer the whole amount
		toAccount.selectByIndex(0);
		
		Thread.sleep(1000);
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSubmit"))).click();
		

		Thread.sleep(1000);
		//Assert Success
		Assert.assertEquals(driver.findElement(By.cssSelector("div.bootstrap-dialog-header div.bootstrap-dialog-title")).getText(), "FUNDS TRANSFER SUCCESSFUL");
		driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button.btn.btn-default")).click();
		
	
	}
	
	@Test(dependsOnMethods="TraderLogIn", alwaysRun=true)
	@Parameters(value= {"OldPwd", "NewPwd"})
	void ChangePWD(@Optional("123Qwe") String OldPwd, @Optional("1234Qwe")String NewPwd)
	{
		//Click My Profile link
		String message;
		
		for(int i=0;i<2;i++)
		{
			driver.navigate().refresh();
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("MY PROFILE"))).click();	
			driver.findElement(By.linkText("CHANGE CLIENT PORTAL PASSWORD")).click();
			
			if(i==0)
			{
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("old_password"))).sendKeys(OldPwd);
				driver.findElement(By.id("new_password")).sendKeys(NewPwd);
				driver.findElement(By.id("confirm_password")).sendKeys(NewPwd);		
			} else if(i==1)
			{
				wait01.until(ExpectedConditions.visibilityOfElementLocated(By.id("old_password"))).sendKeys(NewPwd);
				driver.findElement(By.id("new_password")).sendKeys(OldPwd);
				driver.findElement(By.id("confirm_password")).sendKeys(OldPwd);		
			}
				
			driver.findElement(By.id("typeBtn")).click();
			
			message=wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.title"))).getText();
			Assert.assertTrue(message.contains("Success"));
		}
		
		
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	void ResetMT4Pwd() throws Exception
	{

		driver.navigate().refresh();
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#nav ul.menuBox li:nth-of-type(1) a"))).click();
		
		//Click reset password icon(lock icon)
		driver.findElements(By.cssSelector("a.resetPass")).get(0).click();
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
		
		Thread.sleep(1000);
		
		//Click Forgot password link
		driver.findElement(By.cssSelector("div.pull-right.text-danger.forgetBtn")).click();
		
		Thread.sleep(1000);
		
		List<WebElement> listButton=driver.findElements(By.xpath(".//button[text()='Confirm']"));
		
		//Always click the last one Confirm button. As there are 2 identical Confirm buttons.
		listButton.get(listButton.size()-1).click();
		
		System.out.println(driver.findElements(By.xpath(".//button[text()='Confirm']")).size());
		//Confirm
		//wait01.until(ExpectedConditions.elementToBeClickable(By.xpath(".//button[text()='Confirm']"))).click();
		Thread.sleep(1000);
		//Close the dialog
		wait02.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.close"))).click();	
		
		
	}
	
	@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		driver.navigate().refresh();
		Utils.funcLogOutTrader(driver);
		driver.quit();
	}

	@Test(dependsOnMethods="TraderLogIn")
	void ChangeMT4Pwd()
	{
		
		
		String oldPwd="12345678Qi";
		String newPwd="87654321Qi";
		
		for(int i=0;i<2;i++)
		{		
		
			driver.navigate().refresh();
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#nav ul.menuBox li:nth-of-type(1) a"))).click();
			
			//Click reset password icon(lock icon)
			driver.findElements(By.cssSelector("a.resetPass")).get(0).click();
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
			
			switch(i)
			{
			case 0:
			//Input the old password
				driver.findElement(By.id("old_password")).sendKeys(oldPwd);
				
				//Input the new password
				driver.findElement(By.id("new_password")).sendKeys(newPwd);
				
				//Confirm the new password
				driver.findElement(By.id("confirm_password")).sendKeys(newPwd);
			

			
			case 1:
				//Input the old password
				driver.findElement(By.id("old_password")).sendKeys(newPwd);
				
				//Input the new password
				driver.findElement(By.id("new_password")).sendKeys(oldPwd);
				
				//Confirm the new password
				driver.findElement(By.id("confirm_password")).sendKeys(oldPwd);
				
			}
			
			//Confirm button
			driver.findElement(By.xpath(".//button[text()='Confirm']")).click();
		}
		
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	void passQuestionaire() throws Exception
	{
		try
		{
			wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#appraisalBox>a.appraisal_view_btn")));
						
			//Pass the questionarie
			Assert.assertTrue(funcQuestion(true), "Answer Questionaire has something wrong.");
			
		}catch (TimeoutException e)
		{
			System.out.println("No Questionaire link is available.");
			e.printStackTrace();
		}
	}
	
	@Test(dependsOnMethods="TraderLogIn")
	void failQuestionaire() throws Exception
	{
		try
		{
			wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#appraisalBox>a.appraisal_view_btn")));
						
			//Pass the questionarie
			Assert.assertTrue(funcQuestion(false), "Answer Questionaire has something wrong.");
			
		}catch (TimeoutException e)
		{
			System.out.println("No Questionaire link is available.");
			e.printStackTrace();
		}
	}
	
	boolean funcQuestion(boolean result) throws Exception
	{
		boolean flag=false;
		int[] failResult=new int[] {1,1,1,2,1,1,1,1,2,1};
		int[] successResult = new int[] {2,2,2,1,2,2,2,2,1,4};
		String selector, messageAct, messageExpSuc="Congratulations, your account has been activated and you��re ready to trade!";
		String messageExpFail="Unfortunately, you were not successful. Please take the test again.";
		int j;
		
		//Click the questionnaire button to show questions dialog
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.appraisal_view_btn")));
		driver.findElement(By.cssSelector("a.appraisal_view_btn")).click();
		
		wait02.until(ExpectedConditions.visibilityOfElementLocated(By.id("appraisalList")));
		
		if(result==true)
		{
		
			for(int i=0;i<successResult.length;i++)
			{
				
				Thread.sleep(500);
				j=i+1;
				selector="ul#appraisalList>li:nth-of-type("+j+")>div>label:nth-of-type("+successResult[i]+")>input";
				driver.findElement(By.cssSelector(selector)).click();
			}
			
			//Click submit button
			driver.findElement(By.xpath(".//button[text()='Submit']")).click();
			
			try
			{
				wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div.messenger-message-inner")));
				messageAct=driver.findElement(By.cssSelector("li.messenger-message-slot div.messenger-message-inner")).getText();
				Assert.assertEquals(messageAct, messageExpSuc);
				flag=true;
			}catch(NoSuchElementException e)
			{
				flag=false;
				e.printStackTrace();
			}
			
			
		}else
		{
			for(int i=0;i<failResult.length;i++)
			{
				
				Thread.sleep(500);
				j=i+1;
				selector="ul#appraisalList>li:nth-of-type("+j+")>div>label:nth-of-type("+failResult[i]+")>input";
								
				driver.findElement(By.cssSelector(selector)).click();
				
			}
			
			//Click submit button
			driver.findElement(By.xpath(".//button[text()='Submit']")).click();
			
			try
			{
				wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.messenger-message-slot div.messenger-message-inner")));
				messageAct=driver.findElement(By.cssSelector("li.messenger-message-slot div.messenger-message-inner")).getText();
				Assert.assertEquals(messageAct, messageExpFail);
				flag=true;
				
			}catch(NoSuchElementException e)
			{
				flag=false;
				e.printStackTrace();
			}
		}
		

		
		
		
		return flag;
	}

	@Test(dependsOnMethods="TraderLogIn")
	void changeLeverage() throws Exception
	{
	
		List<WebElement> leverageButtons;
		String curLeverage;
		Select t;
		int i;
		Random r=new Random();
		
		driver.navigate().refresh();
				
		wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#nav ul.menuBox li:nth-of-type(1) a"))).click();
		
		//Click reset password icon(lock icon)
		
		leverageButtons=driver.findElements(By.cssSelector("a.LeverageModify>i.glyphicon.glyphicon-cog"));	
		if(leverageButtons.size()>=1)
		{
			
	
			leverageButtons.get(0).click();
			wait01.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-title")));
			
			Thread.sleep(1000);
			
			//Get current leverage
			curLeverage=driver.findElement(By.cssSelector("label.modalForm:nth-of-type(1)")).getText();
					
			t=new Select(driver.findElement(By.id("leverage")));
			t.selectByIndex(r.nextInt(t.getOptions().size()));
			
			//If selected leverage is the same as the current leverage, select again
			while(t.getFirstSelectedOption().getText().equals(curLeverage))
			{
				t.selectByIndex(r.nextInt(t.getOptions().size()));
			}
			
			//Click checkbox to agree with terms and conditions
			driver.findElement(By.id("disclaimer")).click();			
			
			//Click Confirm button
			driver.findElement(By.xpath("//button[text()='Confirm']")).click();;
			
			Thread.sleep(1000);
			wait02.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.bootstrap-dialog-footer-buttons button.btn.btn-default")));
			System.out.println("Result is: "+ driver.findElement(By.cssSelector("div.bootstrap-dialog-message")).getText());
			
			
			//Close the dialog
			driver.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button.btn.btn-default")).click();
			
		}else
		{
			System.out.println("No Available accounts for leverage change!");
		}
		
	}

}
