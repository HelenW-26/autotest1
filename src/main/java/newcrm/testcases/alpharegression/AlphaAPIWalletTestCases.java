package newcrm.testcases.alpharegression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.CPAPIWalletBase;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.testng.annotations.Test;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.crmapi.CPAPIWalletTestCase;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;


public class AlphaAPIWalletTestCases extends CPAPIWalletTestCase {
    @BeforeTest(groups = {"API_Wallet"})
    @Parameters({"Brand", "Server", "BranchVersion"})
    public void initEnv(String brand, String server, String BranchVersion, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        if ("vfx".equalsIgnoreCase(brand) || "vt".equalsIgnoreCase( brand)) {
            Object[][] adminData = TestDataProvider.getAlphaRegUsersData(brand, server);
            AdminAPIPayment adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String) adminData[0][0]), (String) adminData[0][7], (String) adminData[0][8], GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
            adminPaymentAPI.apiDisableLoginWithdrawal2FA();
            api.setAPIHeader(BranchVersion);
        }
    }

    @Parameters(value = {"Brand"})
    @Test(priority = 0, description = testCaseDescUtils.CPAPIWALLET_OPENACCOUNT,groups = {"API_Wallet"})
    public void testApiWalletOpenAccountValidate(String brand) {
         if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand) || "vt".equalsIgnoreCase(brand)) {
            walletopenaccountvalidate();
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Parameters(value = {"Brand"})
    @Test(priority = 1, description = testCaseDescUtils.CPAPIWALLET_CONVERSION,groups = {"API_Wallet"})
    public void testApiWalletConverSubmit(String brand) {
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand) || "vt".equalsIgnoreCase(brand)) {
            wallet_conver_submit();
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Parameters(value = {"Brand"})
    @Test(priority = 2, description = testCaseDescUtils.CPAPIWALLET_DEPOSIT,groups = {"API_Wallet"})
    public void testApiWalletDeposit(String brand) {
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand) || "vt".equalsIgnoreCase(brand)) {
            wallet_deposit();
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Parameters(value = {"Brand"})
    @Test(priority = 3, description = testCaseDescUtils.CPAPIWALLET_WITHDRAW)
    public void testApiWalletWithdraw(String brand) {

        // Apollo > biz-switch > multi.factor.auth.switch - Require to set to true for golden flow OTP lvl 1. Hence, 2fa will affect wallet API withdraw.
        // Temporarily skip this API until 解耦2fa总开关的需求 completed
        throw new SkipException("Skipping this test intentionally.");

//        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
//            wallet_withdraw();
//        } else {
//            throw new SkipException("Skipping this test intentionally.");
//        }
    }

    @Parameters(value = {"Brand"})
    @Test(priority = 4, description = testCaseDescUtils.CPAPIWALLET_TRANSFER,groups = {"API_Wallet"})
    public void testApiWalletTransfer(String brand) {
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand) || "vt".equalsIgnoreCase(brand)) {
            wallet_transfer();
        } else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }
}
