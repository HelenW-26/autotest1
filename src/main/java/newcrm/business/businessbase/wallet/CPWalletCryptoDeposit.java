package newcrm.business.businessbase.wallet;

import newcrm.global.GlobalProperties.WalletDepositMethod;
import newcrm.pages.clientpages.wallet.WalletCryptoDepositPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CPWalletCryptoDeposit {

    protected WalletCryptoDepositPage walletDepositPage;

    public CPWalletCryptoDeposit(WebDriver driver) {
        this.walletDepositPage = new WalletCryptoDepositPage(driver);
    }

    public void checkSelectedCryptoCurrency(WalletDepositMethod method) {
        walletDepositPage.checkSelectedCryptoCurrency(method);
    }

    public void setCryptoNetwork(WalletDepositMethod method) {
        walletDepositPage.setCryptoNetwork(method);
    }

    public void checkExistsQRCode() {
        walletDepositPage.getCryptoAddressQRCode();
    }

    public void compareWalletCryptoAddress() {
        String cryptoAddressDisplayVal = walletDepositPage.getCryptoAddress();
        String copyText = walletDepositPage.clickCryptoAddressCopyBtn();

        if (!copyText.equals(cryptoAddressDisplayVal)) {
            Assert.fail("Copied Wallet Address mismatch with display value");
        }
    }

    public void waitLoadingWalletDepositContent() {
        walletDepositPage.waitLoadingWalletDepositContent();
    }

    public void waitLoadingWalletDepositHistoryContent() {
        walletDepositPage.waitLoadingWalletDepositHistoryContent();
    }

    public void waitLoadingDepositCryptoAddressContent() {
        walletDepositPage.waitLoadingDepositCryptoAddressContent();
    }

}
