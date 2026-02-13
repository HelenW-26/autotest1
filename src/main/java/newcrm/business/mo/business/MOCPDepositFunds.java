package newcrm.business.mo.business;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.moclientpages.MODepositFundsPage;

public class MOCPDepositFunds extends CPDepositFunds {

	public MOCPDepositFunds(WebDriver driver) {
		super(new MODepositFundsPage(driver));
	}
}
