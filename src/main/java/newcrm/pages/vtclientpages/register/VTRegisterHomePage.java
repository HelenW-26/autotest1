package newcrm.pages.vtclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VTRegisterHomePage extends RegisterHomePage {

    public VTRegisterHomePage(WebDriver driver, String url)
    {
        super(driver,url);
    }

    @Override
    protected WebElement getDemoDomainUrlInput() {
        return assertElementExists(By.xpath("//input[@class='change_live']"), "Domain Url");
    }

    @Override
    protected WebElement getDemoDomainUrlSubmitBtn() {
        return assertElementExists(By.id("change_button"), "Domain Url Submit button");
    }

    @Override
    protected WebElement getDemoContinueBtn() {
        return assertElementExists(By.id("button_next"), "Continue button");
    }

    @Override
    public void registerDemoAccount() {
        WebElement pwd = getProtectedPwdInput();
        pwd.sendKeys("147258");

        getProtectedPwdSubmitBtn().click();

        LogUtils.info("Click Submit button");
    }

}
