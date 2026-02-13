package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPJapanBankWithdraw;
import org.openqa.selenium.WebDriver;

public class MOCPJapanBankWithdraw extends CPJapanBankWithdraw {

    public MOCPJapanBankWithdraw(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankName();
        page.setBankBranch(bank_branch);
        page.setBankAccountName("ファックユー");
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
