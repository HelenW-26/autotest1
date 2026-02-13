package newcrm.pages.umclientpages.download;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.download.DownloadPage;
import newcrm.pages.umclientpages.UMMenuPage;
import org.openqa.selenium.WebDriver;

public class UMDownloadPage extends DownloadPage {

    public UMDownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void checkAppStoreContent(GlobalProperties.PLATFORM platform, GlobalProperties.BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.APPSTORE, "iphone", "App Store", "iphone");
    }

    @Override
    public void checkGooglePlayStoreContent(GlobalProperties.PLATFORM platform, GlobalProperties.BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.PLAYSTORE, "play", "Google Play Store", "play");
    }

    @Override
    public void checkAPKContent(GlobalProperties.PLATFORM platform, GlobalProperties.BRAND brand) {
        checkPlatformContent_withDownload(platform, brand, FILE_SRC.APK, "android", "Android APK", "android");
    }

    @Override
    protected void closeBanner() {
        UMMenuPage menuPage = new UMMenuPage(driver);

        menuPage.closeBanner();
    }

}
