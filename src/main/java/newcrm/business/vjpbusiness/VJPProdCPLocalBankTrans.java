package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.deposit.CPLocalBankTrans;
import newcrm.pages.clientpages.deposit.JapanBankTransferPage;
import newcrm.pages.vjpclientpages.deposit.VJPJapanBankTransferPage;
import org.openqa.selenium.WebDriver;

public class VJPProdCPLocalBankTrans extends CPLocalBankTrans {

    protected JapanBankTransferPage jpbtpage;

    public VJPProdCPLocalBankTrans(WebDriver driver)
    {
        super(driver);
        this.jpbtpage = new VJPJapanBankTransferPage(driver);
    }

    @Override
    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        selectAccount(account);
        setAmount(amount);
    }

}
