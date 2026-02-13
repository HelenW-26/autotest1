package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class PRODVJPPersonalDetailsPage extends VJPPersonalDetailsPage{

    public PRODVJPPersonalDetailsPage(WebDriver driver)
    {
        super(driver);
    }

    public void clickOpenAccountVerifyBtn() {
        WebElement e = assertElementExists(By.xpath("(//div[@class='profilePage']//button[@data-testid='button'])[1]"), "Verify Now button");
        triggerElementClickEvent(e);
        LogUtils.info("Open Account Opening Verification Dialog");
    }

}
