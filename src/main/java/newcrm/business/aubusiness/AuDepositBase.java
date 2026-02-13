package newcrm.business.aubusiness;

import newcrm.business.businessbase.DepositBase;
import newcrm.pages.auclientpages.AuCryptoDepositPage;
import newcrm.pages.auclientpages.AuDepositBasePage;
import newcrm.pages.auclientpages.AuDepositFundsPage;
import newcrm.pages.clientpages.DepositBasePage;
import org.openqa.selenium.WebDriver;

public class AuDepositBase extends DepositBase {

    public AuDepositBase(WebDriver driver) {

      super(new AuDepositBasePage(driver));

    }

    // check deposit kyc not met pop window
    public boolean checkDepositKYCNotMetPopWindow() {
        return page.checkDepositKYCNotMetPopWindow();
    }
}
