package newcrm.testcases.alpharegression.copyTrading;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.*;

import static org.testng.Assert.assertNotNull;

public class AlphaCopyTradingWithdrawTestCases extends WithdrawTestCases {
    String brand;
    Object data[][];
    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        this.brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = TestDataProvider.getAlphaCopyTradingPaymentUsersData(this.brand, server);
        assertNotNull(data);

        adminPaymentAPI = new AdminAPIPayment((String) data[0][5], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][8],(String)data[0][9],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));;
        adminPaymentAPI.apiDisableLoginWithdrawal2FA();

        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][4],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }
    @AfterClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void teardown(String brand,String server, ITestContext context) {
        data = TestDataProvider.getAlphaCopyTradingPaymentUsersData(this.brand, server);;
        assertNotNull(data);

        PCSAPIWithdraw pcswdapi = new PCSAPIWithdraw((String) data[0][4],(String) data[0][1],(String) data[0][2]);
        pcswdapi.cancelSubmittedWithdrawals();
    }

    @Test(priority = 2, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_ETH,groups = {"CP_Withdraw_MTS"})
    public void withdrawCopyTradingTest() {
        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.UM.toString()))
        {
            testInternationalBankWithdrawNew();
        }
        else {
            testCryptoCRYPTOETHNewforMTS();
        }
    }
}
