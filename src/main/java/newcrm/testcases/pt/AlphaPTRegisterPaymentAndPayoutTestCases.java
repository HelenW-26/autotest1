package newcrm.testcases.pt;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.PTRegisterPaymentAndPayoutTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class AlphaPTRegisterPaymentAndPayoutTestCases extends PTRegisterPaymentAndPayoutTestCases {
    private String openAPI;
    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }
    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = TestDataProvider.getAlphaRegUsersData(brand, server);
        assertNotNull(data);
        openAPI = (String) data[0][5];
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],(String)data[0][4],(String)data[0][7], (String)data[0][8],"True",context);
    }
    @Test
    @Parameters(value= {"Country"})
    public void testRegisterPaymentAndPayout(@Optional("")String Country)  {
        System.out.println("brand:" + GlobalMethods.getBrand());
        ptRegisterPaymentAndPayout("", "","PTUzI1NA==", GlobalProperties.PLATFORM.MT4, "Italy", GlobalProperties.ACCOUNTTYPE.valueOf("Basic"),
                GlobalProperties.CURRENCY.EUR, "10000", GlobalProperties.PTPaymentMethod.EwalletPerfectMoney,openAPI,true,GlobalProperties.PTPaymentMethod.CryptoBitCOIN
        );
    }
}
