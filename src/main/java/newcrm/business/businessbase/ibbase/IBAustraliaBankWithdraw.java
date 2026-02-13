package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPAustraliaBankWithdraw;
import newcrm.pages.clientpages.withdraw.AustraliaBankWithdrawPage;

public class IBAustraliaBankWithdraw extends CPAustraliaBankWithdraw {

	
	public IBAustraliaBankWithdraw(WebDriver driver) {
		super(new AustraliaBankWithdrawPage(driver));
	}
	
	@Override
	public boolean setWithdrawInfo(String bankName,String BSB, String BBname,
			String swift,String bankAccNum,String notes) {
		if(!page.chooseAddBankAccount()) {
			return false;
		}
		if(!page.setRegionAsAustralia()) {
			return false;
		}
		
		page.setBankName(bankName);
		page.setBSB(BSB);
		page.setSwift(swift);
		page.setBankAccNum(bankAccNum);
		page.setBeneficiaryName(BBname);
		page.upload();
		page.setNotes(notes);
		return true;
   }
}
