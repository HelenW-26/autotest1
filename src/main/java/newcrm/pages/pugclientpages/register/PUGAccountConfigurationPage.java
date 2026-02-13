package newcrm.pages.pugclientpages.register;

import java.util.ArrayList;
import java.util.List;

import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.AccountConfigurationPage;
import org.testng.Assert;
import utils.LogUtils;

public class PUGAccountConfigurationPage extends AccountConfigurationPage {

	public PUGAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='main']/h3").getText();
	}
	
	@Override
	protected List<WebElement> getAccountTypeEles(){
		return driver.findElements(By.xpath("(//div[@class='box-inner'])[2]//ul/li"));
	}

	@Override
	protected List<WebElement> getCurrencyEles(){
		return driver.findElements(By.xpath("(//div[@class='box-inner'])[3]//ul/li"));
	}

	protected WebElement getAccTypeEle() {
		return assertElementExists(By.xpath("//div[@prop='accountType']//input[@class='el-input__inner']"), "Account Type");
	}

	protected WebElement getAccTypeItemEle(ACCOUNTTYPE accType, int iAccTypeTier, String accTypeDesc) {
		return assertClickableElementExists(By.xpath("(//ul[contains(@class, 'el-cascader-menu__list')])[" + iAccTypeTier + "]//li[.//span[contains(text(),'" + accTypeDesc + "')]]"), accType.getLiveAccountName() + " Account Type");
	}

	@Override
	public void tickBox() {
		WebElement e = getAgreementEle();
		String class_value = e.getAttribute("class");
		if(class_value.contains("active")) {
			LogUtils.info("AccountConfigurationPage: Already ticked agreement");
			return;
		}
		e.click();
		LogUtils.info("AccountConfigurationPage: Tick Agreement");
	}

	@Override
	public void tickTickBoxforCopyTradind() {
		List<WebElement> divCount = driver.findElements(By.xpath("//div[@class='account_configuration_agreement']/div"));

		for(int i = 0;i < divCount.size(); i++)
		{
			try{
			WebElement agreementTick = driver.findElement(By.xpath("(//div[@data-testid='checkbox']//section[contains(@class, 'ht-protocol__checkbox')])["+ (i+1) +"]"));
			String class_value = agreementTick.getAttribute("class");
			if(class_value.contains("active")) {
				LogUtils.info("AccountConfigurationPage: Already ticked agreement");
				continue;
			}
			triggerClickEvent(agreementTick);
			}
			catch (Exception e) {
				LogUtils.info("not find agreement tick");
			}
		}

		LogUtils.info("AccountConfigurationPage: tick agreement");
	}

	@Override
	public String setAccountType(ACCOUNTTYPE accType) {

		WebElement accTypeEle = getAccTypeEle();
		triggerElementClickEvent_withoutMoveElement(accTypeEle);
		waitLoadingCascaderOpendElements();

		int accTypeTier = 1;
		List<String> accTypeDescTier = new ArrayList<>();

		switch (accType) {
			case STANDARD_STP, HEDGE_STP, MTS_HEDGE_STP:
				accTypeDescTier.add("Standard");
				break;
			case MT5_PAMM:
				accTypeDescTier.add("PAMM");
				break;
			case PRO_ECN:
				accTypeDescTier.add("ECN");
				break;
			case ISLAMIC_ECN:
				accTypeTier = 2;
				accTypeDescTier.add("Islamic");
				accTypeDescTier.add("Prime");
				break;
			case ISLAMIC_STP:
				accTypeTier = 2;
				accTypeDescTier.add("Islamic");
				accTypeDescTier.add("Standard");
				break;
		}

		if (accTypeDescTier.isEmpty()) {
			Assert.fail("Account Type not found in list");
		}

		for (int i = 1; i <= accTypeTier; i++) {
			WebElement accTypeItemEle = getAccTypeItemEle(accType, i, accTypeDescTier.get(i - 1));

			String classValue = accTypeItemEle.getAttribute("class");
			boolean isActive = classValue.contains("is-active");

			// When the account type is selected by default, when select again, it will not close the dropdown. Hence, need to click dropdown to close it.
			if (i == accTypeTier && isActive) {
				accTypeEle.click();
			} else {
				triggerElementClickEvent_withoutMoveElement(accTypeItemEle);
			}
		}

		LogUtils.info("AccountConfigurationPage: set Account Type to: " + accType.getLiveAccountName());
		return accType.getTestId();
	}

}
