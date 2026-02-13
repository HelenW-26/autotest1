package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibtestcases.IBAccountManagementTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATIBRebateTransferTestCases extends IBAccountManagementTestCases {

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) throws Exception {
        if(!"".equals(server)) {
            serverName = server;
        }

        launchBrowserIB( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
        assertNotNull(data);

        launchBrowserIB("uat","true", brand, (String)data[0][0], (String)data[0][6], (String)data[0][9], (String)data[0][10],"","","","True", context);
    }

    @Test(priority = 2, description = testCaseDescUtils.IBFUNDTRANS,groups={"IB_Transfer"})
    public void testIBRebateTransfer() throws Exception {
        this.funcTransfer();
    }

}
