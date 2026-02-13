package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.auclientpages.AuDepositFundsPage;

public class AuCPDepositFunds extends CPDepositFunds {
	public AuCPDepositFunds(WebDriver driver) {
		super(new AuDepositFundsPage(driver));
		
	}

}
