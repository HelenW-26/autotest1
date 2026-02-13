package newcrm.business.aubusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.pages.auclientpages.download.AUDownloadPage;
import org.openqa.selenium.WebDriver;

public class AUCPDownload extends CPDownload {

    public AUCPDownload(WebDriver driver) {
        super(new AUDownloadPage(driver));
    }

}
