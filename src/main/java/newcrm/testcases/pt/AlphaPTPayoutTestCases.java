package newcrm.testcases.pt;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.PTPayoutTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class AlphaPTPayoutTestCases extends PTPayoutTestCases {
    private String openAPI;

    @Override
    public void beforMethod(@Optional("alpha") String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("") String TraderURL, @Optional("") String TraderName, @Optional("") String TraderPass,
                            @Optional("") String AdminURL, @Optional("") String AdminName, @Optional("") String AdminPass, @Optional("True") String Debug, @Optional("") String server,
                            ITestContext context) {
        launchBrowser(TestEnv, headless, Brand, Regulator, TraderURL, TraderName, TraderPass, AdminURL, AdminName, AdminPass, Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value = {"Brand", "Server"})
    public void initiEnv(String brand, String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = TestDataProvider.getAlphaRegUsersData(brand, server);
        assertNotNull(data);
        openAPI = (String) data[0][5];
        launchBrowser("alpha", "false", brand, (String) data[0][0], (String) data[0][3], (String) data[0][1], (String) data[0][2], (String) data[0][4], "", "", "True", context);
    }

    @Test
    @Parameters(value = {"Country"})
    public void testPayout(@Optional("") String Country) {
        //ptPayoutTestCases("PTPUTEST2312110002199");
    }
}

