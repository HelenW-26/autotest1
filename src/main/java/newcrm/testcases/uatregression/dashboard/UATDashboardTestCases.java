package newcrm.testcases.uatregression.dashboard;

import newcrm.global.GlobalMethods;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.dashboard.DashboardTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATDashboardTestCases extends DashboardTestCases {

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception {
        brand = GlobalMethods.setEnvValues(brand);
        data = UATTestDataProvider.getUATAccountForgotPwdUsersData(brand, server);
        assertNotNull(data);

        if (!dashboardExcludeBrands.contains(brand.toUpperCase())) {
            launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
        }
    }

    @Test(description = testCaseDescUtils.CPDASHBOARD_ACC_ASSETS, groups= {"CP_Dashboard_Acc_Check"})
    public void testDashboardAccountCheck() throws Exception {
        dashboardAccountCheck();
    }

    @Test(description = testCaseDescUtils.CPDASHBOARD_TRADE_VIEW, groups= {"CP_Dashboard_TradeView_Check"})
    public void testDashboardTradeViewCheck() throws Exception {
        dashboardTradeViewCheck();
    }

}
