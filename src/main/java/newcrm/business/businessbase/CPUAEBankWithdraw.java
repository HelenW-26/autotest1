package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

public class CPUAEBankWithdraw extends CPLocalBankWithdraw {
	
	public CPUAEBankWithdraw(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		page.setBankName();
		page.setBankAddress(city);
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setBeneficiaryAddress(city);
		page.setImportantNotes(notes);
		return true;
	}

}
