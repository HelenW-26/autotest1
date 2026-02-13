package newcrm.business.mo.business;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.moclientpages.MOOpenAdditionalAccountPage;
import org.openqa.selenium.WebDriver;

public class MOCPOpenAdditionalAdccount extends CPOpenAdditionalAccount {

    public MOCPOpenAdditionalAdccount(WebDriver driver) {
        super(new MOOpenAdditionalAccountPage(driver));
    }

}
