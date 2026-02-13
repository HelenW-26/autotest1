package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPBrazilBankWithdraw;
import org.openqa.selenium.WebDriver;

public class STARCPBrazilBankWithdraw extends CPBrazilBankWithdraw {

    public STARCPBrazilBankWithdraw(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankName();
        page.setAccountType();
        page.setDocumentType();
        page.setAccDigit(accdigit);
        page.setDocId(docid);
        page.setBankBranch("testcrmbranch");
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}
