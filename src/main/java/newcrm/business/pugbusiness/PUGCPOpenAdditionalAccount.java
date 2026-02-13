package newcrm.business.pugbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.pugclientpages.PUGOpenAdditionalAccountPage;

public class PUGCPOpenAdditionalAccount extends CPOpenAdditionalAccount {

	public PUGCPOpenAdditionalAccount(WebDriver driver) {
		super(new PUGOpenAdditionalAccountPage(driver));
	}

}
