package newcrm.business.starbusiness.ib.payment;

import newcrm.business.businessbase.ibbase.IBTransactionHistory;
import newcrm.pages.staribpages.payment.STARIBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class STARIBTransactionHistory extends IBTransactionHistory {

    public STARIBTransactionHistory(WebDriver driver) {
        super(new STARIBTransactionHistoryPage(driver));
        super.driver = driver;
    }

}
