package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPAustraliaBankWithdraw;
import newcrm.pages.auclientpages.AuAustraliaBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;

public class AuCPAustraliaBankWithdraw extends CPAustraliaBankWithdraw {

	public AuCPAustraliaBankWithdraw(WebDriver driver) {
		super(new AuAustraliaBankWithdrawPage(driver));
		this.not_cc_page = new AuWithdrawPage(driver);
	}

}
