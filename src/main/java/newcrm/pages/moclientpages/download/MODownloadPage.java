package newcrm.pages.moclientpages.download;

import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.moclientpages.MOMenuPage;
import newcrm.pages.clientpages.download.DownloadPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MODownloadPage extends DownloadPage {

    public MODownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getDownloadContent(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.download_box:has(div.title_container p.title)"), "MetaTrader " + platform.getServerCategory() + " Download Content", e -> e.getText().trim().toLowerCase().contains(platform.name().toLowerCase()));
    }

    @Override
    protected void closeBanner() {
        MOMenuPage menuPage = new MOMenuPage(driver);

        menuPage.closeBannerDialog();
    }

}
