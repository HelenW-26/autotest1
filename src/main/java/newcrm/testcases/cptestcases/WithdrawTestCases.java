package newcrm.testcases.cptestcases;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static utils.StringUtil.safeConvertToDouble;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.business.businessbase.*;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.cpapi.PCSAPIDeposit;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.pages.clientpages.PaymentDetailsPage.Card;
import newcrm.pages.clientpages.WithdrawBasePage;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import tools.ScreenshotHelper;
import utils.CustomAssert;
import utils.LogUtils;


public class WithdrawTestCases extends BaseTestCaseNew {
	protected EmailDB emailDB;
	protected AdminAPIPayment adminPaymentAPI;
	PCSAPIDeposit pcsapi;
	protected Object data[][];

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = (Object[][]) data;
	}



    private Factor myfactor;
    private CPLogin login;
    private WebDriver driver;
    private String TraderName;
    private String TraderPass;
	/*@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand"})
	public void initiEnv(String brand, ITestContext context) throws Exception{
		GlobalMethods.setEnvValues(brand);
		if (BaseTestCase.TraderURL != null) {
			new CPAPIWithdraw(BaseTestCase.TraderURL, BaseTestCase.TraderName, BaseTestCase.TraderPass);
		}		
	}*/
    @BeforeMethod(alwaysRun = true)
    public void initMethod(){
        if (myfactor == null){
            myfactor = getFactorNew();
        }
        if (login == null){
            login = getLogin();
        }
        if (driver ==null){
            driver = getDriverNew();
        }
        if(TraderName ==null || TraderPass == null){
            BaseTestCaseNew.UserConfig user = getConfigNew();
			if (user != null){
				TraderName = user.TraderName;
				TraderPass = user.TraderPass;
			}

        }
    }

    @AfterClass(alwaysRun = true)
	@Parameters(value= {"Brand"})
	public void quitEnv(String brand, ITestContext context) throws Exception{
		if(TraderURL!=null && TestEnv.equalsIgnoreCase("PROD")) {
			new CPAPIWithdraw(TraderURL,TraderName,TraderPass);
		}
		//driver.quit();
	}

	
	private void testWithdrawWithEmail(CPSkrillWithdraw instance, DEPOSITMETHOD method) {
		// test data
		double amount = 0;
		String email = "withdraw@test.com";
		String notes = "auto test";
		String account = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		// switch to cp portal if the account is IB type
		menu.goToMenu(CPMenuName.CPPORTAL);
		
		//some user loading long time due to many accounts
		instance.waitSpinnerLoading();
		
		// Navigate to withdraw
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = instance.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: did not have this currency type account");

		// get balance
		amount = instance.getBalance(account);
		Assert.assertTrue((int)amount > 200, "Balance of account "+ account +" is less than 200!");
		
		//amount = (int) (100 + Math.random() * (amount - 100));
		amount = (int) (100 + Math.random() * 100);

		instance.setAccountAndAmount(account, amount);

	    instance.clickContinue();
		instance.waitCustomiseLoading(120);
		instance.closeWithdrawalOTPWindow();

		instance.setWithdrawMethod(method);
		instance.waitSpinnerLoading();

		if(GlobalProperties.brand.equalsIgnoreCase("vfx") || GlobalProperties.brand.equalsIgnoreCase("vt"))
		{

			instance.setWithdrawInfo(email, notes);
			instance.submitWithoutCheck();
			if(GlobalProperties.env.equalsIgnoreCase("prod"))
			{
				setCode(emailDB);
				Assert.assertTrue(instance.setCodeSubmit(),"Withdraw submit failure or error occurs!");
			}
		}
		else
		{
			if(GlobalProperties.brand.equalsIgnoreCase("star") || GlobalProperties.brand.equalsIgnoreCase("mo") || GlobalProperties.brand.equalsIgnoreCase("vjp"))
			{
				setCode(emailDB);
			}
			Assert.assertTrue(instance.setWithdrawInfoAndSubmit(email, notes),
					"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		}

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, method, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");
	}

	private void testWithdrawWithEmailNew(CPSkrillWithdraw instance, DEPOSITMETHOD method, boolean newAccount) {
		// test data
		double amount = 0, balance = 0;
		String email = "withdraw@test.com";
		String notes = "auto test";
		String account = null, acctCurrency = null;
		List<String> savedEwalletAcct = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		// switch to cp portal if the account is IB type
		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		//some user loading long time due to many accounts
		instance.waitSpinnerLoading();

		// Navigate to withdraw
		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = instance.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		balance = Double.parseDouble(accSelected.getBalance());
		acctCurrency = accSelected.getCurrency();

		// get balance and check if balance less than 100, then make cash adjustment
		//checkBalanceAndCashAdjustment(account, acctCurrency, balance);
		Assert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
		//withdrawal amount
		amount = (int) (100 + Math.random() * 100);

		instance.setAccountAndAmountNew(account, amount);

		instance.clickContinue();
		instance.waitCustomiseLoading(120);
		instance.closeWithdrawalOTPWindow();

		instance.setWithdrawMethodNew(method);
		instance.waitSpinnerLoading();

		if (instance.checkSavedAcctDropdown()) {
			if (newAccount) {
				instance.chooseAddNewEwalletAccount();
				instance.setWithdrawInfo(email, notes);
			} else {
				savedEwalletAcct = instance.getSavedEwalletAccount();
				if (savedEwalletAcct == null) {
					instance.chooseAddNewEwalletAccount();
					instance.setWithdrawInfo(email, notes);
				} else {
					instance.chooseSavedEwalletAccount(savedEwalletAcct.get(0));
				}
			}
		} else {
			instance.setWithdrawInfo(email, notes);
		}

		if(GlobalProperties.brand.equalsIgnoreCase("vfx") || GlobalProperties.brand.equalsIgnoreCase("vt")) {
			instance.submitWithoutCheck();
		} else {
			Assert.assertTrue(instance.submitWithCheck(),"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		}

		System.out.println("***Check Transaction History***");
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, method, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");
	}

	@Test(priority = 0,groups= {"AuRegression"})
	public void testSkrillWithdraw() throws InterruptedException {
		CPSkrillWithdraw skrill = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(skrill, DEPOSITMETHOD.SKRILL);
	}

	@Test(priority = 0)
	public void testNetellerWithdraw() {
		CPSkrillWithdraw neteller = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(neteller, DEPOSITMETHOD.NETELLER);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_EWALLET_STICPAY)
	public void testSticPayWithdraw() {
		CPSkrillWithdraw sticpay = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(sticpay, DEPOSITMETHOD.STICPAY);
	}

	@Test(priority = 0)
	public void testAstropayWithdraw() throws InterruptedException {
		CPSkrillWithdraw astropay = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(astropay, DEPOSITMETHOD.ASTROPAY);
	}
	
	@Test(priority = 0)
	public void testEbuyWithdraw() throws InterruptedException {
		CPEbuyWithdraw ebuy = myfactor.newInstance(CPEbuyWithdraw.class);
		testWithdrawWithEmail(ebuy, DEPOSITMETHOD.EBuyTRANSFER);
	}
	
	@Test(priority = 0)
	public void testPerfectMoneyWithdraw() throws InterruptedException {
		CPSkrillWithdraw perfectMoney = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(perfectMoney, DEPOSITMETHOD.PERFECTMONEY);
	}

	@Test(priority = 0)
	public void testAdvcashWithdraw() throws InterruptedException {
		CPSkrillWithdraw advcash = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(advcash, DEPOSITMETHOD.ADVCASH);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_EWALLET_BITWALLET)
	public void testBitWalletWithdraw() throws InterruptedException {
		CPSkrillWithdraw bitwallet = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(bitwallet, DEPOSITMETHOD.BITWALLET);
	}
	
	@Test(priority = 0)
	public void testFasaPayWithdraw() throws InterruptedException {
		CPSkrillWithdraw fasapay = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(fasapay, DEPOSITMETHOD.FASAPAY);
	}
	
	@Test(priority = 0)
	public void testAirTmWithdraw() throws InterruptedException {
		CPSkrillWithdraw airtm = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(airtm, DEPOSITMETHOD.AirTM);
	}
	
	@Test(priority = 0)
	public void testFXIRWithdraw() throws InterruptedException {
		DEPOSITMETHOD method = DEPOSITMETHOD.FXIR;
		CPFXIRWithdraw instance = myfactor.newInstance(CPFXIRWithdraw.class);
		//testWithdrawWithEmail(fxir, DEPOSITMETHOD.FXIR);

		// test data
		double amount = 0;
		String email = "withdraw@test.com";
		String notes = "auto test";
		String account = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		// switch to cp portal if the account is IB type
		menu.goToMenu(CPMenuName.CPPORTAL);

		//some user loading long time due to many accounts
		instance.waitSpinnerLoading();

		// Navigate to withdraw
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = instance.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: did not have this currency type account");

		// get balance
		amount = instance.getBalance(account);
		Assert.assertTrue((int)amount > 200, "Balance of account "+ account +" is less than 200!");

		//amount = (int) (100 + Math.random() * (amount - 100));
		amount = (int) (100 + Math.random() * 100);

		instance.setAccountAndAmount(account, amount);

		instance.clickContinue();
		instance.waitCustomiseLoading(120);

		instance.setWithdrawMethod(method);
		instance.waitSpinnerLoading();

		if(GlobalProperties.brand.equalsIgnoreCase("vfx") || GlobalProperties.brand.equalsIgnoreCase("vt"))
		{

			instance.setWithdrawInfo(email, notes);
			instance.submitWithoutCheck();
			if(GlobalProperties.env.equalsIgnoreCase("prod"))
			{
				setCode(emailDB);
				Assert.assertTrue(instance.setCodeSubmit(),"Withdraw submit failure or error occurs!");
			}
		}
		else
		{
			if(GlobalProperties.brand.equalsIgnoreCase("star") || GlobalProperties.brand.equalsIgnoreCase("mo") || GlobalProperties.brand.equalsIgnoreCase("vjp"))
			{
				setCode(emailDB);
			}
			Assert.assertTrue(instance.setWithdrawInfoAndSubmit(email, notes),
					"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		}

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, method, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");
	}
	
	@Test(priority = 0)
	public void testJapanBTEmailWithdraw() throws InterruptedException {
		CPSkrillWithdraw japanemail = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(japanemail, DEPOSITMETHOD.JAPANBTEMAIL);
	}

	@Test(priority = 0)
	public void testAirTMWithdraw() {
		CPSkrillWithdraw airTM = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(airTM, DEPOSITMETHOD.AirTM);
	}

	@Test(priority = 0)
	public void testBkashWithdraw() {
		CPSkrillWithdraw bkash = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(bkash, DEPOSITMETHOD.BDTBkash);
	}

	@Test(priority = 0)
	public void testRocketWithdraw() {
		CPSkrillWithdraw rocket = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmail(rocket, DEPOSITMETHOD.BDTRocket);
	}

	private void testOtherBankWithdraw(CPLocalBankWithdraw bank_w, DEPOSITMETHOD wType, boolean newAccount) {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		// test data
		double amount = 0;
		String branch = "perfect street branch";
		String city = "test city";
		String province = "test province";
		String ifsc = "test ifsc";
		String acc_name = "Test account name";
		String bank_acc_num = "3465673";
		String notes = "auto test";
		String accdigit = "123123";
		String docid = "123123";
		String swift_code = "test swift code";
		String account = null;
		List<String> savedBankAcct = null;

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.refresh();
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = bank_w.getBalance(account);
		//amount = (int) (50 + Math.random() * (amount - 50));
		amount = (int) (50 + Math.random());

		bank_w.setAccountAndAmount(account, amount);

		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();

		bank_w.setWithdrawMethod(wType);
		bank_w.waitSpinnerLoading();

		//check if the saved account dropdown is available or not
		if (bank_w.checkSavedAcctDropdown())
		{			LogUtils.info("the saved account dropdown is available");

			//if got, then
			if (newAccount)
			{
				bank_w.chooseAddNewLBTAccount();
				bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
			}
			else {
				savedBankAcct = bank_w.getSavedBankAccount();
				if (savedBankAcct == null) {
					bank_w.chooseAddNewLBTAccount();
					bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
				} else {
					bank_w.selectSavedBankAccount(savedBankAcct.get(0));
				}
			}
		} else {
			bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
		}
		
		Assert.assertTrue(bank_w.submit(), "Submit failure or error occurs");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, wType, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + wType.getWithdrawName() + " withdraw succeed!!********");

	}

	private void testOtherBankWithdrawChangeRateCheck(CPLocalBankWithdraw bank_w, DEPOSITMETHOD wType, boolean newAccount,double changeRate) {
		// Instances
//		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		// test data
		double amount = 0;
		String branch = "perfect street branch";
		String city = "test city";
		String province = "test province";
		String ifsc = "test ifsc";
		String acc_name = "Test account name";
		String bank_acc_num = "3465673";
		String notes = "auto test";
		String accdigit = "123123";
		String docid = "123123";
		String swift_code = "test swift code";
		String account = null;
		List<String> savedBankAcct = null;

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.refresh();
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = bank_w.getBalance(account);
		//amount = (int) (50 + Math.random() * (amount - 50));
		amount = (int) (200 + Math.random());

		bank_w.setAccountAndAmount(account, amount);

		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();

		bank_w.setWithdrawMethod(wType);
		bank_w.waitSpinnerLoading();
		//检查汇率选项
		double rate=bank_w.getChangeRate() ;
		CustomAssert.assertEquals(rate,changeRate,"The change rate is not equal to the expected value");
		//check if the saved account dropdown is available or not
		if (bank_w.checkSavedAcctDropdown())
		{
			//if got, then
			if (newAccount)
			{
				LogUtils.info("The user is newAccount!");

				bank_w.chooseAddNewLBTAccount();
				bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
			}
			else {
				LogUtils.info("The user is oldAccount!");

				savedBankAcct = bank_w.getSavedBankAccount();
				if (savedBankAcct == null) {
					LogUtils.info("The user does not have any saved account!");

					bank_w.chooseAddNewLBTAccount();
					bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
				} else {
					LogUtils.info("The user has saved account!");

					bank_w.selectSavedBankAccount(savedBankAcct.get(0));
				}
			}
		} else {
			LogUtils.info("the saved account dropdown is not available");

			bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
		}
		CustomAssert.assertTrue(bank_w.submit(), "Submit failure or error occurs");
		String withdrawDetail = bank_w.getWithdrawalDetails();
		CustomAssert.assertTrue(withdrawDetail.contains(String.valueOf(amount))&&withdrawDetail.contains(account),"The withdraw detail does not contain branch");

		CustomAssert.assertAll();
		System.out.println(
				"***Test " + wType.getWithdrawName() + " withdraw succeed!!********");

	}

    private void testOtherBankWithdrawNew(CPLocalBankWithdraw bank_w, DEPOSITMETHOD wType, boolean newAccount) {
        // Instances
        CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
        CPMenu menu = myfactor.newInstance(CPMenu.class);

        // test data
        double amount = 0, balance = 0;
        String branch = "perfect street branch";
        String city = "test city";
        String province = "test province";
        String ifsc = "test ifsc";
        String acc_name = "Test account name";
        String bank_acc_num = "3465673";
        String notes = "auto test";
        String accdigit = "123123";
        String docid = "123123";
        String swift_code = "test swift code";
        String account = null, acctCurrency = null;
        List<String> savedBankAcct = null;

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
        menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = bank_w.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		balance = Double.parseDouble(accSelected.getBalance());
		acctCurrency = accSelected.getCurrency();

		//get balance and check if balance less than 100 and make cash adjustment
		//checkBalanceAndCashAdjustment(account, acctCurrency, balance);

		//withdrawal amount
        amount = (int) (100 + Math.random() * 100);

        bank_w.setAccountAndAmountNew(account, amount);
        bank_w.clickContinue();
        bank_w.waitSpinnerLoading();
        bank_w.closeWithdrawalOTPWindow();

        bank_w.setWithdrawMethodNew(wType);
        bank_w.waitSpinnerLoading();

		//check if the saved account dropdown is available or not
        if (bank_w.checkSavedAcctDropdown())
		{
			//if got, then
			if (newAccount)
			{
				bank_w.chooseAddNewLBTAccount();
				bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
			}
			else {
				savedBankAcct = bank_w.getSavedBankAccountNew();
				if (savedBankAcct == null) {
					bank_w.chooseAddNewLBTAccount();
					bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
				} else {
					bank_w.selectSavedBankAccountNew(savedBankAcct.get(0));
				}
			}
		} else {
			bank_w.addNewBankAccount(branch, acc_name, bank_acc_num, city, province, ifsc, notes, accdigit, docid, swift_code);
		}

        Assert.assertTrue(bank_w.submit(), "Submit failure or error occurs!");

		System.out.println("***Check Transaction History***");
        menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
        Assert.assertTrue(
                history.checkWithdrawAndCancelOrder(account, wType, String.valueOf(amount), STATUS.SUBMITTED),
                "Do not Find the withdraw in history page");
        System.out.println(
                "***Test " + wType.getWithdrawName() + " withdraw succeed!!********");

    }

	@Test(priority = 0)
	public void testKoreaBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.KOREAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testKoreaBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.KOREAINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testTaiwanBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.TAIWANINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testTaiwanBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.TAIWANINSTANT, true);
	}
	
	//PM confirmed that Ghana input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testGhanaBankWithdraw() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT, false);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT, false);
		}
	}
	
	@Test(priority = 0)
	public void testGhanaBankWithdrawNewAccount() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT, true);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT, true);
		}
	}
	
	@Test(priority = 0)
	public void testRwandaBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.RWANDAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testRwandaBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.RWANDAINSTANT, true);
	}

	@Test(priority = 0)
	public void testTanzaniaBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.TANZANIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testTanzaniaBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.TANZANIAINSTANT, true);
	}

	//PM confirmed that Uganda input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testUgandaBankWithdraw() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT, false);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT, false);
		}
	}
	
	@Test(priority = 0)
	public void testUgandaBankWithdrawNewAccount() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT, true);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT, true);
		}
	}

	@Test(priority = 0)
	public void testCameroonBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.CAMEROONINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testCameroonBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.CAMEROONINSTANT, true);
	}

	//PM confirmed that Kenya input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testKenyaBankWithdraw() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT, false);
		}else {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT, false);
		}
	}
	
	@Test(priority = 0)
	public void testKenyaBankWithdrawNewAccount() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT, true);
		}else {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT, true);
		}
	}
	
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_MALAYSIA_BT)
	public void testMalaysiaBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.MALAYINSTANT, false);
	}
	
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_MALAYSIA_BT)
	public void testMalaysiaBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.MALAYINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testIndonesiaBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.INDONESIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testIndonesiaBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.INDONESIAINSTANT, true);
	}
	
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_VIETNAM_BT)
	public void testVietnamBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.VIETNAMINSTANT, false);
	}
	
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_VIETNAM_BT)
	public void testVietnamBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.VIETNAMINSTANT, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_VIETNAM_BT)
	public void testVietnamBankWithdrawNewAccountChangeRate(double changeRate) {
		CPVietnamBankWithdraw bank_w = myfactor.newInstance(CPVietnamBankWithdraw.class);
		testOtherBankWithdrawChangeRateCheck(bank_w, DEPOSITMETHOD.VIETNAMINSTANT, true, changeRate);

	}

	@Test(priority = 0)
	public void testSouthAfricaBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT, false);
	}

	@Test(priority = 0)
	public void testSouthAfricaBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testThaiBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.THAIINSTANT, false);
	}

	@Test(priority = 0)
	public void testThaiBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.THAIINSTANT, true);
	}

	@Test(priority = 0)
	public void testIndiaBankWithdraw() {
		CPIndiaBankWithdraw bank_w = myfactor.newInstance(CPIndiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.INDIAIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testIndiaBankWithdrawNewAccount() {
		CPIndiaBankWithdraw bank_w = myfactor.newInstance(CPIndiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.INDIAIAINSTANT, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_JAPAN_BT)
	public void testJapanBankWithdraw() {
		CPJapanBankWithdraw bank_w = myfactor.newInstance(CPJapanBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.JAPANIAINSTANT, false);
	}
	
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_JAPAN_BT)
	public void testJapanBankWithdrawNewAccount() {
		CPJapanBankWithdraw bank_w = myfactor.newInstance(CPJapanBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.JAPANIAINSTANT, true);
	}

	@Test(priority = 0)
	public void testPhilippinesBankWithdraw() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNewAccount() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT, true);
	}

	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNewAccountChangeRateCheck(double changeRate) {
		CPPhillipineBankWithdraw bank_w = myfactor.newInstance(CPPhillipineBankWithdraw.class);
		testOtherBankWithdrawChangeRateCheck(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT, true, changeRate);
	}


	@Test(priority = 0)
	public void testMexicoBankWithdraw() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.MEXICOINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testMexicoBankWithdrawNewAccount() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.MEXICOINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testBrazilBankWithdraw() {
		CPBrazilBankWithdraw bank_w = myfactor.newInstance(CPBrazilBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.BRAZILINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testBrazilBankWithdrawNewAccount() {
		CPBrazilBankWithdraw bank_w = myfactor.newInstance(CPBrazilBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.BRAZILINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testBrazilPIXWithdraw() {
		CPBrazilPIXWithdraw bank_w = myfactor.newInstance(CPBrazilPIXWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.BRAZILPIX, false);
	}
	
	@Test(priority = 0)
	public void testBrazilPIXWithdrawNewAccount() {
		CPBrazilPIXWithdraw bank_w = myfactor.newInstance(CPBrazilPIXWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.BRAZILPIX, true);
	}
	
	@Test(priority = 0)
	public void testHongKongBankWithdraw() {
		CPHongKongBankWithdraw bank_w = myfactor.newInstance(CPHongKongBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.HONGKONGINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testHongKongBankWithdrawNewAccount() {
		CPHongKongBankWithdraw bank_w = myfactor.newInstance(CPHongKongBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.HONGKONGINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testNigeriaBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.NIGERIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testNigeriaBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.NIGERIAINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testChileBankWithdraw() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.CHILEINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testChileBankWithdrawNewAccount() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.CHILEINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testColombiaBankWithdraw() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testColombiaBankWithdrawNewAccount() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testPeruBankWithdraw() {
		CPPeruBankWithdraw bank_w = myfactor.newInstance(CPPeruBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.PERUINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testPeruBankWithdrawNewAccount() {
		CPPeruBankWithdraw bank_w = myfactor.newInstance(CPPeruBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.PERUINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testLaosBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.LAOSINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testLaosBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.LAOSINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testMongoliaBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testMongoliaBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testZambialiaBankWithdraw() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testZambiaBankWithdrawNewAccount() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT, true);
	}
	
	@Test(priority = 0)
	public void testLocalDepositorWithdraw() {
		CPLocalDepositorWithdraw bank_w = myfactor.newInstance(CPLocalDepositorWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.LOCALDEPOSITOR, false);
	}
	
	@Test(priority = 0)
	public void testLocalDepositorWithdrawNewAccount() {
		CPLocalDepositorWithdraw bank_w = myfactor.newInstance(CPLocalDepositorWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.LOCALDEPOSITOR, true);
	}
	
	@Test(priority = 0)
	public void testUAEBankWithdraw() {
		CPUAEBankWithdraw bank_w = myfactor.newInstance(CPUAEBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.UAEINSTANT, false);
	}
	
	@Test(priority = 0)
	public void testUAEBankWithdrawNewAccount() {
		CPUAEBankWithdraw bank_w = myfactor.newInstance(CPUAEBankWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.UAEINSTANT, true);
	}

	@Test(priority = 0)
	public void testAzupayWithdraw() {
		CPAzupayWithdraw bank_w = myfactor.newInstance(CPAzupayWithdraw.class);
		testOtherBankWithdraw(bank_w, DEPOSITMETHOD.AZUPAY, false);
	}

	@Test(priority = 0)
	public void testPaypalWithdraw() {
		CPPaypalWithdraw paypal = myfactor.newInstance(CPPaypalWithdraw.class);
		testWithdrawWithEmailNew(paypal, DEPOSITMETHOD.Paypal, true);
	}
	
	@Test(priority = 0)
	public void testInteracWithdraw() {
		// Instances
		CPInternationalBankWithdraw bank_w = myfactor.newInstance(CPInternationalBankWithdraw.class);
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		// test data
		double amount = 0;
		String notes = "auto test";
		String account = null;
		
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.refresh();
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = bank_w.getBalance(account);
		amount = (int) (100 + Math.random() * 100);
		bank_w.setAccountAndAmount(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.setWithdrawMethod(DEPOSITMETHOD.INTERAC);
		bank_w.waitSpinnerLoading();
		
		//if(GlobalProperties.brand.equalsIgnoreCase("mo")||GlobalProperties.brand.equalsIgnoreCase("pug"))
		if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}
		
		Assert.assertTrue(bank_w.submit(),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.INTERAC, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + DEPOSITMETHOD.INTERAC.getWithdrawHistoryName() + " withdraw succeed!!********");
	}

	@Test(priority = 0)
	public void testInteracWithdrawNew() {
		// Instances
		CPInteracWithdraw bank_w = myfactor.newInstance(CPInteracWithdraw.class);
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		// test data
		double amount = 0;
		String notes = "auto test";
		String account = null;
		String bankBeneName = "Test Auto Name";
		String bankAccNum = "999999";

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		//menu.refresh();
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = bank_w.getBalance(account);
		amount = (int) (100 + Math.random() * 100);
		bank_w.setAccountAndAmount(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.setWithdrawMethodNew(DEPOSITMETHOD.INTERAC_New);
		bank_w.waitSpinnerLoading();

		//if(GlobalProperties.brand.equalsIgnoreCase("mo")||GlobalProperties.brand.equalsIgnoreCase("pug"))
		if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}
		Assert.assertTrue(bank_w.setWithdrawInfo(bankBeneName, bankAccNum, notes), "Invalid Information");
		Assert.assertTrue(bank_w.submit(),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.INTERAC_New, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + DEPOSITMETHOD.INTERAC_New.getWithdrawName() + " withdraw succeed!!********");
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_INTER_BANK_TRANS)
	public void testInternationalBankWithdraw() {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPInternationalBankWithdraw bank_w = myfactor.newInstance(CPInternationalBankWithdraw.class);

		if(GlobalProperties.brand.equalsIgnoreCase("vt")) {
			bank_w = myfactor.newInstance(CPInternationBankCPSWithdraw.class);
		}
		
		// test data
		double amount = 0;
		String bankName = "Test IBT Bank Name";
		String bankaddress = "Test IBT Address 123";
		String bankBeneName = "Test IBT Beneficiary Name";
		String bankAccNum = "999999";
		String bankHolderAddress= "Test IBT Holder Address 666";
		String bankSwiftCode = "30033";
		String bankABAcode = "8888";
		String notes = "auto test";
		String account = null;
		List<String> sel_bank = null;

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: did not have this currency type account");
		
		Assert.assertTrue((amount = bank_w.getBalance(account)) > 200, "Balance of account "+ account +" is less than 200!");
		amount = (int) (50 + Math.random());

		bank_w.setAccountAndAmount(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();
		bank_w.setWithdrawMethod(DEPOSITMETHOD.I12BANKTRANSFER);
		
		if(Brand.equalsIgnoreCase("vfx") || (Brand.equalsIgnoreCase("mo")) || (Brand.equalsIgnoreCase("star"))) 
		{
			double web_amount = bank_w.getWithdrawAccount();
			Assert.assertTrue(amount == web_amount, "Input amount is not equals post amount");
		}

		// if has a saved bank account,use it. otherwise add a new one
		sel_bank = bank_w.getSavedBankAccount();
		if (sel_bank == null) 
		{
			Assert.assertTrue(bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes), "Invalid Information");
		} 
		else 
		{
			Boolean hasAccount = false;
			for (String accNum : sel_bank) {
				if (bank_w.selectSavedBankAccount(accNum)) {
					hasAccount = true;
					break;
				}
			}
			if (!hasAccount) {
				GlobalMethods.printDebugInfo("Client does not have saved international bank info. Add a new one");
				Assert.assertTrue(bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes), "Invalid Information");				
				bank_w.checkFileUploadingCompleted();
				
			} else {
				bank_w.setNotes(notes);
			}
		}
		bank_w.waitSpinnerLoading();
		
		/*if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			if(emailDB==null) {
				emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
			}

			setCode(emailDB);
		}*/
		
		Assert.assertTrue(bank_w.submit(), "Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.I12BANKTRANSFER, String.valueOf(amount), STATUS.SUBMITTED), "Do not Find the withdraw in history page");
		System.out.println("***Test " + DEPOSITMETHOD.I12BANKTRANSFER.getWithdrawName() + " withdraw succeed!!********");

	}

	public void testuatInternationalBankWithdraw() {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPInternationalBankWithdraw bank_w = myfactor.newInstance(CPInternationalBankWithdraw.class);

		if(GlobalProperties.brand.equalsIgnoreCase("vt")) {
			bank_w = myfactor.newInstance(CPInternationBankCPSWithdraw.class);
		}

		// test data
		double amount = 0;
		String bankName = "Test IBT Bank Name";
		String bankaddress = "Test IBT Address 123";
		String bankBeneName = "Test IBT Beneficiary Name";
		String bankAccNum = "999999";
		String bankHolderAddress= "Test IBT Holder Address 666";
		String bankSwiftCode = "30033";
		String bankABAcode = "8888";
		String notes = "auto test";
		String account = null;
		List<String> sel_bank = null;

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: did not have this currency type account");

		Assert.assertTrue((amount = bank_w.getBalance(account)) > 200, "Balance of account "+ account +" is less than 200!");
		amount = (int) (50 + Math.random());

		bank_w.setAccountAndAmount(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();
		bank_w.setWithdrawMethod(DEPOSITMETHOD.I12BANKTRANSFERUAT);

		if(Brand.equalsIgnoreCase("vfx") || (Brand.equalsIgnoreCase("mo")) || (Brand.equalsIgnoreCase("star")))
		{
			double web_amount = bank_w.getWithdrawAccount();
			Assert.assertTrue(amount == web_amount, "Input amount is not equals post amount");
		}

		// if has a saved bank account,use it. otherwise add a new one
		sel_bank = bank_w.getSavedBankAccount();
		if (sel_bank == null)
		{
			Assert.assertTrue(bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes), "Invalid Information");
		}
		else
		{
			Boolean hasAccount = false;
			for (String accNum : sel_bank) {
				if (bank_w.selectSavedBankAccount(accNum)) {
					hasAccount = true;
					break;
				}
			}
			if (!hasAccount) {
				GlobalMethods.printDebugInfo("Client does not have saved international bank info. Add a new one");
				Assert.assertTrue(bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes), "Invalid Information");
				bank_w.checkFileUploadingCompleted();

			} else {
				bank_w.setNotes(notes);
			}
		}
		bank_w.waitSpinnerLoading();

		/*if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			if(emailDB==null) {
				emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
			}

			setCode(emailDB);
		}*/

		Assert.assertTrue(bank_w.submit(), "Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.I12BANKTRANSFER, String.valueOf(amount), STATUS.SUBMITTED), "Do not Find the withdraw in history page");
		System.out.println("***Test " + DEPOSITMETHOD.I12BANKTRANSFER.getWithdrawName() + " withdraw succeed!!********");

	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_INTER_BANK_TRANS)
	public void testInternationalBankWithdrawNew() {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPInternationalBankWithdraw bank_w = myfactor.newInstance(CPInternationalBankWithdraw.class);

		// test data
		double amount = 0, balance = 0;
		String bankName = "Test IBT Bank Name";
		String bankaddress = "Test IBT Address 123";
		String bankBeneName = "Test IBT Beneficiary Name";
		String bankAccNum = "999999";
		String bankHolderAddress= "Test IBT Holder Address 666";
		String bankSwiftCode = "30033";
		String bankABAcode = "8888";
		String notes = "auto test";
		String account = null, acctCurrency = null;
		List<String> sel_bank = null;

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = bank_w.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		balance = Double.parseDouble(accSelected.getBalance());
		acctCurrency = accSelected.getCurrency();

		//get balance and check if balance less than 100 and make cash adjustment
		//checkBalanceAndCashAdjustment(account, acctCurrency, balance);
		Assert.assertTrue(balance > 200, "Balance of account "+ account +" is less than 200!");
		amount = (int) (50 + Math.random());
		if (Brand.equalsIgnoreCase("pug")){
			amount = 100;
		}
		bank_w.setAccountAndAmountNew(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();

		bank_w.setWithdrawMethodNew(DEPOSITMETHOD.I12BANKTRANSFER_New);

		/*if(Brand.equalsIgnoreCase("vfx") || (Brand.equalsIgnoreCase("mo")) || (Brand.equalsIgnoreCase("star")))
		{
			double web_amount = bank_w.getWithdrawAccount();
			Assert.assertTrue(amount == web_amount, "Input amount is not equals post amount");
		}*/

		// if has a saved bank account,use it. otherwise add a new one
		if (bank_w.checkSavedAcctDropdown()) {
			sel_bank = bank_w.getSavedBankAccountNew();
			if (sel_bank == null)
			{
				GlobalMethods.printDebugInfo("No saved bank info. Will proceed to add a new one");
				bank_w.chooseAddNewIBTAccount();
				bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes);
//				Assert.assertTrue(bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes), "Invalid Information");
			}
			else
			{
				GlobalMethods.printDebugInfo("Has saved bank info. Will proceed to use saved bank info");
				bank_w.selectSavedBankAccountNew(sel_bank.get(0));
			}
		} else {
			Assert.assertTrue(bank_w.setWithdrawInfo(bankName, bankaddress, bankBeneName, bankAccNum, bankHolderAddress, bankSwiftCode, bankABAcode, notes), "Invalid Information");
		}
		bank_w.waitSpinnerLoading();

		/*if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			if(emailDB==null) {
				emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
			}

			setCode(emailDB);
		}*/

		Assert.assertTrue(bank_w.submit(), "Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		//Verify balance deduction
		System.out.println("***Check updated account balance***");
		menu.goToMenu(CPMenuName.HOME);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
//        Assert.assertEquals((balance - bank_w.getBalanceNew(account)), amount, "Failed to verify balance deduction after withdrawal!");

		System.out.println("***Check Transaction History***");
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdraw(account, DEPOSITMETHOD.I12BANKTRANSFER_New, String.valueOf(amount), STATUS.SUBMITTED), "Do not Find the withdraw in history page");
		System.out.println("***Test " + DEPOSITMETHOD.I12BANKTRANSFER_New.getWithdrawName() + " withdraw succeed!!********");

	}

	@Test(priority = 0)
	public void testAustraliaBankWithdraw() {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPAustraliaBankWithdraw bank_w = myfactor.newInstance(CPAustraliaBankWithdraw.class);

		// test data
		double amount = 0;
		String bankName = "Test Bank Name";
		String BSB = "34543";
		String BBname = "Test Beneficiary name";
		String bank_acc_num = "3465673";
		String swift = "30033";
		String notes = "Auto test Australia Bank Withdraw";
		String account = null;
		List<String> sel_bank = null;

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = bank_w.getBalance(account);
		//amount = (int) (50 + Math.random() * (amount - 50));
		amount = (int) (100 + Math.random() * 100);
		
		bank_w.setAccountAndAmount(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();
		bank_w.setWithdrawMethod(DEPOSITMETHOD.AUBANKTRANSFER);

		double web_amount = bank_w.getWithdrawAccount();

		Assert.assertTrue(amount == web_amount, "Input amount is not equals post amount");

		sel_bank = bank_w.getSavedBankAccount();

		// if has a saved bank account,use it. otherwise add a new one
		if (sel_bank == null) {
			// add new bank account
			Assert.assertTrue(bank_w.setWithdrawInfo(bankName, BSB, BBname, swift, bank_acc_num, notes),
					"Invalid Information");
		} else {
			Boolean hasAccount = false;
			for (String accNum : sel_bank) {
				if (bank_w.selectSavedBankAccount(accNum)) {
					hasAccount = true;
					break;
				}
			}
			if (!hasAccount) {
				GlobalMethods.printDebugInfo("Client does not have saved Australia bank info. Add a new one");
				Assert.assertTrue(bank_w.setWithdrawInfo(bankName, BSB, BBname, swift, bank_acc_num, notes),
						"Invalid Information");
			} else {
				bank_w.setNotes(notes);
			}
		}

		Assert.assertTrue(bank_w.submit(),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.AUBANKTRANSFER, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + DEPOSITMETHOD.AUBANKTRANSFER.getWithdrawHistoryName() + " withdraw succeed!!********");

	}

	@Test(priority = 0)
	public void testAustraliaBankWithdrawNew() {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPAustraliaBankWithdraw bank_w = myfactor.newInstance(CPAustraliaBankWithdraw.class);

		// test data
		double amount = 0;
		String bankName = "Test Bank Name";
		String BSB = "34543";
		String BBname = "Test Beneficiary name";
		String bank_acc_num = "3465673";
		String swift = "30033";
		String notes = "Auto test Australia Bank Withdraw";
		String account = null;
		List<String> sel_bank = null;

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = bank_w.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = bank_w.getBalance(account);
		//amount = (int) (50 + Math.random() * (amount - 50));
		amount = (int) (100 + Math.random() * 100);

		bank_w.setAccountAndAmount(account, amount);
		bank_w.clickContinue();
		bank_w.waitSpinnerLoading();
		bank_w.closeWithdrawalOTPWindow();
		bank_w.setWithdrawMethodNew(DEPOSITMETHOD.AUBANKTRANSFER_New);

		/*double web_amount = bank_w.getWithdrawAccount();

		Assert.assertTrue(amount == web_amount, "Input amount is not equals post amount");*/

		sel_bank = bank_w.getSavedBankAccountNew();

		// if has a saved bank account,use it. otherwise add a new one
		if (sel_bank == null) {
			// add new bank account
			Assert.assertTrue(bank_w.setWithdrawInfo(bankName, BSB, BBname, swift, bank_acc_num, notes),
					"Invalid Information");
		} else {
			Boolean hasAccount = false;
			for (String accNum : sel_bank) {
				if (bank_w.selectSavedBankAccountNew(accNum)) {
					hasAccount = true;
					break;
				}
			}
			if (!hasAccount) {
				GlobalMethods.printDebugInfo("Client does not have saved Australia bank info. Add a new one");
				Assert.assertTrue(bank_w.setWithdrawInfo(bankName, BSB, BBname, swift, bank_acc_num, notes),
						"Invalid Information");
			} else {
				bank_w.setNotes(notes);
			}
		}

		Assert.assertTrue(bank_w.submit(),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.AUBANKTRANSFER_New, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + DEPOSITMETHOD.AUBANKTRANSFER_New.getWithdrawName() + " withdraw succeed!!********");

	}

	@Test(priority = 0)
	public void testCreditCardWithdraw() throws InterruptedException {

		int amount = 0;
		String email = "withdraw@test.com";
		String notes = "auto test";
		String account = null;

		CPCreditCardWithdraw creditCardWithdraw = myfactor.newInstance(CPCreditCardWithdraw.class);
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		for (CURRENCY currency : CURRENCY.values()) {
			account = creditCardWithdraw.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");
		amount = (int) creditCardWithdraw.getBalance(account);
		amount = (int) (300 + Math.random() * 100);
		
		creditCardWithdraw.setAccountAndAmount(account, amount);
		creditCardWithdraw.clickContinue();
		creditCardWithdraw.waitSpinnerLoading();
		
		//Obtain CC amount
		int amount1 = creditCardWithdraw.getCCAmount1(); 
		
		//Choose second channel
		creditCardWithdraw.setWithdrawMethod(DEPOSITMETHOD.SKRILL);
		creditCardWithdraw.waitSpinnerLoading();

		//if(GlobalProperties.brand.equalsIgnoreCase("pug")||GlobalProperties.brand.equalsIgnoreCase("mo"))
		if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}
		Assert.assertTrue(creditCardWithdraw.setWithdrawInfoAndSubmit(email, notes),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		/*
		 * Assert.assertTrue( history.checkWithdraw(account, DEPOSITMETHOD.SKRILL,
		 * String.valueOf(amount), STATUS.SUBMITTED),
		 * "Have not Find the withdraw in history page");
		 */
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.CREDITORDEBIT, String.valueOf(amount1), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + DEPOSITMETHOD.CREDITORDEBIT.getWithdrawHistoryName() + " withdraw succeed!!********");

	}
	@Test(priority = 0)
	public void testUnionPayWithdraw() {
		// test data
		double amount = 0;
		
		String account = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPUnionPayWithdraw upay = myfactor.newInstance(CPUnionPayWithdraw.class);

		// switch to cp portal if the account is IB type

		// Navigate to withdraw
		menu.goToMenu(CPMenuName.CPPORTAL);

		menu.goToMenu(CPMenuName.PAYMENTDETAILS);
		//test add a new card
		String holderName = GlobalMethods.getRandomString(10);
		String id = GlobalMethods.getRandomNumberString(18);
		String cardNum = GlobalMethods.getRandomNumberString(16);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String branch = GlobalMethods.getRandomString(10);
		Card card = upay.addNewUnionCard(holderName, id, cardNum, mobile, branch);
		assertTrue(upay.submitCardApply("successfully"),"Union Pay submit error!");
		
		upay.goBackHome();
		menu.goToMenu(CPMenuName.PAYMENTDETAILS);
		if(upay.check(card)) {
			System.out.println("* Found the new card");
			card.print();
		}else {
			System.out.println("* Have not found the new card");
			card.print();
			assertTrue(false);
		}
		
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		
		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = upay.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// get balance
		amount = upay.getBalance(account);
		//amount = (int) (100 + Math.random() * (amount - 100));
		amount = (int) (100 + Math.random() * 100);

		upay.setAccountAndAmount(account, amount);

		upay.clickContinue();
		upay.waitSpinnerLoading();
		upay.closeWithdrawalOTPWindow();
		upay.setWithdrawMethod(DEPOSITMETHOD.UNIONPAY);

		assertTrue(upay.hasCard(),"Have not bound card");
		
		upay.confirmPopup();
		
		assertTrue(upay.submit(),"Withdraw submit failed.");
		
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.UNIONPAY, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + DEPOSITMETHOD.UNIONPAY.getWithdrawHistoryName() + " withdraw succeed!!********");
	}
	@Test(priority = 0)
	public void testUnionPayWithdrawV1() {
		// test data
		double amount = 0;

		String account = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPUnionPayWithdraw upay = myfactor.newInstance(CPUnionPayWithdraw.class);

		// switch to cp portal if the account is IB type
		// Navigate to withdraw
		menu.goToMenu(CPMenuName.CPPORTAL);

		menu.refresh();
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = upay.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// get balance
		amount = upay.getBalance(account);
		//amount = (int) (100 + Math.random() * (amount - 100));
		amount = (int) (100 + Math.random() * 100);

		upay.setAccountAndAmount(account, amount);

		upay.clickContinue();
		upay.waitSpinnerLoading();
		upay.closeWithdrawalOTPWindow();
		upay.setWithdrawMethod(DEPOSITMETHOD.UNIONPAY);
		upay.handleICBC();


		assertTrue(upay.submit(),"Withdraw submit failed.");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.UNIONPAY, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + DEPOSITMETHOD.UNIONPAY.getWithdrawHistoryName() + " withdraw succeed!!********");
	}

	//crypto withdrawal
	@Test(priority = 0)
	public void testCryptoBTCWithdraw() {
		testCommonCryptoWithdraw(DEPOSITMETHOD.CRYPTOBIT);
	}

	@Test(priority = 0)
	public void testCryptoCurrencyWithdraw() {
		testCommonCryptoWithdraw(DEPOSITMETHOD.CRYPTOBIT);
		testCommonCryptoWithdraw(DEPOSITMETHOD.CRYPTOERC);
		testCommonCryptoWithdraw(DEPOSITMETHOD.ETH);
		if(!GlobalProperties.brand.equalsIgnoreCase("Star")) {
			testCommonCryptoWithdraw(DEPOSITMETHOD.USDC);
		}
	}
	@Test(priority = 0)
	public void testCryptoCurrencyWithdrawNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOBTC, true);
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOERCNew, true);
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOTRCNew, true);
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.ETH, true);
		if(!GlobalProperties.brand.equalsIgnoreCase("Star")) {
			testCommonCryptoWithdrawNew(DEPOSITMETHOD.USDC, true);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_USDT_ERC20)
	public void testCryptoCRYPTOERCNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOERCNew, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_USDT_TRC20)
	public void testCryptoCRYPTOTRCNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOTRCNew, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_USDC_ERC20)
	public void testCryptoCRYPTOUSDCNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.USDCNew, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_ETH)
	public void testCryptoCRYPTOETHNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.ETH_New, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_BITCOIN)
	public void testCryptoCRYPTOBTCNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOBTC, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_USDT_BEP20)
	public void testCryptoCRYPTOBEPNew() {
		testCommonCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOBEPNew, true);
	}

	public void testCommonCryptoWithdraw(DEPOSITMETHOD method) {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPCryptoWithdraw cryptopay = myfactor.newInstance(CPCryptoWithdraw.class);
		
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		
		//Variables
		double amount = 0;
		String account = null;
		String walletaddress = null;
		String usdtchain = null;
		String notes = "autotest crypto";
		
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = cryptopay.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: did not have this currency type account");

		// get balance
		amount = cryptopay.getBalance(account);
		Assert.assertTrue((int)amount > 200, "Balance of account "+ account +" is less than 200!");
		amount = (int) (Math.random() * 10) + 50;

		cryptopay.setAccountAndAmount(account, amount);
		cryptopay.clickContinue();
		cryptopay.waitSpinnerLoading();
		cryptopay.closeWithdrawalOTPWindow();
		cryptopay.setWithdrawMethod(method);
		cryptopay.waitSpinnerLoading();
		
		if (method.getWithdrawName().equalsIgnoreCase("Cryptocurrency-Bitcoin")) 
		{
			walletaddress  = "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
			cryptopay.setCryptoWithdrawalInfo(walletaddress, notes);
		}
		else if (method.getWithdrawName().equalsIgnoreCase("Cryptocurrency-USDT"))
		{
			walletaddress = "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
			usdtchain = "ERC20";
			cryptopay.setCryptoWithdrawalInfo(walletaddress, usdtchain, notes);
		}
		else if (method.getWithdrawName().equalsIgnoreCase("Cryptocurrency-ETH"))
		{
			walletaddress = "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
			cryptopay.setCryptoWithdrawalInfo(walletaddress, notes);
		}
		else if (method.getWithdrawName().equalsIgnoreCase("Cryptocurrency-USDC"))
		{
			walletaddress = "0x6dba6f6b122038854e299c3033757aa681ec2170";
			cryptopay.setCryptoWithdrawalInfo(walletaddress, notes);
		}
				
		if(!GlobalProperties.brand.equalsIgnoreCase("pug") && !GlobalProperties.brand.equalsIgnoreCase("vfx") && !GlobalProperties.brand.equalsIgnoreCase("vt"))
		{
			setCode(emailDB);
		}

		Assert.assertTrue(cryptopay.submit(),"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, method, String.valueOf(amount), STATUS.SUBMITTED),"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");

	}

	//Perform cash adjusment if balance < 100
	public void checkBalanceAndCashAdjustment(String account, String currency, double balance){
		GlobalMethods.printDebugInfo("Account Balance: "+account);
		if (balance >= 0 && balance <= 100) {
			GlobalMethods.printDebugInfo("Account balance is less than 100. Will proceed cash adjustment.");
			adminPaymentAPI.apiAdminCashAdjustmentForCP(account, currency, "500", "1");
		} else if (balance < 0) {
			GlobalMethods.printDebugInfo("Account balance is negative balance. Will proceed cash adjustment.");
			balance = 1000 - balance;
			adminPaymentAPI.apiAdminCashAdjustmentForCP(account, currency, String.valueOf(balance), "1");
		}
	}

	public void testCommonCryptoWithdrawNew(DEPOSITMETHOD method, boolean newAccount) {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPCryptoWithdraw cryptopay = myfactor.newInstance(CPCryptoWithdraw.class);

		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		//Variables
		double amount = 0, balance = 0;
		String account = null, acctCurrency = null, walletaddress = null, usdtchain = null;
		String notes = "autotest crypto";
        List<String> savedCryptoAcct = null;

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = cryptopay.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		balance = Double.parseDouble(accSelected.getBalance());
		acctCurrency = accSelected.getCurrency();

		// get balance and check if balance less than 100, then make cash adjustment
		//checkBalanceAndCashAdjustment(account, acctCurrency, balance);
		Assert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
		//withdrawal amount
		amount = (int) (Math.random() * 10) + 50;

		cryptopay.setAccountAndAmountNew(account, amount);
		int beforeWithdrawAmount=0;
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			LogUtils.info("AU Brand Max Amount Check Before");
			beforeWithdrawAmount = cryptopay.getWithdrawalLimitAmount();

		}
		cryptopay.clickContinue();
		cryptopay.waitSpinnerLoading();
		cryptopay.closeWithdrawalOTPWindow();
		cryptopay.setWithdrawMethodNew(method);
		cryptopay.waitSpinnerLoading();

        walletaddress = switch (method) {
            case CRYPTOBTC -> "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
            case CRYPTOERCNew -> "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
            case CRYPTOTRC, CRYPTOTRCNew -> "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
            case ETH, ETH_New -> "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
            case USDC, USDCNew -> "0x6dba6f6b122038854e299c3033757aa681ec2170";
            case CRYPTOBEP, CRYPTOBEPNew -> "0x85fdb5595095403c4df0b6327b79c7f77d30cef9";
            default -> null;
        };

        if (cryptopay.checkSavedAcctDropdown()) {
			if (newAccount)
			{
				cryptopay.chooseAddNewCryptoAccount();
				cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);
			}
			else
			{
				savedCryptoAcct = cryptopay.getSavedCryptoAccount();
				if (savedCryptoAcct == null)
				{
					cryptopay.chooseAddNewCryptoAccount();
					cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);
				} else
				{
					cryptopay.chooseSavedCryptoAccount(savedCryptoAcct.get(0));
				}
			}
        } else {
			cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);
        }

		Assert.assertTrue(cryptopay.submit(),"Submit failure or error occurs");

		//Verify balance deduction
		System.out.println("***Check updated account balance***");
		menu.goToMenu(CPMenuName.HOME);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			LogUtils.info("Before:"+beforeWithdrawAmount+" AU Brand Max Amount Check After Withdraw:"+amount);

			int afterWithdrawAmount = cryptopay.getWithdrawalLimitAmount();
			GlobalMethods.printDebugInfo("afterWithdrawAmount from page is:" + afterWithdrawAmount);
			Assert.assertEquals(beforeWithdrawAmount-amount, afterWithdrawAmount, "After Withdrawal amount left is not correct!");

		}
		Assert.assertEquals((balance - cryptopay.getBalanceNew(account)), amount, "Failed to verify balance deduction after withdrawal!");

		System.out.println("***Check Transaction History***");
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdraw(account, method, String.valueOf(amount), STATUS.SUBMITTED),"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");

	}


	public void testCryptoWithdrawNew(DEPOSITMETHOD method, String brand) {
		// Instances
//		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPCryptoWithdraw cryptopay = myfactor.newInstance(CPCryptoWithdraw.class);

		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		//Variables
		double amount = 0, balance = 0;
		String account = null, acctCurrency = null, walletaddress = null, usdtchain = null;
		String notes = "autotest crypto";
		List<String> savedCryptoAcct = null;

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = cryptopay.getValidAccount();
		CustomAssert.assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		balance = Double.parseDouble(accSelected.getBalance());
		acctCurrency = accSelected.getCurrency();

		// get balance and check if balance less than 100, then make cash adjustment
		CustomAssert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
		//withdrawal amount
		amount = (int) (Math.random() * 10) + 50;

		cryptopay.setAccountAndAmountNew(account, amount);
		double beforeWithdrawAmount=0;
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			LogUtils.info("AU Brand Max Amount Check Before");
			beforeWithdrawAmount = cryptopay.getWithdrawalLimitAmount();

		}
		cryptopay.clickContinue();
		cryptopay.waitSpinnerLoading();
		cryptopay.closeWithdrawalOTPWindow();
		cryptopay.setWithdrawMethodNew(method);
		cryptopay.waitSpinnerLoading();

		walletaddress = switch (method) {
			case CRYPTOBTC -> "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
			case CRYPTOERCNew -> "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
			case CRYPTOTRC, CRYPTOTRCNew -> "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
			case ETH, ETH_New -> "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
			case USDC, USDCNew -> "0x6dba6f6b122038854e299c3033757aa681ec2170";
			case CRYPTOBEP, CRYPTOBEPNew -> "0x85fdb5595095403c4df0b6327b79c7f77d30cef9";
			default -> null;
		};
		cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);

		CustomAssert.assertTrue(cryptopay.submit(),"Submit failure or error occurs");

		//Admin > Withdrawl Audit page - approve withdrawal
		adminPaymentAPI.apiWDdailyCapAmt();

	    adminPaymentAPI.updateSingleSRCRiskRecord(account,
				dbenv,
				GlobalProperties.BRAND.valueOf(brand.toUpperCase()),
				GlobalProperties.REGULATOR.valueOf((String)data[0][0]));
		JSONObject wdAuditResult = adminPaymentAPI.apiWDAuditAccountSearch(account,"5");
		String wdAuditRecordID = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("id");
		String recordWDActualAmt = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("actualAmount");
		String recordWDPaymentAmt = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("paymentAmount");
		String recordWDChannel =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("withdrawChannel");
		String recordWDType =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("withdrawType");
		String recordWDCategory =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("category");
		String recordWDRate =  wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("rate");
		String finalWDRate = new BigDecimal(recordWDRate).stripTrailingZeros().toPlainString();
		String operateName = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("operateName");

		if (recordWDChannel == null){
			recordWDChannel = adminPaymentAPI.apiGetWDChannelID(wdAuditRecordID);
		}
		if(operateName== null){
			//只有没有操作记录的才能被认领
			adminPaymentAPI.apiWDAuditCheckClaimStatus(wdAuditRecordID);

		}
		adminPaymentAPI.apiWDAuditClaimRecord(wdAuditRecordID);
		adminPaymentAPI.apiWDAuditApproveRecord(wdAuditRecordID, recordWDActualAmt, recordWDChannel, recordWDType, recordWDPaymentAmt, recordWDCategory, finalWDRate);

		//Verify balance deduction
		System.out.println("***Check updated account balance***");
		menu.goToMenu(CPMenuName.HOME);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			LogUtils.info("Before:"+beforeWithdrawAmount+" AU Brand Max Amount Check After Withdraw:"+amount);

			double afterWithdrawAmount = cryptopay.getWithdrawalLimitAmount();
			GlobalMethods.printDebugInfo("afterWithdrawAmount from page is:" + afterWithdrawAmount);
			CustomAssert.assertEquals((beforeWithdrawAmount-amount), afterWithdrawAmount, "After Withdrawal amount left is not correct!");

		}

		CustomAssert.assertAll();
		//避免钱出完了无法出金，把出掉的钱再入进来
		checkBalanceAndCashAdjustment(account, acctCurrency, amount);
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");

	}

	public void testCommonCryptoWithdrawNewForMTS(DEPOSITMETHOD method, boolean newAccount) {
		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPCryptoWithdraw cryptopay = myfactor.newInstance(CPCryptoWithdraw.class);

		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		//Variables
		double amount = 0, balance = 0;
		String account = null, acctCurrency = null, walletaddress = null, usdtchain = null;
		String notes = "autotest crypto";
		List<String> savedCryptoAcct = null;

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.changeLanguage("English");
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = cryptopay.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		balance = Double.parseDouble(accSelected.getBalance());
		acctCurrency = accSelected.getCurrency();

		// get balance and check if balance less than 100, then make cash adjustment
		//checkBalanceAndCashAdjustment(account, acctCurrency, balance);
		Assert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
		//withdrawal amount
		amount = (int) (Math.random() * 10) + 50;

		cryptopay.setAccountAndAmountNew(account, amount);
		int beforeWithdrawAmount=0;
		/*if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			LogUtils.info("AU Brand Max Amount Check Before");
			beforeWithdrawAmount = cryptopay.getWithdrawalLimitAmount();

		}*/
		cryptopay.clickContinue();
		cryptopay.waitSpinnerLoading();
		cryptopay.closeWithdrawalOTPWindow();
		cryptopay.setWithdrawMethodNew(method);
		cryptopay.waitSpinnerLoading();

		walletaddress = switch (method) {
			case CRYPTOBTC -> "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
			case CRYPTOERCNew -> "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
			case CRYPTOTRC, CRYPTOTRCNew -> "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
			case ETH, ETH_New -> "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
			case USDC, USDCNew -> "0x6dba6f6b122038854e299c3033757aa681ec2170";
			case CRYPTOBEP, CRYPTOBEPNew -> "0x85fdb5595095403c4df0b6327b79c7f77d30cef9";
			default -> null;
		};

		if (cryptopay.checkSavedAcctDropdown()) {
			if (newAccount)
			{
				cryptopay.chooseAddNewCryptoAccount();
				cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);
			}
			else
			{
				savedCryptoAcct = cryptopay.getSavedCryptoAccount();
				if (savedCryptoAcct == null)
				{
					cryptopay.chooseAddNewCryptoAccount();
					cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);
				} else
				{
					cryptopay.chooseSavedCryptoAccount(savedCryptoAcct.get(0));
				}
			}
		} else {
			cryptopay.setCryptoWithdrawalInfoNew(walletaddress, notes);
		}

		Assert.assertTrue(cryptopay.submit(),"Submit failure or error occurs");

		/*//Verify balance deduction
		System.out.println("***Check updated account balance***");
		menu.goToMenu(CPMenuName.HOME);
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
			LogUtils.info("Before:"+beforeWithdrawAmount+" AU Brand Max Amount Check After Withdraw:"+amount);

			int afterWithdrawAmount = cryptopay.getWithdrawalLimitAmount();
			GlobalMethods.printDebugInfo("afterWithdrawAmount from page is:" + afterWithdrawAmount);
			Assert.assertEquals(beforeWithdrawAmount-amount, afterWithdrawAmount, "After Withdrawal amount left is not correct!");

		}
		Assert.assertEquals((balance - cryptopay.getBalanceNew(account)), amount, "Failed to verify balance deduction after withdrawal!");
*/
		System.out.println("***Check Transaction History***");
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, method, String.valueOf(amount), STATUS.SUBMITTED),"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");
	}
	public void setCode(EmailDB instance)
	{
		try 
		{
			WebElement codeBtn = driver.findElement(By.xpath("//button[@data-testid='code-button'] | //span[contains(text(),'Send Code')]"));
			codeBtn.click();
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				throw new RuntimeException(e);
			}
			JSONObject obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator,TraderName);
			System.out.println(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
			String code = obj.getJSONObject("vars").getString("CODE");

			WebElement codeInput = waitUtilVisibility(By.xpath("//input[@data-testid='code']|//div[@id='verificationDialog']//input[@class='el-input__inner']"));
			codeInput.sendKeys(code);
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("No require to enter verification code");
		}
	}
	@Test(priority = 0)
	public void testSouthAfricaBankWithdrawNew() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT, false);
	}
	@Test(priority = 0)
	public void testSouthAfricaBankWithdrawNewAccountNew() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_VIETNAM_BT)
	public void testVietnamBankWithdrawNew() {
		CPVietnamBankWithdraw bank_w = myfactor.newInstance(CPVietnamBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.VIETNAMINSTANT_New, false);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_VIETNAM_BT)
	public void testVietnamBankWithdrawNewAccountNew() {
		CPVietnamBankWithdraw bank_w = myfactor.newInstance(CPVietnamBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.VIETNAMINSTANT_New, true);
	}
	@Test(priority = 0)
	public void testBrazilBankWithdrawNew() {
		CPBrazilBankWithdraw bank_w = myfactor.newInstance(CPBrazilBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.BRAZILINSTANT, false);
	}

	@Test(priority = 0)
	public void testBrazilBankWithdrawNewAccountNew() {
		CPBrazilBankWithdraw bank_w = myfactor.newInstance(CPBrazilBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.BRAZILINSTANT, true);
	}
	@Test(priority = 0)
	public void testBrazilPIXWithdrawNew() {
		CPBrazilPIXWithdraw bank_w = myfactor.newInstance(CPBrazilPIXWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.BRAZILPIX, false);
	}

	@Test(priority = 0)
	public void testBrazilPIXWithdrawNewAccountNew() {
		CPBrazilPIXWithdraw bank_w = myfactor.newInstance(CPBrazilPIXWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.BRAZILPIX, true);
	}
	@Test(priority = 0)
	public void testChileBankWithdrawNew() {
		CPChileBankWithdraw bank_w = myfactor.newInstance(CPChileBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.CHILEINSTANT, false);
	}

	@Test(priority = 0)
	public void testChileBankWithdrawNewAccountNew() {
		CPChileBankWithdraw bank_w = myfactor.newInstance(CPChileBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.CHILEINSTANT, true);
	}
	@Test(priority = 0)
	public void testColombiaBankWithdrawNew() {
		CPChileBankWithdraw bank_w = myfactor.newInstance(CPChileBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT_New, false);
	}

	@Test(priority = 0)
	public void testColombiaBankWithdrawNewAccountNew() {
		CPChileBankWithdraw bank_w = myfactor.newInstance(CPChileBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT_New, true);
	}

	@Test(priority = 0)
	public void testPeruBankWithdrawNew() {
		CPPeruBankWithdraw bank_w = myfactor.newInstance(CPPeruBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.PERUINSTANT, false);
	}

	@Test(priority = 0)
	public void testPeruBankWithdrawNewAccountNew() {
		CPPeruBankWithdraw bank_w = myfactor.newInstance(CPPeruBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.PERUINSTANT, true);
	}

	//PM confirmed that Ghana input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testGhanaBankWithdrawNew() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.GHANAINSTANT, false);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.GHANAINSTANT, false);
		}
	}

	@Test(priority = 0)
	public void testGhanaBankWithdrawNewAccountNew() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.GHANAINSTANT, true);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.GHANAINSTANT, true);
		}
	}
	@Test(priority = 0)
	public void testIndiaBankWithdrawNew() {
		CPIndiaBankWithdraw bank_w = myfactor.newInstance(CPIndiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.INDIAIAINSTANT, false);
	}

	@Test(priority = 0)
	public void testIndiaBankWithdrawNewAccountNew() {
		CPIndiaBankWithdraw bank_w = myfactor.newInstance(CPIndiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.INDIAIAINSTANT, true);
	}
	@Test(priority = 0)
	public void testIndonesiaBankWithdrawNew() {
		CPIndonesiaBankWithdraw bank_w = myfactor.newInstance(CPIndonesiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.INDONESIAINSTANT, false);
	}

	@Test(priority = 0)
	public void testIndonesiaBankWithdrawNewAccountNew() {
		CPIndonesiaBankWithdraw bank_w = myfactor.newInstance(CPIndonesiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.INDONESIAINSTANT, true);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_JAPAN_BT)
	public void testJapanBankWithdrawNew() {
		CPJapanBankWithdraw bank_w = myfactor.newInstance(CPJapanBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.JAPANIAINSTANT, false);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_JAPAN_BT)
	public void testJapanBankWithdrawNewChangeRateCheck(double changeRate) {
		CPJapanBankWithdraw bank_w = myfactor.newInstance(CPJapanBankWithdraw.class);
		testOtherBankWithdrawChangeRateCheck(bank_w, DEPOSITMETHOD.JAPANIAINSTANT, false, changeRate);
	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_JAPAN_BT)
	public void testJapanBankWithdrawNewAccountNew() {
		CPJapanBankWithdraw bank_w = myfactor.newInstance(CPJapanBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.JAPANIAINSTANT, true);
	}
	@Test(priority = 0)
	public void testKoreaBankWithdrawNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.KOREAINSTANT, false);
	}
	@Test(priority = 0)
	public void testKoreaBankWithdrawNewAccountNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.KOREAINSTANT, true);
	}
	@Test(priority = 0)
	public void testLaosBankWithdrawNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.LAOSINSTANT, false);
	}

	@Test(priority = 0)
	public void testLaosBankWithdrawNewAccountNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.LAOSINSTANT, true);
	}
	@Test(priority = 0)
	public void testLocalDepositorWithdrawNew() {
		CPLocalDepositorWithdraw bank_w = myfactor.newInstance(CPLocalDepositorWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.LOCALDEPOSITOR, false);
	}
	@Test(priority = 0)
	public void testMongoliaBankWithdrawNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT, false);
	}

	@Test(priority = 0)
	public void testMongoliaBankWithdrawNewAccountNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT, true);
	}
	@Test(priority = 0)
	public void testMexicoBankWithdrawNew() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.MEXICOINSTANT, false);
	}

	@Test(priority = 0)
	public void testMexicoBankWithdrawNewAccountNew() {
		CPMexicoBankWithdraw bank_w = myfactor.newInstance(CPMexicoBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.MEXICOINSTANT, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_MALAYSIA_BT)
	public void testMalaysiaBankWithdrawNew() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.MALAYINSTANT, false);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_MALAYSIA_BT)
	public void testMalaysiaBankWithdrawNewChangeRateCheck(double changeRate) {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdrawChangeRateCheck(bank_w, DEPOSITMETHOD.MALAYINSTANT, false, changeRate);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_MALAYSIA_BT)
	public void testMalaysiaBankWithdrawNewAccountNew() {
		CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.MALAYINSTANT, true);
	}
	@Test(priority = 0)
	public void testNigeriaBankWithdrawNew() {
		CPNigeriaBankWithdraw bank_w = myfactor.newInstance(CPNigeriaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.NIGERIAINSTANT, false);
	}

	@Test(priority = 0)
	public void testNigeriaBankWithdrawNewAccountNew() {
		CPNigeriaBankWithdraw bank_w = myfactor.newInstance(CPNigeriaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.NIGERIAINSTANT, true);
	}
	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNew() {
		CPPhillipineBankWithdraw bank_w = myfactor.newInstance(CPPhillipineBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT, false);
	}

	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNewAccountNew() {
		CPPhillipineBankWithdraw bank_w = myfactor.newInstance(CPPhillipineBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT, true);
	}
	@Test(priority = 0)
	public void testThaiBankWithdrawNew() {
		CPThaiBankWithdraw bank_w = myfactor.newInstance(CPThaiBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.THAIINSTANT, false);
	}

	@Test(priority = 0)
	public void testThaiBankWithdrawNewAccountNew() {
		CPThaiBankWithdraw bank_w = myfactor.newInstance(CPThaiBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.THAIINSTANT, true);
	}
	@Test(priority = 0)
	public void testTaiwanBankWithdrawNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.TAIWANINSTANT, false);
	}

	@Test(priority = 0)
	public void testTaiwanBankWithdrawNewAccountNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.TAIWANINSTANT, true);
	}
	//PM confirmed that Uganda input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testUgandaBankWithdrawNew() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.UGANDAINSTANT, false);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.UGANDAINSTANT, false);
		}
	}

	@Test(priority = 0)
	public void testUgandaBankWithdrawNewAccountNew() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.UGANDAINSTANT, true);
		}else {
			CPMalaysiaBankWithdraw bank_w = myfactor.newInstance(CPMalaysiaBankWithdraw.class);
			testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.UGANDAINSTANT, true);
		}
	}
	@Test(priority = 0)
	public void testZambiaBankWithdrawNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT, false);
	}

	@Test(priority = 0)
	public void testZambiaBankWithdrawNewAccountNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT, true);
	}
	//PM confirmed that Kenya input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testKenyaBankWithdrawNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.KENYAINSTANT, false);
	}

	@Test(priority = 0)
	public void testKenyaBankWithdrawNewAccountNew() {
		CPKoreaBankWithdraw bank_w = myfactor.newInstance(CPKoreaBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.KENYAINSTANT, true);
	}

	@Test(priority = 0)
	public void testUAEBankWithdrawNew() {
		CPUAEBankWithdraw bank_w = myfactor.newInstance(CPUAEBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.UAEINSTANT, false);
	}

	@Test(priority = 0)
	public void testUAEBankWithdrawNewAccountNew() {
		CPUAEBankWithdraw bank_w = myfactor.newInstance(CPUAEBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.UAEINSTANT, true);
	}

	@Test(priority = 0)
	public void testHongKongBankWithdrawNew() {
		CPHongKongBankWithdraw bank_w = myfactor.newInstance(CPHongKongBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.HONGKONGINSTANT, false);
	}

	@Test(priority = 0)
	public void testHongKongBankWithdrawNewAccountNew() {
		CPHongKongBankWithdraw bank_w = myfactor.newInstance(CPHongKongBankWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.HONGKONGINSTANT, true);
	}

	@Test(priority = 0)
	public void testEuBTNodaPayWithdrawNew() {
		CPEuBankTransWithdraw bank_w = myfactor.newInstance(CPEuBankTransWithdraw.class);
		testOtherBankWithdrawNew(bank_w, DEPOSITMETHOD.EUBT_NodaPay, true);
	}

	@Test(priority = 0)
	public void testBkashWithdrawNew() {
		CPSkrillWithdraw bkash = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(bkash, DEPOSITMETHOD.BDTBkash, true);
	}

	@Test(priority = 0)
	public void testVoletWithdrawNew() {
		CPSkrillWithdraw bkash = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(bkash, DEPOSITMETHOD.VOLET, true);
	}

	@Test(priority = 0)
	public void testRocketWithdrawNew() {
		CPSkrillWithdraw rocket = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(rocket, DEPOSITMETHOD.BDTRocket, true);
	}
	@Test(priority = 0)
	public void testNagadWithdrawNew() {
		CPSkrillWithdraw bkash = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(bkash, DEPOSITMETHOD.BDTNagad, true);
	}

	@Test(priority = 0)
	public void testSkrillWithdrawNew() {
		CPSkrillWithdraw skrill = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(skrill, DEPOSITMETHOD.SKRILL, true);
	}

	@Test(priority = 0)
	public void testNetellerWithdrawNew() {
		CPSkrillWithdraw neteller = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(neteller, DEPOSITMETHOD.NETELLER, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_EWALLET_STICPAY)
	public void testSticPayWithdrawNew() {
		CPSkrillWithdraw sticpay = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(sticpay, DEPOSITMETHOD.STICPAY, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_EWALLET_BINANCEPAY)
	public void testBinanceWithdrawNew() throws InterruptedException {
		CPSkrillWithdraw binancePay = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(binancePay, DEPOSITMETHOD.BINANCE, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_EWALLET_TYGAPAY)
	public void testTygapayWithdrawNew() throws InterruptedException {
		CPSkrillWithdraw tygapay = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(tygapay, DEPOSITMETHOD.Tygapay, true);
	}

	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_EWALLET_BITWALLET)
	public void testBitWalletWithdrawNew() throws InterruptedException {
		CPSkrillWithdraw bitwallet = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(bitwallet, DEPOSITMETHOD.BITWALLET, true);
	}

	@Test(priority = 0)
	public void testAirTMWithdrawNew() {
		CPSkrillWithdraw airTM = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(airTM, DEPOSITMETHOD.AirTM_New, true);
	}

	@Test(priority = 0)
	public void testAdvcashWithdrawNew() throws InterruptedException {
		CPSkrillWithdraw advcash = myfactor.newInstance(CPSkrillWithdraw.class);
		testWithdrawWithEmailNew(advcash, DEPOSITMETHOD.ADVCASH, true);
	}

	@Test(priority = 0)
	public void testFXIRWithdrawNew() throws InterruptedException {
		DEPOSITMETHOD method = DEPOSITMETHOD.FXIR;
		CPFXIRWithdraw instance = myfactor.newInstance(CPFXIRWithdraw.class);
		//testWithdrawWithEmailNew(fxir, DEPOSITMETHOD.FXIR);

		// test data
		double amount = 0;
		String email = "withdraw@test.com";
		String notes = "auto test";
		String account = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}

		// switch to cp portal if the account is IB type
		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		//some user loading long time due to many accounts
		instance.waitSpinnerLoading();

		// Navigate to withdraw
		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = instance.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		amount = Double.parseDouble(accSelected.getBalance());

		// get balance
		Assert.assertTrue((int)amount > 200, "Balance of account "+ account +" is less than 200!");

		//amount = (int) (100 + Math.random() * (amount - 100));
		amount = (int) (100 + Math.random() * 100);

		instance.setAccountAndAmountNew(account, amount);

		instance.clickContinue();
		instance.waitCustomiseLoading(120);

		instance.setWithdrawMethodNew(method);
		instance.waitSpinnerLoading();

		if(GlobalProperties.brand.equalsIgnoreCase("vfx") || GlobalProperties.brand.equalsIgnoreCase("vt"))
		{

			instance.setWithdrawInfo(email, notes);
			instance.submitWithoutCheck();
			if(GlobalProperties.env.equalsIgnoreCase("prod"))
			{
				setCode(emailDB);
				Assert.assertTrue(instance.setCodeSubmit(),"Withdraw submit failure or error occurs!");
			}
		}
		else
		{
			if(GlobalProperties.brand.equalsIgnoreCase("star") || GlobalProperties.brand.equalsIgnoreCase("mo") || GlobalProperties.brand.equalsIgnoreCase("vjp"))
			{
				setCode(emailDB);
			}
			Assert.assertTrue(instance.setWithdrawInfoAndSubmit(email, notes),
					"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		}

		System.out.println("***Check updated account balance***");
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, method, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + method.getWithdrawHistoryName() + " withdraw succeed!!********");
	}

	@Test(priority = 0)
	public void testCreditCardWithdrawNew() throws InterruptedException {

		int amount = 0;
		String email = "withdraw@test.com";
		String notes = "auto test";
		String account = null;

		CPCreditCardWithdraw creditCardWithdraw = myfactor.newInstance(CPCreditCardWithdraw.class);
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = creditCardWithdraw.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		amount = Integer.parseInt(accSelected.getBalance());

		amount = (int) (50 + Math.random() * 10);

		creditCardWithdraw.setAccountAndAmountNew(account, amount);
		creditCardWithdraw.clickContinue();
		creditCardWithdraw.waitSpinnerLoading();

		/*//Obtain CC amount
		int amount1 = creditCardWithdraw.getCCAmount1();

		//Choose second channel
		creditCardWithdraw.setWithdrawMethod(DEPOSITMETHOD.SKRILL);
		creditCardWithdraw.waitSpinnerLoading();*/

		//if(GlobalProperties.brand.equalsIgnoreCase("pug")||GlobalProperties.brand.equalsIgnoreCase("mo"))
		if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}
		Assert.assertTrue(creditCardWithdraw.setWithdrawInfoAndSubmitNew(email, notes),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		System.out.println("***Check Transaction History***");

		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		/*
		 * Assert.assertTrue( history.checkWithdraw(account, DEPOSITMETHOD.SKRILL,
		 * String.valueOf(amount), STATUS.SUBMITTED),
		 * "Have not Find the withdraw in history page");
		 */
		Assert.assertTrue(
				history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.CREDITORDEBIT_New, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println(
				"***Test " + DEPOSITMETHOD.CREDITORDEBIT_New.getWithdrawHistoryName() + " withdraw succeed!!********");

	}

	@Test(priority = 0)
	public void testUnionPayWithdrawNew() {
		// test data
		double amount = 0;
		String account = null;

		// Instances
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPUnionPayWithdraw upay = myfactor.newInstance(CPUnionPayWithdraw.class);

		// Navigate to withdraw
		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Check UnionPay Card Details***");
		menu.goToMenu(CPMenuName.PAYMENTDETAILS);

		//test add a new card
		String holderName = GlobalMethods.getRandomString(10);
		String id = GlobalMethods.getRandomNumberString(18);
		String cardNum = GlobalMethods.getRandomNumberString(16);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String branch = GlobalMethods.getRandomString(10);
		Card card = upay.addNewUnionCard(holderName, id, cardNum, mobile, branch);
		assertTrue(upay.submitCardApply("successfully"),"Union Pay submit error!");

		upay.goBackHome();
		menu.goToMenu(CPMenuName.PAYMENTDETAILS);
		if(upay.check(card)) {
			System.out.println("* Found the new card");
			card.print();
		}else {
			System.out.println("* Have not found the new card");
			card.print();
			assert false : "* Have not found the new card";
		}

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = upay.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		account = accSelected.getAccNumber();
		amount = Double.parseDouble(accSelected.getBalance());

		// get balance
		//amount = (int) (100 + Math.random() * (amount - 100));
		amount = (int) (100 + Math.random() * 100);

		upay.setAccountAndAmountNew(account, amount);

		upay.clickContinue();
		upay.waitSpinnerLoading();
		upay.closeWithdrawalOTPWindow();
		upay.setWithdrawMethodNew(DEPOSITMETHOD.UNIONPAY);

		assertTrue(upay.hasCard(),"Have not bound card");

		upay.confirmPopup();

		assertTrue(upay.submit(),"Withdraw submit failed.");

		System.out.println("***Check Transaction History***");
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(history.checkWithdrawAndCancelOrder(account, DEPOSITMETHOD.UNIONPAY, String.valueOf(amount), STATUS.SUBMITTED),
				"Do not Find the withdraw in history page");
		System.out.println("***Test " + DEPOSITMETHOD.UNIONPAY.getWithdrawHistoryName() + " withdraw succeed!!********");
	}

	@Test
	public void withdrawCreditCardInfoCheck() throws InterruptedException {
		LogUtils.info("Credit Card Deposit Page Check - Credit/Debit Card");

		CPCreditCardWithdraw creditCardWithdraw = myfactor.newInstance(CPCreditCardWithdraw.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		// 进入CP
//		driver.get(getCPURL());
		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.goToMenu(CPMenuName.HOME);

		System.out.println("***Start Withdrawal***");
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);

		// Get valid account
		WithdrawBasePage.Account accSelected = creditCardWithdraw.getValidAccount();
		assertNotNull(accSelected, "No available account found");

		String account = accSelected.getAccNumber();

		double amount =50;

		creditCardWithdraw.setAccountAndAmountNew(account, amount);
		creditCardWithdraw.clickContinue();
		creditCardWithdraw.handleCreditPopup();
		creditCardWithdraw.waitSpinnerLoading();

		String ccWithdrawTabText =creditCardWithdraw.getCCWithdrawTabText();
		CustomAssert.assertEquals(ccWithdrawTabText.contains("4716")&&ccWithdrawTabText.contains("8107")
				, true, "Credit Card Withdraw Tab Text is not correct");
		CustomAssert.assertAll();

		ScreenshotHelper.takeScreenshot(driver, null, "screenshots", "withdraw_creditcard_info_check.png");

		LogUtils.info("***出金渠道 = Credit Card 信用卡信息一致性验证 succeed!!********");

	}

	@Test(description = testCaseDescUtils.CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT_DESC)
	public void withdrawDeductedMaxAmount(String brand) {
		testCryptoWithdrawNew(DEPOSITMETHOD.ETH_New, brand);


	}

	@Test
	public void withDrawPageChangeRateCheck() {
		LogUtils.info("LBT WithDraw Page Check - ChangeRate");
		AdminAPIPayment adminPaymentAPI =  new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String) data[0][0]), (String) data[0][7], (String) data[0][8], GlobalProperties.BRAND.valueOf(Brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

		JSONObject exchangeRate =adminPaymentAPI.getExchangeRate();
		LogUtils.info("exchangeRate:"+exchangeRate);
		// 将 JSONObject 转换为 Map
		Map<String, Object> exchangeRateMap = exchangeRate.getInnerMap();;

		LogUtils.info("exchangeRateMap:"+exchangeRateMap);
		if (Brand.equalsIgnoreCase("um")||Brand.equalsIgnoreCase("vfx")||Brand.equalsIgnoreCase("au")) {
			//获取汇率
			String key="USD2PHPWITH";
			Object rate = exchangeRateMap.get(key);
			double changeRate = safeConvertToDouble(rate, 1.0);
			testPhilippinesBankWithdrawNewAccountChangeRateCheck(changeRate);
		}
		if (Brand.equalsIgnoreCase("pug")){
			String key="USD2VNDWITH";
			Object rate = exchangeRateMap.get(key);
			double changeRate = safeConvertToDouble(rate, 1.0);
			testVietnamBankWithdrawNewAccountChangeRate(changeRate);
		}
		if (Brand.equalsIgnoreCase("vt") || Brand.equalsIgnoreCase("mo")) {
			//获取汇率
			String key="USD2MYRWITH";
			Object rate = exchangeRateMap.get(key);
			double changeRate = safeConvertToDouble(rate, 1.0);
			testMalaysiaBankWithdrawNewChangeRateCheck(changeRate);
		}

		if (Brand.equalsIgnoreCase("vjp")){
			String key="USD2JPYWITH";
			Object rate = exchangeRateMap.get(key);
			double changeRate = safeConvertToDouble(rate, 1.0);
			testJapanBankWithdrawNewChangeRateCheck(changeRate);
		}

	}
	@Test(priority = 0, description = testCaseDescUtils.CPWITHDRAW_CRYPTO_ETH)
	public void testCryptoCRYPTOETHNewforMTS() {
		testCommonCryptoWithdrawNewForMTS(DEPOSITMETHOD.ETH_New, true);
	}
//	@Test(description = testCaseDescUtils.CPAPIWITHDRAW_UNIONPAY_WITHDRAW)
//	public void unionPayWithdraw() {
//
//	}
	@Test
	public void test2FAWithdrawPopUP() {
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.changeLanguage("English");
		menu.refresh();

		CPInternationalBankWithdraw bank_w = myfactor.newInstance(CPInternationalBankWithdraw.class);

		if(GlobalProperties.brand.equalsIgnoreCase("vt")) {
			bank_w = myfactor.newInstance(CPInternationBankCPSWithdraw.class);
		}
		double amount = 50;
		String account = null;
		menu.goToMenu(CPMenuName.WITHDRAWFUNDS);
		CPWithdraw withdraw = myfactor.newInstance(CPWithdraw.class);
		if(Brand.equalsIgnoreCase("vt")){
			String pendingContent=withdraw.checkWithdrawal2FANotice() ;

			CustomAssert.assertEquals(pendingContent.contains("Your proof of ID and proof of address are currently being processed"),
					true, "2FA Withdraw popup content is not correct");
		}else{
			// select an available account with any currency under this method
			for (CURRENCY currency : CURRENCY.values()) {
				account = bank_w.checkAccount(currency);
				if (account != null) {
					break;
				}
			}
			CustomAssert.assertNotNull(account, "Failed: did not have this currency type account");
			bank_w.setAccountAndAmount(account, amount);
			bank_w.clickContinue();
			bank_w.waitSpinnerLoading();
			bank_w.setWithdrawMethod(DEPOSITMETHOD.IBT_EQUALS_UAT);
//			bank_w.submit();
			String pendingContent=withdraw.checkWithdrawal2FANotice() ;
			LogUtils.info("pendingContent:"+pendingContent);
			CustomAssert.assertTrue(pendingContent.contains("Complete the ID/POA verification and unlock the withdrawal authority")||
					pendingContent.contains("Please complete the following verification(s) to unlock the feature."),
					 "2FA Withdraw popup content is not correct");
		}

		CustomAssert.assertAll();
	}
}