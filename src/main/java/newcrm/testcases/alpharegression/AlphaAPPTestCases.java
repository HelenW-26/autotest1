package newcrm.testcases.alpharegression;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.app.APPRegression;
import newcrm.utils.testCaseDescUtils;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertNotNull;

import newcrm.testcases.cptestcases.WithdrawTestCases;
import org.testng.annotations.Test;

import java.util.Map;

public class AlphaAPPTestCases extends APPRegression {
	PCSAPIWithdraw pcswdapi;
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand","Server"})
	public void initiEnv(String brand,String server, ITestContext context) {
		GlobalProperties.env = "ALPHA";

		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
		assertNotNull(data);

		adminData = TestDataProvider.getAlphaRegUsersData(brand, server);
	}

	
	@Test(priority = 0, description = testCaseDescUtils.APPAPIACC_REGISTER_MT4)
	@Parameters(value= {"Brand","Server"})
	public void testAppRegistration(String brand,String server) throws Exception {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
		Object dataCP[][] = TestDataProvider.getAlphaRegUsersData(brand, server);

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
			Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
			GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
			this.deposit((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3],(String)data[0][4],"137");
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIDEPOSIT_CRYPTO_USDT_TRC20)
	@Parameters(value = {"Brand", "Server"})
	public void testAppCryptoDeposit(String brand, String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
		GlobalMethods.printDebugInfo("userid: " + (String) data[0][2] + " accountNo: " + (String) data[0][3]);
		//146 USDT TRC20 app, 7 for VJP
		String depositType = "146";
		if(brand.equalsIgnoreCase("vjp"))
			depositType = "7";
		this.deposit((String) data[0][1], (String) data[0][0], brand, (String) data[0][2], (String) data[0][3], (String) data[0][4], depositType);
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIWITHDRAW_CRYPTO_USDT_TRC20)
	@Parameters(value= {"Brand","Server"})
	public void testAppCryptoWithdraw(String brand,String server) throws Exception {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPWithdrawalUsersData(brand, server);
		Object dataCP[][] = TestDataProvider.getAlphaRegUsersData(brand, server);
		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
		adminAPIUserAccount = new AdminAPIUserAccount((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

		GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
		this.checkBalanceAndCashAdjustment((String)data[0][3]);

		if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("au")){
			pcswdapi= new PCSAPIWithdraw((String)dataCP[0][3],(String)data[0][5],(String)data[0][6]);
			JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
			Map<String, String> channelDetails = pcswdapi.apiWDPCSChannelInfo(accInfo, "F00000",4,86);
			String channelMerchantID = channelDetails.get("channelMerchantId");
			this.appWithdraw((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3],channelMerchantID);

		}else {
			this.withdraw((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);

		}
		//cancel this withdraw application
		new CPAPIWithdraw((String)dataCP[0][3],(String)data[0][5],(String)data[0][6]);
	}
	
	@Test(priority = 0, description = testCaseDescUtils.APPAPIWITHDRAW_JAPAN_BT)
	@Parameters(value= {"Brand","Server"})
	public void testAppCPSWithdraw(String brand,String server) throws Exception {
		brand = GlobalMethods.setEnvValues(brand);
		//if(!brand.equalsIgnoreCase("um")) {
			Object data[][] = TestDataProvider.getAlphaAPPWithdrawalUsersData(brand, server);
			Object dataCP[][] = TestDataProvider.getAlphaRegUsersData(brand, server);
			adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
			adminAPIUserAccount = new AdminAPIUserAccount((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

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
		if(!brand.equalsIgnoreCase("star") ) {
			Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
			Object dataCP[][] = TestDataProvider.getAlphaRegUsersData(brand, server);
			adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
			adminAPIUserAccount = new AdminAPIUserAccount((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

			GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
			this.checkBalanceAndCashAdjustment((String)data[0][3]);
			this.ccWithdraw((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);
			new CPAPIWithdraw((String)dataCP[0][3],(String)data[0][5],(String)data[0][6]);
		}else {
			throw new SkipException("Skipping this test for Star brand");
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIACC_REGISTER_DEMO_MT4,groups={"API_Appservice"})
	@Parameters(value= {"Brand","Server"})
	public void testAppOpenMT4DemoAccount(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
		GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
		this.openDemoAccount((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],"USD","MT4");
	}

	@Test(priority = 0, description = testCaseDescUtils.APPAPIACC_ADDITIONAL_ACC,groups={"API_Appservice"})
	@Parameters(value= {"Brand","Server"})
	public void testAppAddAdditionalAccount(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPAddAccUsersData(brand, server);
		GlobalMethods.printDebugInfo("userid: " + (String)data[0][2] + " accountNo: " + (String)data[0][3]);
		this.addAdditionalAccount((String)data[0][1], (String)data[0][0], brand, (String)data[0][2],(String)data[0][3]);
	}

	@Test(priority = 0)
	@Parameters(value= {"Brand","Server"})
	public void testAppOpenDemoAccountV2(String brand,String server) {
		brand = GlobalMethods.setEnvValues(brand);
		Object data[][] = TestDataProvider.getAlphaAPPRegUsersData(brand, server);

		//The brands which already complete registration enhancement use new registration api(V2) to create demo(currently vfx,vt,pug)
		if(brand.equalsIgnoreCase("star")||brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("vt")||brand.equalsIgnoreCase("pug"))
		{
			this.openDemoAccountV2((String)data[0][1], (String)data[0][0], brand,server,"Italy", "usd", "MT4","standardSTP", "", "");
		}

	}
}
