package newcrm.testcases.alpharegression;

import static org.testng.Assert.assertNotNull;

import adminBase.Login;
import adminBase.TaskManagement;
import newcrm.adminapi.AdminAPI;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.utils.MyWebDriverListener;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.AccountConfigureTestCases;
import org.testng.annotations.Test;
import vantagecrm.Utils;

import java.time.Duration;

public class AlphaAccountConfigureTestCases extends AccountConfigureTestCases {

	private AdminAPI admin;
	String optCode="987654";
	
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
		Object data[][] = TestDataProvider.getAlphaAccountForgotPwdUsersData(brand, server);
		assertNotNull(data);

		AdminAPIUserAccount adminUserAcctAPI = new AdminAPIUserAccount((String) data[0][4], REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8], BRAND.valueOf(brand.toUpperCase()), ENV.valueOf("ALPHA"));
		adminUserAcctAPI.apiEnableAutoLeverageAudit();
		//check admin leverage auto audit status
		/*try
		{
			launchAdminBrowser("true", context);
			//login admin portal
			Login login = new Login(driver);
			admin = new AdminAPI((String)data[0][4], GlobalProperties.REGULATOR.valueOf(((String)data[0][0]).toUpperCase()),(String)data[0][7],(String)data[0][8], GlobalProperties.BRAND.valueOf(GlobalMethods.getBrand().toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
			login.AdminLogIn((String)data[0][4], (String)data[0][7], (String)data[0][8], (String)data[0][0],optCode,GlobalProperties.ENV.ALPHA,GlobalProperties.BRAND.valueOf(brand.toUpperCase()));

			TaskManagement tm = new TaskManagement(driver,GlobalMethods.getBrand());
			System.out.println("brand:" + GlobalMethods.getBrand());

			tm.openLeverageAutoAudit((String)data[0][4]);
			driver.quit();
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("Check admin transfer status failed");
		}*/
		//launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],
		launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
	}

	@Test(description = testCaseDescUtils.CPACC_UPDATE_LEVERAGE_MT4, groups = {"CP_ChangeLeverage"})
	public void testChangeMT4AccountLeverage() throws Exception {
		testChangeAccountLeverage(PLATFORM.MT4);
	}

	@Test(description = testCaseDescUtils.CPACC_UPDATE_LEVERAGE_MT5, groups = {"CP_ChangeLeverage"})
	public void testChangeMT5AccountLeverage() throws Exception {
		testChangeAccountLeverage(PLATFORM.MT5);
	}

}
