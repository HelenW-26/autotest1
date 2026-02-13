package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.ibpages.IBEmailWithdrawPage;

public class IBCPSEwalletWithdraw extends IBEmailWithdraw{

	public IBCPSEwalletWithdraw(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public IBCPSEwalletWithdraw(IBEmailWithdrawPage email_page) {
		super(email_page);
		this.page = email_page;
	}

	public void setWithdrawAccount(String email) {
		email_page.setAccountName(email);
		email_page.setAccountNumber(email);
		setNote(email);	
	}

}
