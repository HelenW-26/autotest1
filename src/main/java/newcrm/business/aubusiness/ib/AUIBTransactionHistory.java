package newcrm.business.aubusiness.ib;


import newcrm.business.businessbase.ibbase.IBTransactionHistory;
import newcrm.pages.auibpages.AUIBTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class AUIBTransactionHistory extends IBTransactionHistory {

	public AUIBTransactionHistory(WebDriver driver) {
		super(new AUIBTransactionHistoryPage(driver));
		super.driver = driver;
	}

}
