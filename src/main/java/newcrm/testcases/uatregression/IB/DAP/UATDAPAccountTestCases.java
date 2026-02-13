package newcrm.testcases.uatregression.IB.DAP;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibtestcases.IBAccountTestCases;
import newcrm.testcases.ibtestcases.daptestcases.DAPAccountTestCases;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static newcrm.utils.testCaseDescUtils.DAP_CPA_Login_Logout;
import static newcrm.utils.testCaseDescUtils.DAP_CPA_Registration;

public class UATDAPAccountTestCases extends DAPAccountTestCases {

    private String brand;

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        launchBrowserDAP( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);

        data= UATTestDataProvider.getUATDAPAccountUsersData(brand, server);
        this.brand = GlobalMethods.setEnvValues(brand);
        UserConfig config = new UserConfig();
        BaseTestCaseNew.OWSURL = (String)data[0][7];
        OWSName = (String)data[0][10];
        OWSPass = (String)data[0][11];

        if(brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())) {
            launchBrowserDAP("uat","true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], (String)data[0][4], (String)data[0][8], (String)data[0][9], "True", context);
        }
    }

    @Test(priority = 0, description = DAP_CPA_Login_Logout, groups = {"DAP_ST"})
    public void testDAPProfilePanel_LoginLogout() throws Exception {
        if (brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())) {
            funcDAPProfilePanel_LoginLogout();
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

}