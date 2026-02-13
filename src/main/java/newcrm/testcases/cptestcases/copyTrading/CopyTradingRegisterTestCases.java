package newcrm.testcases.cptestcases.copyTrading;

import com.alibaba.fastjson.JSONObject;
import newcrm.business.adminbusiness.AdminAPIBusiness;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.copyTrading.CPCopyTrading;
import newcrm.business.businessbase.copyTrading.CPCopyTradingRegister;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.cptestcases.RegisterTestcases;
import newcrm.utils.UATServerEnv;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopyTradingRegisterTestCases extends BaseTestCase {

    public RegisterTestcases registerTestcases = new RegisterTestcases();
    public String email, code, ibCode, firstName, lastName, phone, pwd, idnum;
    public EmailDB emailDB;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;

    @Override
    protected void login() {
        GlobalMethods.printDebugInfo("Do not need to login");
    }

    @Override
    @BeforeMethod(alwaysRun=true)
    public void goToCpHomePage() {
        GlobalMethods.printDebugInfo("Do not need go to home page");
    }

    @Test(priority = 0, description = testCaseDescUtils.CPACC_REGISTER_GOLDEN_FLOW_MT5)
    @Parameters(value= {"Country"})
    public void testRegistMTSGoldenFlowLiveAccount(@Optional("")String country) throws Exception {
        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC.toString()))) {
            ibCode = getPRODIBCode(dbBrand.toString());
            registerGoldenFlow(ibCode,"", GlobalProperties.PLATFORM.MT5,country,false, GlobalProperties.ACCOUNTTYPE.STANDARD_STP, GlobalProperties.CURRENCY.USD);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 0, description = testCaseDescUtils.CPACC_REGISTER_GOLDEN_FLOW_MT5_WITHOUT_CHECK_WITH_SUMSUB)
    @Parameters(value= {"Country"})
    public void testRegistMTSGlodenFlowLiveAccountWithoutCheck(@Optional("")String country) throws Exception {
        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) && (Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC2.toString()) || Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.VFSC.toString()))) {
            ibCode = getPRODIBCode(dbBrand.toString());
            registerGoldenFlow(ibCode,"", GlobalProperties.PLATFORM.MT5,country,false, GlobalProperties.ACCOUNTTYPE.STANDARD_STP, GlobalProperties.CURRENCY.USD);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    public void navigateTraderUrl(CPCopyTradingRegister cp, String ibCode, String rafCode) {
        //you couldn't use ibcode and rafcode at same time
        if(ibCode.trim().length()>0) {
            cp.setIBcode(ibCode.trim(), BaseTestCase.TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()));
        }else {
            if(rafCode.trim().length()>0) {
                cp.setRAFCode(rafCode);
            }
        }

        cp.setTradeUrl(BaseTestCase.TraderURL);
    }

    public String getCountry(String country) {
        // Refer to Apollo配置: 	sumsub.black.list
        if(country.equalsIgnoreCase("")) {

            // Support VFSC regulator only
            switch(BaseTestCase.Regulator.toLowerCase()) {
                case "vfsc":
                    country = "Malaysia";
                    break;
                case "vfsc2":
                    country = "France";
                    break;
                default:
                    throw new SkipException("Skipping this test intentionally.");
            }
        }

        return country;
    }

    public void generateUserTestData(CPCopyTradingRegister cp) throws Exception {

        emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

        firstName = "autotest" + GlobalMethods.getRandomString(10);
        lastName = "TestCRM";
        email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
        idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();
        code = "999999";

        cp.emailDB = emailDB;
        cp.TestEnv = TestEnv;
        cp.Regulator = Regulator;
        cp.dbenv = dbenv;
        cp.dbBrand = dbBrand;
        cp.dbRegulator = dbRegulator;
    }

    public HashMap<String,String> registerNew(String ibCode, String rafCode, GlobalProperties.PLATFORM platform, String country, boolean check
            , GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency) throws Exception {

        //instance
        CPRegister cp = myfactor.newInstance(CPRegister.class);
        CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        CPMenu menu = myfactor.newInstance(CPMenu.class);
        emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

        //data
        String firstName = "autotest" + GlobalMethods.getRandomString(10);
        String lastName = "TestCRM";
        email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        String phone = GlobalMethods.getRandomNumberString(10);
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        String pwd = GlobalMethods.generatePassword();
        code = "987654";
        String regBrand = GlobalMethods.getRegisterBrand(BaseTestCase.Brand.toUpperCase());

        //you couldn't use ibcode and rafcode at same time
        if(ibCode.trim().length()>0) {
            cp.setIBcode(ibCode.trim(), BaseTestCase.TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()));
        }else {
            if(rafCode.trim().length()>0) {
                cp.setRAFCode(rafCode);
            }
        }

        cp.setTradeUrl(BaseTestCase.TraderURL);

        //change country to fit regulator
        if(country.equalsIgnoreCase("")) {
            if((Brand.equalsIgnoreCase(GlobalProperties.BRAND.PUG.toString()) && !Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) ||
                    Brand.equalsIgnoreCase(GlobalProperties.BRAND.VT.toString()) ||
                    (Brand.equalsIgnoreCase(GlobalProperties.BRAND.STAR.toString()) && !Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())))
            {
                country = "Italy";
            }
            else if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VJP.toString()))
            {
                country = "Japan";
            }
            else {
                switch(BaseTestCase.Regulator.toLowerCase()) {
                    case "asic":
                        country = "Australia";
                        break;
                    case "vfsc":
                        country = "Malaysia";
                        break;
                    case "vfsc2":
                        country = "France";
                        break;
                    //如果是prod环境，除特殊监管外使用随机国家注册
                    default:
                        country = "France";
                        break;
                }
            }
        }
        //for brands already complete registration enhancement
        if(BaseTestCase.TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()) )
        {
            List<String> skipBrands = Arrays.asList(GlobalProperties.BRAND.VJP.toString(), GlobalProperties.BRAND.PUG.toString(), GlobalProperties.BRAND.UM.toString(), GlobalProperties.BRAND.VFX.toString(), GlobalProperties.BRAND.STAR.toString(), GlobalProperties.BRAND.MO.toString());
            if(!skipBrands.contains(dbBrand.toString().toUpperCase())) {
                cp.openLiveAccount();
            }
            switch(BaseTestCase.Brand.toUpperCase()) {
                case ("VT"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    break;
                case ("MO"), ("VFX"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    cp.entrySubmit(TraderURL);
                    break;
                case ("PUG"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    cp.entrySubmit(TraderURL);
                    setCode(cp,emailDB);
                    break;
                case ("VJP"):
                    cp.setEmail(email);
                    setCode(cp,emailDB);
                    cp.setUserInfo(firstName,country,email,pwd);
                    break;
                case ("UM"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    setCode(cp,emailDB);
                    cp.entrySubmit(TraderURL);
                    break;
                case ("STAR"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    setCode(cp,emailDB);
                    break;
            }

            cp.entrySubmit(TraderURL);

            //Level 1
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            BaseTestCase.prevExecuteCPAccEmail = email;

            cp.fillPersonalDetails(idnum, firstName, lastName, phone);

            Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
            if (accountType != null && currency != null) {
                cp.fillAccountPage(platform, accountType, currency);
            } else {
                cp.fillAccountPage(platform);
            }

            //Level 2 & 3
            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

            cp.bIDSumsub = cp.checkSumsubExists();
            GlobalMethods.printDebugInfo("Require Sumsub Verification: " + cp.bIDSumsub);

            if (cp.bIDSumsub) {
                cp.fillIDPage_withSumsub(country);
            } else {
                cp.fillIDPage();
            }

            Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }
        else
        {
            //registration api V1 is: /api/registration/register, V2 is:  /api/registrationV2/register
            List<String> v2Brand = Arrays.asList(GlobalProperties.BRAND.VFX.toString(), GlobalProperties.BRAND.VT.toString(), GlobalProperties.BRAND.PUG.toString(), GlobalProperties.BRAND.UM.toString(),
                    GlobalProperties.BRAND.STAR.toString(), GlobalProperties.BRAND.VJP.toString(), GlobalProperties.BRAND.MO.toString());

            if(v2Brand.contains(Brand.toUpperCase())) {
                cp.setRegulatorAndCountry(country, BaseTestCase.Regulator, "V2");
                cp.sendCode(code);
                cp.setUserInfo(firstName, lastName, phone, email, pwd, regBrand);
                cp.setWid(Brand.toString());
                cp.entrySubmit(TraderURL);

                // VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
                // ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
                // Level 1
                Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
                cp.waitLoading();

                // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
                BaseTestCase.prevExecuteCPAccEmail = email;

                cp.fillPersonalDetails(idnum, firstName, lastName, phone);

                if ((Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) || Brand.equalsIgnoreCase(GlobalProperties.BRAND.STAR.toString()) || Brand.equalsIgnoreCase(GlobalProperties.BRAND.PUG.toString())) &&
                        Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

                    Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
                    cp.fillAddressDetails();

                    Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
                    cp.fillFinacialPage();
                }

                Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
                if (accountType != null && currency != null) {
                    cp.fillAccountPage(platform, accountType, currency);
                } else {
                    cp.fillAccountPage(platform);
                }

                // Level 2 POI
                Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

                cp.bIDSumsub = cp.checkSumsubExists();
                GlobalMethods.printDebugInfo("Require Sumsub Verification: " + cp.bIDSumsub);

                if (cp.bIDSumsub) {
                    cp.fillIDPage_withSumsub(country);
                } else {
                    cp.fillIDPage();
                }

                if ((Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()) || Brand.equalsIgnoreCase(GlobalProperties.BRAND.STAR.toString()) || Brand.equalsIgnoreCase(GlobalProperties.BRAND.PUG.toString())) &&
                        Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

                    Assert.assertTrue(cp.goToQuizPage(), "Go to Quiz page failed!");
                    cp.fillQuizPage();
                }

                Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
            }
            else
            {
                cp.setRegulatorAndCountry(country, BaseTestCase.Regulator, "V1");
                //cp.sendCode(code);
                cp.setUserInfo(firstName, lastName, phone, email, pwd, regBrand);
                cp.entrySubmit(TraderURL);

                Assert.assertTrue(cp.goToPersonalDetailPage(),"Go to Personal Details page failed!");
                cp.waitLoading();
                cp.fillPersonalDetails(idnum);

                Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
                cp.fillAddressDetails();

                Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
                cp.fillFinacialPage();

                Assert.assertTrue(cp.goToAccountPage(),"Go to Account page failed!");
                if(accountType!=null && currency!=null) {
                    cp.fillAccountPage(platform, accountType, currency);
                }else {
                    cp.fillAccountPage(platform);
                }

                Assert.assertTrue(cp.goToIDPage(),"Go to ID page failed!");
                cp.fillIDPage();

                Assert.assertTrue(cp.goToFinishPage(),"Go to Finish page failed");
            }
        }
        //check info
        if(check) {
            GlobalMethods.printDebugInfo("Check for account");

            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);
            //Assert.assertTrue(cp.checkInfoAfterFinish(firstName, BaseTestCase.Regulator, BaseTestCase.TestEnv,BaseTestCase.Brand), "Failed at check user info");
            if(dbenv.equals(GlobalProperties.ENV.ALPHA) && !dbBrand.equals(GlobalProperties.BRAND.AT)) {
                if(auditMainAccount(cp.userdetails.get("User ID"),platform))
                {
                    System.out.println("Account Audit Passed");
                }
                Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
            }
            else
            {
                menu.goToMenu(GlobalProperties.CPMenuName.HOME);
                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
            }
        }
        else
        {
            GlobalMethods.printDebugInfo("No check for account");
        }
        cp.printUserRegisterInfo();

        return cp.userdetails;
    }
    public HashMap<String,String> registerGoldenFlow(String ibCode, String rafCode, GlobalProperties.PLATFORM platform, String country, boolean check,
                                                     GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency) throws Exception {

        CPCopyTradingRegister cp = myfactor.newInstance(CPCopyTradingRegister.class);
        CPCopyTrading cpCopyTrading = myfactor.newInstance(CPCopyTrading.class);
        CPMenu menu = myfactor.newInstance(CPMenu.class);


        generateUserTestData(cp);
        navigateTraderUrl(cp, ibCode, rafCode);
        country = getCountry(country);

        if(BaseTestCase.TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()))
        {
            cp.setUserInfo(firstName, country, email, pwd);

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            BaseTestCase.prevExecuteCPAccEmail = email;

            // Phone OTP value can only be found in redis / logs. Hence, proceed till email OTP only
            cp.verifyPersonalDetails_email(email, code);

            Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }
        else
        {
            cp.setRegulatorAndCountry(country, BaseTestCase.Regulator, "V3");
            cp.setUserInfo(firstName, lastName, phone, email, pwd, GlobalMethods.getRegisterBrand(BaseTestCase.Brand.toUpperCase()));
            cp.setWid(Brand);
            cp.entrySubmit(TraderURL);

            // Level 1 - Personal Details Verification
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            BaseTestCase.prevExecuteCPAccEmail = email;

            cp.verifyPersonalDetails_withLinkPhone(phone, email, code,GlobalProperties.ENV.valueOf(GlobalProperties.env.toUpperCase()));
            cp.fillPersonalDetails(firstName, lastName, phone);

            Assert.assertTrue(cp.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed!");
            String accPageType = cp.getAccountPageType();

            if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
                Assert.assertTrue(cp.goToOpenAccountPage(), "Go to Open Account page failed!");
            } else {
                Assert.assertTrue(cp.goToSetupAccountPage(), "Go to Setup Account page failed!");
            }

            //close setup account, go to copier to open copy account
            cp.closeSetUpAccount();
            menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
            cpCopyTrading.openCopyAccount();

            if (accountType != null && currency != null) {
                cp.fillAccountPage(platform, accountType, currency, accPageType);
            } else {
                cp.fillAccountPage(platform, accPageType);
            }

            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

            //check account open review message
            Assert.assertTrue(cp.getAccountOpenTitle(),"The account open review message check failed");
            Assert.assertTrue(cp.getAccountOpenCompleteContext(),"The account open review complete context check failed");
            //Level 2 POI
          //  Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
           /* boolean bIsUseSumsub = cp.checkSumsubExists();
            GlobalMethods.printDebugInfo("Require Sumsub Verification: " + bIsUseSumsub);

            if (bIsUseSumsub) {
                cp.fillIDPage_withSumsub();
            } else {
                cp.fillIDPage();
            }*/

          //  Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }

        //check info
        if(check) {
            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

            if(dbenv.equals(GlobalProperties.ENV.ALPHA)) {
                if(auditMainAccount(cp.userdetails.get("User ID"),platform))
                {
                    System.out.println("Account Audit Passed");
                }
                Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
            }
            else
            {
                menu.goToMenu(GlobalProperties.CPMenuName.HOME);

                CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
            }
        }
        else
        {
            GlobalMethods.printDebugInfo("No check for account");
        }

        cp.printUserRegisterInfo();

        return cp.userdetails;
    }

    public String getPRODIBCode(String brand)
    {
        HashMap<String,String> brandIBCode = new HashMap<>();
        brandIBCode.put("vfx","NBMTAxNTE3Nw==&am=");
        brandIBCode.put("pug","NBMTAwMDE5MDY=");
        brandIBCode.put("vt","NBMTAwMDIzNDc=&am=");
        brandIBCode.put("um","NBODMxMTc0&am=");
        brandIBCode.put("mo","NBMTI4Mg==&am=");
        brandIBCode.put("star","NBMTAxNTE3Nw==&am=");
        brandIBCode.put("vjp","NBMjY2NjQyMw==");

        if(!brandIBCode.containsKey(brand.toLowerCase()))
        {
            return("");
        }
        return brandIBCode.get(brand.toLowerCase());
    }

    public boolean auditMainAccount(String userId, GlobalProperties.PLATFORM platform) throws Exception {

        String user = "cmatest";
        String password = "123Qwe";
        if(dbenv.equals(GlobalProperties.ENV.PROD)) {
            user =  "Test CRM";
            password = "Hc8P4RxuKMMmGKSgEZim";
        }

        // UAT use OWS to audit
        if(dbenv.equals(GlobalProperties.ENV.UAT)) {
            String OWSName = "cmatest";
            String OWSPass = "123Qwe@@";
            String ows_url = UATServerEnv.getOWSUrl(GlobalProperties.ENV.UAT.toString());

            String originalHandle = driver.getWindowHandle();
            driver.switchTo().newWindow(WindowType.TAB);
            owsDashboard = myfactor.newInstance(OWSDashboard.class);
            owsLogin = myfactor.newInstance(OWSLogin.class, ows_url);
            owsLogin.login(OWSName,OWSPass);
            boolean result = owsDashboard.auditTradingAccountFlow(userId,"","");
            driver.close();
            driver.switchTo().window(originalHandle);

            return result;
        }else{
            AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL,dbRegulator,user,password,dbenv,dbBrand);
            return admin.auditMainAccount(userId, platform).getKey();
        }
    }
    public void setCode(CPRegister cp,EmailDB emailDB)
    {
        try {
            cp.clickCodeBtn();
            code = getCode(emailDB);
            cp.sendCode(code);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no need to enter code");
        }
    }
    public String getCode(EmailDB instance)
    {
        JSONObject obj = null;
        Map<String, GlobalProperties.REGULATOR> regulatorMap = Map.of(
                "vfsc", GlobalProperties.REGULATOR.VFSC,
                "vfsc2", GlobalProperties.REGULATOR.VFSC2,
                "svg", GlobalProperties.REGULATOR.SVG,
                "fsa", GlobalProperties.REGULATOR.FSA
        );

        GlobalProperties.REGULATOR regulator = regulatorMap.getOrDefault(BaseTestCase.Regulator.toLowerCase(), null);

        try {
            if (regulator != null) {
                obj = instance.getCodeRecord(dbenv, dbBrand, regulator, email);
            } else {
                obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator, email);
            }
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("An error occurred when retrieve data from db. Error Msg: " + e.getMessage());
        }

        GlobalMethods.printDebugInfo(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
        String code = obj.getJSONObject("vars").getString("CODE");
        return code;
    }

}
