package newcrm.business.umbusiness.ib;


import newcrm.business.businessbase.ibbase.IBTransactionHistory;
import newcrm.pages.umibpages.UMIBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class UMIBTransactionHistory extends IBTransactionHistory {

	public UMIBTransactionHistory(WebDriver driver) {
		super(new UMIBTransactionHistoryPage(driver));
		super.driver = driver;
	}

}
