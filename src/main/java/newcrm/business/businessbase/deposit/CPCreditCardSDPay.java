package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.CreditCardSDPayPage;

public class CPCreditCardSDPay extends CPCreditCardDeposit {

	protected CreditCardSDPayPage sdpaypage;
	public CPCreditCardSDPay(WebDriver driver) {
		super(new CreditCardSDPayPage(driver));
		this.sdpaypage = new CreditCardSDPayPage(driver);
	}
	
	public CPCreditCardSDPay(CreditCardSDPayPage solidpaypage) {
		super(solidpaypage);
		this.sdpaypage = solidpaypage;
	}
	
	/*public void setCCinfo(String cardNum,String cardName) {
		sdpage.setCCNumber(cardNum);
		sdpage.setCCName(cardName);
	}
	
	public String setExpirationDate() {
		return sdpage.setExpirationDate();
	}
	
	public void setCVV(String cvv) {
		if(cvv.length() > 3) {
			GlobalMethods.printDebugInfo("CC-SDPayPage * WARNING!!! cvv is longer than 3");
		}
		
		sdpage.setCVV(cvv);	
	}
	
	public boolean checkIfNavigateToThirdUrl() {
		if (!sdpaypage.checkUrlContains(GlobalProperties.SDPayURL)) {
			return false;
		} else {
			return true;
		}
	}*/
	
	public void ccdeposit(String account, String amount, String notes, String ccNum, String ccName, String cardType, String cvv, String paymentType) {
		sdpaypage.selectAccount(account);
		sdpaypage.setAmount(amount);
		sdpaypage.setCCNumber(ccNum);
		sdpaypage.setCCName(ccName);
		sdpaypage.setVisaMaster(cardType);
		sdpaypage.setExpirationDate();
		sdpaypage.setCVV(cvv);
		sdpaypage.setNotes(notes);
	}
}
