package newcrm.pages.pugclientpages.download;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.pugclientpages.PUMenuPage;
import newcrm.pages.clientpages.download.DownloadPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

public class PUDownloadPage extends DownloadPage {

    public PUDownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getDownloadContent(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.content_card"), "MetaTrader " + platform.getServerCategory() + " Download Content", e -> e.getText().trim().toLowerCase().contains(("MetaTrader " + platform.getServerCategory()).toLowerCase()));
    }

    @Override
    protected WebElement getPlatformContentEle(String testId, String label, PLATFORM platform) {
        return assertElementExists(By.cssSelector("li:has(a > span.item_name)"), "Download MetaTrader " + platform.getServerCategory() + " for " + label, e -> e.getText().trim().equalsIgnoreCase(label), getDownloadContent(platform));
    }

    @Override
    protected WebElement getPlatformTextEle(String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector("a > span.item_name"), "MetaTrader " + platform.getServerCategory() + " " + label + " Text", parentEle);
    }

    @Override
    protected WebElement getPlatformIconEle(String src, String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector("svg > use"), "MetaTrader " + platform.getServerCategory() + " " + label + " Icon", parentEle);
    }

    @Override
    public void getPlatformIcon(PLATFORM platform, String label, String iconSrc, WebElement parentEle) {
        WebElement contentIcon = getPlatformIconEle(iconSrc, label, platform, parentEle);

        // Try reading modern 'href' first
        String href = contentIcon.getDomAttribute("href");
        // Fallback to legacy 'xlink:href' if needed
        if (href == null || href.isEmpty()) {
            href = contentIcon.getDomAttribute("xlink:href");
        }

        if (!href.equalsIgnoreCase("#ht-icon-downloads-" + iconSrc)) {
            Assert.fail("MetaTrader " + platform.getServerCategory() + " " + label + " Icon");
        }

        LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform display icon found");
    }

    @Override
    public void checkAppStoreContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.APPSTORE, "iphone", "iPhone/iPad", "apple");
    }

    @Override
    protected void closeBanner() {
        PUMenuPage menuPage = new PUMenuPage(driver);

        menuPage.closeBannerDialog();
    }

}
