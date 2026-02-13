package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPCreditCardWithdraw;
import newcrm.pages.auclientpages.AuCreditCardWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;

public class AuCPCreditCardWithdraw extends CPCreditCardWithdraw {

	public AuCPCreditCardWithdraw(WebDriver driver) {
		super(new AuCreditCardWithdrawPage(driver));
		this.not_cc_page = new AuWithdrawPage(driver);
	}


}
