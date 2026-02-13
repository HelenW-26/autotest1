package newcrm.pages.clientpages.download;

import lombok.Setter;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.Page;
import newcrm.utils.download.DownloadFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import utils.LogUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.time.Duration;
import java.net.URL;

public class DownloadPage extends Page {

    public DownloadPage(WebDriver driver) {
        super(driver);
    }

    @Setter
    private String domainWebSite = null;
    protected List<String> downloadUrl = null;

    protected static enum FILE_SRC {
        PC_WINDOWS,
        PC_MAC,
        APK,
        APPSTORE,
        PLAYSTORE,
        WEBTRADER,
        PROTRADER;
    }

    private static enum FILE_DOWNLOAD_NAME {
        VFX{
            @Override
            public String getFileDownloadName1() {
                return "20180830";
            }

            @Override
            public String getFileDownloadName2() {
                return "20210107";
            }
        };

        public abstract String getFileDownloadName1();
        public abstract String getFileDownloadName2();
    }

    protected WebElement getDownloadContent(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.mt" + platform.getServerCategory()), "MetaTrader " + platform.getServerCategory() + " Download Content");
    }

    protected WebElement getProTraderBtnEle() {
        return assertElementExists(By.cssSelector("div.download_operation a:has(> button[data-testid='button'])"), "PRO Trader platform Login button");
    }

    protected WebElement getPlatformContentEle(String testId, String label, PLATFORM platform) {
        return assertElementExists(By.cssSelector("[data-testid='" + testId + platform.getServerCategory() + "']"), "Download MetaTrader " + platform.getServerCategory() + " for " + label);
    }

    protected WebElement getPlatformIconEle(String src, String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector("div.picture." + src), "MetaTrader " + platform.getServerCategory() + " " + label + " Icon", parentEle);
    }

    protected WebElement getPlatformTextEle(String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector("a > p"), "MetaTrader " + platform.getServerCategory() + " " + label + " Text", parentEle);
    }

    protected WebElement getPlatformUrlEle(String label, PLATFORM platform, WebElement parentEle) {
        return assertElementExists(By.cssSelector("a[href]"), "MetaTrader " + platform.getServerCategory() + " " + label + " download URL", parentEle);
    }

    public void waitLoadingMT4DownloadContent(PLATFORM platform) {
        getDownloadContent(platform);
    }

    public void waitLoadingMT5DownloadContent(PLATFORM platform) {
        getDownloadContent(platform);
    }

    public void checkPcWindowsContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent_withDownload(platform, brand, FILE_SRC.PC_WINDOWS, "windows", "Windows", "windows");
    }

    public void checkPcMacContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent_withDownload(platform, brand, FILE_SRC.PC_MAC, "mac", "Mac OS", "mac");
    }

    public void checkAppStoreContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.APPSTORE, "iphone", "iPhone/iPad", "iphone");
    }

    public void checkGooglePlayStoreContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.PLAYSTORE, "android", "Android/Tablet", "android");
    }

    public void checkAPKContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent_withoutUrl(platform, brand, FILE_SRC.APK, "apk", "APK", "webtrader");
    }

    public void checkWebTraderContent(PLATFORM platform, BRAND brand) {
        checkPlatformContent(platform, brand, FILE_SRC.WEBTRADER, "web", "WebTrader", "webtrader");
    }

    public void checkProTraderContent(PLATFORM platform, BRAND brand) {
        WebElement e = getProTraderBtnEle();
        String url = e.getAttribute("href").trim();
        if (url.isEmpty()) Assert.fail("PRO Trader platform login url is empty");
        LogUtils.info("PRO Trader platform login url: " + url);
        checkFileDownloadUrl(platform, brand, FILE_SRC.PROTRADER, url);

        LogUtils.info("***********************************************");
    }

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
        closeBanner();

        LogUtils.info("***********************************************");
    }

    protected void checkPlatformContent_withDownload(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String testId, String label, String iconSrc)
    {
        WebElement parentEle = getPlatformContentEle(testId, label, platform);

        // Get platform content icon
        getPlatformIcon(platform, label, iconSrc, parentEle);
        // Get platform content Text
        getPlatformText(platform, label, parentEle);
        // Get platform content url
        getPlatformUrl(platform, brand, fileSrc, label, parentEle);
        // Download platform file
        platformFileDownload(platform, brand, fileSrc, label, parentEle);

        LogUtils.info("***********************************************");
    }

    protected void checkPlatformContent_withoutUrl(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String testId, String label, String iconSrc)
    {
        WebElement parentEle = getPlatformContentEle(testId, label, platform);

        // Get platform content icon
        getPlatformIcon(platform, label, iconSrc, parentEle);
        // Get platform content Text
        getPlatformText(platform, label, parentEle);
        System.out.println("MT" + platform.getServerCategory() + " " + label + " button does not contains download URL link");
        // Download platform file
        platformFileDownload(platform, brand, fileSrc, label, parentEle);

        LogUtils.info("***********************************************");
    }

    public void getPlatformIcon(PLATFORM platform, String label, String iconSrc, WebElement parentEle) {
        WebElement contentIcon = getPlatformIconEle(iconSrc, label, platform, parentEle);
        String imgPath = contentIcon.getCssValue("background-image");

        // Check for broken image
        // background-image usually looks like: url("data:image/png;base64,...") OR url("https://example.com/...")
        if (imgPath != null && imgPath.startsWith("url(")) {
            imgPath = imgPath.substring(4, imgPath.length() - 1); // remove 'url(' and ')'
            imgPath = imgPath.replace("\"", ""); // remove quotes
        }

        try {
            BufferedImage img = null;

            if (imgPath.startsWith("data:image")) {
                // Handle base64 image
                String base64Data = imgPath.substring(imgPath.indexOf("base64,") + 7);
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            } else {
                // Handle URL image
                URL imageUrl = new URL(imgPath);
                img = ImageIO.read(imageUrl);
            }

            if (img != null) {
                LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform display icon found");
            } else {
                Assert.fail("MT" + platform.getServerCategory() + " " + label + " platform display icon broken");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getPlatformText(PLATFORM platform, String label, WebElement parentEle) {
        WebElement contentText = getPlatformTextEle(label, platform, parentEle);
        if (contentText.getText().trim().isEmpty()) Assert.fail("MT" + platform.getServerCategory() + " " + label + " platform display text is empty");
        LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform display text: " + contentText.getText().trim());
    }

    public void getPlatformUrl(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String label, WebElement parentEle) {
        WebElement contentUrlEle = getPlatformUrlEle(label, platform, parentEle);
        String url = contentUrlEle.getAttribute("href").trim();
        if (url.isEmpty()) Assert.fail("MT" + platform.getServerCategory() + " " + label + " platform download URL is empty");
        LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform download URL: " + url);
        checkFileDownloadUrl(platform, brand, fileSrc, url);
    }

    public void platformFileDownload(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String label, WebElement parentEle) {
        // Perform click action to download file
        waitLoading();
        parentEle.click();
        waitLoading();
        LogUtils.info("Start to download MT" + platform.getServerCategory() + " " + label + " platform file...");

        String fileName = "", fileDownloadUrl = "";

        // When dev tools listener is enabled, get file info from listener, else get file info after downloaded file success
//        if (DevtoolsListener.hasDownloadFile()) {
//
//            // File download info captured from network
//            fileName = DevtoolsListener.fileDownloadName.get();
//            fileDownloadUrl = DevtoolsListener.fileDownloadUrl.get();
//
//            LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform file download URL: " + fileDownloadUrl);
//            LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform file downloaded: " + fileName);
//
//            // Delete file after download
//            DownloadFile.deleteDownloadedFile(fileName);
//            DevtoolsListener.resetDownloadInfo();
//            // Verify file name
//            checkFileDownloadName(platform, brand, fileSrc, fileName);
//
//        } else {

            // Wait until the download completes
            int timeoutSeconds = 120;
            Path downloadedFile = DownloadFile.waitForDownloadsToFinish(timeoutSeconds);

            if (downloadedFile != null) {
                fileName = downloadedFile.getFileName().toString();
                LogUtils.info("MT" + platform.getServerCategory() + " " + label + " platform file downloaded: " + fileName);
                // Delete file after download
                DownloadFile.deleteDownloadedFile(fileName);
                // Verify file name
                checkFileDownloadName(platform, brand, fileSrc, fileName);
            } else {
                Assert.fail("MT" + platform.getServerCategory() + " " + label +  " platform file download failed or timed out (" + timeoutSeconds + "s).");
            }

//        }
    }

    public void platformUrlRedirect(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String label, WebElement parentEle) {
        // Store the current window handle
        String originalTab = getCurrentTab();

        // Save current window handles (before clicking)
        Set<String> beforeClickHandles = getAllTab();

        // Perform click action to redirect to actual URL
        waitLoading();
        parentEle.click();
        waitLoading();
        LogUtils.info("Start to redirect to MT" + platform.getServerCategory() + " " + label + " platform download URL ...");

        boolean newTabOpened = false;
        try {
            // Wait for a new window/tab to appear
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> d.getWindowHandles().size() > beforeClickHandles.size());
            newTabOpened = true;
        } catch (TimeoutException e) {
            // No new tab detected within the timeout
        }

        if (newTabOpened) {
            System.out.println("A new tab has opened.");

            // Get new tab handle and switch
            Set<String> afterClickHandles = getAllTab();
            afterClickHandles.removeAll(beforeClickHandles);
            String newTabHandle = afterClickHandles.iterator().next();
            driver.switchTo().window(newTabHandle);
            System.out.println("Switched to new tab with title: " + driver.getTitle());

            // Verify redirection URL
            checkFileDownloadUrlRedirection(platform, brand, fileSrc);

            // Close new tab and return to original
            switchToOriginalTab(originalTab);

        } else {
            System.out.println("No new tab detected. Verifying redirection in same tab...");

            // Verify the redirection URL in the same tab
            checkFileDownloadUrlRedirection(platform, brand, fileSrc);

            // Navigate back to the previous page
            driver.navigate().back();
            waitLoading();
            LogUtils.info("Redirect back to previous page, Current URL: " + getCurrentURL());
        }
    }

    public void checkFileDownloadName(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String fileName) {
        String expectedFileName =  switch (fileSrc) {
            case PC_WINDOWS -> getWindowsSetupFileName(platform, brand);
            case PC_MAC -> getZipFileName(platform, brand);
            case APK -> getAPKFileName(platform, brand);
            default -> "";
        };

        if (!fileName.toLowerCase().trim().contains(expectedFileName.toLowerCase())) {
            Assert.fail("Incorrect file (" + fileName + ") downloaded. Expected file (" + expectedFileName + ")");
        }

        System.out.println("File name check passed");
    }

    public void checkFileDownloadUrl(PLATFORM platform, BRAND brand, FILE_SRC fileSrc, String url) {
        List<String> expectedVal =  switch (fileSrc) {
            case PC_WINDOWS -> getWindowsSetupLink(platform, brand);
            case PC_MAC -> getMacSetupLink(platform, brand);
            case APPSTORE -> getAppStoreLink(platform, brand);
            case PLAYSTORE -> getPlayStoreLink(platform, brand);
            case APK -> getAPKLink(platform, brand);
            case WEBTRADER -> getWebTraderLink(platform, brand);
            case PROTRADER -> getProTraderLink(platform, brand);
            default -> new ArrayList<>();
        };

        downloadUrl = expectedVal;

        // Verify platform link URL
        boolean isValMatch = expectedVal.stream().allMatch(d -> url.toLowerCase().trim().contains(d.toLowerCase()));

        if (!isValMatch) {
            String errMsg = (fileSrc == FILE_SRC.WEBTRADER || fileSrc == FILE_SRC.PROTRADER) ? "trader login" : "file download";
            Assert.fail("Incorrect platform " + errMsg + " URL (" + url + "). Expected URL (" + expectedVal.get(0) + ")");
        }

        System.out.println("Platform download URL check passed");
    }

    public void checkFileDownloadUrlRedirection(PLATFORM platform, BRAND brand, FILE_SRC fileSrc) {
        List<String> expectedVal =  switch (fileSrc) {
            case PC_MAC -> getMacStoreRedirectionLink(platform, brand);
            case APPSTORE -> getAppStoreRedirectionLink(platform, brand);
            case PLAYSTORE -> getPlayStoreRedirectionLink(platform, brand);
            case WEBTRADER -> getWebTraderLink(platform, brand);
            case PROTRADER -> getProTraderLink(platform, brand);
            default -> new ArrayList<>();
        };

        // Verify platform redirection url
        String currentUrl = getCurrentURL();
        LogUtils.info("Current URL after redirection: " + currentUrl);

        boolean isValMatch = expectedVal.stream().allMatch(d -> currentUrl.toLowerCase().contains(d.toLowerCase()));

        if (!isValMatch) {
            Assert.fail(
                    String.format("Redirect to wrong %s URL. %s",
                            (fileSrc == FILE_SRC.WEBTRADER || fileSrc == FILE_SRC.PROTRADER) ? "trader login" : "file download",
                            (fileSrc == FILE_SRC.PLAYSTORE ? String.format("Expected Brand: %s, URL: %s", expectedVal.get(1), expectedVal.get(0)) : String.format("Expected URL: %s", expectedVal.get(0)))
                    ));
        }

        System.out.println("Platform file download redirect URL check passed");
    }

    private String getWindowsSetupFileName(PLATFORM platform, BRAND brand) {
        String val =  switch (brand) {
            case VFX -> "vantageinternational";
            case PUG -> "puprime";
            case VT -> "vtmarkets";
            case UM -> "ultimamarkets";
            case VJP -> "vantagetradingltd";
            case STAR -> "startraderfinancial";
            case MO -> "monetamarkets";
            default -> "";
        };

        return val + platform.getServerCategory() + "setup.exe";
    }

    private String getZipFileName(PLATFORM platform, BRAND brand) {
        String val =  switch (brand) {
            case VFX, PUG, STAR, MO -> "MetaTrader";
            case UM -> "metatrader";
            default -> "";
        };

        return val + platform.getServerCategory() + ".pkg.zip";
    }

    private String getAPKFileName(PLATFORM platform, BRAND brand) {
        String val =  switch (brand) {
            case UM, STAR -> "metatrader";
            default -> "";
        };

        return val + platform.getServerCategory() + ".apk";
    }

    private List<String> getWindowsSetupLink(PLATFORM platform, BRAND brand) {
        String val =  switch (brand) {
            case PUG -> "pu.prime.ltd";
            case VT -> "vt.markets.pty";
            case UM -> "ultima.markets.ltd";
            case VJP -> "vantage.trading.ltd";
            case STAR -> "startrader.financial.markets";
            case MO -> (platform == PLATFORM.MT4 ? "17777" : "moneta.markets.pty");
            default -> "";
        };

        if (brand == BRAND.VFX) {
            return new ArrayList<>(List.of(getDownloadUrl(FILE_DOWNLOAD_NAME.VFX.getFileDownloadName1(), platform, "Windows")));
        }

        return new ArrayList<>(List.of("https://download.mql5.com/cdn/web/" + val + "/mt" + platform.getServerCategory() + "/" + getWindowsSetupFileName(platform, brand)));
    }

    private List<String> getMacSetupLink(PLATFORM platform, BRAND brand) {
        String val =  switch (brand) {
            case STAR, UM -> "https://download.mql5.com/cdn/web/metaquotes." + (platform == PLATFORM.MT4 ? "software.corp" : "ltd")  +"/mt" + platform.getServerCategory() + "/MetaTrader" + platform.getServerCategory() + ".pkg.zip";// ".pkg.zip?utm_source=support.metaquotes.net&utm_campaign=download.mt" + platform.getServerCategory() + ".macos";
            case VFX -> "https://download.mql5.com/cdn/web/metaquotes." + (platform == PLATFORM.MT4 ? "software.corp" : "ltd")  +"/mt" + platform.getServerCategory() + "/MetaTrader" + platform.getServerCategory() + ".pkg.zip";// + ".pkg.zip?utm_source=" + (platform == PLATFORM.MT4 ? "support.metaquotes.net" : "www.metatrader5.com") + "&utm_campaign=download.mt" + platform.getServerCategory() + ".macos";
            case PUG -> "https://download.mql5.com/cdn/web/metaquotes." + (platform == PLATFORM.MT4 ? "software.corp" : "ltd")  +"/mt" + platform.getServerCategory() + "/MetaTrader" + platform.getServerCategory() + (platform == PLATFORM.MT4 ? ".dmg" :  ".pkg.zip");// ".pkg.zip?utm_source=mt4terminal&utm_campaign=download.metatrader5");
            case MO -> "https://download.mql5.com/cdn/web/metaquotes.software.corp/mt" + platform.getServerCategory() + "/MetaTrader" + platform.getServerCategory() + ".dmg";
            default -> "";
        };

        if (brand == BRAND.VFX) {
            return new ArrayList<>(List.of(getDownloadUrl(FILE_DOWNLOAD_NAME.VFX.getFileDownloadName2(), platform, "MAC")));
        }

        if (brand == BRAND.VJP) {
            return getAppStoreLink(platform, brand);
        } else {
            return new ArrayList<>(List.of(val));
        }
    }

    private List<String> getMacStoreRedirectionLink(PLATFORM platform, BRAND brand) {
        return getAppStoreRedirectionLink(platform, brand);
    }

    private List<String> getAppStoreLink(PLATFORM platform, BRAND brand) {
        List<String> rtnValue =  new ArrayList<>();

        String val =  switch (brand) {
            case VFX -> "VantageInternational";
            case PUG -> "PUPrime";
            case VT -> "VTMarkets";
            case UM -> "UltimaMarkets";
            case VJP -> "vantagetradingltd";
            case STAR -> "STARTRADERFinancial";
            case MO -> "MonetaMarkets";
            default -> "";
        };

        if (brand == BRAND.UM && platform == PLATFORM.MT4) {
            rtnValue = getAppStoreRedirectionLink(platform, brand);
        } else if (brand == BRAND.VFX) {
            rtnValue.add(getDownloadUrl(FILE_DOWNLOAD_NAME.VFX.getFileDownloadName1(), platform, "iPhone"));
        } else {
            rtnValue.add("https://download.mql5.com/cdn/mobile/mt" + platform.getServerCategory() + "/ios?server=");
            rtnValue.add(val);
        }

        return rtnValue;
    }

    private List<String> getAppStoreRedirectionLink(PLATFORM platform, BRAND brand) {
        return new ArrayList<>(List.of("https://apps.apple.com/us/app/metatrader-" + platform.getServerCategory()));
    }

    private List<String> getPlayStoreLink(PLATFORM platform, BRAND brand) {
        List<String> rtnValue =  new ArrayList<>();

        String val =  switch (brand) {
            case VFX -> "VantageInternational";
            case PUG -> "PUPrime";
            case VT -> "VTMarkets";
            case UM -> "UltimaMarkets";
            case VJP -> "vantagetradingltd";
            case STAR -> "STARTRADERFinancial";
            case MO -> "MonetaMarkets";
            default -> "";
        };

        if (brand == BRAND.UM && platform == PLATFORM.MT4) {
            rtnValue = getPlayStoreRedirectionLink(platform, brand);
        } else if (brand == BRAND.VFX) {
            rtnValue.add(getDownloadUrl(FILE_DOWNLOAD_NAME.VFX.getFileDownloadName1(), platform, "Android"));
        } else {
            rtnValue.add("https://download.mql5.com/cdn/mobile/mt" + platform.getServerCategory() + "/android?server=");
            rtnValue.add(val);
        }

        return rtnValue;
    }

    private List<String> getPlayStoreRedirectionLink(PLATFORM platform, BRAND brand) {
        List<String> rtnValue =  new ArrayList<>();

        String val =  switch (brand) {
            case VFX -> "VantageInternational";
            case PUG -> "PUPrime";
            case VT -> "VTMarkets";
            case UM -> "UltimaMarkets";
            case VJP -> "vantagetradingltd";
            case STAR -> "STARTRADERFinancial";
            case MO -> "MonetaMarkets";
            default -> "";
        };

        rtnValue.add("https://play.google.com/store/apps/details?id=net.metaquotes.metatrader" + platform.getServerCategory());
        rtnValue.add(val);

        return rtnValue;
    }

    private List<String> getAPKLink(PLATFORM platform, BRAND brand) {
        return new ArrayList<>(List.of("https://download.mql5.com/cdn/web/metaquotes.software.corp/mt" + platform.getServerCategory() + "/metatrader" + platform.getServerCategory() + ".apk?utm_source=www.metatrader" + platform.getServerCategory() + ".com&utm_campaign=install.metaquotes"));
    }

    private List<String> getWebTraderLink(PLATFORM platform, BRAND brand) {
        String val =  switch (brand) {
            case VFX -> (platform == PLATFORM.MT4 ? "https://webtrader.vantagemarkets.com" : "https://www11.vtgmt5web.com/terminal");
            case PUG -> (platform == PLATFORM.MT4 ? "https://webtrader.puprime." : "https://metatraderweb.puprime.");
            case VT -> (platform == PLATFORM.MT4 ? "https://webtrader.vtmarkets.com" : "https://www3.vtmt5web.com");
            case UM -> "https://webtrader.ultimamarkets.com";
            case VJP -> "https://webtrader.vantagetradings.com";
            default -> "";
        };

        if (brand == BRAND.VFX) {
            return new ArrayList<>(List.of(getDownloadUrl(FILE_DOWNLOAD_NAME.VFX.getFileDownloadName1(), platform, "webtrader")));
        }

        return new ArrayList<>(List.of(val));
    }

    private List<String> getProTraderLink(PLATFORM platform, BRAND brand) {
        return new ArrayList<>(List.of("https://protrader.monetamarkets.com" ));
    }

    protected void closeBanner() {}

    private String getDownloadUrl(String fileDlName, PLATFORM platform, String downloadSrc) {
        String downloadUrl = "";

        try{
            if ("webtrader".equalsIgnoreCase(downloadSrc)) {
                // remove www
                String host = domainWebSite.contains("www.") ? domainWebSite.replaceFirst("^www\\.", "") : domainWebSite;
                String[] parts = host.split("\\.");
                downloadUrl = String.format("https://webtrader.%s", parts[0] + "." + parts[parts.length - 1]);
            } else {
                downloadUrl = String.format("https://%s/files/download-%s/MT%s_%s", domainWebSite, fileDlName, platform.getServerCategory(), downloadSrc);
            }

            LogUtils.info("Compare Url: " + downloadUrl);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return downloadUrl;
    }

}
