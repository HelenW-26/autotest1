package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.NetellerWithdrawPage;


/**
 * this class has same business logic as skrill
 * @author CRM QA tea,m
 *
 */
public class CPNetellerWithdraw extends CPSkrillWithdraw {

	public CPNetellerWithdraw(NetellerWithdrawPage v_netellerpage) {
		super(v_netellerpage);
	}
	
	public CPNetellerWithdraw(WebDriver driver) {
		super(new NetellerWithdrawPage(driver));
	}
}
