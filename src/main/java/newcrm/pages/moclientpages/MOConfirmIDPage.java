package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MOConfirmIDPage extends ConfirmIDPage {

    public  MOConfirmIDPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected WebElement getIdentityVerificationContentEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'account_opening_group') and not(contains(@style,'display'))]"), "Identity Verification Content");
    }

    @Override
    protected WebElement getIDTypeInput() {
        return this.findClickableElemntBy(By.xpath("//input[@name='idType']"));
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
