package newcrm.business.businessbase.wallet;

import newcrm.pages.clientpages.wallet.WalletEarnPage;
import org.openqa.selenium.WebDriver;


/**
 * @Author wesley
 * @Description
 **/
public class CPWalletEarn {
    protected WalletEarnPage walletEarnPage;
    public CPWalletEarn(WebDriver driver) {
        walletEarnPage = new WalletEarnPage(driver);
    }

    public void  checkEarnHomePage(){
        walletEarnPage.checkEarnHomePage();
    }

}
