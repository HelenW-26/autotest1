package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.SkrillWithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VJPSkrillWithdrawPage extends SkrillWithdrawPage{

	public VJPSkrillWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void clickContinue() {
		this.findClickableElementByXpath("//button[@data-testid='submit']//span[contains(text(),'Continue')]").click();
	}

	@Override
	public boolean submit() {
		waitLoading();
		WebElement submit = null;
		try {
			submit = this.findClickableElementByXpath("(//button[@data-testid='submit'])[2]");
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the submit button.");
			return false;
		}

		this.moveElementToVisible(submit);

		js.executeScript("arguments[0].click()",submit);
		GlobalMethods.printDebugInfo("Submit Payment");
		this.waitLoadingForCustomise(120);;
		//WebElement response = this.findVisibleElemntByXpath("//div[contains(text(),'withdrawal request')]");
		//Update the xpath element as AT brand unable to detect
		WebElement response = this.findVisibleElemntByXpath("//*[contains(text(),'withdrawal request was successful')]");
		String response_info = response.getText();
		if(response_info != null) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
			if(response_info.toLowerCase().contains("successful")) {
				//withdraw request is succeed. return to home page
				//this.findClickableElementByXpath("//*[@data-testid='bkToHm']").click();
				this.waitLoading();
				return true;
			}
		}

		System.out.println("WithdrawBasePage: ERROR: withdraw requestion is failed");
		return false;
	}
}
