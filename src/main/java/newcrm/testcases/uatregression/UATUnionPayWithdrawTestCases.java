package newcrm.testcases.uatregression;

import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LogUtils;

import static org.testng.Assert.assertNotNull;

public class UATUnionPayWithdrawTestCases extends WithdrawTestCases {
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
		LogUtils.info("brand:"+ brand);
		// 添加这一行来设置父类的 Brand 字段
		Brand = brand;
		Object data[][] = UATTestDataProvider.getUATUnionPayUsersData(brand, server);
		assertNotNull(data);
		//Cancel withdrawal if there's any
		new PCSAPIWithdraw( (String)data[0][3],(String)data[0][1],(String)data[0][2]);
		launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],
				(String)data[0][1],
				(String)data[0][2],"","","","True",context);

	}


	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_UNIONPAY_WITHDRAW)
	public void unionPayWithdraw() {
		if(Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("mo")
				||Brand.equalsIgnoreCase("um")){
			testUnionPayWithdrawV1();
		}else {
			throw new SkipException(String.format("%s无UnionPay出金，Skipping this test intentionally.", Brand));		}
	}

}
