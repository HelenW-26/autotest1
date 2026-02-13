package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class STARRegisterHomePage extends RegisterHomePage {

    public STARRegisterHomePage(WebDriver driver, String url)
    {
        super(driver,url);
    }

    @Override
    protected WebElement getDemoDomainUrlInput() {
        return assertElementExists(By.id("actionInfo"), "Domain Url");
    }

    @Override
    protected WebElement getDemoDomainUrlSubmitBtn() {
        return assertElementExists(By.id("effect"), "Domain Url Submit button");
    }

    @Override
    protected WebElement getDemoContinueBtn() {
        return assertElementExists(By.cssSelector("button#next"), "Continue button");
    }

    @Override
    public void registerLiveAccount() {
        WebElement pwd = driver.findElement(By.xpath("//input[@name='post_password']"));
        pwd.sendKeys("147258");

        WebElement enter = driver.findElement(By.xpath("//input[@name='Submit']"));
        enter.click();
    }

    @Override
    public void registerDemoAccount() {
        WebElement pwd = getProtectedPwdInput();
        pwd.sendKeys("147258");

        getProtectedPwdSubmitBtn().click();

        LogUtils.info("Click Submit button");
    }

}
