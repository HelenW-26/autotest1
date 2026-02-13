package newcrm.adminapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.api.HyTechUrl;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;

import java.util.HashMap;

public class AdminAPICampaign extends AdminAPI {
    /**
     * @param url
     * @param regulator
     * @param adminUser
     * @param password
     * @param Brand
     * @param testEnv
     * @throws Exception
     */

    HashMap<String, String> header = new HashMap<>();
    JSONObject campBody = new JSONObject();


    public AdminAPICampaign(String url, GlobalProperties.REGULATOR regulator, String adminUser, String password, GlobalProperties.BRAND Brand, GlobalProperties.ENV testEnv) {
        super(url, regulator, adminUser, password, Brand, testEnv);
    }


    //Campaign Management页面 > Campaign Database Search > User Participation Info, 根据用户名称,搜索用户参与的活动名称和参加状态
    public void apiSearchCampDBParticInfoByName() throws Exception {
        String fullPath = this.url + HyTechUrl.CAMPAIGNDB_PARTICIPATIONINFO +"?direction=desc&isTest=false&pageNumber=0&pageSize=10&searchType=1&sortBy=createTime&userQuery=testbit";

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        JSONObject firstData = (JSONObject) result.getJSONObject("data").getJSONArray("list").get(0);
        printAPIInfo(brand, regulator,fullPath, "", String.valueOf(firstData));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("User participation info") && resSuccess.equals("true"),"API CampaignDB-Get User participation info failed!!");
    }

    //Campaign Management页面 > Campaign Database Search > User Transaction Data, 根据用户名称和活动名称,搜索此用户入金记录与获取的赠金额
    public void apiSearchCampDBOperRecordByName() throws Exception {
        String fullPath = this.url + HyTechUrl.CAMPAIGNDB_OPERATIONRECORD +"?direction=desc&isTest=false&pageNumber=0&pageSize=10&searchType=1&sortBy=operationTime&userQuery=testbit";

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));

        JSONArray jarr = result.getJSONObject("data").getJSONArray("list");
        if(!jarr.isEmpty()) {
            JSONObject firstData = (JSONObject) result.getJSONObject("data").getJSONArray("list").get(0);
            printAPIInfo(brand, regulator, fullPath, "", String.valueOf(firstData));
        }
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("User operation record") && resSuccess.equals("true"),"API CampaignDB-User operation record failed!!");
    }

    //Campaign Management页面 > Campaign Database Search > User Operation Record, 根据用户名称,搜索此用户被加入黑名单的记录 (Deposit Bonus)
    public void apiSearchCampDBTransDataByName(String brand) throws Exception {
        String[] data = getBrandCampTypeAndId(brand);
        String fullPath = this.url + HyTechUrl.CAMPAIGNDB_DEPOBONUS_TRANSACTION +"?campaignType=" + data[0] +"&direction=desc&isTest=false&pageNumber=0&pageSize=10&searchType=1&sortBy=createTime&userQuery=testbit";

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(GlobalProperties.BRAND.valueOf(brand.toUpperCase()), regulator,fullPath, "", String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Transaction data") && resSuccess.equals("true"),"API CampaignDB-Blacklist/Transaction data failed!!");
    }

    //Campaign Management页面 > Campaign User Data, 根据Campaign类型和名称,搜索用户的参与状态和获得赠金的所有记录 (Deposit Bonus)
    public void apiSearchCampUserDataByCampId(String brand) throws Exception {
        String[] data = getBrandCampTypeAndId(brand);
        String fullPath = this.url + HyTechUrl.CAMPAIGN_USERDATA +"?campaignId="+data[1]+"&direction=desc&pageNumber=0&pageSize=10&sortBy=userId";

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(GlobalProperties.BRAND.valueOf(brand.toUpperCase()), regulator,fullPath, "", String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("User summaries") && resSuccess.equals("true"),"API Campaign User data search result failed!!");
    }

    //Campaign Management页面 > Campaign Transaction Data, 根据Campaign类型和名称,搜索用户的入金和获得赠金的记录 (Deposit Bonus)
    public void apiSearchCampTransDataByCampType(String brand) throws Exception {
        String data[] = getBrandCampTypeAndId(brand);
        String fullPath = this.url + HyTechUrl.CAMPAIGN_TRANSACTIONDATA +"?campaignId=" + data[1] + "&direction=desc&endDate=2025-05-16&pageNumber=0&pageSize=10&sortBy=createTime&startDate=2020-03-04";

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(GlobalProperties.BRAND.valueOf(brand.toUpperCase()), regulator,fullPath, "", String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Transaction data") && resSuccess.equals("true"),"API Campaign Transaction data search result failed!!");
    }


    //Campaign Management页面 > Blacklist, 根据Campaign类型和名称,搜索被添加黑名单的用户记录
    public void apiQueryCampBlacklist(String brand) throws Exception {
        String[] data = getBrandCampTypeAndId(brand);
        String fullPath = this.url + HyTechUrl.CAMPAIGN_BLACKLIST +"?campaignId=" + data[1];

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(GlobalProperties.BRAND.valueOf(brand.toUpperCase()), regulator,fullPath, "", String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Black list") && resSuccess.equals("true"),"API Campaign Blacklist search result failed!!");
    }

    //Campaign Management页面 > Whitelist, 根据Campaign类型和名称,搜索被添加白名单的用户记录
    public void apiQueryCampWhitelist(String brand) throws Exception {
        String[] data = getBrandCampTypeAndId(brand);
        String fullPath = this.url + HyTechUrl.CAMPAIGN_WHITELIST +"?campaignId=" + data[1];

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(GlobalProperties.BRAND.valueOf(brand.toUpperCase()), regulator,fullPath, "", String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("White list") && resSuccess.equals("true"),"API Campaign Whitelist search result failed!!");
    }

    //Campaign Management页面 > Campaign Setting, 根据Campaign类型和名称,搜索活动创建记录和状态
    public JSONObject apiSearchCampaignByName(String name, boolean needResponse) throws Exception {
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_SEARCH_CAMPAIGN +"?campaignType=0&direction=desc&isTest=false&pageNumber=0&pageSize=10&searchType=1&sortBy=updateDate&userQuery=" + name;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Campaign Listing retrieved") && resSuccess.equals("true"),"API Campaign Setting search result failed!!");

        // 如果需要 response，就返回
        if (needResponse) {
            return result;
        } else {
            return null;
        }
    }

    public HashMap<String,String> apiGetActualCampaignInfo(String campaignName) throws Exception {
        JSONObject getCampaignSearchResult = apiSearchCampaignByName(campaignName, true);
        JSONArray listArr = getCampaignSearchResult.getJSONObject("data").getJSONArray("list");
        HashMap<String, String> campInfoMap = new HashMap<>();
        if(listArr.size() != 0) {
            for (int i = 0; i < listArr.size(); i++) {
                JSONObject camp = listArr.getJSONObject(i);
                String campId = camp.getString("actualCampaignId");
                String campTemplate = camp.getString("campaignTemplate");
                if (campId != null && campTemplate.equals("CAMPAIGN")) {
                    campInfoMap.put("Actual Campaign ID",campId);
                    campInfoMap.put("Campaign Name",camp.getString("campaignName"));
                    campInfoMap.put("Campaign Type",camp.getString("campaignType"));
                    campInfoMap.put("Template Campaign ID",camp.getString("campaignId"));
                    campInfoMap.put("Campaign Status",camp.getString("taskStatus"));
                    return campInfoMap;
                }
            }
        }
        return null;
    }

    public void apiQueryLPUserData() throws Exception {
        String fullPath = this.url + HyTechUrl.LOYALTYPROGRAM_USERDATA +"?direction=desc&endDate=2025-05-18&pageNumber=0&pageSize=10&searchType=1&sortBy=userId&startDate=2025-04-19&userQuery=testbit";

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, "", String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("User data") && resSuccess.equals("true"),"API Loyalty Program-User Data search result failed!!");
    }

    //Create Campaign - Deposit Bonus
    public String apiCreateCampaign(String brand) throws Exception {
        String[] data = getBrandCampTypeAndId(brand);
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_CREATE_CAMPAIGN;
        header.put("Content-Type","application/json");
        String campaignName = "autotest_" + GlobalMethods.getRandomString(5);

        campBody.put("campaignName", campaignName);
        campBody.put("campaignType", data[0]);

        JSONArray platform = new JSONArray();
        platform.add("WEB");
        campBody.put("platform", platform);

        JSONArray clientType = new JSONArray();
        clientType.add("RETAIL");
        campBody.put("clientType", clientType);

        JSONArray accountType = new JSONArray();
        accountType.add(1);
        campBody.put("accountType", accountType);

        JSONArray currency = new JSONArray();
        currency.add("USD");
        campBody.put("currency", currency);
        campBody.put("startDate", GlobalMethods.getCurrentDate() + " 00:00:00");
        campBody.put("endDate", GlobalMethods.getCurrentDate() + " 23:00:00");
        campBody.put("autoOptIn", 0);
        campBody.put("ftdBonus", false);
        campBody.put("depositRate", 10);
        campBody.put("mt4Remark", campaignName + "_remarks");
        campBody.put("restrictedDepositMethod", new JSONObject());
        JSONArray supportedCountry = new JSONArray();
        supportedCountry.add("6163");
        campBody.put("supportedCountry", supportedCountry);
        campBody.put("migrateCampaignIds", new JSONArray());
        campBody.put("campaignId", null);
        campBody.put("action", null);
        campBody.put("migrateUser", false);

        // clientPortalSettings: array of one object
        JSONObject clientPortalSetting = new JSONObject();
        clientPortalSetting.put("languageCode", "en_US");
        clientPortalSetting.put("title", campaignName + "_title");
        clientPortalSetting.put("pageTitle", campaignName + "_pageTitle");
        clientPortalSetting.put("description1", "&lt;p&gt;desc1&lt;/p&gt;");
        clientPortalSetting.put("description2", "&lt;p&gt;desc2&lt;/p&gt;");
        clientPortalSetting.put("document", "https://crm-au-alpha.s3.ap-southeast-1.amazonaws.com/other/a965ca947a3a416f853b3c7763062be4.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250527T064743Z&X-Amz-SignedHeaders=host&X-Amz-Expires=599&X-Amz-Credential=AKIA6LZROUZKAQU5T4EI%2F20250527%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=182b9893b96ab787f4f63dc764fdb3226e9e01406a580511c97ccb35c3f1fb0e");
        clientPortalSetting.put("tncLink", "https://www.test.com");
        clientPortalSetting.put("isDel", "false");

        JSONArray clientPortalSettings = new JSONArray();
        clientPortalSettings.add(clientPortalSetting);
        campBody.put("clientPortalSettings", clientPortalSettings);
        campBody.put("appSettings", new JSONArray());
        campBody.put("useCpContent", false);
        campBody.put("isFeatured", false);
        campBody.put("isTest", false);
        campBody.put("targetAudienceType", 0);
        campBody.put("eligibleRegistrationPeriodStart", null);
        campBody.put("eligibleRegistrationPeriodEnd", null);
        campBody.put("newExclusiveParticipate", 1);

        HttpResponse response = httpClient.getPostResponse(fullPath, header, campBody);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(GlobalProperties.BRAND.valueOf(brand.toUpperCase()), regulator,fullPath, campBody, String.valueOf(result));
        if(result== null){
            throw new RuntimeException(fullPath+" response is null\n"+ response);
        }
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Campaign Setting created") && resSuccess.equals("true"),"API Campaign Setting-Add New Campaign failed!!");
        GlobalMethods.printDebugInfo("Created Campaign successful. Campaign Name: " + campaignName + '\n');
        return campaignName;
    }

    public void apiApproveCampaign(String campaignName) throws Exception {
        JSONObject getCampaignSearchResult = apiSearchCampaignByName(campaignName, true);
        JSONArray listArr = getCampaignSearchResult.getJSONObject("data").getJSONArray("list");
        String campaignId = null, campaignType = null;
        for (int i = 0; i < listArr.size(); i++) {
            JSONObject camp = listArr.getJSONObject(i);
            String campTemplate = camp.getString("campaignTemplate");
            if (campTemplate.equals("TEMPLATE")) {
                campaignId = camp.getString("campaignId");
                campaignType = camp.getString("campaignType");
            }
        }
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_APPROVE_CAMPAIGN;
        header.put("Content-Type","application/json");
        JSONObject body = new JSONObject();
        body.put("campaignId", campaignId);
        body.put("campaignType", campaignType);
        body.put("processedNotes", null);

        GlobalMethods.printDebugInfo("Campaign Name: " + campaignName + ", CampaignID: " + campaignId + ", Campaign Type: " + campaignType + "\n");
        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, body, String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Campaign Setting approved") && resSuccess.equals("true"),"API Campaign Setting-Approve Campaign failed!!");
    }

    public void apiEndCampaign(String campName) throws Exception {
        HashMap<String,String> campInfo = apiGetActualCampaignInfo(campName);
        String campID = campInfo.get("Actual Campaign ID");
        String campaignType = campInfo.get("Campaign Type");
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_END_CAMPAIGN + "?campaignId=" + campID + "&campaignType=" + campaignType;

        header.put("Content-Type","application/json");
        JSONObject body = new JSONObject();
        body.put("campaignId", campID);
        body.put("campaignType", campaignType);

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, body, String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Campaign Setting end successfully") && resSuccess.equals("true"),"API Campaign Setting-End Campaign failed!!");

        //Check campaign status whether updated to "Inactive"
        HashMap<String,String> getLatestCampInfo = apiGetActualCampaignInfo(campName);
        Assert.assertEquals(getLatestCampInfo.get("Campaign Status"),"I","Campaign Status is not updated to ‘Inactive’");
    }

    public void apiViewCampaign(String campName) throws Exception {
        HashMap<String,String> campInfo = apiGetActualCampaignInfo(campName);
        System.out.println("campInfo: "+ campInfo);
        String campID = campInfo.get("Actual Campaign ID");
        String campaignType = campInfo.get("Campaign Type");
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_VIEW_CAMPAIGN + "?campaignId=" + campID + "&campaignTemplateType=CAMPAIGN&campaignType=" + campaignType;

        HttpResponse response = httpClient.getGetResponse(fullPath, header,"");
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, "", String.valueOf(result));
        String resCampName = result.getJSONObject("data").getString("campaignName");
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resCampName.equals(campName) && resMsg.equals("View Campaign") && resSuccess.equals("true"),"API Campaign Setting-View Campaign failed!!");
    }

    public void apiEditCampaign(String campName) throws Exception {
        HashMap<String, String> campInfo = apiGetActualCampaignInfo(campName);
        int campID = Integer.parseInt(campInfo.get("Actual Campaign ID"));
        String campaignType = campInfo.get("Campaign Type");
        String campaignName = campInfo.get("Campaign Name");
        campBody.put("campaignId", campID);
        campBody.put("campaignName", campaignName);
        campBody.put("campaignType", campaignType);
        campBody.put("processedNotes", null);
        campBody.put("campaignTemplate", "CAMPAIGN");
        campBody.put("endDate", GlobalMethods.getCurrentDate()  + " 23:30:00");
        campBody.put("eligibleRegistrationPeriodStart", null);
        campBody.put("eligibleRegistrationPeriodEnd", null);

        JSONObject clientPortalSetting = new JSONObject();
        clientPortalSetting.put("languageCode", "en_US");
        clientPortalSetting.put("title", campaignName + "_title");
        clientPortalSetting.put("pageTitle", campaignName + "_pageTitle");
        clientPortalSetting.put("description1", "<p>desc1</p>");
        clientPortalSetting.put("description2", "<p>desc2</p>");
        clientPortalSetting.put("document", "/file/repo/S3/4da178424765c765da9c65d47e6f142e2ef8fa1e0dd0361b0c1941ea8eed8d3f");
        clientPortalSetting.put("tncLink", "https://www.test.com");
        clientPortalSetting.put("isDel", false);
        JSONArray clientPortalSettings = new JSONArray();
        clientPortalSettings.add(clientPortalSetting);
        campBody.put("clientPortalSettings", clientPortalSettings);

        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_EDIT_CAMPAIGN;
        header.put("Content-Type","application/json");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, campBody);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, campBody, String.valueOf(result));
//        String resMsg = result.getString("message");
//        String resSuccess = result.getString("success");
//        Assert.assertTrue(resMsg.equals("Campaign Setting edited") && resSuccess.equals("true"),"API Campaign Setting-Edited Campaign failed!!");
    }

    public void apiArchiveCampaign(String campName) throws Exception {
        HashMap<String,String> campInfo = apiGetActualCampaignInfo(campName);
        String campID = campInfo.get("Actual Campaign ID");
        String campaignType = campInfo.get("Campaign Type");
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_ARCHIVE_CAMPAIGN + "?campaignId=" + campID + "&campaignType=" + campaignType;

        header.put("Content-Type","application/json");
        JSONObject body = new JSONObject();
        body.put("campaignId", campID);
        body.put("campaignType", campaignType);

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, body, String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Campaign Archived") && resSuccess.equals("true"),"API Campaign Setting-Archived Campaign failed!!");

        //Check campaign status whether updated to "Archived"
        HashMap<String,String> getLatestCampInfo = apiGetActualCampaignInfo(campName);
        Assert.assertEquals(getLatestCampInfo.get("Campaign Status"),"AR","Campaign Status is not updated to ‘Archived’");
    }

    public void apiUnarchiveCampaign(String campName) throws Exception {
        HashMap<String,String> campInfo = apiGetActualCampaignInfo(campName);
        String campID = campInfo.get("Actual Campaign ID");
        String campaignType = campInfo.get("Campaign Type");
        String fullPath = this.url + HyTechUrl.CAMPAIGNSETTING_UNARCHIVE_CAMPAIGN + "?campaignId=" + campID + "&campaignType=" + campaignType;

        header.put("Content-Type","application/json");
        JSONObject body = new JSONObject();
        body.put("campaignId", campID);
        body.put("campaignType", campaignType);

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPIInfo(brand, regulator,fullPath, body, String.valueOf(result));
        String resMsg = result.getString("message");
        String resSuccess = result.getString("success");
        Assert.assertTrue(resMsg.equals("Campaign Unarchived") && resSuccess.equals("true"),"API Campaign Setting-Archived Campaign failed!!");

        //Check campaign status whether updated to "Inactive"
        HashMap<String,String> getLatestCampInfo = apiGetActualCampaignInfo(campName);
        Assert.assertEquals(getLatestCampInfo.get("Campaign Status"),"I","Campaign Status is not updated to ‘Inactive’");
    }

    /* Configure data[] = Campaign Type,Campaing Id */
    private String[] getBrandCampTypeAndId(String brand) {
        return switch (brand.toUpperCase()) {
            case "VFX", "AU" -> new String[]{"6", "17"};
            case "VT" -> new String[]{"5", "6"};
            case "PUG" -> new String[]{"6", "32"};
            case "STAR" -> new String[]{"6", "5004"};
            case "MO" -> new String[]{"6", "100046"};
            case "UM" -> new String[]{"6", "2"};
            case "VJP" -> new String[]{"6", "5012"};
            default -> throw new IllegalArgumentException("Unknown brand: " + brand);
        };
    }
}