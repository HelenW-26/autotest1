package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPCryptoWithdraw;
import newcrm.pages.vjpclientpages.VJPCryptoWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VJPCPCryptoWithdraw extends CPCryptoWithdraw {

	public VJPCPCryptoWithdraw(WebDriver driver) {
		super(new VJPCryptoWithdrawPage(driver));
	}
}
