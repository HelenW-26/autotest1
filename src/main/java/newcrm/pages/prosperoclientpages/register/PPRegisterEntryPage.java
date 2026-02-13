package newcrm.pages.prosperoclientpages.register;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterEntryPage;

public class PPRegisterEntryPage extends RegisterEntryPage {

	public PPRegisterEntryPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected List<WebElement> getCountryElements(){
		String xpath = "//select[@id='country']/option";
		this.findVisibleElemntByXpath(xpath);
		return driver.findElements(By.xpath(xpath));
	}
	
	@Override
	public List<String> getCountries(){
		ArrayList<String> result = new ArrayList<>();
		this.getCountryInput().click();
		List<WebElement> countries = this.getCountryElements();
		for(WebElement e:countries) {
			String country = e.getAttribute("value");
			if(country != null && country.trim().length()>0) {
				result.add(country.trim());
			}
		}
		this.getCountryInput().click();
		return result;
	}
	
	@Override
	public boolean setCountry(String country) {
		this.getCountryInput().click();
		List<WebElement> countries = this.getCountryElements();
		for(WebElement e:countries) {
			String value = e.getAttribute("value");
			if(value != null && value.trim().length()>0) {
				if(country.equalsIgnoreCase(value.trim())) {
					this.moveElementToVisible(e);
					e.click();
					GlobalMethods.printDebugInfo("RegisterEntryPage: set country to: " + value.trim());
					return true;
				}
			}
		}
		GlobalMethods.printDebugInfo("RegisterEntryPage: Do not find country : " + country +".");
		this.getCountryInput().click();
		return false;
	}
}
