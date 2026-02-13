package clientBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class cpLogIn {
	
	private WebDriver driver;
	private String Brand;
	
	public cpLogIn(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	//Login Window: Input EMAIL Element
	public WebElement getInputEmail()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("userName_login"));
				break;
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//input[@placeholder='Email']"));
				break;
				
				default:
					tmpEle = driver.findElement(By.xpath("//input[@data-testid='userName_login']"));
				
		}
		
		return tmpEle;		
		
	}
	
	//Login Window: Input Password Element
	public WebElement getInputPwd()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("password_login"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//input[@placeholder='Password']"));
				break;		
				
				default:
					tmpEle = driver.findElement(By.xpath("//input[@data-testid='password_login']"));
				
		}
		
		return tmpEle;		
		
	}
	
	//Login Window: Submit button
	public WebElement getLogINBtn()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.cssSelector("button.el-button"));
		
		switch(Brand)
		{
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.cssSelector("button.el-button.btn_blue.el-button--default"));
				break;
				
				default:
					tmpEle = driver.findElement(By.cssSelector("button.el-button"));
				
		}
		
		return tmpEle;		
		
	}
	
	//Switch language icon	
	public WebElement getLanguageIcon()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
	
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//img[@class='areaImg']"));
				break;
				
				default:
					tmpEle = driver.findElement(By.xpath("//div[@data-testid='dropdownFlag']"));
			
		}		
	
		return tmpEle;	
	}
	
	//Language Elements - English is default option
	public WebElement getLanguageOption(String strLang)
	{
		WebElement tmpEle=null;
		String langLocator = "//span[contains(text(),'English')]";
		
		if(strLang.length()>0)
		{
			langLocator = langLocator.replace("English", strLang);
		}
		
		tmpEle = driver.findElement(By.xpath(langLocator));	
	
		return tmpEle;	
	}
	
	//Reset CP Password Link	
	public WebElement getRestPwdLink()
	{
		WebElement tmpEle=null;
		tmpEle = driver.findElement(By.xpath("//a[contains(translate(text(),'Forgot password', 'Forgot Password'),'Forgot Password')]"));	
	
		return tmpEle;	
	}
}
