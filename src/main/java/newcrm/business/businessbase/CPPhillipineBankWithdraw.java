package newcrm.business.businessbase;

import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class CPPhillipineBankWithdraw extends CPLocalBankWithdraw {

    public CPPhillipineBankWithdraw(WebDriver driver) {
        super(driver);
    }

    public CPPhillipineBankWithdraw(LocalBankWithdrawPage v_page) {
        super(v_page);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        LogUtils.info("Adding new bank account");
        page.setBankName();
        page.setBankBranch(bank_branch);
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
