package newcrm.pages.clientpages.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import newcrm.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LogUtils;

import java.time.Duration;

public class RegisterGoldHomePage extends Page {

    public RegisterGoldHomePage(WebDriver driver,String url) {
        super(driver);
        driver.get(url);
        this.waitLoading();
    }

    protected WebElement getProtectedPwdInput() {
        return assertElementExists(By.xpath("//input[@name='post_password']"), "Page Protected Password");
    }

    protected WebElement getDemoDomainUrlInput() {
        return assertElementExists(By.id("CrmUrl"), "Domain Url");
    }

    protected WebElement getASICRegistrationDomainUrlInput() {
        return assertElementExists(By.cssSelector("div.chang-action-href input"), "Domain Url");
    }

    protected WebElement getDemoDomainUrlSubmitBtn() {
        return assertElementExists(By.id("changeCrmUrl"), "Domain Url Submit button");
    }

    protected WebElement getASICRegistrationDomainUrlSubmitBtn() {
        return assertElementExists(By.cssSelector("div.chang-action-href button"), "Domain Url Submit button");
    }

    protected WebElement getProtectedPwdSubmitBtn() {
        return assertElementExists(By.xpath("//input[@name='Submit']"), "Page Protected Submit button");
    }

    protected WebElement getDemoContinueBtn() {
        return assertElementExists(By.cssSelector("p#next"), "Continue button");
    }

    protected WebElement getSrcBranchVersionInput() {
        return assertElementExists(By.cssSelector("#form_branchversion"), "Branch Version");
    }

    protected WebElement getASICSrcBranchVersionInput() {
        return assertElementExists(By.cssSelector("input.form-branchversion"), "Branch Version");
    }

    public void waitLoadingPageContent(String oldUrl) {
        boolean urlChanged;

        try {
            // Check if still remains at input password page after submission
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            urlChanged = wait.until((ExpectedCondition<Boolean>) wd ->
                    !wd.getCurrentUrl().equals(oldUrl)
            );

        } catch (TimeoutException e) {
            urlChanged = false;
        }

        if (!urlChanged) {
            Assert.fail("Demo Password Protected page still present after timeout");
        }

        LogUtils.info("Successfully navigated away from the Demo Password Protected page");

        // Wait for demo content to load
        assertVisibleElementExists(By.cssSelector("div.demo-account"), "Demo Account Page Content");

        // Wait for Current Submit URL display content to load
        try {
            new WebDriverWait(driver, Duration.ofSeconds(60), Duration.ofSeconds(5)).until(driver -> {
                // Handle UAT abnormal behavior, Current Submit URL display content won't load without page interaction
                this.moveElementToVisible(this.getDemoDomainUrlInput());
                this.getDemoDomainUrlInput().click();
                LogUtils.info("Click Domain Url");

                String text = assertElementExists(By.cssSelector("span#nowAdd"), "Current Submit URL Display Content").getText();
                LogUtils.info("Current Submit URL Display Content: " + text);
                return !text.trim().isEmpty();
            });
        } catch(TimeoutException ex) {
            Assert.fail("Failed to load Current Submit URL display content");
        }

    }

    public void setDemoRegistrationDomainUrl(String url) {
        this.setInputValue(this.getDemoDomainUrlInput(), url);
        LogUtils.info("Set Domain Url to: " + url);

        getDemoDomainUrlSubmitBtn().click();
        LogUtils.info("Click Domain Url Submit button");
    }

    public void setASICRegistrationDomainUrl(String url) {
        this.setInputValue(getASICRegistrationDomainUrlInput(), url);
        LogUtils.info("Set Domain Url to: " + url);

        getASICRegistrationDomainUrlSubmitBtn().click();
        LogUtils.info("Click Domain Url Submit button");
    }

    public void clickDemoContinueBtn() {
        this.getDemoContinueBtn().click();
        LogUtils.info("Click Continue button");
    }

    public void registerDemoAccount() {
        try{
            WebElement pwd = getProtectedPwdInput();
            pwd.sendKeys("147258");

            getProtectedPwdSubmitBtn().click();

        LogUtils.info("Click Submit button");
        } catch (AssertionError e) {
            GlobalMethods.printDebugInfo("Page Protected Password input not found, skip entering password.");
        }

    }

    public void setSrcBranchVersion() {
        WebElement e = getSrcBranchVersionInput();
        e.clear();
    }

    public void setASICSrcBranchVersion() {
        WebElement e = getASICSrcBranchVersionInput();
        e.clear();
    }

}
