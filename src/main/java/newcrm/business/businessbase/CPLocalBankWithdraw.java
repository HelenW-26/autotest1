package newcrm.business.businessbase;

import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class CPLocalBankWithdraw extends CPBankTransferWithdraw {

	protected LocalBankWithdrawPage page;
	public CPLocalBankWithdraw(LocalBankWithdrawPage v_page) {
		super(v_page);
		this.page = v_page;
	}

	public CPLocalBankWithdraw(WebDriver driver) {
		this(new LocalBankWithdrawPage(driver));
	}
	
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		page.setBankName();
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setImportantNotes(notes);
		return true;
	}
	
	public boolean submit() {
		return page.submit();
	}

	public void setNotes(String notes) {
		page.setImportantNotes(notes);
	}

	public boolean checkSavedAcctDropdown() {return page.checkSavedAcctDropdown();}

	public boolean chooseAddNewLBTAccount() {
		return page.chooseAddBankAccount();
	}

	public String getWithdrawalDetails() {
		return page.getWithdrawalDetails();
	}
}
