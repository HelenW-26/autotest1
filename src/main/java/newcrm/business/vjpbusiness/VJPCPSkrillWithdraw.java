package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPSkrillWithdraw;
import newcrm.pages.vjpclientpages.VJPSkrillWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VJPCPSkrillWithdraw extends CPSkrillWithdraw {

	public VJPCPSkrillWithdraw(WebDriver driver) {
		super(new VJPSkrillWithdrawPage(driver));
	}

	@Override
	public boolean setWithdrawInfoAndSubmit(String email,String notes) {
		skrillpage.setAccountName(email);
		skrillpage.setAccountNumber(email);
		skrillpage.setNotes(notes);
		return skrillpage.submit();
	}
}
