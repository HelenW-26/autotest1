package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;

public class CPInternationBankCPSWithdraw extends CPInternationalBankWithdraw {
	protected InternalBankWithdrawPage page;
	
	public CPInternationBankCPSWithdraw(InternalBankWithdrawPage v_page) {
		super(v_page);
		this.page = v_page;
	}
	public CPInternationBankCPSWithdraw(WebDriver driver) {
		super(new InternalBankWithdrawPage(driver));
		this.page = new InternalBankWithdrawPage(driver);
	}
	
	@Override
	public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban, 
			String holderaddress,String swiftcode,String sortcode, String notes) {
		/*if(!page.chooseAddBankAccount()) {
			return false;
		}
		if(!page.setRegionAsInternal()) {
			return false;
		}*/
		page.setCPSRecipientType();
		page.setCPSBankName(bank_name);
		page.setCPSBankAdress(address);
		page.setCPSBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setCPSHolderAddress(holderaddress);
		page.setSwift(swiftcode);
		page.setCPSSortCode(sortcode);
		page.setNotes(notes);
		page.uploadStatement();
		return true;
	}
}
