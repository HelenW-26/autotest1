package newcrm.business.businessbase.owsbase;

import newcrm.pages.owspages.OWSCPAAllocationPage;
import newcrm.pages.owspages.OWSCPAPage;
import newcrm.pages.owspages.OWSDashboardPage;
import newcrm.pages.owspages.OWSKYCRecordsPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

import java.util.List;


public class OWSDashboard {

    protected static WebDriver driver;
    protected OWSDashboardPage owsDashboardPage;
    protected OWSKYCRecordsPage owsKYCRecordsPage;
    protected OWSCPAPage owsCPAPage;
    protected OWSCPAAllocationPage owsCPAAllocationPage;


    public OWSDashboard(WebDriver driver) {
        this.owsDashboardPage = new OWSDashboardPage(driver);
        this.owsKYCRecordsPage = new OWSKYCRecordsPage(driver);
        this.owsCPAPage = new OWSCPAPage(driver);
        this.owsCPAAllocationPage = new OWSCPAAllocationPage(driver);
    }

    public OWSDashboard(OWSDashboardPage owsDashboardPage) {
        this.owsDashboardPage = owsDashboardPage;
    }

    public OWSDashboard(OWSKYCRecordsPage owsKYCRecordsPage) {
        this.owsKYCRecordsPage = owsKYCRecordsPage;
    }

    public OWSDashboard(OWSCPAPage owsCPAPage) {
        this.owsCPAPage = owsCPAPage;
    }

    public OWSDashboard(OWSCPAAllocationPage owsCPAAllocationPage) {
        this.owsCPAAllocationPage = owsCPAAllocationPage;
    }

    public void navigateToKYCRecords()  {
        owsDashboardPage.navigateToKYCRecords();
    }

    public void navigateToAdditionalRecords()  {
        owsDashboardPage.navigateToAdditionalRecords();
    }

    public void navigateToCommissionList()  {
        owsDashboardPage.navigateToCommissionList();
    }

    public void navigateToPayment()  {
        owsDashboardPage.navigateToPayment();
    }

    //Pass one of the three parameters will be sufficient
    public boolean auditTradingAccountFlow(String userId,String clientName, String email)  {
        owsDashboardPage.navigateToKYCRecords();
        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
        return owsKYCRecordsPage.auditTradingAccount();
    }

    //Pass one of the three parameters will be sufficient
    public boolean auditPOI(String userId,String clientName, String email)  {
        return owsKYCRecordsPage.auditPOI();
    }

    public boolean auditPOIFlow(String userId,String clientName, String email)  {
        owsDashboardPage.navigateToKYCRecords();
        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
        return owsKYCRecordsPage.auditPOI();
    }

    //Pass one of the three parameters will be sufficient
    public void auditPOA(String userId,String clientName, String email)  {
        owsKYCRecordsPage.auditPOA();
    }

    //Pass one of the three parameters will be sufficient
    public void auditPOIPOAFlow(String userId,String clientName, String email)  {
        owsDashboardPage.navigateToKYCRecords();
        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
        owsKYCRecordsPage.auditPOI();
        owsKYCRecordsPage.auditPOA();
    }

    public String getRegulator(String userId,String clientName, String email)  {
        owsDashboardPage.navigateToKYCRecords();
        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
        return owsKYCRecordsPage.getRegulator();
    }

    public String getSalesAssignedFlow(String cpaAccount,String cpaName, String userID, String email){
        owsDashboardPage.navigateToCPA();
        owsCPAPage.searchCPA(cpaAccount, cpaName, userID, email);
        return owsCPAPage.getSalesAssigned();
    }

    public String getCPAAllocationSalesFlow(String country){
        owsDashboardPage.navigateToCPAAllocation();
        return owsCPAAllocationPage.getCPAAllocationSales(country);
    }

    public Boolean togglePerformanceReportEntryPoint(String cpaAccount,String cpaName, String userID, String email){
        owsDashboardPage.navigateToCPA();
        owsCPAPage.searchCPA(cpaAccount, cpaName, userID, email);
        return owsCPAPage.togglePerformanceReportEntryPoint();
    }

    public void verifyCPASearchTable(String cpaAccount,String cpaName, String userID, String email){
        owsDashboardPage.navigateToCPA();
        owsCPAPage.verifyCPASearchTableAndOverviewPerformance(cpaAccount, cpaName, userID, email);
    }





}
