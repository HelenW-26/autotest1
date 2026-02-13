package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPSkrillWithdraw;
import newcrm.pages.auclientpages.AuSkrillWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;

public class AuCPSkrillWithdraw extends CPSkrillWithdraw {

	public AuCPSkrillWithdraw(WebDriver driver) {
		super(new AuSkrillWithdrawPage(driver));
		this.not_cc_page = new AuWithdrawPage(driver);
	}

}
