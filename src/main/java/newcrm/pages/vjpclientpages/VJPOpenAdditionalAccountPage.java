package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.clientpages.OpenAdditionalAccountPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VJPOpenAdditionalAccountPage extends OpenAdditionalAccountPage {

	public VJPOpenAdditionalAccountPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getOpenAccountConfirmationEle() {
		return checkElementExists(By.xpath("(//div[contains(@class,'account_opening_drawer') and not(contains(@style,'display'))]//p[normalize-space() = 'Open Account'])[2]"));
	}

	@Override
	protected WebElement getOpenAccountResponseContentEle() {
		return assertVisibleElementExists(By.cssSelector("div.account_opening_complete"), "Open Account Response Content");
	}

	@Override
	protected WebElement getOpenAccountResponseContentBtn() {
		return assertClickableElementExists(By.cssSelector("div.account_opening_complete_btns button"), "I Know button");
	}

	@Override
	protected String getOpenAccountResponseMessage() {
		return "additional live account is being set up";
	}

	@Override
	public void checkExistsOpenAccountConfirmationDialog() {
		LogUtils.info("OpenAdditionalAccountPage: No Open Account Confirmation Dialog found");
	}

	@Override
	public boolean checkExistsOpenAccountHintDialog() {
		WebElement e = getOpenAccountHintResponseContentEle();

		if (e != null) {
			this.waitButtonLoader();
			LogUtils.info("OpenAdditionalAccountPage: Open Account Hint Dialog found");

			triggerElementClickEvent_withoutMoveElement(getOpenAccountConfirmBtn());
			LogUtils.info("OpenAdditionalAccountPage: Click Confirm button");
			this.waitLoading();
			this.waitButtonLoader();
			return true;
		} else {
			LogUtils.info("OpenAdditionalAccountPage: No Open Account Hint Dialog found");
		}
		return false;
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

}
