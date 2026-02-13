package adminBase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Admin 
 */

public class AccountResult {
	
	private WebDriver driver;
	private List<WebElement> queryResult;
	
	public AccountResult(WebDriver tDriver)
	{
		driver = tDriver;
	}

	public List<WebElement> getAccoutQueryResult()
	{
		try {
			queryResult = driver.findElements(By.xpath("//table[@id='table']//tbody//tr"));
			
		}catch(Exception e)
		{
			System.out.println("Getting query result failure.");
		}
		
		return queryResult;
	}
	
	//Get ServerName column, return webElement
	public WebElement getServerEle(WebElement tr)
	{
		WebElement serverName=null;
		
		try {
			serverName = tr.findElement(By.cssSelector("td:nth-of-type(1)"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Server name from List failure.");
		}
		
		return serverName;
	}
	
	//Get userName column: column4, return webelement
	public WebElement getUserEle(WebElement tr)
	{
		WebElement userNameLink=null;
		
		try {
			userNameLink = tr.findElement(By.cssSelector("td:nth-of-type(4) a"));
		
		}catch(Exception e)
		{
			System.out.println("Getting user name from List failure.");
		}
		
		return userNameLink;
	}
	
	//Get AccountNo column		
	public WebElement getAccEle(WebElement tr, Boolean isTrading)
	{
		WebElement accNoLink=null;
		
		try {
			if (isTrading)
				accNoLink = tr.findElement(By.cssSelector("td:nth-of-type(9) a"));
			else
				accNoLink = tr.findElement(By.cssSelector("td:nth-of-type(9)"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Account No Link from List failure.");
		}
		
		return accNoLink;
	}
	
	//Get the value of AccountGroup column
	public String getAccGroup(WebElement tr)
	{
		String accGroup="";
		
		try {
			accGroup=tr.findElement(By.cssSelector("td:nth-of-type(11)")).getText();
		
		}catch(Exception e)
		{
			System.out.println("Getting Account Group from List failure.");
		}
		
		return accGroup;
	}
	
	//Get currency column
	public WebElement getCurrencyEle(WebElement tr)
	{
		WebElement accCurrency=null;
		
		/* try { */
			accCurrency=tr.findElement(By.cssSelector("td:nth-of-type(12)"));
		
		/*}catch(Exception e)
		{
			System.out.println("Getting currency from List failure.");
		}
		*/
		return accCurrency;
	}
	
	//Get Balance column
	public WebElement getBalanceEle(WebElement tr, Boolean isTrading)
	{
		WebElement balanceEle=null;
		
		try {
			if (isTrading)
				balanceEle = tr.findElement(By.cssSelector("td:nth-of-type(14)"));
			else
				balanceEle = tr.findElement(By.cssSelector("td:nth-of-type(13)"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Balance from List failure.");
		}
		
		return balanceEle;
	}
	
	//Get Credit Link
	public WebElement getCreditEle(WebElement tr)
	{
		WebElement accNoLink=null;
		
		try {
			accNoLink = tr.findElement(By.cssSelector("a.update"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Account No Link from List failure.");
		}
		
		return accNoLink;
	}
	
	//Get Different Account Transfer link
	public WebElement getDiffTransferEle(WebElement tr, Boolean isTrading)
	{
		WebElement transLink=null;
		
		try {
			
			transLink = tr.findElement(By.cssSelector("a.accountNo"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Different Account Transfer Link from List failure.");
		}
		
		return transLink;
	}
	
	//Get Commission Rules link
	public WebElement getCommissionRuleEle(WebElement tr)
	{
		WebElement CommissionRule=null;
		
		try {
				
			CommissionRule = tr.findElement(By.cssSelector("td:nth-of-type(14)>a"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Different Account Commission Rules Link from List failure.");
			e.printStackTrace();
		}
		
		return CommissionRule;
	}
	
	//Get Adjustment link
	public WebElement getAdjustmentEle(WebElement tr)
	{
		WebElement adjustLink=null;
		
		try {
			adjustLink = tr.findElement(By.cssSelector("a.accountMake"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Different Account Transfer Link from List failure.");
		}
		
		return adjustLink;
	}
	
	//Get LogIn status link
	public WebElement getLoginEle(WebElement tr)
	{
		WebElement loginLink=null;
		
		try {
			loginLink = tr.findElement(By.cssSelector("td:nth-last-of-type(6)>a"));
		
			
		  }catch(Exception e)
		  {
		  System.out.println("Getting Login Status Link from List failure.");
		  }
			 
		
		return loginLink;
	}
	
	
	//Get Transaction status link
	public WebElement getTransEle(WebElement tr)
	{
		WebElement transLink=null;
		
		try {
			transLink = tr.findElement(By.cssSelector("td:nth-last-of-type(5)>a"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Transaction Status Link from List failure.");
		}
		
		return transLink;
	}
	
	//Get Reset Password link
	public WebElement getResetPwdEle(WebElement tr)
	{
		WebElement resetPwdLink=null;
		
		try {
			resetPwdLink = tr.findElement(By.cssSelector("td:nth-last-of-type(2)>a"));
		
		}catch(Exception e)
		{
			System.out.println("Getting Reset Password Link from List failure.");
		}
		
		return resetPwdLink;
	}
	
}
