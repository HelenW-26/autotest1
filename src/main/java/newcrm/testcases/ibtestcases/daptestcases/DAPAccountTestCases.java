package newcrm.testcases.ibtestcases.daptestcases;

import adminBase.Login;
import cn.hutool.log.Log;
import newcrm.adminapi.AdminAPI;
import newcrm.business.adminbusiness.*;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.dapbase.*;
import newcrm.business.businessbase.ibbase.account.IBDemoAccount;
import newcrm.business.businessbase.ibbase.account.IBProfile;
import newcrm.business.businessbase.ibbase.account.IBProgramRegistration;
import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.business.businessbase.ibbase.report.IBReport;
import newcrm.business.businessbase.owsbase.OWSCommissionList;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.business.businessbase.owsbase.OWSPayment;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.dappages.DAPDeepLinkPage;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.DAPBaseTestCaseNew;
import newcrm.testcases.admintestcases.SystemSettingTestCases;
import newcrm.testcases.cptestcases.RegisterGoldTestcases;
import newcrm.testcases.cptestcases.RegisterTestcases;
import newcrm.testcases.ibtestcases.IBAdminAccountTestCases;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static newcrm.utils.testCaseDescUtils.DAP_CPA_Login_Logout;
import static newcrm.utils.testCaseDescUtils.DAP_CPA_Registration;
import static org.testng.Assert.assertNotNull;

public class DAPAccountTestCases extends DAPBaseTestCaseNew {

    public Object data[][];
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
    protected OWSCommissionList owsCommissionList;
    protected OWSPayment owsPayment;
    protected DAPDashboard dapDashboard;
    protected DAPDeepLink dapDeepLink;
    protected DAPPostbackTracker dapPostbackTracker;
    protected DAPCommissionReport dapCommissionReport;
    protected DAPPayments dapPayments;
    protected DAPCommissionPlan dapCommissionPlan;
    protected DAPMultiCommission dapMultiCommission;

    protected String OWSName;
    protected String OWSPass;

    private Factor myfactor;
    private WebDriver driver;
    protected static CPLogin login;


    @BeforeMethod(alwaysRun=true)
    protected void initMethod(Method method) throws InterruptedException {
        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())) {

            if (driver ==null){
                driver = getDriverNew();
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            if (myfactor == null){
                myfactor = new Factor(TestEnv,Brand,Regulator,driver);
            }

        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
//
//    @AfterMethod(alwaysRun=true)
//    public void tearDown(ITestResult result) {
//
//    }


    public void funcDAPProfilePanel_LoginLogout() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        cpMenu = myfactor.newInstance(CPMenu.class);

        String dapLang = dapDashboard.getActiveLanguage();
        dapDashboard.dapLogout();

        //Make sure language is same after logged out (on login page)
        String loginPageLang = driver.findElement(By.xpath("//span[@class='language-label-text']")).getText().toLowerCase();
        Assert.assertTrue(dapLang.equalsIgnoreCase(loginPageLang));
    }

    @Test(priority = 0)
    public void testDAPProfilePanel() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        cpMenu = myfactor.newInstance(CPMenu.class);

        String dapLang = dapDashboard.getActiveLanguage();

        //Switch to Client Portal
        dapDashboard.retrieveProfilePanelUserID();
        dapDashboard.switchToCP();

        //Make sure language is same on Client Portal
        String cpLang = cpMenu.getActiveLanguage();
        Assert.assertTrue(dapLang.equalsIgnoreCase(cpLang));

        //Switch back to CPA Portal
        cpMenu.goToMenu(GlobalProperties.CPMenuName.DAPPORTAL);
        dapDashboard.dapLogout();

        //Make sure language is same after logged out (on login page)
        String loginPageLang = driver.findElement(By.xpath("//span[@class='language-label-text']")).getText().toLowerCase();
        Assert.assertTrue(dapLang.equalsIgnoreCase(loginPageLang));
    }

    @Test(priority = 0)
    public void testDAPStatisticsTable() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapDashboard.verifyStatisticsTable();
    }

    @Test(priority = 0)
    public void testMarketingDeepLinkLiveAccRegistration() throws Exception {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapDeepLink = myfactor.newInstance(DAPDeepLink.class);
        cpMenu = myfactor.newInstance(CPMenu.class);

        String marketingRegLink = dapDashboard.getRegisterLiveAccLink();
        dapDashboard.navigateToDAPDeepLink();
        String deepRegLink = dapDeepLink.getLiveAccRegistrationDeepLink();
        Assert.assertTrue(marketingRegLink.equalsIgnoreCase(deepRegLink),"Deep Link Registration URL ("+deepRegLink+") does not match the one from Dashboard's Marketing section ("+marketingRegLink+")");
        LogUtils.info("Deep Link Registration URL and Marketing Registration URL are matched.");

        int index = deepRegLink.indexOf("?cpaAffid=");
        // Extract everything from "?cpaAffid=" to the end
        String cpaCode = deepRegLink.substring(index + 10);

        registerFixedOTP_CRM(cpaCode, "", "", GlobalProperties.PLATFORM.MT5, "Thailand", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD);
        String newClientUserID = cpMenu.getUserID();
        logout();

        //POI Audit on OWS
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
        owsLogin.login(OWSName,OWSPass);
        boolean result = owsDashboard.auditPOIFlow(newClientUserID,"","");

        //Go back to DAP Dashboard and verify new client is under CPA's downline
        driver.get((String)data[0][3]);
        List<String> clientJourneyUIDs = dapDashboard.getAllClientJourneyUID();

        if (clientJourneyUIDs.contains(newClientUserID)) {
            LogUtils.info("New Client's ID " + newClientUserID + " is found under CPA's Downline on Dashboard!");
        } else {
            Assert.fail("New Client's ID " + newClientUserID + " is NOT FOUND under CPA's Downline on Dashboard!!");
        }
    }

    @Test(priority = 0)
    public void testLiveAccReferralCodeRegistration() throws Exception {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapDeepLink = myfactor.newInstance(DAPDeepLink.class);
        cpMenu = myfactor.newInstance(CPMenu.class);

        String referralCode = dapDashboard.getReferralCode();

        registerFixedOTP_CRM("", referralCode, "", GlobalProperties.PLATFORM.MT5, "Thailand", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD);
        String newClientUserID = cpMenu.getUserID();
        logout();

        //POI Audit on OWS
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
        owsLogin.login(OWSName,OWSPass);
        boolean result = owsDashboard.auditPOIFlow(newClientUserID,"","");

        //Go back to DAP Dashboard and verify new client is under CPA's downline
        driver.get((String)data[0][3]);
        List<String> clientJourneyUIDs = dapDashboard.getAllClientJourneyUID();

        if (clientJourneyUIDs.contains(newClientUserID)) {
            LogUtils.info("New Client's ID " + newClientUserID + " is found under CPA's Downline on Dashboard!");
        } else {
            Assert.fail("New Client's ID " + newClientUserID + " is NOT FOUND under CPA's Downline on Dashboard!!");
        }
    }

    @Test(priority = 0)
    public void testPerformanceReportEntryPointToggle() throws Exception {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        boolean perfEntryPointExist = dapDashboard.checkPerformanceReportEntryPointExist();
        String originalHandle = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
        owsLogin.login(OWSName,OWSPass);
        boolean perfReportToggle = owsDashboard.togglePerformanceReportEntryPoint("","","",(String)data[0][1]);
        driver.close();

        //perfReportToggle = after toggling, perfEntryPointExist = verified before toggling
        Assert.assertTrue(perfEntryPointExist!=perfReportToggle, "Toggle was set to "+!perfReportToggle+ " but Performance Report existence of entry point on DAP Dashboard is "+perfEntryPointExist);
        LogUtils.info("Toggle was set to "+!perfReportToggle+ " and Performance Report existence of entry point on DAP Dashboard is "+perfEntryPointExist);

        driver.switchTo().window(originalHandle);
        driver.navigate().refresh();
        perfEntryPointExist = dapDashboard.checkPerformanceReportEntryPointExist();

        //verify again after toggling
        Assert.assertTrue(perfEntryPointExist==perfReportToggle, "Toggle was set to "+perfReportToggle+ " but Performance Report existence of entry point on DAP Dashboard is "+perfEntryPointExist);
        LogUtils.info("Toggle was set to "+perfReportToggle+ " and Performance Report existence of entry point on DAP Dashboard is "+perfEntryPointExist);
    }

    @Test(priority = 0)
        public void testDAPPostbackTracker() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapPostbackTracker = myfactor.newInstance(DAPPostbackTracker.class);
        String selectedPostbackEventType = dapDashboard.navigateToPostbackTrackerPage();
        dapPostbackTracker.verifyPostbackURLEventType(selectedPostbackEventType);
    }

    @Test(priority = 0)
    public void testPaymentAudit() {
        String originalHandle = driver.getWindowHandle();

        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapPayments = myfactor.newInstance(DAPPayments.class);
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsPayment = myfactor.newInstance(OWSPayment.class);
        owsCommissionList = myfactor.newInstance(OWSCommissionList.class);
        dapCommissionReport = myfactor.newInstance(DAPCommissionReport.class);

        driver.switchTo().newWindow(WindowType.TAB);
        owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
        owsLogin.login(OWSName,OWSPass);
        owsDashboard.navigateToCommissionList();
        owsCommissionList.commissionAdjustment((String)data[0][12]);
        owsCommissionList.commissionAdjustment((String)data[0][12]);
        String owsHandle = driver.getWindowHandle();

        driver.switchTo().window(originalHandle);
        dapDashboard.navigateToPaymentPage();
        dapPayments.requestPayment();
        dapPayments.requestPayment();

        driver.switchTo().window(owsHandle);
        owsDashboard.navigateToPayment();
        owsPayment.searchPayment_Pending((String)data[0][12]);
        owsPayment.auditPayment_Approve();
        owsPayment.searchPayment_Pending((String)data[0][12]);
        owsPayment.auditPayment_Reject();
        driver.close();

        driver.switchTo().window(originalHandle);
        dapDashboard.navigateToCommissionReportPage();
        dapCommissionReport.filterCommissionReport_Today();
        List<String> statusList = dapCommissionReport.getStatusList();

        Assert.assertTrue(statusList.contains("Settled"), "Approved payment's commission record is not found as Settled status on DAP Commission Report!");
        LogUtils.info("Able to find Approved payment's commission record as Settled status on DAP Commission Report.");
        Assert.assertTrue(statusList.contains("Rejected"), "Rejected payment's commission record is not found as Rejected status on DAP Commission Report!");
        LogUtils.info("Able to find Rejected payment's commission record as Rejected status on DAP Commission Report.");
    }

    @Test(priority = 0)
    public void testDAPCommissionReportPayments() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapCommissionReport = myfactor.newInstance(DAPCommissionReport.class);
        dapPayments = myfactor.newInstance(DAPPayments.class);

        double dashboardTotalCommission = dapDashboard.getTotalCommission();
        dapDashboard.verifyAvailableBalance();
        dapDashboard.navigateToPaymentPage();
        Map<String, Double> totalCommission = dapPayments.getTotalCommissionByType();
        Double applicableCommissionDollar = totalCommission.get("applicableCommissionDollar");
        Double cumulativeCommission = totalCommission.get("cumulativeCommission");
        Double paidCommission = totalCommission.get("paidCommission");

        dapDashboard.navigateToCommissionReportPage();
        dapCommissionReport.filterCommissionReport_OneYear();
//        List<String> statusList = dapCommissionReport.getStatusList();
//        List<Double> commissionList = dapCommissionReport.getCommissionList();

        Map<String, Double> commissionStatusList = dapCommissionReport.getStatusCommissionMap();

        List<String> statusList = new ArrayList<>(commissionStatusList.keySet());
        List<Double> commissionList = new ArrayList<>(commissionStatusList.values());

        double totalApplicableCommission = 0.0;
        double totalCumulativeCommission = 0.0;
        double totalPaidCommission = 0.0;

        //Applicable Commission = 佣金状态为 已拒绝+待申请 汇总
        for(int i=0;i<statusList.size();i++){
            if(statusList.get(i).equalsIgnoreCase("rejected") || statusList.get(i).equalsIgnoreCase("pending application")){
                totalApplicableCommission = totalApplicableCommission + commissionList.get(i);
            }
        }
        Assert.assertTrue(applicableCommissionDollar == totalApplicableCommission, "Applicable Commission on Payment Page ("+applicableCommissionDollar+") does not match the sum of Applicable Commission on Commission Report Page ("+totalApplicableCommission+")");
        LogUtils.info("Applicable Commission on Payment Page and Commission Report Page are matched(" +applicableCommissionDollar+" = "+totalApplicableCommission+").");


        //Cumulative Commission = 待申请(已拒绝后能再申请）+已结算+结算中+统计中 汇总
        for(int i=0;i<statusList.size();i++){
            if(statusList.get(i).equalsIgnoreCase("pending application") || statusList.get(i).equalsIgnoreCase("rejected") || statusList.get(i).equalsIgnoreCase("settled") || statusList.get(i).contains("Processing")|| statusList.get(i).equalsIgnoreCase("settling")){
                totalCumulativeCommission = totalCumulativeCommission + commissionList.get(i);
            }
        }
        Assert.assertTrue(dashboardTotalCommission == totalCumulativeCommission, "Cumulative Commission on Dashboard ("+dashboardTotalCommission+") does not match the sum of Cumulative Commission on Commission Report Page ("+totalCumulativeCommission+")");
        Assert.assertTrue(cumulativeCommission == totalCumulativeCommission, "Cumulative Commission on Payment Page ("+cumulativeCommission+") does not match the sum of Cumulative Commission on Commission Report Page ("+totalCumulativeCommission+")");
        LogUtils.info("Cumulative Commission on Dashboard/Payment Page and Commission Report Page are matched(" +cumulativeCommission+" = "+totalCumulativeCommission+").");


        //Paid Commission = 已结算：佣金状态为 已结算  汇总
        for(int i=0;i<statusList.size();i++){
            if(statusList.get(i).equalsIgnoreCase("settled")){
                totalPaidCommission = totalPaidCommission + commissionList.get(i);
            }
        }
        Assert.assertTrue(paidCommission == totalPaidCommission, "Paid Commission on Payment Page ("+paidCommission+") does not match the sum of Paid Commission on Commission Report Page ("+totalPaidCommission+")");
        LogUtils.info("Paid Commission on Payment Page and Commission Report Page are matched(" +paidCommission+" = "+totalPaidCommission+").");
    }

    @Test(priority = 0)
    public void testDAPCommissionPlan() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapCommissionPlan = myfactor.newInstance(DAPCommissionPlan.class);
        dapDashboard.navigateToCommissionPlanPage();
        dapCommissionPlan.verifyCommissionPlanPage();
    }

    @Test(priority = 0)
    public void testDAPMultiCommissionAndTop5() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapMultiCommission = myfactor.newInstance(DAPMultiCommission.class);

        dapDashboard.navigateToMultiCommissionPage();
        Map<String, List<String>> subCPACommissionMap = dapMultiCommission.verifyCommissionPlanPage();

        dapDashboard.navigateToDashboardPage();
        dapDashboard.verifyTopSubCPATable(subCPACommissionMap);
    }

    public HashMap<String,String> registerFixedOTP_CRM(String cpaCode, String referralCode, String campaignID, GlobalProperties.PLATFORM platform, String country, boolean check,
                                                               GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);

        driver.get(GlobalProperties.TESTENV_LIVEACC_REG_URL);

        String firstName = "autotest" + GlobalMethods.getRandomString(10);
        String lastName = "TestCRM";
        String email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        String phone = "0000"+GlobalMethods.getRandomNumberString(10);
        String pwd = GlobalMethods.generatePassword();

        if (!cpaCode.equals("")) {
            String CPAurl = driver.getCurrentUrl() +"?cpaAffid="+cpaCode.trim();
            driver.navigate().to(CPAurl);
            GlobalMethods.printDebugInfo("RegisterEntryPage: set register url to: " + driver.getCurrentUrl());
        }

        if(TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()))
        {
            cp.setUserInfo(firstName, country, email, pwd);
            cp.entrySubmit(TraderURL);

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

        }
        else
        {
            // AU alpha temp registration. Wait 17.11 all brand 双端统一 go live then remove
            String oldUrl = driver.getCurrentUrl();
            cp.inputPagePassword();
            cp.setDemoRegistrationDomainUrl((String)data[0][6], oldUrl);
//            email = email.replace("testcrmautomation", "testcrm");
            cp.setUserInfo_CRMNEW_TEMP(firstName, country, email, pwd);

            if (!referralCode.equals("")) {
                cp.setReferralCode(referralCode);
                GlobalMethods.printDebugInfo("Referral Code set to "+referralCode);
            }

            cp.entrySubmit((String)data[0][6]);

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

            cp.sendEmailCode("999999");

            try{
                cp.sendPhoneCode("123456");
            } catch (Exception e) {
                cp.setAndSendPhoneCode(phone,"123456");
            }

            cp.fillPersonalDetails(firstName, lastName, phone);

            Assert.assertTrue(cp.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed!");
            String accPageType = cp.getAccountPageType();

            // Setup Account - Fix MT5, Open Account - can choose MT4/MT5
            if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
                Assert.assertTrue(cp.goToOpenAccountPage(), "Go to Open Account page failed!");
            } else {
                Assert.assertTrue(cp.goToSetupAccountPage(), "Go to Setup Account page failed!");
            }

            if (accountType != null && currency != null) {
                cp.fillAccountPage(platform, accountType, currency, accPageType);
            } else {
                cp.fillAccountPage(platform, accPageType);
            }

            //Level 2 POI
            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
            boolean bIsUseSumsub = cp.checkSumsubExists();
            LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

            cp.waitLoadingIdentityVerificationContent();

            if (bIsUseSumsub) {
                cp.fillIDPage_withSumsub(country);
            } else {
                cp.fillIDPage();
                cp.closeProfileVerificationDialog_withoutExit();
            }

            Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }

        cp.printUserRegisterInfo();

        return cp.userdetails;
    }

}
