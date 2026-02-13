package newcrm.pages.staribpages.payment;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class STARIBTransactionHistoryPage extends IBTransactionHistoryPage {

    public STARIBTransactionHistoryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void clickWithdraw() {
        WebElement e = this.findClickableElementByXpath("//div[@id='tab-withdrawHistory']");
        this.moveElementToVisible(e);
        e.click();
        this.waitLoading();
        GlobalMethods.printDebugInfo("Click Withdrawal Tab");
    }

}
