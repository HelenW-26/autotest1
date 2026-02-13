package newcrm.pages.umclientpages.account;

import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UMLoginPage extends LoginPage {

    public UMLoginPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @Override
    protected WebElement getLoginPhoneCountryCodeEle() {
        return assertElementExists(By.xpath("(//div[@data-testid='mobile_login']//input)[2]"), "Phone Country Code");
    }

    @Override
    protected WebElement getLoginPhoneNoEle() {
        return assertElementExists(By.xpath("//div[@data-testid='mobile_login']/input"), "Phone Number");
    }

}
