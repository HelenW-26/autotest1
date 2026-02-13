package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPJapanBankWithdraw;
import newcrm.pages.vjpclientpages.VJPJapanBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VJPCPJapanBankWithdraw extends CPJapanBankWithdraw {
    public VJPCPJapanBankWithdraw(WebDriver driver)
    {
        super(driver);
        this.page = new VJPJapanBankWithdrawPage(driver);
    }
    public void clickContinue() {
        waitSpinnerLoading();
        page.clickContinue();
    }
    public boolean submit() {
        return page.submit();
    }
}
