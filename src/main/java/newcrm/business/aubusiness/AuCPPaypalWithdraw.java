package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPPaypalWithdraw;
import newcrm.pages.auclientpages.AuWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPPaypalWithdraw extends CPPaypalWithdraw {

    public AuCPPaypalWithdraw(WebDriver driver)
    {
        super(driver);
        this.not_cc_page = new AuWithdrawPage(driver);
    }

    @Override
    public void setWithdrawInfo(String accountNumber,String notes) {
        skrillpage.setNotes(notes);
    }

}
