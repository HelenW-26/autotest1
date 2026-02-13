package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class CPLocalDepositorDeposit extends DepositBase {

    protected LocalBankTransferDepositPage page;

    public CPLocalDepositorDeposit(LocalBankTransferDepositPage v_page) {
        super(v_page);
        this.page = v_page;
    }

    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        page.selectAccount(account);
        page.setAmount(amount);
        page.setLocalDepositor();
        page.setNotes(notes);
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
