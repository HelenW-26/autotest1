package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPIndiaBankWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPIndiaBankWithdraw extends CPIndiaBankWithdraw{

	public VTCPIndiaBankWithdraw(WebDriver driver) {
		super(driver);
		this.not_cc_page = new VTWithdrawPage(driver);
		this.page = new VTLocalBankWithdrawPage(driver);
		this.withdrawpage = new VTWithdrawBasePage(driver);
	}
	
	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		page.setBankName();
		page.setBankBranch(bank_branch);
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setBankIFSC(ifsc);
		page.setImportantNotes(notes);

		return true;
	}

}
