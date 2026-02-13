package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.umclientpages.UMMenuPage;
import org.openqa.selenium.WebDriver;

public class UMCPMenu extends CPMenu {
    public UMCPMenu(WebDriver driver)
    {
        super(new UMMenuPage(driver));

    }
}
