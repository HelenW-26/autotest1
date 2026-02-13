package newcrm.pages.vtclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import newcrm.pages.clientpages.WithdrawPage;

import java.time.Duration;

public class VTWithdrawPage extends WithdrawPage {

    WebDriverWait wait03;

    public VTWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Override
    protected WebElement getWithdrawMethodElementNew(GlobalProperties.DEPOSITMETHOD method) {
        waitLoading();
        WebElement w_method = null;

        String xpath = "(//li/div[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '" + method.getWithdrawName().toLowerCase() + "')])[1]";

        try {
            wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            moveElementToVisible(driver.findElement(By.xpath(xpath)));
            w_method =  driver.findElement(By.xpath(xpath));
        }catch(Exception e) {
            assert false : "NonCCWithdrawPage: Could not find the withdraw method: " + method.getWithdrawName();
            //System.out.println("NonCCWithdrawPage: Could not find withdraw methods or you should withdraw through credit card. ");
            return null;
        }
        return w_method;
    }

    @Override
    public void inputPass(String pwdId,String pwd){

        String xpath = "";
        if(pwdId.equalsIgnoreCase("currentPass")){
            xpath = "//label[normalize-space()='Current password']/following-sibling::div//input";
        }else if(pwdId.equalsIgnoreCase("email")){
            xpath = "//label[normalize-space()='Enter new email address']/following-sibling::div//input";
        }else {
            xpath ="//label[normalize-space()='Enter Verification Code']/following-sibling::div//input";
        }

        WebElement webElement = findVisibleElemntByXpath(xpath);
        webElement.clear();
        webElement.sendKeys(pwd);
    }

    @Override
    public void inputModifyPass(String pwdId,String pwd){

        String xpath = "";
        if(pwdId.equalsIgnoreCase("currentPass")){
            xpath = "//label[normalize-space()='Current password']/following-sibling::div//input";
        }else if(pwdId.equalsIgnoreCase("pass")){
            xpath = "//label[normalize-space()='New password']/following-sibling::div//input";
        }else {
            xpath ="//label[normalize-space()='Confirm Password']/following-sibling::div//input";
        }

        WebElement webElement = findVisibleElemntByXpath(xpath);
        webElement.clear();
        webElement.sendKeys(pwd);
    }

    @Override
    public void clickSendCode(){
        WebElement webElement = findVisibleElemntByXpath("//span[contains(@class,'send_code') and normalize-space()='Send Code']");
        webElement.click();
    }

    @Override
    public void closePwdUpdateDialog(){
        try {
            WebElement securityTab = findVisibleElemntByXpath("//div[@id='SuccessComponent']//button[@aria-label='Close']");
            securityTab.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("No dialog");
        }

    }

    @Override
    public void clickPwdModifyConfirmBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[.//span[normalize-space()='Confirm']]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no confirm change password dialog");
        }

    }

    @Override
    public void clickVerify2faConfirmBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[.//span[normalize-space()='Confirm']]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no confirm 2fa dialog");
        }

    }

    @Override
    public WebElement getQRpath(){
        return findVisibleElemntByXpath("//canvas");
    }

    @Override
    public void clickConfirmDisclaimer(){
        try {
            WebElement clickBtn = findClickableElementByXpath("//label[@data-testid='tncConfirm']");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("There isn't confirm disclaimer");
        }
    }

    @Override
    public void inputVerifyCode(String code){
        try {
            WebElement webElement = findVisibleElemntByXpath("//div[@class='authenticator']//input");
            webElement.clear();
            webElement.sendKeys(code);
            clickConfirmBtn(2);
            waitLoading();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("input otp code");
        }
    }

    @Override
    public void inputOtp(String code){
        this.waitLoading();
        WebElement webElement = findVisibleElemntByXpath("//div[@class='authenticator']//span");
        webElement.click();
        webElement = findVisibleElemntByXpath("//div[@class='authenticator']//input");
        webElement.clear();
        webElement.sendKeys(code);
        this.waitLoading();
        clickConfirmBtn(2);
    }

    @Override
    public void creditOK(){

        try {
            WebElement confirmBtn = findClickableElementByXpath("//button[.//span[normalize-space(text())='OK']]");
            confirmBtn.click();
        } catch (Exception  e) {
            GlobalMethods.printDebugInfo("no credit in withdraw page.");
        }
    }

    @Override
    public String getVerifyMsgEnable2FA(){
        try {
            this.waitLoading();
            WebElement webElement = findVisibleElemntByXpath("//div[@id='ConfirmationComponent']//div[@class='body']");

            return webElement.getText();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("get verification message failed when enable withdraw 2FA...");
            return null;
        }
    }
}
