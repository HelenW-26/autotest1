package newcrm.testcases.cptestcases;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import newcrm.business.businessbase.*;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.utils.DepositCallBack;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.adminapi.AdminAPI;
import newcrm.business.businessbase.promotions.DepositBonus;
import newcrm.business.businessbase.promotions.PromotionRecordResult;
import newcrm.business.businessbase.promotions.PromotionResult;
import newcrm.business.businessbase.promotions.TradingBonus;
import newcrm.business.dbbusiness.PaymentDB;
import newcrm.business.dbbusiness.PromotionDB;
import newcrm.business.dbbusiness.UsersDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.PROMOTION;
import newcrm.testcases.BaseTestCase;
import newcrm.utils.AlphaServerEnv;
import newcrm.utils.app.APPPromotions;

public class CPPromotionsTestCase extends BaseTestCase {
	
	protected AdminAPI adminapi;
	protected UsersDB userdb;
	protected PromotionDB promotion;
	protected PaymentDB payment;
	
	protected CPMenu menu;
	protected CPDepositFunds dFunds;
	protected CPFasaPay perfectM;
	protected DepositBonus dpb;
	protected String openAPI;
	
	@Override
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug","Server"})
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, @Optional("")String Regulator,
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
				              ITestContext context) {
		if("prod".equalsIgnoreCase(TestEnv)) {
			AdminName="Test CRM";
			AdminPass="BDAVJECnh4hovtZLV33N";
		}else {
			AdminName="cmatest";
			AdminPass="123Qwe";
		}

		serverName = server;
		Object data[][] = TestDataProvider.getAlphaPromotionUsersData(Brand, server);
		assertNotNull(data);
		openAPI = (String) data[0][5];
		
		launchBrowser( TestEnv,  "true",  Brand,  (String)data[0][0], (String)data[0][3], (String)data[0][1],(String)data[0][2], (String)data[0][4],  AdminName, AdminPass,  Debug, context);
	}

	@Override
	public void goToCpHomePage() {
		//覆盖掉to home page方法
	}
	
	protected void auditLatestDeposit(String account) {
		if(adminapi==null) {
			adminapi = new AdminAPI(AdminURL, dbRegulator, AdminName, AdminPass, dbBrand, dbenv);
		}
		if(payment==null) {
			payment = new PaymentDB(dbenv, dbBrand, dbRegulator);
		}
		JSONObject depositInfo = payment.getLatestDeposit(account);
		assertNotNull(depositInfo,"Have not found deposit information : " + account);
		
		Boolean isAudit = false;
		for(int i=0;i<10;i++){
			Integer status = depositInfo.getInteger("status");
			if(status==6) {
				isAudit = true;
				break;
			}
			menu.refresh();
			depositInfo = payment.getLatestDeposit(account);
		}
		assertTrue(isAudit,"Deposit status have not been changed!!! Please Check account: " + account);
		
		
		String depositID = depositInfo.getString("id");
		String actual_amount = depositInfo.getString("actual_amount");
		
		//adminapi.auditDeposit(depositID, actual_amount, "deposit bonus automation test");
		assertTrue(adminapi.auditDeposit(depositID, actual_amount, "promotion automation test"),"admin audit deposit failed!");
		
		
	}
	public void funcDepositBonus() throws Exception {
		userdb = new UsersDB();
		menu = myfactor.newInstance(CPMenu.class);
		DepositTestCases dt = new DepositTestCases();
		
		JSONArray userinfo = userdb.getUserRegistrationInfo(TraderName.toLowerCase(), dbenv, dbBrand, dbRegulator);
		
		assertTrue(userinfo.size()>0,"have not found user's information: " + TraderName);
		menu.changeLanguage("English");
		menu.waitLoading();
		
		dpb = myfactor.newInstance(DepositBonus.class);
		perfectM = myfactor.newInstance(CPFasaPay.class);
		dFunds = myfactor.newInstance(CPDepositFunds.class);
		promotion = new PromotionDB(dbenv, dbBrand, dbRegulator);
		adminapi = new AdminAPI(AdminURL, dbRegulator, AdminName, AdminPass, dbBrand, dbenv);
		payment = new PaymentDB(dbenv, dbBrand, dbRegulator);

		String userId = userinfo.getJSONObject(0).getString("userId");
		
		JSONObject campaignStatus = promotion.getStatus(PROMOTION.DEPOSITBONUS, userId);
		if(campaignStatus==null || campaignStatus.getIntValue("opt_in")==0) {
			menu.goToMenu(CPMenuName.CPPORTAL);//用户没有OPT-IN，在CP OPT-IN
			menu.goToMenu(CPMenuName.DEPOSITBONUS);
			
			String message = dpb.optInDPB();
			System.out.println("Got Message From Client Portal : " + message);
			assertTrue(message.toLowerCase().contains("successfully"),"Submit Opt-in Failed!");
			//check db
			JSONObject newStatus = promotion.getStatus(PROMOTION.DEPOSITBONUS, userId);
			
			assertTrue(newStatus.getIntValue("opt_in")==1,"update db failed!!");
			campaignStatus = newStatus;
		}

		GlobalMethods.printDebugInfo(campaignStatus.toJSONString());
		GlobalMethods.printDebugInfo("User: " + TraderName + " opt-in " + campaignStatus.getString("name") );

		//入金获取credit
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);

		assertTrue(dFunds.navigateToMethod(DEPOSITMETHOD.PERFECTMONEY),"Did not find deposit method: " + DEPOSITMETHOD.PERFECTMONEY);
		
		String account = "";
		for(CURRENCY c : CURRENCY.values()) {
			account = perfectM.checkAccount(c);
			if(account!=null && !account.equals("")) {
				break;
			}
		}
		menu.goToMenu(CPMenuName.CPPORTAL);

		JSONObject accountInfo = adminapi.getAccountInfo(account);
		assertNotNull(accountInfo,"Have not found account information : " + account);
		Double credit = accountInfo.getDouble("credit");
		GlobalMethods.printDebugInfo("before credit: " + credit);

		dt.testPerfectMoney(openAPI);
		callback(perfectM,account,openAPI,49,10);

		accountInfo = adminapi.getAccountInfo(account);
		Double credit_new = accountInfo.getDouble("credit");
		GlobalMethods.printDebugInfo("credit_new: " + credit_new);

		assertTrue(credit_new-credit>0,"got new credit failed!");
		System.out.println("**************SUMMARY*******************");
		System.out.printf("%-20s: %10s\n","Account",account);
		System.out.printf("%-20s: %10s\n","Promotion",campaignStatus.getString("name"));
		System.out.printf("%-20s: %10s\n","Previous Credit",credit);
		System.out.printf("%-20s: %10s\n","Current Credit",credit_new-credit);
		System.out.printf("%-20s: %10s\n","Total Credit",credit_new);
	}
	
	/**
	 * 目前实现是，当有新promotion添加到case中，实现一个function method，然后在这个方法中调用
	 * @param promotion 从DataProvider中逐行获取需要运行的promotion，根据promotion运行对应的function method
	 * @param context testNG自动赋值
	 */
	@Test(dependsOnMethods="generatePromotions",dataProvider="getPromotions")
	public void testPromotions(@Optional("")String promotion,ITestContext context) throws Exception {
		GlobalMethods.printDebugInfo("promotion: " + promotion);
		context.setAttribute("promotion", promotion);//生成report时使用
		
		if(promotion.contains("Deposit Bonus")) {
			System.out.println("*************************************************************");
			funcDepositBonus();
		}else if(promotion.contains("Refer A Friend")) {
			System.out.println("*************************************************************");
			funcRaf();
		}else if(promotion.contains("Trading Bonus")) {
			System.out.println("*************************************************************");
			this.funcTradingBonus();
		}
		
	}
	
	
	/**
	 * 从testNG xml中读取promotions配置，格式为： "Deposit Bonus,Refer A Fried,Trading Bonus"
	 * 将参数存入到context中，设置为attribute "promotions"
	 * @param promotions testNG xml配置文件中传入参数 
	 * @param context testNG自动赋值
	 */
	@Test
	@Parameters(value= {"Promotions"})
	public void generatePromotions(@Optional("")String promotions, ITestContext context) {
		context.setAttribute("promotions", promotions);
	}
	
	/**
	 * 测试case指定该方法为DataProvider，从context中获取promotions并解析为一个数组，每一行会运行一次测试用例
	 * @param context 测试上下文，带有这个参数后，testNG框架会自动赋值
	 * @return 返回解析出来的promotions列表,比如： {{Refer A Fried},{Trading Bonus},{Deposit Bonus}}
	 */
	@DataProvider(name="getPromotions")
	public Object[][] getPromotions(ITestContext context){
		String promotions = context.getAttribute("promotions").toString();
		if("".equals(promotions)) {
			return new Object[][] {};
		}
		String promotion[] = promotions.split(",");
		Object[][] result =  new Object[promotion.length][1];
		for(int i =0;i<promotion.length;i++) {
			result[i][0] = promotion[i].trim();
		}
		return result;
	}
	
	/**
	 * 测试RAF
	 * 1. 通过APP service获得用户推荐码
	 * 2. 使用推荐码注册新用户
	 * 3. 检查db中RAF的link是否有正常创建
	 * 4. 将RAF的result存到文件中，供后续cron job刷新后检查
	 */
	protected void funcRaf() throws Exception {
		RegisterTestcases rg = new RegisterTestcases();
		userdb = new UsersDB();
		JSONArray userinfo = userdb.getUserRegistrationInfo(TraderName.toLowerCase(), dbenv, dbBrand, dbRegulator);
		
		assertTrue(userinfo.size()>0,"have not found user's information: " + TraderName);
		
		String appHost = AlphaServerEnv.getAPPServiceUrl(serverName);
		
		assertFalse(appHost==null || "".equals(appHost) || "null".equalsIgnoreCase(appHost),"Do not get app service url form server - " + serverName);
		APPPromotions app = new APPPromotions(appHost,dbRegulator.toString(),dbBrand.toString());
		String refererId = userinfo.getJSONObject(0).getString("userId");
		
		
		String rafCode = app.GetRAFCode(refererId, PROMOTION.RAF.getCampaignType(dbenv));
			
		String refereeId = rg.testRegistMT4LiveAccountWithRAFCode(rafCode, "");
		promotion = new PromotionDB(dbenv, dbBrand, dbRegulator);
		
		JSONObject result = promotion.getRafLink(refererId, refereeId);
		
		assertNotNull(result, "Have not found the link referrer : "  +refererId + " and refereeId : " + refereeId);
		
		GlobalMethods.printDebugInfo("Find reference: " + result.toJSONString());

		//创建一个需要检查的result，result信息包括campaign名字，campaign id，participant id，品牌，监管，环境
		PromotionResult record = new PromotionResult(PROMOTION.RAF.toString(),PROMOTION.RAF.getCampaignType(dbenv).toString(),result.getString("participant_id"),
				dbBrand,dbRegulator,dbenv);
		GlobalMethods.printDebugInfo(record.toString());
		
		PromotionRecordResult file = new PromotionRecordResult();
		file.addResult(record);
		file.writeToFile();
	}
	
	protected void funcTradingBonus() throws Exception {
		RegisterTestcases rg = new RegisterTestcases();
		HashMap<String,String> user = rg.createUserForTradingBonus("");//account type is standard,currency USD
		TradingBonus tb = myfactor.newInstance(TradingBonus.class);
		menu = myfactor.newInstance(CPMenu.class);
		menu.refresh();
		menu.goToMenu(CPMenuName.TRADINGBONUS);
		String account = tb.activeTradingBonus();
		assertNotNull(account, "Active Trading Bonus Failed!!!");
		
		DepositTestCases deposit = new DepositTestCases();
		deposit.interBankTransferPay(account,"2500");
		auditLatestDeposit(account);
		
		JSONObject accountInfo = adminapi.getAccountInfo(account);
		assertNotNull(accountInfo,"Have not found account information : " + account);
		
		if(promotion==null) {
			promotion = new PromotionDB(dbenv, dbBrand, dbRegulator);
		}
		String userId = user.get("userId");
		JSONArray vouchers = promotion.getVouchers(userId, PROMOTION.TRADINGBONUS.getCampaignType(dbenv)); 
		for(int i=0;i<10;i++) {
			if(vouchers!=null) {
				break;
			}
			
			menu.refresh();
			vouchers = promotion.getVouchers(userId, PROMOTION.TRADINGBONUS.getCampaignType(dbenv));
		}
		
		assertNotNull(vouchers,"Have not found any voucher for trading bonus!! User ID: " +userId + " Email: " + user.get("email") + " Password: " + user.get("password"));
		
		GlobalMethods.printDebugInfo("Find vouchers for trading bonus: \n"+vouchers.toJSONString()+"\n"+
		"User ID: " +userId + " Email: " + user.get("email") + " Password: " + user.get("password"));
	}
	
	@AfterClass
	public void closeDriver() {
		driver.quit();
	}

	protected void callback(DepositBase perfectM,String account,String openAPIURL,int payment_type,int payment_channel) throws InterruptedException {
		JSONObject obj = perfectM.getDepositRecord(dbenv, dbBrand, dbRegulator, account, payment_type, payment_channel);
		String orderNo = obj.getString("order_number");

		System.out.println("Find  orderNo: " + orderNo);
		System.out.println("***Start send callback and verify********");
		cpsCallback(perfectM,49,10,account,openAPIURL);

		//Need to wait 15 seconds for Kafka and Cron consuming the notification and updating the order status
		Thread.sleep(50000);

		JSONObject objUpdated = perfectM.getDepositRecord(dbenv, dbBrand, dbRegulator, account, payment_type, payment_channel);
		String status = objUpdated.getInteger("status").toString();

		//need more waiting time
		if (!status.equalsIgnoreCase("5"))
		{
			Thread.sleep(50000);
			status = objUpdated.getInteger("status").toString();
		}
		GlobalMethods.printDebugInfo("The status of order " + orderNo + " is: " + status);
		System.out.println("***Callback succeed!!********");
	}

	private void getDepositInfo(DepositBase deposit,String account)
	{
		JSONObject obj = deposit.getDepositRecordByAccount(dbenv, dbBrand, dbRegulator, account);
		System.out.println("************Deposit order info************");
		System.out.println("****************************");
		System.out.printf("%-20s : %s\n", "user_id", obj.getInteger("user_id"));
		System.out.printf("%-20s : %s\n", "order_number", obj.getString("order_number"));
		System.out.printf("%-20s : %s\n", "payment_type", obj.getInteger("payment_type"));
		System.out.printf("%-20s : %s\n", "payment_channel", obj.getInteger("payment_channel"));
		System.out.printf("%-20s : %s\n", "cps_attach_variable", obj.getString("cps_attach_variable"));
		System.out.printf("%-20s : %s\n", "extend1", obj.getString("extend1"));
		System.out.printf("%-20s : %s\n", "extend2", obj.getString("extend2"));
		System.out.printf("%-20s : %s\n", "extend3", obj.getString("extend3"));
		System.out.println("*****************************");
	}
	public void cpsCallback(DepositBase deposit,int depositType, int channel,String account,String openApi)
	{
		JSONObject obj = deposit.getDepositRecord(dbenv, dbBrand, dbRegulator, account, depositType, channel);

		if(dbenv.equals(GlobalProperties.ENV.PROD)) {
			System.out.println(obj.toJSONString());
			Assert.assertTrue(obj.getInteger("status").equals(2),"submit to third part failed.");
		}

		if(!obj.getInteger("status").equals(2))
		{
			Assert.assertTrue(deposit.updateStatus(dbenv, dbBrand, dbRegulator, obj.getInteger("id"), 2),"update deposit status failed");
		}

		//callback
		if(!"".equals(openApi)) {
			DepositCallBack callback = new DepositCallBack(openApi,Brand,Regulator);
			callback.generateCallback(DepositCallBack.CHANNEL.CPSCallback,obj.getString("order_number"),obj.getString("extend3"),obj.getDouble("deposit_amount"),"","","","");
			String response = callback.sendCallback().toLowerCase();

			if(response.contains("ok") || response.contains("success")) {
				System.out.println("***Test callback succeed!!********");
			}else {
				System.out.println("***Test deposit Failed!!********");
				Assert.fail("call back failed");
			}
		}
	}
}
