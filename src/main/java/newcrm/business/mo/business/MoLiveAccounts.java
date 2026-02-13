package newcrm.business.mo.business;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.pages.moclientpages.MOLiveAccountsPage;
import org.openqa.selenium.WebDriver;

public class MoLiveAccounts extends CPLiveAccounts {
    public MoLiveAccounts(WebDriver driver)
    {
        super(new MOLiveAccountsPage(driver));
    }
}
