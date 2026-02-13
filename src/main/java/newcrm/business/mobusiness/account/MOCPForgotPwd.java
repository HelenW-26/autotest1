package newcrm.business.mobusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.moclientpages.account.MOForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class MOCPForgotPwd extends CPForgotPwd {

    public MOCPForgotPwd(WebDriver driver) {
        super(new MOForgotPwdPage(driver));
    }

}
