package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class AUConfirmIDPage extends ConfirmIDPage {

    public AUConfirmIDPage(WebDriver driver) {
        super(driver);
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
