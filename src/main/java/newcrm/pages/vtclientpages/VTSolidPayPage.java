package newcrm.pages.vtclientpages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;

public class VTSolidPayPage extends DepositBasePage {

	public VTSolidPayPage(WebDriver driver){
		super(driver);
	}
	
	public void setCCNumber(String num) {
		this.setInputValue(this.findVisibleElemntByTestId("cardNumber"), num);
		GlobalMethods.printDebugInfo("SolidPayPage * set Credit card number to : " + num);
	}
	
	public void setCCName(String name) {
		this.setInputValue(this.findVisibleElemntByTestId("cardHolderName"), name);
		GlobalMethods.printDebugInfo("SolidPayPage * set Name on card to : " + name);
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
		GlobalMethods.printDebugInfo("SolidPayPage * set expiration date to:" + month + "/" + year);
		return month + "/" + year;
	}
	
	public void setCVV(String cvv) {
		this.setInputValue(this.findVisibleElemntByTestId("cvv"), cvv);
		GlobalMethods.printDebugInfo("SolidPayPage * set Security code CVV to : " + cvv);
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
	}
}
