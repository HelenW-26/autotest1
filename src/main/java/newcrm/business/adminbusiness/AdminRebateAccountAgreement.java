package newcrm.business.adminbusiness;

import newcrm.pages.adminpages.AdminRebateAccountAgreementPage;
import newcrm.pages.adminpages.IDPOAAuditPage;
import org.openqa.selenium.WebDriver;


public class AdminRebateAccountAgreement {

    protected AdminRebateAccountAgreementPage adminRebateAccountAgreementPage;


    public AdminRebateAccountAgreement(WebDriver driver) {
        this.adminRebateAccountAgreementPage = new AdminRebateAccountAgreementPage(driver);
    }

    public AdminRebateAccountAgreement(AdminRebateAccountAgreementPage adminRebateAccountAgreementPage) {
        this.adminRebateAccountAgreementPage = adminRebateAccountAgreementPage;
    }

    public void updateRebateAccountAgreement(String ibAcc) throws InterruptedException {
        adminRebateAccountAgreementPage.updateRebateAccountAgreement(ibAcc);
    }

}
