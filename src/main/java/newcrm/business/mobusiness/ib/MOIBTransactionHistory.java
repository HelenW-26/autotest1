package newcrm.business.mobusiness.ib;

import newcrm.business.businessbase.ibbase.IBTransactionHistory;
import newcrm.pages.moclientpages.ib.MOIBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class MOIBTransactionHistory extends IBTransactionHistory {

    public MOIBTransactionHistory(WebDriver driver) {
        super(new MOIBTransactionHistoryPage(driver));
        super.driver = driver;
    }

}
