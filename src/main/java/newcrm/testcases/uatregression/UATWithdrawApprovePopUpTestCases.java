package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATWithdrawApprovePopUpTestCases extends WithdrawTestCases {
    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = UATTestDataProvider.getUAT2FAPopUpUsersData(brand, server);
        assertNotNull(data);
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],
                (String)data[0][1],
                (String)data[0][2],"","","","True",context);

    }


    @Test(description = testCaseDescUtils.CP2FA_POPUP_CHECK)
    public void withdrawApprovePopupCheck() {
        if(!Brand.equalsIgnoreCase("vjp")||Brand.equalsIgnoreCase("vfx")||Brand.equalsIgnoreCase("au")){
            test2FAWithdrawPopUP();
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }

    }
}
