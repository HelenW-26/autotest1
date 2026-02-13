package newcrm.business.vtbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBReport;
import newcrm.pages.vtibpages.VTIBReportPage;
import org.openqa.selenium.WebDriver;

public class VTIBReport extends IBReport {


    protected static WebDriver driver;
    protected VTIBReportPage ibReportPage;

    public VTIBReport(WebDriver driver) {
        super(driver);
        this.ibReportPage = new VTIBReportPage(driver);
    }

    @Override
    public void verifyRebateReportPage(){
        ibReportPage.setDateRangeToLast30Days();
        ibReportPage.verifyRebateValuesCurrency();
        ibReportPage.verifyAccountTabValues();
    }
}
