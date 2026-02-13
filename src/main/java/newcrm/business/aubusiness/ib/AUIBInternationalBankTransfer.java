package newcrm.business.aubusiness.ib;

import newcrm.business.businessbase.ibbase.IBInternationalBankTransfer;
import org.openqa.selenium.WebDriver;

public class AUIBInternationalBankTransfer extends IBInternationalBankTransfer {
	public AUIBInternationalBankTransfer(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban,
									  String holderaddress,String swiftcode,String sortcode, String notes){
		if(!page.chooseAddBankAccount()) {
			return false;
		}
		page.setCPSBankName(bank_name);
		page.setCPSBankAdress(address);
		page.setCPSBeneficiaryName(bbName);
		page.setBankAccountNumber(iban);
		page.setCPSHolderAddress(holderaddress);
		page.uploadStatement();
		page.setSwift(swiftcode);
		page.setCPSSortCode(sortcode);
		page.setNotes(notes);
		return true;
	}
}
