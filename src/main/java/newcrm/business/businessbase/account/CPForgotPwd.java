package newcrm.business.businessbase.account;

import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.WebDriver;

public class CPForgotPwd {

    protected ForgotPwdPage forgotPwdPage;

    public CPForgotPwd(WebDriver driver) {
        this.forgotPwdPage = new ForgotPwdPage(driver);
    }

    public CPForgotPwd(ForgotPwdPage forgotPwdPage) {
        this.forgotPwdPage = forgotPwdPage;
    }

    public void emailForgotPwd(String email, String url) {
        forgotPwdPage.waitLoading();
        forgotPwdPage.clickEmailForgotPwd();
        forgotPwdPage.checkRedirectToEmailForgotPwdPage(url);
        forgotPwdPage.setEmail(email);
        forgotPwdPage.submitEmail();
        forgotPwdPage.checkEmailForgotPwdSuccess();
    }

    public void emailResetPwd(String pwd) {
        forgotPwdPage.setNewPassword(pwd);
        forgotPwdPage.setConfirmNewPassword(pwd);
        forgotPwdPage.submitResetPwd();
        forgotPwdPage.checkResetPwdSuccess();
    }

    public String[] getPhoneNoFormat(String phone) {
        return forgotPwdPage.getPhoneNoFormat(phone);
    }

    public void requestPhoneOTP(String countryCode, String phoneNo, String url) {
        forgotPwdPage.waitLoading();
        forgotPwdPage.clickPhonePanel();
        forgotPwdPage.clickPhoneForgotPwd();
        forgotPwdPage.checkRedirectToPhoneForgotPwdPage(url);
        forgotPwdPage.setCountryCode(countryCode);
        forgotPwdPage.setPhoneNo(phoneNo);
        forgotPwdPage.clickPhoneOTPReqBtn();
    }

    public void phoneForgotPwd(String code) {
        forgotPwdPage.setPhoneOTP(code);
        forgotPwdPage.submitPhone();
        forgotPwdPage.checkPhoneForgotPwdSuccess();
    }

    public void phoneResetPwd(String pwd) {
        forgotPwdPage.setPhoneNewPassword(pwd);
        forgotPwdPage.setPhoneConfirmNewPassword(pwd);
        forgotPwdPage.submitResetPwd();
        forgotPwdPage.checkResetPwdSuccess();
    }

    public void goEmailResetPwdPage(String resetPwdLink, String url) {
        forgotPwdPage.goEmailResetPwdPage(resetPwdLink);
        forgotPwdPage.checkRedirectToEmailResetPwdPage(url);
    }

    public void clickBackToLogin(String url) {
        forgotPwdPage.clickBackToLogin(url);
    }

}
