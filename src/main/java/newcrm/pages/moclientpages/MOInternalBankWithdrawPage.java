package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class MOInternalBankWithdrawPage extends InternalBankWithdrawPage {
    public MOInternalBankWithdrawPage(WebDriver driver)
    {
        super(driver);
    }
    @Override
    protected WebElement getWithdrawMethodElement(GlobalProperties.DEPOSITMETHOD method) {
        WebElement w_method = null;
        String xpath = "//li/span[text()='withdrawmethod']";
        xpath = xpath.replace("withdrawmethod", method.getWithdrawName());
        GlobalMethods.printDebugInfo("getWithdrawMethodElement xpath: " + xpath);
        waitLoadingForCustomise(60);
        try {
            w_method =  driver.findElement(By.xpath(xpath));
        }catch(Exception e) {
            System.out.println("NonCCWithdrawPage: Could not find withdraw methods or you should withdraw through credit card. ");
            return null;
        }
        return w_method;
    }

    public void closeAuthPopOut() {
        try
        {
            WebElement authpop = driver.findElement(By.xpath("//button/span[contains(text( ),'Cancel')]"));
            js.executeScript("arguments[0].click()", authpop);
        }
        catch (Exception e)
        {
            GlobalMethods.printDebugInfo("do not show authenticator popout");
        }
    }

}
