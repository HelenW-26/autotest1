package clientBase;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/*
 * Yanni on 12/02/2021:  defines cp header part after user logs in CP.
 * vt: only 2 menus: language switch menu and customer name menu
 * AU: 3 MENUS:  sidebar menu, language switch menu and customer name menu
 * PUG: menus are the same as AU. But customer name dropdown menu has more menu items: My Profile and Change CP Password
 */

public class cpHeader {
	
	private WebDriver driver;
	private String Brand;
	
	public cpHeader(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	public WebElement getHideSideBarBtn()
	{
		
		WebElement tempEle = null;
		switch(Brand)
		{
			case "fsa":
			case "svg":
				tempEle = driver.findElements(By.cssSelector("img.header_btn")).get(0);
				break;
				
				
			case "vt":
				System.out.println(Brand + " doesn't have the sidebar folding button");
				 break;
				 
				
				default:
					tempEle = driver.findElement(By.cssSelector("div.header_btn"));	
		}
		
		return tempEle;
	}
	
	public WebElement getShowSideBarBtn()
	{
		
		WebElement tempEle = null;
		switch(Brand)
		{
			case "fsa":
			case "svg":
				tempEle = driver.findElements(By.cssSelector("img.header_btn")).get(1);
				break;
				
				
			case "vt":
				System.out.println(Brand + " doesn't have the sidebar folding button");
				 break;
				 
			//for vfsc2, liufeng
			case "vfsc2":
				tempEle = driver.findElement(By.cssSelector("header li.fl div.header_btn "));
				break;
				default:
					tempEle = driver.findElement(By.cssSelector("img.menu_back"));	
		}
		
		return tempEle;
	}
	
	public WebElement getLangSwitchMenu()
	{
		WebElement tempEle = null; //
		
		switch(Brand)
		{
			case "fsa":
			case "svg":
				tempEle = driver.findElements(By.cssSelector("div.flag.el-dropdown>div>img")).get(0);
				break;
				
				
			case "vt":
				tempEle = driver.findElement(By.cssSelector("div#lang"));
				 break;
				 
				
				default:
					tempEle = driver.findElement(By.cssSelector("li.flag.flagLogin>div>img"));	
		}
		
		
		
		
		return tempEle;
		
	}
	
	public WebElement getCustNameEle()
	{
		WebElement tempEle = null;
	    tempEle = driver.findElement(By.cssSelector("div.login_inner.el-dropdown-selfdefine"));
		
	
		return tempEle;
		
	}
	
	//PUG only: PUG MY PROFILE menu is moved to the top right corner
	public WebElement getMyProfileEle()
	{
		WebElement tempEle = null;
	    tempEle = driver.findElement(By.xpath("//span[contains(text(),'MY PROFILE')]"));
		
	
		return tempEle;
		
	}
	
	//PUG only: PUG MY PROFILE menu is moved to the top right corner
	public WebElement getChgPwdEle()
	{
		WebElement tempEle = null;
	    tempEle = driver.findElement(By.xpath("//li[@class='el-dropdown-menu__item cpPassword']/span[contains(text(),'CHANGE PASSWORD')]"));		
	
		return tempEle;
		
	}
	
	//Get language element
	public WebElement getLanguage(String language)
	{
		WebElement tempEle = null;
		String xpathLoca= "//span[contains(text(),'" + language + "')]";
	    tempEle = driver.findElement(By.xpath(xpathLoca));
		
		return tempEle;
		
	}
	
	//Get LogOut element
	public WebElement getLogoutEle()
	{
		WebElement tempEle = null;		
	    tempEle = driver.findElement(By.xpath("//span[translate(text(),'Logout','LOGOUT')='LOGOUT']"));
		
		return tempEle;
		
	}
}
