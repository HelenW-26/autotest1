package newcrm.pages.auclientpages;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.WithdrawPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;

public class AuWithdrawPage extends WithdrawPage {

    WebDriverWait wait03;

    public AuWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Override
    protected WebElement getWithdrawMethodElementNew(GlobalProperties.DEPOSITMETHOD method) {
        LogUtils.info("in AU getWithdrawMethodElementNew" );
        waitLoading();
        WebElement w_method = null;

        String xpath = "//li[contains(translate(@data-testid, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"+
                method.getWithdrawDataTestId().toLowerCase() +"')]";

        try {
            //wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
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
    public void clickEmailModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[@data-testid='email']");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("modify email");
        }

    }

    @Override
    public void click2faModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[@data-testid='modify'])[2]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("click phone modify button failed!");
        }

    }



    @Override
    public WebElement getQRpath(){
        return findVisibleElemntByXpath("//div[@id='changeAuthenticator']//canvas");
    }


    @Override
    public void inputOtp(String code){
        this.waitLoading();
        WebElement webElement = findVisibleElemntByXpath("//div[@class='opt_send']//span");
        webElement.click();
        this.waitLoading();

       List<WebElement> inputs = driver.findElements(By.xpath("//input[@class='otp_input']"));

       for (int i = 0; i < inputs.size(); i++) {
           WebElement element = inputs.get(i);
           element.click();
           element.sendKeys(String.valueOf(code.charAt(i)));
       }

       this.clickPwdModifyConfirmBtn();

    }

//    @Override
    public void clickPwdModifyBtn(){
        this.waitLoading();
        WebElement clickBtn = findClickableElementByXpath("(//button[@data-testid='modify'])[1]");
        clickBtn.click();
    }


}
