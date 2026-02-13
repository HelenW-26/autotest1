package newcrm.pages.umclientpages.account;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMForgotPwdPage extends ForgotPwdPage {

    public UMForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getEmailForgotPwdEle() {
        return assertElementExists(By.xpath("//div[@id='pane-email']//a[@data-testid='forgetPassword']"), "Email Forgot Password");
    }

    @Override
    protected WebElement getPhoneForgotPwdEle() {
        return assertElementExists(By.xpath("//div[@id='pane-Phone']//a[@data-testid='forgetPassword']"), "Phone Number Forgot Password");
    }

    @Override
    protected WebElement getPhoneCountryCodeEle() {
        return assertVisibleElementExists(By.xpath("//div[@data-testid='countryCode']//input"), "Phone Country Code");
    }

    @Override
    protected WebElement getCountryCodeSearchEle() {
        return getPhoneCountryCodeEle();
    }

    @Override
    protected WebElement getCountryCodeItem(String strCountryCode) {
        return assertElementExists(By.xpath("//li[contains(@class, 'ht-select-input__option')]//span[contains(text(),'+" + strCountryCode + "')]"), strCountryCode + " Country Code");
    }

    @Override
    protected WebElement getBackToLoginBtnEle() {
        return checkElementExists(By.xpath("//div[@class='back-to-login']//span[@class='btn-text']"), "Back To Login In button");
    }

    @Override
    public void setCountryCodeSearchValue(String countryCode) {
        WebElement countryCodeSearch = getCountryCodeSearchEle();
        if (countryCodeSearch != null) {
            countryCodeSearch.sendKeys(countryCode);
            LogUtils.info("Search for Country Code: " + countryCode);
        }
    }

}
