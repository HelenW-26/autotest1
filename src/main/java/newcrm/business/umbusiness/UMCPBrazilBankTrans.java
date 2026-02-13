package newcrm.business.umbusiness;

import newcrm.business.businessbase.deposit.CPBrazilBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class UMCPBrazilBankTrans extends CPBrazilBankTrans {

    public UMCPBrazilBankTrans(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositWithPersonalIDAccNumNew(String account, String amount, String accountNum,String personalID,GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        setAccountNumber(accountNum);
        setPersonalID(personalID);
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

}
