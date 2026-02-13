package newcrm.testcases.ibtestcases;

import newcrm.business.adminbusiness.*;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.ibbase.account.IBProfile;
import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.AdminMenuName;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

public class IBAdminAccountTestCases extends SystemSettingTestCases {

    protected String OWSName;
    protected String OWSPass;

    protected Object data[][];
    protected Object agreementData[][];
    protected AdminExternalUser adminExternalUser;
    protected AdminAccountAudit adminAccountAudit;
    protected AdminAdditionalAccountAudit adminAdditionalAccountAudit;
    protected CPMenu cpMenu;
    protected AdminMenu adminMenu;
    protected AdminClient adminClient;
    protected AdminRebateAccount adminRebateAccount;
    protected AdminIDPOAAudit adminIDPOAAudit;
    protected IBProfile ibProfile;
    protected IBRebateAccount ibRebateAccount;
    protected AdminRebateAccountAgreement adminRebateAccountAgreement;
    //    private SystemSettingTestCases systemSettingTestCases = new SystemSettingTestCases();
    protected ITestContext contextData;
//    protected Factor myfactor;
    protected CPLogin login;
//    private WebDriver driver;

    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) {
        if (driver ==null){
            driver = getDriverNew();
        }
        if (myfactor == null){
//            myfactor = getFactorNew();
            myfactor = new Factor(TestEnv,Brand,Regulator,driver);
        }

        cpMenu = myfactor.newInstance(CPMenu.class);
        adminMenu = myfactor.newInstance(AdminMenu.class);
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
    }

    @AfterMethod()
    public void tearDown(ITestResult result) throws Exception {
//        int totalTests = contextData.getAllTestMethods().length;
//        int executedTests = contextData.getPassedTests().size() + contextData.getFailedTests().size() + contextData.getSkippedTests().size();

//        if (result.getStatus() == ITestResult.FAILURE && executedTests != totalTests) {
//            driver.close();
//        }else{
            driver.navigate().refresh();
//        }
    }

    @Override
    protected void login() {

        driver.get((String)agreementData[0][6]);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement usernameInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-testid='userName_login']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@data-testid='userName_login']")));
        usernameInput.sendKeys((String)agreementData[0][1]);
        driver.findElement(By.xpath("//input[@data-testid='password_login']")).sendKeys((String)agreementData[0][2]);
        driver.findElement(By.xpath("//button[@data-testid='login']")).click();

    }

    @Test(priority = 0, description = testCaseDescUtils.ADMIN_CREATE_IB_EXT_USER)
    public void testAdminCreateIBExtUserAndAdditionalIBAccount() throws Exception {
        AdminCreateIBExtUserAndAdditionalIBAccount();
    }

    @Test(priority = 0, description = testCaseDescUtils.ADMIN_UPGRADE_CLIENT_TO_IB)
    public void testAdminUpgradeClientToIB() throws Exception {
        String email = poiPoaAccountAuditClientUnderIB((String)data[0][1]);
        AdminUpgradeClientToIB(email);
    }

    @Test(priority = 0, description = testCaseDescUtils.ADMIN_CONFIGURE_IB_COMMISSION_RULES)
    public void testConfigureIBCommissionRules() throws InterruptedException {
        configureIBCommissionRules((String)data[0][1]);
    }

    public void AdminCreateIBExtUserAndAdditionalIBAccount() throws InterruptedException {
        String email = addNewIBThroughAdminExternalUser();
        auditIBAccount(email);
        addAdditionalIBThroughAdminExternalUser(email);
        auditAdditionalAccountIB(email);
    }

    @Test(priority = 0, description = testCaseDescUtils.IBACCOUNT_AGREEMENT)
    public void testRebateAccountAgreement() throws InterruptedException {

        adminRebateAccountAgreement = myfactor.newInstance(AdminRebateAccountAgreement.class);
        ibProfile = myfactor.newInstance(IBProfile.class);
        ibRebateAccount = myfactor.newInstance(IBRebateAccount.class);

        funcRebateAccountAgreement((String)agreementData[0][1]);
    }

    //Find a Client under IB, then Audit POI, POA & Account Audit
    public String poiPoaAccountAuditClientUnderIB(String ibEmail) throws InterruptedException {
        List<String> ibAccountNumberList = retrieveIBAccs(ibEmail);
        String email = searchFirstClientNameInList(ibAccountNumberList);
        Boolean ibEnabled = checkFirstClientIBUpgradeEnabled();
        Boolean tradeAccountExist = checkFirstClientTradingAccountExist();
        if (ibEnabled == false){
            clientUploadPoiPoaDocs();
//            if(tradeAccountExist == false){
//                auditTradingAccount(email);
//            }
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
        return adminExternalUser.addNewIBThroughAdminExternalUser((String)data[0][8]);
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

    public void funcRebateAccountAgreement(String email) throws InterruptedException {
        adminMenu = myfactor.newInstance(AdminMenu.class);
        //Admin - Update Rebate Account Agreement
        List<String> ibAccs = retrieveIBAccs(email);
        adminMenu.goToMenu(AdminMenuName.REBATE_ACCOUNT_AGREEMENT);
        adminRebateAccountAgreement.updateRebateAccountAgreement(ibAccs.get(0));

        //IBP - Agree to newly updated Agreement
        login();
        String currentIBAcc = ibRebateAccount.getCurrentlySelectedAccount();
        //If default selected IB account is not the one just agreed, switch to that account
//        if(!currentIBAcc.equals(ibAccs.get(0))){
//            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
//            ibRebateAccount.selectIBAcc(ibAccs.get(0));
//        }else{
            cpMenu.closeSkipDialogPopup();
//        }
        cpMenu.goToMenu(GlobalProperties.CPMenuName.AGREEIBAGREEMENT);
        driver.switchTo().defaultContent();
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
//        cpMenu.changeLanguage("English");

        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.PUG.toString())) {
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBREFERRALLINKSMENU);
        }else{
            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBPROFILEMENU);
        }

        ibProfile.verifyIBAgreementDate(ibAccs.get(0));
    }
}
