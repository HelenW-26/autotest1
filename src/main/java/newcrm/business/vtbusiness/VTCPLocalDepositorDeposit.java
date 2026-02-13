package newcrm.business.vtbusiness;

import newcrm.business.businessbase.deposit.CPLocalDepositorDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.vtclientpages.VTLocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class VTCPLocalDepositorDeposit extends CPLocalDepositorDeposit {

    public VTCPLocalDepositorDeposit(WebDriver driver) {
        super(new VTLocalBankTransferDepositPage(driver));
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD method) {
        setAccountAndAmount(account, amount);
        setDepositMethod(method);
        clickContinue();
        page.setLocalDepositorNew();
        checkPaymentDetails(account,amount);
        payNow();
    }

}
