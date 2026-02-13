package newcrm.business.starbusiness;

import newcrm.business.businessbase.deposit.CPLocalBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class STARCPLocalBankTrans extends CPLocalBankTrans {

    public STARCPLocalBankTrans(WebDriver driver)
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
