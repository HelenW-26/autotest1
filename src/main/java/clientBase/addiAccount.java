package clientBase;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


/*
 * Extracted elements collection for OPEN ADDITIONAL ACCOUNTS page in CP
 */
public class addiAccount {
	
	private WebDriver driver;
	private String Brand;	

	
	public addiAccount(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;	
		
	}
	
	
	//Get all available platforms
	public List<WebElement> getPlatforms()
	{
		List<WebElement> listPlatform = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				listPlatform = driver.findElements(By.xpath("//div[@class='el-form-item is-required']")).get(0).findElements(By.tagName("img"));	
				break;
			case "fsa":
			case "svg":
				listPlatform = driver.findElements(By.cssSelector("div.box.box_platform li"));
				break;				
				
				default:
					listPlatform = driver.findElements(By.cssSelector("div.box.box_platform li>img"));
					
		}

		return listPlatform;
	}
	
	//Get all available account types
	public List<WebElement> getAccountTypes()
	{
		List<WebElement> tempList = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tempList = driver.findElements(By.xpath("//div[@class='el-form-item is-required']")).get(1).findElements(By.tagName("img"));				
				break;
			case "fsa":
			case "svg":
				tempList = driver.findElements(By.cssSelector("div.box.box_type li"));
				break;
				default:
					tempList = driver.findElements(By.cssSelector("div.box.box_type li>img"));
					
		}

		return tempList;
	}
	
	//Get all available currencies
	public List<WebElement> getCurrencies()
	{
		List<WebElement> tempList = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tempList = driver.findElements(By.xpath("//div[@class='el-form-item is-required']")).get(2).findElements(By.tagName("img"));				
				break;
			case "fsa":
			case "svg":
				tempList = driver.findElements(By.cssSelector("div.box.box_currency li img"));
				break;
			case "vfsc2"://liufeng 24.06.2021
				tempList = driver.findElements(By.cssSelector("div.box.box_currency li>div>img"));
				break;
				default:
					tempList = driver.findElements(By.cssSelector("div.box.box_currency li>img"));
					
		}
		//liufeng
		if(tempList.isEmpty()) {
			System.out.println("======Get currencies failed!!!===");
		}
		return tempList;
	}
	
	//Get MT4 platform
	public WebElement getMT4Plat()
	{
		WebElement mt4Plat = null;
		mt4Plat = getPlatforms().get(0);
		
		
		return mt4Plat;
	}
	
	//Get MT5 platform	
	public WebElement getMT5Plat()
	{
		WebElement tempEle = null;
		
		tempEle= getPlatforms().get(1);
		return tempEle;
	}
	
	//Get specific account type element
	public WebElement getAccountType(int typeIndex)
	{
		WebElement tempEle = null;
		
		tempEle= getAccountTypes().get(typeIndex);		
		return tempEle;
	}
	
	
	public WebElement getCurrency(int curIndex)
	{
		WebElement tempEle = null;
		tempEle=getCurrencies().get(curIndex);

		return tempEle;
	}
	

	//Get additional notes element
	public WebElement getNotes()
	{
		WebElement tempEle = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tempEle= driver.findElement(By.id("notes"));			
				break;
			case "fsa":
			case "svg":
				tempEle = driver.findElement(By.cssSelector("div.box.box_notes input"));
				break;				
				
				default:
					tempEle = driver.findElement(By.cssSelector("div.box input"));
					
		}
		
		
		return tempEle;
	}
	
	//Agreement checkbox
	public WebElement getAgreeCheckbox()
	{
		WebElement tempEle = null;
		
		tempEle = driver.findElement(By.xpath("//span[@class='el-checkbox__inner']"));					
	
		return tempEle;
	}
	
	//Submit button
	public WebElement getBtnSubmit()
	{
		WebElement tempEle = null;
		
		tempEle = driver.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]"));					
	
		return tempEle;
	}
	
	//After submit success, the Back to Home page button
	public WebElement getBack2HomePageBtn()
	{
		WebElement tempEle = null;
		
		//tempEle = driver.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]"));	
		switch(Brand.toLowerCase())
		{
			case "vt":
				tempEle = driver.findElement(By.cssSelector("a.el-button.blue_button"));
				break;
				
			case "fsa":
			case "svg":
				tempEle = driver.findElement(By.cssSelector("a.el-button.btn_red"));
				break;
				
				default:
					tempEle = driver.findElement(By.cssSelector("a.el-button.btn_blue"));
		}
		
		return tempEle;		
	}
}
