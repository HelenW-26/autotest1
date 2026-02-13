package newcrm.business.pugbusiness.ib;

import newcrm.business.businessbase.ibbase.IBInternationalBankTransfer;
import newcrm.pages.pugclientpages.ib.PUGIBInternationalBankTransferWithdrawPage;
import org.openqa.selenium.WebDriver;

public class PUGIBInternationalBankTransfer extends IBInternationalBankTransfer {
	protected PUGIBInternationalBankTransferWithdrawPage page;
	public PUGIBInternationalBankTransfer(WebDriver driver) {
		this(new PUGIBInternationalBankTransferWithdrawPage(driver) );
	}

	public PUGIBInternationalBankTransfer(PUGIBInternationalBankTransferWithdrawPage page) {
		super(page);
		this.page = page;
	}

	@Override
	public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban, String holderaddress,String swiftcode,String sortcode, String notes){
		if(!page.chooseAddBankAccount()) {
			return false;
		}
		if(!page.setRegionAsInternal()) {
			return false;
		}
		page.setBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setHolderAddress(holderaddress);
		page.uploadStatement();
		page.setSwift(swiftcode);
		page.setSortCode(sortcode);
		page.setNotes(notes);
		return true;
	}
}
