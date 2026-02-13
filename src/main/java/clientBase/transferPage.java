package clientBase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class transferPage {
	
	private WebDriver driver;
	private String Brand;
	
	public transferPage(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	//From Account Element
	public WebElement getFromAcctEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("fromAccount"));
				 break;
				 
			 case "fsa":
			 case "svg":
				 tmpEle= driver.findElement(By.cssSelector("ul.form_list.clearfix li:nth-of-type(1) input"));
				 break;
				 
				 default:
					 tmpEle= driver.findElement(By.cssSelector("li.fl div.el-select div> input"));
		 }
		
		return tmpEle;		
		
	}
	
	//To Account Element
	public WebElement getToAcctEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "fsa":
			 case "svg":
				 tmpEle = driver.findElement(By.cssSelector("ul.form_list.clearfix li:nth-of-type(3) input"));
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input[@placeholder='Select']"));
		 }
		
		
		return tmpEle;		
		
	}

	//Transfer Amount Element
	public WebElement getAmountEle()
	{
		WebElement tmpEle=null;
		
		switch(Brand.toLowerCase())
		 {
			 case "fsa":
			 case "svg":
				 tmpEle = driver.findElement(By.cssSelector("ul.form_list.clearfix li:nth-of-type(2) input"));
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-input']//input"));
		 }		 
		
		return tmpEle;		
		
	}
	
	//Submit button
	public WebElement getSubmitBtn()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]"));	
		
		return tmpEle;		
		
	}
	
	//Open Position dialog confirm button
	public WebElement getOpenPosConfirmBtn()
	{
		WebElement tmpEle=null;
		
		tmpEle = driver.findElement(By.xpath("//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]"));
		
		return tmpEle;		
		
	}
	
	//After transfer is submitted successfully, Back to Home Page button
	public WebElement getBack2HomeBtn()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 
		 	case "vt":
		 		tmpEle= driver.findElement(By.xpath("//a[@class='el-button blue_button']"));
		 		break;
		 	case "fsa":
			case "svg":
		 		tmpEle= driver.findElement(By.xpath("//a[@class='el-button btn_red']"));
				 break;
				 
				 default:
					 tmpEle= driver.findElement(By.xpath("//a[@class='el-button btn_blue']"));		
		 }
		
		return tmpEle;		
		
	}
}
