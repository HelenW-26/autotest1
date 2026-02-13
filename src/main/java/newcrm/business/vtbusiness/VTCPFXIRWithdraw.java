package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPFXIRWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPFXIRWithdraw extends CPFXIRWithdraw {

    VTLocalBankWithdrawPage vtLocalBankWithdrawPage;

    public VTCPFXIRWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        vtLocalBankWithdrawPage = new VTLocalBankWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
    }

    @Override
    public void setWithdrawInfo(String email,String notes) {
        fxirpage.setFXIRSenderID(email);
        fxirpage.setAccountName(email);
        fxirpage.setAccountNumber(email);
        vtLocalBankWithdrawPage.setImportantNotes(notes);
    }

    @Override
    public boolean setWithdrawInfoAndSubmit(String email,String notes) {
        fxirpage.setFXIRSenderID(email);
        fxirpage.setAccountName(email);
        fxirpage.setAccountNumber(email);
        vtLocalBankWithdrawPage.setImportantNotes(notes);
        return fxirpage.submit();
    }

}
