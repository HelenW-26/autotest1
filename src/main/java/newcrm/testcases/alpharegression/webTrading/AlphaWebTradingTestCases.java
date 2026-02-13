package newcrm.testcases.alpharegression.webTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.app.APPRegression;
import newcrm.testcases.cptestcases.webTrading.WebTradingTestCases;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

import static newcrm.utils.testCaseDescUtils.*;
import static org.testng.Assert.assertNotNull;

public class AlphaWebTradingTestCases extends WebTradingTestCases {
    private String webTradingAccount;
    private String brand;
    private List<String> webTradingBrands;

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
        Object data[][] = TestDataProvider.getAlphaWebTradingUserData(brand, server);
        webTradingAccount = (String) data[0][3];
        assertNotNull(data);

        webTradingBrands = Arrays.asList(GlobalProperties.BRAND.VFX.toString(),GlobalProperties.BRAND.VJP.toString(),GlobalProperties.BRAND.STAR.toString(),GlobalProperties.BRAND.PUG.toString());
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            //Ensure web trading account has enough balance
            APPRegression appRegression = new APPRegression();
            /*AdminAPIUserAccount adminAPIUserAccount = new AdminAPIUserAccount((String) data[0][5], GlobalProperties.REGULATOR.valueOf((String) data[0][0]), (String) data[0][6], (String) data[0][7], GlobalProperties.BRAND.valueOf(this.brand), GlobalProperties.ENV.valueOf("ALPHA"));
            appRegression.checkBalanceAndCashAdjustment(adminAPIUserAccount, webTradingAccount);*/

            launchBrowser("alpha", "true", this.brand, (String) data[0][0], (String) data[0][4], (String) data[0][1], (String) data[0][2], "", "", "", "True", context);
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_PLACEORDER,groups = {"CP_WebTrading"})
    public void testOpenUpdateClosePosition_Buy() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testOpenUpdateCloseBuyPosition(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = CPWEBTRADING_PLACEORDER,groups = {"CP_WebTrading"})
    public void testOpenUpdateClosePosition_Sell() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testOpenUpdateCloseSellPosition(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = CPWEBTRADING_PENDINGORDER_STOPLIMIT,groups = {"CP_WebTrading"})
    public void testPendingPosition_BuyStopLimit() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testPendingPosition_BuyStopLimit(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = CPWEBTRADING_PENDINGORDER_STOPLIMIT,groups = {"CP_WebTrading"})
    public void testPendingPosition_SellStopLimit() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testPendingPosition_SellStopLimit(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = CPWEBTRADING_PENDINGORDER_STOP,groups = {"CP_WebTrading"})
    public void testPendingPosition_BuyStop() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testPendingPosition_BuyStop(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_PENDINGORDER_STOP,groups = {"CP_WebTrading"})
    public void testPendingPosition_SellStop() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testPendingPosition_SellStop(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_PENDINGORDER_LIMIT,groups = {"CP_WebTrading"})
    public void testPendingPosition_BuyLimit() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testPendingPosition_BuyLimit(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_PENDINGORDER_LIMIT,groups = {"CP_WebTrading"})
    public void testPendingPosition_SellLimit() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            testPendingPosition_SellLimit(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(priority = 2, description = CPWEBTRADING_POSITION_CLOSE_ALL,groups = {"CP_WebTrading"})
    public void testPositions_closeAll() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            this.testPositionCloseAll(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_POSITION_REVERSE,groups = {"CP_WebTrading"})
    public void testPositions_reverse() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            this.testPositions_reverse(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_POSITION_CLOSE_BY,groups = {"CP_WebTrading"})
    public void testPositions_closeBy() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            this.testPositions_closeBy(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 2, description = CPWEBTRADING_POSITION_WSS,groups = {"CP_WebTrading"})
    public void test_chartInfo() {
        if (webTradingBrands.contains(this.brand.toUpperCase())) {
            this.test_chartInfoInterval(webTradingAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
}
