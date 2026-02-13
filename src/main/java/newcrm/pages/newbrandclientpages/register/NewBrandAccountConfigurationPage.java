package newcrm.pages.newbrandclientpages.register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.AccountConfigurationPage;

public class NewBrandAccountConfigurationPage extends AccountConfigurationPage {

	public NewBrandAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	

	public String setPlatForm() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='platform']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("AccountConfigurationPage: set Platform to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: AccountConfigurationPage: set Platform failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setAccountType() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='accountType']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("AccountConfigurationPage: set accountType to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: AccountConfigurationPage: set accountType failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setCurrency() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='currency']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("AccountConfigurationPage: set currency to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: AccountConfigurationPage: set currency failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public void tickBox() {
		WebElement tick = this.findClickableElementByXpath("//label[@data-testid='checkbox']/span[1]");
		this.moveElementToVisible(tick);
		tick.click();
	}
}
