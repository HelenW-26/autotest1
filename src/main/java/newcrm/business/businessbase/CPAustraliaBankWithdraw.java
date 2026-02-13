package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.AustraliaBankWithdrawPage;

public class CPAustraliaBankWithdraw extends CPBankTransferWithdraw {

	protected AustraliaBankWithdrawPage page;
	public CPAustraliaBankWithdraw(AustraliaBankWithdrawPage v_page) {
		super(v_page);
		page = v_page;
	}
	
	public CPAustraliaBankWithdraw(WebDriver driver) {
		super(new AustraliaBankWithdrawPage(driver));
		this.page = new AustraliaBankWithdrawPage(driver);
	}
	
	public void setNotes(String note) {
		page.setNotes(note);
	}
	
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
		page.setNotes(notes);
		page.upload();
		return true;
	}
	
	public boolean submit()
	{
		return page.submit();
	}
	
	
	
}
