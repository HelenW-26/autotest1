package newcrm.pages.vtclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.OpenAdditionalAccountPage;
import org.testng.Assert;
import utils.LogUtils;

public class VTOpenAdditionalAccountPage extends OpenAdditionalAccountPage {

	public VTOpenAdditionalAccountPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getNoteEle() {
		return assertElementExists(By.xpath("//textarea[@autocomplete= 'new-password']"), "Note");
	}
	@Override
	protected WebElement getAgreementEle() {
		return assertClickableElementExists(By.xpath("//span[@class='el-checkbox__input']"), "Agreement tick box");
	}

	@Override
	protected WebElement getPlatformItemEle(PLATFORM platform) {
		//return assertClickableElementExists(By.xpath("//label[@data-testid='" + platform.getPlatformDesc().toLowerCase() + "']"), platform.getPlatformDesc() + " Trading Platform ");
		return assertClickableElementExists(
				By.xpath("//label[translate(@data-testid,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
						+ platform.getPlatformDesc().toLowerCase() + "']"),
				platform.getPlatformDesc() + " Trading Platform ");
	}

	@Override
	protected WebElement checkPlatformItemEle(PLATFORM platform) {
		//return assertClickableElementExists(By.xpath("//label[@data-testid='" + platform.getPlatformDesc().toLowerCase() + "']"), platform.getPlatformDesc() + " Trading Platform ");
		return checkElementExists(
				By.xpath("//label[translate(@data-testid,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
						+ platform.getPlatformDesc().toLowerCase() + "']"),
				platform.getPlatformDesc() + " Trading Platform ");
	}

	@Override
	protected WebElement getPlatformContinueBtn(PLATFORM platform) {
		String path = "(//*[contains(@class,'selectMt4TipDialog')])[last()]//button/span";
		assertClickableElementExists(By.xpath(path), platform.getPlatformDesc() + " Continue button");

		return assertElementExists(
				By.xpath(path), platform.getPlatformDesc() + " Continue button",
				e -> {
					String txt = e.getText().trim().toLowerCase();
					return txt.contains(platform.getPlatformDesc().toLowerCase());}
		);
	}

	@Override
	protected WebElement getSubmitBtn() {
		return assertClickableElementExists(By.xpath("//button[@data-testid='next']"), "Submit button");
	}

	@Override
	protected WebElement getOpenAccountResponseContentEle() {
		return assertVisibleElementExists(By.xpath("(//div[@id='openAccount'][.//div[contains(text(),'Open additional accounts')]]//p)[1]"), "Open Account Response Content");
	}

	@Override
	protected String getOpenAccountResponseMessage() {
		return "additional live account is being set up";
	}

	@Override
	protected WebElement getOpenAccountResponseContentBtn() {
		return assertClickableElementExists(By.cssSelector("div#openAccount button"), "Back To Home Page button");
	}

	@Override
	public void checkExistsOpenAccountConfirmationDialog() {
		LogUtils.info("OpenAdditionalAccountPage: No Open Account Confirmation Dialog found");
	}

	@Override
	public void checkPlatForm(PLATFORM platform, boolean bIsCheckVisible) {
		// Check exists platform
		WebElement platformItemEle = checkPlatformItemEle(platform);

		if (bIsCheckVisible && platformItemEle == null) {
			Assert.fail(platform.getPlatformDesc() + " Trading Platform option is not available for selection");
		}

		if (!bIsCheckVisible && platformItemEle != null) {
			Assert.fail(platform.getPlatformDesc() + " Trading Platform option should not be available for selection");
		}
	}

	@Override
	public void setPlatForm(PLATFORM platform) {
		triggerElementClickEvent_withoutMoveElement(getPlatformItemEle(platform));
		LogUtils.info("Click " + platform.getPlatformDesc() + " Trading Platform");

		if (platform == PLATFORM.MT4) {
			WebElement e = getPlatformContinueBtn(platform);
			if (e != null) {
				triggerElementClickEvent_withoutMoveElement(e);
				LogUtils.info("Click Continue button");
			}
		}

		LogUtils.info("OpenAdditionalAccountPage: set Platform to " + platform.getPlatformDesc());
	}

	@Override
	public void waitLoadingAccConfigContent() {
		assertVisibleElementExists(By.cssSelector("#openAccount div.content_box div.inner"),"Open Account Content");
	}

}
