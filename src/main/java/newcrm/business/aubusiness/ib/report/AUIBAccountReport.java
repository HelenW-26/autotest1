package newcrm.business.aubusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.pages.auibpages.AUIBAccountReportPage;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.IBClientReportPage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class AUIBAccountReport extends IBAccountReport {

    public AUIBAccountReport(WebDriver driver) {
        super(new AUIBAccountReportPage(driver));
        this.ibClientReportPage = new IBClientReportPage(driver);
    }

    @Override
    public void verifyAccountReportPage(){
        ibAccountReportPage.verifyAccountReportPage4Tabs();
    }

    @Override
    public void verifyClientReportPage(){
        ibClientReportPage.verifyLeadsAndArchivedClientsTab();
    }

//    @Override
//    public List<String> retrieveDemoAccountNamesInAccountReport(){
//        return ibClientReportPage.retrieveDemoAccountNamesInClientReport();
//    }

}
