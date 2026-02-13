package newcrm.pages.pugclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class PURegisterHomePage extends RegisterHomePage {

    public PURegisterHomePage(WebDriver driver, String url)
    {
        super(driver,url);
    }

    @Override
    protected WebElement getDemoDomainUrlInput() {
        return assertVisibleElementExists(By.id("changeurl"), "Domain Url");
    }

    @Override
    protected WebElement getDemoDomainUrlSubmitBtn() {
        return assertElementExists(By.id("changebtn"), "Domain Url Submit button");
    }

    @Override
    protected WebElement getDemoContinueBtn() {
        return assertElementExists(By.cssSelector("button#button.demo_account_button"), "Continue button");
    }

    @Override
    public void registerDemoAccount() {
        WebElement pwd = getProtectedPwdInput();
        pwd.sendKeys("147258");

        getProtectedPwdSubmitBtn().click();

        LogUtils.info("Click Submit button");
    }

}
