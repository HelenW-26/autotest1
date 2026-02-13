package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPCryptoWithdraw;
import newcrm.pages.moclientpages.MOCryptoWithdrawPage;
import org.openqa.selenium.WebDriver;

public class MOCPCryptoWithdraw extends CPCryptoWithdraw {

	public MOCPCryptoWithdraw(WebDriver driver) {
		super(new MOCryptoWithdrawPage(driver));
	}
}
