package newcrm.business.vtbusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.vtclientpages.download.VTDownloadPage;
import org.openqa.selenium.WebDriver;

public class VTCPDownload extends CPDownload {

    VTDownloadPage downloadPage;

    public VTCPDownload(WebDriver driver) {
        super(new VTDownloadPage(driver));
        downloadPage = new VTDownloadPage(driver);
    }

    @Override
    public void mt4DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT4DownloadContent(platform);
        downloadPage.clickDownloadBtn(platform);
        downloadPage.waitLoadingDownloadDialog(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkWebTraderContent(platform, brand);
        downloadPage.closeDownloadDialog();
    }

    @Override
    public void mt5DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT5DownloadContent(platform);
        downloadPage.clickDownloadBtn(platform);
        downloadPage.waitLoadingDownloadDialog(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
        downloadPage.checkWebTraderContent(platform, brand);
        downloadPage.closeDownloadDialog();
    }

}
