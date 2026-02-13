package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.CPFasaPay;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class CPMalaysiaEwallet extends CPFasaPay {
    public CPMalaysiaEwallet(WebDriver driver) {
        super(driver);
    }

    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetails(account,amount);
        terms();
        payNow();
    }
}
