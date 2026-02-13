package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPPhillipineBankWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPPhillipineBankWithdraw extends CPPhillipineBankWithdraw {

    public VTCPPhillipineBankWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        this.page = new VTLocalBankWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
    }

}
