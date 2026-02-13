package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPSticPayWithdraw;
import newcrm.pages.auclientpages.AuSticPayWithdrawPage;

public class AuCPSticPayWithdraw extends CPSticPayWithdraw {

	public AuCPSticPayWithdraw(WebDriver driver) {
		super(new AuSticPayWithdrawPage(driver));
	}
}
