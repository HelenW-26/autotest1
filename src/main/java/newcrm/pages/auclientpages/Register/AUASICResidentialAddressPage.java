package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class AUASICResidentialAddressPage extends ResidentialAddressPage {

    public AUASICResidentialAddressPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getAddressInput() {
        return this.findVisibleElemntByXpath("//input[@id='ma_autocompletep']");
    }

    @Override
    protected WebElement getSuburbInput() {
        return this.findVisibleElemntByXpath("//input[@data-testid='cityOrSuburb']");
    }


    @Override
    public void setState(String state) {
        WebElement e = this.findClickableElemntByTestId("provinceNameEn");
        this.moveElementToVisible(e);
        e.click();
        List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
        state = this.selectRandomValueFromDropDownList(ops);
        LogUtils.info("ResidentialAddressPage: Set State to :" + state);
    }

    @Override
    public void setAddress(String streetnum, String streetname) {
        this.setInputValue(this.getAddressInput(),streetnum + " " + streetname);
        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + " " + streetname);
    }

    @Override
    public void setSuburb(String suburb) {
        this.setInputValue(this.getSuburbInput(), suburb);
        LogUtils.info("ResidentialAddressPage: Set suburb to :" + suburb);
    }

}
