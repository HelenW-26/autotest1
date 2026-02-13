package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPMalaysiaBankWithdraw;
import newcrm.pages.pugclientpages.PULocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class PUCPMalaysiaBankWithdraw extends CPMalaysiaBankWithdraw {

    public PUCPMalaysiaBankWithdraw(WebDriver driver) {
        super(new PULocalBankWithdrawPage(driver));
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankName();
        page.setBankBranch(bank_branch);
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
