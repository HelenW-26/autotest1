package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VJPRegisterHomePage extends RegisterHomePage {

    public VJPRegisterHomePage(WebDriver driver, String url)
    {
        super(driver,url);
    }

    @Override
    protected WebElement getDemoDomainUrlInput() {
        return assertElementExists(By.id("CrmUrl"), "Domain Url");
    }

    @Override
    protected WebElement getDemoDomainUrlSubmitBtn() {
        return assertElementExists(By.id("changeCrmUrl"), "Domain Url Submit button");
    }

    @Override
    protected WebElement getDemoContinueBtn() {
        return assertElementExists(By.cssSelector("p#next"), "Receive your verification code button");
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
