package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This class is comes from skrill withdrawpage. both of them have the same UI.
 * @author CRM QA Team
 *
 */

public class SticPayWithdrawPage extends SkrillWithdrawPage {

	public SticPayWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void setEmail(String email) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='sticpayEmail']");
		this.moveElementToVisible(input);
		input.sendKeys(email);
	}
}
