package clientBase;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import vantagecrm.Utils;

/*
 * Yanni on 01/03/2021
 * Extract elements in ID POA UPLOAD dialog and related popup windows.
 */
public class idPOAUpload {

	private WebDriver driver;
	private String Brand;	

	
	public idPOAUpload(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;	
		
	}

	//ID/POA Upload Window: Upload button
	public WebElement getUploadFileEle(String fileType)
	{
		
		WebElement tempEle = null;
		int typeIndex = 0;  //default: id upload
		String confirmPrompt = "Confirm ID"; //default  ID prompt
		
		//When there are only 1 upload control, decide whether it is ID UPLOAD or POA UPLOAD
		switch(Brand)
		{
			case "vt":
				if(fileType.equalsIgnoreCase("poa"))
				{
					typeIndex = 1;
					confirmPrompt = "Proof of residency";
				}else if(fileType.equalsIgnoreCase("id"))
				{
					typeIndex = 0;
					confirmPrompt = "Proof of identity";
				}
				break;
				
				default:
					if(fileType.equalsIgnoreCase("poa"))
					{
						typeIndex = 1;
						confirmPrompt = "Confirm POA";
					}else if(fileType.equalsIgnoreCase("id"))
					{
						typeIndex = 0;
						confirmPrompt = "Confirm ID";
					}
		}
				
	
		//If both ID & POA are required to upload, that is, there are 2 Select File controls
		if(driver.findElements(By.xpath("//input[@name='file']")).size() > 1) 
		{	
			tempEle = driver.findElements(By.xpath("//input[@name='file']")).get(typeIndex);

		}else {
			
			//If there is only one upload window and is ID Proof Upload Window
			if (this.getConfirmLabel().getText().contains(confirmPrompt)) 
			{
				tempEle = driver.findElement(By.xpath("//input[@name='file']"));
					
			}else {
				
				System.out.println("\n NOT " + confirmPrompt + " Upload Window!  ");
			}
		}
	
		return tempEle;
	}
	
	public WebElement getConfirmLabel()
	{
		WebElement tempEle = null;
		
		switch(Brand)
		{
			case "vt":
				tempEle = driver.findElement(By.xpath("//div[@id='block']//div[@class='title']"));
				break;
			case "fsa":
			case "svg":
				tempEle=driver.findElement(By.cssSelector("div#block strong"));;
				break;
				
				default:
					tempEle = driver.findElement(By.cssSelector("div#block strong > b"));
		}

		return tempEle;
		
	}

	
	//ID/POA Upload Window: Upload button
	public WebElement getUploadBtn()
	{
		
		WebElement tempEle = null;
		
		switch(Brand)
		{
			case "vt":
				tempEle=driver.findElement(By.xpath("//div[contains(text(),'Upload')]"));
				break;
			case "fsa":
			case "svg":
				tempEle=driver.findElement(By.xpath("//span[contains(text(),'UPLOAD')]"));
				break;
				
				default:
					tempEle = driver.findElement(By.xpath("//div[contains(text(),'UPLOAD')]"));
		}

		return tempEle;
		
	}
	
	//After ID/POA upload button clicked, the success message element of popup dialog 
	public WebElement getMsgEle()
	{
		
		WebElement tempEle = null;
		
		switch(Brand)
		{
			case "vt":
				tempEle=driver.findElement(By.cssSelector("div#success div.el-dialog__body p"));
				break;
			case "fsa":
			case "svg":
				tempEle=driver.findElement(By.cssSelector("div#success div.el-dialog__body div.dialog_body p"));
				break;
				
				default:
					tempEle = driver.findElement(By.cssSelector("div#success p"));
		}

		return tempEle;
		
	}	
	
	//Close popup message
	public void clickClosebtn()
	{
	
		switch(Brand)
		{
			case "vt":
				driver.findElement(By.cssSelector("img.closeImg")).click();
				break;
			case "fsa":
			case "svg":
				driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
				break;
				
				default:
					driver.findElement(By.cssSelector("img.closeImg")).click();
		}

	}

}
