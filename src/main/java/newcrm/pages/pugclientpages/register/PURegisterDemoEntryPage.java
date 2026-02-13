package newcrm.pages.pugclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PURegisterDemoEntryPage extends RegisterDemoEntryPage {

    public PURegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getPlatformListEle() {
        return this.assertElementExists(By.id("platform_type"), "Demo Account Trading Platform List");
    }

    @Override
    protected WebElement getOpenLiveAccBtn() {
        return assertElementExists(By.cssSelector("[data-testid='openLiveAcc']"), "Open Live Account");
    }

    @Override
    protected WebElement getOpenAccountVerifyBtn() {
        return assertElementExists(By.xpath("//button[@data-testid='button' and contains(@class, 'register_guide_btn')]"), "Verification button");
    }

    @Override
    protected WebElement getPlatformEle(GlobalProperties.PLATFORM platform) {
        return this.assertElementExists(By.cssSelector("li[account_data='MetaTrader " + platform.getServerCategory() + "']"), "Demo Account Trading Platform");
    }

    protected WebElement getAccountTypeListEle() {
        return this.assertElementExists(By.id("account_type"), "Demo Account Type List");
    }

    @Override
    protected WebElement getAccountTypeEle(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType) {
        accountType = GlobalProperties.ACCOUNTTYPE.STANDARD_STP;
        return this.assertElementExists(By.cssSelector("li[account_data='" + accountType.getLiveAccountName() + "']"), "Demo Account Type");
    }

    protected WebElement getCurrencyListEle() {
        return this.assertElementExists(By.id("currency"), "Demo Account Currency List");
    }

    @Override
    protected WebElement getCurrencyEle(GlobalProperties.CURRENCY currency) {
        return this.assertElementExists(By.cssSelector("li[account_data='" + currency.getCurrencyDesc() + "']"), "Demo Account Currency");
    }

    protected WebElement getLeverageListEle() {
        return this.assertElementExists(By.id("leverage"), "Demo Account Leverage List");
    }

    @Override
    protected WebElement getLeverageEle(String leverage) {
        return this.assertElementExists(By.cssSelector("li[account_data='" + leverage + "']"), "Demo Account Leverage");
    }

    protected WebElement getAccountBalanceListEle() {
        return this.assertElementExists(By.id("balance"), "Demo Account Balance List");
    }

    @Override
    protected WebElement getAccountBalanceEle(String balance) {
        return this.assertElementExists(By.cssSelector("li[account_data='" + balance + "']"), "Demo Account Balance");
    }

    @Override
    protected WebElement getCountryListItemEle(String country) {
        return assertElementExists(By.xpath("//li[@data-address='"+country+"']"), country + "Country");
    }

    @Override
    protected WebElement getAllowCookieEle() {
        return checkElementExists(By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll"), "Allow All Cookie button");
    }

    @Override
    protected WebElement getExpandAccountConfigEle() {
        return this.assertElementExists(By.cssSelector("p.custom_account"), "Expand Account Config icon");
    }

    @Override
    public WebElement getSubmitButton() {
        return assertElementExists(By.xpath("//button[@id='button']"), "Submit button");
    }

    @Override
    public WebElement getCookiePrefencesContentEle() {
        return assertVisibleElementExists(By.id("CybotCookiebotDialog"), "Cookie Preferences Content");
    }

    @Override
    public boolean setCountry(String country) {
        WebElement e = getCountryInput();
        e.click();
        setInputValue_withoutMoveElement(e, country);
        getCountryListItemEle(country).click();
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Country to: " + country);
        return true;
    }

    @Override
    public void setPlatForm(GlobalProperties.PLATFORM platform) {
//        clickElement(getPlatformListEle());
        clickElement(getPlatformEle(platform));
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Demo Account Trading Platform to: " + platform.getPlatformDesc());
    }

    @Override
    public void setAccountType(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType) {
//        clickElement(getAccountTypeListEle());
        clickElement(getAccountTypeEle(platform, accountType));
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Demo Account Type to: " + accountType.getLiveAccountName());
    }

    @Override
    public void setCurrency(GlobalProperties.CURRENCY currency) {
//        clickElement(getCurrencyListEle());
        clickElement(getCurrencyEle(currency));
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Demo Account Currency to: " + currency.getCurrencyDesc());
    }

    @Override
    public void setLeverage(String leverage) {
//        clickElement(getLeverageListEle());
        clickElement(getLeverageEle(leverage));
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Demo Account Leverage to: " + leverage);
    }

    @Override
    public void setAccountBalance(String balance) {
//        clickElement(getAccountBalanceListEle());
        clickElement(getAccountBalanceEle(balance));
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Demo Account Balance to: " + balance);
    }

    @Override
    public void clickExpandAccountConfig() {
        getExpandAccountConfigEle().click();
        GlobalMethods.printDebugInfo("Click Expand Account Config icon");
    }

}
