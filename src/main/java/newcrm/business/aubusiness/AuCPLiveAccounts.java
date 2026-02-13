package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.pages.auclientpages.AuLiveAccountsPage;

public class AuCPLiveAccounts extends CPLiveAccounts {

	public AuCPLiveAccounts(WebDriver driver) {
		super(new AuLiveAccountsPage(driver));
	}
}
