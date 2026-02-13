package newcrm.business.pugbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.pugclientpages.PUGDepositFundsPage;

public class PUGCPDepositFunds extends CPDepositFunds {

	public PUGCPDepositFunds(WebDriver driver) {
		super(new PUGDepositFundsPage(driver));
	}
}
