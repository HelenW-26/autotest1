package newcrm.business.aubusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.global.GlobalProperties;
import newcrm.pages.auibpages.AuASICIBReferralLinksPage;
import newcrm.pages.auibpages.AuFCAIBReferralLinksPage;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import org.openqa.selenium.WebDriver;


public class AuFCAIBRebateAccount extends IBRebateAccount {

    public GlobalProperties.BRAND dbBrand;


    public AuFCAIBRebateAccount(WebDriver driver) {
        super(new AuFCAIBReferralLinksPage(driver));
        this.ibDashBoardPage = new DashBoardPage(driver);
        this.ibDownloadAppPage = new IBDownloadAppPage(driver);
    }

    @Override
    public void verifyReferralLinks(String affId, String testEnv){
        ibReferralLinksPage.verifyReferralLinks(affId, testEnv);
    }

}
