package newcrm.business.adminbusiness;

import newcrm.global.GlobalProperties;
import newcrm.pages.adminpages.AdminAdditionalAccountAuditPage;
import org.openqa.selenium.WebDriver;

public class AdminAdditionalAccountAudit {

    protected AdminAdditionalAccountAuditPage adminAdditionalAccountAuditPage;

    public AdminAdditionalAccountAudit(WebDriver driver) {
        this.adminAdditionalAccountAuditPage = new AdminAdditionalAccountAuditPage(driver);
    }

    public AdminAdditionalAccountAudit(AdminAdditionalAccountAuditPage adminAdditionalAccountAuditPage) {
        this.adminAdditionalAccountAuditPage = adminAdditionalAccountAuditPage;
    }

    public void auditAdditionalAccountIB(String email, GlobalProperties.BRAND brand){
        adminAdditionalAccountAuditPage.auditAdditionalAccountIB(email, brand);

    }
}
