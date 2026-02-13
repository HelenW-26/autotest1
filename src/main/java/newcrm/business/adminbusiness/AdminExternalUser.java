package newcrm.business.adminbusiness;

import newcrm.pages.adminpages.AdminExternalUserPage;
import org.openqa.selenium.WebDriver;

public class AdminExternalUser {

    protected AdminExternalUserPage adminExternalUserPage;

    public AdminExternalUser(WebDriver driver) {
        this.adminExternalUserPage = new AdminExternalUserPage(driver);
    }

    public AdminExternalUser(AdminExternalUserPage adminExternalUserPage) {
        this.adminExternalUserPage = adminExternalUserPage;
    }

    public String addNewIBThroughAdminExternalUser(String adminName){
        String email = adminExternalUserPage.inputNewExternalUserDetails(adminName);
        adminExternalUserPage.selectIBType();
        adminExternalUserPage.clickConfirmButton();
        adminExternalUserPage.clickEnterSearchContent_OKButton();

        return email;
    }

    public void addAdditionalIBThroughAdminExternalUser(String email){
        adminExternalUserPage.searchExternalUserByEmail(email);
        adminExternalUserPage.addAdditionalIB();
    }

}
