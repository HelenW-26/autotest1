package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPAzupayWithdraw;
import newcrm.pages.auclientpages.AUAzupayWithdrawPage;
import org.openqa.selenium.WebDriver;

public class AuCPAzupayWithdraw extends CPAzupayWithdraw {

    public AuCPAzupayWithdraw(WebDriver driver) {
        super(new AUAzupayWithdrawPage(driver));
    }
}
