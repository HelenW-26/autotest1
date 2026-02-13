package newcrm.business.pugbusiness.ib;

import newcrm.business.businessbase.ibbase.IBTransactionHistory;
import newcrm.pages.pugclientpages.ib.PUIBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class PUIBTransactionHistory extends IBTransactionHistory {

    public PUIBTransactionHistory(WebDriver driver) {
        super(new PUIBTransactionHistoryPage(driver));
        super.driver = driver;
    }

}
