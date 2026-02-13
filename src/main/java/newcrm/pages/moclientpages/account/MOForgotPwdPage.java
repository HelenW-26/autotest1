package newcrm.pages.moclientpages.account;

import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MOForgotPwdPage extends ForgotPwdPage {

    public MOForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getEmailForgotPwdEle() {
        return assertElementExists(By.xpath("//div[@id='pane-email']//a[@data-testid='forgetPassword']"), "Email Forgot Password");
    }

    @Override
    protected WebElement getPhoneCountryCodeEle() {
        return assertVisibleElementExists(By.xpath("//span[@class='el-popover__reference-wrapper']//input"), "Phone Country Code");
    }

    @Override
    protected WebElement getPhoneForgotPwdEle() {
        return assertElementExists(By.xpath("//div[@id='pane-Phone']//a[@data-testid='forgetPassword']"), "Phone Number Forgot Password");
    }

    @Override
    protected WebElement getPhoneNoEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='mobile_login']"), "Phone Number");
    }

    @Override
    protected WebElement getBackToLoginBtnEle() {
        return checkElementExists(By.xpath("//div[@class='back-login']"), "Back To Login In button");
    }

}
