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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import vantagecrm.Utils;

public class withdrawGeneral {
	
	private WebDriver driver;
	private String Brand;
	
	public withdrawGeneral(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	//From Account Input Element: only after clicking this element, the account list is displayed in the dropdown
	public WebElement getFromAcctEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-success is-required']//input"));
				 break;
				 
			 case "fsa":
			 case "svg":
				 tmpEle= driver.findElement(By.xpath("//input[@placeholder='Select' and @class='el-input__inner']"));
				 break;
				 
				 default:
					 tmpEle= driver.findElement(By.xpath("//div[@data-testid='formAccount']"));
		 }		 	
		
		return tmpEle;		
		
	}
	
	//Get available withdraw account list. If the list is null, print error message and exit the test.
	public List<WebElement> getAllWithdrawAccounts()
	{
		
		List<WebElement> tmpLst = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		
		if(tmpLst.size()==0)
		{
			Assert.assertTrue(false,"No available MT4/5 accounts for withdraw.");
		}
		return tmpLst;
	}
	
	//Return the index of the account with the most money	
	public WebElement getMostRichAccount(List<WebElement> actList)
	{
		int actIndex = 0;
		BigDecimal acctBalance = new BigDecimal("0.00");  
		BigDecimal tempBalance = new BigDecimal("0.00"); 
		String[] acctText;
		
		//PUG account list format: li/span: 100001597 - $128.00 USD
		//AU: li/span: 920152 - $8.09 USD
		//VT: li/span: 8000376 - C$160.16 CAD
		
		for(int i=0; i<actList.size(); i++)
		{
			acctText = actList.get(0).getText().split("-");
			tempBalance = new BigDecimal(this.funcExtractAmount(acctText[1]));
			
			if(acctBalance.compareTo(tempBalance) < 0)
			{
				acctBalance = tempBalance;
				actIndex = i;
			}
		}
		
		return actList.get(actIndex);
	}

	
	//Extract numbers and decimal point: to get the balance
	public String funcExtractAmount(String amountText)
	{
		String extractedText = "";
		
		String extractExp="\\d+(\\.\\d+)?";
		Pattern pat=Pattern.compile(extractExp);
		Matcher mat=pat.matcher(amountText);
		if(mat.find())
		{
			//If matched, return all matched result.
			extractedText = mat.group(0);
		}else
		{
			System.out.println("Can't find decimal numbers in text " + amountText);
		}
		return extractedText;
	}
	
	//The element of Withdraw Method
	public WebElement getWDMethodEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 //tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input[@placeholder='Select']"));
				 tmpEle= driver.findElement(By.id("withdrawalType")) ;
				 break;
				 
			 case "fsa":
			 case "svg":
				 /*
				  * PUG UI has totally different design. No need to click the element before showing all withdraw methods. 
				  * To unify the behavior across platforms, return the 1st tab Bank Transfer. But this doesn't make any sense.				 
				 */
				 tmpEle= driver.findElement(By.id("tab-0")) ;
				 break;
				 
			default:
				 tmpEle= driver.findElement(By.xpath("//div[@data-testid='type']"));
		 }		 	
		
		return tmpEle;		
		
	}
	
	//Get the element of withdraw method: AU & VT. wdMethod should be exactly the same as displayed in Webpage
	private WebElement selectWDMethodNonPUG(String wdMethod) throws InterruptedException
	{
	     List<WebElement> methods = null ;
	     WebElement tmpEle=null;
	     String methodxPathLocator = "//span[contains(text(),'method')]";
	     String tempTxt;
	     int i=0;
	     
	     methodxPathLocator = methodxPathLocator.replace("method", wdMethod);
	     this.getWDMethodEle().click();
         
	     methods=Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");	    
         
		if (methods.size()==0) {
		  Assert.assertTrue(false, "Can not load withdraw methods!");
		}
		
	    Thread.sleep(1000);
	    
	     //Yanni on 06/04/2021: AU/VT has a long list. Need to scroll to the withdraw method before selection otherwise exception will occur.
			
		//Find the index of the withdraw method  
		for(i=0; i<methods.size(); i++)
		  {
			  tempTxt = methods.get(i).findElement(By.xpath(".//span")).getText();
			  
			  if(tempTxt.contains(wdMethod))
			  {
				  break;
			  }
		  }
		
		  
		//If scrolling screen is required, that is, i>6, scroll the screen to locate the withdraw method first.  
		if(i>6 && i<methods.size())
		  {
			  ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", methods.get(i));
		  }else if(i>=methods.size())
		  {
			  System.out.println("Can't find " + wdMethod + " in Withdraw List ");
			  Assert.assertTrue(false, "Can't find " + wdMethod + " in Withdraw List ");
		  }
			 

		
	     try
	     {
	    	 tmpEle= driver.findElement(By.xpath(methodxPathLocator));
	     }catch(NoSuchElementException e)
	     {
	    	 System.out.println("Can't find " + wdMethod + " in Withdraw List ");
	     }
		return tmpEle;		
		
	}
	
	//Get the element of specified withdraw method: PUG. wdMethod should be the combination of Category and withdraw method. Format should be like 'Category-withdrawMethod'
	private WebElement selectWDMethodPUG(String wdMethod) throws Exception
	{

	     WebElement tmpEle=null;
	     
	     String wdCategory, wdMethodName;
	     String methodxPathLocator = "//span[contains(text(),'method')]";
	     	     
	     wdCategory = wdMethod.split(":")[0].trim();
	     wdMethodName = wdMethod.split(":")[1].trim();	    		 
	    
	     methodxPathLocator = methodxPathLocator.replace("method", wdMethodName);
	     
	     //Click Category tab
	     switch(wdCategory.toUpperCase())
	     {
	     	
	     	case "BANK TRANSFER":
	     		//click the category tab then wait for 1 second
	     		driver.findElement(By.xpath("//div[@id='tab-0']")).click();
	     		break;
	     
	     	case "CREDIT CARD":
	     		driver.findElement(By.xpath("//div[@id='tab-1']")).click();
	     		break;
	     		
	     	case "E-WALLET":
	     		driver.findElement(By.xpath("//div[@id='tab-2']")).click();
	     		break;
	     		
	     	case "CRYPTOCURRENCY":
	     		driver.findElement(By.xpath("//div[@id='tab-3']")).click();
	     		break;	
	     		
	     	case "LOCAL TRANSFER":
	     		driver.findElement(By.xpath("//div[@id='tab-4']")).click();
	     		break;
	     }
	     
	     Thread.sleep(1000);
	     
	     tmpEle= driver.findElement(By.xpath(methodxPathLocator));
	     
		return tmpEle;		
		
	}
	
	//Get the element of withdraw method: for all Brands
	public WebElement selectWDMethodEle(String wdMethod) throws Exception
	{
	
        WebElement tmpEle=null;
        
        switch(Brand.toLowerCase())
        {
        	case "fsa":
        	case "svg":
        		tmpEle=selectWDMethodPUG(wdMethod);
        		break;
        	
        	default:
        		tmpEle=selectWDMethodNonPUG(wdMethod);
        }

		return tmpEle;		
		
	}
	
	//Get withdrawAmount element
	public WebElement getAmountEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.xpath("//div[@class='el-input']//input[@class='el-input__inner']"));
				 break;
			 case "fsa":
			 case "svg":  //
				 tmpEle = driver.findElement(By.xpath("//li[@class='fr colAmount']//div[@class='el-input']//input[@class='el-input__inner']"));
				 break;
				 
				 default:
					 tmpEle= driver.findElement(By.xpath("//input[@data-testid='numericInput']"));
		 }		 	
		
		return tmpEle;				
	 }
	
	//Get SUBMIT button in withdraw form
	public WebElement getSubmitBtn()
	{
		WebElement tmpEle=null;
		
		tmpEle = driver.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]"));
		
		return tmpEle;		
	}	
	
	//VT, PUG has minimium withdraw amount. This function is to click the Confirm button if there has.
	public  void clickConfirmFeeBtn()
	{
	    //Confirm in the withdraw fee popup dialog if it pops up
	
		try
		{
			driver.findElement(By.xpath("//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]")).click();
		}catch(Exception e)
		{
			System.out.println("No Confirm Fee Dialog pops up");
		}
		    				
	}
	
	
	//Get Continue button after input amount
	public WebElement getContinueBtn()
	{
		WebElement tmpEle=null;
		
		tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Continue')]"));
		
		return tmpEle;		
	}	
	
}
