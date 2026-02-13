package newcrm.business.starbusiness;

import newcrm.business.vtbusiness.CPUnionPay;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class STARCPUnionPay extends CPUnionPay {

    public STARCPUnionPay(WebDriver driver)
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
