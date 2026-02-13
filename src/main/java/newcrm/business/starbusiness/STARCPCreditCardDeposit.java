package newcrm.business.starbusiness;

import newcrm.business.businessbase.deposit.CPCreditCardDeposit;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class STARCPCreditCardDeposit extends CPCreditCardDeposit {

    public STARCPCreditCardDeposit(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void depositNewNoDepositAmountCheck(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

}
