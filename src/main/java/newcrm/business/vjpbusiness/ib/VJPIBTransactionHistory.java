package newcrm.business.vjpbusiness.ib;

import newcrm.business.businessbase.ibbase.IBTransactionHistory;
import newcrm.pages.vjpibpages.VJPIBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class VJPIBTransactionHistory extends IBTransactionHistory {

    public VJPIBTransactionHistory(WebDriver driver) {
        super(new VJPIBTransactionHistoryPage(driver));
        super.driver = driver;
    }

}
