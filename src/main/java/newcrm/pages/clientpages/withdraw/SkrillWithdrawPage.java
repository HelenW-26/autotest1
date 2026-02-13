package newcrm.pages.clientpages.withdraw;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.WithdrawPage;

public class SkrillWithdrawPage extends WithdrawPage {

	public SkrillWithdrawPage(WebDriver driver) {
		super(driver);
	}

	public void setEmail(String email) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='email']");
		this.moveElementToVisible(input);
		input.sendKeys(email);
	}

	public void setNotes(String notes) {
		super.waitLoading();
		WebElement input_notes = this.findVisibleElemntByXpath("//input[@data-testid='importantNotes']");
		this.moveElementToVisible(input_notes);
		input_notes.sendKeys(notes);
	}
	
	public void setAccountName(String accountName) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='accountName']");
		this.moveElementToVisible(input);
		input.sendKeys(accountName);
		GlobalMethods.printDebugInfo("Set Account Name to: " + accountName);
	}	
	
	public void setAccountNumber(String accountNumber) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='accountNumber']");
		this.moveElementToVisible(input);
        this.waitLoading();
		input.sendKeys(accountNumber);
		GlobalMethods.printDebugInfo("Set Account Number / Email to: " + accountNumber);
	}

	public String setPaypalID(String accountNumber) {
		WebElement div = this.findVisibleElemntByTestId("accountNumber");
		this.moveElementToVisible(div);
		div.click();
		String paypalid = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		GlobalMethods.printDebugInfo("PaypalWithdrawPage: Set Paypal ID to: " + paypalid);
		return paypalid;
	}
}
