package newcrm.testcases.ibtestcases.daptestcases;


import adminBase.Login;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.OWSLogin;
import newcrm.business.businessbase.dapbase.DAPClientList;
import newcrm.business.businessbase.dapbase.DAPDashboard;
import newcrm.business.businessbase.owsbase.OWSCommissionList;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.testcases.DAPBaseTestCaseNew;
import newcrm.testcases.OWSBaseTestCaseNew;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

public class DAPOwsCpaTestCases extends OWSBaseTestCaseNew {

    public Object data[][];
    protected Login adminlogin;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;
    protected DAPDashboard dapDashboard;
    protected DAPClientList dapClientList;


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
    public void testOWSClientManagement() {
        owsDashboard = myfactor.newInstance(OWSDashboard.class);
        owsDashboard.verifyCPASearchTable("",(String)data[0][12],"",(String)data[0][1]);
    }







}
