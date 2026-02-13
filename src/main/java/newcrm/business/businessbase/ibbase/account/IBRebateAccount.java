package newcrm.business.businessbase.ibbase.account;

import com.alibaba.fastjson.JSONObject;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.CPRegisterGold;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.cpapi.APIThirdPartyService;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBCampaignLinksPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import newcrm.pages.ibpages.IBReferralLinksPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;


public class IBRebateAccount {

    public GlobalProperties.BRAND dbBrand;

    protected static WebDriver driver;
    protected DashBoardPage ibDashBoardPage;
    protected IBReferralLinksPage ibReferralLinksPage;
    protected IBDownloadAppPage ibDownloadAppPage;
    protected IBCampaignLinksPage ibCampaignLinksPage;


    public IBRebateAccount(WebDriver driver) {
        this.ibDashBoardPage = new DashBoardPage(driver);
        this.ibReferralLinksPage = new IBReferralLinksPage(driver);
        this.ibDownloadAppPage = new IBDownloadAppPage(driver);
        this.ibCampaignLinksPage = new IBCampaignLinksPage(driver);
    }

    public IBRebateAccount(DashBoardPage ibDashBoardPage) {
        this.ibDashBoardPage = ibDashBoardPage;
    }

    public IBRebateAccount(IBReferralLinksPage ibReferralLinksPage) {
        this.ibReferralLinksPage = ibReferralLinksPage;
    }

    public IBRebateAccount(IBDownloadAppPage ibDownloadAppPage) {
        this.ibDownloadAppPage = ibDownloadAppPage;
    }

    public IBRebateAccount(IBCampaignLinksPage ibCampaignLinksPage) {
        this.ibCampaignLinksPage = ibCampaignLinksPage;
    }

    public String retrieveIBAcc(){
        return ibDashBoardPage.getAllAccounts().get(0);
    }

    public String retrieveIBReferralLink() {
        return ibDashBoardPage.getIBReferralLink();
    }

    public String retrieveIBReferralLink_NewTab() {
        return ibDashBoardPage.getIBReferralLink_NewTab();
    }

    public String getCurrentlySelectedAccount(){
        return ibDashBoardPage.getCurrentlySelectedAccount();
    }

    public void selectIBAcc(String ibAcc){
        ibDashBoardPage.setAccount(ibAcc);
    }

    public List<String> retrieveAllRecentlyOpenedTradingAccounts(){
        return ibDashBoardPage.getAllRecentlyOpenedTradingAccounts();
    }

    public void selectRandomCampaignLanguage(){
        ibReferralLinksPage.selectRandomCampaignLanguage();
    }

    public void verifyReferralLinks(String affId, String testEnv){
        ibReferralLinksPage.verifyReferralLinks(affId, testEnv);
    }

    public void navigateToDownloadAppURL(){
        ibReferralLinksPage.navigateToDownloadAppURL();
    }

    public void navigateToDemoAccountURL(String affID){
        ibReferralLinksPage.navigateToDemoAccountURL(affID);
    }

    public String setCountryCode(){
        return ibDownloadAppPage.downloadAppSetCountryCode();
    }

    public String setPhoneNo(){
        return ibDownloadAppPage.downloadAppSetPhoneNo();
    }

    public boolean inputOTP(String otp){
        return ibDownloadAppPage.downloadAppInputOTP(otp);
    }

    public void createNewCampaignLink(){
        String campaignTitle = ibCampaignLinksPage.newCampaignLink();
        ibCampaignLinksPage.selectCampaignLanguage();
        ibCampaignLinksPage.saveCampaign();
        ibCampaignLinksPage.searchByCampaignTitle(campaignTitle);
        ibCampaignLinksPage.verifyCampaignTitle(campaignTitle);
    }

    public String verifyShortCampaignLink(){
        String campaignSourceID = ibCampaignLinksPage.retrieveCampaignSourceID();
//        ibCampaignLinksPage.verifyCampaignShortLink(campaignSourceID);

        return campaignSourceID;
    }

}
