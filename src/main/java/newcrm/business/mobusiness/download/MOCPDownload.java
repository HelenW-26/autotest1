package newcrm.business.mobusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.moclientpages.download.MODownloadPage;
import org.openqa.selenium.WebDriver;

public class MOCPDownload extends CPDownload {

    public MOCPDownload(WebDriver driver) {
        super(new MODownloadPage(driver));
    }

    @Override
    public void mt4DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT4DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
    }

    @Override
    public void mt5DownloadInfoCheck(PLATFORM platform, BRAND brand) {
        downloadPage.waitLoadingMT5DownloadContent(platform);
        downloadPage.checkPcWindowsContent(platform, brand);
        downloadPage.checkPcMacContent(platform, brand);
        downloadPage.checkAppStoreContent(platform, brand);
        downloadPage.checkGooglePlayStoreContent(platform, brand);
    }

    @Override
    public void proTraderDownloadInfoCheck(PLATFORM platform, BRAND brand) {
        System.out.println("***PRO Trader Download Info Check***");
        downloadPage.checkProTraderContent(platform, brand);
    }

}
