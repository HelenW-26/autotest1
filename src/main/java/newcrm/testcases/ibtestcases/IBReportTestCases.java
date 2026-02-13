package newcrm.testcases.ibtestcases;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.business.businessbase.ibbase.report.IBReport;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;


public class IBReportTestCases extends BaseTestCaseNew {


    protected Object data[][];
    protected CPMenu cpMenu;
    protected IBReport ibReport;
    protected IBAccountReport ibAccountReport;
    private Factor myfactor;
    private WebDriver driver;
    protected static CPLogin login;

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
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            driver.navigate().refresh();
//            cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
        }
    }


    @Test(priority = 0, description = testCaseDescUtils.IB_REBATE_REPORT)
    public void testIBRebateReport() throws Exception {
        verifyRebateReport();
    }

    @Test(priority = 0, description = testCaseDescUtils.IB_ACCOUNT_REPORT)
    public void testIBAccountAndClientReport() throws Exception {
        verifyAccountReport();

        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())){
            verifyClientReport();
        }

    }

    public void verifyRebateReport() {
        ibReport = myfactor.newInstance(IBReport.class);
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
//        cpMenu.changeLanguage("English");
        cpMenu.goToMenu((GlobalProperties.CPMenuName.IBREBATEREPORT));
        ibReport.verifyRebateReportPage();
    }

    public void verifyAccountReport() {
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
//        cpMenu.changeLanguage("English");
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBACCOUNTREPORT);
        IBAccountReport ibAccountReport = myfactor.newInstance(IBAccountReport.class);
        ibAccountReport.verifyAccountReportPage();
    }

    public void verifyClientReport() {
//        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBDASHBOARD);
//        cpMenu.changeLanguage("English");
        cpMenu.goToMenu(GlobalProperties.CPMenuName.IBCLIENTREPORT);
        IBAccountReport ibAccountReport = myfactor.newInstance(IBAccountReport.class);
        ibAccountReport.verifyClientReportPage();
    }
}
