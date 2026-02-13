package newcrm.business.businessbase;

import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class CPInteracWithdraw extends CPBankTransferWithdraw {

    protected InternalBankWithdrawPage page;

    public CPInteracWithdraw(WebDriver driver) {
        super(new InternalBankWithdrawPage(driver));
        this.page = new InternalBankWithdrawPage(driver);
    }

    public boolean setWithdrawInfo(String bbName, String iban, String notes) {
//        page.setBankAccountName(bbName);
//        page.setBankAccountNumber(iban);
        page.setNotes(notes);
        return true;
    }

    public boolean submit()
    {
        return page.submit();
    }
}
