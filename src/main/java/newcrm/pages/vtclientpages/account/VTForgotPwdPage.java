package newcrm.pages.vtclientpages.account;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VTForgotPwdPage extends ForgotPwdPage {

    public VTForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getPhoneNewPasswordEle() {
        return assertElementExists(By.xpath("//input[@data-testid='pass']"), "New Password");
    }

    @Override
    protected WebElement getPhoneConfirmNewPasswordEle() {
        return assertElementExists(By.xpath("//input[@data-testid='checkPass']"), "Confirm New Password");
    }

    @Override
    protected WebElement getPhonePanelEle() {
        return assertElementExists(By.cssSelector("label.btn-color-mode-switch-inner"), "Phone Number Tab");
    }

    @Override
    protected WebElement getCountryCodeSearchEle() {
        return assertElementExists(By.xpath("//input[@name='search']"), "Phone Country Code Search Box");
    }

    @Override
    protected WebElement getPhoneCountryCodeEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='el-input el-popover__reference']//input"), "Phone Country Code");
    }

    @Override
    protected WebElement getPhoneNoEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='mobile']"), "Phone Number");
    }

    @Override
    protected WebElement getChgLoginPwdDialogEle() {
        return checkElementExists(By.xpath("//div[contains(@class,'el-dialog__wrapper') and not(contains(@style,'display: none'))]"));
    }

    @Override
    protected WebElement getEmailSubmitBtnEle() {
        return assertElementExists(By.xpath("//button[@data-testid='submit']"), "Submit button");
    }

    @Override
    protected WebElement getPhoneSubmitBtnEle() {
        return assertElementExists(By.xpath("(//button[@data-testid='submit'])[2]"), "Submit button");
    }

    @Override
    protected WebElement getPhoneOTPReqBtnEle() {
        return assertElementExists(By.xpath("(//button[@data-testid='submit'])[1]"), "Send SMS Code button");
    }

    @Override
    protected WebElement getChgLoginPwdConfirmBtnEle() {
        return assertElementExists(By.cssSelector("[class*='dialog-btn']"), "Confirm button", e -> e.getText().toLowerCase().contains("confirm"));
    }

    @Override
    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message");
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
