package newcrm.business.vtbusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.vtclientpages.account.VTForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class VTCPForgotPwd extends CPForgotPwd {

    public VTCPForgotPwd(WebDriver driver) {
        super(new VTForgotPwdPage(driver));
    }

}
