package newcrm.pages.vtclientpages.account;

import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VTLoginPage extends LoginPage {

    public VTLoginPage(WebDriver driver, String url) {
        super(driver,url);
    }

    @Override
    protected WebElement getPhonePanelEle() {
        return assertElementExists(By.cssSelector("label.btn-color-mode-switch-inner"), "Phone Number Tab");
    }

    @Override
    protected WebElement getLoginPhoneCountryCodeEle() {
        return assertElementExists(By.xpath("//div[@class='el-input el-popover__reference']//input"), "Phone Country Code");
    }

    @Override
    protected WebElement getCountryCodeSearchEle() {
        return assertElementExists(By.xpath("//input[@name='search']"), "Phone Country Code Search Box");
    }

    @Override
    protected WebElement getLoginPhoneNoEle() {
        return assertElementExists(By.xpath("//input[@data-testid='phone_login']"), "Phone Number");
    }

    @Override
    protected WebElement getPhoneLoginPwdEle() {
        return assertElementExists(By.xpath("//input[@data-testid='password_login']"), "Login Password");
    }

    @Override
    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.el-message--error > p");
    }

    @Override
    protected WebElement getPhoneSubmitBtnEle() {
        return assertClickableElementByTestIdExists("login", "Phone Number Login button");
    }

    @Override
    public void clickPhonePanel() {
        WebElement phonePanel = getPhonePanelEle();

        String afterContent = (String) js.executeScript(
                "return window.getComputedStyle(arguments[0], '::after').getPropertyValue('content');",
                phonePanel
        );

        if (afterContent == null || !afterContent.contains("Phone Number")) {
            triggerElementClickEvent_withoutMoveElement(phonePanel);
            LogUtils.info("Click Phone Number Tab");
        }
    }

}
