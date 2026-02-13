package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.List;

public class ProdVJPRegisterEntryPage extends RegisterEntryPage {
    public ProdVJPRegisterEntryPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getSubmitButton() {
        return this.findClickableElemntBy(By.id("next"));
    }

    @Override
    public void submit(String url) {
        WebElement button = this.getSubmitButton();
        button.click();
        //this.clickElement(this.getSubmitButton());
        this.checkUrlContains(url);
        this.waitLoading();
    }

    @Override
    public void submit() {
        waitLoading();
        WebElement button = driver.findElement(By.xpath("//p[@id='next']"));
        js.executeScript("arguments[0].click()", button);
        waitLoading();
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "RegSubmit");
    }

    public void setEmail(String email) {
        waitLoading();
        WebElement emailInput = driver.findElement(By.xpath("//input[@id='email']"));

        emailInput.sendKeys(email);
        LogUtils.info("RegisterEntryPage: set email to: " + email);
    }

    @Override
    public void next() {
        this.waitLoading();
        WebElement button = driver.findElement(By.xpath("//button[@id='sub-open']"));
        button.click();
        this.waitLoading();
    }

    @Override
    protected List<WebElement> getCountryElements() {
        String xpath = "//div[@class='country-code']/div/div[@class='results-option']";
        this.findVisibleElemntByXpath(xpath);
        return driver.findElements(By.xpath(xpath));
    }

    @Override
    public void checkNonUSResident() {
        try {
            if (driver.findElement(By.xpath("//input[@type='checkbox'][@id='notUs']")).isDisplayed());
            {
                WebElement checkbox= driver.findElement(By.xpath("//input[@type='checkbox'][@id='notUs']"));
                //checkbox.click();
                js.executeScript("arguments[0].click()", checkbox);
            }
            if (driver.findElement(By.xpath("//input[@type='checkbox'][@id='agreeTerms']")).isDisplayed());
            {
                WebElement termsCheckbox = driver.findElement(By.xpath("//input[@type='checkbox'][@id='agreeTerms']"));
                //checkbox.click();
                js.executeScript("arguments[0].click()", termsCheckbox);
            }
        } catch (Exception e) {
            LogUtils.info("Not US residence or agree terms not need to check");
        }
    }
}
