package newcrm.business.pugbusiness;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.pages.pugclientpages.account.PULiveAccountsPage;

import java.util.List;

public class PUGCPLiveAccounts extends CPLiveAccounts {

	public PUGCPLiveAccounts(WebDriver driver) {
		super(new PULiveAccountsPage(driver));
	}

	public List<LiveAccountsPage.Account> getCopyTradingAccountWithBalance(GlobalProperties.PLATFORM platform){
		List<LiveAccountsPage.Account> accounts = page.getFirstPageMTSAccountsWithBalance(platform);
		return accounts;
	}

	@Override
	public String getProfileUserId() {
		String userId = page.getProfileUserId();

		return userId;
	}

}
