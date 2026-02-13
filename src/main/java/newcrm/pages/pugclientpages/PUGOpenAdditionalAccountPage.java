package newcrm.pages.pugclientpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.OpenAdditionalAccountPage;
import utils.LogUtils;

import java.util.List;
import java.util.ArrayList;

public class PUGOpenAdditionalAccountPage extends OpenAdditionalAccountPage {

	public PUGOpenAdditionalAccountPage(WebDriver driver) {
		super(driver);
	}

	protected WebElement getAccTypeItemEle(ACCOUNTTYPE accType, int iAccTypeTier, String accTypeDesc) {
		return assertClickableElementExists(By.xpath("(//ul[contains(@class, 'el-cascader-menu__list')])[" + iAccTypeTier + "]//li[.//span[contains(text(),'" + accTypeDesc + "')]]"), accType.getLiveAccountName() + " Account Type");
	}

	@Override
	protected WebElement getOpenAccountConfirmationEle() {
		return checkElementExists(By.xpath("//div[@class='account_opening_complete_content']"));
	}

	@Override
	protected WebElement getOpenAccountResponseContentEle() {
		return assertVisibleElementExists(By.cssSelector("div.account_opening_complete"), "Open Account Response Content");
	}

	@Override
	protected String getOpenAccountResponseMessage() {
		return "additional live account is being set up";
	}

	@Override
	protected WebElement getOpenAccountResponseContentBtn() {
		return assertClickableElementExists(By.cssSelector("div.account_opening_complete_btns button"), "I Know button");
	}

	@Override
	public void tickBox() {

		List<WebElement> divCount = assertElementsExists(By.xpath("//div[@class='account_configuration_agreement']/div"), "Agreement List");

		for(int i = 0;i < divCount.size(); i++)
		{
			WebElement agreementTick = assertClickableElementExists(By.xpath("(//div[@data-testid='checkbox']//section[contains(@class, 'ht-protocol__checkbox')])["+ (i+1) +"]"),"Agreement [" + (i+1) + "]");
			String class_value = agreementTick.getAttribute("class");
			if(class_value.contains("active")) {
				LogUtils.info("OpenAdditionalAccountPage: Already ticked agreement [" + (i+1) + "]");
				continue;
			}
			triggerClickEvent(agreementTick);
			LogUtils.info("OpenAdditionalAccountPage: Tick Agreement [" + (i+1) + "]");
		}
	}

	@Override
	public void setAccountType(GlobalProperties.ACCOUNTTYPE accType) {

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

		LogUtils.info("OpenAdditionalAccountPage: set Account Type to: " + accType.getLiveAccountName());
	}

	protected void waitLoadingCascaderOpendElements() {

		String xpath = "//div[contains(@class,'ht-cascader-popper') and not(contains(@style, 'display: none'))]//li";

		try {
			assertVisibleElementExists(By.xpath(xpath), "Account Type List");

		} catch (Exception ex) {
			Assert.fail("No data available or timeout waiting for data listing");
		}
	}

}
