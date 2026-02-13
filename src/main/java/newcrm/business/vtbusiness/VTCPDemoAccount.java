package newcrm.business.vtbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDemoAccount;
import newcrm.pages.vtclientpages.VTDemoAccountConfigurationPage;

public class VTCPDemoAccount extends CPDemoAccount {

	public VTCPDemoAccount(WebDriver driver) {
		super(driver);
		this.config_page = new VTDemoAccountConfigurationPage(driver);
	}
}
