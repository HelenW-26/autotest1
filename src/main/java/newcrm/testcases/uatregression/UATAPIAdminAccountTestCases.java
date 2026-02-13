package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.admintestcases.AdminAccountTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UATAPIAdminAccountTestCases extends AdminAccountTestCases {

	@BeforeTest(alwaysRun = true)
	@Parameters({"Brand", "Server"})
	public void initEnv(String brand, String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);

		Object[][] data = UATTestDataProvider.getUATRegUsersData(brand, server);
		adminAcctAPI = new AdminAPIUserAccount((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIACCOUNT_TRADINGACCT,groups={"API_Admin_Account"})
	public void testAPIAdminTradingAcctPage() {
		try {
			this.apiAdminTradingAcctPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIACCOUNT_REBATEACCT,groups={"API_Admin_Account"})
	public void testAPIAdminRebateAcctPage() {
		try {
			this.apiAdminRebateAcctPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIACCOUNT_USERS,groups={"API_Admin_Account"})
	public void testAPIAdminUserPage() {
		try {
			this.apiUserPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
