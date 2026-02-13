package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.PersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class STARPersonalDetailsPage extends PersonalDetailsPage {

    public STARPersonalDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getMobileInput() {
        return this.findClickableElementByXpath("//div[@data-testid='phoneCode']/input");
    }

    @Override
    public void closeImg()
    {
        try {
            driver.findElement(By.xpath("//a[@class='introjs-skipbutton']")).click();

        } catch (Exception e)
        {
            LogUtils.info("no notification dialog display");
        }

        try {
            driver.findElement(By.xpath("//div[@data-testid='notificationDialog']/div/div/button")).click();
        } catch (Exception e)
        {
            LogUtils.info("no notification dialog display");
        }

        try {
            driver.findElement(By.xpath("(//img[@data-testid='closeImg'])[1]")).click();
        } catch (Exception e)
        {
            LogUtils.info("no img display");
        }

        try {
            driver.findElement(By.xpath("(//img[@data-testid='closeImg'])[2]")).click();
        } catch (Exception e)
        {
            LogUtils.info("no img display");
        }
    }

    @Override
    public String setPhone(String phone) {
        WebElement e = this.getMobileInput();
        this.moveElementToVisible(e);
        e.click();
        e.sendKeys(phone);
        LogUtils.info("PersonalDetailsPage: set mobile to: " + phone);
        return phone;
    }

}
