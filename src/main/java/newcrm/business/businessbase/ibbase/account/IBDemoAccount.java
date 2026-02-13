package newcrm.business.businessbase.ibbase.account;

import newcrm.global.GlobalProperties;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBDemoAccountPage;
import newcrm.pages.ibpages.IBDownloadAppPage;
import newcrm.pages.ibpages.IBReferralLinksPage;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class IBDemoAccount {

    public GlobalProperties.BRAND dbBrand;

    protected static WebDriver driver;
    protected IBDemoAccountPage ibDemoAccountPage;


    public IBDemoAccount(WebDriver driver) {
        this.ibDemoAccountPage = new IBDemoAccountPage(driver);
    }

    public IBDemoAccount(IBDemoAccountPage ibDemoAccountPage) {
        this.ibDemoAccountPage = ibDemoAccountPage;
    }

    public String registerIBDemoAccount(String traderURL, String country) throws InterruptedException {
        return ibDemoAccountPage.registerIBDemoAccount(traderURL, country);
    }




}
