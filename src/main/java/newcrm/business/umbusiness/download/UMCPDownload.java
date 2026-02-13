package newcrm.business.umbusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.umclientpages.download.UMDownloadPage;
import org.openqa.selenium.WebDriver;

public class UMCPDownload extends CPDownload {

    public UMCPDownload(WebDriver driver) {
        super(new UMDownloadPage(driver));
    }

    @Override
    public void mt4DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT4DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkAPKContent(platform, brand);
        downloadPage.checkWebTraderContent(platform, brand);
    }

    @Override
    public void mt5DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT5DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkAPKContent(platform, brand);
        downloadPage.checkWebTraderContent(platform, brand);
    }

}
