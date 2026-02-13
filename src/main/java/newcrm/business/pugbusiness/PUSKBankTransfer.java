package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPSKBankTransfer;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.SKoreaBankTransferPage;
import newcrm.pages.pugclientpages.PUSKBankTransferPage;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;


public class PUSKBankTransfer extends CPSKBankTransfer {
    protected PUSKBankTransferPage pUSKBankTransferPage;
    public PUSKBankTransfer(WebDriver driver) {
        super(driver);
        this.pUSKBankTransferPage = new PUSKBankTransferPage(driver);
    }

    @Override
    public String checkAccount(GlobalProperties.CURRENCY currency) {
        HashMap<String, String> all_accounts = pUSKBankTransferPage.getAllAvailableAccounts();
        return this.GetAvailableAccount(currency, all_accounts);
    }
}
