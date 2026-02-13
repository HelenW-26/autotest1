package newcrm.business.aubusiness;

import newcrm.pages.auclientpages.AUPRODLoginPage;
import newcrm.business.businessbase.CPLogin;
import org.openqa.selenium.WebDriver;

public class AUPRODCPLogin extends CPLogin{
    public AUPRODCPLogin(WebDriver driver, String url) {
        super(new AUPRODLoginPage(driver, url));
    }
}
