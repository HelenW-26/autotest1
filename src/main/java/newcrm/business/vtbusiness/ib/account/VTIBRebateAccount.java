package newcrm.business.vtbusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.pages.ibpages.IBCampaignLinksPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import newcrm.pages.vtibpages.VTIBDashBoardPage;
import org.openqa.selenium.WebDriver;



public class VTIBRebateAccount extends IBRebateAccount {


    protected static WebDriver driver;


    public VTIBRebateAccount(WebDriver driver) {
        super(new VTIBDashBoardPage(driver));
        this.ibDownloadAppPage = new IBDownloadAppPage(driver);
        this.ibCampaignLinksPage = new IBCampaignLinksPage(driver);
    }

    @Override
    public String retrieveIBReferralLink() {
        String ibAffliateID = ibDashBoardPage.getIBReferralLink();
        return ibAffliateID;
    }

    @Override
    public String getCurrentlySelectedAccount(){
        return ibDashBoardPage.getCurrentlySelectedAccount();
    }
}
