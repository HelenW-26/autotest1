package newcrm.testcases.cptestcases.wallet;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.wallet.CPWalletCryptoHome;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class WalletHomeTestCases extends BaseTestCaseNew {

    private CPMenu menu;
    private CPLogin login;
    private Factor myfactor;
    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) {
        checkBrandAccessible();
//        checkValidLoginSession();

        myfactor = getFactorNew();
        menu = myfactor.newInstance(CPMenu.class);
        login = getLogin();

        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
        menu.goToMenu(CPMenuName.WALLET_HOME);
        menu.goToMenu(CPMenuName.CloseVCard);

    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_Balance_View)
    public void testWalletHomeAssetsCheck() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.checkBalanceView();
        walletDeposit.checkBalanceBtn();
        walletDeposit.checkBalanceBtnDeposit();
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_CryptoList)
    public void testWalletHomeCryptoListCheck() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.validateWalletTable();
        walletDeposit.validateCryptoData("USDT");
        walletDeposit.validateCryptoData("BTC");
        walletDeposit.validateCryptoData("ETH");
        walletDeposit.validateCryptoData("USDC");
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_DropdownMenu)
    public void testWalletHomeDropdownMenuCheck() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.selectCrypto("USDT");
        walletDeposit.selectCrypto("BTC");
        walletDeposit.selectCrypto("ETH");
        walletDeposit.selectCrypto("USDC");
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_DropdownMore)
    public void testWalletHomeDropdownMoreCheck() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        List<String> coinList = Arrays.asList("USD", "GBP", "CAD", "AUD", "EUR", "SGD", "NZD", "HKD", "JPY", "PLN");
        for (String coinName : coinList) {
            walletDeposit.selectCryptoEqualValue(coinName);
        }
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_RealDeposit)
    public void testWalletHomeRealDeposit() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.wallectIntoDeposit();
        walletDeposit.wallectDepositOptions("USDT", "Tron(TRC20)");
        walletDeposit.wallectDepositOptions("USDT", "Ethereum(ERC20)");

    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_RealWithdraw)
    public void wallectIntoWithdrawCreatRealOrder() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.wallectIntoWithdraw();
        walletDeposit.wallectWithdrawCreatRealOrderUSDT();
    }
    // 当前钱包只在AU&VT环境部署
    private void checkBrandAccessible() {
        // Skip if not AU & VFSC
        if (Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) && Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString())||
                Brand.equalsIgnoreCase(GlobalProperties.BRAND.VT.toString())){
            LogUtils.info("Run wallet case");
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_Balance_View ,groups = {"CP_Home_Wallet"})
    public void testWalletHomeAssetsCheckUAT() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletFundingPage();
        walletDeposit.checkBalanceViewUAT();
        walletDeposit.checkBalanceBtnUAT();
        walletDeposit.checkBalanceBtnDepositUAT();
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_CryptoList ,groups = {"CP_Home_Wallet"})
    public void testWalletHomeCryptoListCheckUAT() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletFundingPage();
        walletDeposit.validateWalletTableFunding();
        walletDeposit.validateCryptoDataUAT("USDT");
        walletDeposit.validateCryptoDataUAT("BTC");
        walletDeposit.validateCryptoDataUAT("ETH");
        walletDeposit.validateCryptoDataUAT("USDC");
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_DropdownMenu ,groups = {"CP_Home_Wallet"})
    public void testWalletHomeDropdownMenuCheckUAT() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletFundingPage();
        walletDeposit.selectCryptoUAT("USDT");
        walletDeposit.selectCryptoUAT("BTC");
        walletDeposit.selectCryptoUAT("ETH");
        walletDeposit.selectCryptoUAT("USDC");
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_DropdownMore ,groups = {"CP_Home_Wallet"})
    public void testWalletHomeDropdownMoreCheckUAT() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletFundingPage();
        List<String> coinList = Arrays.asList("USD", "GBP", "CAD", "AUD", "EUR", "SGD", "NZD", "HKD", "JPY", "PLN");
        for (String coinName : coinList) {
            walletDeposit.selectCryptoEqualValueUAT(coinName);
        }
    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_RealDeposit ,groups = {"CP_Home_Wallet"})
    public void testWalletHomeRealDepositUAT() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.wallectIntoDeposit();
        walletDeposit.wallectDepositOptions("USDT", "Tron(TRC20)");
        walletDeposit.wallectDepositOptions("USDT", "Ethereum(ERC20)");

    }
    @Test(priority = 2, description = testCaseDescUtils.CPWALLET_HOME_RealWithdraw ,groups = {"CP_Home_Wallet"})
    public void wallectIntoWithdrawCreatRealOrderUAT() {
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.wallectIntoWithdraw();
        walletDeposit.wallectWithdrawCreatRealOrderUSDT();
    }


}
