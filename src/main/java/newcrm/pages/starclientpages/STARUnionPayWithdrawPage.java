package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.UnionPayWithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class STARUnionPayWithdrawPage extends UnionPayWithdrawPage {

	public STARUnionPayWithdrawPage(WebDriver driver) {
		super(driver);
	}

	public boolean submit() {
		waitLoading();
		WebElement submit = null;
		try {
			submit = this.findClickableElementByXpath("//span[contains(text(),'Withdraw Now')]");
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the submit button.");
			return false;
		}

		this.moveElementToVisible(submit);

		js.executeScript("arguments[0].click()",submit);
		GlobalMethods.printDebugInfo("Submit Payment");
		this.waitLoadingForCustomise(120);;
		WebElement response = this.findVisibleElemntByXpath("//*[contains(text(),'withdrawal request was successful') or contains(text(),'withdrawal request has been submitted successfully')]");
		String response_info = response.getText();
		if(response_info != null) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
			if(response_info.toLowerCase().contains("successful")) {
				driver.switchTo().defaultContent();
				goBack();
				this.waitLoading();
				return true;
			}
		}
		System.out.println("WithdrawBasePage: ERROR: withdraw requestion is failed");
		return false;
	}
	
}
