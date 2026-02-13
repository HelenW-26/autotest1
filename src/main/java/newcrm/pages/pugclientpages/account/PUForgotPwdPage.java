package newcrm.pages.pugclientpages.account;

import newcrm.pages.clientpages.account.ForgotPwdPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PUForgotPwdPage extends ForgotPwdPage {

    public PUForgotPwdPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getBackToLoginBtnEle() {
        return checkElementExists(By.xpath("//div[@class='back_to_login']//span[@class='btn_text']"), "Back To Login In button");
    }

}
