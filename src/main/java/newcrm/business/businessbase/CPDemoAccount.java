package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.clientpages.DemoAccountConfigurationPage;
import newcrm.pages.clientpages.DemoAccountsPage;

public class CPDemoAccount {

	protected DemoAccountsPage da_page;
	protected DemoAccountConfigurationPage config_page;
	
	public CPDemoAccount(WebDriver driver) {
		da_page = new DemoAccountsPage(driver);
		config_page = new DemoAccountConfigurationPage(driver);
	}
	
	public void openMT4DemoAccount() {
		da_page.openDemoAccount(PLATFORM.MT4);
	}
	
	public void openMT5DemoAccount() {
		da_page.openDemoAccount(PLATFORM.MT5);
	}
	
	public String getLatestMT4Account() {
		return da_page.getLatestAccount(PLATFORM.MT4);
	}
	
	public String getLatestMT5Account() {
		return da_page.getLatestAccount(PLATFORM.MT5);
	}
	
	public String getConfigTitle() {
		return config_page.getPageTitle();
	}
	
	public void setAnswers() {
		config_page.setValues();
	}
	
	//Submit demo and verify return msg
	public boolean submit() {
		config_page.demo_submit();
		String response = config_page.getReponseElement().getText();
		if(response.contains("additional demo account has been set up")) {
			config_page.backToHome();
			return true;
		}else {
			return false;
		}
	}
}
