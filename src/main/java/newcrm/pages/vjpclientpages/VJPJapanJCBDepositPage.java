package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class VJPJapanJCBDepositPage extends LocalBankTransferDepositPage {
    public VJPJapanJCBDepositPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void submit() {
        WebElement submitBtn = driver.findElement(By.xpath("(//button[@data-testid='submit'])[2]"));
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        javascript.executeScript("arguments[0].click()", submitBtn);
        this.waitLoading();
        GlobalMethods.printDebugInfo("Submit deposit");
    }
}
