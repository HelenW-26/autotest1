package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

public class CPPeruBankWithdraw extends CPLocalBankWithdraw {
	
	public CPPeruBankWithdraw(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		page.setBankName();
		page.setAccountType();
		page.setDocumentType();
		page.setAccDigit(accdigit);
		page.setBankRegion();
		page.setDocId(docid);
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setImportantNotes(notes);
		return true;
	}

}
