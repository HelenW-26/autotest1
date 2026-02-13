package newcrm.business.starbusiness.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.starclientpages.account.STARLoginPage;
import org.openqa.selenium.WebDriver;

public class STARCPLogin extends CPLogin {

    public STARCPLogin(WebDriver driver, String url) {
        super(new STARLoginPage(driver,url));
    }

}
