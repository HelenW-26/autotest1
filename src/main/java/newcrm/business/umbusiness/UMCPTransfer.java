package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.pages.umclientpages.UMCPTransferPage;
import org.openqa.selenium.WebDriver;

public class UMCPTransfer extends CPTransfer {

    public UMCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new UMCPTransferPage(driver);
    }

}
