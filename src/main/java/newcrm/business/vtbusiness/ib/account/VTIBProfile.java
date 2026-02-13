package newcrm.business.vtbusiness.ib.account;

import newcrm.business.businessbase.ibbase.account.IBProfile;
import newcrm.pages.ibpages.IBProfilePage;
import newcrm.pages.vtibpages.VTIBDashBoardPage;
import newcrm.pages.vtibpages.VTIBProfilePage;
import org.openqa.selenium.WebDriver;


public class VTIBProfile extends IBProfile {


    public VTIBProfile(WebDriver driver) {
        super(new VTIBProfilePage(driver));
    }

    @Override
    public void verifyIBAgreementDate(String ibAcc){
        ibProfilePage.verifyIBAgreementDate(ibAcc);
    }


}
