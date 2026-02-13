package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPKoreaBankWithdraw;
import newcrm.pages.auclientpages.AuLocalBankWithdrawPage;
import newcrm.pages.auclientpages.AuWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AUCPKoreaBankWithdraw extends CPKoreaBankWithdraw {

    public AUCPKoreaBankWithdraw(WebDriver driver) {
        super(new AuLocalBankWithdrawPage(driver));
        this.not_cc_page = new AuWithdrawPage(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        //Temporary
        page.setBankName();
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
