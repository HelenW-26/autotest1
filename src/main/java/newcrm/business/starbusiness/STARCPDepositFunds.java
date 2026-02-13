package newcrm.business.starbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.clientpages.DepositFundsPage;

public class STARCPDepositFunds extends CPDepositFunds {
	public STARCPDepositFunds(WebDriver driver) {
		super(new DepositFundsPage(driver));
		
	}
}
