package newcrm.business.businessbase.ibbase.account;

import newcrm.pages.ibpages.*;
import org.openqa.selenium.WebDriver;


public class IBProfile {


    protected IBProfilePage ibProfilePage;

    public IBProfile(WebDriver driver) {
        this.ibProfilePage = new IBProfilePage(driver);

    }

    public IBProfile(IBProfilePage ibProfilePage) {
        this.ibProfilePage = ibProfilePage;
    }

    public void verifyIBAgreementDate(String ibAcc){
        ibProfilePage.verifyIBAgreementDate(ibAcc);
    }


}
