package newcrm.business.umbusiness;

import newcrm.business.businessbase.deposit.CPBrazilPIXDeposit;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class UMCPBrazilPIXDeposit extends CPBrazilPIXDeposit {

    public UMCPBrazilPIXDeposit(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        setPersonalID("12345689101");
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
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
