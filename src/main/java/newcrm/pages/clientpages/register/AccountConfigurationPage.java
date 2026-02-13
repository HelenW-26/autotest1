package newcrm.pages.clientpages.register;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.Page;
import org.testng.Assert;
import utils.LogUtils;

public class AccountConfigurationPage extends Page {

	public AccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	protected List<WebElement> getPlatformEles(){
		return driver.findElements(By.xpath("(//ul[@class='accountConfiguration'])[1]//li"));
	}
	
	protected List<WebElement> getAccountTypeEles(){
		return driver.findElements(By.xpath("(//ul[@class='accountConfiguration'])[2]//li"));
	}
	
	protected List<WebElement> getCurrencyEles(){
		return driver.findElements(By.xpath("(//ul[@class='accountConfiguration'])[3]//li"));
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

	protected WebElement getAccTypeItemEle(ACCOUNTTYPE accType) {
		return assertElementExists(By.xpath("(//*[@data-testid='" + accType.getTestId() + "'])[last()]"), accType.getLiveAccountName() + " Account Type");
	}

	protected WebElement getCurrencyEle() {
		return assertElementExists(By.xpath("//div[@data-testid='currency']"), "Currency");
	}

	protected WebElement getCurrencyItemEle(CURRENCY currency) {
		return assertElementExists(By.xpath("(//*[@data-testid='" + currency.getCurrencyDesc() + "'])[last()]"), currency.getCurrencyDesc() + "Currency");
	}

	protected WebElement getAgreementEle() {
		return assertClickableElementExists(By.xpath("(//*[@data-testid='checkbox'])[last()]//section[contains(@class, 'ht-protocol__checkbox')]"), "Agreement tick box");
	}

	public boolean setPlatForm(PLATFORM platform) {
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

		LogUtils.info("AccountConfigurationPage: set Platform to: " + platform.getPlatformDesc());
		return true;
	}
	
	public String setAccountType() {
		List<WebElement> els = this.getAccountTypeEles();
		LogUtils.info("acccounttype size: " + els.size());
		if(els==null || els.size()< 1) {
			return null;
		}
		Random random = new Random();
		WebElement  e = els.get(random.nextInt(els.size()));
		this.moveElementToVisible(e);
		String result = e.getAttribute("data-testid");
		e.click();
		LogUtils.info("AccountType:" + result);
		LogUtils.info("AccountConfigurationPage: set account type to: " + result);
		return result;
	}
	
	/**
	 *  apply for a specific account type
	 * @param accType
	 * @return accountTypeName
	 */
	public String setAccountType(ACCOUNTTYPE accType) {
		triggerElementClickEvent_withoutMoveElement(getAccTypeItemEle(accType));
		LogUtils.info("AccountConfigurationPage: set Account Type to: " + accType.getLiveAccountName());
		return accType.getTestId();
	}
	
	public String setCurrency() {
		List<WebElement> els = this.getCurrencyEles();
		LogUtils.info("currency els size:" + els.size());
		String result = this.selectRandomValueFromDropDownList(els);
		LogUtils.info("AccountConfigurationPage: set currency to: " + result);
		return result;
	}

	public String setCurrency(CURRENCY currency) {
		triggerElementClickEvent_withoutMoveElement(getCurrencyItemEle(currency));
		LogUtils.info("AccountConfigurationPage: set Currency to: " + currency.getCurrencyDesc());
		return currency.getCurrencyDesc();
	}

	public String setBalance(String balance) {
		driver.findElement(By.xpath("//div[@data-testid='accountBalance']")).click();
		driver.findElement(By.xpath("(//ul[@class='el-scrollbar__view el-select-dropdown__list'])//li[@data-testid='select_"+Integer.parseInt(balance)+"']")).click();
		LogUtils.info("set balance to: " + balance);
		return balance;
	}

	public void tickBox() {
		WebElement e = getAgreementEle();
		String class_value = e.getAttribute("class");
		if(class_value.contains("is-checked")) {
			LogUtils.info("AccountConfigurationPage: Already ticked agreement");
			return;
		}
		e.click();
		LogUtils.info("AccountConfigurationPage: Tick Agreement");
	}

	public void tickTickBoxforCopyTradind() {

	}
	public void next() {
		this.waitLoading();
		WebElement e = assertClickableElementExists(By.xpath("//button[@data-testid='next']"), "Submit button");
		triggerElementClickEvent(e);
	}
	
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//*[self::h2 or self::h3]").getText();
	}

	public String getPTRefistrationFee()
	{return null; }

	protected void waitLoadingCascaderOpendElements() {

		String xpath = "//div[contains(@class,'ht-cascader-popper') and not(contains(@style, 'display: none'))]//li";

		try {
			assertVisibleElementExists(By.xpath(xpath), "Account Type List");

		} catch (Exception ex) {
			Assert.fail("No data available or timeout waiting for data listing");
		}
	}

}
