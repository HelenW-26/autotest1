package newcrm.pages.starclientpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.SkrillWithdrawPage;

public class STARSkrillWithdrawPage extends SkrillWithdrawPage{

	public STARSkrillWithdrawPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void setNotes(String notes) {
		super.waitLoading();
		WebElement input_notes = this.findVisibleElemntByXpath("//div[@id='withdrawal-other-method']//input[@data-testid='importantNotes']");
		this.moveElementToVisible(input_notes);
		input_notes.sendKeys(notes);
	}
	
	@Override
	public boolean submit() {
		WebElement submit = null;
		try {
			submit = this.findClickableElementByXpath("//button[@data-testid='submit']//span[contains(text(),'Submit') or contains(text(),'SUBMIT')]");
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the submit button.");
			return false;
		}
		
		this.moveElementToVisible(submit);

		js.executeScript("arguments[0].click()",submit);
		GlobalMethods.printDebugInfo("Submit Payment");
		this.waitLoadingForCustomise(120);
		
		WebElement response = this.findVisibleElemntByXpath("//div[contains(text(),'withdrawal request was successful')]");		
		String response_info = response.getText();
		if(response_info != null) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
			if(response_info.toLowerCase().contains("successful")) {
				//withdraw request is succeed. return to home page
				this.findClickableElementByXpath("//*[@data-testid='bkToHm']").click();
				this.waitLoading();
				return true;
			}
		}
		
		System.out.println("WithdrawBasePage: ERROR: withdraw requestion is failed");
		return false;
	}
}
