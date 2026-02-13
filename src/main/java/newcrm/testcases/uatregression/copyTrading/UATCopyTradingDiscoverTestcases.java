package newcrm.testcases.uatregression.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.copyTrading.CopyTradingTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATCopyTradingDiscoverTestcases extends CopyTradingTestCases {
    private String strategyAccount;
    private String signalProv;
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
        Object data[][] = UATTestDataProvider.getUATCopyTradingPaymentUsersData(brand, server);
        strategyAccount = (String) data[0][3];
        signalProv = (String) data[0][10];
        assertNotNull(data);
        launchBrowser("uat","false",brand,(String)data[0][0],(String)data[0][4],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(description = testCaseDescUtils.CPCOPYTRADING_DISCOVER, groups = {"CP_Discover_MTS"})
    public void discoverCopyTradingTest() {
            testDiscoverCopyTrading(strategyAccount,signalProv);
    }
    @Test(description = testCaseDescUtils.CPCOPYTRADING_DISCOVER_DETAIL, groups = {"CP_Discover_MTS"})
    public void discoverDetailCopyTradingTest() {
        testDiscoverDetailCopyTrading(strategyAccount,signalProv);
    }
}
