package newcrm.business.starbusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.pages.starclientpages.download.STARDownloadPage;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import org.openqa.selenium.WebDriver;

public class STARCPDownload extends CPDownload {

    public STARCPDownload(WebDriver driver) {
        super(new STARDownloadPage(driver));
    }

    @Override
    public void mt4DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT4DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkAPKContent(platform, brand);
    }

    @Override
    public void mt5DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT5DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkAPKContent(platform, brand);
    }

}
