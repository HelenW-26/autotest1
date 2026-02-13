package newcrm.pages.vtclientpages.download;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.clientpages.download.DownloadPage;
import newcrm.pages.vtclientpages.VTMenuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

public class VTDownloadPage extends DownloadPage {

    public VTDownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getDownloadContent(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.trader_Mt" + platform.getServerCategory()), "MetaTrader " + platform.getServerCategory() + " Download Content");
    }

    @Override
    protected WebElement getPlatformTextEle(String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector(":scope > span"), "MetaTrader " + platform.getServerCategory() + " " + label + " Display Text", parentEle);
    }

    @Override
    protected WebElement getPlatformIconEle(String src, String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector("svg > use"), "MetaTrader " + platform.getServerCategory() + " " + label + " Icon", parentEle);
    }

    protected WebElement getDownloadBtnEle(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.trader_Mt" + platform.getServerCategory() + " button"), "MetaTrader " + platform.getServerCategory() + " Download button");
    }

    protected WebElement getDownloadDialogEle(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.download_Dialog"), "MetaTrader " + platform.getServerCategory() + " Download Dialog");
    }

    public void clickDownloadBtn(PLATFORM platform) {
        WebElement e = getDownloadBtnEle(platform);
        triggerElementClickEvent_withoutMoveElement(e);
    }

    public void waitLoadingDownloadDialog(PLATFORM platform) {
        getDownloadDialogEle(platform);
    }

    @Override
    public void checkWebTraderContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.WEBTRADER, "web", "WebTrader", "web");
    }

    @Override
    public void checkAppStoreContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.APPSTORE, "iphone", "iPhone/iPad", "mac");
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

        if (!href.equalsIgnoreCase("#icon-" + iconSrc)) {
            Assert.fail("MetaTrader " + platform.getServerCategory() + " " + label + " Icon");
        }

        LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform display icon found");
    }

    @Override
    public void getPlatformUrl(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String label, WebElement parentEle) {
        String url = parentEle.getAttribute("href").trim();
        if (url.isEmpty()) Assert.fail("MT" + platform.getServerCategory() + " " + label + " platform download URL is empty");
        LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform download URL: " + url);
        checkFileDownloadUrl(platform, brand, fileSrc, url);
    }

    public void closeDownloadDialog() {
        WebElement popup = assertElementExists(By.xpath("//div[@class='el-dialog__wrapper' and not(contains(@style, 'display: none'))]//button[@class='el-dialog__headerbtn']"), "Download Dialog Close button");
        if (popup != null) {
            triggerClickEvent_withoutMoveElement(popup);
            LogUtils.info("Close Download Dialog");
        }
    }

    @Override
    protected void closeBanner() {
        VTMenuPage menuPage = new VTMenuPage(driver);

        menuPage.closeImgSec();
    }

}
