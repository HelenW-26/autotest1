package newcrm.pages.newbrandclientpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DemoAccountConfigurationPage;

public class NewBrandDemoAccountConfigurationPage extends DemoAccountConfigurationPage {

	public NewBrandDemoAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	
	public String setDemoAccountType() {
		WebElement e = this.findClickableElemntBy(By.xpath("//input[@id='accountType']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("CreateDemoPage: set Account Type to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: CreateDemoPage: set Account Type failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setDemoCurrency() {
		WebElement e = this.findClickableElemntBy(By.xpath("//input[@id='currency']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("CreateDemoPage: set currency to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: CreateDemoPage: set currency failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setDemoLeverage() {
		WebElement e = this.findClickableElemntBy(By.xpath("//input[@id='leverage']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("CreateDemoPage: set leverage to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: CreateDemoPage: set leverage failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setDemoBalance() {
		WebElement e = this.findClickableElemntBy(By.xpath("//input[@id='balance']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("CreateDemoPage: set balance to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: CreateDemoPage: set balance failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public WebElement getReponseElement() {
		return this.findVisibleElemntByXpath("//div[@class='main']/div");
	}
}
