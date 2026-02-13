package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.VirtualPayPage;
import org.openqa.selenium.WebDriver;

public class CPVirtualPay extends DepositBase {

    public CPVirtualPay(WebDriver driver) {
        super(new VirtualPayPage(driver));
    }

    public CPVirtualPay(VirtualPayPage v_page) {
        super(v_page);
        this.page = v_page;
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

}
