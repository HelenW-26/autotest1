package newcrm.testcases.alpharegression.copyTrading;

import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.app.APPRegression;
import newcrm.testcases.cptestcases.copyTrading.CopyTradingTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class AlphaCopyTradingCopierEURTestcases extends CopyTradingTestCases {
    private String signalProvAccount;
    private String copierAccount;
    private String brand;
    private List<String> copyTradingBrands;
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
        Object data[][] = TestDataProvider.getAlphaCopyTradingEURUserData(brand, server);
        copierAccount = (String) data[0][3];
        signalProvAccount = (String) data[0][4];
        assertNotNull(data);

        copyTradingBrands = Arrays.asList("vfx", "pug","vt","vjp","um");

        if (copyTradingBrands.contains(this.brand.toLowerCase())) {
            //Ensure copier account has enough balance
            /*APPRegression appRegression = new APPRegression();
            AdminAPIUserAccount adminAPIUserAccount = new AdminAPIUserAccount((String) data[0][6], GlobalProperties.REGULATOR.valueOf((String) data[0][0]), (String) data[0][7], (String) data[0][8], GlobalProperties.BRAND.valueOf(this.brand), GlobalProperties.ENV.valueOf("ALPHA"));
            appRegression.checkBalanceAndCashAdjustmentForMTS(adminAPIUserAccount, copierAccount);
            appRegression.checkBalanceAndCashAdjustmentForMTS(adminAPIUserAccount, signalProvAccount);*/

            launchBrowser("alpha", "true", this.brand, (String) data[0][0], (String) data[0][5], (String) data[0][1], (String) data[0][2], "", "", "", "True", context);
        }
    }

    @Test(description = testCaseDescUtils.CPCOPYTRADING_EUR_EquivalentMargin, groups = {"CP_CopyTrading_EUR"})
    public void CopierEURCopyTradingTest_EquivalentMargin() {
        if (copyTradingBrands.contains(this.brand.toLowerCase())) {
            testCopyTradingEUR_EquivalentMargin(copierAccount,signalProvAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(description = testCaseDescUtils.CPCOPYTRADING_EUR_FixLots, groups = {"CP_CopyTrading_EUR"})
    public void CopierEURCopyTradingTest_FixLots() {
        if (copyTradingBrands.contains(this.brand.toLowerCase())) {
            testCopyTradingEUR_FixedLots(copierAccount,signalProvAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
    @Test(description = testCaseDescUtils.CPCOPYTRADING_EUR_FixedMultiples, groups = {"CP_CopyTrading_EUR"})
    public void CopierEURCopyTradingTest_FixedMultiples() {
        if (copyTradingBrands.contains(this.brand.toLowerCase())) {
            testCopyTradingEUR_FixedMultiples(copierAccount,signalProvAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
}
