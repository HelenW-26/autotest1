package newcrm.pages.auibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AUIBTransactionHistoryPage extends IBTransactionHistoryPage {

	public AUIBTransactionHistoryPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected void clickWithdraw() {
		WebElement e = this.findClickableElementByXpath("//div[@id='tab-withdrawHistory']");
		this.moveElementToVisible(e);
		e.click();
		this.waitLoading();
		GlobalMethods.printDebugInfo("Click Withdrawal Tab");
	}

}
