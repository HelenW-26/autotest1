package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.FasaPayWithdrawPage;


public class CPFasaPayWithdraw extends CPSkrillWithdraw {
	
	protected FasaPayWithdrawPage fasapaypage;

	public CPFasaPayWithdraw(FasaPayWithdrawPage fasapaywithdraw) {
		super(fasapaywithdraw);
		this.fasapaypage = fasapaywithdraw;
	}
	
	public CPFasaPayWithdraw(WebDriver driver) {
		super(new FasaPayWithdrawPage(driver));
		this.fasapaypage = new FasaPayWithdrawPage(driver);
	}
	
	@Override
	public boolean setWithdrawInfoAndSubmit(String accountInfo,String notes) {
		fasapaypage.setFasaPayAccountName();
		fasapaypage.setFasaPayAccountNNumber(accountInfo);
		fasapaypage.setFasaPayNotes(notes);
		return fasapaypage.submit();
	}

	public void setAccount(String accountInfo,String notes)
	{
		fasapaypage.setFasaPayAccountName();
		fasapaypage.setFasaPayAccountNNumber(accountInfo);
		fasapaypage.setFasaPayNotes(notes);
	}

	public void submit()
	{
		fasapaypage.submit();
	}




}
