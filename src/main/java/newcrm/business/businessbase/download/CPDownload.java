package newcrm.business.businessbase.download;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.clientpages.download.DownloadPage;

import org.openqa.selenium.WebDriver;

public class CPDownload {

    protected DownloadPage downloadPage;

    public CPDownload(WebDriver driver) {
        this.downloadPage = new DownloadPage(driver);
    }

    public CPDownload(DownloadPage downloadPage) {
        this.downloadPage = downloadPage;
    }

    public void mt4DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT4DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkWebTraderContent(platform, brand);
    }

    public void mt5DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT5DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkWebTraderContent(platform, brand);
    }

    public void proTraderDownloadInfoCheck(PLATFORM platform, BRAND brand) {}

    public void setDomainWebSite(String domainWebSite) {
        downloadPage.setDomainWebSite(domainWebSite);
    }

}
