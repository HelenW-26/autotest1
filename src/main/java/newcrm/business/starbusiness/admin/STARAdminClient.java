package newcrm.business.starbusiness.admin;

import newcrm.business.adminbusiness.AdminClient;
import newcrm.pages.adminpages.AdminClientPage;
import org.openqa.selenium.WebDriver;


public class STARAdminClient extends AdminClient {


    public STARAdminClient(WebDriver driver) {
        super(new AdminClientPage(driver));
    }

    @Override
    public Boolean checkFirstClientIBUpgradeEnabled(){
        return adminClientPage.checkFirstClientIBUpgradeEnabled_OperationFlow();
    }

    @Override
    public void upgradeClientToIB(String email){
        adminClientPage.searchByEmail(email);
        adminClientPage.upgradeToIB_OperationFlow();
    }

}
