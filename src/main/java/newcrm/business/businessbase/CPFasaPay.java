package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.FasaPayPage;

public class CPFasaPay extends DepositBase {
	
	protected FasaPayPage fasapaypage;
	
	public CPFasaPay(FasaPayPage fasapaypage) {
		super(fasapaypage);
		this.fasapaypage = fasapaypage;
	}
	
	public CPFasaPay(WebDriver driver) {
		super(new FasaPayPage(driver));
		this.fasapaypage = new FasaPayPage(driver);
		
	}
	
	public void deposit(String account,String amount,String notes) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
		submit();
	}
	
	public boolean checkIfNavigateToThirdUrl(String amount) {
		String web_amount = fasapaypage.getDepoitAmountFromFasaPay();
		if(web_amount == null) {
			return false;
		}
		if(! fasapaypage.checkUrlContains(GlobalProperties.FASAPAYURL)) {
			return false;
		}
		else {
			return true;
		}
	}
	
}
