package newcrm.pages.vtclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VTRegisterDemoEntryPage extends RegisterDemoEntryPage {

    public VTRegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getSubmitButton() {
        return assertClickableElementExists(By.id("button"), "Submit button");
    }

    @Override
    protected WebElement getPlatformEle(PLATFORM platform) {
        return this.assertElementExists(By.cssSelector("span[platform='" + platform.getServerCategory() + "']"), "Demo Account Trading Platform");
    }

    @Override
    protected WebElement getAccountTypeEle(PLATFORM platform, ACCOUNTTYPE accountType) {
        return this.assertElementExists(By.cssSelector("span[account" + platform.getServerCategory() + "='" + accountType.getAccountTypeCode() + "']"), "Demo Account Type");
    }

    @Override
    protected WebElement getCurrencyEle(CURRENCY currency) {
        return this.assertElementExists(By.cssSelector("span[data='" + currency.getCurrencyDesc() + "']"), "Demo Account Currency");
    }

    @Override
    protected WebElement getLeverageEle(String leverage) {
        return this.assertElementExists(By.cssSelector("span[leverage='" + leverage + "']"), "Demo Account Leverage");
    }

    @Override
    protected WebElement getAccountBalanceEle(String balance) {
        return this.assertElementExists(By.cssSelector("span[balance='" + balance + "']"), "Demo Account Balance");
    }

    @Override
    protected WebElement getDemoAccNoEle() {
        return assertVisibleElementExists(By.cssSelector("div#demo table.infoTable > tr:first-of-type > td:last-of-type"), "Demo Account Number");
    }

    @Override
    protected WebElement getDemoAccServerEle() {
        return assertVisibleElementExists(By.cssSelector("div#demo table.infoTable tr:last-of-type td:last-of-type"), "Demo Account Server");
    }

    @Override
    protected WebElement getOpenAccountVerifyBtn() {
        return assertElementExists(
                By.cssSelector("button:not([disabled])"),
                "Open Account Verify Now button",
                e -> e.isDisplayed() &&
                        e.findElements(By.cssSelector("span"))
                                .stream()
                                .anyMatch(t -> t.getText().toLowerCase().contains("verify now"))
        );
    }

    @Override
    public boolean setCountry(String country) {
        WebElement e = getCountryInput();
        e.click();
        setInputValue_withoutMoveElement(e, country);
        getCountryListItemEle(country).click();
        LogUtils.info("RegisterEntryPage: set Country to: " + country);
        return true;
    }

    @Override
    public void setPlatForm(PLATFORM platform) {
        clickElement(getPlatformEle(platform));
        LogUtils.info("RegisterEntryPage: set Demo Account Trading Platform to: " + platform.getPlatformDesc());
    }

    @Override
    public void setAccountType(PLATFORM platform, ACCOUNTTYPE accountType) {
        clickElement(getAccountTypeEle(platform, accountType));
        LogUtils.info("RegisterEntryPage: set Demo Account Type to: " + accountType.getLiveAccountName());
    }

    @Override
    public void setCurrency(CURRENCY currency) {
        clickElement(getCurrencyEle(currency));
        LogUtils.info("RegisterEntryPage: set Demo Account Currency to: " + currency.getCurrencyDesc());
    }

    @Override
    public void setLeverage(String leverage) {
        clickElement(getLeverageEle(leverage));
        LogUtils.info("RegisterEntryPage: set Demo Account Leverage to: " + leverage);
    }

    @Override
    public void setAccountBalance(String balance) {
        clickElement(getAccountBalanceEle(balance));
        LogUtils.info("RegisterEntryPage: set Demo Account Balance to: " + balance);
    }

}
