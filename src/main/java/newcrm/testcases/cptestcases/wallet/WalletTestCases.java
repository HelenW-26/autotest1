package newcrm.testcases.cptestcases.wallet;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.wallet.CPWalletCryptoDeposit;
import newcrm.business.businessbase.wallet.CPWalletCryptoWithdraw;
import newcrm.business.businessbase.wallet.CPWalletTransactionHistory;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.WalletDepositMethod;
import newcrm.global.GlobalProperties.WalletWithdrawMethod;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.pages.clientpages.wallet.WalletCryptoTransactionHistoryPage.HistoryDetails;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.lang.reflect.Method;

public class WalletTestCases extends BaseTestCaseNew {

    private CPMenu menu;
    private Factor myfactor;
    private CPLogin login;

    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) {
        checkBrandAccessible();
        myfactor = getFactorNew();
        login = getLogin();

//        checkValidLoginSession();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }


    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_CRYPTO_BITCOIN, groups= {"CP_Wallet"})
    public void testWalletDepositCryptoBTC() {
        testCommonCryptoDeposit(WalletDepositMethod.CryptoBTC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_CRYPTO_ETH)
    public void testWalletDepositCryptoETH() {
        testCommonCryptoDeposit(WalletDepositMethod.CryptoETH);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_CRYPTO_USDC)
    public void testWalletDepositCryptoUSDC() {
        testCommonCryptoDeposit(WalletDepositMethod.CryptoUSDC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_CRYPTO_USDT, groups= {"CP_Wallet"})
    public void testWalletDepositCryptoUSDTERC() {
        testCommonCryptoDeposit(WalletDepositMethod.CryptoUSDT_ERC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_CRYPTO_USDT)
    public void testWalletDepositCryptoUSDTTRC() {
        testCommonCryptoDeposit(WalletDepositMethod.CryptoUSDT_TRC);
    }


    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_HISTORY_CRYPTO_BITCOIN)
    public void testWalletDepositHistoryCryptoBTC() {
        testCommonCryptoDepositHistory(WalletDepositMethod.CryptoBTC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_HISTORY_CRYPTO_ETH)
    public void testWalletDepositHistoryCryptoETH() {
        testCommonCryptoDepositHistory(WalletDepositMethod.CryptoETH);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_HISTORY_CRYPTO_USDC)
    public void testWalletDepositHistoryCryptoUSDC() {
        testCommonCryptoDepositHistory(WalletDepositMethod.CryptoUSDC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_HISTORY_CRYPTO_USDT)
    public void testWalletDepositHistoryCryptoUSDTERC() {
        testCommonCryptoDepositHistory(WalletDepositMethod.CryptoUSDT_ERC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_DEPOSIT_HISTORY_CRYPTO_USDT)
    public void testWalletDepositHistoryCryptoUSDTTRC() {
        testCommonCryptoDepositHistory(WalletDepositMethod.CryptoUSDT_TRC);
    }


    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_WITHDRAW_CRYPTO_BITCOIN)
    public void testWalletWithdrawCryptoBTC() {
        testCommonCryptoWithdraw(WalletWithdrawMethod.CryptoBTC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_WITHDRAW_CRYPTO_ETH)
    public void testWalletWithdrawCryptoETH() {
        testCommonCryptoWithdraw(WalletWithdrawMethod.CryptoETH);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_WITHDRAW_CRYPTO_USDC, groups= {"CP_Wallet"})
    public void testWalletWithdrawCryptoUSDC() {
        testCommonCryptoWithdraw(WalletWithdrawMethod.CryptoUSDC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_WITHDRAW_CRYPTO_USDT, groups= {"CP_Wallet"})
    public void testWalletWithdrawCryptoUSDTERC() {
        testCommonCryptoWithdraw(WalletWithdrawMethod.CryptoUSDT_ERC);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_WITHDRAW_CRYPTO_USDT)
    public void testWalletWithdrawCryptoUSDTTRC() {
        testCommonCryptoWithdraw(WalletWithdrawMethod.CryptoUSDT_TRC);
    }

    private void testCommonCryptoDeposit(WalletDepositMethod method) {
        CPWalletCryptoDeposit walletDeposit = myfactor.newInstance(CPWalletCryptoDeposit.class);

        menu.walletCryptoDesc = method.getCryptoCurrencyDisplayDesc();
        menu.goToMenu(CPMenuName.WALLET_DEPOSIT);

        // Deposit
        cryptoDepositProcess(walletDeposit, method);
        // Deposit History
        cryptoDepositHistoryProcess(walletDeposit, method);

        System.out.println("***Test " + method.getCryptoCurrencyDisplayDesc() + " crypto deposit succeed!!********");
    }

    private void testCommonCryptoDepositHistory(WalletDepositMethod method) {
        CPWalletCryptoDeposit walletDeposit = myfactor.newInstance(CPWalletCryptoDeposit.class);

        menu.walletCryptoDesc = method.getCryptoCurrencyDisplayDesc();
        menu.goToMenu(CPMenuName.WALLET_DEPOSIT);

        cryptoDepositHistoryProcess(walletDeposit, method);

        System.out.println("***Test " + method.getCryptoCurrencyDisplayDesc() + " crypto deposit history succeed!!********");
    }

    private void testCommonCryptoWithdraw(WalletWithdrawMethod method) {
        CPWalletCryptoWithdraw walletWithdraw = myfactor.newInstance(CPWalletCryptoWithdraw.class);

        menu.walletCryptoDesc = method.getCryptoCurrencyDisplayDesc();
        menu.goToMenu(CPMenuName.WALLET_WITHDRAW);

        // Withdraw
        cryptoWithdrawProcess(walletWithdraw, method);
        // Withdraw History
//        cryptoWithdrawHistoryProcess(walletWithdraw, method);

        System.out.println("***Test " + method.getCryptoCurrencyDisplayDesc() + " crypto withdraw succeed!!********");
    }

    protected void cryptoDepositProcess(CPWalletCryptoDeposit walletDeposit, WalletDepositMethod method) {
        //Step 1 - Choose Coin
        // Wait for wallet deposit page content to load
        walletDeposit.waitLoadingWalletDepositContent();
        // Verify cryptocurrency value selected in Wallet Deposit Page is same as crypto selected in Wallet Home Page
        walletDeposit.checkSelectedCryptoCurrency(method);

        // Step 2 - Choose Network
        // Select crypto network
        walletDeposit.setCryptoNetwork(method);
        // Wait for crypto deposit address to load after selected crypto network

        // Step 3 - Deposit Address
        walletDeposit.waitLoadingDepositCryptoAddressContent();
        // Check if QR Code is loaded
        walletDeposit.checkExistsQRCode();
        // Compare crypto deposit address display value with copied crypto deposit address value
        walletDeposit.compareWalletCryptoAddress();
    }

    protected void cryptoWithdrawProcess(CPWalletCryptoWithdraw walletWithdraw, WalletWithdrawMethod method) {
        walletWithdraw.method = method;
        walletWithdraw.withdrawCryptoCurrency = method.getCryptoCurrencyDisplayDesc();
        walletWithdraw.withdrawCryptoNetwork = method.getCryptoNetworkDisplayDesc();

        // Step 1 - Choose Coin
        // Wait for wallet withdraw page content to load
        walletWithdraw.waitLoadingWalletWithdrawContent();
        // Verify cryptocurrency value selected in Wallet Withdraw Page is same as crypto selected in Wallet Home Page
        walletWithdraw.checkSelectedCryptoCurrency();

        // Wallet address & minimum withdraw amount

        switch (method) {
            case CryptoBTC:
                walletWithdraw.withdrawWalletAddress = "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
                walletWithdraw.withdrawAmount = "0.00150001";
                break;
            case CryptoETH:
                walletWithdraw.withdrawWalletAddress = "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
                walletWithdraw.withdrawAmount = "0.02000006";
                break;
            case CryptoUSDC:
                walletWithdraw.withdrawWalletAddress = "0x6dba6f6b122038854e299c3033757aa681ec2170";
                walletWithdraw.withdrawAmount = "30.0001";
                break;
            case CryptoUSDT_ERC:
                walletWithdraw.withdrawWalletAddress = "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
                walletWithdraw.withdrawAmount = "30.0001";
                break;
            case CryptoUSDT_TRC:
                walletWithdraw.withdrawWalletAddress = "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
                walletWithdraw.withdrawAmount = "30.0001";
                break;
            default:
                break;
        }

        // Step 2 - Withdraw To
        // Set crypto to address
        walletWithdraw.setCryptoToAddress(walletWithdraw.withdrawWalletAddress);
        // Select crypto network
        walletWithdraw.withdrawCryptoNetwork = walletWithdraw.setCryptoNetwork();
        // Verify withdraw to info
        walletWithdraw.checkValidWithdrawCryptoAddress();

        // Step 3 - Withdraw Amount
        // Wait for wallet amount content to load
        walletWithdraw.waitLoadingWalletWithdrawAmountContent();
        // Check minimum withdraw amount
        boolean bIsMinWithdrawAmount = walletWithdraw.checkMinWithdrawAmount();
        // Set withdraw amount when account balance > min withdraw amount
        if (bIsMinWithdrawAmount) {
            walletWithdraw.withdrawAmount = walletWithdraw.getMinWithdrawAmount();
            walletWithdraw.withdrawAmount = walletWithdraw.withdrawAmount.toLowerCase().replace(walletWithdraw.withdrawCryptoCurrency.toLowerCase(), "").trim();;
            GlobalMethods.printDebugInfo("Min Withdraw Amount: " + walletWithdraw.withdrawAmount);
        } else {
            walletWithdraw.setWithdrawAmount();
        }
        // Verify withdraw amount
        walletWithdraw.checkValidWithdrawAmount();
        // Verify withdraw info
        walletWithdraw.checkWithdrawInfo();
        // Submit wallet withdraw
        walletWithdraw.submitWithdrawal();

        // Check Payment Info
//        walletWithdraw.waitLoadingWalletWithdrawOrderContent();
//        walletWithdraw.checkPaymentDetails();
    }

    private void cryptoDepositHistoryProcess(CPWalletCryptoDeposit walletDeposit, WalletDepositMethod method) {
        // Wait for wallet deposit history content to load
        walletDeposit.waitLoadingWalletDepositHistoryContent();

        // Get first deposit record
        CPWalletTransactionHistory walletHistory = myfactor.newInstance(CPWalletTransactionHistory.class);

        // Check no. of deposit history columns & positions
//        walletHistory.compareDepositHistoryColumns();

        // Check whether deposit history is empty
        boolean bIsEmptyDepositHistory = walletHistory.checkEmptyDepositHistory();

        if (!bIsEmptyDepositHistory) {
            // Get first deposit record
            walletHistory.getFirstDeposit();

            // Get deposit details
            walletHistory.clickDetailsBtn();
            walletHistory.waitLoadingWalletDepositDetailsContent();
            HistoryDetails historyDetails  = walletHistory.getDepositDetails();
            walletHistory.closeDepositDetailsDialog();
        }
    }

    private void checkBrandAccessible() {
        // Skip if not AU & VFSC
        if (Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) && Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString())||
                Brand.equalsIgnoreCase(GlobalProperties.BRAND.VT.toString())){
            LogUtils.info("Run wallet case");
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
//        if (!Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) ||
//                (Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) && !(Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString()) ||
//                        Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC.toString())))) {
//
//            throw new SkipException("Skipping this test intentionally.");
//        }
    }

}
