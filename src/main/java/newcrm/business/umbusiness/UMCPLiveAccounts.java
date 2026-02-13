package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.pages.umclientpages.UMLiveAccountsPage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class UMCPLiveAccounts extends CPLiveAccounts {

	public UMCPLiveAccounts(WebDriver driver) {
		super(new UMLiveAccountsPage(driver));
	}
	public List<LiveAccountsPage.Account> getCopyTradingAccountWithBalance(GlobalProperties.PLATFORM platform){
		List<LiveAccountsPage.Account> accounts = page.getFirstPageMTSAccountsWithBalance(platform);

		return accounts;
	}
}
