package newcrm.pages.vjpclientpages.account;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VJPForgotPwdPage extends ForgotPwdPage {

    public VJPForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getPhoneCountryCodeEle() {
        return assertVisibleElementExists(By.xpath("//div[@data-testid='countryCode']//input"), "Phone Country Code");
    }

    @Override
    protected WebElement getCountryCodeItem(String strCountryCode) {
        return assertElementExists(By.xpath("//li[contains(@class, 'ht-select-input__option')]//span[contains(text(),'+" + strCountryCode + "')]"), strCountryCode + " Country Code");
    }

    @Override
    public void setCountryCodeSearchValue(String countryCode) {
        LogUtils.info("No Country Code search box");
    }

}
