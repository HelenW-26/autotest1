package newcrm.testcases.alpharegression.download;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.download.DownloadTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class AlphaDownloadTestCases extends DownloadTestCases {

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
        data = TestDataProvider.getAlphaAccountForgotPwdUsersData(brand, server);
        assertNotNull(data);

        launchBrowser("alpha","true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], "", "", "", "True", context);
    }

    @Test(description = testCaseDescUtils.CPDOWNLOAD_INFO_CHECK, groups= {"CP_Download"})
    public void testDownloadInfoCheck() throws Exception {
        downloadInfoCheck();
    }

}
