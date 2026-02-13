package newcrm.business.umbusiness.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.umclientpages.account.UMLoginPage;
import org.openqa.selenium.WebDriver;

public class UMCPLogin extends CPLogin {

    public UMCPLogin(WebDriver driver, String url) {
        super(new UMLoginPage(driver,url));
    }

}
