package newcrm.business.umbusiness;


import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.openqa.selenium.WebDriver;

public class UMDepositTestCase extends DepositBase {

    public UMDepositTestCase(WebDriver driver) {
        super(new InterBankTransPage(driver));
    }


    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        System.out.println("***************** UM Weini *****************");
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }
}
