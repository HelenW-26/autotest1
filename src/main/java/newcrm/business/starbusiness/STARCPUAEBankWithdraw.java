package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPUAEBankWithdraw;
import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class STARCPUAEBankWithdraw extends CPUAEBankWithdraw {

	InternalBankWithdrawPage uaeibtpage;
	public STARCPUAEBankWithdraw(WebDriver driver) {
		super(driver);
		this.uaeibtpage = new InternalBankWithdrawPage(driver);
	}

	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
		uaeibtpage.setBankName(bank_branch);
		uaeibtpage.setBankAdress(city);
		uaeibtpage.setBeneficiaryName(acc_name);
		uaeibtpage.setBankAccountNumber(acc_number);
		uaeibtpage.setHolderAddress(province);
		uaeibtpage.setSwift(swift_code);
		uaeibtpage.setSortCode(ifsc);
		uaeibtpage.uploadStatement();
		uaeibtpage.setNotes(notes);
		return true;
	}

}
