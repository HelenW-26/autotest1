package newcrm.business.mobusiness.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.moclientpages.account.MOLoginPage;
import org.openqa.selenium.WebDriver;

public class MOCPLogin extends CPLogin {

    public MOCPLogin(WebDriver driver, String url) {
        super(new MOLoginPage(driver,url));
    }

}
