package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPKoreaBankWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPKoreaBankWithdraw extends CPKoreaBankWithdraw {

    public VTCPKoreaBankWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        page = new VTLocalBankWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
    }

}
