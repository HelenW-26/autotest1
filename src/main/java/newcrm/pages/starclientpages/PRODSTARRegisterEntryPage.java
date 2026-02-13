package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class PRODSTARRegisterEntryPage extends RegisterEntryPage {
    public PRODSTARRegisterEntryPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected List<WebElement> getCountryElements(){
        String xpath = "//div[@class='country-code']/div/div[@class='results-option']";
        this.findVisibleElemntByXpath(xpath);
        return driver.findElements(By.xpath(xpath));
    }

    @Override
    public void checkNonUSResident()
    {
        WebElement checkbox = findClickableElementByXpath("//div[@class='label_box align_items_center']//span[@class='check_ico']");
        this.moveElementToVisible(checkbox);
        checkbox.click();
    }

    @Override
    public void checkAgreeCommunication()
    {
        WebElement checkbox = findClickableElementByXpath("//div[@class='label_box']//span[@class='check_ico']");
        this.moveElementToVisible(checkbox);
        checkbox.click();
    }

    @Override
    public void next() {
        try {
            WebElement cookie = driver.findElement(By.xpath("//span[@class='cookie_close']"));
            cookie.click();
        }
        catch (Exception e)
        {
            LogUtils.info("No cookie agree button");
        }
        WebElement button = driver.findElement(By.xpath("//button[@id='sub-open']"));
        button.click();
        this.waitLoading();
    }
}
