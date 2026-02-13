package newcrm.business.aubusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.auclientpages.account.AUForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class AUCPForgotPwd extends CPForgotPwd {

    public AUCPForgotPwd(WebDriver driver) {
        super(new AUForgotPwdPage(driver));
    }

}
