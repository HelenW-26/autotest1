package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.pages.umclientpages.UMTransactionHistoryPage;
import org.openqa.selenium.WebDriver;

public class UMCPTransactionHistory extends CPTransactionHistory {
	public UMCPTransactionHistory(WebDriver driver) {
		super(new UMTransactionHistoryPage(driver));
		super.driver = driver;
	}
}
