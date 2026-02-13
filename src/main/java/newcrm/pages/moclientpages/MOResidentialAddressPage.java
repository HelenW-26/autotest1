package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MOResidentialAddressPage extends ResidentialAddressPage {

    public MOResidentialAddressPage(WebDriver driver)
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

}
