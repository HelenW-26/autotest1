package newcrm.pages.clientpages.account;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Supplier;

public class ForgotPwdPage extends Page {

    public ForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getPhonePanelEle() {
        return assertElementExists(By.xpath("//div[@id='tab-Phone']"), "Phone Number Tab");
    }

    protected WebElement getEmailForgotPwdEle() {
        return assertElementExists(By.xpath("//a[@data-testid='forgetPassword']"), "Email Forgot Password link button");
    }

    protected WebElement getPhoneForgotPwdEle() {
        return assertElementExists(By.xpath("//a[@data-testid='forgetPassword']"), "Phone Number Forgot Password link button");
    }

    protected WebElement getPhoneCountryCodeEle() {
        return assertVisibleElementExists(By.xpath("//div[@data-testid='countrycode']//input"), "Phone Country Code");
    }

    protected WebElement getCountryCodeSearchEle() {
        return assertElementExists(By.xpath("//input[@data-testid='search']"), "Phone Country Code Search Box");
    }

    protected WebElement getCountryCodeItem(String strCountryCode) {
        return assertElementExists(By.xpath("//span[@class='view-value' and text()='+" + strCountryCode + "']"), strCountryCode + " Country Code");
    }

    protected WebElement getEmailEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='email']"), "Email");
    }

    protected WebElement getPhoneNoEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='phone']"), "Phone Number");
    }

    protected WebElement getNewPasswordEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='password']"), "New Password");
    }

    protected WebElement getConfirmNewPasswordEle() {
        return assertElementExists(By.xpath("//input[@data-testid='checkPassword']"), "Confirm New Password");
    }

    protected WebElement getPhoneNewPasswordEle() {
        return assertVisibleElementExists(By.xpath("//input[@data-testid='password']"), "New Password");
    }

    protected WebElement getPhoneConfirmNewPasswordEle() {
        return assertElementExists(By.xpath("//input[@data-testid='checkPassword']"), "Confirm New Password");
    }

    protected WebElement getEmailSubmitBtnEle() {
        return assertElementExists(By.xpath("//button[@data-testid='button']"), "Submit button");
    }

    protected WebElement getPhoneSubmitBtnEle() {
//        return assertElementExists(By.xpath("//button[@data-testid='button']"), "Submit button");
        return assertElementExists(By.xpath("//button[@data-testid='submit']"), "Submit button");
    }

    protected WebElement getPhoneOTPReqBtnEle() {
        return assertElementExists(By.xpath("//button[@data-testid='button']"), "Send SMS Code button");
    }

    protected WebElement getPhoneOTPEle() {
        return assertElementExists(By.xpath("//input[@data-testid='smsCode']"), "SMS Code");
    }

    protected WebElement getResetPwdSubmitBtnEle() {
        return assertElementExists(By.xpath("//button[@data-testid='submit']"), "Submit button");
    }

    protected WebElement getBackToLoginBtnEle() {
        return checkElementExists(By.xpath("//div[@class='back_to_login']//span[@class='btn_text']"), "Back To Login In button");
    }

    protected WebElement getChgLoginPwdDialogEle() {
        return checkElementExists(By.xpath("//div[contains(@class,'el-dialog__wrapper ht-dialog') and not(contains(@style,'display: none'))]"));
    }

    protected WebElement getChgLoginPwdConfirmBtnEle() {
        return assertElementExists(By.cssSelector("[class*='ht-dialog-button']"), "Confirm button", e -> e.getText().toLowerCase().contains("confirm"));
    }

    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.ht-message");
    }

    protected By getExceedAttemptMsgBy() {
        return By.xpath("//*[contains(text(),'User IP attempt exceeded limit')]");
    }

    public void setEmail(String email) {
        getEmailEle().sendKeys(email);
        LogUtils.info("Set Email: " + email);
    }

    public void setNewPassword(String pwd) {
        getNewPasswordEle().sendKeys(pwd);
        LogUtils.info("Set New Password: " + pwd);
    }

    public void setConfirmNewPassword(String pwd) {
        getConfirmNewPasswordEle().sendKeys(pwd);
        LogUtils.info("Set Confirm New Password: " + pwd);
    }

    public void setPhoneNewPassword(String pwd) {
        getPhoneNewPasswordEle().sendKeys(pwd);
        LogUtils.info("Set New Password: " + pwd);
    }

    public void setPhoneConfirmNewPassword(String pwd) {
        getPhoneConfirmNewPasswordEle().sendKeys(pwd);
        LogUtils.info("Set Confirm New Password: " + pwd);
    }

    public String[] getPhoneNoFormat(String val) {
        if (val == null || val.trim().isEmpty()) {
            return new String[0]; // return empty array if input is null/blank
        }

        String[] values = val.trim().split("\\s+"); // split by one or more spaces
        String str1 = values.length > 0 ? values[0].trim() : "";
        String str2 = values.length > 1 ? values[1].trim() : "";

        return new String[]{str1, str2};
    }

    public void setPhoneNo(String phoneNo) {
        // Set phone number
        WebElement phoneNumber = getPhoneNoEle();
        phoneNumber.sendKeys(phoneNo);
        LogUtils.info("Set Phone Number");
    }

    public void setCountryCode(String countryCode) {
        // Click on country code dropdown
        WebElement phoneCountryCode = getPhoneCountryCodeEle();
        phoneCountryCode.click();
        waitLoading();
        LogUtils.info("Click Country Code");
        assertVisibleElementExists(By.xpath("//div[contains(@class,'el-popper') and not(contains(@style, 'display: none'))]"), "Phone Country Code List");

        // Filter country code via search box
        setCountryCodeSearchValue(countryCode);

        // Select country code
        WebElement countryCodeVal = getCountryCodeItem(countryCode);
        this.moveElementToVisible(countryCodeVal);
        countryCodeVal.click();
        LogUtils.info("Select Country Code");
    }

    public void setCountryCodeSearchValue(String countryCode) {
        WebElement countryCodeSearch = getCountryCodeSearchEle();
        if (countryCodeSearch != null) {
            countryCodeSearch.click();
            LogUtils.info("Click Country Code List search box");
            countryCodeSearch.sendKeys(countryCode);
            LogUtils.info("Search for Country Code: " + countryCode);
        }
    }

    public void setPhoneOTP(String code) {
        getPhoneOTPEle().sendKeys(code);
        LogUtils.info("Set OTP: " + code);
    }

    public void clickPhoneOTPReqBtn() {
        WebElement e = getPhoneOTPReqBtnEle();
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Click Send SMS Code button");
    }

    public void clickEmailForgotPwd() {
        WebElement e = getEmailForgotPwdEle();
        triggerElementClickEvent_withoutMoveElement(e);
        waitLoading();
        LogUtils.info("Click Email Forgot Password");
    }

    public void clickPhoneForgotPwd() {
        WebElement e = getPhoneForgotPwdEle();
        triggerElementClickEvent_withoutMoveElement(e);
        waitLoading();
        LogUtils.info("Click Phone Number Forgot Password");
    }

    public void clickPhonePanel() {
        WebElement phonePanel = getPhonePanelEle();
        String ariaSelected = phonePanel.getAttribute("aria-selected");

        if (ariaSelected == null) {
            triggerElementClickEvent_withoutMoveElement(phonePanel);
            LogUtils.info("Click Phone Number Tab");
        }
    }

    public void submitEmail() {
        WebElement btn = this.getEmailSubmitBtnEle();
        triggerElementClickEvent_withoutMoveElement(btn);
        LogUtils.info("Click Submit button");
    }

    public void submitPhone() {
        WebElement btn = this.getPhoneSubmitBtnEle();
        triggerElementClickEvent_withoutMoveElement(btn);
        LogUtils.info("Click Submit button");
    }

    public void checkEmailForgotPwdSuccess() {

        // Check for error
        checkAlertMsg();

        // Check exceed ip attempt msg
        String exceedAttemptMsg = checkExistsIPExceedAttemptMsg();

        if (exceedAttemptMsg != null) {
            Assert.fail("Email Forgot Password failed. Error Msg: " + exceedAttemptMsg);
        }

        // Get response
        WebElement response = checkElementExists(By.xpath("//*[contains(text(),'instructions to reset your password')]"), "Forgot Password Response");
        String respMsg = response.getText();
        LogUtils.info("Forgot Password Resp Msg: " + respMsg);

        if(respMsg == null || respMsg.isEmpty()) {
            Assert.fail("Forgot Password response message is empty");
        }
    }

    public void checkPhoneForgotPwdSuccess() {
        // Check for error
        checkAlertMsg();
    }

    public void submitResetPwd() {
        waitLoading();
        WebElement btn = this.getResetPwdSubmitBtnEle();
        triggerElementClickEvent_withoutMoveElement(btn);
        LogUtils.info("Click Submit button");
    }

    public void checkResetPwdSuccess() {
        // Check for dialog popup
        checkExistsChangeLoginPwdDialog();

        // Get response
        WebElement response = checkElementExists(By.xpath("//*[contains(text(),'password has been updated')]"), "Reset Password Response");
        String respMsg = response.getText();
        LogUtils.info("Reset Password Resp Msg: " + respMsg);

        if(respMsg == null || respMsg.isEmpty()) {
            Assert.fail("Reset Password response message is empty");
        }
    }

    public void clickBackToLogin(String url) {
        // Back to login page
        WebElement bkToLogin = getBackToLoginBtnEle();
        if (bkToLogin != null) {
            triggerElementClickEvent_withoutMoveElement(bkToLogin);
            LogUtils.info("Click Back To Login In button");
        } else {
            LogUtils.info("No Back To Login In button found. Redirecting to login page...");
            driver.get(url);
        }
    }

    public Map.Entry<Boolean, String> checkAlertMsg() {
        return checkAlertMsg(this::getAlertMsgBy, "Forgot Password");
    }

    public String checkExistsIPExceedAttemptMsg() {
        // IP limit based on apollo settings > sys-system
        // anti.attack.switch
        // anti.attack.block.time.in.hour
        // anti.attack.max.number.email.attempt
        // anti.attack.max.number.ip.attempt
        return checkExistsAlertMsg(this::getExceedAttemptMsgBy, "Forgot Password");
    }

    public void checkExistsChangeLoginPwdDialog() {
        WebElement popup = getChgLoginPwdDialogEle();

        if (popup == null) {
            LogUtils.info("No Change Login Password Dialog found");
            return;
        }

        LogUtils.info("Change Login Password Dialog found");

        WebElement btn = getChgLoginPwdConfirmBtnEle();
        triggerElementClickEvent_withoutMoveElement(btn);
        LogUtils.info("Close Change Login Password Dialog");
    }

    public void goEmailResetPwdPage(String resetPwdLink) {
        driver.get(resetPwdLink);
        LogUtils.info("Go Email Reset Password Page");
    }

    public void checkRedirectToEmailForgotPwdPage(String url) {

        // Check redirection url is valid email forgot password url
        try {
            if (!checkNavigateUrl(url)) {
                Assert.fail("Successfully navigated away from the Login Page but destination URL (" + getCurrentURL() + ") is incorrect");
            }

            LogUtils.info("Login success. Successfully navigated away from the Login Page");

        } catch (Exception e) {
            Assert.fail("Email Forgot Password failed", e);
        }

        String currUrl = getCurrentURL();
        String forgetPwdPageName = "forgetPassword";
        boolean bIsRedirectResetPwd = currUrl.contains(forgetPwdPageName);

        if (!bIsRedirectResetPwd) {
            Assert.fail(String.format("Did not navigate to Email Forgot Password page. Current Url: %s, Expected Page: %s", currUrl, forgetPwdPageName));
        }
    }

    public void checkRedirectToEmailResetPwdPage(String url) {

        // Check redirection url is valid email reset password url
        try {
            if (!checkNavigateUrl(url)) {
                Assert.fail("Successfully navigated to Email Reset Password page but destination URL (" + getCurrentURL() + ") is incorrect");
            }
        } catch (Exception e) {
            Assert.fail("Email Reset Password failed", e);
        }

        String currUrl = getCurrentURL();
        String resetPwdPageName = "resetProfilePassword";
        boolean bIsRedirectResetPwd = currUrl.contains(resetPwdPageName);

        if (!bIsRedirectResetPwd) {
            Assert.fail(String.format("Did not navigate to Email Reset Password page. Current Url: %s, Expected Page: %s", currUrl, resetPwdPageName));
        }
    }

    public void checkRedirectToPhoneForgotPwdPage(String url) {

        // Check redirection url is valid phone forgot password url
        try {
            if (!checkNavigateUrl(url)) {
                Assert.fail("Successfully navigated away from the Login Page but destination URL (" + getCurrentURL() + ") is incorrect");
            }
        } catch (Exception e) {
            Assert.fail("Phone Forgot Password failed", e);
        }

        String currUrl = getCurrentURL();
        String phoneForgotPwdPageName = "forgetPasswordMobile";
        boolean bIsRedirectForgotPwd = currUrl.contains(phoneForgotPwdPageName);

        if (!bIsRedirectForgotPwd) {
            Assert.fail(String.format("Did not navigate to Phone Forgot Password page. Current Url: %s, Expected Page: %s", currUrl, phoneForgotPwdPageName));
        }
    }

    public Map.Entry<Boolean, String> checkAlertMsg(Supplier<By> elementSupplier, String label) {
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", label.replace(" ", ""));

        // Element path
        By locator  = elementSupplier.get();
        if (locator == null) return new AbstractMap.SimpleEntry<>(false, null);

        // Get element by locator
        WebElement alertMsgEle = checkElementExists(locator);
        if (alertMsgEle == null) return new AbstractMap.SimpleEntry<>(false, null);

        // Get element class
        String cls = alertMsgEle.getAttribute("class");
        if (cls == null || cls.isEmpty()) return new AbstractMap.SimpleEntry<>(false, null);
        cls = cls.trim().toLowerCase();

        // Get message content element text
        String alertMsg = alertMsgEle.findElement(By.tagName("p")).getText();
        LogUtils.info(label + " Resp Msg: " + alertMsg);

        boolean bIsSuccessMsg = cls.contains("success");
        boolean bIsErrorMsg = cls.contains("error") || cls.contains("warning");

        // Check empty on msg
        if (alertMsg == null || alertMsg.isEmpty()) {
            Assert.fail(String.format("%s %s but response message is empty", label, (bIsSuccessMsg ? "success" : "failed")));
        }

        // Check for error / warning msg
        if(bIsErrorMsg) {
            Assert.fail(label + " failed. Resp Msg: " + alertMsg);
        }

        // Check for  success msg
        if(bIsSuccessMsg) {
            return new AbstractMap.SimpleEntry<>(true, alertMsg);
        }

        return new AbstractMap.SimpleEntry<>(false, null);
    }

}
