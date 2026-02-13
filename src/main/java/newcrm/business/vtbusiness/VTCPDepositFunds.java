package newcrm.business.vtbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.vtclientpages.VTDepositFundsPage;

public class VTCPDepositFunds extends CPDepositFunds {

	public VTCPDepositFunds(WebDriver driver) {
		super(new VTDepositFundsPage(driver));
	}
}
