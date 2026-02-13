package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class DAPRegisterResidentialAddressPage extends Page {

    public DAPRegisterResidentialAddressPage(WebDriver driver)
    {
        super(driver);
    }

    protected WebElement getSuburbInput() {
        return assertElementExists(By.xpath("//input[@data-testid='cityOrSuburb']"), "City / Suburb");
    }

    protected WebElement getAddressInput() {
        return this.findVisibleElemntByXpath("//input[@data-testid='address']");
    }

    protected WebElement getSubmitButton() {
        return assertClickableElementExists(By.xpath("//button[@data-testid='submit']"), "Submit button");
    }

    public void setSuburb(String suburb) {
        this.setInputValue(getSuburbInput(), suburb);
        LogUtils.info("ResidentialAddressPage: Set suburb to :" + suburb);
    }

    public void setAddress(String streetnum, String streetname) {
        this.setInputValue(getAddressInput(),streetnum + " " + streetname);
        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + " " + streetname);
    }

    public void submit() {
        waitLoading();
        WebElement e = this.getSubmitButton();
        triggerClickEvent(e);
        waitButtonLoader();
        waitLoader();
    }

}
