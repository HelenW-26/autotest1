package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPInterBankTrans;
import newcrm.pages.pugclientpages.PUGCPDepositPage;
import newcrm.pages.vjpclientpages.VJPCPDepositPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class PUGCPInterBankTrans extends CPInterBankTrans {

	public PUGCPInterBankTrans(WebDriver driver) {
		super(new PUGCPDepositPage(driver));

	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv){
		LogUtils.info("==========PUG Fill Credit Card Form===================");
		page.fillCCForm(cardNumber, expiryDate, cvv);
	}

	public boolean isCCDepositSuccess(){
		LogUtils.info("==========PUG CC Deposit Success Verify===================");
		return page.isCCDepositSuccess();
	}

//	public void setVerificationCode(){
//		LogUtils.info("==========PUG Set Amount===================");
//		page.setVerificationCode();
//	}
}
