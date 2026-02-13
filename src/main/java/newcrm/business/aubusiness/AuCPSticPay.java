package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPSticPay;
import newcrm.pages.auclientpages.AuSticPayPage;

public class AuCPSticPay extends CPSticPay {

	public AuCPSticPay(WebDriver driver) {
		super(new AuSticPayPage(driver));
	}
}
