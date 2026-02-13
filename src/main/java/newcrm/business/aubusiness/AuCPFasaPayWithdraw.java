package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPFasaPayWithdraw;
import newcrm.pages.auclientpages.AuFasaPayWithdrawPage;

public class AuCPFasaPayWithdraw extends CPFasaPayWithdraw {

	public AuCPFasaPayWithdraw(WebDriver driver) {
		super(new AuFasaPayWithdrawPage(driver));
	}

}
