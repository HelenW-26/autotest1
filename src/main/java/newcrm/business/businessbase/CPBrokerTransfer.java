package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.deposit.InterBankTransPage;

public class CPBrokerTransfer extends CPInterBankTrans {
	
	protected CPBrokerTransfer(InterBankTransPage interbanktranspage) {
		super(interbanktranspage);
	}
	
	public CPBrokerTransfer(WebDriver driver) {
		super(new InterBankTransPage(driver));
	}
	
	
	@Override
	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		upload();
		setNotes(notes);
		submit();
	}


}
