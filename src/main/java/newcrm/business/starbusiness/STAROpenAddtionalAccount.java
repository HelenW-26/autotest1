package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.starclientpages.STAROpenAddtionalAccountPage;
import org.openqa.selenium.WebDriver;

public class STAROpenAddtionalAccount extends CPOpenAdditionalAccount {

    public STAROpenAddtionalAccount(WebDriver driver)
    {
        super(new STAROpenAddtionalAccountPage(driver));
    }

}
