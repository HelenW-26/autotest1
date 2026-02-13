/* Yanni on 12/03/2021 , Funds->Withdraw Funds, Withdraw Page - General Part
 * Select withdraw MT account(with the most money)
 * Select Withdraw Method
 * Input Withdraw amount element
 * Submit button
 * 
*/

package clientBase;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import vantagecrm.Utils;

public class withdrawMalay extends withdrawGeneral{
	
	
	  private WebDriver driver;
	  private String Brand;
	 
	
	public withdrawMalay(WebDriver driver, String Brand)
	{
		
		super(driver,Brand);
		this.driver = driver;
		this.Brand = Brand;
	}
	
	//After selecting Bank Transfer as the withdraw method, customers are then prompted to select Bank Account.  This function returns to the element
	public WebElement getBankAccountSelectEle()
	{
		WebElement tmpEle = null;
		
		switch(Brand.toLowerCase())
		{
			case "vfsc":
			case "cima":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-select']//input[@class='el-input__inner']")).get(2);
				break;
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-select']//input[@class='el-input__inner']")).get(1);
				break;
				
			default:
				//All regulators are with same code.
				tmpEle = driver.findElements(By.xpath("//div[@class='el-select']//input[@class='el-input__inner']")).get(1);
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
	
	
	//Get Bank Name element
	public WebElement getBankNameTxtbox()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vfsc":
			case "cima":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(0);
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(0);
				break;
			
			default:
				//All regulators are with same code.
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(0);
		}
		 
		 return tmpEle;
	}
	
	//Get Bank Address element
	public WebElement getBankAddTxtbox()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vfsc":
			case "cima":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(1);
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(1);
				break;
			
			default: 
				//All regulators are with same code.
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(1);
		}
		 
		 return tmpEle;
	}
	
	
	
	//Get Bank Account Number element
	public WebElement getBankAcctNoTxtbox()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vfsc":
			case "cima":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(3);
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(3);
				break;
			
			default:
				//All regulators are with same code.
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(3);
		}
		 
		 return tmpEle;
	}	
	
	//Get Bank Beneficiary element
	public WebElement getBankBeneficiaryNameTxtbox()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vfsc":
			case "cima":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(2);
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(2);
				break;
			
			default: 
				//All regulators are with same code.
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(2);
		}
		 
		 return tmpEle;
	}
	
	//Get Notes element
	public WebElement getNotesTxtbox()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vfsc":
			case "cima":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(4);
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(4);
				break;
			
			default: 
				//All regulators are with same code.
				tmpEle = driver.findElements(By.xpath("//div[@class='el-input']//input[@type='text']")).get(4);
		}
		 
		 return tmpEle;
	}
	

	//Get Remember My account check box
	public WebElement getRmbAcctChkbox()
	{
		//All regulators are with same code.
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.className("el-checkbox__inner"));
		 
		 return tmpEle;
	}
}
