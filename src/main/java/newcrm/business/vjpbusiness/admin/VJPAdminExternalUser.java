package newcrm.business.vjpbusiness.admin;

import newcrm.business.adminbusiness.AdminExternalUser;
import newcrm.pages.adminpages.AdminExternalUserPage;
import org.openqa.selenium.WebDriver;

public class VJPAdminExternalUser extends AdminExternalUser {

    public VJPAdminExternalUser(WebDriver driver) {
        super(new AdminExternalUserPage(driver));
    }

    @Override
    public String addNewIBThroughAdminExternalUser(String adminName){
        String email = adminExternalUserPage.inputNewExternalUserDetails(adminName);

        adminExternalUserPage.clickConfirmButton();

        return email;
    }

}
