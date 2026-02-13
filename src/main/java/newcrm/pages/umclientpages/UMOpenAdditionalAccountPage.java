package newcrm.pages.umclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.OpenAdditionalAccountPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMOpenAdditionalAccountPage extends OpenAdditionalAccountPage {

	public UMOpenAdditionalAccountPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getOpenAccountConfirmationEle() {
		return checkElementExists(By.cssSelector("div.account_opening_complete_content"));
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
