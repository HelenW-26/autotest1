package clientBase;

/*Yanni on 12/03/2021 , Funds->Withdraw Funds, Withdraw Page - General Part
 * Select withdraw MT account(with the most money)
 * Select Withdraw Method
 * Input Withdraw amount element
 * Submit button
 * 
*/
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import vantagecrm.Utils;

public class withdrawIndia extends withdrawGeneral{
	
	
	  private WebDriver driver;
	  private String Brand;
	 
	
	public withdrawIndia(WebDriver driver, String Brand)
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
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@id='indiaForm']/form/ul//input"));
				break;
			
			default:
				tmpEle = driver.findElement(By.xpath("//div[@id='thaiForm']//div[@class='card_left']//input"));
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
	public WebElement getBankNameEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(0);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(0);
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
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(1);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(1);
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
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(2);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(2);
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
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(3);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(3);
		}
		 
		 return tmpEle;
	}	
	
	//Get Bank Branch element
	public WebElement getBankBranchEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(4);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(4);
		}
		 
		 return tmpEle;
	}	
	
	//Get IFSC Code element
	public WebElement getIFSCodeEle()
	{
		WebElement tmpEle=null;
		switch(Brand.toLowerCase())
		{
			case "vt":
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(5);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(5);
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
				break; // VT doesn't support India Bank Transfer
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='indiaForm']/form/div//input")).get(6);
				break;
			
			default:
				tmpEle = driver.findElements(By.xpath("//div[@id='thaiForm']/form/div[3]//input")).get(6);
		}
		 
		 return tmpEle;
	}
	

	//get Remeber My account checkbox
	public WebElement getRememberAccountCkb()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//span[@class='el-checkbox__inner']"));
		 
		 return tmpEle;
	}
}
