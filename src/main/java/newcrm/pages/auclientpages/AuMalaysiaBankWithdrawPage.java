package newcrm.pages.auclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

public class AuMalaysiaBankWithdrawPage extends LocalBankWithdrawPage {

	public AuMalaysiaBankWithdrawPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void setImportantNotes(String notes) {
		WebElement input_account = this.findVisibleElemntByXpath("(//input[@data-testid='importantNotes'])[2] | //input[@data-testid='importantNotes']");
		this.moveElementToVisible(input_account);
		input_account.clear();
		input_account.sendKeys(notes);
		GlobalMethods.printDebugInfo("Set Important Notes to: " + notes);
	}
}
