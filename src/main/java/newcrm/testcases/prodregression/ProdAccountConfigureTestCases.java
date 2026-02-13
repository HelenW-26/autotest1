package newcrm.testcases.prodregression;

import newcrm.global.GlobalProperties;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.AccountConfigureTestCases;

public class ProdAccountConfigureTestCases extends AccountConfigureTestCases {

	@Override
	public void beforMethod(String TestEnv, String headless, String Brand, String Regulator, String TraderURL,
			String TraderName, String TraderPass, String AdminURL, String AdminName, String AdminPass, String Debug,@Optional("")String server,
			ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 调用account configure中修改leverage方法
	 * @param TestEnv
	 * @param headless
	 * @param Brand
	 * @param Regulator
	 * @param TraderURL
	 * @param TraderName
	 * @param TraderPass
	 * @param AdminURL
	 * @param AdminName
	 * @param AdminPass
	 * @param Debug
	 * @param context
	 */
	@Test(dataProvider="leverageUserProvider",dataProviderClass=TestDataProvider.class)
	public void testChangeLeverage(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
				              ITestContext context) throws Exception {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
		testChangeMT4AccountLeverage();
		//testChangeMT5AccountLeverage();
	}
	
	@Override
	@AfterMethod
	public void QuitDriver() {
		driver.quit();
	}

	@Test(description = testCaseDescUtils.CPACC_UPDATE_LEVERAGE_MT4, groups = {"CP_ChangeLeverage"})
	public void testChangeMT4AccountLeverage() throws Exception {
		testChangeAccountLeverage(GlobalProperties.PLATFORM.MT4);
	}

	@Test(description = testCaseDescUtils.CPACC_UPDATE_LEVERAGE_MT5, groups = {"CP_ChangeLeverage"})
	public void testChangeMT5AccountLeverage() throws Exception {
		testChangeAccountLeverage(GlobalProperties.PLATFORM.MT5);
	}

}
