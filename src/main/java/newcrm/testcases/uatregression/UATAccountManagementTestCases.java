package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.AccountManagementTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class UATAccountManagementTestCases extends AccountManagementTestCases {

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
		data = UATTestDataProvider.getUATAccountForgotPwdUsersData(brand, server);
		assertNotNull(data);
		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
	}

	@Test(description = testCaseDescUtils.CPACC_MT4_LIVE_ACC_INFO_CHECK, groups = {"CP_Live_Acc_Info_Check"})
	public void testMT4LiveAccountInfoCheck() throws Exception {
		liveAccountInfoCheck(GlobalProperties.PLATFORM.MT4);
	}

	@Test(description = testCaseDescUtils.CPACC_MT5_LIVE_ACC_INFO_CHECK, groups = {"CP_Live_Acc_Info_Check"})
	public void testMT5LiveAccountInfoCheck() throws Exception {
		liveAccountInfoCheck(GlobalProperties.PLATFORM.MT5);
	}

	@Test(description = testCaseDescUtils.CPACC_UPDATE_MT4_LIVE_ACC_NICKNAME, groups = {"CP_Live_Acc_Chg_Nickname"})
	public void testChangeMT4LiveAccountNickname() {
		List<String> excludeBrands = Arrays.asList(GlobalProperties.BRAND.VT.toString(), GlobalProperties.BRAND.MO.toString(), GlobalProperties.BRAND.STAR.toString());
		if (excludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
			throw new SkipException("Skipping this test intentionally.");
		}

		changeLiveAccountNickname(GlobalProperties.PLATFORM.MT4);
	}

	@Test(description = testCaseDescUtils.CPACC_UPDATE_MT5_LIVE_ACC_NICKNAME, groups = {"CP_Live_Acc_Chg_Nickname"})
	public void testChangeMT5LiveAccountNickname() {
		List<String> excludeBrands = Arrays.asList(GlobalProperties.BRAND.VT.toString(), GlobalProperties.BRAND.MO.toString(), GlobalProperties.BRAND.STAR.toString());
		if (excludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
			throw new SkipException("Skipping this test intentionally.");
		}

		changeLiveAccountNickname(GlobalProperties.PLATFORM.MT5);
	}

	@Test(description = testCaseDescUtils.CPACC_MT4_LIVE_ACC_SWITCH_DISPLAY_MODE_CHECK, groups = {"CP_Live_Acc_Switch_Display_Mode_Check"})
	public void testMT4LiveAccountSwitchDisplayModeCheck() throws Exception {
		List<String> excludeBrands = Arrays.asList(GlobalProperties.BRAND.STAR.toString());
		if (excludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
			throw new SkipException("Skipping this test intentionally.");
		}

		switchAccountDisplayModeCheck(GlobalProperties.PLATFORM.MT4);
	}

	@Test(description = testCaseDescUtils.CPACC_MT5_LIVE_ACC_SWITCH_DISPLAY_MODE_CHECK, groups = {"CP_Live_Acc_Switch_Display_Mode_Check"})
	public void testMT5LiveAccountSwitchDisplayModeCheck() throws Exception {
		List<String> excludeBrands = Arrays.asList(GlobalProperties.BRAND.STAR.toString());
		if (excludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
			throw new SkipException("Skipping this test intentionally.");
		}

		switchAccountDisplayModeCheck(GlobalProperties.PLATFORM.MT5);
	}

	@Test(description = testCaseDescUtils.CPACC_MT4_LIVE_ACC_REMOVE_CREDIT, groups = {"CP_Live_Acc_Remove_Credit"})
	public void testMT4LiveAccountRemoveCredit() throws Exception {
		List<String> excludeBrands = Arrays.asList(GlobalProperties.BRAND.VT.toString());
		if (excludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
			throw new SkipException("Skipping this test intentionally.");
		}

		liveAccountRemoveCredit(GlobalProperties.PLATFORM.MT4);
	}

	@Test(description = testCaseDescUtils.CPACC_MT5_LIVE_ACC_REMOVE_CREDIT, groups = {"CP_Live_Acc_Remove_Credit"})
	public void testMT5LiveAccountRemoveCredit() throws Exception {
		List<String> excludeBrands = Arrays.asList(GlobalProperties.BRAND.VT.toString());
		if (excludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
			throw new SkipException("Skipping this test intentionally.");
		}

		liveAccountRemoveCredit(GlobalProperties.PLATFORM.MT5);
	}

}
