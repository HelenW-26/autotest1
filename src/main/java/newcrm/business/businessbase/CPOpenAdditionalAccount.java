package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;

import newcrm.pages.clientpages.OpenAdditionalAccountPage;

import java.util.List;

public class CPOpenAdditionalAccount {

	protected OpenAdditionalAccountPage page;

	public CPOpenAdditionalAccount(OpenAdditionalAccountPage v_page) {
		page = v_page;
	}
	
	public CPOpenAdditionalAccount(WebDriver driver) {
		page = new OpenAdditionalAccountPage(driver);
	}

	public boolean submit() {
		page.tickBox();
		return page.submit();
	}

	public void checkPlatForm(PLATFORM platform, boolean bIsCheckVisible) {
		page.checkPlatForm(platform, bIsCheckVisible);
	}

	public void setPlatForm(PLATFORM platform) {
		page.setPlatForm(platform);
	}

	public void setAccountType(ACCOUNTTYPE type) {
		page.setAccountType(type);
	}

	public List<ACCOUNTTYPE> getAccountTypeList(PLATFORM platform) {
		return page.getAccountTypeList(platform);
	}

	public List<CURRENCY> getCurrencyList() {
		return page.getCurrencyList();
	}

	public void setCurrency(CURRENCY currency) {
		page.setCurrency(currency);
	}

	public void setNote(String note) {
		page.setNote(note);
	}

	public void waitLoadingAccConfigContent() {
		page.waitLoading();
		page.waitLoader();
		page.waitLoadingAccConfigContent();
	}

}
