package newcrm.business.businessbase.ibbase.report;

import newcrm.pages.ibpages.IBReportPage;
import org.openqa.selenium.WebDriver;

public class IBReport {

    protected IBReportPage ibReportPage;

    public IBReport(WebDriver driver) {
        this.ibReportPage = new IBReportPage(driver);
    }

    public IBReport(IBReportPage ibReportPage) {
        this.ibReportPage = ibReportPage;
    }

    public void verifyRebateReportPage(){
        ibReportPage.setDateRangeToLast365Days();
        ibReportPage.verifyRebateValuesCurrency();
        ibReportPage.verifyDateTabValues();
        ibReportPage.verifyAccountTabValues();
    }


}
