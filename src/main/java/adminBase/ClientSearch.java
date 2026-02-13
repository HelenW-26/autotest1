package adminBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/*
 * Alex Liu on 27/10/2020: This class is for Client Search. Search -able fields have been defined but only part of functions are implements.
 */


public class ClientSearch {

	private WebElement accountOwner;
	private WebElement userName;
	private WebElement CPA;
	private WebElement clientType;
	private WebElement country;
	private WebElement mobileNo;
	private WebElement contactEmail;
	private WebElement loginEmail;
	private WebElement actNo;
	private WebElement leadSource;
	private WebElement promotion;
	private WebElement salesNotes;
	private WebDriver driver;
	private WebElement searchButton;
	
	public ClientSearch(WebDriver tdriver) {
	
		driver = tdriver;	

		userName = driver.findElement(By.xpath("//input[contains(@class, 'show_name')]"));
		loginEmail = driver.findElement(By.xpath("//input[contains(@class, 'control-email search-input')]"));
		searchButton = driver.findElement(By.id("query"));
	
	}
	
	
	
	public Boolean inputUserName(String userRealName)
	{
		Boolean result = false;
		try {
			userName.sendKeys(userRealName);
			result = true;
		}catch(Exception e)
		{
			System.out.println("Inputting user name error.");
		}	
		
		
		return result;
	}
	
	public Boolean inputUserEmail(String userEmail)
	{
		Boolean result = false;
		try {
			loginEmail.sendKeys(userEmail);
			result = true;
		}catch(Exception e)
		{
			System.out.println("Inputting Email error.");
		}	
		
		
		return result;
	}
	
	
	public Boolean clickSearch()
	{
		Boolean result = false;
		try {
			searchButton.click();;
			result = true;
		}catch(Exception e)
		{
			System.out.println("Search button click error.");
		}	
		
		
		return result;
	}
	
}
