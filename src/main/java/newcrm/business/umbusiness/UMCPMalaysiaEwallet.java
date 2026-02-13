package newcrm.business.umbusiness;

import newcrm.business.businessbase.deposit.CPMalaysiaEwallet;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class UMCPMalaysiaEwallet extends CPMalaysiaEwallet {

    public UMCPMalaysiaEwallet(WebDriver driver) {
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
