package newcrm.pages.auclientpages.download;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.clientpages.download.DownloadPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class AUDownloadPage extends DownloadPage {

    public AUDownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void closeBanner() {
        AUMenuPage menuPage = new AUMenuPage(driver);

        menuPage.closeImgSec();
    }

    @Override
    protected void checkPlatformContent_withDownload(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String testId, String label, String iconSrc)
    {
        WebElement parentEle = getPlatformContentEle(testId, label, platform);

        // Get platform content icon
        getPlatformIcon(platform, label, iconSrc, parentEle);
        // Get platform content Text
        getPlatformText(platform, label, parentEle);
        // Get platform content url
        getPlatformUrl(platform, brand, fileSrc, label, parentEle);
        // Platform content url link redirection
        platformUrlRedirect(platform, brand, fileSrc, label, parentEle);

        LogUtils.info("***********************************************");
    }

    @Override
    protected void checkPlatformContent(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String testId, String label, String iconSrc)
    {
        WebElement parentEle = getPlatformContentEle(testId, label, platform);

        // Get platform content icon
        getPlatformIcon(platform, label, iconSrc, parentEle);
        // Get platform content Text
        getPlatformText(platform, label, parentEle);
        // Get platform content url
        getPlatformUrl(platform, brand, fileSrc, label, parentEle);
        // Platform content url link redirection
        platformUrlRedirect(platform, brand, fileSrc, label, parentEle);

        LogUtils.info("***********************************************");
    }

    @Override
    public void checkFileDownloadUrlRedirection(PLATFORM platform, BRAND brand, FILE_SRC fileSrc) {
        List<String> expectedVal =  downloadUrl;

        // Verify platform redirection url
        String currentUrl = getCurrentURL();
        LogUtils.info("Current URL after redirection: " + currentUrl);

        boolean isValMatch = expectedVal.stream().allMatch(d -> currentUrl.toLowerCase().contains(d.toLowerCase()));
        String desc = (fileSrc == FILE_SRC.WEBTRADER || fileSrc == FILE_SRC.PROTRADER) ? "trader login" : "file download";

        if (!isValMatch) {
            Assert.fail(String.format("Redirect to wrong %s URL. Expected URL: %s", desc, expectedVal.get(0)));
        }

        System.out.println("Platform file download redirect URL check passed");
    }

}
