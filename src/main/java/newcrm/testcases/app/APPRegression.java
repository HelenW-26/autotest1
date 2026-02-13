package newcrm.testcases.app;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.business.adminbusiness.AdminAPIBusiness;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.utils.AlphaServerEnv;
import newcrm.utils.app.*;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalMethods;
import newcrm.testcases.BaseTestCase;
import utils.LogUtils;

import static org.testng.Assert.*;

public class APPRegression {

	protected Object[][] adminData;
	protected AdminAPIPayment adminPaymentAPI;
	protected AdminAPIUserAccount adminAPIUserAccount;

	String amount =String.valueOf((int)(50 + Math.random() * 10));
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","Country","Currency", "Platform", "AccountType","IBCode","RAFCode"})
	public void registration(String host, String regulator, String brand,String server,
							 String country,String currency,String platform,String acountType,String affid,String rafcode) throws Exception {
		platform = platform.toUpperCase();
		currency = currency.toUpperCase();
		regulator = regulator.toUpperCase();
		brand = brand.toUpperCase();

		if("${affid}".equalsIgnoreCase(affid)) {
			affid = "";
		}
		if("${rafcode}".equalsIgnoreCase(rafcode)) {
			rafcode = "";
		}
		System.out.println(host);
		System.out.println(regulator);
		System.out.println(brand);
		System.out.println(country);
		System.out.println(currency);
		System.out.println(platform);
		System.out.println(acountType);
		System.out.println(affid);
		System.out.println(rafcode);
		HashMap<String,String> infos = new HashMap<>();


		//String email = GlobalMethods.getRandomString(8)+GlobalMethods.getRandomNumberString(3)+"@appapiautotest.com";
		//email = email.toLowerCase();
		//09/03/2023 move email to "" in order to simulate real app user
		String email = "";
		String lastName = "AppApi testcrm";
		String firstName = "Automotiontest"+GlobalMethods.getRandomString(6);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String idNo = "API"+GlobalMethods.getRandomString(5)+GlobalMethods.getRandomNumberString(6);

		Register rg = new Register(host,regulator,brand);

		String result = rg.createUser(email, affid, "", firstName, lastName,
				country, mobile, "APP", false, "1264", "automotion app api test", brand.toUpperCase()+"_APP", "", "",
				"", currency, 1, platform, "", "", "", "123Qwe", "", regulator, rafcode, "");
		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		if(rescode.compareTo(1005)==0) {
			System.out.println("Please check APP MD5 and APP aes key in configuration file: sys_security.properties !!");
		}
		//code 1019 is "msg":"Register demo account fail" but actually lead will be created successfully
		//code will be 1000 if set true for createDemo flag
		assertTrue(rescode.equals(1000) || rescode.equals(1019), "create user failed!! \n"+result);
		//assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"create demo user failed!!\n " + result);
		JSONObject response = JSONObject.parseObject(rg.getResponse(result));

		Integer userid = response.getInteger("userId");
		infos.put("userID",userid.toString());
		infos.put("Mobile", "1246 "+mobile);
		infos.put("Email",email);
		infos.put("Password", "123Qwe");
		System.out.println("Mobile : 1246 " + mobile);
		System.out.println("Email: " + email);
		System.out.println("userID: " + userid);
		//step 1
		result = rg.saveStep1(userid, lastName, firstName, "app api test", email, "1264", mobile, "3", "1998-08-08", "3490", idNo);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"save step one failed!!\n " + result);

		//step 2
		result = rg.getInfo(userid, 2);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 2 failed!!\n " + result);
		response = JSONObject.parseObject(rg.getResponse(result));
		Integer countryCode = response.getInteger("countryId");
		result = rg.saveStep2(userid, countryCode, "test address " + GlobalMethods.getRandomString(5), "test state", "old six", "6666");
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"submit step 2 failed!!\n " + result);

		//step 3
		result = rg.saveStep3(userid);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 3 failed!!\n " + result);

		//step 4
		if(platform.equalsIgnoreCase("mts")) {
			acountType = "st";
		}
		result = rg.saveStep4(userid, platform, acountType, currency);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 4 failed!!\n " + result);

		//step 5
		result = rg.saveStep5(userid);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 5 failed!!\n " + result);
		System.out.println("*********************Register Summary**********************");
		for(Entry<String, String> entry: infos.entrySet()) {
			System.out.printf("%-30s : %s\n",entry.getKey(),entry.getValue());
		}
		System.out.println("***********************************************************");

		//account aduit
		String user = "cmatest";
		String password = "123Qwe";
		if(GlobalProperties.env.equalsIgnoreCase("PROD")) {
			user =  "Test CRM";
			password = "BDAVJECnh4hovtZLV33N";
		}

		AdminAPIBusiness admin = null;
		try {
			admin = new AdminAPIBusiness(AlphaServerEnv.getAdminUrl(server),GlobalProperties.REGULATOR.valueOf(regulator),user,password, GlobalProperties.ENV.valueOf(GlobalProperties.env.toUpperCase()),GlobalProperties.BRAND.valueOf(brand));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(GlobalMethods.getBrand().equalsIgnoreCase("vfx") ||GlobalMethods.getBrand().equalsIgnoreCase("vt")||GlobalMethods.getBrand().equalsIgnoreCase("pug"))
		{
			Assert.assertTrue(admin.auditMainAccount(userid.toString(), GlobalProperties.PLATFORM.valueOf(platform)).getKey());
		}
		else
		{
			admin.auditMainAccount(String.valueOf(userid),GlobalProperties.PLATFORM.MT4);
		}
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","Country","Currency", "Platform", "AccountType","IBCode","RAFCode"})
	public void registrationNew(String host, String regulator, String brand,String server,
								String country,String currency,String platform,String acountType,String affid,String rafcode,
								String adminLogin, String adminPwd) throws Exception {

		platform = platform.toUpperCase();
		currency = currency.toUpperCase();
		regulator = regulator.toUpperCase();
		brand = brand.toUpperCase();

		if("${affid}".equalsIgnoreCase(affid)) {
			affid = "";
		}
		if("${rafcode}".equalsIgnoreCase(rafcode)) {
			rafcode = "";
		}
		System.out.println(host);
		System.out.println(regulator);
		System.out.println(brand);
		System.out.println(country);
		System.out.println(currency);
		System.out.println(platform);
		System.out.println(acountType);
		System.out.println(affid);
		System.out.println(rafcode);
		HashMap<String,String> infos = new HashMap<>();


		String email = GlobalMethods.getRandomString(8)+GlobalMethods.getRandomNumberString(3)+"@appapiautotest.com";
		email = email.toLowerCase();
		//09/03/2023 move email to "" in order to simulate real app user

		// 1/8/2023 app user need email as well
		//String email = "";
		String lastName = "AppApi testcrm";
		String middileName = "test";
		String firstName = "Automotiontest"+GlobalMethods.getRandomString(6);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String idNo = "API"+GlobalMethods.getRandomString(5)+GlobalMethods.getRandomNumberString(6);

		Register rg = new Register(host,regulator,brand);

		String brandPUG = brand;
		if(brand.equalsIgnoreCase("pug"))
		{
			brandPUG = "pu";
		}
		String result = rg.createUserNew(email, affid, "", firstName, lastName,
					country, mobile, "APP", false, "39", "automotion app api test", brandPUG.toUpperCase() + "_APP", "", "",
					"", currency, 1, platform, "", "", "", "123Qwe", "", regulator, rafcode, "");

		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);

		Integer rescode = obj.getInteger("code");
		if(rescode.compareTo(1005)==0) {
			System.out.println("Please check APP MD5 and APP aes key in configuration file: sys_security.properties !!");
		}
		//code 1019 is "msg":"Register demo account fail" but actually lead will be created successfully
		//code will be 1000 if set true for createDemo flag
		assertTrue(rescode.equals(1000) || rescode.equals(1019), "create user failed!! \n"+result);
		//assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"create demo user failed!!\n " + result);
		JSONObject response = JSONObject.parseObject(rg.getResponse(result));

		Integer userid = response.getInteger("userId");
		infos.put("userID",userid.toString());
		infos.put("Mobile", "39 "+mobile);
		infos.put("Email",email);
		infos.put("Password", "123Qwe");
		System.out.println("Mobile : 1246 " + mobile);
		System.out.println("Email: " + email);
		System.out.println("userID: " + userid);
		//step 1
		result = rg.saveStep1New(userid, lastName, firstName, "app api test", email, "39", mobile, "112", "1998-08-08", "3490", idNo,"6777","male");
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"save step one failed!!\n " + result);

		//step 4(New register process skip step 2 and 3)
		result = rg.getInfoNew(userid, 4);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 4 failed!!\n " + result);
		if(platform.equalsIgnoreCase("mts")) {
			acountType = "st";
		}
		result = rg.saveStep4New(userid, platform, acountType, currency,"APP");
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"submit step 4 failed!!\n " + result);


		//step 5
		result = rg.getInfoNew(userid, 5);
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 5 failed!!\n " + result);

		result = rg.saveStep5New(userid,firstName,middileName,lastName,"112","2",idNo,"6777",platform,currency,acountType,"test address " + GlobalMethods.getRandomString(5), "test surburb", "test state","test");
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"get info from step 5 failed!!\n " + result);


		//upload poa
		result = rg.uploadPOA(userid,"6777");
		obj = JSONObject.parseObject(result);
		assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"upload poa failed!!\n " + result);

		System.out.println("*********************Register Summary**********************");
		for(Entry<String, String> entry: infos.entrySet()) {
			System.out.printf("%-30s : %s\n",entry.getKey(),entry.getValue());
		}
		System.out.println("***********************************************************");

		//account aduit
		String user = adminLogin;
		String password = adminPwd;
		if(GlobalProperties.env.equalsIgnoreCase("PROD")) {
			user =  "Test CRM";
			password = "Hc8P4RxuKMMmGKSgEZim";
		}

		AdminAPIBusiness admin = null;
		try {
			admin = new AdminAPIBusiness(AlphaServerEnv.getAdminUrl(server),GlobalProperties.REGULATOR.valueOf(regulator),user,password, GlobalProperties.ENV.valueOf(GlobalProperties.env.toUpperCase()),GlobalProperties.BRAND.valueOf(brand));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(GlobalMethods.getBrand().equalsIgnoreCase("vfx") ||GlobalMethods.getBrand().equalsIgnoreCase("vt")||GlobalMethods.getBrand().equalsIgnoreCase("pug"))
		{
			Assert.assertTrue(admin.auditMainAccount(userid.toString(), GlobalProperties.PLATFORM.valueOf(platform)).getKey(), "Account Audit Failed");
		}
		else
		{
			Assert.assertTrue(admin.auditMainAccount(String.valueOf(userid),GlobalProperties.PLATFORM.MT4).getKey(), "Account Audit Failed");
		}
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void deposit(String host, String regulator, String brand, String userId, String accountNo, String platform,String depositType) {
		regulator = regulator.toUpperCase();
		brand = brand.toUpperCase();
		Deposit deposit =new Deposit(host,regulator,brand);
		String cardBeginSixDigits = "545433";
		String cardLastFourDigits = "6666";
		String cardHolderName = "APP-API-CRM";

		//137 - CC powercash, 124 fasapay app, 145 USDT ERC20 app;
		/*
		 * ArrayList<String> dpTypes = new ArrayList<>(); dpTypes.add("137");
		 * dpTypes.add("124"); dpTypes.add("145");
		 *
		 * dpTypes.forEach((e) -> { });
		 */

		String dpType = depositType;
		Double amount = 50.00;

		String result = null;

		if(brand.equalsIgnoreCase("vjp"))
		{
			dpType = "455";

		}
		result = deposit.sendDeposit(String.valueOf(amount), BigDecimal.valueOf(1),
					BigDecimal.valueOf(amount), accountNo, userId, platform, dpType,
					cardBeginSixDigits, cardLastFourDigits, "APP API testCRM",
					"06", "2028", "1", "", "545433,6666,06,2028,1,testcrmdontuse test dadsaautomation", "");

		System.out.println(result);

		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP API deposit failed!! \n"+result);

		JSONObject payment = null;
		int type = 1;
		//find order number in DB
		if(brand.equalsIgnoreCase("vjp")) {
			type = 50;
		}
		if(depositType.equalsIgnoreCase("146"))
		{
			type = 15;
		}
		payment = deposit.getDepositRecord(GlobalProperties.ENV.valueOf(GlobalProperties.env.toUpperCase()),
				GlobalProperties.BRAND.valueOf(brand), GlobalProperties.REGULATOR.valueOf(regulator),
				accountNo, type, Integer.valueOf(dpType));
		String orNum = payment.getString("order_number");

		//update deposit status
		result = deposit.updateStatus(String.valueOf(amount),BigDecimal.valueOf(1), BigDecimal.valueOf(amount),
				accountNo, userId, dpType, platform, orNum,"1",cardBeginSixDigits, cardLastFourDigits,cardHolderName);
		System.out.println("deposit order:" + result);

		obj = JSONObject.parseObject(result);
		rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1134) , "APP API update deposit status failed!! \n"+result);

		result = deposit.updateStatus(String.valueOf(amount), BigDecimal.valueOf(1), BigDecimal.valueOf(amount),
					accountNo, userId, dpType, platform, orNum, "3",cardBeginSixDigits, cardLastFourDigits,cardHolderName);

		System.out.println("deposit action" + result);

		obj = JSONObject.parseObject(result);
		rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP API update deposit status failed!! \n"+result);

	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void addAdditionalAccount(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
		AddAdditonalAccount addAdditonalAccount =new AddAdditonalAccount(host,"/account/apply_namesake_account",regulator,brand);

		String result = addAdditonalAccount.sendAddAddtionalAccount("1",userId,"","USD","accountNo","MT4");
		System.out.println("result:" + result);

		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP API add additional account failed!! \n"+result);
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void withdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
		String result = "";

		List<String> brandList = Arrays.asList("vfx","au","vt","um","pug","vjp","star","mo");
		if(brandList.contains(brand.toLowerCase())) {
			//Crypto V2
			result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD",amount,"86");
		}else {
			result = withdraw.sendCryptoWithdrawBatch(userId,accountNo,"USD",amount,"36","2",
				"testcrm app","","","","",
				"api test app withdrawal","0x8E6fd509F491152bD377854ec3CeD86e96c2e94e");
		}
		
		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP crypto withdraw failed!! \n"+result);
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo","channelMerchantId"})
	public void appWithdraw(String host, String regulator, String brand, String userId, String accountNo, String channelMerchantId) {
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
		String result = "";

		List<String> brandList = Arrays.asList("vfx","au","vt","um","pug","vjp","star","mo");
		if(brandList.contains(brand.toLowerCase())) {
			//Crypto V2
			result = withdraw.sendAPPSpecifiedCPSWithdraw(userId,accountNo,"USD",amount,"86",channelMerchantId);
		}else {
			result = withdraw.sendCryptoWithdrawBatch(userId,accountNo,"USD",amount,"36","2",
					"testcrm app","","","","",
					"api test app withdrawal","0x8E6fd509F491152bD377854ec3CeD86e96c2e94e");
		}

		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP crypto withdraw failed!! \n"+result);
	}


	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo","channelMerchantId"})
	public void cpsWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
		String result = null;
		if(brand.equalsIgnoreCase("vjp"))
		{
			result = withdraw.sendCPSWithdrawBatchV1(userId, accountNo, "USD", amount, "62", "1050");
		}
		else
		{
		//vt:10011286 pu:10004823
		 result = withdraw.sendCPSWithdrawBatchV1(userId, accountNo, "USD", amount, "6", "199");
	}
		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP MYBT CPS withdraw failed!! \n"+result);
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void ccWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);

		String result = withdraw.sendCCWithdrawBatch(userId,accountNo,"USD","50.00","1",
				  "test app","511566","3344","6","2029", "api test app withdrawal");

		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CC withdraw failed!! \n"+result);
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","currency","tradingPlatform"})
	public void openDemoAccount(String host, String regulator, String brand, String userId, String currency, String tradingPlatform) {
		regulator = regulator.toUpperCase();
		ApplyNamesakeAccount applyNamesakeAccount = new ApplyNamesakeAccount(host,regulator,brand);

		String result =applyNamesakeAccount.openDemoAccount(userId,"1",currency,tradingPlatform);
		System.out.println(result);

		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP API open demo account failed!! \n"+result);
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","Country","Currency", "Platform", "AccountType","IBCode","RAFCode"})
	public void openDemoAccountV2(String host, String regulator, String brand,String server,
								  String country,String currency,String platform,String acountType,String affid,String rafcode) {

		platform = platform.toUpperCase();
		currency = currency.toUpperCase();
		regulator = regulator.toUpperCase();
		brand = brand.toUpperCase();


		System.out.println(host);
		System.out.println(regulator);
		System.out.println(brand);
		System.out.println(country);
		System.out.println(currency);
		System.out.println(platform);
		System.out.println(acountType);
		System.out.println(affid);
		System.out.println(rafcode);
		HashMap<String,String> infos = new HashMap<>();


		String email = GlobalMethods.getRandomString(8)+GlobalMethods.getRandomNumberString(3)+"@appapidemoautotest.com";
		email = email.toLowerCase();
		//09/03/2023 move email to "" in order to simulate real app user

		// 1/8/2023 app user need email as well
		//String email = "";
		String lastName = "AppApi demotestcrm";
		String firstName = "Automotiondemotest"+GlobalMethods.getRandomString(6);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String idNo = "API"+GlobalMethods.getRandomString(5)+GlobalMethods.getRandomNumberString(6);

		Register rg = new Register(host,regulator,brand);

		String result = rg.createUserNew(email, affid, "", firstName, lastName,
				country, mobile, "APP", true, "39", "automotion app api test", brand.toUpperCase()+"_APP", "", "",
				"", currency, 1, platform, "", "", "", "123Qwe", "", regulator, rafcode, "");
		System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		if(rescode.compareTo(1005)==0) {
			System.out.println("Please check APP MD5 and APP aes key in configuration file: sys_security.properties !!");
		}
		//code 1019 is "msg":"Register demo account fail" but actually lead will be created successfully
		//code will be 1000 if set true for createDemo flag
		assertTrue(rescode.equals(1000) || rescode.equals(1019), "create demo user failed!! \n"+result);
		//assertEquals(obj.getInteger("code"), Integer.valueOf(1000),"create demo user failed!!\n " + result);
		JSONObject response = JSONObject.parseObject(rg.getResponse(result));

		Integer userid = response.getInteger("userId");
		infos.put("userID",userid.toString());
		infos.put("Mobile", "39 "+mobile);
		infos.put("Email",email);
		infos.put("Password", "123Qwe");
		System.out.println("Mobile : 1246 " + mobile);
		System.out.println("Email: " + email);
		System.out.println("userID: " + userid);

	}

	//Perform cash adjusment if balance < 100
//	public void checkBalanceAndCashAdjustment(String acctNum){
//		JSONObject tradingAcct = (adminAPIUserAccount.apiTradingAcctSearch(acctNum, "", PLATFORM.MT4)).getJSONArray("rows").getJSONObject(0);
//		if(tradingAcct.getInteger("total")==0){
//		tradingAcct = (adminAPIUserAccount.apiTradingAcctSearch(acctNum, "", PLATFORM.MT5)).getJSONArray("rows").getJSONObject(0);
//
//		}
//		double acctBalance = Double.parseDouble(tradingAcct.getString("balance"));
//		String currency = tradingAcct.getString("apply_currency");
//
//		GlobalMethods.printDebugInfo("Acct Balance: "+acctBalance);
//		if (acctBalance >= 0 && acctBalance <= 100) {
//			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency, "1000", "1");
//		} else if (acctBalance < 0) {
//			acctBalance = 1000 - acctBalance;
//			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency, String.valueOf(acctBalance), "1");
//		}
//	}
//Perform cash adjustment if balance < 100
	public void checkBalanceAndCashAdjustment(String acctNum){
		JSONObject tradingAcct = null;

		// 先尝试MT4平台查询
		try {
			tradingAcct = (adminAPIUserAccount.apiTradingAcctSearch(acctNum, "", PLATFORM.MT4, "", "", ""))
					.getJSONArray("rows").getJSONObject(0);
			LogUtils.info("MT4 account found");
			LogUtils.info("tradingAcct:\n " + tradingAcct.toJSONString());
			if(tradingAcct.getInteger("total") == 0){
				throw new RuntimeException("MT4 account not found");
			}
		} catch (Exception e) {
			// MT4查询失败，尝试MT5平台
			try {
				tradingAcct = (adminAPIUserAccount.apiTradingAcctSearch(acctNum, "", PLATFORM.MT5, "", "", ""))
						.getJSONArray("rows").getJSONObject(0);
			} catch (Exception ex) {
				LogUtils.error("MT5 account not found", ex);
			}
		}

		double acctBalance = Double.parseDouble(tradingAcct.getString("balance"));
		String currency = tradingAcct.getString("apply_currency");

		GlobalMethods.printDebugInfo("Acct Balance: " + acctBalance);

		final double ADJUSTMENT_THRESHOLD = 100.0;
		final double TARGET_BALANCE = 1000.0;

		if (acctBalance >= 0 && acctBalance <= ADJUSTMENT_THRESHOLD) {
			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency,
					String.valueOf(TARGET_BALANCE), "1");
		} else if (acctBalance < 0) {
			double adjustmentAmount = TARGET_BALANCE - acctBalance;
			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency,
					String.valueOf(adjustmentAmount), "1");
		}
	}

	public void checkBalanceAndCashAdjustment(AdminAPIUserAccount adminAPIUserAccount,String acctNum){
		JSONObject tradingAcct = (adminAPIUserAccount.apiTradingAcctSearch(acctNum, "", PLATFORM.MT4, "", "", "")).getJSONArray("rows").getJSONObject(0);
		double acctBalance = Double.parseDouble(tradingAcct.getString("balance"));
		String currency = tradingAcct.getString("apply_currency");

		GlobalMethods.printDebugInfo("Acct Balance: "+acctBalance);
		if (acctBalance >= 0 && acctBalance <= 100) {
			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency, "1000", "1");
		} else if (acctBalance < 0) {
			acctBalance = 1000 - acctBalance;
			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency, String.valueOf(acctBalance), "1");
		}
	}
	public void checkBalanceAndCashAdjustmentForMTS(AdminAPIUserAccount adminAPIUserAccount,String acctNum){
		JSONObject tradingAcct = (adminAPIUserAccount.apiCopyTradingAcctSearch(acctNum)).getJSONArray("rows").getJSONObject(0);
		double acctBalance = Double.parseDouble(tradingAcct.getString("balance"));
		String currency = tradingAcct.getString("apply_currency");

		GlobalMethods.printDebugInfo("Acct Balance: "+acctBalance);
		if (acctBalance >= 0 && acctBalance <= 100) {
			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency, "1000", "1");
		} else if (acctBalance < 0) {
			acctBalance = 1000 - acctBalance;
			adminPaymentAPI.apiAdminCashAdjustmentForCP(acctNum, currency, String.valueOf(acctBalance), "1");
		}
	}

	public static void main(String args[]) throws Exception {
		String host = "app-aries-ex.crm-alpha.com";
		String regulator = "SVG";
		String code = "24ex51gne31fn9rNqNTDNw==";

		APPRegression app = new APPRegression();
		app.registration(host, regulator, "VT", "","Italy", "usd", "MT4","standardSTP", "", "");
	}
}
