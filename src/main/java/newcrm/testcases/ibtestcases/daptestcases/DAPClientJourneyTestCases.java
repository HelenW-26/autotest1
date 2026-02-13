package newcrm.testcases.ibtestcases.daptestcases;


import adminBase.Login;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.dapbase.DAPClientList;
import newcrm.business.businessbase.dapbase.DAPDashboard;
import newcrm.business.businessbase.dapbase.DAPDeepLink;
import newcrm.business.businessbase.dapbase.DAPPostbackTracker;
import newcrm.business.businessbase.ibbase.account.IBDemoAccount;
import newcrm.business.businessbase.ibbase.account.IBProgramRegistration;
import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.DAPBaseTestCaseNew;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class DAPClientJourneyTestCases extends DAPBaseTestCaseNew {

    public Object data[][];
    protected Login adminlogin;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;
    protected DAPDashboard dapDashboard;
    protected DAPClientList dapClientList;
    protected DAPPostbackTracker dapPostbackTracker;

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

    @Test(priority = 0)
    public void testDAPClientJourney() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        List<String> clientRegTimeList = dapDashboard.getAllClientJourneyRegTime();
        List<String> clientUIDList = dapDashboard.getAllClientJourneyUID();
        List<String> clientNameList = dapDashboard.getAllClientJourneyName();
        List<String> clientStatusList = dapDashboard.getAllClientJourneyStatus();

        LogUtils.info("Registration Time, UID, Name, Status");

        //Verify Client Journey Status contains registrations, live, qualify (previously created data)
        Assert.assertTrue(clientStatusList.contains("registrations"));
        Integer registrationIndex = clientStatusList.indexOf("registrations");
        LogUtils.info(clientRegTimeList.get(registrationIndex) +", "+ clientUIDList.get(registrationIndex) +", "+ clientNameList.get(registrationIndex) +", "+ clientStatusList.get(registrationIndex));

        Assert.assertTrue(clientStatusList.contains("live"));
        Integer liveIndex = clientStatusList.indexOf("live");
        LogUtils.info(clientRegTimeList.get(liveIndex) +", "+ clientUIDList.get(liveIndex) +", "+ clientNameList.get(liveIndex) +", "+ clientStatusList.get(liveIndex));

        Assert.assertTrue(clientStatusList.contains("qualify"));
        Integer qualifyIndex = clientStatusList.indexOf("qualify");
        LogUtils.info(clientRegTimeList.get(qualifyIndex) +", "+ clientUIDList.get(qualifyIndex) +", "+ clientNameList.get(qualifyIndex) +", "+ clientStatusList.get(qualifyIndex));

    }

    @Test(priority = 0)
    public void testDAPClientList() {
        dapDashboard = myfactor.newInstance(DAPDashboard.class);
        dapClientList = myfactor.newInstance(DAPClientList.class);
        dapDashboard.navigateToClientList();
        dapClientList.verifyClientList();
    }




}
