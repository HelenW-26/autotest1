package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPThaiBankTransfer;
import newcrm.pages.auclientpages.AuThaiBankTransPage;

public class AuCPThaiBankTrans extends CPThaiBankTransfer {

	public AuCPThaiBankTrans(WebDriver driver) {

		super(new AuThaiBankTransPage(driver));

	}

}
