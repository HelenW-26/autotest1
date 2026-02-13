package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPAstropay;
import newcrm.pages.clientpages.deposit.AstropayPage;

public class AuCPAstropay extends CPAstropay {

	public AuCPAstropay(WebDriver driver) {
		super(new AstropayPage(driver));
	}
}
