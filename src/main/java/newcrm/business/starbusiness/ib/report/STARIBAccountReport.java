package newcrm.business.starbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.pages.staribpages.STARIBAccountReportPage;
import org.openqa.selenium.WebDriver;

public class STARIBAccountReport extends IBAccountReport {

    public STARIBAccountReport(WebDriver driver) {
        super(new STARIBAccountReportPage(driver));
    }

    @Override
    public void verifyAccountReportPage(){
        ibAccountReportPage.verifyAccountReportPage4Tabs();
        ibAccountReportPage.verifyLeadsAndArchivedClientsTab();
    }

}
