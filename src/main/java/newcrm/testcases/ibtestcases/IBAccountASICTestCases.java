package newcrm.testcases.ibtestcases;

import newcrm.adminapi.AdminAPI;
import newcrm.business.adminbusiness.*;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.ibbase.account.IBDemoAccount;
import newcrm.business.businessbase.ibbase.account.IBProgramRegistration;
import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.AdminMenuName;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.testcases.cptestcases.RegisterTestcases;
import newcrm.utils.testCaseDescUtils;
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
import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class IBAccountASICTestCases extends RegisterTestcases {


    private AdminAPI admin;

//    public Object data[][];
    protected IBRebateAccount ibRebateAccount;
    protected RegisterTestcases cp;
    protected CPLiveAccounts cpLiveAccounts;
    protected CPMenu cpMenu;
    protected IBDemoAccount ibDemoAccount;
    protected IBAccountReport ibAccountReport;
    protected IBProgramRegistration ibProgramRegistration;
    private RegisterTestcases registerTestcases = new RegisterTestcases();
    private IBAdminAccountTestCases ibAdminAccountTestCases = new IBAdminAccountTestCases();
    private SystemSettingTestCases systemSettingTestCases = new SystemSettingTestCases();

    protected AdminExternalUser adminExternalUser;
    protected AdminAccountAudit adminAccountAudit;
    protected AdminAdditionalAccountAudit adminAdditionalAccountAudit;
    protected AdminMenu adminMenu;
    protected AdminClient adminClient;
    protected AdminRebateAccount adminRebateAccount;
    protected AdminIDPOAAudit adminIDPOAAudit;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;

    protected String clientEmail;
    protected String clientPwd;
    protected String clientName;
    protected String brand, server, testEnv, headless;
    protected String originalHandle, adminHandle;
    protected String OWSName;
    protected String OWSPass;
    protected Boolean tcSkip;
    protected Boolean prevTCFail = false;
    protected ITestContext context;
    private Factor myfactor;
    private WebDriver driver;
    protected static CPLogin login;

    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) {
        originalHandle = null;
        adminHandle = null;
        if (driver ==null){
            driver = getDriverNew();
//            driver = Utils.funcSetupDriver(driver, "chrome", "true");
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
//            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        }
        if (myfactor == null){
//            myfactor = getFactorNew();
            myfactor = new Factor(TestEnv,Brand,Regulator,driver);
        }

        if(prevTCFail){
            login();
        }

        prevTCFail= false;
        tcSkip = false;
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
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

    @AfterMethod
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
    protected void login() {
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
            login.login(config.TraderName, config.TraderPass);

            if (login.checkExistsLoginAlertMsg() != null || !login.checkLoginSuccess().getKey()) {
                Assert.fail("Login Failed for user: " + config.TraderName + ", URL: " + TraderURL);
            }
        } else {
            Assert.fail("User config not found");
        }

        loginThreadLocal.set(login);
    }

    @Test(priority = 2, description = testCaseDescUtils.IBACCOUNT_REFERRAL_LINK_VERIFICATION)
    public void testIBReferralLinkVerificationASIC() throws Exception {
        if(brand.equalsIgnoreCase(BRAND.VFX.toString())) {
            //Use ASIC Account
            myfactor = new Factor(testEnv,brand,(String)data[0][12],driver);
            ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
            cpMenu = myfactor.newInstance(CPMenu.class);
            ibDemoAccount = myfactor.newInstance(IBDemoAccount.class);
            ibAccountReport = myfactor.newInstance(IBAccountReport.class);
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
            cpMenu.changeLanguage("English");

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
            ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);
            cpMenu = myfactor.newInstance(CPMenu.class);
            ibDemoAccount = myfactor.newInstance(IBDemoAccount.class);
            ibAccountReport = myfactor.newInstance(IBAccountReport.class);
            adminMenu = myfactor.newInstance(AdminMenu.class);
            owsDashboard = myfactor.newInstance(OWSDashboard.class);
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
            cpMenu.changeLanguage("English");

            funcIBReferralLinkRegistration();
            logout();
        } else {
            tcSkip = true;
            throw new SkipException("Skipping this test intentionally.");
        }
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
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        cpMenu.changeLanguage("English");

        originalHandle = driver.getWindowHandle();

        String ibAcc = getIBAcc();
        String affId = getAffId_NewTab();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBREFERRALLINKSMENU);

        //Demo Account Flow
        ibRebateAccount.navigateToDemoAccountURL(affId);
        String name = ibDemoAccount.registerIBDemoAccount(TraderURL, "Chile");
        driver.close();
        driver.switchTo().window(originalHandle);
        //Verify Demo Account Dropped Under IB
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBACCOUNTREPORT);
//        ibAccountReport.verifyDemoAccountInAccountReport_Name(name);
        List<String> demoAccountNames = ibAccountReport.retrieveDemoAccountNamesInAccountReport();
        if(demoAccountNames.stream().anyMatch(s -> s.contains(name))){
            LogUtils.info(name + " - (Demo Flow) demo account is found under IB's Account Report.");
        }else{
            Assert.fail(name + " - (Demo Flow) demo account is NOT found under IB's Account Report.");
        }
        LogUtils.info("Demo Account creation flow verified");
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkASICDemoAccountRegistrationIsUnderIB");

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
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBACCOUNTREPORT);
            List<String> newDemoAccountNames = ibAccountReport.retrieveDemoAccountNamesInAccountReport();

            newDemoAccountNames.removeAll(demoAccountNames);
            LogUtils.info(newDemoAccountNames.get(0) + " - (h5 flow) demo account is found under IB's Account Report.");
            LogUtils.info("H5 flow verified");
            // Capture ss
            ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkASICH5DemoAccountRegistrationIsUnderIB");
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
        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "checkASICTradingAccountRegistrationIsUnderIB");
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



    public String registerNewCPThroughIB(String affId, String campaignID, Boolean check) throws Exception {

        HashMap<String,String> userDetails = registerNew_WithoutCheckURL(affId, "", campaignID, GlobalProperties.PLATFORM.MT5, "", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD);
        clientEmail = email;
        clientPwd = pwd;
        clientName = userDetails.get("firstName");

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

//        String userID = cpLiveAccounts.getProfileUserId();
//        auditMainAccount(userID,GlobalProperties.PLATFORM.MT5);

//        cpMenu.closeAccQuizDialogPopup();
        //Close Account Quiz dialog no longer present, can only refresh to proceed
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

    public void verifyNewTradingAccountIsUnderIBDownline_AccountReport(String ibAcc, String tradingAcc, String campaignID) {

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

    public void loginAdmin(String userName, String passWord) throws InterruptedException {

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

    //Find a Client under IB, then Audit POI, POA & Account Audit
    public String poiPoaAccountAuditClientUnderIB(String ibEmail) throws InterruptedException {
        List<String> ibAccountNumberList = retrieveIBAccs(ibEmail);
        String email = searchFirstClientNameInList(ibAccountNumberList);
        Boolean ibEnabled = checkFirstClientIBUpgradeEnabled();
        Boolean tradeAccountExist = checkFirstClientTradingAccountExist();
        if (ibEnabled == false){
            clientUploadPoiPoaDocs();
            if(tradeAccountExist == false){
                auditTradingAccount(email);
            }
            poiPoaAudit(email);
        }
        return email;
    }

    public void AdminUpgradeClientToIB(String email) throws InterruptedException {
        upgradeClientToIB(email);
        auditIBAccount(email);
        List<String> newIBAccount = retrieveIBAccs(email);
        LogUtils.info("Client Upgraded to IB - New Rebate Account: " + newIBAccount.get(0));
    }

    public String addNewIBThroughAdminExternalUser() throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminExternalUser = myfactor.newInstance(AdminExternalUser.class);
        adminMenu.goToMenu(AdminMenuName.EXTERNAL_USER);
        return adminExternalUser.addNewIBThroughAdminExternalUser((String)data[0][7]);
    }

    public void auditIBAccount(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminAccountAudit = myfactor.newInstance(AdminAccountAudit.class);

        adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);
        adminAccountAudit.waitForAccountAuditPageToLoad();
        adminAccountAudit.auditIBAccount(email, GlobalProperties.BRAND.valueOf(BaseTestCaseNew.Brand.toUpperCase()));
    }

    public void addAdditionalIBThroughAdminExternalUser(String email) {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminExternalUser = myfactor.newInstance(AdminExternalUser.class);

        adminMenu.goToMenu(AdminMenuName.EXTERNAL_USER);
        adminExternalUser.addAdditionalIBThroughAdminExternalUser(email);
    }

    public void auditAdditionalAccountIB(String email) {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminAdditionalAccountAudit = myfactor.newInstance(AdminAdditionalAccountAudit.class);

        adminMenu.goToMenu(AdminMenuName.ADDITIONAL_ACCOUNT_AUDIT);
        adminAdditionalAccountAudit.auditAdditionalAccountIB(email, GlobalProperties.BRAND.valueOf(BaseTestCaseNew.Brand.toUpperCase()));
    }

    public List<String> retrieveIBAccs(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminRebateAccount = myfactor.newInstance(AdminRebateAccount.class);

        adminMenu.goToMenu(AdminMenuName.REBATE_ACCOUNT);
        return adminRebateAccount.retrieveIBAccs(email);
    }

    public void configureIBCommissionRules(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminRebateAccount = myfactor.newInstance(AdminRebateAccount.class);

        adminMenu.goToMenu(AdminMenuName.REBATE_ACCOUNT);
        adminRebateAccount.configureIBCommissionRules(email);
    }

    public String searchFirstClientNameInList(List<String> ibAccountNumberList){
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminClient = myfactor.newInstance(AdminClient.class);

        adminMenu.goToMenu(AdminMenuName.CLIENT);
        return adminClient.searchFirstClientNameInList(ibAccountNumberList);
    }

    public void searchClientThroughEmail(String email){
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminClient = myfactor.newInstance(AdminClient.class);

        adminMenu.goToMenu(AdminMenuName.CLIENT);
        adminClient.searchClientThroughEmail(email);
    }

    public Boolean checkFirstClientIBUpgradeEnabled(){
        adminClient = myfactor.newInstance(AdminClient.class);

        return adminClient.checkFirstClientIBUpgradeEnabled();
    }

    public Boolean checkFirstClientTradingAccountExist(){
        adminClient = myfactor.newInstance(AdminClient.class);

        return adminClient.checkFirstClientTradingAccountExist();
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

    public void upgradeClientToIB(String email) {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminClient = myfactor.newInstance(AdminClient.class);

        adminMenu.goToMenu(AdminMenuName.CLIENT);
        adminClient.upgradeClientToIB(email);
    }

    public void auditTradingAccount(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        adminAccountAudit = myfactor.newInstance(AdminAccountAudit.class);

        adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);

        adminAccountAudit.auditTradingAccount(email, GlobalProperties.BRAND.valueOf(BaseTestCaseNew.Brand.toUpperCase()));
    }

}
