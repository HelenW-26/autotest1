package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.pages.vjpclientpages.VJPLiveAccountsPage;
import org.openqa.selenium.WebDriver;

public class VJPCPLiveAccounts extends CPLiveAccounts {

    public VJPCPLiveAccounts(WebDriver driver) {
        super(new VJPLiveAccountsPage(driver));
    }

}
