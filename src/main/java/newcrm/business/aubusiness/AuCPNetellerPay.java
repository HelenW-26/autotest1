package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPNetellerPay;
import newcrm.pages.clientpages.deposit.NetellerPayPage;

public class AuCPNetellerPay extends CPNetellerPay {

	public AuCPNetellerPay(WebDriver driver) {
		super(new NetellerPayPage(driver));
	}
}
