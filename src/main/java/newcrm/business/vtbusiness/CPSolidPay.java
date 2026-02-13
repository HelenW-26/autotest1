package newcrm.business.vtbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalMethods;
import newcrm.pages.vtclientpages.VTSolidPayPage;

public class CPSolidPay extends DepositBase {

	protected VTSolidPayPage solidpaypage;
	public CPSolidPay(WebDriver driver) {
		super(new VTSolidPayPage(driver));
		this.solidpaypage = new VTSolidPayPage(driver);
	}
	
	public void setCCinfo(String cardNum,String cardName,String notes) {
		solidpaypage.setCCNumber(cardNum);
		solidpaypage.setCCName(cardName);
		solidpaypage.setNotes(notes);
	}
	
	public String setExpirationDate() {
		return solidpaypage.setExpirationDate();
	}
	
	public void setCVV(String cvv) {
		if(cvv.length() > 3) {
			GlobalMethods.printDebugInfo("CPSolidPay * WARNING!!! cvv is longer than 3");
		}
		
		solidpaypage.setCVV(cvv);
	}
	
	public void setVisaMaster(String cardType) {
		solidpaypage.setVisaMaster(cardType);
	}
	
}
