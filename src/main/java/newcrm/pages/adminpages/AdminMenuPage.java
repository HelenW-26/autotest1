package newcrm.pages.adminpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class AdminMenuPage extends AdminPage {

    public AdminMenuPage(WebDriver driver) {
        super(driver);
    }

    // region [ Global Functions ]

    public void changeRegulator(String regulator) {
        if (!assertElementsExists(By.id("regulation_label"), "Regulator menu").isEmpty()) {
            WebElement regul= driver.findElement(By.id("regulation_label"));
            JavascriptExecutor javascript = (JavascriptExecutor) driver;
            javascript.executeScript("arguments[0].click()", regul);
            //Switch regulator
            assertElementExists(By.xpath(String.format("//ul[@class='dropdown-menu favorite-content']/li/div/span[text()='%s']", regulator.toUpperCase())), regulator.toUpperCase() + " Regulator menu").click();
            LogUtils.info("Switch Regulator to " + regulator.toUpperCase());
        } else {
            LogUtils.info("Single Regulator. No need to switch.");
        }
    }

    protected WebElement getMenuBarEle(String path, String desc) {
        WebElement menuBar = assertVisibleElementExists(By.cssSelector("ol#Menu1"), "Admin Menu Bar");
        return assertElementExists(By.linkText(path), desc + " main menu", menuBar);
    }

    protected WebElement getMenuHorizontalEle(String path, String desc) {
        return assertElementExists(By.cssSelector("a[code='" + path + "']"), desc + " menu");
    }

    public void clickAdminWebMenu(List<Map.Entry<String, String>> entries) {
        if (entries == null || entries.isEmpty()) return;

        // Helper lambda to find menu element
        BiFunction<String, String, WebElement> findMenu = (path, desc) -> {
            WebElement menuEle = getMenuBarEle(path, desc);
            return menuEle;
        };

        // Loop through all menu levels (main + submenus)
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, String> entry = entries.get(i);
            WebElement menuEle = (i == 0)
                    ? findMenu.apply(entry.getKey(), entry.getValue())
                    : getMenuHorizontalEle(entry.getKey(), entry.getValue());

            // Skip clicking if expandable and already expanded
            if (isElePopupExpanded(menuEle)) {
                continue;
            }

            triggerClickEvent(menuEle);
        }
    }

    @Override
    public boolean isElePopupExpanded(WebElement element) {
        WebElement parentLi = assertElementExists(By.xpath(".."), "", element);
        String val = parentLi.getAttribute("class");
        return val != null && "open".equalsIgnoreCase(val.trim());
    }

    // endregion

    // region [ Admin Main Menu ]

    protected String getSystemSettingMainMenu() {
        return "System Setting";
    }

    protected String getTaskMainMenu() {
        return "Task";
    }

    protected String getClientMainMenu() {
        return "Client";
    }

    protected String getUsersMainMenu() {
        return "Users";
    }

    protected String getAccountMainMenu() {
        return "Account";
    }

    // endregion

    // region [ Admin Menu ]

    protected String getEmailMgmtMenu() {
        return "menu.mail.management";
    }

    protected String get2FALimitSettingMenu() {
        return "menu.2FA.limit.setting";
    }

    protected String getClientMenu() {
        return "menu.customer.resource";
    }

    protected String getPOIPOAAuditMenu() {
        return "menu.user.identity.audit";
    }

    protected String getPTMgmtMenu() {
        return "menu.pt.management";
    }

    protected String getExternalUserMenu() {
        return "menu.exter.user";
    }

    protected String getAccountAuditMenu() {
        return "menu.account.audit";
    }

    protected String getAdditionalAccountAuditMenu() {
        return "menu.account.samename.audit";
    }

    protected String getRebateAccountMenu() {
        return "menu.account.rebateAccountList";
    }

    protected String getRebateAccountAgreementMenu() {
        return "menu.rebate.agreement.query";
    }

    // endregion

    // region [ Admin Menu Click Event ]

    public void clickEmailMgmt() {
        clickAdminWebMenu(List.of(
                Map.entry(getSystemSettingMainMenu(), getSystemSettingMainMenu()),
                Map.entry(getEmailMgmtMenu(), "Email Management")
        ));
        LogUtils.info("Go to Email Management page");
        waitLoadingPageContent();
    }

    public void click2FALimitSetting() {
        clickAdminWebMenu(List.of(
                Map.entry(getTaskMainMenu(), getTaskMainMenu()),
                Map.entry(get2FALimitSettingMenu(), "2FA Limit Setting")
        ));
        LogUtils.info("Go to 2FA Limit Setting page");
        waitLoadingPageContentNew();
    }

    public void clickClient() {
        clickAdminWebMenu(List.of(
                Map.entry(getClientMainMenu(), getClientMainMenu()),
                Map.entry(getClientMenu(), "Client")
        ));
        LogUtils.info("Go to Client page");
        waitLoadingPageContent();
    }

    public void clickPOIPOAAudit() {
        clickAdminWebMenu(List.of(
                Map.entry(getTaskMainMenu(), getTaskMainMenu()),
                Map.entry(getPOIPOAAuditMenu(), "ID & POA Audit")
        ));
        LogUtils.info("Go to ID & POA Audit page");
        waitLoadingPageContent();
    }

    public void clickPTMgmt() {
        clickAdminWebMenu(List.of(
                Map.entry(getTaskMainMenu(), getTaskMainMenu()),
                Map.entry(getPTMgmtMenu(), "PT management")
        ));
        LogUtils.info("Go to PT management page");
        waitLoadingPageContent();
    }

    public void clickExternalUser() {
        clickAdminWebMenu(List.of(
                Map.entry(getUsersMainMenu(), getUsersMainMenu()),
                Map.entry(getExternalUserMenu(), "External User")
        ));
        LogUtils.info("Go to External User page");
        waitLoadingPageContent();
    }

    public void clickAccountAudit() {
        clickAdminWebMenu(List.of(
                Map.entry(getUsersMainMenu(), getUsersMainMenu()),
                Map.entry(getAccountAuditMenu(), "Account Audit")
        ));
        LogUtils.info("Go to Account Audit page");
        waitLoadingPageContent();
    }

    public void clickAdditionalAccountAudit() {
        clickAdminWebMenu(List.of(
                Map.entry(getTaskMainMenu(), getTaskMainMenu()),
                Map.entry(getAdditionalAccountAuditMenu(), "Additional Account Audit")
        ));
        LogUtils.info("Go to Additional Account Audit page");
        waitLoadingPageContent();
    }

    public void clickRebateAccount() {
        clickAdminWebMenu(List.of(
                Map.entry(getAccountMainMenu(), getAccountMainMenu()),
                Map.entry(getRebateAccountMenu(), "Rebate Account")
        ));
        LogUtils.info("Go to Rebate Account page");
        waitLoadingPageContent();
    }

    public void clickRebateAccountAgreement() {
        clickAdminWebMenu(List.of(
                Map.entry(getTaskMainMenu(), getTaskMainMenu()),
                Map.entry(getRebateAccountAgreementMenu(), "Rebate Account Agreement")
        ));
        LogUtils.info("Go to Rebate Account Agreement page");
        waitLoadingPageContentNew();
    }

    // endregion

}
