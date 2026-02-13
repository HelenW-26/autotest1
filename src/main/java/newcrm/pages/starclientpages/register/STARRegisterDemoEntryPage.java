package newcrm.pages.starclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class STARRegisterDemoEntryPage extends RegisterDemoEntryPage {

    public STARRegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getDemoAccConfigResponseContent() {
        return assertVisibleElementExists(By.cssSelector("div.el-card__body"), "Demo Account Response Content");
    }

    @Override
    protected WebElement getFirstNameInput() {
        return assertVisibleElementExists(By.id("name"), "Name");
    }

    @Override
    protected WebElement getOpenLiveAccBtn() {
        return assertElementExists(By.cssSelector("[data-testid='openLiveAcc']"), "Open Live Account");
    }

    @Override
    protected WebElement getOpenAccountVerifyBtn() {
        return assertElementExists(By.cssSelector("div#v_reg > div.v_register_container button"), "Verification button");
    }

    @Override
    protected WebElement getDemoAccNoEle() {
        return assertVisibleElementExists(By.cssSelector("div.content-bottom ul > li:first-of-type > span:last-of-type"), "Demo Account Number");
    }

    @Override
    protected WebElement getDemoAccServerEle() {
        return assertVisibleElementExists(By.cssSelector("div.content-bottom ul > li:nth-of-type(2) > span:last-of-type"), "Demo Account Server");
    }

    @Override
    protected WebElement getDemoAccExpiryDateEle() {
        return assertVisibleElementExists(By.cssSelector("div.content-bottom ul > li:last-of-type > span:last-of-type"), "Demo Account Server");
    }

    @Override
    protected WebElement getAllowCookieEle() {
        return checkElementExists(By.cssSelector("div.cookie_agree"), "Agree Cookie button");
    }

    @Override
    public boolean setCountry(String country) {
        WebElement e = getCountryInput();
        e.click();
        getCountryListItemEle(country).click();
        LogUtils.info("RegisterEntryPage: set Country to: " + country);
        return true;
    }

}
