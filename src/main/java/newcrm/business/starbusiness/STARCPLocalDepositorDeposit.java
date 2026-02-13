package newcrm.business.starbusiness;

import newcrm.business.businessbase.deposit.CPLocalDepositorDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class STARCPLocalDepositorDeposit extends CPLocalDepositorDeposit {

    public STARCPLocalDepositorDeposit(WebDriver driver)
    {
        super(new LocalBankTransferDepositPage(driver));
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        page.setLocalDepositorNew();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

}
