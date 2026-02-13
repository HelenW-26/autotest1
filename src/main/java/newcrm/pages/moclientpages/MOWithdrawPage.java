package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.WithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MOWithdrawPage extends WithdrawPage {
    WebDriverWait wait03;

    public MOWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
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
}
