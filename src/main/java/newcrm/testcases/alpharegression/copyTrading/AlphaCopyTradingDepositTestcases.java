package newcrm.testcases.alpharegression.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import static org.testng.Assert.assertNotNull;

public class AlphaCopyTradingDepositTestcases extends DepositTestCases {
    private String openAPI;
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
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = TestDataProvider.getAlphaCopyTradingPaymentUsersData(brand, server);
        assertNotNull(data);
        openAPI = (String) data[0][6];
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][4],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_TRC20, groups = {"CP_Deposit_MTS"})
    public void depositCopyTradingTest() {
        try {
            if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.UM.toString()))
            {
                testInterBankTransPayNew();
            }
            else {
                testCryptoTRCNew();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }
}
