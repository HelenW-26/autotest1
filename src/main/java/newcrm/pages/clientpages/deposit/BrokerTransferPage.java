package newcrm.pages.clientpages.deposit;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import vantagecrm.Utils;

public class BrokerTransferPage extends InterBankTransPage {

	public BrokerTransferPage(WebDriver driver) {
		super(driver);
	}
	
	/* has same testid
	@Override
	public void setNotes(String notes) {
		WebElement note_element = super.findClickableElemntByTestId("imptNotes");
		note_element.clear();
		note_element.sendKeys(notes);
		GlobalMethods.printDebugInfo("Set Important notes to: " + notes);
	}*/

}
