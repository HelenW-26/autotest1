package newcrm.business.pugbusiness;

import newcrm.business.businessbase.deposit.CPCreditCardDeposit;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class PUCPCreditCardDeposit extends CPCreditCardDeposit {

    public PUCPCreditCardDeposit(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        payNow();
    }

    @Override
    public void depositNewNoDepositAmountCheck(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        payNow();
    }

}
