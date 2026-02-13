package newcrm.business.starbusiness;

import newcrm.business.businessbase.deposit.CPMalaysiaEwallet;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class STARCPMalaysiaEwallet extends CPMalaysiaEwallet {

    public STARCPMalaysiaEwallet(WebDriver driver)
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
        payNow();
    }

}
