package newcrm.business.businessbase;


import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.SkrillPayPage;

public class CPSkrillPay extends DepositBase {
	protected SkrillPayPage skrillpaypage;

	public CPSkrillPay(SkrillPayPage skrillpaypage) {
		super(skrillpaypage);
		this.skrillpaypage = skrillpaypage;
	}
	
	public CPSkrillPay(WebDriver driver) {
		super(new SkrillPayPage(driver));
		this.skrillpaypage = new SkrillPayPage(driver);
	}
	
	public void setEmail(String email) {
		skrillpaypage.setEmail(email);
	}

	public void deposit(String account, String amount, String email, String notes) {
		selectAccount(account);
		setAmount(amount);
		//setEmail(email);
		setNotes(notes);
		submit();
	}

	public boolean checkIfNavigateToThirdUrl(String amount) {
		/*
		String web_amount = skrillpaypage.getDepoitAmountFromSkrill();
		if (web_amount == null) {
			return false;
		}
		*/
		if (!skrillpaypage.checkUrlContains(GlobalProperties.SKRILLURL)) {
			return false;
		}
		return true;
		
		//skrill page does not have amount.2021-12-22
		//return Double.valueOf(web_amount).equals(Double.valueOf(amount));
	}
}
