package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static newcrm.utils.testCaseDescUtils.CPDEPOSIT_GOLD_FLOW;
import static org.testng.Assert.assertNotNull;

public class UATGoldDepositTestCases extends DepositTestCases {
    private Object[][] adminData;

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
        Object data[][] = UATTestDataProvider.getUATGoldDepositData(brand, server);
        adminData = UATTestDataProvider.getUATPageUsersData(brand, server);

        assertNotNull(data);
        adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8], GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    //AU golden flow, check max deposit amount after succeed deposit
    @Test(description = CPDEPOSIT_GOLD_FLOW)
    public void depositDeductedMaxAmount(){
        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())){
            testDepositDeductedMaxAmount();
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

}
