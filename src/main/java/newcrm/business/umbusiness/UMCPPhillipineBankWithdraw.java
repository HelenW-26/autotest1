package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPPhillipineBankWithdraw;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import newcrm.pages.umclientpages.UMLocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class UMCPPhillipineBankWithdraw extends CPPhillipineBankWithdraw {

    public UMCPPhillipineBankWithdraw(WebDriver driver) {
        super(new UMLocalBankWithdrawPage( driver));
    }
}
