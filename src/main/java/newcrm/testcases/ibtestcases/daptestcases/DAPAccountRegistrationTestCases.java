package newcrm.testcases.ibtestcases.daptestcases;


import adminBase.Login;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.dapbase.DAPDashboard;
import newcrm.business.businessbase.dapbase.DAPRegistration;
import newcrm.business.businessbase.ibbase.account.IBDemoAccount;
import newcrm.business.businessbase.ibbase.account.IBProgramRegistration;
import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.DAPBaseTestCaseNew;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.RegisterGoldTestcases;
import newcrm.utils.app.Register;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import javax.validation.constraints.AssertTrue;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import static newcrm.utils.testCaseDescUtils.CPWEBTRADING_PLACEORDER;
import static newcrm.utils.testCaseDescUtils.DAP_CPA_Registration;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DAPAccountRegistrationTestCases extends DAPBaseTestCaseNew {

    protected AdminAPIUserAccount adminAcctAPI;
    public Object data[][];
    protected CPRegisterGold cp;
    protected DAPRegistration dapRegistration;
    protected DAPLogin dapLogin;
    protected DAPDashboard dapDashboard;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;

    protected String OWSName;
    protected String OWSPass;
    public String email, firstName, lastName, phone, pwd, country;

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


    @Override
    protected void login()  {
        //Login not required for DAP Registration Test Cases
    }


    public void funcDAPRegistration() throws Exception {
        dapRegistration = myfactor.newInstance(DAPRegistration.class);
        cp = myfactor.newInstance(CPRegisterGold.class);


        firstName = "autoDAP" + GlobalMethods.getRandomString(10);
        lastName = "TestCRM";
        email = ("autoDAP"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        phone = "0000"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();
        country = "Lithuania";

        dapRegistration.registerNewDAP((String)data[0][6], firstName, lastName, email, phone, pwd, country);
        registerDAP_KYC(GlobalProperties.PLATFORM.MT5, country, false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD, cp);


        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsLogin = myfactor.newInstance(OWSLogin.class, OWSURL);
        owsLogin.login(OWSName,OWSPass);
        String clientRegulator = owsDashboard.getRegulator("","",email);

        //France = V2
        Assert.assertTrue(clientRegulator.equalsIgnoreCase("vfsc2"));
        String dapSales = owsDashboard.getSalesAssignedFlow("","","",email);
        String cpaAllocationSales = owsDashboard.getCPAAllocationSalesFlow(country);

        //Sales should be Vantage Support if no rule is found for the country, else should be same as CPA Allocation Sales
        if(cpaAllocationSales.equals("")){
            Assert.assertTrue(dapSales.equalsIgnoreCase("vantage support"));
        } else {
            Assert.assertTrue(dapSales.equalsIgnoreCase(cpaAllocationSales));
        }
    }

    @Test(priority = 0)
    public void testDAPRegistration_Sumsub() throws Exception {
        dapRegistration = myfactor.newInstance(DAPRegistration.class);
        cp = myfactor.newInstance(CPRegisterGold.class);


        firstName = "autoDAP" + GlobalMethods.getRandomString(10);
        lastName = "TestCRM";
        email = ("autoDAP"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        phone = "0000"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();

        dapRegistration.registerNewDAP((String)data[0][6], firstName, lastName, email, phone, pwd, "Italy");
        registerDAP_KYC_Sumsub(GlobalProperties.PLATFORM.MT5, "Italy", false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD, cp);

        //TODO - TO TEST, UAT CURRENTLY HAVING ISSUE WITH SUMSUB
    }


    @Test(priority = 0)
    public void testDAPPhoneLogin() {
        dapLogin = myfactor.newInstance(DAPLogin.class, (String)data[0][3]);
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapLogin.login_Phone("+33", "00004218993213", (String)data[0][2]);
        dapDashboard.dapLogout();
    }

    public void registerDAP_KYC(GlobalProperties.PLATFORM platform, String country, boolean check,
                                                                                GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency, CPRegisterGold cp) throws Exception {


//        country = getCountry(country);

        if(TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString())) {

            // Level 1 - Personal Details Verification
            Assert.assertTrue(dapRegistration.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            dapRegistration.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;
        }
        else
        {

            // Level 1 - Personal Details Verification
//            Assert.assertTrue(dapRegistration.goToPersonalDetailPage(), "Go to Personal Details page failed!");
//            dapRegistration.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

            driver.switchTo().defaultContent();
            dapRegistration.sendEmailCode("999999");
            dapRegistration.sendPhoneCode("123456");
            dapRegistration.fillPersonalDetails(firstName, lastName, phone);
            Assert.assertTrue(dapRegistration.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed!");

            driver.findElement(By.xpath("//div[@class='result-btns']")).click();


            //Level 2 & 3 - POI & POA
            boolean bIsUseSumsub = cp.checkSumsubExists();
            Assert.assertFalse(bIsUseSumsub);
            LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

            dapRegistration.waitLoadingIdentityVerificationContent();

            //POI without Sumsub
            dapRegistration.fillIDPage();

            //POA without Sumsub
            dapRegistration.fillAddressDetails();
            dapRegistration.closeProfileVerificationDialog_withoutExit();

        }

    }

    public void registerDAP_KYC_Sumsub(GlobalProperties.PLATFORM platform, String country, boolean check,
                                GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency, CPRegisterGold cp) throws Exception {


//        country = getCountry(country);

        if(TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString())) {

            // Level 1 - Personal Details Verification
            Assert.assertTrue(dapRegistration.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            dapRegistration.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;
        }
        else
        {

            // Level 1 - Personal Details Verification
//            Assert.assertTrue(dapRegistration.goToPersonalDetailPage(), "Go to Personal Details page failed!");
//            dapRegistration.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

            driver.switchTo().defaultContent();
            dapRegistration.sendEmailCode("999999");
            dapRegistration.sendPhoneCode("123456");
            dapRegistration.fillPersonalDetails(firstName, lastName, phone);
            Assert.assertTrue(dapRegistration.goToPersonalDetailsSummaryPage(), "Go to Personal Details Summary page failed!");

            driver.findElement(By.xpath("//div[@class='result-btns']")).click();


            //Level 2 & 3 - POI & POA
//            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
            boolean bIsUseSumsub = cp.checkSumsubExists();
            Assert.assertTrue(bIsUseSumsub);
            LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

            dapRegistration.waitLoadingIdentityVerificationContent();

            //POI with Sumsub
            dapRegistration.fillIDPage_withSumsub(country);

            //POA with Sumsub
            dapRegistration.fillAddressDetails_withSumsub();
            dapRegistration.closeProfileVerificationDialog();
        }

    }

}
