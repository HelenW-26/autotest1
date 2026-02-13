package newcrm.business.adminbusiness;

import newcrm.pages.adminpages.AdminRebateAccountPage;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class AdminRebateAccount {

    protected AdminRebateAccountPage adminRebateAccountPage;


    public AdminRebateAccount(WebDriver driver) {
        this.adminRebateAccountPage = new AdminRebateAccountPage(driver);
    }

    public AdminRebateAccount(AdminRebateAccountPage adminRebateAccountPage) {
        this.adminRebateAccountPage = adminRebateAccountPage;
    }

    public List<String> retrieveIBAccs(String email) throws InterruptedException {
        adminRebateAccountPage.searchByEmail(email);
        return adminRebateAccountPage.rebateAccountList();
    }

    public void configureIBCommissionRules(String email) throws InterruptedException {
        adminRebateAccountPage.searchByEmail(email);
        adminRebateAccountPage.configureIBCommissionRules();
    }

}
