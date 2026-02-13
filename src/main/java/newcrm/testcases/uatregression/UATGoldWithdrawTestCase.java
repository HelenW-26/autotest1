package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
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

public class UATGoldWithdrawTestCase extends WithdrawTestCases {

    Object data[][];

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
        Brand = brand;
        data = UATTestDataProvider.getUATGoldWithdrawData(brand, server);
        assertNotNull(data);
        setData( data);

        adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));;
        adminPaymentAPI.apiDisableLoginWithdrawal2FA();

        //new CPAPIWithdraw((String)data[0][3],(String)data[0][1],(String)data[0][2]);
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);

    }


    @Test(priority = 3, description = testCaseDescUtils.CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT_DESC)
    public void testWithdrawDeductedMaxAmount() {
        if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
            withdrawDeductedMaxAmount(Brand);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }

    }
}
