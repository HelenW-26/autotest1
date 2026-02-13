package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPInterBankTrans;

import newcrm.pages.auclientpages.AuInterBankTransPage;
public class AuCPInterBankTrans extends CPInterBankTrans {
	
	public AuCPInterBankTrans(WebDriver driver) {

		super(new AuInterBankTransPage(driver));

	}
}
