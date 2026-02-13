package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;


public class IBVietnamBankTransfer extends IBLocalBankTransfer{
	public IBVietnamBankTransfer(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean addNewBankAccount(String branch,String accName, String accNumber, String city, String province,
									 String ifsc, String notes, String accdigit, String docid, String swift_code,
									 String docType, String accType, String bankName) {
		if(!page.chooseAddBankAccount()) {
			return false;
		}
		page.setBankName();
		page.setBankIFSC(ifsc);
		page.setBankBranch(branch);
		page.setBankAccountName(accName);
		page.setBankAccountNumber(accNumber);
		page.setNotes(notes);
		return true;
	}

}
