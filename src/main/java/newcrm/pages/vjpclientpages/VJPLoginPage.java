package newcrm.pages.vjpclientpages;

import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VJPLoginPage extends LoginPage {
    String url;
    public VJPLoginPage(WebDriver driver, String url) {
        super(driver, url);
        this.url = url;
    }

    @Override
    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.ht-message--error > p, div.el-message.ht-message--warning > p");
    }

}
