package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.vtclientpages.VTMenuPage;
import org.openqa.selenium.WebDriver;

public class VTCPMenu extends CPMenu {

    public VTCPMenu(WebDriver driver)
    {
        super(new VTMenuPage(driver));
    }
}
