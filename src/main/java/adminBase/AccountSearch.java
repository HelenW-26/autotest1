package adminBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/*
 * Yanni on 21/10/2020: This class is for Trading Account/Rebate Account Search. Search -able fields have been defined but only part of functions are implements.
 */


public class AccountSearch {
	
	private WebElement serverList;   //Server List button
	private WebElement accountOwner;
	private WebElement userName;
	private WebElement mobileNo;
	private WebElement loginEmail;
	private WebElement countryName;
	private WebElement actNo;
	private WebElement actType;
	private WebElement currenCy;
	private WebElement regFrom;
	private WebElement regTo;
	private WebElement depositSta;
	private WebElement actActivity;
	private WebDriver driver;
	private WebElement searchButton;
	
	private String[] serverNameList=null;
	
	public AccountSearch(WebDriver tdriver) {
	
		driver = tdriver;	



		
		searchButton = driver.findElement(By.id("query"));
	
	}
	
	
	//Click Sever button then choose the designated server by name. servername must be exactly the same as the name displayed in UI 
	public Boolean selectServer(String serverName) throws InterruptedException
	{
		Boolean result = false;
		String xpathStr = "//div[contains(@style, 'display: block;')]//span[text()='" + serverName + "']/preceding-sibling::input[@type='checkbox']"; 
		
		serverList = driver.findElement(By.xpath("//div[contains(@class, 'mt4_datasource_id')]//button[@class = 'ms-choice']"));

		 try { 
			serverList.click();
			Thread.sleep(1000);
			
			List<WebElement> serverOptions = driver.findElements(By.xpath(xpathStr));
			
			System.out.println("Size: " + serverOptions.size());
			
			serverOptions.get(0).click();
			result = true;
			
			}catch(Exception e)
		{
			System.out.println("Select server error.");
			
		}	
		
		return result;
		
					
	}
	
	public Boolean selectServerAll() throws InterruptedException
	{
		Boolean result = false;
		String xpathStr = "//input[contains(@data-name, 'selectAll')]"; 
		
		serverList = driver.findElement(By.xpath("//div[contains(@class, 'mt4_datasource_id')]//button[@class = 'ms-choice']"));

		 try { 
			serverList.click();
			Thread.sleep(1000);
			
			driver.findElement(By.xpath("//div[3]/ul/li/label/input")).click();
			
			result = true;
			
			}catch(Exception e)
		{
			System.out.println("Select all servers error.");
			e.printStackTrace();
		}	
		
		return result;
						
	}
	
	public WebElement getUserNameEle()
	{
		
		userName = null;
		try {
			userName = driver.findElement(By.xpath("//input[contains(@class, 'real_name')]"));
		}catch (Exception e)
		{
			System.out.println("Getting User Name field in Search Bar error.");
		}
		
		return userName;
	}
	
	public Boolean inputUserName(String userRealName)
	{
		Boolean result = false;
		try {
			this.getUserNameEle().sendKeys(userRealName);
			result = true;
		}catch(Exception e)
		{
			System.out.println("Inputting user name error.");
		}	
		
		
		return result;
	}
	
	public WebElement getUserEmailEle()
	{
		
		loginEmail = null;
		try {
			loginEmail = driver.findElement(By.xpath("//input[contains(@class, 'email_sub')]"));
		}catch (Exception e)
		{
			System.out.println("Getting Login Email field in Search Bar error.");
		}
		
		return loginEmail;
	}
	
	public Boolean inputUserEmail(String userEmail)
	{
		Boolean result = false;
		try {
			this.getUserEmailEle().sendKeys(userEmail);
			result = true;
		}catch(Exception e)
		{
			System.out.println("Inputting Email error.");
		}	
		
		
		return result;
	}
	
	public Boolean inputAccount(String account)
	{
		Boolean result = false;
		try {
			this.getSearchActEle().sendKeys(account);
			result = true;
		}catch(Exception e)
		{
			System.out.println("Inputting Account error.");
		}	
		
		
		return result;
	}
	
	public WebElement getSearchActEle()
	{
		WebElement searchAct = null;
		
		try {
			//input, class name contians "mt4_account"
			 searchAct = driver.findElement(By.xpath("//input[contains(@class, 'mt4_account')]")); 
						
		}catch(Exception e)
		{
			System.out.println("Getting account field in Search Bar error.");
		}	
		
		return searchAct;
	}
	
	
	public Boolean clickSearch()
	{
		Boolean result = false;
		try {
			/* this.getSearchActEle().click(); */
			this.searchButton.click();
			result = true;
		}catch(Exception e)
		{
			System.out.println("Search button click error.");
			e.printStackTrace();
		}	
		
		
		return result;
	}
	
	
	
}
