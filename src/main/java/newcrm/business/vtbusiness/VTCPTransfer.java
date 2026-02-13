package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.TransferPage;
import newcrm.pages.vtclientpages.VTCPTransferPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Random;

public class VTCPTransfer extends CPTransfer {

    public VTCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new VTCPTransferPage(driver);
    }

    //for copy trading, VT can't see difference between copy trading and non-copy trading account
    public String getTransferToCPAccount() {
        tp.waitLoadingTransferToAccountContent();
        List<TransferPage.Account> accounts = tp.getToNonCPAccounts();
        // Randomly select one account
        Random rand = new Random();
        TransferPage.Account randomAccount = accounts.get(rand.nextInt(accounts.size()));
        String accNum = randomAccount.getAccNumber();
        GlobalMethods.printDebugInfo("Found valid transfer to account, Account: " + accNum + ", Currency: " + randomAccount.getCurrency());

        return accNum;
    }
}
