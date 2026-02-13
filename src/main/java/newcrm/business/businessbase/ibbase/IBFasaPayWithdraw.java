package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.ibpages.IBFasaPayWithdrawPage;

public class IBFasaPayWithdraw extends IBEmailWithdraw {

	public IBFasaPayWithdraw(WebDriver driver) {
		super(driver);
		this.email_page = new IBFasaPayWithdrawPage(driver);
	}
	
	public void setAccountName(String accName) {
		((IBFasaPayWithdrawPage)this.email_page).setAccountName(accName);
	}
	
}
