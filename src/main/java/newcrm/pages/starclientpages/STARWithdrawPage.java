package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.WithdrawBasePage;
import newcrm.pages.clientpages.WithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class STARWithdrawPage extends WithdrawPage {

    WebDriverWait wait03;

    public STARWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }


    @Override
    public void clickPwdModifyConfirmBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[.//span[normalize-space()='Confirm']] | //button[@data-testid='button']//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no confirm change password dialog");
        }

    }
    @Override
    public void clickPwdModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[@data-testid='modify']");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("modify password");
        }

    }

    @Override
    public void clickEmailModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[.//span[normalize-space()='Modify']])[1]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("modify email");
        }

    }

    @Override
    public void inputPass(String pwdId,String pwd){

        String xpath = "";
        if(pwdId.equalsIgnoreCase("currentPass")){
            xpath = "//label[normalize-space()='Current Password']/following-sibling::div//input";
        }else if(pwdId.equalsIgnoreCase("email")){
            xpath = "//label[normalize-space()='Enter new email address']/following-sibling::div//input";
        }else {
            xpath ="//label[normalize-space()='Enter verification code']/following-sibling::div//input";
        }

        WebElement webElement = findVisibleElemntByXpath(xpath);
        webElement.clear();
        webElement.sendKeys(pwd);
    }

//    @Override
//    public void clickSendCode(){
//        WebElement webElement = findVisibleElemntByXpath("//div[@class='el-input-group__append' ]//span[normalize-space()='Send Code']");
//        webElement.click();
//    }

    @Override
    public void clickConfirmBtn(int index){
        WebElement clickBtn = findClickableElementByXpath("*//button[@data-testid='changePw']");
        clickBtn.click();
    }

    @Override
    public void click2faModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[@data-testid='modify'])[2]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("click security authenticator App modify button failed!");
        }

    }

    @Override
    public WebElement getQRpath(){
        return findVisibleElemntByXpath("//div[@id='changeAuthenticator']//canvas");
    }


    public void creditOK(){

        try {
            WebElement confirmBtn = findClickableElementByXpath("//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='Confirmation']]");
            confirmBtn.click();
        } catch (Exception  e) {
            GlobalMethods.printDebugInfo("no credit in withdraw page.");
        }
    }
}
