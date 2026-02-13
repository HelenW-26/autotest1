package newcrm.business.vjpbusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBCampaignLinksPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import newcrm.pages.vjpibpages.account.VJPIBDashBoardPage;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class VJPIBRebateAccount extends IBRebateAccount {


    protected static WebDriver driver;


    public VJPIBRebateAccount(WebDriver driver) {
        super(new VJPIBDashBoardPage(driver));
        this.ibDownloadAppPage = new IBDownloadAppPage(driver);
        this.ibCampaignLinksPage = new IBCampaignLinksPage(driver);
    }

    @Override
    public List<String> retrieveAllRecentlyOpenedTradingAccounts(){
        return ibDashBoardPage.getAllRecentlyOpenedTradingAccounts();
    }

    @Override
    public void createNewCampaignLink(){
        String campaignTitle = ibCampaignLinksPage.newCampaignLink();

        ibCampaignLinksPage.saveCampaign();
        ibCampaignLinksPage.verifyCampaignTitle(campaignTitle);
    }

}
