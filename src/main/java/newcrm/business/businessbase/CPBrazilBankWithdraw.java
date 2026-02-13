package newcrm.business.businessbase;

import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class CPBrazilBankWithdraw extends CPLocalBankWithdraw {
	
	public CPBrazilBankWithdraw(WebDriver driver) {
		super(driver);
	}

	public CPBrazilBankWithdraw(LocalBankWithdrawPage v_page) {
		super(v_page);
	}

	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		page.setBankName();
		page.setAccountType();
		page.setDocumentType();
		page.setAccDigit(accdigit);
		page.setDocId(docid);
		page.setBankBranch(bank_branch);
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setImportantNotes(notes);
		return true;
	}

}
