package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPTransfer;
import newcrm.pages.vjpclientpages.VJPCPTransferPage;
import org.openqa.selenium.WebDriver;

public class VJPCPTransfer extends CPTransfer {
    protected VJPCPTransferPage tp;
    public VJPCPTransfer(WebDriver driver)
    {
        super(driver);
        this.tp = new VJPCPTransferPage(driver);
    }

    public void submit() {
        tp.submit();
    }
}
