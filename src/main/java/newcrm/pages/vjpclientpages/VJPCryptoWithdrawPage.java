package newcrm.pages.vjpclientpages;

import newcrm.pages.clientpages.withdraw.CryptoWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VJPCryptoWithdrawPage extends CryptoWithdrawPage {

	public VJPCryptoWithdrawPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void clickContinue() {
		this.findClickableElementByXpath("//button[@data-testid='submit']//span[contains(text(),'Continue')]").click();
	}

}