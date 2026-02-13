package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class CPBrazilPIXDeposit extends DepositBase {

	public CPBrazilPIXDeposit(WebDriver driver) {
        super(new LocalBankTransferDepositPage(driver));
	}

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        setPersonalID("12345689101");
        checkPaymentDetails(account,amount);
        payNow();
    }

}
