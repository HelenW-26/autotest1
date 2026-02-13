package newcrm.business.vtbusiness.wallet;

import newcrm.business.businessbase.wallet.CPWalletCryptoHome;
import newcrm.pages.vtclientpages.wallet.VTWalletCryptoHomePage;
import org.openqa.selenium.WebDriver;


public class VTCPWalletCryptoHome extends CPWalletCryptoHome {
    protected VTWalletCryptoHomePage walletHomePage;
    public VTCPWalletCryptoHome(WebDriver driver) {
        super(driver);
        this.walletHomePage = new VTWalletCryptoHomePage(driver);
    }
    @Override
    public void validateWalletTable(){walletHomePage.validateWalletTable();}

}
