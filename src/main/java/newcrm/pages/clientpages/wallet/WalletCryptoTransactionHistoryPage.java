package newcrm.pages.clientpages.wallet;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class WalletCryptoTransactionHistoryPage extends Page {

    public WalletCryptoTransactionHistoryPage(WebDriver driver) {
        super(driver);
    }

    public class History {

        protected String currency;
        protected String currencyNetwork;
        protected String txnHash;
        protected String depositTo;
        protected String amount;
        protected String txnStatus;
        protected String txnTime;

        public History(String currency, String currencyNetwork, String txnHash, String depositTo, String amount, String txnStatus, String txnTime) {
            this.currency = currency;
            this.currencyNetwork = currencyNetwork;
            this.txnHash = txnHash;
            this.depositTo = depositTo;
            this.amount = amount;
            this.txnStatus = txnStatus;
            this.txnTime = txnTime;
        }

        public void printDepositHistory() {
            System.out.println("************Deposit History************");
            System.out.printf("%-20s : %s\n", "Currency", currency);
            System.out.printf("%-20s : %s\n", "Currency Network", currencyNetwork);
            System.out.printf("%-20s : %s\n", "Txn Hash", txnHash);
            System.out.printf("%-20s : %s\n", "Deposit To", depositTo);
            System.out.printf("%-20s : %s\n", "Amount", amount);
            System.out.printf("%-20s : %s\n", "Status", txnStatus);
            System.out.printf("%-20s : %s\n", "Time", txnTime);
        }

    }

    public class HistoryDetails {

        protected String amount;
        protected String currencyNetwork;
        protected String txnHash;
        protected String depositTo;
        protected String txnStatus;
        protected String txnTime;
        protected String note;
        protected String txnId;
        protected String toAddress;

        public String getToAddress() {
            return this.toAddress;
        }

        public String getTxnHash() {
            return this.txnHash;
        }

        public HistoryDetails(String amount, String currencyNetwork, String txnHash, String depositTo, String txnStatus, String txnTime, String note, String txnId, String toAddress) {
            this.amount = amount;
            this.currencyNetwork = currencyNetwork;
            this.txnHash = txnHash;
            this.depositTo = depositTo;
            this.txnStatus = txnStatus;
            this.txnTime = txnTime;
            this.note = note;
            this.txnId = txnId;
            this.toAddress = toAddress;
        }

        public void printDepositHistoryDetails() {
            System.out.println("************Deposit History Details************");
            System.out.printf("%-20s : %s\n", "Amount", amount);
            System.out.printf("%-20s : %s\n", "Transaction ID", txnId);
            System.out.printf("%-20s : %s\n", "Time", txnTime);
            System.out.printf("%-20s : %s\n", "Status", txnStatus);
            System.out.printf("%-20s : %s\n", "Deposit To", depositTo);
            System.out.printf("%-20s : %s\n", "Currency Network", currencyNetwork);
            System.out.printf("%-20s : %s\n", "To Address", toAddress);
            System.out.printf("%-20s : %s\n", "Txn Hash", txnHash);
            System.out.printf("%-20s : %s\n", "Note", note);
        }

    }

    public void waitLoadingWalletDepositDetailsContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class, 'details_content')]//div[@class='ht-dialog__title-content']"),"Deposit Details Content");
    }

    public void clickDetailsBtn() {
        triggerClickEvent(assertElementExists(By.xpath("(//div[@class='wallet-history']//table)[2]/tbody/tr[1]//button[@data-testid='button']"), "Deposit History Details button"));
    }

    protected WebElement getDepositDetailsAmountEle() {
        return assertElementExists(By.xpath("//span[@class='amount_text']"), "Deposit Details (Amount)");
    }

    // Deposit Details-Transaction ID
    protected WebElement getDepositDetailsTxnIdEle() {
        return assertElementExists(By.xpath("//div[contains(text(),'Transaction ID')]/..//*[contains(@class,'copy-container') or contains(@class,'value')]"), "Deposit Details (Transaction ID)");
    }

    // Deposit Details-Time
    protected WebElement getDepositDetailsTxnTimeEle() {
        return assertElementExists(By.xpath("//div[contains(text(),'Time')]/..//span[contains(@class,'value')]"), "Deposit Details (Time)");
    }

    // Deposit Details-Status
    protected WebElement getDepositDetailsStatusEle() {
        return assertElementExists(By.xpath("//div[@class='wallet-history']/div[@class='history-table-container']//div[@class='el-table__body-wrapper is-scrolling-none']//tr[1]/td[6]//span[@class='status-success' or @class='status-pending']"), "Deposit Details (Status)");
    }

    // Deposit Details-Deposit To
    protected WebElement getDepositDetailsDepositToEle() {
        return assertElementExists(By.xpath("//div[contains(text(),'Deposit To')]/..//*[@class='value']"), "Deposit Details (Deposit To)");
    }

    // Deposit Details-Network
    protected WebElement getDepositDetailsCurrencyNetworkEle() {
        return assertElementExists(By.xpath("//div[text()='Deposit Details']/../..//div[contains(text(),'Network')]/..//span"), "Deposit Details (Network)");
    }

    // Deposit Details-To Address
    protected WebElement getDepositDetailsToAddressEle() {
        return assertElementExists(By.xpath("//div[text()='Deposit Details']/../..//div[contains(text(),'To Address')]/following-sibling::*"), "Deposit Details (To Address)");
    }

    // Deposit Details-Txn Hash
    protected WebElement getDepositDetailsTxnHashEle() {
        return assertElementExists(By.xpath("//div[text()='Deposit Details']/../..//div[contains(text(),'Txn Hash')]/following-sibling::*"), "Deposit Details (Txn Hash)");
    }

    // Deposit Details-Note
    protected WebElement getDepositDetailsNoteEle() {
        return assertElementExists(By.xpath("//div[text()='Deposit Details']/../..//div[contains(text(),'Note')]/..//span"), "Deposit Details (Note)");
    }

    protected List<WebElement> getDepositHistoryColumnEles() {
        List<WebElement> els = new ArrayList<>();
        els.addAll(assertElementsExists(By.xpath("(//div[@class='wallet-history']//table)[1]/thead//th[not(contains(@style, 'display: none')) and not(contains(@class, 'hidden'))]"), "Deposit History Column"));
        els.addAll(assertElementsExists(By.xpath("(//div[@class='wallet-history']//table)[3]/thead//th[not(contains(@style, 'display: none')) and not(contains(@class, 'hidden'))]"), "Deposit History Column"));

        return els;
    }

    protected WebElement getDepositHistoryEle() {
        return assertElementExists(By.xpath("(//div[@class='wallet-history']//table)[2]/tbody/tr[1]"), "Deposit History");
    }

    protected WebElement getDepositDetailsDialogCloseBtn() {
        return assertElementExists(By.xpath("//div[@class='wallet-history']//div[@class='ht-dialog__close']"), "Deposit Details close button");
    }

    protected WebElement getDepositDetailsAmount() {
        return getDepositDetailsAmountEle();
    }

    protected WebElement getDepositDetailsTxnId() {
        return getDepositDetailsTxnIdEle();
    }

    protected WebElement getDepositDetailsTxnTime() {
        return getDepositDetailsTxnTimeEle();
    }

    protected WebElement getDepositDetailsStatus() {
        return getDepositDetailsStatusEle();
    }

    protected WebElement getDepositDetailsDepositTo() {
        return getDepositDetailsDepositToEle();
    }

    protected WebElement getDepositDetailsCurrencyNetwork() {
        return getDepositDetailsCurrencyNetworkEle();
    }

    protected WebElement getDepositDetailsToAddress() {
        return getDepositDetailsToAddressEle();
    }

    protected WebElement getDepositDetailsTxnHash() {
        return getDepositDetailsTxnHashEle();
    }

    protected WebElement getDepositDetailsNote() {
        return getDepositDetailsNoteEle();
    }

    public void closeDepositDetailsDialog() {
        triggerElementClickEvent(getDepositDetailsDialogCloseBtn());
    }

    public List<WebElement> getDepositHistoryColumn() {
        return getDepositHistoryColumnEles();
    }

    public History getFirstDeposit() {
        WebElement e = getDepositHistoryEle();

        return getHistory(e);
    }

    public HistoryDetails getDepositDetails() {
        List<WebElement> els = new ArrayList<>();

        // Get deposit details info
        els.add(getDepositDetailsAmount());
        els.add(getDepositDetailsTxnId());
        els.add(getDepositDetailsTxnTime());
        els.add(getDepositDetailsStatus());
        els.add(getDepositDetailsDepositTo());
        els.add(getDepositDetailsCurrencyNetwork());
        els.add(getDepositDetailsToAddress());
        els.add(getDepositDetailsTxnHash());
        els.add(getDepositDetailsNote());

        return getHistoryDetails(els);
    }

    protected History getHistory(WebElement ele) {
        String info = ele.getText();
        String[] values = info.split("\n");

        return
                new History(
                        values[0].trim(),
                        values[1].trim(),
                        values[2].trim(),
                        values[3].trim(),
                        values[4].trim(),
                        values[5].trim(),
                        values[6].trim()
                );
    }

    protected HistoryDetails getHistoryDetails(List<WebElement> els) {
        return
                new HistoryDetails(
                        els.get(0).getText().trim(),
                        els.get(1).getText().trim(),
                        els.get(2).getText().trim(),
                        els.get(3).getText().trim(),
                        els.get(4).getText().trim(),
                        els.get(5).getText().trim(),
                        els.get(6).getText().trim(),
                        els.get(7).getText().trim(),
                        els.get(8).getText().trim()
                );
    }

    public WebElement getDepositHistoryEmptyContent() {
        WebElement ele = null;

        try {

            ele = driver.findElement(By.xpath("(//div[@class='wallet-history']//table)[2]/following-sibling::div[@class='el-table__empty-block']"));
            GlobalMethods.printDebugInfo("No Deposit History record found");

        } catch (Exception e) {
            return ele;
        }

        return ele;
    }

}
