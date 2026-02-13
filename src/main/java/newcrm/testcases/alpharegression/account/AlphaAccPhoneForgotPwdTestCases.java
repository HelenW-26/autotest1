package newcrm.testcases.alpharegression.account;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.account.AccountForgotPwdTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class AlphaAccPhoneForgotPwdTestCases extends AccountForgotPwdTestCases {

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

    @Test(description = testCaseDescUtils.CPACCOUNT_PHONE_FORGOT_PWD, groups= {"CP_Phone_Forgot_Pwd"})
    public void testAccountPhoneForgotPwd() throws Exception {
        if (!forgotPwdExcludeBrands.contains(dbBrand.toString())) {
            phoneForgotPwd();
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

}
