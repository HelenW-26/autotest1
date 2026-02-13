package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;


import newcrm.business.businessbase.CPBankTransferWithdraw;
import newcrm.pages.ibpages.IBInternationalBankTransferWithdrawPage;

public class IBInternationalBankTransfer extends CPBankTransferWithdraw {
	protected IBInternationalBankTransferWithdrawPage page;

	public IBInternationalBankTransfer(WebDriver driver) {
		this(new IBInternationalBankTransferWithdrawPage(driver));
	}

	public IBInternationalBankTransfer(IBInternationalBankTransferWithdrawPage ibtpage) {
		super(ibtpage);
		this.page = ibtpage;
	}

	public boolean setWithdrawInfo(String bank_name, String address, String bbName, String iban, String holderaddress, String swiftcode, String sortcode, String notes) {
		if(!page.chooseAddBankAccount()) {
			return false;
		}
		if(!page.setRegionAsInternal()) {
			return false;
		}
		page.setBankName(bank_name);
		page.setBankAdress(address);
		page.setBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setHolderAddress(holderaddress);
		page.setSwift(swiftcode);
		page.setSortCode(sortcode);
		page.setNotes(notes);
		page.uploadStatement();
//		this.checkFileUploadingCompleted();
		return true;
	}
	
	public boolean submit()
	{
		return page.submit();
	}

	public void setNotes(String notes)
	{
        page.setNotes(notes);
    }

	public void checkFileUploadingCompleted() {}

}
