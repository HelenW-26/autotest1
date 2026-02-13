package newcrm.business.mobusiness.ib;

import newcrm.business.businessbase.ibbase.IBInternationalBankTransfer;
import newcrm.pages.ibpages.RebateWithdrawBasePage;
import org.openqa.selenium.WebDriver;

public class MOIBInternationalBankTransfer extends IBInternationalBankTransfer {

    protected RebateWithdrawBasePage basepage;

    public MOIBInternationalBankTransfer(WebDriver driver) {
        super(driver);
        basepage = new RebateWithdrawBasePage(driver);
    }

    @Override
    public boolean submit()
    {
        return basepage.submit();
    }

    @Override
    public void checkFileUploadingCompleted() {
        basepage.checkFileUploadingCompleted();
    }
}
