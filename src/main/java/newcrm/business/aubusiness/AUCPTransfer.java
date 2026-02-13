package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.pages.auclientpages.AUCPTransferPage;
import org.openqa.selenium.WebDriver;

public class AUCPTransfer extends CPTransfer {

    public AUCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new AUCPTransferPage(driver);
    }

}
