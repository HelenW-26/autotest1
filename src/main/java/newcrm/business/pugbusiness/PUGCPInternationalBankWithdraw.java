package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPInternationalBankWithdraw;
import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;

import org.openqa.selenium.WebDriver;

public class PUGCPInternationalBankWithdraw extends CPInternationalBankWithdraw {
    public PUGCPInternationalBankWithdraw(WebDriver driver)
    {
        super(new InternalBankWithdrawPage(driver));
        this.page = new InternalBankWithdrawPage(driver);
    }

    @Override
    public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban, String holderaddress,String swiftcode,String sortcode, String notes) {
		/*if(!page.chooseAddBankAccount()) {
			return false;
		}*/
		if(!page.setRegionAsInternal()) {
			return false;
		}
		page.setBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setHolderAddress(holderaddress);
		page.setSwift(swiftcode);
		page.setSortCode(sortcode);
		page.setNotes(notes);
		page.uploadStatement();
		return true;
	}

}
