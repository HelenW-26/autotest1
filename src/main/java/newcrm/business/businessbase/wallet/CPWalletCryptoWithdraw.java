package newcrm.business.businessbase.wallet;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.WalletWithdrawMethod;
import newcrm.pages.clientpages.wallet.WalletCryptoWithdrawPage;
import newcrm.pages.clientpages.wallet.WalletCryptoDepositPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.math.BigDecimal;

public class CPWalletCryptoWithdraw {

    protected WalletCryptoWithdrawPage walletWithdrawPage;
    protected WalletCryptoDepositPage walletDepositPage;

    public WalletWithdrawMethod method;
    public String withdrawCryptoCurrency;
    public String withdrawCryptoNetwork;
    public String withdrawWalletAddress;
    public String withdrawAmount;
    protected String withdrawAmountWithSuffix;
    protected String withdrawFromAccount;
    protected String receiveAmount;
    protected String availableWithdrawAmount;
    protected String withdrawFee;
    protected String withdrawFeeFreeTagDesc;

    public CPWalletCryptoWithdraw(WebDriver driver) {
        this.walletWithdrawPage = new WalletCryptoWithdrawPage(driver);
        this.walletDepositPage = new WalletCryptoDepositPage(driver);
    }

    public void checkSelectedCryptoCurrency() {
        walletWithdrawPage.checkSelectedCryptoCurrency(method);
    }

    public String getMinWithdrawAmount() {
        return walletWithdrawPage.getAvailableWithdrawAmount();
    }

    public void setCryptoToAddress(String walletToAddress) {
        walletWithdrawPage.setCryptoToAddress(walletToAddress);
    }

    public String setCryptoNetwork() {
        return walletWithdrawPage.setCryptoNetwork(method);
    }

    public void setWithdrawAmount() {
        walletWithdrawPage.setWithdrawAmount(withdrawAmount);
    }

    public void submitWithdrawal() {
        walletWithdrawPage.clickWalletWithdrawBtn();
        walletWithdrawPage.waitLoadingWalletConfirmWithdrawContent();
        checkWithdrawReviewInfo();
        compareWalletCryptoAddress();
        walletWithdrawPage.clickWalletWithdrawConfirmBtn();
//        walletWithdrawPage.checkWithdrawAuthenticator();
    }

    public void checkValidWithdrawCryptoAddress() {
        WebElement e = walletWithdrawPage.getCryptoToAddressErrorMsg();

        if (e != null) {
            Assert.fail("Invalid wallet To Address. Err Msg: " + e.getText());
        }
    }

    public boolean checkMinWithdrawAmount() {
        WebElement e = walletWithdrawPage.getCryptoWithdrawAmount();

        // If element is disabled, means account balance is less than minimum withdraw amount
        if (!e.isEnabled()) {
            WebElement infoMsg = walletWithdrawPage.getWithdrawAmountInfoMsg();
            String msg = "";

            if (infoMsg != null) {
                msg = ". Msg: " + infoMsg.getText();
            }

            GlobalMethods.printDebugInfo("The Withdraw Amount element is disabled" + msg);

            return true;
        }

        return false;
    }

    public void checkValidWithdrawAmount() {
        WebElement e = walletWithdrawPage.getWithdrawAmountErrorMsg();

        if (e != null) {
            Assert.fail("Invalid Withdraw Amount. Err Msg: " + e.getText());
        }
    }

    public void waitLoadingWalletWithdrawContent() {
        walletWithdrawPage.waitLoadingWalletWithdrawContent();
    }

    public void waitLoadingWalletWithdrawAmountContent() {
        walletWithdrawPage.waitLoadingWalletWithdrawAmountContent();
    }

    public void waitLoadingWalletWithdrawOrderContent() {
        walletWithdrawPage.waitLoadingWalletWithdrawOrderContent();
    }

    public void checkWithdrawInfo() {

        String withdrawAmountSuffix = walletWithdrawPage.getWithdrawAmountSuffix();
        withdrawAmountWithSuffix = withdrawAmount + " " + withdrawAmountSuffix;
        availableWithdrawAmount = walletWithdrawPage.getAvailableWithdrawAmount();
        withdrawFromAccount = walletWithdrawPage.getWithdrawFromAccount();
        withdrawFee = walletWithdrawPage.getWithdrawFee();
        WebElement withdrawFeeFree = walletWithdrawPage.getWithdrawFeeFree();
        withdrawFeeFreeTagDesc = "";
        receiveAmount = walletWithdrawPage.getReceiveAmount();

        if (!withdrawAmountSuffix.toLowerCase().contains(withdrawCryptoCurrency.toLowerCase())) {
            Assert.fail("Withdraw Amount: " + withdrawAmountWithSuffix + ", incorrect crypto currency (" + withdrawCryptoCurrency + ")");
        }

        if (!availableWithdrawAmount.toLowerCase().contains(withdrawCryptoCurrency.toLowerCase())) {
            Assert.fail("Available Withdraw: " + availableWithdrawAmount + ", incorrect crypto currency (" + withdrawCryptoCurrency + ")");
        }

        if (!withdrawFromAccount.toLowerCase().contains(withdrawCryptoCurrency.toLowerCase())) {
            Assert.fail("Withdraw From: " + withdrawFromAccount + ", incorrect crypto currency (" + withdrawCryptoCurrency + ")");
        }

        if (!withdrawFee.toLowerCase().contains(withdrawCryptoCurrency.toLowerCase())) {
            Assert.fail("Withdraw Fee: " + withdrawFee + ", incorrect crypto currency (" + withdrawCryptoCurrency + ")");
        }

        if (!receiveAmount.toLowerCase().contains(withdrawCryptoCurrency.toLowerCase())) {
            Assert.fail("Receive Amount: " + receiveAmount + ", incorrect crypto currency (" + withdrawCryptoCurrency + ")");
        }

        // Check receive amount based on withdraw fee charges
        String receiveAmountWithoutSuffix = receiveAmount.toLowerCase().replace(withdrawCryptoCurrency.toLowerCase(), "").trim();
        String withdrawFeeWithoutSuffix = withdrawFee.toLowerCase().replace(withdrawCryptoCurrency.toLowerCase(), "").trim();
        BigDecimal bdReceiveAmount = new BigDecimal(receiveAmountWithoutSuffix);
        BigDecimal bdWithdrawFee = new BigDecimal(withdrawFeeWithoutSuffix);
        BigDecimal bdWithdrawAmount = new BigDecimal(withdrawAmount);

        if (withdrawFeeFree != null) {
            // Without withdraw fee charges
            if (!bdReceiveAmount.equals(bdWithdrawAmount)) {
                Assert.fail("Withdraw Amount (" + withdrawAmount + ") & Receive Amount (" + bdReceiveAmount.toPlainString() + ") are not tally when exclude Withdraw Fee (" + bdWithdrawFee.toPlainString() + ")");
            }

            // Get free tag
            WebElement withdrawFeeFreeTag = walletWithdrawPage.getWithdrawFeeFreeTag();
            withdrawFeeFreeTagDesc = withdrawFeeFreeTag.getText().trim();

        } else {
            // With withdraw fee charges
            if (!(bdWithdrawAmount.subtract(bdWithdrawFee)).equals(bdReceiveAmount)) {
                Assert.fail("Withdraw Amount (" + withdrawAmount + ") & Receive Amount (" + bdReceiveAmount.toPlainString() + ") are not tally after deducted Withdraw Fee (" + bdWithdrawFee.toPlainString() + ")");
            }
        }

        walletWithdrawPage.printWithdrawInfo(withdrawAmountWithSuffix, withdrawFromAccount, withdrawFee, withdrawFeeFreeTagDesc, receiveAmount, withdrawCryptoNetwork, withdrawWalletAddress, availableWithdrawAmount);
    }

    public void checkWithdrawReviewInfo() {

        String reviewReceiveAmount = walletWithdrawPage.getReviewReceiveAmount();
        String reviewReceiveAmountSuffix = walletWithdrawPage.getReviewReceiveAmountSuffix();
        String reviewReceiveAmountWithSuffix = reviewReceiveAmount + " " + reviewReceiveAmountSuffix;
        String reviewWithdrawFromAccount = walletWithdrawPage.getReviewWithdrawFromAccount();
        String reviewWithdrawAmount = walletWithdrawPage.getReviewWithdrawAmount();

        String reviewWithdrawFee = walletWithdrawPage.getReviewWithdrawFee();
        WebElement reviewWithdrawFeeFree = walletWithdrawPage.getReviewWithdrawFeeFree();
        String reviewWithdrawFeeFreeTagDesc = "";

        String reviewWithdrawAddress = walletWithdrawPage.getReviewWithdrawAddress();
        String reviewWithdrawCryptoNetwork = walletWithdrawPage.getReviewWithdrawCryptoNetwork();

        if (!reviewReceiveAmountWithSuffix.equalsIgnoreCase(receiveAmount)) {
            Assert.fail("Receive Amount (Review): " + reviewReceiveAmountWithSuffix + ", not tally with actual value: " + receiveAmount);
        }

        if (!reviewWithdrawFromAccount.equalsIgnoreCase(withdrawFromAccount)) {
            Assert.fail("Withdraw From (Review): " + reviewWithdrawFromAccount + ", not tally with actual value: " + withdrawFromAccount);
        }

        if (!reviewWithdrawCryptoNetwork.toLowerCase().contains(withdrawCryptoNetwork.toLowerCase())) {
            Assert.fail("Crypto Network (Review): " + reviewWithdrawCryptoNetwork + ", not tally with actual value: " + withdrawCryptoNetwork);
        }

        if (!reviewWithdrawAddress.equalsIgnoreCase(withdrawWalletAddress)) {
            Assert.fail("Withdraw Address (Review): " + reviewWithdrawAddress + ", not tally with actual value: " + withdrawWalletAddress);
        }

        if (!reviewWithdrawAmount.equalsIgnoreCase(withdrawAmountWithSuffix)) {
            Assert.fail("Available Withdraw (Review): " + reviewWithdrawAmount + ", not tally with actual value: " + withdrawAmountWithSuffix);
        }

        if (!reviewWithdrawFee.equalsIgnoreCase(withdrawFee)) {
            Assert.fail("Withdraw Fee (Review): " + reviewWithdrawFee + ", not tally with actual value: " + withdrawFee);
        }

        if (reviewWithdrawFeeFree != null) {
            // Get withdraw fee free tag
            WebElement reviewWithdrawFeeFreeTag = walletWithdrawPage.getReviewWithdrawFeeFreeTag();
            reviewWithdrawFeeFreeTagDesc = reviewWithdrawFeeFreeTag.getText().trim();
        }

        if (!reviewWithdrawFeeFreeTagDesc.equalsIgnoreCase(withdrawFeeFreeTagDesc)) {
            Assert.fail("Withdraw Fee Tag (Review): " + reviewWithdrawFeeFreeTagDesc + ", not tally with actual value: " + withdrawFeeFreeTagDesc);
        }

        walletWithdrawPage.printReviewWithdrawInfo(reviewWithdrawAmount, reviewWithdrawFromAccount, reviewWithdrawFee, reviewWithdrawFeeFreeTagDesc, reviewReceiveAmountWithSuffix, reviewWithdrawCryptoNetwork, reviewWithdrawAddress);
    }

    public void checkPaymentDetails() {
        String orderCryptoCurrency = walletWithdrawPage.getOrderWithdrawCryptoCurrency();
        String orderCryptoNetwork = walletWithdrawPage.getOrderWithdrawCryptoNetwork();
        String orderWalletAddress = walletWithdrawPage.getOrderWithdrawWalletAddress();
        String orderWithdrawFromAccount = walletWithdrawPage.getOrderWithdrawFromAccount();
        String orderWithdrawAmount = walletWithdrawPage.getOrderWithdrawAmount();
        WebElement e = walletWithdrawPage.getOrderWithdrawFee();
        String orderWithdrawFee = "", orderWithdrawFeeFreeTag = "";
        String orderReceiveAmount = walletWithdrawPage.getOrderWithdrawReceiveAmount();

        if (e != null) {
            orderWithdrawFee = e.getText();
            orderWithdrawFeeFreeTag = walletWithdrawPage.getOrderWithdrawFeeFreeTag();
        }

        if (!orderCryptoCurrency.equalsIgnoreCase(withdrawCryptoCurrency)) {
            Assert.fail("Crypto Currency (Order): " + orderCryptoCurrency + ", not tally with actual value: " + withdrawCryptoCurrency);
        }

        if (!orderCryptoNetwork.equalsIgnoreCase(withdrawCryptoNetwork)) {
            Assert.fail("Crypto Currency (Order): " + orderCryptoNetwork + ", not tally with actual value: " + withdrawCryptoNetwork);
        }

        if (!orderWalletAddress.equalsIgnoreCase(withdrawWalletAddress)) {
            Assert.fail("Withdraw Address (Order): " + orderWalletAddress + ", not tally with actual value: " + withdrawWalletAddress);
        }

        if (!orderWithdrawFromAccount.equalsIgnoreCase(withdrawFromAccount)) {
            Assert.fail("Withdraw From (Order): " + orderWithdrawFromAccount + ", not tally with actual value: " + withdrawFromAccount);
        }

        if (!orderWithdrawAmount.equalsIgnoreCase(withdrawAmountWithSuffix)) {
            Assert.fail("Withdraw Amount (Order): " + orderWithdrawAmount + ", not tally with actual value: " + withdrawAmount);
        }

        if (!orderReceiveAmount.equalsIgnoreCase(receiveAmount)) {
            Assert.fail("Receive Amount (Order): " + orderReceiveAmount + ", not tally with actual value: " + receiveAmount);
        }

        if (!orderWithdrawFee.equalsIgnoreCase(withdrawFee)) {
            Assert.fail("Withdraw Fee (Order): " + orderWithdrawFee + ", not tally with actual value: " + withdrawFee);
        }

        if (!orderWithdrawFeeFreeTag.equalsIgnoreCase(withdrawFeeFreeTagDesc)) {
            Assert.fail("Withdraw Fee Free Tag (Order): " + orderWithdrawFeeFreeTag + ", not tally with actual value: " + withdrawFeeFreeTagDesc);
        }

    }

    public void compareWalletCryptoAddress() {
        String cryptoAddressDisplayVal = walletDepositPage.getCryptoWithdrawalAddress();
        String copyText = walletDepositPage.clickCryptoAddressCopyBtn();

        if (!copyText.equals(cryptoAddressDisplayVal)) {
            Assert.fail("Copied Wallet Address mismatch with display value");
        }
    }

}
