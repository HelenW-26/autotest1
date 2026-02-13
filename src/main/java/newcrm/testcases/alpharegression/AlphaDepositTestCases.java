package newcrm.testcases.alpharegression;

import static newcrm.utils.testCaseDescUtils.CPDEPOSIT_GOLD_FLOW;
import static org.testng.Assert.assertNotNull;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalProperties;
import com.alibaba.fastjson.JSONObject;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import utils.Listeners.MethodTracker;
import utils.LogUtils;

import java.util.Map;

public class AlphaDepositTestCases extends DepositTestCases {

	private String openAPI;
	private Object[][] adminData;
	private WebDriver driver;

	@Override
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
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
		Object data[][] = TestDataProvider.getAlphaRegUsersData(brand, server);
		adminData = TestDataProvider.getAlphaRegUsersData(brand, server);

		assertNotNull(data);
		openAPI = (String) data[0][5];
		adminData = TestDataProvider.getAlphaRegUsersData(brand, server);
		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
		launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
		WebDriver tempDriver = (WebDriver) context.getAttribute("driver");
		if (tempDriver != null) {
			this.driver = tempDriver;
			context.setAttribute("driver", this.driver);
			LogUtils.info("beforMethod 中 driver 初始化成功");
		} else {
			LogUtils.error("beforMethod 中 driver 初始化失败", null);
		}
	}

	@Test(description = testCaseDescUtils.CPDEPOSIT_ST,groups = {"CP_Deposit"})
	public void testDeposit() {

		try {

			// MethodTracker for 多层API call
			if("mo".equalsIgnoreCase(Brand)) {
				MethodTracker.trackMethodExecution(this, "testInterBankTransPayNew", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testCryptoUSDCERCNew", true, null);
//				MethodTracker.trackMethodExecution(this, "testBridgePayNew", true, null, driver);

			}
			else if("um".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testInterBankTransPayNew", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testCryptoERCNew", true, null,driver);
//				MethodTracker.trackMethodExecution(this, "testBridgePayNew", true, null,driver);

			}
			else if("vjp".equalsIgnoreCase(Brand))
			{
				//vjp no ibt
				MethodTracker.trackMethodExecution(this, "testPaymentOptionJPY_New", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testDepositCryptoETHNew", true, null,driver);

			}
			else if("star".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testInterBankTransPayNew", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testDepositCryptoBEPNew", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testCCGoogleApplePayNew", true, null,driver);

			}
			else if("vfx".equalsIgnoreCase(Brand) | "au".equalsIgnoreCase(Brand) | BRAND.PUG.toString().equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testInterBankTransPayNew", true , null,driver);
//				MethodTracker.trackMethodExecution(this, "testBridgePayNew", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testCryptoTRCNew", true, null,driver);
			}
			else if("vt".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testInterBankTransPayNew", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testCryptoERCNew", true, null,driver);
//				MethodTracker.trackMethodExecution(this, "testBridgePayNew", true, null,driver);


			}
			else{
				MethodTracker.trackMethodExecution(this, "testInterBankTransPay", true, null,driver);
				MethodTracker.trackMethodExecution(this, "testDepositCryptoTRC", true, null,driver);
			}

			MethodTracker.checkResultFail();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Test(description = testCaseDescUtils.CPDEPOSIT_ST,groups = {"CP_Deposit_CPSCallBack"})
	public void depositwithCallBack() {

		try {

			// MethodTracker for 多层API call
			if("mo".equalsIgnoreCase(Brand)) {

				MethodTracker.trackMethodExecution(this, "testNetellerPayWithCallbackNew", true, null,driver,openAPI);

			}
			else if("um".equalsIgnoreCase(Brand))
			{

				MethodTracker.trackMethodExecution(this, "testCPSCryptoCommonDepositCallBack", true, null,driver,openAPI);

			}
			else if("vjp".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testCPSCryptoCommonDepositCallBack", true, null,driver,openAPI);

			}
			else if("star".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testNetellerPayWithCallbackNew", true, null,driver,openAPI);

			}
			else if("vfx".equalsIgnoreCase(Brand) | "au".equalsIgnoreCase(Brand) )
			{
				MethodTracker.trackMethodExecution(this, "testSkrillPayWithCallBackNew", true, null,driver,openAPI);
			}
			else if("vt".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testBinancePayWithCallBackNew", true, null,driver,openAPI);

			}
			else if("pug".equalsIgnoreCase(Brand)){
				MethodTracker.trackMethodExecution(this, "testCryptoETHPayWithCallbackNew", true, null,driver,openAPI);
			}

			MethodTracker.checkResultFail();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	@Test(description = testCaseDescUtils.CPDEPOSIT_INPUT_INFO_CHECK)
	public void depositInputDisplayedInfoCheck() {

		try {
			testInputPageInfoCheck();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 验证入金页面信息
	@Test(description = testCaseDescUtils.CPDEPOSIT_ST)

	public void testVerificationDepositPageCheck() {


		AdminAPIPayment adminPaymentAPI =  new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String) adminData[0][0]), (String) adminData[0][7], (String) adminData[0][8], GlobalProperties.BRAND.valueOf(Brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

		JSONObject exchangeRate =adminPaymentAPI.getExchangeRate();
		LogUtils.info("exchangeRate:"+exchangeRate);
		// 将 JSONObject 转换为 Map
		Map<String, Object> exchangeRateMap = exchangeRate.getInnerMap();;

		LogUtils.info("exchangeRateMap:"+exchangeRateMap);

		try {

			// MethodTracker for 多层API call
			if("mo".equalsIgnoreCase(Brand)) {
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_MalaysiaXPAYNew",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_ETH", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_BINANCE", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_I12BankTransferNew", true, null);
			}
			else if("um".equalsIgnoreCase(Brand))
			{
				//传入汇率
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_ZOTAPAYPHPNew",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_USDC", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_CREDITORDEBIT", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null);

			}
			else if("vjp".equalsIgnoreCase(Brand))
			{
				//vjp no ibt
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_JAPANBTTheJapanOne",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_DefaultUSDC", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_PaymentOption", true, null);
				 MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_CREDITORDEBIT", true, null);
			}
			else if("star".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_VietnamInstantNew",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_DefaultUSDC", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_CREDITORDEBIT", true, null);

			}
			else if("vfx".equalsIgnoreCase(Brand) || "au".equalsIgnoreCase(Brand) )
			{
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_ZOTAPAYPHPNew",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_CryptoBIT", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_AppleGooglePay", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_ADVCASH", true, null);
				 MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null);

			}
			else if("vt".equalsIgnoreCase(Brand))
			{
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_MalaysiaXPAYNew",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_USDCNew", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_MalaysiaEWallet", true, null);
			} else if ("pug".equalsIgnoreCase(Brand)) {
				LogUtils.info("pug");
				MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_VietnamInstantNew",
						true, null,exchangeRateMap);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_CryptoERCNew", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null);
				MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_NETELLER", true, null);

			}

			MethodTracker.checkResultFail();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private Map<String, Object> getExchangeRate() {
		AdminAPIPayment adminPaymentAPI = new AdminAPIPayment(
				(String) adminData[0][4],
				GlobalProperties.REGULATOR.valueOf((String) adminData[0][0]),
				(String) adminData[0][7],
				(String) adminData[0][8],
				GlobalProperties.BRAND.valueOf(Brand.toUpperCase()),
				GlobalProperties.ENV.valueOf("UAT"));
		JSONObject exchangeRate =adminPaymentAPI.getExchangeRate();

		LogUtils.info("exchangeRate:"+exchangeRate);
		// 将 JSONObject 转换为 Map
		Map<String, Object> exchangeRateMap = exchangeRate.getInnerMap();;

		LogUtils.info("exchangeRateMap:"+exchangeRateMap);
		return exchangeRateMap;
	}
	@Test(description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositLBTPageCheck() {
		Map<String, Object> exchangeRate = getExchangeRate();
		Brand=Brand.toUpperCase();
		switch(Brand) {
			case "VFX":
			case "AU":
			case "UM":
				testVerificationDepositLBTPageCheck_ZOTAPAYPHPNew(exchangeRate);
				break;
			case "MO":
			case "VT":
				testVerificationDepositLBTPageCheck_MalaysiaXPAYNew(exchangeRate);
				break;
			case "VJP":
				testVerificationDepositLBTPageCheck_JAPANBTTheJapanOne(exchangeRate);
				break;
			case "PUG":
			case "STAR":
				testVerificationDepositLBTPageCheck_VietnamInstantNew(exchangeRate);
				break;
		}
	}
	@Test(description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCryptoPageCheck() {
		Brand=Brand.toUpperCase();
		switch (Brand){
			case "MO":
				testVerificationDepositCryptoPageCheck_ETH();
				break;
			case "UM":
				testVerificationDepositCryptoPageCheck_USDC();
				break;
			case "VJP":
			case "STAR":
				testVerificationDepositCryptoPageCheck_DefaultUSDC();
				break;
			case "VFX":
			case "AU":
				testVerificationDepositCryptoPageCheck_CryptoBIT();
				break;
			case "VT":
				testVerificationDepositCryptoPageCheck_USDCNew();
				break;
			case "PUG":
				testVerificationDepositCryptoPageCheck_CryptoERCNew();
				break;


		}
	}
	@Test(description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositCreditCardPageCheck() {
		Brand=Brand.toUpperCase();
		switch (Brand){
			case "MO":
			case "STAR":
			case "VT":
			case "PUG":
				testVerificationDepositCCPageCheck_CreditDebit();
				break;
			case "VFX":
			case "AU":
				testVerificationDepositCCPageCheck_AppleGooglePay();
				break;
			case "VJP":
				testVerificationDepositCCPageCheck_PaymentOption();
				break;
		}
	}

	@Test(description =  testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)
	public void testVerificationDepositEWalletPageCheck() {
		Brand=Brand.toUpperCase();
		switch (Brand){
			case "MO":
				testVerificationDepositEWalletPageCheck_BINANCE();
				break;
			case "UM":
			case "VJP":
			case "STAR":
				testVerificationDepositEWalletPageCheck_CREDITORDEBIT();
				break;
			case "VFX":
			case "AU":
				testVerificationDepositEWalletPageCheck_ADVCASH();
				break;
			case "PUG":
				testVerificationDepositEWalletPageCheck_NETELLER();
				break;
		}
	}
	@Test(description = testCaseDescUtils.CPDEPOSIT_WITHDRAW_PAGE_CHECK)

	public void testVerificationDepositOfflineCheck() {
		Brand=Brand.toUpperCase();
		switch (Brand){
			case "MO":
				testVerificationDepositOfflineCheck_I12BankTransferNew();
				break;
			case "UM":
			case "VFX":
			case "AU":
			case "VT":
			case "PUG":
				testVerificationDepositOfflineCheck_IBTEquals();
				break;
		}

	}

	//AU golden flow, check max deposit amount after succeed deposit
	@Test(description = CPDEPOSIT_GOLD_FLOW)
	public void depositDeductedMaxAmount(){
		if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString())){
			testDepositDeductedMaxAmount();
		}else {
			throw new SkipException("Skipping this test intentionally.");
		}
	}
	}

