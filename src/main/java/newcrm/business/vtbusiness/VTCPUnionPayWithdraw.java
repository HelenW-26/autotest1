package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPUnionPayWithdraw;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPUnionPayWithdraw extends CPUnionPayWithdraw {

	public VTCPUnionPayWithdraw(WebDriver driver) {
		super(driver);
		this.not_cc_page = new VTWithdrawPage(driver);
		this.withdrawpage = new VTWithdrawBasePage(driver);
	}

}
