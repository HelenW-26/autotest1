package newcrm.business.businessbase.wallet;

import newcrm.pages.clientpages.wallet.WalletCryptoTransactionHistoryPage;
import newcrm.pages.clientpages.wallet.WalletCryptoTransactionHistoryPage.History;
import newcrm.pages.clientpages.wallet.WalletCryptoTransactionHistoryPage.HistoryDetails;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class CPWalletTransactionHistory {

    protected WalletCryptoTransactionHistoryPage walletHistoryPage;

    public CPWalletTransactionHistory(WebDriver driver) {
        this.walletHistoryPage = new WalletCryptoTransactionHistoryPage(driver);
    }

    public void compareDepositHistoryColumns() {

        String[] cols = {"Coin", "Network", "Txn Hash", "Deposit To", "Amount", "Status", "Time", "Action"};
        boolean bIsColFound = false;

        List<WebElement> els = walletHistoryPage.getDepositHistoryColumn();

        if (els.size() != cols.length) {
            Assert.fail("No. of Deposit History Column mismatch");
        }

        for (WebElement ele : els) {
            // Get column text
            String colText = ele.getText();
            // Get current column index
            int idxCurrCol = els.indexOf(ele);
            int idxOriCol = -1;

            // Get column original index
            for (int i = 0; i < cols.length; i++) {
                String oriColText = cols[i];

                if (colText.contains(cols[i])) {
                    bIsColFound = true;
                    idxOriCol = i;
                    break;
                }

                bIsColFound = false;
            }

            // Check if column found
            if (!bIsColFound) {
                Assert.fail("Deposit History Column (" + colText + ") not found");
            }

            // Compare both original and current column index. If not same, might get/store wrong column data during deposit history process
            if (idxCurrCol != idxOriCol) {
                Assert.fail("Deposit History Column (" + colText + ") mismatch.");
            }
        }
    }

    public boolean checkEmptyDepositHistory() {
        WebElement e = walletHistoryPage.getDepositHistoryEmptyContent();

        return e != null;
    }

    public void clickDetailsBtn() {
        walletHistoryPage.clickDetailsBtn();
    }

    public void waitLoadingWalletDepositDetailsContent() {
        walletHistoryPage.waitLoadingWalletDepositDetailsContent();
    }

    public void getFirstDeposit() {
        // Get first deposit record
        History history = walletHistoryPage.getFirstDeposit();

        history.printDepositHistory();
    }

    public HistoryDetails getDepositDetails() {
        // Get deposit details record
        HistoryDetails history =  walletHistoryPage.getDepositDetails();

        history.printDepositHistoryDetails();

        return history;
    }

    public void closeDepositDetailsDialog() {
        walletHistoryPage.closeDepositDetailsDialog();
    }

}
