package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.pages.clientpages.deposit.BrazilBankTransPage;
import org.openqa.selenium.WebDriver;

public class CPBrazilBankTrans extends DepositBase {

    public CPBrazilBankTrans(WebDriver driver) {
        super(new BrazilBankTransPage(driver));
    }

}
