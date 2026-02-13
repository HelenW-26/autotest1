package newcrm.business.vjpbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.pages.vjpibpages.account.VJPIBAccountReportPage;
import org.openqa.selenium.WebDriver;

public class VJPIBAccountReport extends IBAccountReport {


    public VJPIBAccountReport(WebDriver driver) {
        super(new VJPIBAccountReportPage(driver));
    }

}
