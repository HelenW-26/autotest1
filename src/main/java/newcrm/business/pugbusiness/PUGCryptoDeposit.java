package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.pugclientpages.PUGCryptoDepositPage;
import org.openqa.selenium.WebDriver;

public class PUGCryptoDeposit extends CPCryptoDeposit {

    public PUGCryptoDeposit(WebDriver driver) {
        super(new PUGCryptoDepositPage(driver));
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        payNow();
    }
}
