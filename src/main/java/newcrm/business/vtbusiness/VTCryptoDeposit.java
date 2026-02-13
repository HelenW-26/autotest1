package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.pages.vtclientpages.VTCryptoDepositPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class VTCryptoDeposit extends CPCryptoDeposit {
    protected VTCryptoDepositPage vtCryptoPage;

    public VTCryptoDeposit(WebDriver driver)
    {

        super(new VTCryptoDepositPage(driver));
        this.vtCryptoPage = new VTCryptoDepositPage(driver);
    }
    @Override
    public void goBack()
    {
        LogUtils.info("Go back to home page");
        vtCryptoPage.goBackNew();
    }
}
