package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.umclientpages.UMOpenAdditionalAccountPage;
import org.openqa.selenium.WebDriver;

public class UMCPOpenAdditionalAccount extends CPOpenAdditionalAccount {

    public UMCPOpenAdditionalAccount(WebDriver driver)
    {
        super(new UMOpenAdditionalAccountPage(driver));
    }

}
