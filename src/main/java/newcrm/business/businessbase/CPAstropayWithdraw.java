package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.AstropayWithdrawPage;

public class CPAstropayWithdraw extends CPSkrillWithdraw {
	
	protected AstropayWithdrawPage astropaypage;

	public CPAstropayWithdraw(AstropayWithdrawPage astropaywithdraw) {
		super(astropaywithdraw);
		this.astropaypage = astropaywithdraw;
	}
	
	public CPAstropayWithdraw(WebDriver driver) {
		super(new AstropayWithdrawPage(driver));
		this.astropaypage = new AstropayWithdrawPage(driver);
	}
	
	@Override
	public boolean setWithdrawInfoAndSubmit(String accountInfo,String notes) {
		astropaypage.setAstropayAccount(accountInfo);
		skrillpage.setNotes(notes);
		return skrillpage.submit();
	}
}
