package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.ibpages.IBFXIRWithdrawPage;

public class IBFXIRWithdraw extends IBCPSEwalletWithdraw{

	protected IBFXIRWithdrawPage fxirpage;
	
	public IBFXIRWithdraw(WebDriver driver) {
		super(new IBFXIRWithdrawPage(driver));
		this.fxirpage = new IBFXIRWithdrawPage(driver);
	}
	
	public IBFXIRWithdraw(IBFXIRWithdrawPage v_fxirpage) {
		super(v_fxirpage);
		this.fxirpage = v_fxirpage;
	}
	
	@Override
	public void setWithdrawAccount(String email) {
		fxirpage.setAccountName(email);
		fxirpage.setAccountNumber(email);
		fxirpage.setFXIRSenderID(email);
		setNote(email);
	}

}
