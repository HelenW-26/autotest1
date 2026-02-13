package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.pages.moclientpages.MOCPTransferPage;
import org.openqa.selenium.WebDriver;

public class MOCPTransfer extends CPTransfer {

    public MOCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new MOCPTransferPage(driver);
    }

}
