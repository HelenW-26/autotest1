package newcrm.testcases.admintestcases;

import lombok.extern.slf4j.Slf4j;
import newcrm.adminapi.AdminAPI;
import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.utils.api.YamlDataProviderUtils;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static newcrm.adminapi.AdminAPIWallet.*;

@Slf4j
public class AdminAPIWalletTestcase {
    protected Object[][] data;
    public String walletfilePath = "testData/AdminWalletBaseInterfData.yaml";
    protected Map<String, Object> testData = new HashMap<>();
    protected Map<String, Object> configData;

    public AdminAPIWalletTestcase() {
        // 从yaml中读取数据
        // 设置初始值，给单独运行testcase使用
        Object[][] yamlData = YamlDataProviderUtils.getTestData(walletfilePath);
        this.configData = (Map<String, Object>) yamlData[0][0];
    }

    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server", "BranchVersion"})
    public void beforeTest(@Optional String brand, @Optional String server,@Optional String branchVersion, ITestContext context) {
        if (brand==null) {
            // 如果没有提供参数，使用yaml数据
            System.out.println("Using default parameter from YAML");
            this.testData = new HashMap<>(configData);
            brand = GlobalMethods.setEnvValues(testData.get("brand").toString());
            GlobalMethods.setEnvValues(brand);
        } else {
            // 从XML/Jenkins参数到TestDataProvider读取
            // 品牌转换au->vfx
            brand = GlobalMethods.setEnvValues(brand);
            GlobalMethods.setEnvValues(brand);
            if (server.toLowerCase().contains("uat")) {
                data = UATTestDataProvider.getUATRegUsersData(brand, server);
                this.testData.put("testEnv", "UAT");
            }else {
                data = TestDataProvider.getAlphaRegUsersData(brand, server);
                this.testData.put("testEnv", "ALPHA");
            }
            this.testData.put("userName_login", data[0][7].toString());
            this.testData.put("password_login", data[0][8].toString());
            this.testData.put("url", data[0][4].toString());
            this.testData.put("brand", brand);
            this.testData.put("regulator", data[0][0].toString());
            System.out.println(testData);
            System.out.println("Using TestDataProvider from XML/Jenkins parameter");
        }
        AdminAPI.loginAdmin(testData);
        setBranchVersionHeader(branchVersion);
    }
    @DataProvider(name = "testData")
    public Object[][] getTestData() {
        // 获得最新的 testData（包含 jsId）
        return new Object[][] { { new HashMap<>(testData) } };
    }

    @Test(dataProvider = "testData",description = testCaseDescUtils.AdminAPIWALLET_ACCOUNTMANAGEMENT)
    public void admin_account_wallet(Map<String,Object> testData) {
        walletaccountList(testData);
        frozen_detail_list(testData);
        queryAccountCurrencyList(testData);
        platformFundqueryAccountCurrencyList(testData);
        currency_account_info(testData);
        walletAccountPermissions(testData);
        walletAccounSetpermissions(testData);
        walletAccountFundFlowlist(testData);
        walletAccountFreeze(testData);
        walletAccountUnFreeze(testData);
        walletAccountDownload(testData);
        walletAccountFundFlowDownload(testData);
    }
    @Test(dataProvider = "testData",description = testCaseDescUtils.AdminAPIWALLET_ACCOUNTMANAGEMENT)
    public void admin_account_wallet_UAT(Map<String,Object> testData) {
        walletaccountList(testData);
        frozen_detail_list(testData);
        queryAccountCurrencyList(testData);
        platformFundqueryAccountCurrencyList(testData);
        currency_account_info(testData);
        walletAccountPermissions(testData);
        walletAccounSetpermissions(testData);
        walletAccountFundFlowlist(testData);
        walletAccountFreeze(testData);
        walletAccountUnFreeze(testData);
        walletAccountDownload(testData);
        walletAccountFundFlowDownload(testData);
    }

    @Test(dataProvider = "testData",description =testCaseDescUtils.AdminAPIWALLET_DEPOSIT)
    public void adminwalletDepositTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletDepositCurrentList(testData);
        walletOrderList(testData);
        walletOrderDetail(testData);
        walletDepositOrderListDownload(testData);

    }

    @Test(dataProvider = "testData",description = testCaseDescUtils.AdminAPIWALLET_WITHDRAWAL)
    public void adminWalletWithdrawalTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletWithdrawOrderList(testData);
        walletWithdrawCurrencyList(testData);
        walletWithdrawOrderDetail(testData);
        walletIngressWithdrawList(testData);
        walletIngressWithdrawDetail(testData);
        walletIngressWithdrawExport(testData);
        walletWithdrawOrderListDownload(testData);
    }

    @Test(dataProvider = "testData",description = testCaseDescUtils.AdminAPIWALLET_TRANSFER)
    public void adminWalletTransferTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletTransferOrderList(testData);
        walletTransferOrderDetail(testData);
        walletTransferOrderListDownload(testData);

    }

    @Test(dataProvider = "testData",description =testCaseDescUtils.AdminAPIWALLET_CONVERT)
    public void adminWalletConvertTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletConvertOrderList(testData);
        walletHedgeConvertOrderList(testData);
        walletHedgeConvertOrderListDownload(testData);
        walletConvertOrderListDownload(testData);

    }

    @Test(dataProvider = "testData",description = testCaseDescUtils.AdminAPIWALLET_BYBITPLATFROM )
    public void adminWalletPlatfromAccountTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletOrderList(testData);
        walletBybitOrderList(testData);
        walletGetPlatformAccount(testData);
        walletDepositWalletRewardOrderList(testData);
        walletDepositRewardOrderDetail(testData);


    }

    @Test(dataProvider = "testData",description =testCaseDescUtils.AdminAPIWALLET_BYBITPLATFROMTRANSACTION)
    public void adminWalletBybitPlatfromTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletBybitgetFundList(testData);
        wallettPlatFormAccountQueryAccountDetailList(testData);
        walletBybitTransactionRecords(testData);


    }

    @Test(dataProvider = "testData",description =testCaseDescUtils.AdminAPIWALLET_CHANNEL)
    public void adminWalletChannelTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletChannelOrderList(testData);
        walletDepositChannelOrderDetail(testData);
        walletDepositChainList(testData);


    }

    @Test(dataProvider = "testData",description =testCaseDescUtils.AdminAPIWALLET_DEPOSITASSETS)
    public void adminWalletDepositAssetsCollectionTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletChannelTransferRecordList(testData);
        walletDepositChannelTransferRecordDetail(testData);

    }

    @Test(dataProvider = "testData",description = testCaseDescUtils.AdminAPIWALLET_BYBITASSETSDASHBOARD)
    public void adminWalletBybitAssetDashboardTestcase(Map<String, Object> testData)
    {
        walletDepositPermissions(testData);
        walletPlatformAssetQueryToday(testData);
        walletPlatformAssetQueryYesterday(testData);
        walletiIngressWithdrawChainList(testData);

    }


}
