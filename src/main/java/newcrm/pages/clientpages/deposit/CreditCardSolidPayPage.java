package newcrm.pages.clientpages.deposit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;


public class CreditCardSolidPayPage extends CreditCardDepositPage {
	public CreditCardSolidPayPage(WebDriver driver) {
		super(driver);
	}

	public boolean checkIframeExists() {
		this.waitLoading();
		try {
			waitvisible.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//form[@data-action='submit-payment-card']//iframe")));
		}catch(Exception e) {
			System.out.println("!!! Coundn't find Solidpay iFrame!");
			return false;
		}
		GlobalMethods.printDebugInfo("Solidpay iFrame is visible.");
		return true;
	}
}
