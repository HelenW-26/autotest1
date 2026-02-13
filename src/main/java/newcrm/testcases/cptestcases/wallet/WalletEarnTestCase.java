package newcrm.testcases.cptestcases.wallet;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.wallet.CPWalletCryptoHome;
import newcrm.business.businessbase.wallet.CPWalletEarn;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @Author wesley
 * @Description
 **/
public class WalletEarnTestCase extends BaseTestCaseNew {
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
        menu.changeLanguage("English");
        menu.goToMenu(GlobalProperties.CPMenuName.HOME);
        menu.goToMenu(GlobalProperties.CPMenuName.CloseVCard);
    }
    private void checkBrandAccessible() {
        // Skip if not AU & VFSC
        if (!Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) ||
                (Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) &&
                        !(Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC.toString())))) {

            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = testCaseDescUtils.WALLET_HOME_OVERVIEW)
    public void testWalletOverviewPage(){
        menu.goToMenu(GlobalProperties.CPMenuName.WALLET_HOME);
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletOverview();
    }
    @Test(priority = 2, description = testCaseDescUtils.WALLET_HOME_FUNDING)
    public void testWalletFundingPage(){
        menu.goToMenu(GlobalProperties.CPMenuName.WALLET_HOME);
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletFunding();
    }
    @Test(priority = 2, description = testCaseDescUtils.WALLET_HOME_EARN)
    public void testWalletEarnPage(){
        menu.goToMenu(GlobalProperties.CPMenuName.WALLET_HOME);
        CPWalletCryptoHome walletDeposit = myfactor.newInstance(CPWalletCryptoHome.class);
        walletDeposit.walletEarn();
    }

    @Test(priority = 2, description = testCaseDescUtils.WALLET_HOME_EARN_HOME)
    public void testWalletEarnHomePage(){
        menu.goToMenu(GlobalProperties.CPMenuName.WALLET_EARN);
        CPWalletEarn walletEarn = myfactor.newInstance(CPWalletEarn.class);
        walletEarn.checkEarnHomePage();
    }
}
