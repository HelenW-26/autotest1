package newcrm.business.pugbusiness;

import newcrm.business.businessbase.deposit.CPLocalBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class PUCPLocalBankTrans extends CPLocalBankTrans {

    public PUCPLocalBankTrans(WebDriver driver) {
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

}
