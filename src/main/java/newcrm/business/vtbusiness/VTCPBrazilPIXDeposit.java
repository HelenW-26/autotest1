package newcrm.business.vtbusiness;

import newcrm.business.businessbase.deposit.CPBrazilPIXDeposit;
import newcrm.pages.vtclientpages.VTLocalBankTransferDepositPage;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class VTCPBrazilPIXDeposit extends CPBrazilPIXDeposit {

    protected VTLocalBankTransferDepositPage lbPage;

    public VTCPBrazilPIXDeposit(WebDriver driver) {
        super(driver);
        this.lbPage = new VTLocalBankTransferDepositPage(driver);
    }

    @Override
    public void depositWithPersonalIDAccNumNew(String account, String amount, String accountNum, String personalID, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        lbPage.setTaxID(personalID);
        checkPaymentDetails(account,amount);
        payNow();
    }

}
