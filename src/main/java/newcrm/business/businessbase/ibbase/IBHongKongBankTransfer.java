package newcrm.business.businessbase.ibbase;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

public class IBHongKongBankTransfer extends IBLocalBankTransfer{
	
	public IBHongKongBankTransfer(WebDriver driver) {
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
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setNotes(notes);
		return true;
	}

}
