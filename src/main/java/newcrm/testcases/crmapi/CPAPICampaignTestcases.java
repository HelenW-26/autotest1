package newcrm.testcases.crmapi;

import newcrm.utils.api.YamlDataProviderUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static newcrm.cpapi.CPAPIAccount.*;
import static newcrm.cpapi.CPAPILoginBase.loginWithEmail2;
import static newcrm.cpapi.CPAPIEligibleCampaignsBase.*;

public class CPAPICampaignTestcases {

    public String campaignfilePath = "testData/AccountBaseInterfData.yaml";

    @DataProvider(name = "testData")
    public Object[][] getTestData() {
        return YamlDataProviderUtils.getTestData(campaignfilePath);
    }

    @Test(dataProvider = "testData")
    public static void campaign_testcase(Map<String, Object> testData) {

        testData.put("email", testData.get("email"));
        loginWithEmail2(testData);
        eligible_campaign(testData);
        loyalty(testData);
        //tg_bot(testData);
    }
}
