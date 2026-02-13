package newcrm.testcases.crmapi;

import newcrm.cpapi.CPAPIAccount;
import newcrm.global.GlobalMethods;
import newcrm.utils.api.YamlDataProviderUtils;
import org.testng.annotations.*;

import java.util.Map;

public class CPAPIAccountTestcase {
    public String accountfilePath = "testData/AccountBaseInterfData.yaml";

    @DataProvider(name = "testData")
    public Object[][] getTestData() {
        return YamlDataProviderUtils.getTestData(accountfilePath);
    }

    @Test(dataProvider = "testData")
    public void accountsTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());

        api.id3Passed(testData);

        api.banner_enabled(testData);
        api.tokenAntiReuse(testData);
        api.tg_bot(testData);
        api.queryMetaTraderAccountDetail(testData);
        api.queryOpenAccountStatus(testData);
        api.getOpenAccountCondition(testData);
        api.getPerpetualSwitch(testData);
        api.getSupportCryptoCurrencies(testData);
        api.openAccountValidate(testData);
        api.getAccountType(testData);
        api.securityRule(testData);
        api.getMultiAuthMethod(testData);
        api.twoFactorStatus(testData);
        api.twoFactorEnable(testData);
        // TODO: below tests failed
        // api.information(testData);
        api.verificationGetData(testData);
        api.createDemoAccount(testData);
    }

    @Test(dataProvider = "testData")
    public void accountHomeTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());
        //api.identityProof(testData);
        api.homeQueryMetaTraderAccountOverview(testData);
        api.queryClientTotalEquity(testData);
        api.hintInfo(testData);
        api.getDirectClient(testData);
        api.cashRebateAvailableAccounts(testData);
        api.bannerDisplayPlatform(testData);
        api.getAllLastProof(testData);
        api.multiFactorAuth(testData);
        api.multiFactorAuthInfo(testData);
        // TODO: below tests failed
        // api.saveNickname(testData);
        api.getAccountType(testData);
        api.verificationGetData(testData);
        api.applyAdditionalAccount(testData);

    }

    @Test(dataProvider = "testData")
    public static void leverageTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());
        api.queryLeverageStatuses(testData);
        // api.queryAvailableLeverages(testData);
        // api.updateLeverage(testData);
        api.multiFactorAuthverifyStatus(testData);

    }


    @Test(dataProvider = "testData")
    public void userPasswordTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());
        api.keyPairPK(testData);
        api.updatePasswordStatus(testData);
        api.modifyPassword(testData);
        api.getClientSwitch(testData);
        api.sendEmailVerificationCode(testData);
    }

    @Test(dataProvider = "testData")
    public void ibAccountTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());
        api.updatePasswordStatus(testData);
        api.modifyPassword(testData);
        api.getClientSwitch(testData);

    }

    @Test(dataProvider = "testData")
    public void AccountTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());
        api.queryRebatesBlackList(testData);
        api.getUsername(testData);
        api.getInviteCodebyibcount(testData);
        api.ticker(testData);
        api.getcpurl(testData);
        //todo api.employmentFinance(testData);
        //todo api.questionTrading(testData);
        api.multiFactorAuthverifyNum(testData);
//        api.birthPlace(testData);
        //api.provinces(testData);
        api.questionuserAnswerCheck(testData);
        //api.getCurrentStep(testData);
        api.personalDetailverifyMethod(testData);
        //api.categorisationTest(testData);
        api.questionnaire(testData);
        api.sessionId(testData);
        api.jump(testData);
        //api.exchangeRate(testData);
        api.getInstruments(testData);
        //api.getMetaTraderAccounts(testData);

    }

    @Test(dataProvider = "testData")
    public static void profileVerificationTestcase(Map<String, Object> testData)  throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());
        api.encryptTextForOfficeWebsite(testData);
        api.getAddressProofAndBankInfor(testData);
        api.checkTransferIB(testData);
        api.historyIbTransfer(testData);
        api.getSecuritys(testData);
        api.setLanguage(testData);
        api.i18nUrl(testData);
        api.profileInfo(testData);
        //api.resetChangeIPWarn(testData);
        api.emailPreValidate(testData);
        api.accessToken(testData);

    }


    @Test(dataProvider = "testData")
    public static void acccountDemoTestcase(Map<String, Object> testData)  throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(), testData.get("password").toString());
        api.get_demo_accs(testData);
        //api.updateLeverageDemo(testData);
        api.updateBalanceDemo(testData);
    }

}
