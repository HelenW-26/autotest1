package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;

public class STARASICPersonalDetailsPage extends STARPersonalDetailsPage {

    public STARASICPersonalDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getNationalityInput() {
        return this.findClickableElementByXpath("//div[@data-testid='nationality']/div/input");
    }

    @Override
    protected WebElement getMobileInput() {
        return this.findClickableElementByXpath("//div[@data-testid='mobile']/input");
    }

    protected WebElement getMobileCountryCodeInput() {
        return this.findClickableElementByXpath("//div[@data-testid='mobile']//div[contains(@class, 'ht-select')]");
    }

    @Override
    public String setPhone(String phone) {
        WebElement e = this.getMobileCountryCodeInput();
        this.moveElementToVisible(e);
        e.click();

        String item_xpath = "//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li";
        WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item_xpath)));
        List<WebElement> ops = driver.findElements(By.xpath(item_xpath));
        String countryCode = this.selectRandomValueFromDropDownList(ops);

        LogUtils.info("PersonalDetailsPage: set mobile country code to: " + countryCode);

        e = this.getMobileInput();
        this.moveElementToVisible(e);
        e.click();
        e.sendKeys(phone);
        LogUtils.info("PersonalDetailsPage: set mobile to: " + phone);

        return phone;
    }

}
