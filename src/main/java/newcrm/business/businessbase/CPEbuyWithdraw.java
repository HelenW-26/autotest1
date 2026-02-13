package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.SkrillWithdrawPage;

public class CPEbuyWithdraw extends CPSkrillWithdraw{

	public CPEbuyWithdraw(WebDriver driver) {
		super(new SkrillWithdrawPage(driver));
		this.skrillpage = new SkrillWithdrawPage(driver);
	}
	
	@Override
	public boolean setWithdrawInfoAndSubmit(String email,String notes) {
		skrillpage.setNotes(notes);
		return skrillpage.submit();
	}
	
	@Override
	public void setWithdrawInfo(String email,String notes) {
		skrillpage.setNotes(notes);
	}

}
