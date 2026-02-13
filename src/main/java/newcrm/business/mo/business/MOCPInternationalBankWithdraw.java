package newcrm.business.mo.business;

import newcrm.business.businessbase.CPInternationalBankWithdraw;
import newcrm.global.GlobalProperties;
import newcrm.pages.moclientpages.MOInternalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class MOCPInternationalBankWithdraw extends CPInternationalBankWithdraw {
    protected MOInternalBankWithdrawPage page;

    public MOCPInternationalBankWithdraw(WebDriver driver)
    {
        super(new MOInternalBankWithdrawPage(driver));
        this.page = new MOInternalBankWithdrawPage(driver);
    }

    @Override
    public boolean setWithdrawMethodNew(GlobalProperties.DEPOSITMETHOD method) {
        page.closeAuthPopOut();
        String result = not_cc_page.setWithdrawMethodNew(method);
        if(result == null) {
            return false;
        }

        System.out.println("Set Withdraw method : " + method.getWithdrawName());
        return true;
    }
}
