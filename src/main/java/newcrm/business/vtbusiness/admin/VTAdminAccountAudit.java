package newcrm.business.vtbusiness.admin;

import newcrm.business.adminbusiness.AdminAccountAudit;
import newcrm.global.GlobalProperties;
import newcrm.pages.adminpages.AdminAccountAuditPage;
import newcrm.pages.adminpages.AdminExternalUserPage;
import newcrm.pages.vtadminpages.VTAdminAccountAuditPage;
import org.openqa.selenium.WebDriver;

public class VTAdminAccountAudit extends AdminAccountAudit {


    public VTAdminAccountAudit(WebDriver driver) {
        super(new VTAdminAccountAuditPage(driver));
    }

    @Override
    public void auditIBAccount(String email, GlobalProperties.BRAND brand){
        adminAccountAuditPage.waitForAccountAuditPageToLoad();
        adminAccountAuditPage.searchByEmail(email);
        adminAccountAuditPage.auditIBAccount(brand);
    }
}
