package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.deposit.CPLocalBankTrans;
import newcrm.global.GlobalProperties;
import newcrm.pages.vjpclientpages.deposit.VJPLocalBankTransferDepositPage;
import org.openqa.selenium.WebDriver;

public class VJPCPLocalBankTrans extends CPLocalBankTrans {

    protected VJPLocalBankTransferDepositPage jpbtpage;

    public VJPCPLocalBankTrans(WebDriver driver)
    {
        super(driver);
        this.jpbtpage = new VJPLocalBankTransferDepositPage(driver);
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
