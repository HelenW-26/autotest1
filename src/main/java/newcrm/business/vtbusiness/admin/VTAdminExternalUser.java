package newcrm.business.vtbusiness.admin;

import newcrm.business.adminbusiness.AdminExternalUser;
import newcrm.pages.adminpages.AdminExternalUserPage;
import org.openqa.selenium.WebDriver;

public class VTAdminExternalUser extends AdminExternalUser {

    public VTAdminExternalUser(WebDriver driver) {
        super(new AdminExternalUserPage(driver));
    }

    @Override
    public String addNewIBThroughAdminExternalUser(String adminName){
        String email = adminExternalUserPage.inputNewExternalUserDetails(adminName);

        adminExternalUserPage.clickConfirmButton();

        return email;
    }

}
