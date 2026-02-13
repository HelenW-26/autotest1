package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPSkrillPay;
import newcrm.pages.auclientpages.AuSkrillPayPage;

public class AuCPSkrillPay extends CPSkrillPay {
	public AuCPSkrillPay(WebDriver driver) {
		super(new AuSkrillPayPage(driver));
	}
}
