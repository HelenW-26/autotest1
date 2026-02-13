package newcrm.testcases.ibtestcases;

import adminBase.Login;
import newcrm.adminapi.AdminAPI;
import newcrm.business.adminbusiness.*;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.ibbase.account.IBDemoAccount;
import newcrm.business.businessbase.ibbase.account.IBProfile;
import newcrm.business.businessbase.ibbase.account.IBProgramRegistration;
import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.AdminMenuName;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.testcases.cptestcases.RegisterGoldTestcases;
import newcrm.testcases.cptestcases.RegisterTestcases;
import newcrm.utils.testCaseDescUtils;
import newcrm.global.GlobalProperties.BRAND;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class IBAccountTestCases extends RegisterGoldTestcases {


    private AdminAPI admin;

//    public Object data[][];
    protected IBRebateAccount ibRebateAccount;
    protected CPRegisterGold cp;
    protected CPLiveAccounts cpLiveAccounts;
    protected CPMenu cpMenu;
    protected IBDemoAccount ibDemoAccount;
    protected IBAccountReport ibAccountReport;
    protected IBProgramRegistration ibProgramRegistration;
    protected Login adminlogin;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;

    protected AdminExternalUser adminExternalUser;
    protected AdminAccountAudit adminAccountAudit;
    protected AdminAdditionalAccountAudit adminAdditionalAccountAudit;
    protected AdminMenu adminMenu;
    protected AdminClient adminClient;
    protected AdminRebateAccount adminRebateAccount;
    protected AdminIDPOAAudit adminIDPOAAudit;
    protected IBProfile ibProfile;
    protected AdminRebateAccountAgreement adminRebateAccountAgreement;


    private RegisterTestcases registerTestcases = new RegisterTestcases();
//    public RegisterGoldTestcases registerGoldTestcases = new RegisterGoldTestcases();
    private IBAdminAccountTestCases ibAdminAccountTestCases = new IBAdminAccountTestCases();
    private SystemSettingTestCases systemSettingTestCases = new SystemSettingTestCases();

    protected String clientEmail;
    protected String clientPwd;
    protected String clientName;
    protected String brand, server, testEnv, headless;
    protected String originalHandle, adminHandle;
    protected String OWSName;
    protected String OWSPass;
    protected Boolean tcSkip;
    protected Boolean prevTCFail = false;
    protected Boolean loggedOut = false;
    protected ITestContext context;
    private Factor myfactor;
    private WebDriver driver;
    protected static CPLogin login;

    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) throws InterruptedException {
        originalHandle = null;
        adminHandle = null;
        if (driver ==null){
            driver = getDriverNew();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        if (myfactor == null){
            myfactor = new Factor(TestEnv,Brand,Regulator,driver);
        }

        if(prevTCFail || loggedOut){
            login();
        }

        prevTCFail= false;
        tcSkip = false;
        loggedOut = false;
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
    }

    protected void initTestData(String brandData, String serverData, ITestContext contextData, String testEnv, String headless, Object[][] data) {
        brand = brandData;
        server = serverData;
        context = contextData;
        this.testEnv = testEnv;
        this.headless = headless;

        this.data = data;
        assertNotNull(data);
    }

    @AfterMethod(alwaysRun=true)
    public void tearDown(ITestResult result) {
        if(!tcSkip){
            if (result.getStatus() == ITestResult.FAILURE) {
                prevTCFail = true;
                if(adminHandle!=null) {
                    driver.switchTo().window(adminHandle);
                    driver.close();
                    driver.switchTo().window(originalHandle);
                }
                driver.navigate().to((String)data[0][6]);
                driver.navigate().refresh();
                driver.switchTo().defaultContent();
                clearLoginSession();
                logout();
                driver.navigate().refresh();
                driver.switchTo().defaultContent();
            }else{
                driver.navigate().refresh();
                driver.switchTo().defaultContent();
            }
        }

    }

    @Override
    protected void login()  {
        if (driver ==null){
            driver = getDriverNew();
        }
        Factor factor = getFactorNew();
        if (factor == null){
            UserConfig config = getConfigNew();
            WebDriver driver = getDriverNew();
            if (driver != null && config != null) {
                factor = new Factor(TestEnv, Brand, Regulator, driver);
                factorThreadLocal.set(factor);
            } else {
                Assert.fail("Factor not initialized and cannot recreate");
            }
        }

        CPLogin login = factor.newInstance(CPLogin.class, TraderURL);

        if (login == null) {
            Assert.fail("Login Initialization Failed");
        }

        UserConfig config = getConfigNew();
        if (config != null) {
            try {
                login.login(config.TraderName, config.TraderPass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String loginErrMsg = login.checkExistsLoginAlertMsg();

            if (loginErrMsg != null) {
                Assert.fail("An error occurred during login. Error Msg: " + loginErrMsg);
            }
        } else {
            Assert.fail("User config not found");
        }

        loginThreadLocal.set(login);
    }

    @Test(priority = 0, description = testCaseDescUtils.IBPROGRAM_ACCOUNTREGISTRATION)
    public void testIBProgramRegistration() throws Exception {
        Regulator = "VFSC";
        registerNewIBThroughIBProgram((String)data[0][3]);
        clearLoginSession();
        logout();
        loggedOut = true;
    }

    @Test(priority = 0, description = testCaseDescUtils.IBACCOUNT_HOMEPAGE_REFERRAL_LINK_REGISTER_CP_UPGRADE_IB)
    public void testIBReferralLinkAndCPUpgradeIB() throws Exception {
        Regulator = (String)data[0][0];
        funcIBReferralLinkAndCPUpgradeIB();
        loggedOut = true;
    }

    @Test(priority = 0, description = testCaseDescUtils.IBACCOUNT_CAMPAIGN_LINK)
    public void testIBCampaignLink() throws Exception {
        funcIBCampaignLink();
    }


    @Test(priority = 0, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_VERIFICATION)
    public void testIBReferralLinkVerificationV1V2CIMA() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use VFSC Account (VFSC1/VFSC2/CIMA - Same Links)
            myfactor = new Factor(testEnv,brand,(String)data[0][0],driver);

            funcIBReferralLinkVerification();
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 1, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_REGISTRATION)
    public void testIBReferralLinkRegistrationV1V2CIMA() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use VFSC Account (VFSC1/VFSC2/CIMA - Same Links)
            myfactor = new Factor(testEnv,brand,(String)data[0][0],driver);

            funcIBReferralLinkRegistration();
            logout();
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }

    }

    @Test(priority = 2, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_VERIFICATION)
    public void testIBReferralLinkVerificationASIC() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use ASIC Account
            myfactor = new Factor(testEnv,brand,(String)data[0][12],driver);

            funcIBReferralLinkVerification();
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 3, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_REGISTRATION)
    public void testIBReferralLinkRegistrationASIC() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use ASIC Account
            myfactor = new Factor(testEnv,brand,(String)data[0][12],driver);

            funcIBReferralLinkRegistration();
            logout();
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 4, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_VERIFICATION)
    public void testIBReferralLinkVerificationFCA() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use FCA Account
            myfactor = new Factor(testEnv,brand,(String)data[0][15],driver);

            funcIBReferralLinkVerification();
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 5, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_REGISTRATION)
    public void testIBReferralLinkRegistrationFCA() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use FCA Account
            myfactor = new Factor(testEnv,brand,(String)data[0][15],driver);

            funcIBReferralLinkRegistration();
            logout();
        } else {
//            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }

        //last test case, always skip after method
        tcSkip = true;
    }

    public void funcIBReferralLinkAndCPUpgradeIB() throws Exception {
        myfactor = new Factor(testEnv,brand,(String)data[0][0],driver);
        ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
        cpMenu = myfactor.newInstance(CPMenu.class);
        ibDemoAccount = myfactor.newInstance(IBDemoAccount.class);
        ibAccountReport = myfactor.newInstance(IBAccountReport.class);

        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminAccountAudit = myfactor.newInstance(AdminAccountAudit.class);
        adminClient = myfactor.newInstance(AdminClient.class);
        adminIDPOAAudit = myfactor.newInstance(AdminIDPOAAudit.class);
        adminRebateAccountAgreement = myfactor.newInstance(AdminRebateAccountAgreement.class);
        owsDashboard = myfactor.newInstance(OWSDashboard.class);


        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        cpMenu.changeLanguage("English");

        String ibAcc = getIBAcc();
        String affId = getAffId_NewTab();
        logout();
        String newLiveAcc = registerNewCPThroughIB(affId,"",true);
        // Skip CP Upgrade Client To IB flow - if not AU
        if (!brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())) {
            LogUtils.info("Skipping CP Upgrade Client To IB flow intentionally.");
        }else{
            testCPUpgradeClientToIB();
            // Capture ss
            ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkUpgradeClientToIB");
        }
        clearLoginSession();
        logout();
        verifyNewTradingAccountIsUnderIBDownline_HomePage(ibAcc, newLiveAcc);
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkNewDownlineIsUnderIB");
        logout();
    }

    public void funcIBReferralLinkVerification() throws Exception {
        ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
        cpMenu = myfactor.newInstance(CPMenu.class);
        ibDemoAccount = myfactor.newInstance(IBDemoAccount.class);
        ibAccountReport = myfactor.newInstance(IBAccountReport.class);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        cpMenu.changeLanguage("English");

        originalHandle = driver.getWindowHandle();

        String ibAcc = getIBAcc();
        String affId = getAffId_NewTab();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBREFERRALLINKSMENU);
        ibRebateAccount.verifyReferralLinks(affId, testEnv);
    }

    public void funcIBCampaignLink() throws Exception {

        //Skip MO and UM
        if(!brand.equalsIgnoreCase(BRAND.MO.toString()) && !brand.equalsIgnoreCase(BRAND.UM.toString())) {
            myfactor = new Factor(testEnv,brand,(String)data[0][0],driver);
            ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
            cpMenu = myfactor.newInstance(CPMenu.class);
            ibDemoAccount = myfactor.newInstance(IBDemoAccount.class);
            ibAccountReport = myfactor.newInstance(IBAccountReport.class);
            adminMenu = myfactor.newInstance(AdminMenu.class);
            owsDashboard = myfactor.newInstance(OWSDashboard.class);

            String ibAcc = getIBAcc();
            String campaignSourceID = createAndVerifyCampaignLink();
            String newLiveAcc = registerNewCPThroughIB("",campaignSourceID, true);
            clearLoginSession();
            driver.get((String)data[0][6]);
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
            logout();
            verifyNewTradingAccountIsUnderIBDownline_AccountReport(ibAcc, newLiveAcc, campaignSourceID);
            // Capture ss
            ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkCampaignLinkRegistrationIsUnderIB");
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    public String createAndVerifyCampaignLink() throws Exception {
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBCAMPAIGNLINKSMENU);

        ibRebateAccount.createNewCampaignLink();
        return ibRebateAccount.verifyShortCampaignLink();
    }

    public void funcIBReferralLinkRegistration() throws Exception {
        ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
        cpMenu = myfactor.newInstance(CPMenu.class);
        ibDemoAccount = myfactor.newInstance(IBDemoAccount.class);
        ibAccountReport = myfactor.newInstance(IBAccountReport.class);
        adminMenu = myfactor.newInstance(AdminMenu.class);
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        cpMenu.changeLanguage("English");

        originalHandle = driver.getWindowHandle();

        String ibAcc = getIBAcc();
        String affId = getAffId_NewTab();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBREFERRALLINKSMENU);

        //Demo Account Flow
        ibRebateAccount.navigateToDemoAccountURL(affId);
        String name = ibDemoAccount.registerIBDemoAccount(TraderURL, "Brazil");
        driver.close();
        driver.switchTo().window(originalHandle);
        //Verify Demo Account Dropped Under IB
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBCLIENTREPORT);
//        ibAccountReport.verifyDemoAccountInAccountReport_Name(name);
        List<String> demoAccountNames = ibAccountReport.retrieveDemoAccountNamesInClientReport();
        if(demoAccountNames.stream().anyMatch(s -> s.contains(name))){
            LogUtils.info(name + " - (Demo Flow) demo account is found under IB's Account Report.");
        }else{
            Assert.fail(name + " - (Demo Flow) demo account is NOT found under IB's Account Report.");
        }
        LogUtils.info("Demo Account creation flow verified");
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkV1V2DemoAccountRegistrationIsUnderIB");

        if(testEnv.equalsIgnoreCase("alpha")){
            //h5 Flow
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBREFERRALLINKSMENU);
            String phone = null;
            String countryCode = null;
            Integer attempt = 1;
            Boolean phoneAlreadyExist = true;
            while(phoneAlreadyExist && attempt<=5){
                ibRebateAccount.navigateToDownloadAppURL();
                countryCode = ibRebateAccount.setCountryCode();
                phone = ibRebateAccount.setPhoneNo();
                phoneAlreadyExist = ibRebateAccount.inputOTP("123456");
                if (phoneAlreadyExist){
                    LogUtils.info("Attempt "+attempt+": Phone Number ("+countryCode+" "+phone+") already exist");
                    attempt++;
                }
                if(attempt>5){
                    Assert.fail("Failed to register new demo account via H5 flow - Encountered phone number error for 5 attempts");
                }
                driver.close();
                driver.switchTo().window(originalHandle);
            }
            //Verify h5 (Demo) Account Dropped Under IB
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBCLIENTREPORT);
            List<String> newDemoAccountNames = ibAccountReport.retrieveDemoAccountNamesInClientReport();

            newDemoAccountNames.removeAll(demoAccountNames);
            LogUtils.info(newDemoAccountNames.get(0) + " - (h5 flow) demo account is found under IB's Account Report.");
            LogUtils.info("H5 flow verified");
            // Capture ss
            ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkV1V2H5DemoAccountRegistrationIsUnderIB");
        }

        logout();
        //Live Account Flow
        String newLiveAcc = registerNewCPThroughIB(affId,"", false);
        logout();
        //Verify Live Account Dropped Under IB
//        verifyNewTradingAccountIsUnderIBDownline_HomePage(ibAcc, newLiveAcc);

        //Verify Live Account Dropped Under IB - Account Report - Pending Accounts
        verifyNewTradingAccountIsUnderIBDownline_AccountReport_Pending(ibAcc, clientName);

        LogUtils.info("Live Account creation flow verified");
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkV1V2TradingAccountRegistrationIsUnderIB");
    }

    public void testCPUpgradeClientToIB() throws Exception {
        originalHandle = driver.getWindowHandle();
        try {
            cpMenu.goToMenu(GlobalProperties.CPMenuName.UPGRADETOIB);
        } catch (AssertionError e) {
            driver.switchTo().newWindow(WindowType.TAB);
            loginAdmin();
            searchClientThroughEmail(clientEmail);
            clientUploadPoiPoaDocs();
            adminHandle = driver.getWindowHandle();
            driver.switchTo().newWindow(WindowType.TAB);
            owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
//            owsLogin.login(OWSName,OWSPass);
            owsDashboard.auditPOIPOAFlow("","",email);
            driver.close();
            driver.switchTo().window(originalHandle);
            cpMenu.refresh();
            cpMenu.goToMenu(GlobalProperties.CPMenuName.UPGRADETOIB);
            driver.switchTo().newWindow(WindowType.TAB);
            driver.switchTo().window(adminHandle);
//            loginAdmin();
            auditIBAccount(clientEmail);
            driver.close();
            adminHandle = null;

//            driver.switchTo().newWindow(WindowType.TAB);
//            loginAdmin();
//            auditTradingAccount_Admin(clientEmail);
//            searchClientThroughEmail(clientEmail);
//            clientUploadPoiPoaDocs();
//            poiPoaAudit(clientEmail);
//            adminHandle = driver.getWindowHandle();
//            driver.switchTo().window(originalHandle);
//            cpMenu.refresh();
//            cpMenu.goToMenu(GlobalProperties.CPMenuName.UPGRADETOIB);
//            driver.switchTo().window(adminHandle);
//            auditIBAccount(clientEmail);
//            driver.close();
        }
        driver.switchTo().window(originalHandle);
        cpMenu.refresh();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        LogUtils.info("New IB Account: " + ibRebateAccount.retrieveIBAcc());

        //Clear Login session for IBP
//        clearLoginSession();

        //Clear Login session for CP
//        cpMenu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
    }

    public String getIBAcc(){
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        String ibAcc = ibRebateAccount.retrieveIBAcc();
        ibRebateAccount.selectIBAcc(ibAcc);

        return ibAcc;
    }

    public String getAffId()  {
        return ibRebateAccount.retrieveIBReferralLink();
    }

    public String getAffId_NewTab()  {
        return ibRebateAccount.retrieveIBReferralLink_NewTab();
    }

    public void registerNewIBThroughIBProgram(String cpURL) throws Exception {
        myfactor = new Factor(testEnv,brand,Regulator,driver);
        ibProgramRegistration = myfactor.newInstance(IBProgramRegistration.class);
        cp = myfactor.newInstance(CPRegisterGold.class);
        cpMenu = myfactor.newInstance(CPMenu.class);
        ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
        cpLiveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        adminlogin = myfactor.newInstance(Login.class);
        adminMenu = myfactor.newInstance(AdminMenu.class);

        driver.get(GlobalProperties.TESTENV_IBPROGRAM_REG_URL);
//        String country = getCountry("");
        generateUserTestData(cp);
        ibProgramRegistration.registerNewIBThroughIBProgram(cpURL, "France", email, firstName, lastName, phone, pwd);

        originalHandle = driver.getWindowHandle();
        registerIBProgram_PersonalDetailsVerification(GlobalProperties.PLATFORM.MT5, "Brazil", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD, cp);


        driver.switchTo().newWindow(WindowType.TAB);
        adminHandle = driver.getWindowHandle();
        loginAdmin();
        auditIBAccount(email);
        clearLoginSession();
        driver.close();
        adminHandle = null;
        driver.switchTo().window(originalHandle);
//        driver.navigate().refresh();


//        String userID = cpLiveAccounts.getProfileUserId();
//        auditMainAccount(userID,GlobalProperties.PLATFORM.MT5);

//        driver.close();
//        driver.switchTo().window(originalHandle);
        driver.navigate().refresh();
        String newIBAcc = getIBAcc();
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkNewIBProgram_AccountCreated");
        LogUtils.info("New IB Account Created Through Program: " + newIBAcc);
    }



    public String registerNewCPThroughIB(String affId, String campaignID, Boolean check) throws Exception {

        cp = myfactor.newInstance(CPRegisterGold.class);

        if(!campaignID.equals("")){
            registerFixedOTP_CRMNEW_TEMP(affId, "", campaignID, GlobalProperties.PLATFORM.MT5, "Thailand", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD);
            clientEmail = email;
            clientPwd = pwd;
            clientName = firstName;
        }else if ((BaseTestCaseNew.Brand.equalsIgnoreCase("vfx") || BaseTestCaseNew.Brand.equalsIgnoreCase("au")) && (BaseTestCaseNew.Regulator.equalsIgnoreCase("VFSC") || BaseTestCaseNew.Regulator.equalsIgnoreCase("VFSC2"))) {
            registerFixedOTP(affId, "", campaignID, GlobalProperties.PLATFORM.MT5, "Brazil", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD);
            clientEmail = email;
            clientPwd = pwd;
            clientName = firstName;
        }

        if(check){
            //AUDIT
            originalHandle = driver.getWindowHandle();
            driver.switchTo().newWindow(WindowType.TAB);
            owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
            owsLogin.login(OWSName,OWSPass);
            owsDashboard.auditTradingAccountFlow("","",email);
            driver.close();
            driver.switchTo().window(originalHandle);
        }

        cpMenu.refresh();

        cpMenu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
        cpLiveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        cpLiveAccounts.setViewContentGridMode();
        cpLiveAccounts.waitLoadingAccountListContent();
        return cpLiveAccounts.getAccounts(GlobalProperties.PLATFORM.MT5).get(0).getAccountNum();
    }

    public void verifyNewTradingAccountIsUnderIBDownline_HomePage(String ibAcc, String tradingAcc) throws InterruptedException {
        Thread.sleep(500);
        login();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        ibRebateAccount.selectIBAcc(ibAcc);
        List<String> allRecentlyOpenedTradingAccounts = ibRebateAccount.retrieveAllRecentlyOpenedTradingAccounts();

        if (allRecentlyOpenedTradingAccounts.contains(tradingAcc)) {
            LogUtils.info("New Trading account " + tradingAcc + " is found under IB's Downline on IB Dashboard!");
        } else {
            Assert.fail("Trading account " + tradingAcc + " is NOT FOUND under IB's Downline on IB Dashboard!!");
        }
    }

    public void verifyNewTradingAccountIsUnderIBDownline_AccountReport(String ibAcc, String tradingAcc, String campaignID) throws InterruptedException {

        login();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        ibRebateAccount.selectIBAcc(ibAcc);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBACCOUNTREPORT);
        ibAccountReport.searchAccountReport_CampaignSource(campaignID);
        ibAccountReport.verifyCampaignSource(campaignID, tradingAcc);
//        List<String> allRecentlyOpenedTradingAccounts = ibRebateAccount.retrieveAllRecentlyOpenedTradingAccounts();
//
//        if (allRecentlyOpenedTradingAccounts.contains(tradingAcc)) {
//            LogUtils.info("New Trading account " + tradingAcc + " is found under IB's Downline on IB Dashboard!");
//        } else {
//            Assert.fail("Trading account " + tradingAcc + " is NOT FOUND under IB's Downline on IB Dashboard!!");
//        }
    }

    public void verifyNewTradingAccountIsUnderIBDownline_AccountReport_Pending(String ibAcc, String name) throws InterruptedException {

        login();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        ibRebateAccount.selectIBAcc(ibAcc);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBACCOUNTREPORT);
        List<String> pendingAcc = ibAccountReport.retrievePendingAccountNamesInAccountReport(name);

        if(pendingAcc.stream().anyMatch(s -> s.contains(name))){
            LogUtils.info(name + " - pending account is found under IB's Account Report.");
        }else{
            Assert.fail(name + " - pending account is NOT found under IB's Account Report.");
        }

    }


    //Admin related
    public void loginAdmin() throws InterruptedException {

            try {
                //refer to PTRegisterPaymentAndPayoutTestCases.ppAudit();
                driver.get(AdminURL);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exampleInputEmail1")));
                driver.findElement(By.id("exampleInputEmail1")).clear();
                driver.findElement(By.id("exampleInputEmail1")).sendKeys((String)data[0][8]);
                driver.findElement(By.id("password_login")).clear();
                driver.findElement(By.id("password_login")).sendKeys((String)data[0][9]);

                driver.findElement(By.id("btnLogin")).click();
                Thread.sleep(1000);
                //For new regulator branch
                if (driver.findElements(By.id("regulation_label")).size()>0) {
                    //driver.findElement(By.id("regulation_label")).click();
                    WebElement regul= driver.findElement(By.id("regulation_label"));
                    JavascriptExecutor javascript = (JavascriptExecutor) driver;
                    javascript.executeScript("arguments[0].click()", regul);
                    //driver.findElement(By.xpath("//span[contains(text(),'"+Regulator+"')]")).click();
                    //Switch regulator
                    //	driver.findElement(By.xpath("//label[@id='regulation_label']")).click();
                    driver.findElement(By.xpath(String.format("//ul[@class='dropdown-menu favorite-content']/li/div/span[text()='%s']", Regulator.toUpperCase()))).click();

                }else {
                    System.out.println("Single Regulator. No need to switch.");
                }
            } catch (Exception e) {
                System.out.println("Arleady logged in.");
            }
    }

    public void auditIBAccount(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminAccountAudit = myfactor.newInstance(AdminAccountAudit.class);

        adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);
        adminAccountAudit.waitForAccountAuditPageToLoad();
        adminAccountAudit.auditIBAccount(email, GlobalProperties.BRAND.valueOf(BaseTestCaseNew.Brand.toUpperCase()));
    }

    public void searchClientThroughEmail(String email){
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminClient = myfactor.newInstance(AdminClient.class);

        adminMenu.goToMenu(AdminMenuName.CLIENT);
        adminClient.searchClientThroughEmail(email);
    }

    public void clientUploadPoiPoaDocs(){
        adminClient = myfactor.newInstance(AdminClient.class);

        LogUtils.info("IB Upgrade Button not enabled, proceed with POI POA Upload & Audit");
        adminClient.clientUploadPoiPoaDocs();
    }

    public void poiPoaAudit(String email) {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminIDPOAAudit = myfactor.newInstance(AdminIDPOAAudit.class);

        adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);
        adminIDPOAAudit.clientPoiPoaAudit(email);
    }


    public void auditTradingAccount_Admin(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminAccountAudit = myfactor.newInstance(AdminAccountAudit.class);

        adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);

        adminAccountAudit.auditTradingAccount(email, GlobalProperties.BRAND.valueOf(BaseTestCaseNew.Brand.toUpperCase()));
    }

}
