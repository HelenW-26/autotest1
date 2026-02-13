package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATCCWithdrawTestCases extends WithdrawTestCases {
	@Override
	public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
							@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
							@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
							ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}
	
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATCreditInUsersData(brand, server);
		setData(data);
		assertNotNull(data);

		adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));;
		adminPaymentAPI.apiDisableLoginWithdrawal2FA();

		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);


	}


	@Test(priority = 2, description = testCaseDescUtils.CPUI_CREDIT_CARD_WITHDRAWCHECK)
	public void testWithdrawCreditCardInfoCheck() {

        try {
            withdrawCreditCardInfoCheck();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
