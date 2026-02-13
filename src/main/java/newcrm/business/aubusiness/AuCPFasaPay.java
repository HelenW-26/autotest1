package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPFasaPay;
import newcrm.pages.auclientpages.AuFasaPayPage;

public class AuCPFasaPay extends CPFasaPay {
	
	public AuCPFasaPay(WebDriver driver) {
		super(new AuFasaPayPage(driver));
	}

}
