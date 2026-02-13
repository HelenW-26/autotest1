package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.auclientpages.AuOpenAdditionalAccountPage;

public class AuCPOpenAdditionalAccount extends CPOpenAdditionalAccount {

	public AuCPOpenAdditionalAccount(WebDriver driver) {
		super(new AuOpenAdditionalAccountPage(driver));
	}

}
