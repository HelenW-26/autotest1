package newcrm.business.vtbusiness;

import newcrm.business.businessbase.deposit.CPBrazilBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class VTCPBrazilBankTrans extends CPBrazilBankTrans {

    public VTCPBrazilBankTrans(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositWithPersonalIDAccNumNew(String account, String amount, String accountNum,String personalID,GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetails(account,amount);
        payNow();
    }

}
