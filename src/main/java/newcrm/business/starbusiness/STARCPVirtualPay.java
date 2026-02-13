package newcrm.business.starbusiness;

import newcrm.business.businessbase.deposit.CPVirtualPay;
import newcrm.global.GlobalProperties;
import newcrm.pages.starclientpages.STARVirtualPage;
import org.openqa.selenium.WebDriver;

public class STARCPVirtualPay extends CPVirtualPay {

    public STARCPVirtualPay(WebDriver driver)
    {
        super(new STARVirtualPage(driver));
    }

    @Override
    public void depositWithCCInfoNew(String account, String amount, String ccCity,String ccAddress,String ccPostalCode, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        setCreditCardCity(ccCity);
        setCreditCardAddress(ccAddress);
        setCreditCardPostalCode(ccPostalCode);
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

}
