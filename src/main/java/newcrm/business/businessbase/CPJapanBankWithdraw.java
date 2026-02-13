package newcrm.business.businessbase;

import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class CPJapanBankWithdraw extends CPLocalBankWithdraw {
	
	public CPJapanBankWithdraw(WebDriver driver) {
		super(driver);
	}

	public CPJapanBankWithdraw(LocalBankWithdrawPage v_page) {
		super(v_page);
	}

	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		page.setBankName();
		page.setBankBranch(bank_branch);
		page.setAccountType();
		page.setBankAccountName("ファックユー");
		page.setBankAccountNumber(acc_number);
		page.setImportantNotes(notes);
		return true;
	}

}
