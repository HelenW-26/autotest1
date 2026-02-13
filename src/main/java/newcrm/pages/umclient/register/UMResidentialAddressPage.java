package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMResidentialAddressPage extends ResidentialAddressPage {

    public UMResidentialAddressPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected WebElement getSuburbInput() {
        return assertElementExists(By.xpath("//input[@data-testid='cityOrSuburb']"), "City / Suburb");
    }

    @Override
    public void setAddress(String streetnum,String streetname) {
        WebElement address = assertElementExists(By.xpath("//input[@data-testid='address']"), "Address");
        address.sendKeys(streetnum + streetname);
        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + streetname);
    }

    @Override
    public void setState(String state) {
        WebElement address = assertElementExists(By.xpath("//input[@data-testid='state']"), "State");
        address.sendKeys(state);
        LogUtils.info("ResidentialAddressPage: Set State to :" + state);
    }

}
