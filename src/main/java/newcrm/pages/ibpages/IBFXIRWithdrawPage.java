package newcrm.pages.ibpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;

public class IBFXIRWithdrawPage extends IBEmailWithdrawPage {

	public IBFXIRWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	public void setFXIRSenderID(String senderID) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='fxir_id']");
		this.moveElementToVisible(input);
		input.sendKeys(senderID);
	}
}
