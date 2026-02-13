package newcrm.business.pugbusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.pages.pugclientpages.download.PUDownloadPage;
import org.openqa.selenium.WebDriver;

public class PUCPDownload extends CPDownload {

    public PUCPDownload(WebDriver driver) {
        super(new PUDownloadPage(driver));
    }

}
