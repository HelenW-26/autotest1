package newcrm.testcases.pu;

import lombok.extern.slf4j.Slf4j;
import newcrm.utils.api.YamlDataProviderUtils;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static newcrm.cpapi.CPAPILoginBase.loginWithEmail2;
import static newcrm.cpapi.CPAPILoginBase.logout;
import static newcrm.cpapi.CPAPIPaymentBase.*;

@Slf4j
public class cppaymenttestcases {

    public String filePath = "testData/puNetellerInterfData.yaml";
    public String filePathskill = "testData/skillInterfData.yaml";
    public String filePathUSDTTRC = "testData/USDTTRCAllCurrenciesInterfData.yaml";
    public String filePathUSDTBEP = "testData/USDTBEP20AllCurrenciesInterfData.yaml";
    public String filePathUSDTETH = "testData/USDTETHAllCurrenciesInterfData.yaml";
    public String filePathsticpay = "testData/stipaydepositInterfData.yaml";
    public String filePathAdvcash = "testData/advcashInterfData.yaml";
    public String filePathbridagerpay = "testData/bridgerpayBaseInterfData.yaml";
    public String filePathlocalbank = "testData/localBankBaseInterfData.yaml";
    public String brokerfilePath = "testData/brokerBaseInterfData.yaml";


    @DataProvider(name = "testDataLocalbank")
    public Object[][] getTestDataLocalbank(){
        return YamlDataProviderUtils.getTestData(filePathlocalbank);
    }
    @DataProvider(name = "brokertestData")
    public Object[][] getTestDatabroker(){
        return YamlDataProviderUtils.getTestData(brokerfilePath);
    }

    @DataProvider(name = "testDatabridgerpay")
    public Object[][] getTestDatabridger() {
        return YamlDataProviderUtils.getTestData(filePathbridagerpay);
    }

    @DataProvider(name = "testDatasticpay")
    public Object[][] getTestDatasticpay(){return YamlDataProviderUtils.getTestData(filePathsticpay); }

    @DataProvider(name = "testDataAdvcash")
    public Object[][] getTestDataAdvcash(){
        return YamlDataProviderUtils.getTestData(filePathAdvcash);
    }
    @DataProvider(name = "testDataETH")
    public Object[][] getTestDataETH()
    {
        return YamlDataProviderUtils.getTestData(filePathUSDTETH);
    }

    public String filePathUSDTBTC = "testData/USDTBTCAllCurrenciesInterfData.yaml";
    @DataProvider(name = "testDataBTC")
    public Object[][] getTestDataBTC()
    {
        return YamlDataProviderUtils.getTestData(filePathUSDTBTC);
    }

    public String filePathUSDC = "testData/USDCAllCurrenciesInterfData.yaml";
    @DataProvider(name = "testDataUSDC")
    public Object[][] getTestDataUSDC()
    {
        return YamlDataProviderUtils.getTestData(filePathUSDC);
    }

    public String filePathCryptoBTC = "testData/btcethBaseInterFData.yaml";

    @DataProvider(name = "testDataCryptoBTC")
    public Object[][] getTestDataCryptoBTC(){
        return YamlDataProviderUtils.getTestData(filePathCryptoBTC);
    }

    @DataProvider(name = "testDataBEP")
    public Object[][] getTestDataBEP()
    {
        return YamlDataProviderUtils.getTestData(filePathUSDTBEP);
    }

    @DataProvider(name = "testDataTRC")
    public Object[][] getTestDataTRC()
    {
        return YamlDataProviderUtils.getTestData(filePathUSDTTRC);
    }

    @DataProvider(name = "testData")
    public Object[][] getTestData(){
        return YamlDataProviderUtils.getTestData(filePath);
    }
    @DataProvider(name = "testDatafilePathskill")
    public Object[][] getTestDatafilePathskill(){
        return YamlDataProviderUtils.getTestData(filePathskill);
    }

    @Test(dataProvider = "testData")
    public static void Neteller_All_Currenciesdeposit(Map<String,Object> testData)
    {

        testData.put("email","autotestetdfyzri@testcrmautomation.com");
        loginWithEmail2(testData);
        //testData.get("mt4Account");
        init_deposit(testData);
        cpscheckoutcashier(testData);
        depositcps(testData);
        logout(testData);
    }

    public String filePathUSDTERC = "testData/USDTERCAllCurrenciesInterfData.yaml";
    @DataProvider(name = "testDataUSDTERC")
    public Object[][] getTestDataUSDTERC()
    {
        return YamlDataProviderUtils.getTestData(filePathUSDTERC);
    }

    @Test(dataProvider = "testDataUSDTERC")
    public static void Crypto_ALL_USDTERC_Deposit(Map<String,Object> testData)
    {
        //Reporter.log("全币种USDTERC入金");
        loginWithEmail2(testData);
        init_deposit(testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDataTRC")
    public static void Crypto_ALL_USDTTRC_Deposit(Map<String,Object> testData)
    {
        Reporter.log("全币种USDTTRC入金");
        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        Reporter.log("查询返回参数是：" +testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);

    }

    @Test(dataProvider = "testDataBEP")
    public static void Crypto_ALL_USDTBEP20_Deposit(Map<String,Object> testData)
    {
        Reporter.log("全币种USDTBEP20入金");
        testData.put("email",  testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDataETH")
    public static void Crypto_ALL_USDTETH_Deposit(Map<String,Object> testData)
    {
        Reporter.log("全币种USDTETH入金");
        testData.put("email",  testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDataBTC")
    public static void Crypto_ALL_USDTBTC_Deposit(Map<String,Object> testData)
    {
        Reporter.log("全币种USDTBTC入金");
        testData.put("email",  testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDataUSDC")
    public static void Crypto_ALL_USDC_Deposit(Map<String,Object> testData)
    {
        Reporter.log("全币种USDC入金");
        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDataCryptoBTC")
    public static void Crypto_BTCETH_Deposit(Map<String,Object> testData)
    {
        Reporter.log("全币种BTC&ETH& Acount帐户入金");
        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cryptocheckoutcashier(testData);
        checkoutcrypto(testData);
        cryptodepositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDatafilePathskill")
    public static void Skill_All_Currenciesdeposit(Map<String,Object> testData)
    {
        testData.put("email","autotestetdfyzri@testcrmautomation.com");
        loginWithEmail2(testData);
        //testData.get("mt4Account");
        init_deposit(testData);
        cpscheckoutcashier(testData);
        depositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDatasticpay")
    public static void SticPay_All_Currenciesdeposit(Map<String,Object> testData)
    {

        Reporter.log("全币种SticPay渠道入金");
        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cpscheckoutcashier(testData);
        depositcps(testData);
        logout(testData);
    }

    @Test(dataProvider = "testDataAdvcash")
    public static void Advcash_All_Currenciescdeposit(Map<String,Object> testData)
    {
        testData.put("email", "autotestetdfyzri@testcrmautomation.com");
        loginWithEmail2(testData);
        init_deposit(testData);
        cpscheckoutcashier(testData);
        depositcps(testData);
        logout(testData);

    }

    @Test(dataProvider = "testData")
    public static void BridgerPay_All_CurrenciesDeposit(Map<String, Object> testData)
    {
        Reporter.log("全币种BridgerPay渠道入金");
        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cpscheckoutcashier(testData);
        channel_forwarderdeposit(testData);
        logout(testData);
    }

    @Test (dataProvider = "testDataLocalbank")
    public void newzealandLocalbankdeposit(Map<String,Object> testData) {

        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cpscheckoutcashier(testData);
        newzealanddepositcps(testData);
        logout(testData);

    }

    @Test (dataProvider = "brokertestData")
    public void broker_to_broker_deposit(Map<String,Object> testData) {

        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        init_deposit(testData);
        cpscheckoutcashier(testData);
        depositIBT(testData);
        logout(testData);
    }



}
