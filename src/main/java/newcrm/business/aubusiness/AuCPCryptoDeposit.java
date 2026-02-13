package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.pages.auclientpages.AuCryptoDepositPage;

public class AuCPCryptoDeposit extends CPCryptoDeposit {

	public AuCPCryptoDeposit(WebDriver driver)
	{
		super(new AuCryptoDepositPage(driver));
	}
}
