package newcrm.pages.vjpclientpages;

import newcrm.pages.clientpages.WithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class VJPWithdrawPage extends WithdrawPage {
    WebDriverWait wait03;
    public VJPWithdrawPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

}
