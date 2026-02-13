package newcrm.business.businessbase.deposit;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.SouthAfricaBankTransferPage;
import org.openqa.selenium.WebDriver;

public class CPSouthAfricaBankTransfer extends CPLocalBankTrans{
    protected SouthAfricaBankTransferPage sabpage;

    public CPSouthAfricaBankTransfer(WebDriver driver)
    {
        super(driver);
        this.sabpage = new SouthAfricaBankTransferPage(driver);
    }

    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        sabpage.setPersonalID();
        //checkPaymentDetails(account,amount);
        payNow();
    }
}
