package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.ibpages.IBEmailWithdrawPage;

public class IBEmailWithdraw extends IBWithDrawBase {
	protected IBEmailWithdrawPage email_page;
	
	public IBEmailWithdraw(IBEmailWithdrawPage v_page) {
		super(v_page);
		this.page = v_page;
	}
	
	public IBEmailWithdraw(WebDriver driver) {
		super(driver);
		email_page = new IBEmailWithdrawPage(driver);
		this.page = email_page;
	}
	
	public void setWithdrawAccount(String account) {
		email_page.setWithdrawAccount(account);
	}
}
