package newcrm.testcases.alpharegression;

import newcrm.adminapi.AdminAPICampaign;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.admintestcases.AdminAPICampaignTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class AlphaAPIAdminCampaignTestCases extends AdminAPICampaignTestCases {

    protected String brand;

    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server"})
    public void initEnv(String brand, String server, ITestContext context) {
        this.brand = GlobalMethods.setEnvValues(brand);
        Object[][] data = TestDataProvider.getAlphaRegUsersData(this.brand, server);
        adminApiCampaign = new AdminAPICampaign((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(this.brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
    }

    @Test(priority = 0, description = testCaseDescUtils.APAPICAMPAIGN_SEARCHALLPAGE,groups = {"API_Admin_Campaign"})
    public void testAPIAdminCampaignSearchAllPage() throws Exception {
        if (!brand.equalsIgnoreCase("um")) {  //UM has no Campaign DB Search
            apiQueryCampaignDBSearch(brand);
        }
        apiQueryCampaignUserData(brand);
        apiQueryCampaignTransData(brand);
        apiQueryCampaignBlacklist(brand);
        apiQueryCampaignWhitelist(brand);
        apiSearchCampaign("test");
    }


    @Test(priority = 0, description = testCaseDescUtils.APAPILOYALTYPROGRAM_SEARCH,groups = {"API_Admin_Campaign"})
    public void testAPIAdminLoyaltyProgramPage() throws Exception {
        if(brand.equalsIgnoreCase("au") || brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("vt") || brand.equalsIgnoreCase("vjp") || brand.equalsIgnoreCase("star")) {
            apiQueryLoyaltyProgramUserDataPage();
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

    @Test(priority = 0, description = testCaseDescUtils.APAPICAMPAIGNSETTING_ACTIONS,groups = {"API_Admin_Campaign"})
    public void testAPIAdminCampaignSetting() throws Exception {
            apiAdminCampaignSetting(brand);
        }
}
