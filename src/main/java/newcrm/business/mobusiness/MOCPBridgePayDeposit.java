package newcrm.business.mobusiness;

import newcrm.business.businessbase.deposit.CPBridgePayDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.BridgePayDepositPage;
import org.openqa.selenium.WebDriver;

public class MOCPBridgePayDeposit extends CPBridgePayDeposit {

    public MOCPBridgePayDeposit(WebDriver driver) {
        super(new BridgePayDepositPage(driver));
    }

//    @Override
//    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
//    {
//        setAccountAndAmount(account, amount);
//        setDepositMethod(depositMethod);
//        clickContinue();
//        checkPaymentDetails(account,amount);
//        payNow();
//        page.waitCCLoader();
//        page.goBack();
//    }

}
