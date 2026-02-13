package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPDashboardPage;
import newcrm.pages.dappages.DAPRegisterPersonalDetailsPage;
import newcrm.pages.dappages.DAPRegistrationPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DAPDashboard {

    protected DAPDashboardPage dapDashboardPage;

    public DAPDashboard(WebDriver driver) {
        this.dapDashboardPage = new DAPDashboardPage(driver);
    }

    public DAPDashboard(DAPDashboardPage dapDashboardPage) {
        this.dapDashboardPage = dapDashboardPage;
    }

    public String retrieveProfilePanelUserID()  {
        return dapDashboardPage.retrieveProfilePanelUserID();
    }

    public void dapLogout()  {
        dapDashboardPage.dapLogout();
    }

    public void switchToCP()  {
        dapDashboardPage.switchToCP();
    }

    public String getActiveLanguage()  {
        return dapDashboardPage.getActiveLanguage();
    }

    public String getRegisterLiveAccLink(){
        return dapDashboardPage.getRegisterLiveAccLink();
    }

    public String getReferralCode(){
        return dapDashboardPage.getReferralCode();
    }

    public void navigateToDAPDeepLink(){
        dapDashboardPage.navigateToDeepLinkPage();
    }

    public void navigateToCommissionReportPage(){
        dapDashboardPage.navigateToCommissionReportPage();
    }

    public void navigateToPaymentPage(){
        dapDashboardPage.navigateToPaymentPage();
    }

    public void navigateToClientList(){
        dapDashboardPage.navigateToClientListPage();
    }

    public String navigateToPostbackTrackerPage(){
        return dapDashboardPage.navigateToPostbackTrackerPage();
    }

    public void navigateToCommissionPlanPage(){
        dapDashboardPage.navigateToCommissionPlanPage();
    }

    public void navigateToMultiCommissionPage(){
        dapDashboardPage.navigateToMultiCommissionPage();
    }

    public void navigateToDashboardPage(){
        dapDashboardPage.navigateToDashboardPage();
    }

    public List<String> getAllClientJourneyRegTime(){
        return dapDashboardPage.getAllClientJourneyRegTime();
    }

    public List<String> getAllClientJourneyUID(){
        return dapDashboardPage.getAllClientJourneyUID();
    }

    public List<String> getAllClientJourneyName(){
        return dapDashboardPage.getAllClientJourneyName();
    }

    public List<String> getAllClientJourneyStatus(){
        return dapDashboardPage.getAllClientJourneyStatus();
    }

    public double getTotalCommission(){
        return dapDashboardPage.getTotalCommission();
    }

    public void verifyAvailableBalance(){
        dapDashboardPage.verifyAvailableBalance();
    }

    public void verifyStatisticsTable(){
        dapDashboardPage.verifyStatisticsDefaultDate();
        dapDashboardPage.verifyStatisticsRate();
    }

    public void verifyTopSubCPATable(Map<String, List<String>> commissionMap){
        dapDashboardPage.verifyTopSubCPATable(commissionMap);
    }

    public boolean checkPerformanceReportEntryPointExist(){
        return dapDashboardPage.checkPerformanceReportEntryPointExist();
    }

}
