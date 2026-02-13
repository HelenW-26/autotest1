package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPJapanBankWithdraw;
import newcrm.pages.auclientpages.AuLocalBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPJapanBankWithdraw extends CPJapanBankWithdraw {

    public AuCPJapanBankWithdraw(WebDriver driver) {
        super(new AuLocalBankWithdrawPage(driver));
        this.not_cc_page = new AuWithdrawPage(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankName();
        page.setBankBranch(bank_branch);
        page.setAccountType();
        page.setBankAccountName("ファックユー");
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
