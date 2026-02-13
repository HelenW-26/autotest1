package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.FXIRWithdrawPage;

public class CPFXIRWithdraw extends CPWithdraw {

	protected FXIRWithdrawPage fxirpage;

	public CPFXIRWithdraw(WebDriver driver) {
		super(new FXIRWithdrawPage(driver));
		this.fxirpage = new FXIRWithdrawPage(driver);
	}
	
	public CPFXIRWithdraw(FXIRWithdrawPage v_fxirpage) {
		super(v_fxirpage);
		this.fxirpage = v_fxirpage;
	}

	public void setWithdrawInfo(String email,String notes) {
		fxirpage.setFXIRSenderID(email);
		fxirpage.setAccountName(email);
		fxirpage.setAccountNumber(email);
		fxirpage.setNotes(notes);
	}

	public boolean setWithdrawInfoAndSubmit(String email,String notes) {
		fxirpage.setFXIRSenderID(email);
		fxirpage.setAccountName(email);
		fxirpage.setAccountNumber(email);
		fxirpage.setNotes(notes);
		return fxirpage.submit();
	}

	public void submitWithoutCheck() {
		fxirpage.submitWithoutCheck();
	}

	public boolean setCodeSubmit() {
		return fxirpage.codeConfirm();
	}

}
