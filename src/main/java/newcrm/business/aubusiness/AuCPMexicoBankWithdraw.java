package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPMexicoBankWithdraw;
import newcrm.pages.auclientpages.AuLocalBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPMexicoBankWithdraw extends CPMexicoBankWithdraw {

    public AuCPMexicoBankWithdraw(WebDriver driver) {
        super(new AuLocalBankWithdrawPage(driver));
        this.not_cc_page = new AuWithdrawPage(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankName();
        page.setAccountType();
        page.setDocumentType();
        page.setAccDigit(accdigit);
        page.setDocId(docid);
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
