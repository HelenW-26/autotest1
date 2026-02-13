package newcrm.business.adminbusiness;

import newcrm.pages.adminpages.IDPOAAuditPage;
import org.openqa.selenium.WebDriver;


public class AdminIDPOAAudit {

    protected IDPOAAuditPage idpoaAuditPage;


    public AdminIDPOAAudit(WebDriver driver) {
        this.idpoaAuditPage = new IDPOAAuditPage(driver);
    }

    public AdminIDPOAAudit(IDPOAAuditPage idpoaAuditPage) {
        this.idpoaAuditPage = idpoaAuditPage;
    }

    public void clientPoiPoaAudit(String email){
        idpoaAuditPage.poipoaAuditNew(email);
    }

    public void clientPoiAudit(String email){
        idpoaAuditPage.poiAudit(email);
    }

}
