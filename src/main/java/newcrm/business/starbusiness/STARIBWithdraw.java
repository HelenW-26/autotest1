package newcrm.business.starbusiness;

import org.openqa.selenium.WebDriver;
import newcrm.business.businessbase.ibbase.IBEmailWithdraw;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.starclientpages.STARIBWithdrawPage;

public class STARIBWithdraw extends IBEmailWithdraw{
	protected STARIBWithdrawPage not_cc_page;
	public STARIBWithdraw(WebDriver driver) {
		super(driver);
		not_cc_page = new STARIBWithdrawPage(driver);
	}
	
	public boolean setWithdrawMethod(DEPOSITMETHOD method) {
		not_cc_page.setMethod(method);
		return true;
	}
}
