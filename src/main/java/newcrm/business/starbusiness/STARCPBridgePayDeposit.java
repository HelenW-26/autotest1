package newcrm.business.starbusiness;

import newcrm.business.businessbase.deposit.CPBridgePayDeposit;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class STARCPBridgePayDeposit extends CPBridgePayDeposit {

    public STARCPBridgePayDeposit(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        terms();
        payNow();
    }

}
