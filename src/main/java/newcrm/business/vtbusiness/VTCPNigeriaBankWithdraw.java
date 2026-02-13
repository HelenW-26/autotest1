package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPNigeriaBankWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPNigeriaBankWithdraw extends CPNigeriaBankWithdraw {

    public VTCPNigeriaBankWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        page = new VTLocalBankWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
    }

}
