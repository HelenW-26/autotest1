package newcrm.business.vjpbusiness.download;

import newcrm.business.businessbase.download.CPDownload;
import newcrm.pages.vjpclientpages.download.VJPDownloadPage;
import org.openqa.selenium.WebDriver;

public class VJPCPDownload extends CPDownload {

    public VJPCPDownload(WebDriver driver) {
        super(new VJPDownloadPage(driver));
    }

}
