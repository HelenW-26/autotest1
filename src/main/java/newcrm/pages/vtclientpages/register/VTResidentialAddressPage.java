package newcrm.pages.vtclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.register.ResidentialAddressPage;
import org.openqa.selenium.WebElement;

public class VTResidentialAddressPage extends ResidentialAddressPage {

	public VTResidentialAddressPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
	}
	@Override
	public String getCountry() {
		WebElement e = this.findVisibleElemntBy(By.id("countryId"));
		return e.getAttribute("value");
	}
}
