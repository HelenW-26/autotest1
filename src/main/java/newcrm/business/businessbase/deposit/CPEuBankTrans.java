package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.pages.clientpages.deposit.EuBankTransPage;
import org.openqa.selenium.WebDriver;

public class CPEuBankTrans extends DepositBase {

    public CPEuBankTrans(WebDriver driver)
    {
        super(new EuBankTransPage(driver));
    }

    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        selectAccount(account);
        setAmount(amount);
        setNotes(notes);
    }

}
