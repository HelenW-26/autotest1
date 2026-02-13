package newcrm.business.businessbase.wallet;

import newcrm.pages.clientpages.wallet.WalletCardHomePage;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * @Author wesley
 * @Description
 **/
public class CPWalletCardHome {
    protected WalletCardHomePage walletCardHomePage;
    public CPWalletCardHome(WebDriver driver) {
        this.walletCardHomePage = new WalletCardHomePage(driver);
    }

    public void checkWalletCardHomeAvailable(){
        walletCardHomePage.checkWalletCardHomeAvailable();
    }
    public void checkPreMAXTransactions(){
        walletCardHomePage.checkPreMAXTransactions();
    }
    public void checkWalletCardDeposit(){
        walletCardHomePage.checkWalletCardDeposit();
    }
    public void checkWalletCardPayingWith(Map<String,String> availableBalanceList){
        walletCardHomePage.checkWalletCardPayingWith(availableBalanceList);
    }
    public void checkWalletCardManageCard(){
        walletCardHomePage.checkWalletCardManageCard();
    }
    public void cardManagementBackToCard(){
        walletCardHomePage.cardManagementBackToCard();
    }
    public void checkWalletRebates(){
        walletCardHomePage.checkWalletRebates();
    }
    public void checkWalletCardRecentTransactions(){
        walletCardHomePage.checkWalletCardRecentTransactions();
    }
    public void checkWalletCardLimits(){
        walletCardHomePage.checkWalletCardLimits();
    }
    public void checkWalletCardDetails(){
        walletCardHomePage.checkWalletCardDetails();
    }
    public void changeCardStatus(){
        walletCardHomePage.freezeCard();
        walletCardHomePage.cardManagementBackToCard();
        walletCardHomePage.unFreezeCard();
    }
    public void changeAutoCashback(String to){
        walletCardHomePage.changeAutoCashback(to);
        walletCardHomePage.rebatesBackToCardPage();
    }

    public void checkCardAllTransactions(){
        walletCardHomePage.cardSpendingOrdersList();
    }

    public void checkCardAllAuthorizations(){
        walletCardHomePage.cardAuthorizationsOrdersList();
    }

    /**
     *
     * @param transactionType Purchase/Refund
     */
    public void checkCardAllTransactionsDetails(String transactionType){
        walletCardHomePage.cardSpendingOrdersDetails(transactionType);
    }

}
