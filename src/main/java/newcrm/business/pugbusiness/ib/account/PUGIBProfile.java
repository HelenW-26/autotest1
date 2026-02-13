package newcrm.business.pugbusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBProfile;
import newcrm.pages.pugibpages.PUGIBProfilePage;
import org.openqa.selenium.WebDriver;

public class PUGIBProfile extends IBProfile {



    public PUGIBProfile(WebDriver driver) {
        super(new PUGIBProfilePage(driver));
    }

    @Override
    public void verifyIBAgreementDate(String ibAcc){
        ibProfilePage.verifyIBAgreementDate(ibAcc);
    }
}
