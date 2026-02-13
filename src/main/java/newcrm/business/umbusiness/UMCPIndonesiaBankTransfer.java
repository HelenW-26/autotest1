package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPIndonesiaBankTransfer;
import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.AuIndonesiaBankTransferPage;
import newcrm.pages.clientpages.deposit.IndiaUPIPage;
import newcrm.pages.clientpages.deposit.IndonesiaBankTransferPage;
import org.openqa.selenium.WebDriver;

public class UMCPIndonesiaBankTransfer extends CPIndonesiaBankTransfer {

    protected IndonesiaBankTransferPage indonesiaBankTransferPage;
    public UMCPIndonesiaBankTransfer(WebDriver driver)
    {
        super(driver);
        this.indonesiaBankTransferPage = new IndonesiaBankTransferPage(driver);
    }


    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

    public void selectBankTrasfer(String channel)
    {
        indonesiaBankTransferPage.selectBankTrasfer(channel);
    }

    public void setBank()
    {
        indonesiaBankTransferPage.setBank();
    }
}
