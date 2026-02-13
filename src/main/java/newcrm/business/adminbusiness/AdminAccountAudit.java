package newcrm.business.adminbusiness;

import newcrm.global.GlobalProperties;
import newcrm.pages.adminpages.AdminAccountAuditPage;
import org.openqa.selenium.WebDriver;

public class AdminAccountAudit {

    protected AdminAccountAuditPage adminAccountAuditPage;

    public AdminAccountAudit(WebDriver driver) {
        this.adminAccountAuditPage = new AdminAccountAuditPage(driver);
    }

    public AdminAccountAudit(AdminAccountAuditPage adminAccountAuditPage) {
        this.adminAccountAuditPage = adminAccountAuditPage;
    }

    public void auditIBAccount(String email, GlobalProperties.BRAND brand){
        adminAccountAuditPage.searchByEmail(email);
        adminAccountAuditPage.auditIBAccount(brand);
    }

    public void waitForAccountAuditPageToLoad(){
        adminAccountAuditPage.waitForAccountAuditPageToLoad();
    }

    public void auditTradingAccount(String email, GlobalProperties.BRAND brand){
        adminAccountAuditPage.searchByEmail(email);
        adminAccountAuditPage.auditTradingAccount(brand);
    }
}
