package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;


public class ProdMORegisterEntryPage extends MoRegisterEntryPage {
    public ProdMORegisterEntryPage(WebDriver driver) {
        super(driver);
    }
    @Override
    public boolean setCountry(String country) {
        this.getCountryInput().click();
        driver.findElement(By.xpath("//div[@data-conutry='"+country+"']")).click();
        LogUtils.info("Set country as: " + country);
        return true;
    }

    @Override
    protected WebElement getSubmitButton() {
        return this.findClickableElemntBy(By.xpath("//button[@id='button']"));
    }
    @Override
    public void checkNonUSResident()
    {
        WebElement checkbox = driver.findElement(By.xpath("//input[@name='NotUs']"));
        if (checkbox.isSelected()) {
            System.out.println("The checkbox is checked.");
        } else {
            js.executeScript("arguments[0].click()", checkbox);
            System.out.println("The checkbox is not checked.");
        }

        WebElement termsCheckbox = driver.findElement(By.xpath("//input[@name='disclaimer']"));

        if (termsCheckbox.isSelected()) {
            System.out.println("The checkbox is checked.");
        } else {
            js.executeScript("arguments[0].click()", termsCheckbox);
            System.out.println("The checkbox is not checked.");
        }
        //checkbox.click();

    }
}
