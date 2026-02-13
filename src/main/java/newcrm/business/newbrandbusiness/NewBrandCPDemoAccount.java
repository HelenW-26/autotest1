package newcrm.business.newbrandbusiness;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.business.businessbase.CPDemoAccount;
import newcrm.pages.newbrandclientpages.NewBrandDemoAccountConfigurationPage;
import newcrm.pages.newbrandclientpages.register.NewBrandPersonalDetailsPage;
import newcrm.pages.vtclientpages.VTDemoAccountConfigurationPage;

public class NewBrandCPDemoAccount extends CPDemoAccount {
	
	protected NewBrandDemoAccountConfigurationPage nconfig_page;
	
	public NewBrandCPDemoAccount(WebDriver driver) {
		super(driver);
		this.nconfig_page = new NewBrandDemoAccountConfigurationPage(driver);
	}
	
	
	@Override
	public void setAnswers() {
		nconfig_page.setDemoAccountType();
		nconfig_page.setDemoCurrency();
		nconfig_page.setDemoLeverage();
		nconfig_page.setDemoBalance();
	}
	
	@Override
	public boolean submit() {
		nconfig_page.demo_submit();
		String response = nconfig_page.getReponseElement().getText();
		if(response.contains("additional demo account has been set up")) {
			nconfig_page.backToHome();
			return true;
		}else {
			return false;
		}
	}
}
