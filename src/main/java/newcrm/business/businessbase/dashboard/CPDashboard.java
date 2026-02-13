package newcrm.business.businessbase.dashboard;

import newcrm.pages.clientpages.dashboard.DashboardPage;
import org.openqa.selenium.WebDriver;

public class CPDashboard {

    protected DashboardPage dashboardPage;

    public CPDashboard(WebDriver driver) {
        this.dashboardPage = new DashboardPage(driver);
    }

    public void checkTradeViewContent() {
        dashboardPage.checkTradeViewTabList();
//        dashboardPage.clickTradeViewTab("Forex");
        dashboardPage.waitTradeViewLoader();
        dashboardPage.clickTradeViewTab("Crypto");
        dashboardPage.clickTradeViewTab("Shares");
        dashboardPage.clickTradeViewTab("Indices");
        dashboardPage.clickTradeViewTab("Metals");
        dashboardPage.clickTradeViewTab("Energy");
        dashboardPage.clickTradeViewTab("Etfs");
    }

}