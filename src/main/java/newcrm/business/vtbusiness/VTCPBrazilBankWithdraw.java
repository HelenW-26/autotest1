package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPBrazilBankWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPBrazilBankWithdraw extends CPBrazilBankWithdraw {

    public VTCPBrazilBankWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        this.page = new VTLocalBankWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
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
