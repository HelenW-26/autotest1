package newcrm.business.umbusiness.admin;

import newcrm.business.adminbusiness.AdminClient;
import newcrm.pages.adminpages.AdminClientPage;
import org.openqa.selenium.WebDriver;


public class UMAdminClient extends AdminClient {


    public UMAdminClient(WebDriver driver) {
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
