package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.PersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VJPPersonalDetailsPage extends PersonalDetailsPage {
    public VJPPersonalDetailsPage(WebDriver driver)
    {
        super(driver);
    }

    protected WebElement getDayInput() {
        return this.findClickableElementByXpath("//div[@data-testid='dob']/div/input");
    }

    protected WebElement getMonthInput() {
        return this.findClickableElementByXpath("//div[@data-testid='month']/div/input");
    }

    protected WebElement getYearInput() {
        return this.findClickableElementByXpath("//div[@data-testid='year']/div/input");
    }

    protected WebElement getMobileInput() {
        return this.findClickableElementByXpath("//div[@data-testid='phoneCode']/input");
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

    @Override
    public void closeToolSkipButton()
    {
        try {
            driver.findElement(By.xpath("//a[@class='introjs-skipbutton'] | //div[@data-testid='notificationDialog']//button[@class='el-dialog__headerbtn']")).click();
            waitLoading();
        }
        catch (Exception e)
        {
            LogUtils.info("no tools skip popup");
        }
    }
}
