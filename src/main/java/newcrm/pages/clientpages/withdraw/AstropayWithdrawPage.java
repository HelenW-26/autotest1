package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This class is comes from skrill withdrawpage. both of them have the same UI.
 * @author CRM QA Team
 *
 */

public class AstropayWithdrawPage extends SkrillWithdrawPage {

	public AstropayWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	public void setAstropayAccount(String accountInfo) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='accountNumber']");
		this.moveElementToVisible(input);
		input.sendKeys(accountInfo);
	}
}
