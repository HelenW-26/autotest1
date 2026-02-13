package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.deposit.CPJapanBankDeposit;
import newcrm.pages.vjpclientpages.deposit.VJPJapanBankTransferPage;
import org.openqa.selenium.WebDriver;

public class VJPCPJapanBankDeposit extends CPJapanBankDeposit {
    public VJPCPJapanBankDeposit(WebDriver driver)
    {
        super(driver);
        this.jpbtpage = new VJPJapanBankTransferPage(driver);
    }

}
