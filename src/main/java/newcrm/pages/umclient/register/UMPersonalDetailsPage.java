package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.PersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMPersonalDetailsPage extends PersonalDetailsPage {

    public UMPersonalDetailsPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void createButton()
    {
        try{
            driver.findElement(By.xpath("//div[@class='el-dialog__wrapper sp_dialog']//button[@aria-label='Close']")).click();
        }
        catch(Exception e)
        {
            LogUtils.info("no popup window");
        }
        try{
            driver.findElement(By.xpath("//li[@data-testid='menu.register']")).click();
        }
        catch(Exception e)
        {
            LogUtils.info("no need to go to authentication page");
        }
        waitLoading();
        try
        {
        WebElement createBtn = driver.findElement(By.xpath("//button/span[contains(text(),'Verification')]"));
        js.executeScript("arguments[0].click();",createBtn);}
        catch(Exception e)
        {
            LogUtils.info("no need to click verification");
        }
        waitLoading();
    }

    @Override
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

}
