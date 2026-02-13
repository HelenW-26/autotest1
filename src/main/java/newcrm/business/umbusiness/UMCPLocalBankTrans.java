package newcrm.business.umbusiness;

import newcrm.business.businessbase.deposit.CPLocalBankTrans;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class UMCPLocalBankTrans extends CPLocalBankTrans {

    protected LocalBankTransferDepositPage page;
    public UMCPLocalBankTrans(WebDriver driver) {
        this(new LocalBankTransferDepositPage(driver));
    }

    public UMCPLocalBankTrans(LocalBankTransferDepositPage v_page) {
        super(v_page);
        this.page = v_page;
    }

    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

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
