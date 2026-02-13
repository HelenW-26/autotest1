package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPMalaysiaBankWithdraw;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPMalaysiaBankWithdraw extends CPMalaysiaBankWithdraw {

    public VTCPMalaysiaBankWithdraw(WebDriver driver) {
        super(new VTLocalBankWithdrawPage(driver));
        this.not_cc_page = new VTWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
    }

}
