package newcrm.business.vtbusiness.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.vtclientpages.account.VTLoginPage;
import org.openqa.selenium.WebDriver;

public class VTCPLogin extends CPLogin {

    public VTCPLogin(WebDriver driver, String url) {
        super(new VTLoginPage(driver,url));
    }

}
