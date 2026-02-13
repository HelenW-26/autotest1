package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;
import utils.LogUtils;

import static org.testng.Assert.assertNotNull;

public class UATDepositBridgePayCallBackTestCases extends DepositTestCases {

	private String openAPI;
	private Object[][] adminData;
	private WebDriver driver;

	@Override
	public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
				              ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
		WebDriver tempDriver = (WebDriver) context.getAttribute("driver");
		if (tempDriver != null) {
			this.driver = tempDriver;
			context.setAttribute("driver", this.driver);
			LogUtils.info("beforMethod 中 driver 初始化成功");
		} else {
			LogUtils.error("beforMethod 中 driver 初始化失败", null);
		}
	}
	
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATCCallBackUsersData(brand, server);
//		adminData = TestDataProvider.getAlphaRegUsersData(brand, server);

		assertNotNull(data);
		openAPI = (String) data[0][5];
		adminData = UATTestDataProvider.getUATCCallBackUsersData(brand, server);
		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8], GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
		WebDriver tempDriver = (WebDriver) context.getAttribute("driver");
		if (tempDriver != null) {
			this.driver = tempDriver;
			context.setAttribute("driver", this.driver);
			LogUtils.info("beforMethod 中 driver 初始化成功");
		} else {
			LogUtils.error("beforMethod 中 driver 初始化失败", null);
		}
	}

	@Test(description = testCaseDescUtils.CPDEPOSIT_ST,groups = {"CP_Deposit_BridgePayCallBack"})
	public void bridgeCCDepositwithCallBack() {

		try {
			MethodTracker.trackMethodExecution(this, "testBridgePayCallBack", true, null, driver,openAPI);
			MethodTracker.checkResultFail();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	}

