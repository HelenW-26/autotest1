package newcrm.testcases.uatregression.account;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.account.AccountLoginTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATPhoneLoginTestCases extends AccountLoginTestCases {

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
        data = UATTestDataProvider.getUATAccountForgotPwdUsersData(brand, server);
        assertNotNull(data);
//        adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String) data[0][0]), (String) data[0][7], (String) data[0][8], GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

        launchBrowser("uat", "true", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], "", "", "", "True", context);
    }

    @Test(description = testCaseDescUtils.CPACCOUNT_PHONE_LOGIN_FAIL, groups= {"CP_Phone_Login"})
    public void testAccountPhoneLoginFail() throws Exception {
        accountPhoneLoginFail();
    }

    @Test(description = testCaseDescUtils.CPACCOUNT_PHONE_LOGIN_SUCCESS, groups= {"CP_Phone_Login"})
    public void testAccountPhoneLoginSuccess() throws Exception {
        accountPhoneLoginSuccess();
    }

}
