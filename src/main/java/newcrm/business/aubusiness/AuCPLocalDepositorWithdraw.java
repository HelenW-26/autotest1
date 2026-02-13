package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPLocalDepositorWithdraw;
import newcrm.pages.auclientpages.AuLocalBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPLocalDepositorWithdraw extends CPLocalDepositorWithdraw {

    AuLocalBankWithdrawPage localBankWithdrawPage;

    public AuCPLocalDepositorWithdraw(WebDriver driver)
    {
        super(driver);
        this.not_cc_page = new AuWithdrawPage(driver);
        localBankWithdrawPage = new AuLocalBankWithdrawPage(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        localBankWithdrawPage.setLocalDepositor();
        page.setBankName();
        page.setBankAddress(city);
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
