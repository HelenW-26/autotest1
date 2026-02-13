package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPSkrillWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VTCPSkrillWithdraw extends CPSkrillWithdraw {

    VTLocalBankWithdrawPage vtLocalBankWithdrawPage;

    public VTCPSkrillWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
        vtLocalBankWithdrawPage = new VTLocalBankWithdrawPage(driver);
    }

    public boolean setWithdrawInfoAndSubmit(String email,String notes) {
        skrillpage.setAccountName(email);
        skrillpage.setAccountNumber(email);
        vtLocalBankWithdrawPage.setImportantNotes(notes);
        return skrillpage.submit();
    }

    public void setWithdrawInfo(String email,String notes) {
        skrillpage.setAccountName(email);
        skrillpage.setAccountNumber(email);
        vtLocalBankWithdrawPage.setImportantNotes(notes);
    }

}
