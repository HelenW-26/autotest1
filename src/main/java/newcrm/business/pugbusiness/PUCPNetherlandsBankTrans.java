package newcrm.business.pugbusiness;

import newcrm.business.businessbase.deposit.CPNetherlandsBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class PUCPNetherlandsBankTrans extends CPNetherlandsBankTrans {

    public PUCPNetherlandsBankTrans(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        terms();
        payNow();
    }

}
