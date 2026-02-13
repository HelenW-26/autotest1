package newcrm.business.mo.business;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.pages.moclientpages.MOTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class MOCPTransactionHistory extends CPTransactionHistory {
    public MOCPTransactionHistory(WebDriver driver)
    {
        super(new MOTransactionHistoryPage(driver));
        super.driver = driver;
    }
}
