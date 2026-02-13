package newcrm.testcases.cptestcases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.business.adminbusiness.*;
import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.CPRegisterGold;
import newcrm.business.businessbase.OWSLogin;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.business.businessbase.register.CPRegisterDemoGoldenFlow;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.AdminMenuName;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.ENV;

import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.UATServerEnv;
import newcrm.utils.api.RsaUtil;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegisterGoldTestcases extends BaseTestCaseNew {

    public Object data[][];
    public String email, code, ibCode, firstName, lastName, phone, pwd, idnum;
    public EmailDB emailDB;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;
    protected AdminMenu adminMenu;
    protected AdminIDPOAAudit adminIDPOAAudit;
    protected Factor myfactor;
    protected WebDriver driver;
    public String branchVer;

    @Override
    protected void login() {
        LogUtils.info("Do not need to login");
    }

    @Override
    @BeforeMethod(groups = {"CP_GoldenFlow_AU_Only", "CP_GF_Demo_Register", "CP_GF_Auto_Register", "CP_GF_Profile_Verification", "CP_GF_MTS_Register", "CP_GF_Live_Register"})
    public void goToCpHomePage() {
        LogUtils.info("Do not need go to home page");
    }

    @BeforeMethod(groups = {"CP_GoldenFlow_AU_Only", "CP_GF_Demo_Register", "CP_GF_Auto_Register", "CP_GF_Profile_Verification", "CP_GF_MTS_Register", "CP_GF_Live_Register"})
    public void initMethod(){
        if (driver ==null){
            driver = getDriverNew();
        }
        if (myfactor == null){
            myfactor = getFactorNew();
        }
    }

    @Test(description = testCaseDescUtils.CPACC_REGISTER_GOLDEN_FLOW_MT5_WITH_SUMSUB, groups = {"CP_GoldenFlow_AU_Only"})
    @Parameters(value= {"Country"})
    public void testRegistMT5LiveAccount_Gold(@Optional("")String country) throws Exception {
        if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(REGULATOR.VFSC.toString()))) {
            ibCode = GlobalMethods.getRegAffID(dbBrand.toString(), ENV.PROD);
            register(ibCode,"", "", PLATFORM.MT5,country,false, ACCOUNTTYPE.STANDARD_STP, CURRENCY.USD, true);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(description = testCaseDescUtils.CPACC_REGISTER_GOLDEN_FLOW_MT5_WITHOUT_CHECK_WITH_SUMSUB_PROD, groups = {"CP_GF_Live_Register"})
    @Parameters(value= {"Country"})
    public void testRegistMT5LiveAccountWithoutCheck_Gold(@Optional("")String country) throws Exception {
        if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(REGULATOR.VFSC.toString()))) {
            ibCode = GlobalMethods.getRegAffID(dbBrand.toString(), ENV.PROD);
            register(ibCode,"", "", PLATFORM.MT5,country,false, ACCOUNTTYPE.STANDARD_STP, CURRENCY.USD, true);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    public void navigateTraderUrl(CPRegisterGold cp, String ibCode, String rafCode, String campaignCode) {
        //you couldn't use ibcode and rafcode at same time
        if(ibCode.trim().length()>0) {
            cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(ENV.PROD.toString()));
        }else {
            if(rafCode.trim().length()>0) {
                cp.setRAFCode(rafCode);
            }else {
                if(campaignCode.trim().length()>0) {
                    cp.setCampaignCode(campaignCode);
                }
            }
        }

        cp.setTradeUrl(TraderURL);
    }

    public void navigateTraderUrlDemo(CPRegisterGold cp, String ibCode, String rafCode, String campaignCode) {
        //you couldn't use ibcode and rafcode at same time
        if(ibCode.trim().length()>0) {
            cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(ENV.PROD.toString()));
        }else {
            if(rafCode.trim().length()>0) {
                cp.setRAFCode(rafCode);
            }else {
                if(campaignCode.trim().length()>0) {
                    cp.setCampaignCode(campaignCode);
                }
            }
        }
    }

    // Make sure registration country does not match with client auto upgrade IB settings. Admin > Task > Commission Package & Group management
    public String getCountry(String country, boolean bIsUseSumsub) {
        // Refer to Apollo配置: 	sumsub.black.list
        if(country.equalsIgnoreCase("")) {
            ENV env = ENV.valueOf(TestEnv.toUpperCase());
            REGULATOR reg = REGULATOR.valueOf(Regulator.toUpperCase());

            // Support VFSC regulator only
            switch(reg) {
                case VFSC:
                    country = "Malaysia";
                    break;
                case VFSC2:
                    if (env == ENV.PROD) {
                        country = "France";
                        break;
                    }

                    country = bIsUseSumsub ? "Estonia" : "Latvia";
                    break;
                default:
                    throw new SkipException("Skipping this test intentionally.");
            }
        }

        return country;
    }

    public String getAutoRegisterCountry(String country) {
        if(country.equalsIgnoreCase("")) {
            ENV env = ENV.valueOf(TestEnv.toUpperCase());
            REGULATOR reg = REGULATOR.valueOf(Regulator.toUpperCase());

            switch(reg) {
                case VFSC, VFSC2:
                    if (env == ENV.ALPHA) {
                        country = "Laos";
                        break;
                    }
                    country = "Malaysia";
                    break;
                default:
                    throw new SkipException("Skipping this test intentionally.");
            }
        }

        return country;
    }

    public void generateUserTestData(CPRegisterGold cp) throws Exception {

        emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

        firstName = "autotest" + GlobalMethods.getRandomString(10);
        lastName = "TestCRM";
        email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
        idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();
        code = "";//"123456";

        cp.emailDB = emailDB;
        cp.TestEnv = TestEnv;
        cp.Regulator = Regulator;
        cp.dbenv = dbenv;
        cp.dbBrand = dbBrand;
        cp.dbRegulator = dbRegulator;
    }

    public void generateDemoUserTestData(CPRegisterGold cp) throws Exception {

        emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

        firstName = "autodemo" + GlobalMethods.getRandomString(10);
        lastName = "AutoCRM";
        email = ("autodemo"+GlobalMethods.getRandomString(8)+"@nqmo.com").toLowerCase();
        phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
        idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();
        code = "";//"123456";

        cp.emailDB = emailDB;
        cp.TestEnv = TestEnv;
        cp.Regulator = Regulator;
        cp.dbenv = dbenv;
        cp.dbBrand = dbBrand;
        cp.dbRegulator = dbRegulator;
    }

    public void generateUserAutoTestData(CPRegisterGold cp) throws Exception {

        emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

        firstName = "autoCRM" + GlobalMethods.getRandomString(10);
        lastName = "autoCRM";
        email = ("autoCRM"+GlobalMethods.getRandomString(8)+"@crmautomation.com").toLowerCase();
        phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
        idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();
        code = "";//"123456";

        cp.emailDB = emailDB;
        cp.TestEnv = TestEnv;
        cp.Regulator = Regulator;
        cp.dbenv = dbenv;
        cp.dbBrand = dbBrand;
        cp.dbRegulator = dbRegulator;
    }

    public HashMap<String,String> register(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country, boolean check,
                                           ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);

        // Test Data
        generateUserTestData(cp);
        navigateTraderUrl(cp, ibCode, rafCode, campaignID);
        country = getCountry(country, bIsUseSumsub);

        // Fill up registration info (before redirect to CP)
        if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()))
        {
            cp.setUserInfo(firstName, country, email, pwd);
            cp.entrySubmit(TraderURL);
        }
        else
        {
            testSiteRegistrationInfo(cp, country);
        }

        // Personal Details
        goToPersonalDetailPage(cp);
        verifyPersonalDetails_withLinkPhone(cp);
        fillPersonalDetails(cp);

        // Setup account
        setupAccount(cp, platform, accountType, currency);

        // Upload POI
        goToIDPage(cp);
        uploadPOI(cp, country, bIsUseSumsub, false);

        goToFinishPage(cp);

        // Audit account
        HashMap<String,String> userDetails = checkAndAuditAccount(cp, check, platform);

        return userDetails;
    }

    public HashMap<String,String> registerFixedOTP(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country, boolean check,
                                           ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);

        generateUserTestData(cp);
        navigateTraderUrl(cp, ibCode, rafCode, campaignID);
        country = getCountry(country, false);

        if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()))
        {
            cp.setUserInfo(firstName, country, email, pwd);
            cp.entrySubmit(TraderURL);

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

//            cp.verifyPersonalDetails_withLinkPhone(phone, email, code);
//            cp.fillPersonalDetails(firstName, lastName, phone);
//
//            Assert.assertTrue(cp.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed！");
//            String accPageType = cp.getAccountPageType();
//
//            // Setup Account - Fix MT5, Open Account - can choose MT4/MT5
//            if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
//                Assert.assertTrue(cp.goToOpenAccountPage(), "Go to Open Account page failed!");
//            } else {
//                Assert.assertTrue(cp.goToSetupAccountPage(), "Go to Setup Account page failed!");
//            }
//
//            if (accountType != null && currency != null) {
//                cp.fillAccountPage(platform, accountType, currency, accPageType);
//            } else {
//                cp.fillAccountPage(platform, accPageType);
//            }
//
//            //Level 2 POI
//            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
//            boolean bIsUseSumsub = cp.checkSumsubExists();
//            LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);
//
//            if (bIsUseSumsub) {
//                cp.fillIDPage_withSumsub(country);
//            } else {
//                cp.fillIDPage();
//            }
//
//            Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }
        else
        {
//            // AU alpha temp registration. Wait 17.11 all brand 双端统一 go live then remove
//            cp.registerLiveAccount_Temp((String)data[0][3], dbRegulator);
//            cp.setUserInfo(firstName, country, email, pwd);
//            cp.entrySubmit(TraderURL);

            cp.setRegulatorAndCountry(country, Regulator, "V3");
            email = email.replace("testcrmautomation", "testcrm");
            cp.setUserInfo(firstName, lastName, phone, email, pwd, GlobalMethods.getRegisterBrand(Brand.toUpperCase()));
            cp.setWid(Brand);
            cp.entrySubmit(TraderURL);
//            cp.entrySubmit_WithoutCheckURL();

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

//        //check info
//        if(check) {
//            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);
//
//            if(dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) {
//                if(auditMainAccount(cp.userdetails.get("User ID"),platform))
//                {
//                    System.out.println("Account Audit Passed");
//                }
//                Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
//            }
//            else
//            {
//                CPMenu menu = myfactor.newInstance(CPMenu.class);
//                menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);
//
//                CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
//                liveAccounts.waitLoadingAccountListContent();
//                liveAccounts.setViewContentGridMode();
//
//                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
//            }
//        }
//        else
//        {
//            LogUtils.info("No check for account");
//        }

        cp.printUserRegisterInfo();

        return cp.userdetails;
    }

    public HashMap<String,String> registerFixedOTP_CRMNEW_TEMP(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country, boolean check,
                                                   ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);

        driver.get("https://www.vantagemarkets.com/open-live-account-crm/");

        generateUserTestData(cp);
        country = getCountry(country, false);

        if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()))
        {
            cp.setUserInfo(firstName, country, email, pwd);
            cp.entrySubmit(TraderURL);

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

//            cp.verifyPersonalDetails_withLinkPhone(phone, email, code);
//            cp.fillPersonalDetails(firstName, lastName, phone);
//
//            Assert.assertTrue(cp.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed！");
//            String accPageType = cp.getAccountPageType();
//
//            // Setup Account - Fix MT5, Open Account - can choose MT4/MT5
//            if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
//                Assert.assertTrue(cp.goToOpenAccountPage(), "Go to Open Account page failed!");
//            } else {
//                Assert.assertTrue(cp.goToSetupAccountPage(), "Go to Setup Account page failed!");
//            }
//
//            if (accountType != null && currency != null) {
//                cp.fillAccountPage(platform, accountType, currency, accPageType);
//            } else {
//                cp.fillAccountPage(platform, accPageType);
//            }
//
//            //Level 2 POI
//            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
//            boolean bIsUseSumsub = cp.checkSumsubExists();
//            LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);
//
//            if (bIsUseSumsub) {
//                cp.fillIDPage_withSumsub(country);
//            } else {
//                cp.fillIDPage();
//            }
//
//            Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }
        else
        {
            // AU alpha temp registration. Wait 17.11 all brand 双端统一 go live then remove
            navigateTraderUrlDemo(cp, ibCode, rafCode, campaignID);
            String oldUrl = driver.getCurrentUrl();
            cp.inputPagePassword();
            cp.setDemoRegistrationDomainUrl((String)data[0][3], oldUrl);
//            email = email.replace("testcrmautomation", "testcrm");
            cp.setUserInfo_CRMNEW_TEMP(firstName, country, email, pwd);
            cp.entrySubmit(TraderURL);
//            cp.entrySubmit_WithoutCheckURL();

//            cp.setRegulatorAndCountry(country, Regulator, "V3");
//            cp.setUserInfo(firstName, lastName, phone, email, pwd, GlobalMethods.getRegisterBrand(Brand.toUpperCase()));
//            cp.setWid(Brand);
//            cp.entrySubmit(TraderURL);

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

//        //check info
//        if(check) {
//            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);
//
//            if(dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) {
//                if(auditMainAccount(cp.userdetails.get("User ID"),platform))
//                {
//                    System.out.println("Account Audit Passed");
//                }
//                Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
//            }
//            else
//            {
//                CPMenu menu = myfactor.newInstance(CPMenu.class);
//                menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);
//
//                CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
//                liveAccounts.waitLoadingAccountListContent();
//                liveAccounts.setViewContentGridMode();
//
//                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
//            }
//        }
//        else
//        {
//            LogUtils.info("No check for account");
//        }

        cp.printUserRegisterInfo();

        return cp.userdetails;
    }

    public HashMap<String,String> registerIBProgram_PersonalDetailsVerification(PLATFORM platform, String country, boolean check,
                                                                                ACCOUNTTYPE accountType, CURRENCY currency, CPRegisterGold cp) throws Exception {


        country = getCountry(country, false);

        if(TestEnv.equalsIgnoreCase(ENV.PROD.toString())) {


            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;
        }
        else
        {

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

            cp.sendEmailCode("999999");
            cp.sendPhoneCode("123456");
//            cp.verifyPersonalDetails_withLinkPhone(phone, email, ENV.valueOf(GlobalProperties.env.toUpperCase()));
            cp.fillPersonalDetails(firstName, lastName, phone);
            Assert.assertTrue(cp.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed!");

            driver.findElement(By.xpath("//div[@class='result_btns']")).click();

        }

        // Audit account
        HashMap<String,String> userDetails = checkAndAuditAccount(cp, check, platform);

        return userDetails;
    }

    public HashMap<String,String> registerDemo(String ibCode, String rafCode, PLATFORM platform, String country, boolean check,
                                               ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);
        CPRegisterDemoGoldenFlow cpDemo = myfactor.newInstance(CPRegisterDemoGoldenFlow.class);

        // Test Data
        generateDemoUserTestData(cp);
        navigateTraderUrlDemo(cp, ibCode, rafCode,"");
        country = getAutoRegisterCountry(country);

        // Demo account registration
        cpDemo.openDemoAccount((String)data[0][3], email, firstName, country, phone, pwd, branchVer);
        cpDemo.entrySubmit(TraderURL);

        // Personal Details
        goToPersonalDetailPage(cp);
        cpDemo.checkDemoAccountDetails();
        cpDemo.proceedToOpenLiveAccount();
        cp.verifyPersonalDetails(phone, email, ENV.valueOf(GlobalProperties.env.toUpperCase()));
        cp.fillPersonalDetails(firstName, lastName, phone);

        // Setup account
        setupAccount(cp, platform, accountType, currency);

        // Upload POI
        goToIDPage(cp);
        uploadPOI(cp, country, bIsUseSumsub, false);

        goToFinishPage(cp);

        // Check account audit status
        HashMap<String,String> userDetails = checkAccountAuditStatus(cp, check, platform, accountType, currency, country);

        return userDetails;
    }

    public HashMap<String,String> registerAuto(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country,
                                               ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);

        // Test Data
        generateUserAutoTestData(cp);
        navigateTraderUrl(cp, ibCode, rafCode, campaignID);
        country = getAutoRegisterCountry(country);

        // AU alpha temp registration. Wait 17.11 all brand 双端统一 go live then remove
//        cp.registerLiveAccount_Temp((String)data[0][3], dbRegulator);
//        cp.setUserInfo(firstName, country, email, pwd);
//        cp.entrySubmit();

        // Fill up registration info (before redirect to CP)
        testSiteRegistrationInfo(cp, country);

        // Personal Details
        goToPersonalDetailPage(cp);
        verifyPersonalDetails_withLinkPhone(cp);
        fillPersonalDetails(cp);

        // Setup account
        setupAccount(cp, platform, accountType, currency);

        // Upload POI
        goToIDPage(cp);
        uploadPOI(cp, country, bIsUseSumsub, false);

        // Check account audit status
        HashMap<String,String> userDetails = checkAccountAuditStatus(cp, true, platform, accountType, currency, country);

        return userDetails;
    }

    public HashMap<String,String> profileVerification(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country, boolean check,
                                                      ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

        CPRegisterGold cp = myfactor.newInstance(CPRegisterGold.class);
        CPMenu menu = myfactor.newInstance(CPMenu.class);
        CPUserProfile userProfile = myfactor.newInstance(CPUserProfile.class);

        // Test Data
        generateUserAutoTestData(cp);
        navigateTraderUrl(cp, ibCode, rafCode, campaignID);
        country = getCountry(country, bIsUseSumsub);

        // Fill up registration info (before redirect to CP)
        testSiteRegistrationInfo(cp, country);

        // Personal Details
        goToPersonalDetailPage(cp);
        verifyPersonalDetails_withLinkPhone(cp);
        cp.closeProfileVerificationDialog();

        // Standard Profile Verification
        profileVerificationLv1PersonalDetails(cp, menu, userProfile, platform, accountType, currency);
        profileVerificationLv2POIUpload(cp, menu, userProfile, country, bIsUseSumsub);
        profileVerificationLv3POAUpload(cp, menu, userProfile, country, bIsUseSumsub);

        // Advance Profile Verification
        profileVerificationAdvance(cp, menu, userProfile, country, bIsUseSumsub);

        return cp.userdetails;
    }

    private void profileVerificationLv1PersonalDetails(CPRegisterGold cp, CPMenu menu, CPUserProfile userProfile,
                                                       PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {
        // Go to profile page
        System.out.println("***Check Profile Verification Tab Content before complete lv 1***");
        menu.goToMenu(CPMenuName.PROFILES);
        userProfile.waitLoadingProfileTabListContent();

        // Verify Verification Tab Content before completed lv 1 Personal Details Verification
        userProfile.checkVerificationTabContentBeforeCompleteLv1();

        // Complete lv 1 Personal Details Verification
        System.out.println("***Proceed Lv 1 Personal Details Verification***");
        userProfile.clickProfileVTablv1VerifyBtn();

        // Personal Details
        cp.fillPersonalDetails(firstName, lastName, phone);

        // Setup account
        setupAccount(cp, platform, accountType, currency);
        cp.closeProfileVerificationDialog_withoutExit();

        // Go to profile page
        System.out.println("***Check Profile Verification Tab Content after completed lv 1***");

        // Verify Verification Tab Content after completed lv 1 Personal Details Verification
        userProfile.waitLoadingProfileTabListContent();
        userProfile.checkVerificationTabContentAfterCompleteLv1();

        System.out.println("***Completed Lv 1 Personal Details Verification***");
    }

    private void profileVerificationLv2POIUpload(CPRegisterGold cp, CPMenu menu, CPUserProfile userProfile, String country, boolean bIsUseSumsub) throws Exception {
        System.out.println("***Proceed Lv 2 Identity Verification***");
        userProfile.clickProfileVTablv2VerifyBtn();

        // Upload POI
        uploadPOI(cp, country, bIsUseSumsub, false);

        System.out.println("***Check Profile Verification Tab Content after completed lv 2***");

        // Verify Verification Tab Content after completed lv 2 Identity Verification
        userProfile.waitLoadingProfileTabListContent();
        userProfile.checkVerificationTabContentAfterCompleteLv2(bIsUseSumsub);

        System.out.println("***Completed Lv 2 Identity Verification***");
    }

    private void profileVerificationLv3POAUpload(CPRegisterGold cp, CPMenu menu, CPUserProfile userProfile, String country, boolean bIsUseSumsub) {
        System.out.println("***Proceed Lv 3 Residency Address Verification***");
        userProfile.clickProfileVTablv3VerifyBtn();

        // Upload POA
        uploadPOA(cp, country, bIsUseSumsub, false);

        userProfile.waitLoadingProfileTabListContent();

//        System.out.println("***Check Profile Verification Tab Content after completed lv 3***");
//
//        // Verify Verification Tab Content after completed lv 2 Residency Address Verification
//        userProfile.checkVerificationTabContentAfterCompleteLv3();

        System.out.println("***Completed Lv 3 Residency Address Verification***");
    }

    private void profileVerificationAdvance(CPRegisterGold cp, CPMenu menu, CPUserProfile userProfile, String country, boolean bIsUseSumsub) {
        System.out.println("***Proceed POI/POA Advance Verification***");
        userProfile.clickProfileVTabAdvanceVerifyBtn();

        // Upload Advance POI
        uploadPOI(cp, country, bIsUseSumsub, true);

        // Upload Advance POA
        uploadPOA(cp, country, bIsUseSumsub, true);

        System.out.println("***Check Profile Verification Tab Content after completed POI/POA Advance***");

        // Verify Verification Tab Content after completed POI/POA Advance Verification
        userProfile.waitLoadingProfileTabListContent();
        userProfile.checkVerificationTabContentAfterCompleteAdvance();

        System.out.println("***Completed POI/POA Advance Verification***");
    }


    public void testSiteRegistrationInfo(CPRegisterGold cp, String country) {
        cp.setRegulatorAndCountry(country, Regulator, "V3");
        cp.setUserInfo(firstName, lastName, phone, email, pwd, GlobalMethods.getRegisterBrand(Brand.toUpperCase()));
        cp.setWid(Brand);
        cp.setBranchVersion(branchVer);
        cp.entrySubmit(TraderURL);
    }

    public void goToPersonalDetailPage(CPRegisterGold cp) {
        Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
        cp.waitLoading();

        // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
        prevExecuteCPAccEmail = email;
    }

    private void goToIDPage(CPRegisterGold cp) {
        Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
    }

    private void goToFinishPage(CPRegisterGold cp) {
        Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
    }

    public void setupAccount(CPRegisterGold cp, PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {
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
    }

    public void verifyPersonalDetails_withLinkPhone(CPRegisterGold cp) throws Exception {
        cp.verifyPersonalDetails_withLinkPhone(phone, email, ENV.valueOf(GlobalProperties.env.toUpperCase()));
    }

    public void fillPersonalDetails(CPRegisterGold cp) {
        cp.fillPersonalDetails(firstName, lastName, phone);
    }

    private void uploadPOI(CPRegisterGold cp, String country, boolean bIsUseSumsub, boolean bIsAdvancePOI) {
        LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

        String poiDesc = bIsAdvancePOI ? "Advance POI" : "POI";

        if (bIsAdvancePOI) {
            cp.waitLoadingAdvanceVerificationContent();
        } else {
            cp.waitLoadingIdentityVerificationContent();
        }

        // Check exists on Sumsub content
        boolean bIsExistsSumsub = cp.checkSumsubExists();

        // Do not proceed when sumsub content not found when require sumsub verification. Vice versa.
        // Proceed Sumsub or manual verification for POI or Advance POI
        if (bIsUseSumsub) {
            if (!bIsExistsSumsub) {
                Assert.fail(String.format("Not able to proceed %s Sumsub flow due to %s country does not support Sumsub Verification. Please use another country", poiDesc, country));
            }
            if (bIsAdvancePOI) {
                // Sumsub Advance POI
                cp.fillAdvanceFinancialPage_withSumsub(country);
                cp.fillAdvanceIDPage_withSumsub(country);
            } else {
                // Sumsub POI
                cp.fillIDPage_withSumsub(country);
                cp.closeProfileVerificationDialog();
            }
        } else {
            if (bIsExistsSumsub) {
                Assert.fail(String.format("Not able to proceed %s manual flow due to %s country only support Sumsub Verification. Please use another country", poiDesc, country));
            }
            if (bIsAdvancePOI) {
                // Manual Advance POI
                cp.fillAdvanceFinancialPage();
                cp.fillAdvanceIDPage();
            } else {
                // Manual POI
                cp.fillIDPage();
                cp.closeProfileVerificationDialog_withoutExit();
            }
        }
    }

    private void uploadPOA(CPRegisterGold cp, String country, boolean bIsUseSumsub, boolean bIsAdvancePOA) {
        LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

        String poaDesc = bIsAdvancePOA ? "Advance POA" : "POA";

        if (bIsAdvancePOA) {
            cp.waitLoadingAdvanceVerificationContent();
        } else {
            cp.waitLoadingPOAVerificationContent();
        }

        // Check exists on Sumsub content
        boolean bIsExistsSumsub = cp.checkSumsubExists();

        // Proceed Sumsub or manual verification for POA or Advance POA
        if (bIsUseSumsub) {
            if (!bIsExistsSumsub) {
                Assert.fail(String.format("Not able to proceed %s Sumsub flow due to %s country does not support Sumsub Verification. Please use another country", poaDesc, country));
            }
            if (bIsAdvancePOA) {
                // Sumsub Advance POA
                cp.fillAdvanceAddressDetails_withSumsub();
            } else {
                // Sumsub POA
                cp.fillAddressDetails_withSumsub(country);
            }
            cp.closeProfileVerificationDialog();
        } else {
            if (bIsExistsSumsub) {
                Assert.fail(String.format("Not able to proceed %s manual flow due to %s country only support Sumsub Verification. Please use another country", poaDesc, country));
            }
            // Manual POA & Advance POA
            cp.fillAdvanceAddressDetails();
            cp.closeProfileVerificationDialog_withoutExit();
        }
    }

    private HashMap<String,String> checkAndAuditAccount(CPRegisterGold cp, boolean bIsReqCheckAcc, PLATFORM platform) throws Exception {
        // Proceed account checking when require
        if(bIsReqCheckAcc) {
            // Check exists user via db
            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

            if(dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) {
                // Audit account
                auditMainAccount(cp, platform);
            }
            else
            {
                // For production, check if there is any account created in CP account page
                CPMenu menu = myfactor.newInstance(CPMenu.class);
                menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);

                CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
                liveAccounts.waitLoadingAccountListContent();
                liveAccounts.setViewContentGridMode();

                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
            }
        }
        else
        {
            LogUtils.info("Skip account existence check");
        }

        cp.printUserRegisterInfo();

        return cp.userdetails;
    }

    private HashMap<String,String> checkAccountAuditStatus(CPRegisterGold cp, boolean bIsReqCheckAcc, PLATFORM platform,
                                                           ACCOUNTTYPE accountType, CURRENCY currency, String country) throws Exception {
        // Proceed account checking when require
        if(bIsReqCheckAcc) {
            // Check exists user via db
            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

            if(dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) {

                // Check exists user
                cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

                // Check exists account after auto audit
                String userId = cp.userdetails.get("User ID");
                if(!checkAutoAuditMainAccount(userId))
                {
                    Assert.fail(String.format("Account Auto Audit Failed. Please manually check record audit status and auto audit country settings. User ID: %s, Country: %s", userId, country));
                }

                cp.printUserRegisterInfo();

                // Verify account info and update to test group
                checkAccountInfoAndUpdateToTestGroup(platform, userId, email, accountType, currency);
            }
            else
            {
                // For production, check if there is any account created in CP account page
                CPMenu menu = myfactor.newInstance(CPMenu.class);
                menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);

                CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
                liveAccounts.waitLoadingAccountListContent();
                liveAccounts.setViewContentGridMode();

                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");

                cp.printUserRegisterInfo();
            }
        }
        else
        {
            LogUtils.info("Skip account existence check");
            cp.printUserRegisterInfo();
        }

        return cp.userdetails;
    }

    private void checkAccountInfoAndUpdateToTestGroup(PLATFORM platform, String userId, String email, ACCOUNTTYPE accountType, CURRENCY currency) {
        System.out.println("***Check auto register account info***");

        AdminAPIUserAccount adminAcctAPI = new AdminAPIUserAccount((String) data[0][4], REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], BRAND.valueOf(String.valueOf(dbBrand)), ENV.valueOf(String.valueOf(dbenv)));

        // Search trading account info
        JSONObject obj = adminAcctAPI.apiTradingAcctSearch("", RsaUtil.getAdminEmailEncrypt(email), platform, userId, "auto", "ALL");
        JSONArray rows = obj.getJSONArray("rows");

        if(rows==null || rows.isEmpty()) {
            Assert.fail("Auto Register trading account not found. User ID: " + userId);
        }

        JSONObject tradingAcct = rows.getJSONObject(0);
        String accNum = tradingAcct.getString("mt4_account");
        String mt4AccountType = tradingAcct.getString("mt4_account_type");
        String mt4AccountTypeDisplay = tradingAcct.getString("mt4_account_type_display");
        String serverId = tradingAcct.getString("mt4_datasource_id");
        String accLeverage = tradingAcct.getString("leverage");
        String accGroup = tradingAcct.getString("group");
        String accCurrency = tradingAcct.getString("currency");
        String owner = tradingAcct.getString("owner");
        String pIds = tradingAcct.getString("p_ids");
        String[] pIdsArray = pIds.split(",");
        String latestPId = pIdsArray[pIdsArray.length - 1];
        JSONObject dataSrc = tradingAcct.getJSONObject("dataSource");
        String serverPlatform = "";

        LogUtils.info(String.format("Auto Register trading account found. Account: %s, Leverage: %s, Currency: %s, Account Type: %s (%s)", accNum, accLeverage, currency, mt4AccountTypeDisplay, mt4AccountType));

        // Cross-check system data stored with submission info
        // Platform
        if (dataSrc != null) {

            serverPlatform = dataSrc.getString("category");

            LogUtils.info(String.format("Trading Platform: %s", serverPlatform));

            if (serverPlatform == null || serverPlatform.isEmpty()) {
                Assert.fail(String.format("Auto Register Platform is empty. Account: %s", accNum));
            }

            if (!serverPlatform.equals(platform.getPlatformDesc())) {
                Assert.fail(String.format("Auto Register Platform mismatch. Account: %s, Expected: %s, Actual: %s", accNum, platform.getPlatformDesc(), serverPlatform));
            }
        } else {
            Assert.fail(String.format("Auto Register Platform is empty. Account: %s", accNum));
        }

        // Check default leverage
        String defaultLeverage = "500";
        if (accLeverage == null || accLeverage.isEmpty()) {
            Assert.fail("Auto Register account leverage is empty, Account: " + accNum);
        }

        if (!defaultLeverage.equals(accLeverage.trim())) {
            Assert.fail(String.format("Auto Register default account leverage is incorrect, Account: %s, Expected: %s, Actual: %s", accNum, defaultLeverage, accLeverage));
        }

        // Account Type
        if (mt4AccountType == null || mt4AccountType.isEmpty()) {
            Assert.fail(String.format("Auto Register Account Type Code is empty. Account: %s", accNum));
        }

        if (!mt4AccountType.equals(accountType.getAccountTypeCode())) {
            Assert.fail(String.format("Auto Register Account Type Code mismatch. Account: %s, Expected: %s (%s), Actual: %s (%s)", accNum, accountType.getLiveAccountName(), accountType.getAccountTypeCode(), mt4AccountTypeDisplay, mt4AccountType));
        }

        // Currency
        if (accCurrency == null || accCurrency.isEmpty()) {
            Assert.fail(String.format("Auto Register Currency is empty. Account: %s", accNum));
        }

        if (!accCurrency.equals(currency.getCurrencyDesc())) {
            Assert.fail(String.format("Auto Register Currency mismatch. Account: %s, Expected: %s, Actual: %s", accNum, currency.getCurrencyDesc(), accCurrency));
        }

        System.out.println("***Update account to test group***");

        // Get server's account test group
        List<String> groups = adminAcctAPI.getGroups(serverId, "1", userId, mt4AccountType);
        if(groups==null) {
            Assert.fail("Failed to update account to test group. Reason: No account test group is available for Server ID " + serverId);
        }

        List<String> filtered = groups.stream()
                .filter(groupName -> (groupName.contains("test") || groupName.startsWith("t_")) && groupName.contains(currency.getCurrencyDesc()))
                .toList();

        String newGroup = "";

        if (filtered.isEmpty()) {
            Assert.fail("Failed to update account to test group. Reason: No account test group is available for Server ID " + serverId);
        } else {
            Random random = new Random();
            newGroup = filtered.get(random.nextInt(filtered.size()));
            LogUtils.info("Randomly select account test group: " + newGroup + ", Server ID: " + serverId);
        }

        // Update account to test group
        adminAcctAPI.apiTradingAcctUpdate(accNum, accGroup, mt4AccountType, owner, latestPId, newGroup);
        LogUtils.info("Update account to test group, Account: " + accNum + ", Default Group: " + accGroup + ", New Group: " + newGroup);
    }

    public void auditMainAccount(CPRegisterGold cp, PLATFORM platform) throws Exception {
        String userId = cp.userdetails.get("User ID");

        if(dbenv.equals(ENV.UAT)) {
            // Account Audit via OWS
            if (!auditAccountViaOWS(userId)) {
                Assert.fail("Account audit via OWS failed.");
            }
        }else{
            // Account Audit via Admin API
            Map.Entry<Boolean, String> resp = auditAccountViaAdmin(userId, platform);

            if (!resp.getKey()) {
                Assert.fail("Account audit via Admin API failed. Reason: " + resp.getValue());
            }
        }
    }

    public Map.Entry<Boolean, String> auditAccountViaAdmin(String userId, PLATFORM platform) throws Exception {
        // Account Audit via Admin API
        String user = data[0][7].toString();
        String password = data[0][8].toString();

        if(dbenv.equals(ENV.PROD)) {
            user =  "Test CRM";
            password = "Hc8P4RxuKMMmGKSgEZim";
        }

        // Audit Account
        AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL, dbRegulator, user, password, dbenv, dbBrand);
        return admin.auditMainAccount(userId, platform);
    }

    public boolean auditAccountViaOWS(String userId) {
        // Account Audit via OWS
        String OWSName = data[0][9].toString();
        String OWSPass = data[0][10].toString();

        LogUtils.info("**Account Audit via OWS**");

        if (driver ==null){
            driver = getDriverNew();
        }

        if (myfactor == null){
            myfactor = getFactorNew();
        }

        // Open OWS in new tab
        String ows_url = UATServerEnv.getOWSUrl(GlobalProperties.ENV.UAT.toString());
        String originalHandle = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsLogin = myfactor.newInstance(OWSLogin.class, ows_url);

        // OWS Login
        owsLogin.login(OWSName,OWSPass);

        // Audit Account
        boolean result = owsDashboard.auditTradingAccountFlow(userId,"","");

        // Close OWS Tab
        driver.close();
        driver.switchTo().window(originalHandle);

        return result;
    }

    public boolean auditMainAccountAndPOI(String userId, PLATFORM platform) throws Exception {
        String user = data[0][7].toString();
        String password = data[0][8].toString();
        String OWSName = data[0][9].toString();
        String OWSPass = data[0][10].toString();

        if(dbenv.equals(ENV.PROD)) {
            user =  "Test CRM";
            password = "Hc8P4RxuKMMmGKSgEZim";
        }

        // UAT - use OWS to audit account & POI
        // Alpha - use admin API to audit account & POI
        if(dbenv.equals(ENV.UAT)) {

            if (driver ==null){
                driver = getDriverNew();
            }
            if (myfactor == null){
                myfactor = getFactorNew();
            }

            // Open OWS in new tab
            String ows_url = UATServerEnv.getOWSUrl(GlobalProperties.ENV.UAT.toString());
            String originalHandle = driver.getWindowHandle();
            driver.switchTo().newWindow(WindowType.TAB);
            owsDashboard = myfactor.newInstance(OWSDashboard.class);
            owsLogin = myfactor.newInstance(OWSLogin.class, ows_url);

            // OWS Login
            owsLogin.login(OWSName,OWSPass);
            // OWS Account Audit
            boolean accResult = owsDashboard.auditTradingAccountFlow(userId,"","");
            // OWS POI Audit
            boolean poiResult = owsDashboard.auditPOI(userId,"","");
            // Close OWS Tab
            driver.close();
            driver.switchTo().window(originalHandle);

            LogUtils.info(String.format("Account Audit Status: %s, POI Audit Status: %s", accResult, poiResult));
            return accResult && poiResult;
        } else {
            Boolean auditResult;
            AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL, dbRegulator, user, password, dbenv, dbBrand);

            // Admin Account Audit
            auditResult = admin.auditMainAccount(userId, platform).getKey();

            if(auditResult) {
                // Admin POI Audit
                // Open Admin in new tab
                String originalHandle = driver.getWindowHandle();
                driver.switchTo().newWindow(WindowType.TAB);
                BaseTestCaseNew.UserConfig config = getConfigNew();
                AdminLogin apLogin = myfactor.newInstance(AdminLogin.class,AdminURL);
                apLogin.login(config.AdminName, config.AdminPass);

                adminMenu = myfactor.newInstance(AdminMenu.class);
                adminIDPOAAudit = myfactor.newInstance(AdminIDPOAAudit.class);
                adminMenu.changeRegulator(Regulator);
                adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);
                adminIDPOAAudit.clientPoiAudit(email);
                driver.close();
                driver.switchTo().window(originalHandle);
            }

            return auditResult;
        }
    }

    public boolean checkAutoAuditMainAccount(String userId) throws Exception {
        String user = data[0][7].toString();
        String password = data[0][8].toString();
        if(dbenv.equals(ENV.PROD)) {
            user =  "Test CRM";
            password = "Hc8P4RxuKMMmGKSgEZim";
        }

        AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL, dbRegulator, user, password, dbenv, dbBrand);
        return admin.checkAutoAuditMainAccount(userId);
    }

}
