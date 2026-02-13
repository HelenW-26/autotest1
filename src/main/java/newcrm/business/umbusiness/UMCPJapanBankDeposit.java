package newcrm.business.umbusiness;

import newcrm.business.businessbase.deposit.CPJapanBankDeposit;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class UMCPJapanBankDeposit extends CPJapanBankDeposit {

    public UMCPJapanBankDeposit(WebDriver driver) {
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
