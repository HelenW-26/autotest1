package newcrm.pages.umclientpages;

import newcrm.pages.clientpages.WithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UMWithdrawPage extends WithdrawPage {

    WebDriverWait wait03;

    public UMWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Override
    public WebElement getQRpath(){
        return findVisibleElemntByXpath("//div[@id='changeAuthenticator']//canvas");
    }

}
