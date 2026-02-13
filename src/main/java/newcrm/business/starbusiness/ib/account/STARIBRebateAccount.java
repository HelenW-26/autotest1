package newcrm.business.starbusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBCampaignLinksPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import newcrm.pages.staribpages.STARIBDashBoardPage;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class STARIBRebateAccount extends IBRebateAccount {


    protected static WebDriver driver;



    public STARIBRebateAccount(WebDriver driver) {
        super(new STARIBDashBoardPage(driver));
        this.ibDownloadAppPage = new IBDownloadAppPage(driver);
        this.ibCampaignLinksPage = new IBCampaignLinksPage(driver);
    }

    @Override
    public List<String> retrieveAllRecentlyOpenedTradingAccounts(){
        return ibDashBoardPage.getAllRecentlyOpenedTradingAccounts();
    }

}
