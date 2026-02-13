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

public class withdrawCC extends withdrawGeneral{
	
	private WebDriver driver;
	private String Brand;
	
	public withdrawCC(WebDriver driver, String Brand)
	{
		super(driver,Brand);
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	//Credit Card List element: only after clicking this element, the credit card list is displayed in the dropdown
	public WebElement getSelectCCEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-success is-required']//input"));
				 break;
				 
			 case "fsa":
			 case "svg":
				 tmpEle= driver.findElement(By.xpath("//div[@id='visaForm']//input"));
				 break;
				 
				 default:
					 tmpEle= driver.findElement(By.cssSelector("ul:nth-child(1) li.fl div.el-input.el-input--suffix > input"));
		 }	
			
		return tmpEle;		
		
	}
	
	//Get available Credit Card list. If the list is null, print error message and exit the test.
	public List<WebElement> getAllCCList()
	{
		
		List<WebElement> tmpLst = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		
		if(tmpLst.size()==0)
		{
			Assert.assertTrue(false,"Empty CreditCard List. It should have at least one 'Enter credit card manually'");
		}
		return tmpLst;
	}
	
	//Get Element of Name on Card
	public WebElement getNameOnCardEle()
	{
		
		WebElement tmpLst = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				List<WebElement> visaForm = driver.findElements(By.xpath("//div[@id='visaForm']//input[@class='el-input__inner']"));
				//Input Name on Card
				tmpLst = visaForm.get(0);
				break;
				
			case "fsa":
			case "svg":
				break;
				
			default:
				tmpLst = driver.findElement(By.xpath("//div[@class='card_left']/div[@class='el-form-item is-required']/div[@class='el-form-item__content']/div[@class='el-input']/input[1]"));
				
		}
		return tmpLst;
	}
	
	//Get First Six of Credit Card Number
	public WebElement getFirstSixEle()
	{
		
		WebElement tmpLst = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				List<WebElement> visaForm = driver.findElements(By.xpath("//div[@id='visaForm']//input[@class='el-input__inner']"));
				//Input Name on Card
				tmpLst = visaForm.get(1);
				break;
				
			case "fsa":
			case "svg":
				break;
				
			default:
				tmpLst = driver.findElement(By.xpath("//ul[@class='digitsCard']//li[@class='col_1']//input"));
				
		}
		return tmpLst;
	}
	
	//Get First Six of Credit Card Number
	public WebElement getLastFourEle()
	{
		
		WebElement tmpLst = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				List<WebElement> visaForm = driver.findElements(By.xpath("//div[@id='visaForm']//input[@class='el-input__inner']"));
				//Input Name on Card
				tmpLst = visaForm.get(2);
				break;
				
			case "fsa":
			case "svg":
				break;
				
			default:
				tmpLst = driver.findElement(By.xpath("//li[@class='col_2']//input"));
				
		}
		return tmpLst;
	}
	
	//Set Expiration Year
	public void setExpiryYearRandom()
	{
		
		driver.findElement(By.xpath(".//input[@placeholder='Year']")).click();
		Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper").get(1).click();	
	}
	
	//Set Expiration Month
	public void setExpiryMonthRandom()
	{
		
		driver.findElement(By.xpath(".//input[@placeholder='Month']")).click();
		Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper").get(0).click();	
	}
	
	//Get element of Notes field
	public WebElement getNotesEle()
	{
		
		WebElement tmpLst = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":				
				tmpLst = driver.findElement(By.id("notes_visa"));
				break;
				
			case "fsa":
			case "svg":			
			default:
				//Input Note
				tmpLst =driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));				
				
		}
		return tmpLst;
	}
	
	
	public String getCCWDQuota()
	{
		String ccWithdrawQuota = "";
		String ccMessage;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				ccMessage = "";
				break;
				
			case "fsa":
			case "svg":
				ccMessage = driver.findElement(By.xpath("//p[@class='digitsCard__subinfo red']")).getText();

				break;
				
				default:
					ccMessage = driver.findElement(By.xpath("//p[@class='digitsCard__subinfo']")).getText();
		}
				
		
		ccWithdrawQuota = this.funcExtractAmount(ccMessage);		
		
		return ccWithdrawQuota;
	}
	
}
