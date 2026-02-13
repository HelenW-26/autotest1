package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPIndonesiaBankTransfer;
import newcrm.pages.auclientpages.AuIndonesiaBankTransferPage;

public class AuCPIndonesiaBankTransfer extends CPIndonesiaBankTransfer {

	public AuCPIndonesiaBankTransfer(WebDriver driver) {
		super(new AuIndonesiaBankTransferPage(driver));
	}

}
