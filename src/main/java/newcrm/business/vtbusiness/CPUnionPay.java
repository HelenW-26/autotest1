package newcrm.business.vtbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPFasaPay;
import newcrm.pages.clientpages.deposit.FasaPayPage;

public class CPUnionPay extends CPFasaPay {
	public CPUnionPay(WebDriver driver) {
		super(new FasaPayPage(driver));
	}
	

	public boolean checkIfNavigateToThirdUrl(String thirdURL,String tradeURL) {
		if(!fasapaypage.checkUrlContains(thirdURL)) {
			if(fasapaypage.getURL().contains(tradeURL)) {
				return false;
			}else {
				return true;
			}
		}
		else {
			return true;
		}
	}

}
