package newcrm.business.starbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPEbuyWithdraw;
import newcrm.pages.starclientpages.STARSkrillWithdrawPage;

public class STARCPSkrillWithdraw extends CPEbuyWithdraw{

	public STARCPSkrillWithdraw(WebDriver driver) {
		super(driver);
		this.skrillpage = new STARSkrillWithdrawPage(driver);
	}
	
	@Override
	public boolean setWithdrawInfoAndSubmit(String email,String notes) {
		skrillpage.setAccountName(email);
		skrillpage.setAccountNumber(email);
		skrillpage.setNotes(notes);
		return skrillpage.submit();
	}

}
