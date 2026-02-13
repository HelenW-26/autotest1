package newcrm.business.pugbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBReport;
import newcrm.pages.pugibpages.PUGIBReportPage;
import org.openqa.selenium.WebDriver;

public class PUGIBReport extends IBReport {


    protected static WebDriver driver;
    protected PUGIBReportPage ibReportPage;

    public PUGIBReport(WebDriver driver) {
        super(driver);
        this.ibReportPage = new PUGIBReportPage(driver);
    }

    @Override
    public void verifyRebateReportPage(){
        ibReportPage.setDateRangeToLast365Days();
        ibReportPage.verifyRebateValuesCurrency();
        ibReportPage.verifyAccountTabValues();
    }
}
