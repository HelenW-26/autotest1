package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class PRODVFXRegisterEntryPage extends RegisterEntryPage {
    public PRODVFXRegisterEntryPage(WebDriver driver)
    {
        super(driver);
    }

    protected WebElement getSubmitButton() {
        return this.findClickableElemntBy(By.id("sub-open"));
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
        try {
            driver.findElement(By.xpath("//p[@id='delvideo']")).click();

        }catch(Exception e)
        {
            LogUtils.info("no popup ad");
        }
        WebElement button = this.getSubmitButton();
       // button.click();
        js.executeScript("arguments[0].click()",button);
        this.waitLoadingForCustomise(180);
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

        WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox'][@id='notUs']"));
        //checkbox.click();
        js.executeScript("arguments[0].click()", checkbox);

        WebElement termsCheckbox = driver.findElement(By.xpath("//input[@type='checkbox'][@id='agreeTerms']"));
        //checkbox.click();
        js.executeScript("arguments[0].click()", termsCheckbox);
    }
}
