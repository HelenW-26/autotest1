package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;

public class CPInternationalBankWithdraw extends CPBankTransferWithdraw {
	protected InternalBankWithdrawPage page;
	public CPInternationalBankWithdraw(InternalBankWithdrawPage v_page) {
		super(v_page);
		this.page = v_page;
	}
	public CPInternationalBankWithdraw(WebDriver driver) {
		super(new InternalBankWithdrawPage(driver));
		this.page = new InternalBankWithdrawPage(driver);
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
		page.setBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setHolderAddress(holderaddress);
		page.setSwift(swiftcode);
		page.setSortCode(sortcode);
		page.setBsbCode(sortcode);
		page.setNotes(notes);
		page.uploadStatement();
		return true;
	}
	
	public boolean submit()
	{
		return page.submit();
	}
	
	public void setNotes(String notes) {
		page.setNotes(notes);
	}
	public void setSortCode(String code) {
		page.setSortCode(code);
	}
	
	//Wait for the file uploading completed 
	public void checkFileUploadingCompleted(){
		page.findVisibleElemntByCss("i.el-icon-upload-success.el-icon-circle-check");
	}

	public boolean checkSavedAcctDropdown() {return page.checkSavedAcctDropdown();}

	public boolean chooseAddNewIBTAccount() {
		return page.chooseAddNewAccount();
	}
}
