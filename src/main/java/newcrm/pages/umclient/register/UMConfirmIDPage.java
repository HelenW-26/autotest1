package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMConfirmIDPage extends ConfirmIDPage {

    public UMConfirmIDPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected WebElement getIDTypeInput() {
        return this.findClickableElemntBy(By.xpath("//div[@data-testid='idType']/div/input"));
    }

    @Override
    public void next() {
        WebElement e = assertClickableElementExists(By.xpath("//button[@data-testid='uploadBtn']"), "Submit button");
        triggerElementClickEvent(e);
        LogUtils.info("Click Submit button");

        try {
            WebElement popupNotice = driver.findElement(By.xpath("//button[@class='el-button back-btn el-button--text']"));
            js.executeScript("arguments[0].click()",popupNotice);
            this.waitLoading();
        }
        catch(Exception exception)
        {
            LogUtils.info("No pop up notice");
        }
    }

}
