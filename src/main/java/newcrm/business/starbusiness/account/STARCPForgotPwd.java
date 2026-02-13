package newcrm.business.starbusiness.account;

import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.pages.starclientpages.account.STARForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class STARCPForgotPwd extends CPForgotPwd {

    public STARCPForgotPwd(WebDriver driver) {
        super(new STARForgotPwdPage(driver));
    }

}
