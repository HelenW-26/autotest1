package newcrm.pages.ibpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;

public class IBFasaPayWithdrawPage extends IBEmailWithdrawPage {

	public IBFasaPayWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected WebElement getAccountInput() {
		return this.findVisibleElemntByTestId("accountNumber");
	}
	
	public void setAccountName(String name) {
		WebElement e = this.findVisibleElemntByTestId("bankName");
		this.moveElementToVisible(e);
		e.clear();
		e.sendKeys(name);
		GlobalMethods.printDebugInfo("IBFasaPayWithdrawPage: set account name to: " + name);
	}
}
