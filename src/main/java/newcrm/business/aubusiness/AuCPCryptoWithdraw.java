package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPCryptoWithdraw;
import newcrm.pages.auclientpages.AuWithdrawPage;
import newcrm.pages.clientpages.withdraw.CryptoWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPCryptoWithdraw extends CPCryptoWithdraw {

    public AuCPCryptoWithdraw(WebDriver driver)
    {
        super(new CryptoWithdrawPage(driver));
        this.not_cc_page = new AuWithdrawPage(driver);
    }

}
