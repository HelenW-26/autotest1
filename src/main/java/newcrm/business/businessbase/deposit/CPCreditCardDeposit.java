package newcrm.business.businessbase.deposit;


import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.DepositBase;
import newcrm.pages.clientpages.deposit.CreditCardDepositPage;

public class CPCreditCardDeposit extends DepositBase {
	
	protected CreditCardDepositPage ccpage;
	public CPCreditCardDeposit(WebDriver driver) {
		super(new CreditCardDepositPage(driver));
		this.ccpage = new CreditCardDepositPage(driver);
	}

	public CPCreditCardDeposit(CreditCardDepositPage v_page) {
		super(v_page);
		this.ccpage = v_page;
	}

	public void ccdeposit(String account, String amount, String notes, String ccNum, String ccName, String cardType, String cvv, String paymentType) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
	}
	public void appleGooglePagedepositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		selectPaymentMethod();
		payNow();
	}

	
	public boolean checkThirdPartyIframe() {
		if (!ccpage.checkIframeExists()) {
			return false;
		} else {
			return true;
		}

	}

	public boolean checkThirdPartyIframeVisible(String brand,String frameName) {

		return ccpage.checkIframeVisible(brand,frameName);

	}

	public void waitCCLoader() {
		page.waitCCLoader();
	}

}
