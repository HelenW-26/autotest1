package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.pages.starclientpages.STARCPTransferPage;
import org.openqa.selenium.WebDriver;

public class STARCPTransfer extends CPTransfer {

    public STARCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new STARCPTransferPage(driver);
    }

}
