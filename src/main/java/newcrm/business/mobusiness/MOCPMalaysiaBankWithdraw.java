package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPMalaysiaBankWithdraw;
import newcrm.business.businessbase.CPPhillipineBankWithdraw;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import newcrm.pages.moclientpages.MOMalaysiaBankWithdrawPage;
import newcrm.pages.umclientpages.UMLocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class MOCPMalaysiaBankWithdraw extends CPMalaysiaBankWithdraw {


    public MOCPMalaysiaBankWithdraw(WebDriver driver) {
        super(new MOMalaysiaBankWithdrawPage( driver));
    }

}
