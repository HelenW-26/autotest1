package newcrm.pages.auclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.CURRENCY;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.OpenAdditionalAccountPage;
import utils.LogUtils;

public class AuOpenAdditionalAccountPage extends OpenAdditionalAccountPage {

	public AuOpenAdditionalAccountPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getSubmitBtn() {
		return assertClickableElementExists(By.xpath("//button[@data-testid='submit']"), "Submit button");
	}

	@Override
	protected WebElement getNoteEle() {
		return assertElementExists(By.xpath("//textarea[@data-testid='accountNote']"), "Note");
	}

	@Override
	public void tickBox() {
		WebElement e = getAgreementEle();
		String class_value = e.getAttribute("class");
		if(class_value.contains("active")) {
			LogUtils.info("OpenAdditionalAccountPage: Already ticked agreement");
			return;
		}
		e.click();
		LogUtils.info("OpenAdditionalAccountPage: Tick Agreement");
	}

	@Override
	public void setCurrency(CURRENCY currency) {
		triggerElementClickEvent(getCurrencyEle());
		getAllOpendElements();
		triggerElementClickEvent_withoutMoveElement(getCurrencyItemEle(currency));
		LogUtils.info("OpenAdditionalAccountPage: set Currency to: " + currency.getCurrencyDesc());
	}

}
