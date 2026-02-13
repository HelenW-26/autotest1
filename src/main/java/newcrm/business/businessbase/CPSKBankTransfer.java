package newcrm.business.businessbase;

import newcrm.pages.clientpages.deposit.SKoreaBankTransferPage;
import org.openqa.selenium.WebDriver;

public class CPSKBankTransfer extends CPInterBankTrans{

    protected SKoreaBankTransferPage sKoreaBankTransferPage;
    public CPSKBankTransfer(WebDriver driver)
    {
        super(new SKoreaBankTransferPage(driver));
        this.sKoreaBankTransferPage = new SKoreaBankTransferPage(driver);
    }

    @Override
    public void deposit(String account, String amount, String notes) {
        selectAccount(account);
        setAmount(amount);
        setNotes(notes);
    }

    @Override
    public void setEmail(String email) {
        sKoreaBankTransferPage.setEmail(email);
    }
}
