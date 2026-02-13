package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPMalaysiaBankTrans;
import newcrm.pages.auclientpages.AuMalaysiaBankTransPage;

public class AuCPMalaysiaBankTrans extends CPMalaysiaBankTrans {

	public AuCPMalaysiaBankTrans(WebDriver driver) {
		super(new AuMalaysiaBankTransPage(driver));
	}

}
