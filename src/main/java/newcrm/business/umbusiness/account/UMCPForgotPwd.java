package newcrm.business.umbusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.umclientpages.account.UMForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class UMCPForgotPwd extends CPForgotPwd {

    public UMCPForgotPwd(WebDriver driver) {
        super(new UMForgotPwdPage(driver));
    }

}
