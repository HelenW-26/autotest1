package newcrm.business.mobusiness;

import newcrm.business.businessbase.deposit.CPBrazilBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class MOCPBrazilBankTrans extends CPBrazilBankTrans {

    public MOCPBrazilBankTrans(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositWithPersonalIDAccNumNew(String account, String amount, String accountNum, String personalID, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        setAccountNumber(accountNum);
        setPersonalID(personalID);
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        payNow();
    }

}
