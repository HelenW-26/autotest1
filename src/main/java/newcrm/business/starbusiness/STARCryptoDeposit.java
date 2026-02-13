package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.starclientpages.STARCryptoDepositPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class STARCryptoDeposit extends CPCryptoDeposit {
    protected STARCryptoDepositPage starCryptoPage;
    public STARCryptoDeposit(WebDriver driver)
    {
        super(new STARCryptoDepositPage(driver));
        starCryptoPage =  new STARCryptoDepositPage(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

    @Override
    public void goBack()
    {
        LogUtils.info("Go back to home page");
        starCryptoPage.goBackNew();
    }
}
