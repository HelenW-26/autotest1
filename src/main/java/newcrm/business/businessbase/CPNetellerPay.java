package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.NetellerPayPage;
import newcrm.pages.clientpages.deposit.SkrillPayPage;

public class CPNetellerPay extends CPSkrillPay {

	public CPNetellerPay(SkrillPayPage skrillpaypage) {
		super(skrillpaypage);
	}
	
	public CPNetellerPay(WebDriver driver) {
		super(new NetellerPayPage(driver));
	}
	
	@Override
	public boolean checkIfNavigateToThirdUrl(String amount) {
		if(! skrillpaypage.checkUrlContains(GlobalProperties.NETELLERURL)) {
			return false;
		}else {
			return true;
		}
	}
}
