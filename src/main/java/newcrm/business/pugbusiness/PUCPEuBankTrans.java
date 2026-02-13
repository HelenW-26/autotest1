package newcrm.business.pugbusiness;

import newcrm.business.businessbase.deposit.CPEuBankTrans;
import newcrm.global.GlobalProperties;
import newcrm.pages.pugclientpages.PUEuBankTransPage;
import org.openqa.selenium.WebDriver;

public class PUCPEuBankTrans extends CPEuBankTrans {

    protected PUEuBankTransPage page;

    public PUCPEuBankTrans(WebDriver driver)
    {
        super(driver);
        this.page = new PUEuBankTransPage(driver);
    }

    @Override
    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        selectAccount(account);
        setAmount(amount);
        setNotes(notes);
        page.agreeImportantNote();
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        checkPaymentDetailsNoNetDepositAmount(account,amount);
        payNow();
    }

}
