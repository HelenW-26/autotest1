package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPMexicoBankWithdraw;
import org.openqa.selenium.WebDriver;

public class MOCPMexicoBankWithdraw extends CPMexicoBankWithdraw {

    public MOCPMexicoBankWithdraw(WebDriver driver) {
        super(driver);
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
