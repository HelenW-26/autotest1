package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.deposit.CreditCardSolidPayPage;

public class CPCreditCardSolidPay extends CPCreditCardDeposit {

	protected CreditCardSolidPayPage solidpaypage;
	public CPCreditCardSolidPay(WebDriver driver) {
		super(new CreditCardSolidPayPage(driver));
		this.solidpaypage = new CreditCardSolidPayPage(driver);
	}
	
	public CPCreditCardSolidPay(CreditCardSolidPayPage solidpaypage) {
		super(solidpaypage);
		this.solidpaypage = solidpaypage;
	}
	
	public void ccdeposit(String account, String amount, String notes, String ccNum, String ccName, String cardType, String cvv, String paymentType) {
		solidpaypage.selectAccount(account);
		solidpaypage.setAmount(amount);
	}
	
}
