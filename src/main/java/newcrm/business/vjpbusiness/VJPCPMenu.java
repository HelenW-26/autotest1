package newcrm.business.vjpbusiness;

import newcrm.business.aubusiness.AuCPMenu;
import newcrm.business.businessbase.CPMenu;
import newcrm.pages.vjpclientpages.VJPMenuPage;
import org.openqa.selenium.WebDriver;

public class VJPCPMenu extends CPMenu {
    public VJPCPMenu(WebDriver driver)
    {
        super(new VJPMenuPage(driver));
    }



}
