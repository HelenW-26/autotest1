package newcrm.business.businessbase.deposit;


import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.DepositBase;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;

public class CPLocalBankTrans extends DepositBase {
	
	protected LocalBankTransferDepositPage page;
	public CPLocalBankTrans(WebDriver driver) {
		this(new LocalBankTransferDepositPage(driver));
	}

	public CPLocalBankTrans(LocalBankTransferDepositPage v_page) {
		super(v_page);
		this.page = v_page;
	}

	public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
	}
}
