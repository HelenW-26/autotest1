package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPVietnamBankWithdraw;
import newcrm.pages.auclientpages.AuLocalBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPVietnamBankWithdraw extends CPVietnamBankWithdraw {

    public AuCPVietnamBankWithdraw(WebDriver driver) {
        super(new AuLocalBankWithdrawPage(driver));
        this.not_cc_page = new AuWithdrawPage(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankName();
        page.setBankBranch(bank_branch);
//      page.setBankIFSC(ifsc); //this field has been hidden in production
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
