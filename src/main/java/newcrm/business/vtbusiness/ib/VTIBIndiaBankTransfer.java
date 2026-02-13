package newcrm.business.vtbusiness.ib;

import newcrm.business.businessbase.ibbase.IBIndiaBankTransfer;
import org.openqa.selenium.WebDriver;

public class VTIBIndiaBankTransfer extends IBIndiaBankTransfer {

	public VTIBIndiaBankTransfer(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province,
									 String ifsc, String notes, String accdigit, String docid, String swift_code,
									 String docType, String accType, String bankName) {
		if(!page.chooseAddBankAccount()) {
			return false;
		}
		page.setBankName();
		page.setBankBranch(bank_branch);
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setBankIFSC(ifsc);
		page.setNotes(notes);
		return true;
	}
}
