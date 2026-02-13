package newcrm.testcases.alpharegression;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import newcrm.testcases.cptestcases.AccountManagementTestCases;

import static org.testng.Assert.assertNotNull;

public class AlphaMobileViewTestCases extends BaseTestCase {
    AccountManagementTestCases addAdditionalAccount = new AccountManagementTestCases();
    DepositTestCases deposit = new DepositTestCases();
    private String openAPI;
    public Object data[][];

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
        data = TestDataProvider.getAlphaRegUsersData(brand, server);
        assertNotNull(data);
        openAPI = (String) data[0][5];
        GlobalProperties.isWeb = false;
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(priority = 2)
    public void depositwithCallBack() {
        try {
            if("vfx".equalsIgnoreCase(Brand) | "au".equalsIgnoreCase(Brand))
            {
                deposit.testNetellerPayNew();
            }
            else {
                deposit.testSkrillPayWithCallBack(openAPI);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test(priority = 2)
    public void testOpenAdditionalMT4LiveAccount() {
        addAdditionalAccount.data = data;
        addAdditionalAccount.funcAddAcc(PLATFORM.MT4, ACCOUNTTYPE.STANDARD_STP, CURRENCY.USD);
    }

}
