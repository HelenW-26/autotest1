package newcrm.business.mobusiness.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.moclientpages.account.MOProdLoginPage;
import org.openqa.selenium.WebDriver;

public class MOProdCPLogin extends CPLogin {

    public MOProdCPLogin(WebDriver driver, String url) {
        super(new MOProdLoginPage(driver, url));
    }

}
