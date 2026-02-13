package clientBase;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Extract elements in Accounts->Live Accounts page. Not including the left navigation bar and top navigation bar
 */

public class liveAcctHome {
	
	private WebDriver driver;
	private String Brand;	

	
	public liveAcctHome(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;	
		
	}
	
	//select platform = actPlatform, status = actStatus and return the account list
	public List<WebElement> getMTActList(String actPlatform, String actStatus)
	{
		
		List<WebElement> tempListEle = null;   //original list
		List<WebElement> tempFilteredList = null;   //filtered result
		
		int rowNo;
		String cellValuePlat, cellValueStatus;
		
	
		switch(Brand)
		{
			
			//PUG MT4 accounts and MT5 accounts are mixed in one section
			case "fsa":
			case "svg":
				 tempListEle = driver.findElements(By.cssSelector("table.el-table__body tr.el-table__row"));
				 //System.out.println("Table List Size: " + tempListEle.size());
				 
				 if(tempListEle.size()>0)
				 {
					 //Loop the table and remove unmatched records
					 for(rowNo=0; rowNo < tempListEle.size(); rowNo ++)
					 {
						 cellValuePlat = tempListEle.get(rowNo).findElement(By.cssSelector("td:nth-of-type(3)")).getText().trim();
						 cellValueStatus = tempListEle.get(rowNo).findElement(By.cssSelector("td:nth-of-type(7)")).getText().trim();
	
						 //if the record doesn't match the right platform or right status, remove it from the result.
						 if(!cellValuePlat.equalsIgnoreCase(actPlatform) || !cellValueStatus.equalsIgnoreCase(actStatus))
						 {
							 tempListEle.remove(rowNo);
							 rowNo --;						 
							 
						 }						 
						 
					 }
					 
					//if filtering is done and no results are qualified, quit immediately. 
				     if(tempListEle.size() ==0)
					 {
						 System.out.println("No qualified Accounts are found in Account List.");
						 Assert.assertTrue(false);
					 }
					 
				 }else
				 {
					 System.out.println("No Accounts are found in Account List.");
					 Assert.assertTrue(false);
				 }
	
				break;
				
				default:
					//Brands except for PUG have dedicated sections for MT4 and MT5
					if(actPlatform.equalsIgnoreCase("mt5"))
					{
						//Get MT5 table list 
						tempListEle = driver.findElements(By.cssSelector("div.table_item_box:nth-of-type(2) table.el-table__body tr"));

					}else
					{
						//Get MT4 table list
						tempListEle = driver.findElements(By.cssSelector("div.table_item_box:nth-of-type(1) table.el-table__body tr"));
					}
					 
					 
					 if(tempListEle.size()>0)
					 {
						 
						 //Filtering results
						 for(rowNo=0; rowNo < tempListEle.size(); rowNo ++)
						 {
							 //liufeng 24.06.2021 for vfsc2
							
							 cellValueStatus = tempListEle.get(rowNo).findElement(By.xpath("//div[@class='gray']/i | //div[@class='white']")).getText().trim();
							 //cellValueStatus = tempListEle.get(rowNo).findElement(By.cssSelector("td.el-table_4_column_28 div.blue")).getText().trim();
							 
							 if(!cellValueStatus.equalsIgnoreCase(actStatus))
							 {
								 tempListEle.remove(rowNo);
								 rowNo --;
								 
							 }						 
							 
						 }
						 
						//After filtering is done but no results are qualified, quit directly. 
						 if(tempListEle.size() ==0)
						 {
							 System.out.println("No qualified Accounts are found in Account List.");
							 Assert.assertTrue(false);
						 }
						 
					 }else
					 {
						 System.out.println("No Accounts are found in Account List.");
						 Assert.assertTrue(false);
					 }
		
		} //switch end
		
		return tempListEle;
	}  //function end
	
//Change Password Icon in the list
	public WebElement getPwdIcon(WebElement recordRow)
	{
		WebElement pwdEle=null;
		switch(Brand)
		{
			case "fsa":
			case "pug":   //el-table_1_column_8  
				pwdEle = recordRow.findElement(By.cssSelector("td.el-table_1_column_8 a"));
				break;
			//liufeng for vfsc2:
			case "vfsc2":
				pwdEle = recordRow.findElement(By.xpath("//div[@class='cell']/a/img"));
				break;
				default:
					pwdEle=recordRow.findElement(By.cssSelector("td.el-table_1_column_7 a"));
		}
		
		return pwdEle;
	}

	//Change leverage icon in the list
	public WebElement getLeverageIcon(WebElement recordRow)
	{
		WebElement pwdEle=null;
		
		switch(Brand)
		{
			case "fsa":
			case "pug":   //el-table_1_column_8  
				pwdEle = recordRow.findElement(By.cssSelector("td.el-table_1_column_6 a"));
				break;
				
				default:
					pwdEle=recordRow.findElement(By.cssSelector("td.el-table_1_column_5 a"));
		}
		return pwdEle;
	}
	
	//Account Type Text in the list
	public String getAcctNoTxt(WebElement recordRow)
	{
		String tempStr;
		
		tempStr = recordRow.findElements(By.tagName("td")).get(0).getText();
		return tempStr;
	}
	
	
	//Account Type Text in the list
	public String getAcctTypeTxt(WebElement recordRow)
	{
		String tempStr;
		
		tempStr = recordRow.findElements(By.tagName("td")).get(1).getText();
		return tempStr;
	}
	
	//Currency Text in the list
	public String getCurrencyTxt(WebElement recordRow)
	{
		String tempStr;
		
		switch(Brand)
		{
		case "fsa":
		case "svg":
			tempStr = recordRow.findElements(By.tagName("td")).get(3).getText();
			break;
			
		default:
			tempStr = recordRow.findElements(By.tagName("td")).get(2).getText();
		}
		return tempStr;
	}
	
	//Status Text in the list
	public String getStatusTxt(WebElement recordRow)
	{
		String tempStr;
		switch(Brand)
		{
		case "fsa":
		case "svg":
			tempStr = recordRow.findElements(By.tagName("td")).get(6).getText();
			break;
			
		default:
			tempStr = recordRow.findElements(By.tagName("td")).get(5).getText();
		}
		
		return tempStr;
	}
}
