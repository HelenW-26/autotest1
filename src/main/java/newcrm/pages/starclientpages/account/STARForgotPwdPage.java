package newcrm.pages.starclientpages.account;

import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class STARForgotPwdPage extends ForgotPwdPage {

    public STARForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getChgLoginPwdDialogEle() {
        return checkElementExists(By.xpath("//div[contains(@class,'el-dialog__wrapper') and not(contains(@style,'display: none'))]"));
    }

    @Override
    protected WebElement getNewPasswordEle() {
        return assertElementExists(By.xpath("//input[@data-testid='pass']"), "New Password");
    }

    @Override
    protected WebElement getConfirmNewPasswordEle() {
        return assertElementExists(By.xpath("//input[@data-testid='checkPass']"), "Confirm New Password");
    }

    @Override
    protected WebElement getCountryCodeSearchEle() {
        return assertElementExists(By.xpath("//input[@name='search']"), "Phone Country Code Search Box");
    }

    @Override
    protected WebElement getEmailSubmitBtnEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'login-card')]//button"), "Submit button");
    }

    @Override
    protected WebElement getPhoneNoEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='mobile']"), "Phone Number");
    }

    @Override
    protected WebElement getPhoneForgotPwdEle() {
        return assertElementExists(By.xpath("//div[@id='pane-Phone']//a"), "Phone Number Forgot Password");
    }

    @Override
    protected WebElement getPhoneOTPReqBtnEle() {
        return assertElementExists(By.xpath("(//div[contains(@class, 'login-card')]//button)[1]"), "Send SMS Code button");
    }

    @Override
    protected WebElement getBackToLoginBtnEle() {
        return checkElementExists(By.xpath("//p[@class='back-login']"), "Back To Login In button");
    }

    @Override
    protected WebElement getChgLoginPwdConfirmBtnEle() {
        return assertElementExists(By.cssSelector("div.el-dialog__wrapper:not([style*='display: none']) button.el-button"), "Confirm button", e -> e.getText().toLowerCase().contains("confirm"));
    }

    @Override
    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message");
    }

}
