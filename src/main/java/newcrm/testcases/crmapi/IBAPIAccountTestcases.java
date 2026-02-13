package newcrm.testcases.crmapi;

import newcrm.cpapi.CPAPIAccount;
import newcrm.global.GlobalMethods;
import newcrm.utils.api.YamlDataProviderUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class IBAPIAccountTestcases {
    public String accountfilePath = "testData/ibportalAccountBaseInterfData.yaml";

    @DataProvider(name = "testData")
    public Object[][] getTestData() {
        return YamlDataProviderUtils.getTestData(accountfilePath);
    }


    @Test(dataProvider = "testData")
    public void ibHomeTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(), testData.get("password").toString());

        api.sessionid(testData);
        api.toLoginbyToken(testData);
        api.ibRebateList(testData);
        api.ibQueryAvailableBalanc(testData);
        api.getNearestOpenAccount(testData);
        api.queryRebateVolumeList(testData);
        api.queryTotalCommissiont(testData);
        api.getNewOpenUserTotal(testData);
    }

    @Test(dataProvider = "testData")
    public void ibReportTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(), testData.get("password").toString());
        api.getClientSwitch(testData);

        api.rebateReport(testData);
        api.ibRebateList(testData);
    }

    @Test(dataProvider = "testData")
    public void accountReportTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(), testData.get("password").toString());

        api.retailclientsV2(testData);
        api.subIbStree(testData);
        api.zeroBalance(testData);
        api.neverFunded(testData);
        api.queryIbReportData(testData);
        api.getPendingAccount(testData);
        api.leads(testData);
    }


    @Test(dataProvider = "testData")
    public void MultiLevelIBTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(), testData.get("password").toString());

        api.subIbs(testData);
        api.subIbStree(testData);
        api.subIbClients(testData);
        api.getAgreementList(testData);

    }

    @Test(dataProvider = "testData")
    public void ibAccountsTestcase(Map<String, Object> testData) throws Exception {
        GlobalMethods.setEnvValues(testData.get("brand").toString());
        CPAPIAccount api = new CPAPIAccount(testData.get("url").toString(),
                testData.get("email").toString(),testData.get("password").toString());

        api.queryFoldLineTotalCommission(testData);
        api.queryPieTotalVolumeByGoods(testData);
        //api.ibNotification(testData);
        api.getInviteCodebyibcount(testData);
        api.rebateAgreement(testData);
        api.getFirstAccountAuditStatus(testData);
        api.information(testData);
        api.rebateHistory(testData);
        api.transferHistory(testData);
        api.applyCommission(testData);
        api.reportQueryByDate(testData);
        api.transferIBAffiliatenew(testData);

    }
}
