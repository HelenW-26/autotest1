package newcrm.pages.clientpages.deposit;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;

public class CreditCardSDPayPage extends CreditCardDepositPage {

	public CreditCardSDPayPage(WebDriver driver){
		super(driver);
	}
	
	public void setCCNumber(String num) {
		this.setInputValue(this.findVisibleElemntByTestId("cardNumber"), num);
		GlobalMethods.printDebugInfo("SDPayPage * set Credit card number to : " + num);
	}
	
	public void setCCName(String name) {
		this.setInputValue(this.findVisibleElemntByTestId("cardHolderName"), name);
		GlobalMethods.printDebugInfo("SDPayPage * set Name on card to : " + name);
	}
	
	public String setExpirationDate() {
		WebElement yeardiv = this.findClickableElemntByTestId("year");

		this.moveElementToVisible(yeardiv);
		yeardiv.click();
		String year = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(year == null) {
			yeardiv.click();
			return null;
		}
		
		WebElement monthdiv = this.findClickableElemntByTestId("month");
		this.moveElementToVisible(monthdiv);
		monthdiv.click();
		String month = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(month == null){
			monthdiv.click();
			return null;
		}
		GlobalMethods.printDebugInfo("SDPayPage * set expiration date to:" + month + "/" + year);
		return month + "/" + year;
	}
	
	public void setCVV(String cvv) {
		if(cvv.length() > 3) {
			GlobalMethods.printDebugInfo("CPSDPay * WARNING!!! cvv is longer than 3");
		}
		this.setInputValue(this.findVisibleElemntByTestId("cvv"), cvv);
		GlobalMethods.printDebugInfo("SDPayPage * set Security code CVV to : " + cvv);
	}
	
	public void setVisaMaster(String cardType) {
		WebElement type = this.findClickableElemntByTestId("brand");
		this.moveElementToVisible(type);
		type.click();
		List<WebElement> lis = this.getAllOpendElements();
		for(WebElement li: lis) {
			String inner_text = li.getText();
			if(inner_text.equalsIgnoreCase(cardType)) {
				this.moveElementToVisible(li);
				this.clickElement(li);
			}
		}
		GlobalMethods.printDebugInfo("SDPayPage * set Payment / Card Type to : " + cardType);
	}
}
