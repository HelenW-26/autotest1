package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPI;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.TransferTestCase;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertNotNull;

public class UATTrasferTestcases extends TransferTestCase {
	private AdminAPI admin;
	String optCode="987654";
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
		Object data[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
		assertNotNull(data);

		AdminAPIUserAccount adminUserAcctAPI = new AdminAPIUserAccount((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
		adminUserAcctAPI.apiEnableAutoAcctTransferAudit();
		//check admin transfer status
		/*try
		{
		launchAdminBrowser("true", context);
		//login admin portal
		Login login = new Login(driver);
		admin = new AdminAPI((String)data[0][4], GlobalProperties.REGULATOR.valueOf(((String)data[0][0]).toUpperCase()),(String)data[0][7],(String)data[0][8], GlobalProperties.BRAND.valueOf(GlobalMethods.getBrand().toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
		login.AdminLogIn((String)data[0][4], (String)data[0][7], (String)data[0][8], (String)data[0][0],optCode,GlobalProperties.ENV.ALPHA,GlobalProperties.BRAND.valueOf(brand.toUpperCase()));

		TaskManagement tm = new TaskManagement(driver,GlobalMethods.getBrand());
		System.out.println("brand:" + GlobalMethods.getBrand());

		tm.openAutoTransfer((String)data[0][4]);
		driver.quit();
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("Check admin transfer status failed");
		}*/
		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
	}

}
