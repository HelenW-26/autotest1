package newcrm.testcases.admintestcases;

import newcrm.adminapi.AdminAPICampaign;

public class AdminAPICampaignTestCases {

    protected AdminAPICampaign adminApiCampaign;

    //Task > Campaign Management > Campaign Database Search, 根据用户名称，搜索此用户的: 参加活动的状态,获得的赠金,黑名单状态
    public void apiQueryCampaignDBSearch(String brand) throws Exception {
        adminApiCampaign.apiSearchCampDBParticInfoByName();
        adminApiCampaign.apiSearchCampDBOperRecordByName();
        adminApiCampaign.apiSearchCampDBTransDataByName(brand);
    }

    public void apiQueryCampaignUserData(String brand) throws Exception {
        adminApiCampaign.apiSearchCampUserDataByCampId(brand); //search by Campaign Type 6 = Deposit Bonus
    }

    public void apiQueryCampaignTransData(String brand) throws Exception {
        adminApiCampaign.apiSearchCampTransDataByCampType(brand); //search by Campaign Type 6 = Deposit Bonus
    }

    public void apiQueryCampaignBlacklist(String brand) throws Exception {
        adminApiCampaign.apiQueryCampBlacklist(brand);
    }

    public void apiQueryCampaignWhitelist(String brand) throws Exception {
        adminApiCampaign.apiQueryCampWhitelist(brand);
    }

    public void apiSearchCampaign(String name) throws Exception {
        adminApiCampaign.apiSearchCampaignByName(name, false);
    }

    //Task > Loyalty Program Management，搜索所有忠诚计划的用户记录
    public void apiQueryLoyaltyProgramUserDataPage() throws Exception {
        adminApiCampaign.apiQueryLPUserData();
    }

    //Task > Campaign Management > Campaign Setting, Campaign的操作包括：创建/批准/编辑/查看/结束/归档/恢复归档
    public void apiAdminCampaignSetting(String brand) throws Exception {
        String campaignName = adminApiCampaign.apiCreateCampaign(brand); //Create campaign-deposit bonus
        adminApiCampaign.apiApproveCampaign(campaignName);
        adminApiCampaign.apiEditCampaign(campaignName);
        adminApiCampaign.apiViewCampaign(campaignName);
        adminApiCampaign.apiEndCampaign(campaignName);
        adminApiCampaign.apiArchiveCampaign(campaignName);
        adminApiCampaign.apiUnarchiveCampaign(campaignName);
    }

}
