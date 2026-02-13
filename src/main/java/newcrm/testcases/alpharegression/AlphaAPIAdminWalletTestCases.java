package newcrm.testcases.alpharegression;

import newcrm.adminapi.AdminAPI;
import newcrm.global.GlobalMethods;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.admintestcases.AdminAPIWalletTestcase;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class AlphaAPIAdminWalletTestCases  extends AdminAPIWalletTestcase {
    private AllureTestListener allureTestListener = new AllureTestListener();

    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server"})
    public void initEnv(String brand, String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        data = TestDataProvider.getAlphaRegUsersData(brand, server);
        testData.put("userName_login", data[0][7].toString());
        testData.put("password_login", data[0][8].toString());
        testData.put("url", data[0][4].toString());
        testData.put("brand", brand);
        testData.put("regulator", data[0][0].toString());
        testData.put("testEnv", "ALPHA");
    }
    @AfterMethod
    public void tearDown(ITestResult result) {
        // 在测试结束后上报错误信息
        allureTestListener.logTestFailure(result);
    }
    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_ACCOUNTMANAGEMENT,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletAccountManagement(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand) || "vt".equalsIgnoreCase( brand))
        {
            admin_account_wallet(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_DEPOSIT,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletDeposit(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminwalletDepositTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_WITHDRAWAL)
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletWithdrawal(String brand) {
        throw new SkipException("Skipping this test intentionally."); //Skip this due to dependency on previous withdrawal submission was skipped
//        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand))
//        {
//            adminWalletWithdrawalTestcase(testData);
//        }else {
//        }
    }


    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_TRANSFER,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletTransfer(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletTransferTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }


    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_CONVERT,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletConvert(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletConvertTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }


    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_BYBITPLATFROM,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletPlatfromAccount(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletPlatfromAccountTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }


    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_BYBITPLATFROMTRANSACTION,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletBybitPlatfrom(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletBybitPlatfromTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }


    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_CHANNEL,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletChannel(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletChannelTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }


    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_DEPOSITASSETS,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletDEPOSITASSETS(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletDepositAssetsCollectionTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 0, description = testCaseDescUtils.AdminAPIWALLET_BYBITASSETSDASHBOARD,groups = {"API_Admin_Wallet"})
    @Parameters(value= {"Brand"})
    public void testAdminApiWalletBybitAssetDashboard(String brand) {
        if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand)|| "vt".equalsIgnoreCase( brand))
        {
            adminWalletBybitAssetDashboardTestcase(testData);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }


}
