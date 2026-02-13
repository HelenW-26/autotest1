package newcrm.pages.ibpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IBBitwalletWithdrawPage extends IBEmailWithdrawPage {

	public IBBitwalletWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected WebElement getAccountInput() {
		return this.findVisibleElemntByTestId("bitcoinEmail");
	}
}
