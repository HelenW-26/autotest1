package newcrm.pages.moclientpages.ib;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBTransactionHistoryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MOIBTransactionHistoryPage extends IBTransactionHistoryPage {

    public MOIBTransactionHistoryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void clickWithdraw() {
        WebElement e = assertClickableElementExists(By.xpath("//div[@id='tab-withdrawHistory']"), "Withdrawal Tab");
        triggerElementClickEvent_withoutMoveElement(e);
        GlobalMethods.printDebugInfo("Click Withdrawal Tab");
    }

}
