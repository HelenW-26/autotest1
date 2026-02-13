package newcrm.testcases.cptestcases.wallet;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.CPRegisterGold;
import newcrm.business.businessbase.wallet.CPWalletOpenAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.cptestcases.RegisterGoldTestcases;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import utils.LogUtils;

import java.lang.reflect.Method;

public class WalletRegisterTestCases extends BaseTestCaseNew {
    public RegisterGoldTestcases regGoldTc = new RegisterGoldTestcases();
    private static Factor myfactor;

    @Override
    protected void login() {
        LogUtils.info("Do not need to login");
    }

    @Override
    @BeforeMethod(alwaysRun=true)
    public void goToCpHomePage() {
        LogUtils.info("Do not need go to home page");
    }

    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) {

        myfactor = getFactorNew();

        checkBrandAccessible();
    }

    protected void testWalletOpenAccount(boolean bIsCheck, String country) throws Exception {

        CPRegisterGold reg = myfactor.newInstance(CPRegisterGold.class);
        CPWalletOpenAccount cp = myfactor.newInstance(CPWalletOpenAccount.class);
        CPMenu menu = myfactor.newInstance(CPMenu.class);

        PLATFORM platform = PLATFORM.MT5;
        ACCOUNTTYPE accountType = ACCOUNTTYPE.STANDARD_STP;
        CURRENCY currency = CURRENCY.USD;

        regGoldTc.generateUserTestData(reg);
        regGoldTc.navigateTraderUrl(reg, regGoldTc.ibCode, "","");
        country = regGoldTc.getCountry(country, true);

        if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()))
        {
            throw new SkipException("Skipping this test intentionally.");
        }
        else {
            // Test Data
            regGoldTc.testSiteRegistrationInfo(reg, country);
            regGoldTc.goToPersonalDetailPage(reg);
            reg.closeProfileVerificationDialog();

            // Activate wallet
            System.out.println("***Wallet Activation***");
            menu.goToMenu(CPMenuName.WALLET_HOME);
            menu.goToMenu(CPMenuName.CloseVCard);
            cp.activateWallet();

            // Personal Details
            regGoldTc.verifyPersonalDetails_withLinkPhone(reg);
            regGoldTc.fillPersonalDetails(reg);

            // Setup account
            regGoldTc.setupAccount(reg, platform, accountType, currency);

            reg.submitKYC_Lvl1();
            Assert.assertTrue(cp.checkActivationUnderReview(), "Activation submit failure or error occurs!");
        }

        //check info
        if(bIsCheck) {
            reg.checkUserInfo(regGoldTc.email, dbenv, dbBrand, dbRegulator);

            // Audit account
            regGoldTc.auditMainAccount(reg, platform);

            // Check wallet activation status after account audited
            LogUtils.info("***Check wallet activation status after account audited***");
            menu.goToMenu(CPMenuName.HOME);
            menu.goToMenu(CPMenuName.WALLET_HOME);
            menu.goToMenu(CPMenuName.CloseVCard);
            //因alpha&uat性能不足，导致kafka消费延迟，需要再次点击activate now，且开发无修改此问题计划，与QA沟通后最后一步再次点击算通过
            cp.activateNow();
            cp.checkAccountActivated();
        }
        else
        {
            LogUtils.info("Skip account existence check");
        }

        reg.printUserRegisterInfo();
    }

    /**
     * 成功开户钱包后开通vcard
     */
    public void testWalletOpenVCard(){



    }
    private void checkBrandAccessible() {
        // Skip if not AU & VFSC
        if (!Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) ||
                (Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) &&
                        !(Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC.toString())))) {

            throw new SkipException("Skipping this test intentionally.");
        }
    }

}
