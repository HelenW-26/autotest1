package newcrm.business.starbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBReport;
import newcrm.pages.staribpages.STARIBReportPage;
import org.openqa.selenium.WebDriver;

public class STARIBReport extends IBReport {


    protected static WebDriver driver;
    protected STARIBReportPage ibReportPage;

    public STARIBReport(WebDriver driver) {
        super(driver);
        this.ibReportPage = new STARIBReportPage(driver);
    }

    @Override
    public void verifyRebateReportPage(){
        ibReportPage.setDateRangeToLast365Days();
        ibReportPage.verifyRebateValuesCurrency();
        ibReportPage.verifyAccountTabValues();
    }
}
