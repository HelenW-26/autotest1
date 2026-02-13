package newcrm.pages.moclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MORegisterHomePage extends RegisterHomePage {

    public MORegisterHomePage(WebDriver driver, String url)
    {
        super(driver,url);
    }

    @Override
    protected WebElement getDemoDomainUrlInput() {
        return assertElementExists(By.cssSelector("div.chang-action-href > input"), "Domain Url");
    }

    @Override
    protected WebElement getDemoDomainUrlSubmitBtn() {
        return assertElementExists(By.cssSelector("div.chang-action-href > button"), "Domain Url Submit button");
    }

    @Override
    protected WebElement getDemoContinueBtn() {
        return assertElementExists(By.cssSelector("div.button > a"), "Continue button");
    }

    @Override
    public void registerDemoAccount() {
        WebElement pwd = getProtectedPwdInput();
        pwd.sendKeys("147258");

        getProtectedPwdSubmitBtn().click();

        LogUtils.info("Click Submit button");
    }

    @Override
    public void clickDemoContinueBtn() {
        clickElement(getDemoContinueBtn());
        LogUtils.info("Click Continue button");
    }

}
