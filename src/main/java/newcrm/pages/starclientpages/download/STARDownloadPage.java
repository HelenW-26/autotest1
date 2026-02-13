package newcrm.pages.starclientpages.download;

import newcrm.pages.clientpages.download.DownloadPage;

import newcrm.pages.starclientpages.STARMenuPage;
import org.openqa.selenium.WebDriver;

public class STARDownloadPage extends DownloadPage {

    public STARDownloadPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void closeBanner() {
        STARMenuPage menuPage = new STARMenuPage(driver);

        menuPage.closeBannerDialog();
    }

}
