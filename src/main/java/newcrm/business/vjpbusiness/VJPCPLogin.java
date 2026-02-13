package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.vjpclientpages.VJPLoginPage;
import org.openqa.selenium.WebDriver;

public class VJPCPLogin extends CPLogin {
    public VJPCPLogin(WebDriver driver, String url) {
        super(new VJPLoginPage(driver, url));
    }
}
