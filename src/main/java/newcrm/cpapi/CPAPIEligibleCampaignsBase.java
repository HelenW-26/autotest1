package newcrm.cpapi;

import cn.hutool.http.HttpRequest;
import org.testng.annotations.Test;

import java.util.Map;

public class CPAPIEligibleCampaignsBase {

    @Test(dataProvider = "testData")
    public static void eligible_campaign(Map<String, Object> testData) {

        String response = HttpRequest.get(testData.get("url") + "/api/campaign/eligible-campaigns")
                .header("Content-Type","application/json;charset=UTF-8")
                .header("token",testData.get("token").toString())
                .execute().body();
        System.out.println(response);
    }



    //cp/api/loyalty/overview
    @Test(dataProvider = "testData")
    public static void loyalty(Map<String, Object> testData) {

        String response = HttpRequest.get(testData.get("url") + "/cp/api/loyalty/overview")
                .header("Content-Type","application/json;charset=UTF-8")
                .header("token",testData.get("token").toString())
                .execute().body();
        System.out.println(response);
    }



}
