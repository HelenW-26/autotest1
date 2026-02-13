package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.pages.auclientpages.AuTransactionHistoryPage;

public class AuCPTransactionHistory extends CPTransactionHistory {
	public AuCPTransactionHistory(WebDriver driver) {
		super(new AuTransactionHistoryPage(driver));
		super.driver = driver;
	}
}
