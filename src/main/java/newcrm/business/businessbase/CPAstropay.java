package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.AstropayPage;

/**
 * This class is inherit from CPSkrillpay。
 * @author by Alex L 06-10-2021
 *
 */

public class CPAstropay extends CPSkrillPay {
	protected AstropayPage astropaypage;
	
	public CPAstropay(AstropayPage astropaypage) {
		super(astropaypage);
		this.astropaypage = astropaypage;
	}
	
	public CPAstropay(WebDriver driver) {
		super(new AstropayPage(driver));
		this.astropaypage = new AstropayPage(driver);
	}
	
	@Override
	public boolean checkIfNavigateToThirdUrl(String amount) {
		String web_amount = astropaypage.getDepoitAmountFromAstropay();
		if(web_amount == null) {
			return false;
		}
		if(! skrillpaypage.checkUrlContains(GlobalProperties.ASTROPAYURL)) {
			return false;
		}else {
			return true;
		}
	}
	
	public void deposit(String account,String amount,String notes) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
		submit();
	}

	public void returnToCPFrom3rdParty() {
		//astropaypage.returnToCPFrom3rdParty();
		astropaypage.goBack();
	}
}
