package newcrm.business.pugbusiness;

import newcrm.business.businessbase.deposit.CPBridgePayDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.pugclientpages.PUBridgePayDepositPage;

import org.openqa.selenium.WebDriver;

public class PUCPBridgePayDeposit extends CPBridgePayDeposit {

    protected PUBridgePayDepositPage puBridgePayDepositPage;

    public PUCPBridgePayDeposit(WebDriver driver) {
        super(driver);
        this.puBridgePayDepositPage = new PUBridgePayDepositPage(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        puBridgePayDepositPage.tickTickBox();
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        payNow();
    }

}
