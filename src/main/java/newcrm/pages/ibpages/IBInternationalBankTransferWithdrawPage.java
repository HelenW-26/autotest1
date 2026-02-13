package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;

public class IBInternationalBankTransferWithdrawPage extends InternalBankWithdrawPage {
	
	protected InternalBankWithdrawPage page;
	public IBInternationalBankTransferWithdrawPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean submit() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		WebElement confirmbutton = assertClickableElementExists(By.xpath("(//*[@data-testid='submit'])[last()]"), "Submit button");
		js.executeScript("arguments[0].click()",confirmbutton);
		this.waitLoading();
		GlobalMethods.printDebugInfo("Submit IB withdrawal");
		this.moveContainerToTop();
		return false;
	}

}
