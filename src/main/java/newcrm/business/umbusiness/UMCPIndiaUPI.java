package newcrm.business.umbusiness;


import newcrm.business.businessbase.CPIndiaUPI;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.IndiaUPIPage;
import org.openqa.selenium.WebDriver;

public class UMCPIndiaUPI extends CPIndiaUPI {
    protected IndiaUPIPage IndiaUPIPage;

    public UMCPIndiaUPI(WebDriver driver) {
        super(driver);
        this.iupage = new IndiaUPIPage(driver);
    }

    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }

    public void goBack() {
        iupage.goBack();
    }

}
