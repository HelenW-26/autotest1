package newcrm.business.businessbase;

import newcrm.pages.clientpages.withdraw.SkrillWithdrawPage;
import org.openqa.selenium.WebDriver;

public class CPPaypalWithdraw extends CPSkrillWithdraw{

    public CPPaypalWithdraw(WebDriver driver) {
        super(new SkrillWithdrawPage(driver));
        this.skrillpage = new SkrillWithdrawPage(driver);
    }

    @Override
    public boolean setWithdrawInfoAndSubmit(String notes,String accountNumber) {
        skrillpage.setPaypalID(accountNumber);
        skrillpage.setNotes(notes);
        return skrillpage.submit();
    }

    @Override
    public void setWithdrawInfo(String accountNumber,String notes) {
        skrillpage.setPaypalID(accountNumber);
        skrillpage.setNotes(notes);
    }
}
