package newcrm.business.aubusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBRebateAccount;
import newcrm.global.GlobalProperties;
import newcrm.pages.auibpages.AUIBAccountReportPage;
import newcrm.pages.auibpages.AuASICIBReferralLinksPage;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import newcrm.pages.ibpages.IBReferralLinksPage;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class AuASICIBRebateAccount extends IBRebateAccount {

    public GlobalProperties.BRAND dbBrand;


    public AuASICIBRebateAccount(WebDriver driver) {
        super(new AuASICIBReferralLinksPage(driver));
        this.ibDashBoardPage = new DashBoardPage(driver);
//        this.ibReferralLinksPage = new IBReferralLinksPage(driver);
        this.ibDownloadAppPage = new IBDownloadAppPage(driver);
    }

    @Override
    public void verifyReferralLinks(String affId, String testEnv){
        ibReferralLinksPage.verifyReferralLinks(affId, testEnv);
    }

}
