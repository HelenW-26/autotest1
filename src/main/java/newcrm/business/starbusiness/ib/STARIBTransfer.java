package newcrm.business.starbusiness.ib;

import newcrm.business.businessbase.ibbase.IBTransfer;
import newcrm.pages.staribpages.STARIBRebateTransferPage;

import org.openqa.selenium.WebDriver;

public class STARIBTransfer extends IBTransfer {

    public STARIBTransfer(WebDriver driver) {
        super(driver, new STARIBRebateTransferPage(driver));
    }

}
