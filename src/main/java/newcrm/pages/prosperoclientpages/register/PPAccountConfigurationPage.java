package newcrm.pages.prosperoclientpages.register;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.AccountConfigurationPage;

public class PPAccountConfigurationPage extends AccountConfigurationPage {

	public PPAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void tickBox() {
		WebElement tick = this.findClickableElementByXpath("//label[@data-testid='checkbox']/span[1]");
		this.moveElementToVisible(tick);
		tick.click();
	}
}
