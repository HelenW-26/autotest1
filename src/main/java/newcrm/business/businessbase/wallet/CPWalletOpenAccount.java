package newcrm.business.businessbase.wallet;

import newcrm.pages.clientpages.wallet.WalletOpenAccountPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class CPWalletOpenAccount {

    protected WalletOpenAccountPage walletOpenAcc;

    public CPWalletOpenAccount(WebDriver driver) {
        this.walletOpenAcc = new WalletOpenAccountPage(driver);
    }

    public void activateWallet() {
        walletOpenAcc.waitLoading();
        walletOpenAcc.clickActivateBtn();
        walletOpenAcc.waitLoadingWalletActivationContent();
        walletOpenAcc.waitLoader();
        walletOpenAcc.waitLoading();
        walletOpenAcc.clickVerifyNowBtn();
        walletOpenAcc.waitLoader();
    }

    public boolean checkActivationUnderReview() {
        return walletOpenAcc.checkActivationUnderReview();
    }

    public void checkAccountActivated() {
        walletOpenAcc.checkAccountActivated();
    }

    public void activateNow(){
        walletOpenAcc.checkWalletActivateNow();
    }
}
