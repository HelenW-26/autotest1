package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.vjpclientpages.VJPOpenAdditionalAccountPage;
import org.openqa.selenium.WebDriver;

public class VJPCPOpenAdditionalAccount extends CPOpenAdditionalAccount {

	public VJPCPOpenAdditionalAccount(WebDriver driver) {
		super(new VJPOpenAdditionalAccountPage(driver));
	}

}
