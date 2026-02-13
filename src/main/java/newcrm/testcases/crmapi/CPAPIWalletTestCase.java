package newcrm.testcases.crmapi;

import lombok.extern.slf4j.Slf4j;
import newcrm.cpapi.CPAPIWalletBase;
import newcrm.global.GlobalMethods;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.utils.api.YamlDataProviderUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class CPAPIWalletTestCase {
    protected Object data[][];
    protected String currenURL;
    protected String currentBrand;
    protected String currentServer;
    protected String currentEmail;
    protected String currentPassword;
    protected CPAPIWalletBase api;
    protected Map<String, Object> testData = new HashMap<>();
    public final String CONFIG_FILE = "testData/walletBaseInterfData.yaml";
    private final AllureTestListener allureTestListener = new AllureTestListener();

    public CPAPIWalletTestCase() {
        // 从yaml中读取数据
        // 设置初始值，给单独运行testcase使用
        Object[][] configData = YamlDataProviderUtils.getTestData(CONFIG_FILE);
        Map<String, Object> config = (Map<String, Object>) configData[0][0];
        this.currentBrand = String.valueOf(config.get("brand"));
        this.currentServer = String.valueOf(config.get("server"));
        this.currenURL = String.valueOf(config.get("url"));
        this.currentEmail = String.valueOf(config.get("email"));
        this.currentPassword = String.valueOf(config.get("password"));
    }

    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server", "TraderURL", "TraderName", "TraderPass"})
    public void beforeTest(@Optional String brand, @Optional String server, @Optional String apiUrl,@Optional String username,
                           @Optional String password, ITestContext context) {
        if (brand == null) brand = currentBrand;
        brand = GlobalMethods.setEnvValues(brand);
        server = GlobalMethods.setEnvValues(server);
        GlobalMethods.setEnvValues(brand);

        // 如果提供了XML/Jenkins参数，XML/Jenkins参数
        if (apiUrl != null && !apiUrl.isEmpty() && username != null && !username.isEmpty()
                && password != null && !password.isEmpty()) {
            System.out.println("Using XML or Jenkins parameter");
            this.api = new CPAPIWalletBase(apiUrl, username, password);
        } else if(brand!= null && server!= null){
            System.out.println("No password, Using TestDataProvider parameter");
            data = setDate(brand, server);
            this.api = new CPAPIWalletBase((String) data[0][3], (String) data[0][1], (String) data[0][2]);
        }else {
            // 否则使用yaml数据读取方式
            System.out.println("Using default parameter");
            this.api = new CPAPIWalletBase(currenURL, currentEmail, currentPassword);
        }
    }
    public Object[][] setDate(String brand, String server){
        if(server.toLowerCase().contains("uat")) {
            System.out.println(brand + server);
            System.out.println("Using UAT parameter");
            data = UATTestDataProvider.getUATAPIWalletUsersData(brand, server);
        }else {
            System.out.println("Using Alpha parameter");
            data = TestDataProvider.getAlphaAPIWalletUsersData(brand, server);
        }
        return data;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        allureTestListener.logTestFailure(result);
    }

    @Test(description = "钱包帐户页面")
    public void walletopenaccountvalidate() {
        api.open_account_validate();
        api.wallethome();
        api.base_info();
        api.level_info();
//        api.flow_detail();
        api.select_list();
        api.H5getCurrencyChains();
        api.flow_list();
    }

    @Test(description = "钱包闪兑页面操作")
    public void wallet_conver_submit() {
        api.user_permission();
        api.get_conver_list("USDT");
        api.get_quantity_and_exchange("USDT", "USDC", "0.01");
        api.conver_submit(testData, "USDT", "USDC", "0.01");
        testData.get("tradeorder");
        api.refresh_order(testData);
        api.confirm_order(testData);
        api.record_list();

    }


    @Test(description = "钱包入金页面操作")
    public void wallet_deposit() {
        api.getCurrencyChains();
        api.checkDepositPermissions();
        api.mobilecheckDepositPermissions();
        api.depositRecordList(testData);
        api.depositRecordDetail(testData);
    }

    @Test(description = "钱包出金页面操作")
    public void wallet_withdraw() {
        api.queryBlacklist();
        api.withdraw_currency_list(testData);
        api.order_list(testData);
        api.fetch_crypto_fiat_exchange(testData);
        api.calc_fee(testData, testData.get("withdrawAmt").toString(), testData.get("currency").toString(), "TRC20");
        api.order_verification(testData);
        api.getMultiAuthMethod();
        api.withdraw_order_confirm(testData);
        api.order_detail();
    }

    @Test(description = "钱包转帐页面操作")
    public void wallet_transfer() {
        api.anti_reuse();
        api.transfer_getTransferData_cp(testData);
        api.transfer_getWalletExchangeRate_cp(testData);
        api.wallet_transfer_list();
    }

//     Test testAnnotation = method.getAnnotation(Test.class);
//     String description = testAnnotation != null ? testAnnotation.description() : method.getName();
//     Allure.getLifecycle().updateTestCase(testCase -> testCase.setName(description));
}
