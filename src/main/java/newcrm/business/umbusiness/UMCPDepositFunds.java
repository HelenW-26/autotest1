package newcrm.business.umbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.umclientpages.UMDepositFundsPage;

public class UMCPDepositFunds extends CPDepositFunds {

	public UMCPDepositFunds(WebDriver driver) {
		super(new UMDepositFundsPage(driver));
	}
}
