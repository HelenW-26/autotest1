package newcrm.testcases.cptestcases.wallet;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.wallet.CPWalletCardHome;
import newcrm.business.businessbase.wallet.CPWalletCryptoHome;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.factor.Factor;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author wesley
 * @Description
 **/
public class WalletCardHomeTestCases extends BaseTestCaseNew {
    private CPMenu menu;
    private CPLogin login;
    private Factor myfactor;
    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) {
        checkBrandAccessible();
        myfactor = getFactorNew();
        menu = myfactor.newInstance(CPMenu.class);
        login = getLogin();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CloseVCard);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);

    }
    private void checkBrandAccessible() {
        // Skip if not AU & VFSC
        if (!Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) ||
                (Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) &&
                        !(Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC.toString())))) {

            throw new SkipException("Skipping this test intentionally.");
        }
    }
    private Map<String,String> getAvailableBalanceList() {
        menu.goToMenu(CPMenuName.WALLET_HOME);
        CPWalletCryptoHome cryptoHome = myfactor.newInstance(CPWalletCryptoHome.class);
        cryptoHome.walletFundingPage();
        return cryptoHome.getCryptoAvailableBalance();
    }
    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_HOME)
    public void testWalletCardHomeAssertCheck() {
        Map<String,String> availableBalanceList = getAvailableBalanceList();
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.checkWalletCardHomeAvailable();
        walletCardHome.checkPreMAXTransactions();
        walletCardHome.checkWalletCardDeposit();
        walletCardHome.checkWalletCardPayingWith(availableBalanceList);
        walletCardHome.checkWalletCardManageCard();
        walletCardHome.cardManagementBackToCard();
        walletCardHome.checkWalletRebates();
        walletCardHome.checkWalletCardLimits();
        walletCardHome.checkWalletCardRecentTransactions();
    }

    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_DETAILS)
    public void testWalletCardDetails(){
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.checkWalletCardDetails();
    }

    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_CHANGE_AUTO_CASHBACK)
    public void testWalletCardChangeAutoCashback(){
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.changeAutoCashback("on");
        walletCardHome.changeAutoCashback("off");
    }
    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_CHANGE_STATUS)
    public void testWalletCardChangeCardStatus(){
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.changeCardStatus();
    }

    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_ALL_TRANSACTIONS)
    public void testWalletCardAllTransactions(){
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.checkCardAllTransactions();
    }

    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_AUTHORIZATIONS)
    public void testWalletCardAuthorizations(){
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.checkCardAllAuthorizations();
    }

    @Test(priority = 2,description = testCaseDescUtils.WALLET_CARD_TRANSACTION_DETAILS)
    public void testWalletCardTransactionDetails(){
        menu.goToMenu(CPMenuName.WALLET_CARD);
        menu.goToMenu(CPMenuName.CLOSE_VCARD_TIPS);
        CPWalletCardHome walletCardHome = myfactor.newInstance(CPWalletCardHome.class);
        walletCardHome.checkCardAllTransactionsDetails("Purchase");
        menu.goToMenu(CPMenuName.WALLET_CARD);
        walletCardHome.checkCardAllTransactionsDetails("Refund");
    }
}
