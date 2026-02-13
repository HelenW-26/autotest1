package newcrm.testcases.cptestcases.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.cpapi.CPAPIAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.ACC_STATUS;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.clientpages.LiveAccountsPage.Account;
import newcrm.testcases.BaseTestCaseNew;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.testng.Assert.assertTrue;

public class DemoAccountTestCases extends BaseTestCaseNew {

    public Object data[][];
    private CPMenu menu;

    private Factor myfactor;
    private CPLogin login;
    private WebDriver driver;

    protected CPLiveAccounts liveAccounts;

    @BeforeMethod(groups = {"CP_Demo_Acc_Info_Check", "CP_Demo_Acc_Info_Update"})
    protected void initMethod(Method method) {

        myfactor = getFactorNew();
        login = getLogin();
        driver = getDriverNew();

//		checkValidLoginSession();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }

    @AfterMethod(groups = {"CP_Demo_Acc_Info_Check", "CP_Demo_Acc_Info_Update"})
    public void tearDown(ITestResult result) {
        // Close previous left open dialog if any
        if (liveAccounts != null) {
            liveAccounts.checkExistsDialog();
        }
    }

    // region [ Demo Account Info Check ]

    public void demoAccountInfoCheck(PLATFORM platform) throws Exception {
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        CPAPIAccount api = new CPAPIAccount((String)data[0][3], driver);
        GlobalProperties.platform = platform.toString();

        System.out.println("***Live Account Check***");
        List<Account> accountList = redirectToDemoPage(platform, menu, liveAccounts);

        // Get demo accounts
        JSONObject result = null;
        try {
            result = api.queryDemoAccs();
        } catch (Exception ex) {
            Assert.fail("Failed to query demo account info");
        }

        // Validate trading account info
        System.out.println("***Validate Demo Account Info***");
        validateDemoAccountInfo(result, accountList, platform);

        System.out.println("***Test " + platform.getPlatformDesc() + " Demo Account Check succeed!!********");
    }

    private void validateDemoAccountInfo(JSONObject result, List<Account> accountList, PLATFORM filterPlatform) {

        String filterApiPlatform = filterPlatform.getServerCategory();

//		// Check empty on api account list
//		if (result == null || result.isEmpty() || result.getJSONArray("data") == null || result.getJSONArray("data").isEmpty()) {
//			GlobalMethods.printDebugInfo("No need to check account. API Account List is empty");
//			return;
//		}

        JSONArray apiAccountList = result.getJSONArray("data");

        // Filter api account list based on platform
        List<JSONObject> filterApiAccountList = IntStream.range(0, apiAccountList.size())
                .mapToObj(apiAccountList::getJSONObject)
                .filter(obj -> obj.getString("platform").equals(filterApiPlatform))
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

            // Get specific api account by account number
            JSONObject apiAcc  = filterApiAccountList
                    .stream()
                    .filter(f -> f.getString("accNum").equals(accNo))
                    .findFirst()
                    .orElse(null);

            // Check if api account found in Account page
            if (apiAcc == null) {
                Assert.fail("Account: " + accNo + " found in Account page but not in API Account List");
            }

            // Get api value
            String apiServerCat = apiAcc.getString("platform");
            String apiAccNo = apiAcc.getString("accNum");
            String apiEquity = apiAcc.getString("equity");
            String apiCurrency = apiAcc.getString("currency");
            String apiAccTypeCode = apiAcc.getString("accMt4Type");
            String apiStatus = apiAcc.getString("status");
            String apiLeverage  = apiAcc.getString("leverage");
            String apiServer  = apiAcc.getString("server");

            // Compare UI and API values
            // Cross-check platform value
            PLATFORM apiPlatform = PLATFORM.getRecByServerCategory(apiServerCat);
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

            // Cross-check account type value
            ACCOUNTTYPE apiAccType = ACCOUNTTYPE.getRecByAccTypeCode(apiAccTypeCode, apiPlatform);
            String apiAccountTypeDesc = apiAccType == null ? "" : apiAccType.getLiveAccountName();
            String accountTypeDesc = accountType == null ? "" : accountType.getLiveAccountName();

            if (!Objects.toString(accountTypeDesc, "").equals(Objects.toString(apiAccountTypeDesc, ""))) {
                Assert.fail("Account Type mismatch. Account No.: " + apiAccNo + ", Actual: " + apiAccountTypeDesc + " (" + apiAccTypeCode + "),  Display: " + accountTypeDesc);
            }

            // Check Equity, Currency, Credit, Balance
            if (!"active".equalsIgnoreCase(Objects.toString(apiStatus, ""))) {
                if (!"--".equalsIgnoreCase(Objects.toString(equity, ""))) {
                    Assert.fail("Equity display incorrect. Account No.: " + apiAccNo + ", Actual: " + Objects.toString(apiEquity, "") + ", Display: " + equity);
                }
            } else if(apiEquity == null || apiCurrency == null || apiCurrency.isEmpty()) {
                if (!"--.--".equalsIgnoreCase(Objects.toString(equity, "")) && !"--".equalsIgnoreCase(Objects.toString(equity, ""))) {
                    Assert.fail("Equity display incorrect. Account No.: " + apiAccNo + ", Actual: " + Objects.toString(apiEquity, "") + ", Display: " + equity);
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
            }

            acc.printDemoAccountCheck();

            if (i != filterApiAccountList.size() - 1) {
                GlobalMethods.printDebugInfo("********************************************");
            }

        }
    }

    // endregion

    // region [ Update Demo Account Info ]

    public void changeDemoAccountInfo(PLATFORM platform) {
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        GlobalProperties.platform = platform.toString();

        System.out.println("***Change Demo Account Info***");
        List<Account> accountList = redirectToDemoPage(platform, menu, liveAccounts);

        // Random select an account
        Account acc = liveAccounts.randomSelectAccount(accountList);

        // Update account balance
        String newBal = chgDemoAccountBalance(platform, acc);
        // Update account leverage
        String newLeverage = chgDemoAccountLeverage(platform, acc);

        // Check new value update status
        System.out.println("***Check Demo Account Info Update Status***");
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            menu.refresh();
            menu.waitLoading();

            accountList = redirectToDemoPage(platform, menu, liveAccounts);

            for(Account c: accountList) {
                String accNum = c.getAccountNum();
                if(c.getAccountNum().equals(acc.getAccountNum())) {
                    String accLatestBal = c.getEquity();
                    String accLatestLeverage = c.getLeverage().replace(" ", "");
                    System.out.println("Account: " + accNum + ", Balance: " + accLatestBal + ", Expected Balance: " + newBal);
                    System.out.println("Account: " + accNum + ", Leverage: " + accLatestLeverage + ", Expected Leverage: " + newLeverage);

                    boolean isEquityChanged = GlobalMethods.compareNumericValues(newBal, accLatestBal);
                    boolean isLeverageChanged = StringUtils.containsIgnoreCase(accLatestLeverage, newLeverage);

                    if (!isEquityChanged || !isLeverageChanged) break;

                    System.out.println("Account Latest Info:");
                    c.printAccount();
                    System.out.println("***Test Change " + platform + " demo account info succeed!!********");
                    return;
                }
            }

            if (attempt != maxRetries) {
                LogUtils.info("Retry fetching balance & leverage update status on attempt " + attempt);
            }
        }

        LogUtils.info("Slow update from server. Please manually verify the balance & leverage value again in a short while.");
        Assert.fail("Slow update from server. Please manually verify the balance & leverage value again in a short while.");
    }

    private String chgDemoAccountBalance(PLATFORM platform, Account acc) {
        System.out.println("***Change Demo Account Balance***");
        liveAccounts.clickDemoAccountBalanceBtn(platform, acc.getAccountNum());

        String oldBal = acc.getEquity().replace(" ", "");
        String newBal = liveAccounts.setAccountBalance(String.valueOf(GlobalMethods.getBalanceFromString(oldBal)));
        liveAccounts.submitChgDemoAccountBalance();

        return newBal;
    }

    private String chgDemoAccountLeverage(PLATFORM platform, Account acc) {
        System.out.println("***Change Demo Account Leverage***");
        liveAccounts.clickDemoAccountLeverageBtn(platform, acc.getAccountNum());

        String oldLeverage = acc.getLeverage().replace(" ", "");
        String newLeverage = liveAccounts.setAccountLeverage(oldLeverage).replace(" ", "");
        Map.Entry<Boolean, String> resp = liveAccounts.submitLeverageDemo();
        assertTrue(resp.getKey(), "Failed to change leverage for account " + acc.getAccountNum() + ". Resp Msg: " + resp.getValue());

        liveAccounts.closeDialog();

        return newLeverage;
    }

    public void changeDemoAccountLeverage(PLATFORM platform) {
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        GlobalProperties.platform = platform.toString();

        System.out.println("***Change Account Leverage***");
        List<Account> accountList = redirectToDemoPage(platform, menu, liveAccounts);

        // Random select an account
        Account acc = liveAccounts.randomSelectAccount(accountList);

        // Set leverage
        liveAccounts.clickDemoAccountLeverageBtn(platform, acc.getAccountNum());
        String oldLeverage = acc.getLeverage().replace(" ", "");
        String newLeverage = liveAccounts.setAccountLeverage(oldLeverage).replace(" ", "");

        // Submit
        Map.Entry<Boolean, String> resp = liveAccounts.submitLeverageDemo();
        assertTrue(resp.getKey(), "Failed to change leverage for account " + acc.getAccountNum() + ". Resp Msg: " + resp.getValue());

        liveAccounts.closeDialog();

        // Check new value update status
        System.out.println("***Check Account Leverage Update Status***");

        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            menu.refresh();
            accountList = redirectToDemoPage(platform, menu, liveAccounts);

            for(Account c: accountList) {
                String accNum = c.getAccountNum();
                if(c.getAccountNum().equals(acc.getAccountNum())) {
                    String accLatestLeverage = c.getLeverage().replace(" ", "");
                    System.out.println("Account: " + accNum + ", Leverage: " + accLatestLeverage + ", Expected Leverage: " + newLeverage);

                    if(StringUtils.containsIgnoreCase(accLatestLeverage, newLeverage)) {
                        System.out.println("Account Latest Info:");
                        c.printAccount();
                        System.out.println("***Test Change " + platform + " leverage succeed!!********");
                        return;
                    }
                    break;
                }
            }

            if (attempt != maxRetries) {
                LogUtils.info("Retry fetching leverage update status on attempt " + attempt);
            }
        }

        LogUtils.info("Slow update from server. Please manually verify the leverage value again in a short while.");
        Assert.fail("Slow update from server. Please manually verify the leverage value again in a short while.");
    }

    public void changeDemoAccountBalance(PLATFORM platform) {
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        GlobalProperties.platform = platform.toString();

        System.out.println("***Change Account Balance***");
        List<Account> accountList = redirectToDemoPage(platform, menu, liveAccounts);

        // Random select an account
        Account acc = liveAccounts.randomSelectAccount(accountList);

        // Set balance
        liveAccounts.clickDemoAccountBalanceBtn(platform, acc.getAccountNum());
        String oldBal = acc.getEquity().replace(" ", "");
        String newBal = liveAccounts.setAccountBalance(String.valueOf(GlobalMethods.getBalanceFromString(oldBal)));

        // Submit
        liveAccounts.submitChgDemoAccountBalance();

        // Check new value update status
        System.out.println("***Check Account Balance Update Status***");

        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            menu.refresh();
            menu.waitLoading();

            accountList = redirectToDemoPage(platform, menu, liveAccounts);

            for(Account c: accountList) {
                String accNum = c.getAccountNum();
                if(c.getAccountNum().equals(acc.getAccountNum())) {
                    String accLatestBal = c.getEquity();
                    System.out.println("Account: " + accNum + ", Balance: " + accLatestBal + ", Expected Balance: " + newBal);

                    if(GlobalMethods.compareNumericValues(newBal, c.getEquity())) {
                        System.out.println("Account Latest Info:");
                        c.printAccount();
                        System.out.println("***Test Change " + platform + " balance succeed!!********");
                        return;
                    }
                    break;
                }
            }

            if (attempt != maxRetries) {
                LogUtils.info("Retry fetching balance update status on attempt " + attempt);
            }
        }

        LogUtils.info("Slow update from server. Please manually verify the balance value again in a short while.");
        Assert.fail("Slow update from server. Please manually verify the balance value again in a short while.");
    }

    // endregion

    // region [ General Functions ]

    public List<Account> redirectToDemoPage(PLATFORM platform, CPMenu menu, CPLiveAccounts liveAccounts) {
        menu.goToMenu(CPMenuName.LIVEACCOUNTS);

        liveAccounts.waitLoadingAccountListContent();
        // Filter by live account
        liveAccounts.selectDemoAcc();
        // Filter by platform
        liveAccounts.selectPlatform(platform);
        // Filter by account status
        liveAccounts.selectDemoAccStatus(ACC_STATUS.ACTIVE);
        // View record in grid mode
        liveAccounts.setViewContentGridMode();

        // Get account list
        List<Account> accountList = liveAccounts.getDemoAccountsByPlatform(platform);

        // Filter out demo account with invalid equity
        accountList = accountList.stream()
                .filter(m -> !"--.--".equalsIgnoreCase(m.getEquity()))
                .toList();

        if(accountList.isEmpty()) {
            GlobalMethods.printDebugInfo("There is no active " + platform + " account");
            Assert.fail("There is no active " + platform + " account");
        }

        return accountList;
    }

    // endregion
}
