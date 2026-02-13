package newcrm.business.businessbase;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.WithdrawPage;
import utils.LogUtils;

public abstract class CPWithdraw extends CPWithdrawBase {
	
	protected WithdrawPage not_cc_page;
	public CPWithdraw(WithdrawPage page) {
		super(page);
		this.not_cc_page = page;
	}
	
	public boolean setWithdrawMethod(DEPOSITMETHOD method) {
		String result = not_cc_page.setWithdrawMethod(method);
		if(result == null) {
			return false;
		}
		
		System.out.println("Set Withdraw method : " + method.getWithdrawName());
		return true;
	}

	public boolean setWithdrawMethodNew(DEPOSITMETHOD method) {
		String result = not_cc_page.setWithdrawMethodNew(method);
		if(result == null) {
			return false;
		}

		LogUtils.info("Set Withdraw method : " + method.getWithdrawName());
		return true;
	}

	public void handleICBC() {
		not_cc_page.checkICBCPopup();
		not_cc_page.waitLoading();
	}
	/**
	 *检查出金页面2FA验证提示是否展示
	 */
	public String  checkWithdrawal2FANotice() {
		LogUtils.info("Check Withdrawal 2FA notice");
		return not_cc_page.checkWithdrawal2FANotice();
	}
	
}
