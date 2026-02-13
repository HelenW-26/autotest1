package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPCryptoWithdraw;
import newcrm.pages.clientpages.withdraw.CryptoWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPCryptoWithdraw extends CPCryptoWithdraw {

    VTLocalBankWithdrawPage vtLocalBankWithdrawPage;

    public VTCPCryptoWithdraw(WebDriver driver) {
        super(new CryptoWithdrawPage(driver));
        this.not_cc_page = new VTWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
        vtLocalBankWithdrawPage = new VTLocalBankWithdrawPage(driver);
    }

    @Override
    public void setCryptoWithdrawalInfoNew(String walletaddress, String note) {
        cryptowithdrawpage.setWalletAddressNew(walletaddress);
        vtLocalBankWithdrawPage.setImportantNotes(note);

        System.out.println("Set Wallet Address: " + walletaddress);
    }

}
