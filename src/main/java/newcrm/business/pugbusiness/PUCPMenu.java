package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.pugclientpages.PUMenuPage;
import org.openqa.selenium.WebDriver;

public class PUCPMenu extends CPMenu {

    public PUCPMenu(WebDriver driver) {
        super(new PUMenuPage(driver));
    }
}
