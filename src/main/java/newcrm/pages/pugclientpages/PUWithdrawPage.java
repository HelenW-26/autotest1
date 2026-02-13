package newcrm.pages.pugclientpages;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.pages.clientpages.WithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PUWithdrawPage extends WithdrawPage {

    WebDriverWait wait03;


    public PUWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

}
