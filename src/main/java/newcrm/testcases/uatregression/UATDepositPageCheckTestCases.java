package newcrm.testcases.uatregression;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.DepositTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;
import utils.LogUtils;
import org.openqa.selenium.WebDriver;
import java.util.Map;
import static org.testng.Assert.assertNotNull;

public class UATDepositPageCheckTestCases extends DepositTestCases {

    private Object[][] adminData;
    private WebDriver driver; // 类内 driver 实例

    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        // 确保 driver 初始化并返回
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
        Object data[][] = UATTestDataProvider.getUATPageUsersData(brand, server);
        adminData = UATTestDataProvider.getUATPageUsersData(brand, server);

        assertNotNull(data, "测试数据不能为空");
        adminData = UATTestDataProvider.getUATPageUsersData(brand, server);
        adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8], GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));

        // 确保 driver 初始化并返回
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
        WebDriver tempDriver = (WebDriver) context.getAttribute("driver");

        if (tempDriver != null) {
            this.driver = tempDriver;
            context.setAttribute("driver", this.driver);
            LogUtils.info("initiEnv 中 driver 初始化成功");
        } else {
            LogUtils.error("initiEnv 中 driver 初始化失败", null);
        }
    }

    @Test(description = testCaseDescUtils.CPDEPOSIT_INPUT_INFO_CHECK)
    public void depositInputDisplayedInfoCheck() {
        try {
            testInputPageInfoCheck();
        } catch (Exception e) {
            LogUtils.error("depositInputDisplayedInfoCheck 执行失败：" + e.getMessage(), e);
            e.printStackTrace();
        }
    }


    // 验证入金页面信息
    @Test(description = testCaseDescUtils.CPDEPOSIT_ST)
    public void testVerificationDepositPageCheck() {
        // 校验 driver 非空
        if (this.driver == null) {
            LogUtils.error("driver 实例为空，无法执行测试方法", null);
            throw new RuntimeException("driver 实例未初始化");
        }

        AdminAPIPayment adminPaymentAPI =  new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String) adminData[0][0]), (String) adminData[0][7], (String) adminData[0][8], GlobalProperties.BRAND.valueOf(Brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));

        JSONObject exchangeRate =adminPaymentAPI.getExchangeRate();
        LogUtils.info("exchangeRate:"+exchangeRate);
        // 将 JSONObject 转换为 Map
        Map<String, Object> exchangeRateMap = exchangeRate.getInnerMap();;

        LogUtils.info("exchangeRateMap:"+exchangeRateMap);

        try {
            // 核心：传入有效 driver 实例
            if("mo".equalsIgnoreCase(Brand)) {
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_MalaysiaXPAYNew",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_ETH", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_BINANCE", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_I12BankTransferNew", true, null, this.driver);
            }
            else if("um".equalsIgnoreCase(Brand))
            {
                //传入汇率
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_ZOTAPAYPHPNew",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_USDC", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_CREDITORDEBIT", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null, this.driver);
            }
            else if("vjp".equalsIgnoreCase(Brand))
            {
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_JAPANBTTheJapanOne",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_DefaultUSDC", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_PaymentOption", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_CREDITORDEBIT", true, null, this.driver);
            }
            else if("star".equalsIgnoreCase(Brand))
            {
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_VietnamInstantNew",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_DefaultUSDC", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_CREDITORDEBIT", true, null, this.driver);
            }
            else if("vfx".equalsIgnoreCase(Brand) || "au".equalsIgnoreCase(Brand) )
            {
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_ZOTAPAYPHPNew",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_CryptoBIT", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_AppleGooglePay", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_ADVCASH", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null, this.driver);
            }
            else if("vt".equalsIgnoreCase(Brand))
            {
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_MalaysiaXPAYNew",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_USDCNew", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_MalaysiaEWallet", true, null, this.driver);
            } else if ("pug".equalsIgnoreCase(Brand)) {
                LogUtils.info("pug");
                MethodTracker.trackMethodExecution(this, "testVerificationDepositLBTPageCheck_VietnamInstantNew",
                        true, null, this.driver, exchangeRateMap);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositOfflineCheck_IBTEquals", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCryptoPageCheck_CryptoERCNew", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositCCPageCheck_CreditDebit", true, null, this.driver);
                MethodTracker.trackMethodExecution(this, "testVerificationDepositEWalletPageCheck_NETELLER", true, null, this.driver);
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
    public void verificationDepositLBTPageCheck() {
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
    public void verificationDepositCryptoPageCheck() {
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
    public void verificationDepositCreditCardPageCheck() {
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
    public void verificationDepositEWalletPageCheck() {
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

    public void verificationDepositOfflineCheck() {
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
}