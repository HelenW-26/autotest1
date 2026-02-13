package newcrm.pages.vjpclientpages.download;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.clientpages.download.DownloadPage;
import newcrm.pages.vjpclientpages.VJPMenuPage;
import org.openqa.selenium.WebDriver;

public class VJPDownloadPage extends DownloadPage {

    public VJPDownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void checkPcMacContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.PC_MAC, "mac", "Mac OS", "mac");
    }

    @Override
    protected void closeBanner() {
        VJPMenuPage menuPage = new VJPMenuPage(driver);

        menuPage.closeImgSec();
    }

}
