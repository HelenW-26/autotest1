package newcrm.business.vtbusiness;

import java.util.List;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.ibbase.IBEmailWithdraw;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.WithdrawPage;

public class VTIBWithdraw extends IBEmailWithdraw{
	protected WithdrawPage not_cc_page;
	public VTIBWithdraw(WebDriver driver) {
		super(driver);
		not_cc_page = new WithdrawPage(driver);
	}

	@Override
	public boolean setWithdrawMethod(DEPOSITMETHOD method) {
		String result = not_cc_page.setWithdrawMethod(method);
		not_cc_page.waitLoading();
		if(result == null) {
			return false;
		}
		
		System.out.println("Set Withdraw method : " + method.getWithdrawName());
		return true;
	}
}
