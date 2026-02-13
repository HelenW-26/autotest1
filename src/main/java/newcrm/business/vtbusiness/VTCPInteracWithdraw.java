package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPInteracWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPInteracWithdraw extends CPInteracWithdraw {

    VTLocalBankWithdrawPage vtLocalBankWithdrawPage;

    public VTCPInteracWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
        vtLocalBankWithdrawPage = new VTLocalBankWithdrawPage(driver);
    }

    public boolean setWithdrawInfo(String bbName, String iban, String notes) {
        vtLocalBankWithdrawPage.setImportantNotes(notes);
        return true;
    }

}
