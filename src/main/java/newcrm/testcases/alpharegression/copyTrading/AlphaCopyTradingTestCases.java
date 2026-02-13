package newcrm.testcases.alpharegression.copyTrading;

import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.testcases.app.APPRegression;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
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

public class AlphaCopyTradingTestCases extends CopyTradingTestCases {
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
        Object data[][] = TestDataProvider.getAlphaCopyTradingUserData(brand, server);
        copierAccount = (String) data[0][3];
        signalProvAccount = (String) data[0][4];
        assertNotNull(data);

        copyTradingBrands = Arrays.asList("vfx","pug","vjp","vt","um","mo");

        if (copyTradingBrands.contains(this.brand.toLowerCase())) {
            launchBrowser("alpha", "true", this.brand, (String) data[0][0], (String) data[0][5], (String) data[0][1], (String) data[0][2], "", "", "", "True", context);
        }
    }

    @Test(priority = 2, description = testCaseDescUtils.CPCOPYTRADING,groups = {"CP_CopyTrading"})
    public void testCopyTrading() {
        if (copyTradingBrands.contains(this.brand.toLowerCase())) {
            testCopyTrading(copierAccount,signalProvAccount);
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }

    }

}
