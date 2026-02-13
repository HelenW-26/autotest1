package newcrm.business.businessbase.ibbase.report;

import newcrm.pages.ibpages.IBAccountReportPage;
import newcrm.pages.ibpages.IBClientReportPage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class IBAccountReport {

    protected IBAccountReportPage ibAccountReportPage;
    protected IBClientReportPage ibClientReportPage;

    public IBAccountReport(WebDriver driver) {
        this.ibAccountReportPage = new IBAccountReportPage(driver);
        this.ibClientReportPage = new IBClientReportPage(driver);
    }

    public IBAccountReport(IBAccountReportPage ibAccountReportPage) {
        this.ibAccountReportPage = ibAccountReportPage;
    }

    public IBAccountReport(IBClientReportPage ibClientReportPage) {
        this.ibClientReportPage = ibClientReportPage;
    }

    public void searchAccountReport_CampaignSource(String campaignSource){
        ibAccountReportPage.searchAccountReport_CampaignSource(campaignSource);
    }

    public void verifyCampaignSource(String campaignSource,String tradingAcc){
        ibAccountReportPage.verifyCampaignSource(campaignSource,tradingAcc);
    }

    public void verifyAccountReportPage(){
        ibAccountReportPage.verifyAccountReportPage4Tabs();
        ibAccountReportPage.verifyLeadsAndArchivedClientsTab();
    }

    public void verifyClientReportPage(){
    }

//    public void verifyDemoAccountInAccountReport_Name(String name){
//        ibAccountReportPage.verifyDemoAccountInAccountReport_Name(name);
//    }

    public List<String> retrieveDemoAccountNamesInAccountReport(){
        return ibAccountReportPage.retrieveDemoAccountNamesInAccountReport();
    }

    public List<String> retrieveDemoAccountNamesInClientReport(){
        return ibClientReportPage.retrieveDemoAccountNamesInClientReport();
    }

    public List<String> retrievePendingAccountNamesInAccountReport(String tradingAcc){
        return ibAccountReportPage.retrievePendingAccountNamesInAccountReport(tradingAcc);
    }

}
