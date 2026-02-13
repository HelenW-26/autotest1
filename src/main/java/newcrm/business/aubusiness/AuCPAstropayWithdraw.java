package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPAstropayWithdraw;
import newcrm.pages.auclientpages.AuAstropayWithdrawPage;

public class AuCPAstropayWithdraw extends CPAstropayWithdraw {

	public AuCPAstropayWithdraw(WebDriver driver) {
		super(new AuAstropayWithdrawPage(driver));
	}
}
