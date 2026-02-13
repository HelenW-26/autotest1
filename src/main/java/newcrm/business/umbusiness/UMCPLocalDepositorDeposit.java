package newcrm.business.umbusiness;

import newcrm.business.businessbase.deposit.CPLocalDepositorDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class UMCPLocalDepositorDeposit extends CPLocalDepositorDeposit {

    public UMCPLocalDepositorDeposit(WebDriver driver) {
        super(new LocalBankTransferDepositPage(driver));
    }

    @Override
    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        page.selectAccount(account);
        page.setAmount(amount);
        page.setLocalDepositor();
        page.setNotes(notes);
    }

    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD method) {
        setAccountAndAmount(account, amount);
        setDepositMethod(method);
        clickContinue();
        page.setLocalDepositorNew();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }
}
