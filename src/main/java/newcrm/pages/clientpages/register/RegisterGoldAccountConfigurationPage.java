package newcrm.pages.clientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;
import java.util.Random;

public class RegisterGoldAccountConfigurationPage extends Page {

    public RegisterGoldAccountConfigurationPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getSetupAccountButton() {
        return findClickableElementByXpath("//button[@data-testid='mainButton']");
    }

    protected List<WebElement> getAccountTypeEles(){
        return driver.findElements(By.xpath("//ul[contains(@class, 'account_configuration_types')]//li"));
    }

    protected WebElement getCurrencyEle() {
        return driver.findElement(By.xpath("//div[@data-testid='currency']"));
    }

    protected WebElement getTickBoxInput() {
        return driver.findElement(By.xpath("//div[@data-testid='byTicking']"));
    }

    protected WebElement getViewMyAccountsButton() {
        return driver.findElement(By.xpath("//div[@class='account_opening_complete_btns']/button[@data-testid='button']"));
    }

    public String setAccountType() {
        List<WebElement> els = this.getAccountTypeEles();

        if(els == null || els.isEmpty()) {
            Assert.fail("No account type available");
        }

        Random random = new Random();
        WebElement  e = els.get(random.nextInt(els.size()));
        this.moveElementToVisible(e);
        String result = e.getAttribute("data-testid");
        e.click();
        waitLoading();

        LogUtils.info("AccountConfigurationPage: set account type to: " + result);

        return result;
    }

    public String setAccountType(GlobalProperties.ACCOUNTTYPE accountType) {
        String result = accountType.getTestId();
        WebElement accountT = driver.findElement(By.xpath("//li[@data-testid='" + result + "']"));
        //js.executeScript("arguments[0].click()",accountT);
        this.moveElementToVisible(accountT);
        accountT.click();
        waitLoading();

        LogUtils.info("AccountConfigurationPage: set account type to: " + result);
        return result;
    }

    public String setCurrency() {
        WebElement e = this.getCurrencyEle();
        e.click();
        waitLoading();

        String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if (result != null) {
            this.moveElementToVisible(e);
            e.click();
            waitLoading();
            LogUtils.info("AccountConfigurationPage: set currency to: " + result);
        } else
        {
            LogUtils.info("ERROR: AccountConfigurationPage: set currency failed!" );
            Assert.fail("No currency available");
        }

        return result;
    }

    public String setCurrency(GlobalProperties.CURRENCY currency) {
        String result = currency.toString();
        WebElement e = this.getCurrencyEle();
        e.click();
        waitLoading();

        this.getAllOpendElements();
        WebElement accountC = driver.findElement(By.xpath("//li[@data-testid='" + result + "']"));
        this.moveElementToVisible(accountC);
        js.executeScript("arguments[0].click()", accountC);
        waitLoading();
        LogUtils.info("AccountConfigurationPage: set currency to: " + result);
        return result;
    }

    public boolean setPlatForm(GlobalProperties.PLATFORM platform) {
        driver.findElement(By.xpath("//div[@class='account_configuration_platform']//input[@class='el-input__inner']")).click();
        switch (platform.toString().toLowerCase()) {

            case "mt4":
                this.findClickableElementByXpath("//span[contains(text(),'MetaTrader 4')]").click();
                waitLoading();
                this.findClickableElementByXpath("//button/span[contains(text(),'Continue with MetaTrade 4')]").click();
                waitLoading();
                break;

            case "mts":
                this.findClickableElementByXpath("(//ul/li/span[contains(text(),'Copy Trading')])[2]").click();
                waitLoading();
                break;

            case "mt5":
                this.findClickableElementByXpath("//span[contains(text(),'Recommended-MetaTrader 5')]").click();
                waitLoading();
                break;

            default:
                LogUtils.info("OpenAdditionalAccountPage: Not found mt4/5 and mts");
                break;
        }
        LogUtils.info("AccountConfigurationPage: set Platform to: " + platform);
        return true;
    }

    public void clickSetupAccountBtn() {
        triggerClickEvent(this.getSetupAccountButton());
        LogUtils.info("Click Setup Account button");
        this.waitLoading();
    }

    public void clickOpenAccountBtn() {
        triggerClickEvent(this.getSetupAccountButton());
        LogUtils.info("Click Open Account button");
        this.waitLoading();
    }

    public String getSetupAccountBtn() {
        WebElement e = this.getSetupAccountButton();
        this.moveElementToVisible(e);
        WebElement span = e.findElement(By.tagName("span"));

        return span.getText();
    }

    public void selectTickBox() {
        WebElement e = this.getTickBoxInput();
        this.moveElementToVisible(e);

        // Select tick box if not checked
        WebElement section = e.findElement(By.cssSelector("section.ht-protocol__checkbox"));
        String classAttr = section.getAttribute("class");
        if (classAttr.contains("active")) {
            LogUtils.info("AccountConfigurationPage: Already ticked agreement");
            return;
        }
        e.click();
    }

    public void clickViewMyAccountsButton() {
        triggerClickEvent(this.getViewMyAccountsButton());
    }

    public void waitLoadingOpenAccountContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'kyc_drawer') and not(contains(@style,'display'))]//div[@aria-label='Open Account']"),"Open Account Content");
    }

    public void waitLoadingSetupAccountContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'kyc_drawer') and not(contains(@style,'display'))]//div[@aria-label='Setup Trading Account']"),"Setup Account Content");
        waitLoader();
    }
    public void closeSetUpAccount(){
        triggerClickEvent(driver.findElement(By.xpath("//button[@aria-label='close Setup Trading Account']")));
    }

    public String getAccountOpenTitle(){
        return findVisibleElemntBy(By.xpath("//div/*[@class='account_opening_complete_title']")).getText();
    }

    public String getAccountOpenCompleteContext(){
        return findVisibleElemntBy(By.xpath("//p[@class='account_opening_complete_content']")).getText();

    }


}
