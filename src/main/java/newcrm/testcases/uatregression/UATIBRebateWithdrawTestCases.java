package newcrm.testcases.uatregression;

import newcrm.cpapi.CPAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibtestcases.IBRebateWithdrawTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;

import static org.testng.Assert.assertNotNull;

public class UATIBRebateWithdrawTestCases extends IBRebateWithdrawTestcases {

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

        new CPAPIWithdraw((String)data[0][6],(String)data[0][9],(String)data[0][2]);
        launchBrowserIB("uat","true", brand, (String)data[0][0], (String)data[0][6], (String)data[0][9], (String)data[0][10],"","","","True", context);
    }

    @Test(priority = 2, description = testCaseDescUtils.IBWITHDRAW_ST,groups={"IB_Withdraw"})
    public void testIBRebateWithdraw() throws Exception {
        // MethodTracker for 多层API call
        if (BRAND.VJP.toString().equalsIgnoreCase(Brand) || BRAND.UM.toString().equalsIgnoreCase(Brand) || BRAND.VT.toString().equalsIgnoreCase(Brand)) {
            MethodTracker.trackMethodExecution(this, "testCryptoCRYPTOERCNew", true, null);
        }
        else if (BRAND.STAR.toString().equalsIgnoreCase(Brand) || BRAND.PUG.toString().equalsIgnoreCase(Brand) || BRAND.VFX.toString().equalsIgnoreCase(Brand)) {
            MethodTracker.trackMethodExecution(this, "testSticPayWithdrawNew", true, null);
        }
        else { // MO
            MethodTracker.trackMethodExecution(this, "testInternationalBankNewAccountWitdrawNew", true, null);
        }

        MethodTracker.checkResultFail();
    }

}
