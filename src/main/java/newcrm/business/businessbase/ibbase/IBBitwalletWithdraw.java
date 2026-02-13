package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.ibpages.IBBitwalletWithdrawPage;

public class IBBitwalletWithdraw extends IBEmailWithdraw {

	public IBBitwalletWithdraw(WebDriver driver) {
		super(driver);
		this.email_page = new IBBitwalletWithdrawPage(driver);
	}
}
