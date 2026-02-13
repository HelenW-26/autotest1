package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FasaPayWithdrawPage extends SkrillWithdrawPage {

	public FasaPayWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	public void setFasaPayAccountName() {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='accountName']");
		this.moveElementToVisible(input);
		input.sendKeys("Test FasaPay Name");
	}
	
	public void setFasaPayAccountNNumber(String accountInfo) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='accountNumber']");
		this.moveElementToVisible(input);
		input.sendKeys(accountInfo);
	}
	
	public void setFasaPayNotes(String notes) {
		this.setNotes(notes);
	}
}
