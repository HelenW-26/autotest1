package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.deposit.IndonesiaBankTransferPage;

public class CPIndonesiaBankDeposit extends CPLocalBankTrans{
	
	protected IndonesiaBankTransferPage idbtpage;
	
	public CPIndonesiaBankDeposit(WebDriver driver) {
		super(driver);
		this.idbtpage = new IndonesiaBankTransferPage(driver);
	}
	
	public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
		idbtpage.selectAccount(account);
		idbtpage.setAmount(amount);
		idbtpage.setRandomBankName();
		idbtpage.setNotes(notes);
	}
}
