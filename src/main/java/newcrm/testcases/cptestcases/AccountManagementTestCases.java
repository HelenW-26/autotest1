package newcrm.testcases.cptestcases;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.business.businessbase.*;
import newcrm.cpapi.CPAPIAccount;
import newcrm.cpapi.CPAPIWalletBase;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.AUCPTransferPage;
import newcrm.pages.clientpages.LiveAccountsPage.Account;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.api.RsaUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACC_STATUS;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import utils.LogUtils;

import static org.testng.Assert.*;

public class AccountManagementTestCases extends BaseTestCaseNew {

	public Object data[][];
	private CPMenu menu;

    public String loginAccount;
    public String currency;
    public String walletAccount;
    public String walletCurrency;
    public double amount = 20;
    public JSONObject transferRate;

	protected CPLiveAccounts liveAccounts;
	protected AdminAPIPayment adminPaymentAPI;
	protected AdminAPIUserAccount adminAcctAPI;
    private Factor myfactor;
    private CPLogin login;
    private WebDriver driver;
    protected CPAPIWalletBase cpWalletapi;


	@BeforeMethod(groups = {"CP_Live_Acc_Info_Check", "CP_Live_Acc_Switch_Display_Mode_Check", "CP_Live_Acc_Leverage_Check", "CP_Live_Acc_Chg_Nickname", "CP_Live_Acc_Remove_Credit", "CP_Live_Additional_Acc", "CP_AddAdditionalAccount", "CP_Add_Acc_MTS", "CP_Transfer", "CP_Transfer_MTS"})
	protected void initMethod(Method method) {

        myfactor = getFactorNew();
        login = getLogin();
        driver = getDriverNew();

//		checkValidLoginSession();
		login.goToCpHome();
		menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
		menu.changeLanguage("English");
		menu.goToMenu(GlobalProperties.CPMenuName.HOME);
	}

	@AfterMethod(groups = {"CP_Live_Acc_Leverage_Check", "CP_Live_Acc_Chg_Nickname", "CP_Live_Acc_Remove_Credit", "CP_Live_Additional_Acc", "CP_AddAdditionalAccount", "CP_Add_Acc_MTS"})
	public void tearDown(ITestResult result) {
		String methodName = result.getMethod().getMethodName();

		if (methodName != null && !methodName.isEmpty()) {
			switch (methodName) {
				case "testChangeMT4LiveAccountNickname",
					 "testChangeMT5LiveAccountNickname",
					 "testMT4LiveAccountRemoveCredit",
					 "testMT5LiveAccountRemoveCredit",
					 "testOpenAdditionalMT4LiveAccount",
					 "testOpenAdditionalMT5LiveAccount",
					 "testOpenAdditionalMTSAccount",
					 "testMT5LiveAccountLeverage2000Check",
					 "testMT5LiveAccountLeverage500Check",
					 "testMT5LiveAccountLeverage1000Check":

					// Close previous left open dialog if any
					if (liveAccounts != null) {
						liveAccounts.checkExistsDialog();
					}
					break;
			}
		}
	}

	private void checkForCreditAvailability(PLATFORM platform, String accNo) {
		System.out.println("***Check for account credit availability***");

		adminAcctAPI = new AdminAPIUserAccount((String) data[0][4], REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], BRAND.valueOf(String.valueOf(dbBrand)), ENV.valueOf(String.valueOf(dbenv)));
		// Search user account by email to get account info for next API usage
		JSONArray tradingAcctList = (adminAcctAPI.apiTradingAcctSearch(accNo, RsaUtil.getAdminEmailEncrypt((String)data[0][1]), platform, "", "", "")).getJSONArray("rows");

		if (tradingAcctList == null || tradingAcctList.isEmpty()) {
			Assert.fail(String.format("Cannot proceed with removing account credit. Reason: Failed to search account (%s)", accNo));
		}

		JSONObject tradingAcct = tradingAcctList.getJSONObject(0);
		String currency = tradingAcct.getString("apply_currency");
		String accNum = tradingAcct.getString("mt4_account");
		double accCredit = Double.parseDouble(tradingAcct.getString("credit"));

		// Proceed credit adjustment process when account no credit
		if (accCredit < 1) {
			adminPaymentAPI = new AdminAPIPayment((String) data[0][4], REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], BRAND.valueOf(String.valueOf(dbBrand)), ENV.valueOf(String.valueOf(dbenv)));

			System.out.println(String.format("No available credit. Proceed credit adjustment to add credit to account (%s)", accNo));

			// Add credit to account
			Map.Entry<Boolean, String> resp = adminPaymentAPI.apiAdminCreditAdjustmentForCP(accNum, currency, "1", "1");

			if (!resp.getKey()) {
				Assert.fail(String.format("Cannot proceed with removing account credit as the account (%s) has no credit. Reason: %s", accNo, resp.getValue()));
			}

			int maxRetries = 10;

			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				// Search user account for latest credit info
				tradingAcctList = (adminAcctAPI.apiTradingAcctSearch(accNo, RsaUtil.getAdminEmailEncrypt((String)data[0][1]), platform, "", "", "")).getJSONArray("rows");
				tradingAcct = tradingAcctList.getJSONObject(0);
				accCredit = Double.parseDouble(tradingAcct.getString("credit"));

				if (accCredit > 0) {
					return;
				}
				if (attempt != maxRetries) {
					try {
						Thread.sleep(3000); // Wait 3 second before next check
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					LogUtils.info(String.format("Retry fetching account credit update status on attempt %s. Account: %s, Credit Balance: %s", attempt, accNo, accCredit));
				}
			}

			Assert.fail(String.format("Cannot proceed with removing account credit as the account (%s) has no credit. Reason: Credit adjustment completed yet to credit to account. Please manually verify the credit value in a short while and proceed with credit removal.", accNo));
		}
	}

	// region [ Open Additional Account ]

	public void funcAddAcc(PLATFORM platform, ACCOUNTTYPE accType, CURRENCY currency) {
		CPOpenAdditionalAccount additionalAccount = myfactor.newInstance(CPOpenAdditionalAccount.class);
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		AdminAPIUserAccount admin = new AdminAPIUserAccount((String)data[0][4], dbRegulator, (String)data[0][7], (String)data[0][8], dbBrand, dbenv);

		// Get profile user id
		String userId = liveAccounts.getProfileUserId();
		if (userId.isEmpty()) {
			Assert.fail("Profile User Id is empty");
		}

		// Reject past additional account application found
		rejectPastAdditionalAccApplication(admin, userId, "0");

		if(GlobalProperties.isWeb)
			menu.goToMenu(CPMenuName.ADDACCOUNT);
		else
			menu.goToMenu(CPMenuName.ADDACCOUNTMOBILE);

		additionalAccount.waitLoadingAccConfigContent();
		additionalAccount.setPlatForm(platform);
		additionalAccount.setAccountType(accType);
		additionalAccount.setCurrency(currency);
		additionalAccount.setNote("From automation case");

		// Get today latest additional account application
		String auditIDOri = getLatestAdditionalAccAuditID(admin, userId);

		if (!additionalAccount.submit()) {
			Assert.fail("Failed to submit additional account application");
		}

		// Reject current additional account application
		checkAndRejectLatestAdditionalAccApplication(admin, userId, platform, accType, currency, null, auditIDOri);

		System.out.println("***Test Open Additional Account succeed!!********");
	}

	private List<Map<String, String>> getAdditionalAccApplicationList(AdminAPIUserAccount admin, String userId, String status, String startDate, String endDate) {
		// Get additional account application list
		try {
			return admin.testQueryAdditionalAccount(userId, status, startDate, endDate);
		} catch (Exception ex) {
			Assert.fail("An error occurred when query for additional account application list. Resp Msg: " + ex.getMessage());
		}

		return null;
	}

	private String getLatestAdditionalAccAuditID(AdminAPIUserAccount admin, String userId) {
		List<Map<String, String>> additionalAccountList = getAdditionalAccApplicationList(admin, userId, null, GlobalMethods.setFromDateOnly(1), GlobalMethods.setToDateOnly(1));

		if (additionalAccountList != null && !additionalAccountList.isEmpty()) {
			Map<String, String> accountInfo = additionalAccountList.get(0);

			String additionalAccountID = accountInfo.get("accauditID");
			String realName = accountInfo.get("realName");
			String applicationStatus = accountInfo.get("status");
			String applicationDateTime = accountInfo.get("applicationTime");
			String applicationUpdateDateTime = accountInfo.get("update_time");

			System.out.println(String.format("Latest Additional account application, Audit ID: %s, Application Date: %s, Name: %s, Status: %s, Update Date: %s", additionalAccountID, applicationDateTime, realName, applicationStatus, applicationUpdateDateTime));
			return additionalAccountID;
		}

		return null;
	}

	private void updateAdditionalAccApplicationToRejectStatus(AdminAPIUserAccount admin, String userId, String additionalAccountID, String realName, String applicationDateTime) {
		// Reject additional account application
		try {
			admin.testRejectAdditionalAccountByUID(userId, additionalAccountID, realName);
			System.out.println(String.format("Reject additional account application success, Audit ID: %s, Application Date: %s, Name: %s", additionalAccountID, applicationDateTime, realName));
		} catch (Exception ex) {
			Assert.fail(String.format("Reject additional account application failed, Audit ID: %s, Application Date: %s, Name: %s", additionalAccountID, applicationDateTime, realName));
		}
	}

	private void checkAndRejectLatestAdditionalAccApplication(AdminAPIUserAccount admin, String userId, PLATFORM platform, ACCOUNTTYPE accType,
															  CURRENCY currency, String status, String auditIDOri) {

		System.out.println("***Check Latest Additional Account Application***");

		// Get today additional account application
		List<Map<String, String>> additionalAccountList = getAdditionalAccApplicationList(admin, userId, status, GlobalMethods.setFromDateOnly(1), GlobalMethods.setToDateOnly(1));

		if (additionalAccountList == null || additionalAccountList.isEmpty()) {
			Assert.fail("No new additional account application found");
		}

		// Get first record
		Map<String, String> accountInfo = additionalAccountList.get(0);

		String additionalAccountID = accountInfo.get("accauditID");
		String realName = accountInfo.get("realName");
		String applicationStatus = accountInfo.get("status");
		String applicationDateTime = accountInfo.get("applicationTime");
		String applicationUpdateDateTime = accountInfo.get("update_time");
		String mt4AccountType = accountInfo.get("mt4_account_type");
		String mt4AccountTypeDisplay = accountInfo.get("mt4_account_type_display");
		String accCurrency = accountInfo.get("currency");

		// Cross-check with latest audit id, determine any new record created after additional account submission
		if (auditIDOri != null && auditIDOri.equals(additionalAccountID)) {
			Assert.fail(String.format("No new additional account application found. Last application info found, Audit ID: %s, Application Date: %s, Name: %s, Status: %s, Update Date: %s", additionalAccountID, applicationDateTime, realName, applicationStatus, applicationUpdateDateTime));
		}

		System.out.println(String.format("Additional account application found, Audit ID: %s, Application Date: %s, Name: %s, Status: %s, Update Date: %s", additionalAccountID, applicationDateTime, realName, applicationStatus, applicationUpdateDateTime));

		// Cross-check system data stored with submission info
		if (mt4AccountType == null || mt4AccountType.isEmpty()) {
			Assert.fail(String.format("Additional account Account Type Code is empty. Audit ID: %s", additionalAccountID));
		}

		if (!mt4AccountType.equals(accType.getAccountTypeCode())) {
			Assert.fail(String.format("Additional account Account Type Code mismatch. Audit ID: %s, Expected: %s (%s), Actual: %s (%s)", additionalAccountID, accType.getLiveAccountName(), accType.getAccountTypeCode(), mt4AccountTypeDisplay, mt4AccountType));
		}

		if (accCurrency == null || accCurrency.isEmpty()) {
			Assert.fail(String.format("Additional account Currency is empty. Audit ID: %s", additionalAccountID));
		}

		if (!accCurrency.equals(currency.getCurrencyDesc())) {
			Assert.fail(String.format("Additional account Currency mismatch. Audit ID: %s, Expected: %s, Actual: %s", additionalAccountID, currency.getCurrencyDesc(), accCurrency));
		}

		System.out.println("****************Additional Account Summary*****************");
		System.out.printf("%-30s : %s\n", "Audit ID", additionalAccountID);
		System.out.printf("%-30s : %s\n", "Platform", platform.getPlatformDesc());
		System.out.printf("%-30s : %s\n", "Account Type Code", mt4AccountType);
		System.out.printf("%-30s : %s\n", "Account Type", accType.getLiveAccountName());
		System.out.printf("%-30s : %s\n", "Currency", currency);
		System.out.println("***********************************************************");

		// Reject today submitted additional account application
		if ("0".equals(applicationStatus)) {
			updateAdditionalAccApplicationToRejectStatus(admin, userId, additionalAccountID, realName, applicationDateTime);
		}

		System.out.println("***********************************************************");
	}

	private void rejectPastAdditionalAccApplication(AdminAPIUserAccount admin, String userId, String status) {
		System.out.println("***Reject Past Additional Account Application***");

		// Get past submitted additional account application
   		List<Map<String, String>> additionalAccountList = getAdditionalAccApplicationList(admin, userId, status, null, GlobalMethods.setToDateOnly(1));

		if (additionalAccountList != null && !additionalAccountList.isEmpty()) {
			for (Map<String, String> accountInfo : additionalAccountList) {
				String additionalAccountID = accountInfo.get("accauditID");
				String realName = accountInfo.get("realName");
				String applicationDateTime = accountInfo.get("applicationTime");

				updateAdditionalAccApplicationToRejectStatus(admin, userId, additionalAccountID, realName, applicationDateTime);
			}
		} else {
			System.out.println("No past additional account application found");
		}

		System.out.println("***********************************************************");
	}

	/*public void funcAddAccMobile(Boolean isWeb) {
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPOpenAdditionalAccount open_account = myfactor.newInstance(CPOpenAdditionalAccount.class);
		CPLiveAccounts live_account = myfactor.newInstance(CPLiveAccounts.class);

		//Test Data
		PLATFORM p_platform = null;
		ACCOUNTTYPE p_type = null;
		CURRENCY p_currency = null;
		Random random = new Random();

		menu.goToMenu(CPMenuName.CPPORTAL);

		if(isWeb)
			menu.goToMenu(CPMenuName.ADDACCOUNT);
		else
			menu.goToMenu(CPMenuName.ADDACCOUNTMOBILE);

		if(!GlobalMethods.getBrand().equalsIgnoreCase("VT") && !GlobalMethods.getBrand().equalsIgnoreCase("PUG")) {
			List<PLATFORM> platforms = open_account.getAllPlatForms();
			Assert.assertTrue(platforms.size() > 0, "AccountManagementTestCases: Did not find any platform!");
			p_platform = platforms.get(random.nextInt(platforms.size()));
			Assert.assertTrue(open_account.setPlatForm(p_platform));

			//set platform to Globalproperties
			GlobalProperties.platform = p_platform.toString();
			GlobalMethods.printDebugInfo("Global platform is :" + GlobalProperties.platform);

			List<ACCOUNTTYPE> types = open_account.getAlAccountTypes();
			//remove PAMM
			types.removeIf(accType -> {
				if (ACCOUNTTYPE.PAMM.compareTo(accType) == 0) {
					return true;
				}
				return false;
			});
			Assert.assertTrue(types.size() > 0, "AccountManagementTestCases: Did not find any account type!");
			p_type = types.get(random.nextInt(types.size()));
			Assert.assertTrue(open_account.setAccountType(p_type));

			List<CURRENCY> currency = open_account.getAllCurrency();
			Assert.assertTrue(currency.size() > 0, "AccountManagementTestCases: Did not find any currency!");
			p_currency = currency.get(random.nextInt(currency.size()));
			Assert.assertTrue(open_account.setCurrency(p_currency));
			open_account.setNote("From automation testcase");

			String info = "*********Summary information************\n * Platform: " + p_platform.toString()
					+ "\n * Account Type: " + p_type.toString() + "\n * Currency: " + p_currency;
		}
		else {
			open_account.setAccount(PLATFORM.MT4.toString().toLowerCase(), "standardSTP", "USD");
			open_account.setNote("From automation testcase");
		}
		Assert.assertTrue(open_account.submit(),"ERROR: submit application meets error， please check the page！");

		//check
	*//*	if(!BaseTestCase.Brand.equalsIgnoreCase("UM")) {
			menu.goToMenu(CPMenuName.LIVEACCOUNTS);
			Assert.assertTrue(live_account.checkNewAccount(p_platform, p_type, p_currency, STATUS.PROCESSING),"Do Not Find the application!");
			GlobalMethods.printDebugInfo(info);
			GlobalMethods.printDebugInfo("Test open additional accounts succeed!");
		}*//*
	}*/

	// endregion

	// region [ Account Transfer ]

    public void getFromAccountByAPI(){
        Map<String, Object> map = new HashMap<>();
        cpWalletapi.transfer_getAvailableWalletAndLogins(map);

        Map<String,Object> loginData = (Map<String,Object>)map.get("login");
        Map<String,Object> walletData = (Map<String,Object>)map.get("cryptoWallet");

        loginAccount = loginData.get("login").toString();
        currency = loginData.get("currency").toString();

        walletAccount = walletData.get("walletCurrencyAccountNo").toString();

        walletCurrency = walletData.get("currency").toString();

        transferRate = cpWalletapi.getTransferRate(loginAccount, String.valueOf(amount), currency, "0", walletAccount,walletCurrency , walletData.get("userId").toString());

    }

    public void transAccMT2Wallet(){
        System.out.println("***Start Funds Transfer***");

        menu.goToMenu(CPMenuName.TRANSFERACCOUNTS);

        CPTransfer cpTransfer = myfactor.newInstance(CPTransfer.class);

        cpTransfer.clickFrom();
        cpTransfer.setFromAccountNew(loginAccount);
        cpTransfer.clickTo();
        cpTransfer.setToAccountNew(walletAccount);
        cpTransfer.setAmount(String.valueOf(amount));
        cpTransfer.submitTransfer();

        JSONObject rateData = transferRate.getJSONObject("data");

        double processFee = rateData.getDouble("processingFee");
        Double finalAmount = rateData.getDouble("finalAmount");

        this.verifyHistory(loginAccount,"V-Wallet",amount,processFee,finalAmount);

    }

    public void transAccWallet2MT(){
        menu.goToMenu(GlobalProperties.CPMenuName.TRANSFERACCOUNTS);

        CPTransfer cpTransfer = myfactor.newInstance(CPTransfer.class);

        cpTransfer.clickFrom();
        cpTransfer.setFromAccountNew(walletAccount);
        cpTransfer.clickTo();
        cpTransfer.setToAccountNew(loginAccount);
        cpTransfer.setAmount(String.valueOf(amount));
        cpTransfer.submitTransfer();

        JSONObject rateData = transferRate.getJSONObject("data");

        double processFee = rateData.getDouble("processingFee");

        this.verifyHistory("V-Wallet",loginAccount,amount,processFee,null);

    }

    public void transAcMT2MTWithCredit(){

        Assert.assertTrue(StringUtils.isNoneBlank(loginAccount),"There isn't MT account with credit");

        menu.goToMenu(GlobalProperties.CPMenuName.TRANSFERACCOUNTS);

        CPTransfer cpTransfer = myfactor.newInstance(CPTransfer.class);

        cpTransfer.clickFrom();
        cpTransfer.setFromAccountNew(loginAccount);

        String accountTo = cpTransfer.getTransferToAccount();
        cpTransfer.setToAccountNew(accountTo);
        cpTransfer.setAmount(String.valueOf(amount));
        cpTransfer.submitTransfer();

        boolean isCredit = cpTransfer.checkCredit();
        if(!isCredit){
            try {
                cpTransfer.giveUpCreditBtn();
            } catch (Exception e) {
                GlobalMethods.printDebugInfo("give up credit");
            }
        }

        cpTransfer.clickUseCreditBtn();
        cpTransfer.gotoHistory();

        CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
        Assert.assertTrue(history.checkTransferMT2Wallet(loginAccount,accountTo,String.valueOf(amount), GlobalProperties.STATUS.PROCESSING),
                "check history failed");
        LogUtils.info("***Test transfer MT 2 Mt with credit succeed!!********");

    }

    private void verifyHistory(String fromAccount,String accountTo,double amount,double processFee,Double finalAmount){
        CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
        AUCPTransferPage cpTransfer = myfactor.newInstance(AUCPTransferPage.class);

        Assert.assertTrue(cpTransfer.checkTransferReview(fromAccount,accountTo,amount,processFee,finalAmount),"review transfer failed");

        cpTransfer.confirmTransfer();

        try {
            cpTransfer.giveUpCreditBtn();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("There isn't credit in the account");
        }
        cpTransfer.gotoHistory();

        Assert.assertTrue(history.checkTransferMT2Wallet(fromAccount,accountTo,String.valueOf(amount), GlobalProperties.STATUS.PROCESSING),
                "check history failed");
        LogUtils.info("***Test transfer MT 2 wallet succeed!!********");
    }

    protected void funcTransfer() {
		System.out.println("***Start Funds Transfer***");
		menu.goToMenu(CPMenuName.TRANSFERACCOUNTS);
		
		CPTransfer tf = myfactor.newInstance(CPTransfer.class);

		/**
		 * 查找两个相同币种的账号，选择余额多的作为转出账号，余额小的作为转入账号
		 */
		String fr_account = tf.getTransferFromAccount();
		assertNotNull(fr_account,"No available transfer from account found");
		tf.setFromAccountNew(fr_account);

		String to_account = tf.getTransferToAccount();
		assertNotNull(to_account,"No available transfer to account found");
		tf.setToAccountNew(to_account);

		// Set transfer amount to 1 in order to make sure enough balance can be used for other tests
		tf.setAmount("1.00");
		tf.submit();
		System.out.println("***Test Funds Transfer succeed!!********");
	}

//	protected void funcTransfer() {
//		String fr_account="";
//		String to_account="";
//		String amount="";
//
//		CPMenu menu = myfactor.newInstance(CPMenu.class);
//		menu.goToMenu(CPMenuName.CPPORTAL);
//		menu.goToMenu(CPMenuName.TRANSFERACCOUNTS);
//
//		CPTransfer tf = myfactor.newInstance(CPTransfer.class);
//		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
//		Random random = new Random();
//		int f_size = 0;
//		int t_size = 0;
//		Boolean find = false;
//		CURRENCY currency = null;
//		/**
//		 * 查找两个相同币种的账号，选择余额多的作为转出账号，余额小的作为转入账号
//		 */
//		for(CURRENCY c: CURRENCY.values()) {
//			List<Account> accs = tf.getFromAccountByCurrency(c);
//			f_size = accs.size();
//			//need two same currency account
//			if(f_size > 1) {
//				Double max = 0.0;
//				for(Account acc:accs) {
//					Double balance =Double.valueOf(acc.getBalance());
//					if(balance>max) {
//						currency = c;
//						max = balance;
//						fr_account = acc.getAccNumber();
//						int t_amount =0;
//						while((t_amount = random.nextInt(balance.intValue()+1))==0) {
//							//do nothing
//						};
//						amount = String.valueOf(t_amount)+".00";
//						find = true;
//					}
//				}
//			}
//			if(find) {
//				break;
//			}
//		}
//		assertTrue(find,"Did not find available account");
//		tf.setFromAccount(fr_account);
//		List<Account> to_accs = tf.getToAccount();
//		t_size = to_accs.size();
//		assertEquals(f_size, t_size+1,"Find from account("+currency+")： " + f_size + ", and to account size: " + t_size );
//
//		to_account = to_accs.get(random.nextInt(t_size)).getAccNumber();
//
//
//		tf.setToAccount(to_account);
//		//tf.setAmount(amount);
//		//Update transfer amount to 1 in order to make sure enough balance can be used for other tests
//		tf.setAmount("1.00");
//		tf.submit();
//		//no need to check if auto paid for transfer
//	/*
//			menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
//			menu.refresh();
//			//assertTrue(history.checkTransfer(fr_account, to_account, amount, STATUS.PAID));
//			assertTrue(history.checkTransfer(fr_account, to_account, "1.00", STATUS.PAID));
//	*/
//	}

	protected void funcCopytradingTransfer() {
		System.out.println("***Start Copy Trading Funds Transfer***");
		menu.goToMenu(CPMenuName.TRANSFERACCOUNTS);

		CPTransfer tf = myfactor.newInstance(CPTransfer.class);

		/**
		 * non-copytrading作为转出账号，copytrading作为转入账号
		 */
		String fr_account = tf.getTransferFromNonCPAccount();
		assertNotNull(fr_account,"No available transfer from account found");
		tf.setFromAccountNew(fr_account);

		String to_account = tf.getTransferToCPAccount();
		assertNotNull(to_account,"No available transfer to account found");
		tf.setToAccountNew(to_account);

		// Set transfer amount to 1 in order to make sure enough balance can be used for other tests
		tf.setAmount("1.00");
		tf.submit();
		System.out.println("***Test Copy Trading Funds Transfer succeed!!********");
	}

	// endregion

	// region [ Account Info Check ]

	public void liveAccountInfoCheck(PLATFORM platform) throws Exception {
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		CPAPIAccount api = new CPAPIAccount((String)data[0][3], driver);
		GlobalProperties.platform = platform.toString();

		System.out.println("***Live Account Check***");
		List<Account> accountList = redirectToLivePage(platform, menu, liveAccounts);

		// Get account details
		JSONObject result = api.queryMetaTraderAccountDetail();

		// Validate trading account info
		System.out.println("***Validate Live Account Info***");
		validateLiveAccountInfo(result, accountList, platform);

		System.out.println("***Test " + platform.getPlatformDesc() + " Live Account Check succeed!!********");
	}

	public void validateLiveAccountInfo(JSONObject result, List<Account> accountList, PLATFORM filterPlatform) {

		// Check empty on api
		if (result == null || result.getJSONArray("data") == null || result.getJSONArray("data").isEmpty()){
			Assert.fail("No trading account found");
		}

		JSONArray apiAccountList = result.getJSONArray("data");
		String filterApiPlatform = filterPlatform.getServerCategory();

//		// Check empty on api account list
//		if (apiAccountList == null || apiAccountList.isEmpty()) {
//			GlobalMethods.printDebugInfo("No need to check account. API Account List is empty");
//			return;
//		}

		// Filter api account list based on platform
		List<JSONObject> filterApiAccountList = IntStream.range(0, apiAccountList.size())
				.mapToObj(apiAccountList::getJSONObject)
				.filter(obj -> obj.getString("serverCategory").equals(filterApiPlatform))
				.toList();

		// Check empty on api account list
		if (filterApiAccountList.isEmpty()) {
			GlobalMethods.printDebugInfo("No need to check account. No available " + filterPlatform + " account in API List");
			return;
		}

		// Filter account list based on platform
		accountList = accountList
				.stream()
				.filter(obj -> obj.getPlatform().equals(filterPlatform))
				.toList();

		// Check empty on account list
		if (accountList.isEmpty()) {
			Assert.fail(filterPlatform + " found in API Account List but not display in Account Page");
		}

		// Compare account page account values with api list
		for (int i = 0; i < accountList.size(); i++) {

			// Get account list value
			Account acc = accountList.get(i);
			ACC_STATUS status = acc.getAccStatus();
			PLATFORM platform = acc.getPlatform();
			String accNo = acc.getAccountNum();
			String leverage = acc.getLeverage();
			ACCOUNTTYPE accountType = acc.getType();
			String server = acc.getServer();
			String equity = acc.getEquity();
			CURRENCY currency = acc.getCurrency();
			String credit = acc.getCredit();
			String balance = acc.getBalance();
			String accNickname = acc.getAccountNickname();

			// Get specific api account by account number
			JSONObject apiAcc  = filterApiAccountList
					.stream()
					.filter(f -> f.getString("mt4_account").equals(accNo))
					.findFirst()
					.orElse(null);

			// Check if api account found in Account page
			if (apiAcc == null) {
				Assert.fail("Account: " + accNo + " found in Account page but not in API Account List");
			}

			// Get api value
			String apiServerCat = apiAcc.getString("serverCategory");
			String apiAccNo = apiAcc.getString("mt4_account");
			String apiEquity = apiAcc.getString("equity");
			String apiCurrency = apiAcc.getString("currency");
			String apiAccTypeCode = apiAcc.getString("mt4_account_type");
			String apiStatus = apiAcc.getString("status");
			String apiIsArchive  = apiAcc.getString("isArchive");
			String apiCredit  = apiAcc.getString("credit");
			String apiLeverage  = apiAcc.getString("leverage");
			String apiServer  = apiAcc.getString("server");
			String apiBalance  = apiAcc.getString("balance");
			String apiAccNickname  = apiAcc.getString("nickname");

			// Compare UI and API values
			// Cross-check platform value
			PLATFORM apiPlatform = GlobalProperties.PLATFORM.getRecByServerCategory(apiServerCat);
			if (!platform.equals(apiPlatform)) {
				Assert.fail("Account Platform mismatch. Account No.: " + apiAccNo + ", Actual: " + Objects.toString(apiPlatform, "") + " (" + apiServerCat + "), Display: " + platform);
			}

			// Cross-check account number value
			if (!Objects.toString(accNo, "").equalsIgnoreCase(Objects.toString(apiAccNo, ""))) {
				Assert.fail("Account Number mismatch. Actual: " + apiAccNo + ", Display: " + accNo);
			}

			// Cross-check leverage value
			if (!Objects.toString(leverage, "").contains(Objects.toString(apiLeverage, ""))) {
				Assert.fail("Leverage mismatch. Account No.: " + apiAccNo + ", Actual: " + apiLeverage + ", Display: " + leverage);
			}

			// Cross-check server value
			if (!Objects.toString(server, "").equalsIgnoreCase(Objects.toString(apiServer, ""))) {
				Assert.fail("Server mismatch. Account No.: " + apiAccNo + ", Actual: " + apiServer + ", Display: " + server);
			}

			// Cross-check nickname value
			if (!Objects.toString(accNickname, "").equalsIgnoreCase(Objects.toString(apiAccNickname, ""))) {
				Assert.fail("Account nickname mismatch. Account No.: " + apiAccNo + ", Actual: " + apiAccNickname + ", Display: " + accNickname);
			}

			// Cross-check account type value
			ACCOUNTTYPE apiAccType = ACCOUNTTYPE.getRecByAccTypeCode(apiAccTypeCode, apiPlatform);
			String apiAccountTypeDesc = apiAccType == null ? "" : apiAccType.getLiveAccountName();
			String accountTypeDesc = accountType == null ? "" : accountType.getLiveAccountName();

			if (!accountTypeDesc.equals(apiAccountTypeDesc)) {
				Assert.fail("Account Type mismatch. Account No.: " + apiAccNo + ", Actual: " + apiAccountTypeDesc + " (" + apiAccTypeCode + "),  Display: " + accountTypeDesc);
			}

			// Check Equity, Currency, Credit, Balance
			if (!"1".equals(apiStatus) || ("1".equals(apiIsArchive) || "2".equals(apiIsArchive))) {
				if (!Objects.toString(equity, "").equalsIgnoreCase("--")) {
					apiEquity = apiEquity == null ? "" : apiEquity;
					Assert.fail("Equity display incorrect. Account No.: " + apiAccNo + ", Actual: " + apiEquity + ", Display: " + equity);
				}
			} else {
				// Cross-check equity value
				if (!GlobalMethods.compareNumericValues(apiEquity, equity)) {
					Assert.fail("Equity mismatch. Account No.: " + apiAccNo + ", Actual: " + apiEquity + ", Display: " + equity);
				}

				// Cross-check currency value
				if (!Objects.toString(currency, "").equalsIgnoreCase(Objects.toString(apiCurrency, ""))) {
					Assert.fail("Account Currency mismatch. Account No.: " + apiAccNo + ", Actual: " + apiCurrency + ", Display: " + currency);
				}

				// Cross-check credit value
				if (!GlobalMethods.compareNumericValues(apiCredit, credit)) {
					Assert.fail("Account Credit mismatch. Account No.: " + apiAccNo + ", Actual: " + apiCredit + ", Display: " + credit);
				}

				// Cross-check balance value
				if (!GlobalMethods.compareNumericValues(apiBalance, balance)) {
					Assert.fail("Account Balance mismatch. Account No.: " + apiAccNo + ", Actual: " + apiBalance + ", Display: " + balance);
				}
			}

			acc.printAccount();

			if (i != filterApiAccountList.size() - 1) {
				GlobalMethods.printDebugInfo("********************************************");
			}

		}
	}

	public void switchAccountDisplayModeCheck(PLATFORM platform) throws Exception {
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		int gridModeAccSize = 0, listModeAccSize = 0;
		GlobalProperties.platform = platform.toString();

		System.out.println("***Switch Account Display Mode Check***");
		List<Account> gridAccountList = redirectToLivePage(platform, menu, liveAccounts);
		gridModeAccSize = gridAccountList.size();
		GlobalMethods.printDebugInfo("Grid Mode Account List Size: " + gridModeAccSize);

		// View record in list mode
		liveAccounts.setViewContentListMode();
		// Get account list
		List<Account> accountList = liveAccounts.getAccounts_ListMode(platform);
		listModeAccSize = accountList.size();
		GlobalMethods.printDebugInfo("List Mode Account List Size: " + listModeAccSize);

		// Check if both view mode has the same account list size
		if (listModeAccSize != gridModeAccSize) {
			Assert.fail("Account List size mismatch between display mode, Grid Mode: " + gridModeAccSize + ", List Mode: " + listModeAccSize);
		}

		System.out.println("***Test Switch " + platform.getPlatformDesc() +  " Account Display Mode succeed!!********");
	}

	public void liveAccountLeverageCheck(PLATFORM platform, String leverage) {
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		GlobalProperties.platform = platform.toString();

		System.out.println("***Live Account Leverage Check***");
		List<Account> accountList = redirectToLivePage(platform, menu, liveAccounts);

		// Random select an account
		Account acc = liveAccounts.randomSelectAccount(accountList);
		liveAccounts.clickAccountLeverageBtn(platform, acc.getAccountNum());

		String currMaxLeverage = liveAccounts.getMaxLeverage().replace(" ", "").replace(":1", "").trim();
		Map.Entry<String, String> maxLeverageAllowed = getCountryMaxLeverage();

		int current = Integer.parseInt(currMaxLeverage);
		int allowed = Integer.parseInt(maxLeverageAllowed.getValue());
		String countryDesc = maxLeverageAllowed.getKey();

		// Check if country highest leverage match with expected leverage
		if (allowed != Integer.parseInt(leverage)) {
			Assert.fail(String.format("Expected highest leverage is %s but highest leverage in Country %s is %s. Please change another country.", leverage, countryDesc, allowed));
		}

		// Compare current available highest leverage with country allowed max leverage
		if (current > allowed) {
			Assert.fail(String.format("Available highest leverage (%s) exceeds the allowed leverage (%s) in Country %s", current, allowed, countryDesc));
		} else if (current < allowed) {
			Assert.fail(String.format("Available highest leverage is %s; the maximum allowed leverage in Country %s should be %s", current, countryDesc, allowed));
		}

		GlobalMethods.printDebugInfo(String.format("Available highest leverage: %s; Country %s maximum allowed leverage: %s", current, countryDesc, allowed));

		// Close leverage dialog
		liveAccounts.closeDialog();

		System.out.println("***Test Live " + platform.getPlatformDesc() +  " Account Leverage Check succeed!!********");
	}

	public Map.Entry<String, String> getCountryMaxLeverage() {
		CPAPIAccount api = new CPAPIAccount((String)data[0][3], driver);

		// Get user's country
		JSONObject result = null;
		try {
			result = api.queryProfileInfo();
		} catch (Exception ex) {
			Assert.fail("Failed to query profile info");
		}

		String countryId = result.getJSONObject("data").getString("countryId");
		String countryDesc = result.getJSONObject("data").getString("country");
		if (countryId.isEmpty()) Assert.fail("User's country is empty");

		// Max leverage can be retrieved from OWS -> Leverage Mgmt -> Country Configuration
		String maxLeverageAllowed =  switch (countryId) {
			// Malaysia, Thailand
			case "5015", "6163" -> "2000";
			// Brazil, Poland
			case "3636", "3716" -> "1000";
			// Italy, France, United Kingdom
			case "6777", "4101", "6907" -> "500";
			default -> "";
		};

		if (maxLeverageAllowed.isEmpty()) {
			Assert.fail(String.format("Country %s maximum allowed leverage is empty", countryDesc));
		}

		return new AbstractMap.SimpleEntry<>(countryDesc, maxLeverageAllowed);
	}

	// endregion

	// region [ Update Account ]

	public void changeLiveAccountNickname(PLATFORM platform) {
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		GlobalProperties.platform = platform.toString();

		System.out.println("***Change Account Nickname***");
		List<Account> accountList = redirectToLivePage(platform, menu, liveAccounts);

		// Random select an account
		Account acc = liveAccounts.randomSelectAccount(accountList);
		String accNo = acc.getAccountNum();

		// Set nickname
		Random rand = new Random();
		int randomNum = rand.nextInt(1000); // 0 to 1000 inclusive
		String newAccNickname = accNo + String.valueOf(randomNum);
		liveAccounts.setAccountNickname(platform, accNo, newAccNickname);

		// Submit changes
		liveAccounts.submitChgAccountNickname(accNo);

		// Check new value update status
		System.out.println("***Check Account Nickname Update Status***");
		int maxRetries = 3;

		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			menu.refresh();
			menu.waitLoading();

			accountList = redirectToLivePage(platform, menu, liveAccounts);

			for(Account c: accountList) {
				if(c.getAccountNum().equals(accNo)) {
					String accLatestNickname = c.getAccountNickname().replace(" ", "");
					System.out.println("Account: " + accNo + ", Account Nickname: " + accLatestNickname + ", Expected Account Nickname: " + newAccNickname);

					if(StringUtils.containsIgnoreCase(accLatestNickname, newAccNickname)) {
						System.out.println("Account Latest Info:");
						c.printAccount();
						System.out.println("***Test Change " + platform.getPlatformDesc() +  " Account Nickname succeed!!********");
						return;
					}
					break;
				}
			}

			if (attempt != maxRetries) {
				LogUtils.info("Retry fetching account nickname update status on attempt " + attempt);
			}
		}

		LogUtils.info("Slow update from server. Please manually verify the account nickname value again in a short while.");
		Assert.fail("Slow update from server. Please manually verify the account nickname value again in a short while.");
	}

	public void liveAccountRemoveCredit(PLATFORM platform) {
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		GlobalProperties.platform = platform.toString();

		System.out.println("***Remove Account Credit***");
		List<Account> accountList = redirectToLivePage(platform, menu, liveAccounts);

		// Random select account
		Account acc = liveAccounts.randomSelectAccount(accountList);
		String accNo = acc.getAccountNum();

		// Credit Adjustment
		checkForCreditAvailability(platform, accNo);

		// Remove account credit
		liveAccounts.clickRemoveCreditBtn(platform, accNo);
		Map.Entry<Boolean, String> resp = liveAccounts.submitRemoveAccountCredit();
		assertTrue(resp.getKey(), "Failed to remove credit for account " + accNo + ". Resp Msg: " + resp.getValue());

		// Check new value update status
		System.out.println("***Check Account Credit Update Status***");

		int maxRetries = 3;

		for (int attempt = 1; attempt <= maxRetries; attempt++) {

			menu.refresh();
			menu.waitLoading();

			accountList = redirectToLivePage(platform, menu, liveAccounts);

			for(Account c: accountList) {
				if(c.getAccountNum().equals(accNo)) {
					String accLatestCredit = c.getCredit();
					String accNewCredit = "0";
					System.out.println("Account: " + accNo + ", Account Credit: " + accLatestCredit + ", Expected Account: " + accNewCredit);

					if(GlobalMethods.compareNumericValues(accNewCredit, accLatestCredit)) {
						System.out.println("Account Latest Info:");
						c.printAccount();
						System.out.println("***Test Remove " + platform.getPlatformDesc() +  " Account Credit succeed!!********");
						return;
					}
					break;
				}
			}

			if (attempt != maxRetries) {
				LogUtils.info("Retry fetching account credit update status on attempt " + attempt);
			}
		}

		LogUtils.info("Slow update from server. Please manually verify the account credit value again in a short while.");
		Assert.fail("Slow update from server. Please manually verify the account credit value again in a short while.");
	}

	// endregion

	// region [ General Functions ]

	public List<Account> redirectToLivePage(PLATFORM platform, CPMenu menu, CPLiveAccounts liveAccounts) {
		menu.goToMenu(CPMenuName.LIVEACCOUNTS);

		liveAccounts.waitLoadingAccountListContent();
		// Filter by live account
		liveAccounts.selectLiveAcc();
		// Filter by platform
		liveAccounts.selectPlatform(platform);
		// Filter by account status
		liveAccounts.selectAccStatus(ACC_STATUS.ACTIVE);
		// View record in grid mode
		liveAccounts.setViewContentGridMode();

		// Get account list
		List<Account> accountList = liveAccounts.getAccounts(platform);

		if(accountList.isEmpty()) {
			LogUtils.info("There is no active " + platform + " account");
			Assert.fail("There is no active " + platform + " account");
		}

		return accountList;
	}

	// endregion
}
