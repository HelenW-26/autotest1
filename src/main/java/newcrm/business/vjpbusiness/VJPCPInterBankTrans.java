package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.business.businessbase.CPInterBankTrans;
import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.AuInterBankTransPage;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import newcrm.pages.vjpclientpages.VJPCPDepositPage;
import newcrm.pages.vjpclientpages.VJPCryptoDepositPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class VJPCPInterBankTrans extends CPInterBankTrans {

	public VJPCPInterBankTrans(WebDriver driver) {
		super(new VJPCPDepositPage(driver));
	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv){
		LogUtils.info("==========VJP Fill Credit Card Form===================");
		page.fillCCForm(cardNumber, expiryDate, cvv);
	}



}
