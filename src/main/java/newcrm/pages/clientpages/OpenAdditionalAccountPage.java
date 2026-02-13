package newcrm.pages.clientpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.Page;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class OpenAdditionalAccountPage extends Page {

	public OpenAdditionalAccountPage(WebDriver driver) {
		super(driver);
	}

	protected WebElement getNoteEle() {
		return assertElementExists(By.xpath("(//*[contains(@class,'account_configuration_item')]//*[contains(@class,'ht-input')]//input)[last()]"), "Note");
	}

	protected WebElement getAgreementEle() {
		return assertClickableElementExists(By.xpath("(//*[@data-testid='byTicking'])[last()]//section[contains(@class, 'ht-protocol__checkbox')]"), "Agreement tick box");
	}

	protected WebElement getPlatformEle() {
		return assertClickableElementExists(By.xpath("(//*[contains(@class,'account_configuration_platform')])[last()]//input"), "Trading Platform");
	}

	protected WebElement getPlatformItemEle(PLATFORM platform) {
		String path = "div.ht-select-dropdown:not([style*='display: none']) ul > li > span";
		assertClickableElementExists(By.cssSelector(path), platform.getPlatformDesc() + " Trading Platform");

		return assertElementExists(
				By.cssSelector(path), platform.getPlatformDesc() + " Trading Platform",
				e -> {
					String txt = e.getText().trim().toLowerCase();
					return platform == PLATFORM.MTS ? txt.contains("copy trading") : txt.contains("metatrade") && txt.contains(platform.getServerCategory());}
		);
	}

	protected WebElement checkPlatformItemEle(PLATFORM platform) {
		String path = "div.ht-select-dropdown:not([style*='display: none']) ul > li > span";
		assertClickableElementExists(By.cssSelector(path), platform.getPlatformDesc() + " Trading Platform");

		return checkElementExists(
				By.cssSelector(path), platform.getPlatformDesc() + " Trading Platform",
				e -> {
					String txt = e.getText().trim().toLowerCase();
					return platform == PLATFORM.MTS ? txt.contains("copy trading") : txt.contains("metatrade") && txt.contains(platform.getServerCategory());}
		);
	}

	protected WebElement getPlatformContinueBtn(PLATFORM platform) {
		String path = "(//*[contains(@class,'selectMt4TipDialog')])[last()]//button/span";
		assertClickableElementExists(By.xpath(path), platform.getPlatformDesc() + " Continue button");

		return assertElementExists(
				By.xpath(path), platform.getPlatformDesc() + " Continue button",
				e -> {
					String txt = e.getText().trim().toLowerCase();
					return txt.contains("metatrade") && txt.contains(platform.getServerCategory());}
		);
	}

	protected WebElement getAccTypeEle() {
		return assertElementExists(By.xpath("//div[@prop='accountType']//input[@class='el-input__inner']"), "Account Type");
	}

	protected WebElement getAccTypeItemEle(ACCOUNTTYPE accType) {
		return assertElementExists(By.xpath("(//*[@data-testid='" + accType.getTestId() + "'])[last()]"), accType.getLiveAccountName() + " Account Type");
	}

	protected List<WebElement> getAccTypeEles() {
		return assertElementsExists(By.cssSelector("ul.account_configuration_types li"),"Account Type");
	}

	protected List<WebElement> getCurrencyEles() {
		return assertElementsExists(By.cssSelector("li:has(> div.account_configuration_currencies_item)"),"Currency");
	}

	protected WebElement getCurrencyEle() {
		return assertElementExists(By.xpath("//div[@data-testid='currency']"), "Currency");
	}

	protected WebElement getCurrencyItemEle(CURRENCY currency) {
		return assertElementExists(By.xpath("(//*[@data-testid='" + currency.getCurrencyDesc() + "'])[last()]"), currency.getCurrencyDesc() + "Currency");
	}

	protected WebElement getSubmitBtn() {
		return assertClickableElementExists(By.xpath("(//button[@data-testid='next'])[last()]"), "Submit button");
	}

	protected WebElement getOpenAccountConfirmationEle() {
		return checkElementExists(By.xpath("//div[contains(@class,'kyc_drawer') and not(contains(@style,'display'))]//div[@aria-label='Open Account']"));
	}

	public WebElement getOpenAccountConfirmBtn() {
		return assertElementExists(
				By.cssSelector("div.account_opening_complete_btns button"),
				"Open Account Confirm button",
				e -> e.findElements(By.cssSelector("span"))
						.stream()
						.anyMatch(t -> t.getText().toLowerCase().contains("confirm")));
	}

	protected WebElement getOpenAccountResponseContentEle() {
		return assertVisibleElementExists(By.cssSelector("p.account_opening_complete_content"), "Open Account Response Content");
	}

	protected String getOpenAccountResponseMessage() {
		return "successfully submitted";
	}

	protected WebElement getOpenAccountResponseContentBtn() {
		return assertClickableElementExists(By.cssSelector("div.account_opening_complete_btns button"), "View My Accounts button");
	}

	protected WebElement getOpenAccountHintResponseContentEle() {
		return checkElementExists(By.xpath("//div[@class='account_opening_complete_content' and contains(text(), 'pending review record')]"));
	}

	public void setNote(String note) {
		WebElement e =  getNoteEle();
		setInputValue(e, note);
		LogUtils.info("OpenAdditionalAccountPage: set Note to: " + note);
	}
	
	public void tickBox() {
		WebElement e = getAgreementEle();
		String class_value = e.getAttribute("class");
		if(class_value.contains("is-checked")) {
			LogUtils.info("OpenAdditionalAccountPage: Already ticked agreement");
			return;
		}
		e.click();
		LogUtils.info("OpenAdditionalAccountPage: Tick Agreement");
	}

	public boolean submit() {
		triggerElementClickEvent_withoutMoveElement(getSubmitBtn());
		LogUtils.info("Click Submit button");
		checkExistsOpenAccountConfirmationDialog();
		boolean isShowHintPopup = checkExistsOpenAccountHintDialog();
		this.moveContainerToTop();
		if (isShowHintPopup) return true;
		return checkOpenAccountResponse();
	}

	public void checkExistsOpenAccountConfirmationDialog() {
		// Check for Open Account Confirmation Content
		WebElement eleOpenAccount = getOpenAccountConfirmationEle();

		if (eleOpenAccount != null) {
			this.waitButtonLoader();
			LogUtils.info("OpenAdditionalAccountPage: Open Account Confirmation Dialog found");

			triggerElementClickEvent_withoutMoveElement(getOpenAccountConfirmBtn());
			LogUtils.info("OpenAdditionalAccountPage: Click Confirm button");
			this.waitLoading();
			this.waitButtonLoader();
		} else {
			LogUtils.info("OpenAdditionalAccountPage: No Open Account Confirmation Dialog found");
		}
	}

	public boolean checkExistsOpenAccountHintDialog() { return false; }

	public boolean checkOpenAccountResponse() {
		String respContent = getOpenAccountResponseContentEle().getText();
		ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "additionalAccount");
		LogUtils.info("OpenAdditionalAccountPage: Response info: " + respContent.trim());

		String respMsg = getOpenAccountResponseMessage();
		if(respContent.toLowerCase().contains(respMsg.toLowerCase())) {
			triggerElementClickEvent_withoutMoveElement(getOpenAccountResponseContentBtn());
			LogUtils.info("Click button in response content");
			this.waitLoader();
			return true;
		}

		LogUtils.info("OpenAdditionalAccountPage: Failed to submit additional account application");
		return false;
	}

	public void checkPlatForm(PLATFORM platform, boolean bIsCheckVisible) {
		// Click platform dropdown
		triggerElementClickEvent_withoutMoveElement(getPlatformEle());
		LogUtils.info("Open Trading Platform List");
		getAllOpendElements();

		// Check exists platform
		WebElement platformItemEle = checkPlatformItemEle(platform);

		if (bIsCheckVisible && platformItemEle == null) {
			Assert.fail(platform.getPlatformDesc() + " Trading Platform option is not available for selection");
		}

		if (!bIsCheckVisible && platformItemEle != null) {
			Assert.fail(platform.getPlatformDesc() + " Trading Platform option should not be available for selection");
		}

		triggerElementClickEvent_withoutMoveElement(getPlatformEle());
		LogUtils.info("Close Trading Platform List");
	}

	public void setPlatForm(PLATFORM platform) {
		// Click platform dropdown
		triggerElementClickEvent_withoutMoveElement(getPlatformEle());
		LogUtils.info("Open Trading Platform List");
		getAllOpendElements();

		// Select specific platform
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

	public void setAccountType(ACCOUNTTYPE accType) {
		triggerElementClickEvent_withoutMoveElement(getAccTypeItemEle(accType));
		LogUtils.info("OpenAdditionalAccountPage: set Account Type to: " + accType.getLiveAccountName());

		// Some account type will show tooltip when clicked. To handle next element is not being blocked by this tooltip.
		WebElement outside = driver.findElement(By.tagName("body"));
		outside.click();
	}

	public List<ACCOUNTTYPE> getAccountTypeList(PLATFORM platform) {
		List<ACCOUNTTYPE> accTypeList = new ArrayList<>();
		List<WebElement> accTypeEles = getAccTypeEles();

		for(WebElement element:accTypeEles) {
			String accTypeDesc = element.getAttribute("innerText");
			String testId = element.getAttribute("data-testid");
			ACCOUNTTYPE accType = ACCOUNTTYPE.getRecByTestId(testId, platform);

			if (accType == null) {
				Assert.fail(String.format("%s Account Type Test ID: %s not found in matching list", accTypeDesc, testId));
			}

			accTypeList.add(accType);
		}

		return accTypeList;
	}

	public List<CURRENCY> getCurrencyList() {
		List<CURRENCY> currencyList = new ArrayList<>();
		List<WebElement> currencyEles = getCurrencyEles();

		for(WebElement element:currencyEles) {
			String currencyDesc = element.getAttribute("innerText");
			String testId = element.getAttribute("data-testid");

			CURRENCY currency = CURRENCY.getRecByCurrencyCode(testId);

			if (currency == null) {
				Assert.fail(String.format("%s Currency Test ID: %s not found in matching list", currencyDesc, testId));
			}

			currencyList.add(currency);
		}

		return currencyList;
	}

	public void setCurrency(CURRENCY currency) {
		triggerElementClickEvent_withoutMoveElement(getCurrencyItemEle(currency));
		LogUtils.info("OpenAdditionalAccountPage: set Currency to: " + currency.getCurrencyDesc());
	}

	public void waitLoadingAccConfigContent() {
		assertVisibleElementExists(By.xpath("(//div[contains(@class,'account_opening_container') and not(contains(@style,'display'))])[last()]"),"Open Account Content");
	}

}
