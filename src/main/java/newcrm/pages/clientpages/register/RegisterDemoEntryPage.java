package newcrm.pages.clientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tools.ScreenshotHelper;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LogUtils;

import java.time.Duration;

public class RegisterDemoEntryPage extends RegisterEntryPage {

    public RegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getCountryInput() {
        return this.assertElementExists(By.id("country"), "Country");
    }

    protected WebElement getCountryListItemEle(String country) {
        return assertElementExists(By.xpath("//div[@data-conutry='"+country+"']"), country + "Country");
    }

    protected WebElement getOpenLiveAccBtn() {
        return assertElementExists(By.xpath("//div[@id='demo']//button"), "Open live account button", e -> e.getText().toLowerCase().contains("open live account"));
    }

    protected WebElement getDemoAccConfigResponseContent() {
        return assertVisibleElementExists(By.xpath("//div[@id='demo']"), "Demo Account Response Content");
    }

    protected WebElement getDemoAccNoEle() {
        return assertVisibleElementExists(By.cssSelector("div#demo div.content_info div.el-row > div:first-of-type > div:last-of-type"), "Demo Account Number");
    }

    protected WebElement getDemoAccServerEle() {
        return assertVisibleElementExists(By.cssSelector("div#demo div.content_info div.el-row > div:nth-of-type(2) > div:last-of-type"), "Demo Account Server");
    }

    protected WebElement getDemoAccExpiryDateEle() {
        return assertVisibleElementExists(By.cssSelector("div#demo div.content_info div.el-row > div:last-of-type > div:last-of-type"), "Demo Account Server");
    }

    protected WebElement getVerificationCodeEle() {
        return assertElementExists(By.cssSelector("input#captcha"), "Verification Code");
    }


    protected WebElement getOpenAccountVerifyBtn() { return null; }
    protected WebElement getPlatformEle(GlobalProperties.PLATFORM platform) { return null; }
    protected WebElement getAccountTypeEle(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType) { return null; }
    protected WebElement getCurrencyEle(GlobalProperties.CURRENCY currency) { return null; }
    protected WebElement getLeverageEle(String leverage) { return null; }
    protected WebElement getAccountBalanceEle(String balance) { return null; }
    protected WebElement getCookiePrefencesContentEle() { return null; }

    public void clickOpenLiveAccBtn() {
        triggerClickEvent(getOpenLiveAccBtn());
        GlobalMethods.printDebugInfo("Click Open Live Account button");
    }

    public void clickOpenAccountVerifyBtn() {
        triggerClickEvent(getOpenAccountVerifyBtn());
    }

    public void waitLoadingDemoAccConfigResponseContent() {
        getDemoAccConfigResponseContent();
    }

    public WebElement getDemoAccNo() {
        return getDemoAccNoEle();
    }

    public WebElement getDemoAccServer() {
        return getDemoAccServerEle();
    }

    public WebElement getDemoAccExpiryDate() {
        return getDemoAccExpiryDateEle();
    }

    protected WebElement getAllowCookieEle() { return null; }

    public void setAllowCookie() {
        WebElement e = getAllowCookieEle();

        if (e != null) {
            e.click();
        }
    }

    public void setPlatForm(PLATFORM platform) {}
    public void setAccountType(PLATFORM platform, ACCOUNTTYPE accountType) {}
    public void setCurrency(CURRENCY currency) {}
    public void setLeverage(String leverage) {}
    public void setAccountBalance(String balance) {}
    public void agreeTnC() {}
    public void agreePolicy() {}
    public void agreeReceiveDeals() {}

    public void waitLoadingCookiePreferencesContent() {
        getCookiePrefencesContentEle();
    }

    @Override
    public void submit() {
        GlobalMethods.printDebugInfo("Start to click submit button in register entry page");
        WebElement button = this.getSubmitButton();
        js.executeScript("arguments[0].click()",button);
        GlobalMethods.printDebugInfo("Already click submit in register entry page ");
        this.waitLoadingForCustomise(180);
        this.waitLoading();
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "DemoRegSubmit");
    }

    @Override
    public void setBranchVersion(String branchVersion) {
        branchVersion = branchVersion == null ? "" : branchVersion;
        assertElementExists(By.xpath("//input[@id='form_branchversion']"), "Branch Version").sendKeys(branchVersion);
        LogUtils.info("RegisterEntryPage: set Branch Version to: " + branchVersion);
    }

}
