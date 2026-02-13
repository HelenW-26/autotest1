package newcrm.pages.clientpages.deposit;

import com.google.common.base.Verify;
import junit.framework.Assert;
import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Set;

public class CreditCardGooglePayPage extends CreditCardDepositPage {

    public CreditCardGooglePayPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean checkIframeExists() {
        this.waitLoading();
        String expectedMessage = "Click the button above to go to the payment page";
        WebElement actualMessage = driver.findElement(By.xpath("//div[@class='form_deposit']//p[@class='hint']//span[2]"));
        try {
            Assert.assertEquals("GooglePay Payment Page's message not found!", expectedMessage, actualMessage.getText());
        }catch(Exception e) {
            System.out.println("Navigated CC-GooglePay's Payment page failed");
            return false;
        }
        GlobalMethods.printDebugInfo("Navigated to CC-GooglePay's Payment page successful");
        return true;
    }
}
