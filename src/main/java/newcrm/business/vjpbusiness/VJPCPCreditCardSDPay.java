package newcrm.business.vjpbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.deposit.CPCreditCardSDPay;
import newcrm.pages.vjpclientpages.deposit.VJPSDPayPage;

public class VJPCPCreditCardSDPay extends CPCreditCardSDPay {

	protected VJPSDPayPage vjpsdpaypage;
	public VJPCPCreditCardSDPay(WebDriver driver) {
		super(new VJPSDPayPage(driver));
		this.vjpsdpaypage = new VJPSDPayPage(driver);
	}
	
	public VJPCPCreditCardSDPay(VJPSDPayPage vjpsolidpaypage) {
		super(vjpsolidpaypage);
		this.vjpsdpaypage = vjpsolidpaypage;
	}
	
	public void ccdeposit(String account, String amount, String notes, String ccNum, String ccName, String cardType, String cvv, String paymentType) {
		vjpsdpaypage.selectAccount(account);
		vjpsdpaypage.setAmount(amount);
		vjpsdpaypage.setCCNumber(ccNum);
		vjpsdpaypage.setCCName(ccName);
		vjpsdpaypage.setExpirationDate();
		vjpsdpaypage.setCVV(cvv);
	}
	
}
