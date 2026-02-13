package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.SticPayPage;

/**
 * This class is inherit from CPSkrillpay,so use the skrillpaypay variable。
 * @author CRM QA team 05-10-2021
 *
 */

public class CPSticPay extends CPSkrillPay {
	public CPSticPay(SticPayPage sticpaypage) {
		super(sticpaypage);
	}

	public CPSticPay(WebDriver driver) {
		super(new SticPayPage(driver));
	}
	
	@Override
	public boolean checkIfNavigateToThirdUrl(String amount) {
		if(! skrillpaypage.checkUrlContains(GlobalProperties.STICPAYURL)) {
			return false;
		}else {
			return true;
		}
	}
	
}
