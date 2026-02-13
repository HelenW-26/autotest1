package newcrm.business.vjpbusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.vjpclientpages.account.VJPForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class VJPCPForgotPwd extends CPForgotPwd {

    public VJPCPForgotPwd(WebDriver driver) {
        super(new VJPForgotPwdPage(driver));
    }

}
