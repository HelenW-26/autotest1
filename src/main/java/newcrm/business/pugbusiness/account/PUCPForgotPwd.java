package newcrm.business.pugbusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.pugclientpages.account.PUForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class PUCPForgotPwd extends CPForgotPwd {

    public PUCPForgotPwd(WebDriver driver) {
        super(new PUForgotPwdPage(driver));
    }

}
