package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class FXIRWithdrawPage extends SkrillWithdrawPage {

	public FXIRWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	public void setFXIRSenderID(String senderID) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='fxir_id']");
		this.moveElementToVisible(input);
		input.sendKeys(senderID);
	}
}
