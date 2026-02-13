package newcrm.pages.vjpclientpages.deposit;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.deposit.CreditCardSDPayPage;

public class VJPSDPayPage extends CreditCardSDPayPage {

	public VJPSDPayPage(WebDriver driver){
		super(driver);
	}
	
	@Override
	public void setCCNumber(String num) {
		this.setInputValue(this.findVisibleElemntByTestId("masterCreditCard"), num);
		GlobalMethods.printDebugInfo("SolidPayPage * set Credit card number to : " + num);
	}
	
	@Override
	public void setCCName(String name) {
		this.setInputValue(this.findVisibleElemntByTestId("nameCard"), name);
		GlobalMethods.printDebugInfo("SolidPayPage * set Name on card to : " + name);
	}
	
	@Override
	public void setCVV(String cvv) {
		if(cvv.length() > 3) {
			GlobalMethods.printDebugInfo("VJPCPSDPay * WARNING!!! cvv is longer than 3");
		}
		this.setInputValue(this.findVisibleElemntByTestId("securityCode"), cvv);
		GlobalMethods.printDebugInfo("SolidPayPage * set Security code CVV to : " + cvv);
	}
}
