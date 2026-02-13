package newcrm.testcases.alpharegression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.ApiAuditWithdrawTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;

import static org.testng.Assert.assertNotNull;

public class AlphaApiWithdrawTestCases extends ApiAuditWithdrawTestCases {

//    @Override
//    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
//                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
//                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
//                            ITestContext context) {
//
//        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
//    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception{
        brand = GlobalMethods.setEnvValues(brand);
        data = TestDataProvider.getAlphaWithdrawLimitUsersData(brand, server);
        assertNotNull(data);

        try {

            pcswdapi = new PCSAPIWithdraw((String) data[0][3],(String) data[0][1],(String) data[0][2]);
            adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], GlobalProperties.BRAND.valueOf(String.valueOf(GlobalMethods.getBrand())), GlobalProperties.ENV.valueOf("ALPHA"));
        }catch (Exception e){

        }
//        launchBrowser("alpha","false",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);

    }

    @Test(priority = 2, description = "use same channel to deposit and withdrawal")
    @Parameters(value= {"Brand"})
    public void sameChannelDepositWithdraw(String brand){
        try {
            switch(brand.toLowerCase()) {
                case "vjp":
                    MethodTracker.trackMethodExecution(this, "apiVJPInOutWithdrawal", true, null);
                    break;
                default:
                    MethodTracker.trackMethodExecution(this, "apiInOutWithdrawal", true, null);
                    break;
            }
        }catch (Exception e){

        }

    }
}
