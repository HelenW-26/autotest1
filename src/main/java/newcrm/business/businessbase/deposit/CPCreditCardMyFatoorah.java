package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.deposit.CreditCardSDPayPage;

public class CPCreditCardMyFatoorah extends CPCreditCardDeposit {

	protected CreditCardSDPayPage solidpaypage;
	public CPCreditCardMyFatoorah(WebDriver driver) {
		super(new CreditCardSDPayPage(driver));
		this.solidpaypage = new CreditCardSDPayPage(driver);
	}
	
	public CPCreditCardMyFatoorah(CreditCardSDPayPage solidpaypage) {
		super(solidpaypage);
		this.solidpaypage = solidpaypage;
	}
	
	public void ccdeposit(String account, String amount, String notes, String ccNum, String ccName, String cardType, String cvv, String paymentType) {
		solidpaypage.selectAccount(account);
		solidpaypage.setAmount(amount);
		solidpaypage.setCCNumber(ccNum);
		solidpaypage.setVisaMaster(paymentType);
		solidpaypage.setNotes(notes);
	}
	
}
