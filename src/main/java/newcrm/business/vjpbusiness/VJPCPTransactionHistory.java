package newcrm.business.vjpbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.pages.vjpclientpages.VJPTransactionHistoryPage;

public class VJPCPTransactionHistory extends CPTransactionHistory {
	public VJPCPTransactionHistory(WebDriver driver) {
		super(new VJPTransactionHistoryPage(driver));
		super.driver = driver;
	}
}
