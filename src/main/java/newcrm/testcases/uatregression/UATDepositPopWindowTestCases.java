package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATDepositPopWindowTestCases extends DepositTestCases {

	private Object[][] adminData;

	@Override
	public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
				              ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}
	
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATPopUsersData(brand, server);
		Brand = brand;
		assertNotNull(data);
		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],
				(String)data[0][1],
				(String)data[0][2],"","","","True",context);
	}

	//测试渠道最低入金弹框
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_MIN_AMOUNT_POPUP_WINDOW)
	public void depositMinimumAmountPopupWindow() {
		if(Brand.equalsIgnoreCase("mo")||
				Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("um")){
            try {
                testMinDepositAmountPopupWindow();
            } catch (Exception e) {
				throw new RuntimeException(e);
            }
        }else {
			throw new SkipException("Skipping this test intentionally.");
		}
	}
	}

