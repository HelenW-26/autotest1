package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPSkrillPay;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class PUCPSkrillPay extends CPSkrillPay {

    public PUCPSkrillPay(WebDriver driver) {
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
