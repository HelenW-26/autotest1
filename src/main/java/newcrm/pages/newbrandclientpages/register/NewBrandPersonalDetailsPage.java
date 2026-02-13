package newcrm.pages.newbrandclientpages.register;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.PersonalDetailsPage;

public class NewBrandPersonalDetailsPage extends PersonalDetailsPage{
	
	public NewBrandPersonalDetailsPage (WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getDayInput() {
		return this.findClickableElementByXpath("//input[@placeholder='Day']");
	}
}
