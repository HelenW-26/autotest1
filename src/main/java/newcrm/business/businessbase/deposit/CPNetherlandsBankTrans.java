package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.NetherlandsBankTransPage;
import org.openqa.selenium.WebDriver;

public class CPNetherlandsBankTrans extends DepositBase {

    public CPNetherlandsBankTrans(WebDriver driver) {
        super(new NetherlandsBankTransPage(driver));
    }

}
