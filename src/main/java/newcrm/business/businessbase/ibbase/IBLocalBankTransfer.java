package newcrm.business.businessbase.ibbase;

import newcrm.business.businessbase.CPBankTransferWithdraw;
import newcrm.pages.ibpages.IBInternationalBankTransferWithdrawPage;
import org.openqa.selenium.WebDriver;


public class IBLocalBankTransfer extends CPBankTransferWithdraw {
	protected IBInternationalBankTransferWithdrawPage page;
	public IBLocalBankTransfer(IBInternationalBankTransferWithdrawPage localbank_page) {
		super(localbank_page);
		this.page = localbank_page;
	}

	public IBLocalBankTransfer(WebDriver driver) {
		this(new IBInternationalBankTransferWithdrawPage(driver));
	}

	public boolean addNewBankAccount(String branch,String accName, String accNumber, String city, String province,
									 String ifsc, String notes, String accdigit, String docid, String swift_code,
									 String docType, String accType, String bankName) {

		page.setBankName();
		page.setBankBranch(branch);
		page.setBankAccountName(accName);
		page.setBankAccountNumber(accNumber);
		page.setNotes(notes);
		return true;
	}

}
