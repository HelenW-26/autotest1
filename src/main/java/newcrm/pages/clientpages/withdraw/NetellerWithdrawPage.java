package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NetellerWithdrawPage extends SkrillWithdrawPage {
	
	public NetellerWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void setEmail(String email) {
		WebElement input = this.findVisibleElemntByXpath("//input[@data-testid='netellerEmail']");
		this.moveElementToVisible(input);
		input.sendKeys(email);
	}
	
}
