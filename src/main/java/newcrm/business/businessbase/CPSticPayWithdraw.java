package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.SticPayWithdrawPage;

public class CPSticPayWithdraw extends CPSkrillWithdraw {

	public CPSticPayWithdraw(SticPayWithdrawPage sticwithdraw) {
		super(sticwithdraw);
	}
	
	public CPSticPayWithdraw(WebDriver driver) {
		super(new SticPayWithdrawPage(driver));
	}
}
