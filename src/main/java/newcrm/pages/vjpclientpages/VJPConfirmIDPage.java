package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VJPConfirmIDPage extends ConfirmIDPage {

    public VJPConfirmIDPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.ht-message--error > p, div.el-message.ht-message--warning > p");
    }

    @Override
    protected WebElement getIDTypeInput() {
        return this.findClickableElemntBy(By.xpath("//div[@data-testid='idType']/div/input"));
    }

    @Override
    public void uploadBtn() {
        WebElement uBtn = driver.findElement(By.xpath("//button[@data-testid='uploadBtn']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",uBtn);
        waitLoading();
        LogUtils.info("Click upload button");
    }

}
