package newcrm.business.adminbusiness;

import newcrm.pages.adminpages.AdminClientPage;
import newcrm.pages.adminpages.IDPOAAuditPage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class AdminClient {

    protected AdminClientPage adminClientPage;
    protected IDPOAAuditPage idpoaAuditPage;


    public AdminClient(WebDriver driver) {
        this.adminClientPage = new AdminClientPage(driver);
        this.idpoaAuditPage = new IDPOAAuditPage(driver);
    }

    public AdminClient(AdminClientPage adminExternalUserPage) {
        this.adminClientPage = adminExternalUserPage;
    }

    public String searchFirstClientNameInList(List<String> ibAccountNumberList){
        return adminClientPage.searchFirstClientNameInList(ibAccountNumberList);
    }

    public void searchClientThroughEmail(String email){
        adminClientPage.searchByEmail(email);
    }

    public Boolean checkFirstClientIBUpgradeEnabled(){
        return adminClientPage.checkFirstClientIBUpgradeEnabled_NewIBFlow();
    }

    public Boolean checkFirstClientTradingAccountExist(){
        return adminClientPage.checkFirstClientTradingAccountExist();
    }

    public void clientUploadPoiPoaDocs(){
        adminClientPage.clickFirstClientName();
        adminClientPage.inputIdentificationInfo();
        adminClientPage.uploadPOIPOADoc();
    }

    public void upgradeClientToIB(String email){
        adminClientPage.searchByEmail(email);
        adminClientPage.upgradeToIB_NewIBFlow();
    }

    public void clickSearchBtn() {
        adminClientPage.clickSearchBtn();
    }

    public void setUserIdSearchCriteria(String userId) {
        adminClientPage.setUserIdSearchCriteria(userId);
    }

    public void waitLoadingPanelContent(String userId) {
        adminClientPage.waitLoadingPanelContent(userId);
    }

    public boolean checkNoDataContent() {
        return adminClientPage.checkNoDataContent();
    }

}
