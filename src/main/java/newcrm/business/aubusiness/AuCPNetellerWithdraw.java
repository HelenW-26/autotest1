package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPNetellerWithdraw;

import newcrm.pages.auclientpages.AuNetellerWithdrawPage;

public class AuCPNetellerWithdraw extends CPNetellerWithdraw {

	public AuCPNetellerWithdraw(WebDriver driver) {
		super(new AuNetellerWithdrawPage(driver));
	}
}
