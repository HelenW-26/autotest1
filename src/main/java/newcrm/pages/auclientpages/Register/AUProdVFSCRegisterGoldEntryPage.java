package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterGoldEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.List;

public class AUProdVFSCRegisterGoldEntryPage extends RegisterGoldEntryPage {

    public AUProdVFSCRegisterGoldEntryPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void submit() {

        try {
            driver.findElement(By.xpath("//p[@id='delvideo']")).click();

        }catch(Exception e)
        {
            LogUtils.info("no popup ad");
        }

        LogUtils.info("Start to click submit button in register entry page");
        WebElement button = this.getSubmitButton();
        js.executeScript("arguments[0].click()",button);
        LogUtils.info("Already click submit in register entry page ");
        this.waitLoadingForCustomise(180);
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "RegSubmit");
    }

    @Override
    public void checkNonUSResident()
    {
        WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox'][@id='notUs']"));
        js.executeScript("arguments[0].click()", checkbox);

        WebElement termsCheckbox = driver.findElement(By.xpath("//input[@type='checkbox'][@id='agreeTerms']"));
        js.executeScript("arguments[0].click()", termsCheckbox);
    }

    protected WebElement getCountryItemEle(String country) {
        return assertVisibleElementExists(By.xpath("//div[@class='country-code' and @style='display: block;']/div/div[@class='results-option' and @data-conutry='" + country + "']"), country + " Country");
    }

    @Override
    public boolean setCountry(String country) {
        // Loading
        waitLoadingTestSite();

        // Wait for page content to load properly
        try {
            Thread.sleep(5000);
            LogUtils.info("Wait 5s for page content to load properly...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int attempt = 1; attempt <= 5; attempt++) {
            // Click country
            WebElement countryEle = assertElementExists(By.id("country"), "Country");
            this.moveElementToVisible(countryEle);
            clickElement(countryEle);
            LogUtils.info("Click Country");

            WebElement countryListEle = assertElementExists(By.cssSelector("div.country-code"), "Country List");
            if (countryListEle.isDisplayed()) {
                break;
            }

            LogUtils.info("Retry clicking Country on attempt " + attempt);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Get country list
        WebElement countryItemEle = this.getCountryItemEle(country);
        this.moveElementToVisible(countryItemEle);
        countryItemEle.click();
        LogUtils.info("RegisterEntryPage: set country to: " + countryItemEle.getAttribute("innerText").trim());
        return true;
    }

    public void waitLoadingTestSite() {
        try {
            fastwait.until((ExpectedCondition<Boolean>) d -> {
                List<WebElement> loaders = d.findElements(By.xpath("//div[@class='vau-loading-wrapper' and not(contains(@style, 'display: none'))]"));
                return loaders.isEmpty();
            });
        } catch (Exception e) {
        }
    }

}
