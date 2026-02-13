package newcrm.business.pugbusiness.ib.report;

import newcrm.business.businessbase.ibbase.report.IBAccountReport;
import newcrm.pages.pugibpages.PUGIBAccountReportPage;
import newcrm.pages.vtibpages.VTIBAccountReportPage;
import org.openqa.selenium.WebDriver;

public class PUGIBAccountReport extends IBAccountReport {


    public PUGIBAccountReport(WebDriver driver) {
        super(new PUGIBAccountReportPage(driver));
    }


}
