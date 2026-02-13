package clientBase;

import java.math.BigDecimal;
/*Yanni on 12/03/2021 , Funds->Withdraw Funds, Withdraw Page - General Part
 * Select withdraw MT account(with the most money)
 * Select Withdraw Method
 * Input Withdraw amount element
 * Submit button
 * 
*/
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import vantagecrm.Utils;

public class withdrawI18N extends withdrawGeneral{
	
	
	  private WebDriver driver;
	  private String Brand;
	 
	
	public withdrawI18N(WebDriver driver, String Brand)
	{
		
		super(driver,Brand);
		this.driver = driver;
		this.Brand = Brand;
	}
	
	//After selecting Bank Transfer as the withdraw method, customers are then prompted to select Bank Account.  This function returns to the element
	public WebElement getBankAccountSelelctEle()
	{
		WebElement tmpEle = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.xpath("//div[@data-testid='selectedCard']"));
				break;
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[label = 'Select Bank Account']//input"));
				break;
			default:
				tmpEle = driver.findElement(By.xpath("//div[@data-testid='selectedCardID']"));
				break;				
					
		}
		
		return tmpEle;
	}
	
	//Get Bank Account List + Add bank account menu
	public List<WebElement> getBankAccountList()
	{
		List<WebElement> tmpList;
		tmpList = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		return tmpList;
	}
	
	
	//Return the element of specified Country/Region
	public WebElement getBankCountryEle(String countryName)
	{
		WebElement tmpEle;
		//String methodxPathLocator = "//span[contains(text(),'method')]";
		String methodxPathLocator = "//span[text()='method']";
		
		switch(Brand.toLowerCase())
		{

			case "fsa":
			case "svg":
				driver.findElement(By.xpath("//div[@id='transferCountry']/form/ul[1]/li[2]//input")).click();
				break;
				
			default:
				driver.findElement(By.xpath("//div[@data-testid='country_region']")).click();
			break;
		}
		
		methodxPathLocator = methodxPathLocator.replace("method", countryName);
		
		tmpEle = driver.findElement(By.xpath(methodxPathLocator));
		
		return tmpEle;
	}
	
	//Get Bank Name element
	public WebElement getBankNameEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("bankName_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[1]/li[1]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Bank Name']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Bank Address element
	public WebElement getBankAddrEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("bankAddress_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[1]/li[2]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Bank Address']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Bank Beneficiary element
	public WebElement getBankBenefiEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("bankBeneficiaryName_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[2]/li[1]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Bank Beneficiary Name']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Bank Account Number element
	public WebElement getBankAcctNoEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("accountNumber_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[2]/li[2]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Bank Account Number/IBAN']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Bank Account Number element
	public WebElement getAcctHolderAddrEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("accountHolderAddress_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[3]/li[1]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Account Holder’s Address']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Swift Code element
	public WebElement getSwiftCodeEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("swift_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[3]/li[2]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'SWIFT code']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get ABA/Sort Code element
	public WebElement getABACodeEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("abaCode_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[4]/li[1]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'ABA/Sort Code:']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Notes element
	public WebElement getNotesEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("notes_transferInt"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='transferIntForm']/form/ul[4]/li[2]//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Important notes']//input"));
		}
		 
		 return tmpEle;
	}
	
	//Get Upload File element
	public WebElement getUploadFileEle()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//input[@name='file']"));
		 
		 return tmpEle;
	}
	
	//get Remeber My account checkbox
	public WebElement getRememberAccountCkb()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//span[@class='el-checkbox__inner']"));
		 
		 return tmpEle;
	}
	
	//Get available amount
	public WebElement getAvailableAmount()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//input[@data-testid='availableAmount']"));
		 
		return tmpEle;
	}
	
	//Button of confirmAmountFee during submission
	public WebElement getConfirmAmountFee()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//button[@data-testid='confirmAmountFee']"));
		 
		return tmpEle;
	}
	
}
