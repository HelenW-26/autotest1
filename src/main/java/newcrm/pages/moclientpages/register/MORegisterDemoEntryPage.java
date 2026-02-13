package newcrm.pages.moclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MORegisterDemoEntryPage extends RegisterDemoEntryPage {

    public MORegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getPlatformEle(GlobalProperties.PLATFORM platform) {
        return this.assertElementExists(By.cssSelector("span[platform='" + platform.getServerCategory() + "']"), "Demo Account Trading Platform");
    }

    @Override
    protected WebElement getAccountTypeEle(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType) {
        return this.assertElementExists(By.cssSelector("span[account" + platform.getServerCategory() + "='" + accountType.getAccountTypeCode() + "']"), "Demo Account Type");
    }

    @Override
    protected WebElement getCurrencyEle(GlobalProperties.CURRENCY currency) {
        return this.assertElementExists(By.cssSelector("div[data='" + currency.getCurrencyDesc() + "']"), "Demo Account Currency");
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
    protected WebElement getOpenAccountVerifyBtn() {
        return assertElementExists(By.xpath("//button[@data-testid='button' and contains(@class, 'register_guide_btn')]"), "ID Verification button", e -> e.getText().toLowerCase().contains("id verification"));
    }

    @Override
    protected WebElement getAllowCookieEle() {
        return checkElementExists(By.cssSelector("div.public-hint > a"), "Allow All Cookie button");
    }

    @Override
    public boolean setCountry(String country) {
        WebElement e = getCountryInput();
        e.click();
        getCountryListItemEle(country).click();
        LogUtils.info("RegisterEntryPage: set Country to: " + country);
        return true;
    }

    @Override
    public void setPlatForm(GlobalProperties.PLATFORM platform) {
        clickElement(getPlatformEle(platform));
        LogUtils.info("RegisterEntryPage: set Demo Account Trading Platform to: " + platform.getPlatformDesc());
    }

    @Override
    public void setAccountType(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType) {
        clickElement(getAccountTypeEle(platform, accountType));
        LogUtils.info("RegisterEntryPage: set Demo Account Type to: " + accountType.getLiveAccountName());
    }

    @Override
    public void setCurrency(GlobalProperties.CURRENCY currency) {
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
