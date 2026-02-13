package newcrm.pages.clientpages.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.time.Duration;

public class RegisterDemoGoldenFlowEntryPage extends RegisterGoldEntryPage {

    public RegisterDemoGoldenFlowEntryPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getPopupAdCloseBtn() {
        return assertElementExists(By.xpath("//p[@id='delvideo']"), "Popup Ad");
    }

    protected WebElement getOpenLiveAccBtn() {
        return assertElementExists(By.cssSelector("[data-testid='openLiveAcc']"), "Open Live Account");
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

    protected WebElement getOpenAccountVerifyBtn() {
        return assertElementExists(By.xpath("//button[@data-testid='button' and contains(@class, 'register_guide_btn')]"), "Open Account Verify Now button");
    }

    @Override
    protected WebElement getFirstNameInput() {
        return this.findVisibleElemntBy(By.id("name"));
    }

    public void waitLoadingDemoAccConfigResponseContent() {
        getDemoAccConfigResponseContent();
    }

    public void clickOpenLiveAccBtn() {
        triggerClickEvent(getOpenLiveAccBtn());
        GlobalMethods.printDebugInfo("Click Open Live Account button");
    }

    public void clickOpenAccountVerifyBtn() {
        triggerClickEvent(getOpenAccountVerifyBtn());
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

    protected WebElement getSubmitEntryBtn() {
        return assertClickableElementExists(By.id("sub-open"), "Submit button");
    }

    protected WebElement getCountryListItemEle(String country) {
        return assertElementExists(By.xpath("//div[@data-conutry='"+country+"']"), country + "Country");
    }

    @Override
    public boolean setCountry(String country) {
        this.getCountryInput().click();
        getCountryListItemEle(country).click();
        GlobalMethods.printDebugInfo("RegisterEntryPage: set Country to: " + country);
        return true;
    }

    @Override
    public void submit() {
        getPopupAdCloseBtn().click();
        GlobalMethods.printDebugInfo("Close Popup Ad");
        waitLoading();

        GlobalMethods.printDebugInfo("Start to click submit button in register entry page");
        WebElement button = this.getSubmitEntryBtn();
        this.moveElementToVisible(button);
        button.click();
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
