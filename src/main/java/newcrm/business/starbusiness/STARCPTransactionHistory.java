package newcrm.business.starbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.pages.starclientpages.STARTransactionHistoryPage;

public class STARCPTransactionHistory extends CPTransactionHistory {

	public STARCPTransactionHistory(WebDriver driver) {
		super(new STARTransactionHistoryPage(driver));
		super.driver = driver;
	}

}
