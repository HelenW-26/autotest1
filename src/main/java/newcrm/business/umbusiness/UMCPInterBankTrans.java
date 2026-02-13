package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPInterBankTrans;
import newcrm.global.GlobalProperties;
import newcrm.pages.umclientpages.UMInterBankTransPage;
import org.openqa.selenium.WebDriver;

public class UMCPInterBankTrans extends CPInterBankTrans {

    protected UMInterBankTransPage interBankTransPage;

    public UMCPInterBankTrans(UMInterBankTransPage interBankTrans_Page) {
        super(interBankTrans_Page);
        this.interBankTransPage = interBankTrans_Page;
    }

    public UMCPInterBankTrans(WebDriver driver) {
        this(new UMInterBankTransPage(driver));
    }
    public void upload() {
        interBankTransPage.uploadFile();
    }

    public void deposit(String account, String amount, String notes) {
        selectAccount(account);
        upload();
        setAmount(amount);
        setNotes(notes);
        submit();
    }

    public void backHomeButton() {
        interBankTransPage.backHomeButton();

    }
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD method) {
        setAccountAndAmount(account, amount);
        setDepositMethod(method);
        clickContinue();
        uploadNew();
        checkPaymentDetailsNoDepositAmount(account,amount);
        payNow();
    }
    public void uploadNew() {
        interBankTransPage.uploadFileNew();
    }
}
