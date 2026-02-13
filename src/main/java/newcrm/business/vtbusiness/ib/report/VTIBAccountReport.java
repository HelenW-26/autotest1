package newcrm.business.vtbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.pages.vtibpages.VTIBAccountReportPage;
import org.openqa.selenium.WebDriver;

public class VTIBAccountReport extends IBAccountReport {


    public VTIBAccountReport(WebDriver driver) {
        super(new VTIBAccountReportPage(driver));
    }

    @Override
    public void verifyAccountReportPage(){
        ibAccountReportPage.verifyAccountReportPage4Tabs();
        ibAccountReportPage.verifyLeadsAndArchivedClientsTab();
    }

}
