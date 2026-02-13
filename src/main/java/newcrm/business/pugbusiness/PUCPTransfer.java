package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.pages.pugclientpages.PUCPTransferPage;
import org.openqa.selenium.WebDriver;

public class PUCPTransfer extends CPTransfer {

    public PUCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new PUCPTransferPage(driver);
    }

}
