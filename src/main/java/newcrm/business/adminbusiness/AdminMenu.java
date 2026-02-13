package newcrm.business.adminbusiness;

import newcrm.global.GlobalProperties.AdminMenuName;
import newcrm.pages.adminpages.AdminMenuPage;
import org.openqa.selenium.WebDriver;

public class AdminMenu {

    protected AdminMenuPage menu;

    public AdminMenu(AdminMenuPage menu)
    {
        this.menu=menu;
    }

    public AdminMenu(WebDriver driver)
    {
        this.menu = new AdminMenuPage(driver);
    }

    public void goToMenu(AdminMenuName menuName) {

        switch (menuName) {
            case CLIENT:
                menu.clickClient();
                break;
            case EXTERNAL_USER:
                menu.clickExternalUser();
                break;
            case ACCOUNT_AUDIT:
                menu.clickAccountAudit();
                break;
            case ADDITIONAL_ACCOUNT_AUDIT:
                menu.clickAdditionalAccountAudit();
                break;
            case POI_POA_AUDIT:
                menu.clickPOIPOAAudit();
                break;
            case REBATE_ACCOUNT:
                menu.clickRebateAccount();
                break;
            case REBATE_ACCOUNT_AGREEMENT:
                menu.clickRebateAccountAgreement();
                break;
            case EMAIL_MGMT:
                menu.clickEmailMgmt();
                break;
            case TWO_FACTOR_LIMIT_SETTING:
                menu.click2FALimitSetting();
                break;
            case PT_MGMT:
                menu.clickPTMgmt();
                break;
        }

    }

    public void refresh() {
        menu.refresh();
    }

    public void waitLoading() {
        menu.waitLoading();
    }

    public void waitLoadingPageContent() {
        menu.waitLoadingPageContent();
    }

    public void waitLoadingPageContentNew() {
        menu.waitLoadingPageContentNew();
    }

    public void clickSearchBtn() {
        menu.clickSearchBtn();
    }

    public void clickSearchBtnNew() {
        menu.clickSearchBtnNew();
    }

    public void setUserIdSearchCriteria(String userId) {
        menu.setUserIdSearchCriteria(userId);
    }

    public void setUserIdSearchCriteriaNew(String userId) {
        menu.setUserIdSearchCriteriaNew(userId);
    }

    public void changeRegulator(String regulator) {
        menu.changeRegulator(regulator);
    }

}
