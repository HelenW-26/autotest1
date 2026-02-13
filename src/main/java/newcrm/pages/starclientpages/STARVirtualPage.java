package newcrm.pages.starclientpages;

import newcrm.pages.clientpages.deposit.VirtualPayPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class STARVirtualPage extends VirtualPayPage {

    public STARVirtualPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void setCreditCardCity(String city) {
        WebElement ele = findVisibleElemntBy(By.xpath("//input[@data-testid='city']"));
        this.moveElementToVisible(ele);
        ele.sendKeys(city);
    }

    @Override
    public void setCreditCardAddress(String address) {
        WebElement ele = findVisibleElemntBy(By.xpath("//input[@data-testid='stateCode']"));
        this.moveElementToVisible(ele);
        ele.sendKeys(address);
    }

    @Override
    public void setCreditCardPostalCode(String postalCode) {
        WebElement ele = findVisibleElemntBy(By.xpath("//input[@data-testid='postalCode']"));
        this.moveElementToVisible(ele);
        ele.sendKeys(postalCode);
    }

}
