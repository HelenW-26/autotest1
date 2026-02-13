package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.WithdrawPage;

public class UnionPayWithdrawPage extends WithdrawPage {

	public UnionPayWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	protected WebElement getCardsDiv() {
		return this.findClickableElemntByTestId("bankCard");
	}
	
	public String selectCard() {
		this.getCardsDiv().click();
		String result = this.selectRandomValueFromDropDownList(getAllOpendElements());
		GlobalMethods.printDebugInfo("UnionPayWithdrawPage: select card: " + result);
		return result;
	}
	
	public boolean hasCard() {
		return true;
	}
	
}
