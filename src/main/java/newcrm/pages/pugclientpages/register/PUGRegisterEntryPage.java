package newcrm.pages.pugclientpages.register;

import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PUGRegisterEntryPage extends RegisterEntryPage {

    public PUGRegisterEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getSendCodeBtnEle() {
        return assertElementExists(By.xpath("//input[@id='send_code']"), "Send Code button");
    }

}
