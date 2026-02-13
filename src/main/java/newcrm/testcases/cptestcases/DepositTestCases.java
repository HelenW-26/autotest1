package newcrm.testcases.cptestcases;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.deposit.*;
import newcrm.business.vjpbusiness.VJPCPCreditCardSDPay;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.testcases.BaseTestCaseNew;
import org.openqa.selenium.*;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.SkipException;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import newcrm.business.vtbusiness.CPUnionPay;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.utils.DepositCallBack;
import newcrm.utils.DepositCallBack.CHANNEL;
import newcrm.utils.testCaseDescUtils;
import tools.ScreenshotHelper;
import utils.CustomAssert;
import utils.LogUtils;
import vantagecrm.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class  DepositTestCases extends BaseTestCaseNew {

	private String amount;
	private String email="auto_deposit@test.com";
	private String notes="automation test important notes";
	private String account;
	protected AdminAPIPayment adminPaymentAPI;
	protected Object[][] adminData;
    private Factor myfactor;
    private WebDriver driver;
    private CPLogin login;
    @BeforeMethod(alwaysRun = true)
    public void initMethod(){

        myfactor = getFactorNew();
        login = getLogin();
        driver = getDriverNew();

    }
	private void fillDepositBasicInfo(DepositBase deposit, DEPOSITMETHOD method, String sDivTabId, String sImgTabId, String sDivPaneId) {
		amount = this.getRandomDepositAmount(method);
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.changeLanguage("English");
		menu.goToMenu(CPMenuName.CPPORTAL);
		if(GlobalProperties.isWeb) {
			menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		}else {
			menu.goToMenu(CPMenuName.DEPOSITFUNDSMOBILE);
		}
		//pug has different xpath of elements
		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			pugNavigateToMethod(method, deposit, sDivTabId, sImgTabId, sDivPaneId, false, null);
		}
		else
		{
			assertTrue(deposit_funds.navigateToMethod(method), "Failed when try to navigate to method : " + method.toString());
		}

		//au india p2p has different with other brands
		/*
		if(Brand.equalsIgnoreCase(BRAND.VFX.toString()))
		{
			driver.findElement(By.xpath("(//*[@id='indiaP2P']/div[1]/div/div/form/div/div/p)")).click();

		}
		*/
		if (method != DEPOSITMETHOD.INDIAIBT ) {
			for (CURRENCY currency : CURRENCY.values()) {
				account = deposit.checkAccount(currency);
				if (account != null) {
					break;
				}
			}
			assertNotNull(account, "Failed: do not have this currency type account");
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_EWALLET_SKRILL)
	public void testSkrillPay() throws InterruptedException {
		CPFasaPay skrillPay = myfactor.newInstance(CPFasaPay.class);
		eWallet(skrillPay,DEPOSITMETHOD.SKRILL);
	}

	@Test(priority = 2)
	public void testAirTM() throws InterruptedException {
		CPFasaPay airTM = myfactor.newInstance(CPFasaPay.class);
		eWallet(airTM,DEPOSITMETHOD.AirTM);
	}

	@Test(priority = 2)
	public void testBkash() {
		CPFasaPay bkash = myfactor.newInstance(CPFasaPay.class);
		eWallet(bkash, DEPOSITMETHOD.BDTBkash);
	}

	@Test(priority = 2)
	public void testRocket() {
		CPFasaPay bkash = myfactor.newInstance(CPFasaPay.class);
		eWallet(bkash, DEPOSITMETHOD.BDTRocket);
	}

	@Test(priority = 2)
	public void testNagad() {
		CPFasaPay nagad = myfactor.newInstance(CPFasaPay.class);
		eWallet(nagad, DEPOSITMETHOD.BDTNagad);
	}

	@Test(priority = 2,groups= {"AuRegression"}, description = testCaseDescUtils.CPDEPOSIT_EWALLET_SKRILL)
	@Parameters(value= {"OpenAPIURL"})
	public void testSkrillPayWithCallBack(String openAPIURL) throws Exception {
		CPSkrillPay skrill = myfactor.newInstance(CPSkrillPay.class);
		testSkrillPay();
		System.out.println("***Start send skrill callback and verify********");

		String sql = "select order_number from tb_payment_deposit where payment_type=21 and mt4_account="+this.account+" order by create_time desc limit 1;";
		String sql2 = "select status from tb_payment_deposit where payment_type=21 and mt4_account="+this.account+" order by create_time desc limit 1;";

		String v_Brand = "";
		switch(Regulator.toLowerCase().trim()) {
			case "cima":
				if(Brand.equalsIgnoreCase("VT")){
					v_Brand = "vt";
				}else {
					v_Brand = "ky";
				}
				break;
			case "asic":
				v_Brand = "au"; break;
			default:
				v_Brand = Regulator.toLowerCase().trim();
		}

		System.out.println("***Test SkillPay deposit succeed!!********");
		JSONObject obj = skrill.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 21, 3);
		String orderNo = obj.getString("order_number");

		System.out.println("Find Skrill orderNo: " + orderNo);
		System.out.println("***Start send skrill callback and verify********");
		//RestAPI.testPostSkrillCallback(openAPIURL, orderNo);
		cpsCallback(skrill,21,3,account,amount,openAPIURL,DEPOSITMETHOD.SKRILL);

		//Need to wait 15 seconds for Kafka and Cron consuming the notification and updating the order status
		Thread.sleep(50000);

		JSONObject objUpdated = skrill.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 21, 3);
		//String status = DBUtils.funcQueryDB(TestEnv, sql2, v_Brand);
		String status = objUpdated.getInteger("status").toString();

		//need more waiting time
		if (!status.equalsIgnoreCase("5"))
		{
			Thread.sleep(50000);
			status = objUpdated.getInteger("status").toString();
		}
		GlobalMethods.printDebugInfo("The status of Skrill order " + orderNo + " is: " + status);
		Utils.funcIsStringContains(status, "5", v_Brand);
		System.out.println("***Test Skillpay callback succeed!!********");
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_Callback)
	public void testSkrillPayWithCallBackNew(String openAPIURL) throws Exception {
		DepositBase skrill = myfactor.newInstance(DepositBase.class);
		depositCommon(skrill, DEPOSITMETHOD.SKRILL);

		System.out.println("***Start send skrill callback and verify********\n"+openAPIURL);

		String sql = "select order_number from tb_payment_deposit where payment_type=21 and mt4_account="+this.account+" order by create_time desc limit 1;";
		String sql2 = "select status from tb_payment_deposit where payment_type=21 and mt4_account="+this.account+" order by create_time desc limit 1;";

		String v_Brand = "";
		switch(Regulator.toLowerCase().trim()) {
			case "cima":
				if(Brand.equalsIgnoreCase("VT")){
					v_Brand = "vt";
				}else {
					v_Brand = "ky";
				}
				break;
			case "asic":
				v_Brand = "au"; break;
			default:
				v_Brand = Regulator.toLowerCase().trim();
		}

		System.out.println("***Test SkillPay deposit succeed!!********");
		JSONObject obj = skrill.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 21, 3);
		String orderNo = obj.getString("order_number");

		System.out.println("Find Skrill orderNo: " + orderNo);
		System.out.println("***Start send skrill callback and verify********");
		//RestAPI.testPostSkrillCallback(openAPIURL, orderNo);
		cpsCallback(skrill,21,3,account,amount,openAPIURL,DEPOSITMETHOD.SKRILL);

		//Need to wait 15 seconds for Kafka and Cron consuming the notification and updating the order status
		Thread.sleep(50000);

		JSONObject objUpdated = skrill.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 21, 3);
		//String status = DBUtils.funcQueryDB(TestEnv, sql2, v_Brand);
		String status = objUpdated.getInteger("status").toString();

		//need more waiting time
		if (!status.equalsIgnoreCase("5"))
		{
			Thread.sleep(50000);
			status = objUpdated.getInteger("status").toString();
		}
		GlobalMethods.printDebugInfo("The status of Skrill order " + orderNo + " is: " + status);
		Utils.funcIsStringContains(status, "5", v_Brand);
		System.out.println("***Test Skillpay callback succeed!!********");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_Callback)
	public void testBinancePayWithCallBackNew(String openAPIURL) throws Exception {
		DepositBase binance = myfactor.newInstance(DepositBase.class);
		depositCommon(binance, DEPOSITMETHOD.BINANCE);

		System.out.println("***Start send Binance callback and verify********\n"+openAPIURL);

		String v_Brand = "";
		switch(Regulator.toLowerCase().trim()) {
			case "cima":
				if(Brand.equalsIgnoreCase("VT")){
					v_Brand = "vt";
				}else {
					v_Brand = "ky";
				}
				break;
			case "asic":
				v_Brand = "au"; break;
			default:
				v_Brand = Regulator.toLowerCase().trim();
		}
		int depositType = 107;
		int channel = 1;
		System.out.println("***Test Binance deposit succeed!!********");
		JSONObject obj = binance.getDepositRecord(dbenv, dbBrand, dbRegulator, account, depositType, channel);
		String orderNo = obj.getString("order_number");

		System.out.println("Find Binance orderNo: " + orderNo);
		System.out.println("***Start send Binance callback and verify********");
		//RestAPI.testPostSkrillCallback(openAPIURL, orderNo);

		cpsCallback(binance,depositType,channel,account,amount,openAPIURL,DEPOSITMETHOD.BINANCE);

		//Need to wait 15 seconds for Kafka and Cron consuming the notification and updating the order status
		Thread.sleep(50000);

		JSONObject objUpdated = binance.getDepositRecord(dbenv, dbBrand, dbRegulator, account, depositType, channel);
		//String status = DBUtils.funcQueryDB(TestEnv, sql2, v_Brand);
		String status = objUpdated.getInteger("status").toString();

		//need more waiting time
		if (!status.equalsIgnoreCase("5"))
		{
			Thread.sleep(50000);
			status = objUpdated.getInteger("status").toString();
		}
		GlobalMethods.printDebugInfo("The status of Binance order " + orderNo + " is: " + status);
		Utils.funcIsStringContains(status, "5", v_Brand);
		System.out.println("***Test Binance Pay callback succeed!!********");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_EWALLET_STICPAY)
	public void testSticPay() {
		CPNetellerPay sticpay = myfactor.newInstance(CPNetellerPay.class);
		eWallet(sticpay,DEPOSITMETHOD.STICPAY);
	}

	@Test(priority = 2)
	@Parameters(value={"OpenApi"})
	public void testNetellerPayWithCallback(@Optional("")String openapi) {
		CPNetellerPay neteller = myfactor.newInstance(CPNetellerPay.class);
		eWallet(neteller,DEPOSITMETHOD.NETELLER);
		cpsCallback(neteller,11,2,account,amount,openapi,DEPOSITMETHOD.NETELLER);
		System.out.println("***Test Neteller deposit succeed!!********");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_Callback)
	@Parameters(value={"OpenApi"})
	public void testNetellerPayWithCallbackNew(@Optional("")String openapi) {

		DepositBase neteller = myfactor.newInstance(DepositBase.class);
		depositCommon(neteller, DEPOSITMETHOD.NETELLER);
		cpsCallback(neteller,11,2,account,amount,openapi,DEPOSITMETHOD.NETELLER);
		System.out.println("***Test Neteller deposit succeed!!********");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_Callback)
	@Parameters(value={"OpenApi"})
	public void testCryptoETHPayWithCallbackNew(@Optional("")String openapi) {

		DepositBase eth = myfactor.newInstance(DepositBase.class);
		depositCommon(eth, DEPOSITMETHOD.ETH_New);
		cpsCallback(eth,15,8,account,amount,openapi,DEPOSITMETHOD.ETH_New);
		System.out.println("***Test Crypto ETH deposit succeed!!********");
	}
	@Test(priority = 2)
	public void testAstroPay() throws InterruptedException {
		CPAstropay astropay = myfactor.newInstance(CPAstropay.class);
		this.fillDepositBasicInfo(astropay, DEPOSITMETHOD.ASTROPAY, "tab-eWallet", "eWallet", "pane-eWallet");
		// deposit and check
		astropay.deposit(account, amount, notes);
		Assert.assertTrue(astropay.checkIfNavigateToThirdUrl(amount),
				"Submit failure or error occours! Please make sure correct Astropay info configured in disconf (biz_payment.properties)");

		// check deposit history
		astropay.returnToCPFrom3rdParty();
		checkHistory(astropay,DEPOSITMETHOD.ASTROPAY,account,amount);
		System.out.println("***Test Astropay deposit succeed!!********");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_INTER_BANK_TRANS)
	public void testInterBankTransPay() throws InterruptedException {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		this.fillDepositBasicInfo(InterBankTransfer, DEPOSITMETHOD.I12BANKTRANSFER, "tab-bankTransfer", "internationalBankTransfer", "pane-bankTransfer");

		InterBankTransfer.deposit(account, amount, notes);
		InterBankTransfer.backHomeButton();

		// check deposit history
		checkHistory(InterBankTransfer,DEPOSITMETHOD.I12BANKTRANSFER,account,amount);
		System.out.println("***Test International Bank Transfer succeeded!!********");
	}

	@Test(priority = 2)
	public void testEuroSpaIBTPay() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		this.fillDepositBasicInfo(InterBankTransfer, DEPOSITMETHOD.EUROSEPAIBT, "tab-bankTransfer", "internationalBankTransfer", "pane-bankTransfer");

		InterBankTransfer.deposit(account, amount, notes);
		InterBankTransfer.backHomeButton();

		// check deposit history
		checkHistory(InterBankTransfer,DEPOSITMETHOD.EUROSEPAIBT,account,amount);
		System.out.println("***Test IBT - Euro Sepa succeeded!!********");
	}

	@Test(priority = 2)
	public void testIndiaIBTPay() {
		CPIndiaInterBankTrans InterBankTransfer = myfactor.newInstance(CPIndiaInterBankTrans.class);
		this.fillDepositBasicInfo(InterBankTransfer, DEPOSITMETHOD.INDIAIBT, "tab-bankTransfer", "internationalBankTransfer", "pane-bankTransfer");

		InterBankTransfer.checkTnc();
		for (CURRENCY currency : CURRENCY.values()) {
			account = InterBankTransfer.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		InterBankTransfer.deposit(account, amount, notes);

		String convtUsdAmt = InterBankTransfer.getConvertUSDamt();
		if (convtUsdAmt != null) {
			amount = convtUsdAmt;
		}

		InterBankTransfer.submit();
		InterBankTransfer.backHomeButton();

		// check deposit history
		checkHistory(InterBankTransfer,DEPOSITMETHOD.INDIAIBT,account,amount);
		System.out.println("***Test IBT - India succeeded!!********");
	}

	@Test(priority = 2)
	public void testEuBTPay() {
		DEPOSITMETHOD method = DEPOSITMETHOD.EUBT;
		CPEuBankTrans euBankTrans = myfactor.newInstance(CPEuBankTrans.class);
		this.fillDepositBasicInfo(euBankTrans, method, "tab-localTransfer", "localBankTransfer", "pane-localTransfer");

		euBankTrans.deposit(account, amount, notes, "", "", email);
		euBankTrans.submit();
		Assert.assertTrue(euBankTrans.checkUrlNotContains(TraderURL), "Submit failed or error occur for EU Bank Transfer");
		euBankTrans.goBack();
		driver.get(TraderURL);

		// check deposit history
		checkHistory(euBankTrans, method, account, amount);
		System.out.println("***Test EU Bank Transfer succeeded!!********");
	}

	@Test(priority = 2)
	public void testASICCCWorldpay() {
		CPLocalBankTrans asicCC = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(asicCC, DEPOSITMETHOD.ASICWORLDPAY);
	}

	@Test(priority = 2)
	public void testUAEIBTPay() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		this.fillDepositBasicInfo(InterBankTransfer, DEPOSITMETHOD.UAEINSTANT, "tab-bankTransfer", "internationalBankTransfer", "pane-bankTransfer");

		InterBankTransfer.deposit(account, amount, notes);
		InterBankTransfer.backHomeButton();

		// check deposit history
		checkHistory(InterBankTransfer,DEPOSITMETHOD.UAEINSTANT,account,amount);
		System.out.println("***Test IBT - UAE Bank Transfer succeeded!!********");
	}

	/**
	 *
	 * @param amount
	 * @return
	 */
	public void interBankTransferPay(String account,String amount) {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		this.fillDepositBasicInfo(InterBankTransfer, DEPOSITMETHOD.I12BANKTRANSFER, "tab-bankTransfer", "internationalBankTransfer", "pane-bankTransfer");
		InterBankTransfer.deposit(account, amount, "deposit for trading bonus");
		InterBankTransfer.backHomeButton();

	}

	@Test(priority = 2)
	public void testMalayBankZotaPay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPMalaysiaBankTrans malayBank = myfactor.newInstance(CPMalaysiaBankTrans.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.MALAYINSTANT);
		malayBank.secondaryButtonClick();
		malayBank.moveToNoteBox();

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = malayBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		malayBank.deposit(account, amount, notes);
		double amount1 = malayBank.getAmountInMalayUI();
		malayBank.submitButton();
		Assert.assertTrue(malayBank.checkIfNavigateToZotaURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
		Assert.assertTrue(malayBank.compareAmountWithThirdURL(amount1),
				"Malaysian Ringgit amount in Malaysia Bank Transfer UI is the same as it in the third URL");

		malayBank.goBack();
		checkHistory(malayBank,DEPOSITMETHOD.MALAYINSTANT,account,amount);
		System.out.println("***Test Malaysia Bank Transfer By ZOTAPAY Channel succeeded!!********");
	}

	/* You can not run below MalayBankPayTrust in ALPHA ENVIRONMENT */


	@Test(priority = 2)
	public void testMalayBankPayTrust() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPMalaysiaBankTrans malayBank = myfactor.newInstance(CPMalaysiaBankTrans.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.MALAYINSTANT);
		malayBank.moveToNoteBox();

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = malayBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		malayBank.deposit(account, amount, notes);
		// double amount1 = malayBank.getAmountInMalayUI();
		malayBank.submitButton();
		Assert.assertTrue(malayBank.checkIfNavigateToPayTrustURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
//		Assert.assertTrue(malayBank.compareAmountWithThirdURL(amount1),
		// "Malaysian Ringgit amount in Malaysia Bank Transfer UI is the same as it in
		// the third URL");

		malayBank.goBack();

		checkHistory(malayBank,DEPOSITMETHOD.MALAYINSTANT,account,amount);
		System.out.println("***Test Malaysia Bank Transfer By PAY TRUST Channel succeeded!!********");
	}

	/*@Test(priority = 2)
	public void testBridgePay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPBridgePayDeposit bridgePay = myfactor.newInstance(CPBridgePayDeposit.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		
		//pug has different xpath of elements
		if(GlobalProperties.brand.equalsIgnoreCase("pug"))
		{
		    pugNavigateToMethod("tab-creditCard","creditOrDebit");
		}
		else {
		    deposit_funds.navigateToMethod(DEPOSITMETHOD.CREDITORDEBIT);
		}
		
		bridgePay.moveNoteBox();
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bridgePay.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		bridgePay.deposit(account, amount, notes);
		bridgePay.submit();
		// double amount1 = bridgePay.getAmountInBridgePay();
		// System.out.println("Credit Card deposit is "+ amount);

		Assert.assertTrue(bridgePay.checkBridgePayIframe(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");

		bridgePay.goBack();
		
		checkHistory(DEPOSITMETHOD.CREDITORDEBIT,account,amount);
		System.out.println("***Test Credit Card|Bridge PAY Transfer succeeded!!********");
	}*/


	/*@Test(priority = 2)
	public void testSolidPay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;
		String cardType = "VISA";
		String ccNum = "4200000000000091";//sandbox VISA
		String ccName= "auto test";
		String cvv = "123";

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSolidPay solidPay = myfactor.newInstance(CPSolidPay.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		
		deposit_funds.navigateToMethod(DEPOSITMETHOD.CCSOLIDPAY);
		
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = solidPay.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		solidPay.setAmount(amount);
		solidPay.setCCinfo(ccNum, ccName, notes);
		solidPay.setVisaMaster(cardType);
		solidPay.setExpirationDate();
		solidPay.setCVV(cvv);

		solidPay.submit();
		Assert.assertTrue(solidPay.checkUrlContains("transactionHistory"), "Submit failed or error occur for " +DEPOSITMETHOD.CCSOLIDPAY.getWebName());

		checkHistorySuccess(DEPOSITMETHOD.CCSOLIDPAY,account,amount);
		System.out.println("***Test Credit Card|SolidPay deposit succeeded!!********");
	}*/

	//Credit Card Deposit
	public void testCreditCardDeposit(CPCreditCardDeposit creditcard, DEPOSITMETHOD method, String thirdpartyurl) {
		String cardType = "VISA";
		//String ccNum = "4200000000000091";Star sdpay sandbox VISA
		String ccNum = "5560901232771550";
		String ccName= "auto test";
		String cvv = "123";
		String paymentType = "Debit/Credit Cards";

		fillDepositBasicInfo(creditcard, method, "tab-creditCard", "creditCard", "pane-creditCard");
		creditcard.ccdeposit(account, amount, notes, ccNum, ccName,  cardType, cvv, paymentType);
		creditcard.submit();

		if (method == DEPOSITMETHOD.CREDITORDEBIT || method == DEPOSITMETHOD.CCGOOGLEPAY || method == DEPOSITMETHOD.CCBRIDGERPAY)
		{
			Assert.assertTrue(creditcard.checkThirdPartyIframe(), "Submit failure or error occours for " +method.getWebName() + " Please make sure apollo setting is correct in biz_payment");
		}
		else
		{
			Assert.assertTrue(creditcard.checkUrlNotContains(TraderURL), "Submit failed or error occur for " +method.getWebName());
			Assert.assertTrue(creditcard.checkUrlContains(thirdpartyurl), "Submit failed or error occur for " +method.getWebName());
		}
		creditcard.goBack();
		checkHistory(creditcard,method,account,amount);
		//checkHistorySuccess(DEPOSITMETHOD.CCSOLIDPAY,account,amount); - star sdpay sandbox cc use 
		System.out.println("********Test " +method.getWebName()+ " succeeded********");
	}

	@Test(priority = 2)
	public void testBridgePay() 	{
		if(Brand.equalsIgnoreCase((BRAND.UM.toString())))
		{
			CPBridgePayDeposit bridger = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDeposit(bridger, DEPOSITMETHOD.CCBRIDGERPAY, "");
		}else {
			CPBridgePayDeposit bridgepay = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDeposit(bridgepay, DEPOSITMETHOD.CREDITORDEBIT, "");
		}
	}

	@Test(priority = 2)
	public void testSDPay() 	{
		if(Brand.equalsIgnoreCase(BRAND.VJP.toString()))
		{	//no need choose cardType
			VJPCPCreditCardSDPay sdpay = myfactor.newInstance(VJPCPCreditCardSDPay.class);
			testCreditCardDeposit(sdpay, DEPOSITMETHOD.CCSDPAY,GlobalProperties.SDPayURL);
		}
		else
		{
			CPCreditCardSDPay sdpay = myfactor.newInstance(CPCreditCardSDPay.class);
			testCreditCardDeposit(sdpay, DEPOSITMETHOD.CCSDPAY,GlobalProperties.SDPayURL);
		}
	}

	@Test(priority = 2)
	public void testMyFatoorah() 	{
		if(Brand.equalsIgnoreCase(BRAND.STAR.toString()))
		{	//STAR Non-CPS
			CPCreditCardMyFatoorah noncpsmyfatoorah = myfactor.newInstance(CPCreditCardMyFatoorah.class);
			testCreditCardDeposit(noncpsmyfatoorah, DEPOSITMETHOD.CCMYFATOORAH,GlobalProperties.MyFatoorahURL);
		}
		else
		{	//PU and VT CPS
			CPCreditCardDeposit myfatoorah = myfactor.newInstance(CPCreditCardDeposit.class);
			testCreditCardDeposit(myfatoorah, DEPOSITMETHOD.CCMYFATOORAH,GlobalProperties.MyFatoorahURL);
		}
	}

	@Test(priority = 2)
	public void testCCGooglePay() 	{
		CPCreditCardGooglePay googlepay = myfactor.newInstance(CPCreditCardGooglePay.class);
		testCreditCardDeposit(googlepay, DEPOSITMETHOD.CCGOOGLEPAY,"");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CC_APPLEGOOGLEPAY)
	public void testCCGoogleApplePay() 	{
		CPCreditCardDeposit googlepay = myfactor.newInstance(CPCreditCardDeposit.class);
		testCreditCardDeposit(googlepay, DEPOSITMETHOD.CCAPPLEGOOGLEPAY,GlobalProperties.AppleGooglePayURL);
	}


	@Test(priority = 2)
	public void testMultiexc() 	{
		CPLocalBankTrans multiexc = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(multiexc,DEPOSITMETHOD.Multiexc);
	}

	@Test(priority = 2)
	public void testSolidPay() 	{
		CPCreditCardSolidPay soldipay = myfactor.newInstance(CPCreditCardSolidPay.class);
		testCreditCardDeposit(soldipay, DEPOSITMETHOD.CREDITORDEBIT,"");
	}

	//Crypto Deposit
	/*@Test(priority = 2)
	public void testNonCPSCryptoDeposit() throws InterruptedException {
		testCommonCryptoDeposit(CRYPTOBIT);
		testCommonCryptoDeposit(DEPOSITMETHOD.CRYPTOOMNI);
		testCommonCryptoDeposit(DEPOSITMETHOD.CRYPTOERC);
		testCommonCryptoDeposit(DEPOSITMETHOD.CRYPTOTRC);
	}*/

	//to easier no automation background people to know which crypto method fail/pass
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_ERC20)
	public void testDepositCryptoERC() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOERC);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_BITCOIN)
	public void testDepositCryptoBTC() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOBIT);
	}
	@Test(priority = 2)
	public void testDepositCryptoXRP() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOXRP);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_BEP20)
	public void testDepositCryptoBEP() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOBEP);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_TRC20)
	public void testDepositCryptoTRC() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOTRC);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDC_ERC20)
	public void testDepositCryptoUSDC() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.USDC);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_ETH)
	public void testDepositCryptoETH() throws InterruptedException {
		this.testCPSCryptoCommonDeposit(DEPOSITMETHOD.ETH);
	}

	@Test(priority = 2)
	public void testCPSCryptoDeposit() throws InterruptedException {
		testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOBIT);
		testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOERC);
		testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOTRC);
		if(Brand.equalsIgnoreCase(BRAND.STAR.toString()))
		{
			testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOBEP);
		}
		else if(!Brand.equalsIgnoreCase(BRAND.UM.toString()))
		{
			if (Brand.equalsIgnoreCase(BRAND.VJP.toString()))
			{
				testCPSCryptoCommonDeposit(DEPOSITMETHOD.CRYPTOXRP);
			}
			testCPSCryptoCommonDeposit(DEPOSITMETHOD.USDC);
		}
		if(!Brand.equalsIgnoreCase(BRAND.UM.toString())){
			testCPSCryptoCommonDeposit(DEPOSITMETHOD.ETH);
		}
	}

	@Test(priority = 2)
	public void testCPSCryptoCommonDeposit(DEPOSITMETHOD method) throws InterruptedException {
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPCryptoDeposit crypto = myfactor.newInstance(CPCryptoDeposit.class);
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);

		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			pugNavigateToMethod(method, crypto, "tab-cryptoCurrency", "cryptoCurrency", "pane-cryptoCurrency", false, null);
		}
		else
		{
			deposit_funds.navigateToMethod(DEPOSITMETHOD.CRYPTO);
			crypto.goToDepositMethod(method);//VFX method button on the crypto page not deposit funds page
		}

		for (CURRENCY currency : CURRENCY.values()) {
			account = crypto.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		crypto.deposit(account, amount, notes);
		crypto.goBack();
		checkHistory(crypto,method,account,amount);
		System.out.println("***Test "+method.getWebName()+" deposit succeed!!********");

	}
	/*public void testCommonCryptoDeposit(DEPOSITMETHOD method) {
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPCryptoDeposit crypto = myfactor.newInstance(CPCryptoDeposit.class);
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		
		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;
		
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		if(Brand.equalsIgnoreCase("pug")) {
			deposit_funds.navigateToMethod(method);
		}else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.CRYPTO);
			crypto.goToDepositMethod(method);//VFX method button on the crypto page not deposit funds page
		}
		
		for (CURRENCY currency : CURRENCY.values()) {
			account = crypto.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		crypto.deposit(account, amount, notes);
		
		Assert.assertTrue(crypto.checkUrlNotContains(TraderURL), "Submit failed or error occur for " +method.getWebName());
		
		//Assert.assertTrue(crypto.checkIfNavigateToThirdUrl(),
		//		"Submit failure or error occours! Please make sure correct"+ method.getWebName()+  "info configured in disconf (biz_payment.properties)");

		//crypto.getAmountFromThirdParty(method, amount);
		crypto.goBack();
		checkHistory(crypto,method,account,amount);
		System.out.println("***Test "+method.getWebName()+" deposit succeed!!********");

	}*/

	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testFasaPay(@Optional("")String openApi) throws InterruptedException {
		CPFasaPay fasapay = myfactor.newInstance(CPFasaPay.class);
		this.fillDepositBasicInfo(fasapay, DEPOSITMETHOD.FASAPAY, "tab-eWallet", "eWallet", "pane-eWallet");

		fasapay.deposit(account, amount, notes);
		Assert.assertTrue(fasapay.checkIfNavigateToThirdUrl(amount),
				"Submit failure or error occours! Please make sure correct Fasapay info configured in disconf (biz_payment.properties)");

		// check deposit history
		//TestResultPage result = PageFactory.initElements(driver, TestResultPage.class);
		//System.out.println(result.getResult());
		fasapay.goBack();
		checkHistory(fasapay,DEPOSITMETHOD.FASAPAY,account,amount);

		//JSONObject obj = fasapay.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 7, 1);
		//fasapay cps change to channel 2
		JSONObject obj = fasapay.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 7, 2);

		if(dbenv.equals(ENV.PROD)) {
			System.out.println(obj.toJSONString());
			Assert.assertTrue(obj.getInteger("status").equals(2),"submit to third part failed.");
		}

		if(!obj.getInteger("status").equals(2))
		{
			Assert.assertTrue(fasapay.updateStatus(dbenv, dbBrand, dbRegulator, obj.getInteger("id"), 2),"update deposit status failed");
		}

		//callback
		if(!"".equals(openApi)) {
			DepositCallBack callback = new DepositCallBack(openApi,Brand,Regulator);
			callback.generateCallback(CHANNEL.FASAPAY,obj.getString("order_number"),obj.getString("currency"),obj.getDouble("deposit_amount"),"","","","");
			String response = callback.sendCallback().toLowerCase();
			if(response.contains("ok") || response.contains("success")) {
				checkHistory(fasapay,DEPOSITMETHOD.FASAPAY,account,amount);
				System.out.println("***Test FasaPay callback succeed!!********");
			}else {
				System.out.println("***Test FasaPay deposit Failed!!********");
			}
		}

		System.out.println("***Test FasaPay deposit succeed!!********");
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_Callback)

	public void BrigePayCPSCallback(DepositBase deposit,String openApi,int channelType,int channelId) {
		JSONObject obj = deposit.getDepositRecord(dbenv, dbBrand, dbRegulator, account, channelType, channelId);

		if(dbenv.equals(ENV.PROD)) {
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
			callback.generateCallback(CHANNEL.BRIDGE_CARDPAY,obj.getString("order_number"),obj.getString("currency"),obj.getDouble("deposit_amount"),"666666","666666","2045","12");
			String response = callback.sendCallback().toLowerCase();
			if(response.contains("ok") || response.contains("success")) {
				checkHistorySuccess(DEPOSITMETHOD.CREDITORDEBIT_New,account,amount);
				System.out.println("***Test BrigePay callback succeed!!********");
			}else {
				System.out.println("***Test BrigePay callback deposit Failed!!********");
			}
		}
	}

	@Test(priority = 2)
	public void testBrokerTransferPay() throws InterruptedException {
		CPBrokerTransfer BrokerTransfer = myfactor.newInstance(CPBrokerTransfer.class);
		this.fillDepositBasicInfo(BrokerTransfer, DEPOSITMETHOD.B2B, "tab-eWallet", "eWallet", "pane-eWallet");

		BrokerTransfer.deposit(account, amount, notes);

		BrokerTransfer.backHomeButton();

		// check deposit history
		checkHistory(BrokerTransfer,DEPOSITMETHOD.B2B,account,amount);
		System.out.println("***Test Broker to Broker Transfer succeeded!!********");
	}

	@Test(priority = 2)
	public void testThaiBankZotapay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPThaiBankTransfer thaiBank = myfactor.newInstance(CPThaiBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.THAIINSTANT);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String url = js.executeScript("return document.URL").toString();
		System.out.println(url);
		int index = url.lastIndexOf("/");
		String lastString = url.substring(index + 1);
		System.out.println(lastString);
		String zotaURL = url.replace(lastString, "thailandzotapay");
		System.out.println(zotaURL);
		driver.get(zotaURL);

		for (CURRENCY currency : CURRENCY.values()) {
			account = thaiBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check

		thaiBank.deposit(account, amount, notes);
		int amount1 = (int) thaiBank.getAmountInThaiBankUI();
		thaiBank.submit();

		Assert.assertTrue(thaiBank.checkIfNavigateToZotaURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
		Assert.assertTrue(thaiBank.compareAmountWithZotapay(amount1),
				"Thai THB amount in THAI Bank Transfer UI is the same as it in the third URL");

		thaiBank.goback();
		checkHistory(thaiBank,DEPOSITMETHOD.THAIINSTANT,account,amount);
		System.out.println("***Test THAI Bank Transfer By ZOTAPAY Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testThaiBankPayToDAY() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPThaiBankTransfer thaiBank = myfactor.newInstance(CPThaiBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.THAIINSTANT);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String url = js.executeScript("return document.URL").toString();
		System.out.println(url);
		int index = url.lastIndexOf("/");
		String lastString = url.substring(index + 1);
		System.out.println(lastString);
		String payToDayURL = url.replace(lastString, "thailandPayToDay");
		System.out.println(payToDayURL);
		driver.get(payToDayURL);

		for (CURRENCY currency : CURRENCY.values()) {
			account = thaiBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check

		thaiBank.deposit(account, amount, notes);
		int amount1 = (int) thaiBank.getAmountInThaiBankUI();
		thaiBank.submit();
		Assert.assertTrue(thaiBank.checkIfNavigateToThaiPayToDayURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
		Assert.assertTrue(thaiBank.compareAmountWithPayToDAY(amount1),
				"Thai THB amount in THAI Bank Transfer UI is the same as it in the third URL");

		thaiBank.goback();

		checkHistory(thaiBank,DEPOSITMETHOD.THAIINSTANT,account,amount);
		System.out.println("***Test THAI Bank Transfer By PAYTODAY Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testThaiBankPA() throws InterruptedException {
		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPThaiBankTransfer thaiBank = myfactor.newInstance(CPThaiBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.THAIINSTANT);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String url = js.executeScript("return document.URL").toString();
		System.out.println(url);
		int index = url.lastIndexOf("/");
		String lastString = url.substring(index + 1);
		System.out.println(lastString);
		String PAURL = url.replace(lastString, "mijiPay");
		System.out.println(PAURL);
		driver.get(PAURL);

		for (CURRENCY currency : CURRENCY.values()) {
			account = thaiBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check

		thaiBank.deposit(account, amount, notes);
		int amount1 = (int) thaiBank.getAmountInThaiBankUI();
		thaiBank.submit();
		Assert.assertTrue(thaiBank.checkIfNavigateToThaiPAURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
		Assert.assertTrue(thaiBank.compareAmountWithPA(amount1),
				"Thai THB amount in THAI Bank Transfer UI is the same as it in the third URL");

		thaiBank.goback();

		checkHistory(thaiBank,DEPOSITMETHOD.THAIINSTANT,account,amount);

		System.out.println("***Test THAI Bank Transfer By PA Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testThaiBankEeziePay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPThaiBankTransfer thaiBank = myfactor.newInstance(CPThaiBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.THAIINSTANT);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String url = js.executeScript("return document.URL").toString();
		System.out.println(url);
		int index = url.lastIndexOf("/");
		String lastString = url.substring(index + 1);
		System.out.println(lastString);
		String eeziePayURL = url.replace(lastString, "thailandEeziePay");
		System.out.println(eeziePayURL);
		driver.get(eeziePayURL);

		for (CURRENCY currency : CURRENCY.values()) {
			account = thaiBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check

		thaiBank.deposit(account, amount, notes);
		int amount1 = (int) thaiBank.getAmountInThaiBankUI();
		thaiBank.submit();
		Assert.assertTrue(thaiBank.checkIfNavigateToThaiEeziePayURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
		Assert.assertTrue(thaiBank.compareAmountWithEeziePay(amount1),
				"Thai THB amount in THAI Bank Transfer UI is the same as it in the third URL");

		thaiBank.goback();
		checkHistory(thaiBank,DEPOSITMETHOD.THAIINSTANT,account,amount);
		System.out.println("***Test THAI Bank Transfer By EEZIEPAY Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testThaiBankXpay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPThaiBankTransfer thaiBank = myfactor.newInstance(CPThaiBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.THAIINSTANT);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String url = js.executeScript("return document.URL").toString();
		System.out.println(url);
		int index = url.lastIndexOf("/");
		String lastString = url.substring(index + 1);
		System.out.println(lastString);
		String XPayURL = url.replace(lastString, "thailandXPay");
		System.out.println(XPayURL);
		driver.get(XPayURL);

		for (CURRENCY currency : CURRENCY.values()) {
			account = thaiBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check

		thaiBank.deposit(account, amount, notes);
		int amount1 = (int) thaiBank.getAmountInThaiBankUI();
		thaiBank.submit();

		Assert.assertTrue(thaiBank.checkIfNavigateToThaiXPayURL(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)");
		Assert.assertTrue(thaiBank.compareAmountWithXPay(amount1),
				"Thai THB amount in THAI Bank Transfer UI is the same as it in the third URL");

		thaiBank.goback();

		checkHistory(thaiBank,DEPOSITMETHOD.THAIINSTANT,account,amount);
		System.out.println("***Test THAI Bank Transfer By XPAY Channel succeeded!!********");
		System.out.println(
				"***Congratulations!!!! ALL 5 Channels in Thai Bank Automatically ran successfully !!********");
	}

	//some deposit method amount need to greater than 200
	private String getRandomDepositAmount() {
		return String.valueOf((int) (200 + Math.random() * 100));
	}

	private String getRandomDepositAmount(DEPOSITMETHOD method) {
		if(Brand.equalsIgnoreCase(BRAND.STAR.toString()))
		{
			switch(method)
			{
				case I12BANKTRANSFER:
					return String.valueOf((int) (500 + Math.random() * 100));
				case UNIONPAY:
					return String.valueOf((int) (450 + Math.random() * 100));
				case INDIAIBT:
					//Need more than 4500 INR
					return String.valueOf((int) (4500 + Math.random() * 100));
				case UNIONPAY_DynamicPay:
				case UNIONPAY_Chinapay:
				case UNIONPAY_Mpay:
					// Minimum amount requires to unlock payment channel
					return String.valueOf((int) (500 + Math.random() * 100));
				case UNIONPAY_Toppay2:
				case UNIONPAY_uEnjoy:
					// Minimum amount requires to unlock payment channel
					return String.valueOf((int) (1000 + Math.random() * 100));
				case UNIONPAY_Teleport:
					// Minimum amount requires to unlock payment channel
					return String.valueOf((int) (1500 + Math.random() * 100));
			}

		}
		else if (Brand.equalsIgnoreCase(BRAND.VT.toString()) || Brand.equalsIgnoreCase(BRAND.UM.toString()))
		{
			switch(method)
			{
				case UNIONPAY:
					return String.valueOf((int) (1400 + Math.random() * 100));
				case INDIAIBT:
					//UM Need more than 3000 INR
					return String.valueOf((int) (3000 + Math.random() * 100));
				case ARSBT_4ON_RapiPago:
					//Control amount to unlock payment channel (less than 250)
					return String.valueOf((int) (150 + Math.random() * 100));
				case UNIONPAY_Global_Minority:
				case UNIONPAY_ChipPay:
					// Minimum amount requires to unlock payment channel
					return String.valueOf((int) (500 + Math.random() * 100));
			}
		}
		else if (Brand.equalsIgnoreCase(BRAND.MO.toString()))
		{
			switch(method)
			{
				// Minimum amount requires to unlock payment channel
				case UNIONPAY_Bitpay:
				case UNIONPAY_Teleport:
				case UNIONPAY_ChipPay:
				case UNIONPAY_Chinapay:
				case UNIONPAY_Global_Small:
				case UNIONPAY_Global_Medium:
				case UNIONPAY_Global_Large:
				case UNIONPAY_Global_VIP:
				case UNIONPAY_Global_Minority:
					return String.valueOf((int) (1500 + Math.random() * 100));
			}
		}
		else if (Brand.equalsIgnoreCase(BRAND.VFX.toString()))
		{
			switch(method)
			{
				//Cannot over 200 USD
				case BDTBkash:
				case BDTNagad:
				case BDTRocket:
				case ARSBT_4ON_PagoFacil:
				case ARSBT_4ON_MercadoPagoWallet:
				case ARSBT_4ON_RapiPago:
					return String.valueOf((int) (50 + Math.random() * 100));
				case AUBANKTRANSFER_New:
				case B2B_New:
				case BPay:
				case ASIC_CC_Checkout:
					return String.valueOf((int) (50 + Math.random()));
				case INDIAIBT:
					//Need more than 3000 INR
					return String.valueOf((int) (3000 + Math.random() * 100));
			}
		}
		return String.valueOf((int) (200 + Math.random() * 100));
	}

	private void checkHistory(DepositBase deposit,DEPOSITMETHOD method, String account, String amount) {
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		System.out.println("***Check Transaction History***");
		if(GlobalProperties.isWeb)
			menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		else
			menu.goToMenu(CPMenuName.TRANSACTIONHISTORYMOBILE);
		getDepositInfo(deposit,account);
		boolean isFound;
		if(Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("um")){
			isFound=history.checkDeposit(account,method, amount, STATUS.INCOMPLETE);
		}else {
			isFound=history.checkDeposit(account,method, amount, STATUS.PROCESSING);
		}
		Assert.assertTrue(isFound, "Did not Find the deposit in history page");
	}
	private void checkHistoryWithoutDepositinfo(DepositBase deposit,DEPOSITMETHOD method, String account, String amount) {
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		System.out.println("***Check Transaction History***");
		if(GlobalProperties.isWeb)
			menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		else
			menu.goToMenu(CPMenuName.TRANSACTIONHISTORYMOBILE);
		//getDepositInfo(deposit,account);
		Assert.assertTrue(
				history.checkDeposit(account,method, amount, STATUS.INCOMPLETE),
				"Did not Find the deposit in history page");
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


	private void checkHistorySuccess(DEPOSITMETHOD method, String account, String amount) {
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		boolean isFound =history.checkDeposit(account,method, amount, STATUS.SUCCESSFUL);
		CustomAssert.assertTrue(isFound, "Did not Find the deposit in history page");
		CustomAssert.assertAll();

	}

	private void unionpaytest(DEPOSITMETHOD method,String checkURL) {
		CPUnionPay unionpay = myfactor.newInstance(CPUnionPay.class);
		this.fillDepositBasicInfo(unionpay, method, "tab-eWallet", "eWallet", "pane-eWallet");

		// deposit and check
		unionpay.deposit(account, amount, notes);
		assertTrue(unionpay.checkIfNavigateToThirdUrl(checkURL,TraderURL),
				"Submit failure or error occours! Please make sure correct "+method.toString()+" info configured in disconf (biz_payment.properties)");

		// check deposit history
		unionpay.goBack();
		checkHistory(unionpay,method,account,amount);
		System.out.println("***Test unionpay deposit succeed!!********");
	}

	//For UM
	private void mobilepaytest_callback(DEPOSITMETHOD method,String checkURL, String openApi) {
		CPUnionPay unionpay = myfactor.newInstance(CPUnionPay.class);
		this.fillDepositBasicInfo(unionpay, method, "tab-eWallet", "eWallet", "pane-eWallet");

		// deposit and check
		unionpay.deposit(account, amount, notes);
		Assert.assertTrue(unionpay.checkIfNavigateToThirdUrl(checkURL,TraderURL),
				"Submit failure or error occours! Please make sure correct "+method.toString()+" info configured in disconf (biz_payment.properties)");

		//Send callback 9,2 - MobilePay-CPS
		JSONObject obj = unionpay.getDepositRecord(dbenv, dbBrand, dbRegulator, account, 9, 2);

		DepositCallBack callback = new DepositCallBack(openApi,Brand,Regulator);
		callback.generateCallback(CHANNEL.MOBILECPS,obj.getString("order_number"),obj.getString("currency"),obj.getDouble("deposit_amount"),"","","","");
		String response = callback.sendCallback().toLowerCase();


		Assert.assertTrue(response.contains("ok") || response.contains("success"),
				"*****Test MobilePay callback Failed!!*******");


		// check deposit history
		unionpay.goBack();
		checkHistorySuccess(method,account,amount);
		System.out.println("***Test MobilePay deposit succeed!!********");
	}
	
	/*@Test(priority = 2)
	public void testCreditCardSDPay() {
		CPCreditCardSDPay sdpay = myfactor.newInstance(CPCreditCardSDPay.class);
		this.fillDepositBasicInfo(sdpay, DEPOSITMETHOD.CREDITORDEBIT);
		String ccNum = "5555341244441115";
		String ccName= "auto test";
		String cvv = "123";
		sdpay.selectAccount(account);
		sdpay.setAmount(amount);
		sdpay.setCCinfo(ccNum, ccName);
		Assert.assertNotNull(sdpay.setExpirationDate(),"Failed: select expiration date failed!");
		sdpay.setCVV(cvv);
		sdpay.submit();
		
		//Stay at 3rdparty only 5sec and redirect from 3rdparty to CP
		//Assert.assertTrue(sdpay.checkIfNavigateToThirdUrl(),
				"Submit failure or error occours! Please make sure correct method info configured in disconf (biz_payment.properties)"); 
		checkHistory(DEPOSITMETHOD.CREDITORDEBIT,account,amount);
		System.out.println("***Test credit card sdpay succeeded!!********");
	}*/


	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testIndiaUPI() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		if(Integer.parseInt(amount) > 600)
		{
			amount = String.valueOf(Integer.parseInt(amount) - 500);
		}

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndiaUPI iUPI = myfactor.newInstance(CPIndiaUPI.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.IndiaUPI);

		for (CURRENCY currency : CURRENCY.values()) {
			account = iUPI.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		iUPI.deposit(account, amount, notes);
		iUPI.submit();
		iUPI.goBack();

		checkHistory(iUPI,DEPOSITMETHOD.IndiaUPI,account,amount);
		System.out.println("***Test Inida UPI By wallet Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testSKBankTransfer() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);

		//pug has different xpath of elements
		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			pugNavigateToMethod("tab-localTransfer","southkoreabanktransfer");
		}
		else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.SKBANKTRANSFER);
		}
		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();
		skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.SKBANKTRANSFER,account,amount);

		System.out.println("***Test South Korea Bank Transfer By Dollarsmart Channel succeeded!!********");
	}
	@Test(priority = 2)
	public void testMYBankTransfer() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			pugNavigateToMethod("tab-localTransfer","malaysiainstantbanktransfer");
		}
		else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.MALAYINSTANT);
		}
		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();
		skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.MALAYINSTANT,account,amount);
		System.out.println("***Test Malaysia Bank Transfer By Eeziepay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testMYBankTransferXpay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);

		if (Brand.equalsIgnoreCase(BRAND.PUG.toString())) {
			WebElement methoddiv=
					driver.findElement(By.xpath("//div[@id='tab-localTransfer']"));
			methoddiv.click();

			WebElement depositMethod= driver.findElement(By.xpath("(//li[@data-testid='malaysiainstantbanktransfer'])[2]/div[1]"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", depositMethod);
		} else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.MALAYINSTANT);
		}


		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();
		skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.MALAYINSTANT, account, amount);
		System.out.println("***Test Malaysia Bank Transfer By Xpay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testMYBankTransferZotapay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		if (Brand.equalsIgnoreCase(BRAND.PUG.toString())) {
			WebElement methoddiv=
					driver.findElement(By.xpath("//div[@id='tab-localTransfer']"));
			methoddiv.click();

			WebElement depositMethod= driver.findElement(By.xpath("(//li[@data-testid='malaysiainstantbanktransfer'])[3]/div[1]"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", depositMethod);
		} else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.MALAYINSTANT);
		}
		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();
		skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.MALAYINSTANT, account, amount);
		System.out.println("***Test Malaysia Bank Transfer By zotapay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testMYEwallet() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.MalaysiaEWallet);

		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();
		skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.MalaysiaEWallet,account,amount);
		System.out.println("***Test Malaysia Bank Transfer By Eeziepay Channel succeeded!!********");
	}

	@Test(priority = 2)
	@Parameters(value={"OpenApi"})
	public void testIndiaBankTransferWithCallback(@Optional("")String openApi) throws InterruptedException {

		String amount = getRandomDepositAmount();

		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.INDIAIAINSTANT);

		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();
		skBank.checkUrlContains("www.jojostreet616.com");
		skBank.goBack();


		checkHistory(skBank,DEPOSITMETHOD.INDIAIAINSTANT,account,amount);

		cpsCallback(skBank,24,9,account,amount,openApi,DEPOSITMETHOD.INDIAIAINSTANT);

		System.out.println("***Test India instant Bank Transfer By Eeziepay Channel succeeded!!********");
	}


	@Test(priority = 2)
	public void testIndonesiaBankTransferEeziepay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.INDONESIAINSTANT);

		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.INDONESIAINSTANT,account,amount);
		System.out.println("***Test Indonesia instant Bank Transfer By Eeziepay Channel succeeded!!********");
	}
	@Test(priority = 2)
	public void testIndonesiaBankTransferTransact365() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		if(Brand.equalsIgnoreCase(BRAND.STAR.toString())) {
			driver.navigate().refresh();
		}
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.INDONESIAINSTANT);

		if(!Brand.equalsIgnoreCase(BRAND.STAR.toString())) {
			indoBank.selectBankTrasfer("Secondary");
		}
		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.setBank();
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.INDONESIAINSTANT,account,amount);
		System.out.println("***Test Indonesia instant Bank Transfer By Transact365 Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testIndonesiaBankTransferZotapay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.INDONESIAINSTANT);

		if(Brand.equalsIgnoreCase(BRAND.VFX.toString())) {
			indoBank.selectBankTrasfer("Quaternary");
		}else {
			indoBank.selectBankTrasfer("Tertiary");
		}

		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.INDONESIAINSTANT,account,amount);
		System.out.println("***Test Indonesia instant Bank Transfer By Zotapay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testIndonesiaBankTransferPaymentAsia() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.INDONESIAINSTANT);

		indoBank.selectBankTrasfer("Quaternary");

		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.INDONESIAINSTANT,account,amount);
		System.out.println("***Test Indonesia instant Bank Transfer By PaymentAsia Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testVietnamBankTransferEeziepay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.VIETNAMINSTANT);

		try{
			indoBank.selectBankTrasfer("Primary");
		}
		catch (Exception e)
		{
			GlobalMethods.printDebugInfo("Only one channel for Vietnam Bank Transfer");
		}

		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.VIETNAMINSTANT,account,amount);
		System.out.println("***Test Vietnam instant Bank Transfer By Eeziepay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testVietnamBankTransferZotapay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.VIETNAMINSTANT);

		indoBank.selectBankTrasfer("Secondary");

		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.VIETNAMINSTANT,account,amount);
		System.out.println("***Test Vietnam instant Bank Transfer By Zotapay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testVietnamBankTransferWalaopay() throws InterruptedException {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPIndonesiaBankTransfer indoBank = myfactor.newInstance(CPIndonesiaBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.VIETNAMINSTANT);

		indoBank.selectBankTrasfer("Tertiary");

		for (CURRENCY currency : CURRENCY.values()) {
			account = indoBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		indoBank.deposit(account, amount, notes);
		indoBank.submit();
		indoBank.goBack();

		checkHistory(indoBank,DEPOSITMETHOD.VIETNAMINSTANT,account,amount);
		System.out.println("***Test Vietnam instant Bank Transfer By Walaopay Channel succeeded!!********");
	}
	@Test(priority = 2)
	public void testSKEbuyPay() throws InterruptedException {
		CPEbuyDeposit eBuy = myfactor.newInstance(CPEbuyDeposit.class);
		eWallet(eBuy,DEPOSITMETHOD.EBuyTRANSFER);
	}


	@Test(priority = 2)
	public void testAdvcash() {
		CPFasaPay advcash = myfactor.newInstance(CPFasaPay.class);
		eWallet(advcash,DEPOSITMETHOD.ADVCASH);
	}

	@Test(priority = 2)
	public void testUnionPay() {
		unionpaytest(DEPOSITMETHOD.UNIONPAY,GlobalProperties.UNIONPAYURL);
	}

	@Test(priority = 2)
	public void testChinaUnion() {
		unionpaytest(DEPOSITMETHOD.CHINAUNION,GlobalProperties.UNIONPAYURL);
	}

	@Test(priority = 2)
	public void testMobilePay() {
		unionpaytest(DEPOSITMETHOD.MOBILE,GlobalProperties.MOBILEPAYURL);
	}

	@Test(priority = 2)
	@Parameters(value= {"OpenAPIURL"})
	public void testMobilePayCallback(String OpenAPIURL) {
		mobilepaytest_callback(DEPOSITMETHOD.MOBILE,GlobalProperties.MOBILEPAYURL,OpenAPIURL);
	}

	public void pugNavigateToMethod(String divid, String methodDatatestid)
	{
		WebElement methoddiv = driver.findElement(By.xpath("//div[@id='"+divid+"']"));
		methoddiv.click();

		WebElement depositMethod= driver.findElement(By.xpath("//li[@data-testid='"+methodDatatestid+"']/div"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", depositMethod);
	}

	public void pugNavigateToMethod(DEPOSITMETHOD method, DepositBase deposit, String sDivTabId, String sImgTabId, String sDivPaneId, Boolean bIsCpsBTDep, String[] arrResp) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		if (sImgTabId.equalsIgnoreCase("cryptoCurrency"))
		{
			WebElement methodName = driver.findElement(By.xpath("//div[@data-testid='" + method.getCPTestId() + "']"));
			methodName.click();
			/*Map<String, String> methodMap = new HashMap<>();
			methodMap.put("cryptotrc", "//div[@data-testid='usdttrc20']");
			methodMap.put("eth", "//div[@data-testid='eth']");
			methodMap.put("cryptoerc", "//div[@data-testid='usdterc20']");
			methodMap.put("bitcoin", "//div[@data-testid='bitcoin']");
			methodMap.put("usdc", "//div[@data-testid='usdcerc20']");

			// Find the matching key
			for (Map.Entry<String, String> entry : methodMap.entrySet()) {
				if (StringUtils.containsIgnoreCase(method.toString().toLowerCase(), entry.getKey())) {
					WebElement methodName = driver.findElement(By.xpath(entry.getValue()));
					methodName.click();
					return;
				}
			}*/
		}
		else{
			String sTabPath = "//div[@data-testid='" + sImgTabId + "']";
			js.executeScript("arguments[0].click()", driver.findElement(By.xpath(sTabPath)));

			WebElement methodName = driver.findElement(By.xpath("//div[@data-testid='" + method.getCPTestId() + "']"));
			methodName.click();
		}

	}

	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testPerfectMoney(@Optional("")String openApi) throws InterruptedException {
		CPFasaPay fasapay = myfactor.newInstance(CPFasaPay.class);
		eWallet(fasapay,DEPOSITMETHOD.PERFECTMONEY);
	}

	@Test(priority = 2)
	public void testSouthAfricaBTzotapay() {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);

		//pug has different xpath of elements
		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			//pugNavigateToMethod("tab-localTransfer","southAfrica");
			WebElement methoddiv= driver.findElement(By.xpath("//div[@id='tab-localTransfer']"));
			methoddiv.click();

			WebElement depositMethod= driver.findElement(By.xpath("(//li[@data-testid='southafricainstantbanktransfer'])[2]/div[1]"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", depositMethod);
		}
		else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.SOUTHAFRICAINSTANT);
		}
		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();

		Assert.assertTrue(skBank.checkUrlContains("secure.clients.fund"),
				"Submit failure or error occours! Please make sure correct South Africa Bank Transfer info configured in disconf (biz_payment.properties)");


		skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.SOUTHAFRICAINSTANT,account,amount);
		System.out.println("***Test South Africa Bank Transfer By zotapay Channel succeeded!!********");
	}

	@Test(priority = 2)
	public void testSouthAfricaBTWebpayment() {

		String amount = getRandomDepositAmount();
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPSKBankTransfer skBank = myfactor.newInstance(CPSKBankTransfer.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);

		//pug has different xpath of elements
		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			pugNavigateToMethod("tab-localTransfer","southafricainstantbanktransfer");
//			WebElement methoddiv=
//					driver.findElement(By.xpath("//div[@id='tab-localTransfer']"));
//			methoddiv.click();
//
//			WebElement depositMethod= driver.findElement(By.xpath("(//li[@data-testid='southafricainstantbanktransfer'])[1]/div[1]"));
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("arguments[0].click()", depositMethod);
		}
		else {
			deposit_funds.navigateToMethod(DEPOSITMETHOD.SOUTHAFRICAINSTANT);
		}
		for (CURRENCY currency : CURRENCY.values()) {
			account = skBank.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		skBank.deposit(account, amount, notes);
		skBank.submit();

		Assert.assertTrue(skBank.checkUrlContains("secure.stitch.money"),
				"Submit failure or error occours! Please make sure correct South Africa Bank Transfer info configured in disconf (biz_payment.properties)");

		login = myfactor.newInstance(CPLogin.class,TraderURL);
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		//skBank.refresh();
		//skBank.goBack();

		checkHistory(skBank,DEPOSITMETHOD.SOUTHAFRICAINSTANT,account,amount);
		System.out.println("***Test South Africa Bank Transfer By Webpayment Channel succeeded!!********");
	}

	public void eWallet(DepositBase walletPay,DEPOSITMETHOD depositMethod) {

		this.fillDepositBasicInfo(walletPay, depositMethod, "tab-eWallet", "eWallet", "pane-eWallet");
		walletPay.deposit(account, amount, notes);

		Assert.assertTrue(walletPay.checkUrlNotContains(TraderURL), "Submit failed or error occur for " + depositMethod.getWebName());
		walletPay.goBack();

		// check deposit history
		checkHistory(walletPay,depositMethod,account,amount);

		System.out.println("***Test " + depositMethod.getWebName() + " deposit succeed!!********");
	}

	public void cpsCallback(DepositBase deposit,int depositType, int channel,String account, String amount,String openApi,DEPOSITMETHOD method)
	{
		JSONObject obj = deposit.getDepositRecord(dbenv, dbBrand, dbRegulator, account, depositType, channel);
		LogUtils.info("Deposit record: " + obj.toJSONString());

		if(dbenv.equals(ENV.PROD)) {
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
			callback.generateCallback(CHANNEL.CPSCallback,obj.getString("order_number"),obj.getString("currency"),obj.getDouble("deposit_amount"),"","","","");
			String response = callback.sendCallback().toLowerCase();

			if(response.contains("ok") || response.contains("success")) {
				String crruntUrl = deposit.getCurrentUrl();
				long start = System.currentTimeMillis();
				while (!crruntUrl.contains(TraderURL) && System.currentTimeMillis() - start < 30000) {
					deposit.goBack();
					try{Thread.sleep(500);}catch(Exception e){}
					crruntUrl = deposit.getCurrentUrl();
				}

				// 添加30秒内重试查询接口
				JSONObject depositRecords = null;
				long startTime = System.currentTimeMillis();
				long maxWaitTime = 30000; // 30秒最大等待时间

				while (System.currentTimeMillis() - startTime < maxWaitTime) {
					try {
						depositRecords = adminPaymentAPI.apiDPAuditSearch(account,"5");

						// 如果成功获取到数据且有记录，则跳出循环
						if (depositRecords != null && !depositRecords.getJSONArray("rows").isEmpty()) {
							break;
						}

						// 等待一段时间后重试
						Thread.sleep(2000); // 每2秒重试一次

					} catch (Exception e) {
						LogUtils.error("Error calling apiDPAuditSearch: " + e.getMessage(),e);
						try {
							Thread.sleep(2000); // 出现异常也等待2秒后重试
						} catch (InterruptedException ie) {
							Thread.currentThread().interrupt();
							break;
						}
					}
				}


				CustomAssert.assertNotNull(depositRecords, "Deposit record is not Succeed,Please Check");

				if(!depositRecords.getJSONArray("rows").isEmpty())
				{
					JSONObject depositRecord = depositRecords.getJSONArray("rows").getJSONObject(0);
					CustomAssert.assertNotNull(depositRecord, "Deposit record is not Succeed,Please Check");
					CustomAssert.assertAll();
				}
				checkHistorySuccess(method,account,amount);
				System.out.println("***Test callback succeed!!********");
			}else {
				System.out.println("***Test deposit Failed!!********");
				Assert.fail("call back failed");
			}
		}
	}

	//Local Bank Transfer
	public void depSubmitAndCheck(CPLocalBankTrans bank_d, DEPOSITMETHOD method, String account, String amount, String notes, String taxID, String cardnum)
	{
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_d.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		bank_d.deposit(account, amount, notes, taxID, cardnum, email);
		bank_d.submit();
		Assert.assertTrue(bank_d.checkUrlNotContains(TraderURL), "Submit failed or error occur for " +method.getWithdrawName());
		if(method == DEPOSITMETHOD.JAPANIAINSTANT || method == DEPOSITMETHOD.JAPANEWALLET)
		{
			driver.navigate().refresh();
			bank_d.goBack();
			bank_d.goBack();
		}
		bank_d.goBack();
		if(method == DEPOSITMETHOD.BANXA|| method == DEPOSITMETHOD.EUBT || method == DEPOSITMETHOD.Multiexc)
		{
			driver.get(TraderURL);
		}
		checkHistory(bank_d,method,account,amount);
		System.out.println("********Test " +method.getWithdrawName()+ " succeeded********");
	}

	public String getRandomDepositAmountbyDepMethod(DEPOSITMETHOD method)
	{
		if (method == DEPOSITMETHOD.JAPANIAINSTANT) {
			//japan need more than 350
			return String.valueOf((int) (350 + Math.random() * 100));
		}else if (method == DEPOSITMETHOD.TANZANIAINSTANT) {
			//tanzania need less than 200
			return String.valueOf((int) (100 + Math.random() * 100));
		}else if (method == DEPOSITMETHOD.UGANDAINSTANT) {
			//Uganda need between 50 and 100
			return String.valueOf((int) (50 + Math.min(0.49, Math.random()) * 100));
		}else if (method == DEPOSITMETHOD.NIGERIAINSTANT) {
			//VFX Nigeria cannot over 250000 NGN
			return String.valueOf((int) (50 + Math.random() * 100));
		}else {
			return getRandomDepositAmount();
		}
	}

	public void CpsBankTransferDeposit(CPLocalBankTrans bank_d, DEPOSITMETHOD method)
	{
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		String amount = getRandomDepositAmountbyDepMethod(method);
		String taxID = "50284414727";
		String cardnum = "235296825";
		String notes = "auto test";
		String account = null;

		if(Brand.equalsIgnoreCase(BRAND.STAR.toString()) && method == DEPOSITMETHOD.INDONESIAINSTANT)
		{
			driver.navigate().refresh();
		}
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		menu.changeLanguage("English");

		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			pugNavigateToMethod(method, bank_d, "tab-localTransfer", "localBankTransfer", "pane-localTransfer", true, null);
			depSubmitAndCheck(bank_d, method ,account, amount, notes, taxID, cardnum);
			/*String[] arrResp = new String[3];

			while ((arrResp[0] == null || arrResp[0].isEmpty()) || (Integer.parseInt(arrResp[0]) > 1 && arrResp[1] != null && Integer.parseInt(arrResp[1]) <= Integer.parseInt(arrResp[0]))) {

				pugNavigateToMethod(method, bank_d, "tab-localTransfer", "localBankTransfer", "pane-localTransfer", true, arrResp);
				depSubmitAndCheck(bank_d, method ,account, amount, notes, taxID, cardnum);

				if (Integer.parseInt(arrResp[0]) > 1) {
					int iChannelSeq = Integer.parseInt(arrResp[1]);
					arrResp[1] = String.valueOf(iChannelSeq + 1);

					menu.goToMenu(CPMenuName.CPPORTAL);
					menu.goToMenu(CPMenuName.DEPOSITFUNDS);

					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click()", driver.findElement(By.xpath(arrResp[2])));
				}
			}*/
		}
		else
		{
			deposit_funds.navigateToMethod(method);
			String channelpath = "//div[@class='info_box_choose']/button";
			Integer channellistsize = driver.findElements(By.xpath(channelpath)).size();

			if (channellistsize > 0 && ! (dbenv.toString().equalsIgnoreCase("PROD") && method == DEPOSITMETHOD.JAPANIAINSTANT))
			{
				for (int i = 0; i < channellistsize; i++)
				{
					String xpathExpression = null;
					WebElement channel = driver.findElements(By.xpath(channelpath)).get(i);
					String sequence = channel.getAttribute("innerText");
					GlobalMethods.printDebugInfo("Current proceed to : " + method.getWithdrawName() + " - " + sequence);

					if((method == DEPOSITMETHOD.JAPANIAINSTANT && !Brand.equalsIgnoreCase(BRAND.UM.toString())) || method == DEPOSITMETHOD.JAPANEWALLET)
					{
						sequence= sequence.split("\n")[0];
						xpathExpression= "//*[contains(text(),'"+sequence+"')] | //*[translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"+sequence.toLowerCase()+"']";
					}
					else
					{
						xpathExpression = "//span[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '" + sequence.toLowerCase() + "')]";

					}
					WebElement depmethodsequence = driver.findElement(By.xpath(xpathExpression));
					depmethodsequence.click();

					depSubmitAndCheck(bank_d, method ,account, amount, notes, taxID, cardnum);

					menu.goToMenu(CPMenuName.CPPORTAL);
					menu.goToMenu(CPMenuName.DEPOSITFUNDS);
					deposit_funds.navigateToMethod(method);
				}
			}
			else
			{
				GlobalMethods.printDebugInfo("Only one channel for " + method.getWithdrawName());
				depSubmitAndCheck(bank_d, method ,account, amount, notes, taxID, cardnum);
			}
		}
	}

	@Test(priority = 2)
	public void cpsBrazilBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.BRAZILINSTANT);
	}

/*	@Test(priority = 2)
	public void cpsBrazilPIX() 	{
		CPBrazilPIXDeposit bank_d = myfactor.newInstance(CPBrazilPIXDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.BRAZILPIX);
	}*/

	@Test(priority = 2)
	public void cpsBrazilPIXQR() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.BRAZILPIXQR);
	}

	@Test(priority = 2)
	public void cpsBitwallet() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.BITWALLET);
	}

	@Test(priority = 2)
	public void cpsColombiaBT()  {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.COLOMBIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsChileBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.CHILEINSTANT);
	}

	@Test(priority = 2)
	public void cpsCameroonBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.CAMEROONINSTANT);
	}

	@Test(priority = 2)
	public void cpsGhanaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.GHANAINSTANT);
	}

	@Test(priority = 2)
	public void cpsIndiaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.INDIAIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsIndiaUPI() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.IndiaUPI);
	}

	@Test(priority = 2)
	public void cpsIndonesiaBT()  {
		CPIndonesiaBankDeposit bank_d = myfactor.newInstance(CPIndonesiaBankDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.INDONESIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsIndonesiaVABT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.INDONESIAVIRTUAL);
	}

	@Test(priority = 2)
	public void cpsIndonesiaP2PBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.INDONESIAP2P);
	}

	@Test(priority = 2)
	public void cpsIndonesiaGoPay() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.INDONESIAGOPAY);
	}

	@Test(priority = 2)
	public void cpsInteracBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.INTERAC);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_JAPAN_BT)
	public void cpsJapanBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.JAPANIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsJapanEWallet() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.JAPANEWALLET);
	}

	@Test(priority = 2)
	public void cpsJapanLocalCC() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.JAPANCREDITCARD);
	}

	@Test(priority = 2)
	public void cpsJapanJCB() 	{
		CPJapanJCBDeposit bank_d = myfactor.newInstance(CPJapanJCBDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.CREDITORDEBIT);
	}

	@Test(priority = 2)
	public void cpsJapanEmoney() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.JAPANEMONEY);
	}

	@Test(priority = 2)
	public void cpsJapanKonbini() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.JAPANKONBINI);
	}

	@Test(priority = 2)
	public void cpsKenyaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.KENYAINSTANT);
	}

/*	@Test(priority = 2)
	public void cpsLocalDepositor() {
		CPLocalDepositorDeposit bank_d = myfactor.newInstance(CPLocalDepositorDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.LOCALDEPOSITOR);
	}*/

	@Test(priority = 2)
	public void cpsMalaysiaBT() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.MALAYINSTANT);
	}

	@Test(priority = 2)
	public void cpsMalaysiaFPX() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.MalaysiaFPX);
	}

	@Test(priority = 2)
	public void cpsMsiaEwallet() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.MalaysiaEWallet);
	}

	@Test(priority = 2)
	public void cpsMexicoBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.MEXICOINSTANT);
	}

	@Test(priority = 2)
	public void cpsNigeriaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.NIGERIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsNetherlandBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.NETHERLANDINSTANT);
	}

	@Test(priority = 2)
	public void cpsPhilipineBT() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.PHILIPPINESINSTANT);
	}

	@Test(priority = 2)
	public void cpsPeruBT() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.PERUINSTANT);
	}

	@Test(priority = 2)
	public void cpsRwandaBT() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.RWANDAINSTANT);
	}

	@Test(priority = 2)
	public void cpsSouthKoreaBT() 	{
		CPKoreaBankDeposit bank_d = myfactor.newInstance(CPKoreaBankDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.KOREAINSTANT);
	}

	@Test(priority = 2)
	public void cpsSouthAfricaBT() 	{
		//PUG having different input fields
		if(Brand.equalsIgnoreCase(BRAND.PUG.toString()))
		{
			CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
			CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.SOUTHAFRICAINSTANT);
		}
		else
		{
			/*CPBrazilPIXDeposit bank_d = myfactor.newInstance(CPBrazilPIXDeposit.class);
			CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.SOUTHAFRICAINSTANT);*/
		}
	}

	@Test(priority = 2)
	public void cpsThailandBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.THAIINSTANT);
	}

	@Test(priority = 2)
	public void cpsThailandQR() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.THAILANDQR);
	}

	@Test(priority = 2)
	public void cpsTaiwanBT() 	{
		CPKoreaBankDeposit bank_d = myfactor.newInstance(CPKoreaBankDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.TAIWANINSTANT);
	}

	@Test(priority = 2)
	public void cpsTaiwanQR() 	{
		CPTaiwanQRDeposit bank_d = myfactor.newInstance(CPTaiwanQRDeposit.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.TAIWANQR);
	}

	@Test(priority = 2)
	public void cpsTanzaniaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.TANZANIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsUgandaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.UGANDAINSTANT);
	}

	@Test(priority = 2)
	public void cpsVietnamBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.VIETNAMINSTANT);
	}

	@Test(priority = 2)
	public void cpsVietnamP2PBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.VIETNAMP2P);
	}

	@Test(priority = 2)
	public void cpsVietnamMomo() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.VIETNAMMOMO);
	}

	@Test(priority = 2)
	public void cpsVietnamQR() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.VIETNAMQR);
	}

	@Test(priority = 2)
	public void cpsZambiaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.ZAMBIAINSTANT);
	}

	@Test(priority = 2)
	public void cpsBanxa() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.BANXA);
	}

	@Test(priority = 2)
	public void cpsLaosBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.LAOSINSTANT);
	}

	@Test(priority = 2)
	public void cpsAlipayBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.MOBILE);
	}

	@Test(priority = 2)
	public void cpsGiropayBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.GIROPAY);
	}

	@Test(priority = 2)
	public void cpsHongKongBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.HONGKONGINSTANT);
	}

	@Test(priority = 2)
	public void cpsArgentinaBT() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.ARSBT);
	}

	@Test(priority = 2)
	public void cpsAzupay() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.AZUPAY);
	}

	@Test(priority = 2)
	public void cpsPaypal() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.Paypal);
	}

	@Test(priority = 2)
	public void cpsSlash() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		CpsBankTransferDeposit(bank_d, DEPOSITMETHOD.SLASH);
	}

	//methods for new deposit UI
	private void fillDepositBasicInfoNew(DepositBase deposit, DEPOSITMETHOD method)
	{
		amount = this.getRandomDepositAmount(method);
		login.goToCpHome();
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.changeLanguage("English");
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Deposit***");

		if(GlobalProperties.isWeb) {
			menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		}else {
			menu.goToMenu(CPMenuName.DEPOSITFUNDSMOBILE);
		}

		account = deposit.getValidAccount();

//		for (CURRENCY currency : CURRENCY.values()) {
//			account = deposit.checkAccount(currency);
//			if (account != null) {
//				break;
//			}
//		}

		assertNotNull(account, "No available account found");
	}

	public void depositNewProcessNoURLCheck(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositNew(account,amount,method);
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public void depositNewProcessNoURLCheck_waitRedirect(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositNew(account,amount,method);

		// For crypto, payment submit to cps within the same domain > cps redirect back to CP payment summary.
		// In here, page is being loaded entirely and the payment summary countdown is shorter than page loading time.
		// Hence, page is redirect to home when countdown has reached.
		boolean bIsUrlHome = deposit.checkHomeUrl();

		if (!bIsUrlHome) {
			deposit.goHomePage(TraderURL);
		}

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public double depositNewProcessNoURLCheck_checkDepoMaxAmount(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		double depoMaxAmount = deposit.depositNewCheckDepoMaxAmount(account,amount,method);
		return depoMaxAmount;
	}
	public void depositCommon(DepositBase deposit,DEPOSITMETHOD method){
		fillDepositBasicInfoNew(deposit,method);
		amount="50";
		deposit.depositNew(account,amount,method);	}
	public void depositNewProcessNoURLCheck_withoutRedirectAfterDeposit(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositNew(account,amount,method);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}
	// 校验入金支付详情页信息（LBT）
	public void depositAndCheckLBTPaymentInfo(DepositBase deposit,DEPOSITMETHOD method,double exchangeRate)
	{
		fillDepositBasicInfoNew(deposit,method);
		// 入金
		deposit.depositWithOutPay(account,amount,method);

		deposit.setDepositMethod(method);
		// 检查入金支付页详情

		deposit.checkPaymentDetailsWithExchangeRate(account,amount,method,exchangeRate);


		System.out.println("***Test " + method.getWebName() + " deposit page check succeed!!********");
	}

	// 校验入金支付详情页信息（电子钱包和信用卡）
	public void depositAndCheckCCEWalletPaymentInfo(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		// 入金
		deposit.depositWithOutPay(account,amount,method);

		deposit.setDepositMethod(method);
		// 检查入金支付页详情

		deposit.checkCCEwalletPaymentDetails(account,amount,method);


		System.out.println("***Test " + method.getWebName() + " deposit page check succeed!!********");
	}

	public void depositAndCheckCryptoPaymentInfo(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		// 入金
		deposit.depositWithOutPay(account,amount,method);

		deposit.setDepositMethod(method);
		// 检查入金支付页详情

		deposit.checkCryptoPaymentDetails(account,amount,method);


		System.out.println("***Test " + method.getWebName() + " deposit page check succeed!!********");
	}

	public void depositAndCheckOfflinePaymentInfo(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		// 入金
		deposit.depositWithOutPay(account,amount,method);

		deposit.setDepositMethod(method);
		// 检查入金支付页详情

		deposit.checkOfflinePaymentDetails(account,amount,method);
		// 模拟支付并校验必现上传问题件
		deposit.payNowWithoutUpload();
		deposit.checkFileUploadText();

		System.out.println("***Test " + method.getWebName() + " deposit page check succeed!!********");
	}

	// 校验UnionPay入金渠道最低金额弹框提示
	public void depositCheckUnionPayPopUpWindow(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		// 入金
		deposit.depositWithOutPay(account,"100",method);

		deposit.setDepositMethodDisabled(method);
		deposit.exitIframe();

		CustomAssert.assertTrue(deposit.checkDepositAmountNotMetPopWindow(), "UnionPay pop up window check failed");


		//点击选择其他入金方法
		deposit.clickChooseAnotherPaymentMethod();

		deposit.goToIframe("pcs-iframe");
		// 检查入金渠道是否禁用
		deposit.checkPaymentMethodDisabled(method.getdepositDatetestid());
		// 重新选择入金方法再做校验
		deposit.exitIframe();

		deposit.setDepositMethodDisabled(method);
		// 点击修改金额
		deposit.exitIframe();
		deposit.clickChangeAmount();
		CustomAssert.assertTrue(deposit.checkDepositAmountNotMetSuccessPopWindow(), "UnionPay Change Amount check failed");
		//查看渠道是否禁用
		deposit.setDepositMethodDisabled(method);

		ScreenshotHelper.addTextWatermark(driver, "UnionPay pop up window check success");
		CustomAssert.assertAll();
		LogUtils.info("***Test " + method.getWebName() + "客户输入的金额未达到渠道的最低入金，提示弹窗succeed!!********");

	}
	// 校验UnionPay支付页面跳转
	public void checkPaymentRedirect(DepositBase deposit)
	{
		LogUtils.info("***校验UnionPay支付页面跳转********");
		deposit.clickContinue();
		String TraderURL = deposit.getCurrentUrl();
		deposit.payNow();
		if (Brand.equalsIgnoreCase("UM")){
			CustomAssert.assertTrue(!deposit.checkUrlContains(TraderURL), "Submit failed or error occur " );
		}
		if(Brand.equalsIgnoreCase("MO")){
			CustomAssert.assertTrue(!deposit.checkUrlContains(TraderURL), "Submit failed or error occur  " );
		}
		if (Brand.equalsIgnoreCase("VT")){
			CustomAssert.assertTrue(!deposit.checkUrlContains(TraderURL), "Submit failed or error occur  ");
		}
		CustomAssert.assertAll();
		LogUtils.info("***校验UnionPay支付页面跳转验证成功********");

	}

	/**
	 * 校验KYC未审核账号 IBT入金KYC弹框提示
	 * @param deposit
	 * @param method
	 */
	public void depositCheckIBTPopUpWindow(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		// 入金
		deposit.depositWithOutPay(account,"100",method);

		deposit.setDepositMethodDisabled(method);
		deposit.exitIframe();

		CustomAssert.assertTrue(deposit.checkDepositKYCNotMetPopWindow(), "KYC pop up window check failed");

		CustomAssert.assertAll();
		LogUtils.info("***Test " + method.getWebName() + "客户输入的金额未达到渠道的最低入金，提示弹窗succeed!!********");

	}
	public void depositNewProcess(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositNew(account,amount,method);
		Assert.assertTrue(deposit.checkUrlNotContains(TraderURL), "Submit failed or error occur for " + method.getWebName());
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public void depositNewWithURLCheck(DepositBase deposit, DEPOSITMETHOD method, String thirdpartyurl)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositNew(account,amount,method);
		Assert.assertTrue(deposit.checkUrlContains(thirdpartyurl), "URL check failed or error occur for " +method.getWebName());
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public void depositNewProcessNoDepositAmountCheck(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositNewNoDepositAmountCheck(account,amount,method);
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistoryWithoutDepositinfo(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public void depositNewWithURLCheckAccNumPersonID(DepositBase deposit,DEPOSITMETHOD method,String accNum,String personalID,String thirdpartyurl)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositWithPersonalIDAccNumNew(account,amount,accNum,personalID,method);
		Assert.assertTrue(deposit.checkUrlNotContains(thirdpartyurl), "Submit failed or error occur for " + method.getWebName());
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public void depositNewWithMailNoDepositAmount(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositWithMailNoDepositAmountNew(account,amount,email,method);
		Assert.assertTrue(deposit.checkUrlNotContains(TraderURL), "Submit failed or error occur for " + method.getWebName());
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	public void depositNewWithCCInfo(DepositBase deposit,DEPOSITMETHOD method,String ccCity,String ccAddress,String ccPostalCode)
	{
		fillDepositBasicInfoNew(deposit,method);
		deposit.depositWithCCInfoNew(account,amount,ccCity,ccAddress,ccPostalCode,method);
		Assert.assertTrue(deposit.checkUrlNotContains(TraderURL), "Submit failed or error occur for " + method.getWebName());
		deposit.goHomePage(TraderURL);

		// check deposit history
		checkHistory(deposit,method,account,amount);
		System.out.println("***Test " + method.getWebName() + " deposit succeed!!********");
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CC_APPLEGOOGLEPAY)
	public void testCCGoogleApplePayNew() 	{
		CPCreditCardDeposit googlepay = myfactor.newInstance(CPCreditCardDeposit.class);
		depositNewProcessNoDepositAmountCheck(googlepay,DEPOSITMETHOD.CCAPPLEGOOGLEPAYNew);
	}
	@Test(priority = 2)
	public void testGoogleApplePayAUDNew() 	{
		CPCreditCardDeposit googlepay = myfactor.newInstance(CPCreditCardDeposit.class);
		depositNewWithURLCheck(googlepay,DEPOSITMETHOD.CCAPPLEGOOGLEPAYNew,GlobalProperties.ApplePayGooglePay);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CC_BRIDGERPAY)
	public void testBridgePayNew() 	{
		if(Brand.equalsIgnoreCase((BRAND.UM.toString())))
		{
			CPBridgePayDeposit bridger = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDepositNew(bridger, DEPOSITMETHOD.CCBRIDGERPAY);
		}else {
			CPBridgePayDeposit bridgepay = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDepositNew(bridgepay, DEPOSITMETHOD.CREDITORDEBIT_New);
		}
	}
	public void testCreditCardDepositNew(CPCreditCardDeposit creditcard, DEPOSITMETHOD method) {
		fillDepositBasicInfoNew(creditcard, method);
		if(Brand.equalsIgnoreCase(BRAND.UM.toString()))
		{
			creditcard.depositNewNoDepositAmountCheck(account,amount,method);
		}
		else{
			creditcard.depositNew(account,amount,method);
		}
		creditcard.waitCCLoader();
		String frameName = "iFrameResizer0";
		if (Brand.equalsIgnoreCase("UM")){
			frameName="iFrameResizer01";
		}
		boolean iframeVisible =creditcard.checkThirdPartyIframeVisible(Brand,frameName);
		CustomAssert.assertTrue(iframeVisible, "3rd Party iFrame is not visible.");
		CustomAssert.assertTrue(creditcard.checkUrlContains("bridgePaymentDepositPCS"), "redirect 3rd cc page Failed.");
		//creditcard.goBack();
		checkHistory(creditcard,method,account,amount);
		System.out.println("********Test " +method.getWebName()+ " succeeded********");
		CustomAssert.assertAll();

	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_BridgePayWithCallback)
	@Parameters(value={"OpenApi"})
	public void testBridgePayCallBack(@Optional("")String openapi) 	{
		if(Brand.equalsIgnoreCase((BRAND.UM.toString())))
		{
			CPBridgePayDeposit bridger = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDepositWithCallBack(bridger, DEPOSITMETHOD.CCBRIDGERPAY,openapi);
		}else {
			CPBridgePayDeposit bridgepay = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDepositWithCallBack(bridgepay, DEPOSITMETHOD.CREDITORDEBIT_New,openapi);
		}
	}

	public void testCreditCardDepositWithCallBack(CPCreditCardDeposit creditcard, DEPOSITMETHOD method,String openApi) {
		fillDepositBasicInfoNew(creditcard, method);
		if(Brand.equalsIgnoreCase(BRAND.UM.toString()))
		{
			creditcard.depositNewNoDepositAmountCheck(account,amount,method);
		}
		else{
			creditcard.depositNew(account,amount,method);
		}
		creditcard.waitCCLoader();
		String frameName = "iFrameResizer0";
		if (Brand.equalsIgnoreCase("UM")){
			frameName="iFrameResizer01";
		}
		boolean iframeVisible =creditcard.checkThirdPartyIframeVisible(Brand,frameName);
		CustomAssert.assertTrue(iframeVisible, "3rd Party iFrame is not visible.");
		CustomAssert.assertTrue(creditcard.checkUrlContains("bridgePaymentDepositPCS"), "redirect 3rd cc page Failed.");
		//creditcard.goBack();
		int channelType = 1;
		int channelID = 8;
		BrigePayCPSCallback(creditcard,openApi,channelType,channelID);
		JSONObject depositRecords = null;
		long startTime = System.currentTimeMillis();
		long timeout = 30000; // 30秒超时

		while (System.currentTimeMillis() - startTime < timeout) {
			try {
				depositRecords = adminPaymentAPI.apiDPAuditSearch(account, "5");
				if (depositRecords != null && !depositRecords.getJSONArray("rows").isEmpty()) {
					JSONObject depositRecord = depositRecords.getJSONArray("rows").getJSONObject(0);
					if (depositRecord != null) {
						CustomAssert.assertNotNull(depositRecord, "Deposit record is not Succeed,Please Check");
						break; // 成功找到记录，退出循环
					}
				}
			} catch (Exception e) {
				LogUtils.info("Waiting for deposit record, retrying... Error: " + e.getMessage());
			}

			try {
				Thread.sleep(2000); // 每2秒重试一次
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				break;
			}
		}

		if (depositRecords == null || depositRecords.getJSONArray("rows").isEmpty()) {
			CustomAssert.fail("Could not find successful deposit record within 30 seconds");
		}

		System.out.println("********Test " +method.getWebName()+ " succeeded********");
		CustomAssert.assertAll();

	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CC_BRIDGERPAY)
	public void testBridgePayCPSCallBack() 	{
		if(Brand.equalsIgnoreCase((BRAND.UM.toString())))
		{
			CPBridgePayDeposit bridger = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDepositNew(bridger, DEPOSITMETHOD.CCBRIDGERPAY);
		}else {
			CPBridgePayDeposit bridgepay = myfactor.newInstance(CPBridgePayDeposit.class);
			testCreditCardDepositNew(bridgepay, DEPOSITMETHOD.CREDITORDEBIT_New);
		}
	}

	@Test(priority = 2)
	public void testCPSCryptoDepositNew() {

		if(Brand.equalsIgnoreCase(BRAND.STAR.toString()))
		{
			testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOBEP);

		}
		else if(!Brand.equalsIgnoreCase(BRAND.UM.toString()))
		{
			if (Brand.equalsIgnoreCase(BRAND.VJP.toString()))
			{
				testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOXRP);
			}

		}
		if(!Brand.equalsIgnoreCase(BRAND.UM.toString())){
			testCPSCryptoCommonDepositNew(DEPOSITMETHOD.ETH);
		}
	}

	@Test(priority = 2)
	public void testCPSCryptoCommonDepositNew(DEPOSITMETHOD method) {
		CPCryptoDeposit crypto = myfactor.newInstance(CPCryptoDeposit.class);
		depositNewProcessNoURLCheck_waitRedirect(crypto, method);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_Callback)
	@Parameters(value={"OpenApi"})
	public void testCPSCryptoCommonDepositCallBack(@Optional("")String openapi) {

		DepositBase crypto = myfactor.newInstance(DepositBase.class);
		depositCommon(crypto, DEPOSITMETHOD.CRYPTOERCNew);
		cpsCallback(crypto,15,3,account,amount,openapi,DEPOSITMETHOD.CRYPTOERCNew);
		System.out.println("***Test crypto deposit succeed!!********");
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_INTER_BANK_TRANS)
	public void testInterBankTransPayNew() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck_withoutRedirectAfterDeposit(InterBankTransfer,DEPOSITMETHOD.I12BANKTRANSFER_New);
	}

    // 入金输入金额页面输入信息检查（账号币种余额）
    @Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_INPUT_INFO_CHECK)
    public void testInputPageInfoCheck() {
        CPInterBankTrans deposit = myfactor.newInstance(CPInterBankTrans.class);
        try {
            depositNewProcessInputPageInfoCheck(deposit);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 安全地将 Object 转换为 double
	 * @param obj 要转换的对象
	 * @param defaultValue 默认值
	 * @return 转换后的 double 值
	 */
	private double safeConvertToDouble(Object obj, double defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		try {
			if (obj instanceof String) {
				return Double.parseDouble((String) obj);
			} else if (obj instanceof Number) {
				return ((Number) obj).doubleValue();
			} else {
				return Double.parseDouble(obj.toString());
			}
		} catch (NumberFormatException e) {
			LogUtils.info("Failed to convert to double: " + obj + ", using default value: " + defaultValue);
			return defaultValue;
		}
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositLBTPageCheck(Map<String,String> exchangeData) {

		DepositBase InterBankTransfer = myfactor.newInstance(DepositBase.class);
		LogUtils.info("********实际调用实例 " + InterBankTransfer);
		// LBT - 输入信息检查-star
		String brand = Brand.toUpperCase();
		LogUtils.info("********实际调用品牌 " + brand);

		String key = "USD2MYR";
		Object rate = exchangeData.get(key);
		double exchangeRate =safeConvertToDouble(rate, 1.0);

//		String channel = "localBankTransfer";
		switch (brand)
		{
			case "PUG":
			case "STAR" :
				key = "USD2VND";
				rate = exchangeData.get(key);
				exchangeRate =safeConvertToDouble(rate, 1.0);
				depositAndCheckLBTPaymentInfo(InterBankTransfer, DEPOSITMETHOD.VIETNAMINSTANT_New,exchangeRate);
				break;
			case "VT":
			case "MO":
				depositAndCheckLBTPaymentInfo(InterBankTransfer, DEPOSITMETHOD.MalaysiaXPAYNew,exchangeRate);
				break;

			case "VFX":
			case "AU" :
			case "UM" :
				key = "USD2PHP";

				rate = exchangeData.get(key);
				double brandExchangeRate =safeConvertToDouble(rate, 1.0);
				LogUtils.info("********实际调用品牌 " + brand+" "+key +" exchangeRate:"+brandExchangeRate);
				depositAndCheckLBTPaymentInfo(InterBankTransfer, DEPOSITMETHOD.ZOTAPAY_PHP_NEW,brandExchangeRate);
				break;
			case "VJP" :
				key = "USD2JPY";

				rate = exchangeData.get(key);
				double vjpExchangeRate =safeConvertToDouble(rate, 1.0);
				LogUtils.info("********实际调用品牌 " + brand+" "+key +" exchangeRate:"+vjpExchangeRate);

				depositAndCheckLBTPaymentInfo(InterBankTransfer, DEPOSITMETHOD.JAPANBT_TheJapanOne,vjpExchangeRate);
				break;

		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositLBTPageCheck_VietnamInstantNew(Map<String, Object> exchangeData) {
		LogUtils.info("LBT Page Check - Vietnam Instant New");
		if (Brand.equalsIgnoreCase("PUG") || Brand.equalsIgnoreCase("STAR")) {
			DepositBase interBankTransfer = myfactor.newInstance(DepositBase.class);
			String key = "USD2VND";
			Object rate = exchangeData.get(key);
			double exchangeRate = safeConvertToDouble(rate, 1.0);
			depositAndCheckLBTPaymentInfo(interBankTransfer, DEPOSITMETHOD.VIETNAMINSTANT_New, exchangeRate);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositLBTPageCheck_MalaysiaXPAYNew(Map<String, Object> exchangeData) {
		LogUtils.info("LBT Page Check - Malaysia XPAY New");
		if (Brand.equalsIgnoreCase("VT") || Brand.equalsIgnoreCase("MO")) {
			DepositBase interBankTransfer = myfactor.newInstance(DepositBase.class);
			String key = "USD2MYR";
			Object rate = exchangeData.get(key);
			double exchangeRate = safeConvertToDouble(rate, 1.0);
			depositAndCheckLBTPaymentInfo(interBankTransfer, DEPOSITMETHOD.MalaysiaXPAYNew, exchangeRate);
		}
	}

	@Test(priority = 2, description =testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositLBTPageCheck_ZOTAPAYPHPNew(Map<String, Object> exchangeData) {
		LogUtils.info("LBT Page Check - ZOTAPAY PHP NEW");
		if (Brand.equalsIgnoreCase("VFX") || Brand.equalsIgnoreCase("AU") || Brand.equalsIgnoreCase("UM")) {
			DepositBase interBankTransfer = myfactor.newInstance(DepositBase.class);
			String key = "USD2PHP";
			Object rate = exchangeData.get(key);
			double exchangeRate = safeConvertToDouble(rate, 1.0);
			LogUtils.info("Brand: " + Brand + " " + key + " exchangeRate:" + exchangeRate);
			depositAndCheckLBTPaymentInfo(interBankTransfer, DEPOSITMETHOD.ZOTAPAY_PHP_NEW, exchangeRate);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositLBTPageCheck_JAPANBTTheJapanOne(Map<String, Object> exchangeData) {
		LogUtils.info("LBT Page Check - JAPANBT TheJapanOne");
		if (Brand.equalsIgnoreCase("VJP")) {
			DepositBase interBankTransfer = myfactor.newInstance(DepositBase.class);
			String key = "USD2JPY";
			Object rate = exchangeData.get(key);
			double exchangeRate = safeConvertToDouble(rate, 1.0);
			LogUtils.info("Brand: " + Brand + " " + key + " exchangeRate:" + exchangeRate);
			depositAndCheckLBTPaymentInfo(interBankTransfer, DEPOSITMETHOD.JAPANBT_TheJapanOne, exchangeRate);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositOfflineCheck() 	{
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		//Credit Card - 输入信息检查
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")||Brand.equalsIgnoreCase("vt")||Brand.equalsIgnoreCase("um")){
			depositAndCheckOfflinePaymentInfo(InterBankTransfer,DEPOSITMETHOD.IBT_EQUALS);

		}if (Brand.equalsIgnoreCase("mo")){
			depositAndCheckOfflinePaymentInfo(InterBankTransfer,DEPOSITMETHOD.I12BANKTRANSFER_New);
		}

	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositOfflineCheck_IBTEquals() {
		LogUtils.info("Offline Deposit Page Check - IBT Equals");
		if (Brand.equalsIgnoreCase("au") || Brand.equalsIgnoreCase("vfx") ||
				Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("um")||
				Brand.equalsIgnoreCase("pug")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			if(TestEnv.equalsIgnoreCase("uat")){
				depositAndCheckOfflinePaymentInfo(interBankTransfer, DEPOSITMETHOD.IBT_EQUALS_UAT);
			}else {
				depositAndCheckOfflinePaymentInfo(interBankTransfer, DEPOSITMETHOD.IBT_EQUALS);

			}
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositOfflineCheck_I12BankTransferNew() {
		LogUtils.info("Offline Deposit Page Check - I12BANKTRANSFER New");
		if (Brand.equalsIgnoreCase("mo")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckOfflinePaymentInfo(interBankTransfer, DEPOSITMETHOD.I12BANKTRANSFER_New);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		//Crypto - 输入信息检查
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			depositAndCheckCryptoPaymentInfo(InterBankTransfer,DEPOSITMETHOD.CRYPTOBIT);
		}
		else if(Brand.equalsIgnoreCase("vt")){
			depositAndCheckCryptoPaymentInfo(InterBankTransfer,DEPOSITMETHOD.USDCNew);
		}
		else if(Brand.equalsIgnoreCase("mo")){
			depositAndCheckCryptoPaymentInfo(InterBankTransfer,DEPOSITMETHOD.ETH);
		}
		else if(Brand.equalsIgnoreCase("um")){
			depositAndCheckCryptoPaymentInfo(InterBankTransfer,DEPOSITMETHOD.USDC);
		}else if (Brand.equalsIgnoreCase("pug")){
			depositAndCheckCryptoPaymentInfo(InterBankTransfer,DEPOSITMETHOD.CRYPTOERCNew);
		}
		else {
			depositAndCheckCryptoPaymentInfo(InterBankTransfer,DEPOSITMETHOD.USDC);

		}

	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_IBTEquals() {
		LogUtils.info( "Crypto Deposit Page Check - IBT Equals");
		if (Brand.equalsIgnoreCase("au") || Brand.equalsIgnoreCase("vfx")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.CRYPTOBIT);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_USDCNew() {
		LogUtils.info("Crypto Deposit Page Check - USDC New");
		if (Brand.equalsIgnoreCase("vt")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.USDCNew);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_ETH() {
		LogUtils.info("Crypto Deposit Page Check - ETH");
		if (Brand.equalsIgnoreCase("mo")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.ETH);
		}
	}

	@Test(priority = 2, description =testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_USDC() {
		LogUtils.info("Crypto Deposit Page Check - USDC");
		if (Brand.equalsIgnoreCase("um")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.CRYPTOBEPNew);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_CryptoERCNew() {
		LogUtils.info("Crypto Deposit Page Check - Crypto ERC New");
		if (Brand.equalsIgnoreCase("pug")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.CRYPTOERCNew);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_CryptoBIT() {
		LogUtils.info("Crypto Deposit Page Check - Crypto BITCOIN New");
		CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.CRYPTOBTCNEW);

	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck_DefaultUSDC() {
		LogUtils.info("Crypto Deposit Page Check - Default USDC");
		if (!Brand.equalsIgnoreCase("au") && !Brand.equalsIgnoreCase("vfx") &&
				!Brand.equalsIgnoreCase("vt") && !Brand.equalsIgnoreCase("mo") &&
				!Brand.equalsIgnoreCase("um") && !Brand.equalsIgnoreCase("pug")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCryptoPaymentInfo(interBankTransfer, DEPOSITMETHOD.USDC);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCCPageCheck() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		//电子钱包 - 输入信息检查-pu
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")||Brand.equalsIgnoreCase("um"))
		{
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.CCAPPLEGOOGLEPAY);
		} else if (Brand.equalsIgnoreCase("vjp")) {
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.PaymentOption_New);

		}  else {
			//mo\vt
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.CREDITORDEBIT_New);
		}

	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCCPageCheck_AppleGooglePay() {
		LogUtils.info("Credit Card Deposit Page Check - Apple/Google Pay");
		if (Brand.equalsIgnoreCase("au") || Brand.equalsIgnoreCase("vfx")||Brand.equalsIgnoreCase("um")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.CCAPPLEGOOGLEPAY);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCCPageCheck_PaymentOption() {
		LogUtils.info("Credit Card Deposit Page Check - Payment Option");
		if (Brand.equalsIgnoreCase("vjp")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.PaymentOption_New);
		}
	}

	@Test(priority = 2, description =  testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCCPageCheck_CreditDebit() {
		LogUtils.info("Credit Card Deposit Page Check - Credit/Debit Card");
		if (!Brand.equalsIgnoreCase("au") &&!Brand.equalsIgnoreCase("vfx") && !Brand.equalsIgnoreCase("um") && !Brand.equalsIgnoreCase("vjp")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.CREDITORDEBIT_New);
		}else {
			throw new SkipException("Skipping this test intentionally.");
		}
	}


	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		//电子钱包

		if(Brand.equalsIgnoreCase("au"))
		{
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.ADVCASH);
		} else if (Brand.equalsIgnoreCase("vt")) {
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.MalaysiaEWallet);

		} else if (Brand.equalsIgnoreCase("mo")) {
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.BINANCE);
		}else if (Brand.equalsIgnoreCase("pug")) {
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.NETELLER);
		}else {
			depositAndCheckCCEWalletPaymentInfo(InterBankTransfer,DEPOSITMETHOD.CREDITORDEBIT);
		}
		//VJP/STAR
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck_ADVCASH() {
		LogUtils.info("********AU Brand E-Wallet Deposit Page Check " +Brand);
		if (Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			LogUtils.info("********AU Brand E-Wallet Deposit Page Check " );
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.ADVCASH);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck_MalaysiaEWallet() {
		if (Brand.equalsIgnoreCase("vt")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.MalaysiaEWallet);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck_BINANCE() {
		if (Brand.equalsIgnoreCase("mo")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.BINANCE);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck_NETELLER() {
		if (Brand.equalsIgnoreCase("pug")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.NETELLER);
		}
	}

	@Test(priority = 2, description =testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck_CREDITORDEBIT() {
		if (!Brand.equalsIgnoreCase("au") && !Brand.equalsIgnoreCase("vfx") &&!Brand.equalsIgnoreCase("vt")
				&& !Brand.equalsIgnoreCase("mo") && !Brand.equalsIgnoreCase("pug")) {
			CPInterBankTrans interBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
			depositAndCheckCCEWalletPaymentInfo(interBankTransfer, DEPOSITMETHOD.CREDITORDEBIT);
		}
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_INPUT_INFO_CHECK)
	public void testWithdrawInputPageInfoCheck() {
		CPInterBankTrans deposit = myfactor.newInstance(CPInterBankTrans.class);
		try {
			depositNewProcessInputPageInfoCheck(deposit);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    //确保账号页面和出入金页面- 账户号 - 币种- 余额资料正确

	private void depositNewProcessInputPageInfoCheck(CPInterBankTrans deposit) throws IOException {

		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);;
		liveAccounts.selectAccStatus(GlobalProperties.ACC_STATUS.ACTIVE);
		//获取live account页面账号信息
		String accountInfo=liveAccounts.getAccountInfo();
		List<String> formattedResults= liveAccounts.parseAccountData(accountInfo);

		System.out.println("------------------------------");
		System.out.println(formattedResults);
		System.out.println("------------------------------");

		//点击funds菜单(默认进入入金页面)
		inputPageInfoCheck(menu,CPMenuName.DEPOSITFUNDS,formattedResults,deposit);
		//点击withdraw菜单，出金账号信息校验
		inputPageInfoCheck(menu,CPMenuName.WITHDRAWFUNDS,formattedResults,deposit);


	}

	public void inputPageInfoCheck(CPMenu menu,CPMenuName menuName,List<String> formattedAccountResults,CPInterBankTrans deposit) throws IOException {

		if (menuName == CPMenuName.DEPOSITFUNDS) {
			menu.goToMenu(CPMenuName.DEPOSITFUNDS);
			System.out.println("查看入金页账号列表账号信息");
		}
		if (menuName == CPMenuName.WITHDRAWFUNDS) {
			menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
			System.out.println("查看出金页账号列表账号信息");
		}
		// 获取页面账户信息
		List<WebElement> depositFundsAccountsInfo=deposit.getDepositFundsDefaultAccountInfo(Brand.toLowerCase());
		System.out.println(depositFundsAccountsInfo);

		for (WebElement element : depositFundsAccountsInfo) {
			System.out.println(element.getText());
		}
		System.out.println("================================");

		System.out.println("输出Account页格式化的账号信息");
		for (String result : formattedAccountResults) {
			System.out.println(result);
		}
		System.out.println("================================");
		try {
			boolean result = deposit.compareIgnoringWhitespace(depositFundsAccountsInfo, formattedAccountResults);
			CustomAssert.assertTrue(result,"账号数据不一致");
			CustomAssert.assertAll();
			// 处理正常结果
		} catch (IllegalArgumentException e) {
			// 处理参数异常，不要让程序继续执行
			System.err.println("参数错误: " + e.getMessage());
			// 可能需要重新抛出异常或采取其他措施
			throw e;
		}

	}
	@Test(priority = 2)
	public void testEuroSpaIBTPayNew() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(InterBankTransfer,DEPOSITMETHOD.EUROSEPAIBT_New);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_INTER_BANK_TRANS)
	public void testInterBankTransPay_Equals_New() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(InterBankTransfer, DEPOSITMETHOD.IBT_EQUALS);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_INTER_BANK_TRANS)
	public void testInterBankTransPay_Whitespay_New() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(InterBankTransfer, DEPOSITMETHOD.IBT_WHITESPAY);
	}

	@Test(priority = 2)
	public void testAustraliaBankTransPayNew() {
		CPInterBankTrans InterBankTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(InterBankTransfer,DEPOSITMETHOD.AUBANKTRANSFER_New);
	}
	@Test(priority = 2)
	public void testBrokerTransferPayNew() {
		CPInterBankTrans BrokerTransfer = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(BrokerTransfer,DEPOSITMETHOD.B2B_New);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_EWALLET_SKRILL)
	public void testSkrillPayNew() {
		CPSkrillPay skrillPay = myfactor.newInstance(CPSkrillPay.class);
		depositNewWithURLCheck(skrillPay, DEPOSITMETHOD.SKRILL, GlobalProperties.SKRILLURL);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_EWALLET_BINANCEPAY)
	public void testBinancePayNew() {
		CPSkrillPay binancePay = myfactor.newInstance(CPSkrillPay.class);
		depositNewProcessNoURLCheck(binancePay, DEPOSITMETHOD.BINANCE);
	}

	@Test(priority = 2)
	public void testKoraPay_NGN_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d,DEPOSITMETHOD.Korapay_NGN);
	}

	@Test(priority = 2)
	public void testBitolo_NGN_MM_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d,DEPOSITMETHOD.Bitolo_NGN_MM);
	}

	@Test(priority = 2)
	@Parameters(value={"OpenApi"})
	public void testNetellerPayNew() {
		CPNetellerPay neteller = myfactor.newInstance(CPNetellerPay.class);
		depositNewProcess(neteller, DEPOSITMETHOD.NETELLER);
	}

	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testPerfectMoneyNew() {
		CPFasaPay fasapay = myfactor.newInstance(CPFasaPay.class);
		depositNewProcess(fasapay,DEPOSITMETHOD.PERFECTMONEY);
	}

	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testPaymentOptionINR() {
		CPIndiaUPI iUPI = myfactor.newInstance(CPIndiaUPI.class);
		depositNewProcess(iUPI,DEPOSITMETHOD.PaymentOption_UPI);
	}

	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testCheezePayNew() {
		CPIndiaUPI iUPI = myfactor.newInstance(CPIndiaUPI.class);
		depositNewProcess(iUPI,DEPOSITMETHOD.CheesePay);
	}

	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testCricpayzNew() {
		CPIndiaUPI iUPI = myfactor.newInstance(CPIndiaUPI.class);
		depositNewProcess(iUPI,DEPOSITMETHOD.Cricpayz);
	}
	@Test(priority = 2)
	@Parameters(value= {"OpenApi"})
	public void testXpayNew() {
		CPIndiaUPI iUPI = myfactor.newInstance(CPIndiaUPI.class);
		depositNewProcess(iUPI,DEPOSITMETHOD.Xpay);
	}

	@Test(priority = 2)
	public void cpsBrazilBT_Bitolo_PixNew() {
		CPBrazilPIXDeposit bank_d = myfactor.newInstance(CPBrazilPIXDeposit.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.Bitolo_PIX_BRL, GlobalProperties.Bitolo_PIX_URL);
	}

	@Test(priority = 2)
	public void cpsBrazilBTPagsmileNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.Pagsmile_BRL, GlobalProperties.PagsmileURL);
	}

	@Test(priority = 2)
	public void cpsBrazilBT4ON_PixNew() {
		String accountNum = "4567743";
		String personalID = "67455643";
		CPBrazilPIXDeposit bank_d = myfactor.newInstance(CPBrazilPIXDeposit.class);
		depositNewWithURLCheckAccNumPersonID(bank_d, DEPOSITMETHOD.ON4_PIX_BRL,accountNum,personalID,GlobalProperties.ON4_PIX_URL);
	}

	@Test(priority = 2)
	public void cpsBrazilBT_Toppay_PixNew() {
		String accountNum = "4567743";
		String personalID = "67455643";
		CPBrazilBankTrans bank_d = myfactor.newInstance(CPBrazilBankTrans.class);
		depositNewWithURLCheckAccNumPersonID(bank_d, DEPOSITMETHOD.Toppay_PIX_BRL,accountNum,personalID,GlobalProperties.Toppay_PIX_URL);
	}

	@Test(priority = 2)
	public void cpsVietnamBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_VND, GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsUgandaBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_UGX,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsZambiaBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZAMBIABT_Zotapay_ZMW,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsRwandanBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.RwandaBT_ZOTAPAY_RWF,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsTanzaniaBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_TZS,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsSouthAfricaBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_ZAR,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsPhilippinesBTZotaPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_PHP,GlobalProperties.ZotapayURL);
	}
	@Test(priority = 2)
	public void cpsPhilippinesBTPaymentAsiaNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.PaymentAsia_PHP,GlobalProperties.PaymentAsiaURL);
	}
	@Test(priority = 2)
	public void cpsPhilippinesBTToppay_PHPNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.Toppay_PHP,GlobalProperties.Toppay_PIX_URL);
	}
	@Test(priority = 2)
	public void cpsArgentineBT_4ON_PPagoFacil() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ARSBT_4ON_PagoFacil,GlobalProperties.ON4_PIX_URL);
	}
	@Test(priority = 2)
	public void cpsArgentineBT_4ON_MercadoPagoWallet() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ARSBT_4ON_MercadoPagoWallet,GlobalProperties.ON4_PIX_URL);
	}
	@Test(priority = 2)
	public void cpsArgentineBT_4ON_RapiPago() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ARSBT_4ON_RapiPago,GlobalProperties.ON4_PIX_URL);
	}
	//No available personal ID
	@Test(priority = 2)
	public void testSouthAfricaBTOzow_ZARNew() {
		CPSouthAfricaBankTransfer bank_d = myfactor.newInstance(CPSouthAfricaBankTransfer.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.Ozow_ZAR);
	}
	@Test(priority = 2)
	public void testEcpay_Bkash_New() {
		CPFasaPay bkash = myfactor.newInstance(CPFasaPay.class);
		depositNewProcess(bkash, DEPOSITMETHOD.BDTBkash);
	}

	@Test(priority = 2)
	public void testEcpay_Rocket_New() {
		CPFasaPay bkash = myfactor.newInstance(CPFasaPay.class);
		depositNewProcess(bkash, DEPOSITMETHOD.BDTRocket);
	}

	@Test(priority = 2)
	public void testEcpay_Nagad_New() {
		CPFasaPay nagad = myfactor.newInstance(CPFasaPay.class);
		depositNewProcess(nagad, DEPOSITMETHOD.BDTNagad);
	}

	@Test(priority = 2)
	public void cpsCroinPlus_New() {
		CPLocalDepositorDeposit bank_d = myfactor.newInstance(CPLocalDepositorDeposit.class);
		depositNewProcessNoURLCheck(bank_d, DEPOSITMETHOD.LOCALDEPOSITOR_New);
	}

	@Test(priority = 2)
	public void cpsMalaysia2C2PEwallet_New() {
		CPMalaysiaEwallet bank_d = myfactor.newInstance(CPMalaysiaEwallet.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.MalaysiaEWallet,GlobalProperties.MalaysiaEwalletURL);
	}
	@Test(priority = 2)
	public void cpsMalaysia2C2PFPX_New() {
		CPMalaysiaEwallet bank_d = myfactor.newInstance(CPMalaysiaEwallet.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.MalaysiaFPXNew);
	}
	@Test(priority = 2)
	public void cpsMalaysiaXPAY_MYR_New() {
		CPMalaysiaEwallet bank_d = myfactor.newInstance(CPMalaysiaEwallet.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.MalaysiaXPAYNew,GlobalProperties.XpayURL);
	}
	@Test(priority = 2,description = testCaseDescUtils.CPDEPOSIT_EWALLET_BITWALLET)
	public void cpsBitwallet_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcessNoURLCheck(bank_d, DEPOSITMETHOD.BITWALLET);
	}

	@Test(priority = 2)
	public void testSKEbuyPay_New() {
		CPEbuyDeposit eBuy = myfactor.newInstance(CPEbuyDeposit.class);
		depositNewWithURLCheck(eBuy, DEPOSITMETHOD.EBuyTRANSFER,GlobalProperties.EbuyURL);
	}
	@Test(priority = 2)
	public void testPAYWON_KRW_New() {
		CPEbuyDeposit eBuy = myfactor.newInstance(CPEbuyDeposit.class);
		depositNewProcess(eBuy, DEPOSITMETHOD.PAYWON_KRW);
	}
	@Test(priority = 2)
	public void testVpay_KRW_New() {
		CPEbuyDeposit eBuy = myfactor.newInstance(CPEbuyDeposit.class);
		depositNewProcess(eBuy, DEPOSITMETHOD.Vpay_KRW);
	}

	@Test(priority = 2)
	public void testWowsPay_Momopay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.VIETNAMMOMO);
	}

	//No Url check because the third party url changes constantly
	@Test(priority = 2)
	public void testWowsPay_QR_VND_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.WowsPay_QR_VND);
	}
	//No Url check because the third party url changes constantly
	@Test(priority = 2)
	public void testWowsPay_Remit_VND_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.WowsPay_Remit_VND);
	}

	@Test(priority = 2)
	public void testVietnamBTEeziePay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.VNDEeziePay);
	}

	@Test(priority = 2)
	public void cpsVietnamBT_XPay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.VIETNAM_XPay, GlobalProperties.XpayURL);
	}

	@Test(priority = 2)
	public void cpsVietnamBT_WalaoPay() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.WalaoPay_VND, GlobalProperties.WalaoPayURL);
	}

	@Test(priority = 2)
	public void cpsCOPBT_4ON_PSE_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.COLOMBIA_4ON_PSE);
	}

	@Test(priority = 2)
	public void cpsCOPBT_4ON_Efecty_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.COLOMBIA_4ON_Efecty);
	}

	@Test(priority = 2)
	public void cpsCOPBT_Pagsmile_COP_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.COLOMBIAINSTANT,GlobalProperties.PagsmileURL);
	}

	@Test(priority = 2)
	public void cpsCLPBT_Pagsmile_CLP_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.CHILEINSTANT,GlobalProperties.PagsmileURL);
	}

	@Test(priority = 2)
	public void cpsPENBT_Pagsmile_PEN_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.PERUINSTANT,GlobalProperties.PagsmileURL);
	}

	@Test(priority = 2)
	public void cpsPENBT_4ON_PagoEfectivo() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.PENBT_4ON_PagoEfectivo,GlobalProperties.ON4_PIX_URL);
	}

	@Test(priority = 2)
	public void cpsGhanaBTBitolo_GHS() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.GHANA_Bitolo_GHS);
	}

	@Test(priority = 2)
	public void cpsGhanaBTKorapay_GHS() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.GHANA_Korapay_GHS,GlobalProperties.KorapayURL);
	}

	@Test(priority = 2)
	public void cpsGhanaBTZotaPay_GHS() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.GHANA_ZotaPay_GHS,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsIndonesiaBT_XPay_IDR() {
		CPIndonesiaBankDeposit bank_d = myfactor.newInstance(CPIndonesiaBankDeposit.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.INDONESIA_XPay_IDR);
	}

	@Test(priority = 2)
	public void cpsIndonesiaBT_WowsPay_IDR() {
		CPIndonesiaBankDeposit bank_d = myfactor.newInstance(CPIndonesiaBankDeposit.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.INDONESIA_WowsPay_IDR);
	}
	@Test(priority = 2)
	public void cpsIndonesiaBT_ZotaPay_IDR() {
		CPIndonesiaBankDeposit bank_d = myfactor.newInstance(CPIndonesiaBankDeposit.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.INDONESIA_ZotaPay_IDR,GlobalProperties.ZotapayURL);
	}
	@Test(priority = 2)
	public void cpsIndonesiaBT_PaymentAsia_IDR() {
		CPIndonesiaBankDeposit bank_d = myfactor.newInstance(CPIndonesiaBankDeposit.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.INDONESIA_PaymentAsia_IDR,GlobalProperties.PaymentAsiaURL);
	}

	@Test(priority = 2)
	public void cpsKenyaBTBitolo_KES() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.KENYA_Bitolo_KES);
	}

	@Test(priority = 2)
	public void cpsKenyaBTKorapay_KES() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.KENYA_Korapay_KES,GlobalProperties.KorapayURL);
	}

	@Test(priority = 2)
	public void cpsKenyaBTZotaPay_KES() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.KENYA_Zotapay_KES,GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void cpsKenyaBTUBank_KES_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.KENYA_UBank_KES);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_EWALLET_STICPAY)
	public void testSticPayNew() {
		CPNetellerPay sticpay = myfactor.newInstance(CPNetellerPay.class);
		depositNewWithURLCheck(sticpay, DEPOSITMETHOD.SticPayNew, GlobalProperties.STICPAYURL);
	}

	@Test(priority = 2)
	public void cpsMexicoBTBitolo_MXN() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.Bitolo_MXN);
	}

	@Test(priority = 2)
	public void cpsMexicoBT4ON_OXXo_MXN() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d,DEPOSITMETHOD.ON4_Oxxo_MXN);
	}

	@Test(priority = 2)
	public void cpsMexicoBTPagsmile_MXN() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.Pagsmile_MXN, GlobalProperties.PagsmileURL);
	}

	@Test(priority = 2)
	public void cpsTygapay() 	{
		CPNetellerPay bank_d = myfactor.newInstance(CPNetellerPay.class);
		depositNewWithURLCheck(bank_d,DEPOSITMETHOD.Tygapay,GlobalProperties.Tygapay);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_EWALLET_VOLET)
	public void testVoletNew() {
		CPFasaPay advcash = myfactor.newInstance(CPFasaPay.class);
		depositNewWithURLCheck(advcash,DEPOSITMETHOD.ADVCASH,GlobalProperties.AdvcashURL);
	}

	@Test(priority = 2)
	public void testApplePayNew() 	{
		CPCreditCardDeposit googlepay = myfactor.newInstance(CPCreditCardDeposit.class);
		depositNewProcessNoURLCheck(googlepay,DEPOSITMETHOD.APPLEPAYNew);
	}

	@Test(priority = 2)
	public void testGooglePayNew() 	{
		CPCreditCardDeposit googlepay = myfactor.newInstance(CPCreditCardDeposit.class);
		depositNewProcessNoURLCheck(googlepay,DEPOSITMETHOD.GOOGLEPAYNew);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_BITCOIN)
	public void testCryptoBITNew() throws InterruptedException {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOBTC);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_ERC20)
	public void testCryptoERCNew() throws InterruptedException {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOERCNew);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_ERC20)
	public void testCryptoERCCallBack() {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOERCNew);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_TRC20)
	public void testCryptoTRCNew() throws InterruptedException {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOTRCNew);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDC_ERC20)
	public void testCryptoUSDCERCNew() throws InterruptedException {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.USDCNew);
	}
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_USDT_BEP20)
	public void testDepositCryptoBEPNew() throws InterruptedException {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.CRYPTOBEPNew);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CRYPTO_ETH)
	public void testDepositCryptoETHNew() throws InterruptedException {
		testCPSCryptoCommonDepositNew(DEPOSITMETHOD.ETH_New);
	}

	@Test(priority = 2)
	public void cpsThailandBT_iSmartNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.THAIINSTANT_iSmartNew);
	}

	@Test(priority = 2)
	public void cpsThailandBT_XpayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.THAIINSTANT_XPayNew);
	}

	@Test(priority = 2)
	public void cpsThailandQR_Vtpay() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.THAILANDQR_Vtpay);
	}

	@Test(priority = 2)
	public void cpsThailandQR_Xpay() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.THAILANDQR_Xpay);
	}

	@Test(priority = 2)
	public void cpsThailandBT_ZotaPay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_THB, GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void testThailandBT_EeziePay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.THB_EeziePay);
	}

	@Test(priority = 2)
	public void testASICCCWorldpay_New() {
		CPLocalBankTrans asicCC = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcessNoDepositAmountCheck(asicCC, DEPOSITMETHOD.ASICWORLDPAY);
	}
	@Test(priority = 2)
	public void testASICCCCheckout_New() {
		CPLocalBankTrans asicCC = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcessNoDepositAmountCheck(asicCC, DEPOSITMETHOD.CCBRIDGERPAY);
	}
	@Test(priority = 2)
	public void cpsPaypal_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcessNoDepositAmountCheck(bank_d, DEPOSITMETHOD.Paypal);
	}
	@Test(priority = 2)
	public void testASIC_Bpay_New() {
		CPInterBankTrans bpay = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoDepositAmountCheck(bpay, DEPOSITMETHOD.BPay);
	}

	@Test(priority = 2)
	public void testASIC_PayID_New() {
		CPNetellerPay azuPay = myfactor.newInstance(CPNetellerPay.class);
		depositNewProcessNoDepositAmountCheck(azuPay, DEPOSITMETHOD.AZUPAY);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_JAPAN_BT)
	public void cpsJapanBT_New() {
		CPJapanBankDeposit bank_d = myfactor.newInstance(CPJapanBankDeposit.class);
		depositNewProcessNoDepositAmountCheck(bank_d, DEPOSITMETHOD.JAPANBT_TheJapanOne);
	}

	@Test(priority = 2)
	public void cpsHongKongBT_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcessNoDepositAmountCheck(bank_d, DEPOSITMETHOD.HONGKONGINSTANT);
	}
	@Test(priority = 2)
	public void cpsIndonesiaBT_XpayNew() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d,DEPOSITMETHOD.INDONESIA_XPay_IDR);
	}
	@Test(priority = 2)
	public void cpsIndonesiaBT_ZotaPayNew() 	{
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d,DEPOSITMETHOD.INDONESIA_ZotaPay_IDR);
	}

	@Test(priority = 2)
	public void testMyFatoorah_New() {
		CPCreditCardDeposit myfatoorah = myfactor.newInstance(CPCreditCardDeposit.class);
		depositNewWithURLCheck(myfatoorah, DEPOSITMETHOD.CCMYFATOORAH_New, GlobalProperties.MyFatoorahURL);
	}

	@Test(priority = 2)
	public void cpsInteracBTNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.INTERAC_New);
	}

	@Test(priority = 2)
	public void cpsNetherlandBT_Banxa_NL_New() {
		CPNetherlandsBankTrans bank_d = myfactor.newInstance(CPNetherlandsBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.BANXA_NL_New);
	}

	@Test(priority = 2)
	public void testAirTM_New() {
		CPFasaPay airTM = myfactor.newInstance(CPFasaPay.class);
		depositNewProcess(airTM, DEPOSITMETHOD.AirTM_New);
	}

	@Test(priority = 2)
	public void cpsIndiaP2PNew() {
		CPInterBankTrans bank_d = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(bank_d, DEPOSITMETHOD.IndiaP2P_New);
	}

	@Test(priority = 2)
	public void cpsJapanBTTheJapanOne_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcessNoURLCheck(bank_d, DEPOSITMETHOD.JAPANBT_TheJapanOne);
	}

	@Test(priority = 2)
	public void cpsJapanBT_ZotaPay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewWithURLCheck(bank_d, DEPOSITMETHOD.ZOTAPAY_JPY, GlobalProperties.ZotapayURL);
	}

	@Test(priority = 2)
	public void testEuBTNodaPay_New() {
		CPEuBankTrans bank_d = myfactor.newInstance(CPEuBankTrans.class);
		depositNewProcessNoURLCheck(bank_d, DEPOSITMETHOD.EUBT_NodaPay);
	}

	@Test(priority = 2)
	public void testUAEIBTPay_New() {
		CPInterBankTrans bank_d = myfactor.newInstance(CPInterBankTrans.class);
		depositNewProcessNoURLCheck(bank_d, DEPOSITMETHOD.UAEP2P);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_CC_PAYMENT_OPTION)
	public void testPaymentOptionJPY_New() {
		CPJapanBankDeposit bank_d = myfactor.newInstance(CPJapanBankDeposit.class);
		depositNewWithMailNoDepositAmount(bank_d, DEPOSITMETHOD.PaymentOption_New);
	}

	@Test(priority = 2)
	public void cpsJapanEmoney_New() {
		CPJapanBankDeposit bank_d = myfactor.newInstance(CPJapanBankDeposit.class);
		depositNewWithMailNoDepositAmount(bank_d, DEPOSITMETHOD.JAPANEMONEY_New);
	}

	@Test(priority = 2)
	public void cpsJapanKonbini_New() {
		CPJapanBankDeposit bank_d = myfactor.newInstance(CPJapanBankDeposit.class);
		depositNewWithMailNoDepositAmount(bank_d, DEPOSITMETHOD.JAPANKONBINI_New);
	}

	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_JAPAN_BT)
	public void cpsJapanBT_Payeasy_New() {
		CPJapanBankDeposit bank_d = myfactor.newInstance(CPJapanBankDeposit.class);
		depositNewWithMailNoDepositAmount(bank_d, DEPOSITMETHOD.JAPANBT_Payeasy);
	}

	@Test(priority = 2)
	public void testMalaysiaBT_EeziePay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.Malaysia_EeziePay);
	}

	@Test(priority = 2)
	public void testMultiexcNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.Multiexc);
	}

	@Test(priority = 2)
	public void testWowsPay_QR_Gopay_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.WowsPay_QR_Gopay);
	}

	@Test(priority = 2)
	public void cpsLaosBT_ThunderXpay_Lak_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.LAOSINSTANT);
	}

	@Test(priority = 2)
	public void cpsAlipayBT_ChipPayNew() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.ChipPay_Alipay_CNY);
	}

	@Test(priority = 2)
	public void cpsBanxa_New() {
		CPLocalBankTrans bank_d = myfactor.newInstance(CPLocalBankTrans.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.BANXA);
	}

	@Test(priority = 2)
	public void cpsUnionPay_BitpayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Bitpay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_TeleportNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Teleport);
	}

	@Test(priority = 2)
	public void cpsUnionPay_ChipPayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_ChipPay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_ChinapayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Chinapay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_DynamicPayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_DynamicPay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_MpayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Mpay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_MyPayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_MyPay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_PasstopayNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Passtopay);
	}

	@Test(priority = 2)
	public void cpsUnionPay_Toppay2New() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Toppay2);
	}

	@Test(priority = 2)
	public void cpsUnionPay_uEnjoyNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_uEnjoy);
	}

	@Test(priority = 2)
	public void cpsUnionPay_Global_SmallNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Global_Small);
	}

	@Test(priority = 2)
	public void cpsUnionPay_Global_MediumNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Global_Medium);
	}

	@Test(priority = 2)
	public void cpsUnionPay_Global_LargeNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Global_Large);
	}

	@Test(priority = 2)
	public void cpsUnionPay_Global_VIPNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Global_VIP);
	}

	@Test(priority = 2)
	public void cpsUnionPay_Global_MinorityNew() {
		CPUnionPay bank_d = myfactor.newInstance(CPUnionPay.class);
		depositNewProcess(bank_d, DEPOSITMETHOD.UNIONPAY_Global_Minority);
	}

	@Test(priority = 2)
	public void testVirtualPayNew() {
		String city = "test city";
		String address = "test address";
		String postalCode = "12345";
		CPVirtualPay bank_d = myfactor.newInstance(CPVirtualPay.class);
		depositNewWithCCInfo(bank_d, DEPOSITMETHOD.CREDITORDEBIT_New, city, address, postalCode);
	}
	/**
	 * 测试信用卡入金支付信息
	 */
	public void depositAndCheckCCPaymentInfo(DepositBase deposit,DEPOSITMETHOD method)
	{
		fillDepositBasicInfoNew(deposit,method);
		String account = deposit.getValidAccount();
		String amount = "50";

		// 入金
		deposit.depositWithOutPay(account,amount,method);

		deposit.setDepositMethod(method);
		// 检查入金支付页详情

		deposit.checkCCEwalletPaymentDetails(account,amount,method);

		deposit.payNowCC();
//		填入信用卡信息
		String frameName = "iFrameResizer0";
		if (Brand.equalsIgnoreCase("UM")){
			frameName="iFrameResizer01";
		}
		deposit.goToIframe(frameName);
		String cardNum = "4716171623738107";
		String expireDate = "07/2029";
		String cvv = "020";
		deposit.fillCCForm(cardNum, expireDate, cvv);
		deposit.exitIframe();

	}

	@Test(priority = 2)
	public void testDepositDeductedMaxAmount(){

		//CP > deposit with one channel
		CPCryptoDeposit crypto = myfactor.newInstance(CPCryptoDeposit.class);
		double beforeDepositMaxAmount = depositNewProcessNoURLCheck_checkDepoMaxAmount(crypto, DEPOSITMETHOD.ETH);
		GlobalMethods.printDebugInfo("Completed Deposit...");

		//Admin > Deposit Audit page - approve deposit
		JSONObject firstDepResult=adminPaymentAPI.apiDPAuditApproveFirstSuccess(account,"-1");

//		adminPaymentAPI.apiDPAuditApprove(firstDepResult.getString("id"),firstDepResult.getString("actualAmount"));
		double actualDepoAmount = Double.parseDouble(firstDepResult.getString("actualAmount"));

		//CP > Navigate to Deposit Funds Page - get deposit max amount after deposit approved
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		crypto.selectAccountNew(account);
		double afterDepositMaxAmount = crypto.getConvertedDepositMaxAmount();

		//check before & after deposit approved, deposit max amount is deducted as expected
		double diffDepoAmount = beforeDepositMaxAmount - afterDepositMaxAmount;
		Assert.assertEquals(actualDepoAmount,diffDepoAmount, "Deposit limit deducted failed, please check.");
		GlobalMethods.printDebugInfo("Original Deposit Max Amount: " + beforeDepositMaxAmount + ", after approved deposit: " + actualDepoAmount + ", Latest Deposit Max Amount: " + afterDepositMaxAmount);
		}
	//UnionPay入金未达限额弹框提示
	@Test(priority = 2, description = testCaseDescUtils.CPDEPOSIT_MIN_AMOUNT_POPUP_WINDOW)
	public void testMinDepositAmountPopupWindow() {
		DepositBase deposit = myfactor.newInstance(DepositBase.class);
		GlobalProperties.DEPOSITMETHOD method= DEPOSITMETHOD.UNIONPAY_Global_Large;

		LogUtils.info("Start test: 入金渠道" + method.getdepositDatetestid());
		depositCheckUnionPayPopUpWindow(deposit, method);
		checkPaymentRedirect(deposit);

	}
	//入金渠道-IBT进阶审核弹窗
	@Test(description = testCaseDescUtils.CP2FA_POPUP_CHECK)
	public void depositApprovePopupCheck(){

		DepositBase deposit = myfactor.newInstance(DepositBase.class);
		GlobalProperties.DEPOSITMETHOD method= DEPOSITMETHOD.IBT_EQUALS;
		if (TestEnv.equalsIgnoreCase("uat")){
			method= DEPOSITMETHOD.IBT_EQUALS_UAT;
		}
		LogUtils.info("Start test: 入金渠道" + method.getdepositDatetestid());
		depositCheckIBTPopUpWindow(deposit, method);


	}
}