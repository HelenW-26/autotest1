package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPInternationalBankWithdraw;
import newcrm.pages.auclientpages.AuInternalBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;

public class AuCPInternationalBankWithdraw extends CPInternationalBankWithdraw {

	public AuCPInternationalBankWithdraw(WebDriver driver) {
		super(new AuInternalBankWithdrawPage(driver));
		this.not_cc_page = new AuWithdrawPage(driver);
	}

	public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban,
								   String holderaddress,String swiftcode,String sortcode, String notes) {
		/*if(!page.chooseAddBankAccount()) {
			return false;
		}*/
//		if(!page.setRegionAsInternal()) {
//			return false;
//		}
		page.setBankName(bank_name);
		page.setBankAdress(address);
//		page.setBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setHolderAddress(holderaddress);
		page.setSwift(swiftcode);
		page.setSortCode(sortcode);
		page.setNotes(notes);
		page.uploadStatement();
		return true;
	}
}
