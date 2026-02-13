package newcrm.business.pugbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.pages.pugclientpages.PUGTransactionHistoryPage;

public class PUGCPTransactionHistory extends CPTransactionHistory {

	public PUGCPTransactionHistory(WebDriver driver) {
		super(new PUGTransactionHistoryPage(driver));
		super.driver = driver;
	}
}
