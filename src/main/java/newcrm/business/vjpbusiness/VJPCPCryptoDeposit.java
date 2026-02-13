package newcrm.business.vjpbusiness;

import newcrm.global.GlobalProperties;
import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.pages.vjpclientpages.VJPCryptoDepositPage;
import org.openqa.selenium.WebDriver;

public class VJPCPCryptoDeposit extends CPCryptoDeposit {

	public VJPCPCryptoDeposit(WebDriver driver) {
		super(new VJPCryptoDepositPage(driver));
	}

	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		submit();
		paymentConfirm(); /*will use this common method after all brands complete crypto UI upgrade*/
	}

	@Override
	public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		checkPaymentDetailsNoDepositAmount(account,amount);
		payNow();
	}

}
