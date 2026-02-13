package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.app.APPRegression;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATAPPTestCases extends APPRegression {

	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) {
		GlobalProperties.env = "UAT";

		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);
		assertNotNull(data);

		adminData = UATTestDataProvider.getUATRegUsersData(brand, server);
	}

	
	@Test(priority = 0, description = testCaseDescUtils.APPAPIACC_REGISTER_MT4)
	@Parameters(value= {"Brand","Server"})
	public void testAppRegistration(String brand,String server) throws Exception {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);
		Object dataCP[][] = UATTestDataProvider.getUATRegUsersData(brand, server);

		//The brands which already complete registration enhancement use new registration api(V2)(currently all brands done)
		//if(brand.equalsIgnoreCase("star")||brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("vt")||brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("um")||brand.equalsIgnoreCase("vjp"))
		//{
			this.registrationNew((String)data[0][1], (String)data[0][0], brand,server,"Italy", "usd", "MT4","standardSTP", "", "", (String)dataCP[0][7], (String)dataCP[0][8]);
		//}
		/*else
		{
			this.registration((String)data[0][1], (String)data[0][0], brand,server,"Italy", "usd", "MT4","standardSTP", "", "");
		}*/
	}
	
	@Test(priority = 0, description = testCaseDescUtils.APPAPIDEPOSIT_CC)
	@Parameters(value= {"Brand","Server"})
	public void testAppDeposit(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		if(!brand.equalsIgnoreCase("star")) {
			Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);
			GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
			this.deposit((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3],(String)data[0][4],"137");
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIDEPOSIT_CRYPTO_USDT_TRC20)
	@Parameters(value = {"Brand", "Server"})
	public void testAppCryptoDeposit(String brand, String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);
		GlobalMethods.printDebugInfo("userid: " + (String) data[0][2] + " accountNo: " + (String) data[0][3]);
		//146 USDT TRC20 app, 7 for VJP
		String depositType = "146";
		if(brand.equalsIgnoreCase("vjp"))
			depositType = "7";
		this.deposit((String) data[0][1], (String) data[0][0], brand, (String) data[0][2], (String) data[0][3], (String) data[0][4], depositType);
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIWITHDRAW_CRYPTO_USDT_TRC20)
	@Parameters(value= {"Brand","Server"})
	public void testAppCryptoWithdraw(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPWithdrawalUsersData(brand, server);
		Object dataCP[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
		adminAPIUserAccount = new AdminAPIUserAccount((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));

		GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
		this.checkBalanceAndCashAdjustment((String)data[0][3]);
		this.withdraw((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);
		//cancel this withdraw application
		new CPAPIWithdraw((String)dataCP[0][3],(String)data[0][5],(String)data[0][6]);
	}
	
	@Test(priority = 0, description = testCaseDescUtils.APPAPIWITHDRAW_JAPAN_BT)
	@Parameters(value= {"Brand","Server"})
	public void testAppCPSWithdraw(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		//if(!brand.equalsIgnoreCase("um")) {
			Object data[][] = UATTestDataProvider.getUATAPPWithdrawalUsersData(brand, server);
			Object dataCP[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
			adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
			adminAPIUserAccount = new AdminAPIUserAccount((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));

			GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
			this.checkBalanceAndCashAdjustment((String)data[0][3]);
			this.cpsWithdraw((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);
			new CPAPIWithdraw((String)dataCP[0][3],(String)data[0][5],(String)data[0][6]);
		//}
	}
	
	
	@Test(priority = 0, description = testCaseDescUtils.APPAPIWITHDRAW_CC)
	@Parameters(value= {"Brand","Server"})
	public void testAppCCWithdraw(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		//if(!brand.equalsIgnoreCase("um") && !brand.equalsIgnoreCase("vjp") ) {
			Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);
			Object dataCP[][] = UATTestDataProvider.getUATRegUsersData(brand, server);
			adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
			adminAPIUserAccount = new AdminAPIUserAccount((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));

			GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
			this.checkBalanceAndCashAdjustment((String)data[0][3]);
			this.ccWithdraw((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);
			new CPAPIWithdraw((String)dataCP[0][3],(String)data[0][5],(String)data[0][6]);
		//}
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIACC_REGISTER_DEMO_MT4,groups={"API_Appservice"})
	@Parameters(value= {"Brand","Server"})
	public void testAppOpenMT4DemoAccount(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);
		GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
		this.openDemoAccount((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],"USD","MT4");
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIACC_ADDITIONAL_ACC,groups={"API_Appservice"})
	@Parameters(value= {"Brand","Server"})
	public void testAppAddAdditionalAccount(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPAddAccUsersData(brand, server);
		GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
		this.addAdditionalAccount((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);
	}

	@Test(priority = 0)
	@Parameters(value= {"Brand","Server"})
	public void testAppOpenDemoAccountV2(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = UATTestDataProvider.getUATAPPRegUsersData(brand, server);

		//The brands which already complete registration enhancement use new registration api(V2) to create demo(currently vfx,vt,pug)
		if(brand.equalsIgnoreCase("star")||brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("vt")||brand.equalsIgnoreCase("pug"))
		{
			this.openDemoAccountV2((String)data[0][1], (String)data[0][0], brand,server,"Italy", "usd", "MT4","standardSTP", "", "");
		}

	}
}
